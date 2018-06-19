package net.minecraft.item;

import com.google.common.collect.Maps;
import java.util.Map;

import net.minecraft.block.BlockJukebox;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.stats.StatList;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemRecord extends Item {

    private static final Map<SoundEvent, ItemRecord> field_150928_b = Maps.newHashMap();
    private final SoundEvent field_185076_b;
    private final String field_185077_c;

    protected ItemRecord(String s, SoundEvent soundeffect) {
        this.field_185077_c = "item.record." + s + ".desc";
        this.field_185076_b = soundeffect;
        this.field_77777_bU = 1;
        this.func_77637_a(CreativeTabs.field_78026_f);
        ItemRecord.field_150928_b.put(this.field_185076_b, this);
    }

    public EnumActionResult func_180614_a(EntityPlayer entityhuman, World world, BlockPos blockposition, EnumHand enumhand, EnumFacing enumdirection, float f, float f1, float f2) {
        IBlockState iblockdata = world.func_180495_p(blockposition);

        if (iblockdata.func_177230_c() == Blocks.field_150421_aI && !((Boolean) iblockdata.func_177229_b(BlockJukebox.field_176432_a)).booleanValue()) {
            if (!world.field_72995_K) {
                if (true) return EnumActionResult.SUCCESS; // CraftBukkit - handled in ItemStack
                ItemStack itemstack = entityhuman.func_184586_b(enumhand);

                ((BlockJukebox) Blocks.field_150421_aI).func_176431_a(world, blockposition, iblockdata, itemstack);
                world.func_180498_a((EntityPlayer) null, 1010, blockposition, Item.func_150891_b(this));
                itemstack.func_190918_g(1);
                entityhuman.func_71029_a(StatList.field_188092_Z);
            }

            return EnumActionResult.SUCCESS;
        } else {
            return EnumActionResult.PASS;
        }
    }

    public EnumRarity func_77613_e(ItemStack itemstack) {
        return EnumRarity.RARE;
    }
}
