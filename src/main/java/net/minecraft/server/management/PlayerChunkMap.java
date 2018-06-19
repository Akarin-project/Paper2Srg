package net.minecraft.server.management;

import co.aikar.timings.Timing;
import com.google.common.base.Predicate;
import com.google.common.collect.AbstractIterator;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.annotation.Nullable;

import java.util.LinkedList;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;

// CraftBukkit start
import java.util.LinkedList;
// CraftBukkit end

public class PlayerChunkMap {

    private static final Predicate<EntityPlayerMP> field_187308_a = new Predicate() {
        public boolean a(@Nullable EntityPlayerMP entityplayer) {
            return entityplayer != null && !entityplayer.func_175149_v();
        }

        @Override
        public boolean apply(@Nullable Object object) {
            return this.a((EntityPlayerMP) object);
        }
    };
    private static final Predicate<EntityPlayerMP> field_187309_b = new Predicate() {
        public boolean a(@Nullable EntityPlayerMP entityplayer) {
            return entityplayer != null && (!entityplayer.func_175149_v() || entityplayer.func_71121_q().func_82736_K().func_82766_b("spectatorsGenerateChunks"));
        }

        @Override
        public boolean apply(@Nullable Object object) {
            return this.a((EntityPlayerMP) object);
        }
    };
    private final WorldServer field_72701_a;
    private final List<EntityPlayerMP> field_72699_b = Lists.newArrayList();
    private final Long2ObjectMap<PlayerChunkMapEntry> field_72700_c = new Long2ObjectOpenHashMap(4096);
    private final Set<PlayerChunkMapEntry> field_72697_d = Sets.newHashSet();
    private final List<PlayerChunkMapEntry> field_187310_g = Lists.newLinkedList();
    private final List<PlayerChunkMapEntry> field_187311_h = Lists.newLinkedList();
    private final List<PlayerChunkMapEntry> field_111193_e = Lists.newArrayList();
    private int field_72698_e;public int getViewDistance() { return field_72698_e; } // Paper OBFHELPER
    private long field_111192_g;
    private boolean field_187312_l = true;
    private boolean field_187313_m = true;
    private boolean wasNotEmpty; // CraftBukkit - add field

    public PlayerChunkMap(WorldServer worldserver) {
        this.field_72701_a = worldserver;
        this.func_152622_a(worldserver.spigotConfig.viewDistance); // Spigot
    }

    public WorldServer func_72688_a() {
        return this.field_72701_a;
    }

    public Iterator<Chunk> func_187300_b() {
        final Iterator iterator = this.field_111193_e.iterator();

        return new AbstractIterator() {
            protected Chunk a() {
                while (true) {
                    if (iterator.hasNext()) {
                        PlayerChunkMapEntry playerchunk = (PlayerChunkMapEntry) iterator.next();
                        Chunk chunk = playerchunk.func_187266_f();

                        if (chunk == null) {
                            continue;
                        }

                        if (!chunk.func_177423_u() && chunk.func_177419_t()) {
                            return chunk;
                        }

                        if (!chunk.func_186035_j()) {
                            return chunk;
                        }

                        if (!playerchunk.func_187271_a(128.0D, PlayerChunkMap.field_187308_a)) {
                            continue;
                        }

                        return chunk;
                    }

                    return (Chunk) this.endOfData();
                }
            }

            @Override
            protected Object computeNext() {
                return this.a();
            }
        };
    }

