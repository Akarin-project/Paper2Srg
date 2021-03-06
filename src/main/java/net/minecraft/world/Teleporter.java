package net.minecraft.world;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import net.minecraft.block.BlockPortal;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockPattern;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3i;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.entity.EntityPortalExitEvent;
import org.bukkit.event.world.PortalCreateEvent;
import org.bukkit.util.Vector;

// CraftBukkit start
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.entity.EntityPortalExitEvent;
import org.bukkit.event.world.PortalCreateEvent;
import org.bukkit.util.Vector;
// CraftBukkit end

public class Teleporter {

    protected final WorldServer field_85192_a; // Paper - private -> protected
    private final Random field_77187_a;
    private final Long2ObjectMap<Teleporter.PortalPosition> field_85191_c = new Long2ObjectOpenHashMap(4096);

    public Teleporter(WorldServer worldserver) {
        this.field_85192_a = worldserver;
        this.field_77187_a = new Random(worldserver.func_72905_C());
    }

    public void func_180266_a(Entity entity, float f) {
        if (this.field_85192_a.field_73011_w.func_186058_p().func_186068_a() != 1) {
            if (!this.func_180620_b(entity, f)) {
                this.func_85188_a(entity);
                this.func_180620_b(entity, f);
            }
        } else {
            int i = MathHelper.func_76128_c(entity.field_70165_t);
            int j = MathHelper.func_76128_c(entity.field_70163_u) - 1;
            int k = MathHelper.func_76128_c(entity.field_70161_v);
            // CraftBukkit start - Modularize end portal creation
            BlockPos created = this.createEndPortal(entity.field_70165_t, entity.field_70163_u, entity.field_70161_v);
            entity.func_70012_b((double) created.func_177958_n(), (double) created.func_177956_o(), (double) created.func_177952_p(), entity.field_70177_z, 0.0F);
            entity.field_70159_w = entity.field_70181_x = entity.field_70179_y = 0.0D;
        }
    }

    // Split out from original a(Entity, double, double, double, float) method in order to enable being called from createPortal
    private BlockPos createEndPortal(double x, double y, double z) {
            int i = MathHelper.func_76128_c(x);
            int j = MathHelper.func_76128_c(y) - 1;
            int k = MathHelper.func_76128_c(z);
            // CraftBukkit end
            byte b0 = 1;
            byte b1 = 0;

            Collection<Block> bukkitBlocks = new HashSet<>(); // Paper
            Map<BlockPos, IBlockState> nmsBlocks = new HashMap<>(); // Paper

            for (int l = -2; l <= 2; ++l) {
                for (int i1 = -2; i1 <= 2; ++i1) {
                    for (int j1 = -1; j1 < 3; ++j1) {
                        int k1 = i + i1 * 1 + l * 0;
                        int l1 = j + j1;
                        int i2 = k + i1 * 0 - l * 1;
                        boolean flag2 = j1 < 0;

                        // Paper start
                        BlockPos pos = new BlockPos(k1, l1, i2);
                        nmsBlocks.putIfAbsent(pos, flag2 ? Blocks.field_150343_Z.func_176223_P() : Blocks.field_150350_a.func_176223_P());
                        bukkitBlocks.add(this.field_85192_a.getWorld().getBlockAt(pos.func_177958_n(), pos.func_177956_o(), pos.func_177952_p()));
                        // Paper end
                    }
                }
            }

            // Paper start
            PortalCreateEvent event = new PortalCreateEvent(bukkitBlocks, this.field_85192_a.getWorld(), PortalCreateEvent.CreateReason.OBC_DESTINATION);
            if(event.callEvent()){
                nmsBlocks.forEach(this.field_85192_a::func_175656_a);
            }
            // Paper end

        // CraftBukkit start
        return new BlockPos(i, k, k);
    }

