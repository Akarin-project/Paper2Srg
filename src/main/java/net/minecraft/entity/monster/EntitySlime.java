package net.minecraft.entity.monster;

import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIFindEntityNearest;
import net.minecraft.entity.ai.EntityAIFindEntityNearestPlayer;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Biomes;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.storage.loot.LootTableList;
import org.bukkit.event.entity.SlimeSplitEvent;

// CraftBukkit start
import org.bukkit.event.entity.SlimeSplitEvent;
// CraftBukkit end

public class EntitySlime extends EntityLiving implements IMob {

    private static final DataParameter<Integer> field_184711_bt = EntityDataManager.func_187226_a(EntitySlime.class, DataSerializers.field_187192_b);
    public float field_70813_a;
    public float field_70811_b;
    public float field_70812_c;
    private boolean field_175452_bi;

    public EntitySlime(World world) {
        super(world);
        this.field_70765_h = new EntitySlime.SlimeMoveHelper(this);
    }

    protected void func_184651_r() {
        this.field_70714_bg.func_75776_a(1, new EntitySlime.AISlimeFloat(this));
        this.field_70714_bg.func_75776_a(2, new EntitySlime.AISlimeAttack(this));
        this.field_70714_bg.func_75776_a(3, new EntitySlime.AISlimeFaceRandom(this));
        this.field_70714_bg.func_75776_a(5, new EntitySlime.AISlimeHop(this));
        this.field_70715_bh.func_75776_a(1, new EntityAIFindEntityNearestPlayer(this));
        this.field_70715_bh.func_75776_a(3, new EntityAIFindEntityNearest(this, EntityIronGolem.class));
    }

    protected void func_70088_a() {
        super.func_70088_a();
        this.field_70180_af.func_187214_a(EntitySlime.field_184711_bt, Integer.valueOf(1));
    }

    public void func_70799_a(int i, boolean flag) {
        this.field_70180_af.func_187227_b(EntitySlime.field_184711_bt, Integer.valueOf(i));
        this.func_70105_a(0.51000005F * (float) i, 0.51000005F * (float) i);
        this.func_70107_b(this.field_70165_t, this.field_70163_u, this.field_70161_v);
        this.func_110148_a(SharedMonsterAttributes.field_111267_a).func_111128_a((double) (i * i));
        this.func_110148_a(SharedMonsterAttributes.field_111263_d).func_111128_a((double) (0.2F + 0.1F * (float) i));
        if (flag) {
            this.func_70606_j(this.func_110138_aP());
        }

        this.field_70728_aV = i;
    }

    public int func_70809_q() {
        return ((Integer) this.field_70180_af.func_187225_a(EntitySlime.field_184711_bt)).intValue();
    }

    public static void func_189758_c(DataFixer dataconvertermanager) {
        EntityLiving.func_189752_a(dataconvertermanager, EntitySlime.class);
    }

    public void func_70014_b(NBTTagCompound nbttagcompound) {
        super.func_70014_b(nbttagcompound);
        nbttagcompound.func_74768_a("Size", this.func_70809_q() - 1);
        nbttagcompound.func_74757_a("wasOnGround", this.field_175452_bi);
    }

    public void func_70037_a(NBTTagCompound nbttagcompound) {
        super.func_70037_a(nbttagcompound);
        int i = nbttagcompound.func_74762_e("Size");

        if (i < 0) {
            i = 0;
        }

        this.func_70799_a(i + 1, false);
        this.field_175452_bi = nbttagcompound.func_74767_n("wasOnGround");
    }

    public boolean func_189101_db() {
        return this.func_70809_q() <= 1;
    }

    protected EnumParticleTypes func_180487_n() {
        return EnumParticleTypes.SLIME;
    }

    public void func_70071_h_() {
        if (!this.field_70170_p.field_72995_K && this.field_70170_p.func_175659_aa() == EnumDifficulty.PEACEFUL && this.func_70809_q() > 0) {
            this.field_70128_L = true;
        }

        this.field_70811_b += (this.field_70813_a - this.field_70811_b) * 0.5F;
        this.field_70812_c = this.field_70811_b;
        super.func_70071_h_();
        if (this.field_70122_E && !this.field_175452_bi) {
            int i = this.func_70809_q();

            for (int j = 0; j < i * 8; ++j) {
                float f = this.field_70146_Z.nextFloat() * 6.2831855F;
                float f1 = this.field_70146_Z.nextFloat() * 0.5F + 0.5F;
                float f2 = MathHelper.func_76126_a(f) * (float) i * 0.5F * f1;
                float f3 = MathHelper.func_76134_b(f) * (float) i * 0.5F * f1;
                World world = this.field_70170_p;
                EnumParticleTypes enumparticle = this.func_180487_n();
                double d0 = this.field_70165_t + (double) f2;
                double d1 = this.field_70161_v + (double) f3;

                world.func_175688_a(enumparticle, d0, this.func_174813_aQ().field_72338_b, d1, 0.0D, 0.0D, 0.0D, new int[0]);
            }

            this.func_184185_a(this.func_184709_cY(), this.func_70599_aP(), ((this.field_70146_Z.nextFloat() - this.field_70146_Z.nextFloat()) * 0.2F + 1.0F) / 0.8F);
            this.field_70813_a = -0.5F;
        } else if (!this.field_70122_E && this.field_175452_bi) {
            this.field_70813_a = 1.0F;
        }

        this.field_175452_bi = this.field_70122_E;
        this.func_70808_l();
    }