    public void func_72693_b() {
        long i = this.field_72701_a.func_82737_E();
        int j;
        PlayerChunkMapEntry playerchunk;

        if (i - this.field_111192_g > 8000L) {
            try (Timing ignored = field_72701_a.timings.doChunkMapUpdate.startTiming()) { // Paper
            this.field_111192_g = i;

            for (j = 0; j < this.field_111193_e.size(); ++j) {
                playerchunk = this.field_111193_e.get(j);
                playerchunk.func_187280_d();
                playerchunk.func_187279_c();
            }
            } // Paper timing
        }

        if (!this.field_72697_d.isEmpty()) {
            try (Timing ignored = field_72701_a.timings.doChunkMapToUpdate.startTiming()) { // Paper
            Iterator iterator = this.field_72697_d.iterator();

            while (iterator.hasNext()) {
                playerchunk = (PlayerChunkMapEntry) iterator.next();
                playerchunk.func_187280_d();
            }

            this.field_72697_d.clear();
            } // Paper timing
        }

        if (this.field_187312_l && i % 4L == 0L) {
            this.field_187312_l = false;
            try (Timing ignored = field_72701_a.timings.doChunkMapSortMissing.startTiming()) { // Paper
            Collections.sort(this.field_187311_h, new Comparator() {
                public int a(PlayerChunkMapEntry playerchunk, PlayerChunkMapEntry playerchunk1) {
                    return ComparisonChain.start().compare(playerchunk.func_187270_g(), playerchunk1.func_187270_g()).result();
                }

                @Override
                public int compare(Object object, Object object1) {
                    return this.a((PlayerChunkMapEntry) object, (PlayerChunkMapEntry) object1);
                }
            });
            } // Paper timing
        }

        if (this.field_187313_m && i % 4L == 2L) {
            this.field_187313_m = false;
            try (Timing ignored = field_72701_a.timings.doChunkMapSortSendToPlayers.startTiming()) { // Paper
            Collections.sort(this.field_187310_g, new Comparator() {
                public int a(PlayerChunkMapEntry playerchunk, PlayerChunkMapEntry playerchunk1) {
                    return ComparisonChain.start().compare(playerchunk.func_187270_g(), playerchunk1.func_187270_g()).result();
                }

                @Override
                public int compare(Object object, Object object1) {
                    return this.a((PlayerChunkMapEntry) object, (PlayerChunkMapEntry) object1);
                }
            });
            } // Paper timing
        }

        if (!this.field_187311_h.isEmpty()) {
            try (Timing ignored = field_72701_a.timings.doChunkMapPlayersNeedingChunks.startTiming()) { // Paper
            // Spigot start
            org.spigotmc.SlackActivityAccountant activityAccountant = this.field_72701_a.func_73046_m().slackActivityAccountant;
            activityAccountant.startActivity(0.5);
            int chunkGensAllowed = field_72701_a.paperConfig.maxChunkGensPerTick; // Paper
            // Spigot end

            Iterator iterator1 = this.field_187311_h.iterator();

            while (iterator1.hasNext()) {
                PlayerChunkMapEntry playerchunk1 = (PlayerChunkMapEntry) iterator1.next();

                if (playerchunk1.func_187266_f() == null) {
                    boolean flag = playerchunk1.func_187269_a(PlayerChunkMap.field_187309_b);
                    // Paper start
                    if (flag && !playerchunk1.chunkExists && chunkGensAllowed-- <= 0) {
                        continue;
                    }
                    // Paper end

                    if (playerchunk1.func_187268_a(flag)) {
                        iterator1.remove();
                        if (playerchunk1.func_187272_b()) {
                            this.field_187310_g.remove(playerchunk1);
                        }

                        if (activityAccountant.activityTimeIsExhausted()) { // Spigot
                            break;
                        }
                    }
                // CraftBukkit start - SPIGOT-2891: remove once chunk has been provided
                } else {
                    iterator1.remove();
                }
                // CraftBukkit end
            }

            activityAccountant.endActivity(); // Spigot
            } // Paper timing
        }

        if (!this.field_187310_g.isEmpty()) {
            j = field_72701_a.paperConfig.maxChunkSendsPerTick; // Paper
            try (Timing ignored = field_72701_a.timings.doChunkMapPendingSendToPlayers.startTiming()) { // Paper
            Iterator iterator2 = this.field_187310_g.iterator();

            while (iterator2.hasNext()) {
                PlayerChunkMapEntry playerchunk2 = (PlayerChunkMapEntry) iterator2.next();

                if (playerchunk2.func_187272_b()) {
                    iterator2.remove();
                    --j;
                    if (j < 0) {
                        break;
                    }
                }
            }
            } // Paper timing
        }

        if (this.field_72699_b.isEmpty()) {
            try (Timing ignored = field_72701_a.timings.doChunkMapUnloadChunks.startTiming()) { // Paper
            WorldProvider worldprovider = this.field_72701_a.field_73011_w;

            if (!worldprovider.func_76567_e() && !this.field_72701_a.field_73058_d) { // Paper - respect saving disabled setting
                this.field_72701_a.func_72863_F().func_73240_a();
            }
            } // Paper timing
        }

    }

