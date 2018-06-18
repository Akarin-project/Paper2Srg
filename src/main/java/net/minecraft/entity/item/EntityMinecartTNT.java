package net.minecraft.entity.item;
import net.minecraft.block.BlockRailBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;


public class EntityMinecartTNT extends EntityMinecart {

    private int minecartTNTFuse = -1;

    public EntityMinecartTNT(World world) {
        super(world);
    }

    public EntityMinecartTNT(World world, double d0, double d1, double d2) {
        super(world, d0, d1, d2);
    }

    public static void registerFixesMinecartTNT(DataFixer dataconvertermanager) {
        EntityMinecart.registerFixesMinecart(dataconvertermanager, EntityMinecartTNT.class);
    }

    public EntityMinecart.Type getType() {
        return EntityMinecart.Type.TNT;
    }

    public IBlockState getDefaultDisplayTile() {
        return Blocks.TNT.getDefaultState();
    }

    public void onUpdate() {
        super.onUpdate();
        if (this.minecartTNTFuse > 0) {
            --this.minecartTNTFuse;
            this.world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, this.posX, this.posY + 0.5D, this.posZ, 0.0D, 0.0D, 0.0D, new int[0]);
        } else if (this.minecartTNTFuse == 0) {
            this.explodeCart(this.motionX * this.motionX + this.motionZ * this.motionZ);
        }

        if (this.collidedHorizontally) {
            double d0 = this.motionX * this.motionX + this.motionZ * this.motionZ;

            if (d0 >= 0.009999999776482582D) {
                this.explodeCart(d0);
            }
        }

    }

    public boolean attackEntityFrom(DamageSource damagesource, float f) {
        Entity entity = damagesource.getImmediateSource();

        if (entity instanceof EntityArrow) {
            EntityArrow entityarrow = (EntityArrow) entity;

            if (entityarrow.isBurning()) {
                this.explodeCart(entityarrow.motionX * entityarrow.motionX + entityarrow.motionY * entityarrow.motionY + entityarrow.motionZ * entityarrow.motionZ);
            }
        }

        return super.attackEntityFrom(damagesource, f);
    }

    public void killMinecart(DamageSource damagesource) {
        double d0 = this.motionX * this.motionX + this.motionZ * this.motionZ;

        if (!damagesource.isFireDamage() && !damagesource.isExplosion() && d0 < 0.009999999776482582D) {
            super.killMinecart(damagesource);
            if (!damagesource.isExplosion() && this.world.getGameRules().getBoolean("doEntityDrops")) {
                this.entityDropItem(new ItemStack(Blocks.TNT, 1), 0.0F);
            }

        } else {
            if (this.minecartTNTFuse < 0) {
                this.ignite();
                this.minecartTNTFuse = this.rand.nextInt(20) + this.rand.nextInt(20);
            }

        }
    }

    protected void explodeCart(double d0) {
        if (!this.world.isRemote) {
            double d1 = Math.sqrt(d0);

            if (d1 > 5.0D) {
                d1 = 5.0D;
            }

            this.world.createExplosion(this, this.posX, this.posY, this.posZ, (float) (4.0D + this.rand.nextDouble() * 1.5D * d1), true);
            this.setDead();
        }

    }

    public void fall(float f, float f1) {
        if (f >= 3.0F) {
            float f2 = f / 10.0F;

            this.explodeCart((double) (f2 * f2));
        }

        super.fall(f, f1);
    }

    public void onActivatorRailPass(int i, int j, int k, boolean flag) {
        if (flag && this.minecartTNTFuse < 0) {
            this.ignite();
        }

    }

    public void ignite() {
        this.minecartTNTFuse = 80;
        if (!this.world.isRemote) {
            this.world.setEntityState(this, (byte) 10);
            if (!this.isSilent()) {
                this.world.playSound((EntityPlayer) null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.BLOCKS, 1.0F, 1.0F);
            }
        }

    }

    public boolean isIgnited() {
        return this.minecartTNTFuse > -1;
    }

    public float getExplosionResistance(Explosion explosion, World world, BlockPos blockposition, IBlockState iblockdata) {
        return this.isIgnited() && (BlockRailBase.isRailBlock(iblockdata) || BlockRailBase.isRailBlock(world, blockposition.up())) ? 0.0F : super.getExplosionResistance(explosion, world, blockposition, iblockdata);
    }

    public boolean canExplosionDestroyBlock(Explosion explosion, World world, BlockPos blockposition, IBlockState iblockdata, float f) {
        return this.isIgnited() && (BlockRailBase.isRailBlock(iblockdata) || BlockRailBase.isRailBlock(world, blockposition.up())) ? false : super.canExplosionDestroyBlock(explosion, world, blockposition, iblockdata, f);
    }

    protected void readEntityFromNBT(NBTTagCompound nbttagcompound) {
        super.readEntityFromNBT(nbttagcompound);
        if (nbttagcompound.hasKey("TNTFuse", 99)) {
            this.minecartTNTFuse = nbttagcompound.getInteger("TNTFuse");
        }

    }

    protected void writeEntityToNBT(NBTTagCompound nbttagcompound) {
        super.writeEntityToNBT(nbttagcompound);
        nbttagcompound.setInteger("TNTFuse", this.minecartTNTFuse);
    }
}
