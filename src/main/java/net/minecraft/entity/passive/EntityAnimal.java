package net.minecraft.entity.passive;

import java.util.UUID;
import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public abstract class EntityAnimal extends EntityAgeable implements IAnimals {

    protected Block spawnableBlock;
    private int inLove;
    private UUID playerInLove;
    public ItemStack breedItem; // CraftBukkit - Add breedItem variable

    public EntityAnimal(World world) {
        super(world);
        this.spawnableBlock = Blocks.GRASS;
    }

    protected void updateAITasks() {
        if (this.getGrowingAge() != 0) {
            this.inLove = 0;
        }

        super.updateAITasks();
    }

    public void onLivingUpdate() {
        super.onLivingUpdate();
        if (this.getGrowingAge() != 0) {
            this.inLove = 0;
        }

        if (this.inLove > 0) {
            --this.inLove;
            if (this.inLove % 10 == 0) {
                double d0 = this.rand.nextGaussian() * 0.02D;
                double d1 = this.rand.nextGaussian() * 0.02D;
                double d2 = this.rand.nextGaussian() * 0.02D;

                this.world.spawnParticle(EnumParticleTypes.HEART, this.posX + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, this.posY + 0.5D + (double) (this.rand.nextFloat() * this.height), this.posZ + (double) (this.rand.nextFloat() * this.width * 2.0F) - (double) this.width, d0, d1, d2, new int[0]);
            }
        }

    }

    /* CraftBukkit start
    // Function disabled as it has no special function anymore after
    // setSitting is disabled.
    public boolean damageEntity(DamageSource damagesource, float f) {
        if (this.isInvulnerable(damagesource)) {
            return false;
        } else {
            this.bx = 0;
            return super.damageEntity(damagesource, f);
        }
    }
    // CraftBukkit end */

    public float getBlockPathWeight(BlockPos blockposition) {
        return this.world.getBlockState(blockposition.down()).getBlock() == this.spawnableBlock ? 10.0F : this.world.getLightBrightness(blockposition) - 0.5F;
    }

    public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
        super.writeEntityToNBT(nbttagcompound);
        nbttagcompound.setInteger("InLove", this.inLove);
        if (this.playerInLove != null) {
            nbttagcompound.setUniqueId("LoveCause", this.playerInLove);
        }

    }

    public double getYOffset() {
        return 0.14D;
    }

    public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
        super.readEntityFromNBT(nbttagcompound);
        this.inLove = nbttagcompound.getInteger("InLove");
        this.playerInLove = nbttagcompound.hasUniqueId("LoveCause") ? nbttagcompound.getUniqueId("LoveCause") : null;
    }

    public boolean getCanSpawnHere() {
        int i = MathHelper.floor(this.posX);
        int j = MathHelper.floor(this.getEntityBoundingBox().minY);
        int k = MathHelper.floor(this.posZ);
        BlockPos blockposition = new BlockPos(i, j, k);

        return this.world.getBlockState(blockposition.down()).getBlock() == this.spawnableBlock && this.world.getLight(blockposition) > 8 && super.getCanSpawnHere();
    }

    public int getTalkInterval() {
        return 120;
    }

    protected boolean canDespawn() {
        return false;
    }

    protected int getExperiencePoints(EntityPlayer entityhuman) {
        return 1 + this.world.rand.nextInt(3);
    }

    public boolean isBreedingItem(ItemStack itemstack) {
        return itemstack.getItem() == Items.WHEAT;
    }

    public boolean processInteract(EntityPlayer entityhuman, EnumHand enumhand) {
        ItemStack itemstack = entityhuman.getHeldItem(enumhand);

        if (!itemstack.isEmpty()) {
            if (this.isBreedingItem(itemstack) && this.getGrowingAge() == 0 && this.inLove <= 0) {
                this.consumeItemFromStack(entityhuman, itemstack);
                this.setInLove(entityhuman);
                return true;
            }

            if (this.isChild() && this.isBreedingItem(itemstack)) {
                this.consumeItemFromStack(entityhuman, itemstack);
                this.ageUp((int) ((float) (-this.getGrowingAge() / 20) * 0.1F), true);
                return true;
            }
        }

        return super.processInteract(entityhuman, enumhand);
    }

    protected void consumeItemFromStack(EntityPlayer entityhuman, ItemStack itemstack) {
        if (!entityhuman.capabilities.isCreativeMode) {
            itemstack.shrink(1);
        }

    }

    public void setInLove(@Nullable EntityPlayer entityhuman) {
        this.inLove = 600;
        if (entityhuman != null) {
            this.playerInLove = entityhuman.getUniqueID();
        }
        this.breedItem = entityhuman.inventory.getCurrentItem(); // CraftBukkit

        this.world.setEntityState(this, (byte) 18);
    }

    @Nullable
    public EntityPlayerMP getLoveCause() {
        if (this.playerInLove == null) {
            return null;
        } else {
            EntityPlayer entityhuman = this.world.getPlayerEntityByUUID(this.playerInLove);

            return entityhuman instanceof EntityPlayerMP ? (EntityPlayerMP) entityhuman : null;
        }
    }

    public boolean isInLove() {
        return this.inLove > 0;
    }

    public void resetInLove() {
        this.inLove = 0;
    }

    public boolean canMateWith(EntityAnimal entityanimal) {
        return entityanimal == this ? false : (entityanimal.getClass() != this.getClass() ? false : this.isInLove() && entityanimal.isInLove());
    }
}
