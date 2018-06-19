package org.bukkit.craftbukkit.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.minecraft.util.DamageSource;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityDragonFireball;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityLlamaSpit;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.entity.projectile.EntityShulkerBullet;
import net.minecraft.entity.projectile.EntitySmallFireball;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.entity.item.EntityExpBottle;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.entity.projectile.EntitySpectralArrow;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.projectile.EntityWitherSkull;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.Potion;

import org.apache.commons.lang.Validate;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.inventory.CraftEntityEquipment;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.craftbukkit.potion.CraftPotionUtil;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.DragonFireball;
import org.bukkit.entity.Egg;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Fish;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.LingeringPotion;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.LlamaSpit;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.ShulkerBullet;
import org.bukkit.entity.SmallFireball;
import org.bukkit.entity.Snowball;
import org.bukkit.entity.SpectralArrow;
import org.bukkit.entity.ThrownExpBottle;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.entity.TippedArrow;
import org.bukkit.entity.WitherSkull;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;

import net;

public class CraftLivingEntity extends CraftEntity implements LivingEntity {
    private CraftEntityEquipment equipment;

    public CraftLivingEntity(final CraftServer server, final EntityLivingBase entity) {
        super(server, entity);

        if (entity instanceof EntityLiving || entity instanceof EntityArmorStand) {
            equipment = new CraftEntityEquipment(this);
        }
    }

    public double getHealth() {
        return Math.min(Math.max(0, getHandle().func_110143_aJ()), getMaxHealth());
    }

    public void setHealth(double health) {
        health = (float) health;
        if ((health < 0) || (health > getMaxHealth())) {
            // Paper - Be more informative
            throw new IllegalArgumentException("Health must be between 0 and " + getMaxHealth() + ", but was " + health
                    + ". (attribute base value: " + this.getHandle().func_110148_a(SharedMonsterAttributes.field_111267_a).func_111125_b()
                    + (this instanceof CraftPlayer ? ", player: " + this.getName() + ')' : ')'));
        }

        getHandle().func_70606_j((float) health);

        if (health == 0) {
            getHandle().func_70645_a(DamageSource.field_76377_j);
        }
    }

    public double getMaxHealth() {
        return getHandle().func_110138_aP();
    }

    public void setMaxHealth(double amount) {
        Validate.isTrue(amount > 0, "Max health must be greater than 0");

        getHandle().func_110148_a(SharedMonsterAttributes.field_111267_a).func_111128_a(amount);

        if (getHealth() > amount) {
            setHealth(amount);
        }
    }

    public void resetMaxHealth() {
        setMaxHealth(getHandle().func_110148_a(SharedMonsterAttributes.field_111267_a).func_111123_a().func_111110_b());
    }

    public double getEyeHeight() {
        return getHandle().func_70047_e();
    }

    public double getEyeHeight(boolean ignorePose) {
        return getEyeHeight();
    }

    private List<Block> getLineOfSight(Set<Material> transparent, int maxDistance, int maxLength) {
        if (maxDistance > 120) {
            maxDistance = 120;
        }
        ArrayList<Block> blocks = new ArrayList<Block>();
        Iterator<Block> itr = new BlockIterator(this, maxDistance);
        while (itr.hasNext()) {
            Block block = itr.next();
            blocks.add(block);
            if (maxLength != 0 && blocks.size() > maxLength) {
                blocks.remove(0);
            }
            Material material = block.getType();
            if (transparent == null) {
                if (!material.equals(Material.AIR)) {
                    break;
                }
            } else {
                if (!transparent.contains(material)) {
                    break;
                }
            }
        }
        return blocks;
    }

    public List<Block> getLineOfSight(Set<Material> transparent, int maxDistance) {
        return getLineOfSight(transparent, maxDistance, 0);
    }

    public Block getTargetBlock(Set<Material> transparent, int maxDistance) {
        List<Block> blocks = getLineOfSight(transparent, maxDistance, 1);
        return blocks.get(0);
    }

    public List<Block> getLastTwoTargetBlocks(Set<Material> transparent, int maxDistance) {
        return getLineOfSight(transparent, maxDistance, 2);
    }

    public int getRemainingAir() {
        return getHandle().func_70086_ai();
    }

    public void setRemainingAir(int ticks) {
        getHandle().func_70050_g(ticks);
    }

