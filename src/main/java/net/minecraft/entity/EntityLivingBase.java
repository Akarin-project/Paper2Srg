package net.minecraft.entity;

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent;
import com.google.common.base.Objects;
import com.google.common.collect.Maps;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import javax.annotation.Nullable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLadder;
import net.minecraft.block.BlockTrapDoor;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentFrostWalker;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.entity.ai.attributes.AttributeMap;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityFlying;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemElytra;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.Packet;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.server.SPacketAnimation;
import net.minecraft.network.play.server.SPacketCollectItem;
import net.minecraft.network.play.server.SPacketEntityEquipment;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionUtils;
import net.minecraft.stats.StatList;
import net.minecraft.util.CombatRules;
import net.minecraft.util.CombatTracker;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import org.bukkit.Location;
import org.bukkit.craftbukkit.attribute.CraftAttributeMap;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageModifier;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityResurrectEvent;
import org.bukkit.event.entity.EntityTeleportEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;

// CraftBukkit start
import java.util.ArrayList;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import org.bukkit.Location;
import org.bukkit.craftbukkit.attribute.CraftAttributeMap;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageModifier;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityResurrectEvent;
import org.bukkit.event.entity.EntityTeleportEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
// CraftBukkit end

import co.aikar.timings.MinecraftTimings; // Paper

public abstract class EntityLivingBase extends Entity {

    private static final Logger field_190632_a = LogManager.getLogger();
    private static final UUID field_110156_b = UUID.fromString("662A6B8D-DA3E-4C1C-8813-96EA6097278D");
    private static final AttributeModifier field_110157_c = (new AttributeModifier(EntityLivingBase.field_110156_b, "Sprinting speed boost", 0.30000001192092896D, 2)).func_111168_a(false);
    protected static final DataParameter<Byte> field_184621_as = EntityDataManager.func_187226_a(EntityLivingBase.class, DataSerializers.field_187191_a);
    public static final DataParameter<Float> field_184632_c = EntityDataManager.func_187226_a(EntityLivingBase.class, DataSerializers.field_187193_c);
    private static final DataParameter<Integer> field_184633_f = EntityDataManager.func_187226_a(EntityLivingBase.class, DataSerializers.field_187192_b);
    private static final DataParameter<Boolean> field_184634_g = EntityDataManager.func_187226_a(EntityLivingBase.class, DataSerializers.field_187198_h);
    private static final DataParameter<Integer> field_184635_h = EntityDataManager.func_187226_a(EntityLivingBase.class, DataSerializers.field_187192_b);
    private AbstractAttributeMap field_110155_d;
    public CombatTracker field_94063_bt = new CombatTracker(this);
    public final Map<Potion, PotionEffect> field_70713_bf = Maps.newHashMap();
    private final NonNullList<ItemStack> field_184630_bs;
    private final NonNullList<ItemStack> field_184631_bt;
    public boolean field_82175_bq;
    public EnumHand field_184622_au;
    public int field_110158_av;
    public int field_70720_be;
    public int field_70737_aN;
    public int field_70738_aO;
    public float field_70739_aP;
    public int field_70725_aQ;
    public float field_70732_aI;
    public float field_70733_aJ;
    protected int field_184617_aD;
    public float field_184618_aE;
    public float field_70721_aZ;
    public float field_184619_aG;
    public int field_70771_an;
    public float field_70727_aS;
    public float field_70726_aT;
    public float field_70769_ao;
    public float field_70770_ap;
    public float field_70761_aq;
    public float field_70760_ar;
    public float field_70759_as;
    public float field_70758_at;
    public float field_70747_aH;
    public EntityPlayer field_70717_bb;
    public int field_70718_bc; // Paper - public
    protected boolean field_70729_aU;
    protected int field_70708_bq;
    protected float field_70768_au;
    protected float field_110154_aX;
    protected float field_70764_aw;
    protected float field_70763_ax;
    protected float field_70741_aB;
    protected int field_70744_aE;
    public float field_110153_bc;
    protected boolean field_70703_bu;
    public float field_70702_br;
    public float field_70701_bs;
    public float field_191988_bg;
    public float field_70704_bt;
    protected int field_70716_bi;
    protected double field_184623_bh;
    protected double field_184624_bi;
    protected double field_184625_bj;
    protected double field_184626_bk;
    protected double field_70709_bj;
    public boolean field_70752_e;
    public EntityLivingBase field_70755_b;
    public int field_70756_c;
    private EntityLivingBase field_110150_bn;
    private int field_142016_bo;
    private float field_70746_aG;
    private int field_70773_bE;
    private float field_110151_bq;
    protected ItemStack field_184627_bm;
    protected int field_184628_bn;
    protected int field_184629_bo;
    private BlockPos field_184620_bC;
    private DamageSource field_189750_bF;
    private long field_189751_bG;
    // CraftBukkit start
    public int expToDrop;
    public int maxAirTicks = 300;
    boolean forceDrops;
    ArrayList<org.bukkit.inventory.ItemStack> drops = new ArrayList<org.bukkit.inventory.ItemStack>();
    public org.bukkit.craftbukkit.attribute.CraftAttributeMap craftAttributes;
    public boolean collides = true;
    public boolean canPickUpLoot;

    @Override
    public float getBukkitYaw() {
        return func_70079_am();
    }
    // CraftBukkit end
    // Spigot start
    public void inactiveTick()
    {
        super.inactiveTick();
        ++this.field_70708_bq; // Above all the floats
    }
    // Spigot end

    public void func_174812_G() {
        this.func_70097_a(DamageSource.field_76380_i, Float.MAX_VALUE);
    }

    public EntityLivingBase(World world) {
        super(world);
        this.field_184630_bs = NonNullList.func_191197_a(2, ItemStack.field_190927_a);
        this.field_184631_bt = NonNullList.func_191197_a(4, ItemStack.field_190927_a);
        this.field_70771_an = 20;
        this.field_70747_aH = 0.02F;
        this.field_70752_e = true;
        this.field_184627_bm = ItemStack.field_190927_a;
        this.func_110147_ax();
        // CraftBukkit - setHealth(getMaxHealth()) inlined and simplified to skip the instanceof check for EntityPlayer, as getBukkitEntity() is not initialized in constructor
        this.field_70180_af.func_187227_b(EntityLivingBase.field_184632_c, (float) this.func_110148_a(SharedMonsterAttributes.field_111267_a).func_111126_e());
        this.field_70156_m = true;
        this.field_70770_ap = (float) ((Math.random() + 1.0D) * 0.009999999776482582D);
        this.func_70107_b(this.field_70165_t, this.field_70163_u, this.field_70161_v);
        this.field_70769_ao = (float) Math.random() * 12398.0F;
        this.field_70177_z = (float) (Math.random() * 6.2831854820251465D);
        this.field_70759_as = this.field_70177_z;
        this.field_70138_W = 0.6F;
    }

    protected void func_70088_a() {
        this.field_70180_af.func_187214_a(EntityLivingBase.field_184621_as, Byte.valueOf((byte) 0));
        this.field_70180_af.func_187214_a(EntityLivingBase.field_184633_f, Integer.valueOf(0));
        this.field_70180_af.func_187214_a(EntityLivingBase.field_184634_g, Boolean.valueOf(false));
        this.field_70180_af.func_187214_a(EntityLivingBase.field_184635_h, Integer.valueOf(0));
        this.field_70180_af.func_187214_a(EntityLivingBase.field_184632_c, Float.valueOf(1.0F));
    }

    protected void func_110147_ax() {
        this.func_110140_aT().func_111150_b(SharedMonsterAttributes.field_111267_a);
        this.func_110140_aT().func_111150_b(SharedMonsterAttributes.field_111266_c);
        this.func_110140_aT().func_111150_b(SharedMonsterAttributes.field_111263_d);
        this.func_110140_aT().func_111150_b(SharedMonsterAttributes.field_188791_g);
        this.func_110140_aT().func_111150_b(SharedMonsterAttributes.field_189429_h);
    }

    protected void func_184231_a(double d0, boolean flag, IBlockState iblockdata, BlockPos blockposition) {
        if (!this.func_70090_H()) {
            this.func_70072_I();
        }

        if (!this.field_70170_p.field_72995_K && this.field_70143_R > 3.0F && flag) {
            float f = (float) MathHelper.func_76123_f(this.field_70143_R - 3.0F);

            if (iblockdata.func_185904_a() != Material.field_151579_a) {
                double d1 = Math.min((double) (0.2F + f / 15.0F), 2.5D);
                int i = (int) (150.0D * d1);

                // CraftBukkit start - visiblity api
                if (this instanceof EntityPlayerMP) {
                    ((WorldServer) this.field_70170_p).sendParticles((EntityPlayerMP) this, EnumParticleTypes.BLOCK_DUST, false, this.field_70165_t, this.field_70163_u, this.field_70161_v, i, 0.0D, 0.0D, 0.0D, 0.15000000596046448D, new int[] { Block.func_176210_f(iblockdata)});
                } else {
                    ((WorldServer) this.field_70170_p).func_175739_a(EnumParticleTypes.BLOCK_DUST, this.field_70165_t, this.field_70163_u, this.field_70161_v, i, 0.0D, 0.0D, 0.0D, 0.15000000596046448D, new int[] { Block.func_176210_f(iblockdata)});
                }
                // CraftBukkit end
            }
        }

        super.func_184231_a(d0, flag, iblockdata, blockposition);
    }

    public boolean canBreatheUnderwater() { return this.func_70648_aU(); } // Paper - OBFHELPER
    public boolean func_70648_aU() {
        return false;
    }

    public void func_70030_z() {
        this.field_70732_aI = this.field_70733_aJ;
        super.func_70030_z();
        this.field_70170_p.field_72984_F.func_76320_a("livingEntityBaseTick");
        boolean flag = this instanceof EntityPlayer;

        if (this.func_70089_S()) {
            if (this.func_70094_T()) {
                this.func_70097_a(DamageSource.field_76368_d, 1.0F);
            } else if (flag && !this.field_70170_p.func_175723_af().func_177743_a(this.func_174813_aQ())) {
                double d0 = this.field_70170_p.func_175723_af().func_177745_a((Entity) this) + this.field_70170_p.func_175723_af().func_177742_m();

                if (d0 < 0.0D) {
                    double d1 = this.field_70170_p.func_175723_af().func_177727_n();

                    if (d1 > 0.0D) {
                        this.func_70097_a(DamageSource.field_76368_d, (float) Math.max(1, MathHelper.func_76128_c(-d0 * d1)));
                    }
                }
            }
        }

        if (this.func_70045_F() || this.field_70170_p.field_72995_K) {
            this.func_70066_B();
        }

        boolean flag1 = flag && ((EntityPlayer) this).field_71075_bZ.field_75102_a;

        if (this.func_70089_S()) {
            if (this.func_70055_a(Material.field_151586_h)) {
                if (!this.canBreatheUnderwater() && !this.func_70644_a(MobEffects.field_76427_o) && !flag1) {
                    this.func_70050_g(this.func_70682_h(this.func_70086_ai()));
                    if (this.func_70086_ai() == -20) {
                        this.func_70050_g(0);

                        for (int i = 0; i < 8; ++i) {
                            float f = this.field_70146_Z.nextFloat() - this.field_70146_Z.nextFloat();
                            float f1 = this.field_70146_Z.nextFloat() - this.field_70146_Z.nextFloat();
                            float f2 = this.field_70146_Z.nextFloat() - this.field_70146_Z.nextFloat();

                            this.field_70170_p.func_175688_a(EnumParticleTypes.WATER_BUBBLE, this.field_70165_t + (double) f, this.field_70163_u + (double) f1, this.field_70161_v + (double) f2, this.field_70159_w, this.field_70181_x, this.field_70179_y, new int[0]);
                        }

                        this.func_70097_a(DamageSource.field_76369_e, 2.0F);
                    }
                }

                if (!this.field_70170_p.field_72995_K && this.func_184218_aH() && this.func_184187_bx() instanceof EntityLivingBase) {
                    this.func_184210_p();
                }
            } else {
                // CraftBukkit start - Only set if needed to work around a DataWatcher inefficiency
                if (this.func_70086_ai() != 300) {
                    this.func_70050_g(maxAirTicks);
                }
                // CraftBukkit end
            }

            if (!this.field_70170_p.field_72995_K) {
                BlockPos blockposition = new BlockPos(this);

                if (!Objects.equal(this.field_184620_bC, blockposition)) {
                    this.field_184620_bC = blockposition;
                    this.func_184594_b(blockposition);
                }
            }
        }

        if (this.func_70089_S() && this.func_70026_G()) {
            this.func_70066_B();
        }

        this.field_70727_aS = this.field_70726_aT;
        if (this.field_70737_aN > 0) {
            --this.field_70737_aN;
        }

        if (this.field_70172_ad > 0 && !(this instanceof EntityPlayerMP)) {
            --this.field_70172_ad;
        }

        if (this.func_110143_aJ() <= 0.0F) {
            this.func_70609_aI();
        }

        if (this.field_70718_bc > 0) {
            --this.field_70718_bc;
        } else {
            this.field_70717_bb = null;
        }

        if (this.field_110150_bn != null && !this.field_110150_bn.func_70089_S()) {
            this.field_110150_bn = null;
        }

        if (this.field_70755_b != null) {
            if (!this.field_70755_b.func_70089_S()) {
                this.func_70604_c((EntityLivingBase) null);
            } else if (this.field_70173_aa - this.field_70756_c > 100) {
                this.func_70604_c((EntityLivingBase) null);
            }
        }

        this.func_70679_bo();
        this.field_70763_ax = this.field_70764_aw;
        this.field_70760_ar = this.field_70761_aq;
        this.field_70758_at = this.field_70759_as;
        this.field_70126_B = this.field_70177_z;
        this.field_70127_C = this.field_70125_A;
        this.field_70170_p.field_72984_F.func_76319_b();
    }

