package net.minecraft.world;

import java.util.Iterator;
import javax.annotation.Nullable;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketBlockBreakAnim;
import net.minecraft.network.play.server.SPacketEffect;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;

public class ServerWorldEventHandler implements IWorldEventListener {

    private final MinecraftServer mcServer;
    private final WorldServer world;

    public ServerWorldEventHandler(MinecraftServer minecraftserver, WorldServer worldserver) {
        this.mcServer = minecraftserver;
        this.world = worldserver;
    }

    public void spawnParticle(int i, boolean flag, double d0, double d1, double d2, double d3, double d4, double d5, int... aint) {}

    public void spawnParticle(int i, boolean flag, boolean flag1, double d0, double d1, double d2, double d3, double d4, double d5, int... aint) {}

    public void onEntityAdded(Entity entity) {
        this.world.getEntityTracker().track(entity);
        if (entity instanceof EntityPlayerMP) {
            this.world.provider.onPlayerAdded((EntityPlayerMP) entity);
        }

    }

    public void onEntityRemoved(Entity entity) {
        this.world.getEntityTracker().untrack(entity);
        this.world.getScoreboard().removeEntity(entity);
        if (entity instanceof EntityPlayerMP) {
            this.world.provider.onPlayerRemoved((EntityPlayerMP) entity);
        }

    }

    public void playSoundToAllNearExcept(@Nullable EntityPlayer entityhuman, SoundEvent soundeffect, SoundCategory soundcategory, double d0, double d1, double d2, float f, float f1) {
        // CraftBukkit - this.world.dimension
        this.mcServer.getPlayerList().sendToAllNearExcept(entityhuman, d0, d1, d2, f > 1.0F ? (double) (16.0F * f) : 16.0D, this.world.dimension, new SPacketSoundEffect(soundeffect, soundcategory, d0, d1, d2, f, f1));
    }

    public void markBlockRangeForRenderUpdate(int i, int j, int k, int l, int i1, int j1) {}

    public void notifyBlockUpdate(World world, BlockPos blockposition, IBlockState iblockdata, IBlockState iblockdata1, int i) {
        this.world.getPlayerChunkMap().markBlockForUpdate(blockposition);
    }

    public void notifyLightSet(BlockPos blockposition) {}

    public void playRecord(SoundEvent soundeffect, BlockPos blockposition) {}

    public void playEvent(EntityPlayer entityhuman, int i, BlockPos blockposition, int j) {
        // CraftBukkit - this.world.dimension
        this.mcServer.getPlayerList().sendToAllNearExcept(entityhuman, (double) blockposition.getX(), (double) blockposition.getY(), (double) blockposition.getZ(), 64.0D, this.world.dimension, new SPacketEffect(i, blockposition, j, false));
    }

    public void broadcastSound(int i, BlockPos blockposition, int j) {
        this.mcServer.getPlayerList().sendPacketToAllPlayers(new SPacketEffect(i, blockposition, j, true));
    }

    public void sendBlockBreakProgress(int i, BlockPos blockposition, int j) {
        Iterator iterator = this.mcServer.getPlayerList().getPlayers().iterator();

        // CraftBukkit start
        EntityPlayer entityhuman = null;
        Entity entity = world.getEntityByID(i);
        if (entity instanceof EntityPlayer) entityhuman = (EntityPlayer) entity;
        // CraftBukkit end

        while (iterator.hasNext()) {
            EntityPlayerMP entityplayer = (EntityPlayerMP) iterator.next();

            if (entityplayer != null && entityplayer.world == this.world && entityplayer.getEntityId() != i) {
                double d0 = (double) blockposition.getX() - entityplayer.posX;
                double d1 = (double) blockposition.getY() - entityplayer.posY;
                double d2 = (double) blockposition.getZ() - entityplayer.posZ;

                // CraftBukkit start
                if (entityhuman != null && entityhuman instanceof EntityPlayerMP && !entityplayer.getBukkitEntity().canSee(((EntityPlayerMP) entityhuman).getBukkitEntity())) {
                    continue;
                }
                // CraftBukkit end

                if (d0 * d0 + d1 * d1 + d2 * d2 < 1024.0D) {
                    entityplayer.connection.sendPacket(new SPacketBlockBreakAnim(i, blockposition, j));
                }
            }
        }

    }
}
