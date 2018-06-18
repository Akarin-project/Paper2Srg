package net.minecraft.entity.effect;

import java.util.List;


import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import org.bukkit.craftbukkit.event.CraftEventFactory;

public class EntityLightningBolt extends EntityWeatherEffect {

    private int lightningState;
    public long boltVertex;
    private int boltLivingTime;
    private final boolean effectOnly;
    public boolean isEffect; // CraftBukkit
    public boolean isSilent = false; // Spigot

    public EntityLightningBolt(World world, double d0, double d1, double d2, boolean flag) {
        super(world);
        this.isEffect = flag; // CraftBukkit
        this.setLocationAndAngles(d0, d1, d2, 0.0F, 0.0F);
        this.lightningState = 2;
        this.boltVertex = this.rand.nextLong();
        this.boltLivingTime = this.rand.nextInt(3) + 1;
        this.effectOnly = flag;
        BlockPos blockposition = new BlockPos(this);

        if (!flag && !world.isRemote && world.getGameRules().getBoolean("doFireTick") && (world.getDifficulty() == EnumDifficulty.NORMAL || world.getDifficulty() == EnumDifficulty.HARD) && world.isAreaLoaded(blockposition, 10)) {
            if (world.getBlockState(blockposition).getMaterial() == Material.AIR && Blocks.FIRE.canPlaceBlockAt(world, blockposition)) {
                // CraftBukkit start
                if (!CraftEventFactory.callBlockIgniteEvent(world, blockposition.getX(), blockposition.getY(), blockposition.getZ(), this).isCancelled()) {
                    world.setBlockState(blockposition, Blocks.FIRE.getDefaultState());
                }
                // CraftBukkit end
            }

            for (int i = 0; i < 4; ++i) {
                BlockPos blockposition1 = blockposition.add(this.rand.nextInt(3) - 1, this.rand.nextInt(3) - 1, this.rand.nextInt(3) - 1);

                if (world.getBlockState(blockposition1).getMaterial() == Material.AIR && Blocks.FIRE.canPlaceBlockAt(world, blockposition1)) {
                    // CraftBukkit start
                    if (!CraftEventFactory.callBlockIgniteEvent(world, blockposition1.getX(), blockposition1.getY(), blockposition1.getZ(), this).isCancelled()) {
                        world.setBlockState(blockposition1, Blocks.FIRE.getDefaultState());
                    }
                    // CraftBukkit end
                }
            }
        }

    }

    // Spigot start
    public EntityLightningBolt(World world, double d0, double d1, double d2, boolean isEffect, boolean isSilent)
    {
        this( world, d0, d1, d2, isEffect );
        this.isSilent = isSilent;
    }
    // Spigot end

    public SoundCategory getSoundCategory() {
        return SoundCategory.WEATHER;
    }

    public void onUpdate() {
        super.onUpdate();
        if (!isSilent && this.lightningState == 2) { // Spigot
            // CraftBukkit start - Use relative location for far away sounds
            // this.world.a((EntityHuman) null, this.locX, this.locY, this.locZ, SoundEffects.dG, SoundCategory.WEATHER, 10000.0F, 0.8F + this.random.nextFloat() * 0.2F);
            float pitch = 0.8F + this.rand.nextFloat() * 0.2F;
            int viewDistance = ((WorldServer) this.world).getServer().getViewDistance() * 16;
            for (EntityPlayerMP player : (List<EntityPlayerMP>) (List) this.world.playerEntities) {
                double deltaX = this.posX - player.posX;
                double deltaZ = this.posZ - player.posZ;
                double distanceSquared = deltaX * deltaX + deltaZ * deltaZ;
                if (distanceSquared > viewDistance * viewDistance) {
                    double deltaLength = Math.sqrt(distanceSquared);
                    double relativeX = player.posX + (deltaX / deltaLength) * viewDistance;
                    double relativeZ = player.posZ + (deltaZ / deltaLength) * viewDistance;
                    player.connection.sendPacket(new SPacketSoundEffect(SoundEvents.ENTITY_LIGHTNING_THUNDER, SoundCategory.WEATHER, relativeX, this.posY, relativeZ, 10000.0F, pitch));
                } else {
                    player.connection.sendPacket(new SPacketSoundEffect(SoundEvents.ENTITY_LIGHTNING_THUNDER, SoundCategory.WEATHER, this.posX, this.posY, this.posZ, 10000.0F, pitch));
                }
            }
            // CraftBukkit end
            this.world.playSound((EntityPlayer) null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_LIGHTNING_IMPACT, SoundCategory.WEATHER, 2.0F, 0.5F + this.rand.nextFloat() * 0.2F);
        }

        --this.lightningState;
        if (this.lightningState < 0) {
            if (this.boltLivingTime == 0) {
                this.setDead();
            } else if (this.lightningState < -this.rand.nextInt(10)) {
                --this.boltLivingTime;
                this.lightningState = 1;
                if (!this.effectOnly && !this.world.isRemote) {
                    this.boltVertex = this.rand.nextLong();
                    BlockPos blockposition = new BlockPos(this);

                    if (this.world.getGameRules().getBoolean("doFireTick") && this.world.isAreaLoaded(blockposition, 10) && this.world.getBlockState(blockposition).getMaterial() == Material.AIR && Blocks.FIRE.canPlaceBlockAt(this.world, blockposition)) {
                        // CraftBukkit start - add "!isEffect"
                        if (!isEffect && !CraftEventFactory.callBlockIgniteEvent(world, blockposition.getX(), blockposition.getY(), blockposition.getZ(), this).isCancelled()) {
                            this.world.setBlockState(blockposition, Blocks.FIRE.getDefaultState());
                        }
                        // CraftBukkit end
                    }
                }
            }
        }

        if (this.lightningState >= 0 && !this.isEffect) { // CraftBukkit - add !this.isEffect
            if (this.world.isRemote) {
                this.world.setLastLightningBolt(2);
            } else if (!this.effectOnly) {
                double d0 = 3.0D;
                List list = this.world.getEntitiesWithinAABBExcludingEntity(this, new AxisAlignedBB(this.posX - 3.0D, this.posY - 3.0D, this.posZ - 3.0D, this.posX + 3.0D, this.posY + 6.0D + 3.0D, this.posZ + 3.0D));

                for (int i = 0; i < list.size(); ++i) {
                    Entity entity = (Entity) list.get(i);

                    entity.onStruckByLightning(this);
                }
            }
        }

    }

    protected void entityInit() {}

    protected void readEntityFromNBT(NBTTagCompound nbttagcompound) {}

    protected void writeEntityToNBT(NBTTagCompound nbttagcompound) {}
}
