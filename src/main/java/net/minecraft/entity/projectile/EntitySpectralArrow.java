package net.minecraft.entity.projectile;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.world.World;


public class EntitySpectralArrow extends EntityArrow {

    public int field_184562_f = 200;

    public EntitySpectralArrow(World world) {
        super(world);
    }

    public EntitySpectralArrow(World world, EntityLivingBase entityliving) {
        super(world, entityliving);
    }

    public EntitySpectralArrow(World world, double d0, double d1, double d2) {
        super(world, d0, d1, d2);
    }

    public void func_70071_h_() {
        super.func_70071_h_();
        if (this.field_70170_p.field_72995_K && !this.field_70254_i) {
            this.field_70170_p.func_175688_a(EnumParticleTypes.SPELL_INSTANT, this.field_70165_t, this.field_70163_u, this.field_70161_v, 0.0D, 0.0D, 0.0D, new int[0]);
        }

    }

    protected ItemStack func_184550_j() {
        return new ItemStack(Items.field_185166_h);
    }

    protected void func_184548_a(EntityLivingBase entityliving) {
        super.func_184548_a(entityliving);
        PotionEffect mobeffect = new PotionEffect(MobEffects.field_188423_x, this.field_184562_f, 0);

        entityliving.func_70690_d(mobeffect);
    }

    public static void func_189659_b(DataFixer dataconvertermanager) {
        EntityArrow.func_189657_a(dataconvertermanager, "SpectralArrow");
    }

    public void func_70037_a(NBTTagCompound nbttagcompound) {
        super.func_70037_a(nbttagcompound);
        if (nbttagcompound.func_74764_b("Duration")) {
            this.field_184562_f = nbttagcompound.func_74762_e("Duration");
        }

    }

    public void func_70014_b(NBTTagCompound nbttagcompound) {
        super.func_70014_b(nbttagcompound);
        nbttagcompound.func_74768_a("Duration", this.field_184562_f);
    }
}
