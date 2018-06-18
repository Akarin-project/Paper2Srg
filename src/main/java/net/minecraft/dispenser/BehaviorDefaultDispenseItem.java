package net.minecraft.dispenser;

// CraftBukkit start
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.event.block.BlockDispenseEvent;
import net.minecraft.block.BlockDispenser;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

// CraftBukkit end

public class BehaviorDefaultDispenseItem implements IBehaviorDispenseItem {

    public BehaviorDefaultDispenseItem() {}

    public final ItemStack dispense(IBlockSource isourceblock, ItemStack itemstack) {
        ItemStack itemstack1 = this.dispenseStack(isourceblock, itemstack);

        this.playDispenseSound(isourceblock);
        this.spawnDispenseParticles(isourceblock, (EnumFacing) isourceblock.getBlockState().getValue(BlockDispenser.FACING));
        return itemstack1;
    }

    protected ItemStack dispenseStack(IBlockSource isourceblock, ItemStack itemstack) {
        EnumFacing enumdirection = (EnumFacing) isourceblock.getBlockState().getValue(BlockDispenser.FACING);
        IPosition iposition = BlockDispenser.getDispensePosition(isourceblock);
        ItemStack itemstack1 = itemstack.splitStack(1);

        // CraftBukkit start
        if (!a(isourceblock.getWorld(), itemstack1, 6, enumdirection, isourceblock)) {
            itemstack.grow(1);
        }
        // CraftBukkit end
        return itemstack;
    }

    // CraftBukkit start - void -> boolean return, IPosition -> ISourceBlock last argument
    public static boolean a(World world, ItemStack itemstack, int i, EnumFacing enumdirection, IBlockSource isourceblock) {
        IPosition iposition = BlockDispenser.getDispensePosition(isourceblock);
        // CraftBukkit end
        double d0 = iposition.getX();
        double d1 = iposition.getY();
        double d2 = iposition.getZ();

        if (enumdirection.getAxis() == EnumFacing.Axis.Y) {
            d1 -= 0.125D;
        } else {
            d1 -= 0.15625D;
        }

        EntityItem entityitem = new EntityItem(world, d0, d1, d2, itemstack);
        double d3 = world.rand.nextDouble() * 0.1D + 0.2D;

        entityitem.motionX = (double) enumdirection.getFrontOffsetX() * d3;
        entityitem.motionY = 0.20000000298023224D;
        entityitem.motionZ = (double) enumdirection.getFrontOffsetZ() * d3;
        entityitem.motionX += world.rand.nextGaussian() * 0.007499999832361937D * (double) i;
        entityitem.motionY += world.rand.nextGaussian() * 0.007499999832361937D * (double) i;
        entityitem.motionZ += world.rand.nextGaussian() * 0.007499999832361937D * (double) i;

        // CraftBukkit start
        org.bukkit.block.Block block = world.getWorld().getBlockAt(isourceblock.getBlockPos().getX(), isourceblock.getBlockPos().getY(), isourceblock.getBlockPos().getZ());
        CraftItemStack craftItem = CraftItemStack.asCraftMirror(itemstack);

        BlockDispenseEvent event = new BlockDispenseEvent(block, craftItem.clone(), new org.bukkit.util.Vector(entityitem.motionX, entityitem.motionY, entityitem.motionZ));
        if (!BlockDispenser.eventFired) {
            world.getServer().getPluginManager().callEvent(event);
        }

        if (event.isCancelled()) {
            return false;
        }

        entityitem.setItem(CraftItemStack.asNMSCopy(event.getItem()));
        entityitem.motionX = event.getVelocity().getX();
        entityitem.motionY = event.getVelocity().getY();
        entityitem.motionZ = event.getVelocity().getZ();

        if (!event.getItem().getType().equals(craftItem.getType())) {
            // Chain to handler for new item
            ItemStack eventStack = CraftItemStack.asNMSCopy(event.getItem());
            IBehaviorDispenseItem idispensebehavior = (IBehaviorDispenseItem) BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.getObject(eventStack.getItem());
            if (idispensebehavior != IBehaviorDispenseItem.DEFAULT_BEHAVIOR && idispensebehavior.getClass() != BehaviorDefaultDispenseItem.class) {
                idispensebehavior.dispense(isourceblock, eventStack);
            } else {
                world.spawnEntity(entityitem);
            }
            return false;
        }

        world.spawnEntity(entityitem);

        return true;
        // CraftBukkit end
    }

    protected void playDispenseSound(IBlockSource isourceblock) {
        isourceblock.getWorld().playEvent(1000, isourceblock.getBlockPos(), 0);
    }

    protected void spawnDispenseParticles(IBlockSource isourceblock, EnumFacing enumdirection) {
        isourceblock.getWorld().playEvent(2000, isourceblock.getBlockPos(), this.getWorldEventDataFrom(enumdirection));
    }

    private int getWorldEventDataFrom(EnumFacing enumdirection) {
        return enumdirection.getFrontOffsetX() + 1 + (enumdirection.getFrontOffsetZ() + 1) * 3;
    }
}
