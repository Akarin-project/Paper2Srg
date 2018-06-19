package net.minecraft.entity.projectile;

import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.world.World;

public class EntityTippedArrow extends EntityArrow {

    private static final DataParameter<Integer> field_184559_f = EntityDataManager.func_187226_a(EntityTippedArrow.class, DataSerializers.field_187192_b);
    private PotionType field_184560_g;
    public final Set<PotionEffect> field_184561_h;
    private boolean field_191509_at;

    public EntityTippedArrow(World world) {
        super(world);
        this.field_184560_g = PotionTypes.field_185229_a;
        this.field_184561_h = Sets.newHashSet();
    }

    public EntityTippedArrow(World world, double d0, double d1, double d2) {
        super(world, d0, d1, d2);
        this.field_184560_g = PotionTypes.field_185229_a;
        this.field_184561_h = Sets.newHashSet();
    }

    public EntityTippedArrow(World world, EntityLivingBase entityliving) {
        super(world, entityliving);
        this.field_184560_g = PotionTypes.field_185229_a;
        this.field_184561_h = Sets.newHashSet();
    }

    public void func_184555_a(ItemStack itemstack) {
        if (itemstack.func_77973_b() == Items.field_185167_i) {
            this.field_184560_g = PotionUtils.func_185191_c(itemstack);
            List list = PotionUtils.func_185190_b(itemstack);

            if (!list.isEmpty()) {
                Iterator iterator = list.iterator();

                while (iterator.hasNext()) {
                    PotionEffect mobeffect = (PotionEffect) iterator.next();

                    this.field_184561_h.add(new PotionEffect(mobeffect));
                }
            }

            int i = func_191508_b(itemstack);

            if (i == -1) {
                this.func_190548_o();
            } else {
                this.func_191507_d(i);
            }
        } else if (itemstack.func_77973_b() == Items.field_151032_g) {
            this.field_184560_g = PotionTypes.field_185229_a;
            this.field_184561_h.clear();
            this.field_70180_af.func_187227_b(EntityTippedArrow.field_184559_f, Integer.valueOf(-1));
        }

    }

    public static int func_191508_b(ItemStack itemstack) {
        NBTTagCompound nbttagcompound = itemstack.func_77978_p();

        return nbttagcompound != null && nbttagcompound.func_150297_b("CustomPotionColor", 99) ? nbttagcompound.func_74762_e("CustomPotionColor") : -1;
    }

    private void func_190548_o() {
        this.field_191509_at = false;
        this.field_70180_af.func_187227_b(EntityTippedArrow.field_184559_f, Integer.valueOf(PotionUtils.func_185181_a((Collection) PotionUtils.func_185186_a(this.field_184560_g, (Collection) this.field_184561_h))));
    }

    public void func_184558_a(PotionEffect mobeffect) {
        this.field_184561_h.add(mobeffect);
        this.func_184212_Q().func_187227_b(EntityTippedArrow.field_184559_f, Integer.valueOf(PotionUtils.func_185181_a((Collection) PotionUtils.func_185186_a(this.field_184560_g, (Collection) this.field_184561_h))));
    }

    protected void func_70088_a() {
        super.func_70088_a();
        this.field_70180_af.func_187214_a(EntityTippedArrow.field_184559_f, Integer.valueOf(-1));
    }

    public void func_70071_h_() {
        super.func_70071_h_();
        if (this.field_70170_p.field_72995_K) {
            if (this.field_70254_i) {
                if (this.field_184552_b % 5 == 0) {
                    this.func_184556_b(1);
                }
            } else {
                this.func_184556_b(2);
            }
        } else if (this.field_70254_i && this.field_184552_b != 0 && !this.field_184561_h.isEmpty() && this.field_184552_b >= 600) {
            this.field_70170_p.func_72960_a(this, (byte) 0);
            this.field_184560_g = PotionTypes.field_185229_a;
            this.field_184561_h.clear();
            this.field_70180_af.func_187227_b(EntityTippedArrow.field_184559_f, Integer.valueOf(-1));
        }

    }

    private void func_184556_b(int i) {
        int j = this.func_184557_n();

        if (j != -1 && i > 0) {
            double d0 = (double) (j >> 16 & 255) / 255.0D;
            double d1 = (double) (j >> 8 & 255) / 255.0D;
            double d2 = (double) (j >> 0 & 255) / 255.0D;

            for (int k = 0; k < i; ++k) {
                this.field_70170_p.func_175688_a(EnumParticleTypes.SPELL_MOB, this.field_70165_t + (this.field_70146_Z.nextDouble() - 0.5D) * (double) this.field_70130_N, this.field_70163_u + this.field_70146_Z.nextDouble() * (double) this.field_70131_O, this.field_70161_v + (this.field_70146_Z.nextDouble() - 0.5D) * (double) this.field_70130_N, d0, d1, d2, new int[0]);
            }

        }
    }

