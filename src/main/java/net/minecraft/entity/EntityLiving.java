package net.minecraft.entity;

import com.google.common.collect.Maps;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import javax.annotation.Nullable;

import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.ai.EntityJumpHelper;
import net.minecraft.entity.ai.EntityLookHelper;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.entity.ai.EntitySenses;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.Packet;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.server.SPacketEntityAttach;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.IDataWalker;
import net.minecraft.util.datafix.walkers.ItemStackDataLists;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootTable;
import org.bukkit.craftbukkit.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.entity.EntityUnleashEvent;
import org.bukkit.event.entity.EntityUnleashEvent.UnleashReason;

// CraftBukkit start
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.craftbukkit.entity.CraftLivingEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.EntityUnleashEvent;
import org.bukkit.event.entity.EntityUnleashEvent.UnleashReason;
// CraftBukkit end

public abstract class EntityLiving extends EntityLivingBase {

    private static final DataParameter<Byte> field_184654_a = EntityDataManager.func_187226_a(EntityLiving.class, DataSerializers.field_187191_a);
    public int field_70757_a;
    protected int field_70728_aV;
    private final EntityLookHelper field_70749_g;
    protected EntityMoveHelper field_70765_h;
    protected EntityJumpHelper field_70767_i;
    private final EntityBodyHelper field_70762_j;
    protected PathNavigate field_70699_by;
    public EntityAITasks field_70714_bg;
    public EntityAITasks field_70715_bh;
    private EntityLivingBase field_70696_bz;
    private final EntitySenses field_70723_bA;
    private final NonNullList<ItemStack> field_184656_bv;
    public float[] field_82174_bp;
    private final NonNullList<ItemStack> field_184657_bw;
    public float[] field_184655_bs;
    // public boolean canPickUpLoot; // CraftBukkit - moved up to EntityLiving
    public boolean field_82179_bU;
    private final Map<PathNodeType, Float> field_184658_bz;
    private ResourceLocation field_184659_bA;
    private long field_184653_bB;
    private boolean field_110169_bv;
    private Entity field_110168_bw;
    private NBTTagCompound field_110170_bx;
    @Nullable public EntityAISwimming goalFloat; // Paper

    public EntityLiving(World world) {
        super(world);
        this.field_184656_bv = NonNullList.func_191197_a(2, ItemStack.field_190927_a);
        this.field_82174_bp = new float[2];
        this.field_184657_bw = NonNullList.func_191197_a(4, ItemStack.field_190927_a);
        this.field_184655_bs = new float[4];
        this.field_184658_bz = Maps.newEnumMap(PathNodeType.class);
        this.field_70714_bg = new EntityAITasks(world != null && world.field_72984_F != null ? world.field_72984_F : null);
        this.field_70715_bh = new EntityAITasks(world != null && world.field_72984_F != null ? world.field_72984_F : null);
        this.field_70749_g = new EntityLookHelper(this);
        this.field_70765_h = new EntityMoveHelper(this);
        this.field_70767_i = new EntityJumpHelper(this);
        this.field_70762_j = this.func_184650_s();
        this.field_70699_by = this.func_175447_b(world);
        this.field_70723_bA = new EntitySenses(this);
        Arrays.fill(this.field_184655_bs, 0.085F);
        Arrays.fill(this.field_82174_bp, 0.085F);
        if (world != null && !world.field_72995_K) {
            this.func_184651_r();
        }

        // CraftBukkit start - default persistance to type's persistance value
        this.field_82179_bU = !func_70692_ba();
        // CraftBukkit end
    }

    protected void func_184651_r() {}

    protected void func_110147_ax() {
        super.func_110147_ax();
        this.func_110140_aT().func_111150_b(SharedMonsterAttributes.field_111265_b).func_111128_a(16.0D);
    }

    protected PathNavigate func_175447_b(World world) {
        return new PathNavigateGround(this, world);
    }

    public float func_184643_a(PathNodeType pathtype) {
        Float ofloat = (Float) this.field_184658_bz.get(pathtype);

        return ofloat == null ? pathtype.func_186289_a() : ofloat.floatValue();
    }

    public void func_184644_a(PathNodeType pathtype, float f) {
        this.field_184658_bz.put(pathtype, Float.valueOf(f));
    }

    protected EntityBodyHelper func_184650_s() {
        return new EntityBodyHelper(this);
    }

    public EntityLookHelper func_70671_ap() {
        return this.field_70749_g;
    }

    public EntityMoveHelper func_70605_aq() {
        return this.field_70765_h;
    }

    public EntityJumpHelper func_70683_ar() {
        return this.field_70767_i;
    }

    public PathNavigate func_70661_as() {
        return this.field_70699_by;
    }

    public EntitySenses func_70635_at() {
        return this.field_70723_bA;
    }

    @Nullable
    public EntityLivingBase func_70638_az() {
        return this.field_70696_bz;
    }

    public void func_70624_b(@Nullable EntityLivingBase entityliving) {
        // CraftBukkit start - fire event
        setGoalTarget(entityliving, EntityTargetEvent.TargetReason.UNKNOWN, true);
    }

    public boolean setGoalTarget(EntityLivingBase entityliving, EntityTargetEvent.TargetReason reason, boolean fireEvent) {
        if (func_70638_az() == entityliving) return false;
        if (fireEvent) {
            if (reason == EntityTargetEvent.TargetReason.UNKNOWN && func_70638_az() != null && entityliving == null) {
                reason = func_70638_az().func_70089_S() ? EntityTargetEvent.TargetReason.FORGOT_TARGET : EntityTargetEvent.TargetReason.TARGET_DIED;
            }
            if (reason == EntityTargetEvent.TargetReason.UNKNOWN) {
                field_70170_p.getServer().getLogger().log(java.util.logging.Level.WARNING, "Unknown target reason, please report on the issue tracker", new Exception());
            }
            CraftLivingEntity ctarget = null;
            if (entityliving != null) {
                ctarget = (CraftLivingEntity) entityliving.getBukkitEntity();
            }
            EntityTargetLivingEntityEvent event = new EntityTargetLivingEntityEvent(this.getBukkitEntity(), ctarget, reason);
            field_70170_p.getServer().getPluginManager().callEvent(event);
            if (event.isCancelled()) {
                return false;
            }

            if (event.getTarget() != null) {
                entityliving = ((CraftLivingEntity) event.getTarget()).getHandle();
            } else {
                entityliving = null;
            }
        }
        this.field_70696_bz = entityliving;
        return true;
        // CraftBukkit end
    }

