package net.minecraft.block;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.bukkit.block.BlockState;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockSpreadEvent;

// CraftBukkit start
import org.bukkit.block.BlockState;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.block.BlockFadeEvent;
// CraftBukkit end

public class BlockGrass extends Block implements IGrowable {

    public static final PropertyBool field_176498_a = PropertyBool.func_177716_a("snowy");

    protected BlockGrass() {
        super(Material.field_151577_b);
        this.func_180632_j(this.field_176227_L.func_177621_b().func_177226_a(BlockGrass.field_176498_a, Boolean.valueOf(false)));
        this.func_149675_a(true);
        this.func_149647_a(CreativeTabs.field_78030_b);
    }

    public IBlockState func_176221_a(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        Block block = iblockaccess.func_180495_p(blockposition.func_177984_a()).func_177230_c();

        return iblockdata.func_177226_a(BlockGrass.field_176498_a, Boolean.valueOf(block == Blocks.field_150433_aE || block == Blocks.field_150431_aC));
    }

    public void func_180650_b(World world, BlockPos blockposition, IBlockState iblockdata, Random random) {
        if (world.paperConfig.grassUpdateRate != 1 && (world.paperConfig.grassUpdateRate < 1 || (MinecraftServer.currentTick + blockposition.hashCode()) % world.paperConfig.grassUpdateRate != 0)) { return; } // Paper
        if (!world.field_72995_K) {
            int lightLevel = -1; // Paper
            if (world.func_180495_p(blockposition.func_177984_a()).func_185891_c() > 2 && (lightLevel = world.func_175671_l(blockposition.func_177984_a())) < 4) { // Paper - move light check to end to avoid unneeded light lookups
                // CraftBukkit start
                // world.setTypeUpdate(blockposition, Blocks.DIRT.getBlockData());
                org.bukkit.World bworld = world.getWorld();
                BlockState blockState = bworld.getBlockAt(blockposition.func_177958_n(), blockposition.func_177956_o(), blockposition.func_177952_p()).getState();
                blockState.setType(CraftMagicNumbers.getMaterial(Blocks.field_150346_d));

                BlockFadeEvent event = new BlockFadeEvent(blockState.getBlock(), blockState);
                world.getServer().getPluginManager().callEvent(event);

                if (!event.isCancelled()) {
                    blockState.update(true);
                }
                // CraftBukkit end
            } else {
                // Paper start
                // If light was calculated above, reuse it, else grab it
                if (lightLevel == -1) {
                    lightLevel = world.func_175671_l(blockposition.func_177984_a());
                }
                if (lightLevel >= 9) {
                    // Paper end
                    for (int i = 0; i < 4; ++i) {
                        BlockPos blockposition1 = blockposition.func_177982_a(random.nextInt(3) - 1, random.nextInt(5) - 3, random.nextInt(3) - 1);

                        IBlockState iblockdata2 = world.getTypeIfLoaded(blockposition1); // Paper - moved up
                        if (iblockdata2 == null) { // Paper
                            return;
                        }

                        IBlockState iblockdata1 = world.func_180495_p(blockposition1.func_177984_a());
                        //IBlockData iblockdata2 = world.getTypeIfLoaded(blockposition1); // Paper - moved up

                        if (iblockdata2.func_177230_c() == Blocks.field_150346_d && iblockdata2.func_177229_b(BlockDirt.field_176386_a) == BlockDirt.DirtType.DIRT && iblockdata1.func_185891_c() <= 2 && world.isLightLevel(blockposition1.func_177984_a(), 4)) { // Paper - move last check before isLightLevel to avoid unneeded light checks
                            // CraftBukkit start
                            // world.setTypeUpdate(blockposition1, Blocks.GRASS.getBlockData());
                            org.bukkit.World bworld = world.getWorld();
                            BlockState blockState = bworld.getBlockAt(blockposition1.func_177958_n(), blockposition1.func_177956_o(), blockposition1.func_177952_p()).getState();
                            blockState.setType(CraftMagicNumbers.getMaterial(Blocks.field_150349_c));

                            BlockSpreadEvent event = new BlockSpreadEvent(blockState.getBlock(), bworld.getBlockAt(blockposition.func_177958_n(), blockposition.func_177956_o(), blockposition.func_177952_p()), blockState);
                            world.getServer().getPluginManager().callEvent(event);

                            if (!event.isCancelled()) {
                                blockState.update(true);
                            }
                            // CraftBukkit end
                        }
                    }
                }

            }
        }
    }

