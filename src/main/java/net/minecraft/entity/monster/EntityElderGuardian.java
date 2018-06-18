package net.minecraft.entity.monster;

import com.google.common.base.Predicate;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.play.server.SPacketChangeGameState;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityElderGuardian extends EntityGuardian {

    public EntityElderGuardian(World world) {
        super(world);
        this.setSize(this.width * 2.35F, this.height * 2.35F);
        this.enablePersistence();
        if (this.wander != null) {
            this.wander.setExecutionChance(400);
        }

    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.30000001192092896D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(8.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(80.0D);
    }

    public static void registerFixesElderGuardian(DataFixer dataconvertermanager) {
        EntityLiving.registerFixesMob(dataconvertermanager, EntityElderGuardian.class);
    }

    @Nullable
    protected ResourceLocation getLootTable() {
        return LootTableList.ENTITIES_ELDER_GUARDIAN;
    }

    public int getAttackDuration() {
        return 60;
    }

    protected SoundEvent getAmbientSound() {
        return this.isInWater() ? SoundEvents.ENTITY_ELDER_GUARDIAN_AMBIENT : SoundEvents.ENTITY_ELDERGUARDIAN_AMBIENTLAND;
    }

    protected SoundEvent getHurtSound(DamageSource damagesource) {
        return this.isInWater() ? SoundEvents.ENTITY_ELDER_GUARDIAN_HURT : SoundEvents.ENTITY_ELDER_GUARDIAN_HURT_LAND;
    }

    protected SoundEvent getDeathSound() {
        return this.isInWater() ? SoundEvents.ENTITY_ELDER_GUARDIAN_DEATH : SoundEvents.ENTITY_ELDER_GUARDIAN_DEATH_LAND;
    }

    protected SoundEvent getFlopSound() {
        return SoundEvents.ENTITY_ELDER_GUARDIAN_FLOP;
    }

    protected void updateAITasks() {
        super.updateAITasks();
        boolean flag = true;

        if ((this.ticksExisted + this.getEntityId()) % 1200 == 0) {
            Potion mobeffectlist = MobEffects.MINING_FATIGUE;
            List list = this.world.getPlayers(EntityPlayerMP.class, new Predicate() {
                public boolean a(@Nullable EntityPlayerMP entityplayer) {
                    return EntityElderGuardian.this.getDistanceSq(entityplayer) < 2500.0D && entityplayer.interactionManager.survivalOrAdventure();
                }

                public boolean apply(@Nullable Object object) {
                    return this.a((EntityPlayerMP) object);
                }
            });
            boolean flag1 = true;
            boolean flag2 = true;
            boolean flag3 = true;
            Iterator iterator = list.iterator();

            while (iterator.hasNext()) {
                EntityPlayerMP entityplayer = (EntityPlayerMP) iterator.next();

                if (!entityplayer.isPotionActive(mobeffectlist) || entityplayer.getActivePotionEffect(mobeffectlist).getAmplifier() < 2 || entityplayer.getActivePotionEffect(mobeffectlist).getDuration() < 1200) {
                    entityplayer.connection.sendPacket(new SPacketChangeGameState(10, 0.0F));
                    entityplayer.addPotionEffect(new PotionEffect(mobeffectlist, 6000, 2));
                }
            }
        }

        if (!this.hasHome()) {
            this.setHomePosAndDistance(new BlockPos(this), 16);
        }

    }
}
