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

    public final int itemUseDuration;
    private final int healAmount;
    private final float saturationModifier;
    private final boolean isWolfsFavoriteMeat;
    private boolean alwaysEdible;
    private PotionEffect potionId;
    private float potionEffectProbability;

    public ItemFood(int i, float f, boolean flag) {
        this.itemUseDuration = 32;
        this.healAmount = i;
        this.isWolfsFavoriteMeat = flag;
        this.saturationModifier = f;
        this.setCreativeTab(CreativeTabs.FOOD);
    }

    public ItemFood(int i, boolean flag) {
        this(i, 0.6F, flag);
    }

    public ItemStack onItemUseFinish(ItemStack itemstack, World world, EntityLivingBase entityliving) {
        if (entityliving instanceof EntityPlayer) {
            EntityPlayer entityhuman = (EntityPlayer) entityliving;

            entityhuman.getFoodStats().addStats(this, itemstack);
            world.playSound((EntityPlayer) null, entityhuman.posX, entityhuman.posY, entityhuman.posZ, SoundEvents.ENTITY_PLAYER_BURP, SoundCategory.PLAYERS, 0.5F, world.rand.nextFloat() * 0.1F + 0.9F);
            this.onFoodEaten(itemstack, world, entityhuman);
            entityhuman.addStat(StatList.getObjectUseStats((Item) this));
            if (entityhuman instanceof EntityPlayerMP) {
                CriteriaTriggers.CONSUME_ITEM.trigger((EntityPlayerMP) entityhuman, itemstack);
            }
        }

        itemstack.shrink(1);
        return itemstack;
    }

    protected void onFoodEaten(ItemStack itemstack, World world, EntityPlayer entityhuman) {
        if (!world.isRemote && this.potionId != null && world.rand.nextFloat() < this.potionEffectProbability) {
            entityhuman.addPotionEffect(new PotionEffect(this.potionId));
        }

    }

    public int getMaxItemUseDuration(ItemStack itemstack) {
        return 32;
    }

    public EnumAction getItemUseAction(ItemStack itemstack) {
        return EnumAction.EAT;
    }

    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer entityhuman, EnumHand enumhand) {
        ItemStack itemstack = entityhuman.getHeldItem(enumhand);

        if (entityhuman.canEat(this.alwaysEdible)) {
            entityhuman.setActiveHand(enumhand);
            return new ActionResult(EnumActionResult.SUCCESS, itemstack);
        } else {
            return new ActionResult(EnumActionResult.FAIL, itemstack);
        }
    }

    public int getHealAmount(ItemStack itemstack) {
        return this.healAmount;
    }

    public float getSaturationModifier(ItemStack itemstack) {
        return this.saturationModifier;
    }

    public boolean isWolfsFavoriteMeat() {
        return this.isWolfsFavoriteMeat;
    }

    public ItemFood setPotionEffect(PotionEffect mobeffect, float f) {
        this.potionId = mobeffect;
        this.potionEffectProbability = f;
        return this;
    }

    public ItemFood setAlwaysEdible() {
        this.alwaysEdible = true;
        return this;
    }
}
