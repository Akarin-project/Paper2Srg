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

    public final ItemStack func_82482_a(IBlockSource isourceblock, ItemStack itemstack) {
        ItemStack itemstack1 = this.func_82487_b(isourceblock, itemstack);

        this.func_82485_a(isourceblock);
        this.func_82489_a(isourceblock, (EnumFacing) isourceblock.func_189992_e().func_177229_b(BlockDispenser.field_176441_a));
        return itemstack1;
    }

    protected ItemStack func_82487_b(IBlockSource isourceblock, ItemStack itemstack) {
        EnumFacing enumdirection = (EnumFacing) isourceblock.func_189992_e().func_177229_b(BlockDispenser.field_176441_a);
        IPosition iposition = BlockDispenser.func_149939_a(isourceblock);
        ItemStack itemstack1 = itemstack.func_77979_a(1);

        // CraftBukkit start
        if (!a(isourceblock.func_82618_k(), itemstack1, 6, enumdirection, isourceblock)) {
            itemstack.func_190917_f(1);
        }
        // CraftBukkit end
        return itemstack;
    }

    // CraftBukkit start - void -> boolean return, IPosition -> ISourceBlock last argument
    public static boolean a(World world, ItemStack itemstack, int i, EnumFacing enumdirection, IBlockSource isourceblock) {
        IPosition iposition = BlockDispenser.func_149939_a(isourceblock);
        // CraftBukkit end
        double d0 = iposition.func_82615_a();
        double d1 = iposition.func_82617_b();
        double d2 = iposition.func_82616_c();

        if (enumdirection.func_176740_k() == EnumFacing.Axis.Y) {
            d1 -= 0.125D;
        } else {
            d1 -= 0.15625D;
        }

        EntityItem entityitem = new EntityItem(world, d0, d1, d2, itemstack);
        double d3 = world.field_73012_v.nextDouble() * 0.1D + 0.2D;

        entityitem.field_70159_w = (double) enumdirection.func_82601_c() * d3;
        entityitem.field_70181_x = 0.20000000298023224D;
        entityitem.field_70179_y = (double) enumdirection.func_82599_e() * d3;
        entityitem.field_70159_w += world.field_73012_v.nextGaussian() * 0.007499999832361937D * (double) i;
        entityitem.field_70181_x += world.field_73012_v.nextGaussian() * 0.007499999832361937D * (double) i;
        entityitem.field_70179_y += world.field_73012_v.nextGaussian() * 0.007499999832361937D * (double) i;

        // CraftBukkit start
        org.bukkit.block.Block block = world.getWorld().getBlockAt(isourceblock.func_180699_d().func_177958_n(), isourceblock.func_180699_d().func_177956_o(), isourceblock.func_180699_d().func_177952_p());
        CraftItemStack craftItem = CraftItemStack.asCraftMirror(itemstack);

        BlockDispenseEvent event = new BlockDispenseEvent(block, craftItem.clone(), new org.bukkit.util.Vector(entityitem.field_70159_w, entityitem.field_70181_x, entityitem.field_70179_y));
        if (!BlockDispenser.eventFired) {
            world.getServer().getPluginManager().callEvent(event);
        }

        if (event.isCancelled()) {
            return false;
        }

        entityitem.func_92058_a(CraftItemStack.asNMSCopy(event.getItem()));
        entityitem.field_70159_w = event.getVelocity().getX();
        entityitem.field_70181_x = event.getVelocity().getY();
        entityitem.field_70179_y = event.getVelocity().getZ();

        if (!event.getItem().getType().equals(craftItem.getType())) {
            // Chain to handler for new item
            ItemStack eventStack = CraftItemStack.asNMSCopy(event.getItem());
            IBehaviorDispenseItem idispensebehavior = (IBehaviorDispenseItem) BlockDispenser.field_149943_a.func_82594_a(eventStack.func_77973_b());
            if (idispensebehavior != IBehaviorDispenseItem.field_82483_a && idispensebehavior.getClass() != BehaviorDefaultDispenseItem.class) {
                idispensebehavior.func_82482_a(isourceblock, eventStack);
            } else {
                world.func_72838_d(entityitem);
            }
            return false;
        }

        world.func_72838_d(entityitem);

        return true;
        // CraftBukkit end
    }

    protected void func_82485_a(IBlockSource isourceblock) {
        isourceblock.func_82618_k().func_175718_b(1000, isourceblock.func_180699_d(), 0);
    }

    protected void func_82489_a(IBlockSource isourceblock, EnumFacing enumdirection) {
        isourceblock.func_82618_k().func_175718_b(2000, isourceblock.func_180699_d(), this.func_82488_a(enumdirection));
    }

    private int func_82488_a(EnumFacing enumdirection) {
        return enumdirection.func_82601_c() + 1 + (enumdirection.func_82599_e() + 1) * 3;
    }
}
