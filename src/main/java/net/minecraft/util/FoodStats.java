package net.minecraft.util;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateHealth;
import net.minecraft.world.EnumDifficulty;


public class FoodStats {

    public int foodLevel = 20;
    public float foodSaturationLevel = 5.0F;
    public float foodExhaustionLevel;
    private int foodTimer;
    private EntityPlayer entityhuman; // CraftBukkit
    private int prevFoodLevel = 20;

    public FoodStats() { throw new AssertionError("Whoopsie, we missed the bukkit."); } // CraftBukkit start - throw an error

    // CraftBukkit start - added EntityHuman constructor
    public FoodStats(EntityPlayer entityhuman) {
        org.apache.commons.lang.Validate.notNull(entityhuman);
        this.entityhuman = entityhuman;
    }
    // CraftBukkit end

    public void addStats(int i, float f) {
        this.foodLevel = Math.min(i + this.foodLevel, 20);
        this.foodSaturationLevel = Math.min(this.foodSaturationLevel + (float) i * f * 2.0F, (float) this.foodLevel);
    }

    public void addStats(ItemFood itemfood, ItemStack itemstack) {
        // CraftBukkit start
        int oldFoodLevel = foodLevel;

        org.bukkit.event.entity.FoodLevelChangeEvent event = org.bukkit.craftbukkit.event.CraftEventFactory.callFoodLevelChangeEvent(entityhuman, itemfood.getHealAmount(itemstack) + oldFoodLevel);

        if (!event.isCancelled()) {
            this.addStats(event.getFoodLevel() - oldFoodLevel, itemfood.getSaturationModifier(itemstack));
        }

        ((EntityPlayerMP) entityhuman).getBukkitEntity().sendHealthUpdate();
        // CraftBukkit end
    }

    public void onUpdate(EntityPlayer entityhuman) {
        EnumDifficulty enumdifficulty = entityhuman.world.getDifficulty();

        this.prevFoodLevel = this.foodLevel;
        if (this.foodExhaustionLevel > 4.0F) {
            this.foodExhaustionLevel -= 4.0F;
            if (this.foodSaturationLevel > 0.0F) {
                this.foodSaturationLevel = Math.max(this.foodSaturationLevel - 1.0F, 0.0F);
            } else if (enumdifficulty != EnumDifficulty.PEACEFUL) {
                // CraftBukkit start
                org.bukkit.event.entity.FoodLevelChangeEvent event = org.bukkit.craftbukkit.event.CraftEventFactory.callFoodLevelChangeEvent(entityhuman, Math.max(this.foodLevel - 1, 0));

                if (!event.isCancelled()) {
                    this.foodLevel = event.getFoodLevel();
                }

                ((EntityPlayerMP) entityhuman).connection.sendPacket(new SPacketUpdateHealth(((EntityPlayerMP) entityhuman).getBukkitEntity().getScaledHealth(), this.foodLevel, this.foodSaturationLevel));
                // CraftBukkit end
            }
        }

        boolean flag = entityhuman.world.getGameRules().getBoolean("naturalRegeneration");

        if (flag && this.foodSaturationLevel > 0.0F && entityhuman.shouldHeal() && this.foodLevel >= 20) {
            ++this.foodTimer;
            if (this.foodTimer >= 10) {
                float f = Math.min(this.foodSaturationLevel, 6.0F);

                entityhuman.heal(f / 6.0F, org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason.SATIATED, true); // CraftBukkit - added RegainReason // Paper - This is fast regen
                this.addExhaustion(f);
                this.foodTimer = 0;
            }
        } else if (flag && this.foodLevel >= 18 && entityhuman.shouldHeal()) {
            ++this.foodTimer;
            if (this.foodTimer >= 80) {
                entityhuman.heal(1.0F, org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason.SATIATED); // CraftBukkit - added RegainReason
                this.addExhaustion(entityhuman.world.spigotConfig.regenExhaustion); // Spigot - Change to use configurable value
                this.foodTimer = 0;
            }
        } else if (this.foodLevel <= 0) {
            ++this.foodTimer;
            if (this.foodTimer >= 80) {
                if (entityhuman.getHealth() > 10.0F || enumdifficulty == EnumDifficulty.HARD || entityhuman.getHealth() > 1.0F && enumdifficulty == EnumDifficulty.NORMAL) {
                    entityhuman.attackEntityFrom(DamageSource.STARVE, 1.0F);
                }

                this.foodTimer = 0;
            }
        } else {
            this.foodTimer = 0;
        }

    }

    public void readNBT(NBTTagCompound nbttagcompound) {
        if (nbttagcompound.hasKey("foodLevel", 99)) {
            this.foodLevel = nbttagcompound.getInteger("foodLevel");
            this.foodTimer = nbttagcompound.getInteger("foodTickTimer");
            this.foodSaturationLevel = nbttagcompound.getFloat("foodSaturationLevel");
            this.foodExhaustionLevel = nbttagcompound.getFloat("foodExhaustionLevel");
        }

    }

    public void writeNBT(NBTTagCompound nbttagcompound) {
        nbttagcompound.setInteger("foodLevel", this.foodLevel);
        nbttagcompound.setInteger("foodTickTimer", this.foodTimer);
        nbttagcompound.setFloat("foodSaturationLevel", this.foodSaturationLevel);
        nbttagcompound.setFloat("foodExhaustionLevel", this.foodExhaustionLevel);
    }

    public int getFoodLevel() {
        return this.foodLevel;
    }

    public boolean needFood() {
        return this.foodLevel < 20;
    }

    public void addExhaustion(float f) {
        this.foodExhaustionLevel = Math.min(this.foodExhaustionLevel + f, 40.0F);
    }

    public float getSaturationLevel() {
        return this.foodSaturationLevel;
    }

    public void setFoodLevel(int i) {
        this.foodLevel = i;
    }
}
