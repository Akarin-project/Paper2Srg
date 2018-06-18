package net.minecraft.world.end;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.ContiguousSet;
import com.google.common.collect.DiscreteDomain;
import com.google.common.collect.Lists;
import com.google.common.collect.Range;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import javax.annotation.Nullable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.state.BlockWorldState;
import net.minecraft.block.state.pattern.BlockMatcher;
import net.minecraft.block.state.pattern.BlockPattern;
import net.minecraft.block.state.pattern.FactoryBlockPattern;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.dragon.phase.PhaseList;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityEndPortal;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.BossInfo;
import net.minecraft.world.BossInfoServer;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.BiomeEndDecorator;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.feature.WorldGenEndGateway;
import net.minecraft.world.gen.feature.WorldGenEndPodium;
import net.minecraft.world.gen.feature.WorldGenSpikes;

public class DragonFightManager {

    private static final Logger LOGGER = LogManager.getLogger();
    private static final Predicate<EntityPlayerMP> VALID_PLAYER = Predicates.and(EntitySelectors.IS_ALIVE, EntitySelectors.withinRange(0.0D, 128.0D, 0.0D, 192.0D));
    private final BossInfoServer bossInfo;
    private final WorldServer world;
    private final List<Integer> gateways;
    private final BlockPattern portalPattern;
    private int ticksSinceDragonSeen;
    private int aliveCrystals;
    private int ticksSinceCrystalsScanned;
    private int ticksSinceLastPlayerScan;
    private boolean dragonKilled;
    private boolean previouslyKilled;
    private UUID dragonUniqueId;
    private boolean scanForLegacyFight;
    private BlockPos exitPortalLocation;
    private DragonSpawnManager respawnState;
    private int respawnStateTicks;
    private List<EntityEnderCrystal> crystals;

    public DragonFightManager(WorldServer worldserver, NBTTagCompound nbttagcompound) {
        this.bossInfo = (BossInfoServer) (new BossInfoServer(new TextComponentTranslation("entity.EnderDragon.name", new Object[0]), BossInfo.Color.PINK, BossInfo.Overlay.PROGRESS)).setPlayEndBossMusic(true).setCreateFog(true);
        this.gateways = Lists.newArrayList();
        this.scanForLegacyFight = true;
        this.world = worldserver;
        if (nbttagcompound.hasKey("DragonKilled", 99)) {
            if (nbttagcompound.hasUniqueId("DragonUUID")) {
                this.dragonUniqueId = nbttagcompound.getUniqueId("DragonUUID");
            }

            this.dragonKilled = nbttagcompound.getBoolean("DragonKilled");
            this.previouslyKilled = nbttagcompound.getBoolean("PreviouslyKilled");
            if (nbttagcompound.getBoolean("IsRespawning")) {
                this.respawnState = DragonSpawnManager.START;
            }

            if (nbttagcompound.hasKey("ExitPortalLocation", 10)) {
                this.exitPortalLocation = NBTUtil.getPosFromTag(nbttagcompound.getCompoundTag("ExitPortalLocation"));
            }
        } else {
            this.dragonKilled = true;
            this.previouslyKilled = true;
        }

        if (nbttagcompound.hasKey("Gateways", 9)) {
            NBTTagList nbttaglist = nbttagcompound.getTagList("Gateways", 3);

            for (int i = 0; i < nbttaglist.tagCount(); ++i) {
                this.gateways.add(Integer.valueOf(nbttaglist.getIntAt(i)));
            }
        } else {
            this.gateways.addAll(ContiguousSet.create(Range.closedOpen(Integer.valueOf(0), Integer.valueOf(20)), DiscreteDomain.integers()));
            Collections.shuffle(this.gateways, new Random(worldserver.getSeed()));
        }

        this.portalPattern = FactoryBlockPattern.start().aisle(new String[] { "       ", "       ", "       ", "   #   ", "       ", "       ", "       "}).aisle(new String[] { "       ", "       ", "       ", "   #   ", "       ", "       ", "       "}).aisle(new String[] { "       ", "       ", "       ", "   #   ", "       ", "       ", "       "}).aisle(new String[] { "  ###  ", " #   # ", "#     #", "#  #  #", "#     #", " #   # ", "  ###  "}).aisle(new String[] { "       ", "  ###  ", " ##### ", " ##### ", " ##### ", "  ###  ", "       "}).where('#', BlockWorldState.hasState(BlockMatcher.forBlock(Blocks.BEDROCK))).build();
    }

