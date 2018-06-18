package net.minecraft.server.management;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketBlockChange;
import net.minecraft.network.play.server.SPacketChunkData;
import net.minecraft.network.play.server.SPacketMultiBlockChange;
import net.minecraft.network.play.server.SPacketUnloadChunk;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.Chunk;
import org.bukkit.craftbukkit.chunkio.ChunkIOExecutor;

// CraftBukkit Start
import org.bukkit.craftbukkit.chunkio.ChunkIOExecutor;
// CraftBukkit end

public class PlayerChunkMapEntry {

    private static final Logger LOGGER = LogManager.getLogger();
    private final PlayerChunkMap playerChunkMap;
    public final List<EntityPlayerMP> players = Lists.newArrayList(); // CraftBukkit - public
    private final ChunkPos pos;
    private final short[] changedBlocks = new short[64];
    @Nullable
    public Chunk chunk; // CraftBukkit - public
    private int changes;
    private int changedSectionFilter;
    private long lastUpdateInhabitedTime;
    private boolean sentToPlayers;

    // CraftBukkit start - add fields
    boolean chunkExists; // Paper
    private boolean loadInProgress = false;
    private Runnable loadedRunnable = new Runnable() {
        public void run() {
            loadInProgress = false;
            PlayerChunkMapEntry.this.chunk = PlayerChunkMapEntry.this.playerChunkMap.getWorldServer().getChunkProvider().loadChunk(pos.x, pos.z);
            markChunkUsed(); // Paper - delay chunk unloads
        }
    };
    // Paper start - delay chunk unloads
    public final void markChunkUsed() {
        if (chunk != null && chunk.scheduledForUnload != null) {
            chunk.scheduledForUnload = null;
        }
    }
    // Paper end
    // CraftBukkit end

    public PlayerChunkMapEntry(PlayerChunkMap playerchunkmap, int i, int j) {
        this.playerChunkMap = playerchunkmap;
        this.pos = new ChunkPos(i, j);
        // CraftBukkit start
        loadInProgress = true;
        this.chunk = playerchunkmap.getWorldServer().getChunkProvider().getChunkAt(i, j, loadedRunnable, false);
        this.chunkExists = this.chunk != null || ChunkIOExecutor.hasQueuedChunkLoad(playerChunkMap.getWorldServer(), i, j); // Paper
        markChunkUsed(); // Paper - delay chunk unloads
        // CraftBukkit end
    }

    public ChunkPos getPos() {
        return this.pos;
    }

    public void addPlayer(final EntityPlayerMP entityplayer) { // CraftBukkit - added final to argument
        if (this.players.contains(entityplayer)) {
            PlayerChunkMapEntry.LOGGER.debug("Failed to add player. {} already is in chunk {}, {}", entityplayer, Integer.valueOf(this.pos.x), Integer.valueOf(this.pos.z));
        } else {
            if (this.players.isEmpty()) {
                this.lastUpdateInhabitedTime = this.playerChunkMap.getWorldServer().getTotalWorldTime();
            }

            this.players.add(entityplayer);
            // CraftBukkit start - use async chunk io
            // if (this.done) {
            //     this.sendChunk(entityplayer);
            // }
            if (this.sentToPlayers) {
                this.sendToPlayer(entityplayer);
            }
            // CraftBukkit end

        }
    }

    public void removePlayer(EntityPlayerMP entityplayer) {
        if (this.players.contains(entityplayer)) {
            // CraftBukkit start - If we haven't loaded yet don't load the chunk just so we can clean it up
            if (!this.sentToPlayers) {
                this.players.remove(entityplayer);

                if (this.players.isEmpty()) {
                    ChunkIOExecutor.dropQueuedChunkLoad(this.playerChunkMap.getWorldServer(), this.pos.x, this.pos.z, this.loadedRunnable);
                    this.playerChunkMap.removeEntry(this);
                }

                return;
            }
            // CraftBukkit end
            if (this.sentToPlayers) {
                entityplayer.connection.sendPacket(new SPacketUnloadChunk(this.pos.x, this.pos.z));
            }

            this.players.remove(entityplayer);
            if (this.players.isEmpty()) {
                this.playerChunkMap.removeEntry(this);
            }

        }
    }