    public boolean func_70686_a(Class<? extends EntityLivingBase> oclass) {
        return oclass != EntityGhast.class;
    }

    public void func_70615_aA() {}

    protected void func_70088_a() {
        super.func_70088_a();
        this.field_70180_af.func_187214_a(EntityLiving.field_184654_a, Byte.valueOf((byte) 0));
    }

    public int func_70627_aG() {
        return 80;
    }

    public void func_70642_aH() {
        SoundEvent soundeffect = this.func_184639_G();

        if (soundeffect != null) {
            this.func_184185_a(soundeffect, this.func_70599_aP(), this.func_70647_i());
        }

    }

    public void func_70030_z() {
        super.func_70030_z();
        this.field_70170_p.field_72984_F.func_76320_a("mobBaseTick");
        if (this.func_70089_S() && this.field_70146_Z.nextInt(1000) < this.field_70757_a++) {
            this.func_175456_n();
            this.func_70642_aH();
        }

        this.field_70170_p.field_72984_F.func_76319_b();
    }

    protected void func_184581_c(DamageSource damagesource) {
        this.func_175456_n();
        super.func_184581_c(damagesource);
    }

    private void func_175456_n() {
        this.field_70757_a = -this.func_70627_aG();
    }

    protected int func_70693_a(EntityPlayer entityhuman) {
        if (this.field_70728_aV > 0) {
            int i = this.field_70728_aV;

            int j;

            for (j = 0; j < this.field_184657_bw.size(); ++j) {
                if (!((ItemStack) this.field_184657_bw.get(j)).func_190926_b() && this.field_184655_bs[j] <= 1.0F) {
                    i += 1 + this.field_70146_Z.nextInt(3);
                }
            }

            for (j = 0; j < this.field_184656_bv.size(); ++j) {
                if (!((ItemStack) this.field_184656_bv.get(j)).func_190926_b() && this.field_82174_bp[j] <= 1.0F) {
                    i += 1 + this.field_70146_Z.nextInt(3);
                }
            }

            return i;
        } else {
            return this.field_70728_aV;
        }
    }

    public void func_70656_aK() {
        if (this.field_70170_p.field_72995_K) {
            for (int i = 0; i < 20; ++i) {
                double d0 = this.field_70146_Z.nextGaussian() * 0.02D;
                double d1 = this.field_70146_Z.nextGaussian() * 0.02D;
                double d2 = this.field_70146_Z.nextGaussian() * 0.02D;
                double d3 = 10.0D;

                this.field_70170_p.func_175688_a(EnumParticleTypes.EXPLOSION_NORMAL, this.field_70165_t + (double) (this.field_70146_Z.nextFloat() * this.field_70130_N * 2.0F) - (double) this.field_70130_N - d0 * 10.0D, this.field_70163_u + (double) (this.field_70146_Z.nextFloat() * this.field_70131_O) - d1 * 10.0D, this.field_70161_v + (double) (this.field_70146_Z.nextFloat() * this.field_70130_N * 2.0F) - (double) this.field_70130_N - d2 * 10.0D, d0, d1, d2, new int[0]);
            }
        } else {
            this.field_70170_p.func_72960_a(this, (byte) 20);
        }

    }

    public void func_70071_h_() {
        super.func_70071_h_();
        if (!this.field_70170_p.field_72995_K) {
            this.func_110159_bB();
            if (this.field_70173_aa % 5 == 0) {
                boolean flag = !(this.func_184179_bs() instanceof EntityLiving);
                boolean flag1 = !(this.func_184187_bx() instanceof EntityBoat);

                this.field_70714_bg.func_188527_a(1, flag);
                this.field_70714_bg.func_188527_a(4, flag && flag1);
                this.field_70714_bg.func_188527_a(2, flag);
            }
        }

    }

    protected float func_110146_f(float f, float f1) {
        this.field_70762_j.func_75664_a();
        return f1;
    }

    @Nullable
    protected SoundEvent func_184639_G() {
        return null;
    }

    @Nullable
    protected Item func_146068_u() {
        return null;
    }

    protected void func_70628_a(boolean flag, int i) {
        Item item = this.func_146068_u();

        if (item != null) {
            int j = this.field_70146_Z.nextInt(3);

            if (i > 0) {
                j += this.field_70146_Z.nextInt(i + 1);
            }

            for (int k = 0; k < j; ++k) {
                this.func_145779_a(item, 1);
            }
        }

    }

    public static void func_189752_a(DataFixer dataconvertermanager, Class<?> oclass) {
        dataconvertermanager.func_188258_a(FixTypes.ENTITY, (IDataWalker) (new ItemStackDataLists(oclass, new String[] { "ArmorItems", "HandItems"})));
    }

