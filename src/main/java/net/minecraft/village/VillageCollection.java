package net.minecraft.village;

import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.storage.WorldSavedData;

public class VillageCollection extends WorldSavedData {

    private World world;
    private final List<BlockPos> villagerPositionsList = Lists.newArrayList();
    private final List<VillageDoorInfo> newDoors = Lists.newArrayList();
    private final List<Village> villageList = Lists.newArrayList();
    private int tickCounter;

    public VillageCollection(String s) {
        super(s);
    }

    public VillageCollection(World world) {
        super(fileNameForProvider(world.provider));
        this.world = world;
        this.markDirty();
    }

    public void setWorldsForAll(World world) {
        this.world = world;
        Iterator iterator = this.villageList.iterator();

        while (iterator.hasNext()) {
            Village village = (Village) iterator.next();

            village.setWorld(world);
        }

    }

    public void addToVillagerPositionList(BlockPos blockposition) {
        if (this.villagerPositionsList.size() <= 64) {
            if (!this.positionInList(blockposition)) {
                this.villagerPositionsList.add(blockposition);
            }

        }
    }

    public void tick() {
        ++this.tickCounter;
        Iterator iterator = this.villageList.iterator();

        while (iterator.hasNext()) {
            Village village = (Village) iterator.next();

            village.tick(this.tickCounter);
        }

        this.removeAnnihilatedVillages();
        this.dropOldestVillagerPosition();
        this.addNewDoorsToVillageOrCreateVillage();
        if (this.tickCounter % 400 == 0) {
            this.markDirty();
        }

    }

    private void removeAnnihilatedVillages() {
        Iterator iterator = this.villageList.iterator();

        while (iterator.hasNext()) {
            Village village = (Village) iterator.next();

            if (village.isAnnihilated()) {
                iterator.remove();
                this.markDirty();
            }
        }

    }

    public List<Village> getVillageList() {
        return this.villageList;
    }

    public Village getNearestVillage(BlockPos blockposition, int i) {
        Village village = null;
        double d0 = 3.4028234663852886E38D;
        Iterator iterator = this.villageList.iterator();

        while (iterator.hasNext()) {
            Village village1 = (Village) iterator.next();
            double d1 = village1.getCenter().distanceSq(blockposition);

            if (d1 < d0) {
                float f = (float) (i + village1.getVillageRadius());

                if (d1 <= (double) (f * f)) {
                    village = village1;
                    d0 = d1;
                }
            }
        }

        return village;
    }

    private void dropOldestVillagerPosition() {
        if (!this.villagerPositionsList.isEmpty()) {
            this.addDoorsAround((BlockPos) this.villagerPositionsList.remove(0));
        }
    }

    private void addNewDoorsToVillageOrCreateVillage() {
        for (int i = 0; i < this.newDoors.size(); ++i) {
            VillageDoorInfo villagedoor = (VillageDoorInfo) this.newDoors.get(i);
            Village village = this.getNearestVillage(villagedoor.getDoorBlockPos(), 32);

            if (village == null) {
                village = new Village(this.world);
                this.villageList.add(village);
                this.markDirty();
            }

            village.addVillageDoorInfo(villagedoor);
        }

        this.newDoors.clear();
    }

    private void addDoorsAround(BlockPos blockposition) {
        boolean flag = true;
        boolean flag1 = true;
        boolean flag2 = true;

        for (int i = -16; i < 16; ++i) {
            for (int j = -4; j < 4; ++j) {
                for (int k = -16; k < 16; ++k) {
                    BlockPos blockposition1 = blockposition.add(i, j, k);

                    if (this.isWoodDoor(blockposition1)) {
                        VillageDoorInfo villagedoor = this.checkDoorExistence(blockposition1);

                        if (villagedoor == null) {
                            this.addToNewDoorsList(blockposition1);
                        } else {
                            villagedoor.setLastActivityTimestamp(this.tickCounter);
                        }
                    }
                }
            }
        }

    }

    @Nullable
    private VillageDoorInfo checkDoorExistence(BlockPos blockposition) {
        Iterator iterator = this.newDoors.iterator();

        VillageDoorInfo villagedoor;

        do {
            if (!iterator.hasNext()) {
                iterator = this.villageList.iterator();

                VillageDoorInfo villagedoor1;

                do {
                    if (!iterator.hasNext()) {
                        return null;
                    }

                    Village village = (Village) iterator.next();

                    villagedoor1 = village.getExistedDoor(blockposition);
                } while (villagedoor1 == null);

                return villagedoor1;
            }

            villagedoor = (VillageDoorInfo) iterator.next();
        } while (villagedoor.getDoorBlockPos().getX() != blockposition.getX() || villagedoor.getDoorBlockPos().getZ() != blockposition.getZ() || Math.abs(villagedoor.getDoorBlockPos().getY() - blockposition.getY()) > 1);

        return villagedoor;
    }

    private void addToNewDoorsList(BlockPos blockposition) {
        EnumFacing enumdirection = BlockDoor.getFacing(this.world, blockposition);
        EnumFacing enumdirection1 = enumdirection.getOpposite();
        int i = this.countBlocksCanSeeSky(blockposition, enumdirection, 5);
        int j = this.countBlocksCanSeeSky(blockposition, enumdirection1, i + 1);

        if (i != j) {
            this.newDoors.add(new VillageDoorInfo(blockposition, i < j ? enumdirection : enumdirection1, this.tickCounter));
        }

    }

    private int countBlocksCanSeeSky(BlockPos blockposition, EnumFacing enumdirection, int i) {
        int j = 0;

        for (int k = 1; k <= 5; ++k) {
            if (this.world.canSeeSky(blockposition.offset(enumdirection, k))) {
                ++j;
                if (j >= i) {
                    return j;
                }
            }
        }

        return j;
    }

    private boolean positionInList(BlockPos blockposition) {
        Iterator iterator = this.villagerPositionsList.iterator();

        BlockPos blockposition1;

        do {
            if (!iterator.hasNext()) {
                return false;
            }

            blockposition1 = (BlockPos) iterator.next();
        } while (!blockposition1.equals(blockposition));

        return true;
    }

    private boolean isWoodDoor(BlockPos blockposition) {
        // Paper start
        IBlockState iblockdata = this.world.getTypeIfLoaded(blockposition);
        if (iblockdata == null) {
            return false;
        }
        // Paper end
        Block block = iblockdata.getBlock();

        return block instanceof BlockDoor ? iblockdata.getMaterial() == Material.WOOD : false;
    }

    public void readFromNBT(NBTTagCompound nbttagcompound) {
        this.tickCounter = nbttagcompound.getInteger("Tick");
        NBTTagList nbttaglist = nbttagcompound.getTagList("Villages", 10);

        for (int i = 0; i < nbttaglist.tagCount(); ++i) {
            NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
            Village village = new Village(world); // Paper

            village.readVillageDataFromNBT(nbttagcompound1);
            this.villageList.add(village);
        }

    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
        nbttagcompound.setInteger("Tick", this.tickCounter);
        NBTTagList nbttaglist = new NBTTagList();
        Iterator iterator = this.villageList.iterator();

        while (iterator.hasNext()) {
            Village village = (Village) iterator.next();
            NBTTagCompound nbttagcompound1 = new NBTTagCompound();

            village.writeVillageDataToNBT(nbttagcompound1);
            nbttaglist.appendTag(nbttagcompound1);
        }

        nbttagcompound.setTag("Villages", nbttaglist);
        return nbttagcompound;
    }

    public static String fileNameForProvider(WorldProvider worldprovider) {
        return "villages" + worldprovider.getDimensionType().getSuffix();
    }
}
