package net.minecraft.entity.item;
import net.minecraft.block.BlockFurnace;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;


public class EntityMinecartFurnace extends EntityMinecart {

    private static final DataParameter<Boolean> POWERED = EntityDataManager.createKey(EntityMinecartFurnace.class, DataSerializers.BOOLEAN);
    private int fuel;
    public double pushX;
    public double pushZ;

    public EntityMinecartFurnace(World world) {
        super(world);
    }

    public EntityMinecartFurnace(World world, double d0, double d1, double d2) {
        super(world, d0, d1, d2);
    }

    public static void registerFixesMinecartFurnace(DataFixer dataconvertermanager) {
        EntityMinecart.registerFixesMinecart(dataconvertermanager, EntityMinecartFurnace.class);
    }

    public EntityMinecart.Type getType() {
        return EntityMinecart.Type.FURNACE;
    }

    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(EntityMinecartFurnace.POWERED, Boolean.valueOf(false));
    }

    public void onUpdate() {
        super.onUpdate();
        if (this.fuel > 0) {
            --this.fuel;
        }

        if (this.fuel <= 0) {
            this.pushX = 0.0D;
            this.pushZ = 0.0D;
        }

        this.setMinecartPowered(this.fuel > 0);
        if (this.isMinecartPowered() && this.rand.nextInt(4) == 0) {
            this.world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, this.posX, this.posY + 0.8D, this.posZ, 0.0D, 0.0D, 0.0D, new int[0]);
        }

    }

    protected double getMaximumSpeed() {
        return 0.2D;
    }

    public void killMinecart(DamageSource damagesource) {
        super.killMinecart(damagesource);
        if (!damagesource.isExplosion() && this.world.getGameRules().getBoolean("doEntityDrops")) {
            this.entityDropItem(new ItemStack(Blocks.FURNACE, 1), 0.0F);
        }

    }

    protected void moveAlongTrack(BlockPos blockposition, IBlockState iblockdata) {
        super.moveAlongTrack(blockposition, iblockdata);
        double d0 = this.pushX * this.pushX + this.pushZ * this.pushZ;

        if (d0 > 1.0E-4D && this.motionX * this.motionX + this.motionZ * this.motionZ > 0.001D) {
            d0 = (double) MathHelper.sqrt(d0);
            this.pushX /= d0;
            this.pushZ /= d0;
            if (this.pushX * this.motionX + this.pushZ * this.motionZ < 0.0D) {
                this.pushX = 0.0D;
                this.pushZ = 0.0D;
            } else {
                double d1 = d0 / this.getMaximumSpeed();

                this.pushX *= d1;
                this.pushZ *= d1;
            }
        }

    }

    protected void applyDrag() {
        double d0 = this.pushX * this.pushX + this.pushZ * this.pushZ;

        if (d0 > 1.0E-4D) {
            d0 = (double) MathHelper.sqrt(d0);
            this.pushX /= d0;
            this.pushZ /= d0;
            double d1 = 1.0D;

            this.motionX *= 0.800000011920929D;
            this.motionY *= 0.0D;
            this.motionZ *= 0.800000011920929D;
            this.motionX += this.pushX * 1.0D;
            this.motionZ += this.pushZ * 1.0D;
        } else {
            this.motionX *= 0.9800000190734863D;
            this.motionY *= 0.0D;
            this.motionZ *= 0.9800000190734863D;
        }

        super.applyDrag();
    }

    public boolean processInitialInteract(EntityPlayer entityhuman, EnumHand enumhand) {
        ItemStack itemstack = entityhuman.getHeldItem(enumhand);

        if (itemstack.getItem() == Items.COAL && this.fuel + 3600 <= 32000) {
            if (!entityhuman.capabilities.isCreativeMode) {
                itemstack.shrink(1);
            }

            this.fuel += 3600;
        }

        this.pushX = this.posX - entityhuman.posX;
        this.pushZ = this.posZ - entityhuman.posZ;
        return true;
    }

    protected void writeEntityToNBT(NBTTagCompound nbttagcompound) {
        super.writeEntityToNBT(nbttagcompound);
        nbttagcompound.setDouble("PushX", this.pushX);
        nbttagcompound.setDouble("PushZ", this.pushZ);
        nbttagcompound.setShort("Fuel", (short) this.fuel);
    }

    protected void readEntityFromNBT(NBTTagCompound nbttagcompound) {
        super.readEntityFromNBT(nbttagcompound);
        this.pushX = nbttagcompound.getDouble("PushX");
        this.pushZ = nbttagcompound.getDouble("PushZ");
        this.fuel = nbttagcompound.getShort("Fuel");
    }

    protected boolean isMinecartPowered() {
        return ((Boolean) this.dataManager.get(EntityMinecartFurnace.POWERED)).booleanValue();
    }

    protected void setMinecartPowered(boolean flag) {
        this.dataManager.set(EntityMinecartFurnace.POWERED, Boolean.valueOf(flag));
    }

    public IBlockState getDefaultDisplayTile() {
        return (this.isMinecartPowered() ? Blocks.LIT_FURNACE : Blocks.FURNACE).getDefaultState().withProperty(BlockFurnace.FACING, EnumFacing.NORTH);
    }
}
