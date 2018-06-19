package net.minecraft.entity.item;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.PotionTypes;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;


public class EntityExpBottle extends EntityThrowable {

    public EntityExpBottle(World world) {
        super(world);
    }

    public EntityExpBottle(World world, EntityLivingBase entityliving) {
        super(world, entityliving);
    }

    public EntityExpBottle(World world, double d0, double d1, double d2) {
        super(world, d0, d1, d2);
    }

    public static void func_189666_a(DataFixer dataconvertermanager) {
        EntityThrowable.func_189661_a(dataconvertermanager, "ThrowableExpBottle");
    }

    protected float func_70185_h() {
        return 0.07F;
    }

    protected void func_70184_a(RayTraceResult movingobjectposition) {
        if (!this.field_70170_p.field_72995_K) {
            // CraftBukkit - moved to after event
            // this.world.triggerEffect(2002, new BlockPosition(this), PotionUtil.a(Potions.b));
            int i = 3 + this.field_70170_p.field_73012_v.nextInt(5) + this.field_70170_p.field_73012_v.nextInt(5);

            // CraftBukkit start
            org.bukkit.event.entity.ExpBottleEvent event = org.bukkit.craftbukkit.event.CraftEventFactory.callExpBottleEvent(this, i);
            i = event.getExperience();
            if (event.getShowEffect()) {
                this.field_70170_p.func_175718_b(2002, new BlockPos(this), PotionUtils.func_185183_a(PotionTypes.field_185230_b));
            }
            // CraftBukkit end

            while (i > 0) {
                int j = EntityXPOrb.func_70527_a(i);

                i -= j;
                this.field_70170_p.func_72838_d(new EntityXPOrb(this.field_70170_p, this.field_70165_t, this.field_70163_u, this.field_70161_v, j, org.bukkit.entity.ExperienceOrb.SpawnReason.EXP_BOTTLE, func_85052_h(), this)); // Paper
            }

            this.func_70106_y();
        }

    }
}
