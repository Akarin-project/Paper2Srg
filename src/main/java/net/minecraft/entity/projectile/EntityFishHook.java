package net.minecraft.entity.projectile;

import java.util.Iterator;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.stats.StatList;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootTableList;
import org.bukkit.entity.Fish;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerFishEvent;

// CraftBukkit start
import org.bukkit.entity.Player;
import org.bukkit.entity.Fish;
import org.bukkit.event.player.PlayerFishEvent;
// CraftBukkit end

public class EntityFishHook extends Entity {

    private static final DataParameter<Integer> DATA_HOOKED_ENTITY = EntityDataManager.createKey(EntityFishHook.class, DataSerializers.VARINT);
    private boolean inGround;
    private int ticksInGround;
    public EntityPlayer angler;
    private int ticksInAir;
    private int ticksCatchable;
    private int ticksCaughtDelay;
    private int ticksCatchableDelay;
    private float fishApproachAngle;
    public Entity caughtEntity;
    private EntityFishHook.State currentState;
    private int luck;
    private int lureSpeed;

    public EntityFishHook(World world, EntityPlayer entityhuman) {
        super(world);
        this.currentState = EntityFishHook.State.FLYING;
        this.init(entityhuman);
        this.shoot();
    }

    private void init(EntityPlayer entityhuman) {
        this.setSize(0.25F, 0.25F);
        this.ignoreFrustumCheck = true;
        this.angler = entityhuman;
        this.angler.fishEntity = this;
    }

    public void setLureSpeed(int i) {
        this.lureSpeed = i;
    }

    public void setLuck(int i) {
        this.luck = i;
    }

    private void shoot() {
        float f = this.angler.prevRotationPitch + (this.angler.rotationPitch - this.angler.prevRotationPitch);
        float f1 = this.angler.prevRotationYaw + (this.angler.rotationYaw - this.angler.prevRotationYaw);
        float f2 = MathHelper.cos(-f1 * 0.017453292F - 3.1415927F);
        float f3 = MathHelper.sin(-f1 * 0.017453292F - 3.1415927F);
        float f4 = -MathHelper.cos(-f * 0.017453292F);
        float f5 = MathHelper.sin(-f * 0.017453292F);
        double d0 = this.angler.prevPosX + (this.angler.posX - this.angler.prevPosX) - (double) f3 * 0.3D;
        double d1 = this.angler.prevPosY + (this.angler.posY - this.angler.prevPosY) + (double) this.angler.getEyeHeight();
        double d2 = this.angler.prevPosZ + (this.angler.posZ - this.angler.prevPosZ) - (double) f2 * 0.3D;

        this.setLocationAndAngles(d0, d1, d2, f1, f);
        this.motionX = (double) (-f3);
        this.motionY = (double) MathHelper.clamp(-(f5 / f4), -5.0F, 5.0F);
        this.motionZ = (double) (-f2);
        float f6 = MathHelper.sqrt(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);

        this.motionX *= 0.6D / (double) f6 + 0.5D + this.rand.nextGaussian() * 0.0045D;
        this.motionY *= 0.6D / (double) f6 + 0.5D + this.rand.nextGaussian() * 0.0045D;
        this.motionZ *= 0.6D / (double) f6 + 0.5D + this.rand.nextGaussian() * 0.0045D;
        float f7 = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);