    // use logic based on creation to verify end portal
    private BlockPos findEndPortal(BlockPos portal) {
        int i = portal.func_177958_n();
        int j = portal.func_177956_o() - 1;
        int k = portal.func_177952_p();
        byte b0 = 1;
        byte b1 = 0;

        for (int l = -2; l <= 2; ++l) {
            for (int i1 = -2; i1 <= 2; ++i1) {
                for (int j1 = -1; j1 < 3; ++j1) {
                    int k1 = i + i1 * b0 + l * b1;
                    int l1 = j + j1;
                    int i2 = k + i1 * b1 - l * b0;
                    boolean flag = j1 < 0;

                    if (this.field_85192_a.func_180495_p(new BlockPos(k1, l1, i2)).func_177230_c() != (flag ? Blocks.field_150343_Z : Blocks.field_150350_a)) {
                        return null;
                    }
                }
            }
        }
        return new BlockPos(i, j, k);
    }
    // CraftBukkit end

    public boolean func_180620_b(Entity entity, float f) {
        // CraftBukkit start - Modularize portal search process and entity teleportation
        BlockPos found = this.findPortal(entity.field_70165_t, entity.field_70163_u, entity.field_70161_v, field_85192_a.paperConfig.portalSearchRadius); // Paper - Configurable search radius
        if (found == null) {
            return false;
        }

        Location exit = new Location(this.field_85192_a.getWorld(), found.func_177958_n(), found.func_177956_o(), found.func_177952_p(), f, entity.field_70125_A);
        Vector velocity = entity.getBukkitEntity().getVelocity();
        this.adjustExit(entity, exit, velocity);
        entity.func_70012_b(exit.getX(), exit.getY(), exit.getZ(), exit.getYaw(), exit.getPitch());
        if (entity.field_70159_w != velocity.getX() || entity.field_70181_x != velocity.getY() || entity.field_70179_y != velocity.getZ()) {
            entity.getBukkitEntity().setVelocity(velocity);
        }
        return true;
    }

    public BlockPos findPortal(double x, double y, double z, int radius) {
        if (this.field_85192_a.getWorld().getEnvironment() == org.bukkit.World.Environment.THE_END) {
            return this.findEndPortal(this.field_85192_a.field_73011_w.func_177496_h());
        }
        // CraftBukkit end
        double d0 = -1.0D;
        // CraftBukkit start
        int i = MathHelper.func_76128_c(x);
        int j = MathHelper.func_76128_c(z);
        // CraftBukkit end
        boolean flag1 = true;
        Object object = BlockPos.field_177992_a;
        long k = ChunkPos.func_77272_a(i, j);

        if (this.field_85191_c.containsKey(k)) {
            Teleporter.PortalPosition portaltravelagent_chunkcoordinatesportal = (Teleporter.PortalPosition) this.field_85191_c.get(k);

            d0 = 0.0D;
            object = portaltravelagent_chunkcoordinatesportal;
            portaltravelagent_chunkcoordinatesportal.field_85087_d = this.field_85192_a.func_82737_E();
            flag1 = false;
        } else {
            BlockPos blockposition = new BlockPos(x, y, z); // CraftBukkit

            for (int l = -radius; l <= radius; ++l) {
                BlockPos blockposition1;

                for (int i1 = -radius; i1 <= radius; ++i1) {
                    for (BlockPos blockposition2 = blockposition.func_177982_a(l, this.field_85192_a.func_72940_L() - 1 - blockposition.func_177956_o(), i1); blockposition2.func_177956_o() >= 0; blockposition2 = blockposition1) {
                        blockposition1 = blockposition2.func_177977_b();
                        if (this.field_85192_a.func_180495_p(blockposition2).func_177230_c() == Blocks.field_150427_aO) {
                            for (blockposition1 = blockposition2.func_177977_b(); this.field_85192_a.func_180495_p(blockposition1).func_177230_c() == Blocks.field_150427_aO; blockposition1 = blockposition1.func_177977_b()) {
                                blockposition2 = blockposition1;
                            }

                            double d1 = blockposition2.func_177951_i(blockposition);

                            if (d0 < 0.0D || d1 < d0) {
                                d0 = d1;
                                object = blockposition2;
                            }
                        }
                    }
                }
            }
        }

        if (d0 >= 0.0D) {
            if (flag1) {
                this.field_85191_c.put(k, new Teleporter.PortalPosition((BlockPos) object, this.field_85192_a.func_82737_E()));
            }
            // CraftBukkit start - Move entity teleportation logic into exit
            return (BlockPos) object;
        } else {
            return null;
        }
    }