    public int getMaximumAir() {
        return getHandle().maxAirTicks;
    }

    public void setMaximumAir(int ticks) {
        getHandle().maxAirTicks = ticks;
    }

    public void damage(double amount) {
        damage(amount, null);
    }

    public void damage(double amount, org.bukkit.entity.Entity source) {
        DamageSource reason = DamageSource.field_76377_j;

        if (source instanceof HumanEntity) {
            reason = DamageSource.func_76365_a(((CraftHumanEntity) source).getHandle());
        } else if (source instanceof LivingEntity) {
            reason = DamageSource.func_76358_a(((CraftLivingEntity) source).getHandle());
        }

        entity.func_70097_a(reason, (float) amount);
    }

    public Location getEyeLocation() {
        Location loc = getLocation();
        loc.setY(loc.getY() + getEyeHeight());
        return loc;
    }

    public int getMaximumNoDamageTicks() {
        return getHandle().field_70771_an;
    }

    public void setMaximumNoDamageTicks(int ticks) {
        getHandle().field_70771_an = ticks;
    }

    public double getLastDamage() {
        return getHandle().field_110153_bc;
    }

    public void setLastDamage(double damage) {
        getHandle().field_110153_bc = (float) damage;
    }

    public int getNoDamageTicks() {
        return getHandle().field_70172_ad;
    }

    public void setNoDamageTicks(int ticks) {
        getHandle().field_70172_ad = ticks;
    }

    @Override
    public EntityLivingBase getHandle() {
        return (EntityLivingBase) entity;
    }

    public void setHandle(final EntityLivingBase entity) {
        super.setHandle(entity);
    }

    @Override
    public String toString() {
        return "CraftLivingEntity{" + "id=" + getEntityId() + '}';
    }

    public Player getKiller() {
        return getHandle().field_70717_bb == null ? null : (Player) getHandle().field_70717_bb.getBukkitEntity();
    }

    // Paper start
    @Override
    public void setKiller(Player killer) {
        minecraft.entity.player.EntityPlayerMP entityPlayer = killer == null ? null : ((CraftPlayer) killer).getHandle();
        getHandle().field_70717_bb = entityPlayer;
        getHandle().field_70755_b = entityPlayer;
        getHandle().field_70718_bc = entityPlayer == null ? 0 : 100; // 100 value taken from EntityLiving#damageEntity
    }
    // Paper end

    public boolean addPotionEffect(PotionEffect effect) {
        return addPotionEffect(effect, false);
    }

    public boolean addPotionEffect(PotionEffect effect, boolean force) {
        if (hasPotionEffect(effect.getType())) {
            if (!force) {
                return false;
            }
            removePotionEffect(effect.getType());
        }
        getHandle().func_70690_d(new PotionEffect(Potion.func_188412_a(effect.getType().getId()), effect.getDuration(), effect.getAmplifier(), effect.isAmbient(), effect.hasParticles()));
        return true;
    }

    public boolean addPotionEffects(Collection<PotionEffect> effects) {
        boolean success = true;
        for (PotionEffect effect : effects) {
            success &= addPotionEffect(effect);
        }
        return success;
    }

    public boolean hasPotionEffect(PotionEffectType type) {
        return getHandle().func_70644_a(Potion.func_188412_a(type.getId()));
    }

    @Override
    public PotionEffect getPotionEffect(PotionEffectType type) {
        PotionEffect handle = getHandle().func_70660_b(Potion.func_188412_a(type.getId()));
        return (handle == null) ? null : new PotionEffect(PotionEffectType.getById(Potion.func_188409_a(handle.func_188419_a())), handle.func_76459_b(), handle.func_76458_c(), handle.func_82720_e(), handle.func_188418_e());
    }

    public void removePotionEffect(PotionEffectType type) {
        getHandle().func_184589_d(Potion.func_188412_a(type.getId()));
    }

    public Collection<PotionEffect> getActivePotionEffects() {
        List<PotionEffect> effects = new ArrayList<PotionEffect>();
        for (PotionEffect handle : getHandle().field_70713_bf.values()) {
            effects.add(new PotionEffect(PotionEffectType.getById(Potion.func_188409_a(handle.func_188419_a())), handle.func_76459_b(), handle.func_76458_c(), handle.func_82720_e(), handle.func_188418_e()));
        }
        return effects;
    }

