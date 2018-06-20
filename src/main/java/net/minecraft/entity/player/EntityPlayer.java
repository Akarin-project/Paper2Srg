package net.minecraft.entity.player;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfile;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IEntityMultiPart;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.MultiPartEntityPart;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.passive.EntityParrot;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryEnderChest;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemElytra;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.Team;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.CommandBlockBaseLogic;
import net.minecraft.tileentity.TileEntityCommandBlock;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.tileentity.TileEntityStructure;
import net.minecraft.util.CooldownTracker;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.FoodStats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.DataFixesManager;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.IDataFixer;
import net.minecraft.util.datafix.IDataWalker;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.GameType;
import net.minecraft.world.IInteractionObject;
import net.minecraft.world.LockCode;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.MapData;
import org.bukkit.craftbukkit.entity.CraftHumanEntity;
import org.bukkit.craftbukkit.entity.CraftItem;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityCombustByEntityEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerVelocityEvent;
import org.bukkit.util.Vector;

// CraftBukkit start
import org.bukkit.craftbukkit.entity.CraftHumanEntity;
import org.bukkit.craftbukkit.entity.CraftItem;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityCombustByEntityEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerVelocityEvent;
import org.bukkit.util.Vector;
// CraftBukkit end

public abstract class EntityPlayer extends EntityLivingBase {

    private static final DataParameter<Float> field_184829_a = EntityDataManager.func_187226_a(EntityPlayer.class, DataSerializers.field_187193_c);
    private static final DataParameter<Integer> field_184830_b = EntityDataManager.func_187226_a(EntityPlayer.class, DataSerializers.field_187192_b);
    protected static final DataParameter<Byte> field_184827_bp = EntityDataManager.func_187226_a(EntityPlayer.class, DataSerializers.field_187191_a);
    protected static final DataParameter<Byte> field_184828_bq = EntityDataManager.func_187226_a(EntityPlayer.class, DataSerializers.field_187191_a);
    protected static final DataParameter<NBTTagCompound> field_192032_bt = EntityDataManager.func_187226_a(EntityPlayer.class, DataSerializers.field_192734_n);
    protected static final DataParameter<NBTTagCompound> field_192033_bu = EntityDataManager.func_187226_a(EntityPlayer.class, DataSerializers.field_192734_n);
    public InventoryPlayer field_71071_by = new InventoryPlayer(this);
    protected InventoryEnderChest field_71078_a = new InventoryEnderChest(this); // CraftBukkit - add "this" to constructor
    public Container field_71069_bz;
    public Container field_71070_bA;
    protected FoodStats field_71100_bB = new FoodStats(this); // CraftBukkit - add "this" to constructor
    protected int field_71101_bC;
    public float field_71107_bF;
    public float field_71109_bG;
    public int field_71090_bL;
    public double field_71091_bM;
    public double field_71096_bN;
    public double field_71097_bO;
    public double field_71094_bP;
    public double field_71095_bQ;
    public double field_71085_bR;
    public boolean field_71083_bS;
    public BlockPos field_71081_bT;
    public int field_71076_b;
    public float field_71079_bU;
    public float field_71089_bV;
    private BlockPos field_71077_c;
    private boolean field_82248_d;
    public PlayerCapabilities field_71075_bZ = new PlayerCapabilities();
    public int field_71068_ca;
    public int field_71067_cb;
    public float field_71106_cc;
    protected int field_175152_f;
    protected float field_71102_ce = 0.02F;
    private int field_82249_h;
    private GameProfile field_146106_i; public void setProfile(GameProfile profile) { this.field_146106_i = profile; } // Paper - OBFHELPER
    private ItemStack field_184831_bT;
    private final CooldownTracker field_184832_bU;
    @Nullable
    public EntityFishHook field_71104_cf;
    public boolean affectsSpawning = true;

    // CraftBukkit start
    public boolean fauxSleeping;
    public String spawnWorld = "";
    public int oldLevel = -1;

    @Override
    public CraftHumanEntity getBukkitEntity() {
        return (CraftHumanEntity) super.getBukkitEntity();
    }
    // CraftBukkit end

    protected CooldownTracker func_184815_l() {
        return new CooldownTracker();
    }

    public EntityPlayer(World world, GameProfile gameprofile) {
        super(world);
        this.field_184831_bT = ItemStack.field_190927_a;
        this.field_184832_bU = this.func_184815_l();
        this.func_184221_a(func_146094_a(gameprofile));
        this.field_146106_i = gameprofile;
        this.field_71069_bz = new ContainerPlayer(this.field_71071_by, !world.field_72995_K, this);
        this.field_71070_bA = this.field_71069_bz;
        BlockPos blockposition = world.func_175694_M();

        this.func_70012_b(blockposition.func_177958_n() + 0.5D, blockposition.func_177956_o() + 1, blockposition.func_177952_p() + 0.5D, 0.0F, 0.0F);
        this.field_70741_aB = 180.0F;
    }

    @Override
    protected void func_110147_ax() {
        super.func_110147_ax();
        this.func_110140_aT().func_111150_b(SharedMonsterAttributes.field_111264_e).func_111128_a(1.0D);
        this.func_110148_a(SharedMonsterAttributes.field_111263_d).func_111128_a(0.10000000149011612D);
        this.func_110140_aT().func_111150_b(SharedMonsterAttributes.field_188790_f);
        this.func_110140_aT().func_111150_b(SharedMonsterAttributes.field_188792_h);
    }

    @Override
    protected void func_70088_a() {
        super.func_70088_a();
        this.field_70180_af.func_187214_a(EntityPlayer.field_184829_a, Float.valueOf(0.0F));
        this.field_70180_af.func_187214_a(EntityPlayer.field_184830_b, Integer.valueOf(0));
        this.field_70180_af.func_187214_a(EntityPlayer.field_184827_bp, Byte.valueOf((byte) 0));
        this.field_70180_af.func_187214_a(EntityPlayer.field_184828_bq, Byte.valueOf((byte) 1));
        this.field_70180_af.func_187214_a(EntityPlayer.field_192032_bt, new NBTTagCompound());
        this.field_70180_af.func_187214_a(EntityPlayer.field_192033_bu, new NBTTagCompound());
    }

    @Override
    public void func_70071_h_() {
        this.field_70145_X = this.func_175149_v();
        if (this.func_175149_v()) {
            this.field_70122_E = false;
        }

        if (this.field_71090_bL > 0) {
            --this.field_71090_bL;
        }

        if (this.func_70608_bn()) {
            ++this.field_71076_b;
            if (this.field_71076_b > 100) {
                this.field_71076_b = 100;
            }

            if (!this.field_70170_p.field_72995_K) {
                if (!this.func_175143_p()) {
                    this.func_70999_a(true, true, false);
                } else if (this.field_70170_p.func_72935_r()) {
                    this.func_70999_a(false, true, true);
                }
            }
        } else if (this.field_71076_b > 0) {
            ++this.field_71076_b;
            if (this.field_71076_b >= 110) {
                this.field_71076_b = 0;
            }
        }

        super.func_70071_h_();
        if (!this.field_70170_p.field_72995_K && this.field_71070_bA != null && !this.field_71070_bA.func_75145_c(this)) {
            this.func_71053_j();
            this.field_71070_bA = this.field_71069_bz;
        }

        if (this.func_70027_ad() && this.field_71075_bZ.field_75102_a) {
            this.func_70066_B();
        }

        this.func_184820_o();
        if (!this.field_70170_p.field_72995_K) {
            this.field_71100_bB.func_75118_a(this);
            this.func_71029_a(StatList.field_188097_g);
            if (this.func_70089_S()) {
                this.func_71029_a(StatList.field_188098_h);
            }

            if (this.func_70093_af()) {
                this.func_71029_a(StatList.field_188099_i);
            }
        }

        int i = 29999999;
        double d0 = MathHelper.func_151237_a(this.field_70165_t, -2.9999999E7D, 2.9999999E7D);
        double d1 = MathHelper.func_151237_a(this.field_70161_v, -2.9999999E7D, 2.9999999E7D);

        if (d0 != this.field_70165_t || d1 != this.field_70161_v) {
            this.func_70107_b(d0, this.field_70163_u, d1);
        }

        ++this.field_184617_aD;
        ItemStack itemstack = this.func_184614_ca();

        if (!ItemStack.func_77989_b(this.field_184831_bT, itemstack)) {
            if (!ItemStack.func_185132_d(this.field_184831_bT, itemstack)) {
                this.func_184821_cY();
            }

            this.field_184831_bT = itemstack.func_190926_b() ? ItemStack.field_190927_a : itemstack.func_77946_l();
        }

        this.field_184832_bU.func_185144_a();
        this.func_184808_cD();
    }

    private void func_184820_o() {
        this.field_71091_bM = this.field_71094_bP;
        this.field_71096_bN = this.field_71095_bQ;
        this.field_71097_bO = this.field_71085_bR;
        double d0 = this.field_70165_t - this.field_71094_bP;
        double d1 = this.field_70163_u - this.field_71095_bQ;
        double d2 = this.field_70161_v - this.field_71085_bR;
        double d3 = 10.0D;

        if (d0 > 10.0D) {
            this.field_71094_bP = this.field_70165_t;
            this.field_71091_bM = this.field_71094_bP;
        }

        if (d2 > 10.0D) {
            this.field_71085_bR = this.field_70161_v;
            this.field_71097_bO = this.field_71085_bR;
        }

        if (d1 > 10.0D) {
            this.field_71095_bQ = this.field_70163_u;
            this.field_71096_bN = this.field_71095_bQ;
        }

        if (d0 < -10.0D) {
            this.field_71094_bP = this.field_70165_t;
            this.field_71091_bM = this.field_71094_bP;
        }

        if (d2 < -10.0D) {
            this.field_71085_bR = this.field_70161_v;
            this.field_71097_bO = this.field_71085_bR;
        }

        if (d1 < -10.0D) {
            this.field_71095_bQ = this.field_70163_u;
            this.field_71096_bN = this.field_71095_bQ;
        }

        this.field_71094_bP += d0 * 0.25D;
        this.field_71085_bR += d2 * 0.25D;
        this.field_71095_bQ += d1 * 0.25D;
    }

