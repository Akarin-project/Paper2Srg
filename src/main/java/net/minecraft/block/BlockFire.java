package net.minecraft.block;

import com.google.common.collect.Maps;
import java.util.Map;
import java.util.Random;
import javax.annotation.Nullable;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldProviderEnd;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockSpreadEvent;

// CraftBukkit start
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockSpreadEvent;
// CraftBukkit end

public class BlockFire extends Block {

    public static final PropertyInteger field_176543_a = PropertyInteger.func_177719_a("age", 0, 15);
    public static final PropertyBool field_176545_N = PropertyBool.func_177716_a("north");
    public static final PropertyBool field_176546_O = PropertyBool.func_177716_a("east");
    public static final PropertyBool field_176541_P = PropertyBool.func_177716_a("south");
    public static final PropertyBool field_176539_Q = PropertyBool.func_177716_a("west");
    public static final PropertyBool field_176542_R = PropertyBool.func_177716_a("up");
    private final Map<Block, Integer> field_149849_a = Maps.newIdentityHashMap();
    private final Map<Block, Integer> field_149848_b = Maps.newIdentityHashMap();

    public IBlockState func_176221_a(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        return !iblockaccess.func_180495_p(blockposition.func_177977_b()).func_185896_q() && !Blocks.field_150480_ab.func_176535_e(iblockaccess, blockposition.func_177977_b()) ? iblockdata.func_177226_a(BlockFire.field_176545_N, Boolean.valueOf(this.func_176535_e(iblockaccess, blockposition.func_177978_c()))).func_177226_a(BlockFire.field_176546_O, Boolean.valueOf(this.func_176535_e(iblockaccess, blockposition.func_177974_f()))).func_177226_a(BlockFire.field_176541_P, Boolean.valueOf(this.func_176535_e(iblockaccess, blockposition.func_177968_d()))).func_177226_a(BlockFire.field_176539_Q, Boolean.valueOf(this.func_176535_e(iblockaccess, blockposition.func_177976_e()))).func_177226_a(BlockFire.field_176542_R, Boolean.valueOf(this.func_176535_e(iblockaccess, blockposition.func_177984_a()))) : this.func_176223_P();
    }

    protected BlockFire() {
        super(Material.field_151581_o);
        this.func_180632_j(this.field_176227_L.func_177621_b().func_177226_a(BlockFire.field_176543_a, Integer.valueOf(0)).func_177226_a(BlockFire.field_176545_N, Boolean.valueOf(false)).func_177226_a(BlockFire.field_176546_O, Boolean.valueOf(false)).func_177226_a(BlockFire.field_176541_P, Boolean.valueOf(false)).func_177226_a(BlockFire.field_176539_Q, Boolean.valueOf(false)).func_177226_a(BlockFire.field_176542_R, Boolean.valueOf(false)));
        this.func_149675_a(true);
    }

