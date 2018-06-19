package net.minecraft.block;

import java.util.EnumSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.bukkit.block.BlockFace;
import org.bukkit.event.block.BlockFromToEvent;

// CraftBukkit start
import org.bukkit.block.BlockFace;
import org.bukkit.event.block.BlockFromToEvent;
// CraftBukkit end

public class BlockDynamicLiquid extends BlockLiquid {

    int field_149815_a;

    protected BlockDynamicLiquid(Material material) {
        super(material);
    }

    private void func_180690_f(World world, BlockPos blockposition, IBlockState iblockdata) {
        world.func_180501_a(blockposition, func_176363_b(this.field_149764_J).func_176223_P().func_177226_a(BlockDynamicLiquid.field_176367_b, iblockdata.func_177229_b(BlockDynamicLiquid.field_176367_b)), 2);
    }

    public void func_180650_b(World world, BlockPos blockposition, IBlockState iblockdata, Random random) {
        int i = ((Integer) iblockdata.func_177229_b(BlockDynamicLiquid.field_176367_b)).intValue();
        byte b0 = 1;

        if (this.field_149764_J == Material.field_151587_i && !world.field_73011_w.func_177500_n()) {
            b0 = 2;
        }

        int j = this.getFlowSpeed(world, blockposition); // Paper
        int k;

        if (i > 0) {
            int l = -100;

            this.field_149815_a = 0;

            EnumFacing enumdirection;

            for (Iterator iterator = EnumFacing.Plane.HORIZONTAL.iterator(); iterator.hasNext(); l = this.func_176371_a(world, blockposition.func_177972_a(enumdirection), l)) {
                enumdirection = (EnumFacing) iterator.next();
            }

            int i1 = l + b0;

            if (i1 >= 8 || l < 0) {
                i1 = -1;
            }

            k = this.func_189542_i(world.func_180495_p(blockposition.func_177984_a()));
            if (k >= 0) {
                if (k >= 8) {
                    i1 = k;
                } else {
                    i1 = k + 8;
                }
            }

            if (this.field_149815_a >= 2 && this.field_149764_J == Material.field_151586_h) {
                IBlockState iblockdata1 = world.func_180495_p(blockposition.func_177977_b());

                if (iblockdata1.func_185904_a().func_76220_a()) {
                    i1 = 0;
                } else if (iblockdata1.func_185904_a() == this.field_149764_J && ((Integer) iblockdata1.func_177229_b(BlockDynamicLiquid.field_176367_b)).intValue() == 0) {
                    i1 = 0;
                }
            }

            if (!world.paperConfig.fastDrainLava && this.field_149764_J == Material.field_151587_i && i < 8 && i1 < 8 && i1 > i && random.nextInt(4) != 0) { // Paper
                j *= 4;
            }

            if (i1 == i) {
                this.func_180690_f(world, blockposition, iblockdata);
            } else {
                i = i1;
                if (i1 < 0 || canFastDrain(world, blockposition)) { // Paper - Fast draining
                    world.func_175698_g(blockposition);
                } else {
                    iblockdata = iblockdata.func_177226_a(BlockDynamicLiquid.field_176367_b, Integer.valueOf(i1));
                    world.func_180501_a(blockposition, iblockdata, 2);
                    world.func_175684_a(blockposition, (Block) this, j);
                    world.func_175685_c(blockposition, this, false);
                }
            }
        } else {
            this.func_180690_f(world, blockposition, iblockdata);
        }

        if (world.func_180495_p(blockposition).func_177230_c().func_176223_P().func_185904_a() != field_149764_J) return; // Paper - Stop updating flowing block if material has changed
        org.bukkit.block.Block source = world.getWorld().getBlockAt(blockposition.func_177958_n(), blockposition.func_177956_o(), blockposition.func_177952_p()); // CraftBukkit
        IBlockState iblockdata2 = world.func_180495_p(blockposition.func_177977_b());

        if (this.func_176373_h(world, blockposition.func_177977_b(), iblockdata2)) {
            // CraftBukkit start
            if (!canFlowTo(world, source, BlockFace.DOWN)) { return; } // Paper
            BlockFromToEvent event = new BlockFromToEvent(source, BlockFace.DOWN);
            world.getServer().getPluginManager().callEvent(event);

            if (event.isCancelled()) {
                return;
            }
            // CraftBukkit end

            if (this.field_149764_J == Material.field_151587_i && world.func_180495_p(blockposition.func_177977_b()).func_185904_a() == Material.field_151586_h) {
                // CraftBukkit start
                if (org.bukkit.craftbukkit.event.CraftEventFactory.handleBlockFormEvent(world, blockposition.func_177977_b(), Blocks.field_150348_b.func_176223_P(), null)) {
                    this.func_180688_d(world, blockposition.func_177977_b());
                }
                // CraftBukkit end
                return;
            }

            if (i >= 8) {
                this.func_176375_a(world, blockposition.func_177977_b(), iblockdata2, i);
            } else {
                this.func_176375_a(world, blockposition.func_177977_b(), iblockdata2, i + 8);
            }
        } else if (i >= 0 && (i == 0 || this.func_176372_g(world, blockposition.func_177977_b(), iblockdata2))) {
            Set set = this.func_176376_e(world, blockposition);

            k = i + b0;
            if (i >= 8) {
                k = 1;
            }

            if (k >= 8) {
                return;
            }

            Iterator iterator1 = set.iterator();

            while (iterator1.hasNext()) {
                EnumFacing enumdirection1 = (EnumFacing) iterator1.next();

                // CraftBukkit start
                if (!canFlowTo(world, source, org.bukkit.craftbukkit.block.CraftBlock.notchToBlockFace(enumdirection1))) { continue; } // Paper
                BlockFromToEvent event = new BlockFromToEvent(source, org.bukkit.craftbukkit.block.CraftBlock.notchToBlockFace(enumdirection1));
                world.getServer().getPluginManager().callEvent(event);

                if (!event.isCancelled()) {
                    this.func_176375_a(world, blockposition.func_177972_a(enumdirection1), world.func_180495_p(blockposition.func_177972_a(enumdirection1)), k);
                }
                // CraftBukkit end
            }
        }

    }

