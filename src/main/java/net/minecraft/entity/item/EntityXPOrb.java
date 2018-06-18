package net.minecraft.entity.item;

// CraftBukkit start
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Enchantments;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

// CraftBukkit end

public class EntityXPOrb extends Entity {

    public int xpColor;
    public int xpOrbAge;
    public int delayBeforeCanPickup;
    private int xpOrbHealth = 5;
    public int xpValue;
    private EntityPlayer closestPlayer;
    private int xpTargetColor;
    // Paper start
    public java.util.UUID sourceEntityId;
    public java.util.UUID triggerEntityId;
    public org.bukkit.entity.ExperienceOrb.SpawnReason spawnReason = org.bukkit.entity.ExperienceOrb.SpawnReason.UNKNOWN;

    private void loadPaperNBT(NBTTagCompound nbttagcompound) {
        if (!nbttagcompound.hasKey("Paper.ExpData", 10)) { // 10 = compound
            return;
        }
        NBTTagCompound comp = nbttagcompound.getCompoundTag("Paper.ExpData");
        if (comp.hasUUID("source")) {
            this.sourceEntityId = comp.getUUID("source");
        }
        if (comp.hasUUID("trigger")) {
            this.triggerEntityId = comp.getUUID("trigger");
        }
        if (comp.hasKey("reason")) {
            String reason = comp.getString("reason");
            try {
                spawnReason = org.bukkit.entity.ExperienceOrb.SpawnReason.valueOf(reason);
            } catch (Exception e) {
                this.world.getServer().getLogger().warning("Invalid spawnReason set for experience orb: " + e.getMessage() + " - " + reason);
            }
        }
    }
    private void savePaperNBT(NBTTagCompound nbttagcompound) {
        NBTTagCompound comp = new NBTTagCompound();
        if (sourceEntityId != null) {
            comp.setUUID("source", sourceEntityId);
        }
        if (triggerEntityId != null) {
            comp.setUUID("trigger", triggerEntityId);
        }
        if (spawnReason != null && spawnReason != org.bukkit.entity.ExperienceOrb.SpawnReason.UNKNOWN) {
            comp.setString("reason", spawnReason.name());
        }
        nbttagcompound.setTag("Paper.ExpData", comp);
    }
    public EntityXPOrb(World world, double d0, double d1, double d2, int i, org.bukkit.entity.ExperienceOrb.SpawnReason reason, Entity triggerId) {
        this(world, d0, d1, d2, i, reason, triggerId, null);
    }

    public EntityXPOrb(World world, double d0, double d1, double d2, int i, org.bukkit.entity.ExperienceOrb.SpawnReason reason, Entity triggerId, Entity sourceId) {
        super(world);
        this.sourceEntityId = sourceId != null ? sourceId.getUniqueID() : null;
        this.triggerEntityId = triggerId != null ? triggerId.getUniqueID() : null;
        this.spawnReason = reason != null ? reason : org.bukkit.entity.ExperienceOrb.SpawnReason.UNKNOWN;
        // Paper end
        this.setSize(0.5F, 0.5F);
        this.setPosition(d0, d1, d2);
        this.rotationYaw = (float) (Math.random() * 360.0D);
        this.motionX = (double) ((float) (Math.random() * 0.20000000298023224D - 0.10000000149011612D) * 2.0F);
        this.motionY = (double) ((float) (Math.random() * 0.2D) * 2.0F);
        this.motionZ = (double) ((float) (Math.random() * 0.20000000298023224D - 0.10000000149011612D) * 2.0F);
        this.xpValue = i;
    }

    protected boolean canTriggerWalking() {
        return false;
    }

    public EntityXPOrb(World world) {
        super(world);
        this.setSize(0.25F, 0.25F);
    }

    protected void entityInit() {}