    // CraftBukkit start
    public int getExpReward() {
        int exp = this.func_70693_a(this.field_70717_bb);

        if (!this.field_70170_p.field_72995_K && (this.field_70718_bc > 0 || this.func_70684_aJ()) && this.func_146066_aG() && this.field_70170_p.func_82736_K().func_82766_b("doMobLoot")) {
            return exp;
        } else {
            return 0;
        }
    }
    // CraftBukkit end

    protected void func_184594_b(BlockPos blockposition) {
        int i = EnchantmentHelper.func_185284_a(Enchantments.field_185301_j, this);

        if (i > 0) {
            EnchantmentFrostWalker.func_185266_a(this, this.field_70170_p, blockposition, i);
        }

    }

    public boolean func_70631_g_() {
        return false;
    }

    protected void func_70609_aI() {
        ++this.field_70725_aQ;
        if (this.field_70725_aQ >= 20 && !this.field_70128_L) { // CraftBukkit - (this.deathTicks == 20) -> (this.deathTicks >= 20 && !this.dead)
            int i;

            // CraftBukkit start - Update getExpReward() above if the removed if() changes!
            i = this.expToDrop;
            while (i > 0) {
                int j = EntityXPOrb.func_70527_a(i);

                i -= j;
                EntityLivingBase attacker = field_70717_bb != null ? field_70717_bb : field_70755_b; // Paper
                this.field_70170_p.func_72838_d(new EntityXPOrb(this.field_70170_p, this.field_70165_t, this.field_70163_u, this.field_70161_v, j, this instanceof EntityPlayerMP ? org.bukkit.entity.ExperienceOrb.SpawnReason.PLAYER_DEATH : org.bukkit.entity.ExperienceOrb.SpawnReason.ENTITY_DEATH, attacker, this)); // Paper
            }
            this.expToDrop = 0;
            // CraftBukkit end

            this.func_70106_y();

            for (i = 0; i < 20; ++i) {
                double d0 = this.field_70146_Z.nextGaussian() * 0.02D;
                double d1 = this.field_70146_Z.nextGaussian() * 0.02D;
                double d2 = this.field_70146_Z.nextGaussian() * 0.02D;

                this.field_70170_p.func_175688_a(EnumParticleTypes.EXPLOSION_NORMAL, this.field_70165_t + (double) (this.field_70146_Z.nextFloat() * this.field_70130_N * 2.0F) - (double) this.field_70130_N, this.field_70163_u + (double) (this.field_70146_Z.nextFloat() * this.field_70131_O), this.field_70161_v + (double) (this.field_70146_Z.nextFloat() * this.field_70130_N * 2.0F) - (double) this.field_70130_N, d0, d1, d2, new int[0]);
            }
        }

    }

    protected boolean func_146066_aG() {
        return !this.func_70631_g_();
    }

    protected int func_70682_h(int i) {
        int j = EnchantmentHelper.func_185292_c(this);

        return j > 0 && this.field_70146_Z.nextInt(j + 1) > 0 ? i : i - 1;
    }

    protected int func_70693_a(EntityPlayer entityhuman) {
        return 0;
    }

    protected boolean func_70684_aJ() {
        return false;
    }

    public Random func_70681_au() {
        return this.field_70146_Z;
    }

    @Nullable
    public EntityLivingBase func_70643_av() {
        return this.field_70755_b;
    }

    public int func_142015_aE() {
        return this.field_70756_c;
    }

    public void func_70604_c(@Nullable EntityLivingBase entityliving) {
        this.field_70755_b = entityliving;
        this.field_70756_c = this.field_70173_aa;
    }

    public EntityLivingBase func_110144_aD() {
        return this.field_110150_bn;
    }

    public int func_142013_aG() {
        return this.field_142016_bo;
    }

    public void func_130011_c(Entity entity) {
        if (entity instanceof EntityLivingBase) {
            this.field_110150_bn = (EntityLivingBase) entity;
        } else {
            this.field_110150_bn = null;
        }

        this.field_142016_bo = this.field_70173_aa;
    }

    public int func_70654_ax() {
        return this.field_70708_bq;
    }

    protected void func_184606_a_(ItemStack itemstack) {
        if (!itemstack.func_190926_b()) {
            SoundEvent soundeffect = SoundEvents.field_187719_p;
            Item item = itemstack.func_77973_b();

            if (item instanceof ItemArmor) {
                soundeffect = ((ItemArmor) item).func_82812_d().func_185017_b();
            } else if (item == Items.field_185160_cR) {
                soundeffect = SoundEvents.field_191258_p;
            }

            this.func_184185_a(soundeffect, 1.0F, 1.0F);
        }
    }

    public void func_70014_b(NBTTagCompound nbttagcompound) {
        nbttagcompound.func_74776_a("Health", this.func_110143_aJ());
        nbttagcompound.func_74777_a("HurtTime", (short) this.field_70737_aN);
        nbttagcompound.func_74768_a("HurtByTimestamp", this.field_70756_c);
        nbttagcompound.func_74777_a("DeathTime", (short) this.field_70725_aQ);
        nbttagcompound.func_74776_a("AbsorptionAmount", this.func_110139_bj());
        EntityEquipmentSlot[] aenumitemslot = EntityEquipmentSlot.values();
        int i = aenumitemslot.length;

        int j;
        EntityEquipmentSlot enumitemslot;
        ItemStack itemstack;

        for (j = 0; j < i; ++j) {
            enumitemslot = aenumitemslot[j];
            itemstack = this.func_184582_a(enumitemslot);
            if (!itemstack.func_190926_b()) {
                this.func_110140_aT().func_111148_a(itemstack.func_111283_C(enumitemslot));
            }
        }

        nbttagcompound.func_74782_a("Attributes", SharedMonsterAttributes.func_111257_a(this.func_110140_aT()));
        aenumitemslot = EntityEquipmentSlot.values();
        i = aenumitemslot.length;

        for (j = 0; j < i; ++j) {
            enumitemslot = aenumitemslot[j];
            itemstack = this.func_184582_a(enumitemslot);
            if (!itemstack.func_190926_b()) {
                this.func_110140_aT().func_111147_b(itemstack.func_111283_C(enumitemslot));
            }
        }

        if (!this.field_70713_bf.isEmpty()) {
            NBTTagList nbttaglist = new NBTTagList();
            Iterator iterator = this.field_70713_bf.values().iterator();

            while (iterator.hasNext()) {
                PotionEffect mobeffect = (PotionEffect) iterator.next();

                nbttaglist.func_74742_a(mobeffect.func_82719_a(new NBTTagCompound()));
            }

            nbttagcompound.func_74782_a("ActiveEffects", nbttaglist);
        }

        nbttagcompound.func_74757_a("FallFlying", this.func_184613_cA());
    }

    public void func_70037_a(NBTTagCompound nbttagcompound) {
        // Paper start - jvm keeps optimizing the setter
        float absorptionAmount = nbttagcompound.func_74760_g("AbsorptionAmount");
        if (Float.isNaN(absorptionAmount)) {
            absorptionAmount = 0;
        }
        this.func_110149_m(absorptionAmount);
        // Paper end
        if (nbttagcompound.func_150297_b("Attributes", 9) && this.field_70170_p != null && !this.field_70170_p.field_72995_K) {
            SharedMonsterAttributes.func_151475_a(this.func_110140_aT(), nbttagcompound.func_150295_c("Attributes", 10));
        }

        if (nbttagcompound.func_150297_b("ActiveEffects", 9)) {
            NBTTagList nbttaglist = nbttagcompound.func_150295_c("ActiveEffects", 10);

            for (int i = 0; i < nbttaglist.func_74745_c(); ++i) {
                NBTTagCompound nbttagcompound1 = nbttaglist.func_150305_b(i);
                PotionEffect mobeffect = PotionEffect.func_82722_b(nbttagcompound1);

                if (mobeffect != null) {
                    this.field_70713_bf.put(mobeffect.func_188419_a(), mobeffect);
                }
            }
        }

        // CraftBukkit start
        if (nbttagcompound.func_74764_b("Bukkit.MaxHealth")) {
            NBTBase nbtbase = nbttagcompound.func_74781_a("Bukkit.MaxHealth");
            if (nbtbase.func_74732_a() == 5) {
                this.func_110148_a(SharedMonsterAttributes.field_111267_a).func_111128_a(((NBTTagFloat) nbtbase).func_150286_g());
            } else if (nbtbase.func_74732_a() == 3) {
                this.func_110148_a(SharedMonsterAttributes.field_111267_a).func_111128_a(((NBTTagInt) nbtbase).func_150286_g());
            }
        }
        // CraftBukkit end

        if (nbttagcompound.func_150297_b("Health", 99)) {
            this.func_70606_j(nbttagcompound.func_74760_g("Health"));
        }

        this.field_70737_aN = nbttagcompound.func_74765_d("HurtTime");
        this.field_70725_aQ = nbttagcompound.func_74765_d("DeathTime");
        this.field_70756_c = nbttagcompound.func_74762_e("HurtByTimestamp");
        if (nbttagcompound.func_150297_b("Team", 8)) {
            String s = nbttagcompound.func_74779_i("Team");
            boolean flag = this.field_70170_p.func_96441_U().func_151392_a(this.func_189512_bd(), s);

            if (!flag) {
                EntityLivingBase.field_190632_a.warn("Unable to add mob to team \"" + s + "\" (that team probably doesn\'t exist)");
            }
        }

        if (nbttagcompound.func_74767_n("FallFlying")) {
            this.func_70052_a(7, true);
        }

    }

    // CraftBukkit start
    private boolean isTickingEffects = false;
    private List<Object> effectsToProcess = Lists.newArrayList();
    // CraftBukkit end

    protected void func_70679_bo() {
        Iterator iterator = this.field_70713_bf.keySet().iterator();

        isTickingEffects = true; // CraftBukkit
        try {
            while (iterator.hasNext()) {
                Potion mobeffectlist = (Potion) iterator.next();
                PotionEffect mobeffect = (PotionEffect) this.field_70713_bf.get(mobeffectlist);

                if (!mobeffect.func_76455_a(this)) {
                    if (!this.field_70170_p.field_72995_K) {
                        iterator.remove();
                        this.func_70688_c(mobeffect);
                    }
                } else if (mobeffect.func_76459_b() % 600 == 0) {
                    this.func_70695_b(mobeffect, false);
                }
            }
        } catch (ConcurrentModificationException concurrentmodificationexception) {
            ;
        }
        // CraftBukkit start
        isTickingEffects = false;
        for (Object e : effectsToProcess) {
            if (e instanceof PotionEffect) {
                func_70690_d((PotionEffect) e);
            } else {
                func_184589_d((Potion) e);
            }
        }
        effectsToProcess.clear();
        // CraftBukkit end

        if (this.field_70752_e) {
            if (!this.field_70170_p.field_72995_K) {
                this.func_175135_B();
            }

            this.field_70752_e = false;
        }

        int i = ((Integer) this.field_70180_af.func_187225_a(EntityLivingBase.field_184633_f)).intValue();
        boolean flag = ((Boolean) this.field_70180_af.func_187225_a(EntityLivingBase.field_184634_g)).booleanValue();

        if (i > 0) {
            boolean flag1;

            if (this.func_82150_aj()) {
                flag1 = this.field_70146_Z.nextInt(15) == 0;
            } else {
                flag1 = this.field_70146_Z.nextBoolean();
            }

            if (flag) {
                flag1 &= this.field_70146_Z.nextInt(5) == 0;
            }

            if (flag1 && i > 0) {
                double d0 = (double) (i >> 16 & 255) / 255.0D;
                double d1 = (double) (i >> 8 & 255) / 255.0D;
                double d2 = (double) (i >> 0 & 255) / 255.0D;

                this.field_70170_p.func_175688_a(flag ? EnumParticleTypes.SPELL_MOB_AMBIENT : EnumParticleTypes.SPELL_MOB, this.field_70165_t + (this.field_70146_Z.nextDouble() - 0.5D) * (double) this.field_70130_N, this.field_70163_u + this.field_70146_Z.nextDouble() * (double) this.field_70131_O, this.field_70161_v + (this.field_70146_Z.nextDouble() - 0.5D) * (double) this.field_70130_N, d0, d1, d2, new int[0]);
            }
        }

    }

