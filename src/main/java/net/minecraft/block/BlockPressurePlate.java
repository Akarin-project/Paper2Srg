package net.minecraft.block;

import java.util.Iterator;
import java.util.List;


import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.bukkit.event.entity.EntityInteractEvent;

public class BlockPressurePlate extends BlockBasePressurePlate {

    public static final PropertyBool field_176580_a = PropertyBool.func_177716_a("powered");
    private final BlockPressurePlate.Sensitivity field_150069_a;

    protected BlockPressurePlate(Material material, BlockPressurePlate.Sensitivity blockpressureplatebinary_enummobtype) {
        super(material);
        this.func_180632_j(this.field_176227_L.func_177621_b().func_177226_a(BlockPressurePlate.field_176580_a, Boolean.valueOf(false)));
        this.field_150069_a = blockpressureplatebinary_enummobtype;
    }

    protected int func_176576_e(IBlockState iblockdata) {
        return ((Boolean) iblockdata.func_177229_b(BlockPressurePlate.field_176580_a)).booleanValue() ? 15 : 0;
    }

    protected IBlockState func_176575_a(IBlockState iblockdata, int i) {
        return iblockdata.func_177226_a(BlockPressurePlate.field_176580_a, Boolean.valueOf(i > 0));
    }

    protected void func_185507_b(World world, BlockPos blockposition) {
        if (this.field_149764_J == Material.field_151575_d) {
            world.func_184133_a((EntityPlayer) null, blockposition, SoundEvents.field_187895_gX, SoundCategory.BLOCKS, 0.3F, 0.8F);
        } else {
            world.func_184133_a((EntityPlayer) null, blockposition, SoundEvents.field_187901_ga, SoundCategory.BLOCKS, 0.3F, 0.6F);
        }

    }

    protected void func_185508_c(World world, BlockPos blockposition) {
        if (this.field_149764_J == Material.field_151575_d) {
            world.func_184133_a((EntityPlayer) null, blockposition, SoundEvents.field_187893_gW, SoundCategory.BLOCKS, 0.3F, 0.7F);
        } else {
            world.func_184133_a((EntityPlayer) null, blockposition, SoundEvents.field_187847_fZ, SoundCategory.BLOCKS, 0.3F, 0.5F);
        }

    }

    protected int func_180669_e(World world, BlockPos blockposition) {
        AxisAlignedBB axisalignedbb = BlockPressurePlate.field_185511_c.func_186670_a(blockposition);
        List list;

        switch (this.field_150069_a) {
        case EVERYTHING:
            list = world.func_72839_b((Entity) null, axisalignedbb);
            break;

        case MOBS:
            list = world.func_72872_a(EntityLivingBase.class, axisalignedbb);
            break;

        default:
            return 0;
        }

        if (!list.isEmpty()) {
            Iterator iterator = list.iterator();

            while (iterator.hasNext()) {
                Entity entity = (Entity) iterator.next();

                // CraftBukkit start - Call interact event when turning on a pressure plate
                if (this.func_176576_e(world.func_180495_p(blockposition)) == 0) {
                    org.bukkit.World bworld = world.getWorld();
                    org.bukkit.plugin.PluginManager manager = world.getServer().getPluginManager();
                    org.bukkit.event.Cancellable cancellable;

                    if (entity instanceof EntityPlayer) {
                        cancellable = org.bukkit.craftbukkit.event.CraftEventFactory.callPlayerInteractEvent((EntityPlayer) entity, org.bukkit.event.block.Action.PHYSICAL, blockposition, null, null, null);
                    } else {
                        cancellable = new EntityInteractEvent(entity.getBukkitEntity(), bworld.getBlockAt(blockposition.func_177958_n(), blockposition.func_177956_o(), blockposition.func_177952_p()));
                        manager.callEvent((EntityInteractEvent) cancellable);
                    }

                    // We only want to block turning the plate on if all events are cancelled
                    if (cancellable.isCancelled()) {
                        continue;
                    }
                }
                // CraftBukkit end

                if (!entity.func_145773_az()) {
                    return 15;
                }
            }
        }

        return 0;
    }

    public IBlockState func_176203_a(int i) {
        return this.func_176223_P().func_177226_a(BlockPressurePlate.field_176580_a, Boolean.valueOf(i == 1));
    }

    public int func_176201_c(IBlockState iblockdata) {
        return ((Boolean) iblockdata.func_177229_b(BlockPressurePlate.field_176580_a)).booleanValue() ? 1 : 0;
    }

    protected BlockStateContainer func_180661_e() {
        return new BlockStateContainer(this, new IProperty[] { BlockPressurePlate.field_176580_a});
    }

    public static enum Sensitivity {

        EVERYTHING, MOBS;

        private Sensitivity() {}
    }
}
