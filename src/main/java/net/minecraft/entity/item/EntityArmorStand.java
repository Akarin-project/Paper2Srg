package net.minecraft.entity.item;

import com.google.common.base.Predicate;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.IDataWalker;
import net.minecraft.util.datafix.walkers.ItemStackDataLists;
import net.minecraft.util.math.Rotations;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import org.bukkit.craftbukkit.CraftEquipmentSlot;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.inventory.EquipmentSlot;

// CraftBukkit start
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.craftbukkit.CraftEquipmentSlot;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
// CraftBukkit end

public class EntityArmorStand extends EntityLivingBase {

    private static final Rotations field_175435_a = new Rotations(0.0F, 0.0F, 0.0F);
    private static final Rotations field_175433_b = new Rotations(0.0F, 0.0F, 0.0F);
    private static final Rotations field_175434_c = new Rotations(-10.0F, 0.0F, -10.0F);
    private static final Rotations field_175431_d = new Rotations(-15.0F, 0.0F, 10.0F);
    private static final Rotations field_175432_e = new Rotations(-1.0F, 0.0F, -1.0F);
    private static final Rotations field_175429_f = new Rotations(1.0F, 0.0F, 1.0F);
    public static final DataParameter<Byte> field_184801_a = EntityDataManager.func_187226_a(EntityArmorStand.class, DataSerializers.field_187191_a);
    public static final DataParameter<Rotations> field_184802_b = EntityDataManager.func_187226_a(EntityArmorStand.class, DataSerializers.field_187199_i);
    public static final DataParameter<Rotations> field_184803_c = EntityDataManager.func_187226_a(EntityArmorStand.class, DataSerializers.field_187199_i);
    public static final DataParameter<Rotations> field_184804_d = EntityDataManager.func_187226_a(EntityArmorStand.class, DataSerializers.field_187199_i);
    public static final DataParameter<Rotations> field_184805_e = EntityDataManager.func_187226_a(EntityArmorStand.class, DataSerializers.field_187199_i);
    public static final DataParameter<Rotations> field_184806_f = EntityDataManager.func_187226_a(EntityArmorStand.class, DataSerializers.field_187199_i);
    public static final DataParameter<Rotations> field_184807_g = EntityDataManager.func_187226_a(EntityArmorStand.class, DataSerializers.field_187199_i);
    private static final Predicate<Entity> field_184798_bv = new Predicate() {
        public boolean a(@Nullable Entity entity) {
            return entity instanceof EntityMinecart && ((EntityMinecart) entity).func_184264_v() == EntityMinecart.Type.RIDEABLE;
        }

        public boolean apply(@Nullable Object object) {
            return this.a((Entity) object);
        }
    };
    private final NonNullList<ItemStack> field_184799_bw;
    private final NonNullList<ItemStack> field_184800_bx;
    private boolean field_175436_h;
    public long field_175437_i;
    private int field_175442_bg;
    private boolean field_181028_bj;
    public Rotations field_175443_bh;
    public Rotations field_175444_bi;
    public Rotations field_175438_bj;
    public Rotations field_175439_bk;
    public Rotations field_175440_bl;
    public Rotations field_175441_bm;
    public boolean canMove = true; // Paper

    public EntityArmorStand(World world) {
        super(world);
        this.field_184799_bw = NonNullList.func_191197_a(2, ItemStack.field_190927_a);
        this.field_184800_bx = NonNullList.func_191197_a(4, ItemStack.field_190927_a);
        this.field_175443_bh = EntityArmorStand.field_175435_a;
        this.field_175444_bi = EntityArmorStand.field_175433_b;
        this.field_175438_bj = EntityArmorStand.field_175434_c;
        this.field_175439_bk = EntityArmorStand.field_175431_d;
        this.field_175440_bl = EntityArmorStand.field_175432_e;
        this.field_175441_bm = EntityArmorStand.field_175429_f;
        this.field_70145_X = this.func_189652_ae();
        this.func_70105_a(0.5F, 1.975F);
    }

    public EntityArmorStand(World world, double d0, double d1, double d2) {
        this(world);
        this.func_70107_b(d0, d1, d2);
    }

    // CraftBukkit start - SPIGOT-3607, SPIGOT-3637
    @Override
    public float getBukkitYaw() {
        return this.field_70177_z;
    }
    // CraftBukkit end