    protected void func_175135_B() {
        if (this.field_70713_bf.isEmpty()) {
            this.func_175133_bi();
            this.func_82142_c(false);
        } else {
            Collection collection = this.field_70713_bf.values();

            this.field_70180_af.func_187227_b(EntityLivingBase.field_184634_g, Boolean.valueOf(func_184593_a(collection)));
            this.field_70180_af.func_187227_b(EntityLivingBase.field_184633_f, Integer.valueOf(PotionUtils.func_185181_a(collection)));
            this.func_82142_c(this.func_70644_a(MobEffects.field_76441_p));
        }

    }

    public static boolean func_184593_a(Collection<PotionEffect> collection) {
        Iterator iterator = collection.iterator();

        PotionEffect mobeffect;

        do {
            if (!iterator.hasNext()) {
                return true;
            }

            mobeffect = (PotionEffect) iterator.next();
        } while (mobeffect.func_82720_e());

        return false;
    }

    protected void func_175133_bi() {
        this.field_70180_af.func_187227_b(EntityLivingBase.field_184634_g, Boolean.valueOf(false));
        this.field_70180_af.func_187227_b(EntityLivingBase.field_184633_f, Integer.valueOf(0));
    }

    public void func_70674_bp() {
        if (!this.field_70170_p.field_72995_K) {
            Iterator iterator = this.field_70713_bf.values().iterator();

            while (iterator.hasNext()) {
                this.func_70688_c((PotionEffect) iterator.next());
                iterator.remove();
            }

        }
    }

    public Collection<PotionEffect> func_70651_bq() {
        return this.field_70713_bf.values();
    }

    public Map<Potion, PotionEffect> func_193076_bZ() {
        return this.field_70713_bf;
    }

    public boolean func_70644_a(Potion mobeffectlist) {
        return this.field_70713_bf.containsKey(mobeffectlist);
    }

    @Nullable
    public PotionEffect func_70660_b(Potion mobeffectlist) {
        return (PotionEffect) this.field_70713_bf.get(mobeffectlist);
    }

    public void func_70690_d(PotionEffect mobeffect) {
        org.spigotmc.AsyncCatcher.catchOp( "effect add"); // Spigot
        // CraftBukkit start
        if (isTickingEffects) {
            effectsToProcess.add(mobeffect);
            return;
        }
        // CraftBukkit end
        if (this.func_70687_e(mobeffect)) {
            PotionEffect mobeffect1 = (PotionEffect) this.field_70713_bf.get(mobeffect.func_188419_a());

            if (mobeffect1 == null) {
                this.field_70713_bf.put(mobeffect.func_188419_a(), mobeffect);
                this.func_70670_a(mobeffect);
            } else {
                mobeffect1.func_76452_a(mobeffect);
                this.func_70695_b(mobeffect1, true);
            }

        }
    }

    public boolean func_70687_e(PotionEffect mobeffect) {
        if (this.func_70668_bt() == EnumCreatureAttribute.UNDEAD) {
            Potion mobeffectlist = mobeffect.func_188419_a();

            if (mobeffectlist == MobEffects.field_76428_l || mobeffectlist == MobEffects.field_76436_u) {
                return false;
            }
        }

        return true;
    }

    public boolean func_70662_br() {
        return this.func_70668_bt() == EnumCreatureAttribute.UNDEAD;
    }

    @Nullable
    public PotionEffect func_184596_c(@Nullable Potion mobeffectlist) {
        // CraftBukkit start
        if (isTickingEffects) {
            effectsToProcess.add(mobeffectlist);
            return null;
        }
        // CraftBukkit end
        return (PotionEffect) this.field_70713_bf.remove(mobeffectlist);
    }

    public void func_184589_d(Potion mobeffectlist) {
        PotionEffect mobeffect = this.func_184596_c(mobeffectlist);

        if (mobeffect != null) {
            this.func_70688_c(mobeffect);
        }

    }

    protected void func_70670_a(PotionEffect mobeffect) {
        this.field_70752_e = true;
        if (!this.field_70170_p.field_72995_K) {
            mobeffect.func_188419_a().func_111185_a(this, this.func_110140_aT(), mobeffect.func_76458_c());
        }

    }

    protected void func_70695_b(PotionEffect mobeffect, boolean flag) {
        this.field_70752_e = true;
        if (flag && !this.field_70170_p.field_72995_K) {
            Potion mobeffectlist = mobeffect.func_188419_a();

            mobeffectlist.func_111187_a(this, this.func_110140_aT(), mobeffect.func_76458_c());
            mobeffectlist.func_111185_a(this, this.func_110140_aT(), mobeffect.func_76458_c());
        }

    }

    protected void func_70688_c(PotionEffect mobeffect) {
        this.field_70752_e = true;
        if (!this.field_70170_p.field_72995_K) {
            mobeffect.func_188419_a().func_111187_a(this, this.func_110140_aT(), mobeffect.func_76458_c());
        }

    }

    // CraftBukkit start - Delegate so we can handle providing a reason for health being regained
    public void func_70691_i(float f) {
        heal(f, EntityRegainHealthEvent.RegainReason.CUSTOM);
    }

    public void heal(float f, EntityRegainHealthEvent.RegainReason regainReason) {
        // Paper start - Forward
        heal(f, regainReason, false);
    }

    public void heal(float f, EntityRegainHealthEvent.RegainReason regainReason, boolean isFastRegen) {
        // Paper end
        float f1 = this.func_110143_aJ();

        if (f1 > 0.0F) {
            EntityRegainHealthEvent event = new EntityRegainHealthEvent(this.getBukkitEntity(), f, regainReason, isFastRegen); // Paper - Add isFastRegen
            this.field_70170_p.getServer().getPluginManager().callEvent(event);

            if (!event.isCancelled()) {
                this.func_70606_j((float) (this.func_110143_aJ() + event.getAmount()));
            }
            // CraftBukkit end
        }

    }

    public final float func_110143_aJ() {
        // CraftBukkit start - Use unscaled health
        if (this instanceof EntityPlayerMP) {
            return (float) ((EntityPlayerMP) this).getBukkitEntity().getHealth();
        }
        // CraftBukkit end
        return ((Float) this.field_70180_af.func_187225_a(EntityLivingBase.field_184632_c)).floatValue();
    }

    public void func_70606_j(float f) {
        // Paper start
        if (Float.isNaN(f)) { f = func_110138_aP(); if (this.valid) {
            System.err.println("[NAN-HEALTH] " + func_70005_c_() + " had NaN health set");
        } } // Paper end
        // CraftBukkit start - Handle scaled health
        if (this instanceof EntityPlayerMP) {
            org.bukkit.craftbukkit.entity.CraftPlayer player = ((EntityPlayerMP) this).getBukkitEntity();
            // Squeeze
            if (f < 0.0F) {
                player.setRealHealth(0.0D);
            } else if (f > player.getMaxHealth()) {
                player.setRealHealth(player.getMaxHealth());
            } else {
                player.setRealHealth(f);
            }

            player.updateScaledHealth();
            return;
        }
        // CraftBukkit end
        this.field_70180_af.func_187227_b(EntityLivingBase.field_184632_c, Float.valueOf(MathHelper.func_76131_a(f, 0.0F, this.func_110138_aP())));
    }

    public boolean func_70097_a(DamageSource damagesource, float f) {
        if (this.func_180431_b(damagesource)) {
            return false;
        } else if (this.field_70170_p.field_72995_K) {
            return false;
        } else {
            this.field_70708_bq = 0;
            if (this.func_110143_aJ() <= 0.0F) {
                return false;
            } else if (damagesource.func_76347_k() && this.func_70644_a(MobEffects.field_76426_n)) {
                return false;
            } else {
                float f1 = f;

                // CraftBukkit - Moved into damageEntity0(DamageSource, float)
                if (false && (damagesource == DamageSource.field_82728_o || damagesource == DamageSource.field_82729_p) && !this.func_184582_a(EntityEquipmentSlot.HEAD).func_190926_b()) {
                    this.func_184582_a(EntityEquipmentSlot.HEAD).func_77972_a((int) (f * 4.0F + this.field_70146_Z.nextFloat() * f * 2.0F), this);
                    f *= 0.75F;
                }

                boolean flag = f > 0.0F && this.func_184583_d(damagesource); // Copied from below

                // CraftBukkit - Moved into damageEntity0(DamageSource, float)
                if (false && f > 0.0F && this.func_184583_d(damagesource)) {
                    this.func_184590_k(f);
                    f = 0.0F;
                    if (!damagesource.func_76352_a()) {
                        Entity entity = damagesource.func_76364_f();

                        if (entity instanceof EntityLivingBase) {
                            this.func_190629_c((EntityLivingBase) entity);
                        }
                    }

                    flag = true;
                }

                this.field_70721_aZ = 1.5F;
                boolean flag1 = true;

                if ((float) this.field_70172_ad > (float) this.field_70771_an / 2.0F) {
                    if (f <= this.field_110153_bc) {
                        this.forceExplosionKnockback = true; // CraftBukkit - SPIGOT-949 - for vanilla consistency, cooldown does not prevent explosion knockback
                        return false;
                    }

                    // CraftBukkit start
                    if (!this.damageEntity0(damagesource, f - this.field_110153_bc)) {
                        return false;
                    }
                    // CraftBukkit end
                    this.field_110153_bc = f;
                    flag1 = false;
                } else {
                    // CraftBukkit start
                    if (!this.damageEntity0(damagesource, f)) {
                        return false;
                    }
                    this.field_110153_bc = f;
                    this.field_70172_ad = this.field_70771_an;
                    // this.damageEntity0(damagesource, f);
                    // CraftBukkit end
                    this.field_70738_aO = 10;
                    this.field_70737_aN = this.field_70738_aO;
                }

                // CraftBukkit start
                if (this instanceof EntityAnimal) {
                    ((EntityAnimal) this).func_70875_t();
                    if (this instanceof EntityTameable) {
                        ((EntityTameable) this).func_70907_r().func_75270_a(false);
                    }
                }
                // CraftBukkit end

                this.field_70739_aP = 0.0F;
                Entity entity1 = damagesource.func_76346_g();

                if (entity1 != null) {
                    if (entity1 instanceof EntityLivingBase) {
                        this.func_70604_c((EntityLivingBase) entity1);
                    }

                    if (entity1 instanceof EntityPlayer) {
                        this.field_70718_bc = 100;
                        this.field_70717_bb = (EntityPlayer) entity1;
                    } else if (entity1 instanceof EntityWolf) {
                        EntityWolf entitywolf = (EntityWolf) entity1;

                        if (entitywolf.func_70909_n()) {
                            this.field_70718_bc = 100;
                            this.field_70717_bb = null;
                        }
                    }
                }

                boolean knockbackCancelled = field_70170_p.paperConfig.disableExplosionKnockback && damagesource.func_94541_c() && this instanceof EntityPlayer; // Paper - Disable explosion knockback
                if (flag1) {
                    if (flag) {
                        this.field_70170_p.func_72960_a(this, (byte) 29);
                    } else if (damagesource instanceof EntityDamageSource && ((EntityDamageSource) damagesource).func_180139_w()) {
                        this.field_70170_p.func_72960_a(this, (byte) 33);
                    } else {
                        byte b0;

                        if (damagesource == DamageSource.field_76369_e) {
                            b0 = 36;
                        } else if (damagesource.func_76347_k()) {
                            b0 = 37;
                        } else {
                            b0 = 2;
                        }

                        if (!knockbackCancelled) // Paper - Disable explosion knockback
                        this.field_70170_p.func_72960_a(this, b0);
                    }

                    if (damagesource != DamageSource.field_76369_e && (!flag || f > 0.0F)) {
                        this.func_70018_K();
                    }

                    if (entity1 != null) {
                        double d0 = entity1.field_70165_t - this.field_70165_t;

                        double d1;

                        for (d1 = entity1.field_70161_v - this.field_70161_v; d0 * d0 + d1 * d1 < 1.0E-4D; d1 = (Math.random() - Math.random()) * 0.01D) {
                            d0 = (Math.random() - Math.random()) * 0.01D;
                        }

                        this.field_70739_aP = (float) (MathHelper.func_181159_b(d1, d0) * 57.2957763671875D - (double) this.field_70177_z);
                        this.func_70653_a(entity1, 0.4F, d0, d1);
                    } else {
                        this.field_70739_aP = (float) ((int) (Math.random() * 2.0D) * 180);
                    }
                }

                if (knockbackCancelled) this.field_70170_p.func_72960_a(this, (byte) 2); // Paper - Disable explosion knockback

                if (this.func_110143_aJ() <= 0.0F) {
                    if (!this.func_190628_d(damagesource)) {
                        SoundEvent soundeffect = this.func_184615_bR();

                        if (flag1 && soundeffect != null) {
                            this.func_184185_a(soundeffect, this.func_70599_aP(), this.func_70647_i());
                        }

                        this.func_70645_a(damagesource);
                    }
                } else if (flag1) {
                    this.func_184581_c(damagesource);
                }

                boolean flag2 = !flag || f > 0.0F;

                if (flag2) {
                    this.field_189750_bF = damagesource;
                    this.field_189751_bG = this.field_70170_p.func_82737_E();
                }

                if (this instanceof EntityPlayerMP) {
                    CriteriaTriggers.field_192128_h.func_192200_a((EntityPlayerMP) this, damagesource, f1, f, flag);
                }

                if (entity1 instanceof EntityPlayerMP) {
                    CriteriaTriggers.field_192127_g.func_192220_a((EntityPlayerMP) entity1, this, damagesource, f1, f, flag);
                }

                return flag2;
            }
        }
    }

