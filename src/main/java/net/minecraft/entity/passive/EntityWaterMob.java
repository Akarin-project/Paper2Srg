package net.minecraft.entity.passive;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;


public abstract class EntityWaterMob extends EntityLiving implements IAnimals {

    public EntityWaterMob(World world) {
        super(world);
    }

    public boolean canBreatheUnderwater() {
        return true;
    }

    public boolean getCanSpawnHere() {
        // Paper start - Don't let water mobs spawn in non-water blocks
        // Based around EntityAnimal's implementation
        int i = MathHelper.floor(this.posX);
        int j = MathHelper.floor(this.getEntityBoundingBox().minY); // minY of bounding box
        int k = MathHelper.floor(this.posZ);
        Block block = this.world.getBlockState(new BlockPos(i, j, k)).getBlock();

        return block == Blocks.WATER || block == Blocks.FLOWING_WATER;
        // Paper end
    }

    public boolean isNotColliding() {
        return this.world.checkNoEntityCollision(this.getEntityBoundingBox(), (Entity) this);
    }

    public int getTalkInterval() {
        return 120;
    }

    protected boolean canDespawn() {
        return true;
    }

    protected int getExperiencePoints(EntityPlayer entityhuman) {
        return 1 + this.world.rand.nextInt(3);
    }

    public void onEntityUpdate() {
        int i = this.getAir();

        super.onEntityUpdate();
        if (this.isEntityAlive() && !this.isInWater()) {
            --i;
            this.setAir(i);
            if (this.getAir() == -20) {
                this.setAir(0);
                this.attackEntityFrom(DamageSource.DROWN, 2.0F);
            }
        } else {
            this.setAir(300);
        }

    }

    public boolean isPushedByWater() {
        return false;
    }
}