    public final void func_70105_a(float f, float f1) {
        double d0 = this.field_70165_t;
        double d1 = this.field_70163_u;
        double d2 = this.field_70161_v;
        float f2 = this.func_181026_s() ? 0.0F : (this.func_70631_g_() ? 0.5F : 1.0F);

        super.func_70105_a(f * f2, f1 * f2);
        this.func_70107_b(d0, d1, d2);
    }

    public boolean func_70613_aW() {
        return super.func_70613_aW() && !this.func_189652_ae();
    }

    protected void func_70088_a() {
        super.func_70088_a();
        this.field_70180_af.func_187214_a(EntityArmorStand.field_184801_a, Byte.valueOf((byte) 0));
        this.field_70180_af.func_187214_a(EntityArmorStand.field_184802_b, EntityArmorStand.field_175435_a);
        this.field_70180_af.func_187214_a(EntityArmorStand.field_184803_c, EntityArmorStand.field_175433_b);
        this.field_70180_af.func_187214_a(EntityArmorStand.field_184804_d, EntityArmorStand.field_175434_c);
        this.field_70180_af.func_187214_a(EntityArmorStand.field_184805_e, EntityArmorStand.field_175431_d);
        this.field_70180_af.func_187214_a(EntityArmorStand.field_184806_f, EntityArmorStand.field_175432_e);
        this.field_70180_af.func_187214_a(EntityArmorStand.field_184807_g, EntityArmorStand.field_175429_f);
    }

    public Iterable<ItemStack> func_184214_aD() {
        return this.field_184799_bw;
    }

    public Iterable<ItemStack> func_184193_aE() {
        return this.field_184800_bx;
    }

    public ItemStack func_184582_a(EntityEquipmentSlot enumitemslot) {
        switch (enumitemslot.func_188453_a()) {
        case HAND:
            return (ItemStack) this.field_184799_bw.get(enumitemslot.func_188454_b());

        case ARMOR:
            return (ItemStack) this.field_184800_bx.get(enumitemslot.func_188454_b());

        default:
            return ItemStack.field_190927_a;
        }
    }

    public void func_184201_a(EntityEquipmentSlot enumitemslot, ItemStack itemstack) {
        switch (enumitemslot.func_188453_a()) {
        case HAND:
            this.func_184606_a_(itemstack);
            this.field_184799_bw.set(enumitemslot.func_188454_b(), itemstack);
            break;

        case ARMOR:
            this.func_184606_a_(itemstack);
            this.field_184800_bx.set(enumitemslot.func_188454_b(), itemstack);
        }

    }

    public boolean func_174820_d(int i, ItemStack itemstack) {
        EntityEquipmentSlot enumitemslot;

        if (i == 98) {
            enumitemslot = EntityEquipmentSlot.MAINHAND;
        } else if (i == 99) {
            enumitemslot = EntityEquipmentSlot.OFFHAND;
        } else if (i == 100 + EntityEquipmentSlot.HEAD.func_188454_b()) {
            enumitemslot = EntityEquipmentSlot.HEAD;
        } else if (i == 100 + EntityEquipmentSlot.CHEST.func_188454_b()) {
            enumitemslot = EntityEquipmentSlot.CHEST;
        } else if (i == 100 + EntityEquipmentSlot.LEGS.func_188454_b()) {
            enumitemslot = EntityEquipmentSlot.LEGS;
        } else {
            if (i != 100 + EntityEquipmentSlot.FEET.func_188454_b()) {
                return false;
            }

            enumitemslot = EntityEquipmentSlot.FEET;
        }

        if (!itemstack.func_190926_b() && !EntityLiving.func_184648_b(enumitemslot, itemstack) && enumitemslot != EntityEquipmentSlot.HEAD) {
            return false;
        } else {
            this.func_184201_a(enumitemslot, itemstack);
            return true;
        }
    }

    public static void func_189805_a(DataFixer dataconvertermanager) {
        dataconvertermanager.func_188258_a(FixTypes.ENTITY, (IDataWalker) (new ItemStackDataLists(EntityArmorStand.class, new String[] { "ArmorItems", "HandItems"})));
    }