    // Entity repositioning logic split out from original b method and combined with repositioning logic for The End from original a method
    public void adjustExit(Entity entity, Location position, Vector velocity) {
        Location from = position.clone();
        Vector before = velocity.clone();
        BlockPos object = new BlockPos(position.getBlockX(), position.getBlockY(), position.getBlockZ());
        float f = position.getYaw();

        if (this.field_85192_a.getWorld().getEnvironment() == org.bukkit.World.Environment.THE_END || entity.getBukkitEntity().getWorld().getEnvironment() == org.bukkit.World.Environment.THE_END || entity.func_181014_aG() == null) {
            // entity.setPositionRotation((double) i, (double) j, (double) k, entity.yaw, 0.0F);
            // entity.motX = entity.motY = entity.motZ = 0.0D;
            position.setPitch(0.0F);
            velocity.setX(0);
            velocity.setY(0);
            velocity.setZ(0);
        } else {
            // CraftBukkit end

            double d2 = (double) ((BlockPos) object).func_177958_n() + 0.5D;
            double d3 = (double) ((BlockPos) object).func_177952_p() + 0.5D;
            BlockPattern.PatternHelper shapedetector_shapedetectorcollection = Blocks.field_150427_aO.func_181089_f(this.field_85192_a, (BlockPos) object);
            boolean flag2 = shapedetector_shapedetectorcollection.func_177669_b().func_176746_e().func_176743_c() == EnumFacing.AxisDirection.NEGATIVE;
            double d4 = shapedetector_shapedetectorcollection.func_177669_b().func_176740_k() == EnumFacing.Axis.X ? (double) shapedetector_shapedetectorcollection.func_181117_a().func_177952_p() : (double) shapedetector_shapedetectorcollection.func_181117_a().func_177958_n();
            double d5 = (double) (shapedetector_shapedetectorcollection.func_181117_a().func_177956_o() + 1) - entity.func_181014_aG().field_72448_b * (double) shapedetector_shapedetectorcollection.func_181119_e();

            if (flag2) {
                ++d4;
            }

            if (shapedetector_shapedetectorcollection.func_177669_b().func_176740_k() == EnumFacing.Axis.X) {
                d3 = d4 + (1.0D - entity.func_181014_aG().field_72450_a) * (double) shapedetector_shapedetectorcollection.func_181118_d() * (double) shapedetector_shapedetectorcollection.func_177669_b().func_176746_e().func_176743_c().func_179524_a();
            } else {
                d2 = d4 + (1.0D - entity.func_181014_aG().field_72450_a) * (double) shapedetector_shapedetectorcollection.func_181118_d() * (double) shapedetector_shapedetectorcollection.func_177669_b().func_176746_e().func_176743_c().func_179524_a();
            }

            float f1 = 0.0F;
            float f2 = 0.0F;
            float f3 = 0.0F;
            float f4 = 0.0F;

            if (shapedetector_shapedetectorcollection.func_177669_b().func_176734_d() == entity.func_181012_aH()) {
                f1 = 1.0F;
                f2 = 1.0F;
            } else if (shapedetector_shapedetectorcollection.func_177669_b().func_176734_d() == entity.func_181012_aH().func_176734_d()) {
                f1 = -1.0F;
                f2 = -1.0F;
            } else if (shapedetector_shapedetectorcollection.func_177669_b().func_176734_d() == entity.func_181012_aH().func_176746_e()) {
                f3 = 1.0F;
                f4 = -1.0F;
            } else {
                f3 = -1.0F;
                f4 = 1.0F;
            }

            // CraftBukkit start
            double d6 = velocity.getX();
            double d7 = velocity.getZ();
            // CraftBukkit end

            // CraftBukkit start - Adjust position and velocity instances instead of entity
            velocity.setX(d6 * (double) f1 + d7 * (double) f4);
            velocity.setZ(d6 * (double) f3 + d7 * (double) f2);
            f = f - (float) (entity.func_181012_aH().func_176734_d().func_176736_b() * 90) + (float) (shapedetector_shapedetectorcollection.func_177669_b().func_176736_b() * 90);
            // entity.setPositionRotation(d2, d5, d3, entity.yaw, entity.pitch);
            position.setX(d2);
            position.setY(d5);
            position.setZ(d3);
            position.setYaw(f);
        }
        EntityPortalExitEvent event = new EntityPortalExitEvent(entity.getBukkitEntity(), from, position, before, velocity);
        this.field_85192_a.getServer().getPluginManager().callEvent(event);
        Location to = event.getTo();
        if (event.isCancelled() || to == null || !entity.func_70089_S()) {
            position.setX(from.getX());
            position.setY(from.getY());
            position.setZ(from.getZ());
            position.setYaw(from.getYaw());
            position.setPitch(from.getPitch());
            velocity.copy(before);
        } else {
            position.setX(to.getX());
            position.setY(to.getY());
            position.setZ(to.getZ());
            position.setYaw(to.getYaw());
            position.setPitch(to.getPitch());
            velocity.copy(event.getAfter()); // event.getAfter() will never be null, as setAfter() will cause an NPE if null is passed in
        }
        // CraftBukkit end
    }

