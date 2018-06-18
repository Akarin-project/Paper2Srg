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
        super(Material.ROCK);
        this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
        this.setLightLevel(0.2F);
        this.setTickRandomly(true);
    }

    public MapColor getMapColor(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        return MapColor.NETHERRACK;
    }

    public void onEntityWalk(World world, BlockPos blockposition, Entity entity) {
        if (!entity.isImmuneToFire() && entity instanceof EntityLivingBase && !EnchantmentHelper.hasFrostWalkerEnchantment((EntityLivingBase) entity)) {
            org.bukkit.craftbukkit.event.CraftEventFactory.blockDamage = world.getWorld().getBlockAt(blockposition.getX(), blockposition.getY(), blockposition.getZ()); // CraftBukkit
            entity.attackEntityFrom(DamageSource.HOT_FLOOR, 1.0F);
            org.bukkit.craftbukkit.event.CraftEventFactory.blockDamage = null; // CraftBukkit
        }

        super.onEntityWalk(world, blockposition, entity);
    }

    public void updateTick(World world, BlockPos blockposition, IBlockState iblockdata, Random random) {
        BlockPos blockposition1 = blockposition.up();
        IBlockState iblockdata1 = world.getBlockState(blockposition1);

        if (iblockdata1.getBlock() == Blocks.WATER || iblockdata1.getBlock() == Blocks.FLOWING_WATER) {
            world.setBlockToAir(blockposition1);
            world.playSound((EntityPlayer) null, blockposition, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.5F, 2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);
            if (world instanceof WorldServer) {
                ((WorldServer) world).spawnParticle(EnumParticleTypes.SMOKE_LARGE, (double) blockposition1.getX() + 0.5D, (double) blockposition1.getY() + 0.25D, (double) blockposition1.getZ() + 0.5D, 8, 0.5D, 0.25D, 0.5D, 0.0D, new int[0]);
            }
        }

    }

    public boolean canEntitySpawn(IBlockState iblockdata, Entity entity) {
        return entity.isImmuneToFire();
    }
}