    public void func_70014_b(NBTTagCompound nbttagcompound) {
        super.func_70014_b(nbttagcompound);
        NBTTagList nbttaglist = new NBTTagList();

        NBTTagCompound nbttagcompound1;

        for (Iterator iterator = this.field_184800_bx.iterator(); iterator.hasNext(); nbttaglist.func_74742_a(nbttagcompound1)) {
            ItemStack itemstack = (ItemStack) iterator.next();

            nbttagcompound1 = new NBTTagCompound();
            if (!itemstack.func_190926_b()) {
                itemstack.func_77955_b(nbttagcompound1);
            }
        }

        nbttagcompound.func_74782_a("ArmorItems", nbttaglist);
        NBTTagList nbttaglist1 = new NBTTagList();

        NBTTagCompound nbttagcompound2;

        for (Iterator iterator1 = this.field_184799_bw.iterator(); iterator1.hasNext(); nbttaglist1.func_74742_a(nbttagcompound2)) {
            ItemStack itemstack1 = (ItemStack) iterator1.next();

            nbttagcompound2 = new NBTTagCompound();
            if (!itemstack1.func_190926_b()) {
                itemstack1.func_77955_b(nbttagcompound2);
            }
        }

        nbttagcompound.func_74782_a("HandItems", nbttaglist1);
        nbttagcompound.func_74757_a("Invisible", this.func_82150_aj());
        nbttagcompound.func_74757_a("Small", this.func_175410_n());
        nbttagcompound.func_74757_a("ShowArms", this.func_175402_q());
        nbttagcompound.func_74768_a("DisabledSlots", this.field_175442_bg);
        nbttagcompound.func_74757_a("NoBasePlate", this.func_175414_r());
        if (this.func_181026_s()) {
            nbttagcompound.func_74757_a("Marker", this.func_181026_s());
        }

        nbttagcompound.func_74782_a("Pose", this.func_175419_y());
    }

    public void func_70037_a(NBTTagCompound nbttagcompound) {
        super.func_70037_a(nbttagcompound);
        NBTTagList nbttaglist;
        int i;

        if (nbttagcompound.func_150297_b("ArmorItems", 9)) {
            nbttaglist = nbttagcompound.func_150295_c("ArmorItems", 10);

            for (i = 0; i < this.field_184800_bx.size(); ++i) {
                this.field_184800_bx.set(i, new ItemStack(nbttaglist.func_150305_b(i)));
            }
        }

        if (nbttagcompound.func_150297_b("HandItems", 9)) {
            nbttaglist = nbttagcompound.func_150295_c("HandItems", 10);

            for (i = 0; i < this.field_184799_bw.size(); ++i) {
                this.field_184799_bw.set(i, new ItemStack(nbttaglist.func_150305_b(i)));
            }
        }

        this.func_82142_c(nbttagcompound.func_74767_n("Invisible"));
        this.func_175420_a(nbttagcompound.func_74767_n("Small"));
        this.func_175413_k(nbttagcompound.func_74767_n("ShowArms"));
        this.field_175442_bg = nbttagcompound.func_74762_e("DisabledSlots");
        this.func_175426_l(nbttagcompound.func_74767_n("NoBasePlate"));
        this.func_181027_m(nbttagcompound.func_74767_n("Marker"));
        this.field_181028_bj = !this.func_181026_s();
        this.field_70145_X = this.func_189652_ae();
        NBTTagCompound nbttagcompound1 = nbttagcompound.func_74775_l("Pose");

        this.func_175416_h(nbttagcompound1);
    }

    private void func_175416_h(NBTTagCompound nbttagcompound) {
        NBTTagList nbttaglist = nbttagcompound.func_150295_c("Head", 5);

        this.func_175415_a(nbttaglist.func_82582_d() ? EntityArmorStand.field_175435_a : new Rotations(nbttaglist));
        NBTTagList nbttaglist1 = nbttagcompound.func_150295_c("Body", 5);

        this.func_175424_b(nbttaglist1.func_82582_d() ? EntityArmorStand.field_175433_b : new Rotations(nbttaglist1));
        NBTTagList nbttaglist2 = nbttagcompound.func_150295_c("LeftArm", 5);

        this.func_175405_c(nbttaglist2.func_82582_d() ? EntityArmorStand.field_175434_c : new Rotations(nbttaglist2));
        NBTTagList nbttaglist3 = nbttagcompound.func_150295_c("RightArm", 5);

        this.func_175428_d(nbttaglist3.func_82582_d() ? EntityArmorStand.field_175431_d : new Rotations(nbttaglist3));
        NBTTagList nbttaglist4 = nbttagcompound.func_150295_c("LeftLeg", 5);

        this.func_175417_e(nbttaglist4.func_82582_d() ? EntityArmorStand.field_175432_e : new Rotations(nbttaglist4));
        NBTTagList nbttaglist5 = nbttagcompound.func_150295_c("RightLeg", 5);

        this.func_175427_f(nbttaglist5.func_82582_d() ? EntityArmorStand.field_175429_f : new Rotations(nbttaglist5));
    }

