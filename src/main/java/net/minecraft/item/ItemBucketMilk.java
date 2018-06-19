package net.minecraft.item;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;


public class ItemBucketMilk extends Item {

    public ItemBucketMilk() {
        this.func_77625_d(1);
        this.func_77637_a(CreativeTabs.field_78026_f);
    }

    public ItemStack func_77654_b(ItemStack itemstack, World world, EntityLivingBase entityliving) {
        if (entityliving instanceof EntityPlayerMP) {
            EntityPlayerMP entityplayer = (EntityPlayerMP) entityliving;

            CriteriaTriggers.field_193138_y.func_193148_a(entityplayer, itemstack);
            entityplayer.func_71029_a(StatList.func_188057_b((Item) this));
        }

        if (entityliving instanceof EntityPlayer && !((EntityPlayer) entityliving).field_71075_bZ.field_75098_d) {
            itemstack.func_190918_g(1);
        }

        if (!world.field_72995_K) {
            entityliving.func_70674_bp();
        }

        return itemstack.func_190926_b() ? new ItemStack(Items.field_151133_ar) : itemstack;
    }

    public int func_77626_a(ItemStack itemstack) {
        return 32;
    }

    public EnumAction func_77661_b(ItemStack itemstack) {
        return EnumAction.DRINK;
    }

    public ActionResult<ItemStack> func_77659_a(World world, EntityPlayer entityhuman, EnumHand enumhand) {
        entityhuman.func_184598_c(enumhand);
        return new ActionResult(EnumActionResult.SUCCESS, entityhuman.func_184586_b(enumhand));
    }
}