    protected void func_70808_l() {
        this.field_70813_a *= 0.6F;
    }

    protected int func_70806_k() {
        return this.field_70146_Z.nextInt(20) + 10;
    }

    protected EntitySlime func_70802_j() {
        return new EntitySlime(this.field_70170_p);
    }

    public void func_184206_a(DataParameter<?> datawatcherobject) {
        if (EntitySlime.field_184711_bt.equals(datawatcherobject)) {
            int i = this.func_70809_q();

            this.func_70105_a(0.51000005F * (float) i, 0.51000005F * (float) i);
            this.field_70177_z = this.field_70759_as;
            this.field_70761_aq = this.field_70759_as;
            if (this.func_70090_H() && this.field_70146_Z.nextInt(20) == 0) {
                this.func_71061_d_();
            }
        }

        super.func_184206_a(datawatcherobject);
    }

    public void func_70106_y() {
        int i = this.func_70809_q();

        if (!this.field_70170_p.field_72995_K && i > 1 && this.func_110143_aJ() <= 0.0F) {
            int j = 2 + this.field_70146_Z.nextInt(3);

            // CraftBukkit start
            SlimeSplitEvent event = new SlimeSplitEvent((org.bukkit.entity.Slime) this.getBukkitEntity(), j);
            this.field_70170_p.getServer().getPluginManager().callEvent(event);

            if (!event.isCancelled() && event.getCount() > 0) {
                j = event.getCount();
            } else {
                super.func_70106_y();
                return;
            }
            // CraftBukkit end

            for (int k = 0; k < j; ++k) {
                float f = ((float) (k % 2) - 0.5F) * (float) i / 4.0F;
                float f1 = ((float) (k / 2) - 0.5F) * (float) i / 4.0F;
                EntitySlime entityslime = this.func_70802_j();

                if (this.func_145818_k_()) {
                    entityslime.func_96094_a(this.func_95999_t());
                }

                if (this.func_104002_bU()) {
                    entityslime.func_110163_bv();
                }

                entityslime.func_70799_a(i / 2, true);
                entityslime.func_70012_b(this.field_70165_t + (double) f, this.field_70163_u + 0.5D, this.field_70161_v + (double) f1, this.field_70146_Z.nextFloat() * 360.0F, 0.0F);
                this.field_70170_p.addEntity(entityslime, org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason.SLIME_SPLIT); // CraftBukkit - SpawnReason
            }
        }

        super.func_70106_y();
    }

    public void func_70108_f(Entity entity) {
        super.func_70108_f(entity);
        if (entity instanceof EntityIronGolem && this.func_70800_m()) {
            this.func_175451_e((EntityLivingBase) entity);
        }

    }

    public void func_70100_b_(EntityPlayer entityhuman) {
        if (this.func_70800_m()) {
            this.func_175451_e((EntityLivingBase) entityhuman);
        }

    }

    protected void func_175451_e(EntityLivingBase entityliving) {
        int i = this.func_70809_q();

        if (this.func_70685_l(entityliving) && this.func_70068_e(entityliving) < 0.6D * (double) i * 0.6D * (double) i && entityliving.func_70097_a(DamageSource.func_76358_a(this), (float) this.func_70805_n())) {
            this.func_184185_a(SoundEvents.field_187870_fk, 1.0F, (this.field_70146_Z.nextFloat() - this.field_70146_Z.nextFloat()) * 0.2F + 1.0F);
            this.func_174815_a((EntityLivingBase) this, (Entity) entityliving);
        }

    }

    public float func_70047_e() {
        return 0.625F * this.field_70131_O;
    }

    protected boolean func_70800_m() {
        return !this.func_189101_db();
    }

    protected int func_70805_n() {
        return this.func_70809_q();
    }

    protected SoundEvent func_184601_bQ(DamageSource damagesource) {
        return this.func_189101_db() ? SoundEvents.field_187898_fy : SoundEvents.field_187880_fp;
    }

    protected SoundEvent func_184615_bR() {
        return this.func_189101_db() ? SoundEvents.field_187896_fx : SoundEvents.field_187874_fm;
    }

