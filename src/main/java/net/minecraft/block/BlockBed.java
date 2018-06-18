package net.minecraft.block;

import java.util.Iterator;
import java.util.Random;
import javax.annotation.Nullable;

import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Biomes;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBed;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockBed extends BlockHorizontal implements ITileEntityProvider {

    public static final PropertyEnum<BlockBed.EnumPartType> PART = PropertyEnum.create("part", BlockBed.EnumPartType.class);
    public static final PropertyBool OCCUPIED = PropertyBool.create("occupied");
    protected static final AxisAlignedBB BED_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5625D, 1.0D);

    public BlockBed() {
        super(Material.CLOTH);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockBed.PART, BlockBed.EnumPartType.FOOT).withProperty(BlockBed.OCCUPIED, Boolean.valueOf(false)));
        this.hasTileEntity = true;
    }

    public MapColor getMapColor(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        if (iblockdata.getValue(BlockBed.PART) == BlockBed.EnumPartType.FOOT) {
            TileEntity tileentity = iblockaccess.getTileEntity(blockposition);

            if (tileentity instanceof TileEntityBed) {
                EnumDyeColor enumcolor = ((TileEntityBed) tileentity).getColor();

                return MapColor.getBlockColor(enumcolor);
            }
        }

        return MapColor.CLOTH;
    }

    public boolean onBlockActivated(World world, BlockPos blockposition, IBlockState iblockdata, EntityPlayer entityhuman, EnumHand enumhand, EnumFacing enumdirection, float f, float f1, float f2) {
        if (world.isRemote) {
            return true;
        } else {
            if (iblockdata.getValue(BlockBed.PART) != BlockBed.EnumPartType.HEAD) {
                blockposition = blockposition.offset((EnumFacing) iblockdata.getValue(BlockBed.FACING));
                iblockdata = world.getBlockState(blockposition);
                if (iblockdata.getBlock() != this) {
                    return true;
                }
            }

            if (world.provider.canRespawnHere() && world.getBiome(blockposition) != Biomes.HELL) {
                if (((Boolean) iblockdata.getValue(BlockBed.OCCUPIED)).booleanValue()) {
                    EntityPlayer entityhuman1 = this.getPlayerInBed(world, blockposition);

                    if (entityhuman1 != null) {
                        entityhuman.sendStatusMessage((ITextComponent) (new TextComponentTranslation("tile.bed.occupied", new Object[0])), true);
                        return true;
                    }

                    iblockdata = iblockdata.withProperty(BlockBed.OCCUPIED, Boolean.valueOf(false));
                    world.setBlockState(blockposition, iblockdata, 4);
                }

                EntityPlayer.SleepResult entityhuman_enumbedresult = entityhuman.trySleep(blockposition);

                if (entityhuman_enumbedresult == EntityPlayer.SleepResult.OK) {
                    iblockdata = iblockdata.withProperty(BlockBed.OCCUPIED, Boolean.valueOf(true));
                    world.setBlockState(blockposition, iblockdata, 4);
                    return true;
                } else {
                    if (entityhuman_enumbedresult == EntityPlayer.SleepResult.NOT_POSSIBLE_NOW) {
                        entityhuman.sendStatusMessage((ITextComponent) (new TextComponentTranslation("tile.bed.noSleep", new Object[0])), true);
                    } else if (entityhuman_enumbedresult == EntityPlayer.SleepResult.NOT_SAFE) {
                        entityhuman.sendStatusMessage((ITextComponent) (new TextComponentTranslation("tile.bed.notSafe", new Object[0])), true);
                    } else if (entityhuman_enumbedresult == EntityPlayer.SleepResult.TOO_FAR_AWAY) {
                        entityhuman.sendStatusMessage((ITextComponent) (new TextComponentTranslation("tile.bed.tooFarAway", new Object[0])), true);
                    }

                    return true;
                }
            } else {
                world.setBlockToAir(blockposition);
                BlockPos blockposition1 = blockposition.offset(((EnumFacing) iblockdata.getValue(BlockBed.FACING)).getOpposite());

                if (world.getBlockState(blockposition1).getBlock() == this) {
                    world.setBlockToAir(blockposition1);
                }

                world.newExplosion((Entity) null, (double) blockposition.getX() + 0.5D, (double) blockposition.getY() + 0.5D, (double) blockposition.getZ() + 0.5D, 5.0F, true, true);
                return true;
            }
        }
    }

    @Nullable
    private EntityPlayer getPlayerInBed(World world, BlockPos blockposition) {
        Iterator iterator = world.playerEntities.iterator();

        EntityPlayer entityhuman;

        do {
            if (!iterator.hasNext()) {
                return null;
            }

            entityhuman = (EntityPlayer) iterator.next();
        } while (!entityhuman.isPlayerSleeping() || !entityhuman.bedLocation.equals(blockposition));

        return entityhuman;
    }

    public boolean isFullCube(IBlockState iblockdata) {
        return false;
    }

    public boolean isOpaqueCube(IBlockState iblockdata) {
        return false;
    }

    public void onFallenUpon(World world, BlockPos blockposition, Entity entity, float f) {
        super.onFallenUpon(world, blockposition, entity, f * 0.5F);
    }

    public void onLanded(World world, Entity entity) {
        if (entity.isSneaking()) {
            super.onLanded(world, entity);
        } else if (entity.motionY < 0.0D) {
            entity.motionY = -entity.motionY * 0.6600000262260437D;
            if (!(entity instanceof EntityLivingBase)) {
                entity.motionY *= 0.8D;
            }
        }

    }

    public void neighborChanged(IBlockState iblockdata, World world, BlockPos blockposition, Block block, BlockPos blockposition1) {
        EnumFacing enumdirection = (EnumFacing) iblockdata.getValue(BlockBed.FACING);

        if (iblockdata.getValue(BlockBed.PART) == BlockBed.EnumPartType.FOOT) {
            if (world.getBlockState(blockposition.offset(enumdirection)).getBlock() != this) {
                world.setBlockToAir(blockposition);
            }
        } else if (world.getBlockState(blockposition.offset(enumdirection.getOpposite())).getBlock() != this) {
            if (!world.isRemote) {
                this.dropBlockAsItem(world, blockposition, iblockdata, 0);
            }

            world.setBlockToAir(blockposition);
        }

    }

    public Item getItemDropped(IBlockState iblockdata, Random random, int i) {
        return iblockdata.getValue(BlockBed.PART) == BlockBed.EnumPartType.FOOT ? Items.AIR : Items.BED;
    }

    public AxisAlignedBB getBoundingBox(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        return BlockBed.BED_AABB;
    }

    @Nullable
    public static BlockPos getSafeExitLocation(World world, BlockPos blockposition, int i) {
        EnumFacing enumdirection = (EnumFacing) world.getBlockState(blockposition).getValue(BlockBed.FACING);
        int j = blockposition.getX();
        int k = blockposition.getY();
        int l = blockposition.getZ();

        for (int i1 = 0; i1 <= 1; ++i1) {
            int j1 = j - enumdirection.getFrontOffsetX() * i1 - 1;
            int k1 = l - enumdirection.getFrontOffsetZ() * i1 - 1;
            int l1 = j1 + 2;
            int i2 = k1 + 2;

            for (int j2 = j1; j2 <= l1; ++j2) {
                for (int k2 = k1; k2 <= i2; ++k2) {
                    BlockPos blockposition1 = new BlockPos(j2, k, k2);

                    if (hasRoomForPlayer(world, blockposition1)) {
                        if (i <= 0) {
                            return blockposition1;
                        }

                        --i;
                    }
                }
            }
        }

        return null;
    }

    protected static boolean hasRoomForPlayer(World world, BlockPos blockposition) {
        return world.getBlockState(blockposition.down()).isTopSolid() && !world.getBlockState(blockposition).getMaterial().isSolid() && !world.getBlockState(blockposition.up()).getMaterial().isSolid();
    }

    public void dropBlockAsItemWithChance(World world, BlockPos blockposition, IBlockState iblockdata, float f, int i) {
        if (iblockdata.getValue(BlockBed.PART) == BlockBed.EnumPartType.HEAD) {
            TileEntity tileentity = world.getTileEntity(blockposition);
            EnumDyeColor enumcolor = tileentity instanceof TileEntityBed ? ((TileEntityBed) tileentity).getColor() : EnumDyeColor.RED;

            spawnAsEntity(world, blockposition, new ItemStack(Items.BED, 1, enumcolor.getMetadata()));
        }

    }

    public EnumPushReaction getMobilityFlag(IBlockState iblockdata) {
        return EnumPushReaction.DESTROY;
    }

    public EnumBlockRenderType getRenderType(IBlockState iblockdata) {
        return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    public ItemStack getItem(World world, BlockPos blockposition, IBlockState iblockdata) {
        BlockPos blockposition1 = blockposition;

        if (iblockdata.getValue(BlockBed.PART) == BlockBed.EnumPartType.FOOT) {
            blockposition1 = blockposition.offset((EnumFacing) iblockdata.getValue(BlockBed.FACING));
        }

        TileEntity tileentity = world.getTileEntity(blockposition1);
        EnumDyeColor enumcolor = tileentity instanceof TileEntityBed ? ((TileEntityBed) tileentity).getColor() : EnumDyeColor.RED;

        return new ItemStack(Items.BED, 1, enumcolor.getMetadata());
    }

    public void onBlockHarvested(World world, BlockPos blockposition, IBlockState iblockdata, EntityPlayer entityhuman) {
        if (entityhuman.capabilities.isCreativeMode && iblockdata.getValue(BlockBed.PART) == BlockBed.EnumPartType.FOOT) {
            BlockPos blockposition1 = blockposition.offset((EnumFacing) iblockdata.getValue(BlockBed.FACING));

            if (world.getBlockState(blockposition1).getBlock() == this) {
                world.setBlockToAir(blockposition1);
            }
        }

    }

    public void harvestBlock(World world, EntityPlayer entityhuman, BlockPos blockposition, IBlockState iblockdata, TileEntity tileentity, ItemStack itemstack) {
        if (iblockdata.getValue(BlockBed.PART) == BlockBed.EnumPartType.HEAD && tileentity instanceof TileEntityBed) {
            TileEntityBed tileentitybed = (TileEntityBed) tileentity;
            ItemStack itemstack1 = tileentitybed.getItemStack();

            spawnAsEntity(world, blockposition, itemstack1);
        } else {
            super.harvestBlock(world, entityhuman, blockposition, iblockdata, (TileEntity) null, itemstack);
        }

    }

    public void breakBlock(World world, BlockPos blockposition, IBlockState iblockdata) {
        super.breakBlock(world, blockposition, iblockdata);
        world.removeTileEntity(blockposition);
    }

    public IBlockState getStateFromMeta(int i) {
        EnumFacing enumdirection = EnumFacing.getHorizontal(i);

        return (i & 8) > 0 ? this.getDefaultState().withProperty(BlockBed.PART, BlockBed.EnumPartType.HEAD).withProperty(BlockBed.FACING, enumdirection).withProperty(BlockBed.OCCUPIED, Boolean.valueOf((i & 4) > 0)) : this.getDefaultState().withProperty(BlockBed.PART, BlockBed.EnumPartType.FOOT).withProperty(BlockBed.FACING, enumdirection);
    }

    public IBlockState getActualState(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        if (iblockdata.getValue(BlockBed.PART) == BlockBed.EnumPartType.FOOT) {
            IBlockState iblockdata1 = iblockaccess.getBlockState(blockposition.offset((EnumFacing) iblockdata.getValue(BlockBed.FACING)));

            if (iblockdata1.getBlock() == this) {
                iblockdata = iblockdata.withProperty(BlockBed.OCCUPIED, iblockdata1.getValue(BlockBed.OCCUPIED));
            }
        }

        return iblockdata;
    }

    public IBlockState withRotation(IBlockState iblockdata, Rotation enumblockrotation) {
        return iblockdata.withProperty(BlockBed.FACING, enumblockrotation.rotate((EnumFacing) iblockdata.getValue(BlockBed.FACING)));
    }

    public IBlockState withMirror(IBlockState iblockdata, Mirror enumblockmirror) {
        return iblockdata.withRotation(enumblockmirror.toRotation((EnumFacing) iblockdata.getValue(BlockBed.FACING)));
    }

    public int getMetaFromState(IBlockState iblockdata) {
        byte b0 = 0;
        int i = b0 | ((EnumFacing) iblockdata.getValue(BlockBed.FACING)).getHorizontalIndex();

        if (iblockdata.getValue(BlockBed.PART) == BlockBed.EnumPartType.HEAD) {
            i |= 8;
            if (((Boolean) iblockdata.getValue(BlockBed.OCCUPIED)).booleanValue()) {
                i |= 4;
            }
        }

        return i;
    }

    public BlockFaceShape getBlockFaceShape(IBlockAccess iblockaccess, IBlockState iblockdata, BlockPos blockposition, EnumFacing enumdirection) {
        return BlockFaceShape.UNDEFINED;
    }

    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] { BlockBed.FACING, BlockBed.PART, BlockBed.OCCUPIED});
    }

    public TileEntity createNewTileEntity(World world, int i) {
        return new TileEntityBed();
    }

    public static enum EnumPartType implements IStringSerializable {

        HEAD("head"), FOOT("foot");

        private final String name;

        private EnumPartType(String s) {
            this.name = s;
        }

        public String toString() {
            return this.name;
        }

        public String getName() {
            return this.name;
        }
    }
}
