package net.minecraft.item;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.potion.PotionEffect;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;


public class ItemFood extends Item {

    public final int field_77855_a;
    private final int field_77853_b;
    private final float field_77854_c;
    private final boolean field_77856_bY;
    private boolean field_77852_bZ;
    private PotionEffect field_77851_ca;
    private float field_77858_cd;

    public ItemFood(int i, float f, boolean flag) {
        this.field_77855_a = 32;
        this.field_77853_b = i;
        this.field_77856_bY = flag;
        this.field_77854_c = f;
        this.func_77637_a(CreativeTabs.field_78039_h);
    }

    public ItemFood(int i, boolean flag) {
        this(i, 0.6F, flag);
    }

    public ItemStack func_77654_b(ItemStack itemstack, World world, EntityLivingBase entityliving) {
        if (entityliving instanceof EntityPlayer) {
            EntityPlayer entityhuman = (EntityPlayer) entityliving;

            entityhuman.func_71024_bL().func_151686_a(this, itemstack);
            world.func_184148_a((EntityPlayer) null, entityhuman.field_70165_t, entityhuman.field_70163_u, entityhuman.field_70161_v, SoundEvents.field_187739_dZ, SoundCategory.PLAYERS, 0.5F, world.field_73012_v.nextFloat() * 0.1F + 0.9F);
            this.func_77849_c(itemstack, world, entityhuman);
            entityhuman.func_71029_a(StatList.func_188057_b((Item) this));
            if (entityhuman instanceof EntityPlayerMP) {
                CriteriaTriggers.field_193138_y.func_193148_a((EntityPlayerMP) entityhuman, itemstack);
            }
        }

        itemstack.func_190918_g(1);
        return itemstack;
    }

    protected void func_77849_c(ItemStack itemstack, World world, EntityPlayer entityhuman) {
        if (!world.field_72995_K && this.field_77851_ca != null && world.field_73012_v.nextFloat() < this.field_77858_cd) {
            entityhuman.func_70690_d(new PotionEffect(this.field_77851_ca));
        }

    }

    public int func_77626_a(ItemStack itemstack) {
        return 32;
    }

    public EnumAction func_77661_b(ItemStack itemstack) {
        return EnumAction.EAT;
    }

    public ActionResult<ItemStack> func_77659_a(World world, EntityPlayer entityhuman, EnumHand enumhand) {
        ItemStack itemstack = entityhuman.func_184586_b(enumhand);

        if (entityhuman.func_71043_e(this.field_77852_bZ)) {
            entityhuman.func_184598_c(enumhand);
            return new ActionResult(EnumActionResult.SUCCESS, itemstack);
        } else {
            return new ActionResult(EnumActionResult.FAIL, itemstack);
        }
    }

    public int func_150905_g(ItemStack itemstack) {
        return this.field_77853_b;
    }

    public float func_150906_h(ItemStack itemstack) {
        return this.field_77854_c;
    }

    public boolean func_77845_h() {
        return this.field_77856_bY;
    }

    public ItemFood func_185070_a(PotionEffect mobeffect, float f) {
        this.field_77851_ca = mobeffect;
        this.field_77858_cd = f;
        return this;
    }

    public ItemFood func_77848_i() {
        this.field_77852_bZ = true;
        return this;
    }
}