    protected SoundEvent func_184709_cY() {
        return this.func_189101_db() ? SoundEvents.field_187900_fz : SoundEvents.field_187886_fs;
    }

    protected Item func_146068_u() {
        return this.func_70809_q() == 1 ? Items.field_151123_aH : null;
    }

    @Nullable
    protected ResourceLocation func_184647_J() {
        return this.func_70809_q() == 1 ? LootTableList.field_186378_ac : LootTableList.field_186419_a;
    }

    public boolean func_70601_bi() {
        BlockPos blockposition = new BlockPos(MathHelper.func_76128_c(this.field_70165_t), 0, MathHelper.func_76128_c(this.field_70161_v));
        Chunk chunk = this.field_70170_p.func_175726_f(blockposition);

        if (this.field_70170_p.func_72912_H().func_76067_t() == WorldType.field_77138_c && this.field_70146_Z.nextInt(4) != 1) {
            return false;
        } else {
            if (this.field_70170_p.func_175659_aa() != EnumDifficulty.PEACEFUL) {
                Biome biomebase = this.field_70170_p.func_180494_b(blockposition);

                if (biomebase == Biomes.field_76780_h && this.field_70163_u > 50.0D && this.field_70163_u < 70.0D && this.field_70146_Z.nextFloat() < 0.5F && this.field_70146_Z.nextFloat() < this.field_70170_p.func_130001_d() && this.field_70170_p.func_175671_l(new BlockPos(this)) <= this.field_70146_Z.nextInt(8)) {
                    return super.func_70601_bi();
                }
                boolean isSlimeChunk = field_70170_p.paperConfig.allChunksAreSlimeChunks || chunk.func_76617_a(field_70170_p.spigotConfig.slimeSeed).nextInt(10) == 0; // Spigot // Paper
                if (this.field_70146_Z.nextInt(10) == 0 && isSlimeChunk && this.field_70163_u < 40.0D) { // Paper
                    return super.func_70601_bi();
                }
            }

            return false;
        }
    }

    protected float func_70599_aP() {
        return 0.4F * (float) this.func_70809_q();
    }

    public int func_70646_bf() {
        return 0;
    }

    protected boolean func_70807_r() {
        return this.func_70809_q() > 0;
    }

    protected void func_70664_aZ() {
        this.field_70181_x = 0.41999998688697815D;
        this.field_70160_al = true;
    }

    @Nullable
    public IEntityLivingData func_180482_a(DifficultyInstance difficultydamagescaler, @Nullable IEntityLivingData groupdataentity) {
        int i = this.field_70146_Z.nextInt(3);

        if (i < 2 && this.field_70146_Z.nextFloat() < 0.5F * difficultydamagescaler.func_180170_c()) {
            ++i;
        }

        int j = 1 << i;

        this.func_70799_a(j, true);
        return super.func_180482_a(difficultydamagescaler, groupdataentity);
    }

    protected SoundEvent func_184710_cZ() {
        return this.func_189101_db() ? SoundEvents.field_189110_fE : SoundEvents.field_187882_fq;
    }

    static class AISlimeHop extends EntityAIBase {

        private final EntitySlime field_179458_a;

        public AISlimeHop(EntitySlime entityslime) {
            this.field_179458_a = entityslime;
            this.func_75248_a(5);
        }

        public boolean func_75250_a() {
            return true;
        }

        public void func_75246_d() {
            ((EntitySlime.SlimeMoveHelper) this.field_179458_a.func_70605_aq()).func_179921_a(1.0D);
        }
    }

    static class AISlimeFloat extends EntityAIBase {

        private final EntitySlime field_179457_a;

        public AISlimeFloat(EntitySlime entityslime) {
            this.field_179457_a = entityslime;
            this.func_75248_a(5);
            ((PathNavigateGround) entityslime.func_70661_as()).func_179693_d(true);
        }

        public boolean func_75250_a() {
            return this.field_179457_a.func_70090_H() || this.field_179457_a.func_180799_ab();
        }

        public void func_75246_d() {
            if (this.field_179457_a.func_70681_au().nextFloat() < 0.8F) {
                this.field_179457_a.func_70683_ar().func_75660_a();
            }

            ((EntitySlime.SlimeMoveHelper) this.field_179457_a.func_70605_aq()).func_179921_a(1.2D);
        }
    }

    static class AISlimeFaceRandom extends EntityAIBase {

        private final EntitySlime field_179461_a;
        private float field_179459_b;
        private int field_179460_c;

        public AISlimeFaceRandom(EntitySlime entityslime) {
            this.field_179461_a = entityslime;
            this.func_75248_a(2);
        }