    protected void func_184808_cD() {
        float f;
        float f1;

        if (this.func_184613_cA()) {
            f = 0.6F;
            f1 = 0.6F;
        } else if (this.func_70608_bn()) {
            f = 0.2F;
            f1 = 0.2F;
        } else if (this.func_70093_af()) {
            f = 0.6F;
            f1 = 1.65F;
        } else {
            f = 0.6F;
            f1 = 1.8F;
        }

        if (f != this.field_70130_N || f1 != this.field_70131_O) {
            AxisAlignedBB axisalignedbb = this.func_174813_aQ();

            axisalignedbb = new AxisAlignedBB(axisalignedbb.field_72340_a, axisalignedbb.field_72338_b, axisalignedbb.field_72339_c, axisalignedbb.field_72340_a + f, axisalignedbb.field_72338_b + f1, axisalignedbb.field_72339_c + f);
            if (!this.field_70170_p.func_184143_b(axisalignedbb)) {
                this.func_70105_a(f, f1);
            }
        }

    }

    @Override
    public int func_82145_z() {
        return this.field_71075_bZ.field_75102_a ? 1 : 80;
    }

    @Override
    protected SoundEvent func_184184_Z() {
        return SoundEvents.field_187808_ef;
    }

    @Override
    protected SoundEvent func_184181_aa() {
        return SoundEvents.field_187806_ee;
    }

    @Override
    public int func_82147_ab() {
        return 10;
    }

    @Override
    public void func_184185_a(SoundEvent soundeffect, float f, float f1) {
        this.field_70170_p.func_184148_a(this, this.field_70165_t, this.field_70163_u, this.field_70161_v, soundeffect, this.func_184176_by(), f, f1);
    }

    @Override
    public SoundCategory func_184176_by() {
        return SoundCategory.PLAYERS;
    }

    @Override
    public int func_190531_bD() {
        return 20;
    }

    @Override
    protected boolean func_70610_aX() {
        return this.func_110143_aJ() <= 0.0F || this.func_70608_bn();
    }

    public void func_71053_j() {
        this.field_71070_bA = this.field_71069_bz;
    }

    @Override
    public void func_70098_U() {
        if (!this.field_70170_p.field_72995_K && this.func_70093_af() && this.func_184218_aH()) {
            this.func_184210_p();
            this.func_70095_a(false);
        } else {
            double d0 = this.field_70165_t;
            double d1 = this.field_70163_u;
            double d2 = this.field_70161_v;
            float f = this.field_70177_z;
            float f1 = this.field_70125_A;

            super.func_70098_U();
            this.field_71107_bF = this.field_71109_bG;
            this.field_71109_bG = 0.0F;
            this.func_71015_k(this.field_70165_t - d0, this.field_70163_u - d1, this.field_70161_v - d2);
            if (this.func_184187_bx() instanceof EntityPig) {
                this.field_70125_A = f1;
                this.field_70177_z = f;
                this.field_70761_aq = ((EntityPig) this.func_184187_bx()).field_70761_aq;
            }

        }
    }

    @Override
    protected void func_70626_be() {
        super.func_70626_be();
        this.func_82168_bl();
        this.field_70759_as = this.field_70177_z;
    }

    @Override
    public void func_70636_d() {
        if (this.field_71101_bC > 0) {
            --this.field_71101_bC;
        }

        if (this.field_70170_p.func_175659_aa() == EnumDifficulty.PEACEFUL && this.field_70170_p.func_82736_K().func_82766_b("naturalRegeneration")) {
            if (this.func_110143_aJ() < this.func_110138_aP() && this.field_70173_aa % 20 == 0) {
                // CraftBukkit - added regain reason of "REGEN" for filtering purposes.
                this.heal(1.0F, org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason.REGEN);
            }

            if (this.field_71100_bB.func_75121_c() && this.field_70173_aa % 10 == 0) {
                this.field_71100_bB.func_75114_a(this.field_71100_bB.func_75116_a() + 1);
            }
        }

        this.field_71071_by.func_70429_k();
        this.field_71107_bF = this.field_71109_bG;
        super.func_70636_d();
        IAttributeInstance attributeinstance = this.func_110148_a(SharedMonsterAttributes.field_111263_d);

        if (!this.field_70170_p.field_72995_K) {
            attributeinstance.func_111128_a(this.field_71075_bZ.func_75094_b());
        }

        this.field_70747_aH = this.field_71102_ce;
        if (this.func_70051_ag()) {
            this.field_70747_aH = (float) (this.field_70747_aH + this.field_71102_ce * 0.3D);
        }

        this.func_70659_e((float) attributeinstance.func_111126_e());
        float f = MathHelper.func_76133_a(this.field_70159_w * this.field_70159_w + this.field_70179_y * this.field_70179_y);
        float f1 = (float) ( org.bukkit.craftbukkit.TrigMath.atan(-this.field_70181_x * 0.20000000298023224D) * 15.0D); // CraftBukkit

        if (f > 0.1F) {
            f = 0.1F;
        }

        if (!this.field_70122_E || this.func_110143_aJ() <= 0.0F) {
            f = 0.0F;
        }

        if (this.field_70122_E || this.func_110143_aJ() <= 0.0F) {
            f1 = 0.0F;
        }

        this.field_71109_bG += (f - this.field_71109_bG) * 0.4F;
        this.field_70726_aT += (f1 - this.field_70726_aT) * 0.8F;
        if (this.func_110143_aJ() > 0.0F && !this.func_175149_v()) {
            AxisAlignedBB axisalignedbb;

            if (this.func_184218_aH() && !this.func_184187_bx().field_70128_L) {
                axisalignedbb = this.func_174813_aQ().func_111270_a(this.func_184187_bx().func_174813_aQ()).func_72314_b(1.0D, 0.0D, 1.0D);
            } else {
                axisalignedbb = this.func_174813_aQ().func_72314_b(1.0D, 0.5D, 1.0D);
            }

            List list = this.field_70170_p.func_72839_b(this, axisalignedbb);

            for (int i = 0; i < list.size(); ++i) {
                Entity entity = (Entity) list.get(i);

                if (!entity.field_70128_L) {
                    this.func_71044_o(entity);
                }
            }
        }

        this.func_192028_j(this.func_192023_dk());
        this.func_192028_j(this.func_192025_dl());
        if (!this.field_70170_p.field_72995_K && (this.field_70143_R > 0.5F || this.func_70090_H() || this.func_184218_aH()) || this.field_71075_bZ.field_75100_b) {
            if (!this.field_70170_p.paperConfig.parrotsHangOnBetter) this.func_192030_dh(); // Paper - Hang on!
        }

    }

    private void func_192028_j(@Nullable NBTTagCompound nbttagcompound) {
        if (nbttagcompound != null && !nbttagcompound.func_74764_b("Silent") || !nbttagcompound.func_74767_n("Silent")) {
            String s = nbttagcompound.func_74779_i("id");

            if (s.equals(EntityList.func_191306_a(EntityParrot.class).toString())) {
                EntityParrot.func_192005_a(this.field_70170_p, this);
            }
        }

    }

    private void func_71044_o(Entity entity) {
        entity.func_70100_b_(this);
    }

    public int func_71037_bA() {
        return this.field_70180_af.func_187225_a(EntityPlayer.field_184830_b).intValue();
    }

    public void func_85040_s(int i) {
        this.field_70180_af.func_187227_b(EntityPlayer.field_184830_b, Integer.valueOf(i));
    }

    public void func_85039_t(int i) {
        int j = this.func_71037_bA();

        this.field_70180_af.func_187227_b(EntityPlayer.field_184830_b, Integer.valueOf(j + i));
    }

    @Override
    public void func_70645_a(DamageSource damagesource) {
        super.func_70645_a(damagesource);
        this.func_70105_a(0.2F, 0.2F);
        this.func_70107_b(this.field_70165_t, this.field_70163_u, this.field_70161_v);
        this.field_70181_x = 0.10000000149011612D;
        if ("Notch".equals(this.func_70005_c_())) {
            this.func_146097_a(new ItemStack(Items.field_151034_e, 1), true, false);
        }

        if (!this.field_70170_p.func_82736_K().func_82766_b("keepInventory") && !this.func_175149_v()) {
            this.func_190776_cN();
            this.field_71071_by.func_70436_m();
        }

        if (damagesource != null) {
            this.field_70159_w = -MathHelper.func_76134_b((this.field_70739_aP + this.field_70177_z) * 0.017453292F) * 0.1F;
            this.field_70179_y = -MathHelper.func_76126_a((this.field_70739_aP + this.field_70177_z) * 0.017453292F) * 0.1F;
        } else {
            this.field_70159_w = 0.0D;
            this.field_70179_y = 0.0D;
        }

        this.func_71029_a(StatList.field_188069_A);
        this.func_175145_a(StatList.field_188098_h);
        this.func_70066_B();
        this.func_70052_a(0, false);
    }

    protected void func_190776_cN() {
        for (int i = 0; i < this.field_71071_by.func_70302_i_(); ++i) {
            ItemStack itemstack = this.field_71071_by.func_70301_a(i);

            if (!itemstack.func_190926_b() && EnchantmentHelper.func_190939_c(itemstack)) {
                this.field_71071_by.func_70304_b(i);
            }
        }

    }

    @Override
    protected SoundEvent func_184601_bQ(DamageSource damagesource) {
        return damagesource == DamageSource.field_76370_b ? SoundEvents.field_193806_fH : (damagesource == DamageSource.field_76369_e ? SoundEvents.field_193805_fG : SoundEvents.field_187800_eb);
    }

    @Override
    protected SoundEvent func_184615_bR() {
        return SoundEvents.field_187798_ea;
    }

    @Nullable
    public EntityItem func_71040_bB(boolean flag) {
        // Called only when dropped by Q or CTRL-Q
        return this.func_146097_a(this.field_71071_by.func_70298_a(this.field_71071_by.field_70461_c, flag && !this.field_71071_by.func_70448_g().func_190926_b() ? this.field_71071_by.func_70448_g().func_190916_E() : 1), false, true);
    }

    @Nullable
    public EntityItem func_71019_a(ItemStack itemstack, boolean flag) {
        return this.func_146097_a(itemstack, false, flag);
    }