    public NBTTagCompound getCompound() {
        NBTTagCompound nbttagcompound = new NBTTagCompound();

        if (this.dragonUniqueId != null) {
            nbttagcompound.setUniqueId("DragonUUID", this.dragonUniqueId);
        }

        nbttagcompound.setBoolean("DragonKilled", this.dragonKilled);
        nbttagcompound.setBoolean("PreviouslyKilled", this.previouslyKilled);
        if (this.exitPortalLocation != null) {
            nbttagcompound.setTag("ExitPortalLocation", NBTUtil.createPosTag(this.exitPortalLocation));
        }

        NBTTagList nbttaglist = new NBTTagList();
        Iterator iterator = this.gateways.iterator();

        while (iterator.hasNext()) {
            int i = ((Integer) iterator.next()).intValue();

            nbttaglist.appendTag(new NBTTagInt(i));
        }

        nbttagcompound.setTag("Gateways", nbttaglist);
        return nbttagcompound;
    }

    public void tick() {
        this.bossInfo.setVisible(!this.dragonKilled);
        if (++this.ticksSinceLastPlayerScan >= 20) {
            this.updateplayers();
            this.ticksSinceLastPlayerScan = 0;
        }

        if (!this.bossInfo.getPlayers().isEmpty()) {
            if (this.scanForLegacyFight) {
                DragonFightManager.LOGGER.info("Scanning for legacy world dragon fight...");
                this.loadChunks();
                this.scanForLegacyFight = false;
                boolean flag = this.hasDragonBeenKilled();

                if (flag) {
                    DragonFightManager.LOGGER.info("Found that the dragon has been killed in this world already.");
                    this.previouslyKilled = true;
                } else {
                    DragonFightManager.LOGGER.info("Found that the dragon has not yet been killed in this world.");
                    this.previouslyKilled = false;
                    this.generatePortal(false);
                }

                List list = this.world.getEntities(EntityDragon.class, EntitySelectors.IS_ALIVE);

                if (list.isEmpty()) {
                    this.dragonKilled = true;
                } else {
                    EntityDragon entityenderdragon = (EntityDragon) list.get(0);

                    this.dragonUniqueId = entityenderdragon.getUniqueID();
                    DragonFightManager.LOGGER.info("Found that there\'s a dragon still alive ({})", entityenderdragon);
                    this.dragonKilled = false;
                    if (!flag) {
                        DragonFightManager.LOGGER.info("But we didn\'t have a portal, let\'s remove it.");
                        entityenderdragon.setDead();
                        this.dragonUniqueId = null;
                    }
                }

                if (!this.previouslyKilled && this.dragonKilled) {
                    this.dragonKilled = false;
                }
            }

            if (this.respawnState != null) {
                if (this.crystals == null) {
                    this.respawnState = null;
                    this.respawnDragon();
                }

                this.respawnState.process(this.world, this, this.crystals, this.respawnStateTicks++, this.exitPortalLocation);
            }

            if (!this.dragonKilled) {
                if (this.dragonUniqueId == null || ++this.ticksSinceDragonSeen >= 1200) {
                    this.loadChunks();
                    List list1 = this.world.getEntities(EntityDragon.class, EntitySelectors.IS_ALIVE);

                    if (list1.isEmpty()) {
                        DragonFightManager.LOGGER.debug("Haven\'t seen the dragon, respawning it");
                        this.createNewDragon();
                    } else {
                        DragonFightManager.LOGGER.debug("Haven\'t seen our dragon, but found another one to use.");
                        this.dragonUniqueId = ((EntityDragon) list1.get(0)).getUniqueID();
                    }

                    this.ticksSinceDragonSeen = 0;
                }

                if (++this.ticksSinceCrystalsScanned >= 100) {
                    this.findAliveCrystals();
                    this.ticksSinceCrystalsScanned = 0;
                }
            }
        }

    }