    public boolean providePlayerChunk(boolean flag) {
        if (this.chunk != null) {
            return true;
        } else {
            /* CraftBukkit start
            if (flag) {
                this.chunk = this.playerChunkMap.getWorld().getChunkProviderServer().getChunkAt(this.location.x, this.location.z);
            } else {
                this.chunk = this.playerChunkMap.getWorld().getChunkProviderServer().getOrLoadChunkAt(this.location.x, this.location.z);
            }
            */
            if (!loadInProgress) {
                loadInProgress = true;
                this.chunk = playerChunkMap.getWorldServer().getChunkProvider().getChunkAt(this.pos.x, this.pos.z, loadedRunnable, flag);
                markChunkUsed(); // Paper - delay chunk unloads
            }
            // CraftBukkit end

            return this.chunk != null;
        }
    }

    public boolean sendToPlayers() {
        if (this.sentToPlayers) {
            return true;
        } else if (this.chunk == null) {
            return false;
        } else if (!this.chunk.isPopulated()) {
            return false;
        } else if (!this.chunk.world.chunkPacketBlockController.onChunkPacketCreate(this.chunk, '\uffff', false)) { // Paper - Anti-Xray - Load nearby chunks if necessary
            return false; // Paper - Anti-Xray - Wait and try again later
        } else {
            this.changes = 0;
            this.changedSectionFilter = 0;
            this.sentToPlayers = true;
            SPacketChunkData packetplayoutmapchunk = new SPacketChunkData(this.chunk, '\uffff');
            Iterator iterator = this.players.iterator();

            while (iterator.hasNext()) {
                EntityPlayerMP entityplayer = (EntityPlayerMP) iterator.next();

                entityplayer.connection.sendPacket(packetplayoutmapchunk);
                this.playerChunkMap.getWorldServer().getEntityTracker().sendLeashedEntitiesInChunk(entityplayer, this.chunk);
            }

            return true;
        }
    }

    public void sendToPlayer(EntityPlayerMP entityplayer) {
        if (this.sentToPlayers) {
            this.chunk.world.chunkPacketBlockController.onChunkPacketCreate(this.chunk, '\uffff', true); // Paper - Anti-Xray - Load nearby chunks if necessary
            entityplayer.connection.sendPacket(new SPacketChunkData(this.chunk, '\uffff'));
            this.playerChunkMap.getWorldServer().getEntityTracker().sendLeashedEntitiesInChunk(entityplayer, this.chunk);
        }
    }

    public void updateChunkInhabitedTime() {
        long i = this.playerChunkMap.getWorldServer().getTotalWorldTime();

        if (this.chunk != null) {
            this.chunk.setInhabitedTime(this.chunk.getInhabitedTime() + i - this.lastUpdateInhabitedTime);
        }

        this.lastUpdateInhabitedTime = i;
    }

    public void blockChanged(int i, int j, int k) {
        if (this.sentToPlayers) {
            if (this.changes == 0) {
                this.playerChunkMap.entryChanged(this);
            }

            this.changedSectionFilter |= 1 << (j >> 4);
            if (this.changes < 64) {
                short short0 = (short) (i << 12 | k << 8 | j);

                for (int l = 0; l < this.changes; ++l) {
                    if (this.changedBlocks[l] == short0) {
                        return;
                    }
                }

                this.changedBlocks[this.changes++] = short0;
            }

        }
    }

    public void sendPacket(Packet<?> packet) {
        if (this.sentToPlayers) {
            for (int i = 0; i < this.players.size(); ++i) {
                ((EntityPlayerMP) this.players.get(i)).connection.sendPacket(packet);
            }

        }
    }