    protected void func_190629_c(EntityLivingBase entityliving) {
        entityliving.func_70653_a(this, 0.5F, this.field_70165_t - entityliving.field_70165_t, this.field_70161_v - entityliving.field_70161_v);
    }

    private boolean func_190628_d(DamageSource damagesource) {
        if (damagesource.func_76357_e()) {
            return false;
        } else {
            ItemStack itemstack = null;
            EnumHand[] aenumhand = EnumHand.values();
            int i = aenumhand.length;

            // CraftBukkit start
            ItemStack itemstack1 = ItemStack.field_190927_a;
            for (int j = 0; j < i; ++j) {
                EnumHand enumhand = aenumhand[j];
                itemstack1 = this.func_184586_b(enumhand);

                if (itemstack1.func_77973_b() == Items.field_190929_cY) {
                    itemstack = itemstack1.func_77946_l();
                    // itemstack1.subtract(1); // CraftBukkit
                    break;
                }
            }

            EntityResurrectEvent event = new EntityResurrectEvent((LivingEntity) this.getBukkitEntity());
            event.setCancelled(itemstack == null);
            this.field_70170_p.getServer().getPluginManager().callEvent(event);

            if (!event.isCancelled()) {
                if (!itemstack1.func_190926_b()) {
                    itemstack1.func_190918_g(1);
                }
                if (itemstack != null && this instanceof EntityPlayerMP) {
                    // CraftBukkit end
                    EntityPlayerMP entityplayer = (EntityPlayerMP) this;

                    entityplayer.func_71029_a(StatList.func_188057_b(Items.field_190929_cY));
                    CriteriaTriggers.field_193130_A.func_193187_a(entityplayer, itemstack);
                }

                this.func_70606_j(1.0F);
                this.func_70674_bp();
                this.func_70690_d(new PotionEffect(MobEffects.field_76428_l, 900, 1));
                this.func_70690_d(new PotionEffect(MobEffects.field_76444_x, 100, 1));
                this.field_70170_p.func_72960_a(this, (byte) 35);
            }

            return !event.isCancelled();
        }
    }

    @Nullable
    public DamageSource func_189748_bU() {
        if (this.field_70170_p.func_82737_E() - this.field_189751_bG > 40L) {
            this.field_189750_bF = null;
        }

        return this.field_189750_bF;
    }

    protected void func_184581_c(DamageSource damagesource) {
        SoundEvent soundeffect = this.func_184601_bQ(damagesource);

        if (soundeffect != null) {
            this.func_184185_a(soundeffect, this.func_70599_aP(), this.func_70647_i());
        }

    }

    private boolean func_184583_d(DamageSource damagesource) {
        if (!damagesource.func_76363_c() && this.func_184585_cz()) {
            Vec3d vec3d = damagesource.func_188404_v();

            if (vec3d != null) {
                Vec3d vec3d1 = this.func_70676_i(1.0F);
                Vec3d vec3d2 = vec3d.func_72444_a(new Vec3d(this.field_70165_t, this.field_70163_u, this.field_70161_v)).func_72432_b();

                vec3d2 = new Vec3d(vec3d2.field_72450_a, 0.0D, vec3d2.field_72449_c);
                if (vec3d2.func_72430_b(vec3d1) < 0.0D) {
                    return true;
                }
            }
        }

        return false;
    }

    public void func_70669_a(ItemStack itemstack) {
        this.func_184185_a(SoundEvents.field_187635_cQ, 0.8F, 0.8F + this.field_70170_p.field_73012_v.nextFloat() * 0.4F);

        for (int i = 0; i < 5; ++i) {
            Vec3d vec3d = new Vec3d(((double) this.field_70146_Z.nextFloat() - 0.5D) * 0.1D, Math.random() * 0.1D + 0.1D, 0.0D);

            vec3d = vec3d.func_178789_a(-this.field_70125_A * 0.017453292F);
            vec3d = vec3d.func_178785_b(-this.field_70177_z * 0.017453292F);
            double d0 = (double) (-this.field_70146_Z.nextFloat()) * 0.6D - 0.3D;
            Vec3d vec3d1 = new Vec3d(((double) this.field_70146_Z.nextFloat() - 0.5D) * 0.3D, d0, 0.6D);

            vec3d1 = vec3d1.func_178789_a(-this.field_70125_A * 0.017453292F);
            vec3d1 = vec3d1.func_178785_b(-this.field_70177_z * 0.017453292F);
            vec3d1 = vec3d1.func_72441_c(this.field_70165_t, this.field_70163_u + (double) this.func_70047_e(), this.field_70161_v);
            this.field_70170_p.func_175688_a(EnumParticleTypes.ITEM_CRACK, vec3d1.field_72450_a, vec3d1.field_72448_b, vec3d1.field_72449_c, vec3d.field_72450_a, vec3d.field_72448_b + 0.05D, vec3d.field_72449_c, new int[] { Item.func_150891_b(itemstack.func_77973_b())});
        }

    }

    public void func_70645_a(DamageSource damagesource) {
        if (!this.field_70729_aU) {
            Entity entity = damagesource.func_76346_g();
            EntityLivingBase entityliving = this.func_94060_bK();

            if (this.field_70744_aE >= 0 && entityliving != null) {
                entityliving.func_191956_a(this, this.field_70744_aE, damagesource);
            }

            if (entity != null) {
                entity.func_70074_a(this);
            }

            this.field_70729_aU = true;
            this.func_110142_aN().func_94549_h();
            if (!this.field_70170_p.field_72995_K) {
                int i = 0;

                if (entity instanceof EntityPlayer) {
                    i = EnchantmentHelper.func_185283_h((EntityLivingBase) entity);
                }

                if (this.func_146066_aG() && this.field_70170_p.func_82736_K().func_82766_b("doMobLoot")) {
                    boolean flag = this.field_70718_bc > 0;

                    this.func_184610_a(flag, i, damagesource);
                    // CraftBukkit start - Call death event
                    CraftEventFactory.callEntityDeathEvent(this, this.drops);
                    this.drops = new ArrayList<org.bukkit.inventory.ItemStack>();
                } else {
                    CraftEventFactory.callEntityDeathEvent(this);
                    // CraftBukkit end
                }
            }

            this.field_70170_p.func_72960_a(this, (byte) 3);
        }
    }

    protected void func_184610_a(boolean flag, int i, DamageSource damagesource) {
        this.func_70628_a(flag, i);
        this.func_82160_b(flag, i);
    }

    protected void func_82160_b(boolean flag, int i) {}

    public void func_70653_a(Entity entity, float f, double d0, double d1) {
        if (this.field_70146_Z.nextDouble() >= this.func_110148_a(SharedMonsterAttributes.field_111266_c).func_111126_e()) {
            this.field_70160_al = true;
            float f1 = MathHelper.func_76133_a(d0 * d0 + d1 * d1);

            this.field_70159_w /= 2.0D;
            this.field_70179_y /= 2.0D;
            this.field_70159_w -= d0 / (double) f1 * (double) f;
            this.field_70179_y -= d1 / (double) f1 * (double) f;
            if (this.field_70122_E) {
                this.field_70181_x /= 2.0D;
                this.field_70181_x += (double) f;
                if (this.field_70181_x > 0.4000000059604645D) {
                    this.field_70181_x = 0.4000000059604645D;
                }
            }

        }
    }

    @Nullable
    protected SoundEvent func_184601_bQ(DamageSource damagesource) {
        return SoundEvents.field_187543_bD;
    }

    @Nullable
    protected SoundEvent func_184615_bR() {
        return SoundEvents.field_187661_by;
    }

    protected SoundEvent func_184588_d(int i) {
        return i > 4 ? SoundEvents.field_187655_bw : SoundEvents.field_187545_bE;
    }

    protected void func_70628_a(boolean flag, int i) {}

    public boolean func_70617_f_() {
        int i = MathHelper.func_76128_c(this.field_70165_t);
        int j = MathHelper.func_76128_c(this.func_174813_aQ().field_72338_b);
        int k = MathHelper.func_76128_c(this.field_70161_v);

        if (this instanceof EntityPlayer && ((EntityPlayer) this).func_175149_v()) {
            return false;
        } else {
            BlockPos blockposition = new BlockPos(i, j, k);
            IBlockState iblockdata = this.field_70170_p.func_180495_p(blockposition);
            Block block = iblockdata.func_177230_c();

            return block != Blocks.field_150468_ap && block != Blocks.field_150395_bd ? block instanceof BlockTrapDoor && this.func_184604_a(blockposition, iblockdata) : true;
        }
    }

    private boolean func_184604_a(BlockPos blockposition, IBlockState iblockdata) {
        if (((Boolean) iblockdata.func_177229_b(BlockTrapDoor.field_176283_b)).booleanValue()) {
            IBlockState iblockdata1 = this.field_70170_p.func_180495_p(blockposition.func_177977_b());

            if (iblockdata1.func_177230_c() == Blocks.field_150468_ap && iblockdata1.func_177229_b(BlockLadder.field_176382_a) == iblockdata.func_177229_b(BlockTrapDoor.field_176284_a)) {
                return true;
            }
        }

        return false;
    }

    public boolean func_70089_S() {
        return !this.field_70128_L && this.func_110143_aJ() > 0.0F;
    }

    public void func_180430_e(float f, float f1) {
        super.func_180430_e(f, f1);
        PotionEffect mobeffect = this.func_70660_b(MobEffects.field_76430_j);
        float f2 = mobeffect == null ? 0.0F : (float) (mobeffect.func_76458_c() + 1);
        int i = MathHelper.func_76123_f((f - 3.0F - f2) * f1);

        if (i > 0) {
            // CraftBukkit start
            if (!this.func_70097_a(DamageSource.field_76379_h, (float) i)) {
                return;
            }
            // CraftBukkit end
            this.func_184185_a(this.func_184588_d(i), 1.0F, 1.0F);
            // this.damageEntity(DamageSource.FALL, (float) i); // CraftBukkit - moved up
            int j = MathHelper.func_76128_c(this.field_70165_t);
            int k = MathHelper.func_76128_c(this.field_70163_u - 0.20000000298023224D);
            int l = MathHelper.func_76128_c(this.field_70161_v);
            IBlockState iblockdata = this.field_70170_p.func_180495_p(new BlockPos(j, k, l));

            if (iblockdata.func_185904_a() != Material.field_151579_a) {
                SoundType soundeffecttype = iblockdata.func_177230_c().func_185467_w();

                this.func_184185_a(soundeffecttype.func_185842_g(), soundeffecttype.func_185843_a() * 0.5F, soundeffecttype.func_185847_b() * 0.75F);
            }
        }

    }

    public int func_70658_aO() {
        IAttributeInstance attributeinstance = this.func_110148_a(SharedMonsterAttributes.field_188791_g);

        return MathHelper.func_76128_c(attributeinstance.func_111126_e());
    }

    protected void func_70675_k(float f) {}

    protected void func_184590_k(float f) {}

    protected float func_70655_b(DamageSource damagesource, float f) {
        if (!damagesource.func_76363_c()) {
            // this.damageArmor(f); // CraftBukkit - Moved into damageEntity0(DamageSource, float)
            f = CombatRules.func_189427_a(f, (float) this.func_70658_aO(), (float) this.func_110148_a(SharedMonsterAttributes.field_189429_h).func_111126_e());
        }

        return f;
    }

    protected float func_70672_c(DamageSource damagesource, float f) {
        if (damagesource.func_151517_h()) {
            return f;
        } else {
            int i;

            // CraftBukkit - Moved to damageEntity0(DamageSource, float)
            if (false && this.func_70644_a(MobEffects.field_76429_m) && damagesource != DamageSource.field_76380_i) {
                i = (this.func_70660_b(MobEffects.field_76429_m).func_76458_c() + 1) * 5;
                int j = 25 - i;
                float f1 = f * (float) j;

                f = f1 / 25.0F;
            }

            if (f <= 0.0F) {
                return 0.0F;
            } else {
                i = EnchantmentHelper.func_77508_a(this.func_184193_aE(), damagesource);
                if (i > 0) {
                    f = CombatRules.func_188401_b(f, (float) i);
                }

                return f;
            }
        }
    }

