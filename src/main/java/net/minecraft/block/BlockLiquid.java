package net.minecraft.block;

import java.util.Iterator;
import java.util.Random;
import javax.annotation.Nullable;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public abstract class BlockLiquid extends Block {

    public static final PropertyInteger field_176367_b = PropertyInteger.func_177719_a("level", 0, 15);

    protected BlockLiquid(Material material) {
        super(material);
        this.func_180632_j(this.field_176227_L.func_177621_b().func_177226_a(BlockLiquid.field_176367_b, Integer.valueOf(0)));
        this.func_149675_a(true);
    }

    public AxisAlignedBB func_185496_a(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        return BlockLiquid.field_185505_j;
    }

    @Nullable
    public AxisAlignedBB func_180646_a(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        return BlockLiquid.field_185506_k;
    }

    public boolean func_176205_b(IBlockAccess iblockaccess, BlockPos blockposition) {
        return this.field_149764_J != Material.field_151587_i;
    }

    public static float func_149801_b(int i) {
        if (i >= 8) {
            i = 0;
        }

        return (float) (i + 1) / 9.0F;
    }

    protected int func_189542_i(IBlockState iblockdata) {
        return iblockdata.func_185904_a() == this.field_149764_J ? ((Integer) iblockdata.func_177229_b(BlockLiquid.field_176367_b)).intValue() : -1;
    }

    protected int func_189545_x(IBlockState iblockdata) {
        int i = this.func_189542_i(iblockdata);

        return i >= 8 ? 0 : i;
    }

    public boolean func_149686_d(IBlockState iblockdata) {
        return false;
    }

    public boolean func_149662_c(IBlockState iblockdata) {
        return false;
    }

    public boolean func_176209_a(IBlockState iblockdata, boolean flag) {
        return flag && ((Integer) iblockdata.func_177229_b(BlockLiquid.field_176367_b)).intValue() == 0;
    }

    private boolean func_176212_b(IBlockAccess iblockaccess, BlockPos blockposition, EnumFacing enumdirection) {
        IBlockState iblockdata = iblockaccess.func_180495_p(blockposition);
        Block block = iblockdata.func_177230_c();
        Material material = iblockdata.func_185904_a();

        if (material == this.field_149764_J) {
            return false;
        } else if (enumdirection == EnumFacing.UP) {
            return true;
        } else if (material == Material.field_151588_w) {
            return false;
        } else {
            boolean flag = func_193382_c(block) || block instanceof BlockStairs;

            return !flag && iblockdata.func_193401_d(iblockaccess, blockposition, enumdirection) == BlockFaceShape.SOLID;
        }
    }

    public EnumBlockRenderType func_149645_b(IBlockState iblockdata) {
        return EnumBlockRenderType.LIQUID;
    }

    public Item func_180660_a(IBlockState iblockdata, Random random, int i) {
        return Items.field_190931_a;
    }

    public int func_149745_a(Random random) {
        return 0;
    }

    protected Vec3d func_189543_a(IBlockAccess iblockaccess, BlockPos blockposition, IBlockState iblockdata) {
        double d0 = 0.0D;
        double d1 = 0.0D;
        double d2 = 0.0D;
        int i = this.func_189545_x(iblockdata);
        BlockPos.PooledMutableBlockPos blockposition_pooledblockposition = BlockPos.PooledMutableBlockPos.func_185346_s();
        Iterator iterator = EnumFacing.Plane.HORIZONTAL.iterator();

        while (iterator.hasNext()) {
            EnumFacing enumdirection = (EnumFacing) iterator.next();

            blockposition_pooledblockposition.func_189533_g(blockposition).func_189536_c(enumdirection);
            int j = this.func_189545_x(iblockaccess.func_180495_p(blockposition_pooledblockposition));
            int k;

            if (j < 0) {
                if (!iblockaccess.func_180495_p(blockposition_pooledblockposition).func_185904_a().func_76230_c()) {
                    j = this.func_189545_x(iblockaccess.func_180495_p(blockposition_pooledblockposition.func_177977_b()));
                    if (j >= 0) {
                        k = j - (i - 8);
                        d0 += (double) (enumdirection.func_82601_c() * k);
                        d1 += (double) (enumdirection.func_96559_d() * k);
                        d2 += (double) (enumdirection.func_82599_e() * k);
                    }
                }
            } else if (j >= 0) {
                k = j - i;
                d0 += (double) (enumdirection.func_82601_c() * k);
                d1 += (double) (enumdirection.func_96559_d() * k);
                d2 += (double) (enumdirection.func_82599_e() * k);
            }
        }

        Vec3d vec3d = new Vec3d(d0, d1, d2);

        if (((Integer) iblockdata.func_177229_b(BlockLiquid.field_176367_b)).intValue() >= 8) {
            Iterator iterator1 = EnumFacing.Plane.HORIZONTAL.iterator();

            while (iterator1.hasNext()) {
                EnumFacing enumdirection1 = (EnumFacing) iterator1.next();

                blockposition_pooledblockposition.func_189533_g(blockposition).func_189536_c(enumdirection1);
                if (this.func_176212_b(iblockaccess, (BlockPos) blockposition_pooledblockposition, enumdirection1) || this.func_176212_b(iblockaccess, blockposition_pooledblockposition.func_177984_a(), enumdirection1)) {
                    vec3d = vec3d.func_72432_b().func_72441_c(0.0D, -6.0D, 0.0D);
                    break;
                }
            }
        }

        blockposition_pooledblockposition.func_185344_t();
        return vec3d.func_72432_b();
    }

    public Vec3d func_176197_a(World world, BlockPos blockposition, Entity entity, Vec3d vec3d) {
        return vec3d.func_178787_e(this.func_189543_a((IBlockAccess) world, blockposition, world.func_180495_p(blockposition)));
    }

    public int func_149738_a(World world) {
        return this.field_149764_J == Material.field_151586_h ? 5 : (this.field_149764_J == Material.field_151587_i ? (world.field_73011_w.func_177495_o() ? 10 : 30) : 0);
    }

    public void func_176213_c(World world, BlockPos blockposition, IBlockState iblockdata) {
        this.func_176365_e(world, blockposition, iblockdata);
    }

    public void func_189540_a(IBlockState iblockdata, World world, BlockPos blockposition, Block block, BlockPos blockposition1) {
        this.func_176365_e(world, blockposition, iblockdata);
    }

    public boolean func_176365_e(World world, BlockPos blockposition, IBlockState iblockdata) {
        if (this.field_149764_J == Material.field_151587_i) {
            boolean flag = false;
            EnumFacing[] aenumdirection = EnumFacing.values();
            int i = aenumdirection.length;

            for (int j = 0; j < i; ++j) {
                EnumFacing enumdirection = aenumdirection[j];

                if (enumdirection != EnumFacing.DOWN && world.func_180495_p(blockposition.func_177972_a(enumdirection)).func_185904_a() == Material.field_151586_h) {
                    flag = true;
                    break;
                }
            }

            if (flag) {
                Integer integer = (Integer) iblockdata.func_177229_b(BlockLiquid.field_176367_b);

                if (integer.intValue() == 0) {
                    // CraftBukkit start
                    if (org.bukkit.craftbukkit.event.CraftEventFactory.handleBlockFormEvent(world, blockposition, Blocks.field_150343_Z.func_176223_P(), null)) {
                        this.func_180688_d(world, blockposition);
                    }
                    // CraftBukkit end
                    return true;
                }

                if (integer.intValue() <= 4) {
                    // CraftBukkit start
                    if (org.bukkit.craftbukkit.event.CraftEventFactory.handleBlockFormEvent(world, blockposition, Blocks.field_150347_e.func_176223_P(), null)) {
                        this.func_180688_d(world, blockposition);
                    }
                    // CraftBukkit end
                    return true;
                }
            }
        }

        return false;
    }

    protected void func_180688_d(World world, BlockPos blockposition) {
        double d0 = (double) blockposition.func_177958_n();
        double d1 = (double) blockposition.func_177956_o();
        double d2 = (double) blockposition.func_177952_p();

        world.func_184133_a((EntityPlayer) null, blockposition, SoundEvents.field_187659_cY, SoundCategory.BLOCKS, 0.5F, 2.6F + (world.field_73012_v.nextFloat() - world.field_73012_v.nextFloat()) * 0.8F);

        for (int i = 0; i < 8; ++i) {
            world.func_175688_a(EnumParticleTypes.SMOKE_LARGE, d0 + Math.random(), d1 + 1.2D, d2 + Math.random(), 0.0D, 0.0D, 0.0D, new int[0]);
        }

    }

    public IBlockState func_176203_a(int i) {
        return this.func_176223_P().func_177226_a(BlockLiquid.field_176367_b, Integer.valueOf(i));
    }

    public int func_176201_c(IBlockState iblockdata) {
        return ((Integer) iblockdata.func_177229_b(BlockLiquid.field_176367_b)).intValue();
    }

    protected BlockStateContainer func_180661_e() {
        return new BlockStateContainer(this, new IProperty[] { BlockLiquid.field_176367_b});
    }

    public static BlockDynamicLiquid func_176361_a(Material material) {
        if (material == Material.field_151586_h) {
            return Blocks.field_150358_i;
        } else if (material == Material.field_151587_i) {
            return Blocks.field_150356_k;
        } else {
            throw new IllegalArgumentException("Invalid material");
        }
    }

    public static BlockStaticLiquid func_176363_b(Material material) {
        if (material == Material.field_151586_h) {
            return Blocks.field_150355_j;
        } else if (material == Material.field_151587_i) {
            return Blocks.field_150353_l;
        } else {
            throw new IllegalArgumentException("Invalid material");
        }
    }

    public static float func_190973_f(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        int i = ((Integer) iblockdata.func_177229_b(BlockLiquid.field_176367_b)).intValue();

        return (i & 7) == 0 && iblockaccess.func_180495_p(blockposition.func_177984_a()).func_185904_a() == Material.field_151586_h ? 1.0F : 1.0F - func_149801_b(i);
    }

    public static float func_190972_g(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        return (float) blockposition.func_177956_o() + func_190973_f(iblockdata, iblockaccess, blockposition);
    }

    public BlockFaceShape func_193383_a(IBlockAccess iblockaccess, IBlockState iblockdata, BlockPos blockposition, EnumFacing enumdirection) {
        return BlockFaceShape.UNDEFINED;
    }
}
