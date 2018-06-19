package net.minecraft.entity.projectile;

import com.google.common.base.Predicate;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.entity.EntityAreaEffectCloud;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.IDataWalker;
import net.minecraft.util.datafix.walkers.ItemStackData;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import org.bukkit.craftbukkit.entity.CraftLivingEntity;
import org.bukkit.entity.LivingEntity;

// CraftBukkit start
import java.util.HashMap;
import java.util.Map;
import org.bukkit.craftbukkit.entity.CraftLivingEntity;
import org.bukkit.entity.LivingEntity;
// CraftBukkit end

public class EntityPotion extends EntityThrowable {

    private static final DataParameter<ItemStack> field_184545_d = EntityDataManager.func_187226_a(EntityPotion.class, DataSerializers.field_187196_f);
    private static final Logger field_184546_e = LogManager.getLogger();
    public static final Predicate<EntityLivingBase> field_190546_d = new Predicate() {
        public boolean a(@Nullable EntityLivingBase entityliving) {
            return EntityPotion.func_190544_c(entityliving);
        }

        public boolean apply(@Nullable Object object) {
            return this.a((EntityLivingBase) object);
        }
    };

    public EntityPotion(World world) {
        super(world);
    }

    public EntityPotion(World world, EntityLivingBase entityliving, ItemStack itemstack) {
        super(world, entityliving);
        this.func_184541_a(itemstack);
    }

    public EntityPotion(World world, double d0, double d1, double d2, ItemStack itemstack) {
        super(world, d0, d1, d2);
        if (!itemstack.func_190926_b()) {
            this.func_184541_a(itemstack);
        }

    }

    protected void func_70088_a() {
        this.func_184212_Q().func_187214_a(EntityPotion.field_184545_d, ItemStack.field_190927_a);
    }

    public ItemStack func_184543_l() {
        ItemStack itemstack = (ItemStack) this.func_184212_Q().func_187225_a(EntityPotion.field_184545_d);

        if (itemstack.func_77973_b() != Items.field_185155_bH && itemstack.func_77973_b() != Items.field_185156_bI) {
            if (this.field_70170_p != null) {
                EntityPotion.field_184546_e.error("ThrownPotion entity {} has no item?!", Integer.valueOf(this.func_145782_y()));
            }

            return new ItemStack(Items.field_185155_bH);
        } else {
            return itemstack;
        }
    }

    public void func_184541_a(ItemStack itemstack) {
        this.func_184212_Q().func_187227_b(EntityPotion.field_184545_d, itemstack);
        this.func_184212_Q().func_187217_b(EntityPotion.field_184545_d);
    }

    protected float func_70185_h() {
        return 0.05F;
    }

    protected void func_70184_a(RayTraceResult movingobjectposition) {
        if (!this.field_70170_p.field_72995_K) {
            ItemStack itemstack = this.func_184543_l();
            PotionType potionregistry = PotionUtils.func_185191_c(itemstack);
            List list = PotionUtils.func_185189_a(itemstack);
            boolean flag = potionregistry == PotionTypes.field_185230_b && list.isEmpty();

            if (movingobjectposition.field_72313_a == RayTraceResult.Type.BLOCK && flag) {
                BlockPos blockposition = movingobjectposition.func_178782_a().func_177972_a(movingobjectposition.field_178784_b);

                this.func_184542_a(blockposition, movingobjectposition.field_178784_b);
                Iterator iterator = EnumFacing.Plane.HORIZONTAL.iterator();

                while (iterator.hasNext()) {
                    EnumFacing enumdirection = (EnumFacing) iterator.next();

                    this.func_184542_a(blockposition.func_177972_a(enumdirection), enumdirection);
                }
            }

            if (flag) {
                this.func_190545_n();
            } else if (true || !list.isEmpty()) { // CraftBukkit - Call event even if no effects to apply
                if (this.func_184544_n()) {
                    this.func_190542_a(itemstack, potionregistry);
                } else {
                    this.func_190543_a(movingobjectposition, list);
                }
            }

            int i = potionregistry.func_185172_c() ? 2007 : 2002;

            this.field_70170_p.func_175718_b(i, new BlockPos(this), PotionUtils.func_190932_c(itemstack));
            this.func_70106_y();
        }
    }

