package net.minecraft.entity.item;

import com.google.common.base.Optional;
import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldProviderEnd;
import net.minecraft.world.end.DragonFightManager;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.event.entity.ExplosionPrimeEvent;

// CraftBukkit start
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.event.entity.ExplosionPrimeEvent;
// CraftBukkit end

public class EntityEnderCrystal extends Entity {

    private static final DataParameter<Optional<BlockPos>> BEAM_TARGET = EntityDataManager.createKey(EntityEnderCrystal.class, DataSerializers.OPTIONAL_BLOCK_POS);
    private static final DataParameter<Boolean> SHOW_BOTTOM = EntityDataManager.createKey(EntityEnderCrystal.class, DataSerializers.BOOLEAN);
    public int innerRotation;

    public EntityEnderCrystal(World world) {
        super(world);
        this.preventEntitySpawning = true;
        this.setSize(2.0F, 2.0F);
        this.innerRotation = this.rand.nextInt(100000);
    }

    public EntityEnderCrystal(World world, double d0, double d1, double d2) {
        this(world);
        this.setPosition(d0, d1, d2);
    }

    protected boolean canTriggerWalking() {
        return false;
    }

    protected void entityInit() {
        this.getDataManager().register(EntityEnderCrystal.BEAM_TARGET, Optional.absent());
        this.getDataManager().register(EntityEnderCrystal.SHOW_BOTTOM, Boolean.valueOf(true));
    }

    public void onUpdate() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        ++this.innerRotation;
        if (!this.world.isRemote) {
            BlockPos blockposition = new BlockPos(this);

            if (this.world.provider instanceof WorldProviderEnd && this.world.getBlockState(blockposition).getBlock() != Blocks.FIRE) {
                // CraftBukkit start
                if (!CraftEventFactory.callBlockIgniteEvent(this.world, blockposition.getX(), blockposition.getY(), blockposition.getZ(), this).isCancelled()) {
                    this.world.setBlockState(blockposition, Blocks.FIRE.getDefaultState());
                }
                // CraftBukkit end
            }
        }

    }

    protected void writeEntityToNBT(NBTTagCompound nbttagcompound) {
        if (this.getBeamTarget() != null) {
            nbttagcompound.setTag("BeamTarget", NBTUtil.createPosTag(this.getBeamTarget()));
        }

        nbttagcompound.setBoolean("ShowBottom", this.shouldShowBottom());
    }

    protected void readEntityFromNBT(NBTTagCompound nbttagcompound) {
        if (nbttagcompound.hasKey("BeamTarget", 10)) {
            this.setBeamTarget(NBTUtil.getPosFromTag(nbttagcompound.getCompoundTag("BeamTarget")));
        }

        if (nbttagcompound.hasKey("ShowBottom", 1)) {
            this.setShowBottom(nbttagcompound.getBoolean("ShowBottom"));
        }

    }

    public boolean canBeCollidedWith() {
        return true;
    }

    public boolean attackEntityFrom(DamageSource damagesource, float f) {
        if (this.isEntityInvulnerable(damagesource)) {
            return false;
        } else if (damagesource.getTrueSource() instanceof EntityDragon) {
            return false;
        } else {
            if (!this.isDead && !this.world.isRemote) {
                // CraftBukkit start - All non-living entities need this
                if (CraftEventFactory.handleNonLivingEntityDamageEvent(this, damagesource, f)) {
                    return false;
                }
                // CraftBukkit end
                this.setDead();
                if (!this.world.isRemote) {
                    if (!damagesource.isExplosion()) {
                        // CraftBukkit start
                        ExplosionPrimeEvent event = new ExplosionPrimeEvent(this.getBukkitEntity(), 6.0F, true);
                        this.world.getServer().getPluginManager().callEvent(event);
                        if (event.isCancelled()) {
                            this.isDead = false;
                            return false;
                        }
                        this.world.createExplosion(this, this.posX, this.posY, this.posZ, event.getRadius(), event.getFire());
                        // CraftBukkit end
                    }

                    this.onCrystalDestroyed(damagesource);
                }
            }

            return true;
        }
    }

    public void onKillCommand() {
        this.onCrystalDestroyed(DamageSource.GENERIC);
        super.onKillCommand();
    }

    private void onCrystalDestroyed(DamageSource damagesource) {
        if (this.world.provider instanceof WorldProviderEnd) {
            WorldProviderEnd worldprovidertheend = (WorldProviderEnd) this.world.provider;
            DragonFightManager enderdragonbattle = worldprovidertheend.getDragonFightManager();

            if (enderdragonbattle != null) {
                enderdragonbattle.onCrystalDestroyed(this, damagesource);
            }
        }

    }

    public void setBeamTarget(@Nullable BlockPos blockposition) {
        this.getDataManager().set(EntityEnderCrystal.BEAM_TARGET, Optional.fromNullable(blockposition));
    }

    @Nullable
    public BlockPos getBeamTarget() {
        return (BlockPos) ((Optional) this.getDataManager().get(EntityEnderCrystal.BEAM_TARGET)).orNull();
    }

    public void setShowBottom(boolean flag) {
        this.getDataManager().set(EntityEnderCrystal.SHOW_BOTTOM, Boolean.valueOf(flag));
    }

    public boolean shouldShowBottom() {
        return ((Boolean) this.getDataManager().get(EntityEnderCrystal.SHOW_BOTTOM)).booleanValue();
    }
}
