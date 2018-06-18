package net.minecraft.entity;

import org.bukkit.craftbukkit.event.CraftEventFactory;

import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;

import net.minecraft.block.BlockFence;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketEntityAttach;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class EntityLeashKnot extends EntityHanging {

    public EntityLeashKnot(World world) {
        super(world);
    }

    public EntityLeashKnot(World world, BlockPos blockposition) {
        super(world, blockposition);
        this.setPosition((double) blockposition.getX() + 0.5D, (double) blockposition.getY() + 0.5D, (double) blockposition.getZ() + 0.5D);
        float f = 0.125F;
        float f1 = 0.1875F;
        float f2 = 0.25F;

        this.setEntityBoundingBox(new AxisAlignedBB(this.posX - 0.1875D, this.posY - 0.25D + 0.125D, this.posZ - 0.1875D, this.posX + 0.1875D, this.posY + 0.25D + 0.125D, this.posZ + 0.1875D));
        this.forceSpawn = true;
    }

    public void setPosition(double d0, double d1, double d2) {
        super.setPosition((double) MathHelper.floor(d0) + 0.5D, (double) MathHelper.floor(d1) + 0.5D, (double) MathHelper.floor(d2) + 0.5D);
    }

    protected void updateBoundingBox() {
        this.posX = (double) this.hangingPosition.getX() + 0.5D;
        this.posY = (double) this.hangingPosition.getY() + 0.5D;
        this.posZ = (double) this.hangingPosition.getZ() + 0.5D;
    }

    public void updateFacingWithBoundingBox(EnumFacing enumdirection) {}

    public int getWidthPixels() {
        return 9;
    }

    public int getHeightPixels() {
        return 9;
    }

    public float getEyeHeight() {
        return -0.0625F;
    }

    public void onBroken(@Nullable Entity entity) {
        this.playSound(SoundEvents.ENTITY_LEASHKNOT_BREAK, 1.0F, 1.0F);
    }

    public boolean writeToNBTOptional(NBTTagCompound nbttagcompound) {
        return false;
    }

    public void writeEntityToNBT(NBTTagCompound nbttagcompound) {}

    public void readEntityFromNBT(NBTTagCompound nbttagcompound) {}

    public boolean processInitialInteract(EntityPlayer entityhuman, EnumHand enumhand) {
        if (this.world.isRemote) {
            return true;
        } else {
            boolean flag = false;
            double d0 = 7.0D;
            List list = this.world.getEntitiesWithinAABB(EntityLiving.class, new AxisAlignedBB(this.posX - 7.0D, this.posY - 7.0D, this.posZ - 7.0D, this.posX + 7.0D, this.posY + 7.0D, this.posZ + 7.0D));
            Iterator iterator = list.iterator();

            EntityLiving entityinsentient;

            while (iterator.hasNext()) {
                entityinsentient = (EntityLiving) iterator.next();
                if (entityinsentient.getLeashed() && entityinsentient.getLeashHolder() == entityhuman) {
                    // CraftBukkit start
                    if (CraftEventFactory.callPlayerLeashEntityEvent(entityinsentient, this, entityhuman).isCancelled()) {
                        ((EntityPlayerMP) entityhuman).connection.sendPacket(new SPacketEntityAttach(entityinsentient, entityinsentient.getLeashHolder()));
                        continue;
                    }
                    // CraftBukkit end
                    entityinsentient.setLeashHolder(this, true);
                    flag = true;
                }
            }

            if (!flag) {
                // CraftBukkit start - Move below
                // this.die();
                boolean die = true;
                // CraftBukkit end
                if (true || entityhuman.capabilities.isCreativeMode) { // CraftBukkit - Process for non-creative as well
                    iterator = list.iterator();

                    while (iterator.hasNext()) {
                        entityinsentient = (EntityLiving) iterator.next();
                        if (entityinsentient.getLeashed() && entityinsentient.getLeashHolder() == this) {
                            // CraftBukkit start
                            if (CraftEventFactory.callPlayerUnleashEntityEvent(entityinsentient, entityhuman).isCancelled()) {
                                die = false;
                                continue;
                            }
                            entityinsentient.clearLeashed(true, !entityhuman.capabilities.isCreativeMode); // false -> survival mode boolean
                            // CraftBukkit end
                        }
                    }
                    // CraftBukkit start
                    if (die) {
                        this.setDead();
                    }
                    // CraftBukkit end
                }
            }

            return true;
        }
    }

    public boolean onValidSurface() {
        return this.world.getBlockState(this.hangingPosition).getBlock() instanceof BlockFence;
    }

    public static EntityLeashKnot createKnot(World world, BlockPos blockposition) {
        EntityLeashKnot entityleash = new EntityLeashKnot(world, blockposition);

        world.spawnEntity(entityleash);
        entityleash.playPlaceSound();
        return entityleash;
    }

    @Nullable
    public static EntityLeashKnot getKnotForPosition(World world, BlockPos blockposition) {
        int i = blockposition.getX();
        int j = blockposition.getY();
        int k = blockposition.getZ();
        List list = world.getEntitiesWithinAABB(EntityLeashKnot.class, new AxisAlignedBB((double) i - 1.0D, (double) j - 1.0D, (double) k - 1.0D, (double) i + 1.0D, (double) j + 1.0D, (double) k + 1.0D));
        Iterator iterator = list.iterator();

        EntityLeashKnot entityleash;

        do {
            if (!iterator.hasNext()) {
                return null;
            }

            entityleash = (EntityLeashKnot) iterator.next();
        } while (!entityleash.getHangingPosition().equals(blockposition));

        return entityleash;
    }

    public void playPlaceSound() {
        this.playSound(SoundEvents.ENTITY_LEASHKNOT_PLACE, 1.0F, 1.0F);
    }
}
