package org.spigotmc;

import java.util.List;

import co.aikar.timings.MinecraftTimings;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityAmbientCreature;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.MultiPartEntityPart;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityLlama;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.effect.EntityWeatherEffect;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.server.MCUtil;
import net.minecraft.util.math.MathHelper;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;

public class ActivationRange
{

    static AxisAlignedBB maxBB = new AxisAlignedBB( 0, 0, 0, 0, 0, 0 );
    static AxisAlignedBB miscBB = new AxisAlignedBB( 0, 0, 0, 0, 0, 0 );
    static AxisAlignedBB animalBB = new AxisAlignedBB( 0, 0, 0, 0, 0, 0 );
    static AxisAlignedBB monsterBB = new AxisAlignedBB( 0, 0, 0, 0, 0, 0 );

    /**
     * Initializes an entities type on construction to specify what group this
     * entity is in for activation ranges.
     *
     * @param entity
     * @return group id
     */
    public static byte initializeEntityActivationType(Entity entity)
    {
        if ( entity instanceof EntityMob || entity instanceof EntitySlime )
        {
            return 1; // Monster
        } else if ( entity instanceof EntityCreature || entity instanceof EntityAmbientCreature )
        {
            return 2; // Animal
        } else
        {
            return 3; // Misc
        }
    }

    /**
     * These entities are excluded from Activation range checks.
     *
     * @param entity Entity to initialize
     * @param config Spigot config to determine ranges
     * @return boolean If it should always tick.
     */
    public static boolean initializeEntityActivationState(Entity entity, SpigotWorldConfig config)
    {
        if ( ( entity.activationType == 3 && config.miscActivationRange == 0 )
                || ( entity.activationType == 2 && config.animalActivationRange == 0 )
                || ( entity.activationType == 1 && config.monsterActivationRange == 0 )
                || entity instanceof EntityPlayer
                || entity instanceof EntityThrowable
                || entity instanceof EntityDragon
                || entity instanceof MultiPartEntityPart
                || entity instanceof EntityWither
                || entity instanceof EntityFireball
                || entity instanceof EntityWeatherEffect
                || entity instanceof EntityTNTPrimed
                || entity instanceof EntityFallingBlock // Paper - Always tick falling blocks
                || entity instanceof EntityEnderCrystal
                || entity instanceof EntityFireworkRocket )
        {
            return true;
        }

        return false;
    }

    /**
     * Find what entities are in range of the players in the world and set
     * active if in range.
     *
     * @param world
     */
    public static void activateEntities(World world)
    {
        MinecraftTimings.entityActivationCheckTimer.startTiming();
        final int miscActivationRange = world.spigotConfig.miscActivationRange;
        final int animalActivationRange = world.spigotConfig.animalActivationRange;
        final int monsterActivationRange = world.spigotConfig.monsterActivationRange;

        int maxRange = Math.max( monsterActivationRange, animalActivationRange );
        maxRange = Math.max( maxRange, miscActivationRange );
        maxRange = Math.min( ( world.spigotConfig.viewDistance << 4 ) - 8, maxRange );

        Chunk chunk; // Paper
        for ( EntityPlayer player : world.field_73010_i )
        {

            player.activatedTick = MinecraftServer.currentTick;
            maxBB = player.func_174813_aQ().func_72314_b( maxRange, 256, maxRange );
            miscBB = player.func_174813_aQ().func_72314_b( miscActivationRange, 256, miscActivationRange );
            animalBB = player.func_174813_aQ().func_72314_b( animalActivationRange, 256, animalActivationRange );
            monsterBB = player.func_174813_aQ().func_72314_b( monsterActivationRange, 256, monsterActivationRange );

            int i = MathHelper.func_76128_c( maxBB.field_72340_a / 16.0D );
            int j = MathHelper.func_76128_c( maxBB.field_72336_d / 16.0D );
            int k = MathHelper.func_76128_c( maxBB.field_72339_c / 16.0D );
            int l = MathHelper.func_76128_c( maxBB.field_72334_f / 16.0D );

            for ( int i1 = i; i1 <= j; ++i1 )
            {
                for ( int j1 = k; j1 <= l; ++j1 )
                {
                    if ( (chunk = MCUtil.getLoadedChunkWithoutMarkingActive(world, i1, j1 )) != null ) // Paper
                    {
                        activateChunkEntities( chunk ); // Paper
                    }
                }
            }
        }
        MinecraftTimings.entityActivationCheckTimer.stopTiming();
    }

