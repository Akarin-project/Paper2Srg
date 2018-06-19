package net.minecraft.tileentity;

import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.server.MCUtil;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldProviderEnd;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.feature.WorldGenEndGateway;
import net.minecraft.world.gen.feature.WorldGenEndIsland;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.event.player.PlayerTeleportEvent;

// CraftBukkit start
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.event.player.PlayerTeleportEvent;
// CraftBukkit end

public class TileEntityEndGateway extends TileEntityEndPortal implements ITickable {

    private static final Logger field_184314_a = LogManager.getLogger();
    private long field_184315_f;
    private int field_184316_g;
    public BlockPos field_184317_h;
    public boolean field_184318_i;

    public TileEntityEndGateway() {}

    public NBTTagCompound func_189515_b(NBTTagCompound nbttagcompound) {
        super.func_189515_b(nbttagcompound);
        nbttagcompound.func_74772_a("Age", this.field_184315_f);
        if (this.field_184317_h != null) {
            nbttagcompound.func_74782_a("ExitPortal", NBTUtil.func_186859_a(this.field_184317_h));
        }

        if (this.field_184318_i) {
            nbttagcompound.func_74757_a("ExactTeleport", this.field_184318_i);
        }

        return nbttagcompound;
    }

    public void func_145839_a(NBTTagCompound nbttagcompound) {
        super.func_145839_a(nbttagcompound);
        this.field_184315_f = nbttagcompound.func_74763_f("Age");
        if (nbttagcompound.func_150297_b("ExitPortal", 10)) {
            this.field_184317_h = NBTUtil.func_186861_c(nbttagcompound.func_74775_l("ExitPortal"));
        }

        this.field_184318_i = nbttagcompound.func_74767_n("ExactTeleport");
    }

    public void func_73660_a() {
        boolean flag = this.func_184309_b();
        boolean flag1 = this.func_184310_d();

        ++this.field_184315_f;
        if (flag1) {
            --this.field_184316_g;
        } else if (!this.field_145850_b.field_72995_K) {
            List list = this.field_145850_b.func_72872_a(Entity.class, new AxisAlignedBB(this.func_174877_v()));

            if (!list.isEmpty()) {
                this.func_184306_a((Entity) list.get(0));
            }

            if (this.field_184315_f % 2400L == 0L) {
                this.func_184300_h();
            }
        }

        if (flag != this.func_184309_b() || flag1 != this.func_184310_d()) {
            this.func_70296_d();
        }

    }

    public boolean func_184309_b() {
        return this.field_184315_f < 200L;
    }

    public boolean func_184310_d() {
        return this.field_184316_g > 0;
    }

    @Nullable
    public SPacketUpdateTileEntity func_189518_D_() {
        return new SPacketUpdateTileEntity(this.field_174879_c, 8, this.func_189517_E_());
    }

    public NBTTagCompound func_189517_E_() {
        return this.func_189515_b(new NBTTagCompound());
    }

    public void func_184300_h() {
        if (!this.field_145850_b.field_72995_K) {
            this.field_184316_g = 40;
            this.field_145850_b.func_175641_c(this.func_174877_v(), this.func_145838_q(), 1, 0);
            this.func_70296_d();
        }

    }

    public boolean func_145842_c(int i, int j) {
        if (i == 1) {
            this.field_184316_g = 40;
            return true;
        } else {
            return super.func_145842_c(i, j);
        }
    }

