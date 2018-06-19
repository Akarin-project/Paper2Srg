package net.minecraft.enchantment;


import java.util.Iterator;

import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class EnchantmentFrostWalker extends Enchantment {

    public EnchantmentFrostWalker(Enchantment.Rarity enchantment_rarity, EntityEquipmentSlot... aenumitemslot) {
        super(enchantment_rarity, EnumEnchantmentType.ARMOR_FEET, aenumitemslot);
        this.func_77322_b("frostWalker");
    }

    public int func_77321_a(int i) {
        return i * 10;
    }

    public int func_77317_b(int i) {
        return this.func_77321_a(i) + 15;
    }

    public boolean func_185261_e() {
        return true;
    }

    public int func_77325_b() {
        return 2;
    }

    public static void func_185266_a(EntityLivingBase entityliving, World world, BlockPos blockposition, int i) {
        if (entityliving.field_70122_E) {
            float f = (float) Math.min(16, 2 + i);
            BlockPos.MutableBlockPos blockposition_mutableblockposition = new BlockPos.MutableBlockPos(0, 0, 0);
            Iterator iterator = BlockPos.func_177975_b(blockposition.func_177963_a((double) (-f), -1.0D, (double) (-f)), blockposition.func_177963_a((double) f, -1.0D, (double) f)).iterator();

            while (iterator.hasNext()) {
                BlockPos.MutableBlockPos blockposition_mutableblockposition1 = (BlockPos.MutableBlockPos) iterator.next();

                if (blockposition_mutableblockposition1.func_177957_d(entityliving.field_70165_t, entityliving.field_70163_u, entityliving.field_70161_v) <= (double) (f * f)) {
                    blockposition_mutableblockposition.func_181079_c(blockposition_mutableblockposition1.func_177958_n(), blockposition_mutableblockposition1.func_177956_o() + 1, blockposition_mutableblockposition1.func_177952_p());
                    IBlockState iblockdata = world.func_180495_p(blockposition_mutableblockposition);

                    if (iblockdata.func_185904_a() == Material.field_151579_a) {
                        IBlockState iblockdata1 = world.func_180495_p(blockposition_mutableblockposition1);

                        if (iblockdata1.func_185904_a() == Material.field_151586_h && ((Integer) iblockdata1.func_177229_b(BlockLiquid.field_176367_b)).intValue() == 0 && world.func_190527_a(Blocks.field_185778_de, blockposition_mutableblockposition1, false, EnumFacing.DOWN, (Entity) null)) {
                            // CraftBukkit Start - Call EntityBlockFormEvent for Frost Walker
                            if (org.bukkit.craftbukkit.event.CraftEventFactory.handleBlockFormEvent(world, blockposition_mutableblockposition1, Blocks.field_185778_de.func_176223_P(), entityliving)) {
                                world.func_175684_a(blockposition_mutableblockposition1.func_185334_h(), Blocks.field_185778_de, MathHelper.func_76136_a(entityliving.func_70681_au(), 60, 120));
                            }
                            // CraftBukkit End
                        }
                    }
                }
            }

        }
    }

    public boolean func_77326_a(Enchantment enchantment) {
        return super.func_77326_a(enchantment) && enchantment != Enchantments.field_185300_i;
    }
}
