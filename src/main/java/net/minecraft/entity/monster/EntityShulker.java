package net.minecraft.entity.monster;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.BlockPistonExtension;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityBodyHelper;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityShulkerBullet;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;
import org.bukkit.Location;
import org.bukkit.event.entity.EntityTeleportEvent;

// CraftBukkit start
import org.bukkit.Location;
import org.bukkit.event.entity.EntityTeleportEvent;
// CraftBukkit end

public class EntityShulker extends EntityGolem implements IMob {

    private static final UUID field_184703_bv = UUID.fromString("7E0292F2-9434-48D5-A29F-9583AF7DF27F");
    private static final AttributeModifier field_184704_bw = (new AttributeModifier(EntityShulker.field_184703_bv, "Covered armor bonus", 20.0D, 0)).func_111168_a(false);
    protected static final DataParameter<EnumFacing> field_184700_a = EntityDataManager.func_187226_a(EntityShulker.class, DataSerializers.field_187202_l);
    protected static final DataParameter<Optional<BlockPos>> field_184701_b = EntityDataManager.func_187226_a(EntityShulker.class, DataSerializers.field_187201_k);
    protected static final DataParameter<Byte> field_184702_c = EntityDataManager.func_187226_a(EntityShulker.class, DataSerializers.field_187191_a);
    public static final DataParameter<Byte> field_190770_bw = EntityDataManager.func_187226_a(EntityShulker.class, DataSerializers.field_187191_a);
    public static final EnumDyeColor field_190771_bx = EnumDyeColor.PURPLE;
    private float field_184705_bx;
    private float field_184706_by;
    private BlockPos field_184707_bz;
    private int field_184708_bA;

    public EntityShulker(World world) {
        super(world);
        this.func_70105_a(1.0F, 1.0F);
        this.field_70760_ar = 180.0F;
        this.field_70761_aq = 180.0F;
        this.field_70178_ae = true;
        this.field_184707_bz = null;
        this.field_70728_aV = 5;
    }

    @Override
    @Nullable
    public IEntityLivingData func_180482_a(DifficultyInstance difficultydamagescaler, @Nullable IEntityLivingData groupdataentity) {
        this.field_70761_aq = 180.0F;
        this.field_70760_ar = 180.0F;
        this.field_70177_z = 180.0F;
        this.field_70126_B = 180.0F;
        this.field_70759_as = 180.0F;
        this.field_70758_at = 180.0F;
        return super.func_180482_a(difficultydamagescaler, groupdataentity);
    }