    public <T extends Projectile> T launchProjectile(Class<? extends T> projectile) {
        return launchProjectile(projectile, null);
    }

    @SuppressWarnings("unchecked")
    public <T extends Projectile> T launchProjectile(Class<? extends T> projectile, Vector velocity) {
        net.minecraft.world.World world = ((CraftWorld) getWorld()).getHandle();
        net.minecraft.entity.Entity launch = null;

        if (Snowball.class.isAssignableFrom(projectile)) {
            launch = new EntitySnowball(world, getHandle());
            ((EntityThrowable) launch).func_184538_a(getHandle(), getHandle().field_70125_A, getHandle().field_70177_z, 0.0F, 1.5F, 1.0F); // ItemSnowball
        } else if (Egg.class.isAssignableFrom(projectile)) {
            launch = new EntityEgg(world, getHandle());
            ((EntityThrowable) launch).func_184538_a(getHandle(), getHandle().field_70125_A, getHandle().field_70177_z, 0.0F, 1.5F, 1.0F); // ItemEgg
        } else if (EnderPearl.class.isAssignableFrom(projectile)) {
            launch = new EntityEnderPearl(world, getHandle());
            ((EntityThrowable) launch).func_184538_a(getHandle(), getHandle().field_70125_A, getHandle().field_70177_z, 0.0F, 1.5F, 1.0F); // ItemEnderPearl
        } else if (Arrow.class.isAssignableFrom(projectile)) {
            if (TippedArrow.class.isAssignableFrom(projectile)) {
                launch = new EntityTippedArrow(world, getHandle());
                ((EntityTippedArrow) launch).setType(CraftPotionUtil.fromBukkit(new PotionData(PotionType.WATER, false, false)));
            } else if (SpectralArrow.class.isAssignableFrom(projectile)) {
                launch = new EntitySpectralArrow(world, getHandle());
            } else {
                launch = new EntityTippedArrow(world, getHandle());
            }
            ((EntityArrow) launch).func_184547_a(getHandle(), getHandle().field_70125_A, getHandle().field_70177_z, 0.0F, 3.0F, 1.0F); // ItemBow
        } else if (ThrownPotion.class.isAssignableFrom(projectile)) {
            if (LingeringPotion.class.isAssignableFrom(projectile)) {
                launch = new EntityPotion(world, getHandle(), CraftItemStack.asNMSCopy(new ItemStack(org.bukkit.Material.LINGERING_POTION, 1)));
            } else {
                launch = new EntityPotion(world, getHandle(), CraftItemStack.asNMSCopy(new ItemStack(org.bukkit.Material.SPLASH_POTION, 1)));
            }
            ((EntityThrowable) launch).func_184538_a(getHandle(), getHandle().field_70125_A, getHandle().field_70177_z, -20.0F, 0.5F, 1.0F); // ItemSplashPotion
        } else if (ThrownExpBottle.class.isAssignableFrom(projectile)) {
            launch = new EntityExpBottle(world, getHandle());
            ((EntityThrowable) launch).func_184538_a(getHandle(), getHandle().field_70125_A, getHandle().field_70177_z, -20.0F, 0.7F, 1.0F); // ItemExpBottle
        } else if (Fish.class.isAssignableFrom(projectile) && getHandle() instanceof EntityPlayer) {
            launch = new EntityFishHook(world, (EntityPlayer) getHandle());
        } else if (Fireball.class.isAssignableFrom(projectile)) {
            Location location = getEyeLocation();
            Vector direction = location.getDirection().multiply(10);

            if (SmallFireball.class.isAssignableFrom(projectile)) {
                launch = new EntitySmallFireball(world, getHandle(), direction.getX(), direction.getY(), direction.getZ());
            } else if (WitherSkull.class.isAssignableFrom(projectile)) {
                launch = new EntityWitherSkull(world, getHandle(), direction.getX(), direction.getY(), direction.getZ());
            } else if (DragonFireball.class.isAssignableFrom(projectile)) {
                launch = new EntityDragonFireball(world, getHandle(), direction.getX(), direction.getY(), direction.getZ());
            } else {
                launch = new EntityLargeFireball(world, getHandle(), direction.getX(), direction.getY(), direction.getZ());
            }

            ((EntityFireball) launch).projectileSource = this;
            launch.func_70012_b(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        } else if (LlamaSpit.class.isAssignableFrom(projectile)) {
            Location location = getEyeLocation();
            Vector direction = location.getDirection();

            launch = new EntityLlamaSpit(world);

            ((EntityLlamaSpit) launch).field_190539_a = getHandle();
            ((EntityLlamaSpit) launch).func_70186_c(direction.getX(), direction.getY(), direction.getZ(), 1.5F, 10.0F); // EntityLlama
            launch.func_70012_b(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        } else if (ShulkerBullet.class.isAssignableFrom(projectile)) {
            Location location = getEyeLocation();

            launch = new EntityShulkerBullet(world, getHandle(), null, null);
            launch.func_70012_b(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        }

        Validate.notNull(launch, "Projectile not supported");

        if (velocity != null) {
            ((T) launch.getBukkitEntity()).setVelocity(velocity);
        }

        world.func_72838_d(launch);
        return (T) launch.getBukkitEntity();
    }

    public EntityType getType() {
        return EntityType.UNKNOWN;
    }

    public boolean hasLineOfSight(Entity other) {
        return getHandle().func_70685_l(((CraftEntity) other).getHandle());
    }

    public boolean getRemoveWhenFarAway() {
        return getHandle() instanceof EntityLiving && !((EntityLiving) getHandle()).field_82179_bU;
    }

    public void setRemoveWhenFarAway(boolean remove) {
        if (getHandle() instanceof EntityLiving) {
            ((EntityLiving) getHandle()).field_82179_bU = !remove;
        }
    }

    public EntityEquipment getEquipment() {
        return equipment;
    }

    public void setCanPickupItems(boolean pickup) {
        getHandle().canPickUpLoot = pickup;
    }

    public boolean getCanPickupItems() {
        return getHandle().canPickUpLoot;
    }

    @Override
    public boolean teleport(Location location, PlayerTeleportEvent.TeleportCause cause) {
        if (getHealth() == 0) {
            return false;
        }

        return super.teleport(location, cause);
    }

    public boolean isLeashed() {
        if (!(getHandle() instanceof EntityLiving)) {
            return false;
        }
        return ((EntityLiving) getHandle()).func_110166_bE() != null;
    }

    public Entity getLeashHolder() throws IllegalStateException {
        if (!isLeashed()) {
            throw new IllegalStateException("Entity not leashed");
        }
        return ((EntityLiving) getHandle()).func_110166_bE().getBukkitEntity();
    }

    private boolean unleash() {
        if (!isLeashed()) {
            return false;
        }
        ((EntityLiving) getHandle()).func_110160_i(true, false);
        return true;
    }

    public boolean setLeashHolder(Entity holder) {
        if ((getHandle() instanceof EntityWither) || !(getHandle() instanceof EntityLiving)) {
            return false;
        }

        if (holder == null) {
            return unleash();
        }

        if (holder.isDead()) {
            return false;
        }

        unleash();
        ((EntityLiving) getHandle()).func_110162_b(((CraftEntity) holder).getHandle(), true);
        return true;
    }

    @Override
    public boolean isGliding() {
        return getHandle().func_70083_f(7);
    }

    @Override
    public void setGliding(boolean gliding) {
        getHandle().func_70052_a(7, gliding);
    }

    @Override
    public AttributeInstance getAttribute(Attribute attribute) {
        return getHandle().craftAttributes.getAttribute(attribute);
    }

    @Override
    public void setAI(boolean ai) {
        if (this.getHandle() instanceof EntityLiving) {
            ((EntityLiving) this.getHandle()).func_94061_f(!ai);
        }
    }

    @Override
    public boolean hasAI() {
        return (this.getHandle() instanceof EntityLiving) ? !((EntityLiving) this.getHandle()).func_175446_cd(): false;
    }

    @Override
    public void setCollidable(boolean collidable) {
        getHandle().collides = collidable;
    }

    @Override
    public boolean isCollidable() {
        return getHandle().collides;
    }

    // Paper start
    @Override
    public int getArrowsStuck() {
        return getHandle().func_85035_bI();
    }

    @Override
    public void setArrowsStuck(int arrows) {
        getHandle().func_85034_r(arrows);
    }

    @Override
    public int getShieldBlockingDelay() {
        return getHandle().getShieldBlockingDelay();
    }

    @Override
    public void setShieldBlockingDelay(int delay) {
        getHandle().setShieldBlockingDelay(delay);
    }
    // Paper end
}