    public static void func_149843_e() {
        Blocks.field_150480_ab.func_180686_a(Blocks.field_150344_f, 5, 20);
        Blocks.field_150480_ab.func_180686_a(Blocks.field_150373_bw, 5, 20);
        Blocks.field_150480_ab.func_180686_a(Blocks.field_150376_bx, 5, 20);
        Blocks.field_150480_ab.func_180686_a(Blocks.field_180390_bo, 5, 20);
        Blocks.field_150480_ab.func_180686_a(Blocks.field_180391_bp, 5, 20);
        Blocks.field_150480_ab.func_180686_a(Blocks.field_180392_bq, 5, 20);
        Blocks.field_150480_ab.func_180686_a(Blocks.field_180386_br, 5, 20);
        Blocks.field_150480_ab.func_180686_a(Blocks.field_180385_bs, 5, 20);
        Blocks.field_150480_ab.func_180686_a(Blocks.field_180387_bt, 5, 20);
        Blocks.field_150480_ab.func_180686_a(Blocks.field_180407_aO, 5, 20);
        Blocks.field_150480_ab.func_180686_a(Blocks.field_180408_aP, 5, 20);
        Blocks.field_150480_ab.func_180686_a(Blocks.field_180404_aQ, 5, 20);
        Blocks.field_150480_ab.func_180686_a(Blocks.field_180403_aR, 5, 20);
        Blocks.field_150480_ab.func_180686_a(Blocks.field_180406_aS, 5, 20);
        Blocks.field_150480_ab.func_180686_a(Blocks.field_180405_aT, 5, 20);
        Blocks.field_150480_ab.func_180686_a(Blocks.field_150476_ad, 5, 20);
        Blocks.field_150480_ab.func_180686_a(Blocks.field_150487_bG, 5, 20);
        Blocks.field_150480_ab.func_180686_a(Blocks.field_150485_bF, 5, 20);
        Blocks.field_150480_ab.func_180686_a(Blocks.field_150481_bH, 5, 20);
        Blocks.field_150480_ab.func_180686_a(Blocks.field_150400_ck, 5, 20);
        Blocks.field_150480_ab.func_180686_a(Blocks.field_150401_cl, 5, 20);
        Blocks.field_150480_ab.func_180686_a(Blocks.field_150364_r, 5, 5);
        Blocks.field_150480_ab.func_180686_a(Blocks.field_150363_s, 5, 5);
        Blocks.field_150480_ab.func_180686_a(Blocks.field_150362_t, 30, 60);
        Blocks.field_150480_ab.func_180686_a(Blocks.field_150361_u, 30, 60);
        Blocks.field_150480_ab.func_180686_a(Blocks.field_150342_X, 30, 20);
        Blocks.field_150480_ab.func_180686_a(Blocks.field_150335_W, 15, 100);
        Blocks.field_150480_ab.func_180686_a(Blocks.field_150329_H, 60, 100);
        Blocks.field_150480_ab.func_180686_a(Blocks.field_150398_cm, 60, 100);
        Blocks.field_150480_ab.func_180686_a(Blocks.field_150327_N, 60, 100);
        Blocks.field_150480_ab.func_180686_a(Blocks.field_150328_O, 60, 100);
        Blocks.field_150480_ab.func_180686_a(Blocks.field_150330_I, 60, 100);
        Blocks.field_150480_ab.func_180686_a(Blocks.field_150325_L, 30, 60);
        Blocks.field_150480_ab.func_180686_a(Blocks.field_150395_bd, 15, 100);
        Blocks.field_150480_ab.func_180686_a(Blocks.field_150402_ci, 5, 5);
        Blocks.field_150480_ab.func_180686_a(Blocks.field_150407_cf, 60, 20);
        Blocks.field_150480_ab.func_180686_a(Blocks.field_150404_cg, 60, 20);
    }

    public void func_180686_a(Block block, int i, int j) {
        this.field_149849_a.put(block, Integer.valueOf(i));
        this.field_149848_b.put(block, Integer.valueOf(j));
    }

    @Nullable
    public AxisAlignedBB func_180646_a(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        return BlockFire.field_185506_k;
    }

    public boolean func_149662_c(IBlockState iblockdata) {
        return false;
    }

    public boolean func_149686_d(IBlockState iblockdata) {
        return false;
    }

    public int func_149745_a(Random random) {
        return 0;
    }

    public int func_149738_a(World world) {
        return 30;
    }

