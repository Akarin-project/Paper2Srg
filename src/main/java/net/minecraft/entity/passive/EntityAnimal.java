package net.minecraft.entity.passive;

import java.util.UUID;
import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public abstract class EntityAnimal extends EntityAgeable implements IAnimals {

    protected Block field_175506_bl;
    private int field_70881_d;
    private UUID field_146084_br;
    public ItemStack breedItem; // CraftBukkit - Add breedItem variable

    public EntityAnimal(World world) {
        super(world);
        this.field_175506_bl = Blocks.field_150349_c;
    }

    protected void func_70619_bc() {
        if (this.func_70874_b() != 0) {
            this.field_70881_d = 0;
        }

        super.func_70619_bc();
    }

    public void func_70636_d() {
        super.func_70636_d();
        if (this.func_70874_b() != 0) {
            this.field_70881_d = 0;
        }

        if (this.field_70881_d > 0) {
            --this.field_70881_d;
            if (this.field_70881_d % 10 == 0) {
                double d0 = this.field_70146_Z.nextGaussian() * 0.02D;
                double d1 = this.field_70146_Z.nextGaussian() * 0.02D;
                double d2 = this.field_70146_Z.nextGaussian() * 0.02D;

                this.field_70170_p.func_175688_a(EnumParticleTypes.HEART, this.field_70165_t + (double) (this.field_70146_Z.nextFloat() * this.field_70130_N * 2.0F) - (double) this.field_70130_N, this.field_70163_u + 0.5D + (double) (this.field_70146_Z.nextFloat() * this.field_70131_O), this.field_70161_v + (double) (this.field_70146_Z.nextFloat() * this.field_70130_N * 2.0F) - (double) this.field_70130_N, d0, d1, d2, new int[0]);
            }
        }

    }

    /* CraftBukkit start
    // Function disabled as it has no special function anymore after
    // setSitting is disabled.
    public boolean damageEntity(DamageSource damagesource, float f) {
        if (this.isInvulnerable(damagesource)) {
            return false;
        } else {
            this.bx = 0;
            return super.damageEntity(damagesource, f);
        }
    }
    // CraftBukkit end */

    public float func_180484_a(BlockPos blockposition) {
        return this.field_70170_p.func_180495_p(blockposition.func_177977_b()).func_177230_c() == this.field_175506_bl ? 10.0F : this.field_70170_p.func_175724_o(blockposition) - 0.5F;
    }

    public void func_70014_b(NBTTagCompound nbttagcompound) {
        super.func_70014_b(nbttagcompound);
        nbttagcompound.func_74768_a("InLove", this.field_70881_d);
        if (this.field_146084_br != null) {
            nbttagcompound.func_186854_a("LoveCause", this.field_146084_br);
        }

    }

    public double func_70033_W() {
        return 0.14D;
    }

    public void func_70037_a(NBTTagCompound nbttagcompound) {
        super.func_70037_a(nbttagcompound);
        this.field_70881_d = nbttagcompound.func_74762_e("InLove");
        this.field_146084_br = nbttagcompound.func_186855_b("LoveCause") ? nbttagcompound.func_186857_a("LoveCause") : null;
    }

    public boolean func_70601_bi() {
        int i = MathHelper.func_76128_c(this.field_70165_t);
        int j = MathHelper.func_76128_c(this.func_174813_aQ().field_72338_b);
        int k = MathHelper.func_76128_c(this.field_70161_v);
        BlockPos blockposition = new BlockPos(i, j, k);

        return this.field_70170_p.func_180495_p(blockposition.func_177977_b()).func_177230_c() == this.field_175506_bl && this.field_70170_p.func_175699_k(blockposition) > 8 && super.func_70601_bi();
    }

    public int func_70627_aG() {
        return 120;
    }

    protected boolean func_70692_ba() {
        return false;
    }

    protected int func_70693_a(EntityPlayer entityhuman) {
        return 1 + this.field_70170_p.field_73012_v.nextInt(3);
    }

    public boolean func_70877_b(ItemStack itemstack) {
        return itemstack.func_77973_b() == Items.field_151015_O;
    }

    public boolean func_184645_a(EntityPlayer entityhuman, EnumHand enumhand) {
        ItemStack itemstack = entityhuman.func_184586_b(enumhand);

        if (!itemstack.func_190926_b()) {
            if (this.func_70877_b(itemstack) && this.func_70874_b() == 0 && this.field_70881_d <= 0) {
                this.func_175505_a(entityhuman, itemstack);
                this.func_146082_f(entityhuman);
                return true;
            }

            if (this.func_70631_g_() && this.func_70877_b(itemstack)) {
                this.func_175505_a(entityhuman, itemstack);
                this.func_175501_a((int) ((float) (-this.func_70874_b() / 20) * 0.1F), true);
                return true;
            }
        }

        return super.func_184645_a(entityhuman, enumhand);
    }

    protected void func_175505_a(EntityPlayer entityhuman, ItemStack itemstack) {
        if (!entityhuman.field_71075_bZ.field_75098_d) {
            itemstack.func_190918_g(1);
        }

    }

    public void func_146082_f(@Nullable EntityPlayer entityhuman) {
        this.field_70881_d = 600;
        if (entityhuman != null) {
            this.field_146084_br = entityhuman.func_110124_au();
        }
        this.breedItem = entityhuman.field_71071_by.func_70448_g(); // CraftBukkit

        this.field_70170_p.func_72960_a(this, (byte) 18);
    }

    @Nullable
    public EntityPlayerMP func_191993_do() {
        if (this.field_146084_br == null) {
            return null;
        } else {
            EntityPlayer entityhuman = this.field_70170_p.func_152378_a(this.field_146084_br);

            return entityhuman instanceof EntityPlayerMP ? (EntityPlayerMP) entityhuman : null;
        }
    }

    public boolean func_70880_s() {
        return this.field_70881_d > 0;
    }

    public void func_70875_t() {
        this.field_70881_d = 0;
    }

    public boolean func_70878_b(EntityAnimal entityanimal) {
        return entityanimal == this ? false : (entityanimal.getClass() != this.getClass() ? false : this.func_70880_s() && entityanimal.func_70880_s());
    }
}
