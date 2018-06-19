package net.minecraft.block;

import java.util.Random;

import java.util.List;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenBigTree;
import net.minecraft.world.gen.feature.WorldGenBirchTree;
import net.minecraft.world.gen.feature.WorldGenCanopyTree;
import net.minecraft.world.gen.feature.WorldGenMegaJungle;
import net.minecraft.world.gen.feature.WorldGenMegaPineTree;
import net.minecraft.world.gen.feature.WorldGenSavannaTree;
import net.minecraft.world.gen.feature.WorldGenTaiga2;
import net.minecraft.world.gen.feature.WorldGenTrees;
import net.minecraft.world.gen.feature.WorldGenerator;
import org.bukkit.Location;
import org.bukkit.TreeType;
import org.bukkit.block.BlockState;
import org.bukkit.event.world.StructureGrowEvent;

// CraftBukkit start
import java.util.List;

import org.bukkit.Location;
import org.bukkit.TreeType;
import org.bukkit.block.BlockState;
import org.bukkit.event.world.StructureGrowEvent;
// CraftBukkit end

public class BlockSapling extends BlockBush implements IGrowable {

    public static final PropertyEnum<BlockPlanks.EnumType> field_176480_a = PropertyEnum.func_177709_a("type", BlockPlanks.EnumType.class);
    public static final PropertyInteger field_176479_b = PropertyInteger.func_177719_a("stage", 0, 1);
    protected static final AxisAlignedBB field_185520_d = new AxisAlignedBB(0.09999999403953552D, 0.0D, 0.09999999403953552D, 0.8999999761581421D, 0.800000011920929D, 0.8999999761581421D);
    public static TreeType treeType; // CraftBukkit

    protected BlockSapling() {
        this.func_180632_j(this.field_176227_L.func_177621_b().func_177226_a(BlockSapling.field_176480_a, BlockPlanks.EnumType.OAK).func_177226_a(BlockSapling.field_176479_b, Integer.valueOf(0)));
        this.func_149647_a(CreativeTabs.field_78031_c);
    }

    public AxisAlignedBB func_185496_a(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        return BlockSapling.field_185520_d;
    }

    public String func_149732_F() {
        return I18n.func_74838_a(this.func_149739_a() + "." + BlockPlanks.EnumType.OAK.func_176840_c() + ".name");
    }

    public void func_180650_b(World world, BlockPos blockposition, IBlockState iblockdata, Random random) {
        if (!world.field_72995_K) {
            super.func_180650_b(world, blockposition, iblockdata, random);
            if (world.isLightLevel(blockposition.func_177984_a(), 9) && random.nextInt(Math.max(2, (int) (((100.0F / world.spigotConfig.saplingModifier) * 7) + 0.5F))) == 0) { // Spigot // Paper
                // CraftBukkit start
                world.captureTreeGeneration = true;
                // CraftBukkit end
                this.func_176478_d(world, blockposition, iblockdata, random);
                // CraftBukkit start
                world.captureTreeGeneration = false;
                if (world.capturedBlockStates.size() > 0) {
                    TreeType treeType = BlockSapling.treeType;
                    BlockSapling.treeType = null;
                    Location location = new Location(world.getWorld(), blockposition.func_177958_n(), blockposition.func_177956_o(), blockposition.func_177952_p());
                    List<BlockState> blocks = (List<BlockState>) world.capturedBlockStates.clone();
                    world.capturedBlockStates.clear();
                    StructureGrowEvent event = null;
                    if (treeType != null) {
                        event = new StructureGrowEvent(location, treeType, false, null, blocks);
                        org.bukkit.Bukkit.getPluginManager().callEvent(event);
                    }
                    if (event == null || !event.isCancelled()) {
                        for (BlockState blockstate : blocks) {
                            blockstate.update(true);
                        }
                    }
                }
                // CraftBukkit end
            }

        }
    }

