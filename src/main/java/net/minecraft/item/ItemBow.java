package net.minecraft.item;

import org.bukkit.craftbukkit.inventory.CraftItemStack;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import org.bukkit.event.entity.EntityCombustEvent;

public class ItemBow extends Item {

    public ItemBow() {
        this.maxStackSize = 1;
        this.setMaxDamage(384);
        this.setCreativeTab(CreativeTabs.COMBAT);
        this.addPropertyOverride(new ResourceLocation("pull"), new IItemPropertyGetter() {
        });
        this.addPropertyOverride(new ResourceLocation("pulling"), new IItemPropertyGetter() {
        });
    }

    private ItemStack a(EntityPlayer entityhuman, ItemStack bow) { // Paper
        if (this.d(entityhuman, bow, entityhuman.getHeldItem(EnumHand.OFF_HAND))) { // Paper
            return entityhuman.getHeldItem(EnumHand.OFF_HAND);
        } else if (this.d(entityhuman, bow, entityhuman.getHeldItem(EnumHand.MAIN_HAND))) { // Paper
            return entityhuman.getHeldItem(EnumHand.MAIN_HAND);
        } else {
            for (int i = 0; i < entityhuman.inventory.getSizeInventory(); ++i) {
                ItemStack itemstack = entityhuman.inventory.getStackInSlot(i);

                if (this.d(entityhuman, bow, itemstack)) { // Paper
                    return itemstack;
                }
            }

            return ItemStack.EMPTY;
        }
    }

    // Paper start
    protected boolean d(EntityPlayer player, ItemStack bow, ItemStack itemstack) {
        return itemstack.getItem() instanceof ItemArrow && (
                !(player instanceof EntityPlayerMP) ||
                new com.destroystokyo.paper.event.player.PlayerReadyArrowEvent(
                        ((EntityPlayerMP) player).getBukkitEntity(),
                        CraftItemStack.asCraftMirror(bow),
                        CraftItemStack.asCraftMirror(itemstack)
                    ).callEvent());
        // Paper end
    }

    public void onPlayerStoppedUsing(ItemStack itemstack, World world, EntityLivingBase entityliving, int i) {
        if (entityliving instanceof EntityPlayer) {
            EntityPlayer entityhuman = (EntityPlayer) entityliving;
            boolean flag = entityhuman.capabilities.isCreativeMode || EnchantmentHelper.getEnchantmentLevel(Enchantments.INFINITY, itemstack) > 0;
            ItemStack itemstack1 = this.a(entityhuman, itemstack); // Paper

            if (!itemstack1.isEmpty() || flag) {
                if (itemstack1.isEmpty()) {
                    itemstack1 = new ItemStack(Items.ARROW);
                }

                int j = this.getMaxItemUseDuration(itemstack) - i;
                float f = getArrowVelocity(j);

                if ((double) f >= 0.1D) {
                    boolean flag1 = flag && itemstack1.getItem() == Items.ARROW;

                    boolean consumeArrow = true; // Paper
                    if (!world.isRemote) {
                        ItemArrow itemarrow = (ItemArrow) ((ItemArrow) (itemstack1.getItem() instanceof ItemArrow ? itemstack1.getItem() : Items.ARROW));
                        EntityArrow entityarrow = itemarrow.createArrow(world, itemstack1, (EntityLivingBase) entityhuman);

                        entityarrow.shoot(entityhuman, entityhuman.rotationPitch, entityhuman.rotationYaw, 0.0F, f * 3.0F, 1.0F);
                        if (f == 1.0F) {
                            entityarrow.setIsCritical(true);
                        }

                        int k = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, itemstack);

                        if (k > 0) {
                            entityarrow.setDamage(entityarrow.getDamage() + (double) k * 0.5D + 0.5D);
                        }

                        int l = EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH, itemstack);

                        if (l > 0) {
                            entityarrow.setKnockbackStrength(l);
                        }

                        if (EnchantmentHelper.getEnchantmentLevel(Enchantments.FLAME, itemstack) > 0) {
                        // CraftBukkit start - call EntityCombustEvent
                        EntityCombustEvent event = new EntityCombustEvent(entityarrow.getBukkitEntity(), 100);
                        entityarrow.world.getServer().getPluginManager().callEvent(event);

                        if (!event.isCancelled()) {
                            entityarrow.setFire(event.getDuration());
                        }
                        // CraftBukkit end
                        }
                        // CraftBukkit start
                        org.bukkit.event.entity.EntityShootBowEvent event = org.bukkit.craftbukkit.event.CraftEventFactory.callEntityShootBowEvent(entityhuman, itemstack, itemstack1, entityarrow, f); // Paper
                        if (event.isCancelled()) {
                            event.getProjectile().remove();
                            return;
                        }

                        itemstack.damageItem(1, entityhuman);
                        consumeArrow = event.getConsumeArrow(); // Paper
                        if (!consumeArrow || flag1 || (entityhuman.capabilities.isCreativeMode && ((itemstack1.getItem() == Items.SPECTRAL_ARROW) || (itemstack1.getItem() == Items.TIPPED_ARROW)))) { // Paper - add !consumeArrow
                            entityarrow.pickupStatus = EntityArrow.PickupStatus.CREATIVE_ONLY;
                        }

                        if (event.getProjectile() == entityarrow.getBukkitEntity()) {
                            if (!world.spawnEntity(entityarrow)) {
                                if (entityhuman instanceof EntityPlayerMP) {
                                    ((EntityPlayerMP) entityhuman).getBukkitEntity().updateInventory();
                                }
                                return;
                            }
                        }
                        // CraftBukkit end
                    }

                    world.playSound((EntityPlayer) null, entityhuman.posX, entityhuman.posY, entityhuman.posZ, SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS, 1.0F, 1.0F / (ItemBow.itemRand.nextFloat() * 0.4F + 1.2F) + f * 0.5F);
                    if (!flag1 && !entityhuman.capabilities.isCreativeMode && consumeArrow) { // Paper
                        itemstack1.shrink(1);
                        if (itemstack1.isEmpty()) {
                            entityhuman.inventory.deleteStack(itemstack1);
                        }
                    }

                    entityhuman.addStat(StatList.getObjectUseStats((Item) this));
                }
            }
        }
    }

    public static float getArrowVelocity(int i) {
        float f = (float) i / 20.0F;

        f = (f * f + f * 2.0F) / 3.0F;
        if (f > 1.0F) {
            f = 1.0F;
        }

        return f;
    }

    public int getMaxItemUseDuration(ItemStack itemstack) {
        return 72000;
    }

    public EnumAction getItemUseAction(ItemStack itemstack) {
        return EnumAction.BOW;
    }

    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer entityhuman, EnumHand enumhand) {
        ItemStack itemstack = entityhuman.getHeldItem(enumhand);
        boolean flag = !this.a(entityhuman, itemstack).isEmpty(); // Paper

        if (!entityhuman.capabilities.isCreativeMode && !flag) {
            return flag ? new ActionResult(EnumActionResult.PASS, itemstack) : new ActionResult(EnumActionResult.FAIL, itemstack);
        } else {
            entityhuman.setActiveHand(enumhand);
            return new ActionResult(EnumActionResult.SUCCESS, itemstack);
        }
    }

    public int getItemEnchantability() {
        return 1;
    }
}
