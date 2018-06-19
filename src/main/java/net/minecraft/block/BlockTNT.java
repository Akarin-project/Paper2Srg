package net.minecraft.block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;


public class BlockTNT extends Block {

    public static final PropertyBool field_176246_a = PropertyBool.func_177716_a("explode");

    public BlockTNT() {
        super(Material.field_151590_u);
        this.func_180632_j(this.field_176227_L.func_177621_b().func_177226_a(BlockTNT.field_176246_a, Boolean.valueOf(false)));
        this.func_149647_a(CreativeTabs.field_78028_d);
    }

    public void func_176213_c(World world, BlockPos blockposition, IBlockState iblockdata) {
        super.func_176213_c(world, blockposition, iblockdata);
        if (world.func_175640_z(blockposition)) {
            this.func_176206_d(world, blockposition, iblockdata.func_177226_a(BlockTNT.field_176246_a, Boolean.valueOf(true)));
            world.func_175698_g(blockposition);
        }

    }

    public void func_189540_a(IBlockState iblockdata, World world, BlockPos blockposition, Block block, BlockPos blockposition1) {
        if (world.func_175640_z(blockposition)) {
            this.func_176206_d(world, blockposition, iblockdata.func_177226_a(BlockTNT.field_176246_a, Boolean.valueOf(true)));
            world.func_175698_g(blockposition);
        }

    }

    public void func_180652_a(World world, BlockPos blockposition, Explosion explosion) {
        if (!world.field_72995_K) {
            EntityTNTPrimed entitytntprimed = new EntityTNTPrimed(world, (double) ((float) blockposition.func_177958_n() + 0.5F), (double) blockposition.func_177956_o(), (double) ((float) blockposition.func_177952_p() + 0.5F), explosion.func_94613_c());

            entitytntprimed.func_184534_a((short) (world.field_73012_v.nextInt(entitytntprimed.func_184536_l() / 4) + entitytntprimed.func_184536_l() / 8));
            world.func_72838_d(entitytntprimed);
        }
    }

    public void func_176206_d(World world, BlockPos blockposition, IBlockState iblockdata) {
        this.func_180692_a(world, blockposition, iblockdata, (EntityLivingBase) null);
    }

    public void func_180692_a(World world, BlockPos blockposition, IBlockState iblockdata, EntityLivingBase entityliving) {
        if (!world.field_72995_K) {
            if (((Boolean) iblockdata.func_177229_b(BlockTNT.field_176246_a)).booleanValue()) {
                EntityTNTPrimed entitytntprimed = new EntityTNTPrimed(world, (double) ((float) blockposition.func_177958_n() + 0.5F), (double) blockposition.func_177956_o(), (double) ((float) blockposition.func_177952_p() + 0.5F), entityliving);

                world.func_72838_d(entitytntprimed);
                world.func_184148_a((EntityPlayer) null, entitytntprimed.field_70165_t, entitytntprimed.field_70163_u, entitytntprimed.field_70161_v, SoundEvents.field_187904_gd, SoundCategory.BLOCKS, 1.0F, 1.0F);
            }

        }
    }

    public boolean func_180639_a(World world, BlockPos blockposition, IBlockState iblockdata, EntityPlayer entityhuman, EnumHand enumhand, EnumFacing enumdirection, float f, float f1, float f2) {
        ItemStack itemstack = entityhuman.func_184586_b(enumhand);

        if (!itemstack.func_190926_b() && (itemstack.func_77973_b() == Items.field_151033_d || itemstack.func_77973_b() == Items.field_151059_bz)) {
            this.func_180692_a(world, blockposition, iblockdata.func_177226_a(BlockTNT.field_176246_a, Boolean.valueOf(true)), (EntityLivingBase) entityhuman);
            world.func_180501_a(blockposition, Blocks.field_150350_a.func_176223_P(), 11);
            if (itemstack.func_77973_b() == Items.field_151033_d) {
                itemstack.func_77972_a(1, entityhuman);
            } else if (!entityhuman.field_71075_bZ.field_75098_d) {
                itemstack.func_190918_g(1);
            }

            return true;
        } else {
            return super.func_180639_a(world, blockposition, iblockdata, entityhuman, enumhand, enumdirection, f, f1, f2);
        }
    }

    public void func_180634_a(World world, BlockPos blockposition, IBlockState iblockdata, Entity entity) {
        if (!world.field_72995_K && entity instanceof EntityArrow) {
            EntityArrow entityarrow = (EntityArrow) entity;

            if (entityarrow.func_70027_ad()) {
                // CraftBukkit start
                if (org.bukkit.craftbukkit.event.CraftEventFactory.callEntityChangeBlockEvent(entityarrow, blockposition, Blocks.field_150350_a, 0).isCancelled()) {
                    return;
                }
                // CraftBukkit end
                this.func_180692_a(world, blockposition, world.func_180495_p(blockposition).func_177226_a(BlockTNT.field_176246_a, Boolean.valueOf(true)), entityarrow.field_70250_c instanceof EntityLivingBase ? (EntityLivingBase) entityarrow.field_70250_c : null);
                world.func_175698_g(blockposition);
            }
        }

    }

    public boolean func_149659_a(Explosion explosion) {
        return false;
    }

    public IBlockState func_176203_a(int i) {
        return this.func_176223_P().func_177226_a(BlockTNT.field_176246_a, Boolean.valueOf((i & 1) > 0));
    }

    public int func_176201_c(IBlockState iblockdata) {
        return ((Boolean) iblockdata.func_177229_b(BlockTNT.field_176246_a)).booleanValue() ? 1 : 0;
    }

    protected BlockStateContainer func_180661_e() {
        return new BlockStateContainer(this, new IProperty[] { BlockTNT.field_176246_a});
    }
}
