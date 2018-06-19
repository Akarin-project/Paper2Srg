package net.minecraft.item;

// CraftBukkit start
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.event.block.BlockDispenseEvent;
import net.minecraft.block.BlockDispenser;
import net.minecraft.block.BlockRailBase;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

// CraftBukkit end

public class ItemMinecart extends Item {

    private static final IBehaviorDispenseItem field_96602_b = new BehaviorDefaultDispenseItem() {
        private final BehaviorDefaultDispenseItem b = new BehaviorDefaultDispenseItem();

        public ItemStack func_82487_b(IBlockSource isourceblock, ItemStack itemstack) {
            EnumFacing enumdirection = (EnumFacing) isourceblock.func_189992_e().func_177229_b(BlockDispenser.field_176441_a);
            World world = isourceblock.func_82618_k();
            double d0 = isourceblock.func_82615_a() + (double) enumdirection.func_82601_c() * 1.125D;
            double d1 = Math.floor(isourceblock.func_82617_b()) + (double) enumdirection.func_96559_d();
            double d2 = isourceblock.func_82616_c() + (double) enumdirection.func_82599_e() * 1.125D;
            BlockPos blockposition = isourceblock.func_180699_d().func_177972_a(enumdirection);
            IBlockState iblockdata = world.func_180495_p(blockposition);
            BlockRailBase.EnumRailDirection blockminecarttrackabstract_enumtrackposition = iblockdata.func_177230_c() instanceof BlockRailBase ? (BlockRailBase.EnumRailDirection) iblockdata.func_177229_b(((BlockRailBase) iblockdata.func_177230_c()).func_176560_l()) : BlockRailBase.EnumRailDirection.NORTH_SOUTH;
            double d3;

            if (BlockRailBase.func_176563_d(iblockdata)) {
                if (blockminecarttrackabstract_enumtrackposition.func_177018_c()) {
                    d3 = 0.6D;
                } else {
                    d3 = 0.1D;
                }
            } else {
                if (iblockdata.func_185904_a() != Material.field_151579_a || !BlockRailBase.func_176563_d(world.func_180495_p(blockposition.func_177977_b()))) {
                    return this.b.func_82482_a(isourceblock, itemstack);
                }

                IBlockState iblockdata1 = world.func_180495_p(blockposition.func_177977_b());
                BlockRailBase.EnumRailDirection blockminecarttrackabstract_enumtrackposition1 = iblockdata1.func_177230_c() instanceof BlockRailBase ? (BlockRailBase.EnumRailDirection) iblockdata1.func_177229_b(((BlockRailBase) iblockdata1.func_177230_c()).func_176560_l()) : BlockRailBase.EnumRailDirection.NORTH_SOUTH;

                if (enumdirection != EnumFacing.DOWN && blockminecarttrackabstract_enumtrackposition1.func_177018_c()) {
                    d3 = -0.4D;
                } else {
                    d3 = -0.9D;
                }
            }

            // CraftBukkit start
            // EntityMinecartAbstract entityminecartabstract = EntityMinecartAbstract.a(world, d0, d1 + d3, d2, ((ItemMinecart) itemstack.getItem()).b);
            ItemStack itemstack1 = itemstack.func_77979_a(1);
            org.bukkit.block.Block block2 = world.getWorld().getBlockAt(isourceblock.func_180699_d().func_177958_n(), isourceblock.func_180699_d().func_177956_o(), isourceblock.func_180699_d().func_177952_p());
            CraftItemStack craftItem = CraftItemStack.asCraftMirror(itemstack1);

            BlockDispenseEvent event = new BlockDispenseEvent(block2, craftItem.clone(), new org.bukkit.util.Vector(d0, d1 + d3, d2));
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

            itemstack1 = CraftItemStack.asNMSCopy(event.getItem());
            EntityMinecart entityminecartabstract = EntityMinecart.func_184263_a(world, event.getVelocity().getX(), event.getVelocity().getY(), event.getVelocity().getZ(), ((ItemMinecart) itemstack1.func_77973_b()).field_77841_a);

            if (itemstack.func_82837_s()) {
                entityminecartabstract.func_96094_a(itemstack.func_82833_r());
            }

            if (!world.func_72838_d(entityminecartabstract)) itemstack.func_190917_f(1);
            // itemstack.subtract(1); // CraftBukkit - handled during event processing
            // CraftBukkit end
            return itemstack;
        }

        protected void func_82485_a(IBlockSource isourceblock) {
            isourceblock.func_82618_k().func_175718_b(1000, isourceblock.func_180699_d(), 0);
        }
    };
    private final EntityMinecart.Type field_77841_a;

    public ItemMinecart(EntityMinecart.Type entityminecartabstract_enumminecarttype) {
        this.field_77777_bU = 1;
        this.field_77841_a = entityminecartabstract_enumminecarttype;
        this.func_77637_a(CreativeTabs.field_78029_e);
        BlockDispenser.field_149943_a.func_82595_a(this, ItemMinecart.field_96602_b);
    }

    public EnumActionResult func_180614_a(EntityPlayer entityhuman, World world, BlockPos blockposition, EnumHand enumhand, EnumFacing enumdirection, float f, float f1, float f2) {
        IBlockState iblockdata = world.func_180495_p(blockposition);

        if (!BlockRailBase.func_176563_d(iblockdata)) {
            return EnumActionResult.FAIL;
        } else {
            ItemStack itemstack = entityhuman.func_184586_b(enumhand);

            if (!world.field_72995_K) {
                BlockRailBase.EnumRailDirection blockminecarttrackabstract_enumtrackposition = iblockdata.func_177230_c() instanceof BlockRailBase ? (BlockRailBase.EnumRailDirection) iblockdata.func_177229_b(((BlockRailBase) iblockdata.func_177230_c()).func_176560_l()) : BlockRailBase.EnumRailDirection.NORTH_SOUTH;
                double d0 = 0.0D;

                if (blockminecarttrackabstract_enumtrackposition.func_177018_c()) {
                    d0 = 0.5D;
                }

                EntityMinecart entityminecartabstract = EntityMinecart.func_184263_a(world, (double) blockposition.func_177958_n() + 0.5D, (double) blockposition.func_177956_o() + 0.0625D + d0, (double) blockposition.func_177952_p() + 0.5D, this.field_77841_a);

                if (itemstack.func_82837_s()) {
                    entityminecartabstract.func_96094_a(itemstack.func_82833_r());
                }

                if (!world.func_72838_d(entityminecartabstract)) return EnumActionResult.PASS; // CraftBukkit
            }

            itemstack.func_190918_g(1);
            return EnumActionResult.SUCCESS;
        }
    }
}