    public void func_70014_b(NBTTagCompound nbttagcompound) {
        super.func_70014_b(nbttagcompound);
        nbttagcompound.func_74757_a("CanPickUpLoot", this.func_98052_bS());
        nbttagcompound.func_74757_a("PersistenceRequired", this.field_82179_bU);
        NBTTagList nbttaglist = new NBTTagList();

        NBTTagCompound nbttagcompound1;

        for (Iterator iterator = this.field_184657_bw.iterator(); iterator.hasNext(); nbttaglist.func_74742_a(nbttagcompound1)) {
            ItemStack itemstack = (ItemStack) iterator.next();

            nbttagcompound1 = new NBTTagCompound();
            if (!itemstack.func_190926_b()) {
                itemstack.func_77955_b(nbttagcompound1);
            }
        }

        nbttagcompound.func_74782_a("ArmorItems", nbttaglist);
        NBTTagList nbttaglist1 = new NBTTagList();

        NBTTagCompound nbttagcompound2;

        for (Iterator iterator1 = this.field_184656_bv.iterator(); iterator1.hasNext(); nbttaglist1.func_74742_a(nbttagcompound2)) {
            ItemStack itemstack1 = (ItemStack) iterator1.next();

            nbttagcompound2 = new NBTTagCompound();
            if (!itemstack1.func_190926_b()) {
                itemstack1.func_77955_b(nbttagcompound2);
            }
        }

        nbttagcompound.func_74782_a("HandItems", nbttaglist1);
        NBTTagList nbttaglist2 = new NBTTagList();
        float[] afloat = this.field_184655_bs;
        int i = afloat.length;

        int j;

        for (j = 0; j < i; ++j) {
            float f = afloat[j];

            nbttaglist2.func_74742_a(new NBTTagFloat(f));
        }

        nbttagcompound.func_74782_a("ArmorDropChances", nbttaglist2);
        NBTTagList nbttaglist3 = new NBTTagList();
        float[] afloat1 = this.field_82174_bp;

        j = afloat1.length;

        for (int k = 0; k < j; ++k) {
            float f1 = afloat1[k];

            nbttaglist3.func_74742_a(new NBTTagFloat(f1));
        }

        nbttagcompound.func_74782_a("HandDropChances", nbttaglist3);
        nbttagcompound.func_74757_a("Leashed", this.field_110169_bv);
        if (this.field_110168_bw != null) {
            nbttagcompound2 = new NBTTagCompound();
            if (this.field_110168_bw instanceof EntityLivingBase) {
                UUID uuid = this.field_110168_bw.func_110124_au();

                nbttagcompound2.func_186854_a("UUID", uuid);
            } else if (this.field_110168_bw instanceof EntityHanging) {
                BlockPos blockposition = ((EntityHanging) this.field_110168_bw).func_174857_n();

                nbttagcompound2.func_74768_a("X", blockposition.func_177958_n());
                nbttagcompound2.func_74768_a("Y", blockposition.func_177956_o());
                nbttagcompound2.func_74768_a("Z", blockposition.func_177952_p());
            }

            nbttagcompound.func_74782_a("Leash", nbttagcompound2);
        }

        nbttagcompound.func_74757_a("LeftHanded", this.func_184638_cS());
        if (this.field_184659_bA != null) {
            nbttagcompound.func_74778_a("DeathLootTable", this.field_184659_bA.toString());
            if (this.field_184653_bB != 0L) {
                nbttagcompound.func_74772_a("DeathLootTableSeed", this.field_184653_bB);
            }
        }

        if (this.func_175446_cd()) {
            nbttagcompound.func_74757_a("NoAI", this.func_175446_cd());
        }

    }

    public void func_70037_a(NBTTagCompound nbttagcompound) {
        super.func_70037_a(nbttagcompound);

        // CraftBukkit start - If looting or persistence is false only use it if it was set after we started using it
        if (nbttagcompound.func_150297_b("CanPickUpLoot", 1)) {
            boolean data = nbttagcompound.func_74767_n("CanPickUpLoot");
            if (isLevelAtLeast(nbttagcompound, 1) || data) {
                this.func_98053_h(data);
            }
        }

        boolean data = nbttagcompound.func_74767_n("PersistenceRequired");
        if (isLevelAtLeast(nbttagcompound, 1) || data) {
            this.field_82179_bU = data;
        }
        // CraftBukkit end
        NBTTagList nbttaglist;
        int i;

        if (nbttagcompound.func_150297_b("ArmorItems", 9)) {
            nbttaglist = nbttagcompound.func_150295_c("ArmorItems", 10);

            for (i = 0; i < this.field_184657_bw.size(); ++i) {
                this.field_184657_bw.set(i, new ItemStack(nbttaglist.func_150305_b(i)));
            }
        }

        if (nbttagcompound.func_150297_b("HandItems", 9)) {
            nbttaglist = nbttagcompound.func_150295_c("HandItems", 10);

            for (i = 0; i < this.field_184656_bv.size(); ++i) {
                this.field_184656_bv.set(i, new ItemStack(nbttaglist.func_150305_b(i)));
            }
        }

        if (nbttagcompound.func_150297_b("ArmorDropChances", 9)) {
            nbttaglist = nbttagcompound.func_150295_c("ArmorDropChances", 5);

            for (i = 0; i < nbttaglist.func_74745_c(); ++i) {
                this.field_184655_bs[i] = nbttaglist.func_150308_e(i);
            }
        }

        if (nbttagcompound.func_150297_b("HandDropChances", 9)) {
            nbttaglist = nbttagcompound.func_150295_c("HandDropChances", 5);

            for (i = 0; i < nbttaglist.func_74745_c(); ++i) {
                this.field_82174_bp[i] = nbttaglist.func_150308_e(i);
            }
        }

        this.field_110169_bv = nbttagcompound.func_74767_n("Leashed");
        if (this.field_110169_bv && nbttagcompound.func_150297_b("Leash", 10)) {
            this.field_110170_bx = nbttagcompound.func_74775_l("Leash");
        }

        this.func_184641_n(nbttagcompound.func_74767_n("LeftHanded"));
        if (nbttagcompound.func_150297_b("DeathLootTable", 8)) {
            this.field_184659_bA = new ResourceLocation(nbttagcompound.func_74779_i("DeathLootTable"));
            this.field_184653_bB = nbttagcompound.func_74763_f("DeathLootTableSeed");
        }

        this.func_94061_f(nbttagcompound.func_74767_n("NoAI"));
    }

    @Nullable
    protected ResourceLocation func_184647_J() {
        return null;
    }