    // Paper start
    private boolean canFlowTo(World world, org.bukkit.block.Block source, BlockFace face) {
        return source.getWorld().isChunkLoaded((source.getX() + face.getModX()) >> 4, (source.getZ() + face.getModZ()) >> 4);
    }
    // Paper end

    private void func_176375_a(World world, BlockPos blockposition, IBlockState iblockdata, int i) {
        if (/*world.isLoaded(blockposition) &&*/ this.func_176373_h(world, blockposition, iblockdata)) { // CraftBukkit - add isLoaded check // Paper - Already checked before we get here for isLoaded
            if (iblockdata.func_185904_a() != Material.field_151579_a) {
                if (this.field_149764_J == Material.field_151587_i) {
                    this.func_180688_d(world, blockposition);
                } else {
                    iblockdata.func_177230_c().func_176226_b(world, blockposition, iblockdata, 0);
                }
            }

            world.func_180501_a(blockposition, this.func_176223_P().func_177226_a(BlockDynamicLiquid.field_176367_b, Integer.valueOf(i)), 3);
        }

    }

    private int func_176374_a(World world, BlockPos blockposition, int i, EnumFacing enumdirection) {
        int j = 1000;
        Iterator iterator = EnumFacing.Plane.HORIZONTAL.iterator();

        while (iterator.hasNext()) {
            EnumFacing enumdirection1 = (EnumFacing) iterator.next();

            if (enumdirection1 != enumdirection) {
                BlockPos blockposition1 = blockposition.func_177972_a(enumdirection1);
                IBlockState iblockdata = world.func_180495_p(blockposition1);

                if (!this.func_176372_g(world, blockposition1, iblockdata) && (iblockdata.func_185904_a() != this.field_149764_J || ((Integer) iblockdata.func_177229_b(BlockDynamicLiquid.field_176367_b)).intValue() > 0)) {
                    if (!this.func_176372_g(world, blockposition1.func_177977_b(), iblockdata)) {
                        return i;
                    }

                    if (i < this.func_185698_b(world)) {
                        int k = this.func_176374_a(world, blockposition1, i + 1, enumdirection1.func_176734_d());

                        if (k < j) {
                            j = k;
                        }
                    }
                }
            }
        }

        return j;
    }

    private int func_185698_b(World world) {
        return this.field_149764_J == Material.field_151587_i && !world.field_73011_w.func_177500_n() ? 2 : 4;
    }

    private Set<EnumFacing> func_176376_e(World world, BlockPos blockposition) {
        int i = 1000;
        EnumSet enumset = EnumSet.noneOf(EnumFacing.class);
        Iterator iterator = EnumFacing.Plane.HORIZONTAL.iterator();

        while (iterator.hasNext()) {
            EnumFacing enumdirection = (EnumFacing) iterator.next();
            BlockPos blockposition1 = blockposition.func_177972_a(enumdirection);
            IBlockState iblockdata = world.func_180495_p(blockposition1);

            if (!this.func_176372_g(world, blockposition1, iblockdata) && (iblockdata.func_185904_a() != this.field_149764_J || ((Integer) iblockdata.func_177229_b(BlockDynamicLiquid.field_176367_b)).intValue() > 0)) {
                int j;

                if (this.func_176372_g(world, blockposition1.func_177977_b(), world.func_180495_p(blockposition1.func_177977_b()))) {
                    j = this.func_176374_a(world, blockposition1, 1, enumdirection.func_176734_d());
                } else {
                    j = 0;
                }

                if (j < i) {
                    enumset.clear();
                }

                if (j <= i) {
                    enumset.add(enumdirection);
                    i = j;
                }
            }
        }

        return enumset;
    }

    private boolean func_176372_g(World world, BlockPos blockposition, IBlockState iblockdata) {
        Block block = world.func_180495_p(blockposition).func_177230_c();

        return !(block instanceof BlockDoor) && block != Blocks.field_150472_an && block != Blocks.field_150468_ap && block != Blocks.field_150436_aH ? (block.field_149764_J != Material.field_151567_E && block.field_149764_J != Material.field_189963_J ? block.field_149764_J.func_76230_c() : true) : true;
    }

