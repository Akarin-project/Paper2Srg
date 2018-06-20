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

    @Override
    public boolean func_70648_aU() {
        return true;
    }

    @Override
    public boolean func_70601_bi() {
        // Paper start - Don't let water mobs spawn in non-water blocks
        // Based around EntityAnimal's implementation
        int i = MathHelper.func_76128_c(this.field_70165_t);
        int j = MathHelper.func_76128_c(this.func_174813_aQ().field_72338_b); // minY of bounding box
        int k = MathHelper.func_76128_c(this.field_70161_v);
        Block block = this.field_70170_p.func_180495_p(new BlockPos(i, j, k)).func_177230_c();

        return block == Blocks.field_150355_j || block == Blocks.field_150358_i;
        // Paper end
    }

    @Override
    public boolean func_70058_J() {
        return this.field_70170_p.func_72917_a(this.func_174813_aQ(), this);
    }

    @Override
    public int func_70627_aG() {
        return 120;
    }

    @Override
    public boolean func_70692_ba() {
        return true;
    }

    @Override
    protected int func_70693_a(EntityPlayer entityhuman) {
        return 1 + this.field_70170_p.field_73012_v.nextInt(3);
    }

    @Override
    public void func_70030_z() {
        int i = this.func_70086_ai();

        super.func_70030_z();
        if (this.func_70089_S() && !this.func_70090_H()) {
            --i;
            this.func_70050_g(i);
            if (this.func_70086_ai() == -20) {
                this.func_70050_g(0);
                this.func_70097_a(DamageSource.field_76369_e, 2.0F);
            }
        } else {
            this.func_70050_g(300);
        }

    }

    @Override
    public boolean func_96092_aw() {
        return false;
    }
}