    protected void setRespawnState(DragonSpawnManager enumdragonrespawn) {
        if (this.respawnState == null) {
            throw new IllegalStateException("Dragon respawn isn\'t in progress, can\'t skip ahead in the animation.");
        } else {
            this.respawnStateTicks = 0;
            if (enumdragonrespawn == DragonSpawnManager.END) {
                this.respawnState = null;
                this.dragonKilled = false;
                EntityDragon entityenderdragon = this.createNewDragon();
                Iterator iterator = this.bossInfo.getPlayers().iterator();

                while (iterator.hasNext()) {
                    EntityPlayerMP entityplayer = (EntityPlayerMP) iterator.next();

                    CriteriaTriggers.SUMMONED_ENTITY.trigger(entityplayer, (Entity) entityenderdragon);
                }
            } else {
                this.respawnState = enumdragonrespawn;
            }

        }
    }

    private boolean hasDragonBeenKilled() {
        for (int i = -8; i <= 8; ++i) {
            int j = -8;

            label27:
            while (j <= 8) {
                Chunk chunk = this.world.getChunkFromChunkCoords(i, j);
                Iterator iterator = chunk.getTileEntityMap().values().iterator();

                TileEntity tileentity;

                do {
                    if (!iterator.hasNext()) {
                        ++j;
                        continue label27;
                    }

                    tileentity = (TileEntity) iterator.next();
                } while (!(tileentity instanceof TileEntityEndPortal));

                return true;
            }
        }

        return false;
    }

    @Nullable
    private BlockPattern.PatternHelper findExitPortal() {
        int i;
        int j;

        for (i = -8; i <= 8; ++i) {
            for (j = -8; j <= 8; ++j) {
                Chunk chunk = this.world.getChunkFromChunkCoords(i, j);
                Iterator iterator = chunk.getTileEntityMap().values().iterator();

                while (iterator.hasNext()) {
                    TileEntity tileentity = (TileEntity) iterator.next();

                    if (tileentity instanceof TileEntityEndPortal) {
                        BlockPattern.PatternHelper shapedetector_shapedetectorcollection = this.portalPattern.match(this.world, tileentity.getPos());

                        if (shapedetector_shapedetectorcollection != null) {
                            BlockPos blockposition = shapedetector_shapedetectorcollection.translateOffset(3, 3, 3).getPos();

                            if (this.exitPortalLocation == null && blockposition.getX() == 0 && blockposition.getZ() == 0) {
                                this.exitPortalLocation = blockposition;
                            }

                            return shapedetector_shapedetectorcollection;
                        }
                    }
                }
            }
        }

        i = this.world.getHeight(WorldGenEndPodium.END_PODIUM_LOCATION).getY();

        for (j = i; j >= 0; --j) {
            BlockPattern.PatternHelper shapedetector_shapedetectorcollection1 = this.portalPattern.match(this.world, new BlockPos(WorldGenEndPodium.END_PODIUM_LOCATION.getX(), j, WorldGenEndPodium.END_PODIUM_LOCATION.getZ()));

            if (shapedetector_shapedetectorcollection1 != null) {
                if (this.exitPortalLocation == null) {
                    this.exitPortalLocation = shapedetector_shapedetectorcollection1.translateOffset(3, 3, 3).getPos();
                }

                return shapedetector_shapedetectorcollection1;
            }
        }

        return null;
    }

    private void loadChunks() {
        for (int i = -8; i <= 8; ++i) {
            for (int j = -8; j <= 8; ++j) {
                this.world.getChunkFromChunkCoords(i, j);
            }
        }

    }

    private void updateplayers() {
        HashSet hashset = Sets.newHashSet();
        Iterator iterator = this.world.getPlayers(EntityPlayerMP.class, DragonFightManager.VALID_PLAYER).iterator();

        while (iterator.hasNext()) {
            EntityPlayerMP entityplayer = (EntityPlayerMP) iterator.next();

            this.bossInfo.addPlayer(entityplayer);
            hashset.add(entityplayer);
        }

        HashSet hashset1 = Sets.newHashSet(this.bossInfo.getPlayers());

        hashset1.removeAll(hashset);
        Iterator iterator1 = hashset1.iterator();

        while (iterator1.hasNext()) {
            EntityPlayerMP entityplayer1 = (EntityPlayerMP) iterator1.next();

            this.bossInfo.removePlayer(entityplayer1);
        }

    }

