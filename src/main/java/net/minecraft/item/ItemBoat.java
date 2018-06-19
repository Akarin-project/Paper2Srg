package net.minecraft.item;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ItemBoat extends Item {

    private final EntityBoat.Type field_185057_a;

    public ItemBoat(EntityBoat.Type entityboat_enumboattype) {
        this.field_185057_a = entityboat_enumboattype;
        this.field_77777_bU = 1;
        this.func_77637_a(CreativeTabs.field_78029_e);
        this.func_77655_b("boat." + entityboat_enumboattype.func_184980_a());
    }

    public ActionResult<ItemStack> func_77659_a(World world, EntityPlayer entityhuman, EnumHand enumhand) {
        ItemStack itemstack = entityhuman.func_184586_b(enumhand);
        float f = 1.0F;
        float f1 = entityhuman.field_70127_C + (entityhuman.field_70125_A - entityhuman.field_70127_C) * 1.0F;
        float f2 = entityhuman.field_70126_B + (entityhuman.field_70177_z - entityhuman.field_70126_B) * 1.0F;
        double d0 = entityhuman.field_70169_q + (entityhuman.field_70165_t - entityhuman.field_70169_q) * 1.0D;
        double d1 = entityhuman.field_70167_r + (entityhuman.field_70163_u - entityhuman.field_70167_r) * 1.0D + (double) entityhuman.func_70047_e();
        double d2 = entityhuman.field_70166_s + (entityhuman.field_70161_v - entityhuman.field_70166_s) * 1.0D;
        Vec3d vec3d = new Vec3d(d0, d1, d2);
        float f3 = MathHelper.func_76134_b(-f2 * 0.017453292F - 3.1415927F);
        float f4 = MathHelper.func_76126_a(-f2 * 0.017453292F - 3.1415927F);
        float f5 = -MathHelper.func_76134_b(-f1 * 0.017453292F);
        float f6 = MathHelper.func_76126_a(-f1 * 0.017453292F);
        float f7 = f4 * f5;
        float f8 = f3 * f5;
        double d3 = 5.0D;
        Vec3d vec3d1 = vec3d.func_72441_c((double) f7 * 5.0D, (double) f6 * 5.0D, (double) f8 * 5.0D);
        RayTraceResult movingobjectposition = world.func_72901_a(vec3d, vec3d1, true);

        if (movingobjectposition == null) {
            return new ActionResult(EnumActionResult.PASS, itemstack);
        } else {
            Vec3d vec3d2 = entityhuman.func_70676_i(1.0F);
            boolean flag = false;
            List list = world.func_72839_b(entityhuman, entityhuman.func_174813_aQ().func_72321_a(vec3d2.field_72450_a * 5.0D, vec3d2.field_72448_b * 5.0D, vec3d2.field_72449_c * 5.0D).func_186662_g(1.0D));

            for (int i = 0; i < list.size(); ++i) {
                Entity entity = (Entity) list.get(i);

                if (entity.func_70067_L()) {
                    AxisAlignedBB axisalignedbb = entity.func_174813_aQ().func_186662_g((double) entity.func_70111_Y());

                    if (axisalignedbb.func_72318_a(vec3d)) {
                        flag = true;
                    }
                }
            }

            if (flag) {
                return new ActionResult(EnumActionResult.PASS, itemstack);
            } else if (movingobjectposition.field_72313_a != RayTraceResult.Type.BLOCK) {
                return new ActionResult(EnumActionResult.PASS, itemstack);
            } else {
                // CraftBukkit start - Boat placement
                org.bukkit.event.player.PlayerInteractEvent event = org.bukkit.craftbukkit.event.CraftEventFactory.callPlayerInteractEvent(entityhuman, org.bukkit.event.block.Action.RIGHT_CLICK_BLOCK, movingobjectposition.func_178782_a(), movingobjectposition.field_178784_b, itemstack, enumhand);

                if (event.isCancelled()) {
                    return new ActionResult(EnumActionResult.PASS, itemstack);
                }
                // CraftBukkit end
                Block block = world.func_180495_p(movingobjectposition.func_178782_a()).func_177230_c();
                boolean flag1 = block == Blocks.field_150355_j || block == Blocks.field_150358_i;
                EntityBoat entityboat = new EntityBoat(world, movingobjectposition.field_72307_f.field_72450_a, flag1 ? movingobjectposition.field_72307_f.field_72448_b - 0.12D : movingobjectposition.field_72307_f.field_72448_b, movingobjectposition.field_72307_f.field_72449_c);

                entityboat.func_184458_a(this.field_185057_a);
                entityboat.field_70177_z = entityhuman.field_70177_z;
                if (!world.func_184144_a(entityboat, entityboat.func_174813_aQ().func_186662_g(-0.1D)).isEmpty()) {
                    return new ActionResult(EnumActionResult.FAIL, itemstack);
                } else {
                    if (!world.field_72995_K) {
                        if (!world.func_72838_d(entityboat)) return new ActionResult(EnumActionResult.PASS, itemstack); // CraftBukkit
                    }

                    if (!entityhuman.field_71075_bZ.field_75098_d) {
                        itemstack.func_190918_g(1);
                    }

                    entityhuman.func_71029_a(StatList.func_188057_b((Item) this));
                    return new ActionResult(EnumActionResult.SUCCESS, itemstack);
                }
            }
        }
    }
}