    public void onUpdate() {
        super.onUpdate();
        EntityPlayer prevTarget = this.closestPlayer;// CraftBukkit - store old target
        if (this.delayBeforeCanPickup > 0) {
            --this.delayBeforeCanPickup;
        }

        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        if (!this.hasNoGravity()) {
            this.motionY -= 0.029999999329447746D;
        }

        if (this.world.getBlockState(new BlockPos(this)).getMaterial() == Material.LAVA) {
            this.motionY = 0.20000000298023224D;
            this.motionX = (double) ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F);
            this.motionZ = (double) ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F);
            this.playSound(SoundEvents.ENTITY_GENERIC_BURN, 0.4F, 2.0F + this.rand.nextFloat() * 0.4F);
        }

        this.pushOutOfBlocks(this.posX, (this.getEntityBoundingBox().minY + this.getEntityBoundingBox().maxY) / 2.0D, this.posZ);
        double d0 = 8.0D;

        if (this.xpTargetColor < this.xpColor - 20 + this.getEntityId() % 100) {
            if (this.closestPlayer == null || this.closestPlayer.getDistanceSq(this) > 64.0D) {
                this.closestPlayer = this.world.getClosestPlayerToEntity(this, 8.0D);
            }

            this.xpTargetColor = this.xpColor;
        }

        if (this.closestPlayer != null && this.closestPlayer.isSpectator()) {
            this.closestPlayer = null;
        }

        if (this.closestPlayer != null) {
            // CraftBukkit start
            boolean cancelled = false;
            if (this.closestPlayer != prevTarget) {
                EntityTargetLivingEntityEvent event = CraftEventFactory.callEntityTargetLivingEvent(this, closestPlayer, EntityTargetEvent.TargetReason.CLOSEST_PLAYER);
                EntityLivingBase target = event.getTarget() == null ? null : ((org.bukkit.craftbukkit.entity.CraftLivingEntity) event.getTarget()).getHandle();
                closestPlayer = target instanceof EntityPlayer ? (EntityPlayer) target : null;
                cancelled = event.isCancelled();
            }

            if (!cancelled && closestPlayer != null) {
            double d1 = (this.closestPlayer.posX - this.posX) / 8.0D;
            double d2 = (this.closestPlayer.posY + (double) this.closestPlayer.getEyeHeight() / 2.0D - this.posY) / 8.0D;
            double d3 = (this.closestPlayer.posZ - this.posZ) / 8.0D;
            double d4 = Math.sqrt(d1 * d1 + d2 * d2 + d3 * d3);
            double d5 = 1.0D - d4;

            if (d5 > 0.0D) {
                d5 *= d5;
                this.motionX += d1 / d4 * d5 * 0.1D;
                this.motionY += d2 / d4 * d5 * 0.1D;
                this.motionZ += d3 / d4 * d5 * 0.1D;
            }
            }
            // CraftBukkit end
        }

        this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
        float f = 0.98F;

        if (this.onGround) {
            f = this.world.getBlockState(new BlockPos(MathHelper.floor(this.posX), MathHelper.floor(this.getEntityBoundingBox().minY) - 1, MathHelper.floor(this.posZ))).getBlock().slipperiness * 0.98F;
        }

        this.motionX *= (double) f;
        this.motionY *= 0.9800000190734863D;
        this.motionZ *= (double) f;
        if (this.onGround) {
            this.motionY *= -0.8999999761581421D;
        }

        ++this.xpColor;
        ++this.xpOrbAge;
        if (this.xpOrbAge >= 6000) {
            this.setDead();
        }

    }

    public boolean handleWaterMovement() {
        return this.world.handleMaterialAcceleration(this.getEntityBoundingBox(), Material.WATER, (Entity) this);
    }

    protected void burn(int i) {
        this.attackEntityFrom(DamageSource.IN_FIRE, (float) i);
    }

    public boolean attackEntityFrom(DamageSource damagesource, float f) {
        if (this.isEntityInvulnerable(damagesource)) {
            return false;
        } else {
            this.markVelocityChanged();
            this.xpOrbHealth = (int) ((float) this.xpOrbHealth - f);
            if (this.xpOrbHealth <= 0) {
                this.setDead();
            }

            return false;
        }
    }

    public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
        nbttagcompound.setShort("Health", (short) this.xpOrbHealth);
        nbttagcompound.setShort("Age", (short) this.xpOrbAge);
        nbttagcompound.setShort("Value", (short) this.xpValue);
        savePaperNBT(nbttagcompound); // Paper
    }

    public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
        this.xpOrbHealth = nbttagcompound.getShort("Health");
        this.xpOrbAge = nbttagcompound.getShort("Age");
        this.xpValue = nbttagcompound.getShort("Value");
        loadPaperNBT(nbttagcompound); // Paper
    }

    public void onCollideWithPlayer(EntityPlayer entityhuman) {
        if (!this.world.isRemote) {
            if (this.delayBeforeCanPickup == 0 && entityhuman.xpCooldown == 0 && new com.destroystokyo.paper.event.player.PlayerPickupExperienceEvent(((EntityPlayerMP) entityhuman).getBukkitEntity(), (org.bukkit.entity.ExperienceOrb) this.getBukkitEntity()).callEvent()) { // Paper
                entityhuman.xpCooldown = 2;
                entityhuman.onItemPickup(this, 1);
                ItemStack itemstack = EnchantmentHelper.getEnchantedItem(Enchantments.MENDING, (EntityLivingBase) entityhuman);

                if (!itemstack.isEmpty() && itemstack.isItemDamaged()) {
                    int i = Math.min(this.xpToDurability(this.xpValue), itemstack.getItemDamage());

                    // CraftBukkit start
                    org.bukkit.event.player.PlayerItemMendEvent event = CraftEventFactory.callPlayerItemMendEvent(entityhuman, this, itemstack, i);
                    i = event.getRepairAmount();
                    if (!event.isCancelled()) {
                        this.xpValue -= this.durabilityToXp(i);
                        itemstack.setItemDamage(itemstack.getItemDamage() - i);
                    }
                    // CraftBukkit end
				}

                if (this.xpValue > 0) {
                    entityhuman.addExperience(CraftEventFactory.callPlayerExpChangeEvent(entityhuman, this).getAmount()); // CraftBukkit - this.value -> event.getAmount() // Paper - supply experience orb object
                }

                this.setDead();
            }

        }
    }

    public int durToXp(int i) { return durabilityToXp(i); } // Paper OBFHELPER
    private int durabilityToXp(int i) {
        return i / 2;
    }

    public int xpToDur(int i) { return xpToDurability(i); } // Paper OBFHELPER
    private int xpToDurability(int i) {
        return i * 2;
    }

    public int getXpValue() {
        return this.xpValue;
    }

    public static int getXPSplit(int i) {
        // CraftBukkit start
        if (i > 162670129) return i - 100000;
        if (i > 81335063) return 81335063;
        if (i > 40667527) return 40667527;
        if (i > 20333759) return 20333759;
        if (i > 10166857) return 10166857;
        if (i > 5083423) return 5083423;
        if (i > 2541701) return 2541701;
        if (i > 1270849) return 1270849;
        if (i > 635413) return 635413;
        if (i > 317701) return 317701;
        if (i > 158849) return 158849;
        if (i > 79423) return 79423;
        if (i > 39709) return 39709;
        if (i > 19853) return 19853;
        if (i > 9923) return 9923;
        if (i > 4957) return 4957;
        // CraftBukkit end
        return i >= 2477 ? 2477 : (i >= 1237 ? 1237 : (i >= 617 ? 617 : (i >= 307 ? 307 : (i >= 149 ? 149 : (i >= 73 ? 73 : (i >= 37 ? 37 : (i >= 17 ? 17 : (i >= 7 ? 7 : (i >= 3 ? 3 : 1)))))))));
    }

    public boolean canBeAttackedWithItem() {
        return false;
    }
}