    protected int func_176371_a(World world, BlockPos blockposition, int i) {
        int j = this.func_189542_i(world.func_180495_p(blockposition));

        if (j < 0) {
            return i;
        } else {
            if (j == 0) {
                ++this.field_149815_a;
            }

            if (j >= 8) {
                j = 0;
            }

            return i >= 0 && j >= i ? i : j;
        }
    }

    private boolean func_176373_h(World world, BlockPos blockposition, IBlockState iblockdata) {
        Material material = iblockdata.func_185904_a();

        return material != this.field_149764_J && material != Material.field_151587_i && !this.func_176372_g(world, blockposition, iblockdata);
    }

    public void func_176213_c(World world, BlockPos blockposition, IBlockState iblockdata) {
        if (!this.func_176365_e(world, blockposition, iblockdata)) {
            world.func_175684_a(blockposition, (Block) this, this.getFlowSpeed(world, blockposition)); // Paper
        }

    }

    // Paper start
    /**
     * Paper - Get flow speed. Throttle if its water and flowing adjacent to lava
     */
    public int getFlowSpeed(World world, BlockPos blockposition) {
        if (this.field_149764_J == Material.field_151587_i) {
            return world.field_73011_w.isSkyMissing() ? world.paperConfig.lavaFlowSpeedNether : world.paperConfig.lavaFlowSpeedNormal;
        }
        if (this.field_149764_J == Material.field_151586_h && (
                world.func_180495_p(blockposition.func_177964_d(1)).func_177230_c().field_149764_J == Material.field_151587_i ||
                        world.func_180495_p(blockposition.func_177970_e(1)).func_177230_c().field_149764_J == Material.field_151587_i ||
                        world.func_180495_p(blockposition.func_177985_f(1)).func_177230_c().field_149764_J == Material.field_151587_i ||
                        world.func_180495_p(blockposition.func_177965_g(1)).func_177230_c().field_149764_J == Material.field_151587_i)) {
            return world.paperConfig.waterOverLavaFlowSpeed;
        }
        return super.func_149738_a(world);
    }

    private int getFluidLevel(IBlockAccess iblockaccess, BlockPos blockposition) {
        return iblockaccess.func_180495_p(blockposition).func_185904_a() == this.field_149764_J ? iblockaccess.func_180495_p(blockposition).func_177229_b(BlockLiquid.field_176367_b) : -1;
    }

    /**
     * Paper - Data check method for fast draining
     */
    public int getData(World world, BlockPos position) {
        int data = this.getFluidLevel((IBlockAccess) world, position);
        return data < 8 ? data : 0;
    }

    /**
     * Paper - Checks surrounding blocks to determine if block can be fast drained
     */
    public boolean canFastDrain(World world, BlockPos position) {
        boolean result = false;
        int data = getData(world, position);
        if (this.field_149764_J == Material.field_151586_h) {
            if (world.paperConfig.fastDrainWater) {
                result = true;
                if (getData(world, position.func_177977_b()) < 0) {
                    result = false;
                } else if (world.func_180495_p(position.func_177978_c()).func_177230_c().func_176223_P().func_185904_a() == Material.field_151586_h && getData(world, position.func_177978_c()) < data) {
                    result = false;
                } else if (world.func_180495_p(position.func_177968_d()).func_177230_c().func_176223_P().func_185904_a() == Material.field_151586_h && getData(world, position.func_177968_d()) < data) {
                    result = false;
                } else if (world.func_180495_p(position.func_177976_e()).func_177230_c().func_176223_P().func_185904_a() == Material.field_151586_h && getData(world, position.func_177976_e()) < data) {
                    result = false;
                } else if (world.func_180495_p(position.func_177974_f()).func_177230_c().func_176223_P().func_185904_a() == Material.field_151586_h && getData(world, position.func_177974_f()) < data) {
                    result = false;
                }
            }
        } else if (this.field_149764_J == Material.field_151587_i) {
            if (world.paperConfig.fastDrainLava) {
                result = true;
                if (getData(world, position.func_177977_b()) < 0 || world.func_180495_p(position.func_177984_a()).func_177230_c().func_176223_P().func_185904_a() != Material.field_151579_a) {
                    result = false;
                } else if (world.func_180495_p(position.func_177978_c()).func_177230_c().func_176223_P().func_185904_a() == Material.field_151587_i && getData(world, position.func_177978_c()) < data) {
                    result = false;
                } else if (world.func_180495_p(position.func_177968_d()).func_177230_c().func_176223_P().func_185904_a() == Material.field_151587_i && getData(world, position.func_177968_d()) < data) {
                    result = false;
                } else if (world.func_180495_p(position.func_177976_e()).func_177230_c().func_176223_P().func_185904_a() == Material.field_151587_i && getData(world, position.func_177976_e()) < data) {
                    result = false;
                } else if (world.func_180495_p(position.func_177974_f()).func_177230_c().func_176223_P().func_185904_a() == Material.field_151587_i && getData(world, position.func_177974_f()) < data) {
                    result = false;
                }
            }
        }
        return result;
    }
    // Paper end
}
