package net.minecraft.dispenser;

// CraftBukkit start
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.event.block.BlockDispenseEvent;
import net.minecraft.block.BlockDispenser;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IProjectile;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

// CraftBukkit end

public abstract class BehaviorProjectileDispense extends BehaviorDefaultDispenseItem {

    public BehaviorProjectileDispense() {}

    public ItemStack dispenseStack(IBlockSource isourceblock, ItemStack itemstack) {
        World world = isourceblock.getWorld();
        IPosition iposition = BlockDispenser.getDispensePosition(isourceblock);
        EnumFacing enumdirection = (EnumFacing) isourceblock.getBlockState().getValue(BlockDispenser.FACING);
        IProjectile iprojectile = this.getProjectileEntity(world, iposition, itemstack);

        // iprojectile.shoot((double) enumdirection.getAdjacentX(), (double) ((float) enumdirection.getAdjacentY() + 0.1F), (double) enumdirection.getAdjacentZ(), this.getPower(), this.a());
        // CraftBukkit start
        ItemStack itemstack1 = itemstack.splitStack(1);
        org.bukkit.block.Block block = world.getWorld().getBlockAt(isourceblock.getBlockPos().getX(), isourceblock.getBlockPos().getY(), isourceblock.getBlockPos().getZ());
        CraftItemStack craftItem = CraftItemStack.asCraftMirror(itemstack1);

        BlockDispenseEvent event = new BlockDispenseEvent(block, craftItem.clone(), new org.bukkit.util.Vector((double) enumdirection.getFrontOffsetX(), (double) ((float) enumdirection.getFrontOffsetY() + 0.1F), (double) enumdirection.getFrontOffsetZ()));
        if (!BlockDispenser.eventFired) {
            world.getServer().getPluginManager().callEvent(event);
        }

        if (event.isCancelled()) {
            itemstack.grow(1);
            return itemstack;
        }

        if (!event.getItem().equals(craftItem)) {
            itemstack.grow(1);
            // Chain to handler for new item
            ItemStack eventStack = CraftItemStack.asNMSCopy(event.getItem());
            IBehaviorDispenseItem idispensebehavior = (IBehaviorDispenseItem) BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.getObject(eventStack.getItem());
            if (idispensebehavior != IBehaviorDispenseItem.DEFAULT_BEHAVIOR && idispensebehavior != this) {
                idispensebehavior.dispense(isourceblock, eventStack);
                return itemstack;
            }
        }

        iprojectile.shoot(event.getVelocity().getX(), event.getVelocity().getY(), event.getVelocity().getZ(), this.getProjectileVelocity(), this.getProjectileInaccuracy());
        ((Entity) iprojectile).projectileSource = new org.bukkit.craftbukkit.projectiles.CraftBlockProjectileSource((TileEntityDispenser) isourceblock.getBlockTileEntity());
        // CraftBukkit end
        world.spawnEntity((Entity) iprojectile);
        // itemstack.subtract(1); // CraftBukkit - Handled during event processing
        return itemstack;
    }

    protected void playDispenseSound(IBlockSource isourceblock) {
        isourceblock.getWorld().playEvent(1002, isourceblock.getBlockPos(), 0);
    }

    protected abstract IProjectile getProjectileEntity(World world, IPosition iposition, ItemStack itemstack);

    protected float getProjectileInaccuracy() {
        return 6.0F;
    }

    protected float getProjectileVelocity() {
        return 1.1F;
    }
}
