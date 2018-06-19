package net.minecraft.item;

import com.google.common.collect.Multimap;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemTool extends Item {

    private final Set<Block> field_150914_c;
    protected float field_77864_a;
    protected float field_77865_bY;
    protected float field_185065_c;
    protected Item.ToolMaterial field_77862_b;

    protected ItemTool(float f, float f1, Item.ToolMaterial item_enumtoolmaterial, Set<Block> set) {
        this.field_77864_a = 4.0F;
        this.field_77862_b = item_enumtoolmaterial;
        this.field_150914_c = set;
        this.field_77777_bU = 1;
        this.func_77656_e(item_enumtoolmaterial.func_77997_a());
        this.field_77864_a = item_enumtoolmaterial.func_77998_b();
        this.field_77865_bY = f + item_enumtoolmaterial.func_78000_c();
        this.field_185065_c = f1;
        this.func_77637_a(CreativeTabs.field_78040_i);
    }

    protected ItemTool(Item.ToolMaterial item_enumtoolmaterial, Set<Block> set) {
        this(0.0F, 0.0F, item_enumtoolmaterial, set);
    }

    public float func_150893_a(ItemStack itemstack, IBlockState iblockdata) {
        return this.field_150914_c.contains(iblockdata.func_177230_c()) ? this.field_77864_a : 1.0F;
    }

    public boolean func_77644_a(ItemStack itemstack, EntityLivingBase entityliving, EntityLivingBase entityliving1) {
        itemstack.func_77972_a(2, entityliving1);
        return true;
    }

    public boolean func_179218_a(ItemStack itemstack, World world, IBlockState iblockdata, BlockPos blockposition, EntityLivingBase entityliving) {
        if (!world.field_72995_K && (double) iblockdata.func_185887_b(world, blockposition) != 0.0D) {
            itemstack.func_77972_a(1, entityliving);
        }

        return true;
    }

    public int func_77619_b() {
        return this.field_77862_b.func_77995_e();
    }

    public String func_77861_e() {
        return this.field_77862_b.toString();
    }

    public boolean func_82789_a(ItemStack itemstack, ItemStack itemstack1) {
        return this.field_77862_b.func_150995_f() == itemstack1.func_77973_b() ? true : super.func_82789_a(itemstack, itemstack1);
    }

    public Multimap<String, AttributeModifier> func_111205_h(EntityEquipmentSlot enumitemslot) {
        Multimap multimap = super.func_111205_h(enumitemslot);

        if (enumitemslot == EntityEquipmentSlot.MAINHAND) {
            multimap.put(SharedMonsterAttributes.field_111264_e.func_111108_a(), new AttributeModifier(ItemTool.field_111210_e, "Tool modifier", (double) this.field_77865_bY, 0));
            multimap.put(SharedMonsterAttributes.field_188790_f.func_111108_a(), new AttributeModifier(ItemTool.field_185050_h, "Tool modifier", (double) this.field_185065_c, 0));
        }

        return multimap;
    }
}
