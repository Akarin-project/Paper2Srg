package net.minecraft.block;

import java.util.Random;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.CommandBlockBaseLogic;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityCommandBlock;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.StringUtils;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import org.bukkit.event.block.BlockRedstoneEvent;

public class BlockCommandBlock extends BlockContainer {

    private static final Logger LOGGER = LogManager.getLogger();
    public static final PropertyDirection FACING = BlockDirectional.FACING;
    public static final PropertyBool CONDITIONAL = PropertyBool.create("conditional");

    public BlockCommandBlock(MapColor materialmapcolor) {
        super(Material.IRON, materialmapcolor);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockCommandBlock.FACING, EnumFacing.NORTH).withProperty(BlockCommandBlock.CONDITIONAL, Boolean.valueOf(false)));
    }

    public TileEntity createNewTileEntity(World world, int i) {
        TileEntityCommandBlock tileentitycommand = new TileEntityCommandBlock();

        tileentitycommand.setAuto(this == Blocks.CHAIN_COMMAND_BLOCK);
        return tileentitycommand;
    }

    public void neighborChanged(IBlockState iblockdata, World world, BlockPos blockposition, Block block, BlockPos blockposition1) {
        if (!world.isRemote) {
            TileEntity tileentity = world.getTileEntity(blockposition);

            if (tileentity instanceof TileEntityCommandBlock) {
                TileEntityCommandBlock tileentitycommand = (TileEntityCommandBlock) tileentity;
                boolean flag = world.isBlockPowered(blockposition);
                boolean flag1 = tileentitycommand.isPowered();
                // CraftBukkit start
                org.bukkit.block.Block bukkitBlock = world.getWorld().getBlockAt(blockposition.getX(), blockposition.getY(), blockposition.getZ());
                int old = flag1 ? 15 : 0;
                int current = flag ? 15 : 0;

                BlockRedstoneEvent eventRedstone = new BlockRedstoneEvent(bukkitBlock, old, current);
                world.getServer().getPluginManager().callEvent(eventRedstone);
                flag = eventRedstone.getNewCurrent() > 0;
                // CraftBukkit end

                tileentitycommand.setPowered(flag);
                if (!flag1 && !tileentitycommand.isAuto() && tileentitycommand.getMode() != TileEntityCommandBlock.Mode.SEQUENCE) {
                    if (flag) {
                        tileentitycommand.setConditionMet();
                        world.scheduleUpdate(blockposition, (Block) this, this.tickRate(world));
                    }

                }
            }
        }
    }

    public void updateTick(World world, BlockPos blockposition, IBlockState iblockdata, Random random) {
        if (!world.isRemote) {
            TileEntity tileentity = world.getTileEntity(blockposition);

            if (tileentity instanceof TileEntityCommandBlock) {
                TileEntityCommandBlock tileentitycommand = (TileEntityCommandBlock) tileentity;
                CommandBlockBaseLogic commandblocklistenerabstract = tileentitycommand.getCommandBlockLogic();
                boolean flag = !StringUtils.isNullOrEmpty(commandblocklistenerabstract.getCommand());
                TileEntityCommandBlock.Mode tileentitycommand_type = tileentitycommand.getMode();
                boolean flag1 = tileentitycommand.isConditionMet();

                if (tileentitycommand_type == TileEntityCommandBlock.Mode.AUTO) {
                    tileentitycommand.setConditionMet();
                    if (flag1) {
                        this.execute(iblockdata, world, blockposition, commandblocklistenerabstract, flag);
                    } else if (tileentitycommand.isConditional()) {
                        commandblocklistenerabstract.setSuccessCount(0);
                    }

                    if (tileentitycommand.isPowered() || tileentitycommand.isAuto()) {
                        world.scheduleUpdate(blockposition, (Block) this, this.tickRate(world));
                    }
                } else if (tileentitycommand_type == TileEntityCommandBlock.Mode.REDSTONE) {
                    if (flag1) {
                        this.execute(iblockdata, world, blockposition, commandblocklistenerabstract, flag);
                    } else if (tileentitycommand.isConditional()) {
                        commandblocklistenerabstract.setSuccessCount(0);
                    }
                }

                world.updateComparatorOutputLevel(blockposition, this);
            }

        }
    }

    private void execute(IBlockState iblockdata, World world, BlockPos blockposition, CommandBlockBaseLogic commandblocklistenerabstract, boolean flag) {
        if (flag) {
            commandblocklistenerabstract.trigger(world);
        } else {
            commandblocklistenerabstract.setSuccessCount(0);
        }

        executeChain(world, blockposition, (EnumFacing) iblockdata.getValue(BlockCommandBlock.FACING));
    }

    public int tickRate(World world) {
        return 1;
    }

    public boolean onBlockActivated(World world, BlockPos blockposition, IBlockState iblockdata, EntityPlayer entityhuman, EnumHand enumhand, EnumFacing enumdirection, float f, float f1, float f2) {
        TileEntity tileentity = world.getTileEntity(blockposition);

        if (tileentity instanceof TileEntityCommandBlock && entityhuman.canUseCommandBlock()) {
            entityhuman.displayGuiCommandBlock((TileEntityCommandBlock) tileentity);
            return true;
        } else {
            return false;
        }
    }

    public boolean hasComparatorInputOverride(IBlockState iblockdata) {
        return true;
    }

    public int getComparatorInputOverride(IBlockState iblockdata, World world, BlockPos blockposition) {
        TileEntity tileentity = world.getTileEntity(blockposition);

        return tileentity instanceof TileEntityCommandBlock ? ((TileEntityCommandBlock) tileentity).getCommandBlockLogic().getSuccessCount() : 0;
    }

    public void onBlockPlacedBy(World world, BlockPos blockposition, IBlockState iblockdata, EntityLivingBase entityliving, ItemStack itemstack) {
        TileEntity tileentity = world.getTileEntity(blockposition);

        if (tileentity instanceof TileEntityCommandBlock) {
            TileEntityCommandBlock tileentitycommand = (TileEntityCommandBlock) tileentity;
            CommandBlockBaseLogic commandblocklistenerabstract = tileentitycommand.getCommandBlockLogic();

            if (itemstack.hasDisplayName()) {
                commandblocklistenerabstract.setName(itemstack.getDisplayName());
            }

            if (!world.isRemote) {
                NBTTagCompound nbttagcompound = itemstack.getTagCompound();

                if (nbttagcompound == null || !nbttagcompound.hasKey("BlockEntityTag", 10)) {
                    commandblocklistenerabstract.setTrackOutput(world.getGameRules().getBoolean("sendCommandFeedback"));
                    tileentitycommand.setAuto(this == Blocks.CHAIN_COMMAND_BLOCK);
                }

                if (tileentitycommand.getMode() == TileEntityCommandBlock.Mode.SEQUENCE) {
                    boolean flag = world.isBlockPowered(blockposition);

                    tileentitycommand.setPowered(flag);
                }
            }

        }
    }

    public int quantityDropped(Random random) {
        return 0;
    }

    public EnumBlockRenderType getRenderType(IBlockState iblockdata) {
        return EnumBlockRenderType.MODEL;
    }

    public IBlockState getStateFromMeta(int i) {
        return this.getDefaultState().withProperty(BlockCommandBlock.FACING, EnumFacing.getFront(i & 7)).withProperty(BlockCommandBlock.CONDITIONAL, Boolean.valueOf((i & 8) != 0));
    }

    public int getMetaFromState(IBlockState iblockdata) {
        return ((EnumFacing) iblockdata.getValue(BlockCommandBlock.FACING)).getIndex() | (((Boolean) iblockdata.getValue(BlockCommandBlock.CONDITIONAL)).booleanValue() ? 8 : 0);
    }

    public IBlockState withRotation(IBlockState iblockdata, Rotation enumblockrotation) {
        return iblockdata.withProperty(BlockCommandBlock.FACING, enumblockrotation.rotate((EnumFacing) iblockdata.getValue(BlockCommandBlock.FACING)));
    }

    public IBlockState withMirror(IBlockState iblockdata, Mirror enumblockmirror) {
        return iblockdata.withRotation(enumblockmirror.toRotation((EnumFacing) iblockdata.getValue(BlockCommandBlock.FACING)));
    }

    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] { BlockCommandBlock.FACING, BlockCommandBlock.CONDITIONAL});
    }

    public IBlockState getStateForPlacement(World world, BlockPos blockposition, EnumFacing enumdirection, float f, float f1, float f2, int i, EntityLivingBase entityliving) {
        return this.getDefaultState().withProperty(BlockCommandBlock.FACING, EnumFacing.getDirectionFromEntityLiving(blockposition, entityliving)).withProperty(BlockCommandBlock.CONDITIONAL, Boolean.valueOf(false));
    }

    private static void executeChain(World world, BlockPos blockposition, EnumFacing enumdirection) {
        BlockPos.MutableBlockPos blockposition_mutableblockposition = new BlockPos.MutableBlockPos(blockposition);
        GameRules gamerules = world.getGameRules();

        int i;
        IBlockState iblockdata;

        for (i = gamerules.getInt("maxCommandChainLength"); i-- > 0; enumdirection = (EnumFacing) iblockdata.getValue(BlockCommandBlock.FACING)) {
            blockposition_mutableblockposition.move(enumdirection);
            iblockdata = world.getBlockState(blockposition_mutableblockposition);
            Block block = iblockdata.getBlock();

            if (block != Blocks.CHAIN_COMMAND_BLOCK) {
                break;
            }

            TileEntity tileentity = world.getTileEntity(blockposition_mutableblockposition);

            if (!(tileentity instanceof TileEntityCommandBlock)) {
                break;
            }

            TileEntityCommandBlock tileentitycommand = (TileEntityCommandBlock) tileentity;

            if (tileentitycommand.getMode() != TileEntityCommandBlock.Mode.SEQUENCE) {
                break;
            }

            if (tileentitycommand.isPowered() || tileentitycommand.isAuto()) {
                CommandBlockBaseLogic commandblocklistenerabstract = tileentitycommand.getCommandBlockLogic();

                if (tileentitycommand.setConditionMet()) {
                    if (!commandblocklistenerabstract.trigger(world)) {
                        break;
                    }

                    world.updateComparatorOutputLevel(blockposition_mutableblockposition, block);
                } else if (tileentitycommand.isConditional()) {
                    commandblocklistenerabstract.setSuccessCount(0);
                }
            }
        }

        if (i <= 0) {
            int j = Math.max(gamerules.getInt("maxCommandChainLength"), 0);

            BlockCommandBlock.LOGGER.warn("Commandblock chain tried to execure more than " + j + " steps!");
        }

    }
}