    public Item func_180660_a(IBlockState iblockdata, Random random, int i) {
        return Blocks.field_150346_d.func_180660_a(Blocks.field_150346_d.func_176223_P().func_177226_a(BlockDirt.field_176386_a, BlockDirt.DirtType.DIRT), random, i);
    }

    public boolean func_176473_a(World world, BlockPos blockposition, IBlockState iblockdata, boolean flag) {
        return true;
    }

    public boolean func_180670_a(World world, Random random, BlockPos blockposition, IBlockState iblockdata) {
        return true;
    }

    public void func_176474_b(World world, Random random, BlockPos blockposition, IBlockState iblockdata) {
        BlockPos blockposition1 = blockposition.func_177984_a();
        int i = 0;

        while (i < 128) {
            BlockPos blockposition2 = blockposition1;
            int j = 0;

            while (true) {
                if (j < i / 16) {
                    blockposition2 = blockposition2.func_177982_a(random.nextInt(3) - 1, (random.nextInt(3) - 1) * random.nextInt(3) / 2, random.nextInt(3) - 1);
                    if (world.func_180495_p(blockposition2.func_177977_b()).func_177230_c() == Blocks.field_150349_c && !world.func_180495_p(blockposition2).func_185915_l()) {
                        ++j;
                        continue;
                    }
                } else if (world.func_180495_p(blockposition2).func_177230_c().field_149764_J == Material.field_151579_a) {
                    if (random.nextInt(8) == 0) {
                        BlockFlower.EnumFlowerType blockflowers_enumflowervarient = world.func_180494_b(blockposition2).func_180623_a(random, blockposition2);
                        BlockFlower blockflowers = blockflowers_enumflowervarient.func_176964_a().func_180346_a();
                        IBlockState iblockdata1 = blockflowers.func_176223_P().func_177226_a(blockflowers.func_176494_l(), blockflowers_enumflowervarient);

                        if (blockflowers.func_180671_f(world, blockposition2, iblockdata1)) {
                            // world.setTypeAndData(blockposition2, iblockdata1, 3); // CraftBukkit
                            CraftEventFactory.handleBlockGrowEvent(world, blockposition2.func_177958_n(), blockposition2.func_177956_o(), blockposition2.func_177952_p(), iblockdata1.func_177230_c(), iblockdata1.func_177230_c().func_176201_c(iblockdata1)); // CraftBukkit
                        }
                    } else {
                        IBlockState iblockdata2 = Blocks.field_150329_H.func_176223_P().func_177226_a(BlockTallGrass.field_176497_a, BlockTallGrass.EnumType.GRASS);

                        if (Blocks.field_150329_H.func_180671_f(world, blockposition2, iblockdata2)) {
                            // world.setTypeAndData(blockposition2, iblockdata2, 3); // CraftBukkit
                            CraftEventFactory.handleBlockGrowEvent(world, blockposition2.func_177958_n(), blockposition2.func_177956_o(), blockposition2.func_177952_p(), iblockdata2.func_177230_c(), iblockdata2.func_177230_c().func_176201_c(iblockdata2)); // CraftBukkit
                        }
                    }
                }

                ++i;
                break;
            }
        }

    }

    public int func_176201_c(IBlockState iblockdata) {
        return 0;
    }

    protected BlockStateContainer func_180661_e() {
        return new BlockStateContainer(this, new IProperty[] { BlockGrass.field_176498_a});
    }
}