    public void func_180650_b(World world, BlockPos blockposition, IBlockState iblockdata, Random random) {
        if (world.func_82736_K().func_82766_b("doFireTick")) {
            if (!this.func_176196_c(world, blockposition)) {
                fireExtinguished(world, blockposition); // CraftBukkit - invalid place location
            }

            Block block = world.func_180495_p(blockposition.func_177977_b()).func_177230_c();
            boolean flag = block == Blocks.field_150424_aL || block == Blocks.field_189877_df;

            if (world.field_73011_w instanceof WorldProviderEnd && block == Blocks.field_150357_h) {
                flag = true;
            }

            int i = ((Integer) iblockdata.func_177229_b(BlockFire.field_176543_a)).intValue();

            if (!flag && world.func_72896_J() && this.func_176537_d(world, blockposition) && random.nextFloat() < 0.2F + (float) i * 0.03F) {
                fireExtinguished(world, blockposition); // CraftBukkit - extinguished by rain
            } else {
                if (i < 15) {
                    iblockdata = iblockdata.func_177226_a(BlockFire.field_176543_a, Integer.valueOf(i + random.nextInt(3) / 2));
                    world.func_180501_a(blockposition, iblockdata, 4);
                }

                world.func_175684_a(blockposition, (Block) this, this.func_149738_a(world) + random.nextInt(10));
                if (!flag) {
                    if (!this.func_176533_e(world, blockposition)) {
                        if (!world.func_180495_p(blockposition.func_177977_b()).func_185896_q() || i > 3) {
                            fireExtinguished(world, blockposition); // CraftBukkit
                        }

                        return;
                    }

                    if (!this.func_176535_e((IBlockAccess) world, blockposition.func_177977_b()) && i == 15 && random.nextInt(4) == 0) {
                        fireExtinguished(world, blockposition); // CraftBukkit
                        return;
                    }
                }

                boolean flag1 = world.func_180502_D(blockposition);
                byte b0 = 0;

                if (flag1) {
                    b0 = -50;
                }

                // CraftBukkit start - add source blockposition to burn calls
                this.a(world, blockposition.func_177974_f(), 300 + b0, random, i, blockposition);
                this.a(world, blockposition.func_177976_e(), 300 + b0, random, i, blockposition);
                this.a(world, blockposition.func_177977_b(), 250 + b0, random, i, blockposition);
                this.a(world, blockposition.func_177984_a(), 250 + b0, random, i, blockposition);
                this.a(world, blockposition.func_177978_c(), 300 + b0, random, i, blockposition);
                this.a(world, blockposition.func_177968_d(), 300 + b0, random, i, blockposition);
                // CraftBukkit end

                for (int j = -1; j <= 1; ++j) {
                    for (int k = -1; k <= 1; ++k) {
                        for (int l = -1; l <= 4; ++l) {
                            if (j != 0 || l != 0 || k != 0) {
                                int i1 = 100;

                                if (l > 1) {
                                    i1 += (l - 1) * 100;
                                }

                                BlockPos blockposition1 = blockposition.func_177982_a(j, l, k);
                                if (!world.func_175667_e(blockposition1)) continue; // Paper
                                int j1 = this.func_176538_m(world, blockposition1);

                                if (j1 > 0) {
                                    int k1 = (j1 + 40 + world.func_175659_aa().func_151525_a() * 7) / (i + 30);

                                    if (flag1) {
                                        k1 /= 2;
                                    }

                                    if (k1 > 0 && random.nextInt(i1) <= k1 && (!world.func_72896_J() || !this.func_176537_d(world, blockposition1))) {
                                        int l1 = i + random.nextInt(5) / 4;

                                        if (l1 > 15) {
                                            l1 = 15;
                                        }

                                        // CraftBukkit start - Call to stop spread of fire
                                        if (world.func_180495_p(blockposition1) != Blocks.field_150480_ab) {
                                            if (CraftEventFactory.callBlockIgniteEvent(world, blockposition1.func_177958_n(), blockposition1.func_177956_o(), blockposition1.func_177952_p(), blockposition.func_177958_n(), blockposition.func_177956_o(), blockposition.func_177952_p()).isCancelled()) {
                                                continue;
                                            }

                                            org.bukkit.Server server = world.getServer();
                                            org.bukkit.World bworld = world.getWorld();
                                            org.bukkit.block.BlockState blockState = bworld.getBlockAt(blockposition1.func_177958_n(), blockposition1.func_177956_o(), blockposition1.func_177952_p()).getState();
                                            blockState.setTypeId(Block.func_149682_b(this));
                                            blockState.setData(new org.bukkit.material.MaterialData(Block.func_149682_b(this), (byte) l1));

                                            BlockSpreadEvent spreadEvent = new BlockSpreadEvent(blockState.getBlock(), bworld.getBlockAt(blockposition.func_177958_n(), blockposition.func_177956_o(), blockposition.func_177952_p()), blockState);
                                            server.getPluginManager().callEvent(spreadEvent);

                                            if (!spreadEvent.isCancelled()) {
                                                blockState.update(true);
                                            }
                                        }
                                        // CraftBukkit end
                                    }
                                }
                            }
                        }
                    }
                }

            }
        }
    }

    protected boolean func_176537_d(World world, BlockPos blockposition) {
        return world.func_175727_C(blockposition) || world.func_175727_C(blockposition.func_177976_e()) || world.func_175727_C(blockposition.func_177974_f()) || world.func_175727_C(blockposition.func_177978_c()) || world.func_175727_C(blockposition.func_177968_d());
    }

    public boolean func_149698_L() {
        return false;
    }

    int func_176532_c(Block block) {
        Integer integer = (Integer) this.field_149848_b.get(block);

        return integer == null ? 0 : integer.intValue();
    }

    public int func_176534_d(Block block) {
        Integer integer = (Integer) this.field_149849_a.get(block);

        return integer == null ? 0 : integer.intValue();
    }

    private void a(World world, BlockPos blockposition, int i, Random random, int j, BlockPos sourceposition) { // CraftBukkit add sourceposition
        // Paper start
        final IBlockState iblockdata = world.getTypeIfLoaded(blockposition);
        if (iblockdata == null) return;
        int k = this.func_176532_c(world.func_180495_p(blockposition).func_177230_c());

        if (random.nextInt(i) < k) {
            //IBlockData iblockdata = world.getType(blockposition); // Paper

            // CraftBukkit start
            org.bukkit.block.Block theBlock = world.getWorld().getBlockAt(blockposition.func_177958_n(), blockposition.func_177956_o(), blockposition.func_177952_p());
            org.bukkit.block.Block sourceBlock = world.getWorld().getBlockAt(sourceposition.func_177958_n(), sourceposition.func_177956_o(), sourceposition.func_177952_p());

            BlockBurnEvent event = new BlockBurnEvent(theBlock, sourceBlock);
            world.getServer().getPluginManager().callEvent(event);

            if (event.isCancelled()) {
                return;
            }
            // CraftBukkit end

            if (random.nextInt(j + 10) < 5 && !world.func_175727_C(blockposition)) {
                int l = j + random.nextInt(5) / 4;

                if (l > 15) {
                    l = 15;
                }

                world.func_180501_a(blockposition, this.func_176223_P().func_177226_a(BlockFire.field_176543_a, Integer.valueOf(l)), 3);
            } else {
                world.func_175698_g(blockposition);
            }

            if (iblockdata.func_177230_c() == Blocks.field_150335_W) {
                Blocks.field_150335_W.func_176206_d(world, blockposition, iblockdata.func_177226_a(BlockTNT.field_176246_a, Boolean.valueOf(true)));
            }
        }

    }