    public void func_176478_d(World world, BlockPos blockposition, IBlockState iblockdata, Random random) {
        if (((Integer) iblockdata.func_177229_b(BlockSapling.field_176479_b)).intValue() == 0) {
            world.func_180501_a(blockposition, iblockdata.func_177231_a((IProperty) BlockSapling.field_176479_b), 4);
        } else {
            this.func_176476_e(world, blockposition, iblockdata, random);
        }

    }

    public void func_176476_e(World world, BlockPos blockposition, IBlockState iblockdata, Random random) {
        // CraftBukkit start - Turn ternary operator into if statement to set treeType
        // Object object = random.nextInt(10) == 0 ? new WorldGenBigTree(true) : new WorldGenTrees(true);
        Object object;
        if (random.nextInt(10) == 0) {
            treeType = TreeType.BIG_TREE;
            object = new WorldGenBigTree(true);
        } else {
            treeType = TreeType.TREE;
            object = new WorldGenTrees(true);
        }
        // CraftBukkit end
        int i = 0;
        int j = 0;
        boolean flag = false;
        IBlockState iblockdata1;

        switch ((BlockPlanks.EnumType) iblockdata.func_177229_b(BlockSapling.field_176480_a)) {
        case SPRUCE:
            label66:
            for (i = 0; i >= -1; --i) {
                for (j = 0; j >= -1; --j) {
                    if (this.func_181624_a(world, blockposition, i, j, BlockPlanks.EnumType.SPRUCE)) {
                        treeType = TreeType.MEGA_REDWOOD; // CraftBukkit
                        object = new WorldGenMegaPineTree(false, random.nextBoolean());
                        flag = true;
                        break label66;
                    }
                }
            }

            if (!flag) {
                i = 0;
                j = 0;
                treeType = TreeType.REDWOOD; // CraftBukkit
                object = new WorldGenTaiga2(true);
            }
            break;

        case BIRCH:
            treeType = TreeType.BIRCH; // CraftBukkit
            object = new WorldGenBirchTree(true, false);
            break;

        case JUNGLE:
            iblockdata1 = Blocks.field_150364_r.func_176223_P().func_177226_a(BlockOldLog.field_176301_b, BlockPlanks.EnumType.JUNGLE);
            IBlockState iblockdata2 = Blocks.field_150362_t.func_176223_P().func_177226_a(BlockOldLeaf.field_176239_P, BlockPlanks.EnumType.JUNGLE).func_177226_a(BlockLeaves.field_176236_b, Boolean.valueOf(false));

            label78:
            for (i = 0; i >= -1; --i) {
                for (j = 0; j >= -1; --j) {
                    if (this.func_181624_a(world, blockposition, i, j, BlockPlanks.EnumType.JUNGLE)) {
                        treeType = TreeType.JUNGLE; // CraftBukkit
                        object = new WorldGenMegaJungle(true, 10, 20, iblockdata1, iblockdata2);
                        flag = true;
                        break label78;
                    }
                }
            }

            if (!flag) {
                i = 0;
                j = 0;
                treeType = TreeType.SMALL_JUNGLE; // CraftBukkit
                object = new WorldGenTrees(true, 4 + random.nextInt(7), iblockdata1, iblockdata2, false);
            }
            break;

        case ACACIA:
            treeType = TreeType.ACACIA; // CraftBukkit
            object = new WorldGenSavannaTree(true);
            break;

        case DARK_OAK:
            label90:
            for (i = 0; i >= -1; --i) {
                for (j = 0; j >= -1; --j) {
                    if (this.func_181624_a(world, blockposition, i, j, BlockPlanks.EnumType.DARK_OAK)) {
                        treeType = TreeType.DARK_OAK; // CraftBukkit
                        object = new WorldGenCanopyTree(true);
                        flag = true;
                        break label90;
                    }
                }
            }

            if (!flag) {
                return;
            }

        case OAK:
        }

        iblockdata1 = Blocks.field_150350_a.func_176223_P();
        if (flag) {
            world.func_180501_a(blockposition.func_177982_a(i, 0, j), iblockdata1, 4);
            world.func_180501_a(blockposition.func_177982_a(i + 1, 0, j), iblockdata1, 4);
            world.func_180501_a(blockposition.func_177982_a(i, 0, j + 1), iblockdata1, 4);
            world.func_180501_a(blockposition.func_177982_a(i + 1, 0, j + 1), iblockdata1, 4);
        } else {
            world.func_180501_a(blockposition, iblockdata1, 4);
        }

        if (!((WorldGenerator) object).func_180709_b(world, random, blockposition.func_177982_a(i, 0, j))) {
            if (flag) {
                world.func_180501_a(blockposition.func_177982_a(i, 0, j), iblockdata, 4);
                world.func_180501_a(blockposition.func_177982_a(i + 1, 0, j), iblockdata, 4);
                world.func_180501_a(blockposition.func_177982_a(i, 0, j + 1), iblockdata, 4);
                world.func_180501_a(blockposition.func_177982_a(i + 1, 0, j + 1), iblockdata, 4);
            } else {
                world.func_180501_a(blockposition, iblockdata, 4);
            }
        }

    }

