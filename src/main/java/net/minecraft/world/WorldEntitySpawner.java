package net.minecraft.world;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

import com.destroystokyo.paper.exception.ServerInternalException;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRailBase;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.server.MCUtil;
import net.minecraft.server.management.PlayerChunkMapEntry;
import net.minecraft.util.WeightedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.ChunkProviderServer;
import org.bukkit.craftbukkit.util.LongHash;
import org.bukkit.craftbukkit.util.LongHashSet;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

// CraftBukkit start
import com.destroystokyo.paper.exception.ServerInternalException;
import org.bukkit.craftbukkit.util.LongHash;
import org.bukkit.craftbukkit.util.LongHashSet;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
// CraftBukkit end

public final class WorldEntitySpawner {

    private static final int field_180268_a = (int) Math.pow(17.0D, 2.0D);
    private final LongHashSet field_77193_b = new LongHashSet(); // CraftBukkit

    public WorldEntitySpawner() {}

    // Spigot start - get entity count only from chunks being processed in b
    private int getEntityCount(WorldServer server, Class oClass)
    {
        // Paper start - use entire world, not just active chunks. Spigot broke vanilla expectations.
        if (true) {
            return server
                    .func_72863_F()
                    .field_73244_f.values()
                    .stream()
                    .collect(java.util.stream.Collectors.summingInt(c -> c.entityCount.get(oClass)));
        }
        // Paper end
        int i = 0;
        Iterator<Long> it = this.field_77193_b.iterator();
        while ( it.hasNext() )
        {
            Long coord = it.next();
            int x = LongHash.msw( coord );
            int z = LongHash.lsw( coord );
            if ( !((ChunkProviderServer)server.field_73020_y).field_73248_b.contains( coord ) && server.func_175680_a( x, z, true ) )
            {
                i += server.func_72964_e( x, z ).entityCount.get( oClass );
            }
        }
        return i;
    }
    // Spigot end

