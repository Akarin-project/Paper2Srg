package org.bukkit.craftbukkit.chunkio;

import java.io.IOException;

import co.aikar.timings.Timing;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.storage.AnvilChunkLoader;
import net.minecraft.nbt.NBTTagCompound;

import org.bukkit.craftbukkit.util.AsynchronousExecutor;

import java.util.concurrent.atomic.AtomicInteger;

class ChunkIOProvider implements AsynchronousExecutor.CallBackProvider<QueuedChunk, Chunk, Runnable, RuntimeException> {
    private final AtomicInteger threadNumber = new AtomicInteger(1);

    // async stuff
    public Chunk callStage1(QueuedChunk queuedChunk) throws RuntimeException {
        try (Timing ignored = queuedChunk.provider.field_73251_h.timings.chunkIOStage1.startTimingIfSync()) { // Paper
            AnvilChunkLoader loader = queuedChunk.loader;
            Object[] data = loader.loadChunk(queuedChunk.world, queuedChunk.x, queuedChunk.z);
            
            if (data != null) {
                queuedChunk.compound = (NBTTagCompound) data[1];
                return (Chunk) data[0];
            }

            return null;
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        // Paper - Mirror vanilla by catching everything (else) rather than immediately crashing the server
        // stage2 will receive a null chunk and then load it synchronously, where vanilla MC will properly log and recover
        // stage2 will _not_ however return that instance, only load it
        } catch (Exception ex) {
            return null;
        }
    }

    // sync stuff
    public void callStage2(QueuedChunk queuedChunk, Chunk chunk) throws RuntimeException {
        if (chunk == null || queuedChunk.provider.field_73244_f.containsKey(ChunkPos.func_77272_a(queuedChunk.x, queuedChunk.z))) { // Paper - also call original if it was already loaded
            // If the chunk loading failed (or was already loaded for some reason) just do it synchronously (may generate)
            queuedChunk.provider.originalGetChunkAt(queuedChunk.x, queuedChunk.z);
            return;
        }
        try (Timing ignored = queuedChunk.provider.field_73251_h.timings.chunkIOStage2.startTimingIfSync()) { // Paper

        queuedChunk.loader.loadEntities(chunk, queuedChunk.compound.func_74775_l("Level"), queuedChunk.world);
        chunk.func_177432_b(queuedChunk.provider.field_73251_h.func_82737_E());
        queuedChunk.provider.field_73244_f.put(ChunkPos.func_77272_a(queuedChunk.x, queuedChunk.z), chunk);
        chunk.func_76631_c();

        if (queuedChunk.provider.field_186029_c != null) {
            queuedChunk.provider.field_186029_c.func_180514_a(chunk, queuedChunk.x, queuedChunk.z);
        }

        chunk.loadNearby(queuedChunk.provider, queuedChunk.provider.field_186029_c, false);
        } // Paper
    }

    public void callStage3(QueuedChunk queuedChunk, Chunk chunk, Runnable runnable) throws RuntimeException {
        runnable.run();
    }

    public Thread newThread(Runnable runnable) {
        Thread thread = new Thread(runnable, "Chunk I/O Executor Thread-" + threadNumber.getAndIncrement());
        thread.setDaemon(true);
        return thread;
    }
}
