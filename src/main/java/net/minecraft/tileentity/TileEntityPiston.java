package net.minecraft.tileentity;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.BlockPistonExtension;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class TileEntityPiston extends TileEntity implements ITickable {

    private IBlockState field_174932_a;
    private EnumFacing field_174931_f;
    private boolean field_145875_k;
    private boolean field_145872_l;
    private static final ThreadLocal<EnumFacing> field_190613_i = new ThreadLocal() {
        protected EnumFacing a() {
            return null;
        }

        @Override
        protected Object initialValue() {
            return this.a();
        }
    };
    private float field_145873_m;
    private float field_145870_n;

    public TileEntityPiston() {}

    public TileEntityPiston(IBlockState iblockdata, EnumFacing enumdirection, boolean flag, boolean flag1) {
        this.field_174932_a = iblockdata;
        this.field_174931_f = enumdirection;
        this.field_145875_k = flag;
        this.field_145872_l = flag1;
    }

    public IBlockState func_174927_b() {
        return this.field_174932_a;
    }

    @Override
    public NBTTagCompound func_189517_E_() {
        return this.func_189515_b(new NBTTagCompound());
    }

    @Override
    public int func_145832_p() {
        return 0;
    }

    public boolean func_145868_b() {
        return this.field_145875_k;
    }

    public EnumFacing func_174930_e() {
        return this.field_174931_f;
    }

    public boolean func_145867_d() {
        return this.field_145872_l;
    }

    private float func_184320_e(float f) {
        return this.field_145875_k ? f - 1.0F : 1.0F - f;
    }

    public AxisAlignedBB func_184321_a(IBlockAccess iblockaccess, BlockPos blockposition) {
        return this.func_184319_a(iblockaccess, blockposition, this.field_145873_m).func_111270_a(this.func_184319_a(iblockaccess, blockposition, this.field_145870_n));
    }

    public AxisAlignedBB func_184319_a(IBlockAccess iblockaccess, BlockPos blockposition, float f) {
        f = this.func_184320_e(f);
        IBlockState iblockdata = this.func_190606_j();

        return iblockdata.func_185900_c(iblockaccess, blockposition).func_72317_d(f * this.field_174931_f.func_82601_c(), f * this.field_174931_f.func_96559_d(), f * this.field_174931_f.func_82599_e());
    }

    private IBlockState func_190606_j() {
        return !this.func_145868_b() && this.func_145867_d() ? Blocks.field_150332_K.func_176223_P().func_177226_a(BlockPistonExtension.field_176325_b, this.field_174932_a.func_177230_c() == Blocks.field_150320_F ? BlockPistonExtension.EnumPistonType.STICKY : BlockPistonExtension.EnumPistonType.DEFAULT).func_177226_a(BlockPistonExtension.field_176387_N, this.field_174932_a.func_177229_b(BlockPistonBase.field_176387_N)) : this.field_174932_a;
    }

    private void func_184322_i(float f) {
        EnumFacing enumdirection = this.field_145875_k ? this.field_174931_f : this.field_174931_f.func_176734_d();
        double d0 = f - this.field_145873_m;
        ArrayList arraylist = Lists.newArrayList();

        this.func_190606_j().func_185908_a(this.field_145850_b, BlockPos.field_177992_a, new AxisAlignedBB(BlockPos.field_177992_a), arraylist, (Entity) null, true);
        if (!arraylist.isEmpty()) {
            AxisAlignedBB axisalignedbb = this.func_190607_a(this.func_191515_a(arraylist));
            List list = this.field_145850_b.func_72839_b((Entity) null, this.func_190610_a(axisalignedbb, enumdirection, d0).func_111270_a(axisalignedbb));

            if (!list.isEmpty()) {
                boolean flag = this.field_174932_a.func_177230_c() == Blocks.field_180399_cE;

                for (int i = 0; i < list.size(); ++i) {
                    Entity entity = (Entity) list.get(i);

                    if (entity.func_184192_z() != EnumPushReaction.IGNORE) {
                        if (flag) {
                            switch (enumdirection.func_176740_k()) {
                            case X:
                                entity.field_70159_w = enumdirection.func_82601_c();
                                break;

                            case Y:
                                entity.field_70181_x = enumdirection.func_96559_d();
                                break;

                            case Z:
                                entity.field_70179_y = enumdirection.func_82599_e();
                            }
                        }

                        double d1 = 0.0D;

                        for (int j = 0; j < arraylist.size(); ++j) {
                            AxisAlignedBB axisalignedbb1 = this.func_190610_a(this.func_190607_a((AxisAlignedBB) arraylist.get(j)), enumdirection, d0);
                            AxisAlignedBB axisalignedbb2 = entity.func_174813_aQ();

                            if (axisalignedbb1.func_72326_a(axisalignedbb2)) {
                                d1 = Math.max(d1, this.func_190612_a(axisalignedbb1, enumdirection, axisalignedbb2));
                                if (d1 >= d0) {
                                    break;
                                }
                            }
                        }

                        if (d1 > 0.0D) {
                            d1 = Math.min(d1, d0) + 0.01D;
                            TileEntityPiston.field_190613_i.set(enumdirection);
                            entity.func_70091_d(MoverType.PISTON, d1 * enumdirection.func_82601_c(), d1 * enumdirection.func_96559_d(), d1 * enumdirection.func_82599_e());
                            TileEntityPiston.field_190613_i.set(null);
                            if (!this.field_145875_k && this.field_145872_l) {
                                this.func_190605_a(entity, enumdirection, d0);
                            }
                        }
                    }
                }

            }
        }
    }

    private AxisAlignedBB func_191515_a(List<AxisAlignedBB> list) {
        double d0 = 0.0D;
        double d1 = 0.0D;
        double d2 = 0.0D;
        double d3 = 1.0D;
        double d4 = 1.0D;
        double d5 = 1.0D;

        AxisAlignedBB axisalignedbb;

        for (Iterator iterator = list.iterator(); iterator.hasNext(); d5 = Math.max(axisalignedbb.field_72334_f, d5)) {
            axisalignedbb = (AxisAlignedBB) iterator.next();
            d0 = Math.min(axisalignedbb.field_72340_a, d0);
            d1 = Math.min(axisalignedbb.field_72338_b, d1);
            d2 = Math.min(axisalignedbb.field_72339_c, d2);
            d3 = Math.max(axisalignedbb.field_72336_d, d3);
            d4 = Math.max(axisalignedbb.field_72337_e, d4);
        }

        return new AxisAlignedBB(d0, d1, d2, d3, d4, d5);
    }

    private double func_190612_a(AxisAlignedBB axisalignedbb, EnumFacing enumdirection, AxisAlignedBB axisalignedbb1) {
        switch (enumdirection.func_176740_k()) {
        case X:
            return func_190611_b(axisalignedbb, enumdirection, axisalignedbb1);

        case Y:
        default:
            return func_190608_c(axisalignedbb, enumdirection, axisalignedbb1);

        case Z:
            return func_190604_d(axisalignedbb, enumdirection, axisalignedbb1);
        }
    }

    private AxisAlignedBB func_190607_a(AxisAlignedBB axisalignedbb) {
        double d0 = this.func_184320_e(this.field_145873_m);

        return axisalignedbb.func_72317_d(this.field_174879_c.func_177958_n() + d0 * this.field_174931_f.func_82601_c(), this.field_174879_c.func_177956_o() + d0 * this.field_174931_f.func_96559_d(), this.field_174879_c.func_177952_p() + d0 * this.field_174931_f.func_82599_e());
    }

    private AxisAlignedBB func_190610_a(AxisAlignedBB axisalignedbb, EnumFacing enumdirection, double d0) {
        double d1 = d0 * enumdirection.func_176743_c().func_179524_a();
        double d2 = Math.min(d1, 0.0D);
        double d3 = Math.max(d1, 0.0D);

        switch (enumdirection) {
        case WEST:
            return new AxisAlignedBB(axisalignedbb.field_72340_a + d2, axisalignedbb.field_72338_b, axisalignedbb.field_72339_c, axisalignedbb.field_72340_a + d3, axisalignedbb.field_72337_e, axisalignedbb.field_72334_f);

        case EAST:
            return new AxisAlignedBB(axisalignedbb.field_72336_d + d2, axisalignedbb.field_72338_b, axisalignedbb.field_72339_c, axisalignedbb.field_72336_d + d3, axisalignedbb.field_72337_e, axisalignedbb.field_72334_f);

        case DOWN:
            return new AxisAlignedBB(axisalignedbb.field_72340_a, axisalignedbb.field_72338_b + d2, axisalignedbb.field_72339_c, axisalignedbb.field_72336_d, axisalignedbb.field_72338_b + d3, axisalignedbb.field_72334_f);

        case UP:
        default:
            return new AxisAlignedBB(axisalignedbb.field_72340_a, axisalignedbb.field_72337_e + d2, axisalignedbb.field_72339_c, axisalignedbb.field_72336_d, axisalignedbb.field_72337_e + d3, axisalignedbb.field_72334_f);

        case NORTH:
            return new AxisAlignedBB(axisalignedbb.field_72340_a, axisalignedbb.field_72338_b, axisalignedbb.field_72339_c + d2, axisalignedbb.field_72336_d, axisalignedbb.field_72337_e, axisalignedbb.field_72339_c + d3);

        case SOUTH:
            return new AxisAlignedBB(axisalignedbb.field_72340_a, axisalignedbb.field_72338_b, axisalignedbb.field_72334_f + d2, axisalignedbb.field_72336_d, axisalignedbb.field_72337_e, axisalignedbb.field_72334_f + d3);
        }
    }

    private void func_190605_a(Entity entity, EnumFacing enumdirection, double d0) {
        AxisAlignedBB axisalignedbb = entity.func_174813_aQ();
        AxisAlignedBB axisalignedbb1 = Block.field_185505_j.func_186670_a(this.field_174879_c);

        if (axisalignedbb.func_72326_a(axisalignedbb1)) {
            EnumFacing enumdirection1 = enumdirection.func_176734_d();
            double d1 = this.func_190612_a(axisalignedbb1, enumdirection1, axisalignedbb) + 0.01D;
            double d2 = this.func_190612_a(axisalignedbb1, enumdirection1, axisalignedbb.func_191500_a(axisalignedbb1)) + 0.01D;

            if (Math.abs(d1 - d2) < 0.01D) {
                d1 = Math.min(d1, d0) + 0.01D;
                TileEntityPiston.field_190613_i.set(enumdirection);
                entity.func_70091_d(MoverType.PISTON, d1 * enumdirection1.func_82601_c(), d1 * enumdirection1.func_96559_d(), d1 * enumdirection1.func_82599_e());
                TileEntityPiston.field_190613_i.set(null);
            }
        }

    }

    private static double func_190611_b(AxisAlignedBB axisalignedbb, EnumFacing enumdirection, AxisAlignedBB axisalignedbb1) {
        return enumdirection.func_176743_c() == EnumFacing.AxisDirection.POSITIVE ? axisalignedbb.field_72336_d - axisalignedbb1.field_72340_a : axisalignedbb1.field_72336_d - axisalignedbb.field_72340_a;
    }

    private static double func_190608_c(AxisAlignedBB axisalignedbb, EnumFacing enumdirection, AxisAlignedBB axisalignedbb1) {
        return enumdirection.func_176743_c() == EnumFacing.AxisDirection.POSITIVE ? axisalignedbb.field_72337_e - axisalignedbb1.field_72338_b : axisalignedbb1.field_72337_e - axisalignedbb.field_72338_b;
    }

    private static double func_190604_d(AxisAlignedBB axisalignedbb, EnumFacing enumdirection, AxisAlignedBB axisalignedbb1) {
        return enumdirection.func_176743_c() == EnumFacing.AxisDirection.POSITIVE ? axisalignedbb.field_72334_f - axisalignedbb1.field_72339_c : axisalignedbb1.field_72334_f - axisalignedbb.field_72339_c;
    }

    public void func_145866_f() {
        if (this.field_145870_n < 1.0F && this.field_145850_b != null) {
            this.field_145873_m = 1.0F;
            this.field_145870_n = this.field_145873_m;
            this.field_145850_b.func_175713_t(this.field_174879_c);
            this.func_145843_s();
            if (this.field_145850_b.func_180495_p(this.field_174879_c).func_177230_c() == Blocks.field_180384_M) {
                this.field_145850_b.func_180501_a(this.field_174879_c, this.field_174932_a, 3);
                this.field_145850_b.func_190524_a(this.field_174879_c, this.field_174932_a.func_177230_c(), this.field_174879_c);
            }
        }

    }

    @Override
    public void func_73660_a() {
        this.field_145870_n = this.field_145873_m;
        if (this.field_145870_n >= 1.0F) {
            this.field_145850_b.func_175713_t(this.field_174879_c);
            this.func_145843_s();
            if (this.field_145850_b.func_180495_p(this.field_174879_c).func_177230_c() == Blocks.field_180384_M) {
                this.field_145850_b.func_180501_a(this.field_174879_c, this.field_174932_a, 3);
                this.field_145850_b.func_190524_a(this.field_174879_c, this.field_174932_a.func_177230_c(), this.field_174879_c);
            }

        } else {
            float f = this.field_145873_m + 0.5F;

            this.func_184322_i(f);
            this.field_145873_m = f;
            if (this.field_145873_m >= 1.0F) {
                this.field_145873_m = 1.0F;
            }

        }
    }

    public static void func_189685_a(DataFixer dataconvertermanager) {}

    @Override
    public void func_145839_a(NBTTagCompound nbttagcompound) {
        super.func_145839_a(nbttagcompound);
        this.field_174932_a = Block.func_149729_e(nbttagcompound.func_74762_e("blockId")).func_176203_a(nbttagcompound.func_74762_e("blockData"));
        this.field_174931_f = EnumFacing.func_82600_a(nbttagcompound.func_74762_e("facing"));
        this.field_145873_m = nbttagcompound.func_74760_g("progress");
        this.field_145870_n = this.field_145873_m;
        this.field_145875_k = nbttagcompound.func_74767_n("extending");
        this.field_145872_l = nbttagcompound.func_74767_n("source");
    }

    @Override
    public NBTTagCompound func_189515_b(NBTTagCompound nbttagcompound) {
        super.func_189515_b(nbttagcompound);
        nbttagcompound.func_74768_a("blockId", Block.func_149682_b(this.field_174932_a.func_177230_c()));
        nbttagcompound.func_74768_a("blockData", this.field_174932_a.func_177230_c().func_176201_c(this.field_174932_a));
        nbttagcompound.func_74768_a("facing", this.field_174931_f.func_176745_a());
        nbttagcompound.func_74776_a("progress", this.field_145870_n);
        nbttagcompound.func_74757_a("extending", this.field_145875_k);
        nbttagcompound.func_74757_a("source", this.field_145872_l);
        return nbttagcompound;
    }

    public void func_190609_a(World world, BlockPos blockposition, AxisAlignedBB axisalignedbb, List<AxisAlignedBB> list, @Nullable Entity entity) {
        if (!this.field_145875_k && this.field_145872_l) {
            this.field_174932_a.func_177226_a(BlockPistonBase.field_176320_b, Boolean.valueOf(true)).func_185908_a(world, blockposition, axisalignedbb, list, entity, false);
        }

        EnumFacing enumdirection = TileEntityPiston.field_190613_i.get();

        if (this.field_145873_m >= 1.0D || enumdirection != (this.field_145875_k ? this.field_174931_f : this.field_174931_f.func_176734_d())) {
            int i = list.size();
            IBlockState iblockdata;

            if (this.func_145867_d()) {
                iblockdata = Blocks.field_150332_K.func_176223_P().func_177226_a(BlockPistonExtension.field_176387_N, this.field_174931_f).func_177226_a(BlockPistonExtension.field_176327_M, Boolean.valueOf(this.field_145875_k != 1.0F - this.field_145873_m < 0.25F));
            } else {
                iblockdata = this.field_174932_a;
            }

            float f = this.func_184320_e(this.field_145873_m);
            double d0 = this.field_174931_f.func_82601_c() * f;
            double d1 = this.field_174931_f.func_96559_d() * f;
            double d2 = this.field_174931_f.func_82599_e() * f;

            iblockdata.func_185908_a(world, blockposition, axisalignedbb.func_72317_d(-d0, -d1, -d2), list, entity, true);

            for (int j = i; j < list.size(); ++j) {
                list.set(j, list.get(j).func_72317_d(d0, d1, d2));
            }

        }
    }
}
