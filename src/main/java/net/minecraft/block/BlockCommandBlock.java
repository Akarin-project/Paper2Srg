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

    private static final Logger field_193388_c = LogManager.getLogger();
    public static final PropertyDirection field_185564_a = BlockDirectional.field_176387_N;
    public static final PropertyBool field_185565_b = PropertyBool.func_177716_a("conditional");

    public BlockCommandBlock(MapColor materialmapcolor) {
        super(Material.field_151573_f, materialmapcolor);
        this.func_180632_j(this.field_176227_L.func_177621_b().func_177226_a(BlockCommandBlock.field_185564_a, EnumFacing.NORTH).func_177226_a(BlockCommandBlock.field_185565_b, Boolean.valueOf(false)));
    }

    public TileEntity func_149915_a(World world, int i) {
        TileEntityCommandBlock tileentitycommand = new TileEntityCommandBlock();

        tileentitycommand.func_184253_b(this == Blocks.field_185777_dd);
        return tileentitycommand;
    }

    public void func_189540_a(IBlockState iblockdata, World world, BlockPos blockposition, Block block, BlockPos blockposition1) {
        if (!world.field_72995_K) {
            TileEntity tileentity = world.func_175625_s(blockposition);

            if (tileentity instanceof TileEntityCommandBlock) {
                TileEntityCommandBlock tileentitycommand = (TileEntityCommandBlock) tileentity;
                boolean flag = world.func_175640_z(blockposition);
                boolean flag1 = tileentitycommand.func_184255_d();
                // CraftBukkit start
                org.bukkit.block.Block bukkitBlock = world.getWorld().getBlockAt(blockposition.func_177958_n(), blockposition.func_177956_o(), blockposition.func_177952_p());
                int old = flag1 ? 15 : 0;
                int current = flag ? 15 : 0;

                BlockRedstoneEvent eventRedstone = new BlockRedstoneEvent(bukkitBlock, old, current);
                world.getServer().getPluginManager().callEvent(eventRedstone);
                flag = eventRedstone.getNewCurrent() > 0;
                // CraftBukkit end

                tileentitycommand.func_184250_a(flag);
                if (!flag1 && !tileentitycommand.func_184254_e() && tileentitycommand.func_184251_i() != TileEntityCommandBlock.Mode.SEQUENCE) {
                    if (flag) {
                        tileentitycommand.func_184249_c();
                        world.func_175684_a(blockposition, (Block) this, this.func_149738_a(world));
                    }

                }
            }
        }
    }

    public void func_180650_b(World world, BlockPos blockposition, IBlockState iblockdata, Random random) {
        if (!world.field_72995_K) {
            TileEntity tileentity = world.func_175625_s(blockposition);

            if (tileentity instanceof TileEntityCommandBlock) {
                TileEntityCommandBlock tileentitycommand = (TileEntityCommandBlock) tileentity;
                CommandBlockBaseLogic commandblocklistenerabstract = tileentitycommand.func_145993_a();
                boolean flag = !StringUtils.func_151246_b(commandblocklistenerabstract.func_145753_i());
                TileEntityCommandBlock.Mode tileentitycommand_type = tileentitycommand.func_184251_i();
                boolean flag1 = tileentitycommand.func_184256_g();

                if (tileentitycommand_type == TileEntityCommandBlock.Mode.AUTO) {
                    tileentitycommand.func_184249_c();
                    if (flag1) {
                        this.func_193387_a(iblockdata, world, blockposition, commandblocklistenerabstract, flag);
                    } else if (tileentitycommand.func_184258_j()) {
                        commandblocklistenerabstract.func_184167_a(0);
                    }

                    if (tileentitycommand.func_184255_d() || tileentitycommand.func_184254_e()) {
                        world.func_175684_a(blockposition, (Block) this, this.func_149738_a(world));
                    }
                } else if (tileentitycommand_type == TileEntityCommandBlock.Mode.REDSTONE) {
                    if (flag1) {
                        this.func_193387_a(iblockdata, world, blockposition, commandblocklistenerabstract, flag);
                    } else if (tileentitycommand.func_184258_j()) {
                        commandblocklistenerabstract.func_184167_a(0);
                    }
                }

                world.func_175666_e(blockposition, this);
            }

        }
    }

    private void func_193387_a(IBlockState iblockdata, World world, BlockPos blockposition, CommandBlockBaseLogic commandblocklistenerabstract, boolean flag) {
        if (flag) {
            commandblocklistenerabstract.func_145755_a(world);
        } else {
            commandblocklistenerabstract.func_184167_a(0);
        }

        func_193386_c(world, blockposition, (EnumFacing) iblockdata.func_177229_b(BlockCommandBlock.field_185564_a));
    }

    public int func_149738_a(World world) {
        return 1;
    }

    public boolean func_180639_a(World world, BlockPos blockposition, IBlockState iblockdata, EntityPlayer entityhuman, EnumHand enumhand, EnumFacing enumdirection, float f, float f1, float f2) {
        TileEntity tileentity = world.func_175625_s(blockposition);

        if (tileentity instanceof TileEntityCommandBlock && entityhuman.func_189808_dh()) {
            entityhuman.func_184824_a((TileEntityCommandBlock) tileentity);
            return true;
        } else {
            return false;
        }
    }

    public boolean func_149740_M(IBlockState iblockdata) {
        return true;
    }

    public int func_180641_l(IBlockState iblockdata, World world, BlockPos blockposition) {
        TileEntity tileentity = world.func_175625_s(blockposition);

        return tileentity instanceof TileEntityCommandBlock ? ((TileEntityCommandBlock) tileentity).func_145993_a().func_145760_g() : 0;
    }

    public void func_180633_a(World world, BlockPos blockposition, IBlockState iblockdata, EntityLivingBase entityliving, ItemStack itemstack) {
        TileEntity tileentity = world.func_175625_s(blockposition);

        if (tileentity instanceof TileEntityCommandBlock) {
            TileEntityCommandBlock tileentitycommand = (TileEntityCommandBlock) tileentity;
            CommandBlockBaseLogic commandblocklistenerabstract = tileentitycommand.func_145993_a();

            if (itemstack.func_82837_s()) {
                commandblocklistenerabstract.func_145754_b(itemstack.func_82833_r());
            }

            if (!world.field_72995_K) {
                NBTTagCompound nbttagcompound = itemstack.func_77978_p();

                if (nbttagcompound == null || !nbttagcompound.func_150297_b("BlockEntityTag", 10)) {
                    commandblocklistenerabstract.func_175573_a(world.func_82736_K().func_82766_b("sendCommandFeedback"));
                    tileentitycommand.func_184253_b(this == Blocks.field_185777_dd);
                }

                if (tileentitycommand.func_184251_i() == TileEntityCommandBlock.Mode.SEQUENCE) {
                    boolean flag = world.func_175640_z(blockposition);

                    tileentitycommand.func_184250_a(flag);
                }
            }

        }
    }

    public int func_149745_a(Random random) {
        return 0;
    }

    public EnumBlockRenderType func_149645_b(IBlockState iblockdata) {
        return EnumBlockRenderType.MODEL;
    }

    public IBlockState func_176203_a(int i) {
        return this.func_176223_P().func_177226_a(BlockCommandBlock.field_185564_a, EnumFacing.func_82600_a(i & 7)).func_177226_a(BlockCommandBlock.field_185565_b, Boolean.valueOf((i & 8) != 0));
    }

    public int func_176201_c(IBlockState iblockdata) {
        return ((EnumFacing) iblockdata.func_177229_b(BlockCommandBlock.field_185564_a)).func_176745_a() | (((Boolean) iblockdata.func_177229_b(BlockCommandBlock.field_185565_b)).booleanValue() ? 8 : 0);
    }

    public IBlockState func_185499_a(IBlockState iblockdata, Rotation enumblockrotation) {
        return iblockdata.func_177226_a(BlockCommandBlock.field_185564_a, enumblockrotation.func_185831_a((EnumFacing) iblockdata.func_177229_b(BlockCommandBlock.field_185564_a)));
    }

    public IBlockState func_185471_a(IBlockState iblockdata, Mirror enumblockmirror) {
        return iblockdata.func_185907_a(enumblockmirror.func_185800_a((EnumFacing) iblockdata.func_177229_b(BlockCommandBlock.field_185564_a)));
    }

    protected BlockStateContainer func_180661_e() {
        return new BlockStateContainer(this, new IProperty[] { BlockCommandBlock.field_185564_a, BlockCommandBlock.field_185565_b});
    }

    public IBlockState func_180642_a(World world, BlockPos blockposition, EnumFacing enumdirection, float f, float f1, float f2, int i, EntityLivingBase entityliving) {
        return this.func_176223_P().func_177226_a(BlockCommandBlock.field_185564_a, EnumFacing.func_190914_a(blockposition, entityliving)).func_177226_a(BlockCommandBlock.field_185565_b, Boolean.valueOf(false));
    }

    private static void func_193386_c(World world, BlockPos blockposition, EnumFacing enumdirection) {
        BlockPos.MutableBlockPos blockposition_mutableblockposition = new BlockPos.MutableBlockPos(blockposition);
        GameRules gamerules = world.func_82736_K();

        int i;
        IBlockState iblockdata;

        for (i = gamerules.func_180263_c("maxCommandChainLength"); i-- > 0; enumdirection = (EnumFacing) iblockdata.func_177229_b(BlockCommandBlock.field_185564_a)) {
            blockposition_mutableblockposition.func_189536_c(enumdirection);
            iblockdata = world.func_180495_p(blockposition_mutableblockposition);
            Block block = iblockdata.func_177230_c();

            if (block != Blocks.field_185777_dd) {
                break;
            }

            TileEntity tileentity = world.func_175625_s(blockposition_mutableblockposition);

            if (!(tileentity instanceof TileEntityCommandBlock)) {
                break;
            }

            TileEntityCommandBlock tileentitycommand = (TileEntityCommandBlock) tileentity;

            if (tileentitycommand.func_184251_i() != TileEntityCommandBlock.Mode.SEQUENCE) {
                break;
            }

            if (tileentitycommand.func_184255_d() || tileentitycommand.func_184254_e()) {
                CommandBlockBaseLogic commandblocklistenerabstract = tileentitycommand.func_145993_a();

                if (tileentitycommand.func_184249_c()) {
                    if (!commandblocklistenerabstract.func_145755_a(world)) {
                        break;
                    }

                    world.func_175666_e(blockposition_mutableblockposition, block);
                } else if (tileentitycommand.func_184258_j()) {
                    commandblocklistenerabstract.func_184167_a(0);
                }
            }
        }

        if (i <= 0) {
            int j = Math.max(gamerules.func_180263_c("maxCommandChainLength"), 0);

            BlockCommandBlock.field_193388_c.warn("Commandblock chain tried to execure more than " + j + " steps!");
        }

    }
}
