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
    public boolean isLoadingStructure = false; // Paper
    private static final Logger field_145852_a = LogManager.getLogger();
    private static final RegistryNamespaced<ResourceLocation, Class<? extends TileEntity>> field_190562_f = new RegistryNamespaced();
    public World field_145850_b;
    public BlockPos field_174879_c;
    protected boolean field_145846_f;
    private int field_145847_g;
    protected Block field_145854_h;

    public TileEntity() {
        this.field_174879_c = BlockPos.field_177992_a;
        this.field_145847_g = -1;
    }

    private static void func_190560_a(String s, Class<? extends TileEntity> oclass) {
        TileEntity.field_190562_f.func_82595_a(new ResourceLocation(s), oclass);
    }

    @Nullable
    public static ResourceLocation func_190559_a(Class<? extends TileEntity> oclass) {
        return TileEntity.field_190562_f.func_177774_c(oclass);
    }

    static boolean IGNORE_TILE_UPDATES = false; // Paper
    public World func_145831_w() {
        return this.field_145850_b;
    }

    public void func_145834_a(World world) {
        this.field_145850_b = world;
    }

    public boolean func_145830_o() {
        return this.field_145850_b != null;
    }

    public void func_145839_a(NBTTagCompound nbttagcompound) {
        this.field_174879_c = new BlockPos(nbttagcompound.func_74762_e("x"), nbttagcompound.func_74762_e("y"), nbttagcompound.func_74762_e("z"));
    }

    public NBTTagCompound func_189515_b(NBTTagCompound nbttagcompound) {
        return this.func_189516_d(nbttagcompound);
    }

    private NBTTagCompound func_189516_d(NBTTagCompound nbttagcompound) {
        ResourceLocation minecraftkey = TileEntity.field_190562_f.func_177774_c(this.getClass());

        if (minecraftkey == null) {
            throw new RuntimeException(this.getClass() + " is missing a mapping! This is a bug!");
        } else {
            nbttagcompound.func_74778_a("id", minecraftkey.toString());
            nbttagcompound.func_74768_a("x", this.field_174879_c.func_177958_n());
            nbttagcompound.func_74768_a("y", this.field_174879_c.func_177956_o());
            nbttagcompound.func_74768_a("z", this.field_174879_c.func_177952_p());
            return nbttagcompound;
        }
    }

    @Nullable
    public static TileEntity func_190200_a(World world, NBTTagCompound nbttagcompound) {
        TileEntity tileentity = null;
        String s = nbttagcompound.func_74779_i("id");

        try {
            Class oclass = TileEntity.field_190562_f.func_82594_a(new ResourceLocation(s));

            if (oclass != null) {
                tileentity = (TileEntity) oclass.newInstance();
            }
        } catch (Throwable throwable) {
            TileEntity.field_145852_a.error("Failed to create block entity {}", s, throwable);
        }

        if (tileentity != null) {
            try {
                tileentity.func_190201_b(world);
                tileentity.func_145839_a(nbttagcompound);
            } catch (Throwable throwable1) {
                TileEntity.field_145852_a.error("Failed to load data for block entity {}", s, throwable1);
                tileentity = null;
            }
        } else {
            TileEntity.field_145852_a.warn("Skipping BlockEntity with id {}", s);
        }

        return tileentity;
    }

    protected void func_190201_b(World world) {}

    public int func_145832_p() {
        if (this.field_145847_g == -1) {
            IBlockState iblockdata = this.field_145850_b.func_180495_p(this.field_174879_c);

            this.field_145847_g = iblockdata.func_177230_c().func_176201_c(iblockdata);
        }

        return this.field_145847_g;
    }

    public void func_70296_d() {
        if (this.field_145850_b != null) {
            if (IGNORE_TILE_UPDATES) return; // Paper
            IBlockState iblockdata = this.field_145850_b.func_180495_p(this.field_174879_c);

            this.field_145847_g = iblockdata.func_177230_c().func_176201_c(iblockdata);
            this.field_145850_b.func_175646_b(this.field_174879_c, this);
            if (this.func_145838_q() != Blocks.field_150350_a) {
                this.field_145850_b.func_175666_e(this.field_174879_c, this.func_145838_q());
            }
        }

    }

    public BlockPos func_174877_v() {
        return this.field_174879_c;
    }

    public Block func_145838_q() {
        if (this.field_145854_h == null && this.field_145850_b != null) {
            this.field_145854_h = this.field_145850_b.func_180495_p(this.field_174879_c).func_177230_c();
        }

        return this.field_145854_h;
    }

    @Nullable
    public SPacketUpdateTileEntity func_189518_D_() {
        return null;
    }

    public NBTTagCompound func_189517_E_() {
        return this.func_189516_d(new NBTTagCompound());
    }

    public boolean func_145837_r() {
        return this.field_145846_f;
    }

    public void func_145843_s() {
        this.field_145846_f = true;
    }

    public void func_145829_t() {
        this.field_145846_f = false;
    }

    public boolean func_145842_c(int i, int j) {
        return false;
    }

    public void func_145836_u() {
        this.field_145854_h = null;
        this.field_145847_g = -1;
    }

    public void func_145828_a(CrashReportCategory crashreportsystemdetails) {
        crashreportsystemdetails.func_189529_a("Name", new ICrashReportDetail() {
            public String a() throws Exception {
                return TileEntity.field_190562_f.func_177774_c(TileEntity.this.getClass()) + " // " + TileEntity.this.getClass().getCanonicalName();
            }

            @Override
            public Object call() throws Exception {
                return this.a();
            }
        });
        if (this.field_145850_b != null) {
            // Paper start - Prevent TileEntity and Entity crashes
            Block block = this.func_145838_q();
            if (block != null) {
                CrashReportCategory.func_180523_a(crashreportsystemdetails, this.field_174879_c, this.func_145838_q(), this.func_145832_p());
            }
            // Paper end
            crashreportsystemdetails.func_189529_a("Actual block type", new ICrashReportDetail() {
                public String a() throws Exception {
                    int i = Block.func_149682_b(TileEntity.this.field_145850_b.func_180495_p(TileEntity.this.field_174879_c).func_177230_c());

                    try {
                        return String.format("ID #%d (%s // %s)", new Object[] { Integer.valueOf(i), Block.func_149729_e(i).func_149739_a(), Block.func_149729_e(i).getClass().getCanonicalName()});
                    } catch (Throwable throwable) {
                        return "ID #" + i;
                    }
                }

                @Override
                public Object call() throws Exception {
                    return this.a();
                }
            });
            crashreportsystemdetails.func_189529_a("Actual block data value", new ICrashReportDetail() {
                public String a() throws Exception {
                    IBlockState iblockdata = TileEntity.this.field_145850_b.func_180495_p(TileEntity.this.field_174879_c);
                    int i = iblockdata.func_177230_c().func_176201_c(iblockdata);

                    if (i < 0) {
                        return "Unknown? (Got " + i + ")";
                    } else {
                        String s = String.format("%4s", new Object[] { Integer.toBinaryString(i)}).replace(" ", "0");

                        return String.format("%1$d / 0x%1$X / 0b%2$s", new Object[] { Integer.valueOf(i), s});
                    }
                }

                @Override
                public Object call() throws Exception {
                    return this.a();
                }
            });
        }
    }

    public void func_174878_a(BlockPos blockposition) {
        this.field_174879_c = blockposition.func_185334_h();
    }

    public boolean func_183000_F() {
        return false;
    }

    @Nullable
    public ITextComponent func_145748_c_() {
        return null;
    }

    public void func_189667_a(Rotation enumblockrotation) {}

    public void func_189668_a(Mirror enumblockmirror) {}

    static {
        func_190560_a("furnace", TileEntityFurnace.class);
        func_190560_a("chest", TileEntityChest.class);
        func_190560_a("ender_chest", TileEntityEnderChest.class);
        func_190560_a("jukebox", BlockJukebox.TileEntityJukebox.class);
        func_190560_a("dispenser", TileEntityDispenser.class);
        func_190560_a("dropper", TileEntityDropper.class);
        func_190560_a("sign", TileEntitySign.class);
        func_190560_a("mob_spawner", TileEntityMobSpawner.class);
        func_190560_a("noteblock", TileEntityNote.class);
        func_190560_a("piston", TileEntityPiston.class);
        func_190560_a("brewing_stand", TileEntityBrewingStand.class);
        func_190560_a("enchanting_table", TileEntityEnchantmentTable.class);
        func_190560_a("end_portal", TileEntityEndPortal.class);
        func_190560_a("beacon", TileEntityBeacon.class);
        func_190560_a("skull", TileEntitySkull.class);
        func_190560_a("daylight_detector", TileEntityDaylightDetector.class);
        func_190560_a("hopper", TileEntityHopper.class);
        func_190560_a("comparator", TileEntityComparator.class);
        func_190560_a("flower_pot", TileEntityFlowerPot.class);
        func_190560_a("banner", TileEntityBanner.class);
        func_190560_a("structure_block", TileEntityStructure.class);
        func_190560_a("end_gateway", TileEntityEndGateway.class);
        func_190560_a("command_block", TileEntityCommandBlock.class);
        func_190560_a("shulker_box", TileEntityShulkerBox.class);
        func_190560_a("bed", TileEntityBed.class);
    }

    // CraftBukkit start - add method
    // Paper start
    public InventoryHolder getOwner() {
        return getOwner(true);
    }
    public InventoryHolder getOwner(boolean useSnapshot) {
        // Paper end
        if (field_145850_b == null) return null;
        // Spigot start
        org.bukkit.block.Block block = field_145850_b.getWorld().getBlockAt(field_174879_c.func_177958_n(), field_174879_c.func_177956_o(), field_174879_c.func_177952_p());
        if (block == null) {
            org.bukkit.Bukkit.getLogger().log(java.util.logging.Level.WARNING, "No block for owner at %s %d %d %d", new Object[]{field_145850_b.getWorld(), field_174879_c.func_177958_n(), field_174879_c.func_177956_o(), field_174879_c.func_177952_p()});
            return null;
        }
        // Spigot end
        org.bukkit.block.BlockState state = block.getState(useSnapshot); // Paper
        if (state instanceof InventoryHolder) return (InventoryHolder) state;
        return null;
    }
    // CraftBukkit end
}