        public boolean func_75250_a() {
            return this.field_179461_a.func_70638_az() == null && (this.field_179461_a.field_70122_E || this.field_179461_a.func_70090_H() || this.field_179461_a.func_180799_ab() || this.field_179461_a.func_70644_a(MobEffects.field_188424_y));
        }

        public void func_75246_d() {
            if (--this.field_179460_c <= 0) {
                this.field_179460_c = 40 + this.field_179461_a.func_70681_au().nextInt(60);
                this.field_179459_b = (float) this.field_179461_a.func_70681_au().nextInt(360);
            }

            ((EntitySlime.SlimeMoveHelper) this.field_179461_a.func_70605_aq()).func_179920_a(this.field_179459_b, false);
        }
    }

    static class AISlimeAttack extends EntityAIBase {

        private final EntitySlime field_179466_a;
        private int field_179465_b;

        public AISlimeAttack(EntitySlime entityslime) {
            this.field_179466_a = entityslime;
            this.func_75248_a(2);
        }

        public boolean func_75250_a() {
            EntityLivingBase entityliving = this.field_179466_a.func_70638_az();

            return entityliving == null ? false : (!entityliving.func_70089_S() ? false : !(entityliving instanceof EntityPlayer) || !((EntityPlayer) entityliving).field_71075_bZ.field_75102_a);
        }

        public void func_75249_e() {
            this.field_179465_b = 300;
            super.func_75249_e();
        }

        public boolean func_75253_b() {
            EntityLivingBase entityliving = this.field_179466_a.func_70638_az();

            return entityliving == null ? false : (!entityliving.func_70089_S() ? false : (entityliving instanceof EntityPlayer && ((EntityPlayer) entityliving).field_71075_bZ.field_75102_a ? false : --this.field_179465_b > 0));
        }

        public void func_75246_d() {
            this.field_179466_a.func_70625_a((Entity) this.field_179466_a.func_70638_az(), 10.0F, 10.0F);
            ((EntitySlime.SlimeMoveHelper) this.field_179466_a.func_70605_aq()).func_179920_a(this.field_179466_a.field_70177_z, this.field_179466_a.func_70800_m());
        }
    }

    static class SlimeMoveHelper extends EntityMoveHelper {

        private float field_179922_g;
        private int field_179924_h;
        private final EntitySlime field_179925_i;
        private boolean field_179923_j;

        public SlimeMoveHelper(EntitySlime entityslime) {
            super(entityslime);
            this.field_179925_i = entityslime;
            this.field_179922_g = 180.0F * entityslime.field_70177_z / 3.1415927F;
        }

        public void func_179920_a(float f, boolean flag) {
            this.field_179922_g = f;
            this.field_179923_j = flag;
        }

        public void func_179921_a(double d0) {
            this.field_75645_e = d0;
            this.field_188491_h = EntityMoveHelper.Action.MOVE_TO;
        }

        public void func_75641_c() {
            this.field_75648_a.field_70177_z = this.func_75639_a(this.field_75648_a.field_70177_z, this.field_179922_g, 90.0F);
            this.field_75648_a.field_70759_as = this.field_75648_a.field_70177_z;
            this.field_75648_a.field_70761_aq = this.field_75648_a.field_70177_z;
            if (this.field_188491_h != EntityMoveHelper.Action.MOVE_TO) {
                this.field_75648_a.func_191989_p(0.0F);
            } else {
                this.field_188491_h = EntityMoveHelper.Action.WAIT;
                if (this.field_75648_a.field_70122_E) {
                    this.field_75648_a.func_70659_e((float) (this.field_75645_e * this.field_75648_a.func_110148_a(SharedMonsterAttributes.field_111263_d).func_111126_e()));
                    if (this.field_179924_h-- <= 0) {
                        this.field_179924_h = this.field_179925_i.func_70806_k();
                        if (this.field_179923_j) {
                            this.field_179924_h /= 3;
                        }

                        this.field_179925_i.func_70683_ar().func_75660_a();
                        if (this.field_179925_i.func_70807_r()) {
                            this.field_179925_i.func_184185_a(this.field_179925_i.func_184710_cZ(), this.field_179925_i.func_70599_aP(), ((this.field_179925_i.func_70681_au().nextFloat() - this.field_179925_i.func_70681_au().nextFloat()) * 0.2F + 1.0F) * 0.8F);
                        }
                    } else {
                        this.field_179925_i.field_70702_br = 0.0F;
                        this.field_179925_i.field_191988_bg = 0.0F;
                        this.field_75648_a.func_70659_e(0.0F);
                    }
                } else {
                    this.field_75648_a.func_70659_e((float) (this.field_75645_e * this.field_75648_a.func_110148_a(SharedMonsterAttributes.field_111263_d).func_111126_e()));
                }

            }
        }
    }
}