    @Nullable
    public EntityItem func_146097_a(ItemStack itemstack, boolean flag, boolean flag1) {
        if (itemstack.func_190926_b()) {
            return null;
        } else {
            double d0 = this.field_70163_u - 0.30000001192092896D + this.func_70047_e();
            EntityItem entityitem = new EntityItem(this.field_70170_p, this.field_70165_t, d0, this.field_70161_v, itemstack);

            entityitem.func_174867_a(40);
            if (flag1) {
                entityitem.func_145799_b(this.func_70005_c_());
            }

            float f;
            float f1;

            if (flag) {
                f = this.field_70146_Z.nextFloat() * 0.5F;
                f1 = this.field_70146_Z.nextFloat() * 6.2831855F;
                entityitem.field_70159_w = -MathHelper.func_76126_a(f1) * f;
                entityitem.field_70179_y = MathHelper.func_76134_b(f1) * f;
                entityitem.field_70181_x = 0.20000000298023224D;
            } else {
                f = 0.3F;
                entityitem.field_70159_w = -MathHelper.func_76126_a(this.field_70177_z * 0.017453292F) * MathHelper.func_76134_b(this.field_70125_A * 0.017453292F) * f;
                entityitem.field_70179_y = MathHelper.func_76134_b(this.field_70177_z * 0.017453292F) * MathHelper.func_76134_b(this.field_70125_A * 0.017453292F) * f;
                entityitem.field_70181_x = -MathHelper.func_76126_a(this.field_70125_A * 0.017453292F) * f + 0.1F;
                f1 = this.field_70146_Z.nextFloat() * 6.2831855F;
                f = 0.02F * this.field_70146_Z.nextFloat();
                entityitem.field_70159_w += Math.cos(f1) * f;
                entityitem.field_70181_x += (this.field_70146_Z.nextFloat() - this.field_70146_Z.nextFloat()) * 0.1F;
                entityitem.field_70179_y += Math.sin(f1) * f;
            }

            // CraftBukkit start - fire PlayerDropItemEvent
            Player player = (Player) this.getBukkitEntity();
            CraftItem drop = new CraftItem(this.field_70170_p.getServer(), entityitem);

            PlayerDropItemEvent event = new PlayerDropItemEvent(player, drop);
            this.field_70170_p.getServer().getPluginManager().callEvent(event);

            if (event.isCancelled()) {
                org.bukkit.inventory.ItemStack cur = player.getInventory().getItemInHand();
                if (flag1 && (cur == null || cur.getAmount() == 0)) {
                    // The complete stack was dropped
                    player.getInventory().setItemInHand(drop.getItemStack());
                } else if (flag1 && cur.isSimilar(drop.getItemStack()) && cur.getAmount() < cur.getMaxStackSize() && drop.getItemStack().getAmount() == 1) {
                    // Only one item is dropped
                    cur.setAmount(cur.getAmount() + 1);
                    player.getInventory().setItemInHand(cur);
                } else {
                    // Fallback
                    player.getInventory().addItem(drop.getItemStack());
                }
                return null;
            }
            // CraftBukkit end
            // Paper start - remove player from map on drop
            if (itemstack.func_77973_b() == Items.field_151098_aY) {
                MapData worldmap = Items.field_151098_aY.func_77873_a(itemstack, this.field_70170_p);
                worldmap.updateSeenPlayers(this, itemstack);
            }
            // Paper stop

            ItemStack itemstack1 = this.func_184816_a(entityitem);

            if (flag1) {
                if (!itemstack1.func_190926_b()) {
                    this.func_71064_a(StatList.func_188058_e(itemstack1.func_77973_b()), itemstack.func_190916_E());
                }

                this.func_71029_a(StatList.field_75952_v);
            }

            return entityitem;
        }
    }

    protected ItemStack func_184816_a(EntityItem entityitem) {
        this.field_70170_p.func_72838_d(entityitem);
        return entityitem.func_92059_d();
    }

    public float func_184813_a(IBlockState iblockdata) {
        float f = this.field_71071_by.func_184438_a(iblockdata);

        if (f > 1.0F) {
            int i = EnchantmentHelper.func_185293_e(this);
            ItemStack itemstack = this.func_184614_ca();

            if (i > 0 && !itemstack.func_190926_b()) {
                f += i * i + 1;
            }
        }

        if (this.func_70644_a(MobEffects.field_76422_e)) {
            f *= 1.0F + (this.func_70660_b(MobEffects.field_76422_e).func_76458_c() + 1) * 0.2F;
        }

        if (this.func_70644_a(MobEffects.field_76419_f)) {
            float f1;

            switch (this.func_70660_b(MobEffects.field_76419_f).func_76458_c()) {
            case 0:
                f1 = 0.3F;
                break;

            case 1:
                f1 = 0.09F;
                break;

            case 2:
                f1 = 0.0027F;
                break;

            case 3:
            default:
                f1 = 8.1E-4F;
            }

            f *= f1;
        }

        if (this.func_70055_a(Material.field_151586_h) && !EnchantmentHelper.func_185287_i(this)) {
            f /= 5.0F;
        }

        if (!this.field_70122_E) {
            f /= 5.0F;
        }

        return f;
    }

    public boolean func_184823_b(IBlockState iblockdata) {
        return this.field_71071_by.func_184432_b(iblockdata);
    }

    public static void func_189806_a(DataFixer dataconvertermanager) {
        dataconvertermanager.func_188258_a(FixTypes.PLAYER, new IDataWalker() {
            @Override
            public NBTTagCompound func_188266_a(IDataFixer dataconverter, NBTTagCompound nbttagcompound, int i) {
                DataFixesManager.func_188278_b(dataconverter, nbttagcompound, i, "Inventory");
                DataFixesManager.func_188278_b(dataconverter, nbttagcompound, i, "EnderItems");
                if (nbttagcompound.func_150297_b("ShoulderEntityLeft", 10)) {
                    nbttagcompound.func_74782_a("ShoulderEntityLeft", dataconverter.func_188251_a(FixTypes.ENTITY, nbttagcompound.func_74775_l("ShoulderEntityLeft"), i));
                }

                if (nbttagcompound.func_150297_b("ShoulderEntityRight", 10)) {
                    nbttagcompound.func_74782_a("ShoulderEntityRight", dataconverter.func_188251_a(FixTypes.ENTITY, nbttagcompound.func_74775_l("ShoulderEntityRight"), i));
                }

                return nbttagcompound;
            }
        });
    }

    @Override
    public void func_70037_a(NBTTagCompound nbttagcompound) {
        super.func_70037_a(nbttagcompound);
        this.func_184221_a(func_146094_a(this.field_146106_i));
        NBTTagList nbttaglist = nbttagcompound.func_150295_c("Inventory", 10);

        this.field_71071_by.func_70443_b(nbttaglist);
        this.field_71071_by.field_70461_c = nbttagcompound.func_74762_e("SelectedItemSlot");
        this.field_71083_bS = nbttagcompound.func_74767_n("Sleeping");
        this.field_71076_b = nbttagcompound.func_74765_d("SleepTimer");
        this.field_71106_cc = nbttagcompound.func_74760_g("XpP");
        this.field_71068_ca = nbttagcompound.func_74762_e("XpLevel");
        this.field_71067_cb = nbttagcompound.func_74762_e("XpTotal");
        this.field_175152_f = nbttagcompound.func_74762_e("XpSeed");
        if (this.field_175152_f == 0) {
            this.field_175152_f = this.field_70146_Z.nextInt();
        }

        this.func_85040_s(nbttagcompound.func_74762_e("Score"));
        if (this.field_71083_bS) {
            this.field_71081_bT = new BlockPos(this);
            this.func_70999_a(true, true, false);
        }

        // CraftBukkit start
        this.spawnWorld = nbttagcompound.func_74779_i("SpawnWorld");
        if ("".equals(spawnWorld)) {
            this.spawnWorld = this.field_70170_p.getServer().getWorlds().get(0).getName();
        }
        // CraftBukkit end

        if (nbttagcompound.func_150297_b("SpawnX", 99) && nbttagcompound.func_150297_b("SpawnY", 99) && nbttagcompound.func_150297_b("SpawnZ", 99)) {
            this.field_71077_c = new BlockPos(nbttagcompound.func_74762_e("SpawnX"), nbttagcompound.func_74762_e("SpawnY"), nbttagcompound.func_74762_e("SpawnZ"));
            this.field_82248_d = nbttagcompound.func_74767_n("SpawnForced");
        }

        this.field_71100_bB.func_75112_a(nbttagcompound);
        this.field_71075_bZ.func_75095_b(nbttagcompound);
        if (nbttagcompound.func_150297_b("EnderItems", 9)) {
            NBTTagList nbttaglist1 = nbttagcompound.func_150295_c("EnderItems", 10);

            this.field_71078_a.func_70486_a(nbttaglist1);
        }

        if (nbttagcompound.func_150297_b("ShoulderEntityLeft", 10)) {
            this.func_192029_h(nbttagcompound.func_74775_l("ShoulderEntityLeft"));
        }

        if (nbttagcompound.func_150297_b("ShoulderEntityRight", 10)) {
            this.func_192031_i(nbttagcompound.func_74775_l("ShoulderEntityRight"));
        }

    }

    @Override
    public void func_70014_b(NBTTagCompound nbttagcompound) {
        super.func_70014_b(nbttagcompound);
        nbttagcompound.func_74768_a("DataVersion", 1343);
        nbttagcompound.func_74782_a("Inventory", this.field_71071_by.func_70442_a(new NBTTagList()));
        nbttagcompound.func_74768_a("SelectedItemSlot", this.field_71071_by.field_70461_c);
        nbttagcompound.func_74757_a("Sleeping", this.field_71083_bS);
        nbttagcompound.func_74777_a("SleepTimer", (short) this.field_71076_b);
        nbttagcompound.func_74776_a("XpP", this.field_71106_cc);
        nbttagcompound.func_74768_a("XpLevel", this.field_71068_ca);
        nbttagcompound.func_74768_a("XpTotal", this.field_71067_cb);
        nbttagcompound.func_74768_a("XpSeed", this.field_175152_f);
        nbttagcompound.func_74768_a("Score", this.func_71037_bA());
        if (this.field_71077_c != null) {
            nbttagcompound.func_74768_a("SpawnX", this.field_71077_c.func_177958_n());
            nbttagcompound.func_74768_a("SpawnY", this.field_71077_c.func_177956_o());
            nbttagcompound.func_74768_a("SpawnZ", this.field_71077_c.func_177952_p());
            nbttagcompound.func_74757_a("SpawnForced", this.field_82248_d);
        }

        this.field_71100_bB.func_75117_b(nbttagcompound);
        this.field_71075_bZ.func_75091_a(nbttagcompound);
        nbttagcompound.func_74782_a("EnderItems", this.field_71078_a.func_70487_g());
        if (!this.func_192023_dk().func_82582_d()) {
            nbttagcompound.func_74782_a("ShoulderEntityLeft", this.func_192023_dk());
        }

        if (!this.func_192025_dl().func_82582_d()) {
            nbttagcompound.func_74782_a("ShoulderEntityRight", this.func_192025_dl());
        }
        nbttagcompound.func_74778_a("SpawnWorld", spawnWorld); // CraftBukkit - fixes bed spawns for multiworld worlds

    }

