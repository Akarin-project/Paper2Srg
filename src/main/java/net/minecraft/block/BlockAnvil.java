package net.minecraft.block;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerRepair;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.IInteractionObject;
import net.minecraft.world.World;

public class BlockAnvil extends BlockFalling {

    public static final PropertyDirection field_176506_a = BlockHorizontal.field_185512_D;
    public static final PropertyInteger field_176505_b = PropertyInteger.func_177719_a("damage", 0, 2);
    protected static final AxisAlignedBB field_185760_c = new AxisAlignedBB(0.0D, 0.0D, 0.125D, 1.0D, 1.0D, 0.875D);
    protected static final AxisAlignedBB field_185761_d = new AxisAlignedBB(0.125D, 0.0D, 0.0D, 0.875D, 1.0D, 1.0D);
    protected static final Logger field_185762_e = LogManager.getLogger();

    protected BlockAnvil() {
        super(Material.field_151574_g);
        this.func_180632_j(this.field_176227_L.func_177621_b().func_177226_a(BlockAnvil.field_176506_a, EnumFacing.NORTH).func_177226_a(BlockAnvil.field_176505_b, Integer.valueOf(0)));
        this.func_149713_g(0);
        this.func_149647_a(CreativeTabs.field_78031_c);
    }

    public boolean func_149686_d(IBlockState iblockdata) {
        return false;
    }

    public BlockFaceShape func_193383_a(IBlockAccess iblockaccess, IBlockState iblockdata, BlockPos blockposition, EnumFacing enumdirection) {
        return BlockFaceShape.UNDEFINED;
    }

    public boolean func_149662_c(IBlockState iblockdata) {
        return false;
    }

    public IBlockState func_180642_a(World world, BlockPos blockposition, EnumFacing enumdirection, float f, float f1, float f2, int i, EntityLivingBase entityliving) {
        EnumFacing enumdirection1 = entityliving.func_174811_aO().func_176746_e();

        try {
            return super.func_180642_a(world, blockposition, enumdirection, f, f1, f2, i, entityliving).func_177226_a(BlockAnvil.field_176506_a, enumdirection1).func_177226_a(BlockAnvil.field_176505_b, Integer.valueOf(i >> 2));
        } catch (IllegalArgumentException illegalargumentexception) {
            if (!world.field_72995_K) {
                BlockAnvil.field_185762_e.warn(String.format("Invalid damage property for anvil at %s. Found %d, must be in [0, 1, 2]", new Object[] { blockposition, Integer.valueOf(i >> 2)}));
                if (entityliving instanceof EntityPlayer) {
                    entityliving.func_145747_a(new TextComponentTranslation("Invalid damage property. Please pick in [0, 1, 2]", new Object[0]));
                }
            }

            return super.func_180642_a(world, blockposition, enumdirection, f, f1, f2, 0, entityliving).func_177226_a(BlockAnvil.field_176506_a, enumdirection1).func_177226_a(BlockAnvil.field_176505_b, Integer.valueOf(0));
        }
    }

    public boolean func_180639_a(World world, BlockPos blockposition, IBlockState iblockdata, EntityPlayer entityhuman, EnumHand enumhand, EnumFacing enumdirection, float f, float f1, float f2) {
        if (!world.field_72995_K) {
            entityhuman.func_180468_a(new BlockAnvil.Anvil(world, blockposition));
        }

        return true;
    }

    public int func_180651_a(IBlockState iblockdata) {
        return ((Integer) iblockdata.func_177229_b(BlockAnvil.field_176505_b)).intValue();
    }

    public AxisAlignedBB func_185496_a(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        EnumFacing enumdirection = (EnumFacing) iblockdata.func_177229_b(BlockAnvil.field_176506_a);

        return enumdirection.func_176740_k() == EnumFacing.Axis.X ? BlockAnvil.field_185760_c : BlockAnvil.field_185761_d;
    }

    public void func_149666_a(CreativeTabs creativemodetab, NonNullList<ItemStack> nonnulllist) {
        nonnulllist.add(new ItemStack(this));
        nonnulllist.add(new ItemStack(this, 1, 1));
        nonnulllist.add(new ItemStack(this, 1, 2));
    }

    protected void func_149829_a(EntityFallingBlock entityfallingblock) {
        entityfallingblock.func_145806_a(true);
    }

    public void func_176502_a_(World world, BlockPos blockposition, IBlockState iblockdata, IBlockState iblockdata1) {
        world.func_175718_b(1031, blockposition, 0);
    }

    public void func_190974_b(World world, BlockPos blockposition) {
        world.func_175718_b(1029, blockposition, 0);
    }

    public IBlockState func_176203_a(int i) {
        return this.func_176223_P().func_177226_a(BlockAnvil.field_176506_a, EnumFacing.func_176731_b(i & 3)).func_177226_a(BlockAnvil.field_176505_b, Integer.valueOf((i & 15) >> 2));
    }

    public int func_176201_c(IBlockState iblockdata) {
        byte b0 = 0;
        int i = b0 | ((EnumFacing) iblockdata.func_177229_b(BlockAnvil.field_176506_a)).func_176736_b();

        i |= ((Integer) iblockdata.func_177229_b(BlockAnvil.field_176505_b)).intValue() << 2;
        return i;
    }

    public IBlockState func_185499_a(IBlockState iblockdata, Rotation enumblockrotation) {
        return iblockdata.func_177230_c() != this ? iblockdata : iblockdata.func_177226_a(BlockAnvil.field_176506_a, enumblockrotation.func_185831_a((EnumFacing) iblockdata.func_177229_b(BlockAnvil.field_176506_a)));
    }

    protected BlockStateContainer func_180661_e() {
        return new BlockStateContainer(this, new IProperty[] { BlockAnvil.field_176506_a, BlockAnvil.field_176505_b});
    }

    public static class Anvil implements IInteractionObject {

        private final World field_175130_a;
        private final BlockPos field_175129_b;

        public Anvil(World world, BlockPos blockposition) {
            this.field_175130_a = world;
            this.field_175129_b = blockposition;
        }

        public String func_70005_c_() {
            return "anvil";
        }

        public boolean func_145818_k_() {
            return false;
        }

        public ITextComponent func_145748_c_() {
            return new TextComponentTranslation(Blocks.field_150467_bQ.func_149739_a() + ".name", new Object[0]);
        }

        public Container func_174876_a(InventoryPlayer playerinventory, EntityPlayer entityhuman) {
            return new ContainerRepair(playerinventory, this.field_175130_a, this.field_175129_b, entityhuman);
        }

        public String func_174875_k() {
            return "minecraft:anvil";
        }
    }
}