    protected void func_184610_a(boolean flag, int i, DamageSource damagesource) {
        ResourceLocation minecraftkey = this.field_184659_bA;

        if (minecraftkey == null) {
            minecraftkey = this.func_184647_J();
        }

        if (minecraftkey != null) {
            LootTable loottable = this.field_70170_p.func_184146_ak().func_186521_a(minecraftkey);

            this.field_184659_bA = null;
            LootTableInfo.a loottableinfo_a = (new LootTableInfo.a((WorldServer) this.field_70170_p)).a((Entity) this).a(damagesource);

            if (flag && this.field_70717_bb != null) {
                loottableinfo_a = loottableinfo_a.a(this.field_70717_bb).a(this.field_70717_bb.func_184817_da());
            }

            List list = loottable.func_186462_a(this.field_184653_bB == 0L ? this.field_70146_Z : new Random(this.field_184653_bB), loottableinfo_a.a());
            Iterator iterator = list.iterator();

            while (iterator.hasNext()) {
                ItemStack itemstack = (ItemStack) iterator.next();

                this.func_70099_a(itemstack, 0.0F);
            }

            this.func_82160_b(flag, i);
        } else {
            super.func_184610_a(flag, i, damagesource);
        }

    }

    public void func_191989_p(float f) {
        this.field_191988_bg = f;
    }

    public void func_70657_f(float f) {
        this.field_70701_bs = f;
    }

    public void func_184646_p(float f) {
        this.field_70702_br = f;
    }

    public void func_70659_e(float f) {
        super.func_70659_e(f);
        this.func_191989_p(f);
    }

    public void func_70636_d() {
        super.func_70636_d();
        this.field_70170_p.field_72984_F.func_76320_a("looting");
        if (!this.field_70170_p.field_72995_K && this.func_98052_bS() && !this.field_70729_aU && this.field_70170_p.func_82736_K().func_82766_b("mobGriefing")) {
            List list = this.field_70170_p.func_72872_a(EntityItem.class, this.func_174813_aQ().func_72314_b(1.0D, 0.0D, 1.0D));
            Iterator iterator = list.iterator();

            while (iterator.hasNext()) {
                EntityItem entityitem = (EntityItem) iterator.next();

                // Paper Start
                if (!entityitem.canMobPickup) {
                    continue;
                }
                // Paper End

                if (!entityitem.field_70128_L && !entityitem.func_92059_d().func_190926_b() && !entityitem.func_174874_s()) {
                    this.func_175445_a(entityitem);
                }
            }
        }

        this.field_70170_p.field_72984_F.func_76319_b();
    }

    protected void func_175445_a(EntityItem entityitem) {
        ItemStack itemstack = entityitem.func_92059_d();
        EntityEquipmentSlot enumitemslot = func_184640_d(itemstack);
        boolean flag = true;
        ItemStack itemstack1 = this.func_184582_a(enumitemslot);

        if (!itemstack1.func_190926_b()) {
            if (enumitemslot.func_188453_a() == EntityEquipmentSlot.Type.HAND) {
                if (itemstack.func_77973_b() instanceof ItemSword && !(itemstack1.func_77973_b() instanceof ItemSword)) {
                    flag = true;
                } else if (itemstack.func_77973_b() instanceof ItemSword && itemstack1.func_77973_b() instanceof ItemSword) {
                    ItemSword itemsword = (ItemSword) itemstack.func_77973_b();
                    ItemSword itemsword1 = (ItemSword) itemstack1.func_77973_b();

                    if (itemsword.func_150931_i() == itemsword1.func_150931_i()) {
                        flag = itemstack.func_77960_j() > itemstack1.func_77960_j() || itemstack.func_77942_o() && !itemstack1.func_77942_o();
                    } else {
                        flag = itemsword.func_150931_i() > itemsword1.func_150931_i();
                    }
                } else if (itemstack.func_77973_b() instanceof ItemBow && itemstack1.func_77973_b() instanceof ItemBow) {
                    flag = itemstack.func_77942_o() && !itemstack1.func_77942_o();
                } else {
                    flag = false;
                }
            } else if (itemstack.func_77973_b() instanceof ItemArmor && !(itemstack1.func_77973_b() instanceof ItemArmor)) {
                flag = true;
            } else if (itemstack.func_77973_b() instanceof ItemArmor && itemstack1.func_77973_b() instanceof ItemArmor && !EnchantmentHelper.func_190938_b(itemstack1)) {
                ItemArmor itemarmor = (ItemArmor) itemstack.func_77973_b();
                ItemArmor itemarmor1 = (ItemArmor) itemstack1.func_77973_b();

                if (itemarmor.field_77879_b == itemarmor1.field_77879_b) {
                    flag = itemstack.func_77960_j() > itemstack1.func_77960_j() || itemstack.func_77942_o() && !itemstack1.func_77942_o();
                } else {
                    flag = itemarmor.field_77879_b > itemarmor1.field_77879_b;
                }
            } else {
                flag = false;
            }
        }

        // CraftBukkit start
        boolean canPickup = flag && this.func_175448_a(itemstack);

        EntityPickupItemEvent entityEvent = new EntityPickupItemEvent((LivingEntity) getBukkitEntity(), (org.bukkit.entity.Item) entityitem.getBukkitEntity(), 0);
        entityEvent.setCancelled(!canPickup);
        this.field_70170_p.getServer().getPluginManager().callEvent(entityEvent);
        canPickup = !entityEvent.isCancelled();
        if (canPickup) {
            // CraftBukkit end
            double d0;

            switch (enumitemslot.func_188453_a()) {
            case HAND:
                d0 = (double) this.field_82174_bp[enumitemslot.func_188454_b()];
                break;

            case ARMOR:
                d0 = (double) this.field_184655_bs[enumitemslot.func_188454_b()];
                break;

            default:
                d0 = 0.0D;
            }

            if (!itemstack1.func_190926_b() && (double) (this.field_70146_Z.nextFloat() - 0.1F) < d0) {
                this.forceDrops = true; // CraftBukkit
                this.func_70099_a(itemstack1, 0.0F);
                this.forceDrops = false; // CraftBukkit
            }

            this.func_184201_a(enumitemslot, itemstack);
            switch (enumitemslot.func_188453_a()) {
            case HAND:
                this.field_82174_bp[enumitemslot.func_188454_b()] = 2.0F;
                break;

            case ARMOR:
                this.field_184655_bs[enumitemslot.func_188454_b()] = 2.0F;
            }

            this.field_82179_bU = true;
            this.func_71001_a(entityitem, itemstack.func_190916_E());
            entityitem.func_70106_y();
        }

    }

