package net.minecraft.entity;

import org.bukkit.craftbukkit.event.CraftEventFactory;

import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;

import net.minecraft.block.BlockFence;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketEntityAttach;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class EntityLeashKnot extends EntityHanging {

    public EntityLeashKnot(World world) {
        super(world);
    }

    public EntityLeashKnot(World world, BlockPos blockposition) {
        super(world, blockposition);
        this.func_70107_b((double) blockposition.func_177958_n() + 0.5D, (double) blockposition.func_177956_o() + 0.5D, (double) blockposition.func_177952_p() + 0.5D);
        float f = 0.125F;
        float f1 = 0.1875F;
        float f2 = 0.25F;

        this.func_174826_a(new AxisAlignedBB(this.field_70165_t - 0.1875D, this.field_70163_u - 0.25D + 0.125D, this.field_70161_v - 0.1875D, this.field_70165_t + 0.1875D, this.field_70163_u + 0.25D + 0.125D, this.field_70161_v + 0.1875D));
        this.field_98038_p = true;
    }

    public void func_70107_b(double d0, double d1, double d2) {
        super.func_70107_b((double) MathHelper.func_76128_c(d0) + 0.5D, (double) MathHelper.func_76128_c(d1) + 0.5D, (double) MathHelper.func_76128_c(d2) + 0.5D);
    }

    protected void func_174856_o() {
        this.field_70165_t = (double) this.field_174861_a.func_177958_n() + 0.5D;
        this.field_70163_u = (double) this.field_174861_a.func_177956_o() + 0.5D;
        this.field_70161_v = (double) this.field_174861_a.func_177952_p() + 0.5D;
    }

    public void func_174859_a(EnumFacing enumdirection) {}

    public int func_82329_d() {
        return 9;
    }

    public int func_82330_g() {
        return 9;
    }

    public float func_70047_e() {
        return -0.0625F;
    }

    public void func_110128_b(@Nullable Entity entity) {
        this.func_184185_a(SoundEvents.field_187746_da, 1.0F, 1.0F);
    }

    public boolean func_70039_c(NBTTagCompound nbttagcompound) {
        return false;
    }

    public void func_70014_b(NBTTagCompound nbttagcompound) {}

    public void func_70037_a(NBTTagCompound nbttagcompound) {}

    public boolean func_184230_a(EntityPlayer entityhuman, EnumHand enumhand) {
        if (this.field_70170_p.field_72995_K) {
            return true;
        } else {
            boolean flag = false;
            double d0 = 7.0D;
            List list = this.field_70170_p.func_72872_a(EntityLiving.class, new AxisAlignedBB(this.field_70165_t - 7.0D, this.field_70163_u - 7.0D, this.field_70161_v - 7.0D, this.field_70165_t + 7.0D, this.field_70163_u + 7.0D, this.field_70161_v + 7.0D));
            Iterator iterator = list.iterator();

            EntityLiving entityinsentient;

            while (iterator.hasNext()) {
                entityinsentient = (EntityLiving) iterator.next();
                if (entityinsentient.func_110167_bD() && entityinsentient.func_110166_bE() == entityhuman) {
                    // CraftBukkit start
                    if (CraftEventFactory.callPlayerLeashEntityEvent(entityinsentient, this, entityhuman).isCancelled()) {
                        ((EntityPlayerMP) entityhuman).field_71135_a.func_147359_a(new SPacketEntityAttach(entityinsentient, entityinsentient.func_110166_bE()));
                        continue;
                    }
                    // CraftBukkit end
                    entityinsentient.func_110162_b(this, true);
                    flag = true;
                }
            }

            if (!flag) {
                // CraftBukkit start - Move below
                // this.die();
                boolean die = true;
                // CraftBukkit end
                if (true || entityhuman.field_71075_bZ.field_75098_d) { // CraftBukkit - Process for non-creative as well
                    iterator = list.iterator();

                    while (iterator.hasNext()) {
                        entityinsentient = (EntityLiving) iterator.next();
                        if (entityinsentient.func_110167_bD() && entityinsentient.func_110166_bE() == this) {
                            // CraftBukkit start
                            if (CraftEventFactory.callPlayerUnleashEntityEvent(entityinsentient, entityhuman).isCancelled()) {
                                die = false;
                                continue;
                            }
                            entityinsentient.func_110160_i(true, !entityhuman.field_71075_bZ.field_75098_d); // false -> survival mode boolean
                            // CraftBukkit end
                        }
                    }
                    // CraftBukkit start
                    if (die) {
                        this.func_70106_y();
                    }
                    // CraftBukkit end
                }
            }

            return true;
        }
    }

    public boolean func_70518_d() {
        return this.field_70170_p.func_180495_p(this.field_174861_a).func_177230_c() instanceof BlockFence;
    }

    public static EntityLeashKnot func_174862_a(World world, BlockPos blockposition) {
        EntityLeashKnot entityleash = new EntityLeashKnot(world, blockposition);

        world.func_72838_d(entityleash);
        entityleash.func_184523_o();
        return entityleash;
    }

    @Nullable
    public static EntityLeashKnot func_174863_b(World world, BlockPos blockposition) {
        int i = blockposition.func_177958_n();
        int j = blockposition.func_177956_o();
        int k = blockposition.func_177952_p();
        List list = world.func_72872_a(EntityLeashKnot.class, new AxisAlignedBB((double) i - 1.0D, (double) j - 1.0D, (double) k - 1.0D, (double) i + 1.0D, (double) j + 1.0D, (double) k + 1.0D));
        Iterator iterator = list.iterator();

        EntityLeashKnot entityleash;

        do {
            if (!iterator.hasNext()) {
                return null;
            }

            entityleash = (EntityLeashKnot) iterator.next();
        } while (!entityleash.func_174857_n().equals(blockposition));

        return entityleash;
    }

    public void func_184523_o() {
        this.func_184185_a(SoundEvents.field_187748_db, 1.0F, 1.0F);
    }
}
