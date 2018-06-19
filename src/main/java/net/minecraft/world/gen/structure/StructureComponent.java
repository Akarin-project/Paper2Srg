package net.minecraft.world.gen.structure;

import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;

import net.minecraft.block.BlockDispenser;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.TemplateManager;

public abstract class StructureComponent {

    protected StructureBoundingBox field_74887_e;
    @Nullable
    private EnumFacing field_74885_f;
    private Mirror field_186168_b;
    private Rotation field_186169_c;
    protected int field_74886_g;

    public StructureComponent() {}

    protected StructureComponent(int i) {
        this.field_74886_g = i;
    }

    public final NBTTagCompound func_143010_b() {
        NBTTagCompound nbttagcompound = new NBTTagCompound();

        nbttagcompound.func_74778_a("id", MapGenStructureIO.func_143036_a(this));
        nbttagcompound.func_74782_a("BB", this.field_74887_e.func_151535_h());
        EnumFacing enumdirection = this.func_186165_e();

        nbttagcompound.func_74768_a("O", enumdirection == null ? -1 : enumdirection.func_176736_b());
        nbttagcompound.func_74768_a("GD", this.field_74886_g);
        this.func_143012_a(nbttagcompound);
        return nbttagcompound;
    }

    protected abstract void func_143012_a(NBTTagCompound nbttagcompound);

    public void func_143009_a(World world, NBTTagCompound nbttagcompound) {
        if (nbttagcompound.func_74764_b("BB")) {
            this.field_74887_e = new StructureBoundingBox(nbttagcompound.func_74759_k("BB"));
        }

        int i = nbttagcompound.func_74762_e("O");

        this.func_186164_a(i == -1 ? null : EnumFacing.func_176731_b(i));
        this.field_74886_g = nbttagcompound.func_74762_e("GD");
        this.func_143011_b(nbttagcompound, world.func_72860_G().func_186340_h());
    }

    protected abstract void func_143011_b(NBTTagCompound nbttagcompound, TemplateManager definedstructuremanager);

    public void func_74861_a(StructureComponent structurepiece, List<StructureComponent> list, Random random) {}

    public abstract boolean func_74875_a(World world, Random random, StructureBoundingBox structureboundingbox);

    public StructureBoundingBox getBoundingBox() { return func_74874_b(); } // Paper - OBFHELPER
    public StructureBoundingBox func_74874_b() {
        return this.field_74887_e;
    }

    public int func_74877_c() {
        return this.field_74886_g;
    }

    public static StructureComponent func_74883_a(List<StructureComponent> list, StructureBoundingBox structureboundingbox) {
        Iterator iterator = list.iterator();

        StructureComponent structurepiece;

        do {
            if (!iterator.hasNext()) {
                return null;
            }

            structurepiece = (StructureComponent) iterator.next();
        } while (structurepiece.func_74874_b() == null || !structurepiece.func_74874_b().func_78884_a(structureboundingbox));

        return structurepiece;
    }

    protected boolean func_74860_a(World world, StructureBoundingBox structureboundingbox) {
        int i = Math.max(this.field_74887_e.field_78897_a - 1, structureboundingbox.field_78897_a);
        int j = Math.max(this.field_74887_e.field_78895_b - 1, structureboundingbox.field_78895_b);
        int k = Math.max(this.field_74887_e.field_78896_c - 1, structureboundingbox.field_78896_c);
        int l = Math.min(this.field_74887_e.field_78893_d + 1, structureboundingbox.field_78893_d);
        int i1 = Math.min(this.field_74887_e.field_78894_e + 1, structureboundingbox.field_78894_e);
        int j1 = Math.min(this.field_74887_e.field_78892_f + 1, structureboundingbox.field_78892_f);
        BlockPos.MutableBlockPos blockposition_mutableblockposition = new BlockPos.MutableBlockPos();

        int k1;
        int l1;

        for (k1 = i; k1 <= l; ++k1) {
            for (l1 = k; l1 <= j1; ++l1) {
                if (world.func_180495_p(blockposition_mutableblockposition.func_181079_c(k1, j, l1)).func_185904_a().func_76224_d()) {
                    return true;
                }

                if (world.func_180495_p(blockposition_mutableblockposition.func_181079_c(k1, i1, l1)).func_185904_a().func_76224_d()) {
                    return true;
                }
            }
        }

        for (k1 = i; k1 <= l; ++k1) {
            for (l1 = j; l1 <= i1; ++l1) {
                if (world.func_180495_p(blockposition_mutableblockposition.func_181079_c(k1, l1, k)).func_185904_a().func_76224_d()) {
                    return true;
                }

                if (world.func_180495_p(blockposition_mutableblockposition.func_181079_c(k1, l1, j1)).func_185904_a().func_76224_d()) {
                    return true;
                }
            }
        }

        for (k1 = k; k1 <= j1; ++k1) {
            for (l1 = j; l1 <= i1; ++l1) {
                if (world.func_180495_p(blockposition_mutableblockposition.func_181079_c(i, l1, k1)).func_185904_a().func_76224_d()) {
                    return true;
                }

                if (world.func_180495_p(blockposition_mutableblockposition.func_181079_c(l, l1, k1)).func_185904_a().func_76224_d()) {
                    return true;
                }
            }
        }

        return false;
    }

