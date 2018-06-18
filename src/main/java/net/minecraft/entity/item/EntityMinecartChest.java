package net.minecraft.entity.item;
import net.minecraft.block.BlockChest;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.item.Item;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.world.World;


public class EntityMinecartChest extends EntityMinecartContainer {

    public EntityMinecartChest(World world) {
        super(world);
    }

    public EntityMinecartChest(World world, double d0, double d1, double d2) {
        super(world, d0, d1, d2);
    }

    public static void registerFixesMinecartChest(DataFixer dataconvertermanager) {
        EntityMinecartContainer.addDataFixers(dataconvertermanager, EntityMinecartChest.class);
    }

    public void killMinecart(DamageSource damagesource) {
        super.killMinecart(damagesource);
        if (this.world.getGameRules().getBoolean("doEntityDrops")) {
            this.dropItemWithOffset(Item.getItemFromBlock(Blocks.CHEST), 1, 0.0F);
        }

    }

    public int getSizeInventory() {
        return 27;
    }

    public EntityMinecart.Type getType() {
        return EntityMinecart.Type.CHEST;
    }

    public IBlockState getDefaultDisplayTile() {
        return Blocks.CHEST.getDefaultState().withProperty(BlockChest.FACING, EnumFacing.NORTH);
    }

    public int getDefaultDisplayTileOffset() {
        return 8;
    }

    public String getGuiID() {
        return "minecraft:chest";
    }

    public Container createContainer(InventoryPlayer playerinventory, EntityPlayer entityhuman) {
        this.addLoot(entityhuman);
        return new ContainerChest(playerinventory, this, entityhuman);
    }
}
