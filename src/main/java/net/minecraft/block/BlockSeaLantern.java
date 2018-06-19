package net.minecraft.block;

import java.util.Random;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;

public class BlockSeaLantern extends Block {

    public BlockSeaLantern(Material material) {
        super(material);
        this.func_149647_a(CreativeTabs.field_78030_b);
    }

    public int func_149745_a(Random random) {
        return 2 + random.nextInt(2);
    }

    public int func_149679_a(int i, Random random) {
        return MathHelper.func_76125_a(this.func_149745_a(random) + random.nextInt(i + 1), 1, 5);
    }

    public Item func_180660_a(IBlockState iblockdata, Random random, int i) {
        return Items.field_179563_cD;
    }

    public MapColor func_180659_g(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        return MapColor.field_151677_p;
    }

    protected boolean func_149700_E() {
        return true;
    }
}
