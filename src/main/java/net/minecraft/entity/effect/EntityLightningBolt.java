package net.minecraft.entity.effect;

import java.util.List;


import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import org.bukkit.craftbukkit.event.CraftEventFactory;

public class EntityLightningBolt extends EntityWeatherEffect {

    private int field_70262_b;
    public long field_70264_a;
    private int field_70263_c;
    private final boolean field_184529_d;
    public boolean isEffect; // CraftBukkit
    public boolean isSilent = false; // Spigot

    public EntityLightningBolt(World world, double d0, double d1, double d2, boolean flag) {
        super(world);
        this.isEffect = flag; // CraftBukkit
        this.func_70012_b(d0, d1, d2, 0.0F, 0.0F);
        this.field_70262_b = 2;
        this.field_70264_a = this.field_70146_Z.nextLong();
        this.field_70263_c = this.field_70146_Z.nextInt(3) + 1;
        this.field_184529_d = flag;
        BlockPos blockposition = new BlockPos(this);

        if (!flag && !world.field_72995_K && world.func_82736_K().func_82766_b("doFireTick") && (world.func_175659_aa() == EnumDifficulty.NORMAL || world.func_175659_aa() == EnumDifficulty.HARD) && world.func_175697_a(blockposition, 10)) {
            if (world.func_180495_p(blockposition).func_185904_a() == Material.field_151579_a && Blocks.field_150480_ab.func_176196_c(world, blockposition)) {
                // CraftBukkit start
                if (!CraftEventFactory.callBlockIgniteEvent(world, blockposition.func_177958_n(), blockposition.func_177956_o(), blockposition.func_177952_p(), this).isCancelled()) {
                    world.func_175656_a(blockposition, Blocks.field_150480_ab.func_176223_P());
                }
                // CraftBukkit end
            }

            for (int i = 0; i < 4; ++i) {
                BlockPos blockposition1 = blockposition.func_177982_a(this.field_70146_Z.nextInt(3) - 1, this.field_70146_Z.nextInt(3) - 1, this.field_70146_Z.nextInt(3) - 1);

                if (world.func_180495_p(blockposition1).func_185904_a() == Material.field_151579_a && Blocks.field_150480_ab.func_176196_c(world, blockposition1)) {
                    // CraftBukkit start
                    if (!CraftEventFactory.callBlockIgniteEvent(world, blockposition1.func_177958_n(), blockposition1.func_177956_o(), blockposition1.func_177952_p(), this).isCancelled()) {
                        world.func_175656_a(blockposition1, Blocks.field_150480_ab.func_176223_P());
                    }
                    // CraftBukkit end
                }
            }
        }

    }

    // Spigot start
    public EntityLightningBolt(World world, double d0, double d1, double d2, boolean isEffect, boolean isSilent)
    {
        this( world, d0, d1, d2, isEffect );
        this.isSilent = isSilent;
    }
    // Spigot end

    public SoundCategory func_184176_by() {
        return SoundCategory.WEATHER;
    }

    public void func_70071_h_() {
        super.func_70071_h_();
        if (!isSilent && this.field_70262_b == 2) { // Spigot
            // CraftBukkit start - Use relative location for far away sounds
            // this.world.a((EntityHuman) null, this.locX, this.locY, this.locZ, SoundEffects.dG, SoundCategory.WEATHER, 10000.0F, 0.8F + this.random.nextFloat() * 0.2F);
            float pitch = 0.8F + this.field_70146_Z.nextFloat() * 0.2F;
            int viewDistance = ((WorldServer) this.field_70170_p).getServer().getViewDistance() * 16;
            for (EntityPlayerMP player : (List<EntityPlayerMP>) (List) this.field_70170_p.field_73010_i) {
                double deltaX = this.field_70165_t - player.field_70165_t;
                double deltaZ = this.field_70161_v - player.field_70161_v;
                double distanceSquared = deltaX * deltaX + deltaZ * deltaZ;
                if (distanceSquared > viewDistance * viewDistance) {
                    double deltaLength = Math.sqrt(distanceSquared);
                    double relativeX = player.field_70165_t + (deltaX / deltaLength) * viewDistance;
                    double relativeZ = player.field_70161_v + (deltaZ / deltaLength) * viewDistance;
                    player.field_71135_a.func_147359_a(new SPacketSoundEffect(SoundEvents.field_187754_de, SoundCategory.WEATHER, relativeX, this.field_70163_u, relativeZ, 10000.0F, pitch));
                } else {
                    player.field_71135_a.func_147359_a(new SPacketSoundEffect(SoundEvents.field_187754_de, SoundCategory.WEATHER, this.field_70165_t, this.field_70163_u, this.field_70161_v, 10000.0F, pitch));
                }
            }
            // CraftBukkit end
            this.field_70170_p.func_184148_a((EntityPlayer) null, this.field_70165_t, this.field_70163_u, this.field_70161_v, SoundEvents.field_187752_dd, SoundCategory.WEATHER, 2.0F, 0.5F + this.field_70146_Z.nextFloat() * 0.2F);
        }

        --this.field_70262_b;
        if (this.field_70262_b < 0) {
            if (this.field_70263_c == 0) {
                this.func_70106_y();
            } else if (this.field_70262_b < -this.field_70146_Z.nextInt(10)) {
                --this.field_70263_c;
                this.field_70262_b = 1;
                if (!this.field_184529_d && !this.field_70170_p.field_72995_K) {
                    this.field_70264_a = this.field_70146_Z.nextLong();
                    BlockPos blockposition = new BlockPos(this);

                    if (this.field_70170_p.func_82736_K().func_82766_b("doFireTick") && this.field_70170_p.func_175697_a(blockposition, 10) && this.field_70170_p.func_180495_p(blockposition).func_185904_a() == Material.field_151579_a && Blocks.field_150480_ab.func_176196_c(this.field_70170_p, blockposition)) {
                        // CraftBukkit start - add "!isEffect"
                        if (!isEffect && !CraftEventFactory.callBlockIgniteEvent(field_70170_p, blockposition.func_177958_n(), blockposition.func_177956_o(), blockposition.func_177952_p(), this).isCancelled()) {
                            this.field_70170_p.func_175656_a(blockposition, Blocks.field_150480_ab.func_176223_P());
                        }
                        // CraftBukkit end
                    }
                }
            }
        }

        if (this.field_70262_b >= 0 && !this.isEffect) { // CraftBukkit - add !this.isEffect
            if (this.field_70170_p.field_72995_K) {
                this.field_70170_p.func_175702_c(2);
            } else if (!this.field_184529_d) {
                double d0 = 3.0D;
                List list = this.field_70170_p.func_72839_b(this, new AxisAlignedBB(this.field_70165_t - 3.0D, this.field_70163_u - 3.0D, this.field_70161_v - 3.0D, this.field_70165_t + 3.0D, this.field_70163_u + 6.0D + 3.0D, this.field_70161_v + 3.0D));

                for (int i = 0; i < list.size(); ++i) {
                    Entity entity = (Entity) list.get(i);

                    entity.func_70077_a(this);
                }
            }
        }

    }

    protected void func_70088_a() {}

    protected void func_70037_a(NBTTagCompound nbttagcompound) {}

    protected void func_70014_b(NBTTagCompound nbttagcompound) {}
}
