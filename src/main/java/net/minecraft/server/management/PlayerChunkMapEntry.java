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

    private static final Logger field_187281_a = LogManager.getLogger();
    private final PlayerChunkMap field_187282_b;
    public final List<EntityPlayerMP> field_187283_c = Lists.newArrayList(); // CraftBukkit - public
    private final ChunkPos field_187284_d;
    private final short[] field_187285_e = new short[64];
    @Nullable
    public Chunk field_187286_f; // CraftBukkit - public
    private int field_187287_g;
    private int field_187288_h;
    private long field_187289_i;
    private boolean field_187290_j;

    // CraftBukkit start - add fields
    boolean chunkExists; // Paper
    private boolean loadInProgress = false;
    private Runnable loadedRunnable = new Runnable() {
        public void run() {
            loadInProgress = false;
            PlayerChunkMapEntry.this.field_187286_f = PlayerChunkMapEntry.this.field_187282_b.func_72688_a().func_72863_F().func_186028_c(field_187284_d.field_77276_a, field_187284_d.field_77275_b);
            markChunkUsed(); // Paper - delay chunk unloads
        }
    };
    // Paper start - delay chunk unloads
    public final void markChunkUsed() {
        if (field_187286_f != null && field_187286_f.scheduledForUnload != null) {
            field_187286_f.scheduledForUnload = null;
        }
    }
    // Paper end
    // CraftBukkit end

    public PlayerChunkMapEntry(PlayerChunkMap playerchunkmap, int i, int j) {
        this.field_187282_b = playerchunkmap;
        this.field_187284_d = new ChunkPos(i, j);
        // CraftBukkit start
        loadInProgress = true;
        this.field_187286_f = playerchunkmap.func_72688_a().func_72863_F().getChunkAt(i, j, loadedRunnable, false);
        this.chunkExists = this.field_187286_f != null || ChunkIOExecutor.hasQueuedChunkLoad(field_187282_b.func_72688_a(), i, j); // Paper
        markChunkUsed(); // Paper - delay chunk unloads
        // CraftBukkit end
    }

    public ChunkPos func_187264_a() {
        return this.field_187284_d;
    }

    public void func_187276_a(final EntityPlayerMP entityplayer) { // CraftBukkit - added final to argument
        if (this.field_187283_c.contains(entityplayer)) {
            PlayerChunkMapEntry.field_187281_a.debug("Failed to add player. {} already is in chunk {}, {}", entityplayer, Integer.valueOf(this.field_187284_d.field_77276_a), Integer.valueOf(this.field_187284_d.field_77275_b));
        } else {
            if (this.field_187283_c.isEmpty()) {
                this.field_187289_i = this.field_187282_b.func_72688_a().func_82737_E();
            }

            this.field_187283_c.add(entityplayer);
            // CraftBukkit start - use async chunk io
            // if (this.done) {
            //     this.sendChunk(entityplayer);
            // }
            if (this.field_187290_j) {
                this.func_187278_c(entityplayer);
            }
            // CraftBukkit end

        }
    }

    public void func_187277_b(EntityPlayerMP entityplayer) {
        if (this.field_187283_c.contains(entityplayer)) {
            // CraftBukkit start - If we haven't loaded yet don't load the chunk just so we can clean it up
            if (!this.field_187290_j) {
                this.field_187283_c.remove(entityplayer);

                if (this.field_187283_c.isEmpty()) {
                    ChunkIOExecutor.dropQueuedChunkLoad(this.field_187282_b.func_72688_a(), this.field_187284_d.field_77276_a, this.field_187284_d.field_77275_b, this.loadedRunnable);
                    this.field_187282_b.func_187305_b(this);
                }

                return;
            }
            // CraftBukkit end
            if (this.field_187290_j) {
                entityplayer.field_71135_a.func_147359_a(new SPacketUnloadChunk(this.field_187284_d.field_77276_a, this.field_187284_d.field_77275_b));
            }

            this.field_187283_c.remove(entityplayer);
            if (this.field_187283_c.isEmpty()) {
                this.field_187282_b.func_187305_b(this);
            }

        }
    }

    public boolean func_187268_a(boolean flag) {
        if (this.field_187286_f != null) {
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
                this.field_187286_f = field_187282_b.func_72688_a().func_72863_F().getChunkAt(this.field_187284_d.field_77276_a, this.field_187284_d.field_77275_b, loadedRunnable, flag);
                markChunkUsed(); // Paper - delay chunk unloads
            }
            // CraftBukkit end

            return this.field_187286_f != null;
        }
    }

    public boolean func_187272_b() {
        if (this.field_187290_j) {
            return true;
        } else if (this.field_187286_f == null) {
            return false;
        } else if (!this.field_187286_f.func_150802_k()) {
            return false;
        } else if (!this.field_187286_f.field_76637_e.chunkPacketBlockController.onChunkPacketCreate(this.field_187286_f, '\uffff', false)) { // Paper - Anti-Xray - Load nearby chunks if necessary
            return false; // Paper - Anti-Xray - Wait and try again later
        } else {
            this.field_187287_g = 0;
            this.field_187288_h = 0;
            this.field_187290_j = true;
            SPacketChunkData packetplayoutmapchunk = new SPacketChunkData(this.field_187286_f, '\uffff');
            Iterator iterator = this.field_187283_c.iterator();

            while (iterator.hasNext()) {
                EntityPlayerMP entityplayer = (EntityPlayerMP) iterator.next();

                entityplayer.field_71135_a.func_147359_a(packetplayoutmapchunk);
                this.field_187282_b.func_72688_a().func_73039_n().func_85172_a(entityplayer, this.field_187286_f);
            }

            return true;
        }
    }

    public void func_187278_c(EntityPlayerMP entityplayer) {
        if (this.field_187290_j) {
            this.field_187286_f.field_76637_e.chunkPacketBlockController.onChunkPacketCreate(this.field_187286_f, '\uffff', true); // Paper - Anti-Xray - Load nearby chunks if necessary
            entityplayer.field_71135_a.func_147359_a(new SPacketChunkData(this.field_187286_f, '\uffff'));
            this.field_187282_b.func_72688_a().func_73039_n().func_85172_a(entityplayer, this.field_187286_f);
        }
    }

    public void func_187279_c() {
        long i = this.field_187282_b.func_72688_a().func_82737_E();

        if (this.field_187286_f != null) {
            this.field_187286_f.func_177415_c(this.field_187286_f.func_177416_w() + i - this.field_187289_i);
        }

        this.field_187289_i = i;
    }

    public void func_187265_a(int i, int j, int k) {
        if (this.field_187290_j) {
            if (this.field_187287_g == 0) {
                this.field_187282_b.func_187304_a(this);
            }

            this.field_187288_h |= 1 << (j >> 4);
            if (this.field_187287_g < 64) {
                short short0 = (short) (i << 12 | k << 8 | j);

                for (int l = 0; l < this.field_187287_g; ++l) {
                    if (this.field_187285_e[l] == short0) {
                        return;
                    }
                }

                this.field_187285_e[this.field_187287_g++] = short0;
            }

        }
    }

    public void func_187267_a(Packet<?> packet) {
        if (this.field_187290_j) {
            for (int i = 0; i < this.field_187283_c.size(); ++i) {
                ((EntityPlayerMP) this.field_187283_c.get(i)).field_71135_a.func_147359_a(packet);
            }

        }
    }

    public void func_187280_d() {
        if (this.field_187290_j && this.field_187286_f != null) {
            if (this.field_187287_g != 0) {
                int i;
                int j;
                int k;

                if (this.field_187287_g == 1) {
                    i = (this.field_187285_e[0] >> 12 & 15) + this.field_187284_d.field_77276_a * 16;
                    j = this.field_187285_e[0] & 255;
                    k = (this.field_187285_e[0] >> 8 & 15) + this.field_187284_d.field_77275_b * 16;
                    BlockPos blockposition = new BlockPos(i, j, k);

                    this.func_187267_a((Packet) (new SPacketBlockChange(this.field_187282_b.func_72688_a(), blockposition)));
                    if (this.field_187282_b.func_72688_a().func_180495_p(blockposition).func_177230_c().func_149716_u()) {
                        this.func_187273_a(this.field_187282_b.func_72688_a().func_175625_s(blockposition));
                    }
                } else if (this.field_187287_g == 64) {
                    // Paper - Anti-Xray - Loading chunks here could cause a ConcurrentModificationException #1104
                    //this.chunk.world.chunkPacketBlockController.onChunkPacketCreate(this.chunk, this.h, true); // Paper - Anti-Xray - Load nearby chunks if necessary
                    this.func_187267_a((Packet) (new SPacketChunkData(this.field_187286_f, this.field_187288_h)));
                } else {
                    this.func_187267_a((Packet) (new SPacketMultiBlockChange(this.field_187287_g, this.field_187285_e, this.field_187286_f)));

                    for (i = 0; i < this.field_187287_g; ++i) {
                        j = (this.field_187285_e[i] >> 12 & 15) + this.field_187284_d.field_77276_a * 16;
                        k = this.field_187285_e[i] & 255;
                        int l = (this.field_187285_e[i] >> 8 & 15) + this.field_187284_d.field_77275_b * 16;
                        BlockPos blockposition1 = new BlockPos(j, k, l);

                        if (this.field_187282_b.func_72688_a().func_180495_p(blockposition1).func_177230_c().func_149716_u()) {
                            this.func_187273_a(this.field_187282_b.func_72688_a().func_175625_s(blockposition1));
                        }
                    }
                }

                this.field_187287_g = 0;
                this.field_187288_h = 0;
            }
        }
    }

    private void func_187273_a(@Nullable TileEntity tileentity) {
        if (tileentity != null) {
            SPacketUpdateTileEntity packetplayouttileentitydata = tileentity.func_189518_D_();

            if (packetplayouttileentitydata != null) {
                this.func_187267_a((Packet) packetplayouttileentitydata);
            }
        }

    }

    public boolean func_187275_d(EntityPlayerMP entityplayer) {
        return this.field_187283_c.contains(entityplayer);
    }

    public boolean func_187269_a(Predicate<EntityPlayerMP> predicate) {
        return Iterables.tryFind(this.field_187283_c, predicate).isPresent();
    }

    public boolean func_187271_a(double d0, Predicate<EntityPlayerMP> predicate) {
        int i = 0;

        for (int j = this.field_187283_c.size(); i < j; ++i) {
            EntityPlayerMP entityplayer = (EntityPlayerMP) this.field_187283_c.get(i);

            if (predicate.apply(entityplayer) && this.field_187284_d.func_185327_a(entityplayer) < d0 * d0) {
                return true;
            }
        }

        return false;
    }

    public boolean func_187274_e() {
        return this.field_187290_j;
    }

    @Nullable
    public Chunk func_187266_f() {
        return this.field_187286_f;
    }

    public double func_187270_g() {
        double d0 = Double.MAX_VALUE;
        Iterator iterator = this.field_187283_c.iterator();

        while (iterator.hasNext()) {
            EntityPlayerMP entityplayer = (EntityPlayerMP) iterator.next();
            double d1 = this.field_187284_d.func_185327_a(entityplayer);

            if (d1 < d0) {
                d0 = d1;
            }
        }

        return d0;
    }
}