    public void func_184306_a(Entity entity) {
        if (!this.field_145850_b.field_72995_K && !this.func_184310_d()) {
            this.field_184316_g = 100;
            if (this.field_184317_h == null && this.field_145850_b.field_73011_w instanceof WorldProviderEnd) {
                this.func_184311_k();
            }

            if (this.field_184317_h != null) {
                BlockPos blockposition = this.field_184318_i ? this.field_184317_h : this.func_184303_j();

                // CraftBukkit start - Fire PlayerTeleportEvent
                if (entity instanceof EntityPlayerMP) {
                    org.bukkit.craftbukkit.entity.CraftPlayer player = (CraftPlayer) entity.getBukkitEntity();
                    org.bukkit.Location location = new Location(field_145850_b.getWorld(), (double) blockposition.func_177958_n() + 0.5D, (double) blockposition.func_177956_o() + 0.5D, (double) blockposition.func_177952_p() + 0.5D);
                    location.setPitch(player.getLocation().getPitch());
                    location.setYaw(player.getLocation().getYaw());

                    PlayerTeleportEvent teleEvent = new com.destroystokyo.paper.event.player.PlayerTeleportEndGatewayEvent(player, player.getLocation(), location, new org.bukkit.craftbukkit.block.CraftEndGateway(MCUtil.toLocation(field_145850_b, this.func_174877_v()).getBlock())); // Paper
                    Bukkit.getPluginManager().callEvent(teleEvent);
                    if (teleEvent.isCancelled()) {
                        return;
                    }

                    ((EntityPlayerMP) entity).field_71135_a.teleport(teleEvent.getTo());
                    this.func_184300_h(); // CraftBukkit - call at end of method
                    return;

                }
                // CraftBukkit end
                // Paper start - EntityTeleportEndGatewayEvent - replicated from above
                org.bukkit.craftbukkit.entity.CraftEntity bukkitEntity = entity.getBukkitEntity();
                org.bukkit.Location location = new Location(field_145850_b.getWorld(), (double) blockposition.func_177958_n() + 0.5D, (double) blockposition.func_177956_o() + 0.5D, (double) blockposition.func_177952_p() + 0.5D);
                location.setPitch(bukkitEntity.getLocation().getPitch());
                location.setYaw(bukkitEntity.getLocation().getYaw());

                com.destroystokyo.paper.event.entity.EntityTeleportEndGatewayEvent event = new com.destroystokyo.paper.event.entity.EntityTeleportEndGatewayEvent(bukkitEntity, bukkitEntity.getLocation(), location, new org.bukkit.craftbukkit.block.CraftEndGateway(MCUtil.toLocation(field_145850_b, this.func_174877_v()).getBlock()));
                if (!event.callEvent()) {
                    return;
                }

                entity.func_70634_a(event.getTo().getX(), event.getTo().getY(), event.getTo().getZ());
                // Paper end - EntityTeleportEndGatewayEvent
            }

            this.func_184300_h();
        }
    }

    private BlockPos func_184303_j() {
        BlockPos blockposition = func_184308_a(this.field_145850_b, this.field_184317_h, 5, false);

        TileEntityEndGateway.field_184314_a.debug("Best exit position for portal at {} is {}", this.field_184317_h, blockposition);
        return blockposition.func_177984_a();
    }

    private void func_184311_k() {
        Vec3d vec3d = (new Vec3d((double) this.func_174877_v().func_177958_n(), 0.0D, (double) this.func_174877_v().func_177952_p())).func_72432_b();
        Vec3d vec3d1 = vec3d.func_186678_a(1024.0D);

        int i;

        for (i = 16; func_184301_a(this.field_145850_b, vec3d1).func_76625_h() > 0 && i-- > 0; vec3d1 = vec3d1.func_178787_e(vec3d.func_186678_a(-16.0D))) {
            TileEntityEndGateway.field_184314_a.debug("Skipping backwards past nonempty chunk at {}", vec3d1);
        }

        for (i = 16; func_184301_a(this.field_145850_b, vec3d1).func_76625_h() == 0 && i-- > 0; vec3d1 = vec3d1.func_178787_e(vec3d.func_186678_a(16.0D))) {
            TileEntityEndGateway.field_184314_a.debug("Skipping forward past empty chunk at {}", vec3d1);
        }

        TileEntityEndGateway.field_184314_a.debug("Found chunk at {}", vec3d1);
        Chunk chunk = func_184301_a(this.field_145850_b, vec3d1);

        this.field_184317_h = func_184307_a(chunk);
        if (this.field_184317_h == null) {
            this.field_184317_h = new BlockPos(vec3d1.field_72450_a + 0.5D, 75.0D, vec3d1.field_72449_c + 0.5D);
            TileEntityEndGateway.field_184314_a.debug("Failed to find suitable block, settling on {}", this.field_184317_h);
            (new WorldGenEndIsland()).func_180709_b(this.field_145850_b, new Random(this.field_184317_h.func_177986_g()), this.field_184317_h);
        } else {
            TileEntityEndGateway.field_184314_a.debug("Found block at {}", this.field_184317_h);
        }

        this.field_184317_h = func_184308_a(this.field_145850_b, this.field_184317_h, 16, true);
        TileEntityEndGateway.field_184314_a.debug("Creating portal at {}", this.field_184317_h);
        this.field_184317_h = this.field_184317_h.func_177981_b(10);
        this.func_184312_b(this.field_184317_h);
        this.func_70296_d();
    }