    // CraftBukkit start
    protected boolean damageEntity0(final DamageSource damagesource, float f) { // void -> boolean, add final
       if (!this.func_180431_b(damagesource)) {
            final boolean human = this instanceof EntityPlayer;
            float originalDamage = f;
            Function<Double, Double> hardHat = new Function<Double, Double>() {
                @Override
                public Double apply(Double f) {
                    if ((damagesource == DamageSource.field_82728_o || damagesource == DamageSource.field_82729_p) && !EntityLivingBase.this.func_184582_a(EntityEquipmentSlot.HEAD).func_190926_b()) {
                        return -(f - (f * 0.75F));

                    }
                    return -0.0;
                }
            };
            float hardHatModifier = hardHat.apply((double) f).floatValue();
            f += hardHatModifier;

            Function<Double, Double> blocking = new Function<Double, Double>() {
                @Override
                public Double apply(Double f) {
                    return -((EntityLivingBase.this.func_184583_d(damagesource)) ? f : 0.0);
                }
            };
            float blockingModifier = blocking.apply((double) f).floatValue();
            f += blockingModifier;

            Function<Double, Double> armor = new Function<Double, Double>() {
                @Override
                public Double apply(Double f) {
                    return -(f - EntityLivingBase.this.func_70655_b(damagesource, f.floatValue()));
                }
            };
            float armorModifier = armor.apply((double) f).floatValue();
            f += armorModifier;

            Function<Double, Double> resistance = new Function<Double, Double>() {
                @Override
                public Double apply(Double f) {
                    if (!damagesource.func_151517_h() && EntityLivingBase.this.func_70644_a(MobEffects.field_76429_m) && damagesource != DamageSource.field_76380_i) {
                        int i = (EntityLivingBase.this.func_70660_b(MobEffects.field_76429_m).func_76458_c() + 1) * 5;
                        int j = 25 - i;
                        float f1 = f.floatValue() * (float) j;
                        return -(f - (f1 / 25.0F));
                    }
                    return -0.0;
                }
            };
            float resistanceModifier = resistance.apply((double) f).floatValue();
            f += resistanceModifier;

            Function<Double, Double> magic = new Function<Double, Double>() {
                @Override
                public Double apply(Double f) {
                    return -(f - EntityLivingBase.this.func_70672_c(damagesource, f.floatValue()));
                }
            };
            float magicModifier = magic.apply((double) f).floatValue();
            f += magicModifier;

            Function<Double, Double> absorption = new Function<Double, Double>() {
                @Override
                public Double apply(Double f) {
                    return -(Math.max(f - Math.max(f - EntityLivingBase.this.func_110139_bj(), 0.0F), 0.0F));
                }
            };
            float absorptionModifier = absorption.apply((double) f).floatValue();

            EntityDamageEvent event = CraftEventFactory.handleLivingEntityDamageEvent(this, damagesource, originalDamage, hardHatModifier, blockingModifier, armorModifier, resistanceModifier, magicModifier, absorptionModifier, hardHat, blocking, armor, resistance, magic, absorption);
            if (event.isCancelled()) {
                return false;
            }

            f = (float) event.getFinalDamage();

            // Apply damage to helmet
            if ((damagesource == DamageSource.field_82728_o || damagesource == DamageSource.field_82729_p) && this.func_184582_a(EntityEquipmentSlot.HEAD) != null) {
                this.func_184582_a(EntityEquipmentSlot.HEAD).func_77972_a((int) (event.getDamage() * 4.0F + this.field_70146_Z.nextFloat() * event.getDamage() * 2.0F), this);
            }

            // Apply damage to armor
            if (!damagesource.func_76363_c()) {
                float armorDamage = (float) (event.getDamage() + event.getDamage(DamageModifier.BLOCKING) + event.getDamage(DamageModifier.HARD_HAT));
                this.func_70675_k(armorDamage);
            }

            // Apply blocking code // PAIL: steal from above
            if (event.getDamage(DamageModifier.BLOCKING) < 0) {
                this.func_184590_k((float) -event.getDamage(DamageModifier.BLOCKING));
                Entity entity = damagesource.func_76364_f();

                if (entity instanceof EntityLivingBase) {
                    this.func_190629_c((EntityLivingBase) entity);
                }
            }

            absorptionModifier = (float) -event.getDamage(DamageModifier.ABSORPTION);
            this.func_110149_m(Math.max(this.func_110139_bj() - absorptionModifier, 0.0F));
            if (f > 0 || !human) {
                if (human) {
                    // PAIL: Be sure to drag all this code from the EntityHuman subclass each update.
                    ((EntityPlayer) this).func_71020_j(damagesource.func_76345_d());
                    if (f < 3.4028235E37F) {
                        ((EntityPlayer) this).func_71064_a(StatList.field_188112_z, Math.round(f * 10.0F));
                    }
                }
                // CraftBukkit end
                float f2 = this.func_110143_aJ();

                this.func_70606_j(f2 - f);
                this.func_110142_aN().func_94547_a(damagesource, f2, f);
                // CraftBukkit start
                if (!human) {
                    this.func_110149_m(this.func_110139_bj() - f);
                }

                return true;
            } else {
                // Duplicate triggers if blocking
                if (event.getDamage(DamageModifier.BLOCKING) < 0) {
                    if (this instanceof EntityPlayerMP) {
                        CriteriaTriggers.field_192128_h.func_192200_a((EntityPlayerMP) this, damagesource, f, originalDamage, true);
                    }

                    if (damagesource.func_76346_g() instanceof EntityPlayerMP) {
                        CriteriaTriggers.field_192127_g.func_192220_a((EntityPlayerMP) damagesource.func_76346_g(), this, damagesource, f, originalDamage, true);
                    }

                    return false;
                } else {
                    return originalDamage > 0;
                }
                // CraftBukkit end
            }
        }
        return false; // CraftBukkit
    }

    public CombatTracker func_110142_aN() {
        return this.field_94063_bt;
    }

    @Nullable
    public EntityLivingBase func_94060_bK() {
        return (EntityLivingBase) (this.field_94063_bt.func_94550_c() != null ? this.field_94063_bt.func_94550_c() : (this.field_70717_bb != null ? this.field_70717_bb : (this.field_70755_b != null ? this.field_70755_b : null)));
    }

    public final float func_110138_aP() {
        return (float) this.func_110148_a(SharedMonsterAttributes.field_111267_a).func_111126_e();
    }

    public final int func_85035_bI() {
        return ((Integer) this.field_70180_af.func_187225_a(EntityLivingBase.field_184635_h)).intValue();
    }

    public final void func_85034_r(int i) {
        this.field_70180_af.func_187227_b(EntityLivingBase.field_184635_h, Integer.valueOf(i));
    }

    private int func_82166_i() {
        return this.func_70644_a(MobEffects.field_76422_e) ? 6 - (1 + this.func_70660_b(MobEffects.field_76422_e).func_76458_c()) : (this.func_70644_a(MobEffects.field_76419_f) ? 6 + (1 + this.func_70660_b(MobEffects.field_76419_f).func_76458_c()) * 2 : 6);
    }

    public void func_184609_a(EnumHand enumhand) {
        if (!this.field_82175_bq || this.field_110158_av >= this.func_82166_i() / 2 || this.field_110158_av < 0) {
            this.field_110158_av = -1;
            this.field_82175_bq = true;
            this.field_184622_au = enumhand;
            if (this.field_70170_p instanceof WorldServer) {
                ((WorldServer) this.field_70170_p).func_73039_n().func_151247_a((Entity) this, (Packet) (new SPacketAnimation(this, enumhand == EnumHand.MAIN_HAND ? 0 : 3)));
            }
        }

    }

    protected void func_70076_C() {
        this.func_70097_a(DamageSource.field_76380_i, 4.0F);
    }

    protected void func_82168_bl() {
        int i = this.func_82166_i();

        if (this.field_82175_bq) {
            ++this.field_110158_av;
            if (this.field_110158_av >= i) {
                this.field_110158_av = 0;
                this.field_82175_bq = false;
            }
        } else {
            this.field_110158_av = 0;
        }

        this.field_70733_aJ = (float) this.field_110158_av / (float) i;
    }

    public IAttributeInstance func_110148_a(IAttribute iattribute) {
        return this.func_110140_aT().func_111151_a(iattribute);
    }

    public AbstractAttributeMap func_110140_aT() {
        if (this.field_110155_d == null) {
            this.field_110155_d = new AttributeMap();
            this.craftAttributes = new CraftAttributeMap(field_110155_d); // CraftBukkit
        }

        return this.field_110155_d;
    }

    public EnumCreatureAttribute func_70668_bt() {
        return EnumCreatureAttribute.UNDEFINED;
    }

    public ItemStack func_184614_ca() {
        return this.func_184582_a(EntityEquipmentSlot.MAINHAND);
    }

    public ItemStack func_184592_cb() {
        return this.func_184582_a(EntityEquipmentSlot.OFFHAND);
    }

    public ItemStack func_184586_b(EnumHand enumhand) {
        if (enumhand == EnumHand.MAIN_HAND) {
            return this.func_184582_a(EntityEquipmentSlot.MAINHAND);
        } else if (enumhand == EnumHand.OFF_HAND) {
            return this.func_184582_a(EntityEquipmentSlot.OFFHAND);
        } else {
            throw new IllegalArgumentException("Invalid hand " + enumhand);
        }
    }

    public void func_184611_a(EnumHand enumhand, ItemStack itemstack) {
        if (enumhand == EnumHand.MAIN_HAND) {
            this.func_184201_a(EntityEquipmentSlot.MAINHAND, itemstack);
        } else {
            if (enumhand != EnumHand.OFF_HAND) {
                throw new IllegalArgumentException("Invalid hand " + enumhand);
            }

            this.func_184201_a(EntityEquipmentSlot.OFFHAND, itemstack);
        }

    }

    public boolean func_190630_a(EntityEquipmentSlot enumitemslot) {
        return !this.func_184582_a(enumitemslot).func_190926_b();
    }

    public abstract Iterable<ItemStack> func_184193_aE();

    public abstract ItemStack func_184582_a(EntityEquipmentSlot enumitemslot);

    public abstract void func_184201_a(EntityEquipmentSlot enumitemslot, ItemStack itemstack);

    public void func_70031_b(boolean flag) {
        super.func_70031_b(flag);
        IAttributeInstance attributeinstance = this.func_110148_a(SharedMonsterAttributes.field_111263_d);

        if (attributeinstance.func_111127_a(EntityLivingBase.field_110156_b) != null) {
            attributeinstance.func_111124_b(EntityLivingBase.field_110157_c);
        }

        if (flag) {
            attributeinstance.func_111121_a(EntityLivingBase.field_110157_c);
        }

    }

    protected float func_70599_aP() {
        return 1.0F;
    }

    protected float func_70647_i() {
        return this.func_70631_g_() ? (this.field_70146_Z.nextFloat() - this.field_70146_Z.nextFloat()) * 0.2F + 1.5F : (this.field_70146_Z.nextFloat() - this.field_70146_Z.nextFloat()) * 0.2F + 1.0F;
    }

    protected boolean func_70610_aX() {
        return this.func_110143_aJ() <= 0.0F;
    }

    public void func_110145_l(Entity entity) {
        double d0;

        if (!(entity instanceof EntityBoat) && !(entity instanceof AbstractHorse)) {
            double d1 = entity.field_70165_t;
            double d2 = entity.func_174813_aQ().field_72338_b + (double) entity.field_70131_O;

            d0 = entity.field_70161_v;
            EnumFacing enumdirection = entity.func_184172_bi();

            if (enumdirection != null) {
                EnumFacing enumdirection1 = enumdirection.func_176746_e();
                int[][] aint = new int[][] { { 0, 1}, { 0, -1}, { -1, 1}, { -1, -1}, { 1, 1}, { 1, -1}, { -1, 0}, { 1, 0}, { 0, 1}};
                double d3 = Math.floor(this.field_70165_t) + 0.5D;
                double d4 = Math.floor(this.field_70161_v) + 0.5D;
                double d5 = this.func_174813_aQ().field_72336_d - this.func_174813_aQ().field_72340_a;
                double d6 = this.func_174813_aQ().field_72334_f - this.func_174813_aQ().field_72339_c;
                AxisAlignedBB axisalignedbb = new AxisAlignedBB(d3 - d5 / 2.0D, entity.func_174813_aQ().field_72338_b, d4 - d6 / 2.0D, d3 + d5 / 2.0D, Math.floor(entity.func_174813_aQ().field_72338_b) + (double) this.field_70131_O, d4 + d6 / 2.0D);
                int[][] aint1 = aint;
                int i = aint.length;

                for (int j = 0; j < i; ++j) {
                    int[] aint2 = aint1[j];
                    double d7 = (double) (enumdirection.func_82601_c() * aint2[0] + enumdirection1.func_82601_c() * aint2[1]);
                    double d8 = (double) (enumdirection.func_82599_e() * aint2[0] + enumdirection1.func_82599_e() * aint2[1]);
                    double d9 = d3 + d7;
                    double d10 = d4 + d8;
                    AxisAlignedBB axisalignedbb1 = axisalignedbb.func_72317_d(d7, 0.0D, d8);

                    if (!this.field_70170_p.func_184143_b(axisalignedbb1)) {
                        if (this.field_70170_p.func_180495_p(new BlockPos(d9, this.field_70163_u, d10)).func_185896_q()) {
                            this.func_70634_a(d9, this.field_70163_u + 1.0D, d10);
                            return;
                        }

                        BlockPos blockposition = new BlockPos(d9, this.field_70163_u - 1.0D, d10);

                        if (this.field_70170_p.func_180495_p(blockposition).func_185896_q() || this.field_70170_p.func_180495_p(blockposition).func_185904_a() == Material.field_151586_h) {
                            d1 = d9;
                            d2 = this.field_70163_u + 1.0D;
                            d0 = d10;
                        }
                    } else if (!this.field_70170_p.func_184143_b(axisalignedbb1.func_72317_d(0.0D, 1.0D, 0.0D)) && this.field_70170_p.func_180495_p(new BlockPos(d9, this.field_70163_u + 1.0D, d10)).func_185896_q()) {
                        d1 = d9;
                        d2 = this.field_70163_u + 2.0D;
                        d0 = d10;
                    }
                }
            }

            this.func_70634_a(d1, d2, d0);
        } else {
            double d11 = (double) (this.field_70130_N / 2.0F + entity.field_70130_N / 2.0F) + 0.4D;
            float f;

            if (entity instanceof EntityBoat) {
                f = 0.0F;
            } else {
                f = 1.5707964F * (float) (this.func_184591_cq() == EnumHandSide.RIGHT ? -1 : 1);
            }

            float f1 = -MathHelper.func_76126_a(-this.field_70177_z * 0.017453292F - 3.1415927F + f);
            float f2 = -MathHelper.func_76134_b(-this.field_70177_z * 0.017453292F - 3.1415927F + f);

            d0 = Math.abs(f1) > Math.abs(f2) ? d11 / (double) Math.abs(f1) : d11 / (double) Math.abs(f2);
            double d12 = this.field_70165_t + (double) f1 * d0;
            double d13 = this.field_70161_v + (double) f2 * d0;

            this.func_70107_b(d12, entity.field_70163_u + (double) entity.field_70131_O + 0.001D, d13);
            if (this.field_70170_p.func_184143_b(this.func_174813_aQ())) {
                this.func_70107_b(d12, entity.field_70163_u + (double) entity.field_70131_O + 1.001D, d13);
                if (this.field_70170_p.func_184143_b(this.func_174813_aQ())) {
                    this.func_70107_b(entity.field_70165_t, entity.field_70163_u + (double) this.field_70131_O + 0.001D, entity.field_70161_v);
                }
            }
        }
    }

