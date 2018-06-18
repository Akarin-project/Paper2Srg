package net.minecraft.tileentity;

import javax.annotation.Nullable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import co.aikar.timings.MinecraftTimings;
import co.aikar.timings.Timing;
import net.minecraft.block.Block;
import net.minecraft.block.BlockJukebox;
import net.minecraft.block.state.IBlockState;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.ICrashReportDetail;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.RegistryNamespaced;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import org.bukkit.inventory.InventoryHolder;

public abstract class TileEntity {

    public Timing tickTimer = MinecraftTimings.getTileEntityTimings(this); // Paper
    boolean isLoadingStructure = false; // Paper
    private static final Logger LOGGER = LogManager.getLogger();
    private static final RegistryNamespaced<ResourceLocation, Class<? extends TileEntity>> REGISTRY = new RegistryNamespaced();
    protected World world;
    protected BlockPos pos;
    protected boolean tileEntityInvalid;
    private int blockMetadata;
    protected Block blockType;

    public TileEntity() {
        this.pos = BlockPos.ORIGIN;
        this.blockMetadata = -1;
    }

    private static void register(String s, Class<? extends TileEntity> oclass) {
        TileEntity.REGISTRY.putObject(new ResourceLocation(s), oclass);
    }

    @Nullable
    public static ResourceLocation getKey(Class<? extends TileEntity> oclass) {
        return (ResourceLocation) TileEntity.REGISTRY.getNameForObject(oclass);
    }