    private void findAliveCrystals() {
        this.ticksSinceCrystalsScanned = 0;
        this.aliveCrystals = 0;
        WorldGenSpikes.EndSpike[] aworldgenender_spike = BiomeEndDecorator.getSpikesForWorld(this.world);
        int i = aworldgenender_spike.length;

        for (int j = 0; j < i; ++j) {
            WorldGenSpikes.EndSpike worldgenender_spike = aworldgenender_spike[j];

            this.aliveCrystals += this.world.getEntitiesWithinAABB(EntityEnderCrystal.class, worldgenender_spike.getTopBoundingBox()).size();
        }

        DragonFightManager.LOGGER.debug("Found {} end crystals still alive", Integer.valueOf(this.aliveCrystals));
    }

    public void processDragonDeath(EntityDragon entityenderdragon) {
        if (entityenderdragon.getUniqueID().equals(this.dragonUniqueId)) {
            this.bossInfo.setPercent(0.0F);
            this.bossInfo.setVisible(false);
            this.generatePortal(true);
            this.spawnNewGateway();
            if (!this.previouslyKilled) {
                this.world.setBlockState(this.world.getHeight(WorldGenEndPodium.END_PODIUM_LOCATION), Blocks.DRAGON_EGG.getDefaultState());
            }

            this.previouslyKilled = true;
            this.dragonKilled = true;
        }

    }

    private void spawnNewGateway() {
        if (!this.gateways.isEmpty()) {
            int i = ((Integer) this.gateways.remove(this.gateways.size() - 1)).intValue();
            int j = (int) (96.0D * Math.cos(2.0D * (-3.141592653589793D + 0.15707963267948966D * (double) i)));
            int k = (int) (96.0D * Math.sin(2.0D * (-3.141592653589793D + 0.15707963267948966D * (double) i)));

            this.generateGateway(new BlockPos(j, 75, k));
        }
    }

    private void generateGateway(BlockPos blockposition) {
        this.world.playEvent(3000, blockposition, 0);
        (new WorldGenEndGateway()).generate(this.world, new Random(), blockposition);
    }

    private void generatePortal(boolean flag) {
        WorldGenEndPodium worldgenendtrophy = new WorldGenEndPodium(flag);

        if (this.exitPortalLocation == null) {
            for (this.exitPortalLocation = this.world.getTopSolidOrLiquidBlock(WorldGenEndPodium.END_PODIUM_LOCATION).down(); this.world.getBlockState(this.exitPortalLocation).getBlock() == Blocks.BEDROCK && this.exitPortalLocation.getY() > this.world.getSeaLevel(); this.exitPortalLocation = this.exitPortalLocation.down()) {
                ;
            }
        }

        worldgenendtrophy.generate(this.world, new Random(), this.exitPortalLocation);
    }

    private EntityDragon createNewDragon() {
        this.world.getChunkFromBlockCoords(new BlockPos(0, 128, 0));
        EntityDragon entityenderdragon = new EntityDragon(this.world);

        entityenderdragon.getPhaseManager().setPhase(PhaseList.HOLDING_PATTERN);
        entityenderdragon.setLocationAndAngles(0.0D, 128.0D, 0.0D, this.world.rand.nextFloat() * 360.0F, 0.0F);
        this.world.spawnEntity(entityenderdragon);
        this.dragonUniqueId = entityenderdragon.getUniqueID();
        return entityenderdragon;
    }

    public void dragonUpdate(EntityDragon entityenderdragon) {
        if (entityenderdragon.getUniqueID().equals(this.dragonUniqueId)) {
            this.bossInfo.setPercent(entityenderdragon.getHealth() / entityenderdragon.getMaxHealth());
            this.ticksSinceDragonSeen = 0;
            if (entityenderdragon.hasCustomName()) {
                this.bossInfo.setName(entityenderdragon.getDisplayName());
            }
        }

    }

    public int getNumAliveCrystals() {
        return this.aliveCrystals;
    }

