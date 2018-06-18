package net.minecraft.world;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentProtection;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.server.Explosion.CacheKey;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.bukkit.Location;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

// CraftBukkit start
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.Location;
import org.bukkit.event.block.BlockExplodeEvent;
// CraftBukkit end

public class Explosion {

    private final boolean causesFire;
    private final boolean damagesTerrain;
    private final Random random = new Random();
    private final World world;
    private final double x;
    private final double y;
    private final double z;
    public final Entity exploder;
    private final float size;
    private final List<BlockPos> affectedBlockPositions = Lists.newArrayList();
    private final Map<EntityPlayer, Vec3d> playerKnockbackMap = Maps.newHashMap();
    public boolean wasCanceled = false; // CraftBukkit - add field

    public Explosion(World world, Entity entity, double d0, double d1, double d2, float f, boolean flag, boolean flag1) {
        this.world = world;
        this.exploder = entity;
        this.size = (float) Math.max(f, 0.0); // CraftBukkit - clamp bad values
        this.x = d0;
        this.y = d1;
        this.z = d2;
        this.causesFire = flag;
        this.damagesTerrain = flag1;
    }

    public void doExplosionA() {
        // CraftBukkit start
        if (this.size < 0.1F) {
            return;
        }
        // CraftBukkit end
        HashSet hashset = Sets.newHashSet();
        boolean flag = true;

        int i;
        int j;

        for (int k = 0; k < 16; ++k) {
            for (i = 0; i < 16; ++i) {
                for (j = 0; j < 16; ++j) {
                    if (k == 0 || k == 15 || i == 0 || i == 15 || j == 0 || j == 15) {
                        double d0 = (double) ((float) k / 15.0F * 2.0F - 1.0F);
                        double d1 = (double) ((float) i / 15.0F * 2.0F - 1.0F);
                        double d2 = (double) ((float) j / 15.0F * 2.0F - 1.0F);
                        double d3 = Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);

                        d0 /= d3;
                        d1 /= d3;
                        d2 /= d3;
                        float f = this.size * (0.7F + this.world.rand.nextFloat() * 0.6F);
                        double d4 = this.x;
                        double d5 = this.y;
                        double d6 = this.z;

                        for (float f1 = 0.3F; f > 0.0F; f -= 0.22500001F) {
                            BlockPos blockposition = new BlockPos(d4, d5, d6);
                            IBlockState iblockdata = this.world.getBlockState(blockposition);

                            if (iblockdata.getMaterial() != Material.AIR) {
                                float f2 = this.exploder != null ? this.exploder.getExplosionResistance(this, this.world, blockposition, iblockdata) : iblockdata.getBlock().getExplosionResistance((Entity) null);

                                f -= (f2 + 0.3F) * 0.3F;
                            }

                            if (f > 0.0F && (this.exploder == null || this.exploder.canExplosionDestroyBlock(this, this.world, blockposition, iblockdata, f)) && blockposition.getY() < 256 && blockposition.getY() >= 0) { // CraftBukkit - don't wrap explosions
                                hashset.add(blockposition);
                            }

                            d4 += d0 * 0.30000001192092896D;
                            d5 += d1 * 0.30000001192092896D;
                            d6 += d2 * 0.30000001192092896D;
                        }
                    }
                }
            }
        }

        this.affectedBlockPositions.addAll(hashset);
        float f3 = this.size * 2.0F;

        i = MathHelper.floor(this.x - (double) f3 - 1.0D);
        j = MathHelper.floor(this.x + (double) f3 + 1.0D);
        int l = MathHelper.floor(this.y - (double) f3 - 1.0D);
        int i1 = MathHelper.floor(this.y + (double) f3 + 1.0D);
        int j1 = MathHelper.floor(this.z - (double) f3 - 1.0D);
        int k1 = MathHelper.floor(this.z + (double) f3 + 1.0D);
        // Paper start - Fix lag from explosions processing dead entities
        List list = this.world.getEntitiesInAABBexcluding(this.exploder, new AxisAlignedBB((double) i, (double) l, (double) j1, (double) j, (double) i1, (double) k1), new com.google.common.base.Predicate<Entity>() {
            @Override
            public boolean apply(Entity entity) {
                return EntitySelectors.CAN_AI_TARGET.apply(entity) && !entity.isDead;
            }
        });
        // Paper end
        Vec3d vec3d = new Vec3d(this.x, this.y, this.z);

