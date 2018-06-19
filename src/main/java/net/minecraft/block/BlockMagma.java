package net.minecraft.block;

import java.util.Random;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class BlockMagma extends Block {

    public BlockMagma() {
        super(Material.field_151576_e);
        this.func_149647_a(CreativeTabs.field_78030_b);
        this.func_149715_a(0.2F);
        this.func_149675_a(true);
    }

    public MapColor func_180659_g(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        return MapColor.field_151655_K;
    }

    public void func_176199_a(World world, BlockPos blockposition, Entity entity) {
        if (!entity.func_70045_F() && entity instanceof EntityLivingBase && !EnchantmentHelper.func_189869_j((EntityLivingBase) entity)) {
            org.bukkit.craftbukkit.event.CraftEventFactory.blockDamage = world.getWorld().getBlockAt(blockposition.func_177958_n(), blockposition.func_177956_o(), blockposition.func_177952_p()); // CraftBukkit
            entity.func_70097_a(DamageSource.field_190095_e, 1.0F);
            org.bukkit.craftbukkit.event.CraftEventFactory.blockDamage = null; // CraftBukkit
        }

        super.func_176199_a(world, blockposition, entity);
    }

    public void func_180650_b(World world, BlockPos blockposition, IBlockState iblockdata, Random random) {
        BlockPos blockposition1 = blockposition.func_177984_a();
        IBlockState iblockdata1 = world.func_180495_p(blockposition1);

        if (iblockdata1.func_177230_c() == Blocks.field_150355_j || iblockdata1.func_177230_c() == Blocks.field_150358_i) {
            world.func_175698_g(blockposition1);
            world.func_184133_a((EntityPlayer) null, blockposition, SoundEvents.field_187646_bt, SoundCategory.BLOCKS, 0.5F, 2.6F + (world.field_73012_v.nextFloat() - world.field_73012_v.nextFloat()) * 0.8F);
            if (world instanceof WorldServer) {
                ((WorldServer) world).func_175739_a(EnumParticleTypes.SMOKE_LARGE, (double) blockposition1.func_177958_n() + 0.5D, (double) blockposition1.func_177956_o() + 0.25D, (double) blockposition1.func_177952_p() + 0.5D, 8, 0.5D, 0.25D, 0.5D, 0.0D, new int[0]);
            }
        }

    }

    public boolean func_189872_a(IBlockState iblockdata, Entity entity) {
        return entity.func_70045_F();
    }
}
