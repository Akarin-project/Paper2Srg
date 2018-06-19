package net.minecraft.block;


import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.bukkit.event.entity.EntityInteractEvent;

public class BlockPressurePlateWeighted extends BlockBasePressurePlate {

    public static final PropertyInteger field_176579_a = PropertyInteger.func_177719_a("power", 0, 15);
    private final int field_150068_a;

    protected BlockPressurePlateWeighted(Material material, int i) {
        this(material, i, material.func_151565_r());
    }

    protected BlockPressurePlateWeighted(Material material, int i, MapColor materialmapcolor) {
        super(material, materialmapcolor);
        this.func_180632_j(this.field_176227_L.func_177621_b().func_177226_a(BlockPressurePlateWeighted.field_176579_a, Integer.valueOf(0)));
        this.field_150068_a = i;
    }

    protected int func_180669_e(World world, BlockPos blockposition) {
        // CraftBukkit start
        // int i = Math.min(world.a(Entity.class, BlockPressurePlateWeighted.c.a(blockposition)).size(), this.weight);
        int i = 0;
        java.util.Iterator iterator = world.func_72872_a(Entity.class, BlockPressurePlateWeighted.field_185511_c.func_186670_a(blockposition)).iterator();

        while (iterator.hasNext()) {
            Entity entity = (Entity) iterator.next();

            org.bukkit.event.Cancellable cancellable;

            if (entity instanceof EntityPlayer) {
                cancellable = org.bukkit.craftbukkit.event.CraftEventFactory.callPlayerInteractEvent((EntityPlayer) entity, org.bukkit.event.block.Action.PHYSICAL, blockposition, null, null, null);
            } else {
                cancellable = new EntityInteractEvent(entity.getBukkitEntity(), world.getWorld().getBlockAt(blockposition.func_177958_n(), blockposition.func_177956_o(), blockposition.func_177952_p()));
                world.getServer().getPluginManager().callEvent((EntityInteractEvent) cancellable);
            }

            // We only want to block turning the plate on if all events are cancelled
            if (!cancellable.isCancelled()) {
                i++;
            }
        }

        i = Math.min(i, this.field_150068_a);
        // CraftBukkit end

        if (i > 0) {
            float f = (float) Math.min(this.field_150068_a, i) / (float) this.field_150068_a;

            return MathHelper.func_76123_f(f * 15.0F);
        } else {
            return 0;
        }
    }

    protected void func_185507_b(World world, BlockPos blockposition) {
        world.func_184133_a((EntityPlayer) null, blockposition, SoundEvents.field_187776_dp, SoundCategory.BLOCKS, 0.3F, 0.90000004F);
    }

    protected void func_185508_c(World world, BlockPos blockposition) {
        world.func_184133_a((EntityPlayer) null, blockposition, SoundEvents.field_187774_do, SoundCategory.BLOCKS, 0.3F, 0.75F);
    }

    protected int func_176576_e(IBlockState iblockdata) {
        return ((Integer) iblockdata.func_177229_b(BlockPressurePlateWeighted.field_176579_a)).intValue();
    }

    protected IBlockState func_176575_a(IBlockState iblockdata, int i) {
        return iblockdata.func_177226_a(BlockPressurePlateWeighted.field_176579_a, Integer.valueOf(i));
    }

    public int func_149738_a(World world) {
        return 10;
    }

    public IBlockState func_176203_a(int i) {
        return this.func_176223_P().func_177226_a(BlockPressurePlateWeighted.field_176579_a, Integer.valueOf(i));
    }

    public int func_176201_c(IBlockState iblockdata) {
        return ((Integer) iblockdata.func_177229_b(BlockPressurePlateWeighted.field_176579_a)).intValue();
    }

    protected BlockStateContainer func_180661_e() {
        return new BlockStateContainer(this, new IProperty[] { BlockPressurePlateWeighted.field_176579_a});
    }
}