    private NBTTagCompound func_175419_y() {
        NBTTagCompound nbttagcompound = new NBTTagCompound();

        if (!EntityArmorStand.field_175435_a.equals(this.field_175443_bh)) {
            nbttagcompound.func_74782_a("Head", this.field_175443_bh.func_179414_a());
        }

        if (!EntityArmorStand.field_175433_b.equals(this.field_175444_bi)) {
            nbttagcompound.func_74782_a("Body", this.field_175444_bi.func_179414_a());
        }

        if (!EntityArmorStand.field_175434_c.equals(this.field_175438_bj)) {
            nbttagcompound.func_74782_a("LeftArm", this.field_175438_bj.func_179414_a());
        }

        if (!EntityArmorStand.field_175431_d.equals(this.field_175439_bk)) {
            nbttagcompound.func_74782_a("RightArm", this.field_175439_bk.func_179414_a());
        }

        if (!EntityArmorStand.field_175432_e.equals(this.field_175440_bl)) {
            nbttagcompound.func_74782_a("LeftLeg", this.field_175440_bl.func_179414_a());
        }

        if (!EntityArmorStand.field_175429_f.equals(this.field_175441_bm)) {
            nbttagcompound.func_74782_a("RightLeg", this.field_175441_bm.func_179414_a());
        }

        return nbttagcompound;
    }

    public boolean func_70104_M() {
        return false;
    }

    protected void func_82167_n(Entity entity) {}

    protected void func_85033_bc() {
        List list = this.field_70170_p.func_175674_a(this, this.func_174813_aQ(), EntityArmorStand.field_184798_bv);

        for (int i = 0; i < list.size(); ++i) {
            Entity entity = (Entity) list.get(i);

            if (this.func_70068_e(entity) <= 0.2D) {
                entity.func_70108_f(this);
            }
        }

    }

    public EnumActionResult func_184199_a(EntityPlayer entityhuman, Vec3d vec3d, EnumHand enumhand) {
        ItemStack itemstack = entityhuman.func_184586_b(enumhand);

        if (!this.func_181026_s() && itemstack.func_77973_b() != Items.field_151057_cb) {
            if (!this.field_70170_p.field_72995_K && !entityhuman.func_175149_v()) {
                EntityEquipmentSlot enumitemslot = EntityLiving.func_184640_d(itemstack);

                if (itemstack.func_190926_b()) {
                    EntityEquipmentSlot enumitemslot1 = this.func_190772_a(vec3d);
                    EntityEquipmentSlot enumitemslot2 = this.func_184796_b(enumitemslot1) ? enumitemslot : enumitemslot1;

                    if (this.func_190630_a(enumitemslot2)) {
                        this.func_184795_a(entityhuman, enumitemslot2, itemstack, enumhand);
                    }
                } else {
                    if (this.func_184796_b(enumitemslot)) {
                        return EnumActionResult.FAIL;
                    }

                    if (enumitemslot.func_188453_a() == EntityEquipmentSlot.Type.HAND && !this.func_175402_q()) {
                        return EnumActionResult.FAIL;
                    }

                    this.func_184795_a(entityhuman, enumitemslot, itemstack, enumhand);
                }

                return EnumActionResult.SUCCESS;
            } else {
                return EnumActionResult.SUCCESS;
            }
        } else {
            return EnumActionResult.PASS;
        }
    }

