package net.minecraft.tileentity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;


public class TileEntityEnderChest extends TileEntity { // Paper - Remove ITickable

    public float field_145972_a; // Paper - lid angle
    public float field_145975_i;
    public int field_145973_j; // Paper - Number of viewers
    private int field_145974_k;

    public TileEntityEnderChest() {}

    public void func_73660_a() {
        // Paper start - Disable all of this, just in case this does get ticked
        /*
        if (++this.h % 20 * 4 == 0) {
            this.world.playBlockAction(this.position, Blocks.ENDER_CHEST, 1, this.g);
        }

        this.f = this.a;
        int i = this.position.getX();
        int j = this.position.getY();
        int k = this.position.getZ();
        float f = 0.1F;
        double d0;

        if (this.g > 0 && this.a == 0.0F) {
            double d1 = (double) i + 0.5D;

            d0 = (double) k + 0.5D;
            this.world.a((EntityHuman) null, d1, (double) j + 0.5D, d0, SoundEffects.aT, SoundCategory.BLOCKS, 0.5F, this.world.random.nextFloat() * 0.1F + 0.9F);
        }

        if (this.g == 0 && this.a > 0.0F || this.g > 0 && this.a < 1.0F) {
            float f1 = this.a;

            if (this.g > 0) {
                this.a += 0.1F;
            } else {
                this.a -= 0.1F;
            }

            if (this.a > 1.0F) {
                this.a = 1.0F;
            }

            float f2 = 0.5F;

            if (this.a < 0.5F && f1 >= 0.5F) {
                d0 = (double) i + 0.5D;
                double d2 = (double) k + 0.5D;

                this.world.a((EntityHuman) null, d0, (double) j + 0.5D, d2, SoundEffects.aS, SoundCategory.BLOCKS, 0.5F, this.world.random.nextFloat() * 0.1F + 0.9F);
            }

            if (this.a < 0.0F) {
                this.a = 0.0F;
            }
        }
        */
        // Paper end

    }

    public boolean func_145842_c(int i, int j) {
        if (i == 1) {
            this.field_145973_j = j;
            return true;
        } else {
            return super.func_145842_c(i, j);
        }
    }

    public void func_145843_s() {
        this.func_145836_u();
        super.func_145843_s();
    }

    public void func_145969_a() {
        ++this.field_145973_j;

        // Paper start - Move enderchest open sounds out of the tick loop
        if (this.field_145973_j > 0 && this.field_145972_a == 0.0F) {
            this.field_145972_a = 0.7F;

            double d1 = (double) this.func_174877_v().func_177958_n() + 0.5D;
            double d0 = (double) this.func_174877_v().func_177952_p() + 0.5D;

            this.field_145850_b.func_184148_a((EntityPlayer) null, d1, (double) this.func_174877_v().func_177956_o() + 0.5D, d0, SoundEvents.field_187520_aJ, SoundCategory.BLOCKS, 0.5F, this.field_145850_b.field_73012_v.nextFloat() * 0.1F + 0.9F);
        }
        // Paper end

        this.field_145850_b.func_175641_c(this.field_174879_c, Blocks.field_150477_bB, 1, this.field_145973_j);
    }

    public void func_145970_b() {
        --this.field_145973_j;

        // Paper start - Move enderchest close sounds out of the tick loop
        if (this.field_145973_j == 0 && this.field_145972_a > 0.0F || this.field_145973_j > 0 && this.field_145972_a < 1.0F) {
            double d0 = (double) this.func_174877_v().func_177958_n() + 0.5D;
            double d2 = (double) this.func_174877_v().func_177952_p() + 0.5D;

            this.field_145850_b.func_184148_a((EntityPlayer) null, d0, (double) this.func_174877_v().func_177956_o() + 0.5D, d2, SoundEvents.field_187519_aI, SoundCategory.BLOCKS, 0.5F, this.field_145850_b.field_73012_v.nextFloat() * 0.1F + 0.9F);
            this.field_145972_a = 0.0F;
        }
        // Paper end

        this.field_145850_b.func_175641_c(this.field_174879_c, Blocks.field_150477_bB, 1, this.field_145973_j);
    }

    public boolean func_145971_a(EntityPlayer entityhuman) {
        return this.field_145850_b.func_175625_s(this.field_174879_c) != this ? false : entityhuman.func_70092_e((double) this.field_174879_c.func_177958_n() + 0.5D, (double) this.field_174879_c.func_177956_o() + 0.5D, (double) this.field_174879_c.func_177952_p() + 0.5D) <= 64.0D;
    }
}