    public void update() {
        if (this.sentToPlayers && this.chunk != null) {
            if (this.changes != 0) {
                int i;
                int j;
                int k;

                if (this.changes == 1) {
                    i = (this.changedBlocks[0] >> 12 & 15) + this.pos.x * 16;
                    j = this.changedBlocks[0] & 255;
                    k = (this.changedBlocks[0] >> 8 & 15) + this.pos.z * 16;
                    BlockPos blockposition = new BlockPos(i, j, k);

                    this.sendPacket((Packet) (new SPacketBlockChange(this.playerChunkMap.getWorldServer(), blockposition)));
                    if (this.playerChunkMap.getWorldServer().getBlockState(blockposition).getBlock().hasTileEntity()) {
                        this.sendBlockEntity(this.playerChunkMap.getWorldServer().getTileEntity(blockposition));
                    }
                } else if (this.changes == 64) {
                    // Paper - Anti-Xray - Loading chunks here could cause a ConcurrentModificationException #1104
                    //this.chunk.world.chunkPacketBlockController.onChunkPacketCreate(this.chunk, this.h, true); // Paper - Anti-Xray - Load nearby chunks if necessary
                    this.sendPacket((Packet) (new SPacketChunkData(this.chunk, this.changedSectionFilter)));
                } else {
                    this.sendPacket((Packet) (new SPacketMultiBlockChange(this.changes, this.changedBlocks, this.chunk)));

                    for (i = 0; i < this.changes; ++i) {
                        j = (this.changedBlocks[i] >> 12 & 15) + this.pos.x * 16;
                        k = this.changedBlocks[i] & 255;
                        int l = (this.changedBlocks[i] >> 8 & 15) + this.pos.z * 16;
                        BlockPos blockposition1 = new BlockPos(j, k, l);

                        if (this.playerChunkMap.getWorldServer().getBlockState(blockposition1).getBlock().hasTileEntity()) {
                            this.sendBlockEntity(this.playerChunkMap.getWorldServer().getTileEntity(blockposition1));
                        }
                    }
                }

                this.changes = 0;
                this.changedSectionFilter = 0;
            }
        }
    }

    private void sendBlockEntity(@Nullable TileEntity tileentity) {
        if (tileentity != null) {
            SPacketUpdateTileEntity packetplayouttileentitydata = tileentity.getUpdatePacket();

            if (packetplayouttileentitydata != null) {
                this.sendPacket((Packet) packetplayouttileentitydata);
            }
        }

    }

    public boolean containsPlayer(EntityPlayerMP entityplayer) {
        return this.players.contains(entityplayer);
    }

    public boolean hasPlayerMatching(Predicate<EntityPlayerMP> predicate) {
        return Iterables.tryFind(this.players, predicate).isPresent();
    }

    public boolean hasPlayerMatchingInRange(double d0, Predicate<EntityPlayerMP> predicate) {
        int i = 0;

        for (int j = this.players.size(); i < j; ++i) {
            EntityPlayerMP entityplayer = (EntityPlayerMP) this.players.get(i);

            if (predicate.apply(entityplayer) && this.pos.getDistanceSq(entityplayer) < d0 * d0) {
                return true;
            }
        }

        return false;
    }

    public boolean isSentToPlayers() {
        return this.sentToPlayers;
    }

    @Nullable
    public Chunk getChunk() {
        return this.chunk;
    }

    public double getClosestPlayerDistance() {
        double d0 = Double.MAX_VALUE;
        Iterator iterator = this.players.iterator();

        while (iterator.hasNext()) {
            EntityPlayerMP entityplayer = (EntityPlayerMP) iterator.next();
            double d1 = this.pos.getDistanceSq(entityplayer);

            if (d1 < d0) {
                d0 = d1;
            }
        }

        return d0;
    }
}
