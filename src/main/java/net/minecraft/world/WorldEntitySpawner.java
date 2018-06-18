package net.minecraft.world;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

import com.destroystokyo.paper.exception.ServerInternalException;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRailBase;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.server.MCUtil;
import net.minecraft.server.management.PlayerChunkMapEntry;
import net.minecraft.util.WeightedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.ChunkProviderServer;
import org.bukkit.craftbukkit.util.LongHash;
import org.bukkit.craftbukkit.util.LongHashSet;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

// CraftBukkit start
import com.destroystokyo.paper.exception.ServerInternalException;
import org.bukkit.craftbukkit.util.LongHash;
import org.bukkit.craftbukkit.util.LongHashSet;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
// CraftBukkit end

public final class WorldEntitySpawner {

    private static final int MOB_COUNT_DIV = (int) Math.pow(17.0D, 2.0D);
    private final LongHashSet eligibleChunksForSpawning = new LongHashSet(); // CraftBukkit

    public WorldEntitySpawner() {}

    // Spigot start - get entity count only from chunks being processed in b
    private int getEntityCount(WorldServer server, Class oClass)
    {
        // Paper start - use entire world, not just active chunks. Spigot broke vanilla expectations.
        if (true) {
            return server
                    .getChunkProvider()
                    .id2ChunkMap.values()
                    .stream()
                    .collect(java.util.stream.Collectors.summingInt(c -> c.entityCount.get(oClass)));
        }
        // Paper end
        int i = 0;
        Iterator<Long> it = this.eligibleChunksForSpawning.iterator();
        while ( it.hasNext() )
        {
            Long coord = it.next();
            int x = LongHash.msw( coord );
            int z = LongHash.lsw( coord );
            if ( !((ChunkProviderServer)server.chunkProvider).droppedChunksSet.contains( coord ) && server.isChunkLoaded( x, z, true ) )
            {
                i += server.getChunkFromChunkCoords( x, z ).entityCount.get( oClass );
            }
        }
        return i;
    }
    // Spigot end