    /**
     * Checks for the activation state of all entities in this chunk.
     *
     * @param chunk
     */
    private static void activateChunkEntities(Chunk chunk)
    {
        for ( List<Entity> slice : chunk.field_76645_j )
        {
            for ( Entity entity : slice )
            {
                if ( MinecraftServer.currentTick > entity.activatedTick )
                {
                    if ( entity.defaultActivationState )
                    {
                        entity.activatedTick = MinecraftServer.currentTick;
                        continue;
                    }
                    switch ( entity.activationType )
                    {
                        case 1:
                            if ( monsterBB.func_72326_a( entity.func_174813_aQ() ) )
                            {
                                entity.activatedTick = MinecraftServer.currentTick;
                            }
                            break;
                        case 2:
                            if ( animalBB.func_72326_a( entity.func_174813_aQ() ) )
                            {
                                entity.activatedTick = MinecraftServer.currentTick;
                            }
                            break;
                        case 3:
                        default:
                            if ( miscBB.func_72326_a( entity.func_174813_aQ() ) )
                            {
                                entity.activatedTick = MinecraftServer.currentTick;
                            }
                    }
                }
            }
        }
    }

    /**
     * If an entity is not in range, do some more checks to see if we should
     * give it a shot.
     *
     * @param entity
     * @return
     */
    public static boolean checkEntityImmunities(Entity entity)
    {
        // quick checks.
        if ( entity.field_70171_ac || entity.field_190534_ay > 0 )
        {
            return true;
        }
        if ( !( entity instanceof EntityArrow ) )
        {
            if ( !entity.field_70122_E || !entity.field_184244_h.isEmpty() || entity.func_184218_aH() )
            {
                return true;
            }
        } else if ( !( (EntityArrow) entity ).field_70254_i )
        {
            return true;
        }
        // special cases.
        if ( entity instanceof EntityLivingBase )
        {
            EntityLivingBase living = (EntityLivingBase) entity;
            if ( living.field_70718_bc > 0 || living.field_70737_aN > 0 || living.field_70713_bf.size() > 0 ) // Paper
            {
                return true;
            }
            if ( entity instanceof EntityCreature )
            {
                // Paper start
                EntityCreature creature = (EntityCreature) entity;
                if (creature.func_70638_az() != null || creature.getMovingTarget() != null) {
                    return true;
                }
                // Paper end
            }
            if ( entity instanceof EntityVillager && ( (EntityVillager) entity ).func_70941_o() )
            {
                return true;
            }
            // Paper start
            if ( entity instanceof EntityLlama && ( (EntityLlama ) entity ).inCaravan() )
            {
                return true;
            }
            // Paper end
            if ( entity instanceof EntityAnimal )
            {
                EntityAnimal animal = (EntityAnimal) entity;
                if ( animal.func_70631_g_() || animal.func_70880_s() )
                {
                    return true;
                }
                if ( entity instanceof EntitySheep && ( (EntitySheep) entity ).func_70892_o() )
                {
                    return true;
                }
            }
            if (entity instanceof EntityCreeper && ((EntityCreeper) entity).func_146078_ca()) { // isExplosive
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the entity is active for this tick.
     *
     * @param entity
     * @return
     */
    public static boolean checkIfActive(Entity entity)
    {
        // Never safe to skip fireworks or entities not yet added to chunk
        // PAIL: inChunk - boolean under datawatchers
        if ( !entity.field_70175_ag || entity instanceof EntityFireworkRocket ) {
            return true;
        }

        boolean isActive = entity.activatedTick >= MinecraftServer.currentTick || entity.defaultActivationState;

        // Should this entity tick?
        if ( !isActive )
        {
            if ( ( MinecraftServer.currentTick - entity.activatedTick - 1 ) % 20 == 0 )
            {
                // Check immunities every 20 ticks.
                if ( checkEntityImmunities( entity ) )
                {
                    // Triggered some sort of immunity, give 20 full ticks before we check again.
                    entity.activatedTick = MinecraftServer.currentTick + 20;
                }
                isActive = true;
            }
            // Add a little performance juice to active entities. Skip 1/4 if not immune.
        } else if ( !entity.defaultActivationState && entity.field_70173_aa % 4 == 0 && !checkEntityImmunities( entity ) )
        {
            isActive = false;
        }
        int x = MathHelper.func_76128_c( entity.field_70165_t );
        int z = MathHelper.func_76128_c( entity.field_70161_v );
        // Make sure not on edge of unloaded chunk
        Chunk chunk = entity.field_70170_p.getChunkIfLoaded( x >> 4, z >> 4 );
        if ( isActive && !( chunk != null && chunk.areNeighborsLoaded( 1 ) ) )
        {
            isActive = false;
        }
        // Paper start - Skip ticking in chunks scheduled for unload
        if(entity.field_70170_p.paperConfig.skipEntityTickingInChunksScheduledForUnload && (chunk == null || chunk.isUnloading() || chunk.scheduledForUnload != null))
            isActive = false;
        // Paper end
        return isActive;
    }
}
