package net.minecraft.block;

import java.util.Iterator;
import java.util.List;


import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.bukkit.event.entity.EntityInteractEvent;

public class BlockPressurePlate extends BlockBasePressurePlate {

    public static final PropertyBool POWERED = PropertyBool.create("powered");
    private final BlockPressurePlate.Sensitivity sensitivity;

    protected BlockPressurePlate(Material material, BlockPressurePlate.Sensitivity blockpressureplatebinary_enummobtype) {
        super(material);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockPressurePlate.POWERED, Boolean.valueOf(false)));
        this.sensitivity = blockpressureplatebinary_enummobtype;
    }

    protected int getRedstoneStrength(IBlockState iblockdata) {
        return ((Boolean) iblockdata.getValue(BlockPressurePlate.POWERED)).booleanValue() ? 15 : 0;
    }

    protected IBlockState setRedstoneStrength(IBlockState iblockdata, int i) {
        return iblockdata.withProperty(BlockPressurePlate.POWERED, Boolean.valueOf(i > 0));
    }

    protected void playClickOnSound(World world, BlockPos blockposition) {
        if (this.blockMaterial == Material.WOOD) {
            world.playSound((EntityPlayer) null, blockposition, SoundEvents.BLOCK_WOOD_PRESSPLATE_CLICK_ON, SoundCategory.BLOCKS, 0.3F, 0.8F);
        } else {
            world.playSound((EntityPlayer) null, blockposition, SoundEvents.BLOCK_STONE_PRESSPLATE_CLICK_ON, SoundCategory.BLOCKS, 0.3F, 0.6F);
        }

    }

    protected void playClickOffSound(World world, BlockPos blockposition) {
        if (this.blockMaterial == Material.WOOD) {
            world.playSound((EntityPlayer) null, blockposition, SoundEvents.BLOCK_WOOD_PRESSPLATE_CLICK_OFF, SoundCategory.BLOCKS, 0.3F, 0.7F);
        } else {
            world.playSound((EntityPlayer) null, blockposition, SoundEvents.BLOCK_STONE_PRESSPLATE_CLICK_OFF, SoundCategory.BLOCKS, 0.3F, 0.5F);
        }

    }

    protected int computeRedstoneStrength(World world, BlockPos blockposition) {
        AxisAlignedBB axisalignedbb = BlockPressurePlate.PRESSURE_AABB.offset(blockposition);
        List list;

        switch (this.sensitivity) {
        case EVERYTHING:
            list = world.getEntitiesWithinAABBExcludingEntity((Entity) null, axisalignedbb);
            break;

        case MOBS:
            list = world.getEntitiesWithinAABB(EntityLivingBase.class, axisalignedbb);
            break;

        default:
            return 0;
        }

        if (!list.isEmpty()) {
            Iterator iterator = list.iterator();

            while (iterator.hasNext()) {
                Entity entity = (Entity) iterator.next();

                // CraftBukkit start - Call interact event when turning on a pressure plate
                if (this.getRedstoneStrength(world.getBlockState(blockposition)) == 0) {
                    org.bukkit.World bworld = world.getWorld();
                    org.bukkit.plugin.PluginManager manager = world.getServer().getPluginManager();
                    org.bukkit.event.Cancellable cancellable;

                    if (entity instanceof EntityPlayer) {
                        cancellable = org.bukkit.craftbukkit.event.CraftEventFactory.callPlayerInteractEvent((EntityPlayer) entity, org.bukkit.event.block.Action.PHYSICAL, blockposition, null, null, null);
                    } else {
                        cancellable = new EntityInteractEvent(entity.getBukkitEntity(), bworld.getBlockAt(blockposition.getX(), blockposition.getY(), blockposition.getZ()));
                        manager.callEvent((EntityInteractEvent) cancellable);
                    }

                    // We only want to block turning the plate on if all events are cancelled
                    if (cancellable.isCancelled()) {
                        continue;
                    }
                }
                // CraftBukkit end

                if (!entity.doesEntityNotTriggerPressurePlate()) {
                    return 15;
                }
            }
        }

        return 0;
    }

    public IBlockState getStateFromMeta(int i) {
        return this.getDefaultState().withProperty(BlockPressurePlate.POWERED, Boolean.valueOf(i == 1));
    }

    public int getMetaFromState(IBlockState iblockdata) {
        return ((Boolean) iblockdata.getValue(BlockPressurePlate.POWERED)).booleanValue() ? 1 : 0;
    }

    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] { BlockPressurePlate.POWERED});
    }

    public static enum Sensitivity {

        EVERYTHING, MOBS;

        private Sensitivity() {}
    }
}