    private void func_190545_n() {
        AxisAlignedBB axisalignedbb = this.func_174813_aQ().func_72314_b(4.0D, 2.0D, 4.0D);
        List list = this.field_70170_p.func_175647_a(EntityLivingBase.class, axisalignedbb, EntityPotion.field_190546_d);

        if (!list.isEmpty()) {
            Iterator iterator = list.iterator();

            while (iterator.hasNext()) {
                EntityLivingBase entityliving = (EntityLivingBase) iterator.next();
                double d0 = this.func_70068_e(entityliving);

                if (d0 < 16.0D && func_190544_c(entityliving)) {
                    entityliving.func_70097_a(DamageSource.field_76369_e, 1.0F);
                }
            }
        }

    }

    private void func_190543_a(RayTraceResult movingobjectposition, List<PotionEffect> list) {
        AxisAlignedBB axisalignedbb = this.func_174813_aQ().func_72314_b(4.0D, 2.0D, 4.0D);
        List list1 = this.field_70170_p.func_72872_a(EntityLivingBase.class, axisalignedbb);
        Map<LivingEntity, Double> affected = new HashMap<LivingEntity, Double>(); // CraftBukkit

        if (!list1.isEmpty()) {
            Iterator iterator = list1.iterator();

            while (iterator.hasNext()) {
                EntityLivingBase entityliving = (EntityLivingBase) iterator.next();

                if (entityliving.func_184603_cC()) {
                    double d0 = this.func_70068_e(entityliving);

                    if (d0 < 16.0D) {
                        double d1 = 1.0D - Math.sqrt(d0) / 4.0D;

                        if (entityliving == movingobjectposition.field_72308_g) {
                            d1 = 1.0D;
                        }

                        // CraftBukkit start
                        affected.put((LivingEntity) entityliving.getBukkitEntity(), d1);
                    }
                }
            }
        }

        org.bukkit.event.entity.PotionSplashEvent event = org.bukkit.craftbukkit.event.CraftEventFactory.callPotionSplashEvent(this, affected);
        if (!event.isCancelled() && list != null && !list.isEmpty()) { // do not process effects if there are no effects to process
            for (LivingEntity victim : event.getAffectedEntities()) {
                if (!(victim instanceof CraftLivingEntity)) {
                    continue;
                }

                EntityLivingBase entityliving = ((CraftLivingEntity) victim).getHandle();
                double d1 = event.getIntensity(victim);
                // CraftBukkit end

                Iterator iterator1 = list.iterator();

                while (iterator1.hasNext()) {
                    PotionEffect mobeffect = (PotionEffect) iterator1.next();
                    Potion mobeffectlist = mobeffect.func_188419_a();
                    // CraftBukkit start - Abide by PVP settings - for players only!
                    if (!this.field_70170_p.pvpMode && this.func_85052_h() instanceof EntityPlayerMP && entityliving instanceof EntityPlayerMP && entityliving != this.func_85052_h()) {
                        int i = Potion.func_188409_a(mobeffectlist);
                        // Block SLOWER_MOVEMENT, SLOWER_DIG, HARM, BLINDNESS, HUNGER, WEAKNESS and POISON potions
                        if (i == 2 || i == 4 || i == 7 || i == 15 || i == 17 || i == 18 || i == 19) {
                            continue;
                        }
                    }
                    // CraftBukkit end

                    if (mobeffectlist.func_76403_b()) {
                        mobeffectlist.func_180793_a(this, this.func_85052_h(), entityliving, mobeffect.func_76458_c(), d1);
                    } else {
                        int i = (int) (d1 * (double) mobeffect.func_76459_b() + 0.5D);

                        if (i > 20) {
                            entityliving.func_70690_d(new PotionEffect(mobeffectlist, i, mobeffect.func_76458_c(), mobeffect.func_82720_e(), mobeffect.func_188418_e()));
                        }
                    }
                }
            }
        }

    }

