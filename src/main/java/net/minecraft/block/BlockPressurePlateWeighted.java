package net.minecraft.block;


import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.bukkit.event.entity.EntityInteractEvent;

public class BlockPressurePlateWeighted extends BlockBasePressurePlate {

    public static final PropertyInteger POWER = PropertyInteger.create("power", 0, 15);
    private final int maxWeight;

    protected BlockPressurePlateWeighted(Material material, int i) {
        this(material, i, material.getMaterialMapColor());
    }

    protected BlockPressurePlateWeighted(Material material, int i, MapColor materialmapcolor) {
        super(material, materialmapcolor);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockPressurePlateWeighted.POWER, Integer.valueOf(0)));
        this.maxWeight = i;
    }

    protected int computeRedstoneStrength(World world, BlockPos blockposition) {
        // CraftBukkit start
        // int i = Math.min(world.a(Entity.class, BlockPressurePlateWeighted.c.a(blockposition)).size(), this.weight);
        int i = 0;
        java.util.Iterator iterator = world.getEntitiesWithinAABB(Entity.class, BlockPressurePlateWeighted.PRESSURE_AABB.offset(blockposition)).iterator();

        while (iterator.hasNext()) {
            Entity entity = (Entity) iterator.next();

            org.bukkit.event.Cancellable cancellable;

            if (entity instanceof EntityPlayer) {
                cancellable = org.bukkit.craftbukkit.event.CraftEventFactory.callPlayerInteractEvent((EntityPlayer) entity, org.bukkit.event.block.Action.PHYSICAL, blockposition, null, null, null);
            } else {
                cancellable = new EntityInteractEvent(entity.getBukkitEntity(), world.getWorld().getBlockAt(blockposition.getX(), blockposition.getY(), blockposition.getZ()));
                world.getServer().getPluginManager().callEvent((EntityInteractEvent) cancellable);
            }

            // We only want to block turning the plate on if all events are cancelled
            if (!cancellable.isCancelled()) {
                i++;
            }
        }

        i = Math.min(i, this.maxWeight);
        // CraftBukkit end

        if (i > 0) {
            float f = (float) Math.min(this.maxWeight, i) / (float) this.maxWeight;

            return MathHelper.ceil(f * 15.0F);
        } else {
            return 0;
        }
    }

    protected void playClickOnSound(World world, BlockPos blockposition) {
        world.playSound((EntityPlayer) null, blockposition, SoundEvents.BLOCK_METAL_PRESSPLATE_CLICK_ON, SoundCategory.BLOCKS, 0.3F, 0.90000004F);
    }

    protected void playClickOffSound(World world, BlockPos blockposition) {
        world.playSound((EntityPlayer) null, blockposition, SoundEvents.BLOCK_METAL_PRESSPLATE_CLICK_OFF, SoundCategory.BLOCKS, 0.3F, 0.75F);
    }

    protected int getRedstoneStrength(IBlockState iblockdata) {
        return ((Integer) iblockdata.getValue(BlockPressurePlateWeighted.POWER)).intValue();
    }

    protected IBlockState setRedstoneStrength(IBlockState iblockdata, int i) {
        return iblockdata.withProperty(BlockPressurePlateWeighted.POWER, Integer.valueOf(i));
    }

    public int tickRate(World world) {
        return 10;
    }

    public IBlockState getStateFromMeta(int i) {
        return this.getDefaultState().withProperty(BlockPressurePlateWeighted.POWER, Integer.valueOf(i));
    }

    public int getMetaFromState(IBlockState iblockdata) {
        return ((Integer) iblockdata.getValue(BlockPressurePlateWeighted.POWER)).intValue();
    }

    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] { BlockPressurePlateWeighted.POWER});
    }
}