    @Override
    public boolean func_70097_a(DamageSource damagesource, float f) {
        if (this.func_180431_b(damagesource)) {
            return false;
        } else if (this.field_71075_bZ.field_75102_a && !damagesource.func_76357_e()) {
            return false;
        } else {
            this.field_70708_bq = 0;
            if (this.func_110143_aJ() <= 0.0F) {
                return false;
            } else {
                if (this.func_70608_bn() && !this.field_70170_p.field_72995_K) {
                    this.func_70999_a(true, true, false);
                }

                // this.releaseShoulderEntities(); // CraftBukkit - moved down
                if (damagesource.func_76350_n()) {
                    if (this.field_70170_p.func_175659_aa() == EnumDifficulty.PEACEFUL) {
                        return false; // CraftBukkit - f = 0.0f -> return false
                    }

                    if (this.field_70170_p.func_175659_aa() == EnumDifficulty.EASY) {
                        f = Math.min(f / 2.0F + 1.0F, f);
                    }

                    if (this.field_70170_p.func_175659_aa() == EnumDifficulty.HARD) {
                        f = f * 3.0F / 2.0F;
                    }
                }

                // CraftBukkit start - Don't filter out 0 damage
                boolean damaged = super.func_70097_a(damagesource, f);
                if (damaged) {
                    this.func_192030_dh();
                }
                return damaged;
                // CraftBukkit end
            }
        }
    }

    @Override
    protected void func_190629_c(EntityLivingBase entityliving) {
        super.func_190629_c(entityliving);
        if (entityliving.func_184614_ca().func_77973_b() instanceof ItemAxe) {
            this.func_190777_m(true);
        }

    }

    public boolean func_96122_a(EntityPlayer entityhuman) {
        // CraftBukkit start - Change to check OTHER player's scoreboard team according to API
        // To summarize this method's logic, it's "Can parameter hurt this"
        org.bukkit.scoreboard.Team team;
        if (entityhuman instanceof EntityPlayerMP) {
            EntityPlayerMP thatPlayer = (EntityPlayerMP) entityhuman;
            team = thatPlayer.getBukkitEntity().getScoreboard().getPlayerTeam(thatPlayer.getBukkitEntity());
            if (team == null || team.allowFriendlyFire()) {
                return true;
            }
        } else {
            // This should never be called, but is implemented anyway
            org.bukkit.OfflinePlayer thisPlayer = entityhuman.field_70170_p.getServer().getOfflinePlayer(entityhuman.func_70005_c_());
            team = entityhuman.field_70170_p.getServer().getScoreboardManager().getMainScoreboard().getPlayerTeam(thisPlayer);
            if (team == null || team.allowFriendlyFire()) {
                return true;
            }
        }

        if (this instanceof EntityPlayerMP) {
            return !team.hasPlayer(((EntityPlayerMP) this).getBukkitEntity());
        }
        return !team.hasPlayer(this.field_70170_p.getServer().getOfflinePlayer(this.func_70005_c_()));
        // CraftBukkit end
    }

    @Override
    protected void func_70675_k(float f) {
        this.field_71071_by.func_70449_g(f);
    }

    @Override
    protected void func_184590_k(float f) {
        if (f >= 3.0F && this.field_184627_bm.func_77973_b() == Items.field_185159_cQ) {
            int i = 1 + MathHelper.func_76141_d(f);

            this.field_184627_bm.func_77972_a(i, this);
            if (this.field_184627_bm.func_190926_b()) {
                EnumHand enumhand = this.func_184600_cs();

                if (enumhand == EnumHand.MAIN_HAND) {
                    this.func_184201_a(EntityEquipmentSlot.MAINHAND, ItemStack.field_190927_a);
                } else {
                    this.func_184201_a(EntityEquipmentSlot.OFFHAND, ItemStack.field_190927_a);
                }

                this.field_184627_bm = ItemStack.field_190927_a;
                this.func_184185_a(SoundEvents.field_187769_eM, 0.8F, 0.8F + this.field_70170_p.field_73012_v.nextFloat() * 0.4F);
            }
        }

    }

    public float func_82243_bO() {
        int i = 0;
        Iterator iterator = this.field_71071_by.field_70460_b.iterator();

        while (iterator.hasNext()) {
            ItemStack itemstack = (ItemStack) iterator.next();

            if (!itemstack.func_190926_b()) {
                ++i;
            }
        }

        return (float) i / (float) this.field_71071_by.field_70460_b.size();
    }

    // CraftBukkit start
    @Override
    protected boolean damageEntity0(DamageSource damagesource, float f) { // void -> boolean
        if (true) {
            return super.damageEntity0(damagesource, f);
        }
        // CraftBukkit end
        if (!this.func_180431_b(damagesource)) {
            f = this.func_70655_b(damagesource, f);
            f = this.func_70672_c(damagesource, f);
            float f1 = f;

            f = Math.max(f - this.func_110139_bj(), 0.0F);
            this.func_110149_m(this.func_110139_bj() - (f1 - f));
            if (f != 0.0F) {
                this.func_71020_j(damagesource.func_76345_d());
                float f2 = this.func_110143_aJ();

                this.func_70606_j(this.func_110143_aJ() - f);
                this.func_110142_aN().func_94547_a(damagesource, f2, f);
                if (f < 3.4028235E37F) {
                    this.func_71064_a(StatList.field_188112_z, Math.round(f * 10.0F));
                }

            }
        }
        return false; // CraftBukkit
    }

    public void func_175141_a(TileEntitySign tileentitysign) {}

    public void func_184809_a(CommandBlockBaseLogic commandblocklistenerabstract) {}

    public void func_184824_a(TileEntityCommandBlock tileentitycommand) {}

    public void func_189807_a(TileEntityStructure tileentitystructure) {}

    public void func_180472_a(IMerchant imerchant) {}

    public void func_71007_a(IInventory iinventory) {}

    public void func_184826_a(AbstractHorse entityhorseabstract, IInventory iinventory) {}

    public void func_180468_a(IInteractionObject itileentitycontainer) {}

    public void func_184814_a(ItemStack itemstack, EnumHand enumhand) {}

    public EnumActionResult func_190775_a(Entity entity, EnumHand enumhand) {
        if (this.func_175149_v()) {
            if (entity instanceof IInventory) {
                this.func_71007_a((IInventory) entity);
            }

            return EnumActionResult.PASS;
        } else {
            ItemStack itemstack = this.func_184586_b(enumhand);
            ItemStack itemstack1 = itemstack.func_190926_b() ? ItemStack.field_190927_a : itemstack.func_77946_l();

            if (entity.func_184230_a(this, enumhand)) {
                if (this.field_71075_bZ.field_75098_d && itemstack == this.func_184586_b(enumhand) && itemstack.func_190916_E() < itemstack1.func_190916_E()) {
                    itemstack.func_190920_e(itemstack1.func_190916_E());
                }

                return EnumActionResult.SUCCESS;
            } else {
                if (!itemstack.func_190926_b() && entity instanceof EntityLivingBase) {
                    if (this.field_71075_bZ.field_75098_d) {
                        itemstack = itemstack1;
                    }

                    if (itemstack.func_111282_a(this, (EntityLivingBase) entity, enumhand)) {
                        if (itemstack.func_190926_b() && !this.field_71075_bZ.field_75098_d) {
                            this.func_184611_a(enumhand, ItemStack.field_190927_a);
                        }

                        return EnumActionResult.SUCCESS;
                    }
                }

                return EnumActionResult.PASS;
            }
        }
    }

    @Override
    public double func_70033_W() {
        return -0.35D;
    }

    @Override
    public void func_184210_p() {
        super.func_184210_p();
        this.field_184245_j = 0;
    }

    // Paper start - send SoundEffect to everyone who can see fromEntity
    private static void sendSoundEffect(EntityPlayer fromEntity, double x, double y, double z, SoundEvent soundEffect, SoundCategory soundCategory, float volume, float pitch) {
        fromEntity.field_70170_p.sendSoundEffect(fromEntity, x, y, z, soundEffect, soundCategory, volume, pitch); // This will not send the effect to the entity himself
        if (fromEntity instanceof EntityPlayerMP) {
            ((EntityPlayerMP) fromEntity).field_71135_a.func_147359_a(new SPacketSoundEffect(soundEffect, soundCategory, x, y, z, volume, pitch));
        }
    }
    // Paper end