    protected int func_74865_a(int i, int j) {
        EnumFacing enumdirection = this.func_186165_e();

        if (enumdirection == null) {
            return i;
        } else {
            switch (enumdirection) {
            case NORTH:
            case SOUTH:
                return this.field_74887_e.field_78897_a + i;

            case WEST:
                return this.field_74887_e.field_78893_d - j;

            case EAST:
                return this.field_74887_e.field_78897_a + j;

            default:
                return i;
            }
        }
    }

    protected int func_74862_a(int i) {
        return this.func_186165_e() == null ? i : i + this.field_74887_e.field_78895_b;
    }

    protected int func_74873_b(int i, int j) {
        EnumFacing enumdirection = this.func_186165_e();

        if (enumdirection == null) {
            return j;
        } else {
            switch (enumdirection) {
            case NORTH:
                return this.field_74887_e.field_78892_f - j;

            case SOUTH:
                return this.field_74887_e.field_78896_c + j;

            case WEST:
            case EAST:
                return this.field_74887_e.field_78896_c + i;

            default:
                return j;
            }
        }
    }

    protected void func_175811_a(World world, IBlockState iblockdata, int i, int j, int k, StructureBoundingBox structureboundingbox) {
        BlockPos blockposition = new BlockPos(this.func_74865_a(i, k), this.func_74862_a(j), this.func_74873_b(i, k));

        if (structureboundingbox.func_175898_b((Vec3i) blockposition)) {
            if (this.field_186168_b != Mirror.NONE) {
                iblockdata = iblockdata.func_185902_a(this.field_186168_b);
            }

            if (this.field_186169_c != Rotation.NONE) {
                iblockdata = iblockdata.func_185907_a(this.field_186169_c);
            }

            world.func_180501_a(blockposition, iblockdata, 2);
        }
    }

    protected IBlockState func_175807_a(World world, int i, int j, int k, StructureBoundingBox structureboundingbox) {
        int l = this.func_74865_a(i, k);
        int i1 = this.func_74862_a(j);
        int j1 = this.func_74873_b(i, k);
        BlockPos blockposition = new BlockPos(l, i1, j1);

        return !structureboundingbox.func_175898_b((Vec3i) blockposition) ? Blocks.field_150350_a.func_176223_P() : world.func_180495_p(blockposition);
    }

    protected int func_189916_b(World world, int i, int j, int k, StructureBoundingBox structureboundingbox) {
        int l = this.func_74865_a(i, k);
        int i1 = this.func_74862_a(j + 1);
        int j1 = this.func_74873_b(i, k);
        BlockPos blockposition = new BlockPos(l, i1, j1);

        return !structureboundingbox.func_175898_b((Vec3i) blockposition) ? EnumSkyBlock.SKY.field_77198_c : world.func_175642_b(EnumSkyBlock.SKY, blockposition);
    }

    protected void func_74878_a(World world, StructureBoundingBox structureboundingbox, int i, int j, int k, int l, int i1, int j1) {
        for (int k1 = j; k1 <= i1; ++k1) {
            for (int l1 = i; l1 <= l; ++l1) {
                for (int i2 = k; i2 <= j1; ++i2) {
                    this.func_175811_a(world, Blocks.field_150350_a.func_176223_P(), l1, k1, i2, structureboundingbox);
                }
            }
        }

    }

    protected void func_175804_a(World world, StructureBoundingBox structureboundingbox, int i, int j, int k, int l, int i1, int j1, IBlockState iblockdata, IBlockState iblockdata1, boolean flag) {
        for (int k1 = j; k1 <= i1; ++k1) {
            for (int l1 = i; l1 <= l; ++l1) {
                for (int i2 = k; i2 <= j1; ++i2) {
                    if (!flag || this.func_175807_a(world, l1, k1, i2, structureboundingbox).func_185904_a() != Material.field_151579_a) {
                        if (k1 != j && k1 != i1 && l1 != i && l1 != l && i2 != k && i2 != j1) {
                            this.func_175811_a(world, iblockdata1, l1, k1, i2, structureboundingbox);
                        } else {
                            this.func_175811_a(world, iblockdata, l1, k1, i2, structureboundingbox);
                        }
                    }
                }
            }
        }

    }

