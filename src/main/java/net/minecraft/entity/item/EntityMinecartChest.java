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

    public static void func_189681_a(DataFixer dataconvertermanager) {
        EntityMinecartContainer.func_190574_b(dataconvertermanager, EntityMinecartChest.class);
    }

    public void func_94095_a(DamageSource damagesource) {
        super.func_94095_a(damagesource);
        if (this.field_70170_p.func_82736_K().func_82766_b("doEntityDrops")) {
            this.func_145778_a(Item.func_150898_a(Blocks.field_150486_ae), 1, 0.0F);
        }

    }

    public int func_70302_i_() {
        return 27;
    }

    public EntityMinecart.Type func_184264_v() {
        return EntityMinecart.Type.CHEST;
    }

    public IBlockState func_180457_u() {
        return Blocks.field_150486_ae.func_176223_P().func_177226_a(BlockChest.field_176459_a, EnumFacing.NORTH);
    }

    public int func_94085_r() {
        return 8;
    }

    public String func_174875_k() {
        return "minecraft:chest";
    }

    public Container func_174876_a(InventoryPlayer playerinventory, EntityPlayer entityhuman) {
        this.func_184288_f(entityhuman);
        return new ContainerChest(playerinventory, this, entityhuman);
    }
}
