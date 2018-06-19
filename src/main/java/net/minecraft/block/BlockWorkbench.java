package net.minecraft.block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerWorkbench;
import net.minecraft.stats.StatList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IInteractionObject;
import net.minecraft.world.World;


public class BlockWorkbench extends Block {

    protected BlockWorkbench() {
        super(Material.field_151575_d);
        this.func_149647_a(CreativeTabs.field_78031_c);
    }

    public boolean func_180639_a(World world, BlockPos blockposition, IBlockState iblockdata, EntityPlayer entityhuman, EnumHand enumhand, EnumFacing enumdirection, float f, float f1, float f2) {
        if (world.field_72995_K) {
            return true;
        } else {
            entityhuman.func_180468_a(new BlockWorkbench.InterfaceCraftingTable(world, blockposition));
            entityhuman.func_71029_a(StatList.field_188062_ab);
            return true;
        }
    }

    public static class InterfaceCraftingTable implements IInteractionObject {

        private final World field_175128_a;
        private final BlockPos field_175127_b;

        public InterfaceCraftingTable(World world, BlockPos blockposition) {
            this.field_175128_a = world;
            this.field_175127_b = blockposition;
        }

        public String func_70005_c_() {
            return "crafting_table";
        }

        public boolean func_145818_k_() {
            return false;
        }

        public ITextComponent func_145748_c_() {
            return new TextComponentTranslation(Blocks.field_150462_ai.func_149739_a() + ".name", new Object[0]);
        }

        public Container func_174876_a(InventoryPlayer playerinventory, EntityPlayer entityhuman) {
            return new ContainerWorkbench(playerinventory, this.field_175128_a, this.field_175127_b);
        }

        public String func_174875_k() {
            return "minecraft:crafting_table";
        }
    }
}