    protected void func_74882_a(World world, StructureBoundingBox structureboundingbox, int i, int j, int k, int l, int i1, int j1, boolean flag, Random random, StructureComponent.BlockSelector structurepiece_structurepieceblockselector) {
        for (int k1 = j; k1 <= i1; ++k1) {
            for (int l1 = i; l1 <= l; ++l1) {
                for (int i2 = k; i2 <= j1; ++i2) {
                    if (!flag || this.func_175807_a(world, l1, k1, i2, structureboundingbox).func_185904_a() != Material.field_151579_a) {
                        structurepiece_structurepieceblockselector.func_75062_a(random, l1, k1, i2, k1 == j || k1 == i1 || l1 == i || l1 == l || i2 == k || i2 == j1);
                        this.func_175811_a(world, structurepiece_structurepieceblockselector.func_180780_a(), l1, k1, i2, structureboundingbox);
                    }
                }
            }
        }

    }

    protected void func_189914_a(World world, StructureBoundingBox structureboundingbox, Random random, float f, int i, int j, int k, int l, int i1, int j1, IBlockState iblockdata, IBlockState iblockdata1, boolean flag, int k1) {
        for (int l1 = j; l1 <= i1; ++l1) {
            for (int i2 = i; i2 <= l; ++i2) {
                for (int j2 = k; j2 <= j1; ++j2) {
                    if (random.nextFloat() <= f && (!flag || this.func_175807_a(world, i2, l1, j2, structureboundingbox).func_185904_a() != Material.field_151579_a) && (k1 <= 0 || this.func_189916_b(world, i2, l1, j2, structureboundingbox) < k1)) {
                        if (l1 != j && l1 != i1 && i2 != i && i2 != l && j2 != k && j2 != j1) {
                            this.func_175811_a(world, iblockdata1, i2, l1, j2, structureboundingbox);
                        } else {
                            this.func_175811_a(world, iblockdata, i2, l1, j2, structureboundingbox);
                        }
                    }
                }
            }
        }

    }

    protected void func_175809_a(World world, StructureBoundingBox structureboundingbox, Random random, float f, int i, int j, int k, IBlockState iblockdata) {
        if (random.nextFloat() < f) {
            this.func_175811_a(world, iblockdata, i, j, k, structureboundingbox);
        }

    }

    protected void func_180777_a(World world, StructureBoundingBox structureboundingbox, int i, int j, int k, int l, int i1, int j1, IBlockState iblockdata, boolean flag) {
        float f = (float) (l - i + 1);
        float f1 = (float) (i1 - j + 1);
        float f2 = (float) (j1 - k + 1);
        float f3 = (float) i + f / 2.0F;
        float f4 = (float) k + f2 / 2.0F;

        for (int k1 = j; k1 <= i1; ++k1) {
            float f5 = (float) (k1 - j) / f1;

            for (int l1 = i; l1 <= l; ++l1) {
                float f6 = ((float) l1 - f3) / (f * 0.5F);

                for (int i2 = k; i2 <= j1; ++i2) {
                    float f7 = ((float) i2 - f4) / (f2 * 0.5F);

                    if (!flag || this.func_175807_a(world, l1, k1, i2, structureboundingbox).func_185904_a() != Material.field_151579_a) {
                        float f8 = f6 * f6 + f5 * f5 + f7 * f7;

                        if (f8 <= 1.05F) {
                            this.func_175811_a(world, iblockdata, l1, k1, i2, structureboundingbox);
                        }
                    }
                }
            }
        }

    }

    protected void func_74871_b(World world, int i, int j, int k, StructureBoundingBox structureboundingbox) {
        BlockPos blockposition = new BlockPos(this.func_74865_a(i, k), this.func_74862_a(j), this.func_74873_b(i, k));

        if (structureboundingbox.func_175898_b((Vec3i) blockposition)) {
            while (!world.func_175623_d(blockposition) && blockposition.func_177956_o() < 255) {
                world.func_180501_a(blockposition, Blocks.field_150350_a.func_176223_P(), 2);
                blockposition = blockposition.func_177984_a();
            }

        }
    }

    protected void func_175808_b(World world, IBlockState iblockdata, int i, int j, int k, StructureBoundingBox structureboundingbox) {
        int l = this.func_74865_a(i, k);
        int i1 = this.func_74862_a(j);
        int j1 = this.func_74873_b(i, k);

        if (structureboundingbox.func_175898_b((Vec3i) (new BlockPos(l, i1, j1)))) {
            while ((world.func_175623_d(new BlockPos(l, i1, j1)) || world.func_180495_p(new BlockPos(l, i1, j1)).func_185904_a().func_76224_d()) && i1 > 1) {
                world.func_180501_a(new BlockPos(l, i1, j1), iblockdata, 2);
                --i1;
            }

        }
    }

