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

    public static final PropertyBool EXPLODE = PropertyBool.create("explode");

    public BlockTNT() {
        super(Material.TNT);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockTNT.EXPLODE, Boolean.valueOf(false)));
        this.setCreativeTab(CreativeTabs.REDSTONE);
    }

    public void onBlockAdded(World world, BlockPos blockposition, IBlockState iblockdata) {
        super.onBlockAdded(world, blockposition, iblockdata);
        if (world.isBlockPowered(blockposition)) {
            this.onBlockDestroyedByPlayer(world, blockposition, iblockdata.withProperty(BlockTNT.EXPLODE, Boolean.valueOf(true)));
            world.setBlockToAir(blockposition);
        }

    }

    public void neighborChanged(IBlockState iblockdata, World world, BlockPos blockposition, Block block, BlockPos blockposition1) {
        if (world.isBlockPowered(blockposition)) {
            this.onBlockDestroyedByPlayer(world, blockposition, iblockdata.withProperty(BlockTNT.EXPLODE, Boolean.valueOf(true)));
            world.setBlockToAir(blockposition);
        }

    }

    public void onBlockDestroyedByExplosion(World world, BlockPos blockposition, Explosion explosion) {
        if (!world.isRemote) {
            EntityTNTPrimed entitytntprimed = new EntityTNTPrimed(world, (double) ((float) blockposition.getX() + 0.5F), (double) blockposition.getY(), (double) ((float) blockposition.getZ() + 0.5F), explosion.getExplosivePlacedBy());

            entitytntprimed.setFuse((short) (world.rand.nextInt(entitytntprimed.getFuse() / 4) + entitytntprimed.getFuse() / 8));
            world.spawnEntity(entitytntprimed);
        }
    }

    public void onBlockDestroyedByPlayer(World world, BlockPos blockposition, IBlockState iblockdata) {
        this.explode(world, blockposition, iblockdata, (EntityLivingBase) null);
    }

    public void explode(World world, BlockPos blockposition, IBlockState iblockdata, EntityLivingBase entityliving) {
        if (!world.isRemote) {
            if (((Boolean) iblockdata.getValue(BlockTNT.EXPLODE)).booleanValue()) {
                EntityTNTPrimed entitytntprimed = new EntityTNTPrimed(world, (double) ((float) blockposition.getX() + 0.5F), (double) blockposition.getY(), (double) ((float) blockposition.getZ() + 0.5F), entityliving);

                world.spawnEntity(entitytntprimed);
                world.playSound((EntityPlayer) null, entitytntprimed.posX, entitytntprimed.posY, entitytntprimed.posZ, SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.BLOCKS, 1.0F, 1.0F);
            }

        }
    }

    public boolean onBlockActivated(World world, BlockPos blockposition, IBlockState iblockdata, EntityPlayer entityhuman, EnumHand enumhand, EnumFacing enumdirection, float f, float f1, float f2) {
        ItemStack itemstack = entityhuman.getHeldItem(enumhand);

        if (!itemstack.isEmpty() && (itemstack.getItem() == Items.FLINT_AND_STEEL || itemstack.getItem() == Items.FIRE_CHARGE)) {
            this.explode(world, blockposition, iblockdata.withProperty(BlockTNT.EXPLODE, Boolean.valueOf(true)), (EntityLivingBase) entityhuman);
            world.setBlockState(blockposition, Blocks.AIR.getDefaultState(), 11);
            if (itemstack.getItem() == Items.FLINT_AND_STEEL) {
                itemstack.damageItem(1, entityhuman);
            } else if (!entityhuman.capabilities.isCreativeMode) {
                itemstack.shrink(1);
            }

            return true;
        } else {
            return super.onBlockActivated(world, blockposition, iblockdata, entityhuman, enumhand, enumdirection, f, f1, f2);
        }
    }

    public void onEntityCollidedWithBlock(World world, BlockPos blockposition, IBlockState iblockdata, Entity entity) {
        if (!world.isRemote && entity instanceof EntityArrow) {
            EntityArrow entityarrow = (EntityArrow) entity;

            if (entityarrow.isBurning()) {
                // CraftBukkit start
                if (org.bukkit.craftbukkit.event.CraftEventFactory.callEntityChangeBlockEvent(entityarrow, blockposition, Blocks.AIR, 0).isCancelled()) {
                    return;
                }
                // CraftBukkit end
                this.explode(world, blockposition, world.getBlockState(blockposition).withProperty(BlockTNT.EXPLODE, Boolean.valueOf(true)), entityarrow.shootingEntity instanceof EntityLivingBase ? (EntityLivingBase) entityarrow.shootingEntity : null);
                world.setBlockToAir(blockposition);
            }
        }

    }

    public boolean canDropFromExplosion(Explosion explosion) {
        return false;
    }

    public IBlockState getStateFromMeta(int i) {
        return this.getDefaultState().withProperty(BlockTNT.EXPLODE, Boolean.valueOf((i & 1) > 0));
    }

    public int getMetaFromState(IBlockState iblockdata) {
        return ((Boolean) iblockdata.getValue(BlockTNT.EXPLODE)).booleanValue() ? 1 : 0;
    }

    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] { BlockTNT.EXPLODE});
    }
}
