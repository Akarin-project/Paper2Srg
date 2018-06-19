package net.minecraft.item;

import com.google.common.collect.Multimap;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemSword extends Item {

    private final float field_150934_a;
    private final Item.ToolMaterial field_150933_b;

    public ItemSword(Item.ToolMaterial item_enumtoolmaterial) {
        this.field_150933_b = item_enumtoolmaterial;
        this.field_77777_bU = 1;
        this.func_77656_e(item_enumtoolmaterial.func_77997_a());
        this.func_77637_a(CreativeTabs.field_78037_j);
        this.field_150934_a = 3.0F + item_enumtoolmaterial.func_78000_c();
    }

    public float func_150931_i() {
        return this.field_150933_b.func_78000_c();
    }

    public float func_150893_a(ItemStack itemstack, IBlockState iblockdata) {
        Block block = iblockdata.func_177230_c();

        if (block == Blocks.field_150321_G) {
            return 15.0F;
        } else {
            Material material = iblockdata.func_185904_a();

            return material != Material.field_151585_k && material != Material.field_151582_l && material != Material.field_151589_v && material != Material.field_151584_j && material != Material.field_151572_C ? 1.0F : 1.5F;
        }
    }

    public boolean func_77644_a(ItemStack itemstack, EntityLivingBase entityliving, EntityLivingBase entityliving1) {
        itemstack.func_77972_a(1, entityliving1);
        return true;
    }

    public boolean func_179218_a(ItemStack itemstack, World world, IBlockState iblockdata, BlockPos blockposition, EntityLivingBase entityliving) {
        if ((double) iblockdata.func_185887_b(world, blockposition) != 0.0D) {
            itemstack.func_77972_a(2, entityliving);
        }

        return true;
    }

    public boolean func_150897_b(IBlockState iblockdata) {
        return iblockdata.func_177230_c() == Blocks.field_150321_G;
    }

    public int func_77619_b() {
        return this.field_150933_b.func_77995_e();
    }

    public String func_150932_j() {
        return this.field_150933_b.toString();
    }

    public boolean func_82789_a(ItemStack itemstack, ItemStack itemstack1) {
        return this.field_150933_b.func_150995_f() == itemstack1.func_77973_b() ? true : super.func_82789_a(itemstack, itemstack1);
    }

    public Multimap<String, AttributeModifier> func_111205_h(EntityEquipmentSlot enumitemslot) {
        Multimap multimap = super.func_111205_h(enumitemslot);

        if (enumitemslot == EntityEquipmentSlot.MAINHAND) {
            multimap.put(SharedMonsterAttributes.field_111264_e.func_111108_a(), new AttributeModifier(ItemSword.field_111210_e, "Weapon modifier", (double) this.field_150934_a, 0));
            multimap.put(SharedMonsterAttributes.field_188790_f.func_111108_a(), new AttributeModifier(ItemSword.field_185050_h, "Weapon modifier", -2.4000000953674316D, 0));
        }

        return multimap;
    }
}
