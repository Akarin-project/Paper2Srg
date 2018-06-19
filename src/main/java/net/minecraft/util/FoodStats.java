package net.minecraft.util;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateHealth;
import net.minecraft.world.EnumDifficulty;


public class FoodStats {

    public int field_75127_a = 20;
    public float field_75125_b = 5.0F;
    public float field_75126_c;
    private int field_75123_d;
    private EntityPlayer entityhuman; // CraftBukkit
    private int field_75124_e = 20;

    public FoodStats() { throw new AssertionError("Whoopsie, we missed the bukkit."); } // CraftBukkit start - throw an error

    // CraftBukkit start - added EntityHuman constructor
    public FoodStats(EntityPlayer entityhuman) {
        org.apache.commons.lang.Validate.notNull(entityhuman);
        this.entityhuman = entityhuman;
    }
    // CraftBukkit end

    public void func_75122_a(int i, float f) {
        this.field_75127_a = Math.min(i + this.field_75127_a, 20);
        this.field_75125_b = Math.min(this.field_75125_b + (float) i * f * 2.0F, (float) this.field_75127_a);
    }

    public void func_151686_a(ItemFood itemfood, ItemStack itemstack) {
        // CraftBukkit start
        int oldFoodLevel = field_75127_a;

        org.bukkit.event.entity.FoodLevelChangeEvent event = org.bukkit.craftbukkit.event.CraftEventFactory.callFoodLevelChangeEvent(entityhuman, itemfood.func_150905_g(itemstack) + oldFoodLevel);

        if (!event.isCancelled()) {
            this.func_75122_a(event.getFoodLevel() - oldFoodLevel, itemfood.func_150906_h(itemstack));
        }

        ((EntityPlayerMP) entityhuman).getBukkitEntity().sendHealthUpdate();
        // CraftBukkit end
    }

    public void func_75118_a(EntityPlayer entityhuman) {
        EnumDifficulty enumdifficulty = entityhuman.field_70170_p.func_175659_aa();

        this.field_75124_e = this.field_75127_a;
        if (this.field_75126_c > 4.0F) {
            this.field_75126_c -= 4.0F;
            if (this.field_75125_b > 0.0F) {
                this.field_75125_b = Math.max(this.field_75125_b - 1.0F, 0.0F);
            } else if (enumdifficulty != EnumDifficulty.PEACEFUL) {
                // CraftBukkit start
                org.bukkit.event.entity.FoodLevelChangeEvent event = org.bukkit.craftbukkit.event.CraftEventFactory.callFoodLevelChangeEvent(entityhuman, Math.max(this.field_75127_a - 1, 0));

                if (!event.isCancelled()) {
                    this.field_75127_a = event.getFoodLevel();
                }

                ((EntityPlayerMP) entityhuman).field_71135_a.func_147359_a(new SPacketUpdateHealth(((EntityPlayerMP) entityhuman).getBukkitEntity().getScaledHealth(), this.field_75127_a, this.field_75125_b));
                // CraftBukkit end
            }
        }

        boolean flag = entityhuman.field_70170_p.func_82736_K().func_82766_b("naturalRegeneration");

        if (flag && this.field_75125_b > 0.0F && entityhuman.func_70996_bM() && this.field_75127_a >= 20) {
            ++this.field_75123_d;
            if (this.field_75123_d >= 10) {
                float f = Math.min(this.field_75125_b, 6.0F);

                entityhuman.heal(f / 6.0F, org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason.SATIATED, true); // CraftBukkit - added RegainReason // Paper - This is fast regen
                this.func_75113_a(f);
                this.field_75123_d = 0;
            }
        } else if (flag && this.field_75127_a >= 18 && entityhuman.func_70996_bM()) {
            ++this.field_75123_d;
            if (this.field_75123_d >= 80) {
                entityhuman.heal(1.0F, org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason.SATIATED); // CraftBukkit - added RegainReason
                this.func_75113_a(entityhuman.field_70170_p.spigotConfig.regenExhaustion); // Spigot - Change to use configurable value
                this.field_75123_d = 0;
            }
        } else if (this.field_75127_a <= 0) {
            ++this.field_75123_d;
            if (this.field_75123_d >= 80) {
                if (entityhuman.func_110143_aJ() > 10.0F || enumdifficulty == EnumDifficulty.HARD || entityhuman.func_110143_aJ() > 1.0F && enumdifficulty == EnumDifficulty.NORMAL) {
                    entityhuman.func_70097_a(DamageSource.field_76366_f, 1.0F);
                }

                this.field_75123_d = 0;
            }
        } else {
            this.field_75123_d = 0;
        }

    }

    public void func_75112_a(NBTTagCompound nbttagcompound) {
        if (nbttagcompound.func_150297_b("foodLevel", 99)) {
            this.field_75127_a = nbttagcompound.func_74762_e("foodLevel");
            this.field_75123_d = nbttagcompound.func_74762_e("foodTickTimer");
            this.field_75125_b = nbttagcompound.func_74760_g("foodSaturationLevel");
            this.field_75126_c = nbttagcompound.func_74760_g("foodExhaustionLevel");
        }

    }

    public void func_75117_b(NBTTagCompound nbttagcompound) {
        nbttagcompound.func_74768_a("foodLevel", this.field_75127_a);
        nbttagcompound.func_74768_a("foodTickTimer", this.field_75123_d);
        nbttagcompound.func_74776_a("foodSaturationLevel", this.field_75125_b);
        nbttagcompound.func_74776_a("foodExhaustionLevel", this.field_75126_c);
    }

    public int func_75116_a() {
        return this.field_75127_a;
    }

    public boolean func_75121_c() {
        return this.field_75127_a < 20;
    }

    public void func_75113_a(float f) {
        this.field_75126_c = Math.min(this.field_75126_c + f, 40.0F);
    }

    public float func_75115_e() {
        return this.field_75125_b;
    }

    public void func_75114_a(int i) {
        this.field_75127_a = i;
    }
}
