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

    private static final ResourceLocation AIR_ID = new ResourceLocation("air");
    public static final RegistryNamespacedDefaultedByKey<ResourceLocation, Block> REGISTRY = new RegistryNamespacedDefaultedByKey(Block.AIR_ID);
    public static final ObjectIntIdentityMap<IBlockState> BLOCK_STATE_IDS = new ObjectIntIdentityMap();
    public static final AxisAlignedBB FULL_BLOCK_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
    @Nullable
    public static final AxisAlignedBB NULL_AABB = null;
    private CreativeTabs displayOnCreativeTab;
    protected boolean fullBlock;
    protected int lightOpacity;
    protected boolean translucent;
    protected int lightValue;
    protected boolean useNeighborBrightness;
    protected float blockHardness;
    protected float blockResistance;
    protected boolean enableStats;
    protected boolean needsRandomTick;
    protected boolean hasTileEntity;
    protected SoundType blockSoundType;
    public float blockParticleGravity;
    protected final Material blockMaterial;
    protected final MapColor blockMapColor;
    public float slipperiness;
    protected final BlockStateContainer blockState;
    private IBlockState defaultBlockState;
    private String unlocalizedName;
    // Paper start
    public co.aikar.timings.Timing timing;
    public co.aikar.timings.Timing getTiming() {
        if (timing == null) {
            timing = co.aikar.timings.MinecraftTimings.getBlockTiming(this);
        }
        return timing;
    }
    // Paper end

    public static int getIdFromBlock(Block block) {
        return Block.REGISTRY.getIDForObject(block); // CraftBukkit - decompile error
    }

    public static int getStateId(IBlockState iblockdata) {
        Block block = iblockdata.getBlock();

        return getIdFromBlock(block) + (block.getMetaFromState(iblockdata) << 12);
    }

    public static Block getBlockById(int i) {
        return (Block) Block.REGISTRY.getObjectById(i);
    }

    public static IBlockState getStateById(int i) {
        int j = i & 4095;
        int k = i >> 12 & 15;

        return getBlockById(j).getStateFromMeta(k);
    }

    public static Block getBlockFromItem(@Nullable Item item) {
        return item instanceof ItemBlock ? ((ItemBlock) item).getBlock() : Blocks.AIR;
    }

    @Nullable
    public static Block getBlockFromName(String s) {
        ResourceLocation minecraftkey = new ResourceLocation(s);

        if (Block.REGISTRY.containsKey(minecraftkey)) {
            return (Block) Block.REGISTRY.getObject(minecraftkey);
        } else {
            try {
                return (Block) Block.REGISTRY.getObjectById(Integer.parseInt(s));
            } catch (NumberFormatException numberformatexception) {
                return null;
            }
        }
    }

    @Deprecated
    public boolean isTopSolid(IBlockState iblockdata) {
        return iblockdata.getMaterial().isOpaque() && iblockdata.isFullCube();
    }

    @Deprecated
    public boolean isFullBlock(IBlockState iblockdata) {
        return this.fullBlock;
    }

    @Deprecated
    public boolean canEntitySpawn(IBlockState iblockdata, Entity entity) {
        return true;
    }

    @Deprecated
    public int getLightOpacity(IBlockState iblockdata) {
        return this.lightOpacity;
    }

    @Deprecated
    public int getLightValue(IBlockState iblockdata) {
        return this.lightValue;
    }

    @Deprecated
    public boolean getUseNeighborBrightness(IBlockState iblockdata) {
        return this.useNeighborBrightness;
    }

    @Deprecated
    public Material getMaterial(IBlockState iblockdata) {
        return this.blockMaterial;
    }

    @Deprecated
    public MapColor getMapColor(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        return this.blockMapColor;
    }

    @Deprecated
    public IBlockState getStateFromMeta(int i) {
        return this.getDefaultState();
    }

    public int getMetaFromState(IBlockState iblockdata) {
        if (iblockdata.getPropertyKeys().isEmpty()) {
            return 0;
        } else {
            throw new IllegalArgumentException("Don\'t know how to convert " + iblockdata + " back into data...");
        }
    }

    @Deprecated
    public IBlockState getActualState(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        return iblockdata;
    }

    @Deprecated
    public IBlockState withRotation(IBlockState iblockdata, Rotation enumblockrotation) {
        return iblockdata;
    }

    @Deprecated
    public IBlockState withMirror(IBlockState iblockdata, Mirror enumblockmirror) {
        return iblockdata;
    }

    public Block(Material material, MapColor materialmapcolor) {
        this.enableStats = true;
        this.blockSoundType = SoundType.STONE;
        this.blockParticleGravity = 1.0F;
        this.slipperiness = 0.6F;
        this.blockMaterial = material;
        this.blockMapColor = materialmapcolor;
        this.blockState = this.createBlockState();
        this.setDefaultState(this.blockState.getBaseState());
        this.fullBlock = this.getDefaultState().isOpaqueCube();
        this.lightOpacity = this.fullBlock ? 255 : 0;
        this.translucent = !material.blocksLight();
    }

    protected Block(Material material) {
        this(material, material.getMaterialMapColor());
    }

    protected Block setSoundType(SoundType soundeffecttype) {
        this.blockSoundType = soundeffecttype;
        return this;
    }

    protected Block setLightOpacity(int i) {
        this.lightOpacity = i;
        return this;
    }

    protected Block setLightLevel(float f) {
        this.lightValue = (int) (15.0F * f);
        return this;
    }

    protected Block setResistance(float f) {
        this.blockResistance = f * 3.0F;
        return this;
    }

    protected static boolean isExceptionBlockForAttaching(Block block) {
        return block instanceof BlockShulkerBox || block instanceof BlockLeaves || block instanceof BlockTrapDoor || block == Blocks.BEACON || block == Blocks.CAULDRON || block == Blocks.GLASS || block == Blocks.GLOWSTONE || block == Blocks.ICE || block == Blocks.SEA_LANTERN || block == Blocks.STAINED_GLASS;
    }

    protected static boolean isExceptBlockForAttachWithPiston(Block block) {
        return isExceptionBlockForAttaching(block) || block == Blocks.PISTON || block == Blocks.STICKY_PISTON || block == Blocks.PISTON_HEAD;
    }

    @Deprecated
    public boolean isBlockNormalCube(IBlockState iblockdata) {
        return iblockdata.getMaterial().blocksMovement() && iblockdata.isFullCube();
    }

    @Deprecated
    public boolean isNormalCube(IBlockState iblockdata) {
        return iblockdata.getMaterial().isOpaque() && iblockdata.isFullCube() && !iblockdata.canProvidePower();
    }

    @Deprecated
    public boolean causesSuffocation(IBlockState iblockdata) {
        return this.blockMaterial.blocksMovement() && this.getDefaultState().isFullCube();
    }

    @Deprecated
    public boolean isFullCube(IBlockState iblockdata) {
        return true;
    }

    public boolean isPassable(IBlockAccess iblockaccess, BlockPos blockposition) {
        return !this.blockMaterial.blocksMovement();
    }

    @Deprecated
    public EnumBlockRenderType getRenderType(IBlockState iblockdata) {
        return EnumBlockRenderType.MODEL;
    }

    public boolean isReplaceable(IBlockAccess iblockaccess, BlockPos blockposition) {
        return false;
    }

    protected Block setHardness(float f) {
        this.blockHardness = f;
        if (this.blockResistance < f * 5.0F) {
            this.blockResistance = f * 5.0F;
        }

        return this;
    }

    protected Block setBlockUnbreakable() {
        this.setHardness(-1.0F);
        return this;
    }

    @Deprecated
    public float getBlockHardness(IBlockState iblockdata, World world, BlockPos blockposition) {
        return this.blockHardness;
    }

    protected Block setTickRandomly(boolean flag) {
        this.needsRandomTick = flag;
        return this;
    }

    public boolean getTickRandomly() {
        return this.needsRandomTick;
    }

    public boolean hasTileEntity() {
        return this.hasTileEntity;
    }

    @Deprecated
    public AxisAlignedBB getBoundingBox(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        return Block.FULL_BLOCK_AABB;
    }

    @Deprecated
    public BlockFaceShape getBlockFaceShape(IBlockAccess iblockaccess, IBlockState iblockdata, BlockPos blockposition, EnumFacing enumdirection) {
        return BlockFaceShape.SOLID;
    }

    @Deprecated
    public void addCollisionBoxToList(IBlockState iblockdata, World world, BlockPos blockposition, AxisAlignedBB axisalignedbb, List<AxisAlignedBB> list, @Nullable Entity entity, boolean flag) {
        addCollisionBoxToList(blockposition, axisalignedbb, list, iblockdata.getCollisionBoundingBox(world, blockposition));
    }

    protected static void addCollisionBoxToList(BlockPos blockposition, AxisAlignedBB axisalignedbb, List<AxisAlignedBB> list, @Nullable AxisAlignedBB axisalignedbb1) {
        if (axisalignedbb1 != Block.NULL_AABB) {
            AxisAlignedBB axisalignedbb2 = axisalignedbb1.offset(blockposition);

            if (axisalignedbb.intersects(axisalignedbb2)) {
                list.add(axisalignedbb2);
            }
        }

    }

    @Deprecated
    @Nullable
    public AxisAlignedBB getCollisionBoundingBox(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        return iblockdata.getBoundingBox(iblockaccess, blockposition);
    }

    @Deprecated
    public boolean isOpaqueCube(IBlockState iblockdata) {
        return true;
    }

    public boolean canCollideCheck(IBlockState iblockdata, boolean flag) {
        return this.isCollidable();
    }

    public boolean isCollidable() {
        return true;
    }

    public void randomTick(World world, BlockPos blockposition, IBlockState iblockdata, Random random) {
        this.updateTick(world, blockposition, iblockdata, random);
    }

    public void updateTick(World world, BlockPos blockposition, IBlockState iblockdata, Random random) {}

    public void onBlockDestroyedByPlayer(World world, BlockPos blockposition, IBlockState iblockdata) {}

    @Deprecated
    public void neighborChanged(IBlockState iblockdata, World world, BlockPos blockposition, Block block, BlockPos blockposition1) {}

    public int tickRate(World world) {
        return 10;
    }

    public void onBlockAdded(World world, BlockPos blockposition, IBlockState iblockdata) {
        org.spigotmc.AsyncCatcher.catchOp( "block onPlace"); // Spigot
    }

    public void breakBlock(World world, BlockPos blockposition, IBlockState iblockdata) {
        org.spigotmc.AsyncCatcher.catchOp( "block remove"); // Spigot
    }

    public int quantityDropped(Random random) {
        return 1;
    }

    public Item getItemDropped(IBlockState iblockdata, Random random, int i) {
        return Item.getItemFromBlock(this);
    }

    @Deprecated
    public float getPlayerRelativeBlockHardness(IBlockState iblockdata, EntityPlayer entityhuman, World world, BlockPos blockposition) {
        float f = iblockdata.getBlockHardness(world, blockposition);

        return f < 0.0F ? 0.0F : (!entityhuman.canHarvestBlock(iblockdata) ? entityhuman.getDigSpeed(iblockdata) / f / 100.0F : entityhuman.getDigSpeed(iblockdata) / f / 30.0F);
    }

    public final void dropBlockAsItem(World world, BlockPos blockposition, IBlockState iblockdata, int i) {
        this.dropBlockAsItemWithChance(world, blockposition, iblockdata, 1.0F, i);
    }

    public void dropBlockAsItemWithChance(World world, BlockPos blockposition, IBlockState iblockdata, float f, int i) {
        if (!world.isRemote) {
            int j = this.quantityDroppedWithBonus(i, world.rand);

            for (int k = 0; k < j; ++k) {
                // CraftBukkit - <= to < to allow for plugins to completely disable block drops from explosions
                if (world.rand.nextFloat() < f) {
                    Item item = this.getItemDropped(iblockdata, world.rand, i);

                    if (item != Items.AIR) {
                        spawnAsEntity(world, blockposition, new ItemStack(item, 1, this.damageDropped(iblockdata)));
                    }
                }
            }

        }
    }

    public static void spawnAsEntity(World world, BlockPos blockposition, ItemStack itemstack) {
        if (!world.isRemote && !itemstack.isEmpty() && world.getGameRules().getBoolean("doTileDrops")) {
            float f = 0.5F;
            double d0 = (double) (world.rand.nextFloat() * 0.5F) + 0.25D;
            double d1 = (double) (world.rand.nextFloat() * 0.5F) + 0.25D;
            double d2 = (double) (world.rand.nextFloat() * 0.5F) + 0.25D;
            EntityItem entityitem = new EntityItem(world, (double) blockposition.getX() + d0, (double) blockposition.getY() + d1, (double) blockposition.getZ() + d2, itemstack);

            entityitem.setDefaultPickupDelay();
            // CraftBukkit start
            if (world.captureDrops != null) {
                world.captureDrops.add(entityitem);
            } else {
                world.spawnEntity(entityitem);
            }
            // CraftBukkit end
        }
    }

    protected void dropExperience(World world, BlockPos blockposition, int i, EntityPlayerMP player) { // Paper
        if (!world.isRemote && world.getGameRules().getBoolean("doTileDrops")) {
            while (i > 0) {
                int j = EntityXPOrb.getXPSplit(i);

                i -= j;
                world.spawnEntity(new EntityXPOrb(world, (double) blockposition.getX() + 0.5D, (double) blockposition.getY() + 0.5D, (double) blockposition.getZ() + 0.5D, j, org.bukkit.entity.ExperienceOrb.SpawnReason.BLOCK_BREAK, player)); // Paper
            }
        }

    }

    public int damageDropped(IBlockState iblockdata) {
        return 0;
    }

    public float getExplosionResistance(Entity entity) {
        return this.blockResistance / 5.0F;
    }

    @Deprecated
    @Nullable
    public RayTraceResult collisionRayTrace(IBlockState iblockdata, World world, BlockPos blockposition, Vec3d vec3d, Vec3d vec3d1) {
        return this.rayTrace(blockposition, vec3d, vec3d1, iblockdata.getBoundingBox(world, blockposition));
    }

    @Nullable
    protected RayTraceResult rayTrace(BlockPos blockposition, Vec3d vec3d, Vec3d vec3d1, AxisAlignedBB axisalignedbb) {
        Vec3d vec3d2 = vec3d.subtract((double) blockposition.getX(), (double) blockposition.getY(), (double) blockposition.getZ());
        Vec3d vec3d3 = vec3d1.subtract((double) blockposition.getX(), (double) blockposition.getY(), (double) blockposition.getZ());
        RayTraceResult movingobjectposition = axisalignedbb.calculateIntercept(vec3d2, vec3d3);

        return movingobjectposition == null ? null : new RayTraceResult(movingobjectposition.hitVec.addVector((double) blockposition.getX(), (double) blockposition.getY(), (double) blockposition.getZ()), movingobjectposition.sideHit, blockposition);
    }

    public void onBlockDestroyedByExplosion(World world, BlockPos blockposition, Explosion explosion) {}

    public boolean canPlaceBlockOnSide(World world, BlockPos blockposition, EnumFacing enumdirection) {
        return this.canPlaceBlockAt(world, blockposition);
    }

    public boolean canPlaceBlockAt(World world, BlockPos blockposition) {
        return world.getBlockState(blockposition).getBlock().blockMaterial.isReplaceable();
    }

    public boolean onBlockActivated(World world, BlockPos blockposition, IBlockState iblockdata, EntityPlayer entityhuman, EnumHand enumhand, EnumFacing enumdirection, float f, float f1, float f2) {
        return false;
    }

    public void onEntityWalk(World world, BlockPos blockposition, Entity entity) {}

    public IBlockState getStateForPlacement(World world, BlockPos blockposition, EnumFacing enumdirection, float f, float f1, float f2, int i, EntityLivingBase entityliving) {
        return this.getStateFromMeta(i);
    }

    public void onBlockClicked(World world, BlockPos blockposition, EntityPlayer entityhuman) {}

    public Vec3d modifyAcceleration(World world, BlockPos blockposition, Entity entity, Vec3d vec3d) {
        return vec3d;
    }

    @Deprecated
    public int getWeakPower(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition, EnumFacing enumdirection) {
        return 0;
    }

    @Deprecated
    public boolean canProvidePower(IBlockState iblockdata) {
        return false;
    }

    public void onEntityCollidedWithBlock(World world, BlockPos blockposition, IBlockState iblockdata, Entity entity) {}

    @Deprecated
    public int getStrongPower(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition, EnumFacing enumdirection) {
        return 0;
    }

    public void harvestBlock(World world, EntityPlayer entityhuman, BlockPos blockposition, IBlockState iblockdata, @Nullable TileEntity tileentity, ItemStack itemstack) {
        entityhuman.addStat(StatList.getBlockStats(this));
        entityhuman.addExhaustion(0.005F);
        if (this.canSilkHarvest() && EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, itemstack) > 0) {
            ItemStack itemstack1 = this.getSilkTouchDrop(iblockdata);

            spawnAsEntity(world, blockposition, itemstack1);
        } else {
            int i = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, itemstack);

            this.dropBlockAsItem(world, blockposition, iblockdata, i);
        }

    }

    protected boolean canSilkHarvest() {
        return this.getDefaultState().isFullCube() && !this.hasTileEntity;
    }

    protected ItemStack getSilkTouchDrop(IBlockState iblockdata) {
        Item item = Item.getItemFromBlock(this);
        int i = 0;

        if (item.getHasSubtypes()) {
            i = this.getMetaFromState(iblockdata);
        }

        return new ItemStack(item, 1, i);
    }

    public int quantityDroppedWithBonus(int i, Random random) {
        return this.quantityDropped(random);
    }

    public void onBlockPlacedBy(World world, BlockPos blockposition, IBlockState iblockdata, EntityLivingBase entityliving, ItemStack itemstack) {}

    public boolean canSpawnInBlock() {
        return !this.blockMaterial.isSolid() && !this.blockMaterial.isLiquid();
    }

    public Block setUnlocalizedName(String s) {
        this.unlocalizedName = s;
        return this;
    }

    public String getLocalizedName() {
        return I18n.translateToLocal(this.getUnlocalizedName() + ".name");
    }

    public String getUnlocalizedName() {
        return "tile." + this.unlocalizedName;
    }

    @Deprecated
    public boolean eventReceived(IBlockState iblockdata, World world, BlockPos blockposition, int i, int j) {
        return false;
    }

    public boolean getEnableStats() {
        return this.enableStats;
    }

    protected Block disableStats() {
        this.enableStats = false;
        return this;
    }

    @Deprecated
    public EnumPushReaction getMobilityFlag(IBlockState iblockdata) {
        return this.blockMaterial.getMobilityFlag();
    }

    public void onFallenUpon(World world, BlockPos blockposition, Entity entity, float f) {
        entity.fall(f, 1.0F);
    }

    public void onLanded(World world, Entity entity) {
        entity.motionY = 0.0D;
    }

    public ItemStack getItem(World world, BlockPos blockposition, IBlockState iblockdata) {
        return new ItemStack(Item.getItemFromBlock(this), 1, this.damageDropped(iblockdata));
    }

    public void getSubBlocks(CreativeTabs creativemodetab, NonNullList<ItemStack> nonnulllist) {
        nonnulllist.add(new ItemStack(this));
    }

    public CreativeTabs getCreativeTabToDisplayOn() {
        return this.displayOnCreativeTab;
    }

    public Block setCreativeTab(CreativeTabs creativemodetab) {
        this.displayOnCreativeTab = creativemodetab;
        return this;
    }

    public void onBlockHarvested(World world, BlockPos blockposition, IBlockState iblockdata, EntityPlayer entityhuman) {}

    public void fillWithRain(World world, BlockPos blockposition) {}

    public boolean requiresUpdates() {
        return true;
    }

    public boolean canDropFromExplosion(Explosion explosion) {
        return true;
    }

    public boolean isAssociatedBlock(Block block) {
        return this == block;
    }

    public static boolean isEqualTo(Block block, Block block1) {
        return block != null && block1 != null ? (block == block1 ? true : block.isAssociatedBlock(block1)) : false;
    }

    @Deprecated
    public boolean hasComparatorInputOverride(IBlockState iblockdata) {
        return false;
    }

    @Deprecated
    public int getComparatorInputOverride(IBlockState iblockdata, World world, BlockPos blockposition) {
        return 0;
    }

    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[0]);
    }

    public BlockStateContainer getBlockState() {
        return this.blockState;
    }

    protected final void setDefaultState(IBlockState iblockdata) {
        this.defaultBlockState = iblockdata;
    }

    public final IBlockState getDefaultState() {
        return this.defaultBlockState;
    }

    public Block.EnumOffsetType getOffsetType() {
        return Block.EnumOffsetType.NONE;
    }

    @Deprecated
    public Vec3d getOffset(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        Block.EnumOffsetType block_enumrandomoffset = this.getOffsetType();

        if (block_enumrandomoffset == Block.EnumOffsetType.NONE) {
            return Vec3d.ZERO;
        } else {
            long i = MathHelper.getCoordinateRandom(blockposition.getX(), 0, blockposition.getZ());

            return new Vec3d(((double) ((float) (i >> 16 & 15L) / 15.0F) - 0.5D) * 0.5D, block_enumrandomoffset == Block.EnumOffsetType.XYZ ? ((double) ((float) (i >> 20 & 15L) / 15.0F) - 1.0D) * 0.2D : 0.0D, ((double) ((float) (i >> 24 & 15L) / 15.0F) - 0.5D) * 0.5D);
        }
    }

    public SoundType getSoundType() {
        return this.blockSoundType;
    }

    public String toString() {
        return "Block{" + Block.REGISTRY.getNameForObject(this) + "}";
    }

    public static void registerBlocks() {
        registerBlock(0, Block.AIR_ID, (new BlockAir()).setUnlocalizedName("air"));
        registerBlock(1, "stone", (new BlockStone()).setHardness(1.5F).setResistance(10.0F).setSoundType(SoundType.STONE).setUnlocalizedName("stone"));
        registerBlock(2, "grass", (new BlockGrass()).setHardness(0.6F).setSoundType(SoundType.PLANT).setUnlocalizedName("grass"));
        registerBlock(3, "dirt", (new BlockDirt()).setHardness(0.5F).setSoundType(SoundType.GROUND).setUnlocalizedName("dirt"));
        Block block = (new Block(Material.ROCK)).setHardness(2.0F).setResistance(10.0F).setSoundType(SoundType.STONE).setUnlocalizedName("stonebrick").setCreativeTab(CreativeTabs.BUILDING_BLOCKS);

        registerBlock(4, "cobblestone", block);
        Block block1 = (new BlockPlanks()).setHardness(2.0F).setResistance(5.0F).setSoundType(SoundType.WOOD).setUnlocalizedName("wood");

        registerBlock(5, "planks", block1);
        registerBlock(6, "sapling", (new BlockSapling()).setHardness(0.0F).setSoundType(SoundType.PLANT).setUnlocalizedName("sapling"));
        registerBlock(7, "bedrock", (new BlockEmptyDrops(Material.ROCK)).setBlockUnbreakable().setResistance(6000000.0F).setSoundType(SoundType.STONE).setUnlocalizedName("bedrock").disableStats().setCreativeTab(CreativeTabs.BUILDING_BLOCKS));
        registerBlock(8, "flowing_water", (new BlockDynamicLiquid(Material.WATER)).setHardness(100.0F).setLightOpacity(3).setUnlocalizedName("water").disableStats());
        registerBlock(9, "water", (new BlockStaticLiquid(Material.WATER)).setHardness(100.0F).setLightOpacity(3).setUnlocalizedName("water").disableStats());
        registerBlock(10, "flowing_lava", (new BlockDynamicLiquid(Material.LAVA)).setHardness(100.0F).setLightLevel(1.0F).setUnlocalizedName("lava").disableStats());
        registerBlock(11, "lava", (new BlockStaticLiquid(Material.LAVA)).setHardness(100.0F).setLightLevel(1.0F).setUnlocalizedName("lava").disableStats());
        registerBlock(12, "sand", (new BlockSand()).setHardness(0.5F).setSoundType(SoundType.SAND).setUnlocalizedName("sand"));
        registerBlock(13, "gravel", (new BlockGravel()).setHardness(0.6F).setSoundType(SoundType.GROUND).setUnlocalizedName("gravel"));
        registerBlock(14, "gold_ore", (new BlockOre()).setHardness(3.0F).setResistance(5.0F).setSoundType(SoundType.STONE).setUnlocalizedName("oreGold"));
        registerBlock(15, "iron_ore", (new BlockOre()).setHardness(3.0F).setResistance(5.0F).setSoundType(SoundType.STONE).setUnlocalizedName("oreIron"));
        registerBlock(16, "coal_ore", (new BlockOre()).setHardness(3.0F).setResistance(5.0F).setSoundType(SoundType.STONE).setUnlocalizedName("oreCoal"));
        registerBlock(17, "log", (new BlockOldLog()).setUnlocalizedName("log"));
        registerBlock(18, "leaves", (new BlockOldLeaf()).setUnlocalizedName("leaves"));
        registerBlock(19, "sponge", (new BlockSponge()).setHardness(0.6F).setSoundType(SoundType.PLANT).setUnlocalizedName("sponge"));
        registerBlock(20, "glass", (new BlockGlass(Material.GLASS, false)).setHardness(0.3F).setSoundType(SoundType.GLASS).setUnlocalizedName("glass"));
        registerBlock(21, "lapis_ore", (new BlockOre()).setHardness(3.0F).setResistance(5.0F).setSoundType(SoundType.STONE).setUnlocalizedName("oreLapis"));
        registerBlock(22, "lapis_block", (new Block(Material.IRON, MapColor.LAPIS)).setHardness(3.0F).setResistance(5.0F).setSoundType(SoundType.STONE).setUnlocalizedName("blockLapis").setCreativeTab(CreativeTabs.BUILDING_BLOCKS));
        registerBlock(23, "dispenser", (new BlockDispenser()).setHardness(3.5F).setSoundType(SoundType.STONE).setUnlocalizedName("dispenser"));
        Block block2 = (new BlockSandStone()).setSoundType(SoundType.STONE).setHardness(0.8F).setUnlocalizedName("sandStone");

        registerBlock(24, "sandstone", block2);
        registerBlock(25, "noteblock", (new BlockNote()).setSoundType(SoundType.WOOD).setHardness(0.8F).setUnlocalizedName("musicBlock"));
        registerBlock(26, "bed", (new BlockBed()).setSoundType(SoundType.WOOD).setHardness(0.2F).setUnlocalizedName("bed").disableStats());
        registerBlock(27, "golden_rail", (new BlockRailPowered()).setHardness(0.7F).setSoundType(SoundType.METAL).setUnlocalizedName("goldenRail"));
        registerBlock(28, "detector_rail", (new BlockRailDetector()).setHardness(0.7F).setSoundType(SoundType.METAL).setUnlocalizedName("detectorRail"));
        registerBlock(29, "sticky_piston", (new BlockPistonBase(true)).setUnlocalizedName("pistonStickyBase"));
        registerBlock(30, "web", (new BlockWeb()).setLightOpacity(1).setHardness(4.0F).setUnlocalizedName("web"));
        registerBlock(31, "tallgrass", (new BlockTallGrass()).setHardness(0.0F).setSoundType(SoundType.PLANT).setUnlocalizedName("tallgrass"));
        registerBlock(32, "deadbush", (new BlockDeadBush()).setHardness(0.0F).setSoundType(SoundType.PLANT).setUnlocalizedName("deadbush"));
        registerBlock(33, "piston", (new BlockPistonBase(false)).setUnlocalizedName("pistonBase"));
        registerBlock(34, "piston_head", (new BlockPistonExtension()).setUnlocalizedName("pistonBase"));
        registerBlock(35, "wool", (new BlockColored(Material.CLOTH)).setHardness(0.8F).setSoundType(SoundType.CLOTH).setUnlocalizedName("cloth"));
        registerBlock(36, "piston_extension", new BlockPistonMoving());
        registerBlock(37, "yellow_flower", (new BlockYellowFlower()).setHardness(0.0F).setSoundType(SoundType.PLANT).setUnlocalizedName("flower1"));
        registerBlock(38, "red_flower", (new BlockRedFlower()).setHardness(0.0F).setSoundType(SoundType.PLANT).setUnlocalizedName("flower2"));
        Block block3 = (new BlockMushroom()).setHardness(0.0F).setSoundType(SoundType.PLANT).setLightLevel(0.125F).setUnlocalizedName("mushroom");

        registerBlock(39, "brown_mushroom", block3);
        Block block4 = (new BlockMushroom()).setHardness(0.0F).setSoundType(SoundType.PLANT).setUnlocalizedName("mushroom");

        registerBlock(40, "red_mushroom", block4);
        registerBlock(41, "gold_block", (new Block(Material.IRON, MapColor.GOLD)).setHardness(3.0F).setResistance(10.0F).setSoundType(SoundType.METAL).setUnlocalizedName("blockGold").setCreativeTab(CreativeTabs.BUILDING_BLOCKS));
        registerBlock(42, "iron_block", (new Block(Material.IRON, MapColor.IRON)).setHardness(5.0F).setResistance(10.0F).setSoundType(SoundType.METAL).setUnlocalizedName("blockIron").setCreativeTab(CreativeTabs.BUILDING_BLOCKS));
        registerBlock(43, "double_stone_slab", (new BlockDoubleStoneSlab()).setHardness(2.0F).setResistance(10.0F).setSoundType(SoundType.STONE).setUnlocalizedName("stoneSlab"));
        registerBlock(44, "stone_slab", (new BlockHalfStoneSlab()).setHardness(2.0F).setResistance(10.0F).setSoundType(SoundType.STONE).setUnlocalizedName("stoneSlab"));
        Block block5 = (new Block(Material.ROCK, MapColor.RED)).setHardness(2.0F).setResistance(10.0F).setSoundType(SoundType.STONE).setUnlocalizedName("brick").setCreativeTab(CreativeTabs.BUILDING_BLOCKS);

        registerBlock(45, "brick_block", block5);
        registerBlock(46, "tnt", (new BlockTNT()).setHardness(0.0F).setSoundType(SoundType.PLANT).setUnlocalizedName("tnt"));
        registerBlock(47, "bookshelf", (new BlockBookshelf()).setHardness(1.5F).setSoundType(SoundType.WOOD).setUnlocalizedName("bookshelf"));
        registerBlock(48, "mossy_cobblestone", (new Block(Material.ROCK)).setHardness(2.0F).setResistance(10.0F).setSoundType(SoundType.STONE).setUnlocalizedName("stoneMoss").setCreativeTab(CreativeTabs.BUILDING_BLOCKS));
        registerBlock(49, "obsidian", (new BlockObsidian()).setHardness(50.0F).setResistance(2000.0F).setSoundType(SoundType.STONE).setUnlocalizedName("obsidian"));
        registerBlock(50, "torch", (new BlockTorch()).setHardness(0.0F).setLightLevel(0.9375F).setSoundType(SoundType.WOOD).setUnlocalizedName("torch"));
        registerBlock(51, "fire", (new BlockFire()).setHardness(0.0F).setLightLevel(1.0F).setSoundType(SoundType.CLOTH).setUnlocalizedName("fire").disableStats());
        registerBlock(52, "mob_spawner", (new BlockMobSpawner()).setHardness(5.0F).setSoundType(SoundType.METAL).setUnlocalizedName("mobSpawner").disableStats());
        registerBlock(53, "oak_stairs", (new BlockStairs(block1.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.OAK))).setUnlocalizedName("stairsWood"));
        registerBlock(54, "chest", (new BlockChest(BlockChest.Type.BASIC)).setHardness(2.5F).setSoundType(SoundType.WOOD).setUnlocalizedName("chest"));
        registerBlock(55, "redstone_wire", (new BlockRedstoneWire()).setHardness(0.0F).setSoundType(SoundType.STONE).setUnlocalizedName("redstoneDust").disableStats());
        registerBlock(56, "diamond_ore", (new BlockOre()).setHardness(3.0F).setResistance(5.0F).setSoundType(SoundType.STONE).setUnlocalizedName("oreDiamond"));
        registerBlock(57, "diamond_block", (new Block(Material.IRON, MapColor.DIAMOND)).setHardness(5.0F).setResistance(10.0F).setSoundType(SoundType.METAL).setUnlocalizedName("blockDiamond").setCreativeTab(CreativeTabs.BUILDING_BLOCKS));
        registerBlock(58, "crafting_table", (new BlockWorkbench()).setHardness(2.5F).setSoundType(SoundType.WOOD).setUnlocalizedName("workbench"));
        registerBlock(59, "wheat", (new BlockCrops()).setUnlocalizedName("crops"));
        Block block6 = (new BlockFarmland()).setHardness(0.6F).setSoundType(SoundType.GROUND).setUnlocalizedName("farmland");

        registerBlock(60, "farmland", block6);
        registerBlock(61, "furnace", (new BlockFurnace(false)).setHardness(3.5F).setSoundType(SoundType.STONE).setUnlocalizedName("furnace").setCreativeTab(CreativeTabs.DECORATIONS));
        registerBlock(62, "lit_furnace", (new BlockFurnace(true)).setHardness(3.5F).setSoundType(SoundType.STONE).setLightLevel(0.875F).setUnlocalizedName("furnace"));
        registerBlock(63, "standing_sign", (new BlockStandingSign()).setHardness(1.0F).setSoundType(SoundType.WOOD).setUnlocalizedName("sign").disableStats());
        registerBlock(64, "wooden_door", (new BlockDoor(Material.WOOD)).setHardness(3.0F).setSoundType(SoundType.WOOD).setUnlocalizedName("doorOak").disableStats());
        registerBlock(65, "ladder", (new BlockLadder()).setHardness(0.4F).setSoundType(SoundType.LADDER).setUnlocalizedName("ladder"));
        registerBlock(66, "rail", (new BlockRail()).setHardness(0.7F).setSoundType(SoundType.METAL).setUnlocalizedName("rail"));
        registerBlock(67, "stone_stairs", (new BlockStairs(block.getDefaultState())).setUnlocalizedName("stairsStone"));
        registerBlock(68, "wall_sign", (new BlockWallSign()).setHardness(1.0F).setSoundType(SoundType.WOOD).setUnlocalizedName("sign").disableStats());
        registerBlock(69, "lever", (new BlockLever()).setHardness(0.5F).setSoundType(SoundType.WOOD).setUnlocalizedName("lever"));
        registerBlock(70, "stone_pressure_plate", (new BlockPressurePlate(Material.ROCK, BlockPressurePlate.Sensitivity.MOBS)).setHardness(0.5F).setSoundType(SoundType.STONE).setUnlocalizedName("pressurePlateStone"));
        registerBlock(71, "iron_door", (new BlockDoor(Material.IRON)).setHardness(5.0F).setSoundType(SoundType.METAL).setUnlocalizedName("doorIron").disableStats());
        registerBlock(72, "wooden_pressure_plate", (new BlockPressurePlate(Material.WOOD, BlockPressurePlate.Sensitivity.EVERYTHING)).setHardness(0.5F).setSoundType(SoundType.WOOD).setUnlocalizedName("pressurePlateWood"));
        registerBlock(73, "redstone_ore", (new BlockRedstoneOre(false)).setHardness(3.0F).setResistance(5.0F).setSoundType(SoundType.STONE).setUnlocalizedName("oreRedstone").setCreativeTab(CreativeTabs.BUILDING_BLOCKS));
        registerBlock(74, "lit_redstone_ore", (new BlockRedstoneOre(true)).setLightLevel(0.625F).setHardness(3.0F).setResistance(5.0F).setSoundType(SoundType.STONE).setUnlocalizedName("oreRedstone"));
        registerBlock(75, "unlit_redstone_torch", (new BlockRedstoneTorch(false)).setHardness(0.0F).setSoundType(SoundType.WOOD).setUnlocalizedName("notGate"));
        registerBlock(76, "redstone_torch", (new BlockRedstoneTorch(true)).setHardness(0.0F).setLightLevel(0.5F).setSoundType(SoundType.WOOD).setUnlocalizedName("notGate").setCreativeTab(CreativeTabs.REDSTONE));
        registerBlock(77, "stone_button", (new BlockButtonStone()).setHardness(0.5F).setSoundType(SoundType.STONE).setUnlocalizedName("button"));
        registerBlock(78, "snow_layer", (new BlockSnow()).setHardness(0.1F).setSoundType(SoundType.SNOW).setUnlocalizedName("snow").setLightOpacity(0));
        registerBlock(79, "ice", (new BlockIce()).setHardness(0.5F).setLightOpacity(3).setSoundType(SoundType.GLASS).setUnlocalizedName("ice"));
        registerBlock(80, "snow", (new BlockSnowBlock()).setHardness(0.2F).setSoundType(SoundType.SNOW).setUnlocalizedName("snow"));
        registerBlock(81, "cactus", (new BlockCactus()).setHardness(0.4F).setSoundType(SoundType.CLOTH).setUnlocalizedName("cactus"));
        registerBlock(82, "clay", (new BlockClay()).setHardness(0.6F).setSoundType(SoundType.GROUND).setUnlocalizedName("clay"));
        registerBlock(83, "reeds", (new BlockReed()).setHardness(0.0F).setSoundType(SoundType.PLANT).setUnlocalizedName("reeds").disableStats());
        registerBlock(84, "jukebox", (new BlockJukebox()).setHardness(2.0F).setResistance(10.0F).setSoundType(SoundType.STONE).setUnlocalizedName("jukebox"));
        registerBlock(85, "fence", (new BlockFence(Material.WOOD, BlockPlanks.EnumType.OAK.getMapColor())).setHardness(2.0F).setResistance(5.0F).setSoundType(SoundType.WOOD).setUnlocalizedName("fence"));
        Block block7 = (new BlockPumpkin()).setHardness(1.0F).setSoundType(SoundType.WOOD).setUnlocalizedName("pumpkin");

        registerBlock(86, "pumpkin", block7);
        registerBlock(87, "netherrack", (new BlockNetherrack()).setHardness(0.4F).setSoundType(SoundType.STONE).setUnlocalizedName("hellrock"));
        registerBlock(88, "soul_sand", (new BlockSoulSand()).setHardness(0.5F).setSoundType(SoundType.SAND).setUnlocalizedName("hellsand"));
        registerBlock(89, "glowstone", (new BlockGlowstone(Material.GLASS)).setHardness(0.3F).setSoundType(SoundType.GLASS).setLightLevel(1.0F).setUnlocalizedName("lightgem"));
        registerBlock(90, "portal", (new BlockPortal()).setHardness(-1.0F).setSoundType(SoundType.GLASS).setLightLevel(0.75F).setUnlocalizedName("portal"));
        registerBlock(91, "lit_pumpkin", (new BlockPumpkin()).setHardness(1.0F).setSoundType(SoundType.WOOD).setLightLevel(1.0F).setUnlocalizedName("litpumpkin"));
        registerBlock(92, "cake", (new BlockCake()).setHardness(0.5F).setSoundType(SoundType.CLOTH).setUnlocalizedName("cake").disableStats());
        registerBlock(93, "unpowered_repeater", (new BlockRedstoneRepeater(false)).setHardness(0.0F).setSoundType(SoundType.WOOD).setUnlocalizedName("diode").disableStats());
        registerBlock(94, "powered_repeater", (new BlockRedstoneRepeater(true)).setHardness(0.0F).setSoundType(SoundType.WOOD).setUnlocalizedName("diode").disableStats());
        registerBlock(95, "stained_glass", (new BlockStainedGlass(Material.GLASS)).setHardness(0.3F).setSoundType(SoundType.GLASS).setUnlocalizedName("stainedGlass"));
        registerBlock(96, "trapdoor", (new BlockTrapDoor(Material.WOOD)).setHardness(3.0F).setSoundType(SoundType.WOOD).setUnlocalizedName("trapdoor").disableStats());
        registerBlock(97, "monster_egg", (new BlockSilverfish()).setHardness(0.75F).setUnlocalizedName("monsterStoneEgg"));
        Block block8 = (new BlockStoneBrick()).setHardness(1.5F).setResistance(10.0F).setSoundType(SoundType.STONE).setUnlocalizedName("stonebricksmooth");

        registerBlock(98, "stonebrick", block8);
        registerBlock(99, "brown_mushroom_block", (new BlockHugeMushroom(Material.WOOD, MapColor.DIRT, block3)).setHardness(0.2F).setSoundType(SoundType.WOOD).setUnlocalizedName("mushroom"));
        registerBlock(100, "red_mushroom_block", (new BlockHugeMushroom(Material.WOOD, MapColor.RED, block4)).setHardness(0.2F).setSoundType(SoundType.WOOD).setUnlocalizedName("mushroom"));
        registerBlock(101, "iron_bars", (new BlockPane(Material.IRON, true)).setHardness(5.0F).setResistance(10.0F).setSoundType(SoundType.METAL).setUnlocalizedName("fenceIron"));
        registerBlock(102, "glass_pane", (new BlockPane(Material.GLASS, false)).setHardness(0.3F).setSoundType(SoundType.GLASS).setUnlocalizedName("thinGlass"));
        Block block9 = (new BlockMelon()).setHardness(1.0F).setSoundType(SoundType.WOOD).setUnlocalizedName("melon");

        registerBlock(103, "melon_block", block9);
        registerBlock(104, "pumpkin_stem", (new BlockStem(block7)).setHardness(0.0F).setSoundType(SoundType.WOOD).setUnlocalizedName("pumpkinStem"));
        registerBlock(105, "melon_stem", (new BlockStem(block9)).setHardness(0.0F).setSoundType(SoundType.WOOD).setUnlocalizedName("pumpkinStem"));
        registerBlock(106, "vine", (new BlockVine()).setHardness(0.2F).setSoundType(SoundType.PLANT).setUnlocalizedName("vine"));
        registerBlock(107, "fence_gate", (new BlockFenceGate(BlockPlanks.EnumType.OAK)).setHardness(2.0F).setResistance(5.0F).setSoundType(SoundType.WOOD).setUnlocalizedName("fenceGate"));
        registerBlock(108, "brick_stairs", (new BlockStairs(block5.getDefaultState())).setUnlocalizedName("stairsBrick"));
        registerBlock(109, "stone_brick_stairs", (new BlockStairs(block8.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.DEFAULT))).setUnlocalizedName("stairsStoneBrickSmooth"));
        registerBlock(110, "mycelium", (new BlockMycelium()).setHardness(0.6F).setSoundType(SoundType.PLANT).setUnlocalizedName("mycel"));
        registerBlock(111, "waterlily", (new BlockLilyPad()).setHardness(0.0F).setSoundType(SoundType.PLANT).setUnlocalizedName("waterlily"));
        Block block10 = (new BlockNetherBrick()).setHardness(2.0F).setResistance(10.0F).setSoundType(SoundType.STONE).setUnlocalizedName("netherBrick").setCreativeTab(CreativeTabs.BUILDING_BLOCKS);

        registerBlock(112, "nether_brick", block10);
        registerBlock(113, "nether_brick_fence", (new BlockFence(Material.ROCK, MapColor.NETHERRACK)).setHardness(2.0F).setResistance(10.0F).setSoundType(SoundType.STONE).setUnlocalizedName("netherFence"));
        registerBlock(114, "nether_brick_stairs", (new BlockStairs(block10.getDefaultState())).setUnlocalizedName("stairsNetherBrick"));
        registerBlock(115, "nether_wart", (new BlockNetherWart()).setUnlocalizedName("netherStalk"));
        registerBlock(116, "enchanting_table", (new BlockEnchantmentTable()).setHardness(5.0F).setResistance(2000.0F).setUnlocalizedName("enchantmentTable"));
        registerBlock(117, "brewing_stand", (new BlockBrewingStand()).setHardness(0.5F).setLightLevel(0.125F).setUnlocalizedName("brewingStand"));
        registerBlock(118, "cauldron", (new BlockCauldron()).setHardness(2.0F).setUnlocalizedName("cauldron"));
        registerBlock(119, "end_portal", (new BlockEndPortal(Material.PORTAL)).setHardness(-1.0F).setResistance(6000000.0F));
        registerBlock(120, "end_portal_frame", (new BlockEndPortalFrame()).setSoundType(SoundType.GLASS).setLightLevel(0.125F).setHardness(-1.0F).setUnlocalizedName("endPortalFrame").setResistance(6000000.0F).setCreativeTab(CreativeTabs.DECORATIONS));
        registerBlock(121, "end_stone", (new Block(Material.ROCK, MapColor.SAND)).setHardness(3.0F).setResistance(15.0F).setSoundType(SoundType.STONE).setUnlocalizedName("whiteStone").setCreativeTab(CreativeTabs.BUILDING_BLOCKS));
        registerBlock(122, "dragon_egg", (new BlockDragonEgg()).setHardness(3.0F).setResistance(15.0F).setSoundType(SoundType.STONE).setLightLevel(0.125F).setUnlocalizedName("dragonEgg"));
        registerBlock(123, "redstone_lamp", (new BlockRedstoneLight(false)).setHardness(0.3F).setSoundType(SoundType.GLASS).setUnlocalizedName("redstoneLight").setCreativeTab(CreativeTabs.REDSTONE));
        registerBlock(124, "lit_redstone_lamp", (new BlockRedstoneLight(true)).setHardness(0.3F).setSoundType(SoundType.GLASS).setUnlocalizedName("redstoneLight"));
        registerBlock(125, "double_wooden_slab", (new BlockDoubleWoodSlab()).setHardness(2.0F).setResistance(5.0F).setSoundType(SoundType.WOOD).setUnlocalizedName("woodSlab"));
        registerBlock(126, "wooden_slab", (new BlockHalfWoodSlab()).setHardness(2.0F).setResistance(5.0F).setSoundType(SoundType.WOOD).setUnlocalizedName("woodSlab"));
        registerBlock(127, "cocoa", (new BlockCocoa()).setHardness(0.2F).setResistance(5.0F).setSoundType(SoundType.WOOD).setUnlocalizedName("cocoa"));
        registerBlock(128, "sandstone_stairs", (new BlockStairs(block2.getDefaultState().withProperty(BlockSandStone.TYPE, BlockSandStone.EnumType.SMOOTH))).setUnlocalizedName("stairsSandStone"));
        registerBlock(129, "emerald_ore", (new BlockOre()).setHardness(3.0F).setResistance(5.0F).setSoundType(SoundType.STONE).setUnlocalizedName("oreEmerald"));
        registerBlock(130, "ender_chest", (new BlockEnderChest()).setHardness(22.5F).setResistance(1000.0F).setSoundType(SoundType.STONE).setUnlocalizedName("enderChest").setLightLevel(0.5F));
        registerBlock(131, "tripwire_hook", (new BlockTripWireHook()).setUnlocalizedName("tripWireSource"));
        registerBlock(132, "tripwire", (new BlockTripWire()).setUnlocalizedName("tripWire"));
        registerBlock(133, "emerald_block", (new Block(Material.IRON, MapColor.EMERALD)).setHardness(5.0F).setResistance(10.0F).setSoundType(SoundType.METAL).setUnlocalizedName("blockEmerald").setCreativeTab(CreativeTabs.BUILDING_BLOCKS));
        registerBlock(134, "spruce_stairs", (new BlockStairs(block1.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.SPRUCE))).setUnlocalizedName("stairsWoodSpruce"));
        registerBlock(135, "birch_stairs", (new BlockStairs(block1.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.BIRCH))).setUnlocalizedName("stairsWoodBirch"));
        registerBlock(136, "jungle_stairs", (new BlockStairs(block1.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.JUNGLE))).setUnlocalizedName("stairsWoodJungle"));
        registerBlock(137, "command_block", (new BlockCommandBlock(MapColor.BROWN)).setBlockUnbreakable().setResistance(6000000.0F).setUnlocalizedName("commandBlock"));
        registerBlock(138, "beacon", (new BlockBeacon()).setUnlocalizedName("beacon").setLightLevel(1.0F));
        registerBlock(139, "cobblestone_wall", (new BlockWall(block)).setUnlocalizedName("cobbleWall"));
        registerBlock(140, "flower_pot", (new BlockFlowerPot()).setHardness(0.0F).setSoundType(SoundType.STONE).setUnlocalizedName("flowerPot"));
        registerBlock(141, "carrots", (new BlockCarrot()).setUnlocalizedName("carrots"));
        registerBlock(142, "potatoes", (new BlockPotato()).setUnlocalizedName("potatoes"));
        registerBlock(143, "wooden_button", (new BlockButtonWood()).setHardness(0.5F).setSoundType(SoundType.WOOD).setUnlocalizedName("button"));
        registerBlock(144, "skull", (new BlockSkull()).setHardness(1.0F).setSoundType(SoundType.STONE).setUnlocalizedName("skull"));
        registerBlock(145, "anvil", (new BlockAnvil()).setHardness(5.0F).setSoundType(SoundType.ANVIL).setResistance(2000.0F).setUnlocalizedName("anvil"));
        registerBlock(146, "trapped_chest", (new BlockChest(BlockChest.Type.TRAP)).setHardness(2.5F).setSoundType(SoundType.WOOD).setUnlocalizedName("chestTrap"));
        registerBlock(147, "light_weighted_pressure_plate", (new BlockPressurePlateWeighted(Material.IRON, 15, MapColor.GOLD)).setHardness(0.5F).setSoundType(SoundType.WOOD).setUnlocalizedName("weightedPlate_light"));
        registerBlock(148, "heavy_weighted_pressure_plate", (new BlockPressurePlateWeighted(Material.IRON, 150)).setHardness(0.5F).setSoundType(SoundType.WOOD).setUnlocalizedName("weightedPlate_heavy"));
        registerBlock(149, "unpowered_comparator", (new BlockRedstoneComparator(false)).setHardness(0.0F).setSoundType(SoundType.WOOD).setUnlocalizedName("comparator").disableStats());
        registerBlock(150, "powered_comparator", (new BlockRedstoneComparator(true)).setHardness(0.0F).setLightLevel(0.625F).setSoundType(SoundType.WOOD).setUnlocalizedName("comparator").disableStats());
        registerBlock(151, "daylight_detector", new BlockDaylightDetector(false));
        registerBlock(152, "redstone_block", (new BlockCompressedPowered(Material.IRON, MapColor.TNT)).setHardness(5.0F).setResistance(10.0F).setSoundType(SoundType.METAL).setUnlocalizedName("blockRedstone").setCreativeTab(CreativeTabs.REDSTONE));
        registerBlock(153, "quartz_ore", (new BlockOre(MapColor.NETHERRACK)).setHardness(3.0F).setResistance(5.0F).setSoundType(SoundType.STONE).setUnlocalizedName("netherquartz"));
        registerBlock(154, "hopper", (new BlockHopper()).setHardness(3.0F).setResistance(8.0F).setSoundType(SoundType.METAL).setUnlocalizedName("hopper"));
        Block block11 = (new BlockQuartz()).setSoundType(SoundType.STONE).setHardness(0.8F).setUnlocalizedName("quartzBlock");

        registerBlock(155, "quartz_block", block11);
        registerBlock(156, "quartz_stairs", (new BlockStairs(block11.getDefaultState().withProperty(BlockQuartz.VARIANT, BlockQuartz.EnumType.DEFAULT))).setUnlocalizedName("stairsQuartz"));
        registerBlock(157, "activator_rail", (new BlockRailPowered()).setHardness(0.7F).setSoundType(SoundType.METAL).setUnlocalizedName("activatorRail"));
        registerBlock(158, "dropper", (new BlockDropper()).setHardness(3.5F).setSoundType(SoundType.STONE).setUnlocalizedName("dropper"));
        registerBlock(159, "stained_hardened_clay", (new BlockStainedHardenedClay()).setHardness(1.25F).setResistance(7.0F).setSoundType(SoundType.STONE).setUnlocalizedName("clayHardenedStained"));
        registerBlock(160, "stained_glass_pane", (new BlockStainedGlassPane()).setHardness(0.3F).setSoundType(SoundType.GLASS).setUnlocalizedName("thinStainedGlass"));
        registerBlock(161, "leaves2", (new BlockNewLeaf()).setUnlocalizedName("leaves"));
        registerBlock(162, "log2", (new BlockNewLog()).setUnlocalizedName("log"));
        registerBlock(163, "acacia_stairs", (new BlockStairs(block1.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.ACACIA))).setUnlocalizedName("stairsWoodAcacia"));
        registerBlock(164, "dark_oak_stairs", (new BlockStairs(block1.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.DARK_OAK))).setUnlocalizedName("stairsWoodDarkOak"));
        registerBlock(165, "slime", (new BlockSlime()).setUnlocalizedName("slime").setSoundType(SoundType.SLIME));
        registerBlock(166, "barrier", (new BlockBarrier()).setUnlocalizedName("barrier"));
        registerBlock(167, "iron_trapdoor", (new BlockTrapDoor(Material.IRON)).setHardness(5.0F).setSoundType(SoundType.METAL).setUnlocalizedName("ironTrapdoor").disableStats());
        registerBlock(168, "prismarine", (new BlockPrismarine()).setHardness(1.5F).setResistance(10.0F).setSoundType(SoundType.STONE).setUnlocalizedName("prismarine"));
        registerBlock(169, "sea_lantern", (new BlockSeaLantern(Material.GLASS)).setHardness(0.3F).setSoundType(SoundType.GLASS).setLightLevel(1.0F).setUnlocalizedName("seaLantern"));
        registerBlock(170, "hay_block", (new BlockHay()).setHardness(0.5F).setSoundType(SoundType.PLANT).setUnlocalizedName("hayBlock").setCreativeTab(CreativeTabs.BUILDING_BLOCKS));
        registerBlock(171, "carpet", (new BlockCarpet()).setHardness(0.1F).setSoundType(SoundType.CLOTH).setUnlocalizedName("woolCarpet").setLightOpacity(0));
        registerBlock(172, "hardened_clay", (new BlockHardenedClay()).setHardness(1.25F).setResistance(7.0F).setSoundType(SoundType.STONE).setUnlocalizedName("clayHardened"));
        registerBlock(173, "coal_block", (new Block(Material.ROCK, MapColor.BLACK)).setHardness(5.0F).setResistance(10.0F).setSoundType(SoundType.STONE).setUnlocalizedName("blockCoal").setCreativeTab(CreativeTabs.BUILDING_BLOCKS));
        registerBlock(174, "packed_ice", (new BlockPackedIce()).setHardness(0.5F).setSoundType(SoundType.GLASS).setUnlocalizedName("icePacked"));
        registerBlock(175, "double_plant", new BlockDoublePlant());
        registerBlock(176, "standing_banner", (new BlockBanner.BlockBannerStanding()).setHardness(1.0F).setSoundType(SoundType.WOOD).setUnlocalizedName("banner").disableStats());
        registerBlock(177, "wall_banner", (new BlockBanner.BlockBannerHanging()).setHardness(1.0F).setSoundType(SoundType.WOOD).setUnlocalizedName("banner").disableStats());
        registerBlock(178, "daylight_detector_inverted", new BlockDaylightDetector(true));
        Block block12 = (new BlockRedSandstone()).setSoundType(SoundType.STONE).setHardness(0.8F).setUnlocalizedName("redSandStone");

        registerBlock(179, "red_sandstone", block12);
        registerBlock(180, "red_sandstone_stairs", (new BlockStairs(block12.getDefaultState().withProperty(BlockRedSandstone.TYPE, BlockRedSandstone.EnumType.SMOOTH))).setUnlocalizedName("stairsRedSandStone"));
        registerBlock(181, "double_stone_slab2", (new BlockDoubleStoneSlabNew()).setHardness(2.0F).setResistance(10.0F).setSoundType(SoundType.STONE).setUnlocalizedName("stoneSlab2"));
        registerBlock(182, "stone_slab2", (new BlockHalfStoneSlabNew()).setHardness(2.0F).setResistance(10.0F).setSoundType(SoundType.STONE).setUnlocalizedName("stoneSlab2"));
        registerBlock(183, "spruce_fence_gate", (new BlockFenceGate(BlockPlanks.EnumType.SPRUCE)).setHardness(2.0F).setResistance(5.0F).setSoundType(SoundType.WOOD).setUnlocalizedName("spruceFenceGate"));
        registerBlock(184, "birch_fence_gate", (new BlockFenceGate(BlockPlanks.EnumType.BIRCH)).setHardness(2.0F).setResistance(5.0F).setSoundType(SoundType.WOOD).setUnlocalizedName("birchFenceGate"));
        registerBlock(185, "jungle_fence_gate", (new BlockFenceGate(BlockPlanks.EnumType.JUNGLE)).setHardness(2.0F).setResistance(5.0F).setSoundType(SoundType.WOOD).setUnlocalizedName("jungleFenceGate"));
        registerBlock(186, "dark_oak_fence_gate", (new BlockFenceGate(BlockPlanks.EnumType.DARK_OAK)).setHardness(2.0F).setResistance(5.0F).setSoundType(SoundType.WOOD).setUnlocalizedName("darkOakFenceGate"));
        registerBlock(187, "acacia_fence_gate", (new BlockFenceGate(BlockPlanks.EnumType.ACACIA)).setHardness(2.0F).setResistance(5.0F).setSoundType(SoundType.WOOD).setUnlocalizedName("acaciaFenceGate"));
        registerBlock(188, "spruce_fence", (new BlockFence(Material.WOOD, BlockPlanks.EnumType.SPRUCE.getMapColor())).setHardness(2.0F).setResistance(5.0F).setSoundType(SoundType.WOOD).setUnlocalizedName("spruceFence"));
        registerBlock(189, "birch_fence", (new BlockFence(Material.WOOD, BlockPlanks.EnumType.BIRCH.getMapColor())).setHardness(2.0F).setResistance(5.0F).setSoundType(SoundType.WOOD).setUnlocalizedName("birchFence"));
        registerBlock(190, "jungle_fence", (new BlockFence(Material.WOOD, BlockPlanks.EnumType.JUNGLE.getMapColor())).setHardness(2.0F).setResistance(5.0F).setSoundType(SoundType.WOOD).setUnlocalizedName("jungleFence"));
        registerBlock(191, "dark_oak_fence", (new BlockFence(Material.WOOD, BlockPlanks.EnumType.DARK_OAK.getMapColor())).setHardness(2.0F).setResistance(5.0F).setSoundType(SoundType.WOOD).setUnlocalizedName("darkOakFence"));
        registerBlock(192, "acacia_fence", (new BlockFence(Material.WOOD, BlockPlanks.EnumType.ACACIA.getMapColor())).setHardness(2.0F).setResistance(5.0F).setSoundType(SoundType.WOOD).setUnlocalizedName("acaciaFence"));
        registerBlock(193, "spruce_door", (new BlockDoor(Material.WOOD)).setHardness(3.0F).setSoundType(SoundType.WOOD).setUnlocalizedName("doorSpruce").disableStats());
        registerBlock(194, "birch_door", (new BlockDoor(Material.WOOD)).setHardness(3.0F).setSoundType(SoundType.WOOD).setUnlocalizedName("doorBirch").disableStats());
        registerBlock(195, "jungle_door", (new BlockDoor(Material.WOOD)).setHardness(3.0F).setSoundType(SoundType.WOOD).setUnlocalizedName("doorJungle").disableStats());
        registerBlock(196, "acacia_door", (new BlockDoor(Material.WOOD)).setHardness(3.0F).setSoundType(SoundType.WOOD).setUnlocalizedName("doorAcacia").disableStats());
        registerBlock(197, "dark_oak_door", (new BlockDoor(Material.WOOD)).setHardness(3.0F).setSoundType(SoundType.WOOD).setUnlocalizedName("doorDarkOak").disableStats());
        registerBlock(198, "end_rod", (new BlockEndRod()).setHardness(0.0F).setLightLevel(0.9375F).setSoundType(SoundType.WOOD).setUnlocalizedName("endRod"));
        registerBlock(199, "chorus_plant", (new BlockChorusPlant()).setHardness(0.4F).setSoundType(SoundType.WOOD).setUnlocalizedName("chorusPlant"));
        registerBlock(200, "chorus_flower", (new BlockChorusFlower()).setHardness(0.4F).setSoundType(SoundType.WOOD).setUnlocalizedName("chorusFlower"));
        Block block13 = (new Block(Material.ROCK, MapColor.MAGENTA)).setHardness(1.5F).setResistance(10.0F).setSoundType(SoundType.STONE).setCreativeTab(CreativeTabs.BUILDING_BLOCKS).setUnlocalizedName("purpurBlock");

        registerBlock(201, "purpur_block", block13);
        registerBlock(202, "purpur_pillar", (new BlockRotatedPillar(Material.ROCK, MapColor.MAGENTA)).setHardness(1.5F).setResistance(10.0F).setSoundType(SoundType.STONE).setCreativeTab(CreativeTabs.BUILDING_BLOCKS).setUnlocalizedName("purpurPillar"));
        registerBlock(203, "purpur_stairs", (new BlockStairs(block13.getDefaultState())).setUnlocalizedName("stairsPurpur"));
        registerBlock(204, "purpur_double_slab", (new BlockPurpurSlab.Double()).setHardness(2.0F).setResistance(10.0F).setSoundType(SoundType.STONE).setUnlocalizedName("purpurSlab"));
        registerBlock(205, "purpur_slab", (new BlockPurpurSlab.Half()).setHardness(2.0F).setResistance(10.0F).setSoundType(SoundType.STONE).setUnlocalizedName("purpurSlab"));
        registerBlock(206, "end_bricks", (new Block(Material.ROCK, MapColor.SAND)).setSoundType(SoundType.STONE).setHardness(0.8F).setCreativeTab(CreativeTabs.BUILDING_BLOCKS).setUnlocalizedName("endBricks"));
        registerBlock(207, "beetroots", (new BlockBeetroot()).setUnlocalizedName("beetroots"));
        Block block14 = (new BlockGrassPath()).setHardness(0.65F).setSoundType(SoundType.PLANT).setUnlocalizedName("grassPath").disableStats();

        registerBlock(208, "grass_path", block14);
        registerBlock(209, "end_gateway", (new BlockEndGateway(Material.PORTAL)).setHardness(-1.0F).setResistance(6000000.0F));
        registerBlock(210, "repeating_command_block", (new BlockCommandBlock(MapColor.PURPLE)).setBlockUnbreakable().setResistance(6000000.0F).setUnlocalizedName("repeatingCommandBlock"));
        registerBlock(211, "chain_command_block", (new BlockCommandBlock(MapColor.GREEN)).setBlockUnbreakable().setResistance(6000000.0F).setUnlocalizedName("chainCommandBlock"));
        registerBlock(212, "frosted_ice", (new BlockFrostedIce()).setHardness(0.5F).setLightOpacity(3).setSoundType(SoundType.GLASS).setUnlocalizedName("frostedIce"));
        registerBlock(213, "magma", (new BlockMagma()).setHardness(0.5F).setSoundType(SoundType.STONE).setUnlocalizedName("magma"));
        registerBlock(214, "nether_wart_block", (new Block(Material.GRASS, MapColor.RED)).setCreativeTab(CreativeTabs.BUILDING_BLOCKS).setHardness(1.0F).setSoundType(SoundType.WOOD).setUnlocalizedName("netherWartBlock"));
        registerBlock(215, "red_nether_brick", (new BlockNetherBrick()).setHardness(2.0F).setResistance(10.0F).setSoundType(SoundType.STONE).setUnlocalizedName("redNetherBrick").setCreativeTab(CreativeTabs.BUILDING_BLOCKS));
        registerBlock(216, "bone_block", (new BlockBone()).setUnlocalizedName("boneBlock"));
        registerBlock(217, "structure_void", (new BlockStructureVoid()).setUnlocalizedName("structureVoid"));
        registerBlock(218, "observer", (new BlockObserver()).setHardness(3.0F).setUnlocalizedName("observer"));
        registerBlock(219, "white_shulker_box", (new BlockShulkerBox(EnumDyeColor.WHITE)).setHardness(2.0F).setSoundType(SoundType.STONE).setUnlocalizedName("shulkerBoxWhite"));
        registerBlock(220, "orange_shulker_box", (new BlockShulkerBox(EnumDyeColor.ORANGE)).setHardness(2.0F).setSoundType(SoundType.STONE).setUnlocalizedName("shulkerBoxOrange"));
        registerBlock(221, "magenta_shulker_box", (new BlockShulkerBox(EnumDyeColor.MAGENTA)).setHardness(2.0F).setSoundType(SoundType.STONE).setUnlocalizedName("shulkerBoxMagenta"));
        registerBlock(222, "light_blue_shulker_box", (new BlockShulkerBox(EnumDyeColor.LIGHT_BLUE)).setHardness(2.0F).setSoundType(SoundType.STONE).setUnlocalizedName("shulkerBoxLightBlue"));
        registerBlock(223, "yellow_shulker_box", (new BlockShulkerBox(EnumDyeColor.YELLOW)).setHardness(2.0F).setSoundType(SoundType.STONE).setUnlocalizedName("shulkerBoxYellow"));
        registerBlock(224, "lime_shulker_box", (new BlockShulkerBox(EnumDyeColor.LIME)).setHardness(2.0F).setSoundType(SoundType.STONE).setUnlocalizedName("shulkerBoxLime"));
        registerBlock(225, "pink_shulker_box", (new BlockShulkerBox(EnumDyeColor.PINK)).setHardness(2.0F).setSoundType(SoundType.STONE).setUnlocalizedName("shulkerBoxPink"));
        registerBlock(226, "gray_shulker_box", (new BlockShulkerBox(EnumDyeColor.GRAY)).setHardness(2.0F).setSoundType(SoundType.STONE).setUnlocalizedName("shulkerBoxGray"));
        registerBlock(227, "silver_shulker_box", (new BlockShulkerBox(EnumDyeColor.SILVER)).setHardness(2.0F).setSoundType(SoundType.STONE).setUnlocalizedName("shulkerBoxSilver"));
        registerBlock(228, "cyan_shulker_box", (new BlockShulkerBox(EnumDyeColor.CYAN)).setHardness(2.0F).setSoundType(SoundType.STONE).setUnlocalizedName("shulkerBoxCyan"));
        registerBlock(229, "purple_shulker_box", (new BlockShulkerBox(EnumDyeColor.PURPLE)).setHardness(2.0F).setSoundType(SoundType.STONE).setUnlocalizedName("shulkerBoxPurple"));
        registerBlock(230, "blue_shulker_box", (new BlockShulkerBox(EnumDyeColor.BLUE)).setHardness(2.0F).setSoundType(SoundType.STONE).setUnlocalizedName("shulkerBoxBlue"));
        registerBlock(231, "brown_shulker_box", (new BlockShulkerBox(EnumDyeColor.BROWN)).setHardness(2.0F).setSoundType(SoundType.STONE).setUnlocalizedName("shulkerBoxBrown"));
        registerBlock(232, "green_shulker_box", (new BlockShulkerBox(EnumDyeColor.GREEN)).setHardness(2.0F).setSoundType(SoundType.STONE).setUnlocalizedName("shulkerBoxGreen"));
        registerBlock(233, "red_shulker_box", (new BlockShulkerBox(EnumDyeColor.RED)).setHardness(2.0F).setSoundType(SoundType.STONE).setUnlocalizedName("shulkerBoxRed"));
        registerBlock(234, "black_shulker_box", (new BlockShulkerBox(EnumDyeColor.BLACK)).setHardness(2.0F).setSoundType(SoundType.STONE).setUnlocalizedName("shulkerBoxBlack"));
        registerBlock(235, "white_glazed_terracotta", new BlockGlazedTerracotta(EnumDyeColor.WHITE));
        registerBlock(236, "orange_glazed_terracotta", new BlockGlazedTerracotta(EnumDyeColor.ORANGE));
        registerBlock(237, "magenta_glazed_terracotta", new BlockGlazedTerracotta(EnumDyeColor.MAGENTA));
        registerBlock(238, "light_blue_glazed_terracotta", new BlockGlazedTerracotta(EnumDyeColor.LIGHT_BLUE));
        registerBlock(239, "yellow_glazed_terracotta", new BlockGlazedTerracotta(EnumDyeColor.YELLOW));
        registerBlock(240, "lime_glazed_terracotta", new BlockGlazedTerracotta(EnumDyeColor.LIME));
        registerBlock(241, "pink_glazed_terracotta", new BlockGlazedTerracotta(EnumDyeColor.PINK));
        registerBlock(242, "gray_glazed_terracotta", new BlockGlazedTerracotta(EnumDyeColor.GRAY));
        registerBlock(243, "silver_glazed_terracotta", new BlockGlazedTerracotta(EnumDyeColor.SILVER));
        registerBlock(244, "cyan_glazed_terracotta", new BlockGlazedTerracotta(EnumDyeColor.CYAN));
        registerBlock(245, "purple_glazed_terracotta", new BlockGlazedTerracotta(EnumDyeColor.PURPLE));
        registerBlock(246, "blue_glazed_terracotta", new BlockGlazedTerracotta(EnumDyeColor.BLUE));
        registerBlock(247, "brown_glazed_terracotta", new BlockGlazedTerracotta(EnumDyeColor.BROWN));
        registerBlock(248, "green_glazed_terracotta", new BlockGlazedTerracotta(EnumDyeColor.GREEN));
        registerBlock(249, "red_glazed_terracotta", new BlockGlazedTerracotta(EnumDyeColor.RED));
        registerBlock(250, "black_glazed_terracotta", new BlockGlazedTerracotta(EnumDyeColor.BLACK));
        registerBlock(251, "concrete", (new BlockColored(Material.ROCK)).setHardness(1.8F).setSoundType(SoundType.STONE).setUnlocalizedName("concrete"));
        registerBlock(252, "concrete_powder", (new BlockConcretePowder()).setHardness(0.5F).setSoundType(SoundType.SAND).setUnlocalizedName("concretePowder"));
        registerBlock(255, "structure_block", (new BlockStructure()).setBlockUnbreakable().setResistance(6000000.0F).setUnlocalizedName("structureBlock"));
        Block.REGISTRY.validateKey();
        Iterator iterator = Block.REGISTRY.iterator();

        while (iterator.hasNext()) {
            Block block15 = (Block) iterator.next();

            if (block15.blockMaterial == Material.AIR) {
                block15.useNeighborBrightness = false;
            } else {
                boolean flag = false;
                boolean flag1 = block15 instanceof BlockStairs;
                boolean flag2 = block15 instanceof BlockSlab;
                boolean flag3 = block15 == block6 || block15 == block14;
                boolean flag4 = block15.translucent;
                boolean flag5 = block15.lightOpacity == 0;

                if (flag1 || flag2 || flag3 || flag4 || flag5) {
                    flag = true;
                }

                block15.useNeighborBrightness = flag;
            }
        }

        HashSet hashset = Sets.newHashSet(new Block[] { (Block) Block.REGISTRY.getObject(new ResourceLocation("tripwire"))});
        Iterator iterator1 = Block.REGISTRY.iterator();

        while (iterator1.hasNext()) {
            Block block16 = (Block) iterator1.next();

            if (hashset.contains(block16)) {
                for (int i = 0; i < 15; ++i) {
                    int j = Block.REGISTRY.getIDForObject(block16) << 4 | i; // CraftBukkit - decompile error

                    Block.BLOCK_STATE_IDS.put(block16.getStateFromMeta(i), j);
                }
            } else {
                UnmodifiableIterator unmodifiableiterator = block16.getBlockState().getValidStates().iterator();

                while (unmodifiableiterator.hasNext()) {
                    IBlockState iblockdata = (IBlockState) unmodifiableiterator.next();
                    int k = Block.REGISTRY.getIDForObject(block16) << 4 | block16.getMetaFromState(iblockdata); // CraftBukkit - decompile error

                    Block.BLOCK_STATE_IDS.put(iblockdata, k);
                }
            }
        }

    }

    // CraftBukkit start
    public int getExpDrop(World world, IBlockState data, int enchantmentLevel) {
        return 0;
    }
    // CraftBukkit end

    private static void registerBlock(int i, ResourceLocation minecraftkey, Block block) {
        Block.REGISTRY.register(i, minecraftkey, block);
    }

    private static void registerBlock(int i, String s, Block block) {
        registerBlock(i, new ResourceLocation(s), block);
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