    static boolean IGNORE_TILE_UPDATES = false; // Paper
    public World getWorld() {
        return this.world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public boolean hasWorld() {
        return this.world != null;
    }

    public void readFromNBT(NBTTagCompound nbttagcompound) {
        this.pos = new BlockPos(nbttagcompound.getInteger("x"), nbttagcompound.getInteger("y"), nbttagcompound.getInteger("z"));
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
        return this.writeInternal(nbttagcompound);
    }

    private NBTTagCompound writeInternal(NBTTagCompound nbttagcompound) {
        ResourceLocation minecraftkey = (ResourceLocation) TileEntity.REGISTRY.getNameForObject(this.getClass());

        if (minecraftkey == null) {
            throw new RuntimeException(this.getClass() + " is missing a mapping! This is a bug!");
        } else {
            nbttagcompound.setString("id", minecraftkey.toString());
            nbttagcompound.setInteger("x", this.pos.getX());
            nbttagcompound.setInteger("y", this.pos.getY());
            nbttagcompound.setInteger("z", this.pos.getZ());
            return nbttagcompound;
        }
    }

    @Nullable
    public static TileEntity create(World world, NBTTagCompound nbttagcompound) {
        TileEntity tileentity = null;
        String s = nbttagcompound.getString("id");

        try {
            Class oclass = (Class) TileEntity.REGISTRY.getObject(new ResourceLocation(s));

            if (oclass != null) {
                tileentity = (TileEntity) oclass.newInstance();
            }
        } catch (Throwable throwable) {
            TileEntity.LOGGER.error("Failed to create block entity {}", s, throwable);
        }

        if (tileentity != null) {
            try {
                tileentity.setWorldCreate(world);
                tileentity.readFromNBT(nbttagcompound);
            } catch (Throwable throwable1) {
                TileEntity.LOGGER.error("Failed to load data for block entity {}", s, throwable1);
                tileentity = null;
            }
        } else {
            TileEntity.LOGGER.warn("Skipping BlockEntity with id {}", s);
        }

        return tileentity;
    }

    protected void setWorldCreate(World world) {}

    public int getBlockMetadata() {
        if (this.blockMetadata == -1) {
            IBlockState iblockdata = this.world.getBlockState(this.pos);

            this.blockMetadata = iblockdata.getBlock().getMetaFromState(iblockdata);
        }

        return this.blockMetadata;
    }

    public void markDirty() {
        if (this.world != null) {
            if (IGNORE_TILE_UPDATES) return; // Paper
            IBlockState iblockdata = this.world.getBlockState(this.pos);

            this.blockMetadata = iblockdata.getBlock().getMetaFromState(iblockdata);
            this.world.markChunkDirty(this.pos, this);
            if (this.getBlockType() != Blocks.AIR) {
                this.world.updateComparatorOutputLevel(this.pos, this.getBlockType());
            }
        }

    }

    public BlockPos getPos() {
        return this.pos;
    }

    public Block getBlockType() {
        if (this.blockType == null && this.world != null) {
            this.blockType = this.world.getBlockState(this.pos).getBlock();
        }

        return this.blockType;
    }

    @Nullable
    public SPacketUpdateTileEntity getUpdatePacket() {
        return null;
    }

    public NBTTagCompound getUpdateTag() {
        return this.writeInternal(new NBTTagCompound());
    }

    public boolean isInvalid() {
        return this.tileEntityInvalid;
    }

    public void invalidate() {
        this.tileEntityInvalid = true;
    }

    public void validate() {
        this.tileEntityInvalid = false;
    }

    public boolean receiveClientEvent(int i, int j) {
        return false;
    }

    public void updateContainingBlockInfo() {
        this.blockType = null;
        this.blockMetadata = -1;
    }

    public void addInfoToCrashReport(CrashReportCategory crashreportsystemdetails) {
        crashreportsystemdetails.addDetail("Name", new ICrashReportDetail() {
            public String a() throws Exception {
                return TileEntity.REGISTRY.getNameForObject(TileEntity.this.getClass()) + " // " + TileEntity.this.getClass().getCanonicalName();
            }

            public Object call() throws Exception {
                return this.a();
            }
        });
        if (this.world != null) {
            // Paper start - Prevent TileEntity and Entity crashes
            Block block = this.getBlockType();
            if (block != null) {
                CrashReportCategory.addBlockInfo(crashreportsystemdetails, this.pos, this.getBlockType(), this.getBlockMetadata());
            }
            // Paper end
            crashreportsystemdetails.addDetail("Actual block type", new ICrashReportDetail() {
                public String a() throws Exception {
                    int i = Block.getIdFromBlock(TileEntity.this.world.getBlockState(TileEntity.this.pos).getBlock());

                    try {
                        return String.format("ID #%d (%s // %s)", new Object[] { Integer.valueOf(i), Block.getBlockById(i).getUnlocalizedName(), Block.getBlockById(i).getClass().getCanonicalName()});
                    } catch (Throwable throwable) {
                        return "ID #" + i;
                    }
                }

                public Object call() throws Exception {
                    return this.a();
                }
            });
            crashreportsystemdetails.addDetail("Actual block data value", new ICrashReportDetail() {
                public String a() throws Exception {
                    IBlockState iblockdata = TileEntity.this.world.getBlockState(TileEntity.this.pos);
                    int i = iblockdata.getBlock().getMetaFromState(iblockdata);

                    if (i < 0) {
                        return "Unknown? (Got " + i + ")";
                    } else {
                        String s = String.format("%4s", new Object[] { Integer.toBinaryString(i)}).replace(" ", "0");

                        return String.format("%1$d / 0x%1$X / 0b%2$s", new Object[] { Integer.valueOf(i), s});
                    }
                }

                public Object call() throws Exception {
                    return this.a();
                }
            });
        }
    }

    public void setPos(BlockPos blockposition) {
        this.pos = blockposition.toImmutable();
    }

    public boolean onlyOpsCanSetNbt() {
        return false;
    }

    @Nullable
    public ITextComponent getDisplayName() {
        return null;
    }

    public void rotate(Rotation enumblockrotation) {}

    public void mirror(Mirror enumblockmirror) {}

    static {
        register("furnace", TileEntityFurnace.class);
        register("chest", TileEntityChest.class);
        register("ender_chest", TileEntityEnderChest.class);
        register("jukebox", BlockJukebox.TileEntityJukebox.class);
        register("dispenser", TileEntityDispenser.class);
        register("dropper", TileEntityDropper.class);
        register("sign", TileEntitySign.class);
        register("mob_spawner", TileEntityMobSpawner.class);
        register("noteblock", TileEntityNote.class);
        register("piston", TileEntityPiston.class);
        register("brewing_stand", TileEntityBrewingStand.class);
        register("enchanting_table", TileEntityEnchantmentTable.class);
        register("end_portal", TileEntityEndPortal.class);
        register("beacon", TileEntityBeacon.class);
        register("skull", TileEntitySkull.class);
        register("daylight_detector", TileEntityDaylightDetector.class);
        register("hopper", TileEntityHopper.class);
        register("comparator", TileEntityComparator.class);
        register("flower_pot", TileEntityFlowerPot.class);
        register("banner", TileEntityBanner.class);
        register("structure_block", TileEntityStructure.class);
        register("end_gateway", TileEntityEndGateway.class);
        register("command_block", TileEntityCommandBlock.class);
        register("shulker_box", TileEntityShulkerBox.class);
        register("bed", TileEntityBed.class);
    }

    // CraftBukkit start - add method
    // Paper start
    public InventoryHolder getOwner() {
        return getOwner(true);
    }
    public InventoryHolder getOwner(boolean useSnapshot) {
        // Paper end
        if (world == null) return null;
        // Spigot start
        org.bukkit.block.Block block = world.getWorld().getBlockAt(pos.getX(), pos.getY(), pos.getZ());
        if (block == null) {
            org.bukkit.Bukkit.getLogger().log(java.util.logging.Level.WARNING, "No block for owner at %s %d %d %d", new Object[]{world.getWorld(), pos.getX(), pos.getY(), pos.getZ()});
            return null;
        }
        // Spigot end
        org.bukkit.block.BlockState state = block.getState(useSnapshot); // Paper
        if (state instanceof InventoryHolder) return (InventoryHolder) state;
        return null;
    }
    // CraftBukkit end
}
