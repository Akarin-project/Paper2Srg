package net.minecraft.entity.monster;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.BlockPistonExtension;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityBodyHelper;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityShulkerBullet;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.server.EntityShulker.a;
import net.minecraft.server.EntityShulker.b;
import net.minecraft.server.EntityShulker.c;
import net.minecraft.server.EntityShulker.d;
import net.minecraft.server.EntityShulker.e;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;
import org.bukkit.Location;
import org.bukkit.event.entity.EntityTeleportEvent;

// CraftBukkit start
import org.bukkit.Location;
import org.bukkit.event.entity.EntityTeleportEvent;
// CraftBukkit end

public class EntityShulker extends EntityGolem implements IMob {

    private static final UUID COVERED_ARMOR_BONUS_ID = UUID.fromString("7E0292F2-9434-48D5-A29F-9583AF7DF27F");
    private static final AttributeModifier COVERED_ARMOR_BONUS_MODIFIER = (new AttributeModifier(EntityShulker.COVERED_ARMOR_BONUS_ID, "Covered armor bonus", 20.0D, 0)).setSaved(false);
    protected static final DataParameter<EnumFacing> ATTACHED_FACE = EntityDataManager.createKey(EntityShulker.class, DataSerializers.FACING);
    protected static final DataParameter<Optional<BlockPos>> ATTACHED_BLOCK_POS = EntityDataManager.createKey(EntityShulker.class, DataSerializers.OPTIONAL_BLOCK_POS);
    protected static final DataParameter<Byte> PEEK_TICK = EntityDataManager.createKey(EntityShulker.class, DataSerializers.BYTE);
    public static final DataParameter<Byte> COLOR = EntityDataManager.createKey(EntityShulker.class, DataSerializers.BYTE);
    public static final EnumDyeColor DEFAULT_COLOR = EnumDyeColor.PURPLE;
    private float prevPeekAmount;
    private float peekAmount;
    private BlockPos currentAttachmentPosition;
    private int clientSideTeleportInterpolation;

    public EntityShulker(World world) {
        super(world);
        this.setSize(1.0F, 1.0F);
        this.prevRenderYawOffset = 180.0F;
        this.renderYawOffset = 180.0F;
        this.isImmuneToFire = true;
        this.currentAttachmentPosition = null;
        this.experienceValue = 5;
    }