    public boolean func_152621_a(int i, int j) {
        long k = func_187307_d(i, j);

        return this.field_72700_c.get(k) != null;
    }

    @Nullable
    public PlayerChunkMapEntry func_187301_b(int i, int j) {
        return this.field_72700_c.get(func_187307_d(i, j));
    }

    private PlayerChunkMapEntry func_187302_c(int i, int j) {
        long k = func_187307_d(i, j);
        PlayerChunkMapEntry playerchunk = this.field_72700_c.get(k);

        if (playerchunk == null) {
            playerchunk = new PlayerChunkMapEntry(this, i, j);
            this.field_72700_c.put(k, playerchunk);
            this.field_111193_e.add(playerchunk);
            if (playerchunk.func_187266_f() == null) {
                this.field_187311_h.add(playerchunk);
            }

            if (!playerchunk.func_187272_b()) {
                this.field_187310_g.add(playerchunk);
            }
        }

        return playerchunk;
    }

    // CraftBukkit start - add method
    public final boolean isChunkInUse(int x, int z) {
        PlayerChunkMapEntry pi = func_187301_b(x, z);
        if (pi != null) {
            return (pi.field_187283_c.size() > 0);
        }
        return false;
    }
    // CraftBukkit end

    public void func_180244_a(BlockPos blockposition) {
        int i = blockposition.func_177958_n() >> 4;
        int j = blockposition.func_177952_p() >> 4;
        PlayerChunkMapEntry playerchunk = this.func_187301_b(i, j);

        if (playerchunk != null) {
            playerchunk.func_187265_a(blockposition.func_177958_n() & 15, blockposition.func_177956_o(), blockposition.func_177952_p() & 15);
        }

    }

    public void func_72683_a(EntityPlayerMP entityplayer) {
        int i = (int) entityplayer.field_70165_t >> 4;
        int j = (int) entityplayer.field_70161_v >> 4;

        entityplayer.field_71131_d = entityplayer.field_70165_t;
        entityplayer.field_71132_e = entityplayer.field_70161_v;


        // CraftBukkit start - Load nearby chunks first
        List<ChunkPos> chunkList = new LinkedList<ChunkPos>();

        // Paper start - Player view distance API
        int viewDistance = entityplayer.getViewDistance();
        for (int k = i - viewDistance; k <= i + viewDistance; ++k) {
            for (int l = j - viewDistance; l <= j + viewDistance; ++l) {
                // Paper end
                chunkList.add(new ChunkPos(k, l));
            }
        }

        Collections.sort(chunkList, new ChunkCoordComparator(entityplayer));
        for (ChunkPos pair : chunkList) {
            this.func_187302_c(pair.field_77276_a, pair.field_77275_b).func_187276_a(entityplayer);
        }
        // CraftBukkit end

        this.field_72699_b.add(entityplayer);
        this.func_187306_e();
    }

    public void func_72695_c(EntityPlayerMP entityplayer) {
        int i = (int) entityplayer.field_71131_d >> 4;
        int j = (int) entityplayer.field_71132_e >> 4;

        // Paper start - Player view distance API
        int viewDistance = entityplayer.getViewDistance();
        for (int k = i - viewDistance; k <= i + viewDistance; ++k) {
            for (int l = j - viewDistance; l <= j + viewDistance; ++l) {
                // Paper end
                PlayerChunkMapEntry playerchunk = this.func_187301_b(k, l);

                if (playerchunk != null) {
                    playerchunk.func_187277_b(entityplayer);
                }
            }
        }

        this.field_72699_b.remove(entityplayer);
        this.func_187306_e();
    }

    private boolean func_72684_a(int i, int j, int k, int l, int i1) {
        int j1 = i - k;
        int k1 = j - l;

        return j1 >= -i1 && j1 <= i1 ? k1 >= -i1 && k1 <= i1 : false;
    }

