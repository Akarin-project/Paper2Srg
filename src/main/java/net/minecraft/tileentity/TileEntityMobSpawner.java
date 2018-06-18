package net.minecraft.tileentity;

import javax.annotation.Nullable;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.WeightedSpawnerEntity;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.IDataFixer;
import net.minecraft.util.datafix.IDataWalker;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEntityMobSpawner extends TileEntity implements ITickable {

    private final MobSpawnerBaseLogic spawnerLogic = new MobSpawnerBaseLogic() {
        public void broadcastEvent(int i) {
            TileEntityMobSpawner.this.world.addBlockEvent(TileEntityMobSpawner.this.pos, Blocks.MOB_SPAWNER, i, 0);
        }

        public World getSpawnerWorld() {
            return TileEntityMobSpawner.this.world;
        }

        public BlockPos getSpawnerPosition() {
            return TileEntityMobSpawner.this.pos;
        }

        public void setNextSpawnData(WeightedSpawnerEntity mobspawnerdata) {
            super.setNextSpawnData(mobspawnerdata);
            if (this.getSpawnerWorld() != null) {
                IBlockState iblockdata = this.getSpawnerWorld().getBlockState(this.getSpawnerPosition());

                this.getSpawnerWorld().notifyBlockUpdate(TileEntityMobSpawner.this.pos, iblockdata, iblockdata, 4);
            }

        }
    };

    public TileEntityMobSpawner() {}

    public static void registerFixesMobSpawner(DataFixer dataconvertermanager) {
        dataconvertermanager.registerWalker(FixTypes.BLOCK_ENTITY, new IDataWalker() {
            public NBTTagCompound process(IDataFixer dataconverter, NBTTagCompound nbttagcompound, int i) {
                if (TileEntity.getKey(TileEntityMobSpawner.class).equals(new ResourceLocation(nbttagcompound.getString("id")))) {
                    if (nbttagcompound.hasKey("SpawnPotentials", 9)) {
                        NBTTagList nbttaglist = nbttagcompound.getTagList("SpawnPotentials", 10);

                        for (int j = 0; j < nbttaglist.tagCount(); ++j) {
                            NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(j);

                            nbttagcompound1.setTag("Entity", dataconverter.process(FixTypes.ENTITY, nbttagcompound1.getCompoundTag("Entity"), i));
                        }
                    }

                    nbttagcompound.setTag("SpawnData", dataconverter.process(FixTypes.ENTITY, nbttagcompound.getCompoundTag("SpawnData"), i));
                }

                return nbttagcompound;
            }
        });
    }

    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.spawnerLogic.readFromNBT(nbttagcompound);
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        this.spawnerLogic.writeToNBT(nbttagcompound);
        return nbttagcompound;
    }

    public void update() {
        this.spawnerLogic.updateSpawner();
    }

    @Nullable
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(this.pos, 1, this.getUpdateTag());
    }

    public NBTTagCompound getUpdateTag() {
        NBTTagCompound nbttagcompound = this.writeToNBT(new NBTTagCompound());

        nbttagcompound.removeTag("SpawnPotentials");
        return nbttagcompound;
    }

    public boolean receiveClientEvent(int i, int j) {
        return this.spawnerLogic.setDelayToMin(i) ? true : super.receiveClientEvent(i, j);
    }

    public boolean onlyOpsCanSetNbt() {
        return true;
    }

    public MobSpawnerBaseLogic getSpawnerBaseLogic() {
        return this.spawnerLogic;
    }
}