    @Nullable
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficultydamagescaler, @Nullable IEntityLivingData groupdataentity) {
        this.renderYawOffset = 180.0F;
        this.prevRenderYawOffset = 180.0F;
        this.rotationYaw = 180.0F;
        this.prevRotationYaw = 180.0F;
        this.rotationYawHead = 180.0F;
        this.prevRotationYawHead = 180.0F;
        return super.onInitialSpawn(difficultydamagescaler, groupdataentity);
    }

    protected void initEntityAI() {
        this.tasks.addTask(1, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(4, new EntityShulker.a());
        this.tasks.addTask(7, new EntityShulker.e(null));
        this.tasks.addTask(8, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true, new Class[0]));
        this.targetTasks.addTask(2, new EntityShulker.d(this));
        this.targetTasks.addTask(3, new EntityShulker.c(this));
    }

    protected boolean canTriggerWalking() {
        return false;
    }

    public SoundCategory getSoundCategory() {
        return SoundCategory.HOSTILE;
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_SHULKER_AMBIENT;
    }

    public void playLivingSound() {
        if (!this.isClosed()) {
            super.playLivingSound();
        }

    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_SHULKER_DEATH;
    }

    protected SoundEvent getHurtSound(DamageSource damagesource) {
        return this.isClosed() ? SoundEvents.ENTITY_SHULKER_HURT_CLOSED : SoundEvents.ENTITY_SHULKER_HURT;
    }

    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(EntityShulker.ATTACHED_FACE, EnumFacing.DOWN);
        this.dataManager.register(EntityShulker.ATTACHED_BLOCK_POS, Optional.absent());
        this.dataManager.register(EntityShulker.PEEK_TICK, Byte.valueOf((byte) 0));
        this.dataManager.register(EntityShulker.COLOR, Byte.valueOf((byte) EntityShulker.DEFAULT_COLOR.getMetadata()));
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(30.0D);
    }

    protected EntityBodyHelper createBodyHelper() {
        return new EntityShulker.b(this);
    }

    public static void registerFixesShulker(DataFixer dataconvertermanager) {
        EntityLiving.registerFixesMob(dataconvertermanager, EntityShulker.class);
    }

    public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
        super.readEntityFromNBT(nbttagcompound);
        this.dataManager.set(EntityShulker.ATTACHED_FACE, EnumFacing.getFront(nbttagcompound.getByte("AttachFace")));
        this.dataManager.set(EntityShulker.PEEK_TICK, Byte.valueOf(nbttagcompound.getByte("Peek")));
        this.dataManager.set(EntityShulker.COLOR, Byte.valueOf(nbttagcompound.getByte("Color")));
        if (nbttagcompound.hasKey("APX")) {
            int i = nbttagcompound.getInteger("APX");
            int j = nbttagcompound.getInteger("APY");
            int k = nbttagcompound.getInteger("APZ");

            this.dataManager.set(EntityShulker.ATTACHED_BLOCK_POS, Optional.of(new BlockPos(i, j, k)));
        } else {
            this.dataManager.set(EntityShulker.ATTACHED_BLOCK_POS, Optional.<BlockPos>absent());
        }

    }

    public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
        super.writeEntityToNBT(nbttagcompound);
        nbttagcompound.setByte("AttachFace", (byte) ((EnumFacing) this.dataManager.get(EntityShulker.ATTACHED_FACE)).getIndex());
        nbttagcompound.setByte("Peek", ((Byte) this.dataManager.get(EntityShulker.PEEK_TICK)).byteValue());
        nbttagcompound.setByte("Color", ((Byte) this.dataManager.get(EntityShulker.COLOR)).byteValue());
        BlockPos blockposition = this.getAttachmentPos();

        if (blockposition != null) {
            nbttagcompound.setInteger("APX", blockposition.getX());
            nbttagcompound.setInteger("APY", blockposition.getY());
            nbttagcompound.setInteger("APZ", blockposition.getZ());
        }

    }

    public void onUpdate() {
        super.onUpdate();
        BlockPos blockposition = (BlockPos) ((Optional) this.dataManager.get(EntityShulker.ATTACHED_BLOCK_POS)).orNull();

        if (blockposition == null && !this.world.isRemote) {
            blockposition = new BlockPos(this);
            this.dataManager.set(EntityShulker.ATTACHED_BLOCK_POS, Optional.of(blockposition));
        }

        float f;

        if (this.isRiding()) {
            blockposition = null;
            f = this.getRidingEntity().rotationYaw;
            this.rotationYaw = f;
            this.renderYawOffset = f;
            this.prevRenderYawOffset = f;
            this.clientSideTeleportInterpolation = 0;
        } else if (!this.world.isRemote) {
            IBlockState iblockdata = this.world.getBlockState(blockposition);

            if (iblockdata.getMaterial() != Material.AIR) {
                EnumFacing enumdirection;

                if (iblockdata.getBlock() == Blocks.PISTON_EXTENSION) {
                    enumdirection = (EnumFacing) iblockdata.getValue(BlockPistonBase.FACING);
                    if (this.world.isAirBlock(blockposition.offset(enumdirection))) {
                        blockposition = blockposition.offset(enumdirection);
                        this.dataManager.set(EntityShulker.ATTACHED_BLOCK_POS, Optional.of(blockposition));
                    } else {
                        this.tryTeleportToNewPosition();
                    }
                } else if (iblockdata.getBlock() == Blocks.PISTON_HEAD) {
                    enumdirection = (EnumFacing) iblockdata.getValue(BlockPistonExtension.FACING);
                    if (this.world.isAirBlock(blockposition.offset(enumdirection))) {
                        blockposition = blockposition.offset(enumdirection);
                        this.dataManager.set(EntityShulker.ATTACHED_BLOCK_POS, Optional.of(blockposition));
                    } else {
                        this.tryTeleportToNewPosition();
                    }
                } else {
                    this.tryTeleportToNewPosition();
                }
            }

            BlockPos blockposition1 = blockposition.offset(this.getAttachmentFacing());

            if (!this.world.isBlockNormalCube(blockposition1, false)) {
                boolean flag = false;
                EnumFacing[] aenumdirection = EnumFacing.values();
                int i = aenumdirection.length;

                for (int j = 0; j < i; ++j) {
                    EnumFacing enumdirection1 = aenumdirection[j];

                    blockposition1 = blockposition.offset(enumdirection1);
                    if (this.world.isBlockNormalCube(blockposition1, false)) {
                        this.dataManager.set(EntityShulker.ATTACHED_FACE, enumdirection1);
                        flag = true;
                        break;
                    }
                }

                if (!flag) {
                    this.tryTeleportToNewPosition();
                }
            }

            BlockPos blockposition2 = blockposition.offset(this.getAttachmentFacing().getOpposite());

            if (this.world.isBlockNormalCube(blockposition2, false)) {
                this.tryTeleportToNewPosition();
            }
        }

        f = (float) this.getPeekTick() * 0.01F;
        this.prevPeekAmount = this.peekAmount;
        if (this.peekAmount > f) {
            this.peekAmount = MathHelper.clamp(this.peekAmount - 0.05F, f, 1.0F);
        } else if (this.peekAmount < f) {
            this.peekAmount = MathHelper.clamp(this.peekAmount + 0.05F, 0.0F, f);
        }

        if (blockposition != null) {
            if (this.world.isRemote) {
                if (this.clientSideTeleportInterpolation > 0 && this.currentAttachmentPosition != null) {
                    --this.clientSideTeleportInterpolation;
                } else {
                    this.currentAttachmentPosition = blockposition;
                }
            }

            this.posX = (double) blockposition.getX() + 0.5D;
            this.posY = (double) blockposition.getY();
            this.posZ = (double) blockposition.getZ() + 0.5D;
            this.prevPosX = this.posX;
            this.prevPosY = this.posY;
            this.prevPosZ = this.posZ;
            this.lastTickPosX = this.posX;
            this.lastTickPosY = this.posY;
            this.lastTickPosZ = this.posZ;
            double d0 = 0.5D - (double) MathHelper.sin((0.5F + this.peekAmount) * 3.1415927F) * 0.5D;
            double d1 = 0.5D - (double) MathHelper.sin((0.5F + this.prevPeekAmount) * 3.1415927F) * 0.5D;
            double d2 = d0 - d1;
            double d3 = 0.0D;
            double d4 = 0.0D;
            double d5 = 0.0D;
            EnumFacing enumdirection2 = this.getAttachmentFacing();

            switch (enumdirection2) {
            case DOWN:
                this.setEntityBoundingBox(new AxisAlignedBB(this.posX - 0.5D, this.posY, this.posZ - 0.5D, this.posX + 0.5D, this.posY + 1.0D + d0, this.posZ + 0.5D));
                d4 = d2;
                break;

            case UP:
                this.setEntityBoundingBox(new AxisAlignedBB(this.posX - 0.5D, this.posY - d0, this.posZ - 0.5D, this.posX + 0.5D, this.posY + 1.0D, this.posZ + 0.5D));
                d4 = -d2;
                break;

            case NORTH:
                this.setEntityBoundingBox(new AxisAlignedBB(this.posX - 0.5D, this.posY, this.posZ - 0.5D, this.posX + 0.5D, this.posY + 1.0D, this.posZ + 0.5D + d0));
                d5 = d2;
                break;

            case SOUTH:
                this.setEntityBoundingBox(new AxisAlignedBB(this.posX - 0.5D, this.posY, this.posZ - 0.5D - d0, this.posX + 0.5D, this.posY + 1.0D, this.posZ + 0.5D));
                d5 = -d2;
                break;

            case WEST:
                this.setEntityBoundingBox(new AxisAlignedBB(this.posX - 0.5D, this.posY, this.posZ - 0.5D, this.posX + 0.5D + d0, this.posY + 1.0D, this.posZ + 0.5D));
                d3 = d2;
                break;

            case EAST:
                this.setEntityBoundingBox(new AxisAlignedBB(this.posX - 0.5D - d0, this.posY, this.posZ - 0.5D, this.posX + 0.5D, this.posY + 1.0D, this.posZ + 0.5D));
                d3 = -d2;
            }

            if (d2 > 0.0D) {
                List list = this.world.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox());

                if (!list.isEmpty()) {
                    Iterator iterator = list.iterator();

                    while (iterator.hasNext()) {
                        Entity entity = (Entity) iterator.next();

                        if (!(entity instanceof EntityShulker) && !entity.noClip) {
                            entity.move(MoverType.SHULKER, d3, d4, d5);
                        }
                    }
                }
            }
        }

    }

    public void move(MoverType enummovetype, double d0, double d1, double d2) {
        if (enummovetype == MoverType.SHULKER_BOX) {
            this.tryTeleportToNewPosition();
        } else {
            super.move(enummovetype, d0, d1, d2);
        }

    }

    public void setPosition(double d0, double d1, double d2) {
        super.setPosition(d0, d1, d2);
        if (this.dataManager != null && this.ticksExisted != 0) {
            Optional optional = (Optional) this.dataManager.get(EntityShulker.ATTACHED_BLOCK_POS);
            Optional optional1 = Optional.of(new BlockPos(d0, d1, d2));

            if (!optional1.equals(optional)) {
                this.dataManager.set(EntityShulker.ATTACHED_BLOCK_POS, optional1);
                this.dataManager.set(EntityShulker.PEEK_TICK, Byte.valueOf((byte) 0));
                this.isAirBorne = true;
            }

        }
    }

    protected boolean tryTeleportToNewPosition() {
        if (!this.isAIDisabled() && this.isEntityAlive()) {
            BlockPos blockposition = new BlockPos(this);

            for (int i = 0; i < 5; ++i) {
                BlockPos blockposition1 = blockposition.add(8 - this.rand.nextInt(17), 8 - this.rand.nextInt(17), 8 - this.rand.nextInt(17));

                if (blockposition1.getY() > 0 && this.world.isAirBlock(blockposition1) && this.world.isInsideWorldBorder(this) && this.world.getCollisionBoxes(this, new AxisAlignedBB(blockposition1)).isEmpty()) {
                    boolean flag = false;
                    EnumFacing[] aenumdirection = EnumFacing.values();
                    int j = aenumdirection.length;

                    for (int k = 0; k < j; ++k) {
                        EnumFacing enumdirection = aenumdirection[k];

                        if (this.world.isBlockNormalCube(blockposition1.offset(enumdirection), false)) {
                            // CraftBukkit start
                            EntityTeleportEvent teleport = new EntityTeleportEvent(this.getBukkitEntity(), this.getBukkitEntity().getLocation(), new Location(this.world.getWorld(), blockposition1.getX(), blockposition1.getY(), blockposition1.getZ()));
                            this.world.getServer().getPluginManager().callEvent(teleport);
                            if (!teleport.isCancelled()) {
                                Location to = teleport.getTo();
                                blockposition1 = new BlockPos(to.getX(), to.getY(), to.getZ());

                                this.dataManager.set(EntityShulker.ATTACHED_FACE, enumdirection);
                                flag = true;
                            }
                            // CraftBukkit end
                            break;
                        }
                    }

                    if (flag) {
                        this.playSound(SoundEvents.ENTITY_SHULKER_TELEPORT, 1.0F, 1.0F);
                        this.dataManager.set(EntityShulker.ATTACHED_BLOCK_POS, Optional.of(blockposition1));
                        this.dataManager.set(EntityShulker.PEEK_TICK, Byte.valueOf((byte) 0));
                        this.setAttackTarget((EntityLivingBase) null);
                        return true;
                    }
                }
            }

            return false;
        } else {
            return true;
        }
    }

    public void onLivingUpdate() {
        super.onLivingUpdate();
        this.motionX = 0.0D;
        this.motionY = 0.0D;
        this.motionZ = 0.0D;
        this.prevRenderYawOffset = 180.0F;
        this.renderYawOffset = 180.0F;
        this.rotationYaw = 180.0F;
    }

    public void notifyDataManagerChange(DataParameter<?> datawatcherobject) {
        if (EntityShulker.ATTACHED_BLOCK_POS.equals(datawatcherobject) && this.world.isRemote && !this.isRiding()) {
            BlockPos blockposition = this.getAttachmentPos();

            if (blockposition != null) {
                if (this.currentAttachmentPosition == null) {
                    this.currentAttachmentPosition = blockposition;
                } else {
                    this.clientSideTeleportInterpolation = 6;
                }

                this.posX = (double) blockposition.getX() + 0.5D;
                this.posY = (double) blockposition.getY();
                this.posZ = (double) blockposition.getZ() + 0.5D;
                this.prevPosX = this.posX;
                this.prevPosY = this.posY;
                this.prevPosZ = this.posZ;
                this.lastTickPosX = this.posX;
                this.lastTickPosY = this.posY;
                this.lastTickPosZ = this.posZ;
            }
        }

        super.notifyDataManagerChange(datawatcherobject);
    }

    public boolean attackEntityFrom(DamageSource damagesource, float f) {
        if (this.isClosed()) {
            Entity entity = damagesource.getImmediateSource();

            if (entity instanceof EntityArrow) {
                return false;
            }
        }

        if (super.attackEntityFrom(damagesource, f)) {
            if ((double) this.getHealth() < (double) this.getMaxHealth() * 0.5D && this.rand.nextInt(4) == 0) {
                this.tryTeleportToNewPosition();
            }

            return true;
        } else {
            return false;
        }
    }

    private boolean isClosed() {
        return this.getPeekTick() == 0;
    }

    @Nullable
    public AxisAlignedBB getCollisionBoundingBox() {
        return this.isEntityAlive() ? this.getEntityBoundingBox() : null;
    }

    public EnumFacing getAttachmentFacing() {
        return (EnumFacing) this.dataManager.get(EntityShulker.ATTACHED_FACE);
    }

    @Nullable
    public BlockPos getAttachmentPos() {
        return (BlockPos) ((Optional) this.dataManager.get(EntityShulker.ATTACHED_BLOCK_POS)).orNull();
    }

    public void setAttachmentPos(@Nullable BlockPos blockposition) {
        this.dataManager.set(EntityShulker.ATTACHED_BLOCK_POS, Optional.fromNullable(blockposition));
    }

    public int getPeekTick() {
        return ((Byte) this.dataManager.get(EntityShulker.PEEK_TICK)).byteValue();
    }

    public void updateArmorModifier(int i) {
        if (!this.world.isRemote) {
            this.getEntityAttribute(SharedMonsterAttributes.ARMOR).removeModifier(EntityShulker.COVERED_ARMOR_BONUS_MODIFIER);
            if (i == 0) {
                this.getEntityAttribute(SharedMonsterAttributes.ARMOR).applyModifier(EntityShulker.COVERED_ARMOR_BONUS_MODIFIER);
                this.playSound(SoundEvents.ENTITY_SHULKER_CLOSE, 1.0F, 1.0F);
            } else {
                this.playSound(SoundEvents.ENTITY_SHULKER_OPEN, 1.0F, 1.0F);
            }
        }

        this.dataManager.set(EntityShulker.PEEK_TICK, Byte.valueOf((byte) i));
    }

    public float getEyeHeight() {
        return 0.5F;
    }

    public int getVerticalFaceSpeed() {
        return 180;
    }

    public int getHorizontalFaceSpeed() {
        return 180;
    }

    public void applyEntityCollision(Entity entity) {}

    public float getCollisionBorderSize() {
        return 0.0F;
    }

    @Nullable
    protected ResourceLocation getLootTable() {
        return LootTableList.ENTITIES_SHULKER;
    }

    static class c extends EntityAINearestAttackableTarget<EntityLivingBase> {

        public c(EntityShulker entityshulker) {
            super(entityshulker, EntityLivingBase.class, 10, true, false, new Predicate() {
                public boolean a(@Nullable EntityLivingBase entityliving) {
                    return entityliving instanceof IMob;
                }

                public boolean apply(@Nullable Object object) {
                    return this.a((EntityLivingBase) object);
                }
            });
        }

        public boolean shouldExecute() {
            return this.taskOwner.getTeam() == null ? false : super.shouldExecute();
        }

        protected AxisAlignedBB getTargetableArea(double d0) {
            EnumFacing enumdirection = ((EntityShulker) this.taskOwner).getAttachmentFacing();

            return enumdirection.getAxis() == EnumFacing.Axis.X ? this.taskOwner.getEntityBoundingBox().grow(4.0D, d0, d0) : (enumdirection.getAxis() == EnumFacing.Axis.Z ? this.taskOwner.getEntityBoundingBox().grow(d0, d0, 4.0D) : this.taskOwner.getEntityBoundingBox().grow(d0, 4.0D, d0));
        }
    }

    class d extends EntityAINearestAttackableTarget<EntityPlayer> {

        public d(EntityShulker entityshulker) {
            super(entityshulker, EntityPlayer.class, true);
        }

        public boolean shouldExecute() {
            return EntityShulker.this.world.getDifficulty() == EnumDifficulty.PEACEFUL ? false : super.shouldExecute();
        }

        protected AxisAlignedBB getTargetableArea(double d0) {
            EnumFacing enumdirection = ((EntityShulker) this.taskOwner).getAttachmentFacing();

            return enumdirection.getAxis() == EnumFacing.Axis.X ? this.taskOwner.getEntityBoundingBox().grow(4.0D, d0, d0) : (enumdirection.getAxis() == EnumFacing.Axis.Z ? this.taskOwner.getEntityBoundingBox().grow(d0, d0, 4.0D) : this.taskOwner.getEntityBoundingBox().grow(d0, 4.0D, d0));
        }
    }

    class a extends EntityAIBase {

        private int b;

        public a() {
            this.setMutexBits(3);
        }

        public boolean shouldExecute() {
            EntityLivingBase entityliving = EntityShulker.this.getAttackTarget();

            return entityliving != null && entityliving.isEntityAlive() ? EntityShulker.this.world.getDifficulty() != EnumDifficulty.PEACEFUL : false;
        }

        public void startExecuting() {
            this.b = 20;
            EntityShulker.this.updateArmorModifier(100);
        }

        public void resetTask() {
            EntityShulker.this.updateArmorModifier(0);
        }

        public void updateTask() {
            if (EntityShulker.this.world.getDifficulty() != EnumDifficulty.PEACEFUL) {
                --this.b;
                EntityLivingBase entityliving = EntityShulker.this.getAttackTarget();

                EntityShulker.this.getLookHelper().setLookPositionWithEntity(entityliving, 180.0F, 180.0F);
                double d0 = EntityShulker.this.getDistanceSq(entityliving);

                if (d0 < 400.0D) {
                    if (this.b <= 0) {
                        this.b = 20 + EntityShulker.this.rand.nextInt(10) * 20 / 2;
                        EntityShulkerBullet entityshulkerbullet = new EntityShulkerBullet(EntityShulker.this.world, EntityShulker.this, entityliving, EntityShulker.this.getAttachmentFacing().getAxis());

                        EntityShulker.this.world.spawnEntity(entityshulkerbullet);
                        EntityShulker.this.playSound(SoundEvents.ENTITY_SHULKER_SHOOT, 2.0F, (EntityShulker.this.rand.nextFloat() - EntityShulker.this.rand.nextFloat()) * 0.2F + 1.0F);
                    }
                } else {
                    EntityShulker.this.setAttackTarget((EntityLivingBase) null);
                }

                super.updateTask();
            }
        }
    }

    class e extends EntityAIBase {

        private int b;

        private e() {}

        public boolean shouldExecute() {
            return EntityShulker.this.getAttackTarget() == null && EntityShulker.this.rand.nextInt(40) == 0;
        }

        public boolean shouldContinueExecuting() {
            return EntityShulker.this.getAttackTarget() == null && this.b > 0;
        }

        public void startExecuting() {
            this.b = 20 * (1 + EntityShulker.this.rand.nextInt(3));
            EntityShulker.this.updateArmorModifier(30);
        }

        public void resetTask() {
            if (EntityShulker.this.getAttackTarget() == null) {
                EntityShulker.this.updateArmorModifier(0);
            }

        }

        public void updateTask() {
            --this.b;
        }

        e(Object object) {
            this();
        }
    }

    class b extends EntityBodyHelper {

        public b(EntityLivingBase entityliving) {
            super(entityliving);
        }

        public void updateRenderAngles() {}
    }
}
