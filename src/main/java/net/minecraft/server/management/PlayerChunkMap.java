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
import net.minecraft.server.PlayerChunkMap.ChunkCoordComparator;
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

    private static final Predicate<EntityPlayerMP> NOT_SPECTATOR = new Predicate() {
        public boolean a(@Nullable EntityPlayerMP entityplayer) {
            return entityplayer != null && !entityplayer.isSpectator();
        }

        public boolean apply(@Nullable Object object) {
            return this.a((EntityPlayerMP) object);
        }
    };
    private static final Predicate<EntityPlayerMP> CAN_GENERATE_CHUNKS = new Predicate() {
        public boolean a(@Nullable EntityPlayerMP entityplayer) {
            return entityplayer != null && (!entityplayer.isSpectator() || entityplayer.getServerWorld().getGameRules().getBoolean("spectatorsGenerateChunks"));
        }

        public boolean apply(@Nullable Object object) {
            return this.a((EntityPlayerMP) object);
        }
    };
    private final WorldServer world;
    private final List<EntityPlayerMP> players = Lists.newArrayList();
    private final Long2ObjectMap<PlayerChunkMapEntry> entryMap = new Long2ObjectOpenHashMap(4096);
    private final Set<PlayerChunkMapEntry> dirtyEntries = Sets.newHashSet();
    private final List<PlayerChunkMapEntry> pendingSendToPlayers = Lists.newLinkedList();
    private final List<PlayerChunkMapEntry> entriesWithoutChunks = Lists.newLinkedList();
    private final List<PlayerChunkMapEntry> entries = Lists.newArrayList();
    private int playerViewRadius;public int getViewDistance() { return playerViewRadius; } // Paper OBFHELPER
    private long previousTotalWorldTime;
    private boolean sortMissingChunks = true;
    private boolean sortSendToPlayers = true;
    private boolean wasNotEmpty; // CraftBukkit - add field

    public PlayerChunkMap(WorldServer worldserver) {
        this.world = worldserver;
        this.setPlayerViewRadius(worldserver.spigotConfig.viewDistance); // Spigot
    }

    public WorldServer getWorldServer() {
        return this.world;
    }

    public Iterator<Chunk> getChunkIterator() {
        final Iterator iterator = this.entries.iterator();

        return new AbstractIterator() {
            protected Chunk a() {
                while (true) {
                    if (iterator.hasNext()) {
                        PlayerChunkMapEntry playerchunk = (PlayerChunkMapEntry) iterator.next();
                        Chunk chunk = playerchunk.getChunk();

                        if (chunk == null) {
                            continue;
                        }

                        if (!chunk.isLightPopulated() && chunk.isTerrainPopulated()) {
                            return chunk;
                        }

                        if (!chunk.wasTicked()) {
                            return chunk;
                        }

                        if (!playerchunk.hasPlayerMatchingInRange(128.0D, PlayerChunkMap.NOT_SPECTATOR)) {
                            continue;
                        }

                        return chunk;
                    }

                    return (Chunk) this.endOfData();
                }
            }

            protected Object computeNext() {
                return this.a();
            }
        };
    }

    public void tick() {
        long i = this.world.getTotalWorldTime();
        int j;
        PlayerChunkMapEntry playerchunk;

        if (i - this.previousTotalWorldTime > 8000L) {
            try (Timing ignored = world.timings.doChunkMapUpdate.startTiming()) { // Paper
            this.previousTotalWorldTime = i;

            for (j = 0; j < this.entries.size(); ++j) {
                playerchunk = (PlayerChunkMapEntry) this.entries.get(j);
                playerchunk.update();
                playerchunk.updateChunkInhabitedTime();
            }
            } // Paper timing
        }

        if (!this.dirtyEntries.isEmpty()) {
            try (Timing ignored = world.timings.doChunkMapToUpdate.startTiming()) { // Paper
            Iterator iterator = this.dirtyEntries.iterator();

            while (iterator.hasNext()) {
                playerchunk = (PlayerChunkMapEntry) iterator.next();
                playerchunk.update();
            }

            this.dirtyEntries.clear();
            } // Paper timing
        }

        if (this.sortMissingChunks && i % 4L == 0L) {
            this.sortMissingChunks = false;
            try (Timing ignored = world.timings.doChunkMapSortMissing.startTiming()) { // Paper
            Collections.sort(this.entriesWithoutChunks, new Comparator() {
                public int a(PlayerChunkMapEntry playerchunk, PlayerChunkMapEntry playerchunk1) {
                    return ComparisonChain.start().compare(playerchunk.getClosestPlayerDistance(), playerchunk1.getClosestPlayerDistance()).result();
                }

                public int compare(Object object, Object object1) {
                    return this.a((PlayerChunkMapEntry) object, (PlayerChunkMapEntry) object1);
                }
            });
            } // Paper timing
        }

        if (this.sortSendToPlayers && i % 4L == 2L) {
            this.sortSendToPlayers = false;
            try (Timing ignored = world.timings.doChunkMapSortSendToPlayers.startTiming()) { // Paper
            Collections.sort(this.pendingSendToPlayers, new Comparator() {
                public int a(PlayerChunkMapEntry playerchunk, PlayerChunkMapEntry playerchunk1) {
                    return ComparisonChain.start().compare(playerchunk.getClosestPlayerDistance(), playerchunk1.getClosestPlayerDistance()).result();
                }

                public int compare(Object object, Object object1) {
                    return this.a((PlayerChunkMapEntry) object, (PlayerChunkMapEntry) object1);
                }
            });
            } // Paper timing
        }

        if (!this.entriesWithoutChunks.isEmpty()) {
            try (Timing ignored = world.timings.doChunkMapPlayersNeedingChunks.startTiming()) { // Paper
            // Spigot start
            org.spigotmc.SlackActivityAccountant activityAccountant = this.world.getMinecraftServer().slackActivityAccountant;
            activityAccountant.startActivity(0.5);
            int chunkGensAllowed = world.paperConfig.maxChunkGensPerTick; // Paper
            // Spigot end

            Iterator iterator1 = this.entriesWithoutChunks.iterator();

            while (iterator1.hasNext()) {
                PlayerChunkMapEntry playerchunk1 = (PlayerChunkMapEntry) iterator1.next();

                if (playerchunk1.getChunk() == null) {
                    boolean flag = playerchunk1.hasPlayerMatching(PlayerChunkMap.CAN_GENERATE_CHUNKS);
                    // Paper start
                    if (flag && !playerchunk1.chunkExists && chunkGensAllowed-- <= 0) {
                        continue;
                    }
                    // Paper end

                    if (playerchunk1.providePlayerChunk(flag)) {
                        iterator1.remove();
                        if (playerchunk1.sendToPlayers()) {
                            this.pendingSendToPlayers.remove(playerchunk1);
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

        if (!this.pendingSendToPlayers.isEmpty()) {
            j = world.paperConfig.maxChunkSendsPerTick; // Paper
            try (Timing ignored = world.timings.doChunkMapPendingSendToPlayers.startTiming()) { // Paper
            Iterator iterator2 = this.pendingSendToPlayers.iterator();

            while (iterator2.hasNext()) {
                PlayerChunkMapEntry playerchunk2 = (PlayerChunkMapEntry) iterator2.next();

                if (playerchunk2.sendToPlayers()) {
                    iterator2.remove();
                    --j;
                    if (j < 0) {
                        break;
                    }
                }
            }
            } // Paper timing
        }

        if (this.players.isEmpty()) {
            try (Timing ignored = world.timings.doChunkMapUnloadChunks.startTiming()) { // Paper
            WorldProvider worldprovider = this.world.provider;

            if (!worldprovider.canRespawnHere() && !this.world.disableLevelSaving) { // Paper - respect saving disabled setting
                this.world.getChunkProvider().queueUnloadAll();
            }
            } // Paper timing
        }

    }

    public boolean contains(int i, int j) {
        long k = getIndex(i, j);

        return this.entryMap.get(k) != null;
    }

    @Nullable
    public PlayerChunkMapEntry getEntry(int i, int j) {
        return (PlayerChunkMapEntry) this.entryMap.get(getIndex(i, j));
    }

    private PlayerChunkMapEntry getOrCreateEntry(int i, int j) {
        long k = getIndex(i, j);
        PlayerChunkMapEntry playerchunk = (PlayerChunkMapEntry) this.entryMap.get(k);

        if (playerchunk == null) {
            playerchunk = new PlayerChunkMapEntry(this, i, j);
            this.entryMap.put(k, playerchunk);
            this.entries.add(playerchunk);
            if (playerchunk.getChunk() == null) {
                this.entriesWithoutChunks.add(playerchunk);
            }

            if (!playerchunk.sendToPlayers()) {
                this.pendingSendToPlayers.add(playerchunk);
            }
        }

        return playerchunk;
    }

    // CraftBukkit start - add method
    public final boolean isChunkInUse(int x, int z) {
        PlayerChunkMapEntry pi = getEntry(x, z);
        if (pi != null) {
            return (pi.players.size() > 0);
        }
        return false;
    }
    // CraftBukkit end

    public void markBlockForUpdate(BlockPos blockposition) {
        int i = blockposition.getX() >> 4;
        int j = blockposition.getZ() >> 4;
        PlayerChunkMapEntry playerchunk = this.getEntry(i, j);

        if (playerchunk != null) {
            playerchunk.blockChanged(blockposition.getX() & 15, blockposition.getY(), blockposition.getZ() & 15);
        }

    }

    public void addPlayer(EntityPlayerMP entityplayer) {
        int i = (int) entityplayer.posX >> 4;
        int j = (int) entityplayer.posZ >> 4;

        entityplayer.managedPosX = entityplayer.posX;
        entityplayer.managedPosZ = entityplayer.posZ;


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
            this.getOrCreateEntry(pair.x, pair.z).addPlayer(entityplayer);
        }
        // CraftBukkit end

        this.players.add(entityplayer);
        this.markSortPending();
    }

    public void removePlayer(EntityPlayerMP entityplayer) {
        int i = (int) entityplayer.managedPosX >> 4;
        int j = (int) entityplayer.managedPosZ >> 4;

        // Paper start - Player view distance API
        int viewDistance = entityplayer.getViewDistance();
        for (int k = i - viewDistance; k <= i + viewDistance; ++k) {
            for (int l = j - viewDistance; l <= j + viewDistance; ++l) {
                // Paper end
                PlayerChunkMapEntry playerchunk = this.getEntry(k, l);

                if (playerchunk != null) {
                    playerchunk.removePlayer(entityplayer);
                }
            }
        }

        this.players.remove(entityplayer);
        this.markSortPending();
    }

    private boolean overlaps(int i, int j, int k, int l, int i1) {
        int j1 = i - k;
        int k1 = j - l;

        return j1 >= -i1 && j1 <= i1 ? k1 >= -i1 && k1 <= i1 : false;
    }

    public void updateMovingPlayer(EntityPlayerMP entityplayer) {
        int i = (int) entityplayer.posX >> 4;
        int j = (int) entityplayer.posZ >> 4;
        double d0 = entityplayer.managedPosX - entityplayer.posX;
        double d1 = entityplayer.managedPosZ - entityplayer.posZ;
        double d2 = d0 * d0 + d1 * d1;

        if (d2 >= 64.0D) {
            int k = (int) entityplayer.managedPosX >> 4;
            int l = (int) entityplayer.managedPosZ >> 4;
            final int viewDistance = entityplayer.getViewDistance(); // Paper - Player view distance API
            int i1 = Math.max(getViewDistance(), viewDistance); // Paper - Player view distance API

            int j1 = i - k;
            int k1 = j - l;

            List<ChunkPos> chunksToLoad = new LinkedList<ChunkPos>(); // CraftBukkit

            if (j1 != 0 || k1 != 0) {
                for (int l1 = i - i1; l1 <= i + i1; ++l1) {
                    for (int i2 = j - i1; i2 <= j + i1; ++i2) {
                        if (!this.overlaps(l1, i2, k, l, viewDistance)) { // Paper - Player view distance API
                            // this.c(l1, i2).a(entityplayer);
                            chunksToLoad.add(new ChunkPos(l1, i2)); // CraftBukkit
                        }

                        if (!this.overlaps(l1 - j1, i2 - k1, i, j, i1)) {
                            PlayerChunkMapEntry playerchunk = this.getEntry(l1 - j1, i2 - k1);

                            if (playerchunk != null) {
                                playerchunk.removePlayer(entityplayer);
                            }
                        }
                    }
                }

                entityplayer.managedPosX = entityplayer.posX;
                entityplayer.managedPosZ = entityplayer.posZ;
                this.markSortPending();

                // CraftBukkit start - send nearest chunks first
                Collections.sort(chunksToLoad, new ChunkCoordComparator(entityplayer));
                for (ChunkPos pair : chunksToLoad) {
                    this.getOrCreateEntry(pair.x, pair.z).addPlayer(entityplayer);
                }
                // CraftBukkit end
            }
        }
    }

    public boolean isPlayerWatchingChunk(EntityPlayerMP entityplayer, int i, int j) {
        PlayerChunkMapEntry playerchunk = this.getEntry(i, j);

        return playerchunk != null && playerchunk.containsPlayer(entityplayer) && playerchunk.isSentToPlayers();
    }

    public final void setViewDistanceForAll(int viewDistance) { this.setPlayerViewRadius(viewDistance); } // Paper - OBFHELPER
    // Paper start - Separate into two methods
    public void setPlayerViewRadius(int i) {
        i = MathHelper.clamp(i, 3, 32);
        if (i != this.playerViewRadius) {
            int j = i - this.playerViewRadius;
            ArrayList arraylist = Lists.newArrayList(this.players);
            Iterator iterator = arraylist.iterator();

            while (iterator.hasNext()) {
                EntityPlayerMP entityplayer = (EntityPlayerMP) iterator.next();
                this.setViewDistance(entityplayer, i, false); // Paper - Split, don't mark sort pending, we'll handle it after
            }

            this.playerViewRadius = i;
            this.markSortPending();
        }
    }

    public void setViewDistance(EntityPlayerMP entityplayer, int i) {
        this.setViewDistance(entityplayer, i, true); // Mark sort pending by default so we don't have to remember to do so all the time
    }
    
    // Copied from above with minor changes
    public void setViewDistance(EntityPlayerMP entityplayer, int i, boolean markSort) {
        i = MathHelper.clamp(i, 3, 32);
        int oldViewDistance = entityplayer.getViewDistance();
        if (i != oldViewDistance) {
            int j = i - oldViewDistance;
            
            int k = (int) entityplayer.posX >> 4;
            int l = (int) entityplayer.posZ >> 4;
            int i1;
            int j1;

            if (j > 0) {
                for (i1 = k - i; i1 <= k + i; ++i1) {
                    for (j1 = l - i; j1 <= l + i; ++j1) {
                        PlayerChunkMapEntry playerchunk = this.getOrCreateEntry(i1, j1);

                        if (!playerchunk.containsPlayer(entityplayer)) {
                            playerchunk.addPlayer(entityplayer);
                        }
                    }
                }
            } else {
                for (i1 = k - oldViewDistance; i1 <= k + oldViewDistance; ++i1) {
                    for (j1 = l - oldViewDistance; j1 <= l + oldViewDistance; ++j1) {
                        if (!this.overlaps(i1, j1, k, l, i)) {
                            this.getOrCreateEntry(i1, j1).removePlayer(entityplayer);
                        }
                    }
                }
                if (markSort) {
                    this.markSortPending();
                }
            }
        }
    }
    // Paper end

    private void markSortPending() {
        this.sortMissingChunks = true;
        this.sortSendToPlayers = true;
    }

    public static int getFurthestViewableBlock(int i) {
        return i * 16 - 16;
    }

    private static long getIndex(int i, int j) {
        return (long) i + 2147483647L | (long) j + 2147483647L << 32;
    }

    public void entryChanged(PlayerChunkMapEntry playerchunk) {
        org.spigotmc.AsyncCatcher.catchOp("Async Player Chunk Add"); // Paper
        this.dirtyEntries.add(playerchunk);
    }

    public void removeEntry(PlayerChunkMapEntry playerchunk) {
        org.spigotmc.AsyncCatcher.catchOp("Async Player Chunk Remove"); // Paper
        ChunkPos chunkcoordintpair = playerchunk.getPos();
        long i = getIndex(chunkcoordintpair.x, chunkcoordintpair.z);

        playerchunk.updateChunkInhabitedTime();
        this.entryMap.remove(i);
        this.entries.remove(playerchunk);
        this.dirtyEntries.remove(playerchunk);
        this.pendingSendToPlayers.remove(playerchunk);
        this.entriesWithoutChunks.remove(playerchunk);
        Chunk chunk = playerchunk.getChunk();

        if (chunk != null) {
            // Paper start - delay chunk unloads
            if (world.paperConfig.delayChunkUnloadsBy <= 0) {
                this.getWorldServer().getChunkProvider().queueUnload(chunk);
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
            x = (int) entityplayer.posX >> 4;
            z = (int) entityplayer.posZ >> 4;
        }

        public int compare(ChunkPos a, ChunkPos b) {
            if (a.equals(b)) {
                return 0;
            }

            // Subtract current position to set center point
            int ax = a.x - this.x;
            int az = a.z - this.z;
            int bx = b.x - this.x;
            int bz = b.z - this.z;

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
        int playerViewDistance = MathHelper.clamp(distanceIn, 3, 32);

        // This value is the one we actually use to update the chunk map
        // We don't ever want this to be a negative
        int toSet = playerViewDistance;

        if (distanceIn < 0) {
            playerViewDistance = -1;
            toSet = world.getPlayerChunkMap().getViewDistance();
        }

        if (toSet != oldViewDistance) {
            // Order matters
            this.setViewDistance(player, toSet);
            player.setViewDistance(playerViewDistance);
        }
    }
    // Paper end
}
