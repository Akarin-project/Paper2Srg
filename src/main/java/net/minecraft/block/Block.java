package net.minecraft.block;

import com.google.common.collect.Sets;
import com.google.common.collect.UnmodifiableIterator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;

import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ObjectIntIdentityMap;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.RegistryNamespacedDefaultedByKey;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class Block {

    private static final ResourceLocation field_176230_a = new ResourceLocation("air");
    public static final RegistryNamespacedDefaultedByKey<ResourceLocation, Block> field_149771_c = new RegistryNamespacedDefaultedByKey(Block.field_176230_a);
    public static final ObjectIntIdentityMap<IBlockState> field_176229_d = new ObjectIntIdentityMap();
    public static final AxisAlignedBB field_185505_j = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
    @Nullable
    public static final AxisAlignedBB field_185506_k = null;
    private CreativeTabs field_149772_a;
    protected boolean field_149787_q;
    protected int field_149786_r;
    protected boolean field_149785_s;
    protected int field_149784_t;
    protected boolean field_149783_u;
    protected float field_149782_v;
    protected float field_149781_w;
    protected boolean field_149790_y;
    protected boolean field_149789_z;
    protected boolean field_149758_A;
    protected SoundType field_149762_H;
    public float field_149763_I;
    protected final Material field_149764_J;
    protected final MapColor field_181083_K;
    public float field_149765_K;
    protected final BlockStateContainer field_176227_L;
    private IBlockState field_176228_M;
    private String field_149770_b;
    // Paper start
    public co.aikar.timings.Timing timing;
    public co.aikar.timings.Timing getTiming() {
        if (timing == null) {
            timing = co.aikar.timings.MinecraftTimings.getBlockTiming(this);
        }
        return timing;
    }
    // Paper end

    public static int func_149682_b(Block block) {
        return Block.field_149771_c.func_148757_b(block); // CraftBukkit - decompile error
    }

    public static int func_176210_f(IBlockState iblockdata) {
        Block block = iblockdata.func_177230_c();

        return func_149682_b(block) + (block.func_176201_c(iblockdata) << 12);
    }

    public static Block func_149729_e(int i) {
        return (Block) Block.field_149771_c.func_148754_a(i);
    }

    public static IBlockState func_176220_d(int i) {
        int j = i & 4095;
        int k = i >> 12 & 15;

        return func_149729_e(j).func_176203_a(k);
    }

    public static Block func_149634_a(@Nullable Item item) {
        return item instanceof ItemBlock ? ((ItemBlock) item).func_179223_d() : Blocks.field_150350_a;
    }

    @Nullable
    public static Block func_149684_b(String s) {
        ResourceLocation minecraftkey = new ResourceLocation(s);

        if (Block.field_149771_c.func_148741_d(minecraftkey)) {
            return (Block) Block.field_149771_c.func_82594_a(minecraftkey);
        } else {
            try {
                return (Block) Block.field_149771_c.func_148754_a(Integer.parseInt(s));
            } catch (NumberFormatException numberformatexception) {
                return null;
            }
        }
    }

    @Deprecated
    public boolean func_185481_k(IBlockState iblockdata) {
        return iblockdata.func_185904_a().func_76218_k() && iblockdata.func_185917_h();
    }

    @Deprecated
    public boolean func_149730_j(IBlockState iblockdata) {
        return this.field_149787_q;
    }

    @Deprecated
    public boolean func_189872_a(IBlockState iblockdata, Entity entity) {
        return true;
    }

    @Deprecated
    public int func_149717_k(IBlockState iblockdata) {
        return this.field_149786_r;
    }

    @Deprecated
    public int func_149750_m(IBlockState iblockdata) {
        return this.field_149784_t;
    }

    @Deprecated
    public boolean func_149710_n(IBlockState iblockdata) {
        return this.field_149783_u;
    }

    @Deprecated
    public Material func_149688_o(IBlockState iblockdata) {
        return this.field_149764_J;
    }

    @Deprecated
    public MapColor func_180659_g(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        return this.field_181083_K;
    }

    @Deprecated
    public IBlockState func_176203_a(int i) {
        return this.func_176223_P();
    }

    public int func_176201_c(IBlockState iblockdata) {
        if (iblockdata.func_177227_a().isEmpty()) {
            return 0;
        } else {
            throw new IllegalArgumentException("Don\'t know how to convert " + iblockdata + " back into data...");
        }
    }

    @Deprecated
    public IBlockState func_176221_a(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        return iblockdata;
    }

    @Deprecated
    public IBlockState func_185499_a(IBlockState iblockdata, Rotation enumblockrotation) {
        return iblockdata;
    }

    @Deprecated
    public IBlockState func_185471_a(IBlockState iblockdata, Mirror enumblockmirror) {
        return iblockdata;
    }

    public Block(Material material, MapColor materialmapcolor) {
        this.field_149790_y = true;
        this.field_149762_H = SoundType.field_185851_d;
        this.field_149763_I = 1.0F;
        this.field_149765_K = 0.6F;
        this.field_149764_J = material;
        this.field_181083_K = materialmapcolor;
        this.field_176227_L = this.func_180661_e();
        this.func_180632_j(this.field_176227_L.func_177621_b());
        this.field_149787_q = this.func_176223_P().func_185914_p();
        this.field_149786_r = this.field_149787_q ? 255 : 0;
        this.field_149785_s = !material.func_76228_b();
    }

    protected Block(Material material) {
        this(material, material.func_151565_r());
    }

    protected Block func_149672_a(SoundType soundeffecttype) {
        this.field_149762_H = soundeffecttype;
        return this;
    }

    protected Block func_149713_g(int i) {
        this.field_149786_r = i;
        return this;
    }

    protected Block func_149715_a(float f) {
        this.field_149784_t = (int) (15.0F * f);
        return this;
    }

    protected Block func_149752_b(float f) {
        this.field_149781_w = f * 3.0F;
        return this;
    }

    protected static boolean func_193384_b(Block block) {
        return block instanceof BlockShulkerBox || block instanceof BlockLeaves || block instanceof BlockTrapDoor || block == Blocks.field_150461_bJ || block == Blocks.field_150383_bp || block == Blocks.field_150359_w || block == Blocks.field_150426_aN || block == Blocks.field_150432_aD || block == Blocks.field_180398_cJ || block == Blocks.field_150399_cn;
    }

    protected static boolean func_193382_c(Block block) {
        return func_193384_b(block) || block == Blocks.field_150331_J || block == Blocks.field_150320_F || block == Blocks.field_150332_K;
    }

    @Deprecated
    public boolean func_149637_q(IBlockState iblockdata) {
        return iblockdata.func_185904_a().func_76230_c() && iblockdata.func_185917_h();
    }

    @Deprecated
    public boolean func_149721_r(IBlockState iblockdata) {
        return iblockdata.func_185904_a().func_76218_k() && iblockdata.func_185917_h() && !iblockdata.func_185897_m();
    }

    @Deprecated
    public boolean func_176214_u(IBlockState iblockdata) {
        return this.field_149764_J.func_76230_c() && this.func_176223_P().func_185917_h();
    }

    @Deprecated
    public boolean func_149686_d(IBlockState iblockdata) {
        return true;
    }

    public boolean func_176205_b(IBlockAccess iblockaccess, BlockPos blockposition) {
        return !this.field_149764_J.func_76230_c();
    }

    @Deprecated
    public EnumBlockRenderType func_149645_b(IBlockState iblockdata) {
        return EnumBlockRenderType.MODEL;
    }

    public boolean func_176200_f(IBlockAccess iblockaccess, BlockPos blockposition) {
        return false;
    }

    protected Block func_149711_c(float f) {
        this.field_149782_v = f;
        if (this.field_149781_w < f * 5.0F) {
            this.field_149781_w = f * 5.0F;
        }

        return this;
    }

    protected Block func_149722_s() {
        this.func_149711_c(-1.0F);
        return this;
    }

    @Deprecated
    public float func_176195_g(IBlockState iblockdata, World world, BlockPos blockposition) {
        return this.field_149782_v;
    }

    protected Block func_149675_a(boolean flag) {
        this.field_149789_z = flag;
        return this;
    }

    public boolean func_149653_t() {
        return this.field_149789_z;
    }

    public boolean func_149716_u() {
        return this.field_149758_A;
    }

    @Deprecated
    public AxisAlignedBB func_185496_a(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        return Block.field_185505_j;
    }

    @Deprecated
    public BlockFaceShape func_193383_a(IBlockAccess iblockaccess, IBlockState iblockdata, BlockPos blockposition, EnumFacing enumdirection) {
        return BlockFaceShape.SOLID;
    }

    @Deprecated
    public void func_185477_a(IBlockState iblockdata, World world, BlockPos blockposition, AxisAlignedBB axisalignedbb, List<AxisAlignedBB> list, @Nullable Entity entity, boolean flag) {
        func_185492_a(blockposition, axisalignedbb, list, iblockdata.func_185890_d(world, blockposition));
    }

    protected static void func_185492_a(BlockPos blockposition, AxisAlignedBB axisalignedbb, List<AxisAlignedBB> list, @Nullable AxisAlignedBB axisalignedbb1) {
        if (axisalignedbb1 != Block.field_185506_k) {
            AxisAlignedBB axisalignedbb2 = axisalignedbb1.func_186670_a(blockposition);

            if (axisalignedbb.func_72326_a(axisalignedbb2)) {
                list.add(axisalignedbb2);
            }
        }

    }

    @Deprecated
    @Nullable
    public AxisAlignedBB func_180646_a(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        return iblockdata.func_185900_c(iblockaccess, blockposition);
    }

    @Deprecated
    public boolean func_149662_c(IBlockState iblockdata) {
        return true;
    }

    public boolean func_176209_a(IBlockState iblockdata, boolean flag) {
        return this.func_149703_v();
    }

    public boolean func_149703_v() {
        return true;
    }

    public void func_180645_a(World world, BlockPos blockposition, IBlockState iblockdata, Random random) {
        this.func_180650_b(world, blockposition, iblockdata, random);
    }

    public void func_180650_b(World world, BlockPos blockposition, IBlockState iblockdata, Random random) {}

    public void func_176206_d(World world, BlockPos blockposition, IBlockState iblockdata) {}

    @Deprecated
    public void func_189540_a(IBlockState iblockdata, World world, BlockPos blockposition, Block block, BlockPos blockposition1) {}

    public int func_149738_a(World world) {
        return 10;
    }

    public void func_176213_c(World world, BlockPos blockposition, IBlockState iblockdata) {
        org.spigotmc.AsyncCatcher.catchOp( "block onPlace"); // Spigot
    }

    public void func_180663_b(World world, BlockPos blockposition, IBlockState iblockdata) {
        org.spigotmc.AsyncCatcher.catchOp( "block remove"); // Spigot
    }

    public int func_149745_a(Random random) {
        return 1;
    }

    public Item func_180660_a(IBlockState iblockdata, Random random, int i) {
        return Item.func_150898_a(this);
    }

    @Deprecated
    public float func_180647_a(IBlockState iblockdata, EntityPlayer entityhuman, World world, BlockPos blockposition) {
        float f = iblockdata.func_185887_b(world, blockposition);

        return f < 0.0F ? 0.0F : (!entityhuman.func_184823_b(iblockdata) ? entityhuman.func_184813_a(iblockdata) / f / 100.0F : entityhuman.func_184813_a(iblockdata) / f / 30.0F);
    }

    public final void func_176226_b(World world, BlockPos blockposition, IBlockState iblockdata, int i) {
        this.func_180653_a(world, blockposition, iblockdata, 1.0F, i);
    }

    public void func_180653_a(World world, BlockPos blockposition, IBlockState iblockdata, float f, int i) {
        if (!world.field_72995_K) {
            int j = this.func_149679_a(i, world.field_73012_v);

            for (int k = 0; k < j; ++k) {
                // CraftBukkit - <= to < to allow for plugins to completely disable block drops from explosions
                if (world.field_73012_v.nextFloat() < f) {
                    Item item = this.func_180660_a(iblockdata, world.field_73012_v, i);

                    if (item != Items.field_190931_a) {
                        func_180635_a(world, blockposition, new ItemStack(item, 1, this.func_180651_a(iblockdata)));
                    }
                }
            }

        }
    }

    public static void func_180635_a(World world, BlockPos blockposition, ItemStack itemstack) {
        if (!world.field_72995_K && !itemstack.func_190926_b() && world.func_82736_K().func_82766_b("doTileDrops")) {
            float f = 0.5F;
            double d0 = (double) (world.field_73012_v.nextFloat() * 0.5F) + 0.25D;
            double d1 = (double) (world.field_73012_v.nextFloat() * 0.5F) + 0.25D;
            double d2 = (double) (world.field_73012_v.nextFloat() * 0.5F) + 0.25D;
            EntityItem entityitem = new EntityItem(world, (double) blockposition.func_177958_n() + d0, (double) blockposition.func_177956_o() + d1, (double) blockposition.func_177952_p() + d2, itemstack);

            entityitem.func_174869_p();
            // CraftBukkit start
            if (world.captureDrops != null) {
                world.captureDrops.add(entityitem);
            } else {
                world.func_72838_d(entityitem);
            }
            // CraftBukkit end
        }
    }

    protected void dropExperience(World world, BlockPos blockposition, int i, EntityPlayerMP player) { // Paper
        if (!world.field_72995_K && world.func_82736_K().func_82766_b("doTileDrops")) {
            while (i > 0) {
                int j = EntityXPOrb.func_70527_a(i);

                i -= j;
                world.func_72838_d(new EntityXPOrb(world, (double) blockposition.func_177958_n() + 0.5D, (double) blockposition.func_177956_o() + 0.5D, (double) blockposition.func_177952_p() + 0.5D, j, org.bukkit.entity.ExperienceOrb.SpawnReason.BLOCK_BREAK, player)); // Paper
            }
        }

    }

    public int func_180651_a(IBlockState iblockdata) {
        return 0;
    }

    public float func_149638_a(Entity entity) {
        return this.field_149781_w / 5.0F;
    }

    @Deprecated
    @Nullable
    public RayTraceResult func_180636_a(IBlockState iblockdata, World world, BlockPos blockposition, Vec3d vec3d, Vec3d vec3d1) {
        return this.func_185503_a(blockposition, vec3d, vec3d1, iblockdata.func_185900_c(world, blockposition));
    }

    @Nullable
    protected RayTraceResult func_185503_a(BlockPos blockposition, Vec3d vec3d, Vec3d vec3d1, AxisAlignedBB axisalignedbb) {
        Vec3d vec3d2 = vec3d.func_178786_a((double) blockposition.func_177958_n(), (double) blockposition.func_177956_o(), (double) blockposition.func_177952_p());
        Vec3d vec3d3 = vec3d1.func_178786_a((double) blockposition.func_177958_n(), (double) blockposition.func_177956_o(), (double) blockposition.func_177952_p());
        RayTraceResult movingobjectposition = axisalignedbb.func_72327_a(vec3d2, vec3d3);

        return movingobjectposition == null ? null : new RayTraceResult(movingobjectposition.field_72307_f.func_72441_c((double) blockposition.func_177958_n(), (double) blockposition.func_177956_o(), (double) blockposition.func_177952_p()), movingobjectposition.field_178784_b, blockposition);
    }

    public void func_180652_a(World world, BlockPos blockposition, Explosion explosion) {}

    public boolean func_176198_a(World world, BlockPos blockposition, EnumFacing enumdirection) {
        return this.func_176196_c(world, blockposition);
    }

    public boolean func_176196_c(World world, BlockPos blockposition) {
        return world.func_180495_p(blockposition).func_177230_c().field_149764_J.func_76222_j();
    }

    public boolean func_180639_a(World world, BlockPos blockposition, IBlockState iblockdata, EntityPlayer entityhuman, EnumHand enumhand, EnumFacing enumdirection, float f, float f1, float f2) {
        return false;
    }

    public void func_176199_a(World world, BlockPos blockposition, Entity entity) {}

    public IBlockState func_180642_a(World world, BlockPos blockposition, EnumFacing enumdirection, float f, float f1, float f2, int i, EntityLivingBase entityliving) {
        return this.func_176203_a(i);
    }

    public void func_180649_a(World world, BlockPos blockposition, EntityPlayer entityhuman) {}

    public Vec3d func_176197_a(World world, BlockPos blockposition, Entity entity, Vec3d vec3d) {
        return vec3d;
    }

    @Deprecated
    public int func_180656_a(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition, EnumFacing enumdirection) {
        return 0;
    }

    @Deprecated
    public boolean func_149744_f(IBlockState iblockdata) {
        return false;
    }

    public void func_180634_a(World world, BlockPos blockposition, IBlockState iblockdata, Entity entity) {}

    @Deprecated
    public int func_176211_b(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition, EnumFacing enumdirection) {
        return 0;
    }

    public void func_180657_a(World world, EntityPlayer entityhuman, BlockPos blockposition, IBlockState iblockdata, @Nullable TileEntity tileentity, ItemStack itemstack) {
        entityhuman.func_71029_a(StatList.func_188055_a(this));
        entityhuman.func_71020_j(0.005F);
        if (this.func_149700_E() && EnchantmentHelper.func_77506_a(Enchantments.field_185306_r, itemstack) > 0) {
            ItemStack itemstack1 = this.func_180643_i(iblockdata);

            func_180635_a(world, blockposition, itemstack1);
        } else {
            int i = EnchantmentHelper.func_77506_a(Enchantments.field_185308_t, itemstack);

            this.func_176226_b(world, blockposition, iblockdata, i);
        }

    }

    protected boolean func_149700_E() {
        return this.func_176223_P().func_185917_h() && !this.field_149758_A;
    }

    protected ItemStack func_180643_i(IBlockState iblockdata) {
        Item item = Item.func_150898_a(this);
        int i = 0;

        if (item.func_77614_k()) {
            i = this.func_176201_c(iblockdata);
        }

        return new ItemStack(item, 1, i);
    }

    public int func_149679_a(int i, Random random) {
        return this.func_149745_a(random);
    }

    public void func_180633_a(World world, BlockPos blockposition, IBlockState iblockdata, EntityLivingBase entityliving, ItemStack itemstack) {}

    public boolean func_181623_g() {
        return !this.field_149764_J.func_76220_a() && !this.field_149764_J.func_76224_d();
    }

    public Block func_149663_c(String s) {
        this.field_149770_b = s;
        return this;
    }

    public String func_149732_F() {
        return I18n.func_74838_a(this.func_149739_a() + ".name");
    }

    public String func_149739_a() {
        return "tile." + this.field_149770_b;
    }

    @Deprecated
    public boolean func_189539_a(IBlockState iblockdata, World world, BlockPos blockposition, int i, int j) {
        return false;
    }

    public boolean func_149652_G() {
        return this.field_149790_y;
    }

    protected Block func_149649_H() {
        this.field_149790_y = false;
        return this;
    }

    @Deprecated
    public EnumPushReaction func_149656_h(IBlockState iblockdata) {
        return this.field_149764_J.func_186274_m();
    }

    public void func_180658_a(World world, BlockPos blockposition, Entity entity, float f) {
        entity.func_180430_e(f, 1.0F);
    }

    public void func_176216_a(World world, Entity entity) {
        entity.field_70181_x = 0.0D;
    }

    public ItemStack func_185473_a(World world, BlockPos blockposition, IBlockState iblockdata) {
        return new ItemStack(Item.func_150898_a(this), 1, this.func_180651_a(iblockdata));
    }

    public void func_149666_a(CreativeTabs creativemodetab, NonNullList<ItemStack> nonnulllist) {
        nonnulllist.add(new ItemStack(this));
    }

    public CreativeTabs func_149708_J() {
        return this.field_149772_a;
    }

    public Block func_149647_a(CreativeTabs creativemodetab) {
        this.field_149772_a = creativemodetab;
        return this;
    }

    public void func_176208_a(World world, BlockPos blockposition, IBlockState iblockdata, EntityPlayer entityhuman) {}

    public void func_176224_k(World world, BlockPos blockposition) {}

    public boolean func_149698_L() {
        return true;
    }

    public boolean func_149659_a(Explosion explosion) {
        return true;
    }

    public boolean func_149667_c(Block block) {
        return this == block;
    }

    public static boolean func_149680_a(Block block, Block block1) {
        return block != null && block1 != null ? (block == block1 ? true : block.func_149667_c(block1)) : false;
    }

    @Deprecated
    public boolean func_149740_M(IBlockState iblockdata) {
        return false;
    }

    @Deprecated
    public int func_180641_l(IBlockState iblockdata, World world, BlockPos blockposition) {
        return 0;
    }

    protected BlockStateContainer func_180661_e() {
        return new BlockStateContainer(this, new IProperty[0]);
    }

    public BlockStateContainer func_176194_O() {
        return this.field_176227_L;
    }

    protected final void func_180632_j(IBlockState iblockdata) {
        this.field_176228_M = iblockdata;
    }

    public final IBlockState func_176223_P() {
        return this.field_176228_M;
    }

    public Block.EnumOffsetType func_176218_Q() {
        return Block.EnumOffsetType.NONE;
    }

    @Deprecated
    public Vec3d func_190949_e(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        Block.EnumOffsetType block_enumrandomoffset = this.func_176218_Q();

        if (block_enumrandomoffset == Block.EnumOffsetType.NONE) {
            return Vec3d.field_186680_a;
        } else {
            long i = MathHelper.func_180187_c(blockposition.func_177958_n(), 0, blockposition.func_177952_p());

            return new Vec3d(((double) ((float) (i >> 16 & 15L) / 15.0F) - 0.5D) * 0.5D, block_enumrandomoffset == Block.EnumOffsetType.XYZ ? ((double) ((float) (i >> 20 & 15L) / 15.0F) - 1.0D) * 0.2D : 0.0D, ((double) ((float) (i >> 24 & 15L) / 15.0F) - 0.5D) * 0.5D);
        }
    }

    public SoundType func_185467_w() {
        return this.field_149762_H;
    }

    public String toString() {
        return "Block{" + Block.field_149771_c.func_177774_c(this) + "}";
    }

    public static void func_149671_p() {
        func_176215_a(0, Block.field_176230_a, (new BlockAir()).func_149663_c("air"));
        func_176219_a(1, "stone", (new BlockStone()).func_149711_c(1.5F).func_149752_b(10.0F).func_149672_a(SoundType.field_185851_d).func_149663_c("stone"));
        func_176219_a(2, "grass", (new BlockGrass()).func_149711_c(0.6F).func_149672_a(SoundType.field_185850_c).func_149663_c("grass"));
        func_176219_a(3, "dirt", (new BlockDirt()).func_149711_c(0.5F).func_149672_a(SoundType.field_185849_b).func_149663_c("dirt"));
        Block block = (new Block(Material.field_151576_e)).func_149711_c(2.0F).func_149752_b(10.0F).func_149672_a(SoundType.field_185851_d).func_149663_c("stonebrick").func_149647_a(CreativeTabs.field_78030_b);

        func_176219_a(4, "cobblestone", block);
        Block block1 = (new BlockPlanks()).func_149711_c(2.0F).func_149752_b(5.0F).func_149672_a(SoundType.field_185848_a).func_149663_c("wood");

        func_176219_a(5, "planks", block1);
        func_176219_a(6, "sapling", (new BlockSapling()).func_149711_c(0.0F).func_149672_a(SoundType.field_185850_c).func_149663_c("sapling"));
        func_176219_a(7, "bedrock", (new BlockEmptyDrops(Material.field_151576_e)).func_149722_s().func_149752_b(6000000.0F).func_149672_a(SoundType.field_185851_d).func_149663_c("bedrock").func_149649_H().func_149647_a(CreativeTabs.field_78030_b));
        func_176219_a(8, "flowing_water", (new BlockDynamicLiquid(Material.field_151586_h)).func_149711_c(100.0F).func_149713_g(3).func_149663_c("water").func_149649_H());
        func_176219_a(9, "water", (new BlockStaticLiquid(Material.field_151586_h)).func_149711_c(100.0F).func_149713_g(3).func_149663_c("water").func_149649_H());
        func_176219_a(10, "flowing_lava", (new BlockDynamicLiquid(Material.field_151587_i)).func_149711_c(100.0F).func_149715_a(1.0F).func_149663_c("lava").func_149649_H());
        func_176219_a(11, "lava", (new BlockStaticLiquid(Material.field_151587_i)).func_149711_c(100.0F).func_149715_a(1.0F).func_149663_c("lava").func_149649_H());
        func_176219_a(12, "sand", (new BlockSand()).func_149711_c(0.5F).func_149672_a(SoundType.field_185855_h).func_149663_c("sand"));
        func_176219_a(13, "gravel", (new BlockGravel()).func_149711_c(0.6F).func_149672_a(SoundType.field_185849_b).func_149663_c("gravel"));
        func_176219_a(14, "gold_ore", (new BlockOre()).func_149711_c(3.0F).func_149752_b(5.0F).func_149672_a(SoundType.field_185851_d).func_149663_c("oreGold"));
        func_176219_a(15, "iron_ore", (new BlockOre()).func_149711_c(3.0F).func_149752_b(5.0F).func_149672_a(SoundType.field_185851_d).func_149663_c("oreIron"));
        func_176219_a(16, "coal_ore", (new BlockOre()).func_149711_c(3.0F).func_149752_b(5.0F).func_149672_a(SoundType.field_185851_d).func_149663_c("oreCoal"));
        func_176219_a(17, "log", (new BlockOldLog()).func_149663_c("log"));
        func_176219_a(18, "leaves", (new BlockOldLeaf()).func_149663_c("leaves"));
        func_176219_a(19, "sponge", (new BlockSponge()).func_149711_c(0.6F).func_149672_a(SoundType.field_185850_c).func_149663_c("sponge"));
        func_176219_a(20, "glass", (new BlockGlass(Material.field_151592_s, false)).func_149711_c(0.3F).func_149672_a(SoundType.field_185853_f).func_149663_c("glass"));
        func_176219_a(21, "lapis_ore", (new BlockOre()).func_149711_c(3.0F).func_149752_b(5.0F).func_149672_a(SoundType.field_185851_d).func_149663_c("oreLapis"));
        func_176219_a(22, "lapis_block", (new Block(Material.field_151573_f, MapColor.field_151652_H)).func_149711_c(3.0F).func_149752_b(5.0F).func_149672_a(SoundType.field_185851_d).func_149663_c("blockLapis").func_149647_a(CreativeTabs.field_78030_b));
        func_176219_a(23, "dispenser", (new BlockDispenser()).func_149711_c(3.5F).func_149672_a(SoundType.field_185851_d).func_149663_c("dispenser"));
        Block block2 = (new BlockSandStone()).func_149672_a(SoundType.field_185851_d).func_149711_c(0.8F).func_149663_c("sandStone");

        func_176219_a(24, "sandstone", block2);
        func_176219_a(25, "noteblock", (new BlockNote()).func_149672_a(SoundType.field_185848_a).func_149711_c(0.8F).func_149663_c("musicBlock"));
        func_176219_a(26, "bed", (new BlockBed()).func_149672_a(SoundType.field_185848_a).func_149711_c(0.2F).func_149663_c("bed").func_149649_H());
        func_176219_a(27, "golden_rail", (new BlockRailPowered()).func_149711_c(0.7F).func_149672_a(SoundType.field_185852_e).func_149663_c("goldenRail"));
        func_176219_a(28, "detector_rail", (new BlockRailDetector()).func_149711_c(0.7F).func_149672_a(SoundType.field_185852_e).func_149663_c("detectorRail"));
        func_176219_a(29, "sticky_piston", (new BlockPistonBase(true)).func_149663_c("pistonStickyBase"));
        func_176219_a(30, "web", (new BlockWeb()).func_149713_g(1).func_149711_c(4.0F).func_149663_c("web"));
        func_176219_a(31, "tallgrass", (new BlockTallGrass()).func_149711_c(0.0F).func_149672_a(SoundType.field_185850_c).func_149663_c("tallgrass"));
        func_176219_a(32, "deadbush", (new BlockDeadBush()).func_149711_c(0.0F).func_149672_a(SoundType.field_185850_c).func_149663_c("deadbush"));
        func_176219_a(33, "piston", (new BlockPistonBase(false)).func_149663_c("pistonBase"));
        func_176219_a(34, "piston_head", (new BlockPistonExtension()).func_149663_c("pistonBase"));
        func_176219_a(35, "wool", (new BlockColored(Material.field_151580_n)).func_149711_c(0.8F).func_149672_a(SoundType.field_185854_g).func_149663_c("cloth"));
        func_176219_a(36, "piston_extension", new BlockPistonMoving());
        func_176219_a(37, "yellow_flower", (new BlockYellowFlower()).func_149711_c(0.0F).func_149672_a(SoundType.field_185850_c).func_149663_c("flower1"));
        func_176219_a(38, "red_flower", (new BlockRedFlower()).func_149711_c(0.0F).func_149672_a(SoundType.field_185850_c).func_149663_c("flower2"));
        Block block3 = (new BlockMushroom()).func_149711_c(0.0F).func_149672_a(SoundType.field_185850_c).func_149715_a(0.125F).func_149663_c("mushroom");

        func_176219_a(39, "brown_mushroom", block3);
        Block block4 = (new BlockMushroom()).func_149711_c(0.0F).func_149672_a(SoundType.field_185850_c).func_149663_c("mushroom");

        func_176219_a(40, "red_mushroom", block4);
        func_176219_a(41, "gold_block", (new Block(Material.field_151573_f, MapColor.field_151647_F)).func_149711_c(3.0F).func_149752_b(10.0F).func_149672_a(SoundType.field_185852_e).func_149663_c("blockGold").func_149647_a(CreativeTabs.field_78030_b));
        func_176219_a(42, "iron_block", (new Block(Material.field_151573_f, MapColor.field_151668_h)).func_149711_c(5.0F).func_149752_b(10.0F).func_149672_a(SoundType.field_185852_e).func_149663_c("blockIron").func_149647_a(CreativeTabs.field_78030_b));
        func_176219_a(43, "double_stone_slab", (new BlockDoubleStoneSlab()).func_149711_c(2.0F).func_149752_b(10.0F).func_149672_a(SoundType.field_185851_d).func_149663_c("stoneSlab"));
        func_176219_a(44, "stone_slab", (new BlockHalfStoneSlab()).func_149711_c(2.0F).func_149752_b(10.0F).func_149672_a(SoundType.field_185851_d).func_149663_c("stoneSlab"));
        Block block5 = (new Block(Material.field_151576_e, MapColor.field_151645_D)).func_149711_c(2.0F).func_149752_b(10.0F).func_149672_a(SoundType.field_185851_d).func_149663_c("brick").func_149647_a(CreativeTabs.field_78030_b);

        func_176219_a(45, "brick_block", block5);
        func_176219_a(46, "tnt", (new BlockTNT()).func_149711_c(0.0F).func_149672_a(SoundType.field_185850_c).func_149663_c("tnt"));
        func_176219_a(47, "bookshelf", (new BlockBookshelf()).func_149711_c(1.5F).func_149672_a(SoundType.field_185848_a).func_149663_c("bookshelf"));
        func_176219_a(48, "mossy_cobblestone", (new Block(Material.field_151576_e)).func_149711_c(2.0F).func_149752_b(10.0F).func_149672_a(SoundType.field_185851_d).func_149663_c("stoneMoss").func_149647_a(CreativeTabs.field_78030_b));
        func_176219_a(49, "obsidian", (new BlockObsidian()).func_149711_c(50.0F).func_149752_b(2000.0F).func_149672_a(SoundType.field_185851_d).func_149663_c("obsidian"));
        func_176219_a(50, "torch", (new BlockTorch()).func_149711_c(0.0F).func_149715_a(0.9375F).func_149672_a(SoundType.field_185848_a).func_149663_c("torch"));
        func_176219_a(51, "fire", (new BlockFire()).func_149711_c(0.0F).func_149715_a(1.0F).func_149672_a(SoundType.field_185854_g).func_149663_c("fire").func_149649_H());
        func_176219_a(52, "mob_spawner", (new BlockMobSpawner()).func_149711_c(5.0F).func_149672_a(SoundType.field_185852_e).func_149663_c("mobSpawner").func_149649_H());
        func_176219_a(53, "oak_stairs", (new BlockStairs(block1.func_176223_P().func_177226_a(BlockPlanks.field_176383_a, BlockPlanks.EnumType.OAK))).func_149663_c("stairsWood"));
        func_176219_a(54, "chest", (new BlockChest(BlockChest.Type.BASIC)).func_149711_c(2.5F).func_149672_a(SoundType.field_185848_a).func_149663_c("chest"));
        func_176219_a(55, "redstone_wire", (new BlockRedstoneWire()).func_149711_c(0.0F).func_149672_a(SoundType.field_185851_d).func_149663_c("redstoneDust").func_149649_H());
        func_176219_a(56, "diamond_ore", (new BlockOre()).func_149711_c(3.0F).func_149752_b(5.0F).func_149672_a(SoundType.field_185851_d).func_149663_c("oreDiamond"));
        func_176219_a(57, "diamond_block", (new Block(Material.field_151573_f, MapColor.field_151648_G)).func_149711_c(5.0F).func_149752_b(10.0F).func_149672_a(SoundType.field_185852_e).func_149663_c("blockDiamond").func_149647_a(CreativeTabs.field_78030_b));
        func_176219_a(58, "crafting_table", (new BlockWorkbench()).func_149711_c(2.5F).func_149672_a(SoundType.field_185848_a).func_149663_c("workbench"));
        func_176219_a(59, "wheat", (new BlockCrops()).func_149663_c("crops"));
        Block block6 = (new BlockFarmland()).func_149711_c(0.6F).func_149672_a(SoundType.field_185849_b).func_149663_c("farmland");

        func_176219_a(60, "farmland", block6);
        func_176219_a(61, "furnace", (new BlockFurnace(false)).func_149711_c(3.5F).func_149672_a(SoundType.field_185851_d).func_149663_c("furnace").func_149647_a(CreativeTabs.field_78031_c));
        func_176219_a(62, "lit_furnace", (new BlockFurnace(true)).func_149711_c(3.5F).func_149672_a(SoundType.field_185851_d).func_149715_a(0.875F).func_149663_c("furnace"));
        func_176219_a(63, "standing_sign", (new BlockStandingSign()).func_149711_c(1.0F).func_149672_a(SoundType.field_185848_a).func_149663_c("sign").func_149649_H());
        func_176219_a(64, "wooden_door", (new BlockDoor(Material.field_151575_d)).func_149711_c(3.0F).func_149672_a(SoundType.field_185848_a).func_149663_c("doorOak").func_149649_H());
        func_176219_a(65, "ladder", (new BlockLadder()).func_149711_c(0.4F).func_149672_a(SoundType.field_185857_j).func_149663_c("ladder"));
        func_176219_a(66, "rail", (new BlockRail()).func_149711_c(0.7F).func_149672_a(SoundType.field_185852_e).func_149663_c("rail"));
        func_176219_a(67, "stone_stairs", (new BlockStairs(block.func_176223_P())).func_149663_c("stairsStone"));
        func_176219_a(68, "wall_sign", (new BlockWallSign()).func_149711_c(1.0F).func_149672_a(SoundType.field_185848_a).func_149663_c("sign").func_149649_H());
        func_176219_a(69, "lever", (new BlockLever()).func_149711_c(0.5F).func_149672_a(SoundType.field_185848_a).func_149663_c("lever"));
        func_176219_a(70, "stone_pressure_plate", (new BlockPressurePlate(Material.field_151576_e, BlockPressurePlate.Sensitivity.MOBS)).func_149711_c(0.5F).func_149672_a(SoundType.field_185851_d).func_149663_c("pressurePlateStone"));
        func_176219_a(71, "iron_door", (new BlockDoor(Material.field_151573_f)).func_149711_c(5.0F).func_149672_a(SoundType.field_185852_e).func_149663_c("doorIron").func_149649_H());
        func_176219_a(72, "wooden_pressure_plate", (new BlockPressurePlate(Material.field_151575_d, BlockPressurePlate.Sensitivity.EVERYTHING)).func_149711_c(0.5F).func_149672_a(SoundType.field_185848_a).func_149663_c("pressurePlateWood"));
        func_176219_a(73, "redstone_ore", (new BlockRedstoneOre(false)).func_149711_c(3.0F).func_149752_b(5.0F).func_149672_a(SoundType.field_185851_d).func_149663_c("oreRedstone").func_149647_a(CreativeTabs.field_78030_b));
        func_176219_a(74, "lit_redstone_ore", (new BlockRedstoneOre(true)).func_149715_a(0.625F).func_149711_c(3.0F).func_149752_b(5.0F).func_149672_a(SoundType.field_185851_d).func_149663_c("oreRedstone"));
        func_176219_a(75, "unlit_redstone_torch", (new BlockRedstoneTorch(false)).func_149711_c(0.0F).func_149672_a(SoundType.field_185848_a).func_149663_c("notGate"));
        func_176219_a(76, "redstone_torch", (new BlockRedstoneTorch(true)).func_149711_c(0.0F).func_149715_a(0.5F).func_149672_a(SoundType.field_185848_a).func_149663_c("notGate").func_149647_a(CreativeTabs.field_78028_d));
        func_176219_a(77, "stone_button", (new BlockButtonStone()).func_149711_c(0.5F).func_149672_a(SoundType.field_185851_d).func_149663_c("button"));
        func_176219_a(78, "snow_layer", (new BlockSnow()).func_149711_c(0.1F).func_149672_a(SoundType.field_185856_i).func_149663_c("snow").func_149713_g(0));
        func_176219_a(79, "ice", (new BlockIce()).func_149711_c(0.5F).func_149713_g(3).func_149672_a(SoundType.field_185853_f).func_149663_c("ice"));
        func_176219_a(80, "snow", (new BlockSnowBlock()).func_149711_c(0.2F).func_149672_a(SoundType.field_185856_i).func_149663_c("snow"));
        func_176219_a(81, "cactus", (new BlockCactus()).func_149711_c(0.4F).func_149672_a(SoundType.field_185854_g).func_149663_c("cactus"));
        func_176219_a(82, "clay", (new BlockClay()).func_149711_c(0.6F).func_149672_a(SoundType.field_185849_b).func_149663_c("clay"));
        func_176219_a(83, "reeds", (new BlockReed()).func_149711_c(0.0F).func_149672_a(SoundType.field_185850_c).func_149663_c("reeds").func_149649_H());
        func_176219_a(84, "jukebox", (new BlockJukebox()).func_149711_c(2.0F).func_149752_b(10.0F).func_149672_a(SoundType.field_185851_d).func_149663_c("jukebox"));
        func_176219_a(85, "fence", (new BlockFence(Material.field_151575_d, BlockPlanks.EnumType.OAK.func_181070_c())).func_149711_c(2.0F).func_149752_b(5.0F).func_149672_a(SoundType.field_185848_a).func_149663_c("fence"));
        Block block7 = (new BlockPumpkin()).func_149711_c(1.0F).func_149672_a(SoundType.field_185848_a).func_149663_c("pumpkin");

        func_176219_a(86, "pumpkin", block7);
        func_176219_a(87, "netherrack", (new BlockNetherrack()).func_149711_c(0.4F).func_149672_a(SoundType.field_185851_d).func_149663_c("hellrock"));
        func_176219_a(88, "soul_sand", (new BlockSoulSand()).func_149711_c(0.5F).func_149672_a(SoundType.field_185855_h).func_149663_c("hellsand"));
        func_176219_a(89, "glowstone", (new BlockGlowstone(Material.field_151592_s)).func_149711_c(0.3F).func_149672_a(SoundType.field_185853_f).func_149715_a(1.0F).func_149663_c("lightgem"));
        func_176219_a(90, "portal", (new BlockPortal()).func_149711_c(-1.0F).func_149672_a(SoundType.field_185853_f).func_149715_a(0.75F).func_149663_c("portal"));
        func_176219_a(91, "lit_pumpkin", (new BlockPumpkin()).func_149711_c(1.0F).func_149672_a(SoundType.field_185848_a).func_149715_a(1.0F).func_149663_c("litpumpkin"));
        func_176219_a(92, "cake", (new BlockCake()).func_149711_c(0.5F).func_149672_a(SoundType.field_185854_g).func_149663_c("cake").func_149649_H());
        func_176219_a(93, "unpowered_repeater", (new BlockRedstoneRepeater(false)).func_149711_c(0.0F).func_149672_a(SoundType.field_185848_a).func_149663_c("diode").func_149649_H());
        func_176219_a(94, "powered_repeater", (new BlockRedstoneRepeater(true)).func_149711_c(0.0F).func_149672_a(SoundType.field_185848_a).func_149663_c("diode").func_149649_H());
        func_176219_a(95, "stained_glass", (new BlockStainedGlass(Material.field_151592_s)).func_149711_c(0.3F).func_149672_a(SoundType.field_185853_f).func_149663_c("stainedGlass"));
        func_176219_a(96, "trapdoor", (new BlockTrapDoor(Material.field_151575_d)).func_149711_c(3.0F).func_149672_a(SoundType.field_185848_a).func_149663_c("trapdoor").func_149649_H());
        func_176219_a(97, "monster_egg", (new BlockSilverfish()).func_149711_c(0.75F).func_149663_c("monsterStoneEgg"));
        Block block8 = (new BlockStoneBrick()).func_149711_c(1.5F).func_149752_b(10.0F).func_149672_a(SoundType.field_185851_d).func_149663_c("stonebricksmooth");

        func_176219_a(98, "stonebrick", block8);
        func_176219_a(99, "brown_mushroom_block", (new BlockHugeMushroom(Material.field_151575_d, MapColor.field_151664_l, block3)).func_149711_c(0.2F).func_149672_a(SoundType.field_185848_a).func_149663_c("mushroom"));
        func_176219_a(100, "red_mushroom_block", (new BlockHugeMushroom(Material.field_151575_d, MapColor.field_151645_D, block4)).func_149711_c(0.2F).func_149672_a(SoundType.field_185848_a).func_149663_c("mushroom"));
        func_176219_a(101, "iron_bars", (new BlockPane(Material.field_151573_f, true)).func_149711_c(5.0F).func_149752_b(10.0F).func_149672_a(SoundType.field_185852_e).func_149663_c("fenceIron"));
        func_176219_a(102, "glass_pane", (new BlockPane(Material.field_151592_s, false)).func_149711_c(0.3F).func_149672_a(SoundType.field_185853_f).func_149663_c("thinGlass"));
        Block block9 = (new BlockMelon()).func_149711_c(1.0F).func_149672_a(SoundType.field_185848_a).func_149663_c("melon");

        func_176219_a(103, "melon_block", block9);
        func_176219_a(104, "pumpkin_stem", (new BlockStem(block7)).func_149711_c(0.0F).func_149672_a(SoundType.field_185848_a).func_149663_c("pumpkinStem"));
        func_176219_a(105, "melon_stem", (new BlockStem(block9)).func_149711_c(0.0F).func_149672_a(SoundType.field_185848_a).func_149663_c("pumpkinStem"));
        func_176219_a(106, "vine", (new BlockVine()).func_149711_c(0.2F).func_149672_a(SoundType.field_185850_c).func_149663_c("vine"));
        func_176219_a(107, "fence_gate", (new BlockFenceGate(BlockPlanks.EnumType.OAK)).func_149711_c(2.0F).func_149752_b(5.0F).func_149672_a(SoundType.field_185848_a).func_149663_c("fenceGate"));
        func_176219_a(108, "brick_stairs", (new BlockStairs(block5.func_176223_P())).func_149663_c("stairsBrick"));
        func_176219_a(109, "stone_brick_stairs", (new BlockStairs(block8.func_176223_P().func_177226_a(BlockStoneBrick.field_176249_a, BlockStoneBrick.EnumType.DEFAULT))).func_149663_c("stairsStoneBrickSmooth"));
        func_176219_a(110, "mycelium", (new BlockMycelium()).func_149711_c(0.6F).func_149672_a(SoundType.field_185850_c).func_149663_c("mycel"));
        func_176219_a(111, "waterlily", (new BlockLilyPad()).func_149711_c(0.0F).func_149672_a(SoundType.field_185850_c).func_149663_c("waterlily"));
        Block block10 = (new BlockNetherBrick()).func_149711_c(2.0F).func_149752_b(10.0F).func_149672_a(SoundType.field_185851_d).func_149663_c("netherBrick").func_149647_a(CreativeTabs.field_78030_b);

        func_176219_a(112, "nether_brick", block10);
        func_176219_a(113, "nether_brick_fence", (new BlockFence(Material.field_151576_e, MapColor.field_151655_K)).func_149711_c(2.0F).func_149752_b(10.0F).func_149672_a(SoundType.field_185851_d).func_149663_c("netherFence"));
        func_176219_a(114, "nether_brick_stairs", (new BlockStairs(block10.func_176223_P())).func_149663_c("stairsNetherBrick"));
        func_176219_a(115, "nether_wart", (new BlockNetherWart()).func_149663_c("netherStalk"));
        func_176219_a(116, "enchanting_table", (new BlockEnchantmentTable()).func_149711_c(5.0F).func_149752_b(2000.0F).func_149663_c("enchantmentTable"));
        func_176219_a(117, "brewing_stand", (new BlockBrewingStand()).func_149711_c(0.5F).func_149715_a(0.125F).func_149663_c("brewingStand"));
        func_176219_a(118, "cauldron", (new BlockCauldron()).func_149711_c(2.0F).func_149663_c("cauldron"));
        func_176219_a(119, "end_portal", (new BlockEndPortal(Material.field_151567_E)).func_149711_c(-1.0F).func_149752_b(6000000.0F));
        func_176219_a(120, "end_portal_frame", (new BlockEndPortalFrame()).func_149672_a(SoundType.field_185853_f).func_149715_a(0.125F).func_149711_c(-1.0F).func_149663_c("endPortalFrame").func_149752_b(6000000.0F).func_149647_a(CreativeTabs.field_78031_c));
        func_176219_a(121, "end_stone", (new Block(Material.field_151576_e, MapColor.field_151658_d)).func_149711_c(3.0F).func_149752_b(15.0F).func_149672_a(SoundType.field_185851_d).func_149663_c("whiteStone").func_149647_a(CreativeTabs.field_78030_b));
        func_176219_a(122, "dragon_egg", (new BlockDragonEgg()).func_149711_c(3.0F).func_149752_b(15.0F).func_149672_a(SoundType.field_185851_d).func_149715_a(0.125F).func_149663_c("dragonEgg"));
        func_176219_a(123, "redstone_lamp", (new BlockRedstoneLight(false)).func_149711_c(0.3F).func_149672_a(SoundType.field_185853_f).func_149663_c("redstoneLight").func_149647_a(CreativeTabs.field_78028_d));
        func_176219_a(124, "lit_redstone_lamp", (new BlockRedstoneLight(true)).func_149711_c(0.3F).func_149672_a(SoundType.field_185853_f).func_149663_c("redstoneLight"));
        func_176219_a(125, "double_wooden_slab", (new BlockDoubleWoodSlab()).func_149711_c(2.0F).func_149752_b(5.0F).func_149672_a(SoundType.field_185848_a).func_149663_c("woodSlab"));
        func_176219_a(126, "wooden_slab", (new BlockHalfWoodSlab()).func_149711_c(2.0F).func_149752_b(5.0F).func_149672_a(SoundType.field_185848_a).func_149663_c("woodSlab"));
        func_176219_a(127, "cocoa", (new BlockCocoa()).func_149711_c(0.2F).func_149752_b(5.0F).func_149672_a(SoundType.field_185848_a).func_149663_c("cocoa"));
        func_176219_a(128, "sandstone_stairs", (new BlockStairs(block2.func_176223_P().func_177226_a(BlockSandStone.field_176297_a, BlockSandStone.EnumType.SMOOTH))).func_149663_c("stairsSandStone"));
        func_176219_a(129, "emerald_ore", (new BlockOre()).func_149711_c(3.0F).func_149752_b(5.0F).func_149672_a(SoundType.field_185851_d).func_149663_c("oreEmerald"));
        func_176219_a(130, "ender_chest", (new BlockEnderChest()).func_149711_c(22.5F).func_149752_b(1000.0F).func_149672_a(SoundType.field_185851_d).func_149663_c("enderChest").func_149715_a(0.5F));
        func_176219_a(131, "tripwire_hook", (new BlockTripWireHook()).func_149663_c("tripWireSource"));
        func_176219_a(132, "tripwire", (new BlockTripWire()).func_149663_c("tripWire"));
        func_176219_a(133, "emerald_block", (new Block(Material.field_151573_f, MapColor.field_151653_I)).func_149711_c(5.0F).func_149752_b(10.0F).func_149672_a(SoundType.field_185852_e).func_149663_c("blockEmerald").func_149647_a(CreativeTabs.field_78030_b));
        func_176219_a(134, "spruce_stairs", (new BlockStairs(block1.func_176223_P().func_177226_a(BlockPlanks.field_176383_a, BlockPlanks.EnumType.SPRUCE))).func_149663_c("stairsWoodSpruce"));
        func_176219_a(135, "birch_stairs", (new BlockStairs(block1.func_176223_P().func_177226_a(BlockPlanks.field_176383_a, BlockPlanks.EnumType.BIRCH))).func_149663_c("stairsWoodBirch"));
        func_176219_a(136, "jungle_stairs", (new BlockStairs(block1.func_176223_P().func_177226_a(BlockPlanks.field_176383_a, BlockPlanks.EnumType.JUNGLE))).func_149663_c("stairsWoodJungle"));
        func_176219_a(137, "command_block", (new BlockCommandBlock(MapColor.field_151650_B)).func_149722_s().func_149752_b(6000000.0F).func_149663_c("commandBlock"));
        func_176219_a(138, "beacon", (new BlockBeacon()).func_149663_c("beacon").func_149715_a(1.0F));
        func_176219_a(139, "cobblestone_wall", (new BlockWall(block)).func_149663_c("cobbleWall"));
        func_176219_a(140, "flower_pot", (new BlockFlowerPot()).func_149711_c(0.0F).func_149672_a(SoundType.field_185851_d).func_149663_c("flowerPot"));
        func_176219_a(141, "carrots", (new BlockCarrot()).func_149663_c("carrots"));
        func_176219_a(142, "potatoes", (new BlockPotato()).func_149663_c("potatoes"));
        func_176219_a(143, "wooden_button", (new BlockButtonWood()).func_149711_c(0.5F).func_149672_a(SoundType.field_185848_a).func_149663_c("button"));
        func_176219_a(144, "skull", (new BlockSkull()).func_149711_c(1.0F).func_149672_a(SoundType.field_185851_d).func_149663_c("skull"));
        func_176219_a(145, "anvil", (new BlockAnvil()).func_149711_c(5.0F).func_149672_a(SoundType.field_185858_k).func_149752_b(2000.0F).func_149663_c("anvil"));
        func_176219_a(146, "trapped_chest", (new BlockChest(BlockChest.Type.TRAP)).func_149711_c(2.5F).func_149672_a(SoundType.field_185848_a).func_149663_c("chestTrap"));
        func_176219_a(147, "light_weighted_pressure_plate", (new BlockPressurePlateWeighted(Material.field_151573_f, 15, MapColor.field_151647_F)).func_149711_c(0.5F).func_149672_a(SoundType.field_185848_a).func_149663_c("weightedPlate_light"));
        func_176219_a(148, "heavy_weighted_pressure_plate", (new BlockPressurePlateWeighted(Material.field_151573_f, 150)).func_149711_c(0.5F).func_149672_a(SoundType.field_185848_a).func_149663_c("weightedPlate_heavy"));
        func_176219_a(149, "unpowered_comparator", (new BlockRedstoneComparator(false)).func_149711_c(0.0F).func_149672_a(SoundType.field_185848_a).func_149663_c("comparator").func_149649_H());
        func_176219_a(150, "powered_comparator", (new BlockRedstoneComparator(true)).func_149711_c(0.0F).func_149715_a(0.625F).func_149672_a(SoundType.field_185848_a).func_149663_c("comparator").func_149649_H());
        func_176219_a(151, "daylight_detector", new BlockDaylightDetector(false));
        func_176219_a(152, "redstone_block", (new BlockCompressedPowered(Material.field_151573_f, MapColor.field_151656_f)).func_149711_c(5.0F).func_149752_b(10.0F).func_149672_a(SoundType.field_185852_e).func_149663_c("blockRedstone").func_149647_a(CreativeTabs.field_78028_d));
        func_176219_a(153, "quartz_ore", (new BlockOre(MapColor.field_151655_K)).func_149711_c(3.0F).func_149752_b(5.0F).func_149672_a(SoundType.field_185851_d).func_149663_c("netherquartz"));
        func_176219_a(154, "hopper", (new BlockHopper()).func_149711_c(3.0F).func_149752_b(8.0F).func_149672_a(SoundType.field_185852_e).func_149663_c("hopper"));
        Block block11 = (new BlockQuartz()).func_149672_a(SoundType.field_185851_d).func_149711_c(0.8F).func_149663_c("quartzBlock");

        func_176219_a(155, "quartz_block", block11);
        func_176219_a(156, "quartz_stairs", (new BlockStairs(block11.func_176223_P().func_177226_a(BlockQuartz.field_176335_a, BlockQuartz.EnumType.DEFAULT))).func_149663_c("stairsQuartz"));
        func_176219_a(157, "activator_rail", (new BlockRailPowered()).func_149711_c(0.7F).func_149672_a(SoundType.field_185852_e).func_149663_c("activatorRail"));
        func_176219_a(158, "dropper", (new BlockDropper()).func_149711_c(3.5F).func_149672_a(SoundType.field_185851_d).func_149663_c("dropper"));
        func_176219_a(159, "stained_hardened_clay", (new BlockStainedHardenedClay()).func_149711_c(1.25F).func_149752_b(7.0F).func_149672_a(SoundType.field_185851_d).func_149663_c("clayHardenedStained"));
        func_176219_a(160, "stained_glass_pane", (new BlockStainedGlassPane()).func_149711_c(0.3F).func_149672_a(SoundType.field_185853_f).func_149663_c("thinStainedGlass"));
        func_176219_a(161, "leaves2", (new BlockNewLeaf()).func_149663_c("leaves"));
        func_176219_a(162, "log2", (new BlockNewLog()).func_149663_c("log"));
        func_176219_a(163, "acacia_stairs", (new BlockStairs(block1.func_176223_P().func_177226_a(BlockPlanks.field_176383_a, BlockPlanks.EnumType.ACACIA))).func_149663_c("stairsWoodAcacia"));
        func_176219_a(164, "dark_oak_stairs", (new BlockStairs(block1.func_176223_P().func_177226_a(BlockPlanks.field_176383_a, BlockPlanks.EnumType.DARK_OAK))).func_149663_c("stairsWoodDarkOak"));
        func_176219_a(165, "slime", (new BlockSlime()).func_149663_c("slime").func_149672_a(SoundType.field_185859_l));
        func_176219_a(166, "barrier", (new BlockBarrier()).func_149663_c("barrier"));
        func_176219_a(167, "iron_trapdoor", (new BlockTrapDoor(Material.field_151573_f)).func_149711_c(5.0F).func_149672_a(SoundType.field_185852_e).func_149663_c("ironTrapdoor").func_149649_H());
        func_176219_a(168, "prismarine", (new BlockPrismarine()).func_149711_c(1.5F).func_149752_b(10.0F).func_149672_a(SoundType.field_185851_d).func_149663_c("prismarine"));
        func_176219_a(169, "sea_lantern", (new BlockSeaLantern(Material.field_151592_s)).func_149711_c(0.3F).func_149672_a(SoundType.field_185853_f).func_149715_a(1.0F).func_149663_c("seaLantern"));
        func_176219_a(170, "hay_block", (new BlockHay()).func_149711_c(0.5F).func_149672_a(SoundType.field_185850_c).func_149663_c("hayBlock").func_149647_a(CreativeTabs.field_78030_b));
        func_176219_a(171, "carpet", (new BlockCarpet()).func_149711_c(0.1F).func_149672_a(SoundType.field_185854_g).func_149663_c("woolCarpet").func_149713_g(0));
        func_176219_a(172, "hardened_clay", (new BlockHardenedClay()).func_149711_c(1.25F).func_149752_b(7.0F).func_149672_a(SoundType.field_185851_d).func_149663_c("clayHardened"));
        func_176219_a(173, "coal_block", (new Block(Material.field_151576_e, MapColor.field_151646_E)).func_149711_c(5.0F).func_149752_b(10.0F).func_149672_a(SoundType.field_185851_d).func_149663_c("blockCoal").func_149647_a(CreativeTabs.field_78030_b));
        func_176219_a(174, "packed_ice", (new BlockPackedIce()).func_149711_c(0.5F).func_149672_a(SoundType.field_185853_f).func_149663_c("icePacked"));
        func_176219_a(175, "double_plant", new BlockDoublePlant());
        func_176219_a(176, "standing_banner", (new BlockBanner.BlockBannerStanding()).func_149711_c(1.0F).func_149672_a(SoundType.field_185848_a).func_149663_c("banner").func_149649_H());
        func_176219_a(177, "wall_banner", (new BlockBanner.BlockBannerHanging()).func_149711_c(1.0F).func_149672_a(SoundType.field_185848_a).func_149663_c("banner").func_149649_H());
        func_176219_a(178, "daylight_detector_inverted", new BlockDaylightDetector(true));
        Block block12 = (new BlockRedSandstone()).func_149672_a(SoundType.field_185851_d).func_149711_c(0.8F).func_149663_c("redSandStone");

        func_176219_a(179, "red_sandstone", block12);
        func_176219_a(180, "red_sandstone_stairs", (new BlockStairs(block12.func_176223_P().func_177226_a(BlockRedSandstone.field_176336_a, BlockRedSandstone.EnumType.SMOOTH))).func_149663_c("stairsRedSandStone"));
        func_176219_a(181, "double_stone_slab2", (new BlockDoubleStoneSlabNew()).func_149711_c(2.0F).func_149752_b(10.0F).func_149672_a(SoundType.field_185851_d).func_149663_c("stoneSlab2"));
        func_176219_a(182, "stone_slab2", (new BlockHalfStoneSlabNew()).func_149711_c(2.0F).func_149752_b(10.0F).func_149672_a(SoundType.field_185851_d).func_149663_c("stoneSlab2"));
        func_176219_a(183, "spruce_fence_gate", (new BlockFenceGate(BlockPlanks.EnumType.SPRUCE)).func_149711_c(2.0F).func_149752_b(5.0F).func_149672_a(SoundType.field_185848_a).func_149663_c("spruceFenceGate"));
        func_176219_a(184, "birch_fence_gate", (new BlockFenceGate(BlockPlanks.EnumType.BIRCH)).func_149711_c(2.0F).func_149752_b(5.0F).func_149672_a(SoundType.field_185848_a).func_149663_c("birchFenceGate"));
        func_176219_a(185, "jungle_fence_gate", (new BlockFenceGate(BlockPlanks.EnumType.JUNGLE)).func_149711_c(2.0F).func_149752_b(5.0F).func_149672_a(SoundType.field_185848_a).func_149663_c("jungleFenceGate"));
        func_176219_a(186, "dark_oak_fence_gate", (new BlockFenceGate(BlockPlanks.EnumType.DARK_OAK)).func_149711_c(2.0F).func_149752_b(5.0F).func_149672_a(SoundType.field_185848_a).func_149663_c("darkOakFenceGate"));
        func_176219_a(187, "acacia_fence_gate", (new BlockFenceGate(BlockPlanks.EnumType.ACACIA)).func_149711_c(2.0F).func_149752_b(5.0F).func_149672_a(SoundType.field_185848_a).func_149663_c("acaciaFenceGate"));
        func_176219_a(188, "spruce_fence", (new BlockFence(Material.field_151575_d, BlockPlanks.EnumType.SPRUCE.func_181070_c())).func_149711_c(2.0F).func_149752_b(5.0F).func_149672_a(SoundType.field_185848_a).func_149663_c("spruceFence"));
        func_176219_a(189, "birch_fence", (new BlockFence(Material.field_151575_d, BlockPlanks.EnumType.BIRCH.func_181070_c())).func_149711_c(2.0F).func_149752_b(5.0F).func_149672_a(SoundType.field_185848_a).func_149663_c("birchFence"));
        func_176219_a(190, "jungle_fence", (new BlockFence(Material.field_151575_d, BlockPlanks.EnumType.JUNGLE.func_181070_c())).func_149711_c(2.0F).func_149752_b(5.0F).func_149672_a(SoundType.field_185848_a).func_149663_c("jungleFence"));
        func_176219_a(191, "dark_oak_fence", (new BlockFence(Material.field_151575_d, BlockPlanks.EnumType.DARK_OAK.func_181070_c())).func_149711_c(2.0F).func_149752_b(5.0F).func_149672_a(SoundType.field_185848_a).func_149663_c("darkOakFence"));
        func_176219_a(192, "acacia_fence", (new BlockFence(Material.field_151575_d, BlockPlanks.EnumType.ACACIA.func_181070_c())).func_149711_c(2.0F).func_149752_b(5.0F).func_149672_a(SoundType.field_185848_a).func_149663_c("acaciaFence"));
        func_176219_a(193, "spruce_door", (new BlockDoor(Material.field_151575_d)).func_149711_c(3.0F).func_149672_a(SoundType.field_185848_a).func_149663_c("doorSpruce").func_149649_H());
        func_176219_a(194, "birch_door", (new BlockDoor(Material.field_151575_d)).func_149711_c(3.0F).func_149672_a(SoundType.field_185848_a).func_149663_c("doorBirch").func_149649_H());
        func_176219_a(195, "jungle_door", (new BlockDoor(Material.field_151575_d)).func_149711_c(3.0F).func_149672_a(SoundType.field_185848_a).func_149663_c("doorJungle").func_149649_H());
        func_176219_a(196, "acacia_door", (new BlockDoor(Material.field_151575_d)).func_149711_c(3.0F).func_149672_a(SoundType.field_185848_a).func_149663_c("doorAcacia").func_149649_H());
        func_176219_a(197, "dark_oak_door", (new BlockDoor(Material.field_151575_d)).func_149711_c(3.0F).func_149672_a(SoundType.field_185848_a).func_149663_c("doorDarkOak").func_149649_H());
        func_176219_a(198, "end_rod", (new BlockEndRod()).func_149711_c(0.0F).func_149715_a(0.9375F).func_149672_a(SoundType.field_185848_a).func_149663_c("endRod"));
        func_176219_a(199, "chorus_plant", (new BlockChorusPlant()).func_149711_c(0.4F).func_149672_a(SoundType.field_185848_a).func_149663_c("chorusPlant"));
        func_176219_a(200, "chorus_flower", (new BlockChorusFlower()).func_149711_c(0.4F).func_149672_a(SoundType.field_185848_a).func_149663_c("chorusFlower"));
        Block block13 = (new Block(Material.field_151576_e, MapColor.field_151675_r)).func_149711_c(1.5F).func_149752_b(10.0F).func_149672_a(SoundType.field_185851_d).func_149647_a(CreativeTabs.field_78030_b).func_149663_c("purpurBlock");

        func_176219_a(201, "purpur_block", block13);
        func_176219_a(202, "purpur_pillar", (new BlockRotatedPillar(Material.field_151576_e, MapColor.field_151675_r)).func_149711_c(1.5F).func_149752_b(10.0F).func_149672_a(SoundType.field_185851_d).func_149647_a(CreativeTabs.field_78030_b).func_149663_c("purpurPillar"));
        func_176219_a(203, "purpur_stairs", (new BlockStairs(block13.func_176223_P())).func_149663_c("stairsPurpur"));
        func_176219_a(204, "purpur_double_slab", (new BlockPurpurSlab.Double()).func_149711_c(2.0F).func_149752_b(10.0F).func_149672_a(SoundType.field_185851_d).func_149663_c("purpurSlab"));
        func_176219_a(205, "purpur_slab", (new BlockPurpurSlab.Half()).func_149711_c(2.0F).func_149752_b(10.0F).func_149672_a(SoundType.field_185851_d).func_149663_c("purpurSlab"));
        func_176219_a(206, "end_bricks", (new Block(Material.field_151576_e, MapColor.field_151658_d)).func_149672_a(SoundType.field_185851_d).func_149711_c(0.8F).func_149647_a(CreativeTabs.field_78030_b).func_149663_c("endBricks"));
        func_176219_a(207, "beetroots", (new BlockBeetroot()).func_149663_c("beetroots"));
        Block block14 = (new BlockGrassPath()).func_149711_c(0.65F).func_149672_a(SoundType.field_185850_c).func_149663_c("grassPath").func_149649_H();

        func_176219_a(208, "grass_path", block14);
        func_176219_a(209, "end_gateway", (new BlockEndGateway(Material.field_151567_E)).func_149711_c(-1.0F).func_149752_b(6000000.0F));
        func_176219_a(210, "repeating_command_block", (new BlockCommandBlock(MapColor.field_151678_z)).func_149722_s().func_149752_b(6000000.0F).func_149663_c("repeatingCommandBlock"));
        func_176219_a(211, "chain_command_block", (new BlockCommandBlock(MapColor.field_151651_C)).func_149722_s().func_149752_b(6000000.0F).func_149663_c("chainCommandBlock"));
        func_176219_a(212, "frosted_ice", (new BlockFrostedIce()).func_149711_c(0.5F).func_149713_g(3).func_149672_a(SoundType.field_185853_f).func_149663_c("frostedIce"));
        func_176219_a(213, "magma", (new BlockMagma()).func_149711_c(0.5F).func_149672_a(SoundType.field_185851_d).func_149663_c("magma"));
        func_176219_a(214, "nether_wart_block", (new Block(Material.field_151577_b, MapColor.field_151645_D)).func_149647_a(CreativeTabs.field_78030_b).func_149711_c(1.0F).func_149672_a(SoundType.field_185848_a).func_149663_c("netherWartBlock"));
        func_176219_a(215, "red_nether_brick", (new BlockNetherBrick()).func_149711_c(2.0F).func_149752_b(10.0F).func_149672_a(SoundType.field_185851_d).func_149663_c("redNetherBrick").func_149647_a(CreativeTabs.field_78030_b));
        func_176219_a(216, "bone_block", (new BlockBone()).func_149663_c("boneBlock"));
        func_176219_a(217, "structure_void", (new BlockStructureVoid()).func_149663_c("structureVoid"));
        func_176219_a(218, "observer", (new BlockObserver()).func_149711_c(3.0F).func_149663_c("observer"));
        func_176219_a(219, "white_shulker_box", (new BlockShulkerBox(EnumDyeColor.WHITE)).func_149711_c(2.0F).func_149672_a(SoundType.field_185851_d).func_149663_c("shulkerBoxWhite"));
        func_176219_a(220, "orange_shulker_box", (new BlockShulkerBox(EnumDyeColor.ORANGE)).func_149711_c(2.0F).func_149672_a(SoundType.field_185851_d).func_149663_c("shulkerBoxOrange"));
        func_176219_a(221, "magenta_shulker_box", (new BlockShulkerBox(EnumDyeColor.MAGENTA)).func_149711_c(2.0F).func_149672_a(SoundType.field_185851_d).func_149663_c("shulkerBoxMagenta"));
        func_176219_a(222, "light_blue_shulker_box", (new BlockShulkerBox(EnumDyeColor.LIGHT_BLUE)).func_149711_c(2.0F).func_149672_a(SoundType.field_185851_d).func_149663_c("shulkerBoxLightBlue"));
        func_176219_a(223, "yellow_shulker_box", (new BlockShulkerBox(EnumDyeColor.YELLOW)).func_149711_c(2.0F).func_149672_a(SoundType.field_185851_d).func_149663_c("shulkerBoxYellow"));
        func_176219_a(224, "lime_shulker_box", (new BlockShulkerBox(EnumDyeColor.LIME)).func_149711_c(2.0F).func_149672_a(SoundType.field_185851_d).func_149663_c("shulkerBoxLime"));
        func_176219_a(225, "pink_shulker_box", (new BlockShulkerBox(EnumDyeColor.PINK)).func_149711_c(2.0F).func_149672_a(SoundType.field_185851_d).func_149663_c("shulkerBoxPink"));
        func_176219_a(226, "gray_shulker_box", (new BlockShulkerBox(EnumDyeColor.GRAY)).func_149711_c(2.0F).func_149672_a(SoundType.field_185851_d).func_149663_c("shulkerBoxGray"));
        func_176219_a(227, "silver_shulker_box", (new BlockShulkerBox(EnumDyeColor.SILVER)).func_149711_c(2.0F).func_149672_a(SoundType.field_185851_d).func_149663_c("shulkerBoxSilver"));
        func_176219_a(228, "cyan_shulker_box", (new BlockShulkerBox(EnumDyeColor.CYAN)).func_149711_c(2.0F).func_149672_a(SoundType.field_185851_d).func_149663_c("shulkerBoxCyan"));
        func_176219_a(229, "purple_shulker_box", (new BlockShulkerBox(EnumDyeColor.PURPLE)).func_149711_c(2.0F).func_149672_a(SoundType.field_185851_d).func_149663_c("shulkerBoxPurple"));
        func_176219_a(230, "blue_shulker_box", (new BlockShulkerBox(EnumDyeColor.BLUE)).func_149711_c(2.0F).func_149672_a(SoundType.field_185851_d).func_149663_c("shulkerBoxBlue"));
        func_176219_a(231, "brown_shulker_box", (new BlockShulkerBox(EnumDyeColor.BROWN)).func_149711_c(2.0F).func_149672_a(SoundType.field_185851_d).func_149663_c("shulkerBoxBrown"));
        func_176219_a(232, "green_shulker_box", (new BlockShulkerBox(EnumDyeColor.GREEN)).func_149711_c(2.0F).func_149672_a(SoundType.field_185851_d).func_149663_c("shulkerBoxGreen"));
        func_176219_a(233, "red_shulker_box", (new BlockShulkerBox(EnumDyeColor.RED)).func_149711_c(2.0F).func_149672_a(SoundType.field_185851_d).func_149663_c("shulkerBoxRed"));
        func_176219_a(234, "black_shulker_box", (new BlockShulkerBox(EnumDyeColor.BLACK)).func_149711_c(2.0F).func_149672_a(SoundType.field_185851_d).func_149663_c("shulkerBoxBlack"));
        func_176219_a(235, "white_glazed_terracotta", new BlockGlazedTerracotta(EnumDyeColor.WHITE));
        func_176219_a(236, "orange_glazed_terracotta", new BlockGlazedTerracotta(EnumDyeColor.ORANGE));
        func_176219_a(237, "magenta_glazed_terracotta", new BlockGlazedTerracotta(EnumDyeColor.MAGENTA));
        func_176219_a(238, "light_blue_glazed_terracotta", new BlockGlazedTerracotta(EnumDyeColor.LIGHT_BLUE));
        func_176219_a(239, "yellow_glazed_terracotta", new BlockGlazedTerracotta(EnumDyeColor.YELLOW));
        func_176219_a(240, "lime_glazed_terracotta", new BlockGlazedTerracotta(EnumDyeColor.LIME));
        func_176219_a(241, "pink_glazed_terracotta", new BlockGlazedTerracotta(EnumDyeColor.PINK));
        func_176219_a(242, "gray_glazed_terracotta", new BlockGlazedTerracotta(EnumDyeColor.GRAY));
        func_176219_a(243, "silver_glazed_terracotta", new BlockGlazedTerracotta(EnumDyeColor.SILVER));
        func_176219_a(244, "cyan_glazed_terracotta", new BlockGlazedTerracotta(EnumDyeColor.CYAN));
        func_176219_a(245, "purple_glazed_terracotta", new BlockGlazedTerracotta(EnumDyeColor.PURPLE));
        func_176219_a(246, "blue_glazed_terracotta", new BlockGlazedTerracotta(EnumDyeColor.BLUE));
        func_176219_a(247, "brown_glazed_terracotta", new BlockGlazedTerracotta(EnumDyeColor.BROWN));
        func_176219_a(248, "green_glazed_terracotta", new BlockGlazedTerracotta(EnumDyeColor.GREEN));
        func_176219_a(249, "red_glazed_terracotta", new BlockGlazedTerracotta(EnumDyeColor.RED));
        func_176219_a(250, "black_glazed_terracotta", new BlockGlazedTerracotta(EnumDyeColor.BLACK));
        func_176219_a(251, "concrete", (new BlockColored(Material.field_151576_e)).func_149711_c(1.8F).func_149672_a(SoundType.field_185851_d).func_149663_c("concrete"));
        func_176219_a(252, "concrete_powder", (new BlockConcretePowder()).func_149711_c(0.5F).func_149672_a(SoundType.field_185855_h).func_149663_c("concretePowder"));
        func_176219_a(255, "structure_block", (new BlockStructure()).func_149722_s().func_149752_b(6000000.0F).func_149663_c("structureBlock"));
        Block.field_149771_c.func_177776_a();
        Iterator iterator = Block.field_149771_c.iterator();

        while (iterator.hasNext()) {
            Block block15 = (Block) iterator.next();

            if (block15.field_149764_J == Material.field_151579_a) {
                block15.field_149783_u = false;
            } else {
                boolean flag = false;
                boolean flag1 = block15 instanceof BlockStairs;
                boolean flag2 = block15 instanceof BlockSlab;
                boolean flag3 = block15 == block6 || block15 == block14;
                boolean flag4 = block15.field_149785_s;
                boolean flag5 = block15.field_149786_r == 0;

                if (flag1 || flag2 || flag3 || flag4 || flag5) {
                    flag = true;
                }

                block15.field_149783_u = flag;
            }
        }

        HashSet hashset = Sets.newHashSet(new Block[] { (Block) Block.field_149771_c.func_82594_a(new ResourceLocation("tripwire"))});
        Iterator iterator1 = Block.field_149771_c.iterator();

        while (iterator1.hasNext()) {
            Block block16 = (Block) iterator1.next();

            if (hashset.contains(block16)) {
                for (int i = 0; i < 15; ++i) {
                    int j = Block.field_149771_c.func_148757_b(block16) << 4 | i; // CraftBukkit - decompile error

                    Block.field_176229_d.func_148746_a(block16.func_176203_a(i), j);
                }
            } else {
                UnmodifiableIterator unmodifiableiterator = block16.func_176194_O().func_177619_a().iterator();

                while (unmodifiableiterator.hasNext()) {
                    IBlockState iblockdata = (IBlockState) unmodifiableiterator.next();
                    int k = Block.field_149771_c.func_148757_b(block16) << 4 | block16.func_176201_c(iblockdata); // CraftBukkit - decompile error

                    Block.field_176229_d.func_148746_a(iblockdata, k);
                }
            }
        }

    }

    // CraftBukkit start
    public int getExpDrop(World world, IBlockState data, int enchantmentLevel) {
        return 0;
    }
    // CraftBukkit end

    private static void func_176215_a(int i, ResourceLocation minecraftkey, Block block) {
        Block.field_149771_c.func_177775_a(i, minecraftkey, block);
    }

    private static void func_176219_a(int i, String s, Block block) {
        func_176215_a(i, new ResourceLocation(s), block);
    }

    // Spigot start
    public static float range(float min, float value, float max) {
        if (value < min) {
            return min;
        }
        if (value > max) {
            return max;
        }
        return value;
    }
    // Spigot end

    public static enum EnumOffsetType {

        NONE, XZ, XYZ;

        private EnumOffsetType() {}
    }
}