    protected EntityEquipmentSlot func_190772_a(Vec3d vec3d) {
        EntityEquipmentSlot enumitemslot = EntityEquipmentSlot.MAINHAND;
        boolean flag = this.func_175410_n();
        double d0 = flag ? vec3d.field_72448_b * 2.0D : vec3d.field_72448_b;
        EntityEquipmentSlot enumitemslot1 = EntityEquipmentSlot.FEET;

        if (d0 >= 0.1D && d0 < 0.1D + (flag ? 0.8D : 0.45D) && this.func_190630_a(enumitemslot1)) {
            enumitemslot = EntityEquipmentSlot.FEET;
        } else if (d0 >= 0.9D + (flag ? 0.3D : 0.0D) && d0 < 0.9D + (flag ? 1.0D : 0.7D) && this.func_190630_a(EntityEquipmentSlot.CHEST)) {
            enumitemslot = EntityEquipmentSlot.CHEST;
        } else if (d0 >= 0.4D && d0 < 0.4D + (flag ? 1.0D : 0.8D) && this.func_190630_a(EntityEquipmentSlot.LEGS)) {
            enumitemslot = EntityEquipmentSlot.LEGS;
        } else if (d0 >= 1.6D && this.func_190630_a(EntityEquipmentSlot.HEAD)) {
            enumitemslot = EntityEquipmentSlot.HEAD;
        }

        return enumitemslot;
    }

    private boolean func_184796_b(EntityEquipmentSlot enumitemslot) {
        return (this.field_175442_bg & 1 << enumitemslot.func_188452_c()) != 0;
    }

    private void func_184795_a(EntityPlayer entityhuman, EntityEquipmentSlot enumitemslot, ItemStack itemstack, EnumHand enumhand) {
        ItemStack itemstack1 = this.func_184582_a(enumitemslot);

        if (itemstack1.func_190926_b() || (this.field_175442_bg & 1 << enumitemslot.func_188452_c() + 8) == 0) {
            if (!itemstack1.func_190926_b() || (this.field_175442_bg & 1 << enumitemslot.func_188452_c() + 16) == 0) {
                ItemStack itemstack2;
                // CraftBukkit start
                org.bukkit.inventory.ItemStack armorStandItem = CraftItemStack.asCraftMirror(itemstack1);
                org.bukkit.inventory.ItemStack playerHeldItem = CraftItemStack.asCraftMirror(itemstack);

                Player player = (Player) entityhuman.getBukkitEntity();
                ArmorStand self = (ArmorStand) this.getBukkitEntity();

                EquipmentSlot slot = CraftEquipmentSlot.getSlot(enumitemslot);
                PlayerArmorStandManipulateEvent armorStandManipulateEvent = new PlayerArmorStandManipulateEvent(player,self,playerHeldItem,armorStandItem,slot);
                this.field_70170_p.getServer().getPluginManager().callEvent(armorStandManipulateEvent);

                if (armorStandManipulateEvent.isCancelled()) {
                    return;
                }
                // CraftBukkit end

                if (entityhuman.field_71075_bZ.field_75098_d && itemstack1.func_190926_b() && !itemstack.func_190926_b()) {
                    itemstack2 = itemstack.func_77946_l();
                    itemstack2.func_190920_e(1);
                    this.func_184201_a(enumitemslot, itemstack2);
                } else if (!itemstack.func_190926_b() && itemstack.func_190916_E() > 1) {
                    if (itemstack1.func_190926_b()) {
                        itemstack2 = itemstack.func_77946_l();
                        itemstack2.func_190920_e(1);
                        this.func_184201_a(enumitemslot, itemstack2);
                        itemstack.func_190918_g(1);
                    }
                } else {
                    this.func_184201_a(enumitemslot, itemstack);
                    entityhuman.func_184611_a(enumhand, itemstack1);
                }
            }
        }
    }

