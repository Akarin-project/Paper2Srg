package net.minecraft.item;


import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.init.SoundEvents;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import org.bukkit.event.player.PlayerFishEvent;

public class ItemFishingRod extends Item {

    public ItemFishingRod() {
        this.func_77656_e(64);
        this.func_77625_d(1);
        this.func_77637_a(CreativeTabs.field_78040_i);
        this.func_185043_a(new ResourceLocation("cast"), new IItemPropertyGetter() {
        });
    }

    public ActionResult<ItemStack> func_77659_a(World world, EntityPlayer entityhuman, EnumHand enumhand) {
        ItemStack itemstack = entityhuman.func_184586_b(enumhand);

        if (entityhuman.field_71104_cf != null) {
            int i = entityhuman.field_71104_cf.func_146034_e();

            itemstack.func_77972_a(i, entityhuman);
            entityhuman.func_184609_a(enumhand);
            world.func_184148_a((EntityPlayer) null, entityhuman.field_70165_t, entityhuman.field_70163_u, entityhuman.field_70161_v, SoundEvents.field_193780_J, SoundCategory.NEUTRAL, 1.0F, 0.4F / (ItemFishingRod.field_77697_d.nextFloat() * 0.4F + 0.8F));
        } else {
            // world.a((EntityHuman) null, entityhuman.locX, entityhuman.locY, entityhuman.locZ, SoundEffects.L, SoundCategory.NEUTRAL, 0.5F, 0.4F / (ItemFishingRod.j.nextFloat() * 0.4F + 0.8F)); // CraftBukkit - moved down
            if (!world.field_72995_K) {
                EntityFishHook entityfishinghook = new EntityFishHook(world, entityhuman);
                int j = EnchantmentHelper.func_191528_c(itemstack);

                if (j > 0) {
                    entityfishinghook.func_191516_a(j);
                }

                int k = EnchantmentHelper.func_191529_b(itemstack);

                if (k > 0) {
                    entityfishinghook.func_191517_b(k);
                }

                // CraftBukkit start
                PlayerFishEvent playerFishEvent = new PlayerFishEvent((org.bukkit.entity.Player) entityhuman.getBukkitEntity(), null, (org.bukkit.entity.Fish) entityfishinghook.getBukkitEntity(), PlayerFishEvent.State.FISHING);
                world.getServer().getPluginManager().callEvent(playerFishEvent);

                if (playerFishEvent.isCancelled()) {
                    entityhuman.field_71104_cf = null;
                    return new ActionResult(EnumActionResult.PASS, itemstack);
                }
                world.func_184148_a((EntityPlayer) null, entityhuman.field_70165_t, entityhuman.field_70163_u, entityhuman.field_70161_v, SoundEvents.field_187612_G, SoundCategory.NEUTRAL, 0.5F, 0.4F / (ItemFishingRod.field_77697_d.nextFloat() * 0.4F + 0.8F));
                // CraftBukkit end

                world.func_72838_d(entityfishinghook);
            }

            entityhuman.func_184609_a(enumhand);
            entityhuman.func_71029_a(StatList.func_188057_b((Item) this));
        }

        return new ActionResult(EnumActionResult.SUCCESS, itemstack);
    }

    public int func_77619_b() {
        return 1;
    }
}
