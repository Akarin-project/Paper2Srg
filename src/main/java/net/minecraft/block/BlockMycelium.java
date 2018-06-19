package net.minecraft.block;

import java.util.Random;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.bukkit.block.BlockState;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockSpreadEvent;

// CraftBukkit start
import org.bukkit.block.BlockState;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockSpreadEvent;
// CraftBukkit end

public class BlockMycelium extends Block {

    public static final PropertyBool field_176384_a = PropertyBool.func_177716_a("snowy");

    protected BlockMycelium() {
        super(Material.field_151577_b, MapColor.field_151678_z);
        this.func_180632_j(this.field_176227_L.func_177621_b().func_177226_a(BlockMycelium.field_176384_a, Boolean.valueOf(false)));
        this.func_149675_a(true);
        this.func_149647_a(CreativeTabs.field_78030_b);
    }

    public IBlockState func_176221_a(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        Block block = iblockaccess.func_180495_p(blockposition.func_177984_a()).func_177230_c();

        return iblockdata.func_177226_a(BlockMycelium.field_176384_a, Boolean.valueOf(block == Blocks.field_150433_aE || block == Blocks.field_150431_aC));
    }

    public void func_180650_b(World world, BlockPos blockposition, IBlockState iblockdata, Random random) {
        if (!world.field_72995_K) {
            if (world.func_175671_l(blockposition.func_177984_a()) < 4 && world.func_180495_p(blockposition.func_177984_a()).func_185891_c() > 2) {
                // CraftBukkit start
                // world.setTypeUpdate(blockposition, Blocks.DIRT.getBlockData().set(BlockDirt.VARIANT, BlockDirt.EnumDirtVariant.DIRT));
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
                if (world.func_175671_l(blockposition.func_177984_a()) >= 9) {
                    for (int i = 0; i < 4; ++i) {
                        BlockPos blockposition1 = blockposition.func_177982_a(random.nextInt(3) - 1, random.nextInt(5) - 3, random.nextInt(3) - 1);
                        IBlockState iblockdata1 = world.func_180495_p(blockposition1);
                        IBlockState iblockdata2 = world.func_180495_p(blockposition1.func_177984_a());

                        if (iblockdata1.func_177230_c() == Blocks.field_150346_d && iblockdata1.func_177229_b(BlockDirt.field_176386_a) == BlockDirt.DirtType.DIRT && world.func_175671_l(blockposition1.func_177984_a()) >= 4 && iblockdata2.func_185891_c() <= 2) {
                            // CraftBukkit start
                            // world.setTypeUpdate(blockposition1, this.getBlockData());
                            org.bukkit.World bworld = world.getWorld();
                            BlockState blockState = bworld.getBlockAt(blockposition1.func_177958_n(), blockposition1.func_177956_o(), blockposition1.func_177952_p()).getState();
                            blockState.setType(CraftMagicNumbers.getMaterial(this));

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

    public int func_176201_c(IBlockState iblockdata) {
        return 0;
    }

    protected BlockStateContainer func_180661_e() {
        return new BlockStateContainer(this, new IProperty[] { BlockMycelium.field_176384_a});
    }
}
