package net.minecraft.entity.ai;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

import net.minecraft.block.Block;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockStateMatcher;
import net.minecraft.entity.EntityLiving;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.bukkit.Material;
import org.bukkit.craftbukkit.event.CraftEventFactory;

// CraftBukkit start
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.Material;
// CraftBukkit end

public class EntityAIEatGrass extends EntityAIBase {

    private static final Predicate<IBlockState> field_179505_b = BlockStateMatcher.func_177638_a((Block) Blocks.field_150329_H).func_177637_a(BlockTallGrass.field_176497_a, Predicates.equalTo(BlockTallGrass.EnumType.GRASS));
    private final EntityLiving field_151500_b;
    private final World field_151501_c;
    int field_151502_a;

    public EntityAIEatGrass(EntityLiving entityinsentient) {
        this.field_151500_b = entityinsentient;
        this.field_151501_c = entityinsentient.field_70170_p;
        this.func_75248_a(7);
    }

    public boolean func_75250_a() {
        if (this.field_151500_b.func_70681_au().nextInt(this.field_151500_b.func_70631_g_() ? 50 : 1000) != 0) {
            return false;
        } else {
            BlockPos blockposition = new BlockPos(this.field_151500_b.field_70165_t, this.field_151500_b.field_70163_u, this.field_151500_b.field_70161_v);

            return EntityAIEatGrass.field_179505_b.apply(this.field_151501_c.func_180495_p(blockposition)) ? true : this.field_151501_c.func_180495_p(blockposition.func_177977_b()).func_177230_c() == Blocks.field_150349_c;
        }
    }

    public void func_75249_e() {
        this.field_151502_a = 40;
        this.field_151501_c.func_72960_a(this.field_151500_b, (byte) 10);
        this.field_151500_b.func_70661_as().func_75499_g();
    }

    public void func_75251_c() {
        this.field_151502_a = 0;
    }

    public boolean func_75253_b() {
        return this.field_151502_a > 0;
    }

    public int func_151499_f() {
        return this.field_151502_a;
    }

    public void func_75246_d() {
        this.field_151502_a = Math.max(0, this.field_151502_a - 1);
        if (this.field_151502_a == 4) {
            BlockPos blockposition = new BlockPos(this.field_151500_b.field_70165_t, this.field_151500_b.field_70163_u, this.field_151500_b.field_70161_v);

            if (EntityAIEatGrass.field_179505_b.apply(this.field_151501_c.func_180495_p(blockposition))) {
                // CraftBukkit
                if (!CraftEventFactory.callEntityChangeBlockEvent(this.field_151500_b, this.field_151500_b.field_70170_p.getWorld().getBlockAt(blockposition.func_177958_n(), blockposition.func_177956_o(), blockposition.func_177952_p()), Material.AIR, !this.field_151501_c.func_82736_K().func_82766_b("mobGriefing")).isCancelled()) {
                    this.field_151501_c.func_175655_b(blockposition, false);
                }

                this.field_151500_b.func_70615_aA();
            } else {
                BlockPos blockposition1 = blockposition.func_177977_b();

                if (this.field_151501_c.func_180495_p(blockposition1).func_177230_c() == Blocks.field_150349_c) {
                    // CraftBukkit
                    if (!CraftEventFactory.callEntityChangeBlockEvent(this.field_151500_b, this.field_151500_b.field_70170_p.getWorld().getBlockAt(blockposition.func_177958_n(), blockposition.func_177956_o(), blockposition.func_177952_p()), Material.AIR, !this.field_151501_c.func_82736_K().func_82766_b("mobGriefing")).isCancelled()) {
                        this.field_151501_c.func_175718_b(2001, blockposition1, Block.func_149682_b(Blocks.field_150349_c));
                        this.field_151501_c.func_180501_a(blockposition1, Blocks.field_150346_d.func_176223_P(), 2);
                    }

                    this.field_151500_b.func_70615_aA();
                }
            }

        }
    }
}