    protected boolean func_186167_a(World world, StructureBoundingBox structureboundingbox, Random random, int i, int j, int k, ResourceLocation minecraftkey) {
        BlockPos blockposition = new BlockPos(this.func_74865_a(i, k), this.func_74862_a(j), this.func_74873_b(i, k));

        return this.func_191080_a(world, structureboundingbox, random, blockposition, minecraftkey, (IBlockState) null);
    }

    protected boolean func_191080_a(World world, StructureBoundingBox structureboundingbox, Random random, BlockPos blockposition, ResourceLocation minecraftkey, @Nullable IBlockState iblockdata) {
        if (structureboundingbox.func_175898_b((Vec3i) blockposition) && world.func_180495_p(blockposition).func_177230_c() != Blocks.field_150486_ae) {
            if (iblockdata == null) {
                iblockdata = Blocks.field_150486_ae.func_176458_f(world, blockposition, Blocks.field_150486_ae.func_176223_P());
            }

            world.func_180501_a(blockposition, iblockdata, 2);
            TileEntity tileentity = world.func_175625_s(blockposition);

            if (tileentity instanceof TileEntityChest) {
                ((TileEntityChest) tileentity).func_189404_a(minecraftkey, random.nextLong());
            }

            return true;
        } else {
            return false;
        }
    }

    protected boolean func_189419_a(World world, StructureBoundingBox structureboundingbox, Random random, int i, int j, int k, EnumFacing enumdirection, ResourceLocation minecraftkey) {
        BlockPos blockposition = new BlockPos(this.func_74865_a(i, k), this.func_74862_a(j), this.func_74873_b(i, k));

        if (structureboundingbox.func_175898_b((Vec3i) blockposition) && world.func_180495_p(blockposition).func_177230_c() != Blocks.field_150367_z) {
            this.func_175811_a(world, Blocks.field_150367_z.func_176223_P().func_177226_a(BlockDispenser.field_176441_a, enumdirection), i, j, k, structureboundingbox);
            TileEntity tileentity = world.func_175625_s(blockposition);

            if (tileentity instanceof TileEntityDispenser) {
                ((TileEntityDispenser) tileentity).func_189404_a(minecraftkey, random.nextLong());
            }

            return true;
        } else {
            return false;
        }
    }

    protected void func_189915_a(World world, StructureBoundingBox structureboundingbox, Random random, int i, int j, int k, EnumFacing enumdirection, BlockDoor blockdoor) {
        this.func_175811_a(world, blockdoor.func_176223_P().func_177226_a(BlockDoor.field_176520_a, enumdirection), i, j, k, structureboundingbox);
        this.func_175811_a(world, blockdoor.func_176223_P().func_177226_a(BlockDoor.field_176520_a, enumdirection).func_177226_a(BlockDoor.field_176523_O, BlockDoor.EnumDoorHalf.UPPER), i, j + 1, k, structureboundingbox);
    }

    public void func_181138_a(int i, int j, int k) {
        this.field_74887_e.func_78886_a(i, j, k);
    }

    @Nullable
    public EnumFacing func_186165_e() {
        return this.field_74885_f;
    }

    public void func_186164_a(@Nullable EnumFacing enumdirection) {
        this.field_74885_f = enumdirection;
        if (enumdirection == null) {
            this.field_186169_c = Rotation.NONE;
            this.field_186168_b = Mirror.NONE;
        } else {
            switch (enumdirection) {
            case SOUTH:
                this.field_186168_b = Mirror.LEFT_RIGHT;
                this.field_186169_c = Rotation.NONE;
                break;

            case WEST:
                this.field_186168_b = Mirror.LEFT_RIGHT;
                this.field_186169_c = Rotation.CLOCKWISE_90;
                break;

            case EAST:
                this.field_186168_b = Mirror.NONE;
                this.field_186169_c = Rotation.CLOCKWISE_90;
                break;

            default:
                this.field_186168_b = Mirror.NONE;
                this.field_186169_c = Rotation.NONE;
            }
        }

    }

    public abstract static class BlockSelector {

        protected IBlockState field_151562_a;

        protected BlockSelector() {
            this.field_151562_a = Blocks.field_150350_a.func_176223_P();
        }

        public abstract void func_75062_a(Random random, int i, int j, int k, boolean flag);

        public IBlockState func_180780_a() {
            return this.field_151562_a;
        }
    }
}