    protected boolean func_175448_a(ItemStack itemstack) {
        return true;
    }

    protected boolean func_70692_ba() {
        return true;
    }

    protected void func_70623_bb() {
        if (this.field_82179_bU) {
            this.field_70708_bq = 0;
        } else {
            EntityPlayer entityhuman = this.field_70170_p.func_72890_a(this, -1.0D);

            if (entityhuman != null && entityhuman.affectsSpawning) { // Paper - Affects Spawning API
                double d0 = entityhuman.field_70165_t - this.field_70165_t;
                double d1 = entityhuman.field_70163_u - this.field_70163_u;
                double d2 = entityhuman.field_70161_v - this.field_70161_v;
                double d3 = d0 * d0 + d1 * d1 + d2 * d2;

                if (d3 > field_70170_p.paperConfig.hardDespawnDistance) { // CraftBukkit - remove isTypeNotPersistent() check // Paper - custom despawn distances
                    this.func_70106_y();
                }

                if (this.field_70708_bq > 600 && this.field_70146_Z.nextInt(800) == 0 && d3 > field_70170_p.paperConfig.softDespawnDistance) { // CraftBukkit - remove isTypeNotPersistent() check // Paper - custom despawn distances
                    this.func_70106_y();
                } else if (d3 < field_70170_p.paperConfig.softDespawnDistance) { // Paper - custom despawn distances
                    this.field_70708_bq = 0;
                }
            }

        }
    }

    protected final void func_70626_be() {
        ++this.field_70708_bq;
        this.field_70170_p.field_72984_F.func_76320_a("checkDespawn");
        this.func_70623_bb();
        this.field_70170_p.field_72984_F.func_76319_b();
        // Spigot Start
        if ( this.fromMobSpawner )
        {
            // Paper start - Allow nerfed mobs to jump and float
            if (goalFloat != null) {
                if (goalFloat.validConditions()) goalFloat.update();
                this.func_70683_ar().jumpIfSet();
            }
            // Paper end
            return;
        }
        // Spigot End
        this.field_70170_p.field_72984_F.func_76320_a("sensing");
        this.field_70723_bA.func_75523_a();
        this.field_70170_p.field_72984_F.func_76319_b();
        this.field_70170_p.field_72984_F.func_76320_a("targetSelector");
        this.field_70715_bh.func_75774_a();
        this.field_70170_p.field_72984_F.func_76319_b();
        this.field_70170_p.field_72984_F.func_76320_a("goalSelector");
        this.field_70714_bg.func_75774_a();
        this.field_70170_p.field_72984_F.func_76319_b();
        this.field_70170_p.field_72984_F.func_76320_a("navigation");
        this.field_70699_by.func_75501_e();
        this.field_70170_p.field_72984_F.func_76319_b();
        this.field_70170_p.field_72984_F.func_76320_a("mob tick");
        this.func_70619_bc();
        this.field_70170_p.field_72984_F.func_76319_b();
        if (this.func_184218_aH() && this.func_184187_bx() instanceof EntityLiving) {
            EntityLiving entityinsentient = (EntityLiving) this.func_184187_bx();

            entityinsentient.func_70661_as().func_75484_a(this.func_70661_as().func_75505_d(), 1.5D);
            entityinsentient.func_70605_aq().func_188487_a(this.func_70605_aq());
        }

        this.field_70170_p.field_72984_F.func_76320_a("controls");
        this.field_70170_p.field_72984_F.func_76320_a("move");
        this.field_70765_h.func_75641_c();
        this.field_70170_p.field_72984_F.func_76318_c("look");
        this.field_70749_g.func_75649_a();
        this.field_70170_p.field_72984_F.func_76318_c("jump");
        this.field_70767_i.func_75661_b();
        this.field_70170_p.field_72984_F.func_76319_b();
        this.field_70170_p.field_72984_F.func_76319_b();
    }

    protected void func_70619_bc() {}

    public int func_70646_bf() {
        return 40;
    }

    public int func_184649_cE() {
        return 10;
    }

    public void func_70625_a(Entity entity, float f, float f1) {
        double d0 = entity.field_70165_t - this.field_70165_t;
        double d1 = entity.field_70161_v - this.field_70161_v;
        double d2;

        if (entity instanceof EntityLivingBase) {
            EntityLivingBase entityliving = (EntityLivingBase) entity;

            d2 = entityliving.field_70163_u + (double) entityliving.func_70047_e() - (this.field_70163_u + (double) this.func_70047_e());
        } else {
            d2 = (entity.func_174813_aQ().field_72338_b + entity.func_174813_aQ().field_72337_e) / 2.0D - (this.field_70163_u + (double) this.func_70047_e());
        }

        double d3 = (double) MathHelper.func_76133_a(d0 * d0 + d1 * d1);
        float f2 = (float) (MathHelper.func_181159_b(d1, d0) * 57.2957763671875D) - 90.0F;
        float f3 = (float) (-(MathHelper.func_181159_b(d2, d3) * 57.2957763671875D));

        this.field_70125_A = this.func_70663_b(this.field_70125_A, f3, f1);
        this.field_70177_z = this.func_70663_b(this.field_70177_z, f2, f);
    }

    private float func_70663_b(float f, float f1, float f2) {
        float f3 = MathHelper.func_76142_g(f1 - f);

        if (f3 > f2) {
            f3 = f2;
        }

        if (f3 < -f2) {
            f3 = -f2;
        }

        return f + f3;
    }