        this.rotationYaw = (float) (MathHelper.atan2(this.motionX, this.motionZ) * 57.2957763671875D);
        this.rotationPitch = (float) (MathHelper.atan2(this.motionY, (double) f7) * 57.2957763671875D);
        this.prevRotationYaw = this.rotationYaw;
        this.prevRotationPitch = this.rotationPitch;
    }

    protected void entityInit() {
        this.getDataManager().register(EntityFishHook.DATA_HOOKED_ENTITY, Integer.valueOf(0));
    }

    public void notifyDataManagerChange(DataParameter<?> datawatcherobject) {
        if (EntityFishHook.DATA_HOOKED_ENTITY.equals(datawatcherobject)) {
            int i = ((Integer) this.getDataManager().get(EntityFishHook.DATA_HOOKED_ENTITY)).intValue();

            this.caughtEntity = i > 0 ? this.world.getEntityByID(i - 1) : null;
        }

        super.notifyDataManagerChange(datawatcherobject);
    }

    public void onUpdate() {
        super.onUpdate();
        if (this.angler == null) {
            this.setDead();
        } else if (this.world.isRemote || !this.shouldStopFishing()) {
            if (this.inGround) {
                ++this.ticksInGround;
                if (this.ticksInGround >= 1200) {
                    this.setDead();
                    return;
                }
            }

            float f = 0.0F;
            BlockPos blockposition = new BlockPos(this);
            IBlockState iblockdata = this.world.getBlockState(blockposition);

            if (iblockdata.getMaterial() == Material.WATER) {
                f = BlockLiquid.getBlockLiquidHeight(iblockdata, this.world, blockposition);
            }

            double d0;

            if (this.currentState == EntityFishHook.State.FLYING) {
                if (this.caughtEntity != null) {
                    this.motionX = 0.0D;
                    this.motionY = 0.0D;
                    this.motionZ = 0.0D;
                    this.currentState = EntityFishHook.State.HOOKED_IN_ENTITY;
                    return;
                }

                if (f > 0.0F) {
                    this.motionX *= 0.3D;
                    this.motionY *= 0.2D;
                    this.motionZ *= 0.3D;
                    this.currentState = EntityFishHook.State.BOBBING;
                    return;
                }

                if (!this.world.isRemote) {
                    this.checkCollision();
                }

                if (!this.inGround && !this.onGround && !this.collidedHorizontally) {
                    ++this.ticksInAir;
                } else {
                    this.ticksInAir = 0;
                    this.motionX = 0.0D;
                    this.motionY = 0.0D;
                    this.motionZ = 0.0D;
                }
            } else {
                if (this.currentState == EntityFishHook.State.HOOKED_IN_ENTITY) {
                    if (this.caughtEntity != null) {
                        if (this.caughtEntity.isDead) {
                            this.caughtEntity = null;
                            this.currentState = EntityFishHook.State.FLYING;
                        } else {
                            this.posX = this.caughtEntity.posX;
                            double d1 = (double) this.caughtEntity.height;

                            this.posY = this.caughtEntity.getEntityBoundingBox().minY + d1 * 0.8D;
                            this.posZ = this.caughtEntity.posZ;
                            this.setPosition(this.posX, this.posY, this.posZ);
                        }
                    }

                    return;
                }

                if (this.currentState == EntityFishHook.State.BOBBING) {
                    this.motionX *= 0.9D;
                    this.motionZ *= 0.9D;
                    d0 = this.posY + this.motionY - (double) blockposition.getY() - (double) f;
                    if (Math.abs(d0) < 0.01D) {
                        d0 += Math.signum(d0) * 0.1D;
                    }

                    this.motionY -= d0 * (double) this.rand.nextFloat() * 0.2D;
                    if (!this.world.isRemote && f > 0.0F) {
                        this.catchingFish(blockposition);
                    }
                }
            }

            if (iblockdata.getMaterial() != Material.WATER) {
                this.motionY -= 0.03D;
            }

            this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
            this.updateRotation();
            d0 = 0.92D;
            this.motionX *= 0.92D;
            this.motionY *= 0.92D;
            this.motionZ *= 0.92D;
            this.setPosition(this.posX, this.posY, this.posZ);

            // Paper start - These shouldn't be going through portals
            if (this.inPortal()) {
                this.setDead();
            }
            // Paper end
        }
    }

    private boolean shouldStopFishing() {
        ItemStack itemstack = this.angler.getHeldItemMainhand();
        ItemStack itemstack1 = this.angler.getHeldItemOffhand();
        boolean flag = itemstack.getItem() == Items.FISHING_ROD;
        boolean flag1 = itemstack1.getItem() == Items.FISHING_ROD;

        if (!this.angler.isDead && this.angler.isEntityAlive() && (flag || flag1) && this.getDistanceSq(this.angler) <= 1024.0D) {
            return false;
        } else {
            this.setDead();
            return true;
        }
    }

    private void updateRotation() {
        float f = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);

        this.rotationYaw = (float) (MathHelper.atan2(this.motionX, this.motionZ) * 57.2957763671875D);

        for (this.rotationPitch = (float) (MathHelper.atan2(this.motionY, (double) f) * 57.2957763671875D); this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F) {
            ;
        }

        while (this.rotationPitch - this.prevRotationPitch >= 180.0F) {
            this.prevRotationPitch += 360.0F;
        }

        while (this.rotationYaw - this.prevRotationYaw < -180.0F) {
            this.prevRotationYaw -= 360.0F;
        }

        while (this.rotationYaw - this.prevRotationYaw >= 180.0F) {
            this.prevRotationYaw += 360.0F;
        }

        this.rotationPitch = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2F;
        this.rotationYaw = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2F;
    }

    private void checkCollision() {
        Vec3d vec3d = new Vec3d(this.posX, this.posY, this.posZ);
        Vec3d vec3d1 = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
        RayTraceResult movingobjectposition = this.world.rayTraceBlocks(vec3d, vec3d1, false, true, false);

        vec3d = new Vec3d(this.posX, this.posY, this.posZ);
        vec3d1 = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);

        // Paper start - Call ProjectileCollideEvent
        if (movingobjectposition != null && movingobjectposition.entityHit != null) {
            com.destroystokyo.paper.event.entity.ProjectileCollideEvent event = org.bukkit.craftbukkit.event.CraftEventFactory.callProjectileCollideEvent(this, movingobjectposition);
            if (event.isCancelled()) {
                movingobjectposition = null;
            }
        }
        // Paper end

        if (movingobjectposition != null) {
            vec3d1 = new Vec3d(movingobjectposition.hitVec.x, movingobjectposition.hitVec.y, movingobjectposition.hitVec.z);
        }

        Entity entity = null;
        List list = this.world.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().expand(this.motionX, this.motionY, this.motionZ).grow(1.0D));
        double d0 = 0.0D;
        Iterator iterator = list.iterator();

        while (iterator.hasNext()) {
            Entity entity1 = (Entity) iterator.next();

            if (this.canBeHooked(entity1) && (entity1 != this.angler || this.ticksInAir >= 5)) {
                AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox().grow(0.30000001192092896D);
                RayTraceResult movingobjectposition1 = axisalignedbb.calculateIntercept(vec3d, vec3d1);

                if (movingobjectposition1 != null) {
                    double d1 = vec3d.squareDistanceTo(movingobjectposition1.hitVec);

                    if (d1 < d0 || d0 == 0.0D) {
                        entity = entity1;
                        d0 = d1;
                    }
                }
            }
        }

        if (entity != null) {
            movingobjectposition = new RayTraceResult(entity);
        }

        if (movingobjectposition != null && movingobjectposition.typeOfHit != RayTraceResult.Type.MISS) {
            org.bukkit.craftbukkit.event.CraftEventFactory.callProjectileHitEvent(this, movingobjectposition); // Craftbukkit - Call event
            if (movingobjectposition.typeOfHit == RayTraceResult.Type.ENTITY) {
                this.caughtEntity = movingobjectposition.entityHit;
                this.setHookedEntity();
            } else {
                this.inGround = true;
            }
        }

    }

    private void setHookedEntity() {
        this.getDataManager().set(EntityFishHook.DATA_HOOKED_ENTITY, Integer.valueOf(this.caughtEntity.getEntityId() + 1));
    }

    private void catchingFish(BlockPos blockposition) {
        WorldServer worldserver = (WorldServer) this.world;
        int i = 1;
        BlockPos blockposition1 = blockposition.up();

        if (this.rand.nextFloat() < 0.25F && this.world.isRainingAt(blockposition1)) {
            ++i;
        }

        if (this.rand.nextFloat() < 0.5F && !this.world.canSeeSky(blockposition1)) {
            --i;
        }

        if (this.ticksCatchable > 0) {
            --this.ticksCatchable;
            if (this.ticksCatchable <= 0) {
                this.ticksCaughtDelay = 0;
                this.ticksCatchableDelay = 0;
                // CraftBukkit start
                PlayerFishEvent playerFishEvent = new PlayerFishEvent((Player) this.angler.getBukkitEntity(), null, (Fish) this.getBukkitEntity(), PlayerFishEvent.State.FAILED_ATTEMPT);
                this.world.getServer().getPluginManager().callEvent(playerFishEvent);
                // CraftBukkit end
            } else {
                this.motionY -= 0.2D * (double) this.rand.nextFloat() * (double) this.rand.nextFloat();
            }
        } else {
            float f;
            float f1;
            float f2;
            double d0;
            double d1;
            double d2;
            Block block;

            if (this.ticksCatchableDelay > 0) {
                this.ticksCatchableDelay -= i;
                if (this.ticksCatchableDelay > 0) {
                    this.fishApproachAngle = (float) ((double) this.fishApproachAngle + this.rand.nextGaussian() * 4.0D);
                    f = this.fishApproachAngle * 0.017453292F;
                    f1 = MathHelper.sin(f);
                    f2 = MathHelper.cos(f);
                    d0 = this.posX + (double) (f1 * (float) this.ticksCatchableDelay * 0.1F);
                    d1 = (double) ((float) MathHelper.floor(this.getEntityBoundingBox().minY) + 1.0F);
                    d2 = this.posZ + (double) (f2 * (float) this.ticksCatchableDelay * 0.1F);
                    block = worldserver.getBlockState(new BlockPos(d0, d1 - 1.0D, d2)).getBlock();
                    if (block == Blocks.WATER || block == Blocks.FLOWING_WATER) {
                        if (this.rand.nextFloat() < 0.15F) {
                            worldserver.spawnParticle(EnumParticleTypes.WATER_BUBBLE, d0, d1 - 0.10000000149011612D, d2, 1, (double) f1, 0.1D, (double) f2, 0.0D, new int[0]);
                        }

                        float f3 = f1 * 0.04F;
                        float f4 = f2 * 0.04F;

                        worldserver.spawnParticle(EnumParticleTypes.WATER_WAKE, d0, d1, d2, 0, (double) f4, 0.01D, (double) (-f3), 1.0D, new int[0]);
                        worldserver.spawnParticle(EnumParticleTypes.WATER_WAKE, d0, d1, d2, 0, (double) (-f4), 0.01D, (double) f3, 1.0D, new int[0]);
                    }
                } else {
                    // CraftBukkit start
                    PlayerFishEvent playerFishEvent = new PlayerFishEvent((Player) this.angler.getBukkitEntity(), null, (Fish) this.getBukkitEntity(), PlayerFishEvent.State.BITE);
                    this.world.getServer().getPluginManager().callEvent(playerFishEvent);
                    if (playerFishEvent.isCancelled()) {
                        return;
                    }
                    // CraftBukkit end
                    this.motionY = (double) (-0.4F * MathHelper.nextFloat(this.rand, 0.6F, 1.0F));
                    this.playSound(SoundEvents.ENTITY_BOBBER_SPLASH, 0.25F, 1.0F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.4F);
                    double d3 = this.getEntityBoundingBox().minY + 0.5D;

                    worldserver.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX, d3, this.posZ, (int) (1.0F + this.width * 20.0F), (double) this.width, 0.0D, (double) this.width, 0.20000000298023224D, new int[0]);
                    worldserver.spawnParticle(EnumParticleTypes.WATER_WAKE, this.posX, d3, this.posZ, (int) (1.0F + this.width * 20.0F), (double) this.width, 0.0D, (double) this.width, 0.20000000298023224D, new int[0]);
                    this.ticksCatchable = MathHelper.getInt(this.rand, 20, 40);
                }
            } else if (this.ticksCaughtDelay > 0) {
                this.ticksCaughtDelay -= i;
                f = 0.15F;
                if (this.ticksCaughtDelay < 20) {
                    f = (float) ((double) f + (double) (20 - this.ticksCaughtDelay) * 0.05D);
                } else if (this.ticksCaughtDelay < 40) {
                    f = (float) ((double) f + (double) (40 - this.ticksCaughtDelay) * 0.02D);
                } else if (this.ticksCaughtDelay < 60) {
                    f = (float) ((double) f + (double) (60 - this.ticksCaughtDelay) * 0.01D);
                }

                if (this.rand.nextFloat() < f) {
                    f1 = MathHelper.nextFloat(this.rand, 0.0F, 360.0F) * 0.017453292F;
                    f2 = MathHelper.nextFloat(this.rand, 25.0F, 60.0F);
                    d0 = this.posX + (double) (MathHelper.sin(f1) * f2 * 0.1F);
                    d1 = (double) ((float) MathHelper.floor(this.getEntityBoundingBox().minY) + 1.0F);
                    d2 = this.posZ + (double) (MathHelper.cos(f1) * f2 * 0.1F);
                    block = worldserver.getBlockState(new BlockPos((int) d0, (int) d1 - 1, (int) d2)).getBlock();
                    if (block == Blocks.WATER || block == Blocks.FLOWING_WATER) {
                        worldserver.spawnParticle(EnumParticleTypes.WATER_SPLASH, d0, d1, d2, 2 + this.rand.nextInt(2), 0.10000000149011612D, 0.0D, 0.10000000149011612D, 0.0D, new int[0]);
                    }
                }

                if (this.ticksCaughtDelay <= 0) {
                    this.fishApproachAngle = MathHelper.nextFloat(this.rand, 0.0F, 360.0F);
                    this.ticksCatchableDelay = MathHelper.getInt(this.rand, 20, 80);
                }
            } else {
                this.ticksCaughtDelay = MathHelper.getInt(this.rand, world.paperConfig.fishingMinTicks, world.paperConfig.fishingMaxTicks); // Paper
                this.ticksCaughtDelay -= this.lureSpeed * 20 * 5;
            }
        }

    }

    protected boolean canBeHooked(Entity entity) {
        return entity.canBeCollidedWith() || entity instanceof EntityItem;
    }

    public void writeEntityToNBT(NBTTagCompound nbttagcompound) {}

    public void readEntityFromNBT(NBTTagCompound nbttagcompound) {}

    public int handleHookRetraction() {
        if (!this.world.isRemote && this.angler != null) {
            int i = 0;

            if (this.caughtEntity != null) {
                // CraftBukkit start
                PlayerFishEvent playerFishEvent = new PlayerFishEvent((Player) this.angler.getBukkitEntity(), this.caughtEntity.getBukkitEntity(), (Fish) this.getBukkitEntity(), PlayerFishEvent.State.CAUGHT_ENTITY);
                this.world.getServer().getPluginManager().callEvent(playerFishEvent);

                if (playerFishEvent.isCancelled()) {
                    return 0;
                }
                // CraftBukkit end
                this.bringInHookedEntity();
                this.world.setEntityState(this, (byte) 31);
                i = this.caughtEntity instanceof EntityItem ? 3 : 5;
            } else if (this.ticksCatchable > 0) {
                LootTableInfo.a loottableinfo_a = new LootTableInfo.a((WorldServer) this.world);

                loottableinfo_a.a((float) this.luck + this.angler.getLuck());
                Iterator iterator = this.world.getLootTableManager().getLootTableFromLocation(LootTableList.GAMEPLAY_FISHING).generateLootForPools(this.rand, loottableinfo_a.a()).iterator();

                while (iterator.hasNext()) {
                    ItemStack itemstack = (ItemStack) iterator.next();
                    EntityItem entityitem = new EntityItem(this.world, this.posX, this.posY, this.posZ, itemstack);
                    // CraftBukkit start
                    PlayerFishEvent playerFishEvent = new PlayerFishEvent((Player) this.angler.getBukkitEntity(), entityitem.getBukkitEntity(), (Fish) this.getBukkitEntity(), PlayerFishEvent.State.CAUGHT_FISH);
                    playerFishEvent.setExpToDrop(this.rand.nextInt(6) + 1);
                    this.world.getServer().getPluginManager().callEvent(playerFishEvent);

                    if (playerFishEvent.isCancelled()) {
                        return 0;
                    }
                    // CraftBukkit end
                    double d0 = this.angler.posX - this.posX;
                    double d1 = this.angler.posY - this.posY;
                    double d2 = this.angler.posZ - this.posZ;
                    double d3 = (double) MathHelper.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
                    double d4 = 0.1D;

                    entityitem.motionX = d0 * 0.1D;
                    entityitem.motionY = d1 * 0.1D + (double) MathHelper.sqrt(d3) * 0.08D;
                    entityitem.motionZ = d2 * 0.1D;
                    this.world.spawnEntity(entityitem);
                    // CraftBukkit start - this.random.nextInt(6) + 1 -> playerFishEvent.getExpToDrop()
                    if (playerFishEvent.getExpToDrop() > 0) {
                        this.angler.world.spawnEntity(new EntityXPOrb(this.angler.world, this.angler.posX, this.angler.posY + 0.5D, this.angler.posZ + 0.5D, playerFishEvent.getExpToDrop(), org.bukkit.entity.ExperienceOrb.SpawnReason.FISHING, this.angler, this)); // Paper
                    }
                    // CraftBukkit end
                    Item item = itemstack.getItem();

                    if (item == Items.FISH || item == Items.COOKED_FISH) {
                        this.angler.addStat(StatList.FISH_CAUGHT, 1);
                    }
                }

                i = 1;
            }

            if (this.inGround) {
                // CraftBukkit start
                PlayerFishEvent playerFishEvent = new PlayerFishEvent((Player) this.angler.getBukkitEntity(), null, (Fish) this.getBukkitEntity(), PlayerFishEvent.State.IN_GROUND);
                this.world.getServer().getPluginManager().callEvent(playerFishEvent);

                if (playerFishEvent.isCancelled()) {
                    return 0;
                }
                // CraftBukkit end
                i = 2;
            }
            // CraftBukkit start
            if (i == 0) {
                PlayerFishEvent playerFishEvent = new PlayerFishEvent((Player) this.angler.getBukkitEntity(), null, (Fish) this.getBukkitEntity(), PlayerFishEvent.State.FAILED_ATTEMPT);
                this.world.getServer().getPluginManager().callEvent(playerFishEvent);
                if (playerFishEvent.isCancelled()) {
                    return 0;
                }
            }
            // CraftBukkit end

            this.setDead();
            return i;
        } else {
            return 0;
        }
    }

    protected void bringInHookedEntity() {
        if (this.angler != null) {
            double d0 = this.angler.posX - this.posX;
            double d1 = this.angler.posY - this.posY;
            double d2 = this.angler.posZ - this.posZ;
            double d3 = 0.1D;

            this.caughtEntity.motionX += d0 * 0.1D;
            this.caughtEntity.motionY += d1 * 0.1D;
            this.caughtEntity.motionZ += d2 * 0.1D;
        }
    }

    protected boolean canTriggerWalking() {
        return false;
    }

    public void setDead() {
        super.setDead();
        if (this.angler != null) {
            this.angler.fishEntity = null;
        }

    }

    public EntityPlayer getAngler() {
        return this.angler;
    }

    static enum State {

        FLYING, HOOKED_IN_ENTITY, BOBBING;

        private State() {}
    }
}