    private static BlockPos func_184308_a(World world, BlockPos blockposition, int i, boolean flag) {
        BlockPos blockposition1 = null;

        for (int j = -i; j <= i; ++j) {
            for (int k = -i; k <= i; ++k) {
                if (j != 0 || k != 0 || flag) {
                    for (int l = 255; l > (blockposition1 == null ? 0 : blockposition1.func_177956_o()); --l) {
                        BlockPos blockposition2 = new BlockPos(blockposition.func_177958_n() + j, l, blockposition.func_177952_p() + k);
                        IBlockState iblockdata = world.func_180495_p(blockposition2);

                        if (iblockdata.func_185898_k() && (flag || iblockdata.func_177230_c() != Blocks.field_150357_h)) {
                            blockposition1 = blockposition2;
                            break;
                        }
                    }
                }
            }
        }

        return blockposition1 == null ? blockposition : blockposition1;
    }

    private static Chunk func_184301_a(World world, Vec3d vec3d) {
        return world.func_72964_e(MathHelper.func_76128_c(vec3d.field_72450_a / 16.0D), MathHelper.func_76128_c(vec3d.field_72449_c / 16.0D));
    }

    @Nullable
    private static BlockPos func_184307_a(Chunk chunk) {
        BlockPos blockposition = new BlockPos(chunk.field_76635_g * 16, 30, chunk.field_76647_h * 16);
        int i = chunk.func_76625_h() + 16 - 1;
        BlockPos blockposition1 = new BlockPos(chunk.field_76635_g * 16 + 16 - 1, i, chunk.field_76647_h * 16 + 16 - 1);
        BlockPos blockposition2 = null;
        double d0 = 0.0D;
        Iterator iterator = BlockPos.func_177980_a(blockposition, blockposition1).iterator();

        while (iterator.hasNext()) {
            BlockPos blockposition3 = (BlockPos) iterator.next();
            IBlockState iblockdata = chunk.func_177435_g(blockposition3);

            if (iblockdata.func_177230_c() == Blocks.field_150377_bs && !chunk.func_177435_g(blockposition3.func_177981_b(1)).func_185898_k() && !chunk.func_177435_g(blockposition3.func_177981_b(2)).func_185898_k()) {
                double d1 = blockposition3.func_177957_d(0.0D, 0.0D, 0.0D);

                if (blockposition2 == null || d1 < d0) {
                    blockposition2 = blockposition3;
                    d0 = d1;
                }
            }
        }

        return blockposition2;
    }

    private void func_184312_b(BlockPos blockposition) {
        (new WorldGenEndGateway()).func_180709_b(this.field_145850_b, new Random(), blockposition);
        TileEntity tileentity = this.field_145850_b.func_175625_s(blockposition);

        if (tileentity instanceof TileEntityEndGateway) {
            TileEntityEndGateway tileentityendgateway = (TileEntityEndGateway) tileentity;

            tileentityendgateway.field_184317_h = new BlockPos(this.func_174877_v());
            tileentityendgateway.func_70296_d();
        } else {
            TileEntityEndGateway.field_184314_a.warn("Couldn\'t save exit portal at {}", blockposition);
        }

    }

    public void func_190603_b(BlockPos blockposition) {
        this.field_184318_i = true;
        this.field_184317_h = blockposition;
    }
}
