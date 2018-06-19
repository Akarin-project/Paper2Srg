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

    public ItemStack func_82487_b(IBlockSource isourceblock, ItemStack itemstack) {
        World world = isourceblock.func_82618_k();
        IPosition iposition = BlockDispenser.func_149939_a(isourceblock);
        EnumFacing enumdirection = (EnumFacing) isourceblock.func_189992_e().func_177229_b(BlockDispenser.field_176441_a);
        IProjectile iprojectile = this.func_82499_a(world, iposition, itemstack);

        // iprojectile.shoot((double) enumdirection.getAdjacentX(), (double) ((float) enumdirection.getAdjacentY() + 0.1F), (double) enumdirection.getAdjacentZ(), this.getPower(), this.a());
        // CraftBukkit start
        ItemStack itemstack1 = itemstack.func_77979_a(1);
        org.bukkit.block.Block block = world.getWorld().getBlockAt(isourceblock.func_180699_d().func_177958_n(), isourceblock.func_180699_d().func_177956_o(), isourceblock.func_180699_d().func_177952_p());
        CraftItemStack craftItem = CraftItemStack.asCraftMirror(itemstack1);

        BlockDispenseEvent event = new BlockDispenseEvent(block, craftItem.clone(), new org.bukkit.util.Vector((double) enumdirection.func_82601_c(), (double) ((float) enumdirection.func_96559_d() + 0.1F), (double) enumdirection.func_82599_e()));
        if (!BlockDispenser.eventFired) {
            world.getServer().getPluginManager().callEvent(event);
        }

        if (event.isCancelled()) {
            itemstack.func_190917_f(1);
            return itemstack;
        }

        if (!event.getItem().equals(craftItem)) {
            itemstack.func_190917_f(1);
            // Chain to handler for new item
            ItemStack eventStack = CraftItemStack.asNMSCopy(event.getItem());
            IBehaviorDispenseItem idispensebehavior = (IBehaviorDispenseItem) BlockDispenser.field_149943_a.func_82594_a(eventStack.func_77973_b());
            if (idispensebehavior != IBehaviorDispenseItem.field_82483_a && idispensebehavior != this) {
                idispensebehavior.func_82482_a(isourceblock, eventStack);
                return itemstack;
            }
        }

        iprojectile.func_70186_c(event.getVelocity().getX(), event.getVelocity().getY(), event.getVelocity().getZ(), this.func_82500_b(), this.func_82498_a());
        ((Entity) iprojectile).projectileSource = new org.bukkit.craftbukkit.projectiles.CraftBlockProjectileSource((TileEntityDispenser) isourceblock.func_150835_j());
        // CraftBukkit end
        world.func_72838_d((Entity) iprojectile);
        // itemstack.subtract(1); // CraftBukkit - Handled during event processing
        return itemstack;
    }

    protected void func_82485_a(IBlockSource isourceblock) {
        isourceblock.func_82618_k().func_175718_b(1002, isourceblock.func_180699_d(), 0);
    }

    protected abstract IProjectile func_82499_a(World world, IPosition iposition, ItemStack itemstack);

    protected float func_82498_a() {
        return 6.0F;
    }

    protected float func_82500_b() {
        return 1.1F;
    }
}