    public boolean func_70601_bi() {
        IBlockState iblockdata = this.field_70170_p.func_180495_p((new BlockPos(this)).func_177977_b());

        return iblockdata.func_189884_a((Entity) this);
    }

    public boolean func_70058_J() {
        return !this.field_70170_p.func_72953_d(this.func_174813_aQ()) && this.field_70170_p.func_184144_a(this, this.func_174813_aQ()).isEmpty() && this.field_70170_p.func_72917_a(this.func_174813_aQ(), (Entity) this);
    }

    public int func_70641_bl() {
        return 4;
    }

    public int func_82143_as() {
        if (this.func_70638_az() == null) {
            return 3;
        } else {
            int i = (int) (this.func_110143_aJ() - this.func_110138_aP() * 0.33F);

            i -= (3 - this.field_70170_p.func_175659_aa().func_151525_a()) * 4;
            if (i < 0) {
                i = 0;
            }

            return i + 3;
        }
    }

    public Iterable<ItemStack> func_184214_aD() {
        return this.field_184656_bv;
    }

    public Iterable<ItemStack> func_184193_aE() {
        return this.field_184657_bw;
    }

    public ItemStack func_184582_a(EntityEquipmentSlot enumitemslot) {
        switch (enumitemslot.func_188453_a()) {
        case HAND:
            return (ItemStack) this.field_184656_bv.get(enumitemslot.func_188454_b());

        case ARMOR:
            return (ItemStack) this.field_184657_bw.get(enumitemslot.func_188454_b());

        default:
            return ItemStack.field_190927_a;
        }
    }

    public void func_184201_a(EntityEquipmentSlot enumitemslot, ItemStack itemstack) {
        switch (enumitemslot.func_188453_a()) {
        case HAND:
            this.field_184656_bv.set(enumitemslot.func_188454_b(), itemstack);
            break;

        case ARMOR:
            this.field_184657_bw.set(enumitemslot.func_188454_b(), itemstack);
        }

    }

    protected void func_82160_b(boolean flag, int i) {
        EntityEquipmentSlot[] aenumitemslot = EntityEquipmentSlot.values();
        int j = aenumitemslot.length;

        for (int k = 0; k < j; ++k) {
            EntityEquipmentSlot enumitemslot = aenumitemslot[k];
            ItemStack itemstack = this.func_184582_a(enumitemslot);
            double d0;

            switch (enumitemslot.func_188453_a()) {
            case HAND:
                d0 = (double) this.field_82174_bp[enumitemslot.func_188454_b()];
                break;

            case ARMOR:
                d0 = (double) this.field_184655_bs[enumitemslot.func_188454_b()];
                break;

            default:
                d0 = 0.0D;
            }

            boolean flag1 = d0 > 1.0D;

            if (!itemstack.func_190926_b() && !EnchantmentHelper.func_190939_c(itemstack) && (flag || flag1) && (double) (this.field_70146_Z.nextFloat() - (float) i * 0.01F) < d0) {
                if (!flag1 && itemstack.func_77984_f()) {
                    itemstack.func_77964_b(itemstack.func_77958_k() - this.field_70146_Z.nextInt(1 + this.field_70146_Z.nextInt(Math.max(itemstack.func_77958_k() - 3, 1))));
                }

                this.func_70099_a(itemstack, 0.0F);
            }
        }

    }

    protected void func_180481_a(DifficultyInstance difficultydamagescaler) {
        if (this.field_70146_Z.nextFloat() < 0.15F * difficultydamagescaler.func_180170_c()) {
            int i = this.field_70146_Z.nextInt(2);
            float f = this.field_70170_p.func_175659_aa() == EnumDifficulty.HARD ? 0.1F : 0.25F;

            if (this.field_70146_Z.nextFloat() < 0.095F) {
                ++i;
            }

            if (this.field_70146_Z.nextFloat() < 0.095F) {
                ++i;
            }

            if (this.field_70146_Z.nextFloat() < 0.095F) {
                ++i;
            }

            boolean flag = true;
            EntityEquipmentSlot[] aenumitemslot = EntityEquipmentSlot.values();
            int j = aenumitemslot.length;

            for (int k = 0; k < j; ++k) {
                EntityEquipmentSlot enumitemslot = aenumitemslot[k];

                if (enumitemslot.func_188453_a() == EntityEquipmentSlot.Type.ARMOR) {
                    ItemStack itemstack = this.func_184582_a(enumitemslot);

                    if (!flag && this.field_70146_Z.nextFloat() < f) {
                        break;
                    }

                    flag = false;
                    if (itemstack.func_190926_b()) {
                        Item item = func_184636_a(enumitemslot, i);

                        if (item != null) {
                            this.func_184201_a(enumitemslot, new ItemStack(item));
                        }
                    }
                }
            }
        }

    }

    public static EntityEquipmentSlot func_184640_d(ItemStack itemstack) {
        return itemstack.func_77973_b() != Item.func_150898_a(Blocks.field_150423_aK) && itemstack.func_77973_b() != Items.field_151144_bL ? (itemstack.func_77973_b() instanceof ItemArmor ? ((ItemArmor) itemstack.func_77973_b()).field_77881_a : (itemstack.func_77973_b() == Items.field_185160_cR ? EntityEquipmentSlot.CHEST : (itemstack.func_77973_b() == Items.field_185159_cQ ? EntityEquipmentSlot.OFFHAND : EntityEquipmentSlot.MAINHAND))) : EntityEquipmentSlot.HEAD;
    }