    // CraftBukkit start accessor methods
    public void refreshEffects() {
        this.func_184212_Q().func_187227_b(EntityTippedArrow.field_184559_f, Integer.valueOf(PotionUtils.func_185181_a((Collection) PotionUtils.func_185186_a(this.field_184560_g, (Collection) this.field_184561_h))));
    }

    public String getType() {
        return ((ResourceLocation) PotionType.field_185176_a.func_177774_c(this.field_184560_g)).toString();
    }

    public void setType(String string) {
        this.field_184560_g = PotionType.field_185176_a.func_82594_a(new ResourceLocation(string));
        this.field_70180_af.func_187227_b(EntityTippedArrow.field_184559_f, Integer.valueOf(PotionUtils.func_185181_a((Collection) PotionUtils.func_185186_a(this.field_184560_g, (Collection) this.field_184561_h))));
    }

    public boolean isTipped() {
        return !(this.field_184561_h.isEmpty() && this.field_184560_g == PotionTypes.field_185229_a);
    }
    // CraftBukkit end

    public int func_184557_n() {
        return ((Integer) this.field_70180_af.func_187225_a(EntityTippedArrow.field_184559_f)).intValue();
    }

    public void func_191507_d(int i) {
        this.field_191509_at = true;
        this.field_70180_af.func_187227_b(EntityTippedArrow.field_184559_f, Integer.valueOf(i));
    }

    public static void func_189660_b(DataFixer dataconvertermanager) {
        EntityArrow.func_189657_a(dataconvertermanager, "TippedArrow");
    }

    public void func_70014_b(NBTTagCompound nbttagcompound) {
        super.func_70014_b(nbttagcompound);
        if (this.field_184560_g != PotionTypes.field_185229_a && this.field_184560_g != null) {
            nbttagcompound.func_74778_a("Potion", ((ResourceLocation) PotionType.field_185176_a.func_177774_c(this.field_184560_g)).toString());
        }

        if (this.field_191509_at) {
            nbttagcompound.func_74768_a("Color", this.func_184557_n());
        }

        if (!this.field_184561_h.isEmpty()) {
            NBTTagList nbttaglist = new NBTTagList();
            Iterator iterator = this.field_184561_h.iterator();

            while (iterator.hasNext()) {
                PotionEffect mobeffect = (PotionEffect) iterator.next();

                nbttaglist.func_74742_a(mobeffect.func_82719_a(new NBTTagCompound()));
            }

            nbttagcompound.func_74782_a("CustomPotionEffects", nbttaglist);
        }

    }

    public void func_70037_a(NBTTagCompound nbttagcompound) {
        super.func_70037_a(nbttagcompound);
        if (nbttagcompound.func_150297_b("Potion", 8)) {
            this.field_184560_g = PotionUtils.func_185187_c(nbttagcompound);
        }

        Iterator iterator = PotionUtils.func_185192_b(nbttagcompound).iterator();

        while (iterator.hasNext()) {
            PotionEffect mobeffect = (PotionEffect) iterator.next();

            this.func_184558_a(mobeffect);
        }

        if (nbttagcompound.func_150297_b("Color", 99)) {
            this.func_191507_d(nbttagcompound.func_74762_e("Color"));
        } else {
            this.func_190548_o();
        }

    }

    protected void func_184548_a(EntityLivingBase entityliving) {
        super.func_184548_a(entityliving);
        Iterator iterator = this.field_184560_g.func_185170_a().iterator();

        PotionEffect mobeffect;

        while (iterator.hasNext()) {
            mobeffect = (PotionEffect) iterator.next();
            entityliving.func_70690_d(new PotionEffect(mobeffect.func_188419_a(), Math.max(mobeffect.func_76459_b() / 8, 1), mobeffect.func_76458_c(), mobeffect.func_82720_e(), mobeffect.func_188418_e()));
        }

        if (!this.field_184561_h.isEmpty()) {
            iterator = this.field_184561_h.iterator();

            while (iterator.hasNext()) {
                mobeffect = (PotionEffect) iterator.next();
                entityliving.func_70690_d(mobeffect);
            }
        }

    }

    protected ItemStack func_184550_j() {
        if (this.field_184561_h.isEmpty() && this.field_184560_g == PotionTypes.field_185229_a) {
            return new ItemStack(Items.field_151032_g);
        } else {
            ItemStack itemstack = new ItemStack(Items.field_185167_i);

            PotionUtils.func_185188_a(itemstack, this.field_184560_g);
            PotionUtils.func_185184_a(itemstack, (Collection) this.field_184561_h);
            if (this.field_191509_at) {
                NBTTagCompound nbttagcompound = itemstack.func_77978_p();

                if (nbttagcompound == null) {
                    nbttagcompound = new NBTTagCompound();
                    itemstack.func_77982_d(nbttagcompound);
                }

                nbttagcompound.func_74768_a("CustomPotionColor", this.func_184557_n());
            }

            return itemstack;
        }
    }
}