    private boolean func_181624_a(World world, BlockPos blockposition, int i, int j, BlockPlanks.EnumType blockwood_enumlogvariant) {
        return this.func_176477_a(world, blockposition.func_177982_a(i, 0, j), blockwood_enumlogvariant) && this.func_176477_a(world, blockposition.func_177982_a(i + 1, 0, j), blockwood_enumlogvariant) && this.func_176477_a(world, blockposition.func_177982_a(i, 0, j + 1), blockwood_enumlogvariant) && this.func_176477_a(world, blockposition.func_177982_a(i + 1, 0, j + 1), blockwood_enumlogvariant);
    }

    public boolean func_176477_a(World world, BlockPos blockposition, BlockPlanks.EnumType blockwood_enumlogvariant) {
        IBlockState iblockdata = world.func_180495_p(blockposition);

        return iblockdata.func_177230_c() == this && iblockdata.func_177229_b(BlockSapling.field_176480_a) == blockwood_enumlogvariant;
    }

    public int func_180651_a(IBlockState iblockdata) {
        return ((BlockPlanks.EnumType) iblockdata.func_177229_b(BlockSapling.field_176480_a)).func_176839_a();
    }

    public void func_149666_a(CreativeTabs creativemodetab, NonNullList<ItemStack> nonnulllist) {
        BlockPlanks.EnumType[] ablockwood_enumlogvariant = BlockPlanks.EnumType.values();
        int i = ablockwood_enumlogvariant.length;

        for (int j = 0; j < i; ++j) {
            BlockPlanks.EnumType blockwood_enumlogvariant = ablockwood_enumlogvariant[j];

            nonnulllist.add(new ItemStack(this, 1, blockwood_enumlogvariant.func_176839_a()));
        }

    }

    public boolean func_176473_a(World world, BlockPos blockposition, IBlockState iblockdata, boolean flag) {
        return true;
    }

    public boolean func_180670_a(World world, Random random, BlockPos blockposition, IBlockState iblockdata) {
        return (double) world.field_73012_v.nextFloat() < 0.45D;
    }

    public void func_176474_b(World world, Random random, BlockPos blockposition, IBlockState iblockdata) {
        this.func_176478_d(world, blockposition, iblockdata, random);
    }

    public IBlockState func_176203_a(int i) {
        return this.func_176223_P().func_177226_a(BlockSapling.field_176480_a, BlockPlanks.EnumType.func_176837_a(i & 7)).func_177226_a(BlockSapling.field_176479_b, Integer.valueOf((i & 8) >> 3));
    }

    public int func_176201_c(IBlockState iblockdata) {
        byte b0 = 0;
        int i = b0 | ((BlockPlanks.EnumType) iblockdata.func_177229_b(BlockSapling.field_176480_a)).func_176839_a();

        i |= ((Integer) iblockdata.func_177229_b(BlockSapling.field_176479_b)).intValue() << 3;
        return i;
    }

    protected BlockStateContainer func_180661_e() {
        return new BlockStateContainer(this, new IProperty[] { BlockSapling.field_176480_a, BlockSapling.field_176479_b});
    }
}