    protected float func_175134_bD() {
        return 0.42F;
    }

    protected void func_70664_aZ() {
        this.field_70181_x = (double) this.func_175134_bD();
        if (this.func_70644_a(MobEffects.field_76430_j)) {
            this.field_70181_x += (double) ((float) (this.func_70660_b(MobEffects.field_76430_j).func_76458_c() + 1) * 0.1F);
        }

        if (this.func_70051_ag()) {
            float f = this.field_70177_z * 0.017453292F;

            this.field_70159_w -= (double) (MathHelper.func_76126_a(f) * 0.2F);
            this.field_70179_y += (double) (MathHelper.func_76134_b(f) * 0.2F);
        }

        this.field_70160_al = true;
    }

    protected void func_70629_bd() {
        this.field_70181_x += 0.03999999910593033D;
    }

    protected void func_180466_bG() {
        this.field_70181_x += 0.03999999910593033D;
    }

    protected float func_189749_co() {
        return 0.8F;
    }

    public void func_191986_a(float f, float f1, float f2) {
        double d0;
        double d1;
        double d2;

        if (this.func_70613_aW() || this.func_184186_bw()) {
            float f3;
            float f4;
            float f5;

            if (this.func_70090_H() && (!(this instanceof EntityPlayer) || !((EntityPlayer) this).field_71075_bZ.field_75100_b)) {
                d2 = this.field_70163_u;
                f4 = this.func_189749_co();
                f3 = 0.02F;
                f5 = (float) EnchantmentHelper.func_185294_d(this);
                if (f5 > 3.0F) {
                    f5 = 3.0F;
                }

                if (!this.field_70122_E) {
                    f5 *= 0.5F;
                }

                if (f5 > 0.0F) {
                    f4 += (0.54600006F - f4) * f5 / 3.0F;
                    f3 += (this.func_70689_ay() - f3) * f5 / 3.0F;
                }

                this.func_191958_b(f, f1, f2, f3);
                this.func_70091_d(MoverType.SELF, this.field_70159_w, this.field_70181_x, this.field_70179_y);
                this.field_70159_w *= (double) f4;
                this.field_70181_x *= 0.800000011920929D;
                this.field_70179_y *= (double) f4;
                if (!this.func_189652_ae()) {
                    this.field_70181_x -= 0.02D;
                }

                if (this.field_70123_F && this.func_70038_c(this.field_70159_w, this.field_70181_x + 0.6000000238418579D - this.field_70163_u + d2, this.field_70179_y)) {
                    this.field_70181_x = 0.30000001192092896D;
                }
            } else if (this.func_180799_ab() && (!(this instanceof EntityPlayer) || !((EntityPlayer) this).field_71075_bZ.field_75100_b)) {
                d2 = this.field_70163_u;
                this.func_191958_b(f, f1, f2, 0.02F);
                this.func_70091_d(MoverType.SELF, this.field_70159_w, this.field_70181_x, this.field_70179_y);
                this.field_70159_w *= 0.5D;
                this.field_70181_x *= 0.5D;
                this.field_70179_y *= 0.5D;
                if (!this.func_189652_ae()) {
                    this.field_70181_x -= 0.02D;
                }

                if (this.field_70123_F && this.func_70038_c(this.field_70159_w, this.field_70181_x + 0.6000000238418579D - this.field_70163_u + d2, this.field_70179_y)) {
                    this.field_70181_x = 0.30000001192092896D;
                }
            } else if (this.func_184613_cA()) {
                if (field_70170_p.paperConfig.elytraHitWallDamage) { // Paper start - Toggleable Elytra Wall Damage
                if (this.field_70181_x > -0.5D) {
                    this.field_70143_R = 1.0F;
                }

                Vec3d vec3d = this.func_70040_Z();
                float f6 = this.field_70125_A * 0.017453292F;

                d0 = Math.sqrt(vec3d.field_72450_a * vec3d.field_72450_a + vec3d.field_72449_c * vec3d.field_72449_c);
                d1 = Math.sqrt(this.field_70159_w * this.field_70159_w + this.field_70179_y * this.field_70179_y);
                double d3 = vec3d.func_72433_c();
                float f7 = MathHelper.func_76134_b(f6);

                f7 = (float) ((double) f7 * (double) f7 * Math.min(1.0D, d3 / 0.4D));
                this.field_70181_x += -0.08D + (double) f7 * 0.06D;
                double d4;

                if (this.field_70181_x < 0.0D && d0 > 0.0D) {
                    d4 = this.field_70181_x * -0.1D * (double) f7;
                    this.field_70181_x += d4;
                    this.field_70159_w += vec3d.field_72450_a * d4 / d0;
                    this.field_70179_y += vec3d.field_72449_c * d4 / d0;
                }

                if (f6 < 0.0F) {
                    d4 = d1 * (double) (-MathHelper.func_76126_a(f6)) * 0.04D;
                    this.field_70181_x += d4 * 3.2D;
                    this.field_70159_w -= vec3d.field_72450_a * d4 / d0;
                    this.field_70179_y -= vec3d.field_72449_c * d4 / d0;
                }

                if (d0 > 0.0D) {
                    this.field_70159_w += (vec3d.field_72450_a / d0 * d1 - this.field_70159_w) * 0.1D;
                    this.field_70179_y += (vec3d.field_72449_c / d0 * d1 - this.field_70179_y) * 0.1D;
                }

                this.field_70159_w *= 0.9900000095367432D;
                this.field_70181_x *= 0.9800000190734863D;
                this.field_70179_y *= 0.9900000095367432D;
                this.func_70091_d(MoverType.SELF, this.field_70159_w, this.field_70181_x, this.field_70179_y);
                if (this.field_70123_F && !this.field_70170_p.field_72995_K) {
                    d4 = Math.sqrt(this.field_70159_w * this.field_70159_w + this.field_70179_y * this.field_70179_y);
                    double d5 = d1 - d4;
                    float f8 = (float) (d5 * 10.0D - 3.0D);

                    if (f8 > 0.0F) {
                        this.func_184185_a(this.func_184588_d((int) f8), 1.0F, 1.0F);
                        this.func_70097_a(DamageSource.field_188406_j, f8);
                    }
                }
                } // Paper end - Elyta Wall Damage if statement

                if (this.field_70122_E && !this.field_70170_p.field_72995_K) {
                    if (func_70083_f(7) && !CraftEventFactory.callToggleGlideEvent(this, false).isCancelled()) // CraftBukkit
                    this.func_70052_a(7, false);
                }
            } else {
                float f9 = 0.91F;
                BlockPos.PooledMutableBlockPos blockposition_pooledblockposition = BlockPos.PooledMutableBlockPos.func_185345_c(this.field_70165_t, this.func_174813_aQ().field_72338_b - 1.0D, this.field_70161_v);

                if (this.field_70122_E) {
                    f9 = this.field_70170_p.func_180495_p(blockposition_pooledblockposition).func_177230_c().field_149765_K * 0.91F;
                }

                f4 = 0.16277136F / (f9 * f9 * f9);
                if (this.field_70122_E) {
                    f3 = this.func_70689_ay() * f4;
                } else {
                    f3 = this.field_70747_aH;
                }

                this.func_191958_b(f, f1, f2, f3);
                f9 = 0.91F;
                if (this.field_70122_E) {
                    f9 = this.field_70170_p.func_180495_p(blockposition_pooledblockposition.func_189532_c(this.field_70165_t, this.func_174813_aQ().field_72338_b - 1.0D, this.field_70161_v)).func_177230_c().field_149765_K * 0.91F;
                }

                if (this.func_70617_f_()) {
                    f5 = 0.15F;
                    this.field_70159_w = MathHelper.func_151237_a(this.field_70159_w, -0.15000000596046448D, 0.15000000596046448D);
                    this.field_70179_y = MathHelper.func_151237_a(this.field_70179_y, -0.15000000596046448D, 0.15000000596046448D);
                    this.field_70143_R = 0.0F;
                    if (this.field_70181_x < -0.15D) {
                        this.field_70181_x = -0.15D;
                    }

                    boolean flag = this.func_70093_af() && this instanceof EntityPlayer;

                    if (flag && this.field_70181_x < 0.0D) {
                        this.field_70181_x = 0.0D;
                    }
                }

                this.func_70091_d(MoverType.SELF, this.field_70159_w, this.field_70181_x, this.field_70179_y);
                if (this.field_70123_F && this.func_70617_f_()) {
                    this.field_70181_x = 0.2D;
                }

                if (this.func_70644_a(MobEffects.field_188424_y)) {
                    this.field_70181_x += (0.05D * (double) (this.func_70660_b(MobEffects.field_188424_y).func_76458_c() + 1) - this.field_70181_x) * 0.2D;
                } else {
                    blockposition_pooledblockposition.func_189532_c(this.field_70165_t, 0.0D, this.field_70161_v);
                    if (this.field_70170_p.field_72995_K && (!this.field_70170_p.func_175667_e(blockposition_pooledblockposition) || !this.field_70170_p.func_175726_f(blockposition_pooledblockposition).func_177410_o())) {
                        if (this.field_70163_u > 0.0D) {
                            this.field_70181_x = -0.1D;
                        } else {
                            this.field_70181_x = 0.0D;
                        }
                    } else if (!this.func_189652_ae()) {
                        this.field_70181_x -= 0.08D;
                    }
                }

                this.field_70181_x *= 0.9800000190734863D;
                this.field_70159_w *= (double) f9;
                this.field_70179_y *= (double) f9;
                blockposition_pooledblockposition.func_185344_t();
            }
        }

        this.field_184618_aE = this.field_70721_aZ;
        d2 = this.field_70165_t - this.field_70169_q;
        d0 = this.field_70161_v - this.field_70166_s;
        d1 = this instanceof EntityFlying ? this.field_70163_u - this.field_70167_r : 0.0D;
        float f10 = MathHelper.func_76133_a(d2 * d2 + d1 * d1 + d0 * d0) * 4.0F;

        if (f10 > 1.0F) {
            f10 = 1.0F;
        }

        this.field_70721_aZ += (f10 - this.field_70721_aZ) * 0.4F;
        this.field_184619_aG += this.field_70721_aZ;
    }

    public float func_70689_ay() {
        return this.field_70746_aG;
    }

    public void func_70659_e(float f) {
        this.field_70746_aG = f;
    }

    public boolean func_70652_k(Entity entity) {
        this.func_130011_c(entity);
        return false;
    }

    public boolean func_70608_bn() {
        return false;
    }

