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
        this.field_77777_bU = 1;
        this.func_77656_e(384);
        this.func_77637_a(CreativeTabs.field_78037_j);
        this.func_185043_a(new ResourceLocation("pull"), new IItemPropertyGetter() {
        });
        this.func_185043_a(new ResourceLocation("pulling"), new IItemPropertyGetter() {
        });
    }

    private ItemStack a(EntityPlayer entityhuman, ItemStack bow) { // Paper
        if (this.d(entityhuman, bow, entityhuman.func_184586_b(EnumHand.OFF_HAND))) { // Paper
            return entityhuman.func_184586_b(EnumHand.OFF_HAND);
        } else if (this.d(entityhuman, bow, entityhuman.func_184586_b(EnumHand.MAIN_HAND))) { // Paper
            return entityhuman.func_184586_b(EnumHand.MAIN_HAND);
        } else {
            for (int i = 0; i < entityhuman.field_71071_by.func_70302_i_(); ++i) {
                ItemStack itemstack = entityhuman.field_71071_by.func_70301_a(i);

                if (this.d(entityhuman, bow, itemstack)) { // Paper
                    return itemstack;
                }
            }

            return ItemStack.field_190927_a;
        }
    }

    // Paper start
    protected boolean d(EntityPlayer player, ItemStack bow, ItemStack itemstack) {
        return itemstack.func_77973_b() instanceof ItemArrow && (
                !(player instanceof EntityPlayerMP) ||
                new com.destroystokyo.paper.event.player.PlayerReadyArrowEvent(
                        ((EntityPlayerMP) player).getBukkitEntity(),
                        CraftItemStack.asCraftMirror(bow),
                        CraftItemStack.asCraftMirror(itemstack)
                    ).callEvent());
        // Paper end
    }

    public void func_77615_a(ItemStack itemstack, World world, EntityLivingBase entityliving, int i) {
        if (entityliving instanceof EntityPlayer) {
            EntityPlayer entityhuman = (EntityPlayer) entityliving;
            boolean flag = entityhuman.field_71075_bZ.field_75098_d || EnchantmentHelper.func_77506_a(Enchantments.field_185312_x, itemstack) > 0;
            ItemStack itemstack1 = this.a(entityhuman, itemstack); // Paper

            if (!itemstack1.func_190926_b() || flag) {
                if (itemstack1.func_190926_b()) {
                    itemstack1 = new ItemStack(Items.field_151032_g);
                }

                int j = this.func_77626_a(itemstack) - i;
                float f = func_185059_b(j);

                if ((double) f >= 0.1D) {
                    boolean flag1 = flag && itemstack1.func_77973_b() == Items.field_151032_g;

                    boolean consumeArrow = true; // Paper
                    if (!world.field_72995_K) {
                        ItemArrow itemarrow = (ItemArrow) ((ItemArrow) (itemstack1.func_77973_b() instanceof ItemArrow ? itemstack1.func_77973_b() : Items.field_151032_g));
                        EntityArrow entityarrow = itemarrow.func_185052_a(world, itemstack1, (EntityLivingBase) entityhuman);

                        entityarrow.func_184547_a(entityhuman, entityhuman.field_70125_A, entityhuman.field_70177_z, 0.0F, f * 3.0F, 1.0F);
                        if (f == 1.0F) {
                            entityarrow.func_70243_d(true);
                        }

                        int k = EnchantmentHelper.func_77506_a(Enchantments.field_185309_u, itemstack);

                        if (k > 0) {
                            entityarrow.func_70239_b(entityarrow.func_70242_d() + (double) k * 0.5D + 0.5D);
                        }

                        int l = EnchantmentHelper.func_77506_a(Enchantments.field_185310_v, itemstack);

                        if (l > 0) {
                            entityarrow.func_70240_a(l);
                        }

                        if (EnchantmentHelper.func_77506_a(Enchantments.field_185311_w, itemstack) > 0) {
                        // CraftBukkit start - call EntityCombustEvent
                        EntityCombustEvent event = new EntityCombustEvent(entityarrow.getBukkitEntity(), 100);
                        entityarrow.field_70170_p.getServer().getPluginManager().callEvent(event);

                        if (!event.isCancelled()) {
                            entityarrow.func_70015_d(event.getDuration());
                        }
                        // CraftBukkit end
                        }
                        // CraftBukkit start
                        org.bukkit.event.entity.EntityShootBowEvent event = org.bukkit.craftbukkit.event.CraftEventFactory.callEntityShootBowEvent(entityhuman, itemstack, itemstack1, entityarrow, f); // Paper
                        if (event.isCancelled()) {
                            event.getProjectile().remove();
                            return;
                        }

                        itemstack.func_77972_a(1, entityhuman);
                        consumeArrow = event.getConsumeArrow(); // Paper
                        if (!consumeArrow || flag1 || (entityhuman.field_71075_bZ.field_75098_d && ((itemstack1.func_77973_b() == Items.field_185166_h) || (itemstack1.func_77973_b() == Items.field_185167_i)))) { // Paper - add !consumeArrow
                            entityarrow.field_70251_a = EntityArrow.PickupStatus.CREATIVE_ONLY;
                        }

                        if (event.getProjectile() == entityarrow.getBukkitEntity()) {
                            if (!world.func_72838_d(entityarrow)) {
                                if (entityhuman instanceof EntityPlayerMP) {
                                    ((EntityPlayerMP) entityhuman).getBukkitEntity().updateInventory();
                                }
                                return;
                            }
                        }
                        // CraftBukkit end
                    }

                    world.func_184148_a((EntityPlayer) null, entityhuman.field_70165_t, entityhuman.field_70163_u, entityhuman.field_70161_v, SoundEvents.field_187737_v, SoundCategory.PLAYERS, 1.0F, 1.0F / (ItemBow.field_77697_d.nextFloat() * 0.4F + 1.2F) + f * 0.5F);
                    if (!flag1 && !entityhuman.field_71075_bZ.field_75098_d && consumeArrow) { // Paper
                        itemstack1.func_190918_g(1);
                        if (itemstack1.func_190926_b()) {
                            entityhuman.field_71071_by.func_184437_d(itemstack1);
                        }
                    }

                    entityhuman.func_71029_a(StatList.func_188057_b((Item) this));
                }
            }
        }
    }

    public static float func_185059_b(int i) {
        float f = (float) i / 20.0F;

        f = (f * f + f * 2.0F) / 3.0F;
        if (f > 1.0F) {
            f = 1.0F;
        }

        return f;
    }

    public int func_77626_a(ItemStack itemstack) {
        return 72000;
    }

    public EnumAction func_77661_b(ItemStack itemstack) {
        return EnumAction.BOW;
    }

    public ActionResult<ItemStack> func_77659_a(World world, EntityPlayer entityhuman, EnumHand enumhand) {
        ItemStack itemstack = entityhuman.func_184586_b(enumhand);
        boolean flag = !this.a(entityhuman, itemstack).func_190926_b(); // Paper

        if (!entityhuman.field_71075_bZ.field_75098_d && !flag) {
            return flag ? new ActionResult(EnumActionResult.PASS, itemstack) : new ActionResult(EnumActionResult.FAIL, itemstack);
        } else {
            entityhuman.func_184598_c(enumhand);
            return new ActionResult(EnumActionResult.SUCCESS, itemstack);
        }
    }

    public int func_77619_b() {
        return 1;
    }
}