    public int func_77192_a(WorldServer worldserver, boolean flag, boolean flag1, boolean flag2) {
        org.spigotmc.AsyncCatcher.catchOp("check for eligible spawn chunks"); // Paper - At least until we figure out what is calling this async
        if (!flag && !flag1) {
            return 0;
        } else {
            this.field_77193_b.clear();
            int i = 0;
            Iterator iterator = worldserver.field_73010_i.iterator();

            int j;
            int k;

            while (iterator.hasNext()) {
                EntityPlayer entityhuman = (EntityPlayer) iterator.next();

                if (!entityhuman.func_175149_v() && entityhuman.affectsSpawning) {
                    int l = MathHelper.func_76128_c(entityhuman.field_70165_t / 16.0D);

                    j = MathHelper.func_76128_c(entityhuman.field_70161_v / 16.0D);
                    boolean flag3 = true;
                    // Spigot Start
                    byte b0 = worldserver.spigotConfig.mobSpawnRange;
                    b0 = ( b0 > worldserver.spigotConfig.viewDistance ) ? (byte) worldserver.spigotConfig.viewDistance : b0;
                    b0 = ( b0 > 8 ) ? 8 : b0;
                    // Paper start
                    com.destroystokyo.paper.event.entity.PlayerNaturallySpawnCreaturesEvent event;
                    event = new com.destroystokyo.paper.event.entity.PlayerNaturallySpawnCreaturesEvent(
                            (org.bukkit.entity.Player) entityhuman.getBukkitEntity(), b0);
                    if (!event.callEvent()) {
                        continue;
                    }
                    b0 = event.getSpawnRadius();
                    // Paperr end

                    for (int i1 = -b0; i1 <= b0; ++i1) {
                        for (k = -b0; k <= b0; ++k) {
                            boolean flag4 = i1 == -b0 || i1 == b0 || k == -b0 || k == b0;
                            // Spigot End
                            ChunkPos chunkcoordintpair = new ChunkPos(i1 + l, k + j);

                            // CraftBukkit start - use LongHash and LongHashSet
                            long chunkCoords = LongHash.toLong(chunkcoordintpair.field_77276_a, chunkcoordintpair.field_77275_b);
                            if (!this.field_77193_b.contains(chunkCoords)) {
                                ++i;
                                if (!flag4 && worldserver.func_175723_af().func_177730_a(chunkcoordintpair)) {
                                    PlayerChunkMapEntry playerchunk = worldserver.func_184164_w().func_187301_b(chunkcoordintpair.field_77276_a, chunkcoordintpair.field_77275_b);

                                    if (playerchunk != null && playerchunk.func_187274_e()) {
                                        this.field_77193_b.add(chunkCoords);
                                        // CraftBukkit end
                                    }
                                }
                            }
                        }
                    }
                }
            }

            int j1 = 0;
            BlockPos blockposition = worldserver.func_175694_M();
            EnumCreatureType[] aenumcreaturetype = EnumCreatureType.values();

            j = aenumcreaturetype.length;

            for (int k1 = 0; k1 < j; ++k1) {
                EnumCreatureType enumcreaturetype = aenumcreaturetype[k1];

               // CraftBukkit start - Use per-world spawn limits
                int limit = enumcreaturetype.func_75601_b();
                switch (enumcreaturetype) {
                    case MONSTER:
                        limit = worldserver.getWorld().getMonsterSpawnLimit();
                        break;
                    case CREATURE:
                        limit = worldserver.getWorld().getAnimalSpawnLimit();
                        break;
                    case WATER_CREATURE:
                        limit = worldserver.getWorld().getWaterAnimalSpawnLimit();
                        break;
                    case AMBIENT:
                        limit = worldserver.getWorld().getAmbientSpawnLimit();
                        break;
                }

                if (limit == 0) {
                    continue;
                }
				int mobcnt = 0; // Spigot
                // CraftBukkit end

                if ((!enumcreaturetype.func_75599_d() || flag1) && (enumcreaturetype.func_75599_d() || flag) && (!enumcreaturetype.func_82705_e() || flag2)) {
                    /* Paper start - As far as I can tell neither of these are even used
                    k = worldserver.a(enumcreaturetype.a());
                    int l1 = limit * i / a; // CraftBukkit - use per-world limits
                    */ // Paper end

                    if ((mobcnt = getEntityCount(worldserver, enumcreaturetype.func_75598_a())) <= limit * i / 289) { // Paper - use 17x17 like vanilla (a at top of file)
                        BlockPos.MutableBlockPos blockposition_mutableblockposition = new BlockPos.MutableBlockPos();
                        Iterator iterator1 = this.field_77193_b.iterator();

                        int moblimit = (limit * i / 256) - mobcnt + 1; // Spigot - up to 1 more than limit
                        label120:
                        while (iterator1.hasNext() && (moblimit > 0)) { // Spigot - while more allowed
                            // CraftBukkit start = use LongHash and LongObjectHashMap
                            long key = ((Long) iterator1.next()).longValue();
                            BlockPos blockposition1 = func_180621_a(worldserver, LongHash.msw(key), LongHash.lsw(key));
                            // CraftBukkit
                            int i2 = blockposition1.func_177958_n();
                            int j2 = blockposition1.func_177956_o();
                            int k2 = blockposition1.func_177952_p();
                            IBlockState iblockdata = worldserver.func_180495_p(blockposition1);

                            if (!iblockdata.func_185915_l()) {
                                int l2 = 0;
                                int i3 = 0;

                                while (i3 < 3) {
                                    int j3 = i2;
                                    int k3 = j2;
                                    int l3 = k2;
                                    boolean flag5 = true;
                                    Biome.SpawnListEntry biomebase_biomemeta = null;
                                    IEntityLivingData groupdataentity = null;
                                    int i4 = MathHelper.func_76143_f(Math.random() * 4.0D);
                                    int j4 = 0;

                                    while (true) {
                                        if (j4 < i4) {
                                            label113: {
                                                j3 += worldserver.field_73012_v.nextInt(6) - worldserver.field_73012_v.nextInt(6);
                                                k3 += worldserver.field_73012_v.nextInt(1) - worldserver.field_73012_v.nextInt(1);
                                                l3 += worldserver.field_73012_v.nextInt(6) - worldserver.field_73012_v.nextInt(6);
                                                blockposition_mutableblockposition.func_181079_c(j3, k3, l3);
                                                float f = (float) j3 + 0.5F;
                                                float f1 = (float) l3 + 0.5F;

                                                if (!worldserver.func_175636_b((double) f, (double) k3, (double) f1, 24.0D) && blockposition.func_177954_c((double) f, (double) k3, (double) f1) >= 576.0D) {
                                                    if (biomebase_biomemeta == null) {
                                                        biomebase_biomemeta = worldserver.func_175734_a(enumcreaturetype, (BlockPos) blockposition_mutableblockposition);
                                                        if (biomebase_biomemeta == null) {
                                                            break label113;
                                                        }
                                                    }

                                                    if (worldserver.func_175732_a(enumcreaturetype, biomebase_biomemeta, (BlockPos) blockposition_mutableblockposition) && func_180267_a(EntitySpawnPlacementRegistry.func_180109_a(biomebase_biomemeta.field_76300_b), worldserver, blockposition_mutableblockposition)) {
                                                        // Paper start
                                                        com.destroystokyo.paper.event.entity.PreCreatureSpawnEvent event;
                                                        Class<? extends EntityLiving> cls = biomebase_biomemeta.field_76300_b;
                                                        org.bukkit.entity.EntityType type = EntityList.clsToTypeMap.get(cls);
                                                        if (type != null) {
                                                            event = new com.destroystokyo.paper.event.entity.PreCreatureSpawnEvent(
                                                                    MCUtil.toLocation(worldserver, blockposition_mutableblockposition),
                                                                    type, SpawnReason.NATURAL
                                                            );
                                                            if (!event.callEvent()) {
                                                                if (event.shouldAbortSpawn()) {
                                                                    continue label120;
                                                                }
                                                                j1 += l2;
                                                                ++j4;
                                                                continue;
                                                            }
                                                        }
                                                        // Paper end
                                                        EntityLiving entityinsentient;

                                                        try {
                                                            entityinsentient = (EntityLiving) biomebase_biomemeta.field_76300_b.getConstructor(new Class[] { World.class}).newInstance(new Object[] { worldserver});
                                                        } catch (Exception exception) {
                                                            exception.printStackTrace();
                                                            ServerInternalException.reportInternalException(exception); // Paper
                                                            return j1;
                                                        }

                                                        entityinsentient.func_70012_b((double) f, (double) k3, (double) f1, worldserver.field_73012_v.nextFloat() * 360.0F, 0.0F);
                                                        if (entityinsentient.func_70601_bi() && entityinsentient.func_70058_J()) {
                                                            groupdataentity = entityinsentient.func_180482_a(worldserver.func_175649_E(new BlockPos(entityinsentient)), groupdataentity);
                                                            if (entityinsentient.func_70058_J()) {
                                                                // CraftBukkit start
                                                                if (worldserver.addEntity(entityinsentient, SpawnReason.NATURAL)) {
                                                                    ++l2;
                                                                    moblimit--; // Spigot
                                                                }
                                                                // CraftBukkit end
                                                            } else {
                                                                entityinsentient.func_70106_y();
                                                            }

                                                            // Spigot start
                                                            if ( moblimit <= 0 ) {
                                                                // If we're past limit, stop spawn
                                                                // Spigot end
                                                                continue label120;
                                                            }
                                                        }

                                                        j1 += l2;
                                                    }
                                                }

                                                ++j4;
                                                continue;
                                            }
                                        }

                                        ++i3;
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }

            return j1;
        }
    }

    private static BlockPos func_180621_a(World world, int i, int j) {
        Chunk chunk = world.func_72964_e(i, j);
        int k = i * 16 + world.field_73012_v.nextInt(16);
        int l = j * 16 + world.field_73012_v.nextInt(16);
        int i1 = MathHelper.func_154354_b(chunk.func_177433_f(new BlockPos(k, 0, l)) + 1, 16);
        int j1 = world.field_73012_v.nextInt(i1 > 0 ? i1 : chunk.func_76625_h() + 16 - 1);

        return new BlockPos(k, j1, l);
    }

    public static boolean func_185331_a(IBlockState iblockdata) {
        return iblockdata.func_185898_k() ? false : (iblockdata.func_185897_m() ? false : (iblockdata.func_185904_a().func_76224_d() ? false : !BlockRailBase.func_176563_d(iblockdata)));
    }

    public static boolean func_180267_a(EntityLiving.SpawnPlacementType entityinsentient_enumentitypositiontype, World world, BlockPos blockposition) {
        if (!world.func_175723_af().func_177746_a(blockposition)) {
            return false;
        } else {
            IBlockState iblockdata = world.func_180495_p(blockposition);

            if (entityinsentient_enumentitypositiontype == EntityLiving.SpawnPlacementType.IN_WATER) {
                return iblockdata.func_185904_a() == Material.field_151586_h && world.func_180495_p(blockposition.func_177977_b()).func_185904_a() == Material.field_151586_h && !world.func_180495_p(blockposition.func_177984_a()).func_185915_l();
            } else {
                BlockPos blockposition1 = blockposition.func_177977_b();

                if (!world.func_180495_p(blockposition1).func_185896_q()) {
                    return false;
                } else {
                    Block block = world.func_180495_p(blockposition1).func_177230_c();
                    boolean flag = block != Blocks.field_150357_h && block != Blocks.field_180401_cv;

                    return flag && func_185331_a(iblockdata) && func_185331_a(world.func_180495_p(blockposition.func_177984_a()));
                }
            }
        }
    }

    public static void func_77191_a(World world, Biome biomebase, int i, int j, int k, int l, Random random) {
        List list = biomebase.func_76747_a(EnumCreatureType.CREATURE);

        if (!list.isEmpty()) {
            while (random.nextFloat() < biomebase.func_76741_f()) {
                Biome.SpawnListEntry biomebase_biomemeta = (Biome.SpawnListEntry) WeightedRandom.func_76271_a(world.field_73012_v, list);
                int i1 = biomebase_biomemeta.field_76301_c + random.nextInt(1 + biomebase_biomemeta.field_76299_d - biomebase_biomemeta.field_76301_c);
                IEntityLivingData groupdataentity = null;
                int j1 = i + random.nextInt(k);
                int k1 = j + random.nextInt(l);
                int l1 = j1;
                int i2 = k1;

                for (int j2 = 0; j2 < i1; ++j2) {
                    boolean flag = false;

                    for (int k2 = 0; !flag && k2 < 4; ++k2) {
                        BlockPos blockposition = world.func_175672_r(new BlockPos(j1, 0, k1));

                        if (func_180267_a(EntityLiving.SpawnPlacementType.ON_GROUND, world, blockposition)) {
                            EntityLiving entityinsentient;

                            try {
                                entityinsentient = (EntityLiving) biomebase_biomemeta.field_76300_b.getConstructor(new Class[] { World.class}).newInstance(new Object[] { world});
                            } catch (Exception exception) {
                                exception.printStackTrace();
                                ServerInternalException.reportInternalException(exception); // Paper
                                continue;
                            }

                            entityinsentient.func_70012_b((double) ((float) j1 + 0.5F), (double) blockposition.func_177956_o(), (double) ((float) k1 + 0.5F), random.nextFloat() * 360.0F, 0.0F);
                            // CraftBukkit start - Added a reason for spawning this creature, moved entityinsentient.prepare(groupdataentity) up
                            groupdataentity = entityinsentient.func_180482_a(world.func_175649_E(new BlockPos(entityinsentient)), groupdataentity);
                            world.addEntity(entityinsentient, SpawnReason.CHUNK_GEN);
                            // CraftBukkit end
                            flag = true;
                        }

                        j1 += random.nextInt(5) - random.nextInt(5);

                        for (k1 += random.nextInt(5) - random.nextInt(5); j1 < i || j1 >= i + k || k1 < j || k1 >= j + k; k1 = i2 + random.nextInt(5) - random.nextInt(5)) {
                            j1 = l1 + random.nextInt(5) - random.nextInt(5);
                        }
                    }
                }
            }

        }
    }
}
