package net.minecraft.entity.boss.dragon.phase;

import javax.annotation.Nullable;

import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.gen.feature.WorldGenEndPodium;

public class PhaseHoldingPattern extends PhaseBase {

    private Path currentPath;
    private Vec3d targetLocation;
    private boolean clockwise;

    public PhaseHoldingPattern(EntityDragon entityenderdragon) {
        super(entityenderdragon);
    }

    public PhaseList<PhaseHoldingPattern> getType() {
        return PhaseList.HOLDING_PATTERN;
    }

    public void doLocalUpdate() {
        double d0 = this.targetLocation == null ? 0.0D : this.targetLocation.squareDistanceTo(this.dragon.posX, this.dragon.posY, this.dragon.posZ);

        if (d0 < 100.0D || d0 > 22500.0D || this.dragon.collidedHorizontally || this.dragon.collidedVertically) {
            this.findNewTarget();
        }

    }

    public void initPhase() {
        this.currentPath = null;
        this.targetLocation = null;
    }

    @Nullable
    public Vec3d getTargetLocation() {
        return this.targetLocation;
    }

    private void findNewTarget() {
        int i;

        if (this.currentPath != null && this.currentPath.isFinished()) {
            BlockPos blockposition = this.dragon.world.getTopSolidOrLiquidBlock(new BlockPos(WorldGenEndPodium.END_PODIUM_LOCATION));

            i = this.dragon.getFightManager() == null ? 0 : this.dragon.getFightManager().getNumAliveCrystals();
            if (this.dragon.getRNG().nextInt(i + 3) == 0) {
                this.dragon.getPhaseManager().setPhase(PhaseList.LANDING_APPROACH);
                return;
            }

            double d0 = 64.0D;
            EntityPlayer entityhuman = this.dragon.world.getNearestAttackablePlayer(blockposition, d0, d0);

            if (entityhuman != null) {
                d0 = entityhuman.getDistanceSqToCenter(blockposition) / 512.0D;
            }

            if (entityhuman != null && (this.dragon.getRNG().nextInt(MathHelper.abs((int) d0) + 2) == 0 || this.dragon.getRNG().nextInt(i + 2) == 0)) {
                this.strafePlayer(entityhuman);
                return;
            }
        }

        if (this.currentPath == null || this.currentPath.isFinished()) {
            int j = this.dragon.initPathPoints();

            i = j;
            if (this.dragon.getRNG().nextInt(8) == 0) {
                this.clockwise = !this.clockwise;
                i = j + 6;
            }

            if (this.clockwise) {
                ++i;
            } else {
                --i;
            }

            if (this.dragon.getFightManager() != null && this.dragon.getFightManager().getNumAliveCrystals() >= 0) {
                i %= 12;
                if (i < 0) {
                    i += 12;
                }
            } else {
                i -= 12;
                i &= 7;
                i += 12;
            }

            this.currentPath = this.dragon.findPath(j, i, (PathPoint) null);
            if (this.currentPath != null) {
                this.currentPath.incrementPathIndex();
            }
        }

        this.navigateToNextPathNode();
    }

    private void strafePlayer(EntityPlayer entityhuman) {
        this.dragon.getPhaseManager().setPhase(PhaseList.STRAFE_PLAYER);
        ((PhaseStrafePlayer) this.dragon.getPhaseManager().getPhase(PhaseList.STRAFE_PLAYER)).setTarget(entityhuman);
    }

    private void navigateToNextPathNode() {
        if (this.currentPath != null && !this.currentPath.isFinished()) {
            Vec3d vec3d = this.currentPath.getCurrentPos();

            this.currentPath.incrementPathIndex();
            double d0 = vec3d.x;
            double d1 = vec3d.z;

            double d2;

            do {
                d2 = vec3d.y + (double) (this.dragon.getRNG().nextFloat() * 20.0F);
            } while (d2 < vec3d.y);

            this.targetLocation = new Vec3d(d0, d2, d1);
        }

    }

    public void onCrystalDestroyed(EntityEnderCrystal entityendercrystal, BlockPos blockposition, DamageSource damagesource, @Nullable EntityPlayer entityhuman) {
        if (entityhuman != null && !entityhuman.capabilities.disableDamage) {
            this.strafePlayer(entityhuman);
        }

    }
}