    public void func_70071_h_() {
        super.func_70071_h_();
        this.func_184608_ct();
        if (!this.field_70170_p.field_72995_K) {
            int i = this.func_85035_bI();

            if (i > 0) {
                if (this.field_70720_be <= 0) {
                    this.field_70720_be = 20 * (30 - i);
                }

                --this.field_70720_be;
                if (this.field_70720_be <= 0) {
                    this.func_85034_r(i - 1);
                }
            }

            EntityEquipmentSlot[] aenumitemslot = EntityEquipmentSlot.values();
            int j = aenumitemslot.length;

            for (int k = 0; k < j; ++k) {
                EntityEquipmentSlot enumitemslot = aenumitemslot[k];
                ItemStack itemstack;

                switch (enumitemslot.func_188453_a()) {
                case HAND:
                    itemstack = (ItemStack) this.field_184630_bs.get(enumitemslot.func_188454_b());
                    break;

                case ARMOR:
                    itemstack = (ItemStack) this.field_184631_bt.get(enumitemslot.func_188454_b());
                    break;

                default:
                    continue;
                }

                ItemStack itemstack1 = this.func_184582_a(enumitemslot);

                if (!ItemStack.func_77989_b(itemstack1, itemstack)) {
                    // Paper start - PlayerArmorChangeEvent
                    if (this instanceof EntityPlayerMP && enumitemslot.getType() == EntityEquipmentSlot.Type.ARMOR && !itemstack.func_77973_b().equals(itemstack1.func_77973_b())) {
                        final org.bukkit.inventory.ItemStack oldItem = CraftItemStack.asBukkitCopy(itemstack);
                        final org.bukkit.inventory.ItemStack newItem = CraftItemStack.asBukkitCopy(itemstack1);
                        new PlayerArmorChangeEvent((Player) this.getBukkitEntity(), PlayerArmorChangeEvent.SlotType.valueOf(enumitemslot.name()), oldItem, newItem).callEvent();
                    }
                    // Paper end
                    ((WorldServer) this.field_70170_p).func_73039_n().func_151247_a((Entity) this, (Packet) (new SPacketEntityEquipment(this.func_145782_y(), enumitemslot, itemstack1)));
                    if (!itemstack.func_190926_b()) {
                        this.func_110140_aT().func_111148_a(itemstack.func_111283_C(enumitemslot));
                    }

                    if (!itemstack1.func_190926_b()) {
                        this.func_110140_aT().func_111147_b(itemstack1.func_111283_C(enumitemslot));
                    }

                    switch (enumitemslot.func_188453_a()) {
                    case HAND:
                        this.field_184630_bs.set(enumitemslot.func_188454_b(), itemstack1.func_190926_b() ? ItemStack.field_190927_a : itemstack1.func_77946_l());
                        break;

                    case ARMOR:
                        this.field_184631_bt.set(enumitemslot.func_188454_b(), itemstack1.func_190926_b() ? ItemStack.field_190927_a : itemstack1.func_77946_l());
                    }
                }
            }

            if (this.field_70173_aa % 20 == 0) {
                this.func_110142_aN().func_94549_h();
            }

            if (!this.field_184238_ar) {
                boolean flag = this.func_70644_a(MobEffects.field_188423_x);

                if (this.func_70083_f(6) != flag) {
                    this.func_70052_a(6, flag);
                }
            }
        }

        this.func_70636_d();
        double d0 = this.field_70165_t - this.field_70169_q;
        double d1 = this.field_70161_v - this.field_70166_s;
        float f = (float) (d0 * d0 + d1 * d1);
        float f1 = this.field_70761_aq;
        float f2 = 0.0F;

        this.field_70768_au = this.field_110154_aX;
        float f3 = 0.0F;

        if (f > 0.0025000002F) {
            f3 = 1.0F;
            f2 = (float) Math.sqrt((double) f) * 3.0F;
            float f4 = (float) MathHelper.func_181159_b(d1, d0) * 57.295776F - 90.0F;
            float f5 = MathHelper.func_76135_e(MathHelper.func_76142_g(this.field_70177_z) - f4);

            if (95.0F < f5 && f5 < 265.0F) {
                f1 = f4 - 180.0F;
            } else {
                f1 = f4;
            }
        }

        if (this.field_70733_aJ > 0.0F) {
            f1 = this.field_70177_z;
        }

        if (!this.field_70122_E) {
            f3 = 0.0F;
        }

        this.field_110154_aX += (f3 - this.field_110154_aX) * 0.3F;
        this.field_70170_p.field_72984_F.func_76320_a("headTurn");
        f2 = this.func_110146_f(f1, f2);
        this.field_70170_p.field_72984_F.func_76319_b();
        this.field_70170_p.field_72984_F.func_76320_a("rangeChecks");

        while (this.field_70177_z - this.field_70126_B < -180.0F) {
            this.field_70126_B -= 360.0F;
        }

        while (this.field_70177_z - this.field_70126_B >= 180.0F) {
            this.field_70126_B += 360.0F;
        }

        while (this.field_70761_aq - this.field_70760_ar < -180.0F) {
            this.field_70760_ar -= 360.0F;
        }

        while (this.field_70761_aq - this.field_70760_ar >= 180.0F) {
            this.field_70760_ar += 360.0F;
        }

        while (this.field_70125_A - this.field_70127_C < -180.0F) {
            this.field_70127_C -= 360.0F;
        }

        while (this.field_70125_A - this.field_70127_C >= 180.0F) {
            this.field_70127_C += 360.0F;
        }

        while (this.field_70759_as - this.field_70758_at < -180.0F) {
            this.field_70758_at -= 360.0F;
        }

        while (this.field_70759_as - this.field_70758_at >= 180.0F) {
            this.field_70758_at += 360.0F;
        }

        this.field_70170_p.field_72984_F.func_76319_b();
        this.field_70764_aw += f2;
        if (this.func_184613_cA()) {
            ++this.field_184629_bo;
        } else {
            this.field_184629_bo = 0;
        }
    }

    protected float func_110146_f(float f, float f1) {
        float f2 = MathHelper.func_76142_g(f - this.field_70761_aq);

        this.field_70761_aq += f2 * 0.3F;
        float f3 = MathHelper.func_76142_g(this.field_70177_z - this.field_70761_aq);
        boolean flag = f3 < -90.0F || f3 >= 90.0F;

        if (f3 < -75.0F) {
            f3 = -75.0F;
        }

        if (f3 >= 75.0F) {
            f3 = 75.0F;
        }

        this.field_70761_aq = this.field_70177_z - f3;
        if (f3 * f3 > 2500.0F) {
            this.field_70761_aq += f3 * 0.2F;
        }

        if (flag) {
            f1 *= -1.0F;
        }

        return f1;
    }

    public void func_70636_d() {
        if (this.field_70773_bE > 0) {
            --this.field_70773_bE;
        }

        if (this.field_70716_bi > 0 && !this.func_184186_bw()) {
            double d0 = this.field_70165_t + (this.field_184623_bh - this.field_70165_t) / (double) this.field_70716_bi;
            double d1 = this.field_70163_u + (this.field_184624_bi - this.field_70163_u) / (double) this.field_70716_bi;
            double d2 = this.field_70161_v + (this.field_184625_bj - this.field_70161_v) / (double) this.field_70716_bi;
            double d3 = MathHelper.func_76138_g(this.field_184626_bk - (double) this.field_70177_z);

            this.field_70177_z = (float) ((double) this.field_70177_z + d3 / (double) this.field_70716_bi);
            this.field_70125_A = (float) ((double) this.field_70125_A + (this.field_70709_bj - (double) this.field_70125_A) / (double) this.field_70716_bi);
            --this.field_70716_bi;
            this.func_70107_b(d0, d1, d2);
            this.func_70101_b(this.field_70177_z, this.field_70125_A);
        } else if (!this.func_70613_aW()) {
            this.field_70159_w *= 0.98D;
            this.field_70181_x *= 0.98D;
            this.field_70179_y *= 0.98D;
        }

        if (Math.abs(this.field_70159_w) < 0.003D) {
            this.field_70159_w = 0.0D;
        }

        if (Math.abs(this.field_70181_x) < 0.003D) {
            this.field_70181_x = 0.0D;
        }

        if (Math.abs(this.field_70179_y) < 0.003D) {
            this.field_70179_y = 0.0D;
        }

        this.field_70170_p.field_72984_F.func_76320_a("ai");
        if (this.func_70610_aX()) {
            this.field_70703_bu = false;
            this.field_70702_br = 0.0F;
            this.field_191988_bg = 0.0F;
            this.field_70704_bt = 0.0F;
        } else if (this.func_70613_aW()) {
            this.field_70170_p.field_72984_F.func_76320_a("newAi");
            this.func_70626_be();
            this.field_70170_p.field_72984_F.func_76319_b();
        }

        this.field_70170_p.field_72984_F.func_76319_b();
        this.field_70170_p.field_72984_F.func_76320_a("jump");
        if (this.field_70703_bu) {
            if (this.func_70090_H()) {
                this.func_70629_bd();
            } else if (this.func_180799_ab()) {
                this.func_180466_bG();
            } else if (this.field_70122_E && this.field_70773_bE == 0) {
                this.func_70664_aZ();
                this.field_70773_bE = 10;
            }
        } else {
            this.field_70773_bE = 0;
        }

        this.field_70170_p.field_72984_F.func_76319_b();
        this.field_70170_p.field_72984_F.func_76320_a("travel");
        this.field_70702_br *= 0.98F;
        this.field_191988_bg *= 0.98F;
        this.field_70704_bt *= 0.9F;
        this.func_184616_r();
        this.func_191986_a(this.field_70702_br, this.field_70701_bs, this.field_191988_bg);
        this.field_70170_p.field_72984_F.func_76319_b();
        this.field_70170_p.field_72984_F.func_76320_a("push");
        this.func_85033_bc();
        this.field_70170_p.field_72984_F.func_76319_b();
    }

    private void func_184616_r() {
        boolean flag = this.func_70083_f(7);

        if (flag && !this.field_70122_E && !this.func_184218_aH()) {
            ItemStack itemstack = this.func_184582_a(EntityEquipmentSlot.CHEST);

            if (itemstack.func_77973_b() == Items.field_185160_cR && ItemElytra.func_185069_d(itemstack)) {
                flag = true;
                if (!this.field_70170_p.field_72995_K && (this.field_184629_bo + 1) % 20 == 0) {
                    itemstack.func_77972_a(1, this);
                }
            } else {
                flag = false;
            }
        } else {
            flag = false;
        }

        if (!this.field_70170_p.field_72995_K) {
            if (flag != this.func_70083_f(7) && !CraftEventFactory.callToggleGlideEvent(this, flag).isCancelled()) // CraftBukkit
            this.func_70052_a(7, flag);
        }

    }

    protected void func_70626_be() {}

    protected void func_85033_bc() {
        List list = this.field_70170_p.func_175674_a(this, this.func_174813_aQ(), EntitySelectors.func_188442_a(this));

        if (!list.isEmpty()) {
            int i = this.field_70170_p.func_82736_K().func_180263_c("maxEntityCramming");
            int j;

            if (i > 0 && list.size() > i - 1 && this.field_70146_Z.nextInt(4) == 0) {
                j = 0;

                for (int k = 0; k < list.size(); ++k) {
                    if (!((Entity) list.get(k)).func_184218_aH()) {
                        ++j;
                    }
                }

                if (j > i - 1) {
                    this.func_70097_a(DamageSource.field_191291_g, 6.0F);
                }
            }

            numCollisions = Math.max(0, numCollisions - field_70170_p.paperConfig.maxCollisionsPerEntity); // Paper
            for (j = 0; j < list.size() && numCollisions < field_70170_p.paperConfig.maxCollisionsPerEntity; ++j) { // Paper
                Entity entity = (Entity) list.get(j);
                entity.numCollisions++; // Paper
                numCollisions++; // Paper

                this.func_82167_n(entity);
            }
        }

    }

    protected void func_82167_n(Entity entity) {
        entity.func_70108_f(this);
    }

    public void func_184210_p() {
        Entity entity = this.func_184187_bx();

        super.func_184210_p();
        if (entity != null && entity != this.func_184187_bx() && !this.field_70170_p.field_72995_K) {
            this.func_110145_l(entity);
        }

    }

    public void func_70098_U() {
        super.func_70098_U();
        this.field_70768_au = this.field_110154_aX;
        this.field_110154_aX = 0.0F;
        this.field_70143_R = 0.0F;
    }

    public void func_70637_d(boolean flag) {
        this.field_70703_bu = flag;
    }

    public void func_71001_a(Entity entity, int i) {
        if (!entity.field_70128_L && !this.field_70170_p.field_72995_K) {
            EntityTracker entitytracker = ((WorldServer) this.field_70170_p).func_73039_n();

            if (entity instanceof EntityItem || entity instanceof EntityArrow || entity instanceof EntityXPOrb) {
                entitytracker.func_151247_a(entity, (Packet) (new SPacketCollectItem(entity.func_145782_y(), this.func_145782_y(), i)));
            }
        }

    }

    public boolean func_70685_l(Entity entity) {
        return this.field_70170_p.func_147447_a(new Vec3d(this.field_70165_t, this.field_70163_u + (double) this.func_70047_e(), this.field_70161_v), new Vec3d(entity.field_70165_t, entity.field_70163_u + (double) entity.func_70047_e(), entity.field_70161_v), false, true, false) == null;
    }

