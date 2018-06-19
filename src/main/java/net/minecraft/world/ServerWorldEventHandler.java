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

    private final MinecraftServer field_72783_a;
    private final WorldServer field_72782_b;

    public ServerWorldEventHandler(MinecraftServer minecraftserver, WorldServer worldserver) {
        this.field_72783_a = minecraftserver;
        this.field_72782_b = worldserver;
    }

    public void func_180442_a(int i, boolean flag, double d0, double d1, double d2, double d3, double d4, double d5, int... aint) {}

    public void func_190570_a(int i, boolean flag, boolean flag1, double d0, double d1, double d2, double d3, double d4, double d5, int... aint) {}

    public void func_72703_a(Entity entity) {
        this.field_72782_b.func_73039_n().func_72786_a(entity);
        if (entity instanceof EntityPlayerMP) {
            this.field_72782_b.field_73011_w.func_186061_a((EntityPlayerMP) entity);
        }

    }

    public void func_72709_b(Entity entity) {
        this.field_72782_b.func_73039_n().func_72790_b(entity);
        this.field_72782_b.func_96441_U().func_181140_a(entity);
        if (entity instanceof EntityPlayerMP) {
            this.field_72782_b.field_73011_w.func_186062_b((EntityPlayerMP) entity);
        }

    }

    public void func_184375_a(@Nullable EntityPlayer entityhuman, SoundEvent soundeffect, SoundCategory soundcategory, double d0, double d1, double d2, float f, float f1) {
        // CraftBukkit - this.world.dimension
        this.field_72783_a.func_184103_al().func_148543_a(entityhuman, d0, d1, d2, f > 1.0F ? (double) (16.0F * f) : 16.0D, this.field_72782_b.dimension, new SPacketSoundEffect(soundeffect, soundcategory, d0, d1, d2, f, f1));
    }

    public void func_147585_a(int i, int j, int k, int l, int i1, int j1) {}

    public void func_184376_a(World world, BlockPos blockposition, IBlockState iblockdata, IBlockState iblockdata1, int i) {
        this.field_72782_b.func_184164_w().func_180244_a(blockposition);
    }

    public void func_174959_b(BlockPos blockposition) {}

    public void func_184377_a(SoundEvent soundeffect, BlockPos blockposition) {}

    public void func_180439_a(EntityPlayer entityhuman, int i, BlockPos blockposition, int j) {
        // CraftBukkit - this.world.dimension
        this.field_72783_a.func_184103_al().func_148543_a(entityhuman, (double) blockposition.func_177958_n(), (double) blockposition.func_177956_o(), (double) blockposition.func_177952_p(), 64.0D, this.field_72782_b.dimension, new SPacketEffect(i, blockposition, j, false));
    }

    public void func_180440_a(int i, BlockPos blockposition, int j) {
        this.field_72783_a.func_184103_al().func_148540_a(new SPacketEffect(i, blockposition, j, true));
    }

    public void func_180441_b(int i, BlockPos blockposition, int j) {
        Iterator iterator = this.field_72783_a.func_184103_al().func_181057_v().iterator();

        // CraftBukkit start
        EntityPlayer entityhuman = null;
        Entity entity = field_72782_b.func_73045_a(i);
        if (entity instanceof EntityPlayer) entityhuman = (EntityPlayer) entity;
        // CraftBukkit end

        while (iterator.hasNext()) {
            EntityPlayerMP entityplayer = (EntityPlayerMP) iterator.next();

            if (entityplayer != null && entityplayer.field_70170_p == this.field_72782_b && entityplayer.func_145782_y() != i) {
                double d0 = (double) blockposition.func_177958_n() - entityplayer.field_70165_t;
                double d1 = (double) blockposition.func_177956_o() - entityplayer.field_70163_u;
                double d2 = (double) blockposition.func_177952_p() - entityplayer.field_70161_v;

                // CraftBukkit start
                if (entityhuman != null && entityhuman instanceof EntityPlayerMP && !entityplayer.getBukkitEntity().canSee(((EntityPlayerMP) entityhuman).getBukkitEntity())) {
                    continue;
                }
                // CraftBukkit end

                if (d0 * d0 + d1 * d1 + d2 * d2 < 1024.0D) {
                    entityplayer.field_71135_a.func_147359_a(new SPacketBlockBreakAnim(i, blockposition, j));
                }
            }
        }

    }
}