    public void func_71059_n(Entity entity) {
        if (entity.func_70075_an()) {
            if (!entity.func_85031_j(this)) {
                float f = (float) this.func_110148_a(SharedMonsterAttributes.field_111264_e).func_111126_e();
                float f1;

                if (entity instanceof EntityLivingBase) {
                    f1 = EnchantmentHelper.func_152377_a(this.func_184614_ca(), ((EntityLivingBase) entity).func_70668_bt());
                } else {
                    f1 = EnchantmentHelper.func_152377_a(this.func_184614_ca(), EnumCreatureAttribute.UNDEFINED);
                }

                float f2 = this.func_184825_o(0.5F);

                f *= 0.2F + f2 * f2 * 0.8F;
                f1 *= f2;
                this.func_184821_cY();
                if (f > 0.0F || f1 > 0.0F) {
                    boolean flag = f2 > 0.9F;
                    boolean flag1 = false;
                    byte b0 = 0;
                    int i = b0 + EnchantmentHelper.func_77501_a(this);

                    if (this.func_70051_ag() && flag) {
                        sendSoundEffect(this, this.field_70165_t, this.field_70163_u, this.field_70161_v, SoundEvents.field_187721_dT, this.func_184176_by(), 1.0F, 1.0F); // Paper - send while respecting visibility
                        ++i;
                        flag1 = true;
                    }

                    boolean flag2 = flag && this.field_70143_R > 0.0F && !this.field_70122_E && !this.func_70617_f_() && !this.func_70090_H() && !this.func_70644_a(MobEffects.field_76440_q) && !this.func_184218_aH() && entity instanceof EntityLivingBase;
                    flag2 = flag2 && !field_70170_p.paperConfig.disablePlayerCrits; // Paper
                    flag2 = flag2 && !this.func_70051_ag();
                    if (flag2) {
                        f *= 1.5F;
                    }

                    f += f1;
                    boolean flag3 = false;
                    double d0 = this.field_70140_Q - this.field_70141_P;

                    if (flag && !flag2 && !flag1 && this.field_70122_E && d0 < this.func_70689_ay()) {
                        ItemStack itemstack = this.func_184586_b(EnumHand.MAIN_HAND);

                        if (itemstack.func_77973_b() instanceof ItemSword) {
                            flag3 = true;
                        }
                    }

                    float f3 = 0.0F;
                    boolean flag4 = false;
                    int j = EnchantmentHelper.func_90036_a(this);

                    if (entity instanceof EntityLivingBase) {
                        f3 = ((EntityLivingBase) entity).func_110143_aJ();
                        if (j > 0 && !entity.func_70027_ad()) {
                            // CraftBukkit start - Call a combust event when somebody hits with a fire enchanted item
                            EntityCombustByEntityEvent combustEvent = new EntityCombustByEntityEvent(this.getBukkitEntity(), entity.getBukkitEntity(), 1);
                            org.bukkit.Bukkit.getPluginManager().callEvent(combustEvent);

                            if (!combustEvent.isCancelled()) {
                                flag4 = true;
                                entity.func_70015_d(combustEvent.getDuration());
                            }
                            // CraftBukkit end
                        }
                    }

                    double d1 = entity.field_70159_w;
                    double d2 = entity.field_70181_x;
                    double d3 = entity.field_70179_y;
                    boolean flag5 = entity.func_70097_a(DamageSource.func_76365_a(this), f);

                    if (flag5) {
                        if (i > 0) {
                            if (entity instanceof EntityLivingBase) {
                                ((EntityLivingBase) entity).func_70653_a(this, i * 0.5F, MathHelper.func_76126_a(this.field_70177_z * 0.017453292F), (-MathHelper.func_76134_b(this.field_70177_z * 0.017453292F)));
                            } else {
                                entity.func_70024_g(-MathHelper.func_76126_a(this.field_70177_z * 0.017453292F) * i * 0.5F, 0.1D, MathHelper.func_76134_b(this.field_70177_z * 0.017453292F) * i * 0.5F);
                            }

                            this.field_70159_w *= 0.6D;
                            this.field_70179_y *= 0.6D;
                            // Paper start - Configuration option to disable automatic sprint interruption
                            if (!field_70170_p.paperConfig.disableSprintInterruptionOnAttack) {
                                this.func_70031_b(false);
                            }
                            // Paper end
                        }

                        if (flag3) {
                            float f4 = 1.0F + EnchantmentHelper.func_191527_a(this) * f;
                            List list = this.field_70170_p.func_72872_a(EntityLivingBase.class, entity.func_174813_aQ().func_72314_b(1.0D, 0.25D, 1.0D));
                            Iterator iterator = list.iterator();

                            while (iterator.hasNext()) {
                                EntityLivingBase entityliving = (EntityLivingBase) iterator.next();

                                if (entityliving != this && entityliving != entity && !this.func_184191_r(entityliving) && this.func_70068_e(entityliving) < 9.0D) {
                                    // CraftBukkit start - Only apply knockback if the damage hits
                                    if (entityliving.func_70097_a(DamageSource.func_76365_a(this).sweep(), f4)) {
                                    entityliving.func_70653_a(this, 0.4F, MathHelper.func_76126_a(this.field_70177_z * 0.017453292F), (-MathHelper.func_76134_b(this.field_70177_z * 0.017453292F)));
                                    }
                                    // CraftBukkit end
                                }
                            }

                            sendSoundEffect(this, this.field_70165_t, this.field_70163_u, this.field_70161_v, SoundEvents.field_187730_dW, this.func_184176_by(), 1.0F, 1.0F); // Paper - send while respecting visibility
                            this.func_184810_cG();
                        }

                        if (entity instanceof EntityPlayerMP && entity.field_70133_I) {
                            // CraftBukkit start - Add Velocity Event
                            boolean cancelled = false;
                            Player player = (Player) entity.getBukkitEntity();
                            org.bukkit.util.Vector velocity = new Vector( d1, d2, d3 );

                            PlayerVelocityEvent event = new PlayerVelocityEvent(player, velocity.clone());
                            field_70170_p.getServer().getPluginManager().callEvent(event);

                            if (event.isCancelled()) {
                                cancelled = true;
                            } else if (!velocity.equals(event.getVelocity())) {
                                player.setVelocity(event.getVelocity());
                            }

                            if (!cancelled) {
                            ((EntityPlayerMP) entity).field_71135_a.func_147359_a(new SPacketEntityVelocity(entity));
                            entity.field_70133_I = false;
                            entity.field_70159_w = d1;
                            entity.field_70181_x = d2;
                            entity.field_70179_y = d3;
                            }
                            // CraftBukkit end
                        }

                        if (flag2) {
                            sendSoundEffect(this, this.field_70165_t, this.field_70163_u, this.field_70161_v, SoundEvents.field_187718_dS, this.func_184176_by(), 1.0F, 1.0F); // Paper - send while respecting visibility
                            this.func_71009_b(entity);
                        }

                        if (!flag2 && !flag3) {
                            if (flag) {
                                sendSoundEffect(this, this.field_70165_t, this.field_70163_u, this.field_70161_v, SoundEvents.field_187727_dV, this.func_184176_by(), 1.0F, 1.0F); // Paper - send while respecting visibility
                            } else {
                                sendSoundEffect(this, this.field_70165_t, this.field_70163_u, this.field_70161_v, SoundEvents.field_187733_dX, this.func_184176_by(), 1.0F, 1.0F); // Paper - send while respecting visibility
                            }
                        }

                        if (f1 > 0.0F) {
                            this.func_71047_c(entity);
                        }

                        this.func_130011_c(entity);
                        if (entity instanceof EntityLivingBase) {
                            EnchantmentHelper.func_151384_a((EntityLivingBase) entity, (Entity) this);
                        }

                        EnchantmentHelper.func_151385_b(this, entity);
                        ItemStack itemstack1 = this.func_184614_ca();
                        Object object = entity;

                        if (entity instanceof MultiPartEntityPart) {
                            IEntityMultiPart icomplex = ((MultiPartEntityPart) entity).field_70259_a;

                            if (icomplex instanceof EntityLivingBase) {
                                object = icomplex;
                            }
                        }

                        if (!itemstack1.func_190926_b() && object instanceof EntityLivingBase) {
                            itemstack1.func_77961_a((EntityLivingBase) object, this);
                            if (itemstack1.func_190926_b()) {
                                this.func_184611_a(EnumHand.MAIN_HAND, ItemStack.field_190927_a);
                            }
                        }

                        if (entity instanceof EntityLivingBase) {
                            float f5 = f3 - ((EntityLivingBase) entity).func_110143_aJ();

                            this.func_71064_a(StatList.field_188111_y, Math.round(f5 * 10.0F));
                            if (j > 0) {
                                // CraftBukkit start - Call a combust event when somebody hits with a fire enchanted item
                                EntityCombustByEntityEvent combustEvent = new EntityCombustByEntityEvent(this.getBukkitEntity(), entity.getBukkitEntity(), j * 4);
                                org.bukkit.Bukkit.getPluginManager().callEvent(combustEvent);

                                if (!combustEvent.isCancelled()) {
                                    entity.func_70015_d(combustEvent.getDuration());
                                }
                                // CraftBukkit end
                            }

                            if (this.field_70170_p instanceof WorldServer && f5 > 2.0F) {
                                int k = (int) (f5 * 0.5D);

                                ((WorldServer) this.field_70170_p).func_175739_a(EnumParticleTypes.DAMAGE_INDICATOR, entity.field_70165_t, entity.field_70163_u + entity.field_70131_O * 0.5F, entity.field_70161_v, k, 0.1D, 0.0D, 0.1D, 0.2D, new int[0]);
                            }
                        }

                        this.func_71020_j(field_70170_p.spigotConfig.combatExhaustion); // Spigot - Change to use configurable value
                    } else {
                        sendSoundEffect(this, this.field_70165_t, this.field_70163_u, this.field_70161_v, SoundEvents.field_187724_dU, this.func_184176_by(), 1.0F, 1.0F); // Paper - send while respecting visibility
                        if (flag4) {
                            entity.func_70066_B();
                        }
                        // CraftBukkit start - resync on cancelled event
                        if (this instanceof EntityPlayerMP) {
                            ((EntityPlayerMP) this).getBukkitEntity().updateInventory();
                        }
                        // CraftBukkit end
                    }
                }

            }
        }
    }

    public void func_190777_m(boolean flag) {
        float f = 0.25F + EnchantmentHelper.func_185293_e(this) * 0.05F;

        if (flag) {
            f += 0.75F;
        }

        if (this.field_70146_Z.nextFloat() < f) {
            this.func_184811_cZ().func_185145_a(Items.field_185159_cQ, 100);
            this.func_184602_cy();
            this.field_70170_p.func_72960_a(this, (byte) 30);
        }

    }

    public void func_71009_b(Entity entity) {}

    public void func_71047_c(Entity entity) {}

    public void func_184810_cG() {
        double d0 = (-MathHelper.func_76126_a(this.field_70177_z * 0.017453292F));
        double d1 = MathHelper.func_76134_b(this.field_70177_z * 0.017453292F);

        if (this.field_70170_p instanceof WorldServer) {
            ((WorldServer) this.field_70170_p).func_175739_a(EnumParticleTypes.SWEEP_ATTACK, this.field_70165_t + d0, this.field_70163_u + this.field_70131_O * 0.5D, this.field_70161_v + d1, 0, d0, 0.0D, d1, 0.0D, new int[0]);
        }

    }

    @Override
    public void func_70106_y() {
        super.func_70106_y();
        this.field_71069_bz.func_75134_a(this);
        if (this.field_71070_bA != null) {
            this.field_71070_bA.func_75134_a(this);
        }

    }

    @Override
    public boolean func_70094_T() {
        return !this.field_71083_bS && super.func_70094_T();
    }

    public boolean func_175144_cb() {
        return false;
    }

    public GameProfile func_146103_bH() {
        return this.field_146106_i;
    }