    public boolean func_70097_a(DamageSource damagesource, float f) {
        // CraftBukkit start
        if (org.bukkit.craftbukkit.event.CraftEventFactory.handleNonLivingEntityDamageEvent(this, damagesource, f)) {
            return false;
        }
        // CraftBukkit end
        if (!this.field_70170_p.field_72995_K && !this.field_70128_L) {
            if (DamageSource.field_76380_i.equals(damagesource)) {
                this.func_174812_G(); // CraftBukkit - this.die() -> this.killEntity()
                return false;
            } else if (!this.func_180431_b(damagesource) && !this.field_175436_h && !this.func_181026_s()) {
                if (damagesource.func_94541_c()) {
                    this.func_175409_C();
                    this.func_174812_G(); // CraftBukkit - this.die() -> this.killEntity()
                    return false;
                } else if (DamageSource.field_76372_a.equals(damagesource)) {
                    if (this.func_70027_ad()) {
                        this.func_175406_a(0.15F);
                    } else {
                        this.func_70015_d(5);
                    }

                    return false;
                } else if (DamageSource.field_76370_b.equals(damagesource) && this.func_110143_aJ() > 0.5F) {
                    this.func_175406_a(4.0F);
                    return false;
                } else {
                    boolean flag = "arrow".equals(damagesource.func_76355_l());
                    boolean flag1 = "player".equals(damagesource.func_76355_l());

                    if (!flag1 && !flag) {
                        return false;
                    } else {
                        if (damagesource.func_76364_f() instanceof EntityArrow) {
                            damagesource.func_76364_f().func_70106_y();
                        }

                        if (damagesource.func_76346_g() instanceof EntityPlayer && !((EntityPlayer) damagesource.func_76346_g()).field_71075_bZ.field_75099_e) {
                            return false;
                        } else if (damagesource.func_180136_u()) {
                            this.func_190773_I();
                            this.func_175412_z();
                            this.func_174812_G(); // CraftBukkit - this.die() -> this.killEntity()
                            return false;
                        } else {
                            long i = this.field_70170_p.func_82737_E();

                            if (i - this.field_175437_i > 5L && !flag) {
                                this.field_70170_p.func_72960_a(this, (byte) 32);
                                this.field_175437_i = i;
                            } else {
                                this.func_175421_A();
                                this.func_175412_z();
                                this.func_174812_G(); // CraftBukkit - this.die() -> this.killEntity()
                            }

                            return false;
                        }
                    }
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private void func_175412_z() {
        if (this.field_70170_p instanceof WorldServer) {
            ((WorldServer) this.field_70170_p).func_175739_a(EnumParticleTypes.BLOCK_DUST, this.field_70165_t, this.field_70163_u + (double) this.field_70131_O / 1.5D, this.field_70161_v, 10, (double) (this.field_70130_N / 4.0F), (double) (this.field_70131_O / 4.0F), (double) (this.field_70130_N / 4.0F), 0.05D, new int[] { Block.func_176210_f(Blocks.field_150344_f.func_176223_P())});
        }

    }

    private void func_175406_a(float f) {
        float f1 = this.func_110143_aJ();

        f1 -= f;
        if (f1 <= 0.5F) {
            this.func_175409_C();
            this.func_174812_G(); // CraftBukkit - this.die() -> this.killEntity()
        } else {
            this.func_70606_j(f1);
        }

    }

    private void func_175421_A() {
        drops.add(org.bukkit.craftbukkit.inventory.CraftItemStack.asBukkitCopy(new ItemStack(Items.field_179565_cj))); // CraftBukkit - add to drops
        this.func_175409_C();
    }

    private void func_175409_C() {
        this.func_190773_I();

        int i;
        ItemStack itemstack;

        for (i = 0; i < this.field_184799_bw.size(); ++i) {
            itemstack = (ItemStack) this.field_184799_bw.get(i);
            if (!itemstack.func_190926_b()) {
                drops.add(org.bukkit.craftbukkit.inventory.CraftItemStack.asBukkitCopy(itemstack)); // CraftBukkit - add to drops
                this.field_184799_bw.set(i, ItemStack.field_190927_a);
            }
        }

        for (i = 0; i < this.field_184800_bx.size(); ++i) {
            itemstack = (ItemStack) this.field_184800_bx.get(i);
            if (!itemstack.func_190926_b()) {
                drops.add(org.bukkit.craftbukkit.inventory.CraftItemStack.asBukkitCopy(itemstack)); // CraftBukkit - add to drops
                this.field_184800_bx.set(i, ItemStack.field_190927_a);
            }
        }

    }

    private void func_190773_I() {
        this.field_70170_p.func_184148_a((EntityPlayer) null, this.field_70165_t, this.field_70163_u, this.field_70161_v, SoundEvents.field_187701_j, this.func_184176_by(), 1.0F, 1.0F);
    }

    protected float func_110146_f(float f, float f1) {
        this.field_70760_ar = this.field_70126_B;
        this.field_70761_aq = this.field_70177_z;
        return 0.0F;
    }

    public float func_70047_e() {
        return this.func_70631_g_() ? this.field_70131_O * 0.5F : this.field_70131_O * 0.9F;
    }

    public double func_70033_W() {
        return this.func_181026_s() ? 0.0D : 0.10000000149011612D;
    }

    public void func_191986_a(float f, float f1, float f2) {
        if (!this.func_189652_ae()) {
            super.func_191986_a(f, f1, f2);
        }
    }

    public void func_181013_g(float f) {
        this.field_70760_ar = this.field_70126_B = f;
        this.field_70758_at = this.field_70759_as = f;
    }

    public void func_70034_d(float f) {
        this.field_70760_ar = this.field_70126_B = f;
        this.field_70758_at = this.field_70759_as = f;
    }

    public void func_70071_h_() {
        super.func_70071_h_();
        Rotations vector3f = (Rotations) this.field_70180_af.func_187225_a(EntityArmorStand.field_184802_b);

        if (!this.field_175443_bh.equals(vector3f)) {
            this.func_175415_a(vector3f);
        }

        Rotations vector3f1 = (Rotations) this.field_70180_af.func_187225_a(EntityArmorStand.field_184803_c);

        if (!this.field_175444_bi.equals(vector3f1)) {
            this.func_175424_b(vector3f1);
        }

        Rotations vector3f2 = (Rotations) this.field_70180_af.func_187225_a(EntityArmorStand.field_184804_d);

        if (!this.field_175438_bj.equals(vector3f2)) {
            this.func_175405_c(vector3f2);
        }

        Rotations vector3f3 = (Rotations) this.field_70180_af.func_187225_a(EntityArmorStand.field_184805_e);

        if (!this.field_175439_bk.equals(vector3f3)) {
            this.func_175428_d(vector3f3);
        }

        Rotations vector3f4 = (Rotations) this.field_70180_af.func_187225_a(EntityArmorStand.field_184806_f);

        if (!this.field_175440_bl.equals(vector3f4)) {
            this.func_175417_e(vector3f4);
        }

        Rotations vector3f5 = (Rotations) this.field_70180_af.func_187225_a(EntityArmorStand.field_184807_g);

        if (!this.field_175441_bm.equals(vector3f5)) {
            this.func_175427_f(vector3f5);
        }

        boolean flag = this.func_181026_s();

        if (this.field_181028_bj != flag) {
            this.func_181550_a(flag);
            this.field_70156_m = !flag;
            this.field_181028_bj = flag;
        }

    }

    private void func_181550_a(boolean flag) {
        if (flag) {
            this.func_70105_a(0.0F, 0.0F);
        } else {
            this.func_70105_a(0.5F, 1.975F);
        }

    }

    protected void func_175135_B() {
        this.func_82142_c(this.field_175436_h);
    }

    public void func_82142_c(boolean flag) {
        this.field_175436_h = flag;
        super.func_82142_c(flag);
    }

    public boolean func_70631_g_() {
        return this.func_175410_n();
    }

    public void func_174812_G() {
        org.bukkit.craftbukkit.event.CraftEventFactory.callEntityDeathEvent(this, drops); // CraftBukkit - call event
        this.func_70106_y();
    }

    public boolean func_180427_aV() {
        return this.func_82150_aj();
    }

    public EnumPushReaction func_184192_z() {
        return this.func_181026_s() ? EnumPushReaction.IGNORE : super.func_184192_z();
    }

    public void func_175420_a(boolean flag) {
        this.field_70180_af.func_187227_b(EntityArmorStand.field_184801_a, Byte.valueOf(this.func_184797_a(((Byte) this.field_70180_af.func_187225_a(EntityArmorStand.field_184801_a)).byteValue(), 1, flag)));
        this.func_70105_a(0.5F, 1.975F);
    }

    public boolean func_175410_n() {
        return (((Byte) this.field_70180_af.func_187225_a(EntityArmorStand.field_184801_a)).byteValue() & 1) != 0;
    }

    public void func_175413_k(boolean flag) {
        this.field_70180_af.func_187227_b(EntityArmorStand.field_184801_a, Byte.valueOf(this.func_184797_a(((Byte) this.field_70180_af.func_187225_a(EntityArmorStand.field_184801_a)).byteValue(), 4, flag)));
    }

    public boolean func_175402_q() {
        return (((Byte) this.field_70180_af.func_187225_a(EntityArmorStand.field_184801_a)).byteValue() & 4) != 0;
    }

    public void func_175426_l(boolean flag) {
        this.field_70180_af.func_187227_b(EntityArmorStand.field_184801_a, Byte.valueOf(this.func_184797_a(((Byte) this.field_70180_af.func_187225_a(EntityArmorStand.field_184801_a)).byteValue(), 8, flag)));
    }

    public boolean func_175414_r() {
        return (((Byte) this.field_70180_af.func_187225_a(EntityArmorStand.field_184801_a)).byteValue() & 8) != 0;
    }

    public void func_181027_m(boolean flag) {
        this.field_70180_af.func_187227_b(EntityArmorStand.field_184801_a, Byte.valueOf(this.func_184797_a(((Byte) this.field_70180_af.func_187225_a(EntityArmorStand.field_184801_a)).byteValue(), 16, flag)));
        this.func_70105_a(0.5F, 1.975F);
    }

    public boolean func_181026_s() {
        return (((Byte) this.field_70180_af.func_187225_a(EntityArmorStand.field_184801_a)).byteValue() & 16) != 0;
    }

    private byte func_184797_a(byte b0, int i, boolean flag) {
        if (flag) {
            b0 = (byte) (b0 | i);
        } else {
            b0 = (byte) (b0 & ~i);
        }

        return b0;
    }

    public void func_175415_a(Rotations vector3f) {
        this.field_175443_bh = vector3f;
        this.field_70180_af.func_187227_b(EntityArmorStand.field_184802_b, vector3f);
    }

    public void func_175424_b(Rotations vector3f) {
        this.field_175444_bi = vector3f;
        this.field_70180_af.func_187227_b(EntityArmorStand.field_184803_c, vector3f);
    }

    public void func_175405_c(Rotations vector3f) {
        this.field_175438_bj = vector3f;
        this.field_70180_af.func_187227_b(EntityArmorStand.field_184804_d, vector3f);
    }

    public void func_175428_d(Rotations vector3f) {
        this.field_175439_bk = vector3f;
        this.field_70180_af.func_187227_b(EntityArmorStand.field_184805_e, vector3f);
    }

    public void func_175417_e(Rotations vector3f) {
        this.field_175440_bl = vector3f;
        this.field_70180_af.func_187227_b(EntityArmorStand.field_184806_f, vector3f);
    }

    public void func_175427_f(Rotations vector3f) {
        this.field_175441_bm = vector3f;
        this.field_70180_af.func_187227_b(EntityArmorStand.field_184807_g, vector3f);
    }

    public Rotations func_175418_s() {
        return this.field_175443_bh;
    }

    public Rotations func_175408_t() {
        return this.field_175444_bi;
    }

    public boolean func_70067_L() {
        return super.func_70067_L() && !this.func_181026_s();
    }

    public EnumHandSide func_184591_cq() {
        return EnumHandSide.RIGHT;
    }

    protected SoundEvent func_184588_d(int i) {
        return SoundEvents.field_187704_k;
    }

    @Nullable
    protected SoundEvent func_184601_bQ(DamageSource damagesource) {
        return SoundEvents.field_187707_l;
    }

    @Nullable
    protected SoundEvent func_184615_bR() {
        return SoundEvents.field_187701_j;
    }

    public void func_70077_a(EntityLightningBolt entitylightning) {}

    public boolean func_184603_cC() {
        return false;
    }

    public void func_184206_a(DataParameter<?> datawatcherobject) {
        if (EntityArmorStand.field_184801_a.equals(datawatcherobject)) {
            this.func_70105_a(0.5F, 1.975F);
        }

        super.func_184206_a(datawatcherobject);
    }

    public boolean func_190631_cK() {
        return false;
    }

    // Paper start
    @Override
    public void func_70091_d(MoverType moveType, double x, double y, double z) {
        if (this.canMove) {
            super.func_70091_d(moveType, x, y, z);
        }
    }

    @Override
    public boolean canBreatheUnderwater() { // Skips a bit of damage handling code, probably a micro-optimization
        return true;
    }
    // Paper end
}
