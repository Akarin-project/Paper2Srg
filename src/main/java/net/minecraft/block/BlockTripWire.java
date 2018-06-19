package net.minecraft.block;

import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;


import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.bukkit.event.entity.EntityInteractEvent;

public class BlockTripWire extends Block {

    public static final PropertyBool field_176293_a = PropertyBool.func_177716_a("powered");
    public static final PropertyBool field_176294_M = PropertyBool.func_177716_a("attached");
    public static final PropertyBool field_176295_N = PropertyBool.func_177716_a("disarmed");
    public static final PropertyBool field_176296_O = PropertyBool.func_177716_a("north");
    public static final PropertyBool field_176291_P = PropertyBool.func_177716_a("east");
    public static final PropertyBool field_176289_Q = PropertyBool.func_177716_a("south");
    public static final PropertyBool field_176292_R = PropertyBool.func_177716_a("west");
    protected static final AxisAlignedBB field_185747_B = new AxisAlignedBB(0.0D, 0.0625D, 0.0D, 1.0D, 0.15625D, 1.0D);
    protected static final AxisAlignedBB field_185748_C = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D);

    public BlockTripWire() {
        super(Material.field_151594_q);
        this.func_180632_j(this.field_176227_L.func_177621_b().func_177226_a(BlockTripWire.field_176293_a, Boolean.valueOf(false)).func_177226_a(BlockTripWire.field_176294_M, Boolean.valueOf(false)).func_177226_a(BlockTripWire.field_176295_N, Boolean.valueOf(false)).func_177226_a(BlockTripWire.field_176296_O, Boolean.valueOf(false)).func_177226_a(BlockTripWire.field_176291_P, Boolean.valueOf(false)).func_177226_a(BlockTripWire.field_176289_Q, Boolean.valueOf(false)).func_177226_a(BlockTripWire.field_176292_R, Boolean.valueOf(false)));
        this.func_149675_a(true);
    }

    public AxisAlignedBB func_185496_a(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        return !((Boolean) iblockdata.func_177229_b(BlockTripWire.field_176294_M)).booleanValue() ? BlockTripWire.field_185748_C : BlockTripWire.field_185747_B;
    }

    public IBlockState func_176221_a(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        return iblockdata.func_177226_a(BlockTripWire.field_176296_O, Boolean.valueOf(func_176287_c(iblockaccess, blockposition, iblockdata, EnumFacing.NORTH))).func_177226_a(BlockTripWire.field_176291_P, Boolean.valueOf(func_176287_c(iblockaccess, blockposition, iblockdata, EnumFacing.EAST))).func_177226_a(BlockTripWire.field_176289_Q, Boolean.valueOf(func_176287_c(iblockaccess, blockposition, iblockdata, EnumFacing.SOUTH))).func_177226_a(BlockTripWire.field_176292_R, Boolean.valueOf(func_176287_c(iblockaccess, blockposition, iblockdata, EnumFacing.WEST)));
    }

    @Nullable
    public AxisAlignedBB func_180646_a(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        return BlockTripWire.field_185506_k;
    }

    public boolean func_149662_c(IBlockState iblockdata) {
        return false;
    }

    public boolean func_149686_d(IBlockState iblockdata) {
        return false;
    }

    public Item func_180660_a(IBlockState iblockdata, Random random, int i) {
        return Items.field_151007_F;
    }

    public ItemStack func_185473_a(World world, BlockPos blockposition, IBlockState iblockdata) {
        return new ItemStack(Items.field_151007_F);
    }

    public void func_176213_c(World world, BlockPos blockposition, IBlockState iblockdata) {
        world.func_180501_a(blockposition, iblockdata, 3);
        this.func_176286_e(world, blockposition, iblockdata);
    }

    public void func_180663_b(World world, BlockPos blockposition, IBlockState iblockdata) {
        this.func_176286_e(world, blockposition, iblockdata.func_177226_a(BlockTripWire.field_176293_a, Boolean.valueOf(true)));
    }

    public void func_176208_a(World world, BlockPos blockposition, IBlockState iblockdata, EntityPlayer entityhuman) {
        if (!world.field_72995_K) {
            if (!entityhuman.func_184614_ca().func_190926_b() && entityhuman.func_184614_ca().func_77973_b() == Items.field_151097_aZ) {
                world.func_180501_a(blockposition, iblockdata.func_177226_a(BlockTripWire.field_176295_N, Boolean.valueOf(true)), 4);
            }

        }
    }

    private void func_176286_e(World world, BlockPos blockposition, IBlockState iblockdata) {
        EnumFacing[] aenumdirection = new EnumFacing[] { EnumFacing.SOUTH, EnumFacing.WEST};
        int i = aenumdirection.length;
        int j = 0;

        while (j < i) {
            EnumFacing enumdirection = aenumdirection[j];
            int k = 1;

            while (true) {
                if (k < 42) {
                    BlockPos blockposition1 = blockposition.func_177967_a(enumdirection, k);
                    IBlockState iblockdata1 = world.func_180495_p(blockposition1);

                    if (iblockdata1.func_177230_c() == Blocks.field_150479_bC) {
                        if (iblockdata1.func_177229_b(BlockTripWireHook.field_176264_a) == enumdirection.func_176734_d()) {
                            Blocks.field_150479_bC.func_176260_a(world, blockposition1, iblockdata1, false, true, k, iblockdata);
                        }
                    } else if (iblockdata1.func_177230_c() == Blocks.field_150473_bD) {
                        ++k;
                        continue;
                    }
                }

                ++j;
                break;
            }
        }

    }

    public void func_180634_a(World world, BlockPos blockposition, IBlockState iblockdata, Entity entity) {
        if (!world.field_72995_K) {
            if (!((Boolean) iblockdata.func_177229_b(BlockTripWire.field_176293_a)).booleanValue()) {
                this.func_176288_d(world, blockposition);
            }
        }
    }

    public void func_180645_a(World world, BlockPos blockposition, IBlockState iblockdata, Random random) {}

    public void func_180650_b(World world, BlockPos blockposition, IBlockState iblockdata, Random random) {
        if (!world.field_72995_K) {
            if (((Boolean) world.func_180495_p(blockposition).func_177229_b(BlockTripWire.field_176293_a)).booleanValue()) {
                this.func_176288_d(world, blockposition);
            }
        }
    }

    private void func_176288_d(World world, BlockPos blockposition) {
        IBlockState iblockdata = world.func_180495_p(blockposition);
        boolean flag = ((Boolean) iblockdata.func_177229_b(BlockTripWire.field_176293_a)).booleanValue();
        boolean flag1 = false;
        List list = world.func_72839_b((Entity) null, iblockdata.func_185900_c(world, blockposition).func_186670_a(blockposition));

        if (!list.isEmpty()) {
            Iterator iterator = list.iterator();

            while (iterator.hasNext()) {
                Entity entity = (Entity) iterator.next();

                if (!entity.func_145773_az()) {
                    flag1 = true;
                    break;
                }
            }
        }

        // CraftBukkit start - Call interact even when triggering connected tripwire
        if (flag != flag1 && flag1 && (Boolean)iblockdata.func_177229_b(field_176294_M)) {
            org.bukkit.World bworld = world.getWorld();
            org.bukkit.plugin.PluginManager manager = world.getServer().getPluginManager();
            org.bukkit.block.Block block = bworld.getBlockAt(blockposition.func_177958_n(), blockposition.func_177956_o(), blockposition.func_177952_p());
            boolean allowed = false;

            // If all of the events are cancelled block the tripwire trigger, else allow
            for (Object object : list) {
                if (object != null) {
                    org.bukkit.event.Cancellable cancellable;

                    if (object instanceof EntityPlayer) {
                        cancellable = org.bukkit.craftbukkit.event.CraftEventFactory.callPlayerInteractEvent((EntityPlayer) object, org.bukkit.event.block.Action.PHYSICAL, blockposition, null, null, null);
                    } else if (object instanceof Entity) {
                        cancellable = new EntityInteractEvent(((Entity) object).getBukkitEntity(), block);
                        manager.callEvent((EntityInteractEvent) cancellable);
                    } else {
                        continue;
                    }

                    if (!cancellable.isCancelled()) {
                        allowed = true;
                        break;
                    }
                }
            }

            if (!allowed) {
                return;
            }
        }
        // CraftBukkit end

        if (flag1 != flag) {
            iblockdata = iblockdata.func_177226_a(BlockTripWire.field_176293_a, Boolean.valueOf(flag1));
            world.func_180501_a(blockposition, iblockdata, 3);
            this.func_176286_e(world, blockposition, iblockdata);
        }

        if (flag1) {
            world.func_175684_a(new BlockPos(blockposition), (Block) this, this.func_149738_a(world));
        }

    }

    public static boolean func_176287_c(IBlockAccess iblockaccess, BlockPos blockposition, IBlockState iblockdata, EnumFacing enumdirection) {
        BlockPos blockposition1 = blockposition.func_177972_a(enumdirection);
        IBlockState iblockdata1 = iblockaccess.func_180495_p(blockposition1);
        Block block = iblockdata1.func_177230_c();

        if (block == Blocks.field_150479_bC) {
            EnumFacing enumdirection1 = enumdirection.func_176734_d();

            return iblockdata1.func_177229_b(BlockTripWireHook.field_176264_a) == enumdirection1;
        } else {
            return block == Blocks.field_150473_bD;
        }
    }

    public IBlockState func_176203_a(int i) {
        return this.func_176223_P().func_177226_a(BlockTripWire.field_176293_a, Boolean.valueOf((i & 1) > 0)).func_177226_a(BlockTripWire.field_176294_M, Boolean.valueOf((i & 4) > 0)).func_177226_a(BlockTripWire.field_176295_N, Boolean.valueOf((i & 8) > 0));
    }

    public int func_176201_c(IBlockState iblockdata) {
        int i = 0;

        if (((Boolean) iblockdata.func_177229_b(BlockTripWire.field_176293_a)).booleanValue()) {
            i |= 1;
        }

        if (((Boolean) iblockdata.func_177229_b(BlockTripWire.field_176294_M)).booleanValue()) {
            i |= 4;
        }

        if (((Boolean) iblockdata.func_177229_b(BlockTripWire.field_176295_N)).booleanValue()) {
            i |= 8;
        }

        return i;
    }

    public IBlockState func_185499_a(IBlockState iblockdata, Rotation enumblockrotation) {
        switch (enumblockrotation) {
        case CLOCKWISE_180:
            return iblockdata.func_177226_a(BlockTripWire.field_176296_O, iblockdata.func_177229_b(BlockTripWire.field_176289_Q)).func_177226_a(BlockTripWire.field_176291_P, iblockdata.func_177229_b(BlockTripWire.field_176292_R)).func_177226_a(BlockTripWire.field_176289_Q, iblockdata.func_177229_b(BlockTripWire.field_176296_O)).func_177226_a(BlockTripWire.field_176292_R, iblockdata.func_177229_b(BlockTripWire.field_176291_P));

        case COUNTERCLOCKWISE_90:
            return iblockdata.func_177226_a(BlockTripWire.field_176296_O, iblockdata.func_177229_b(BlockTripWire.field_176291_P)).func_177226_a(BlockTripWire.field_176291_P, iblockdata.func_177229_b(BlockTripWire.field_176289_Q)).func_177226_a(BlockTripWire.field_176289_Q, iblockdata.func_177229_b(BlockTripWire.field_176292_R)).func_177226_a(BlockTripWire.field_176292_R, iblockdata.func_177229_b(BlockTripWire.field_176296_O));

        case CLOCKWISE_90:
            return iblockdata.func_177226_a(BlockTripWire.field_176296_O, iblockdata.func_177229_b(BlockTripWire.field_176292_R)).func_177226_a(BlockTripWire.field_176291_P, iblockdata.func_177229_b(BlockTripWire.field_176296_O)).func_177226_a(BlockTripWire.field_176289_Q, iblockdata.func_177229_b(BlockTripWire.field_176291_P)).func_177226_a(BlockTripWire.field_176292_R, iblockdata.func_177229_b(BlockTripWire.field_176289_Q));

        default:
            return iblockdata;
        }
    }

    public IBlockState func_185471_a(IBlockState iblockdata, Mirror enumblockmirror) {
        switch (enumblockmirror) {
        case LEFT_RIGHT:
            return iblockdata.func_177226_a(BlockTripWire.field_176296_O, iblockdata.func_177229_b(BlockTripWire.field_176289_Q)).func_177226_a(BlockTripWire.field_176289_Q, iblockdata.func_177229_b(BlockTripWire.field_176296_O));

        case FRONT_BACK:
            return iblockdata.func_177226_a(BlockTripWire.field_176291_P, iblockdata.func_177229_b(BlockTripWire.field_176292_R)).func_177226_a(BlockTripWire.field_176292_R, iblockdata.func_177229_b(BlockTripWire.field_176291_P));

        default:
            return super.func_185471_a(iblockdata, enumblockmirror);
        }
    }

    protected BlockStateContainer func_180661_e() {
        return new BlockStateContainer(this, new IProperty[] { BlockTripWire.field_176293_a, BlockTripWire.field_176294_M, BlockTripWire.field_176295_N, BlockTripWire.field_176296_O, BlockTripWire.field_176291_P, BlockTripWire.field_176292_R, BlockTripWire.field_176289_Q});
    }

    public BlockFaceShape func_193383_a(IBlockAccess iblockaccess, IBlockState iblockdata, BlockPos blockposition, EnumFacing enumdirection) {
        return BlockFaceShape.UNDEFINED;
    }
}
