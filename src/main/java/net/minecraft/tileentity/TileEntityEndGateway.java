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

    private static final Logger LOGGER = LogManager.getLogger();
    private long age;
    private int teleportCooldown;
    public BlockPos exitPortal;
    public boolean exactTeleport;

    public TileEntityEndGateway() {}

    public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setLong("Age", this.age);
        if (this.exitPortal != null) {
            nbttagcompound.setTag("ExitPortal", NBTUtil.createPosTag(this.exitPortal));
        }

        if (this.exactTeleport) {
            nbttagcompound.setBoolean("ExactTeleport", this.exactTeleport);
        }

        return nbttagcompound;
    }

    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.age = nbttagcompound.getLong("Age");
        if (nbttagcompound.hasKey("ExitPortal", 10)) {
            this.exitPortal = NBTUtil.getPosFromTag(nbttagcompound.getCompoundTag("ExitPortal"));
        }

        this.exactTeleport = nbttagcompound.getBoolean("ExactTeleport");
    }

    public void update() {
        boolean flag = this.isSpawning();
        boolean flag1 = this.isCoolingDown();

        ++this.age;
        if (flag1) {
            --this.teleportCooldown;
        } else if (!this.world.isRemote) {
            List list = this.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(this.getPos()));

            if (!list.isEmpty()) {
                this.teleportEntity((Entity) list.get(0));
            }

            if (this.age % 2400L == 0L) {
                this.triggerCooldown();
            }
        }

        if (flag != this.isSpawning() || flag1 != this.isCoolingDown()) {
            this.markDirty();
        }

    }

    public boolean isSpawning() {
        return this.age < 200L;
    }

    public boolean isCoolingDown() {
        return this.teleportCooldown > 0;
    }

    @Nullable
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(this.pos, 8, this.getUpdateTag());
    }

    public NBTTagCompound getUpdateTag() {
        return this.writeToNBT(new NBTTagCompound());
    }

    public void triggerCooldown() {
        if (!this.world.isRemote) {
            this.teleportCooldown = 40;
            this.world.addBlockEvent(this.getPos(), this.getBlockType(), 1, 0);
            this.markDirty();
        }

    }

    public boolean receiveClientEvent(int i, int j) {
        if (i == 1) {
            this.teleportCooldown = 40;
            return true;
        } else {
            return super.receiveClientEvent(i, j);
        }
    }

    public void teleportEntity(Entity entity) {
        if (!this.world.isRemote && !this.isCoolingDown()) {
            this.teleportCooldown = 100;
            if (this.exitPortal == null && this.world.provider instanceof WorldProviderEnd) {
                this.findExitPortal();
            }

            if (this.exitPortal != null) {
                BlockPos blockposition = this.exactTeleport ? this.exitPortal : this.findExitPosition();

                // CraftBukkit start - Fire PlayerTeleportEvent
                if (entity instanceof EntityPlayerMP) {
                    org.bukkit.craftbukkit.entity.CraftPlayer player = (CraftPlayer) entity.getBukkitEntity();
                    org.bukkit.Location location = new Location(world.getWorld(), (double) blockposition.getX() + 0.5D, (double) blockposition.getY() + 0.5D, (double) blockposition.getZ() + 0.5D);
                    location.setPitch(player.getLocation().getPitch());
                    location.setYaw(player.getLocation().getYaw());

                    PlayerTeleportEvent teleEvent = new com.destroystokyo.paper.event.player.PlayerTeleportEndGatewayEvent(player, player.getLocation(), location, new org.bukkit.craftbukkit.block.CraftEndGateway(MCUtil.toLocation(world, this.getPos()).getBlock())); // Paper
                    Bukkit.getPluginManager().callEvent(teleEvent);
                    if (teleEvent.isCancelled()) {
                        return;
                    }

                    ((EntityPlayerMP) entity).connection.teleport(teleEvent.getTo());
                    this.triggerCooldown(); // CraftBukkit - call at end of method
                    return;

                }
                // CraftBukkit end
                // Paper start - EntityTeleportEndGatewayEvent - replicated from above
                org.bukkit.craftbukkit.entity.CraftEntity bukkitEntity = entity.getBukkitEntity();
                org.bukkit.Location location = new Location(world.getWorld(), (double) blockposition.getX() + 0.5D, (double) blockposition.getY() + 0.5D, (double) blockposition.getZ() + 0.5D);
                location.setPitch(bukkitEntity.getLocation().getPitch());
                location.setYaw(bukkitEntity.getLocation().getYaw());

                com.destroystokyo.paper.event.entity.EntityTeleportEndGatewayEvent event = new com.destroystokyo.paper.event.entity.EntityTeleportEndGatewayEvent(bukkitEntity, bukkitEntity.getLocation(), location, new org.bukkit.craftbukkit.block.CraftEndGateway(MCUtil.toLocation(world, this.getPos()).getBlock()));
                if (!event.callEvent()) {
                    return;
                }

                entity.setPositionAndUpdate(event.getTo().getX(), event.getTo().getY(), event.getTo().getZ());
                // Paper end - EntityTeleportEndGatewayEvent
            }

            this.triggerCooldown();
        }
    }

    private BlockPos findExitPosition() {
        BlockPos blockposition = findHighestBlock(this.world, this.exitPortal, 5, false);

        TileEntityEndGateway.LOGGER.debug("Best exit position for portal at {} is {}", this.exitPortal, blockposition);
        return blockposition.up();
    }

    private void findExitPortal() {
        Vec3d vec3d = (new Vec3d((double) this.getPos().getX(), 0.0D, (double) this.getPos().getZ())).normalize();
        Vec3d vec3d1 = vec3d.scale(1024.0D);

        int i;

        for (i = 16; getChunk(this.world, vec3d1).getTopFilledSegment() > 0 && i-- > 0; vec3d1 = vec3d1.add(vec3d.scale(-16.0D))) {
            TileEntityEndGateway.LOGGER.debug("Skipping backwards past nonempty chunk at {}", vec3d1);
        }

        for (i = 16; getChunk(this.world, vec3d1).getTopFilledSegment() == 0 && i-- > 0; vec3d1 = vec3d1.add(vec3d.scale(16.0D))) {
            TileEntityEndGateway.LOGGER.debug("Skipping forward past empty chunk at {}", vec3d1);
        }

        TileEntityEndGateway.LOGGER.debug("Found chunk at {}", vec3d1);
        Chunk chunk = getChunk(this.world, vec3d1);

        this.exitPortal = findSpawnpointInChunk(chunk);
        if (this.exitPortal == null) {
            this.exitPortal = new BlockPos(vec3d1.x + 0.5D, 75.0D, vec3d1.z + 0.5D);
            TileEntityEndGateway.LOGGER.debug("Failed to find suitable block, settling on {}", this.exitPortal);
            (new WorldGenEndIsland()).generate(this.world, new Random(this.exitPortal.toLong()), this.exitPortal);
        } else {
            TileEntityEndGateway.LOGGER.debug("Found block at {}", this.exitPortal);
        }

        this.exitPortal = findHighestBlock(this.world, this.exitPortal, 16, true);
        TileEntityEndGateway.LOGGER.debug("Creating portal at {}", this.exitPortal);
        this.exitPortal = this.exitPortal.up(10);
        this.createExitPortal(this.exitPortal);
        this.markDirty();
    }

    private static BlockPos findHighestBlock(World world, BlockPos blockposition, int i, boolean flag) {
        BlockPos blockposition1 = null;

        for (int j = -i; j <= i; ++j) {
            for (int k = -i; k <= i; ++k) {
                if (j != 0 || k != 0 || flag) {
                    for (int l = 255; l > (blockposition1 == null ? 0 : blockposition1.getY()); --l) {
                        BlockPos blockposition2 = new BlockPos(blockposition.getX() + j, l, blockposition.getZ() + k);
                        IBlockState iblockdata = world.getBlockState(blockposition2);

                        if (iblockdata.isBlockNormalCube() && (flag || iblockdata.getBlock() != Blocks.BEDROCK)) {
                            blockposition1 = blockposition2;
                            break;
                        }
                    }
                }
            }
        }

        return blockposition1 == null ? blockposition : blockposition1;
    }

    private static Chunk getChunk(World world, Vec3d vec3d) {
        return world.getChunkFromChunkCoords(MathHelper.floor(vec3d.x / 16.0D), MathHelper.floor(vec3d.z / 16.0D));
    }

    @Nullable
    private static BlockPos findSpawnpointInChunk(Chunk chunk) {
        BlockPos blockposition = new BlockPos(chunk.x * 16, 30, chunk.z * 16);
        int i = chunk.getTopFilledSegment() + 16 - 1;
        BlockPos blockposition1 = new BlockPos(chunk.x * 16 + 16 - 1, i, chunk.z * 16 + 16 - 1);
        BlockPos blockposition2 = null;
        double d0 = 0.0D;
        Iterator iterator = BlockPos.getAllInBox(blockposition, blockposition1).iterator();

        while (iterator.hasNext()) {
            BlockPos blockposition3 = (BlockPos) iterator.next();
            IBlockState iblockdata = chunk.getBlockState(blockposition3);

            if (iblockdata.getBlock() == Blocks.END_STONE && !chunk.getBlockState(blockposition3.up(1)).isBlockNormalCube() && !chunk.getBlockState(blockposition3.up(2)).isBlockNormalCube()) {
                double d1 = blockposition3.distanceSqToCenter(0.0D, 0.0D, 0.0D);

                if (blockposition2 == null || d1 < d0) {
                    blockposition2 = blockposition3;
                    d0 = d1;
                }
            }
        }

        return blockposition2;
    }

    private void createExitPortal(BlockPos blockposition) {
        (new WorldGenEndGateway()).generate(this.world, new Random(), blockposition);
        TileEntity tileentity = this.world.getTileEntity(blockposition);

        if (tileentity instanceof TileEntityEndGateway) {
            TileEntityEndGateway tileentityendgateway = (TileEntityEndGateway) tileentity;

            tileentityendgateway.exitPortal = new BlockPos(this.getPos());
            tileentityendgateway.markDirty();
        } else {
            TileEntityEndGateway.LOGGER.warn("Couldn\'t save exit portal at {}", blockposition);
        }

    }

    public void setExactPosition(BlockPos blockposition) {
        this.exactTeleport = true;
        this.exitPortal = blockposition;
    }
}