    private boolean func_176533_e(World world, BlockPos blockposition) {
        EnumFacing[] aenumdirection = EnumFacing.values();
        int i = aenumdirection.length;

        for (int j = 0; j < i; ++j) {
            EnumFacing enumdirection = aenumdirection[j];

            if (this.func_176535_e((IBlockAccess) world, blockposition.func_177972_a(enumdirection))) {
                return true;
            }
        }

        return false;
    }

    private int func_176538_m(World world, BlockPos blockposition) {
        if (!world.func_175623_d(blockposition)) {
            return 0;
        } else {
            int i = 0;
            EnumFacing[] aenumdirection = EnumFacing.values();
            int j = aenumdirection.length;

            for (int k = 0; k < j; ++k) {
                EnumFacing enumdirection = aenumdirection[k];

                final IBlockState type = world.getTypeIfLoaded(blockposition.func_177972_a(enumdirection)); // Paper
                if (type == null) continue; // Paper
                i = Math.max(this.func_176534_d(world.func_180495_p(blockposition.func_177972_a(enumdirection)).func_177230_c()), i);
            }

            return i;
        }
    }

    public boolean func_149703_v() {
        return false;
    }

    public boolean func_176535_e(IBlockAccess iblockaccess, BlockPos blockposition) {
        return this.func_176534_d(iblockaccess.func_180495_p(blockposition).func_177230_c()) > 0;
    }

    public boolean func_176196_c(World world, BlockPos blockposition) {
        return world.func_180495_p(blockposition.func_177977_b()).func_185896_q() || this.func_176533_e(world, blockposition);
    }

    public void func_189540_a(IBlockState iblockdata, World world, BlockPos blockposition, Block block, BlockPos blockposition1) {
        if (!world.func_180495_p(blockposition.func_177977_b()).func_185896_q() && !this.func_176533_e(world, blockposition)) {
            fireExtinguished(world, blockposition); // CraftBukkit - fuel block gone
        }

    }

    public void func_176213_c(World world, BlockPos blockposition, IBlockState iblockdata) {
        if (world.field_73011_w.func_186058_p().func_186068_a() > 0 || !Blocks.field_150427_aO.func_176548_d(world, blockposition)) {
            if (!world.func_180495_p(blockposition.func_177977_b()).func_185896_q() && !this.func_176533_e(world, blockposition)) {
                fireExtinguished(world, blockposition); // CraftBukkit - fuel block broke
            } else {
                world.func_175684_a(blockposition, (Block) this, this.func_149738_a(world) + world.field_73012_v.nextInt(10));
            }
        }
    }

    public MapColor func_180659_g(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        return MapColor.field_151656_f;
    }

    public IBlockState func_176203_a(int i) {
        return this.func_176223_P().func_177226_a(BlockFire.field_176543_a, Integer.valueOf(i));
    }

    public int func_176201_c(IBlockState iblockdata) {
        return ((Integer) iblockdata.func_177229_b(BlockFire.field_176543_a)).intValue();
    }

    protected BlockStateContainer func_180661_e() {
        return new BlockStateContainer(this, new IProperty[] { BlockFire.field_176543_a, BlockFire.field_176545_N, BlockFire.field_176546_O, BlockFire.field_176541_P, BlockFire.field_176539_Q, BlockFire.field_176542_R});
    }

    public BlockFaceShape func_193383_a(IBlockAccess iblockaccess, IBlockState iblockdata, BlockPos blockposition, EnumFacing enumdirection) {
        return BlockFaceShape.UNDEFINED;
    }

    // CraftBukkit start
    private void fireExtinguished(World world, BlockPos position) {
        if (!CraftEventFactory.callBlockFadeEvent(world.getWorld().getBlockAt(position.func_177958_n(), position.func_177956_o(), position.func_177952_p()), Blocks.field_150350_a).isCancelled()) {
            world.func_175698_g(position);
        }
    }
    // CraftBukkit end
}