    @Nullable
    public static Item func_184636_a(EntityEquipmentSlot enumitemslot, int i) {
        switch (enumitemslot) {
        case HEAD:
            if (i == 0) {
                return Items.field_151024_Q;
            } else if (i == 1) {
                return Items.field_151169_ag;
            } else if (i == 2) {
                return Items.field_151020_U;
            } else if (i == 3) {
                return Items.field_151028_Y;
            } else if (i == 4) {
                return Items.field_151161_ac;
            }

        case CHEST:
            if (i == 0) {
                return Items.field_151027_R;
            } else if (i == 1) {
                return Items.field_151171_ah;
            } else if (i == 2) {
                return Items.field_151023_V;
            } else if (i == 3) {
                return Items.field_151030_Z;
            } else if (i == 4) {
                return Items.field_151163_ad;
            }

        case LEGS:
            if (i == 0) {
                return Items.field_151026_S;
            } else if (i == 1) {
                return Items.field_151149_ai;
            } else if (i == 2) {
                return Items.field_151022_W;
            } else if (i == 3) {
                return Items.field_151165_aa;
            } else if (i == 4) {
                return Items.field_151173_ae;
            }

        case FEET:
            if (i == 0) {
                return Items.field_151021_T;
            } else if (i == 1) {
                return Items.field_151151_aj;
            } else if (i == 2) {
                return Items.field_151029_X;
            } else if (i == 3) {
                return Items.field_151167_ab;
            } else if (i == 4) {
                return Items.field_151175_af;
            }

        default:
            return null;
        }
    }

    protected void func_180483_b(DifficultyInstance difficultydamagescaler) {
        float f = difficultydamagescaler.func_180170_c();

        if (!this.func_184614_ca().func_190926_b() && this.field_70146_Z.nextFloat() < 0.25F * f) {
            this.func_184201_a(EntityEquipmentSlot.MAINHAND, EnchantmentHelper.func_77504_a(this.field_70146_Z, this.func_184614_ca(), (int) (5.0F + f * (float) this.field_70146_Z.nextInt(18)), false));
        }

        EntityEquipmentSlot[] aenumitemslot = EntityEquipmentSlot.values();
        int i = aenumitemslot.length;

        for (int j = 0; j < i; ++j) {
            EntityEquipmentSlot enumitemslot = aenumitemslot[j];

            if (enumitemslot.func_188453_a() == EntityEquipmentSlot.Type.ARMOR) {
                ItemStack itemstack = this.func_184582_a(enumitemslot);

                if (!itemstack.func_190926_b() && this.field_70146_Z.nextFloat() < 0.5F * f) {
                    this.func_184201_a(enumitemslot, EnchantmentHelper.func_77504_a(this.field_70146_Z, itemstack, (int) (5.0F + f * (float) this.field_70146_Z.nextInt(18)), false));
                }
            }
        }

    }

    @Nullable
    public IEntityLivingData func_180482_a(DifficultyInstance difficultydamagescaler, @Nullable IEntityLivingData groupdataentity) {
        this.func_110148_a(SharedMonsterAttributes.field_111265_b).func_111121_a(new AttributeModifier("Random spawn bonus", this.field_70146_Z.nextGaussian() * 0.05D, 1));
        if (this.field_70146_Z.nextFloat() < 0.05F) {
            this.func_184641_n(true);
        } else {
            this.func_184641_n(false);
        }

        return groupdataentity;
    }

    public boolean func_82171_bF() {
        return false;
    }

    public void func_110163_bv() {
        this.field_82179_bU = true;
    }

    public void func_184642_a(EntityEquipmentSlot enumitemslot, float f) {
        switch (enumitemslot.func_188453_a()) {
        case HAND:
            this.field_82174_bp[enumitemslot.func_188454_b()] = f;
            break;

        case ARMOR:
            this.field_184655_bs[enumitemslot.func_188454_b()] = f;
        }

    }

    public boolean func_98052_bS() {
        return this.canPickUpLoot;
    }

    public void func_98053_h(boolean flag) {
        this.canPickUpLoot = flag;
    }

    public boolean func_104002_bU() {
        return this.field_82179_bU;
    }

    public final boolean func_184230_a(EntityPlayer entityhuman, EnumHand enumhand) {
        if (this.func_110167_bD() && this.func_110166_bE() == entityhuman) {
            // CraftBukkit start - fire PlayerUnleashEntityEvent
            if (CraftEventFactory.callPlayerUnleashEntityEvent(this, entityhuman).isCancelled()) {
                ((EntityPlayerMP) entityhuman).field_71135_a.func_147359_a(new SPacketEntityAttach(this, this.func_110166_bE()));
                return false;
            }
            // CraftBukkit end
            this.func_110160_i(true, !entityhuman.field_71075_bZ.field_75098_d);
            return true;
        } else {
            ItemStack itemstack = entityhuman.func_184586_b(enumhand);

            if (itemstack.func_77973_b() == Items.field_151058_ca && this.func_184652_a(entityhuman)) {
                // CraftBukkit start - fire PlayerLeashEntityEvent
                if (CraftEventFactory.callPlayerLeashEntityEvent(this, entityhuman, entityhuman).isCancelled()) {
                    ((EntityPlayerMP) entityhuman).field_71135_a.func_147359_a(new SPacketEntityAttach(this, this.func_110166_bE()));
                    return false;
                }
                // CraftBukkit end
                this.func_110162_b(entityhuman, true);
                itemstack.func_190918_g(1);
                return true;
            } else {
                return this.func_184645_a(entityhuman, enumhand) ? true : super.func_184230_a(entityhuman, enumhand);
            }
        }
    }

    protected boolean func_184645_a(EntityPlayer entityhuman, EnumHand enumhand) {
        return false;
    }

    protected void func_110159_bB() {
        if (this.field_110170_bx != null) {
            this.func_110165_bF();
        }

        if (this.field_110169_bv) {
            if (!this.func_70089_S()) {
                this.field_70170_p.getServer().getPluginManager().callEvent(new EntityUnleashEvent(this.getBukkitEntity(), UnleashReason.PLAYER_UNLEASH)); // CraftBukkit
                this.func_110160_i(true, true);
            }

            if (this.field_110168_bw == null || this.field_110168_bw.field_70128_L) {
                this.field_70170_p.getServer().getPluginManager().callEvent(new EntityUnleashEvent(this.getBukkitEntity(), UnleashReason.HOLDER_GONE)); // CraftBukkit
                this.func_110160_i(true, true);
            }
        }
    }