    public EntityPlayer.SleepResult func_180469_a(BlockPos blockposition) {
        EnumFacing enumdirection = this.field_70170_p.func_180495_p(blockposition).func_177229_b(BlockHorizontal.field_185512_D);

        if (!this.field_70170_p.field_72995_K) {
            if (this.func_70608_bn() || !this.func_70089_S()) {
                return EntityPlayer.SleepResult.OTHER_PROBLEM;
            }

            if (!this.field_70170_p.field_73011_w.func_76569_d()) {
                return EntityPlayer.SleepResult.NOT_POSSIBLE_HERE;
            }

            if (this.field_70170_p.func_72935_r()) {
                return EntityPlayer.SleepResult.NOT_POSSIBLE_NOW;
            }

            if (!this.func_190774_a(blockposition, enumdirection)) {
                return EntityPlayer.SleepResult.TOO_FAR_AWAY;
            }

            double d0 = 8.0D;
            double d1 = 5.0D;
            List list = this.field_70170_p.func_175647_a(EntityMob.class, new AxisAlignedBB(blockposition.func_177958_n() - 8.0D, blockposition.func_177956_o() - 5.0D, blockposition.func_177952_p() - 8.0D, blockposition.func_177958_n() + 8.0D, blockposition.func_177956_o() + 5.0D, blockposition.func_177952_p() + 8.0D), (Predicate) (new EntityPlayer.c(this, null)));

            if (!list.isEmpty()) {
                return EntityPlayer.SleepResult.NOT_SAFE;
            }
        }

        if (this.func_184218_aH()) {
            this.func_184210_p();
        }

        // CraftBukkit start - fire PlayerBedEnterEvent
        if (this.getBukkitEntity() instanceof Player) {
            Player player = (Player) this.getBukkitEntity();
            org.bukkit.block.Block bed = this.field_70170_p.getWorld().getBlockAt(blockposition.func_177958_n(), blockposition.func_177956_o(), blockposition.func_177952_p());

            PlayerBedEnterEvent event = new PlayerBedEnterEvent(player, bed);
            this.field_70170_p.getServer().getPluginManager().callEvent(event);

            if (event.isCancelled()) {
                return SleepResult.OTHER_PROBLEM;
            }
        }
        // CraftBukkit end

        this.func_192030_dh();
        this.func_70105_a(0.2F, 0.2F);
        if (this.field_70170_p.func_175667_e(blockposition)) {
            float f = 0.5F + enumdirection.func_82601_c() * 0.4F;
            float f1 = 0.5F + enumdirection.func_82599_e() * 0.4F;

            this.func_175139_a(enumdirection);
            this.func_70107_b(blockposition.func_177958_n() + f, blockposition.func_177956_o() + 0.6875F, blockposition.func_177952_p() + f1);
        } else {
            this.func_70107_b(blockposition.func_177958_n() + 0.5F, blockposition.func_177956_o() + 0.6875F, blockposition.func_177952_p() + 0.5F);
        }

        this.field_71083_bS = true;
        this.field_71076_b = 0;
        this.field_71081_bT = blockposition;
        this.field_70159_w = 0.0D;
        this.field_70181_x = 0.0D;
        this.field_70179_y = 0.0D;
        if (!this.field_70170_p.field_72995_K) {
            this.field_70170_p.func_72854_c();
        }

        return EntityPlayer.SleepResult.OK;
    }

    private boolean func_190774_a(BlockPos blockposition, EnumFacing enumdirection) {
        if (Math.abs(this.field_70165_t - blockposition.func_177958_n()) <= 3.0D && Math.abs(this.field_70163_u - blockposition.func_177956_o()) <= 2.0D && Math.abs(this.field_70161_v - blockposition.func_177952_p()) <= 3.0D) {
            return true;
        } else {
            BlockPos blockposition1 = blockposition.func_177972_a(enumdirection.func_176734_d());

            return Math.abs(this.field_70165_t - blockposition1.func_177958_n()) <= 3.0D && Math.abs(this.field_70163_u - blockposition1.func_177956_o()) <= 2.0D && Math.abs(this.field_70161_v - blockposition1.func_177952_p()) <= 3.0D;
        }
    }

    private void func_175139_a(EnumFacing enumdirection) {
        this.field_71079_bU = -1.8F * enumdirection.func_82601_c();
        this.field_71089_bV = -1.8F * enumdirection.func_82599_e();
    }

    public void func_70999_a(boolean flag, boolean flag1, boolean flag2) {
        this.func_70105_a(0.6F, 1.8F);
        IBlockState iblockdata = this.field_70170_p.func_180495_p(this.field_71081_bT);

        if (this.field_71081_bT != null && iblockdata.func_177230_c() == Blocks.field_150324_C) {
            this.field_70170_p.func_180501_a(this.field_71081_bT, iblockdata.func_177226_a(BlockBed.field_176471_b, Boolean.valueOf(false)), 4);
            BlockPos blockposition = BlockBed.func_176468_a(this.field_70170_p, this.field_71081_bT, 0);

            if (blockposition == null) {
                blockposition = this.field_71081_bT.func_177984_a();
            }

            this.func_70107_b(blockposition.func_177958_n() + 0.5F, blockposition.func_177956_o() + 0.1F, blockposition.func_177952_p() + 0.5F);
        }

        this.field_71083_bS = false;
        if (!this.field_70170_p.field_72995_K && flag1) {
            this.field_70170_p.func_72854_c();
        }

        // CraftBukkit start - fire PlayerBedLeaveEvent
        if (this.getBukkitEntity() instanceof Player) {
            Player player = (Player) this.getBukkitEntity();

            org.bukkit.block.Block bed;
            BlockPos blockposition = this.field_71081_bT;
            if (blockposition != null) {
                bed = this.field_70170_p.getWorld().getBlockAt(blockposition.func_177958_n(), blockposition.func_177956_o(), blockposition.func_177952_p());
            } else {
                bed = this.field_70170_p.getWorld().getBlockAt(player.getLocation());
            }

            PlayerBedLeaveEvent event = new PlayerBedLeaveEvent(player, bed);
            this.field_70170_p.getServer().getPluginManager().callEvent(event);
        }
        // CraftBukkit end

        this.field_71076_b = flag ? 0 : 100;
        if (flag2) {
            this.func_180473_a(this.field_71081_bT, false);
        }

    }

    private boolean func_175143_p() {
        return this.field_70170_p.func_180495_p(this.field_71081_bT).func_177230_c() == Blocks.field_150324_C;
    }

    @Nullable
    public static BlockPos func_180467_a(World world, BlockPos blockposition, boolean flag) {
        Block block = world.func_180495_p(blockposition).func_177230_c();

        if (block != Blocks.field_150324_C) {
            if (!flag) {
                return null;
            } else {
                boolean flag1 = block.func_181623_g();
                boolean flag2 = world.func_180495_p(blockposition.func_177984_a()).func_177230_c().func_181623_g();

                return flag1 && flag2 ? blockposition : null;
            }
        } else {
            return BlockBed.func_176468_a(world, blockposition, 0);
        }
    }

    @Override
    public boolean func_70608_bn() {
        return this.field_71083_bS;
    }

    public boolean func_71026_bH() {
        return this.field_71083_bS && this.field_71076_b >= 100;
    }

    public void func_146105_b(ITextComponent ichatbasecomponent, boolean flag) {}

    public BlockPos func_180470_cg() {
        return this.field_71077_c;
    }

    public boolean func_82245_bX() {
        return this.field_82248_d;
    }

    public void func_180473_a(BlockPos blockposition, boolean flag) {
        if (blockposition != null) {
            this.field_71077_c = blockposition;
            this.field_82248_d = flag;
            this.spawnWorld = this.field_70170_p.field_72986_A.func_76065_j(); // CraftBukkit
        } else {
            this.field_71077_c = null;
            this.field_82248_d = false;
            this.spawnWorld = ""; // CraftBukkit
        }

    }

    public void func_71029_a(StatBase statistic) {
        this.func_71064_a(statistic, 1);
    }

    public void func_71064_a(StatBase statistic, int i) {}

    public void func_175145_a(StatBase statistic) {}

    public void func_192021_a(List<IRecipe> list) {}

    public void func_193102_a(ResourceLocation[] aminecraftkey) {}

    public void func_192022_b(List<IRecipe> list) {}

    public void jump() { this.func_70664_aZ(); } // Paper - OBFHELPER
    @Override
    public void func_70664_aZ() {
        super.func_70664_aZ();
        this.func_71029_a(StatList.field_75953_u);
        if (this.func_70051_ag()) {
            this.func_71020_j(field_70170_p.spigotConfig.jumpSprintExhaustion); // Spigot - Change to use configurable value
        } else {
            this.func_71020_j(field_70170_p.spigotConfig.jumpWalkExhaustion); // Spigot - Change to use configurable value
        }

    }

    @Override
    public void func_191986_a(float f, float f1, float f2) {
        double d0 = this.field_70165_t;
        double d1 = this.field_70163_u;
        double d2 = this.field_70161_v;

        if (this.field_71075_bZ.field_75100_b && !this.func_184218_aH()) {
            double d3 = this.field_70181_x;
            float f3 = this.field_70747_aH;

            this.field_70747_aH = this.field_71075_bZ.func_75093_a() * (this.func_70051_ag() ? 2 : 1);
            super.func_191986_a(f, f1, f2);
            this.field_70181_x = d3 * 0.6D;
            this.field_70747_aH = f3;
            this.field_70143_R = 0.0F;
            // CraftBukkit start
            if (func_70083_f(7) && !org.bukkit.craftbukkit.event.CraftEventFactory.callToggleGlideEvent(this, false).isCancelled()) {
                this.func_70052_a(7, false);
            }
            // CraftBukkit end
        } else {
            super.func_191986_a(f, f1, f2);
        }

        this.func_71000_j(this.field_70165_t - d0, this.field_70163_u - d1, this.field_70161_v - d2);
    }

    @Override
    public float func_70689_ay() {
        return (float) this.func_110148_a(SharedMonsterAttributes.field_111263_d).func_111126_e();
    }