    @Override
    protected void func_184651_r() {
        this.field_70714_bg.func_75776_a(1, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.field_70714_bg.func_75776_a(4, new EntityShulker.a());
        this.field_70714_bg.func_75776_a(7, new EntityShulker.e(null));
        this.field_70714_bg.func_75776_a(8, new EntityAILookIdle(this));
        this.field_70715_bh.func_75776_a(1, new EntityAIHurtByTarget(this, true, new Class[0]));
        this.field_70715_bh.func_75776_a(2, new EntityShulker.d(this));
        this.field_70715_bh.func_75776_a(3, new EntityShulker.c(this));
    }

    @Override
    protected boolean func_70041_e_() {
        return false;
    }

    @Override
    public SoundCategory func_184176_by() {
        return SoundCategory.HOSTILE;
    }

    @Override
    protected SoundEvent func_184639_G() {
        return SoundEvents.field_187773_eO;
    }

    @Override
    public void func_70642_aH() {
        if (!this.func_184686_df()) {
            super.func_70642_aH();
        }

    }

    @Override
    protected SoundEvent func_184615_bR() {
        return SoundEvents.field_187781_eS;
    }

    @Override
    protected SoundEvent func_184601_bQ(DamageSource damagesource) {
        return this.func_184686_df() ? SoundEvents.field_187785_eU : SoundEvents.field_187783_eT;
    }

    @Override
    protected void func_70088_a() {
        super.func_70088_a();
        this.field_70180_af.func_187214_a(EntityShulker.field_184700_a, EnumFacing.DOWN);
        this.field_70180_af.func_187214_a(EntityShulker.field_184701_b, Optional.absent());
        this.field_70180_af.func_187214_a(EntityShulker.field_184702_c, Byte.valueOf((byte) 0));
        this.field_70180_af.func_187214_a(EntityShulker.field_190770_bw, Byte.valueOf((byte) EntityShulker.field_190771_bx.func_176765_a()));
    }

    @Override
    protected void func_110147_ax() {
        super.func_110147_ax();
        this.func_110148_a(SharedMonsterAttributes.field_111267_a).func_111128_a(30.0D);
    }

    @Override
    protected EntityBodyHelper func_184650_s() {
        return new EntityShulker.b(this);
    }

    public static void func_189757_b(DataFixer dataconvertermanager) {
        EntityLiving.func_189752_a(dataconvertermanager, EntityShulker.class);
    }

    @Override
    public void func_70037_a(NBTTagCompound nbttagcompound) {
        super.func_70037_a(nbttagcompound);
        this.field_70180_af.func_187227_b(EntityShulker.field_184700_a, EnumFacing.func_82600_a(nbttagcompound.func_74771_c("AttachFace")));
        this.field_70180_af.func_187227_b(EntityShulker.field_184702_c, Byte.valueOf(nbttagcompound.func_74771_c("Peek")));
        this.field_70180_af.func_187227_b(EntityShulker.field_190770_bw, Byte.valueOf(nbttagcompound.func_74771_c("Color")));
        if (nbttagcompound.func_74764_b("APX")) {
            int i = nbttagcompound.func_74762_e("APX");
            int j = nbttagcompound.func_74762_e("APY");
            int k = nbttagcompound.func_74762_e("APZ");

            this.field_70180_af.func_187227_b(EntityShulker.field_184701_b, Optional.of(new BlockPos(i, j, k)));
        } else {
            this.field_70180_af.func_187227_b(EntityShulker.field_184701_b, Optional.<BlockPos>absent());
        }

    }

    @Override
    public void func_70014_b(NBTTagCompound nbttagcompound) {
        super.func_70014_b(nbttagcompound);
        nbttagcompound.func_74774_a("AttachFace", (byte) this.field_70180_af.func_187225_a(EntityShulker.field_184700_a).func_176745_a());
        nbttagcompound.func_74774_a("Peek", this.field_70180_af.func_187225_a(EntityShulker.field_184702_c).byteValue());
        nbttagcompound.func_74774_a("Color", this.field_70180_af.func_187225_a(EntityShulker.field_190770_bw).byteValue());
        BlockPos blockposition = this.func_184699_da();

        if (blockposition != null) {
            nbttagcompound.func_74768_a("APX", blockposition.func_177958_n());
            nbttagcompound.func_74768_a("APY", blockposition.func_177956_o());
            nbttagcompound.func_74768_a("APZ", blockposition.func_177952_p());
        }

    }

    @Override
    public void func_70071_h_() {
        super.func_70071_h_();
        BlockPos blockposition = (BlockPos) ((Optional) this.field_70180_af.func_187225_a(EntityShulker.field_184701_b)).orNull();

        if (blockposition == null && !this.field_70170_p.field_72995_K) {
            blockposition = new BlockPos(this);
            this.field_70180_af.func_187227_b(EntityShulker.field_184701_b, Optional.of(blockposition));
        }

        float f;

        if (this.func_184218_aH()) {
            blockposition = null;
            f = this.func_184187_bx().field_70177_z;
            this.field_70177_z = f;
            this.field_70761_aq = f;
            this.field_70760_ar = f;
            this.field_184708_bA = 0;
        } else if (!this.field_70170_p.field_72995_K) {
            IBlockState iblockdata = this.field_70170_p.func_180495_p(blockposition);

            if (iblockdata.func_185904_a() != Material.field_151579_a) {
                EnumFacing enumdirection;

                if (iblockdata.func_177230_c() == Blocks.field_180384_M) {
                    enumdirection = iblockdata.func_177229_b(BlockPistonBase.field_176387_N);
                    if (this.field_70170_p.func_175623_d(blockposition.func_177972_a(enumdirection))) {
                        blockposition = blockposition.func_177972_a(enumdirection);
                        this.field_70180_af.func_187227_b(EntityShulker.field_184701_b, Optional.of(blockposition));
                    } else {
                        this.func_184689_o();
                    }
                } else if (iblockdata.func_177230_c() == Blocks.field_150332_K) {
                    enumdirection = iblockdata.func_177229_b(BlockPistonExtension.field_176387_N);
                    if (this.field_70170_p.func_175623_d(blockposition.func_177972_a(enumdirection))) {
                        blockposition = blockposition.func_177972_a(enumdirection);
                        this.field_70180_af.func_187227_b(EntityShulker.field_184701_b, Optional.of(blockposition));
                    } else {
                        this.func_184689_o();
                    }
                } else {
                    this.func_184689_o();
                }
            }

            BlockPos blockposition1 = blockposition.func_177972_a(this.func_184696_cZ());

            if (!this.field_70170_p.func_175677_d(blockposition1, false)) {
                boolean flag = false;
                EnumFacing[] aenumdirection = EnumFacing.values();
                int i = aenumdirection.length;

                for (int j = 0; j < i; ++j) {
                    EnumFacing enumdirection1 = aenumdirection[j];

                    blockposition1 = blockposition.func_177972_a(enumdirection1);
                    if (this.field_70170_p.func_175677_d(blockposition1, false)) {
                        this.field_70180_af.func_187227_b(EntityShulker.field_184700_a, enumdirection1);
                        flag = true;
                        break;
                    }
                }

                if (!flag) {
                    this.func_184689_o();
                }
            }

            BlockPos blockposition2 = blockposition.func_177972_a(this.func_184696_cZ().func_176734_d());

            if (this.field_70170_p.func_175677_d(blockposition2, false)) {
                this.func_184689_o();
            }
        }

        f = this.func_184684_db() * 0.01F;
        this.field_184705_bx = this.field_184706_by;
        if (this.field_184706_by > f) {
            this.field_184706_by = MathHelper.func_76131_a(this.field_184706_by - 0.05F, f, 1.0F);
        } else if (this.field_184706_by < f) {
            this.field_184706_by = MathHelper.func_76131_a(this.field_184706_by + 0.05F, 0.0F, f);
        }

        if (blockposition != null) {
            if (this.field_70170_p.field_72995_K) {
                if (this.field_184708_bA > 0 && this.field_184707_bz != null) {
                    --this.field_184708_bA;
                } else {
                    this.field_184707_bz = blockposition;
                }
            }

            this.field_70165_t = blockposition.func_177958_n() + 0.5D;
            this.field_70163_u = blockposition.func_177956_o();
            this.field_70161_v = blockposition.func_177952_p() + 0.5D;
            this.field_70169_q = this.field_70165_t;
            this.field_70167_r = this.field_70163_u;
            this.field_70166_s = this.field_70161_v;
            this.field_70142_S = this.field_70165_t;
            this.field_70137_T = this.field_70163_u;
            this.field_70136_U = this.field_70161_v;
            double d0 = 0.5D - MathHelper.func_76126_a((0.5F + this.field_184706_by) * 3.1415927F) * 0.5D;
            double d1 = 0.5D - MathHelper.func_76126_a((0.5F + this.field_184705_bx) * 3.1415927F) * 0.5D;
            double d2 = d0 - d1;
            double d3 = 0.0D;
            double d4 = 0.0D;
            double d5 = 0.0D;
            EnumFacing enumdirection2 = this.func_184696_cZ();

            switch (enumdirection2) {
            case DOWN:
                this.func_174826_a(new AxisAlignedBB(this.field_70165_t - 0.5D, this.field_70163_u, this.field_70161_v - 0.5D, this.field_70165_t + 0.5D, this.field_70163_u + 1.0D + d0, this.field_70161_v + 0.5D));
                d4 = d2;
                break;

            case UP:
                this.func_174826_a(new AxisAlignedBB(this.field_70165_t - 0.5D, this.field_70163_u - d0, this.field_70161_v - 0.5D, this.field_70165_t + 0.5D, this.field_70163_u + 1.0D, this.field_70161_v + 0.5D));
                d4 = -d2;
                break;

            case NORTH:
                this.func_174826_a(new AxisAlignedBB(this.field_70165_t - 0.5D, this.field_70163_u, this.field_70161_v - 0.5D, this.field_70165_t + 0.5D, this.field_70163_u + 1.0D, this.field_70161_v + 0.5D + d0));
                d5 = d2;
                break;

            case SOUTH:
                this.func_174826_a(new AxisAlignedBB(this.field_70165_t - 0.5D, this.field_70163_u, this.field_70161_v - 0.5D - d0, this.field_70165_t + 0.5D, this.field_70163_u + 1.0D, this.field_70161_v + 0.5D));
                d5 = -d2;
                break;

            case WEST:
                this.func_174826_a(new AxisAlignedBB(this.field_70165_t - 0.5D, this.field_70163_u, this.field_70161_v - 0.5D, this.field_70165_t + 0.5D + d0, this.field_70163_u + 1.0D, this.field_70161_v + 0.5D));
                d3 = d2;
                break;

            case EAST:
                this.func_174826_a(new AxisAlignedBB(this.field_70165_t - 0.5D - d0, this.field_70163_u, this.field_70161_v - 0.5D, this.field_70165_t + 0.5D, this.field_70163_u + 1.0D, this.field_70161_v + 0.5D));
                d3 = -d2;
            }

            if (d2 > 0.0D) {
                List list = this.field_70170_p.func_72839_b(this, this.func_174813_aQ());

                if (!list.isEmpty()) {
                    Iterator iterator = list.iterator();

                    while (iterator.hasNext()) {
                        Entity entity = (Entity) iterator.next();

                        if (!(entity instanceof EntityShulker) && !entity.field_70145_X) {
                            entity.func_70091_d(MoverType.SHULKER, d3, d4, d5);
                        }
                    }
                }
            }
        }

    }

    @Override
    public void func_70091_d(MoverType enummovetype, double d0, double d1, double d2) {
        if (enummovetype == MoverType.SHULKER_BOX) {
            this.func_184689_o();
        } else {
            super.func_70091_d(enummovetype, d0, d1, d2);
        }

    }

    @Override
    public void func_70107_b(double d0, double d1, double d2) {
        super.func_70107_b(d0, d1, d2);
        if (this.field_70180_af != null && this.field_70173_aa != 0) {
            Optional optional = this.field_70180_af.func_187225_a(EntityShulker.field_184701_b);
            Optional optional1 = Optional.of(new BlockPos(d0, d1, d2));

            if (!optional1.equals(optional)) {
                this.field_70180_af.func_187227_b(EntityShulker.field_184701_b, optional1);
                this.field_70180_af.func_187227_b(EntityShulker.field_184702_c, Byte.valueOf((byte) 0));
                this.field_70160_al = true;
            }

        }
    }

    protected boolean func_184689_o() {
        if (!this.func_175446_cd() && this.func_70089_S()) {
            BlockPos blockposition = new BlockPos(this);

            for (int i = 0; i < 5; ++i) {
                BlockPos blockposition1 = blockposition.func_177982_a(8 - this.field_70146_Z.nextInt(17), 8 - this.field_70146_Z.nextInt(17), 8 - this.field_70146_Z.nextInt(17));

                if (blockposition1.func_177956_o() > 0 && this.field_70170_p.func_175623_d(blockposition1) && this.field_70170_p.func_191503_g(this) && this.field_70170_p.func_184144_a(this, new AxisAlignedBB(blockposition1)).isEmpty()) {
                    boolean flag = false;
                    EnumFacing[] aenumdirection = EnumFacing.values();
                    int j = aenumdirection.length;

                    for (int k = 0; k < j; ++k) {
                        EnumFacing enumdirection = aenumdirection[k];

                        if (this.field_70170_p.func_175677_d(blockposition1.func_177972_a(enumdirection), false)) {
                            // CraftBukkit start
                            EntityTeleportEvent teleport = new EntityTeleportEvent(this.getBukkitEntity(), this.getBukkitEntity().getLocation(), new Location(this.field_70170_p.getWorld(), blockposition1.func_177958_n(), blockposition1.func_177956_o(), blockposition1.func_177952_p()));
                            this.field_70170_p.getServer().getPluginManager().callEvent(teleport);
                            if (!teleport.isCancelled()) {
                                Location to = teleport.getTo();
                                blockposition1 = new BlockPos(to.getX(), to.getY(), to.getZ());

                                this.field_70180_af.func_187227_b(EntityShulker.field_184700_a, enumdirection);
                                flag = true;
                            }
                            // CraftBukkit end
                            break;
                        }
                    }

                    if (flag) {
                        this.func_184185_a(SoundEvents.field_187791_eX, 1.0F, 1.0F);
                        this.field_70180_af.func_187227_b(EntityShulker.field_184701_b, Optional.of(blockposition1));
                        this.field_70180_af.func_187227_b(EntityShulker.field_184702_c, Byte.valueOf((byte) 0));
                        this.func_70624_b((EntityLivingBase) null);
                        return true;
                    }
                }
            }

            return false;
        } else {
            return true;
        }
    }

    @Override
    public void func_70636_d() {
        super.func_70636_d();
        this.field_70159_w = 0.0D;
        this.field_70181_x = 0.0D;
        this.field_70179_y = 0.0D;
        this.field_70760_ar = 180.0F;
        this.field_70761_aq = 180.0F;
        this.field_70177_z = 180.0F;
    }

    @Override
    public void func_184206_a(DataParameter<?> datawatcherobject) {
        if (EntityShulker.field_184701_b.equals(datawatcherobject) && this.field_70170_p.field_72995_K && !this.func_184218_aH()) {
            BlockPos blockposition = this.func_184699_da();

            if (blockposition != null) {
                if (this.field_184707_bz == null) {
                    this.field_184707_bz = blockposition;
                } else {
                    this.field_184708_bA = 6;
                }

                this.field_70165_t = blockposition.func_177958_n() + 0.5D;
                this.field_70163_u = blockposition.func_177956_o();
                this.field_70161_v = blockposition.func_177952_p() + 0.5D;
                this.field_70169_q = this.field_70165_t;
                this.field_70167_r = this.field_70163_u;
                this.field_70166_s = this.field_70161_v;
                this.field_70142_S = this.field_70165_t;
                this.field_70137_T = this.field_70163_u;
                this.field_70136_U = this.field_70161_v;
            }
        }

        super.func_184206_a(datawatcherobject);
    }

    @Override
    public boolean func_70097_a(DamageSource damagesource, float f) {
        if (this.func_184686_df()) {
            Entity entity = damagesource.func_76364_f();

            if (entity instanceof EntityArrow) {
                return false;
            }
        }

        if (super.func_70097_a(damagesource, f)) {
            if (this.func_110143_aJ() < this.func_110138_aP() * 0.5D && this.field_70146_Z.nextInt(4) == 0) {
                this.func_184689_o();
            }

            return true;
        } else {
            return false;
        }
    }

    private boolean func_184686_df() {
        return this.func_184684_db() == 0;
    }

    @Override
    @Nullable
    public AxisAlignedBB func_70046_E() {
        return this.func_70089_S() ? this.func_174813_aQ() : null;
    }

    public EnumFacing func_184696_cZ() {
        return this.field_70180_af.func_187225_a(EntityShulker.field_184700_a);
    }

    @Nullable
    public BlockPos func_184699_da() {
        return (BlockPos) ((Optional) this.field_70180_af.func_187225_a(EntityShulker.field_184701_b)).orNull();
    }

    public void func_184694_g(@Nullable BlockPos blockposition) {
        this.field_70180_af.func_187227_b(EntityShulker.field_184701_b, Optional.fromNullable(blockposition));
    }

    public int func_184684_db() {
        return this.field_70180_af.func_187225_a(EntityShulker.field_184702_c).byteValue();
    }

    public void func_184691_a(int i) {
        if (!this.field_70170_p.field_72995_K) {
            this.func_110148_a(SharedMonsterAttributes.field_188791_g).func_111124_b(EntityShulker.field_184704_bw);
            if (i == 0) {
                this.func_110148_a(SharedMonsterAttributes.field_188791_g).func_111121_a(EntityShulker.field_184704_bw);
                this.func_184185_a(SoundEvents.field_187779_eR, 1.0F, 1.0F);
            } else {
                this.func_184185_a(SoundEvents.field_187787_eV, 1.0F, 1.0F);
            }
        }

        this.field_70180_af.func_187227_b(EntityShulker.field_184702_c, Byte.valueOf((byte) i));
    }

    @Override
    public float func_70047_e() {
        return 0.5F;
    }

    @Override
    public int func_70646_bf() {
        return 180;
    }

    @Override
    public int func_184649_cE() {
        return 180;
    }

    @Override
    public void func_70108_f(Entity entity) {}

    @Override
    public float func_70111_Y() {
        return 0.0F;
    }

    @Override
    @Nullable
    protected ResourceLocation func_184647_J() {
        return LootTableList.field_186442_x;
    }

    static class c extends EntityAINearestAttackableTarget<EntityLivingBase> {

        public c(EntityShulker entityshulker) {
            super(entityshulker, EntityLivingBase.class, 10, true, false, new Predicate() {
                public boolean a(@Nullable EntityLivingBase entityliving) {
                    return entityliving instanceof IMob;
                }

                @Override
                public boolean apply(@Nullable Object object) {
                    return this.a((EntityLivingBase) object);
                }
            });
        }

        @Override
        public boolean func_75250_a() {
            return this.field_75299_d.func_96124_cp() == null ? false : super.func_75250_a();
        }

        @Override
        protected AxisAlignedBB func_188511_a(double d0) {
            EnumFacing enumdirection = ((EntityShulker) this.field_75299_d).func_184696_cZ();

            return enumdirection.func_176740_k() == EnumFacing.Axis.X ? this.field_75299_d.func_174813_aQ().func_72314_b(4.0D, d0, d0) : (enumdirection.func_176740_k() == EnumFacing.Axis.Z ? this.field_75299_d.func_174813_aQ().func_72314_b(d0, d0, 4.0D) : this.field_75299_d.func_174813_aQ().func_72314_b(d0, 4.0D, d0));
        }
    }

    class d extends EntityAINearestAttackableTarget<EntityPlayer> {

        public d(EntityShulker entityshulker) {
            super(entityshulker, EntityPlayer.class, true);
        }

        @Override
        public boolean func_75250_a() {
            return EntityShulker.this.field_70170_p.func_175659_aa() == EnumDifficulty.PEACEFUL ? false : super.func_75250_a();
        }

        @Override
        protected AxisAlignedBB func_188511_a(double d0) {
            EnumFacing enumdirection = ((EntityShulker) this.field_75299_d).func_184696_cZ();

            return enumdirection.func_176740_k() == EnumFacing.Axis.X ? this.field_75299_d.func_174813_aQ().func_72314_b(4.0D, d0, d0) : (enumdirection.func_176740_k() == EnumFacing.Axis.Z ? this.field_75299_d.func_174813_aQ().func_72314_b(d0, d0, 4.0D) : this.field_75299_d.func_174813_aQ().func_72314_b(d0, 4.0D, d0));
        }
    }

    class a extends EntityAIBase {

        private int b;

        public a() {
            this.func_75248_a(3);
        }

        @Override
        public boolean func_75250_a() {
            EntityLivingBase entityliving = EntityShulker.this.func_70638_az();

            return entityliving != null && entityliving.func_70089_S() ? EntityShulker.this.field_70170_p.func_175659_aa() != EnumDifficulty.PEACEFUL : false;
        }

        @Override
        public void func_75249_e() {
            this.b = 20;
            EntityShulker.this.func_184691_a(100);
        }

        @Override
        public void func_75251_c() {
            EntityShulker.this.func_184691_a(0);
        }

        @Override
        public void func_75246_d() {
            if (EntityShulker.this.field_70170_p.func_175659_aa() != EnumDifficulty.PEACEFUL) {
                --this.b;
                EntityLivingBase entityliving = EntityShulker.this.func_70638_az();

                EntityShulker.this.func_70671_ap().func_75651_a(entityliving, 180.0F, 180.0F);
                double d0 = EntityShulker.this.func_70068_e(entityliving);

                if (d0 < 400.0D) {
                    if (this.b <= 0) {
                        this.b = 20 + EntityShulker.this.field_70146_Z.nextInt(10) * 20 / 2;
                        EntityShulkerBullet entityshulkerbullet = new EntityShulkerBullet(EntityShulker.this.field_70170_p, EntityShulker.this, entityliving, EntityShulker.this.func_184696_cZ().func_176740_k());

                        EntityShulker.this.field_70170_p.func_72838_d(entityshulkerbullet);
                        EntityShulker.this.func_184185_a(SoundEvents.field_187789_eW, 2.0F, (EntityShulker.this.field_70146_Z.nextFloat() - EntityShulker.this.field_70146_Z.nextFloat()) * 0.2F + 1.0F);
                    }
                } else {
                    EntityShulker.this.func_70624_b((EntityLivingBase) null);
                }

                super.func_75246_d();
            }
        }
    }

    class e extends EntityAIBase {

        private int b;

        private e() {}

        @Override
        public boolean func_75250_a() {
            return EntityShulker.this.func_70638_az() == null && EntityShulker.this.field_70146_Z.nextInt(40) == 0;
        }

        @Override
        public boolean func_75253_b() {
            return EntityShulker.this.func_70638_az() == null && this.b > 0;
        }

        @Override
        public void func_75249_e() {
            this.b = 20 * (1 + EntityShulker.this.field_70146_Z.nextInt(3));
            EntityShulker.this.func_184691_a(30);
        }

        @Override
        public void func_75251_c() {
            if (EntityShulker.this.func_70638_az() == null) {
                EntityShulker.this.func_184691_a(0);
            }

        }

        @Override
        public void func_75246_d() {
            --this.b;
        }

        e(Object object) {
            this();
        }
    }

    class b extends EntityBodyHelper {

        public b(EntityLivingBase entityliving) {
            super(entityliving);
        }

        @Override
        public void func_75664_a() {}
    }
}