        for (int l1 = 0; l1 < list.size(); ++l1) {
            Entity entity = (Entity) list.get(l1);

            if (!entity.isImmuneToExplosions()) {
                double d7 = entity.getDistance(this.x, this.y, this.z) / (double) f3;

                if (d7 <= 1.0D) {
                    double d8 = entity.posX - this.x;
                    double d9 = entity.posY + (double) entity.getEyeHeight() - this.y;
                    double d10 = entity.posZ - this.z;
                    double d11 = (double) MathHelper.sqrt(d8 * d8 + d9 * d9 + d10 * d10);

                    if (d11 != 0.0D) {
                        d8 /= d11;
                        d9 /= d11;
                        d10 /= d11;
                        double d12 = this.getBlockDensity(vec3d, entity.getEntityBoundingBox()); // Paper - Optimize explosions
                        double d13 = (1.0D - d7) * d12;

                        // CraftBukkit start
                        // entity.damageEntity(DamageSource.explosion(this), (float) ((int) ((d13 * d13 + d13) / 2.0D * 7.0D * (double) f3 + 1.0D)));
                        CraftEventFactory.entityDamage = exploder;
                        entity.forceExplosionKnockback = false;
                        boolean wasDamaged = entity.attackEntityFrom(DamageSource.causeExplosionDamage(this), (float) ((int) ((d13 * d13 + d13) / 2.0D * 7.0D * (double) f3 + 1.0D)));
                        CraftEventFactory.entityDamage = null;
                        if (!wasDamaged && !(entity instanceof EntityTNTPrimed || entity instanceof EntityFallingBlock) && !entity.forceExplosionKnockback) {
                            continue;
                        }
                        // CraftBukkit end
                        double d14 = d13;

                        if (entity instanceof EntityLivingBase) {
                            d14 = entity instanceof EntityPlayer && world.paperConfig.disableExplosionKnockback ? 0 : EnchantmentProtection.getBlastDamageReduction((EntityLivingBase) entity, d13); // Paper - Disable explosion knockback
                        }

                        entity.motionX += d8 * d14;
                        entity.motionY += d9 * d14;
                        entity.motionZ += d10 * d14;
                        if (entity instanceof EntityPlayer) {
                            EntityPlayer entityhuman = (EntityPlayer) entity;

                            if (!entityhuman.isSpectator() && (!entityhuman.isCreative() && !world.paperConfig.disableExplosionKnockback || !entityhuman.capabilities.isFlying)) { // Paper - Disable explosion knockback
                                this.playerKnockbackMap.put(entityhuman, new Vec3d(d8 * d13, d9 * d13, d10 * d13));
                            }
                        }
                    }
                }
            }
        }

    }

    public void doExplosionB(boolean flag) {
        this.world.playSound((EntityPlayer) null, this.x, this.y, this.z, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 4.0F, (1.0F + (this.world.rand.nextFloat() - this.world.rand.nextFloat()) * 0.2F) * 0.7F);
        if (this.size >= 2.0F && this.damagesTerrain) {
            this.world.spawnParticle(EnumParticleTypes.EXPLOSION_HUGE, this.x, this.y, this.z, 1.0D, 0.0D, 0.0D, new int[0]);
        } else {
            this.world.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, this.x, this.y, this.z, 1.0D, 0.0D, 0.0D, new int[0]);
        }

        Iterator iterator;
        BlockPos blockposition;

        if (this.damagesTerrain) {
            // CraftBukkit start
            org.bukkit.World bworld = this.world.getWorld();
            org.bukkit.entity.Entity explode = this.exploder == null ? null : this.exploder.getBukkitEntity();
            Location location = new Location(bworld, this.x, this.y, this.z);

            List<org.bukkit.block.Block> blockList = Lists.newArrayList();
            for (int i1 = this.affectedBlockPositions.size() - 1; i1 >= 0; i1--) {
                BlockPos cpos = (BlockPos) this.affectedBlockPositions.get(i1);
                org.bukkit.block.Block bblock = bworld.getBlockAt(cpos.getX(), cpos.getY(), cpos.getZ());
                if (bblock.getType() != org.bukkit.Material.AIR) {
                    blockList.add(bblock);
                }
            }

            boolean cancelled;
            List<org.bukkit.block.Block> bukkitBlocks;
            float yield;

            if (explode != null) {
                EntityExplodeEvent event = new EntityExplodeEvent(explode, location, blockList, 1.0F / this.size);
                this.world.getServer().getPluginManager().callEvent(event);
                cancelled = event.isCancelled();
                bukkitBlocks = event.blockList();
                yield = event.getYield();
            } else {
                BlockExplodeEvent event = new BlockExplodeEvent(location.getBlock(), blockList, 1.0F / this.size);
                this.world.getServer().getPluginManager().callEvent(event);
                cancelled = event.isCancelled();
                bukkitBlocks = event.blockList();
                yield = event.getYield();
            }

            this.affectedBlockPositions.clear();

            for (org.bukkit.block.Block bblock : bukkitBlocks) {
                BlockPos coords = new BlockPos(bblock.getX(), bblock.getY(), bblock.getZ());
                affectedBlockPositions.add(coords);
            }

            if (cancelled) {
                this.wasCanceled = true;
                return;
            }
            // CraftBukkit end
            iterator = this.affectedBlockPositions.iterator();

            while (iterator.hasNext()) {
                blockposition = (BlockPos) iterator.next();
                IBlockState iblockdata = this.world.getBlockState(blockposition);
                Block block = iblockdata.getBlock();
                this.world.chunkPacketBlockController.updateNearbyBlocks(this.world, blockposition); // Paper - Anti-Xray

                if (flag) {
                    double d0 = (double) ((float) blockposition.getX() + this.world.rand.nextFloat());
                    double d1 = (double) ((float) blockposition.getY() + this.world.rand.nextFloat());
                    double d2 = (double) ((float) blockposition.getZ() + this.world.rand.nextFloat());
                    double d3 = d0 - this.x;
                    double d4 = d1 - this.y;
                    double d5 = d2 - this.z;
                    double d6 = (double) MathHelper.sqrt(d3 * d3 + d4 * d4 + d5 * d5);

                    d3 /= d6;
                    d4 /= d6;
                    d5 /= d6;
                    double d7 = 0.5D / (d6 / (double) this.size + 0.1D);

                    d7 *= (double) (this.world.rand.nextFloat() * this.world.rand.nextFloat() + 0.3F);
                    d3 *= d7;
                    d4 *= d7;
                    d5 *= d7;
                    this.world.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, (d0 + this.x) / 2.0D, (d1 + this.y) / 2.0D, (d2 + this.z) / 2.0D, d3, d4, d5, new int[0]);
                    this.world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0, d1, d2, d3, d4, d5, new int[0]);
                }

                if (iblockdata.getMaterial() != Material.AIR) {
                    if (block.canDropFromExplosion(this)) {
                        // CraftBukkit - add yield
                        block.dropBlockAsItemWithChance(this.world, blockposition, this.world.getBlockState(blockposition), yield, 0);
                    }

                    this.world.setBlockState(blockposition, Blocks.AIR.getDefaultState(), 3);
                    block.onBlockDestroyedByExplosion(this.world, blockposition, this);
                }
            }
        }

        if (this.causesFire) {
            iterator = this.affectedBlockPositions.iterator();

            while (iterator.hasNext()) {
                blockposition = (BlockPos) iterator.next();
                if (this.world.getBlockState(blockposition).getMaterial() == Material.AIR && this.world.getBlockState(blockposition.down()).isFullBlock() && this.random.nextInt(3) == 0) {
                    // CraftBukkit start - Ignition by explosion
                    if (!org.bukkit.craftbukkit.event.CraftEventFactory.callBlockIgniteEvent(this.world, blockposition.getX(), blockposition.getY(), blockposition.getZ(), this).isCancelled()) {
                        this.world.setBlockState(blockposition, Blocks.FIRE.getDefaultState());
                    }
                    // CraftBukkit end
                }
            }
        }

    }

    public Map<EntityPlayer, Vec3d> getPlayerKnockbackMap() {
        return this.playerKnockbackMap;
    }

    @Nullable
    public EntityLivingBase getExplosivePlacedBy() {
        // CraftBukkit start - obtain Fireball shooter for explosion tracking
        return this.exploder == null ? null : (this.exploder instanceof EntityTNTPrimed ? ((EntityTNTPrimed) this.exploder).getTntPlacedBy() : (this.exploder instanceof EntityLivingBase ? (EntityLivingBase) this.exploder : (this.exploder instanceof EntityFireball ? ((EntityFireball) this.exploder).shootingEntity : null)));
        // CraftBukkit end
    }

    public void clearAffectedBlockPositions() {
        this.affectedBlockPositions.clear();
    }

    public List<BlockPos> getAffectedBlockPositions() {
        return this.affectedBlockPositions;
    }

    // Paper start - Optimize explosions
    private float getBlockDensity(Vec3d vec3d, AxisAlignedBB aabb) {
        if (!this.world.paperConfig.optimizeExplosions) {
            return this.world.getBlockDensity(vec3d, aabb);
        }
        CacheKey key = new CacheKey(this, aabb);
        Float blockDensity = this.world.explosionDensityCache.get(key);
        if (blockDensity == null) {
            blockDensity = this.world.getBlockDensity(vec3d, aabb);
            this.world.explosionDensityCache.put(key, blockDensity);
        }

        return blockDensity;
    }

    static class CacheKey {
        private final World world;
        private final double posX, posY, posZ;
        private final double minX, minY, minZ;
        private final double maxX, maxY, maxZ;

        public CacheKey(Explosion explosion, AxisAlignedBB aabb) {
            this.world = explosion.world;
            this.posX = explosion.x;
            this.posY = explosion.y;
            this.posZ = explosion.z;
            this.minX = aabb.minX;
            this.minY = aabb.minY;
            this.minZ = aabb.minZ;
            this.maxX = aabb.maxX;
            this.maxY = aabb.maxY;
            this.maxZ = aabb.maxZ;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            CacheKey cacheKey = (CacheKey) o;

            if (Double.compare(cacheKey.posX, posX) != 0) return false;
            if (Double.compare(cacheKey.posY, posY) != 0) return false;
            if (Double.compare(cacheKey.posZ, posZ) != 0) return false;
            if (Double.compare(cacheKey.minX, minX) != 0) return false;
            if (Double.compare(cacheKey.minY, minY) != 0) return false;
            if (Double.compare(cacheKey.minZ, minZ) != 0) return false;
            if (Double.compare(cacheKey.maxX, maxX) != 0) return false;
            if (Double.compare(cacheKey.maxY, maxY) != 0) return false;
            if (Double.compare(cacheKey.maxZ, maxZ) != 0) return false;
            return world.equals(cacheKey.world);
        }

        @Override
        public int hashCode() {
            int result;
            long temp;
            result = world.hashCode();
            temp = Double.doubleToLongBits(posX);
            result = 31 * result + (int) (temp ^ (temp >>> 32));
            temp = Double.doubleToLongBits(posY);
            result = 31 * result + (int) (temp ^ (temp >>> 32));
            temp = Double.doubleToLongBits(posZ);
            result = 31 * result + (int) (temp ^ (temp >>> 32));
            temp = Double.doubleToLongBits(minX);
            result = 31 * result + (int) (temp ^ (temp >>> 32));
            temp = Double.doubleToLongBits(minY);
            result = 31 * result + (int) (temp ^ (temp >>> 32));
            temp = Double.doubleToLongBits(minZ);
            result = 31 * result + (int) (temp ^ (temp >>> 32));
            temp = Double.doubleToLongBits(maxX);
            result = 31 * result + (int) (temp ^ (temp >>> 32));
            temp = Double.doubleToLongBits(maxY);
            result = 31 * result + (int) (temp ^ (temp >>> 32));
            temp = Double.doubleToLongBits(maxZ);
            result = 31 * result + (int) (temp ^ (temp >>> 32));
            return result;
        }
    }
    // Paper end
}