    private void func_190542_a(ItemStack itemstack, PotionType potionregistry) {
        EntityAreaEffectCloud entityareaeffectcloud = new EntityAreaEffectCloud(this.field_70170_p, this.field_70165_t, this.field_70163_u, this.field_70161_v);

        entityareaeffectcloud.func_184481_a(this.func_85052_h());
        entityareaeffectcloud.func_184483_a(3.0F);
        entityareaeffectcloud.func_184495_b(-0.5F);
        entityareaeffectcloud.func_184485_d(10);
        entityareaeffectcloud.func_184487_c(-entityareaeffectcloud.func_184490_j() / (float) entityareaeffectcloud.func_184489_o());
        entityareaeffectcloud.func_184484_a(potionregistry);
        Iterator iterator = PotionUtils.func_185190_b(itemstack).iterator();

        while (iterator.hasNext()) {
            PotionEffect mobeffect = (PotionEffect) iterator.next();

            entityareaeffectcloud.func_184496_a(new PotionEffect(mobeffect));
        }

        NBTTagCompound nbttagcompound = itemstack.func_77978_p();

        if (nbttagcompound != null && nbttagcompound.func_150297_b("CustomPotionColor", 99)) {
            entityareaeffectcloud.func_184482_a(nbttagcompound.func_74762_e("CustomPotionColor"));
        }

        // CraftBukkit start
        org.bukkit.event.entity.LingeringPotionSplashEvent event = org.bukkit.craftbukkit.event.CraftEventFactory.callLingeringPotionSplashEvent(this, entityareaeffectcloud);
        if (!(event.isCancelled() || entityareaeffectcloud.field_70128_L)) {
            this.field_70170_p.func_72838_d(entityareaeffectcloud);
        } else {
            entityareaeffectcloud.field_70128_L = true;
        }
        // CraftBukkit end
    }

    public boolean func_184544_n() {
        return this.func_184543_l().func_77973_b() == Items.field_185156_bI;
    }

    private void func_184542_a(BlockPos blockposition, EnumFacing enumdirection) {
        if (this.field_70170_p.func_180495_p(blockposition).func_177230_c() == Blocks.field_150480_ab) {
            this.field_70170_p.func_175719_a((EntityPlayer) null, blockposition.func_177972_a(enumdirection), enumdirection.func_176734_d());
        }

    }

    public static void func_189665_a(DataFixer dataconvertermanager) {
        EntityThrowable.func_189661_a(dataconvertermanager, "ThrownPotion");
        dataconvertermanager.func_188258_a(FixTypes.ENTITY, (IDataWalker) (new ItemStackData(EntityPotion.class, new String[] { "Potion"})));
    }

    public void func_70037_a(NBTTagCompound nbttagcompound) {
        super.func_70037_a(nbttagcompound);
        ItemStack itemstack = new ItemStack(nbttagcompound.func_74775_l("Potion"));

        if (itemstack.func_190926_b()) {
            this.func_70106_y();
        } else {
            this.func_184541_a(itemstack);
        }

    }

    public void func_70014_b(NBTTagCompound nbttagcompound) {
        super.func_70014_b(nbttagcompound);
        ItemStack itemstack = this.func_184543_l();

        if (!itemstack.func_190926_b()) {
            nbttagcompound.func_74782_a("Potion", itemstack.func_77955_b(new NBTTagCompound()));
        }

    }

    private static boolean func_190544_c(EntityLivingBase entityliving) {
        return entityliving instanceof EntityEnderman || entityliving instanceof EntityBlaze;
    }
}
