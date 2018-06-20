package net.minecraft.entity.monster;

import java.util.UUID;
import javax.annotation.Nullable;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityZombieVillager extends EntityZombie {

    private static final DataParameter<Boolean> field_184739_bx = EntityDataManager.func_187226_a(EntityZombieVillager.class, DataSerializers.field_187198_h);
    private static final DataParameter<Integer> field_190739_c = EntityDataManager.func_187226_a(EntityZombieVillager.class, DataSerializers.field_187192_b);
    private int field_82234_d;
    private UUID field_191992_by;
    private int lastTick = MinecraftServer.currentTick; // CraftBukkit - add field

    public EntityZombieVillager(World world) {
        super(world);
    }

    @Override
    protected void func_70088_a() {
        super.func_70088_a();
        this.field_70180_af.func_187214_a(EntityZombieVillager.field_184739_bx, Boolean.valueOf(false));
        this.field_70180_af.func_187214_a(EntityZombieVillager.field_190739_c, Integer.valueOf(0));
    }

    public void func_190733_a(int i) {
        this.field_70180_af.func_187227_b(EntityZombieVillager.field_190739_c, Integer.valueOf(i));
    }

    public int func_190736_dl() {
        return Math.max(this.field_70180_af.func_187225_a(EntityZombieVillager.field_190739_c).intValue() % 6, 0);
    }

    public static void func_190737_b(DataFixer dataconvertermanager) {
        EntityLiving.func_189752_a(dataconvertermanager, EntityZombieVillager.class);
    }

    @Override
    public void func_70014_b(NBTTagCompound nbttagcompound) {
        super.func_70014_b(nbttagcompound);
        nbttagcompound.func_74768_a("Profession", this.func_190736_dl());
        nbttagcompound.func_74768_a("ConversionTime", this.func_82230_o() ? this.field_82234_d : -1);
        if (this.field_191992_by != null) {
            nbttagcompound.func_186854_a("ConversionPlayer", this.field_191992_by);
        }

    }

    @Override
    public void func_70037_a(NBTTagCompound nbttagcompound) {
        super.func_70037_a(nbttagcompound);
        this.func_190733_a(nbttagcompound.func_74762_e("Profession"));
        if (nbttagcompound.func_150297_b("ConversionTime", 99) && nbttagcompound.func_74762_e("ConversionTime") > -1) {
            this.func_191991_a(nbttagcompound.func_186855_b("ConversionPlayer") ? nbttagcompound.func_186857_a("ConversionPlayer") : null, nbttagcompound.func_74762_e("ConversionTime"));
        }

    }

    @Override
    @Nullable
    public IEntityLivingData func_180482_a(DifficultyInstance difficultydamagescaler, @Nullable IEntityLivingData groupdataentity) {
        this.func_190733_a(this.field_70170_p.field_73012_v.nextInt(6));
        return super.func_180482_a(difficultydamagescaler, groupdataentity);
    }

    @Override
    public void func_70071_h_() {
        if (!this.field_70170_p.field_72995_K && this.func_82230_o()) {
            int i = this.func_190735_dq();
            // CraftBukkit start - Use wall time instead of ticks for villager conversion
            int elapsedTicks = MinecraftServer.currentTick - this.lastTick;
            this.lastTick = MinecraftServer.currentTick;
            i *= elapsedTicks;
            // CraftBukkit end

            this.field_82234_d -= i;
            if (this.field_82234_d <= 0) {
                this.func_190738_dp();
            }
        }

        super.func_70071_h_();
    }

    @Override
    public boolean func_184645_a(EntityPlayer entityhuman, EnumHand enumhand) {
        ItemStack itemstack = entityhuman.func_184586_b(enumhand);

        if (itemstack.func_77973_b() == Items.field_151153_ao && itemstack.func_77960_j() == 0 && this.func_70644_a(MobEffects.field_76437_t)) {
            if (!entityhuman.field_71075_bZ.field_75098_d) {
                itemstack.func_190918_g(1);
            }

            if (!this.field_70170_p.field_72995_K) {
                this.func_191991_a(entityhuman.func_110124_au(), this.field_70146_Z.nextInt(2401) + 3600);
            }

            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean func_70692_ba() {
        return !this.func_82230_o();
    }

    public boolean func_82230_o() {
        return this.func_184212_Q().func_187225_a(EntityZombieVillager.field_184739_bx).booleanValue();
    }

    protected void func_191991_a(@Nullable UUID uuid, int i) {
        this.field_191992_by = uuid;
        this.field_82234_d = i;
        this.func_184212_Q().func_187227_b(EntityZombieVillager.field_184739_bx, Boolean.valueOf(true));
        this.func_184589_d(MobEffects.field_76437_t);
        this.func_70690_d(new PotionEffect(MobEffects.field_76420_g, i, Math.min(this.field_70170_p.func_175659_aa().func_151525_a() - 1, 0)));
        this.field_70170_p.func_72960_a(this, (byte) 16);
    }

    protected void func_190738_dp() {
        EntityVillager entityvillager = new EntityVillager(this.field_70170_p);

        entityvillager.func_82149_j(this);
        entityvillager.func_70938_b(this.func_190736_dl());
        entityvillager.func_190672_a(this.field_70170_p.func_175649_E(new BlockPos(entityvillager)), (IEntityLivingData) null, false);
        entityvillager.func_82187_q();
        if (this.func_70631_g_()) {
            entityvillager.func_70873_a(-24000);
        }

        this.field_70170_p.func_72900_e(this);
        entityvillager.func_94061_f(this.func_175446_cd());
        if (this.func_145818_k_()) {
            entityvillager.func_96094_a(this.func_95999_t());
            entityvillager.func_174805_g(this.func_174833_aM());
        }

        this.field_70170_p.addEntity(entityvillager, org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason.CURED); // CraftBukkit - add SpawnReason
        if (this.field_191992_by != null) {
            EntityPlayer entityhuman = this.field_70170_p.func_152378_a(this.field_191992_by);

            if (entityhuman instanceof EntityPlayerMP) {
                CriteriaTriggers.field_192137_q.func_192183_a((EntityPlayerMP) entityhuman, this, entityvillager);
            }
        }

        entityvillager.func_70690_d(new PotionEffect(MobEffects.field_76431_k, 200, 0));
        this.field_70170_p.func_180498_a((EntityPlayer) null, 1027, new BlockPos((int) this.field_70165_t, (int) this.field_70163_u, (int) this.field_70161_v), 0);
    }

    protected int func_190735_dq() {
        int i = 1;

        if (this.field_70146_Z.nextFloat() < 0.01F) {
            int j = 0;
            BlockPos.MutableBlockPos blockposition_mutableblockposition = new BlockPos.MutableBlockPos();

            for (int k = (int) this.field_70165_t - 4; k < (int) this.field_70165_t + 4 && j < 14; ++k) {
                for (int l = (int) this.field_70163_u - 4; l < (int) this.field_70163_u + 4 && j < 14; ++l) {
                    for (int i1 = (int) this.field_70161_v - 4; i1 < (int) this.field_70161_v + 4 && j < 14; ++i1) {
                        Block block = this.field_70170_p.func_180495_p(blockposition_mutableblockposition.func_181079_c(k, l, i1)).func_177230_c();

                        if (block == Blocks.field_150411_aY || block == Blocks.field_150324_C) {
                            if (this.field_70146_Z.nextFloat() < 0.3F) {
                                ++i;
                            }

                            ++j;
                        }
                    }
                }
            }
        }

        return i;
    }

    @Override
    protected float func_70647_i() {
        return this.func_70631_g_() ? (this.field_70146_Z.nextFloat() - this.field_70146_Z.nextFloat()) * 0.2F + 2.0F : (this.field_70146_Z.nextFloat() - this.field_70146_Z.nextFloat()) * 0.2F + 1.0F;
    }

    @Override
    public SoundEvent func_184639_G() {
        return SoundEvents.field_187940_hn;
    }

    @Override
    public SoundEvent func_184601_bQ(DamageSource damagesource) {
        return SoundEvents.field_187944_hr;
    }

    @Override
    public SoundEvent func_184615_bR() {
        return SoundEvents.field_187943_hq;
    }

    @Override
    public SoundEvent func_190731_di() {
        return SoundEvents.field_187946_ht;
    }

    @Override
    @Nullable
    protected ResourceLocation func_184647_J() {
        return LootTableList.field_191183_as;
    }

    @Override
    protected ItemStack func_190732_dj() {
        return ItemStack.field_190927_a;
    }
}