    public void func_71000_j(double d0, double d1, double d2) {
        if (!this.func_184218_aH()) {
            int i;

            if (this.func_70055_a(Material.field_151586_h)) {
                i = Math.round(MathHelper.func_76133_a(d0 * d0 + d1 * d1 + d2 * d2) * 100.0F);
                if (i > 0) {
                    this.func_71064_a(StatList.field_188105_q, i);
                    this.func_71020_j(field_70170_p.spigotConfig.swimMultiplier * i * 0.01F); // Spigot
                }
            } else if (this.func_70090_H()) {
                i = Math.round(MathHelper.func_76133_a(d0 * d0 + d2 * d2) * 100.0F);
                if (i > 0) {
                    this.func_71064_a(StatList.field_75946_m, i);
                    this.func_71020_j(field_70170_p.spigotConfig.swimMultiplier * i * 0.01F); // Spigot
                }
            } else if (this.func_70617_f_()) {
                if (d1 > 0.0D) {
                    this.func_71064_a(StatList.field_188103_o, (int) Math.round(d1 * 100.0D));
                }
            } else if (this.field_70122_E) {
                i = Math.round(MathHelper.func_76133_a(d0 * d0 + d2 * d2) * 100.0F);
                if (i > 0) {
                    if (this.func_70051_ag()) {
                        this.func_71064_a(StatList.field_188102_l, i);
                        this.func_71020_j(field_70170_p.spigotConfig.sprintMultiplier * i * 0.01F); // Spigot
                    } else if (this.func_70093_af()) {
                        this.func_71064_a(StatList.field_188101_k, i);
                        this.func_71020_j(field_70170_p.spigotConfig.otherMultiplier * i * 0.01F); // Spigot
                    } else {
                        this.func_71064_a(StatList.field_188100_j, i);
                        this.func_71020_j(field_70170_p.spigotConfig.otherMultiplier * i * 0.01F); // Spigot
                    }
                }
            } else if (this.func_184613_cA()) {
                i = Math.round(MathHelper.func_76133_a(d0 * d0 + d1 * d1 + d2 * d2) * 100.0F);
                this.func_71064_a(StatList.field_188110_v, i);
            } else {
                i = Math.round(MathHelper.func_76133_a(d0 * d0 + d2 * d2) * 100.0F);
                if (i > 25) {
                    this.func_71064_a(StatList.field_188104_p, i);
                }
            }

        }
    }

    private void func_71015_k(double d0, double d1, double d2) {
        if (this.func_184218_aH()) {
            int i = Math.round(MathHelper.func_76133_a(d0 * d0 + d1 * d1 + d2 * d2) * 100.0F);

            if (i > 0) {
                if (this.func_184187_bx() instanceof EntityMinecart) {
                    this.func_71064_a(StatList.field_188106_r, i);
                } else if (this.func_184187_bx() instanceof EntityBoat) {
                    this.func_71064_a(StatList.field_188107_s, i);
                } else if (this.func_184187_bx() instanceof EntityPig) {
                    this.func_71064_a(StatList.field_188108_t, i);
                } else if (this.func_184187_bx() instanceof AbstractHorse) {
                    this.func_71064_a(StatList.field_188109_u, i);
                }
            }
        }

    }

    @Override
    public void func_180430_e(float f, float f1) {
        if (!this.field_71075_bZ.field_75101_c) {
            if (f >= 2.0F) {
                this.func_71064_a(StatList.field_75943_n, (int) Math.round(f * 100.0D));
            }

            super.func_180430_e(f, f1);
        }
    }

    @Override
    protected void func_71061_d_() {
        if (!this.func_175149_v()) {
            super.func_71061_d_();
        }

    }

    @Override
    protected SoundEvent func_184588_d(int i) {
        return i > 4 ? SoundEvents.field_187736_dY : SoundEvents.field_187804_ed;
    }

    @Override
    public void func_70074_a(EntityLivingBase entityliving) {
        EntityList.EntityEggInfo entitytypes_monsteregginfo = EntityList.field_75627_a.get(EntityList.func_191301_a(entityliving));

        if (entitytypes_monsteregginfo != null) {
            this.func_71029_a(entitytypes_monsteregginfo.field_151512_d);
        }

    }

    @Override
    public void func_70110_aj() {
        if (!this.field_71075_bZ.field_75100_b) {
            super.func_70110_aj();
        }

    }

    public void func_71023_q(int i) {
        this.func_85039_t(i);
        int j = Integer.MAX_VALUE - this.field_71067_cb;

        if (i > j) {
            i = j;
        }

        this.field_71106_cc += (float) i / (float) this.func_71050_bK();

        for (this.field_71067_cb += i; this.field_71106_cc >= 1.0F; this.field_71106_cc /= this.func_71050_bK()) {
            this.field_71106_cc = (this.field_71106_cc - 1.0F) * this.func_71050_bK();
            this.func_82242_a(1);
        }

    }

    public int func_175138_ci() {
        return this.field_175152_f;
    }

    public void func_192024_a(ItemStack itemstack, int i) {
        this.field_71068_ca -= i;
        if (this.field_71068_ca < 0) {
            this.field_71068_ca = 0;
            this.field_71106_cc = 0.0F;
            this.field_71067_cb = 0;
        }

        this.field_175152_f = this.field_70146_Z.nextInt();
    }

    public void func_82242_a(int i) {
        this.field_71068_ca += i;
        if (this.field_71068_ca < 0) {
            this.field_71068_ca = 0;
            this.field_71106_cc = 0.0F;
            this.field_71067_cb = 0;
        }

        if (i > 0 && this.field_71068_ca % 5 == 0 && this.field_82249_h < this.field_70173_aa - 100.0F) {
            float f = this.field_71068_ca > 30 ? 1.0F : this.field_71068_ca / 30.0F;

            this.field_70170_p.func_184148_a((EntityPlayer) null, this.field_70165_t, this.field_70163_u, this.field_70161_v, SoundEvents.field_187802_ec, this.func_184176_by(), f * 0.75F, 1.0F);
            this.field_82249_h = this.field_70173_aa;
        }

    }

    public int func_71050_bK() {
        return this.field_71068_ca >= 30 ? 112 + (this.field_71068_ca - 30) * 9 : (this.field_71068_ca >= 15 ? 37 + (this.field_71068_ca - 15) * 5 : 7 + this.field_71068_ca * 2);
    }

    public void func_71020_j(float f) {
        if (!this.field_71075_bZ.field_75102_a) {
            if (!this.field_70170_p.field_72995_K) {
                this.field_71100_bB.func_75113_a(f);
            }

        }
    }

    public FoodStats func_71024_bL() {
        return this.field_71100_bB;
    }

    public boolean func_71043_e(boolean flag) {
        return (flag || this.field_71100_bB.func_75121_c()) && !this.field_71075_bZ.field_75102_a;
    }

    public boolean func_70996_bM() {
        return this.func_110143_aJ() > 0.0F && this.func_110143_aJ() < this.func_110138_aP();
    }

    public boolean func_175142_cm() {
        return this.field_71075_bZ.field_75099_e;
    }

    public boolean func_175151_a(BlockPos blockposition, EnumFacing enumdirection, ItemStack itemstack) {
        if (this.field_71075_bZ.field_75099_e) {
            return true;
        } else if (itemstack.func_190926_b()) {
            return false;
        } else {
            BlockPos blockposition1 = blockposition.func_177972_a(enumdirection.func_176734_d());
            Block block = this.field_70170_p.func_180495_p(blockposition1).func_177230_c();

            return itemstack.func_179547_d(block) || itemstack.func_82835_x();
        }
    }

    @Override
    protected int func_70693_a(EntityPlayer entityhuman) {
        if (!this.field_70170_p.func_82736_K().func_82766_b("keepInventory") && !this.func_175149_v()) {
            int i = this.field_71068_ca * 7;

            return i > 100 ? 100 : i;
        } else {
            return 0;
        }
    }

    @Override
    protected boolean func_70684_aJ() {
        return true;
    }

    @Override
    protected boolean func_70041_e_() {
        return !this.field_71075_bZ.field_75100_b;
    }

    public void func_71016_p() {}

    public void func_71033_a(GameType enumgamemode) {}

    @Override
    public String func_70005_c_() {
        return this.field_146106_i.getName();
    }

    public InventoryEnderChest func_71005_bN() {
        return this.field_71078_a;
    }

    @Override
    public ItemStack func_184582_a(EntityEquipmentSlot enumitemslot) {
        return enumitemslot == EntityEquipmentSlot.MAINHAND ? this.field_71071_by.func_70448_g() : (enumitemslot == EntityEquipmentSlot.OFFHAND ? (ItemStack) this.field_71071_by.field_184439_c.get(0) : (enumitemslot.func_188453_a() == EntityEquipmentSlot.Type.ARMOR ? (ItemStack) this.field_71071_by.field_70460_b.get(enumitemslot.func_188454_b()) : ItemStack.field_190927_a));
    }

    @Override
    public void func_184201_a(EntityEquipmentSlot enumitemslot, ItemStack itemstack) {
        if (enumitemslot == EntityEquipmentSlot.MAINHAND) {
            this.func_184606_a_(itemstack);
            this.field_71071_by.field_70462_a.set(this.field_71071_by.field_70461_c, itemstack);
        } else if (enumitemslot == EntityEquipmentSlot.OFFHAND) {
            this.func_184606_a_(itemstack);
            this.field_71071_by.field_184439_c.set(0, itemstack);
        } else if (enumitemslot.func_188453_a() == EntityEquipmentSlot.Type.ARMOR) {
            this.func_184606_a_(itemstack);
            this.field_71071_by.field_70460_b.set(enumitemslot.func_188454_b(), itemstack);
        }

    }

    public boolean func_191521_c(ItemStack itemstack) {
        this.func_184606_a_(itemstack);
        return this.field_71071_by.func_70441_a(itemstack);
    }

    @Override
    public Iterable<ItemStack> func_184214_aD() {
        return Lists.newArrayList(new ItemStack[] { this.func_184614_ca(), this.func_184592_cb()});
    }

    @Override
    public Iterable<ItemStack> func_184193_aE() {
        return this.field_71071_by.field_70460_b;
    }