    public void func_110160_i(boolean flag, boolean flag1) {
        if (this.field_110169_bv) {
            this.field_110169_bv = false;
            this.field_110168_bw = null;
            if (!this.field_70170_p.field_72995_K && flag1) {
                this.forceDrops = true; // CraftBukkit
                this.func_145779_a(Items.field_151058_ca, 1);
                this.forceDrops = false; // CraftBukkit
            }

            if (!this.field_70170_p.field_72995_K && flag && this.field_70170_p instanceof WorldServer) {
                ((WorldServer) this.field_70170_p).func_73039_n().func_151247_a((Entity) this, (Packet) (new SPacketEntityAttach(this, (Entity) null)));
            }
        }

    }

    public boolean func_184652_a(EntityPlayer entityhuman) {
        return !this.func_110167_bD() && !(this instanceof IMob);
    }

    public boolean func_110167_bD() {
        return this.field_110169_bv;
    }

    public Entity func_110166_bE() {
        return this.field_110168_bw;
    }

    public void func_110162_b(Entity entity, boolean flag) {
        this.field_110169_bv = true;
        this.field_110168_bw = entity;
        if (!this.field_70170_p.field_72995_K && flag && this.field_70170_p instanceof WorldServer) {
            ((WorldServer) this.field_70170_p).func_73039_n().func_151247_a((Entity) this, (Packet) (new SPacketEntityAttach(this, this.field_110168_bw)));
        }

        if (this.func_184218_aH()) {
            this.func_184210_p();
        }

    }

    public boolean func_184205_a(Entity entity, boolean flag) {
        boolean flag1 = super.func_184205_a(entity, flag);

        if (flag1 && this.func_110167_bD()) {
            this.func_110160_i(true, true);
        }

        return flag1;
    }

    private void func_110165_bF() {
        if (this.field_110169_bv && this.field_110170_bx != null) {
            if (this.field_110170_bx.func_186855_b("UUID")) {
                UUID uuid = this.field_110170_bx.func_186857_a("UUID");
                List list = this.field_70170_p.func_72872_a(EntityLivingBase.class, this.func_174813_aQ().func_186662_g(10.0D));
                Iterator iterator = list.iterator();

                while (iterator.hasNext()) {
                    EntityLivingBase entityliving = (EntityLivingBase) iterator.next();

                    if (entityliving.func_110124_au().equals(uuid)) {
                        this.func_110162_b(entityliving, true);
                        break;
                    }
                }
            } else if (this.field_110170_bx.func_150297_b("X", 99) && this.field_110170_bx.func_150297_b("Y", 99) && this.field_110170_bx.func_150297_b("Z", 99)) {
                BlockPos blockposition = new BlockPos(this.field_110170_bx.func_74762_e("X"), this.field_110170_bx.func_74762_e("Y"), this.field_110170_bx.func_74762_e("Z"));
                EntityLeashKnot entityleash = EntityLeashKnot.func_174863_b(this.field_70170_p, blockposition);

                if (entityleash == null) {
                    entityleash = EntityLeashKnot.func_174862_a(this.field_70170_p, blockposition);
                }

                this.func_110162_b(entityleash, true);
            } else {
                this.field_70170_p.getServer().getPluginManager().callEvent(new EntityUnleashEvent(this.getBukkitEntity(), UnleashReason.UNKNOWN)); // CraftBukkit
                this.func_110160_i(false, true);
            }
        }

        this.field_110170_bx = null;
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

        if (!itemstack.func_190926_b() && !func_184648_b(enumitemslot, itemstack) && enumitemslot != EntityEquipmentSlot.HEAD) {
            return false;
        } else {
            this.func_184201_a(enumitemslot, itemstack);
            return true;
        }
    }

    public boolean func_184186_bw() {
        return this.func_82171_bF() && super.func_184186_bw();
    }

    public static boolean func_184648_b(EntityEquipmentSlot enumitemslot, ItemStack itemstack) {
        EntityEquipmentSlot enumitemslot1 = func_184640_d(itemstack);

        return enumitemslot1 == enumitemslot || enumitemslot1 == EntityEquipmentSlot.MAINHAND && enumitemslot == EntityEquipmentSlot.OFFHAND || enumitemslot1 == EntityEquipmentSlot.OFFHAND && enumitemslot == EntityEquipmentSlot.MAINHAND;
    }

    public boolean func_70613_aW() {
        return super.func_70613_aW() && !this.func_175446_cd();
    }

    public void func_94061_f(boolean flag) {
        byte b0 = ((Byte) this.field_70180_af.func_187225_a(EntityLiving.field_184654_a)).byteValue();

        this.field_70180_af.func_187227_b(EntityLiving.field_184654_a, Byte.valueOf(flag ? (byte) (b0 | 1) : (byte) (b0 & -2)));
    }

    public void func_184641_n(boolean flag) {
        byte b0 = ((Byte) this.field_70180_af.func_187225_a(EntityLiving.field_184654_a)).byteValue();

        this.field_70180_af.func_187227_b(EntityLiving.field_184654_a, Byte.valueOf(flag ? (byte) (b0 | 2) : (byte) (b0 & -3)));
    }

    public boolean func_175446_cd() {
        return (((Byte) this.field_70180_af.func_187225_a(EntityLiving.field_184654_a)).byteValue() & 1) != 0;
    }

    public boolean func_184638_cS() {
        return (((Byte) this.field_70180_af.func_187225_a(EntityLiving.field_184654_a)).byteValue() & 2) != 0;
    }

    public EnumHandSide func_184591_cq() {
        return this.func_184638_cS() ? EnumHandSide.LEFT : EnumHandSide.RIGHT;
    }

    public static enum SpawnPlacementType {

        ON_GROUND, IN_AIR, IN_WATER;

        private SpawnPlacementType() {}
    }
}