    public boolean func_85188_a(Entity entity) {
        // CraftBukkit start - Allow for portal creation to be based on coordinates instead of entity
        return this.createPortal(entity.field_70165_t, entity.field_70163_u, entity.field_70161_v, 16);
    }

    public boolean createPortal(double x, double y, double z, int b0) {
        if (this.field_85192_a.getWorld().getEnvironment() == org.bukkit.World.Environment.THE_END) {
            createEndPortal(x, y, z);
            return true;
        }
        // CraftBukkit end
        double d0 = -1.0D;
        // CraftBukkit start
        int i = MathHelper.func_76128_c(x);
        int j = MathHelper.func_76128_c(y);
        int k = MathHelper.func_76128_c(z);
        // CraftBukkit end
        int l = i;
        int i1 = j;
        int j1 = k;
        int k1 = 0;
        int l1 = this.field_77187_a.nextInt(4);
        BlockPos.MutableBlockPos blockposition_mutableblockposition = new BlockPos.MutableBlockPos();

        int i2;
        double d1;
        int j2;
        double d2;
        int k2;
        int l2;
        int i3;
        int j3;
        int k3;
        int l3;
        int i4;
        int j4;
        int k4;
        double d3;
        double d4;

        for (i2 = i - 16; i2 <= i + 16; ++i2) {
            d1 = (double) i2 + 0.5D - x; // CraftBukkit

            for (j2 = k - 16; j2 <= k + 16; ++j2) {
                d2 = (double) j2 + 0.5D - z; // CraftBukkit

                label271:
                for (k2 = this.field_85192_a.func_72940_L() - 1; k2 >= 0; --k2) {
                    if (this.field_85192_a.func_175623_d(blockposition_mutableblockposition.func_181079_c(i2, k2, j2))) {
                        while (k2 > 0 && this.field_85192_a.func_175623_d(blockposition_mutableblockposition.func_181079_c(i2, k2 - 1, j2))) {
                            --k2;
                        }

                        for (l2 = l1; l2 < l1 + 4; ++l2) {
                            i3 = l2 % 2;
                            j3 = 1 - i3;
                            if (l2 % 4 >= 2) {
                                i3 = -i3;
                                j3 = -j3;
                            }

                            for (k3 = 0; k3 < 3; ++k3) {
                                for (l3 = 0; l3 < 4; ++l3) {
                                    for (i4 = -1; i4 < 4; ++i4) {
                                        j4 = i2 + (l3 - 1) * i3 + k3 * j3;
                                        k4 = k2 + i4;
                                        int l4 = j2 + (l3 - 1) * j3 - k3 * i3;

                                        blockposition_mutableblockposition.func_181079_c(j4, k4, l4);
                                        if (i4 < 0 && !this.field_85192_a.func_180495_p(blockposition_mutableblockposition).func_185904_a().func_76220_a() || i4 >= 0 && !this.field_85192_a.func_175623_d(blockposition_mutableblockposition)) {
                                            continue label271;
                                        }
                                    }
                                }
                            }

                            d3 = (double) k2 + 0.5D - y; // CraftBukkit
                            d4 = d1 * d1 + d3 * d3 + d2 * d2;
                            if (d0 < 0.0D || d4 < d0) {
                                d0 = d4;
                                l = i2;
                                i1 = k2;
                                j1 = j2;
                                k1 = l2 % 4;
                            }
                        }
                    }
                }
            }
        }

        if (d0 < 0.0D) {
            for (i2 = i - 16; i2 <= i + 16; ++i2) {
                d1 = (double) i2 + 0.5D - x; // CraftBukkit

                for (j2 = k - 16; j2 <= k + 16; ++j2) {
                    d2 = (double) j2 + 0.5D - z; // CraftBukkit

                    label219:
                    for (k2 = this.field_85192_a.func_72940_L() - 1; k2 >= 0; --k2) {
                        if (this.field_85192_a.func_175623_d(blockposition_mutableblockposition.func_181079_c(i2, k2, j2))) {
                            while (k2 > 0 && this.field_85192_a.func_175623_d(blockposition_mutableblockposition.func_181079_c(i2, k2 - 1, j2))) {
                                --k2;
                            }

                            for (l2 = l1; l2 < l1 + 2; ++l2) {
                                i3 = l2 % 2;
                                j3 = 1 - i3;

                                for (k3 = 0; k3 < 4; ++k3) {
                                    for (l3 = -1; l3 < 4; ++l3) {
                                        i4 = i2 + (k3 - 1) * i3;
                                        j4 = k2 + l3;
                                        k4 = j2 + (k3 - 1) * j3;
                                        blockposition_mutableblockposition.func_181079_c(i4, j4, k4);
                                        if (l3 < 0 && !this.field_85192_a.func_180495_p(blockposition_mutableblockposition).func_185904_a().func_76220_a() || l3 >= 0 && !this.field_85192_a.func_175623_d(blockposition_mutableblockposition)) {
                                            continue label219;
                                        }
                                    }
                                }

                                d3 = (double) k2 + 0.5D - y; // CraftBukkit
                                d4 = d1 * d1 + d3 * d3 + d2 * d2;
                                if (d0 < 0.0D || d4 < d0) {
                                    d0 = d4;
                                    l = i2;
                                    i1 = k2;
                                    j1 = j2;
                                    k1 = l2 % 2;
                                }
                            }
                        }
                    }
                }
            }
        }

        int i5 = l;
        int j5 = i1;

        j2 = j1;
        int k5 = k1 % 2;
        int l5 = 1 - k5;

        if (k1 % 4 >= 2) {
            k5 = -k5;
            l5 = -l5;
        }

        Collection<Block> bukkitBlocks = new HashSet<>(); // Paper
        Map<BlockPos, IBlockState> nmsBlocks = new HashMap<>(); // Paper

        if (d0 < 0.0D) {
            i1 = MathHelper.func_76125_a(i1, 70, this.field_85192_a.func_72940_L() - 10);
            j5 = i1;

            for (k2 = -1; k2 <= 1; ++k2) {
                for (l2 = 1; l2 < 3; ++l2) {
                    for (i3 = -1; i3 < 3; ++i3) {
                        j3 = i5 + (l2 - 1) * k5 + k2 * l5;
                        k3 = j5 + i3;
                        l3 = j2 + (l2 - 1) * l5 - k2 * k5;
                        boolean flag1 = i3 < 0;

                        // Paper start
                        BlockPos pos = new BlockPos(j3, k3, l3);
                        nmsBlocks.putIfAbsent(pos, flag1 ? Blocks.field_150343_Z.func_176223_P() : Blocks.field_150350_a.func_176223_P());
                        bukkitBlocks.add(this.field_85192_a.getWorld().getBlockAt(pos.func_177958_n(), pos.func_177956_o(), pos.func_177952_p()));
                        // Paper end
                    }
                }
            }
        }

        IBlockState iblockdata = Blocks.field_150427_aO.func_176223_P().func_177226_a(BlockPortal.field_176550_a, k5 == 0 ? EnumFacing.Axis.Z : EnumFacing.Axis.X);

        for (l2 = 0; l2 < 4; ++l2) {
            for (i3 = 0; i3 < 4; ++i3) {
                for (j3 = -1; j3 < 4; ++j3) {
                    k3 = i5 + (i3 - 1) * k5;
                    l3 = j5 + j3;
                    i4 = j2 + (i3 - 1) * l5;
                    boolean flag2 = i3 == 0 || i3 == 3 || j3 == -1 || j3 == 3;

                    // Paper start
                    BlockPos pos = new BlockPos(k3, l3, i4);
                    nmsBlocks.putIfAbsent(pos, flag2 ? Blocks.field_150343_Z.func_176223_P() : iblockdata);
                    bukkitBlocks.add(this.field_85192_a.getWorld().getBlockAt(pos.func_177958_n(), pos.func_177956_o(), pos.func_177952_p()));
                    // Paper end
                }
            }

            for (i3 = 0; i3 < 4; ++i3) {
                for (j3 = -1; j3 < 4; ++j3) {
                    k3 = i5 + (i3 - 1) * k5;
                    l3 = j5 + j3;
                    i4 = j2 + (i3 - 1) * l5;
                    BlockPos blockposition = new BlockPos(k3, l3, i4);

                    this.field_85192_a.func_175685_c(blockposition, this.field_85192_a.func_180495_p(blockposition).func_177230_c(), false);
                }
            }
        }

        // Paper start
        PortalCreateEvent event = new PortalCreateEvent(bukkitBlocks, this.field_85192_a.getWorld(), PortalCreateEvent.CreateReason.OBC_DESTINATION);
        if(event.callEvent()){
            nmsBlocks.forEach((pos, data) -> this.field_85192_a.func_180501_a(pos, data, 2));
        }
        // Paper end

        return true;
    }

    public void func_85189_a(long i) {
        if (i % 100L == 0L) {
            long j = i - 300L;
            ObjectIterator objectiterator = this.field_85191_c.values().iterator();

            while (objectiterator.hasNext()) {
                Teleporter.PortalPosition portaltravelagent_chunkcoordinatesportal = (Teleporter.PortalPosition) objectiterator.next();

                if (portaltravelagent_chunkcoordinatesportal == null || portaltravelagent_chunkcoordinatesportal.field_85087_d < j) {
                    objectiterator.remove();
                }
            }
        }

    }

    public class PortalPosition extends BlockPos {

        public long field_85087_d;

        public PortalPosition(BlockPos blockposition, long i) {
            super(blockposition.func_177958_n(), blockposition.func_177956_o(), blockposition.func_177952_p());
            this.field_85087_d = i;
        }

        @Override
        public int compareTo(Vec3i o) {
            return this.compareTo(o);
        }
    }
}
