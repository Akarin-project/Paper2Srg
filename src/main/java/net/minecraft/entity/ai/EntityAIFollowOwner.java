package net.minecraft.entity.ai;

// CraftBukkit start
import org.bukkit.Location;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.event.entity.EntityTeleportEvent;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNavigateFlying;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

// CraftBukkit end

public class EntityAIFollowOwner extends EntityAIBase {

    private final EntityTameable tameable;
    private EntityLivingBase owner;
    World world;
    private final double followSpeed;
    private final PathNavigate petPathfinder;
    private int timeToRecalcPath;
    float maxDist;
    float minDist;
    private float oldWaterCost;

    public EntityAIFollowOwner(EntityTameable entitytameableanimal, double d0, float f, float f1) {
        this.tameable = entitytameableanimal;
        this.world = entitytameableanimal.world;
        this.followSpeed = d0;
        this.petPathfinder = entitytameableanimal.getNavigator();
        this.minDist = f;
        this.maxDist = f1;
        this.setMutexBits(3);
        if (!(entitytameableanimal.getNavigator() instanceof PathNavigateGround) && !(entitytameableanimal.getNavigator() instanceof PathNavigateFlying)) {
            throw new IllegalArgumentException("Unsupported mob type for FollowOwnerGoal");
        }
    }

    public boolean shouldExecute() {
        EntityLivingBase entityliving = this.tameable.getOwner();

        if (entityliving == null) {
            return false;
        } else if (entityliving instanceof EntityPlayer && ((EntityPlayer) entityliving).isSpectator()) {
            return false;
        } else if (this.tameable.isSitting()) {
            return false;
        } else if (this.tameable.getDistanceSq(entityliving) < (double) (this.minDist * this.minDist)) {
            return false;
        } else {
            this.owner = entityliving;
            return true;
        }
    }

    public boolean shouldContinueExecuting() {
        return !this.petPathfinder.noPath() && this.tameable.getDistanceSq(this.owner) > (double) (this.maxDist * this.maxDist) && !this.tameable.isSitting();
    }

    public void startExecuting() {
        this.timeToRecalcPath = 0;
        this.oldWaterCost = this.tameable.getPathPriority(PathNodeType.WATER);
        this.tameable.setPathPriority(PathNodeType.WATER, 0.0F);
    }

    public void resetTask() {
        this.owner = null;
        this.petPathfinder.clearPath();
        this.tameable.setPathPriority(PathNodeType.WATER, this.oldWaterCost);
    }

    public void updateTask() {
        this.tameable.getLookHelper().setLookPositionWithEntity(this.owner, 10.0F, (float) this.tameable.getVerticalFaceSpeed());
        if (!this.tameable.isSitting()) {
            if (--this.timeToRecalcPath <= 0) {
                this.timeToRecalcPath = 10;
                if (!this.petPathfinder.tryMoveToEntityLiving((Entity) this.owner, this.followSpeed)) {
                    if (!this.tameable.getLeashed() && !this.tameable.isRiding()) {
                        if (this.tameable.getDistanceSq(this.owner) >= 144.0D) {
                            int i = MathHelper.floor(this.owner.posX) - 2;
                            int j = MathHelper.floor(this.owner.posZ) - 2;
                            int k = MathHelper.floor(this.owner.getEntityBoundingBox().minY);

                            for (int l = 0; l <= 4; ++l) {
                                for (int i1 = 0; i1 <= 4; ++i1) {
                                    if ((l < 1 || i1 < 1 || l > 3 || i1 > 3) && this.isTeleportFriendlyBlock(i, j, k, l, i1)) {
                                        // CraftBukkit start
                                        CraftEntity entity = this.tameable.getBukkitEntity();
                                        Location to = new Location(entity.getWorld(), (double) ((float) (i + l) + 0.5F), (double) k, (double) ((float) (j + i1) + 0.5F), this.tameable.rotationYaw, this.tameable.rotationPitch);
                                        EntityTeleportEvent event = new EntityTeleportEvent(entity, entity.getLocation(), to);
                                        this.tameable.world.getServer().getPluginManager().callEvent(event);
                                        if (event.isCancelled()) {
                                            return;
                                        }
                                        to = event.getTo();

                                        this.tameable.setLocationAndAngles(to.getX(), to.getY(), to.getZ(), to.getYaw(), to.getPitch());
                                        // CraftBukkit end
                                        this.petPathfinder.clearPath();
                                        return;
                                    }
                                }
                            }

                        }
                    }
                }
            }
        }
    }

    protected boolean isTeleportFriendlyBlock(int i, int j, int k, int l, int i1) {
        BlockPos blockposition = new BlockPos(i + l, k - 1, j + i1);
        IBlockState iblockdata = this.world.getBlockState(blockposition);

        return iblockdata.getBlockFaceShape(this.world, blockposition, EnumFacing.DOWN) == BlockFaceShape.SOLID && iblockdata.canEntitySpawn((Entity) this.tameable) && this.world.isAirBlock(blockposition.up()) && this.world.isAirBlock(blockposition.up(2));
    }
}