    public int findChunksForSpawning(WorldServer worldserver, boolean flag, boolean flag1, boolean flag2) {
        org.spigotmc.AsyncCatcher.catchOp("check for eligible spawn chunks"); // Paper - At least until we figure out what is calling this async
        if (!flag && !flag1) {
            return 0;
        } else {
            this.eligibleChunksForSpawning.clear();
            int i = 0;
            Iterator iterator = worldserver.playerEntities.iterator();

            int j;
            int k;

            while (iterator.hasNext()) {
                EntityPlayer entityhuman = (EntityPlayer) iterator.next();

                if (!entityhuman.isSpectator() && entityhuman.affectsSpawning) {
                    int l = MathHelper.floor(entityhuman.posX / 16.0D);

                    j = MathHelper.floor(entityhuman.posZ / 16.0D);
                    boolean flag3 = true;
                    // Spigot Start
                    byte b0 = worldserver.spigotConfig.mobSpawnRange;
                    b0 = ( b0 > worldserver.spigotConfig.viewDistance ) ? (byte) worldserver.spigotConfig.viewDistance : b0;
                    b0 = ( b0 > 8 ) ? 8 : b0;
                    // Paper start
                    com.destroystokyo.paper.event.entity.PlayerNaturallySpawnCreaturesEvent event;
                    event = new com.destroystokyo.paper.event.entity.PlayerNaturallySpawnCreaturesEvent(
                            (org.bukkit.entity.Player) entityhuman.getBukkitEntity(), b0);
                    if (!event.callEvent()) {
                        continue;
                    }
                    b0 = event.getSpawnRadius();
                    // Paperr end

                    for (int i1 = -b0; i1 <= b0; ++i1) {
                        for (k = -b0; k <= b0; ++k) {
                            boolean flag4 = i1 == -b0 || i1 == b0 || k == -b0 || k == b0;
                            // Spigot End
                            ChunkPos chunkcoordintpair = new ChunkPos(i1 + l, k + j);

                            // CraftBukkit start - use LongHash and LongHashSet
                            long chunkCoords = LongHash.toLong(chunkcoordintpair.x, chunkcoordintpair.z);
                            if (!this.eligibleChunksForSpawning.contains(chunkCoords)) {
                                ++i;
                                if (!flag4 && worldserver.getWorldBorder().contains(chunkcoordintpair)) {
                                    PlayerChunkMapEntry playerchunk = worldserver.getPlayerChunkMap().getEntry(chunkcoordintpair.x, chunkcoordintpair.z);

                                    if (playerchunk != null && playerchunk.isSentToPlayers()) {
                                        this.eligibleChunksForSpawning.add(chunkCoords);
                                        // CraftBukkit end
                                    }
                                }
                            }
                        }
                    }
                }
            }

            int j1 = 0;
            BlockPos blockposition = worldserver.getSpawnPoint();
            EnumCreatureType[] aenumcreaturetype = EnumCreatureType.values();

            j = aenumcreaturetype.length;

            for (int k1 = 0; k1 < j; ++k1) {
                EnumCreatureType enumcreaturetype = aenumcreaturetype[k1];

               // CraftBukkit start - Use per-world spawn limits
                int limit = enumcreaturetype.getMaxNumberOfCreature();
                switch (enumcreaturetype) {
                    case MONSTER:
                        limit = worldserver.getWorld().getMonsterSpawnLimit();
                        break;
                    case CREATURE:
                        limit = worldserver.getWorld().getAnimalSpawnLimit();
                        break;
                    case WATER_CREATURE:
                        limit = worldserver.getWorld().getWaterAnimalSpawnLimit();
                        break;
                    case AMBIENT:
                        limit = worldserver.getWorld().getAmbientSpawnLimit();
                        break;
                }

                if (limit == 0) {
                    continue;
                }
				int mobcnt = 0; // Spigot
                // CraftBukkit end

                if ((!enumcreaturetype.getPeacefulCreature() || flag1) && (enumcreaturetype.getPeacefulCreature() || flag) && (!enumcreaturetype.getAnimal() || flag2)) {
                    /* Paper start - As far as I can tell neither of these are even used
                    k = worldserver.a(enumcreaturetype.a());
                    int l1 = limit * i / a; // CraftBukkit - use per-world limits
                    */ // Paper end

                    if ((mobcnt = getEntityCount(worldserver, enumcreaturetype.getCreatureClass())) <= limit * i / 289) { // Paper - use 17x17 like vanilla (a at top of file)
                        BlockPos.MutableBlockPos blockposition_mutableblockposition = new BlockPos.MutableBlockPos();
                        Iterator iterator1 = this.eligibleChunksForSpawning.iterator();

                        int moblimit = (limit * i / 256) - mobcnt + 1; // Spigot - up to 1 more than limit
                        label120:
                        while (iterator1.hasNext() && (moblimit > 0)) { // Spigot - while more allowed
                            // CraftBukkit start = use LongHash and LongObjectHashMap
                            long key = ((Long) iterator1.next()).longValue();
                            BlockPos blockposition1 = getRandomChunkPosition(worldserver, LongHash.msw(key), LongHash.lsw(key));
                            // CraftBukkit
                            int i2 = blockposition1.getX();
                            int j2 = blockposition1.getY();
                            int k2 = blockposition1.getZ();
                            IBlockState iblockdata = worldserver.getBlockState(blockposition1);

                            if (!iblockdata.isNormalCube()) {
                                int l2 = 0;
                                int i3 = 0;

                                while (i3 < 3) {
                                    int j3 = i2;
                                    int k3 = j2;
                                    int l3 = k2;
                                    boolean flag5 = true;
                                    Biome.SpawnListEntry biomebase_biomemeta = null;
                                    IEntityLivingData groupdataentity = null;
                                    int i4 = MathHelper.ceil(Math.random() * 4.0D);
                                    int j4 = 0;

                                    while (true) {
                                        if (j4 < i4) {
                                            label113: {
                                                j3 += worldserver.rand.nextInt(6) - worldserver.rand.nextInt(6);
                                                k3 += worldserver.rand.nextInt(1) - worldserver.rand.nextInt(1);
                                                l3 += worldserver.rand.nextInt(6) - worldserver.rand.nextInt(6);
                                                blockposition_mutableblockposition.setPos(j3, k3, l3);
                                                float f = (float) j3 + 0.5F;
                                                float f1 = (float) l3 + 0.5F;

                                                if (!worldserver.isAnyPlayerWithinRangeAt((double) f, (double) k3, (double) f1, 24.0D) && blockposition.distanceSq((double) f, (double) k3, (double) f1) >= 576.0D) {
                                                    if (biomebase_biomemeta == null) {
                                                        biomebase_biomemeta = worldserver.getSpawnListEntryForTypeAt(enumcreaturetype, (BlockPos) blockposition_mutableblockposition);
                                                        if (biomebase_biomemeta == null) {
                                                            break label113;
                                                        }
                                                    }

                                                    if (worldserver.canCreatureTypeSpawnHere(enumcreaturetype, biomebase_biomemeta, (BlockPos) blockposition_mutableblockposition) && canCreatureTypeSpawnAtLocation(EntitySpawnPlacementRegistry.getPlacementForEntity(biomebase_biomemeta.entityClass), worldserver, blockposition_mutableblockposition)) {
                                                        // Paper start
                                                        com.destroystokyo.paper.event.entity.PreCreatureSpawnEvent event;
                                                        Class<? extends EntityLiving> cls = biomebase_biomemeta.entityClass;
                                                        org.bukkit.entity.EntityType type = EntityList.clsToTypeMap.get(cls);
                                                        if (type != null) {
                                                            event = new com.destroystokyo.paper.event.entity.PreCreatureSpawnEvent(
                                                                    MCUtil.toLocation(worldserver, blockposition_mutableblockposition),
                                                                    type, SpawnReason.NATURAL
                                                            );
                                                            if (!event.callEvent()) {
                                                                if (event.shouldAbortSpawn()) {
                                                                    continue label120;
                                                                }
                                                                j1 += l2;
                                                                ++j4;
                                                                continue;
                                                            }
                                                        }
                                                        // Paper end
                                                        EntityLiving entityinsentient;

                                                        try {
                                                            entityinsentient = (EntityLiving) biomebase_biomemeta.entityClass.getConstructor(new Class[] { World.class}).newInstance(new Object[] { worldserver});
                                                        } catch (Exception exception) {
                                                            exception.printStackTrace();
                                                            ServerInternalException.reportInternalException(exception); // Paper
                                                            return j1;
                                                        }

                                                        entityinsentient.setLocationAndAngles((double) f, (double) k3, (double) f1, worldserver.rand.nextFloat() * 360.0F, 0.0F);
                                                        if (entityinsentient.getCanSpawnHere() && entityinsentient.isNotColliding()) {
                                                            groupdataentity = entityinsentient.onInitialSpawn(worldserver.getDifficultyForLocation(new BlockPos(entityinsentient)), groupdataentity);
                                                            if (entityinsentient.isNotColliding()) {
                                                                // CraftBukkit start
                                                                if (worldserver.addEntity(entityinsentient, SpawnReason.NATURAL)) {
                                                                    ++l2;
                                                                    moblimit--; // Spigot
                                                                }
                                                                // CraftBukkit end
                                                            } else {
                                                                entityinsentient.setDead();
                                                            }

                                                            // Spigot start
                                                            if ( moblimit <= 0 ) {
                                                                // If we're past limit, stop spawn
                                                                // Spigot end
                                                                continue label120;
                                                            }
                                                        }

                                                        j1 += l2;
                                                    }
                                                }

                                                ++j4;
                                                continue;
                                            }
                                        }

                                        ++i3;
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }

            return j1;
        }
    }

    private static BlockPos getRandomChunkPosition(World world, int i, int j) {
        Chunk chunk = world.getChunkFromChunkCoords(i, j);
        int k = i * 16 + world.rand.nextInt(16);
        int l = j * 16 + world.rand.nextInt(16);
        int i1 = MathHelper.roundUp(chunk.getHeight(new BlockPos(k, 0, l)) + 1, 16);
        int j1 = world.rand.nextInt(i1 > 0 ? i1 : chunk.getTopFilledSegment() + 16 - 1);

        return new BlockPos(k, j1, l);
    }

    public static boolean isValidEmptySpawnBlock(IBlockState iblockdata) {
        return iblockdata.isBlockNormalCube() ? false : (iblockdata.canProvidePower() ? false : (iblockdata.getMaterial().isLiquid() ? false : !BlockRailBase.isRailBlock(iblockdata)));
    }

    public static boolean canCreatureTypeSpawnAtLocation(EntityLiving.SpawnPlacementType entityinsentient_enumentitypositiontype, World world, BlockPos blockposition) {
        if (!world.getWorldBorder().contains(blockposition)) {
            return false;
        } else {
            IBlockState iblockdata = world.getBlockState(blockposition);

            if (entityinsentient_enumentitypositiontype == EntityLiving.SpawnPlacementType.IN_WATER) {
                return iblockdata.getMaterial() == Material.WATER && world.getBlockState(blockposition.down()).getMaterial() == Material.WATER && !world.getBlockState(blockposition.up()).isNormalCube();
            } else {
                BlockPos blockposition1 = blockposition.down();

                if (!world.getBlockState(blockposition1).isTopSolid()) {
                    return false;
                } else {
                    Block block = world.getBlockState(blockposition1).getBlock();
                    boolean flag = block != Blocks.BEDROCK && block != Blocks.BARRIER;

                    return flag && isValidEmptySpawnBlock(iblockdata) && isValidEmptySpawnBlock(world.getBlockState(blockposition.up()));
                }
            }
        }
    }

    public static void performWorldGenSpawning(World world, Biome biomebase, int i, int j, int k, int l, Random random) {
        List list = biomebase.getSpawnableList(EnumCreatureType.CREATURE);

        if (!list.isEmpty()) {
            while (random.nextFloat() < biomebase.getSpawningChance()) {
                Biome.SpawnListEntry biomebase_biomemeta = (Biome.SpawnListEntry) WeightedRandom.getRandomItem(world.rand, list);
                int i1 = biomebase_biomemeta.minGroupCount + random.nextInt(1 + biomebase_biomemeta.maxGroupCount - biomebase_biomemeta.minGroupCount);
                IEntityLivingData groupdataentity = null;
                int j1 = i + random.nextInt(k);
                int k1 = j + random.nextInt(l);
                int l1 = j1;
                int i2 = k1;

                for (int j2 = 0; j2 < i1; ++j2) {
                    boolean flag = false;

                    for (int k2 = 0; !flag && k2 < 4; ++k2) {
                        BlockPos blockposition = world.getTopSolidOrLiquidBlock(new BlockPos(j1, 0, k1));

                        if (canCreatureTypeSpawnAtLocation(EntityLiving.SpawnPlacementType.ON_GROUND, world, blockposition)) {
                            EntityLiving entityinsentient;

                            try {
                                entityinsentient = (EntityLiving) biomebase_biomemeta.entityClass.getConstructor(new Class[] { World.class}).newInstance(new Object[] { world});
                            } catch (Exception exception) {
                                exception.printStackTrace();
                                ServerInternalException.reportInternalException(exception); // Paper
                                continue;
                            }

                            entityinsentient.setLocationAndAngles((double) ((float) j1 + 0.5F), (double) blockposition.getY(), (double) ((float) k1 + 0.5F), random.nextFloat() * 360.0F, 0.0F);
                            // CraftBukkit start - Added a reason for spawning this creature, moved entityinsentient.prepare(groupdataentity) up
                            groupdataentity = entityinsentient.onInitialSpawn(world.getDifficultyForLocation(new BlockPos(entityinsentient)), groupdataentity);
                            world.addEntity(entityinsentient, SpawnReason.CHUNK_GEN);
                            // CraftBukkit end
                            flag = true;
                        }

                        j1 += random.nextInt(5) - random.nextInt(5);

                        for (k1 += random.nextInt(5) - random.nextInt(5); j1 < i || j1 >= i + k || k1 < j || k1 >= j + k; k1 = i2 + random.nextInt(5) - random.nextInt(5)) {
                            j1 = l1 + random.nextInt(5) - random.nextInt(5);
                        }
                    }
                }
            }

        }
    }
}
