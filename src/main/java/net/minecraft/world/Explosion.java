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

    private final boolean field_77286_a;
    private final boolean field_82755_b;
    private final Random field_77290_i = new Random();
    private final World field_77287_j;
    private final double field_77284_b;
    private final double field_77285_c;
    private final double field_77282_d;
    public final Entity field_77283_e;
    private final float field_77280_f;
    private final List<BlockPos> field_77281_g = Lists.newArrayList();
    private final Map<EntityPlayer, Vec3d> field_77288_k = Maps.newHashMap();
    public boolean wasCanceled = false; // CraftBukkit - add field

    public Explosion(World world, Entity entity, double d0, double d1, double d2, float f, boolean flag, boolean flag1) {
        this.field_77287_j = world;
        this.field_77283_e = entity;
        this.field_77280_f = (float) Math.max(f, 0.0); // CraftBukkit - clamp bad values
        this.field_77284_b = d0;
        this.field_77285_c = d1;
        this.field_77282_d = d2;
        this.field_77286_a = flag;
        this.field_82755_b = flag1;
    }

    public void func_77278_a() {
        // CraftBukkit start
        if (this.field_77280_f < 0.1F) {
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
                        float f = this.field_77280_f * (0.7F + this.field_77287_j.field_73012_v.nextFloat() * 0.6F);
                        double d4 = this.field_77284_b;
                        double d5 = this.field_77285_c;
                        double d6 = this.field_77282_d;

                        for (float f1 = 0.3F; f > 0.0F; f -= 0.22500001F) {
                            BlockPos blockposition = new BlockPos(d4, d5, d6);
                            IBlockState iblockdata = this.field_77287_j.func_180495_p(blockposition);

                            if (iblockdata.func_185904_a() != Material.field_151579_a) {
                                float f2 = this.field_77283_e != null ? this.field_77283_e.func_180428_a(this, this.field_77287_j, blockposition, iblockdata) : iblockdata.func_177230_c().func_149638_a((Entity) null);

                                f -= (f2 + 0.3F) * 0.3F;
                            }

                            if (f > 0.0F && (this.field_77283_e == null || this.field_77283_e.func_174816_a(this, this.field_77287_j, blockposition, iblockdata, f)) && blockposition.func_177956_o() < 256 && blockposition.func_177956_o() >= 0) { // CraftBukkit - don't wrap explosions
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

        this.field_77281_g.addAll(hashset);
        float f3 = this.field_77280_f * 2.0F;

        i = MathHelper.func_76128_c(this.field_77284_b - (double) f3 - 1.0D);
        j = MathHelper.func_76128_c(this.field_77284_b + (double) f3 + 1.0D);
        int l = MathHelper.func_76128_c(this.field_77285_c - (double) f3 - 1.0D);
        int i1 = MathHelper.func_76128_c(this.field_77285_c + (double) f3 + 1.0D);
        int j1 = MathHelper.func_76128_c(this.field_77282_d - (double) f3 - 1.0D);
        int k1 = MathHelper.func_76128_c(this.field_77282_d + (double) f3 + 1.0D);
        // Paper start - Fix lag from explosions processing dead entities
        List list = this.field_77287_j.func_175674_a(this.field_77283_e, new AxisAlignedBB((double) i, (double) l, (double) j1, (double) j, (double) i1, (double) k1), new com.google.common.base.Predicate<Entity>() {
            @Override
            public boolean apply(Entity entity) {
                return EntitySelectors.field_188444_d.apply(entity) && !entity.field_70128_L;
            }
        });
        // Paper end
        Vec3d vec3d = new Vec3d(this.field_77284_b, this.field_77285_c, this.field_77282_d);

        for (int l1 = 0; l1 < list.size(); ++l1) {
            Entity entity = (Entity) list.get(l1);

            if (!entity.func_180427_aV()) {
                double d7 = entity.func_70011_f(this.field_77284_b, this.field_77285_c, this.field_77282_d) / (double) f3;

                if (d7 <= 1.0D) {
                    double d8 = entity.field_70165_t - this.field_77284_b;
                    double d9 = entity.field_70163_u + (double) entity.func_70047_e() - this.field_77285_c;
                    double d10 = entity.field_70161_v - this.field_77282_d;
                    double d11 = (double) MathHelper.func_76133_a(d8 * d8 + d9 * d9 + d10 * d10);

                    if (d11 != 0.0D) {
                        d8 /= d11;
                        d9 /= d11;
                        d10 /= d11;
                        double d12 = this.getBlockDensity(vec3d, entity.func_174813_aQ()); // Paper - Optimize explosions
                        double d13 = (1.0D - d7) * d12;

                        // CraftBukkit start
                        // entity.damageEntity(DamageSource.explosion(this), (float) ((int) ((d13 * d13 + d13) / 2.0D * 7.0D * (double) f3 + 1.0D)));
                        CraftEventFactory.entityDamage = field_77283_e;
                        entity.forceExplosionKnockback = false;
                        boolean wasDamaged = entity.func_70097_a(DamageSource.func_94539_a(this), (float) ((int) ((d13 * d13 + d13) / 2.0D * 7.0D * (double) f3 + 1.0D)));
                        CraftEventFactory.entityDamage = null;
                        if (!wasDamaged && !(entity instanceof EntityTNTPrimed || entity instanceof EntityFallingBlock) && !entity.forceExplosionKnockback) {
                            continue;
                        }
                        // CraftBukkit end
                        double d14 = d13;

                        if (entity instanceof EntityLivingBase) {
                            d14 = entity instanceof EntityPlayer && field_77287_j.paperConfig.disableExplosionKnockback ? 0 : EnchantmentProtection.func_92092_a((EntityLivingBase) entity, d13); // Paper - Disable explosion knockback
                        }

                        entity.field_70159_w += d8 * d14;
                        entity.field_70181_x += d9 * d14;
                        entity.field_70179_y += d10 * d14;
                        if (entity instanceof EntityPlayer) {
                            EntityPlayer entityhuman = (EntityPlayer) entity;

                            if (!entityhuman.func_175149_v() && (!entityhuman.func_184812_l_() && !field_77287_j.paperConfig.disableExplosionKnockback || !entityhuman.field_71075_bZ.field_75100_b)) { // Paper - Disable explosion knockback
                                this.field_77288_k.put(entityhuman, new Vec3d(d8 * d13, d9 * d13, d10 * d13));
                            }
                        }
                    }
                }
            }
        }

    }

    public void func_77279_a(boolean flag) {
        this.field_77287_j.func_184148_a((EntityPlayer) null, this.field_77284_b, this.field_77285_c, this.field_77282_d, SoundEvents.field_187539_bB, SoundCategory.BLOCKS, 4.0F, (1.0F + (this.field_77287_j.field_73012_v.nextFloat() - this.field_77287_j.field_73012_v.nextFloat()) * 0.2F) * 0.7F);
        if (this.field_77280_f >= 2.0F && this.field_82755_b) {
            this.field_77287_j.func_175688_a(EnumParticleTypes.EXPLOSION_HUGE, this.field_77284_b, this.field_77285_c, this.field_77282_d, 1.0D, 0.0D, 0.0D, new int[0]);
        } else {
            this.field_77287_j.func_175688_a(EnumParticleTypes.EXPLOSION_LARGE, this.field_77284_b, this.field_77285_c, this.field_77282_d, 1.0D, 0.0D, 0.0D, new int[0]);
        }

        Iterator iterator;
        BlockPos blockposition;

        if (this.field_82755_b) {
            // CraftBukkit start
            org.bukkit.World bworld = this.field_77287_j.getWorld();
            org.bukkit.entity.Entity explode = this.field_77283_e == null ? null : this.field_77283_e.getBukkitEntity();
            Location location = new Location(bworld, this.field_77284_b, this.field_77285_c, this.field_77282_d);

            List<org.bukkit.block.Block> blockList = Lists.newArrayList();
            for (int i1 = this.field_77281_g.size() - 1; i1 >= 0; i1--) {
                BlockPos cpos = (BlockPos) this.field_77281_g.get(i1);
                org.bukkit.block.Block bblock = bworld.getBlockAt(cpos.func_177958_n(), cpos.func_177956_o(), cpos.func_177952_p());
                if (bblock.getType() != org.bukkit.Material.AIR) {
                    blockList.add(bblock);
                }
            }

            boolean cancelled;
            List<org.bukkit.block.Block> bukkitBlocks;
            float yield;

            if (explode != null) {
                EntityExplodeEvent event = new EntityExplodeEvent(explode, location, blockList, 1.0F / this.field_77280_f);
                this.field_77287_j.getServer().getPluginManager().callEvent(event);
                cancelled = event.isCancelled();
                bukkitBlocks = event.blockList();
                yield = event.getYield();
            } else {
                BlockExplodeEvent event = new BlockExplodeEvent(location.getBlock(), blockList, 1.0F / this.field_77280_f);
                this.field_77287_j.getServer().getPluginManager().callEvent(event);
                cancelled = event.isCancelled();
                bukkitBlocks = event.blockList();
                yield = event.getYield();
            }

            this.field_77281_g.clear();

            for (org.bukkit.block.Block bblock : bukkitBlocks) {
                BlockPos coords = new BlockPos(bblock.getX(), bblock.getY(), bblock.getZ());
                field_77281_g.add(coords);
            }

            if (cancelled) {
                this.wasCanceled = true;
                return;
            }
            // CraftBukkit end
            iterator = this.field_77281_g.iterator();

            while (iterator.hasNext()) {
                blockposition = (BlockPos) iterator.next();
                IBlockState iblockdata = this.field_77287_j.func_180495_p(blockposition);
                Block block = iblockdata.func_177230_c();
                this.field_77287_j.chunkPacketBlockController.updateNearbyBlocks(this.field_77287_j, blockposition); // Paper - Anti-Xray

                if (flag) {
                    double d0 = (double) ((float) blockposition.func_177958_n() + this.field_77287_j.field_73012_v.nextFloat());
                    double d1 = (double) ((float) blockposition.func_177956_o() + this.field_77287_j.field_73012_v.nextFloat());
                    double d2 = (double) ((float) blockposition.func_177952_p() + this.field_77287_j.field_73012_v.nextFloat());
                    double d3 = d0 - this.field_77284_b;
                    double d4 = d1 - this.field_77285_c;
                    double d5 = d2 - this.field_77282_d;
                    double d6 = (double) MathHelper.func_76133_a(d3 * d3 + d4 * d4 + d5 * d5);

                    d3 /= d6;
                    d4 /= d6;
                    d5 /= d6;
                    double d7 = 0.5D / (d6 / (double) this.field_77280_f + 0.1D);

                    d7 *= (double) (this.field_77287_j.field_73012_v.nextFloat() * this.field_77287_j.field_73012_v.nextFloat() + 0.3F);
                    d3 *= d7;
                    d4 *= d7;
                    d5 *= d7;
                    this.field_77287_j.func_175688_a(EnumParticleTypes.EXPLOSION_NORMAL, (d0 + this.field_77284_b) / 2.0D, (d1 + this.field_77285_c) / 2.0D, (d2 + this.field_77282_d) / 2.0D, d3, d4, d5, new int[0]);
                    this.field_77287_j.func_175688_a(EnumParticleTypes.SMOKE_NORMAL, d0, d1, d2, d3, d4, d5, new int[0]);
                }

                if (iblockdata.func_185904_a() != Material.field_151579_a) {
                    if (block.func_149659_a(this)) {
                        // CraftBukkit - add yield
                        block.func_180653_a(this.field_77287_j, blockposition, this.field_77287_j.func_180495_p(blockposition), yield, 0);
                    }

                    this.field_77287_j.func_180501_a(blockposition, Blocks.field_150350_a.func_176223_P(), 3);
                    block.func_180652_a(this.field_77287_j, blockposition, this);
                }
            }
        }

        if (this.field_77286_a) {
            iterator = this.field_77281_g.iterator();

            while (iterator.hasNext()) {
                blockposition = (BlockPos) iterator.next();
                if (this.field_77287_j.func_180495_p(blockposition).func_185904_a() == Material.field_151579_a && this.field_77287_j.func_180495_p(blockposition.func_177977_b()).func_185913_b() && this.field_77290_i.nextInt(3) == 0) {
                    // CraftBukkit start - Ignition by explosion
                    if (!org.bukkit.craftbukkit.event.CraftEventFactory.callBlockIgniteEvent(this.field_77287_j, blockposition.func_177958_n(), blockposition.func_177956_o(), blockposition.func_177952_p(), this).isCancelled()) {
                        this.field_77287_j.func_175656_a(blockposition, Blocks.field_150480_ab.func_176223_P());
                    }
                    // CraftBukkit end
                }
            }
        }

    }

    public Map<EntityPlayer, Vec3d> func_77277_b() {
        return this.field_77288_k;
    }

    @Nullable
    public EntityLivingBase func_94613_c() {
        // CraftBukkit start - obtain Fireball shooter for explosion tracking
        return this.field_77283_e == null ? null : (this.field_77283_e instanceof EntityTNTPrimed ? ((EntityTNTPrimed) this.field_77283_e).func_94083_c() : (this.field_77283_e instanceof EntityLivingBase ? (EntityLivingBase) this.field_77283_e : (this.field_77283_e instanceof EntityFireball ? ((EntityFireball) this.field_77283_e).field_70235_a : null)));
        // CraftBukkit end
    }

    public void func_180342_d() {
        this.field_77281_g.clear();
    }

    public List<BlockPos> func_180343_e() {
        return this.field_77281_g;
    }

    // Paper start - Optimize explosions
    private float getBlockDensity(Vec3d vec3d, AxisAlignedBB aabb) {
        if (!this.field_77287_j.paperConfig.optimizeExplosions) {
            return this.field_77287_j.func_72842_a(vec3d, aabb);
        }
        CacheKey key = new CacheKey(this, aabb);
        Float blockDensity = this.field_77287_j.explosionDensityCache.get(key);
        if (blockDensity == null) {
            blockDensity = this.field_77287_j.func_72842_a(vec3d, aabb);
            this.field_77287_j.explosionDensityCache.put(key, blockDensity);
        }

        return blockDensity;
    }

    static class CacheKey {
        private final World world;
        private final double posX, posY, posZ;
        private final double minX, minY, minZ;
        private final double maxX, maxY, maxZ;

        public CacheKey(Explosion explosion, AxisAlignedBB aabb) {
            this.world = explosion.field_77287_j;
            this.posX = explosion.field_77284_b;
            this.posY = explosion.field_77285_c;
            this.posZ = explosion.field_77282_d;
            this.minX = aabb.field_72340_a;
            this.minY = aabb.field_72338_b;
            this.minZ = aabb.field_72339_c;
            this.maxX = aabb.field_72336_d;
            this.maxY = aabb.field_72337_e;
            this.maxZ = aabb.field_72334_f;
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