    public void func_72685_d(EntityPlayerMP entityplayer) {
        int i = (int) entityplayer.field_70165_t >> 4;
        int j = (int) entityplayer.field_70161_v >> 4;
        double d0 = entityplayer.field_71131_d - entityplayer.field_70165_t;
        double d1 = entityplayer.field_71132_e - entityplayer.field_70161_v;
        double d2 = d0 * d0 + d1 * d1;

        if (d2 >= 64.0D) {
            int k = (int) entityplayer.field_71131_d >> 4;
            int l = (int) entityplayer.field_71132_e >> 4;
            final int viewDistance = entityplayer.getViewDistance(); // Paper - Player view distance API
            int i1 = Math.max(getViewDistance(), viewDistance); // Paper - Player view distance API

            int j1 = i - k;
            int k1 = j - l;

            List<ChunkPos> chunksToLoad = new LinkedList<ChunkPos>(); // CraftBukkit

            if (j1 != 0 || k1 != 0) {
                for (int l1 = i - i1; l1 <= i + i1; ++l1) {
                    for (int i2 = j - i1; i2 <= j + i1; ++i2) {
                        if (!this.func_72684_a(l1, i2, k, l, viewDistance)) { // Paper - Player view distance API
                            // this.c(l1, i2).a(entityplayer);
                            chunksToLoad.add(new ChunkPos(l1, i2)); // CraftBukkit
                        }

                        if (!this.func_72684_a(l1 - j1, i2 - k1, i, j, i1)) {
                            PlayerChunkMapEntry playerchunk = this.func_187301_b(l1 - j1, i2 - k1);

                            if (playerchunk != null) {
                                playerchunk.func_187277_b(entityplayer);
                            }
                        }
                    }
                }

                entityplayer.field_71131_d = entityplayer.field_70165_t;
                entityplayer.field_71132_e = entityplayer.field_70161_v;
                this.func_187306_e();

                // CraftBukkit start - send nearest chunks first
                Collections.sort(chunksToLoad, new ChunkCoordComparator(entityplayer));
                for (ChunkPos pair : chunksToLoad) {
                    this.func_187302_c(pair.field_77276_a, pair.field_77275_b).func_187276_a(entityplayer);
                }
                // CraftBukkit end
            }
        }
    }

    public boolean func_72694_a(EntityPlayerMP entityplayer, int i, int j) {
        PlayerChunkMapEntry playerchunk = this.func_187301_b(i, j);

        return playerchunk != null && playerchunk.func_187275_d(entityplayer) && playerchunk.func_187274_e();
    }

    public final void setViewDistanceForAll(int viewDistance) { this.func_152622_a(viewDistance); } // Paper - OBFHELPER
    // Paper start - Separate into two methods
    public void func_152622_a(int i) {
        i = MathHelper.func_76125_a(i, 3, 32);
        if (i != this.field_72698_e) {
            int j = i - this.field_72698_e;
            ArrayList arraylist = Lists.newArrayList(this.field_72699_b);
            Iterator iterator = arraylist.iterator();

            while (iterator.hasNext()) {
                EntityPlayerMP entityplayer = (EntityPlayerMP) iterator.next();
                this.setViewDistance(entityplayer, i, false); // Paper - Split, don't mark sort pending, we'll handle it after
            }

            this.field_72698_e = i;
            this.func_187306_e();
        }
    }

    public void setViewDistance(EntityPlayerMP entityplayer, int i) {
        this.setViewDistance(entityplayer, i, true); // Mark sort pending by default so we don't have to remember to do so all the time
    }
    
    // Copied from above with minor changes
    public void setViewDistance(EntityPlayerMP entityplayer, int i, boolean markSort) {
        i = MathHelper.func_76125_a(i, 3, 32);
        int oldViewDistance = entityplayer.getViewDistance();
        if (i != oldViewDistance) {
            int j = i - oldViewDistance;
            
            int k = (int) entityplayer.field_70165_t >> 4;
            int l = (int) entityplayer.field_70161_v >> 4;
            int i1;
            int j1;

            if (j > 0) {
                for (i1 = k - i; i1 <= k + i; ++i1) {
                    for (j1 = l - i; j1 <= l + i; ++j1) {
                        PlayerChunkMapEntry playerchunk = this.func_187302_c(i1, j1);

                        if (!playerchunk.func_187275_d(entityplayer)) {
                            playerchunk.func_187276_a(entityplayer);
                        }
                    }
                }
            } else {
                for (i1 = k - oldViewDistance; i1 <= k + oldViewDistance; ++i1) {
                    for (j1 = l - oldViewDistance; j1 <= l + oldViewDistance; ++j1) {
                        if (!this.func_72684_a(i1, j1, k, l, i)) {
                            this.func_187302_c(i1, j1).func_187277_b(entityplayer);
                        }
                    }
                }
                if (markSort) {
                    this.func_187306_e();
                }
            }
        }
    }
    // Paper end

