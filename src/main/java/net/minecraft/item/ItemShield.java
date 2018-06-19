package net.minecraft.item;
import net.minecraft.block.BlockDispenser;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;


public class ItemShield extends Item {

    public ItemShield() {
        this.field_77777_bU = 1;
        this.func_77637_a(CreativeTabs.field_78037_j);
        this.func_77656_e(336);
        this.func_185043_a(new ResourceLocation("blocking"), new IItemPropertyGetter() {
        });
        BlockDispenser.field_149943_a.func_82595_a(this, ItemArmor.field_96605_cw);
    }

    public String func_77653_i(ItemStack itemstack) {
        if (itemstack.func_179543_a("BlockEntityTag") != null) {
            EnumDyeColor enumcolor = TileEntityBanner.func_190616_d(itemstack);

            return I18n.func_74838_a("item.shield." + enumcolor.func_176762_d() + ".name");
        } else {
            return I18n.func_74838_a("item.shield.name");
        }
    }

    public EnumAction func_77661_b(ItemStack itemstack) {
        return EnumAction.BLOCK;
    }

    public int func_77626_a(ItemStack itemstack) {
        return 72000;
    }

    public ActionResult<ItemStack> func_77659_a(World world, EntityPlayer entityhuman, EnumHand enumhand) {
        ItemStack itemstack = entityhuman.func_184586_b(enumhand);

        entityhuman.func_184598_c(enumhand);
        return new ActionResult(EnumActionResult.SUCCESS, itemstack);
    }

    public boolean func_82789_a(ItemStack itemstack, ItemStack itemstack1) {
        return itemstack1.func_77973_b() == Item.func_150898_a(Blocks.field_150344_f) ? true : super.func_82789_a(itemstack, itemstack1);
    }
}