    public void onCrystalDestroyed(EntityEnderCrystal entityendercrystal, DamageSource damagesource) {
        if (this.respawnState != null && this.crystals.contains(entityendercrystal)) {
            DragonFightManager.LOGGER.debug("Aborting respawn sequence");
            this.respawnState = null;
            this.respawnStateTicks = 0;
            this.resetSpikeCrystals();
            this.generatePortal(true);
        } else {
            this.findAliveCrystals();
            Entity entity = this.world.getEntityFromUuid(this.dragonUniqueId);

            if (entity instanceof EntityDragon) {
                ((EntityDragon) entity).onCrystalDestroyed(entityendercrystal, new BlockPos(entityendercrystal), damagesource);
            }
        }

    }

    public boolean hasPreviouslyKilledDragon() {
        return this.previouslyKilled;
    }

    public void respawnDragon() {
        if (this.dragonKilled && this.respawnState == null) {
            BlockPos blockposition = this.exitPortalLocation;

            if (blockposition == null) {
                DragonFightManager.LOGGER.debug("Tried to respawn, but need to find the portal first.");
                BlockPattern.PatternHelper shapedetector_shapedetectorcollection = this.findExitPortal();

                if (shapedetector_shapedetectorcollection == null) {
                    DragonFightManager.LOGGER.debug("Couldn\'t find a portal, so we made one.");
                    this.generatePortal(true);
                } else {
                    DragonFightManager.LOGGER.debug("Found the exit portal & temporarily using it.");
                }

                blockposition = this.exitPortalLocation;
            }

            ArrayList arraylist = Lists.newArrayList();
            BlockPos blockposition1 = blockposition.up(1);
            Iterator iterator = EnumFacing.Plane.HORIZONTAL.iterator();

            while (iterator.hasNext()) {
                EnumFacing enumdirection = (EnumFacing) iterator.next();
                List list = this.world.getEntitiesWithinAABB(EntityEnderCrystal.class, new AxisAlignedBB(blockposition1.offset(enumdirection, 2)));

                if (list.isEmpty()) {
                    return;
                }

                arraylist.addAll(list);
            }

            DragonFightManager.LOGGER.debug("Found all crystals, respawning dragon.");
            this.respawnDragon((List) arraylist);
        }

    }

    private void respawnDragon(List<EntityEnderCrystal> list) {
        if (this.dragonKilled && this.respawnState == null) {
            for (BlockPattern.PatternHelper shapedetector_shapedetectorcollection = this.findExitPortal(); shapedetector_shapedetectorcollection != null; shapedetector_shapedetectorcollection = this.findExitPortal()) {
                for (int i = 0; i < this.portalPattern.getPalmLength(); ++i) {
                    for (int j = 0; j < this.portalPattern.getThumbLength(); ++j) {
                        for (int k = 0; k < this.portalPattern.getFingerLength(); ++k) {
                            BlockWorldState shapedetectorblock = shapedetector_shapedetectorcollection.translateOffset(i, j, k);

                            if (shapedetectorblock.getBlockState().getBlock() == Blocks.BEDROCK || shapedetectorblock.getBlockState().getBlock() == Blocks.END_PORTAL) {
                                this.world.setBlockState(shapedetectorblock.getPos(), Blocks.END_STONE.getDefaultState());
                            }
                        }
                    }
                }
            }

            this.respawnState = DragonSpawnManager.START;
            this.respawnStateTicks = 0;
            this.generatePortal(false);
            this.crystals = list;
        }

    }

    public void resetSpikeCrystals() {
        WorldGenSpikes.EndSpike[] aworldgenender_spike = BiomeEndDecorator.getSpikesForWorld(this.world);
        int i = aworldgenender_spike.length;

        for (int j = 0; j < i; ++j) {
            WorldGenSpikes.EndSpike worldgenender_spike = aworldgenender_spike[j];
            List list = this.world.getEntitiesWithinAABB(EntityEnderCrystal.class, worldgenender_spike.getTopBoundingBox());
            Iterator iterator = list.iterator();

            while (iterator.hasNext()) {
                EntityEnderCrystal entityendercrystal = (EntityEnderCrystal) iterator.next();

                entityendercrystal.setEntityInvulnerable(false);
                entityendercrystal.setBeamTarget((BlockPos) null);
            }
        }

    }
}
