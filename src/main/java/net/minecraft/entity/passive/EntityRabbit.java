package net.minecraft.entity.passive;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCarrot;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAIMoveToBlock;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityJumpHelper;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.Path;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeDesert;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityRabbit extends EntityAnimal {

    private static final DataParameter<Integer> field_184773_bv = EntityDataManager.func_187226_a(EntityRabbit.class, DataSerializers.field_187192_b);
    private int field_175540_bm;
    private int field_175535_bn;
    private boolean field_175537_bp;
    private int field_175538_bq;
    private int field_175541_bs;

    public EntityRabbit(World world) {
        super(world);
        this.func_70105_a(0.4F, 0.5F);
        this.field_70767_i = new EntityRabbit.RabbitJumpHelper(this);
        this.field_70765_h = new EntityRabbit.RabbitMoveHelper(this);
        this.initializePathFinderGoals(); // CraftBukkit - moved code
    }

    // CraftBukkit start - code from constructor
    public void initializePathFinderGoals(){
        this.func_175515_b(0.0D);
    }
    // CraftBukkit end

    @Override
    protected void func_184651_r() {
        this.field_70714_bg.func_75776_a(1, new EntityAISwimming(this));
        this.field_70714_bg.func_75776_a(1, new EntityRabbit.AIPanic(this, 2.2D));
        this.field_70714_bg.func_75776_a(2, new EntityAIMate(this, 0.8D));
        this.field_70714_bg.func_75776_a(3, new EntityAITempt(this, 1.0D, Items.field_151172_bF, false));
        this.field_70714_bg.func_75776_a(3, new EntityAITempt(this, 1.0D, Items.field_151150_bK, false));
        this.field_70714_bg.func_75776_a(3, new EntityAITempt(this, 1.0D, Item.func_150898_a(Blocks.field_150327_N), false));
        this.field_70714_bg.func_75776_a(4, new EntityRabbit.AIAvoidEntity(this, EntityPlayer.class, 8.0F, 2.2D, 2.2D));
        this.field_70714_bg.func_75776_a(4, new EntityRabbit.AIAvoidEntity(this, EntityWolf.class, 10.0F, 2.2D, 2.2D));
        this.field_70714_bg.func_75776_a(4, new EntityRabbit.AIAvoidEntity(this, EntityMob.class, 4.0F, 2.2D, 2.2D));
        this.field_70714_bg.func_75776_a(5, new EntityRabbit.AIRaidFarm(this));
        this.field_70714_bg.func_75776_a(6, new EntityAIWanderAvoidWater(this, 0.6D));
        this.field_70714_bg.func_75776_a(11, new EntityAIWatchClosest(this, EntityPlayer.class, 10.0F));
    }

    @Override
    protected float func_175134_bD() {
        if (!this.field_70123_F && (!this.field_70765_h.func_75640_a() || this.field_70765_h.func_179919_e() <= this.field_70163_u + 0.5D)) {
            Path pathentity = this.field_70699_by.func_75505_d();

            if (pathentity != null && pathentity.func_75873_e() < pathentity.func_75874_d()) {
                Vec3d vec3d = pathentity.func_75878_a(this);

                if (vec3d.field_72448_b > this.field_70163_u + 0.5D) {
                    return 0.5F;
                }
            }

            return this.field_70765_h.func_75638_b() <= 0.6D ? 0.2F : 0.3F;
        } else {
            return 0.5F;
        }
    }

    @Override
    protected void func_70664_aZ() {
        super.func_70664_aZ();
        double d0 = this.field_70765_h.func_75638_b();

        if (d0 > 0.0D) {
            double d1 = this.field_70159_w * this.field_70159_w + this.field_70179_y * this.field_70179_y;

            if (d1 < 0.010000000000000002D) {
                this.func_191958_b(0.0F, 0.0F, 1.0F, 0.1F);
            }
        }

        if (!this.field_70170_p.field_72995_K) {
            this.field_70170_p.func_72960_a(this, (byte) 1);
        }

    }

    public void func_175515_b(double d0) {
        this.func_70661_as().func_75489_a(d0);
        this.field_70765_h.func_75642_a(this.field_70765_h.func_179917_d(), this.field_70765_h.func_179919_e(), this.field_70765_h.func_179918_f(), d0);
    }

    @Override
    public void func_70637_d(boolean flag) {
        super.func_70637_d(flag);
        if (flag) {
            this.func_184185_a(this.func_184771_da(), this.func_70599_aP(), ((this.field_70146_Z.nextFloat() - this.field_70146_Z.nextFloat()) * 0.2F + 1.0F) * 0.8F);
        }

    }

    public void func_184770_cZ() {
        this.func_70637_d(true);
        this.field_175535_bn = 10;
        this.field_175540_bm = 0;
    }

    @Override
    protected void func_70088_a() {
        super.func_70088_a();
        this.field_70180_af.func_187214_a(EntityRabbit.field_184773_bv, Integer.valueOf(0));
    }

    @Override
    public void func_70619_bc() {
        if (this.field_175538_bq > 0) {
            --this.field_175538_bq;
        }

        if (this.field_175541_bs > 0) {
            this.field_175541_bs -= this.field_70146_Z.nextInt(3);
            if (this.field_175541_bs < 0) {
                this.field_175541_bs = 0;
            }
        }

        if (this.field_70122_E) {
            if (!this.field_175537_bp) {
                this.func_70637_d(false);
                this.func_175517_cu();
            }

            if (this.func_175531_cl() == 99 && this.field_175538_bq == 0) {
                EntityLivingBase entityliving = this.func_70638_az();

                if (entityliving != null && this.func_70068_e(entityliving) < 16.0D) {
                    this.func_175533_a(entityliving.field_70165_t, entityliving.field_70161_v);
                    this.field_70765_h.func_75642_a(entityliving.field_70165_t, entityliving.field_70163_u, entityliving.field_70161_v, this.field_70765_h.func_75638_b());
                    this.func_184770_cZ();
                    this.field_175537_bp = true;
                }
            }

            EntityRabbit.RabbitJumpHelper entityrabbit_controllerjumprabbit = (EntityRabbit.RabbitJumpHelper) this.field_70767_i;

            if (!entityrabbit_controllerjumprabbit.func_180067_c()) {
                if (this.field_70765_h.func_75640_a() && this.field_175538_bq == 0) {
                    Path pathentity = this.field_70699_by.func_75505_d();
                    Vec3d vec3d = new Vec3d(this.field_70765_h.func_179917_d(), this.field_70765_h.func_179919_e(), this.field_70765_h.func_179918_f());

                    if (pathentity != null && pathentity.func_75873_e() < pathentity.func_75874_d()) {
                        vec3d = pathentity.func_75878_a(this);
                    }

                    this.func_175533_a(vec3d.field_72450_a, vec3d.field_72449_c);
                    this.func_184770_cZ();
                }
            } else if (!entityrabbit_controllerjumprabbit.func_180065_d()) {
                this.func_175518_cr();
            }
        }

        this.field_175537_bp = this.field_70122_E;
    }

    @Override
    public void func_174830_Y() {}

    private void func_175533_a(double d0, double d1) {
        this.field_70177_z = (float) (MathHelper.func_181159_b(d1 - this.field_70161_v, d0 - this.field_70165_t) * 57.2957763671875D) - 90.0F;
    }

    private void func_175518_cr() {
        ((EntityRabbit.RabbitJumpHelper) this.field_70767_i).func_180066_a(true);
    }

    private void func_175520_cs() {
        ((EntityRabbit.RabbitJumpHelper) this.field_70767_i).func_180066_a(false);
    }

    private void func_175530_ct() {
        if (this.field_70765_h.func_75638_b() < 2.2D) {
            this.field_175538_bq = 10;
        } else {
            this.field_175538_bq = 1;
        }

    }

    private void func_175517_cu() {
        this.func_175530_ct();
        this.func_175520_cs();
    }

    @Override
    public void func_70636_d() {
        super.func_70636_d();
        if (this.field_175540_bm != this.field_175535_bn) {
            ++this.field_175540_bm;
        } else if (this.field_175535_bn != 0) {
            this.field_175540_bm = 0;
            this.field_175535_bn = 0;
            this.func_70637_d(false);
        }

    }

    @Override
    protected void func_110147_ax() {
        super.func_110147_ax();
        this.func_110148_a(SharedMonsterAttributes.field_111267_a).func_111128_a(3.0D);
        this.func_110148_a(SharedMonsterAttributes.field_111263_d).func_111128_a(0.30000001192092896D);
    }

    public static void func_189801_b(DataFixer dataconvertermanager) {
        EntityLiving.func_189752_a(dataconvertermanager, EntityRabbit.class);
    }

    @Override
    public void func_70014_b(NBTTagCompound nbttagcompound) {
        super.func_70014_b(nbttagcompound);
        nbttagcompound.func_74768_a("RabbitType", this.func_175531_cl());
        nbttagcompound.func_74768_a("MoreCarrotTicks", this.field_175541_bs);
    }

    @Override
    public void func_70037_a(NBTTagCompound nbttagcompound) {
        super.func_70037_a(nbttagcompound);
        this.func_175529_r(nbttagcompound.func_74762_e("RabbitType"));
        this.field_175541_bs = nbttagcompound.func_74762_e("MoreCarrotTicks");
    }

    protected SoundEvent func_184771_da() {
        return SoundEvents.field_187824_en;
    }

    @Override
    protected SoundEvent func_184639_G() {
        return SoundEvents.field_187816_ej;
    }

    @Override
    protected SoundEvent func_184601_bQ(DamageSource damagesource) {
        return SoundEvents.field_187822_em;
    }

    @Override
    protected SoundEvent func_184615_bR() {
        return SoundEvents.field_187820_el;
    }

    @Override
    public boolean func_70652_k(Entity entity) {
        if (this.func_175531_cl() == 99) {
            this.func_184185_a(SoundEvents.field_187818_ek, 1.0F, (this.field_70146_Z.nextFloat() - this.field_70146_Z.nextFloat()) * 0.2F + 1.0F);
            return entity.func_70097_a(DamageSource.func_76358_a(this), 8.0F);
        } else {
            return entity.func_70097_a(DamageSource.func_76358_a(this), 3.0F);
        }
    }

    @Override
    public SoundCategory func_184176_by() {
        return this.func_175531_cl() == 99 ? SoundCategory.HOSTILE : SoundCategory.NEUTRAL;
    }

    @Override
    public boolean func_70097_a(DamageSource damagesource, float f) {
        return this.func_180431_b(damagesource) ? false : super.func_70097_a(damagesource, f);
    }

    @Override
    @Nullable
    protected ResourceLocation func_184647_J() {
        return LootTableList.field_186393_A;
    }

    private boolean func_175525_a(Item item) {
        return item == Items.field_151172_bF || item == Items.field_151150_bK || item == Item.func_150898_a(Blocks.field_150327_N);
    }

    @Override
    public EntityRabbit func_90011_a(EntityAgeable entityageable) {
        EntityRabbit entityrabbit = new EntityRabbit(this.field_70170_p);
        int i = this.func_184772_dk();

        if (this.field_70146_Z.nextInt(20) != 0) {
            if (entityageable instanceof EntityRabbit && this.field_70146_Z.nextBoolean()) {
                i = ((EntityRabbit) entityageable).func_175531_cl();
            } else {
                i = this.func_175531_cl();
            }
        }

        entityrabbit.func_175529_r(i);
        return entityrabbit;
    }

    @Override
    public boolean func_70877_b(ItemStack itemstack) {
        return this.func_175525_a(itemstack.func_77973_b());
    }

    public int func_175531_cl() {
        return this.field_70180_af.func_187225_a(EntityRabbit.field_184773_bv).intValue();
    }

    public void func_175529_r(int i) {
        if (i == 99) {
            this.func_110148_a(SharedMonsterAttributes.field_188791_g).func_111128_a(8.0D);
            this.field_70714_bg.func_75776_a(4, new EntityRabbit.AIEvilAttack(this));
            this.field_70715_bh.func_75776_a(1, new EntityAIHurtByTarget(this, false, new Class[0]));
            this.field_70715_bh.func_75776_a(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
            this.field_70715_bh.func_75776_a(2, new EntityAINearestAttackableTarget(this, EntityWolf.class, true));
            if (!this.func_145818_k_()) {
                this.func_96094_a(I18n.func_74838_a("entity.KillerBunny.name"));
            }
        }

        this.field_70180_af.func_187227_b(EntityRabbit.field_184773_bv, Integer.valueOf(i));
    }

    @Override
    @Nullable
    public IEntityLivingData func_180482_a(DifficultyInstance difficultydamagescaler, @Nullable IEntityLivingData groupdataentity) {
        Object object = super.func_180482_a(difficultydamagescaler, groupdataentity);
        int i = this.func_184772_dk();
        boolean flag = false;

        if (object instanceof EntityRabbit.RabbitTypeData) {
            i = ((EntityRabbit.RabbitTypeData) object).field_179427_a;
            flag = true;
        } else {
            object = new EntityRabbit.RabbitTypeData(i);
        }

        this.func_175529_r(i);
        if (flag) {
            this.func_70873_a(-24000);
        }

        return (IEntityLivingData) object;
    }

    private int func_184772_dk() {
        Biome biomebase = this.field_70170_p.func_180494_b(new BlockPos(this));
        int i = this.field_70146_Z.nextInt(100);

        return biomebase.func_150559_j() ? (i < 80 ? 1 : 3) : (biomebase instanceof BiomeDesert ? 4 : (i < 50 ? 0 : (i < 90 ? 5 : 2)));
    }

    private boolean func_175534_cv() {
        return this.field_175541_bs == 0;
    }

    protected void func_175528_cn() {
        BlockCarrot blockcarrots = (BlockCarrot) Blocks.field_150459_bM;
        IBlockState iblockdata = blockcarrots.func_185528_e(blockcarrots.func_185526_g());

        this.field_70170_p.func_175688_a(EnumParticleTypes.BLOCK_DUST, this.field_70165_t + this.field_70146_Z.nextFloat() * this.field_70130_N * 2.0F - this.field_70130_N, this.field_70163_u + 0.5D + this.field_70146_Z.nextFloat() * this.field_70131_O, this.field_70161_v + this.field_70146_Z.nextFloat() * this.field_70130_N * 2.0F - this.field_70130_N, 0.0D, 0.0D, 0.0D, new int[] { Block.func_176210_f(iblockdata)});
        this.field_175541_bs = 40;
    }

    static class AIEvilAttack extends EntityAIAttackMelee {

        public AIEvilAttack(EntityRabbit entityrabbit) {
            super(entityrabbit, 1.4D, true);
        }

        @Override
        protected double func_179512_a(EntityLivingBase entityliving) {
            return 4.0F + entityliving.field_70130_N;
        }
    }

    static class AIPanic extends EntityAIPanic {

        private final EntityRabbit field_179486_b;

        public AIPanic(EntityRabbit entityrabbit, double d0) {
            super(entityrabbit, d0);
            this.field_179486_b = entityrabbit;
        }

        @Override
        public void func_75246_d() {
            super.func_75246_d();
            this.field_179486_b.func_175515_b(this.field_75265_b);
        }
    }

    static class AIRaidFarm extends EntityAIMoveToBlock {

        private final EntityRabbit field_179500_c;
        private boolean field_179498_d;
        private boolean field_179499_e;

        public AIRaidFarm(EntityRabbit entityrabbit) {
            super(entityrabbit, 0.699999988079071D, 16);
            this.field_179500_c = entityrabbit;
        }

        @Override
        public boolean func_75250_a() {
            if (this.field_179496_a <= 0) {
                if (!this.field_179500_c.field_70170_p.func_82736_K().func_82766_b("mobGriefing")) {
                    return false;
                }

                this.field_179499_e = false;
                this.field_179498_d = this.field_179500_c.func_175534_cv();
                this.field_179498_d = true;
            }

            return super.func_75250_a();
        }

        @Override
        public boolean func_75253_b() {
            return this.field_179499_e && super.func_75253_b();
        }

        @Override
        public void func_75246_d() {
            super.func_75246_d();
            this.field_179500_c.func_70671_ap().func_75650_a(this.field_179494_b.func_177958_n() + 0.5D, this.field_179494_b.func_177956_o() + 1, this.field_179494_b.func_177952_p() + 0.5D, 10.0F, this.field_179500_c.func_70646_bf());
            if (this.func_179487_f()) {
                World world = this.field_179500_c.field_70170_p;
                BlockPos blockposition = this.field_179494_b.func_177984_a();
                IBlockState iblockdata = world.func_180495_p(blockposition);
                Block block = iblockdata.func_177230_c();

                if (this.field_179499_e && block instanceof BlockCarrot) {
                    Integer integer = iblockdata.func_177229_b(BlockCarrot.field_176488_a);

                    if (integer.intValue() == 0) {
                        // CraftBukkit start
                        if (org.bukkit.craftbukkit.event.CraftEventFactory.callEntityChangeBlockEvent(this.field_179500_c, blockposition, Blocks.field_150350_a, 0).isCancelled()) {
                            return;
                        }
                        // CraftBukkit end
                        world.func_180501_a(blockposition, Blocks.field_150350_a.func_176223_P(), 2);
                        world.func_175655_b(blockposition, true);
                    } else {
                        // CraftBukkit start
                        if (org.bukkit.craftbukkit.event.CraftEventFactory.callEntityChangeBlockEvent(
                                this.field_179500_c,
                                blockposition,
                                block, block.func_176201_c(iblockdata.func_177226_a(BlockCarrot.field_176488_a, Integer.valueOf(integer.intValue() - 1)))
                        ).isCancelled()) {
                            return;
                        }
                        // CraftBukkit end
                        world.func_180501_a(blockposition, iblockdata.func_177226_a(BlockCarrot.field_176488_a, Integer.valueOf(integer.intValue() - 1)), 2);
                        world.func_175718_b(2001, blockposition, Block.func_176210_f(iblockdata));
                    }

                    this.field_179500_c.func_175528_cn();
                }

                this.field_179499_e = false;
                this.field_179496_a = 10;
            }

        }

        @Override
        protected boolean func_179488_a(World world, BlockPos blockposition) {
            Block block = world.func_180495_p(blockposition).func_177230_c();

            if (block == Blocks.field_150458_ak && this.field_179498_d && !this.field_179499_e) {
                blockposition = blockposition.func_177984_a();
                IBlockState iblockdata = world.func_180495_p(blockposition);

                block = iblockdata.func_177230_c();
                if (block instanceof BlockCarrot && ((BlockCarrot) block).func_185525_y(iblockdata)) {
                    this.field_179499_e = true;
                    return true;
                }
            }

            return false;
        }
    }

    static class AIAvoidEntity<T extends Entity> extends EntityAIAvoidEntity<T> {

        private final EntityRabbit field_179511_d;

        public AIAvoidEntity(EntityRabbit entityrabbit, Class<T> oclass, float f, double d0, double d1) {
            super(entityrabbit, oclass, f, d0, d1);
            this.field_179511_d = entityrabbit;
        }

        @Override
        public boolean func_75250_a() {
            return this.field_179511_d.func_175531_cl() != 99 && super.func_75250_a();
        }
    }

    static class RabbitMoveHelper extends EntityMoveHelper {

        private final EntityRabbit field_179929_g;
        private double field_188492_j;

        public RabbitMoveHelper(EntityRabbit entityrabbit) {
            super(entityrabbit);
            this.field_179929_g = entityrabbit;
        }

        @Override
        public void func_75641_c() {
            if (this.field_179929_g.field_70122_E && !this.field_179929_g.field_70703_bu && !((EntityRabbit.RabbitJumpHelper) this.field_179929_g.field_70767_i).func_180067_c()) {
                this.field_179929_g.func_175515_b(0.0D);
            } else if (this.func_75640_a()) {
                this.field_179929_g.func_175515_b(this.field_188492_j);
            }

            super.func_75641_c();
        }

        @Override
        public void func_75642_a(double d0, double d1, double d2, double d3) {
            if (this.field_179929_g.func_70090_H()) {
                d3 = 1.5D;
            }

            super.func_75642_a(d0, d1, d2, d3);
            if (d3 > 0.0D) {
                this.field_188492_j = d3;
            }

        }
    }

    public class RabbitJumpHelper extends EntityJumpHelper {

        private final EntityRabbit field_180070_c;
        private boolean field_180068_d;

        public RabbitJumpHelper(EntityRabbit entityrabbit) {
            super(entityrabbit);
            this.field_180070_c = entityrabbit;
        }

        public boolean func_180067_c() {
            return this.field_75662_b;
        }

        public boolean func_180065_d() {
            return this.field_180068_d;
        }

        public void func_180066_a(boolean flag) {
            this.field_180068_d = flag;
        }

        @Override
        public void func_75661_b() {
            if (this.field_75662_b) {
                this.field_180070_c.func_184770_cZ();
                this.field_75662_b = false;
            }

        }
    }

    public static class RabbitTypeData implements IEntityLivingData {

        public int field_179427_a;

        public RabbitTypeData(int i) {
            this.field_179427_a = i;
        }
    }
}