    public Vec3d func_70676_i(float f) {
        if (f == 1.0F) {
            return this.func_174806_f(this.field_70125_A, this.field_70759_as);
        } else {
            float f1 = this.field_70127_C + (this.field_70125_A - this.field_70127_C) * f;
            float f2 = this.field_70758_at + (this.field_70759_as - this.field_70758_at) * f;

            return this.func_174806_f(f1, f2);
        }
    }

    public boolean func_70613_aW() {
        return !this.field_70170_p.field_72995_K;
    }

    public boolean func_70067_L() {
        return !this.field_70128_L && this.collides; // CraftBukkit
    }

    public boolean func_70104_M() {
        return this.func_70089_S() && !this.func_70617_f_() && this.collides; // CraftBukkit
    }

    protected void func_70018_K() {
        this.field_70133_I = this.field_70146_Z.nextDouble() >= this.func_110148_a(SharedMonsterAttributes.field_111266_c).func_111126_e();
    }

    public float func_70079_am() {
        return this.field_70759_as;
    }

    public void func_70034_d(float f) {
        this.field_70759_as = f;
    }

    public void func_181013_g(float f) {
        this.field_70761_aq = f;
    }

    public float func_110139_bj() {
        return this.field_110151_bq;
    }

    public void func_110149_m(float f) {
        if (f < 0.0F || Float.isNaN(f)) { // Paper
            f = 0.0F;
        }

        this.field_110151_bq = f;
    }

    public void func_152111_bt() {}

    public void func_152112_bu() {}

    protected void func_175136_bO() {
        this.field_70752_e = true;
    }

    public abstract EnumHandSide func_184591_cq();

    public boolean func_184587_cr() {
        return (((Byte) this.field_70180_af.func_187225_a(EntityLivingBase.field_184621_as)).byteValue() & 1) > 0;
    }

    public EnumHand func_184600_cs() {
        return (((Byte) this.field_70180_af.func_187225_a(EntityLivingBase.field_184621_as)).byteValue() & 2) > 0 ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND;
    }

    protected void func_184608_ct() {
        if (this.func_184587_cr()) {
            ItemStack itemstack = this.func_184586_b(this.func_184600_cs());

            if (itemstack == this.field_184627_bm) {
                if (this.func_184605_cv() <= 25 && this.func_184605_cv() % 4 == 0) {
                    this.func_184584_a(this.field_184627_bm, 5);
                }

                if (--this.field_184628_bn == 0 && !this.field_70170_p.field_72995_K) {
                    this.func_71036_o();
                }
            } else {
                this.func_184602_cy();
            }
        }

    }

    public void func_184598_c(EnumHand enumhand) {
        ItemStack itemstack = this.func_184586_b(enumhand);

        if (!itemstack.func_190926_b() && !this.func_184587_cr()) {
            this.field_184627_bm = itemstack;
            this.field_184628_bn = itemstack.func_77988_m();
            if (!this.field_70170_p.field_72995_K) {
                int i = 1;

                if (enumhand == EnumHand.OFF_HAND) {
                    i |= 2;
                }

                this.field_70180_af.func_187227_b(EntityLivingBase.field_184621_as, Byte.valueOf((byte) i));
            }

        }
    }

    public void func_184206_a(DataParameter<?> datawatcherobject) {
        super.func_184206_a(datawatcherobject);
        if (EntityLivingBase.field_184621_as.equals(datawatcherobject) && this.field_70170_p.field_72995_K) {
            if (this.func_184587_cr() && this.field_184627_bm.func_190926_b()) {
                this.field_184627_bm = this.func_184586_b(this.func_184600_cs());
                if (!this.field_184627_bm.func_190926_b()) {
                    this.field_184628_bn = this.field_184627_bm.func_77988_m();
                }
            } else if (!this.func_184587_cr() && !this.field_184627_bm.func_190926_b()) {
                this.field_184627_bm = ItemStack.field_190927_a;
                this.field_184628_bn = 0;
            }
        }

    }

    protected void func_184584_a(ItemStack itemstack, int i) {
        if (!itemstack.func_190926_b() && this.func_184587_cr()) {
            if (itemstack.func_77975_n() == EnumAction.DRINK) {
                this.func_184185_a(SoundEvents.field_187664_bz, 0.5F, this.field_70170_p.field_73012_v.nextFloat() * 0.1F + 0.9F);
            }

            if (itemstack.func_77975_n() == EnumAction.EAT) {
                for (int j = 0; j < i; ++j) {
                    Vec3d vec3d = new Vec3d(((double) this.field_70146_Z.nextFloat() - 0.5D) * 0.1D, Math.random() * 0.1D + 0.1D, 0.0D);

                    vec3d = vec3d.func_178789_a(-this.field_70125_A * 0.017453292F);
                    vec3d = vec3d.func_178785_b(-this.field_70177_z * 0.017453292F);
                    double d0 = (double) (-this.field_70146_Z.nextFloat()) * 0.6D - 0.3D;
                    Vec3d vec3d1 = new Vec3d(((double) this.field_70146_Z.nextFloat() - 0.5D) * 0.3D, d0, 0.6D);

                    vec3d1 = vec3d1.func_178789_a(-this.field_70125_A * 0.017453292F);
                    vec3d1 = vec3d1.func_178785_b(-this.field_70177_z * 0.017453292F);
                    vec3d1 = vec3d1.func_72441_c(this.field_70165_t, this.field_70163_u + (double) this.func_70047_e(), this.field_70161_v);
                    if (itemstack.func_77981_g()) {
                        this.field_70170_p.func_175688_a(EnumParticleTypes.ITEM_CRACK, vec3d1.field_72450_a, vec3d1.field_72448_b, vec3d1.field_72449_c, vec3d.field_72450_a, vec3d.field_72448_b + 0.05D, vec3d.field_72449_c, new int[] { Item.func_150891_b(itemstack.func_77973_b()), itemstack.func_77960_j()});
                    } else {
                        this.field_70170_p.func_175688_a(EnumParticleTypes.ITEM_CRACK, vec3d1.field_72450_a, vec3d1.field_72448_b, vec3d1.field_72449_c, vec3d.field_72450_a, vec3d.field_72448_b + 0.05D, vec3d.field_72449_c, new int[] { Item.func_150891_b(itemstack.func_77973_b())});
                    }
                }

                this.func_184185_a(SoundEvents.field_187537_bA, 0.5F + 0.5F * (float) this.field_70146_Z.nextInt(2), (this.field_70146_Z.nextFloat() - this.field_70146_Z.nextFloat()) * 0.2F + 1.0F);
            }

        }
    }

    protected void func_71036_o() {
        if (!this.field_184627_bm.func_190926_b() && this.func_184587_cr()) {
            PlayerItemConsumeEvent event = null; // Paper
            this.func_184584_a(this.field_184627_bm, 16);
            // CraftBukkit start - fire PlayerItemConsumeEvent
            ItemStack itemstack;
            if (this instanceof EntityPlayerMP) {
                org.bukkit.inventory.ItemStack craftItem = CraftItemStack.asBukkitCopy(this.field_184627_bm);
                event = new PlayerItemConsumeEvent((Player) this.getBukkitEntity(), craftItem); // Paper
                field_70170_p.getServer().getPluginManager().callEvent(event);

                if (event.isCancelled()) {
                    // Update client
                    ((EntityPlayerMP) this).getBukkitEntity().updateInventory();
                    ((EntityPlayerMP) this).getBukkitEntity().updateScaledHealth();
                    return;
                }

                itemstack = (craftItem.equals(event.getItem())) ? this.field_184627_bm.func_77950_b(this.field_70170_p, this) : CraftItemStack.asNMSCopy(event.getItem()).func_77950_b(field_70170_p, this);
            } else {
                itemstack = this.field_184627_bm.func_77950_b(this.field_70170_p, this);
            }

            // Paper start - save the default replacement item and change it if necessary
            final ItemStack defaultReplacement = itemstack;
            if (event != null && event.getReplacement() != null) {
                itemstack = CraftItemStack.asNMSCopy(event.getReplacement());
            }
            // Paper end

            this.func_184611_a(this.func_184600_cs(), itemstack);
            // CraftBukkit end
            this.func_184602_cy();

            // Paper start - if the replacement is anything but the default, update the client inventory
            if (this instanceof EntityPlayerMP && !com.google.common.base.Objects.equal(defaultReplacement, itemstack)) {
                ((EntityPlayerMP) this).getBukkitEntity().updateInventory();
            }
        }

    }

    public ItemStack func_184607_cu() {
        return this.field_184627_bm;
    }

    public int func_184605_cv() {
        return this.field_184628_bn;
    }

    public int func_184612_cw() {
        return this.func_184587_cr() ? this.field_184627_bm.func_77988_m() - this.func_184605_cv() : 0;
    }

    public void func_184597_cx() {
        if (!this.field_184627_bm.func_190926_b()) {
            this.field_184627_bm.func_77974_b(this.field_70170_p, this, this.func_184605_cv());
        }

        this.func_184602_cy();
    }

    public void func_184602_cy() {
        if (!this.field_70170_p.field_72995_K) {
            this.field_70180_af.func_187227_b(EntityLivingBase.field_184621_as, Byte.valueOf((byte) 0));
        }

        this.field_184627_bm = ItemStack.field_190927_a;
        this.field_184628_bn = 0;
    }

    public boolean func_184585_cz() {
        if (this.func_184587_cr() && !this.field_184627_bm.func_190926_b()) {
            Item item = this.field_184627_bm.func_77973_b();

            return item.func_77661_b(this.field_184627_bm) != EnumAction.BLOCK ? false : item.func_77626_a(this.field_184627_bm) - this.field_184628_bn >= getShieldBlockingDelay(); // Paper - shieldBlockingDelay
        } else {
            return false;
        }
    }

    public boolean func_184613_cA() {
        return this.func_70083_f(7);
    }

    public boolean func_184595_k(double d0, double d1, double d2) {
        double d3 = this.field_70165_t;
        double d4 = this.field_70163_u;
        double d5 = this.field_70161_v;

        this.field_70165_t = d0;
        this.field_70163_u = d1;
        this.field_70161_v = d2;
        boolean flag = false;
        BlockPos blockposition = new BlockPos(this);
        World world = this.field_70170_p;
        Random random = this.func_70681_au();
        boolean flag1;

        if (world.func_175667_e(blockposition)) {
            flag1 = false;

            while (!flag1 && blockposition.func_177956_o() > 0) {
                BlockPos blockposition1 = blockposition.func_177977_b();
                IBlockState iblockdata = world.func_180495_p(blockposition1);

                if (iblockdata.func_185904_a().func_76230_c()) {
                    flag1 = true;
                } else {
                    --this.field_70163_u;
                    blockposition = blockposition1;
                }
            }

            if (flag1) {
                // CraftBukkit start - Teleport event
                // this.enderTeleportTo(this.locX, this.locY, this.locZ);
                EntityTeleportEvent teleport = new EntityTeleportEvent(this.getBukkitEntity(), new Location(this.field_70170_p.getWorld(), d3, d4, d5), new Location(this.field_70170_p.getWorld(), this.field_70165_t, this.field_70163_u, this.field_70161_v));
                this.field_70170_p.getServer().getPluginManager().callEvent(teleport);
                if (!teleport.isCancelled()) {
                    Location to = teleport.getTo();
                    this.func_70634_a(to.getX(), to.getY(), to.getZ());
                    if (world.func_184144_a(this, this.func_174813_aQ()).isEmpty() && !world.func_72953_d(this.func_174813_aQ())) {
                        flag = true;
                    }
                }
                // CraftBukkit end
            }
        }

        if (!flag) {
            this.func_70634_a(d3, d4, d5);
            return false;
        } else {
            flag1 = true;

            for (int i = 0; i < 128; ++i) {
                double d6 = (double) i / 127.0D;
                float f = (random.nextFloat() - 0.5F) * 0.2F;
                float f1 = (random.nextFloat() - 0.5F) * 0.2F;
                float f2 = (random.nextFloat() - 0.5F) * 0.2F;
                double d7 = d3 + (this.field_70165_t - d3) * d6 + (random.nextDouble() - 0.5D) * (double) this.field_70130_N * 2.0D;
                double d8 = d4 + (this.field_70163_u - d4) * d6 + random.nextDouble() * (double) this.field_70131_O;
                double d9 = d5 + (this.field_70161_v - d5) * d6 + (random.nextDouble() - 0.5D) * (double) this.field_70130_N * 2.0D;

                world.func_175688_a(EnumParticleTypes.PORTAL, d7, d8, d9, (double) f, (double) f1, (double) f2, new int[0]);
            }

            if (this instanceof EntityCreature) {
                ((EntityCreature) this).func_70661_as().func_75499_g();
            }

            return true;
        }
    }

    public boolean func_184603_cC() {
        return true;
    }

    public boolean func_190631_cK() {
        return true;
    }

    // Paper start
    public int shieldBlockingDelay = field_70170_p.paperConfig.shieldBlockingDelay;

    public int getShieldBlockingDelay() {
        return shieldBlockingDelay;
    }

    public void setShieldBlockingDelay(int shieldBlockingDelay) {
        this.shieldBlockingDelay = shieldBlockingDelay;
    }
    // Paper end
}