    public boolean func_192027_g(NBTTagCompound nbttagcompound) {
        if (!this.func_184218_aH() && this.field_70122_E && !this.func_70090_H()) {
            if (this.func_192023_dk().func_82582_d()) {
                this.func_192029_h(nbttagcompound);
                return true;
            } else if (this.func_192025_dl().func_82582_d()) {
                this.func_192031_i(nbttagcompound);
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public void func_192030_dh() {
        // CraftBukkit start
        if (this.spawnEntityFromShoulder(this.func_192023_dk())) {
            this.func_192029_h(new NBTTagCompound());
        }
        if (this.spawnEntityFromShoulder(this.func_192025_dl())) {
            this.func_192031_i(new NBTTagCompound());
        }
        // CraftBukkit end
    }
    // Paper start
    public Entity releaseLeftShoulderEntity() {
        Entity entity = this.spawnEntityFromShoulder0(this.func_192023_dk());
        if (entity != null) {
            this.func_192029_h(new NBTTagCompound());
        }
        return entity;
    }

    public Entity releaseRightShoulderEntity() {
        Entity entity = this.spawnEntityFromShoulder0(this.func_192025_dl());
        if (entity != null) {
            this.func_192031_i(new NBTTagCompound());
        }
        return entity;
    }

    // Paper - incase any plugins used NMS to call this... old method signature to avoid other diff
    private boolean spawnEntityFromShoulder(@Nullable NBTTagCompound nbttagcompound) {
        return spawnEntityFromShoulder0(nbttagcompound) != null;
    }
    // Paper - Moved to new method that now returns entity, and properly null checks
    private Entity spawnEntityFromShoulder0(@Nullable NBTTagCompound nbttagcompound) { // CraftBukkit void->boolean - Paper - return Entity
        if (!this.field_70170_p.field_72995_K && nbttagcompound != null && !nbttagcompound.func_82582_d()) { // Paper - null check
            Entity entity = EntityList.func_75615_a(nbttagcompound, this.field_70170_p);
            if (entity == null) { // Paper - null check
                return null;
            }

            if (entity instanceof EntityTameable) {
                ((EntityTameable) entity).func_184754_b(this.field_96093_i);
            }

            entity.func_70107_b(this.field_70165_t, this.field_70163_u + 0.699999988079071D, this.field_70161_v);
            if (this.field_70170_p.addEntity(entity, CreatureSpawnEvent.SpawnReason.SHOULDER_ENTITY)) { // CraftBukkit
                return entity;
            }
        }

        return null;
    }
    // Paper end

    public abstract boolean func_175149_v();

    public abstract boolean func_184812_l_();

    @Override
    public boolean func_96092_aw() {
        return !this.field_71075_bZ.field_75100_b;
    }

    public Scoreboard func_96123_co() {
        return this.field_70170_p.func_96441_U();
    }

    @Override
    public Team func_96124_cp() {
        return this.func_96123_co().func_96509_i(this.func_70005_c_());
    }

    @Override
    public ITextComponent func_145748_c_() {
        TextComponentString chatcomponenttext = new TextComponentString(ScorePlayerTeam.func_96667_a(this.func_96124_cp(), this.func_70005_c_()));

        chatcomponenttext.func_150256_b().func_150241_a(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/msg " + this.func_70005_c_() + " "));
        chatcomponenttext.func_150256_b().func_150209_a(this.func_174823_aP());
        chatcomponenttext.func_150256_b().func_179989_a(this.func_70005_c_());
        return chatcomponenttext;
    }

    @Override
    public float func_70047_e() {
        float f = 1.62F;

        if (this.func_70608_bn()) {
            f = 0.2F;
        } else if (!this.func_70093_af() && this.field_70131_O != 1.65F) {
            if (this.func_184613_cA() || this.field_70131_O == 0.6F) {
                f = 0.4F;
            }
        } else {
            f -= 0.08F;
        }

        return f;
    }

    @Override
    public void func_110149_m(float f) {
        if (f < 0.0F) {
            f = 0.0F;
        }

        this.func_184212_Q().func_187227_b(EntityPlayer.field_184829_a, Float.valueOf(f));
    }

    @Override
    public float func_110139_bj() {
        return this.func_184212_Q().func_187225_a(EntityPlayer.field_184829_a).floatValue();
    }

    public static UUID func_146094_a(GameProfile gameprofile) {
        UUID uuid = gameprofile.getId();

        if (uuid == null) {
            uuid = func_175147_b(gameprofile.getName());
        }

        return uuid;
    }

    public static UUID func_175147_b(String s) {
        return UUID.nameUUIDFromBytes(("OfflinePlayer:" + s).getBytes(StandardCharsets.UTF_8));
    }

    public boolean func_175146_a(LockCode chestlock) {
        if (chestlock.func_180160_a()) {
            return true;
        } else {
            ItemStack itemstack = this.func_184614_ca();

            return !itemstack.func_190926_b() && itemstack.func_82837_s() ? itemstack.func_82833_r().equals(chestlock.func_180159_b()) : false;
        }
    }

    @Override
    public boolean func_174792_t_() {
        return this.func_184102_h().field_71305_c[0].func_82736_K().func_82766_b("sendCommandFeedback");
    }

    @Override
    public boolean func_174820_d(int i, ItemStack itemstack) {
        if (i >= 0 && i < this.field_71071_by.field_70462_a.size()) {
            this.field_71071_by.func_70299_a(i, itemstack);
            return true;
        } else {
            EntityEquipmentSlot enumitemslot;

            if (i == 100 + EntityEquipmentSlot.HEAD.func_188454_b()) {
                enumitemslot = EntityEquipmentSlot.HEAD;
            } else if (i == 100 + EntityEquipmentSlot.CHEST.func_188454_b()) {
                enumitemslot = EntityEquipmentSlot.CHEST;
            } else if (i == 100 + EntityEquipmentSlot.LEGS.func_188454_b()) {
                enumitemslot = EntityEquipmentSlot.LEGS;
            } else if (i == 100 + EntityEquipmentSlot.FEET.func_188454_b()) {
                enumitemslot = EntityEquipmentSlot.FEET;
            } else {
                enumitemslot = null;
            }

            if (i == 98) {
                this.func_184201_a(EntityEquipmentSlot.MAINHAND, itemstack);
                return true;
            } else if (i == 99) {
                this.func_184201_a(EntityEquipmentSlot.OFFHAND, itemstack);
                return true;
            } else if (enumitemslot == null) {
                int j = i - 200;

                if (j >= 0 && j < this.field_71078_a.func_70302_i_()) {
                    this.field_71078_a.func_70299_a(j, itemstack);
                    return true;
                } else {
                    return false;
                }
            } else {
                if (!itemstack.func_190926_b()) {
                    if (!(itemstack.func_77973_b() instanceof ItemArmor) && !(itemstack.func_77973_b() instanceof ItemElytra)) {
                        if (enumitemslot != EntityEquipmentSlot.HEAD) {
                            return false;
                        }
                    } else if (EntityLiving.func_184640_d(itemstack) != enumitemslot) {
                        return false;
                    }
                }

                this.field_71071_by.func_70299_a(enumitemslot.func_188454_b() + this.field_71071_by.field_70462_a.size(), itemstack);
                return true;
            }
        }
    }

    @Override
    public EnumHandSide func_184591_cq() {
        return this.field_70180_af.func_187225_a(EntityPlayer.field_184828_bq).byteValue() == 0 ? EnumHandSide.LEFT : EnumHandSide.RIGHT;
    }

    public void func_184819_a(EnumHandSide enummainhand) {
        this.field_70180_af.func_187227_b(EntityPlayer.field_184828_bq, Byte.valueOf((byte) (enummainhand == EnumHandSide.LEFT ? 0 : 1)));
    }

    public NBTTagCompound func_192023_dk() {
        return this.field_70180_af.func_187225_a(EntityPlayer.field_192032_bt);
    }

    public void func_192029_h(NBTTagCompound nbttagcompound) {
        this.field_70180_af.func_187227_b(EntityPlayer.field_192032_bt, nbttagcompound);
    }

    public NBTTagCompound func_192025_dl() {
        return this.field_70180_af.func_187225_a(EntityPlayer.field_192033_bu);
    }

    public void func_192031_i(NBTTagCompound nbttagcompound) {
        this.field_70180_af.func_187227_b(EntityPlayer.field_192033_bu, nbttagcompound);
    }

    public float func_184818_cX() {
        return (float) (1.0D / this.func_110148_a(SharedMonsterAttributes.field_188790_f).func_111126_e() * 20.0D);
    }

    public float func_184825_o(float f) {
        return MathHelper.func_76131_a((this.field_184617_aD + f) / this.func_184818_cX(), 0.0F, 1.0F);
    }

    public void func_184821_cY() {
        this.field_184617_aD = 0;
    }

    public CooldownTracker func_184811_cZ() {
        return this.field_184832_bU;
    }

    @Override
    public void func_70108_f(Entity entity) {
        if (!this.func_70608_bn()) {
            super.func_70108_f(entity);
        }

    }

    public float func_184817_da() {
        return (float) this.func_110148_a(SharedMonsterAttributes.field_188792_h).func_111126_e();
    }

    public boolean func_189808_dh() {
        return this.field_71075_bZ.field_75098_d && this.func_70003_b(2, "");
    }

    static class c implements Predicate<EntityMob> {

        private final EntityPlayer a;

        private c(EntityPlayer entityhuman) {
            this.a = entityhuman;
        }

        public boolean a(@Nullable EntityMob entitymonster) {
            return entitymonster.func_191990_c(this.a);
        }

        @Override
        public boolean apply(@Nullable EntityMob object) { // CraftBukkit - decompile error
            return this.a(object);
        }

        c(EntityPlayer entityhuman, Object object) {
            this(entityhuman);
        }
    }

    public static enum SleepResult {

        OK, NOT_POSSIBLE_HERE, NOT_POSSIBLE_NOW, TOO_FAR_AWAY, OTHER_PROBLEM, NOT_SAFE;

        private SleepResult() {}
    }

    public static enum EnumChatVisibility {

        FULL(0, "options.chat.visibility.full"), SYSTEM(1, "options.chat.visibility.system"), HIDDEN(2, "options.chat.visibility.hidden");

        private static final EntityPlayer.EnumChatVisibility[] field_151432_d = new EntityPlayer.EnumChatVisibility[values().length];
        private final int field_151433_e;
        private final String field_151430_f;

        private EnumChatVisibility(int i, String s) {
            this.field_151433_e = i;
            this.field_151430_f = s;
        }

        static {
            EntityPlayer.EnumChatVisibility[] aentityhuman_enumchatvisibility = values();
            int i = aentityhuman_enumchatvisibility.length;

            for (int j = 0; j < i; ++j) {
                EntityPlayer.EnumChatVisibility entityhuman_enumchatvisibility = aentityhuman_enumchatvisibility[j];

                EntityPlayer.EnumChatVisibility.field_151432_d[entityhuman_enumchatvisibility.field_151433_e] = entityhuman_enumchatvisibility;
            }

        }
    }
}
