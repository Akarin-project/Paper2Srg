package net.minecraft.item;

import com.google.common.collect.Multimap;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemHoe extends Item {

    private final float field_185072_b;
    protected Item.ToolMaterial field_77843_a;

    public ItemHoe(Item.ToolMaterial item_enumtoolmaterial) {
        this.field_77843_a = item_enumtoolmaterial;
        this.field_77777_bU = 1;
        this.func_77656_e(item_enumtoolmaterial.func_77997_a());
        this.func_77637_a(CreativeTabs.field_78040_i);
        this.field_185072_b = item_enumtoolmaterial.func_78000_c() + 1.0F;
    }

    public EnumActionResult func_180614_a(EntityPlayer entityhuman, World world, BlockPos blockposition, EnumHand enumhand, EnumFacing enumdirection, float f, float f1, float f2) {
        ItemStack itemstack = entityhuman.func_184586_b(enumhand);

        if (!entityhuman.func_175151_a(blockposition.func_177972_a(enumdirection), enumdirection, itemstack)) {
            return EnumActionResult.FAIL;
        } else {
            IBlockState iblockdata = world.func_180495_p(blockposition);
            Block block = iblockdata.func_177230_c();

            if (enumdirection != EnumFacing.DOWN && world.func_180495_p(blockposition.func_177984_a()).func_185904_a() == Material.field_151579_a) {
                if (block == Blocks.field_150349_c || block == Blocks.field_185774_da) {
                    this.func_185071_a(itemstack, entityhuman, world, blockposition, Blocks.field_150458_ak.func_176223_P());
                    return EnumActionResult.SUCCESS;
                }

                if (block == Blocks.field_150346_d) {
                    switch ((BlockDirt.DirtType) iblockdata.func_177229_b(BlockDirt.field_176386_a)) {
                    case DIRT:
                        this.func_185071_a(itemstack, entityhuman, world, blockposition, Blocks.field_150458_ak.func_176223_P());
                        return EnumActionResult.SUCCESS;

                    case COARSE_DIRT:
                        this.func_185071_a(itemstack, entityhuman, world, blockposition, Blocks.field_150346_d.func_176223_P().func_177226_a(BlockDirt.field_176386_a, BlockDirt.DirtType.DIRT));
                        return EnumActionResult.SUCCESS;
                    }
                }
            }

            return EnumActionResult.PASS;
        }
    }

    public boolean func_77644_a(ItemStack itemstack, EntityLivingBase entityliving, EntityLivingBase entityliving1) {
        itemstack.func_77972_a(1, entityliving1);
        return true;
    }

    protected void func_185071_a(ItemStack itemstack, EntityPlayer entityhuman, World world, BlockPos blockposition, IBlockState iblockdata) {
        world.func_184133_a(entityhuman, blockposition, SoundEvents.field_187693_cj, SoundCategory.BLOCKS, 1.0F, 1.0F);
        if (!world.field_72995_K) {
            world.func_180501_a(blockposition, iblockdata, 11);
            itemstack.func_77972_a(1, entityhuman);
        }

    }

    public String func_77842_f() {
        return this.field_77843_a.toString();
    }

    public Multimap<String, AttributeModifier> func_111205_h(EntityEquipmentSlot enumitemslot) {
        Multimap multimap = super.func_111205_h(enumitemslot);

        if (enumitemslot == EntityEquipmentSlot.MAINHAND) {
            multimap.put(SharedMonsterAttributes.field_111264_e.func_111108_a(), new AttributeModifier(ItemHoe.field_111210_e, "Weapon modifier", 0.0D, 0));
            multimap.put(SharedMonsterAttributes.field_188790_f.func_111108_a(), new AttributeModifier(ItemHoe.field_185050_h, "Weapon modifier", (double) (this.field_185072_b - 4.0F), 0));
        }

        return multimap;
    }
}