    private void func_187306_e() {
        this.field_187312_l = true;
        this.field_187313_m = true;
    }

    public static int func_72686_a(int i) {
        return i * 16 - 16;
    }

    private static long func_187307_d(int i, int j) {
        return i + 2147483647L | j + 2147483647L << 32;
    }

    public void func_187304_a(PlayerChunkMapEntry playerchunk) {
        org.spigotmc.AsyncCatcher.catchOp("Async Player Chunk Add"); // Paper
        this.field_72697_d.add(playerchunk);
    }

    public void func_187305_b(PlayerChunkMapEntry playerchunk) {
        org.spigotmc.AsyncCatcher.catchOp("Async Player Chunk Remove"); // Paper
        ChunkPos chunkcoordintpair = playerchunk.func_187264_a();
        long i = func_187307_d(chunkcoordintpair.field_77276_a, chunkcoordintpair.field_77275_b);

        playerchunk.func_187279_c();
        this.field_72700_c.remove(i);
        this.field_111193_e.remove(playerchunk);
        this.field_72697_d.remove(playerchunk);
        this.field_187310_g.remove(playerchunk);
        this.field_187311_h.remove(playerchunk);
        Chunk chunk = playerchunk.func_187266_f();

        if (chunk != null) {
            // Paper start - delay chunk unloads
            if (field_72701_a.paperConfig.delayChunkUnloadsBy <= 0) {
                this.func_72688_a().func_72863_F().func_189549_a(chunk);
            } else {
                chunk.scheduledForUnload = System.currentTimeMillis();
            }
            // Paper end
        }

    }

    // CraftBukkit start - Sorter to load nearby chunks first
    private static class ChunkCoordComparator implements java.util.Comparator<ChunkPos> {
        private int x;
        private int z;

        public ChunkCoordComparator (EntityPlayerMP entityplayer) {
            x = (int) entityplayer.field_70165_t >> 4;
            z = (int) entityplayer.field_70161_v >> 4;
        }

        @Override
        public int compare(ChunkPos a, ChunkPos b) {
            if (a.equals(b)) {
                return 0;
            }

            // Subtract current position to set center point
            int ax = a.field_77276_a - this.x;
            int az = a.field_77275_b - this.z;
            int bx = b.field_77276_a - this.x;
            int bz = b.field_77275_b - this.z;

            int result = ((ax - bx) * (ax + bx)) + ((az - bz) * (az + bz));
            if (result != 0) {
                return result;
            }

            if (ax < 0) {
                if (bx < 0) {
                    return bz - az;
                } else {
                    return -1;
                }
            } else {
                if (bx < 0) {
                    return 1;
                } else {
                    return az - bz;
                }
            }
        }
    }
    // CraftBukkit end

    // Paper start - Player view distance API
    public void updateViewDistance(EntityPlayerMP player, int distanceIn) {
        final int oldViewDistance = player.getViewDistance();

        // This represents the view distance that we will set on the player
        // It can exist as a negative value
        int playerViewDistance = MathHelper.func_76125_a(distanceIn, 3, 32);

        // This value is the one we actually use to update the chunk map
        // We don't ever want this to be a negative
        int toSet = playerViewDistance;

        if (distanceIn < 0) {
            playerViewDistance = -1;
            toSet = field_72701_a.func_184164_w().getViewDistance();
        }

        if (toSet != oldViewDistance) {
            // Order matters
            this.setViewDistance(player, toSet);
            player.setViewDistance(playerViewDistance);
        }
    }
    // Paper end
}
