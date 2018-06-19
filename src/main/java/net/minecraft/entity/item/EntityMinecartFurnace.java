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

    private static final DataParameter<Boolean> field_184275_c = EntityDataManager.func_187226_a(EntityMinecartFurnace.class, DataSerializers.field_187198_h);
    private int field_94110_c;
    public double field_94111_a;
    public double field_94109_b;

    public EntityMinecartFurnace(World world) {
        super(world);
    }

    public EntityMinecartFurnace(World world, double d0, double d1, double d2) {
        super(world, d0, d1, d2);
    }

    public static void func_189671_a(DataFixer dataconvertermanager) {
        EntityMinecart.func_189669_a(dataconvertermanager, EntityMinecartFurnace.class);
    }

    public EntityMinecart.Type func_184264_v() {
        return EntityMinecart.Type.FURNACE;
    }

    protected void func_70088_a() {
        super.func_70088_a();
        this.field_70180_af.func_187214_a(EntityMinecartFurnace.field_184275_c, Boolean.valueOf(false));
    }

    public void func_70071_h_() {
        super.func_70071_h_();
        if (this.field_94110_c > 0) {
            --this.field_94110_c;
        }

        if (this.field_94110_c <= 0) {
            this.field_94111_a = 0.0D;
            this.field_94109_b = 0.0D;
        }

        this.func_94107_f(this.field_94110_c > 0);
        if (this.func_94108_c() && this.field_70146_Z.nextInt(4) == 0) {
            this.field_70170_p.func_175688_a(EnumParticleTypes.SMOKE_LARGE, this.field_70165_t, this.field_70163_u + 0.8D, this.field_70161_v, 0.0D, 0.0D, 0.0D, new int[0]);
        }

    }

    protected double func_174898_m() {
        return 0.2D;
    }

    public void func_94095_a(DamageSource damagesource) {
        super.func_94095_a(damagesource);
        if (!damagesource.func_94541_c() && this.field_70170_p.func_82736_K().func_82766_b("doEntityDrops")) {
            this.func_70099_a(new ItemStack(Blocks.field_150460_al, 1), 0.0F);
        }

    }

    protected void func_180460_a(BlockPos blockposition, IBlockState iblockdata) {
        super.func_180460_a(blockposition, iblockdata);
        double d0 = this.field_94111_a * this.field_94111_a + this.field_94109_b * this.field_94109_b;

        if (d0 > 1.0E-4D && this.field_70159_w * this.field_70159_w + this.field_70179_y * this.field_70179_y > 0.001D) {
            d0 = (double) MathHelper.func_76133_a(d0);
            this.field_94111_a /= d0;
            this.field_94109_b /= d0;
            if (this.field_94111_a * this.field_70159_w + this.field_94109_b * this.field_70179_y < 0.0D) {
                this.field_94111_a = 0.0D;
                this.field_94109_b = 0.0D;
            } else {
                double d1 = d0 / this.func_174898_m();

                this.field_94111_a *= d1;
                this.field_94109_b *= d1;
            }
        }

    }

    protected void func_94101_h() {
        double d0 = this.field_94111_a * this.field_94111_a + this.field_94109_b * this.field_94109_b;

        if (d0 > 1.0E-4D) {
            d0 = (double) MathHelper.func_76133_a(d0);
            this.field_94111_a /= d0;
            this.field_94109_b /= d0;
            double d1 = 1.0D;

            this.field_70159_w *= 0.800000011920929D;
            this.field_70181_x *= 0.0D;
            this.field_70179_y *= 0.800000011920929D;
            this.field_70159_w += this.field_94111_a * 1.0D;
            this.field_70179_y += this.field_94109_b * 1.0D;
        } else {
            this.field_70159_w *= 0.9800000190734863D;
            this.field_70181_x *= 0.0D;
            this.field_70179_y *= 0.9800000190734863D;
        }

        super.func_94101_h();
    }

    public boolean func_184230_a(EntityPlayer entityhuman, EnumHand enumhand) {
        ItemStack itemstack = entityhuman.func_184586_b(enumhand);

        if (itemstack.func_77973_b() == Items.field_151044_h && this.field_94110_c + 3600 <= 32000) {
            if (!entityhuman.field_71075_bZ.field_75098_d) {
                itemstack.func_190918_g(1);
            }

            this.field_94110_c += 3600;
        }

        this.field_94111_a = this.field_70165_t - entityhuman.field_70165_t;
        this.field_94109_b = this.field_70161_v - entityhuman.field_70161_v;
        return true;
    }

    protected void func_70014_b(NBTTagCompound nbttagcompound) {
        super.func_70014_b(nbttagcompound);
        nbttagcompound.func_74780_a("PushX", this.field_94111_a);
        nbttagcompound.func_74780_a("PushZ", this.field_94109_b);
        nbttagcompound.func_74777_a("Fuel", (short) this.field_94110_c);
    }

    protected void func_70037_a(NBTTagCompound nbttagcompound) {
        super.func_70037_a(nbttagcompound);
        this.field_94111_a = nbttagcompound.func_74769_h("PushX");
        this.field_94109_b = nbttagcompound.func_74769_h("PushZ");
        this.field_94110_c = nbttagcompound.func_74765_d("Fuel");
    }

    protected boolean func_94108_c() {
        return ((Boolean) this.field_70180_af.func_187225_a(EntityMinecartFurnace.field_184275_c)).booleanValue();
    }

    protected void func_94107_f(boolean flag) {
        this.field_70180_af.func_187227_b(EntityMinecartFurnace.field_184275_c, Boolean.valueOf(flag));
    }

    public IBlockState func_180457_u() {
        return (this.func_94108_c() ? Blocks.field_150470_am : Blocks.field_150460_al).func_176223_P().func_177226_a(BlockFurnace.field_176447_a, EnumFacing.NORTH);
    }
}
