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
        super(Material.WOOD);
        this.setCreativeTab(CreativeTabs.DECORATIONS);
    }

    public boolean onBlockActivated(World world, BlockPos blockposition, IBlockState iblockdata, EntityPlayer entityhuman, EnumHand enumhand, EnumFacing enumdirection, float f, float f1, float f2) {
        if (world.isRemote) {
            return true;
        } else {
            entityhuman.displayGui(new BlockWorkbench.InterfaceCraftingTable(world, blockposition));
            entityhuman.addStat(StatList.CRAFTING_TABLE_INTERACTION);
            return true;
        }
    }

    public static class InterfaceCraftingTable implements IInteractionObject {

        private final World world;
        private final BlockPos position;

        public InterfaceCraftingTable(World world, BlockPos blockposition) {
            this.world = world;
            this.position = blockposition;
        }

        public String getName() {
            return "crafting_table";
        }

        public boolean hasCustomName() {
            return false;
        }

        public ITextComponent getDisplayName() {
            return new TextComponentTranslation(Blocks.CRAFTING_TABLE.getUnlocalizedName() + ".name", new Object[0]);
        }

        public Container createContainer(InventoryPlayer playerinventory, EntityPlayer entityhuman) {
            return new ContainerWorkbench(playerinventory, this.world, this.position);
        }

        public String getGuiID() {
            return "minecraft:crafting_table";
        }
    }
}
