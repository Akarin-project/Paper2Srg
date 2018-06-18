package net.minecraft.village;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mojang.authlib.GameProfile;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.management.PlayerProfileCache;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

public class Village {

    private World world;
    private final List<VillageDoorInfo> villageDoorInfoList = Lists.newArrayList();
    private BlockPos centerHelper;
    private BlockPos center;
    private int villageRadius;
    private int lastAddDoorTimestamp;
    private int tickCounter;
    private int numVillagers;
    private int noBreedTicks;
    private final Map<String, Integer> playerReputation;
    private final List<Village.VillageAggressor> villageAgressors;
    private int numIronGolems;

    private Village() { // Paper - Nothing should call this - world needs to be set.
        this.centerHelper = BlockPos.ORIGIN;
        this.center = BlockPos.ORIGIN;
        this.playerReputation = Maps.newHashMap();
        this.villageAgressors = Lists.newArrayList();
    }

    public Village(World world) {
        this.centerHelper = BlockPos.ORIGIN;
        this.center = BlockPos.ORIGIN;
        this.playerReputation = Maps.newHashMap();
        this.villageAgressors = Lists.newArrayList();
        this.world = world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public void tick(int i) {
        this.tickCounter = i;
        this.removeDeadAndOutOfRangeDoors();
        this.removeDeadAndOldAgressors();
        if (i % 20 == 0) {
            this.updateNumVillagers();
        }

        if (i % 30 == 0) {
            this.updateNumIronGolems();
        }

        int j = this.numVillagers / 10;

        if (this.numIronGolems < j && this.villageDoorInfoList.size() > 20 && this.world.rand.nextInt(7000) == 0) {
            Vec3d vec3d = this.findRandomSpawnPos(this.center, 2, 4, 2);

            if (vec3d != null) {
                EntityIronGolem entityirongolem = new EntityIronGolem(this.world);

                entityirongolem.setPosition(vec3d.x, vec3d.y, vec3d.z);
                this.world.addEntity(entityirongolem, org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason.VILLAGE_DEFENSE); // CraftBukkit
                ++this.numIronGolems;
            }
        }

    }

    private Vec3d findRandomSpawnPos(BlockPos blockposition, int i, int j, int k) {
        for (int l = 0; l < 10; ++l) {
            BlockPos blockposition1 = blockposition.add(this.world.rand.nextInt(16) - 8, this.world.rand.nextInt(6) - 3, this.world.rand.nextInt(16) - 8);

            if (this.isBlockPosWithinSqVillageRadius(blockposition1) && this.isAreaClearAround(new BlockPos(i, j, k), blockposition1)) {
                return new Vec3d((double) blockposition1.getX(), (double) blockposition1.getY(), (double) blockposition1.getZ());
            }
        }

        return null;
    }

    private boolean isAreaClearAround(BlockPos blockposition, BlockPos blockposition1) {
        if (!this.world.getBlockState(blockposition1.down()).isTopSolid()) {
            return false;
        } else {
            int i = blockposition1.getX() - blockposition.getX() / 2;
            int j = blockposition1.getZ() - blockposition.getZ() / 2;

            for (int k = i; k < i + blockposition.getX(); ++k) {
                for (int l = blockposition1.getY(); l < blockposition1.getY() + blockposition.getY(); ++l) {
                    for (int i1 = j; i1 < j + blockposition.getZ(); ++i1) {
                        if (this.world.getBlockState(new BlockPos(k, l, i1)).isNormalCube()) {
                            return false;
                        }
                    }
                }
            }

            return true;
        }
    }

    private void updateNumIronGolems() {
        List list = this.world.getEntitiesWithinAABB(EntityIronGolem.class, new AxisAlignedBB((double) (this.center.getX() - this.villageRadius), (double) (this.center.getY() - 4), (double) (this.center.getZ() - this.villageRadius), (double) (this.center.getX() + this.villageRadius), (double) (this.center.getY() + 4), (double) (this.center.getZ() + this.villageRadius)));

        this.numIronGolems = list.size();
    }

    private void updateNumVillagers() {
        List list = this.world.getEntitiesWithinAABB(EntityVillager.class, new AxisAlignedBB((double) (this.center.getX() - this.villageRadius), (double) (this.center.getY() - 4), (double) (this.center.getZ() - this.villageRadius), (double) (this.center.getX() + this.villageRadius), (double) (this.center.getY() + 4), (double) (this.center.getZ() + this.villageRadius)));

        this.numVillagers = list.size();
        if (this.numVillagers == 0) {
            this.playerReputation.clear();
        }

    }

    public BlockPos getCenter() {
        return this.center;
    }

    public int getVillageRadius() {
        return this.villageRadius;
    }

    public int getNumVillageDoors() {
        return this.villageDoorInfoList.size();
    }

    public int getTicksSinceLastDoorAdding() {
        return this.tickCounter - this.lastAddDoorTimestamp;
    }

    public int getNumVillagers() {
        return this.numVillagers;
    }

    public boolean isBlockPosWithinSqVillageRadius(BlockPos blockposition) {
        return this.center.distanceSq(blockposition) < (double) (this.villageRadius * this.villageRadius);
    }

    public List<VillageDoorInfo> getVillageDoorInfoList() {
        return this.villageDoorInfoList;
    }

    public VillageDoorInfo getNearestDoor(BlockPos blockposition) {
        VillageDoorInfo villagedoor = null;
        int i = Integer.MAX_VALUE;
        Iterator iterator = this.villageDoorInfoList.iterator();

        while (iterator.hasNext()) {
            VillageDoorInfo villagedoor1 = (VillageDoorInfo) iterator.next();
            int j = villagedoor1.getDistanceToDoorBlockSq(blockposition);

            if (j < i) {
                villagedoor = villagedoor1;
                i = j;
            }
        }

        return villagedoor;
    }

    public VillageDoorInfo getDoorInfo(BlockPos blockposition) {
        VillageDoorInfo villagedoor = null;
        int i = Integer.MAX_VALUE;
        Iterator iterator = this.villageDoorInfoList.iterator();

        while (iterator.hasNext()) {
            VillageDoorInfo villagedoor1 = (VillageDoorInfo) iterator.next();
            int j = villagedoor1.getDistanceToDoorBlockSq(blockposition);

            if (j > 256) {
                j *= 1000;
            } else {
                j = villagedoor1.getDoorOpeningRestrictionCounter();
            }

            if (j < i) {
                BlockPos blockposition1 = villagedoor1.getDoorBlockPos();
                EnumFacing enumdirection = villagedoor1.getInsideDirection();

                if (this.world.getBlockState(blockposition1.offset(enumdirection, 1)).getBlock().isPassable(this.world, blockposition1.offset(enumdirection, 1)) && this.world.getBlockState(blockposition1.offset(enumdirection, -1)).getBlock().isPassable(this.world, blockposition1.offset(enumdirection, -1)) && this.world.getBlockState(blockposition1.up().offset(enumdirection, 1)).getBlock().isPassable(this.world, blockposition1.up().offset(enumdirection, 1)) && this.world.getBlockState(blockposition1.up().offset(enumdirection, -1)).getBlock().isPassable(this.world, blockposition1.up().offset(enumdirection, -1))) {
                    villagedoor = villagedoor1;
                    i = j;
                }
            }
        }

        return villagedoor;
    }

    @Nullable
    public VillageDoorInfo getExistedDoor(BlockPos blockposition) {
        if (this.center.distanceSq(blockposition) > (double) (this.villageRadius * this.villageRadius)) {
            return null;
        } else {
            Iterator iterator = this.villageDoorInfoList.iterator();

            VillageDoorInfo villagedoor;

            do {
                if (!iterator.hasNext()) {
                    return null;
                }

                villagedoor = (VillageDoorInfo) iterator.next();
            } while (villagedoor.getDoorBlockPos().getX() != blockposition.getX() || villagedoor.getDoorBlockPos().getZ() != blockposition.getZ() || Math.abs(villagedoor.getDoorBlockPos().getY() - blockposition.getY()) > 1);

            return villagedoor;
        }
    }

    public void addVillageDoorInfo(VillageDoorInfo villagedoor) {
        this.villageDoorInfoList.add(villagedoor);
        this.centerHelper = this.centerHelper.add((Vec3i) villagedoor.getDoorBlockPos());
        this.updateVillageRadiusAndCenter();
        this.lastAddDoorTimestamp = villagedoor.getLastActivityTimestamp();
    }

    public boolean isAnnihilated() {
        return this.villageDoorInfoList.isEmpty();
    }

    public void addOrRenewAgressor(EntityLivingBase entityliving) {
        Iterator iterator = this.villageAgressors.iterator();

        Village.VillageAggressor village_aggressor;

        do {
            if (!iterator.hasNext()) {
                this.villageAgressors.add(new Village.VillageAggressor(entityliving, this.tickCounter));
                return;
            }

            village_aggressor = (Village.VillageAggressor) iterator.next();
        } while (village_aggressor.agressor != entityliving);

        village_aggressor.agressionTime = this.tickCounter;
    }

    @Nullable
    public EntityLivingBase findNearestVillageAggressor(EntityLivingBase entityliving) {
        double d0 = Double.MAX_VALUE;
        Village.VillageAggressor village_aggressor = null;

        for (int i = 0; i < this.villageAgressors.size(); ++i) {
            Village.VillageAggressor village_aggressor1 = (Village.VillageAggressor) this.villageAgressors.get(i);
            double d1 = village_aggressor1.agressor.getDistanceSq(entityliving);

            if (d1 <= d0) {
                village_aggressor = village_aggressor1;
                d0 = d1;
            }
        }

        return village_aggressor == null ? null : village_aggressor.agressor;
    }

    public EntityPlayer getNearestTargetPlayer(EntityLivingBase entityliving) {
        double d0 = Double.MAX_VALUE;
        EntityPlayer entityhuman = null;
        Iterator iterator = this.playerReputation.keySet().iterator();

        while (iterator.hasNext()) {
            String s = (String) iterator.next();

            if (this.isPlayerReputationTooLow(s)) {
                EntityPlayer entityhuman1 = this.world.getPlayerEntityByName(s);

                if (entityhuman1 != null) {
                    double d1 = entityhuman1.getDistanceSq(entityliving);

                    if (d1 <= d0) {
                        entityhuman = entityhuman1;
                        d0 = d1;
                    }
                }
            }
        }

        return entityhuman;
    }

    private void removeDeadAndOldAgressors() {
        Iterator iterator = this.villageAgressors.iterator();

        while (iterator.hasNext()) {
            Village.VillageAggressor village_aggressor = (Village.VillageAggressor) iterator.next();

            if (!village_aggressor.agressor.isEntityAlive() || Math.abs(this.tickCounter - village_aggressor.agressionTime) > 300) {
                iterator.remove();
            }
        }

    }

    private void removeDeadAndOutOfRangeDoors() {
        boolean flag = false;
        boolean flag1 = this.world.rand.nextInt(50) == 0;
        Iterator iterator = this.villageDoorInfoList.iterator();

        while (iterator.hasNext()) {
            VillageDoorInfo villagedoor = (VillageDoorInfo) iterator.next();

            if (flag1) {
                villagedoor.resetDoorOpeningRestrictionCounter();
            }

            if (!this.isWoodDoor(villagedoor.getDoorBlockPos()) || Math.abs(this.tickCounter - villagedoor.getLastActivityTimestamp()) > 1200) {
                this.centerHelper = this.centerHelper.subtract(villagedoor.getDoorBlockPos());
                flag = true;
                villagedoor.setIsDetachedFromVillageFlag(true);
                iterator.remove();
            }
        }

        if (flag) {
            this.updateVillageRadiusAndCenter();
        }

    }

    private boolean isWoodDoor(BlockPos blockposition) {
        IBlockState iblockdata = this.world.getBlockState(blockposition);
        Block block = iblockdata.getBlock();

        return block instanceof BlockDoor ? iblockdata.getMaterial() == Material.WOOD : false;
    }

    private void updateVillageRadiusAndCenter() {
        int i = this.villageDoorInfoList.size();

        if (i == 0) {
            this.center = BlockPos.ORIGIN;
            this.villageRadius = 0;
        } else {
            this.center = new BlockPos(this.centerHelper.getX() / i, this.centerHelper.getY() / i, this.centerHelper.getZ() / i);
            int j = 0;

            VillageDoorInfo villagedoor;

            for (Iterator iterator = this.villageDoorInfoList.iterator(); iterator.hasNext(); j = Math.max(villagedoor.getDistanceToDoorBlockSq(this.center), j)) {
                villagedoor = (VillageDoorInfo) iterator.next();
            }

            this.villageRadius = Math.max(32, (int) Math.sqrt((double) j) + 1);
        }
    }

    public int getPlayerReputation(String s) {
        Integer integer = (Integer) this.playerReputation.get(s);

        return integer == null ? 0 : integer.intValue();
    }

    public int modifyPlayerReputation(String s, int i) {
        int j = this.getPlayerReputation(s);
        int k = MathHelper.clamp(j + i, -30, 10);

        this.playerReputation.put(s, Integer.valueOf(k));
        return k;
    }

    public boolean isPlayerReputationTooLow(String s) {
        return this.getPlayerReputation(s) <= -15;
    }

    public void readVillageDataFromNBT(NBTTagCompound nbttagcompound) {
        this.numVillagers = nbttagcompound.getInteger("PopSize");
        this.villageRadius = nbttagcompound.getInteger("Radius");
        this.numIronGolems = nbttagcompound.getInteger("Golems");
        this.lastAddDoorTimestamp = nbttagcompound.getInteger("Stable");
        this.tickCounter = nbttagcompound.getInteger("Tick");
        this.noBreedTicks = nbttagcompound.getInteger("MTick");
        this.center = new BlockPos(nbttagcompound.getInteger("CX"), nbttagcompound.getInteger("CY"), nbttagcompound.getInteger("CZ"));
        this.centerHelper = new BlockPos(nbttagcompound.getInteger("ACX"), nbttagcompound.getInteger("ACY"), nbttagcompound.getInteger("ACZ"));
        NBTTagList nbttaglist = nbttagcompound.getTagList("Doors", 10);

        for (int i = 0; i < nbttaglist.tagCount(); ++i) {
            NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
            VillageDoorInfo villagedoor = new VillageDoorInfo(new BlockPos(nbttagcompound1.getInteger("X"), nbttagcompound1.getInteger("Y"), nbttagcompound1.getInteger("Z")), nbttagcompound1.getInteger("IDX"), nbttagcompound1.getInteger("IDZ"), nbttagcompound1.getInteger("TS"));

            this.villageDoorInfoList.add(villagedoor);
        }

        NBTTagList nbttaglist1 = nbttagcompound.getTagList("Players", 10);

        for (int j = 0; j < nbttaglist1.tagCount(); ++j) {
            NBTTagCompound nbttagcompound2 = nbttaglist1.getCompoundTagAt(j);

            if (nbttagcompound2.hasKey("UUID") && this.world != null && this.world.getMinecraftServer() != null) {
                PlayerProfileCache usercache = this.world.getMinecraftServer().getPlayerProfileCache();
                GameProfile gameprofile = usercache.getProfileByUUID(UUID.fromString(nbttagcompound2.getString("UUID")));

                if (gameprofile != null) {
                    this.playerReputation.put(gameprofile.getName(), Integer.valueOf(nbttagcompound2.getInteger("S")));
                }
            } else {
                this.playerReputation.put(nbttagcompound2.getString("Name"), Integer.valueOf(nbttagcompound2.getInteger("S")));
            }
        }

    }

    public void writeVillageDataToNBT(NBTTagCompound nbttagcompound) {
        nbttagcompound.setInteger("PopSize", this.numVillagers);
        nbttagcompound.setInteger("Radius", this.villageRadius);
        nbttagcompound.setInteger("Golems", this.numIronGolems);
        nbttagcompound.setInteger("Stable", this.lastAddDoorTimestamp);
        nbttagcompound.setInteger("Tick", this.tickCounter);
        nbttagcompound.setInteger("MTick", this.noBreedTicks);
        nbttagcompound.setInteger("CX", this.center.getX());
        nbttagcompound.setInteger("CY", this.center.getY());
        nbttagcompound.setInteger("CZ", this.center.getZ());
        nbttagcompound.setInteger("ACX", this.centerHelper.getX());
        nbttagcompound.setInteger("ACY", this.centerHelper.getY());
        nbttagcompound.setInteger("ACZ", this.centerHelper.getZ());
        NBTTagList nbttaglist = new NBTTagList();
        Iterator iterator = this.villageDoorInfoList.iterator();

        while (iterator.hasNext()) {
            VillageDoorInfo villagedoor = (VillageDoorInfo) iterator.next();
            NBTTagCompound nbttagcompound1 = new NBTTagCompound();

            nbttagcompound1.setInteger("X", villagedoor.getDoorBlockPos().getX());
            nbttagcompound1.setInteger("Y", villagedoor.getDoorBlockPos().getY());
            nbttagcompound1.setInteger("Z", villagedoor.getDoorBlockPos().getZ());
            nbttagcompound1.setInteger("IDX", villagedoor.getInsideOffsetX());
            nbttagcompound1.setInteger("IDZ", villagedoor.getInsideOffsetZ());
            nbttagcompound1.setInteger("TS", villagedoor.getLastActivityTimestamp());
            nbttaglist.appendTag(nbttagcompound1);
        }

        nbttagcompound.setTag("Doors", nbttaglist);
        NBTTagList nbttaglist1 = new NBTTagList();
        Iterator iterator1 = this.playerReputation.keySet().iterator();

        while (iterator1.hasNext()) {
            String s = (String) iterator1.next();
            NBTTagCompound nbttagcompound2 = new NBTTagCompound();
            PlayerProfileCache usercache = this.world.getMinecraftServer().getPlayerProfileCache();

            try {
                GameProfile gameprofile = usercache.getGameProfileForUsername(s);

                if (gameprofile != null) {
                    nbttagcompound2.setString("UUID", gameprofile.getId().toString());
                    nbttagcompound2.setInteger("S", ((Integer) this.playerReputation.get(s)).intValue());
                    nbttaglist1.appendTag(nbttagcompound2);
                }
            } catch (RuntimeException runtimeexception) {
                ;
            }
        }

        nbttagcompound.setTag("Players", nbttaglist1);
    }

    public void endMatingSeason() {
        this.noBreedTicks = this.tickCounter;
    }

    public boolean isMatingSeason() {
        return this.noBreedTicks == 0 || this.tickCounter - this.noBreedTicks >= 3600;
    }

    public void setDefaultPlayerReputation(int i) {
        Iterator iterator = this.playerReputation.keySet().iterator();

        while (iterator.hasNext()) {
            String s = (String) iterator.next();

            this.modifyPlayerReputation(s, i);
        }

    }

    class VillageAggressor {

        public EntityLivingBase agressor;
        public int agressionTime;

        VillageAggressor(EntityLivingBase entityliving, int i) {
            this.agressor = entityliving;
            this.agressionTime = i;
        }
    }
}
