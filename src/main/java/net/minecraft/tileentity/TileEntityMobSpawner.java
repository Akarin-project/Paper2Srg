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

    private final MobSpawnerBaseLogic field_145882_a = new MobSpawnerBaseLogic() {
        public void func_98267_a(int i) {
            TileEntityMobSpawner.this.field_145850_b.func_175641_c(TileEntityMobSpawner.this.field_174879_c, Blocks.field_150474_ac, i, 0);
        }

        public World func_98271_a() {
            return TileEntityMobSpawner.this.field_145850_b;
        }

        public BlockPos func_177221_b() {
            return TileEntityMobSpawner.this.field_174879_c;
        }

        public void func_184993_a(WeightedSpawnerEntity mobspawnerdata) {
            super.func_184993_a(mobspawnerdata);
            if (this.func_98271_a() != null) {
                IBlockState iblockdata = this.func_98271_a().func_180495_p(this.func_177221_b());

                this.func_98271_a().func_184138_a(TileEntityMobSpawner.this.field_174879_c, iblockdata, iblockdata, 4);
            }

        }
    };

    public TileEntityMobSpawner() {}

    public static void func_189684_a(DataFixer dataconvertermanager) {
        dataconvertermanager.func_188258_a(FixTypes.BLOCK_ENTITY, new IDataWalker() {
            public NBTTagCompound func_188266_a(IDataFixer dataconverter, NBTTagCompound nbttagcompound, int i) {
                if (TileEntity.func_190559_a(TileEntityMobSpawner.class).equals(new ResourceLocation(nbttagcompound.func_74779_i("id")))) {
                    if (nbttagcompound.func_150297_b("SpawnPotentials", 9)) {
                        NBTTagList nbttaglist = nbttagcompound.func_150295_c("SpawnPotentials", 10);

                        for (int j = 0; j < nbttaglist.func_74745_c(); ++j) {
                            NBTTagCompound nbttagcompound1 = nbttaglist.func_150305_b(j);

                            nbttagcompound1.func_74782_a("Entity", dataconverter.func_188251_a(FixTypes.ENTITY, nbttagcompound1.func_74775_l("Entity"), i));
                        }
                    }

                    nbttagcompound.func_74782_a("SpawnData", dataconverter.func_188251_a(FixTypes.ENTITY, nbttagcompound.func_74775_l("SpawnData"), i));
                }

                return nbttagcompound;
            }
        });
    }

    public void func_145839_a(NBTTagCompound nbttagcompound) {
        super.func_145839_a(nbttagcompound);
        this.field_145882_a.func_98270_a(nbttagcompound);
    }

    public NBTTagCompound func_189515_b(NBTTagCompound nbttagcompound) {
        super.func_189515_b(nbttagcompound);
        this.field_145882_a.func_189530_b(nbttagcompound);
        return nbttagcompound;
    }

    public void func_73660_a() {
        this.field_145882_a.func_98278_g();
    }

    @Nullable
    public SPacketUpdateTileEntity func_189518_D_() {
        return new SPacketUpdateTileEntity(this.field_174879_c, 1, this.func_189517_E_());
    }

    public NBTTagCompound func_189517_E_() {
        NBTTagCompound nbttagcompound = this.func_189515_b(new NBTTagCompound());

        nbttagcompound.func_82580_o("SpawnPotentials");
        return nbttagcompound;
    }

    public boolean func_145842_c(int i, int j) {
        return this.field_145882_a.func_98268_b(i) ? true : super.func_145842_c(i, j);
    }

    public boolean func_183000_F() {
        return true;
    }

    public MobSpawnerBaseLogic func_145881_a() {
        return this.field_145882_a;
    }
}
