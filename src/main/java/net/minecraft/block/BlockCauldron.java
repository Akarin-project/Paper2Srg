package net.minecraft.block;

import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBanner;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionUtils;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.bukkit.event.block.CauldronLevelChangeEvent;

public class BlockCauldron extends Block {

    public static final PropertyInteger LEVEL = PropertyInteger.create("level", 0, 3);
    protected static final AxisAlignedBB AABB_LEGS = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.3125D, 1.0D);
    protected static final AxisAlignedBB AABB_WALL_NORTH = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.125D);
    protected static final AxisAlignedBB AABB_WALL_SOUTH = new AxisAlignedBB(0.0D, 0.0D, 0.875D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB AABB_WALL_EAST = new AxisAlignedBB(0.875D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB AABB_WALL_WEST = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.125D, 1.0D, 1.0D);

    public BlockCauldron() {
        super(Material.IRON, MapColor.STONE);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockCauldron.LEVEL, Integer.valueOf(0)));
    }

    public void addCollisionBoxToList(IBlockState iblockdata, World world, BlockPos blockposition, AxisAlignedBB axisalignedbb, List<AxisAlignedBB> list, @Nullable Entity entity, boolean flag) {
        addCollisionBoxToList(blockposition, axisalignedbb, list, BlockCauldron.AABB_LEGS);
        addCollisionBoxToList(blockposition, axisalignedbb, list, BlockCauldron.AABB_WALL_WEST);
        addCollisionBoxToList(blockposition, axisalignedbb, list, BlockCauldron.AABB_WALL_NORTH);
        addCollisionBoxToList(blockposition, axisalignedbb, list, BlockCauldron.AABB_WALL_EAST);
        addCollisionBoxToList(blockposition, axisalignedbb, list, BlockCauldron.AABB_WALL_SOUTH);
    }

    public AxisAlignedBB getBoundingBox(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        return BlockCauldron.FULL_BLOCK_AABB;
    }

    public boolean isOpaqueCube(IBlockState iblockdata) {
        return false;
    }

    public boolean isFullCube(IBlockState iblockdata) {
        return false;
    }

    public void onEntityCollidedWithBlock(World world, BlockPos blockposition, IBlockState iblockdata, Entity entity) {
        int i = ((Integer) iblockdata.getValue(BlockCauldron.LEVEL)).intValue();
        float f = (float) blockposition.getY() + (6.0F + (float) (3 * i)) / 16.0F;

        if (!world.isRemote && entity.isBurning() && i > 0 && entity.getEntityBoundingBox().minY <= (double) f) {
            // CraftBukkit start
            if (!this.changeLevel(world, blockposition, iblockdata, i - 1, entity, CauldronLevelChangeEvent.ChangeReason.EXTINGUISH)) {
                return;
            }
            entity.extinguish();
            // this.a(world, blockposition, iblockdata, i - 1);
            // CraftBukkit end
        }

    }

    public boolean onBlockActivated(World world, BlockPos blockposition, IBlockState iblockdata, EntityPlayer entityhuman, EnumHand enumhand, EnumFacing enumdirection, float f, float f1, float f2) {
        ItemStack itemstack = entityhuman.getHeldItem(enumhand);

        if (itemstack.isEmpty()) {
            return true;
        } else {
            int i = ((Integer) iblockdata.getValue(BlockCauldron.LEVEL)).intValue();
            Item item = itemstack.getItem();

            if (item == Items.WATER_BUCKET) {
                if (i < 3 && !world.isRemote) {
                    // CraftBukkit start
                    if (!this.changeLevel(world, blockposition, iblockdata, 3, entityhuman, CauldronLevelChangeEvent.ChangeReason.BUCKET_EMPTY)) {
                        return true;
                    }
                    if (!entityhuman.capabilities.isCreativeMode) {
                        entityhuman.setHeldItem(enumhand, new ItemStack(Items.BUCKET));
                    }

                    entityhuman.addStat(StatList.CAULDRON_FILLED);
                    // this.a(world, blockposition, iblockdata, 3);
                    // CraftBukkit end
                    world.playSound((EntityPlayer) null, blockposition, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
                }

                return true;
            } else if (item == Items.BUCKET) {
                if (i == 3 && !world.isRemote) {
                    // CraftBukkit start
                    if (!this.changeLevel(world, blockposition, iblockdata, 0, entityhuman, CauldronLevelChangeEvent.ChangeReason.BUCKET_FILL)) {
                        return true;
                    }
                    if (!entityhuman.capabilities.isCreativeMode) {
                        itemstack.shrink(1);
                        if (itemstack.isEmpty()) {
                            entityhuman.setHeldItem(enumhand, new ItemStack(Items.WATER_BUCKET));
                        } else if (!entityhuman.inventory.addItemStackToInventory(new ItemStack(Items.WATER_BUCKET))) {
                            entityhuman.dropItem(new ItemStack(Items.WATER_BUCKET), false);
                        }
                    }

                    entityhuman.addStat(StatList.CAULDRON_USED);
                    // this.a(world, blockposition, iblockdata, 0);
                    // CraftBukkit end
                    world.playSound((EntityPlayer) null, blockposition, SoundEvents.ITEM_BUCKET_FILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
                }

                return true;
            } else {
                ItemStack itemstack1;

                if (item == Items.GLASS_BOTTLE) {
                    if (i > 0 && !world.isRemote) {
                        // CraftBukkit start
                        if (!this.changeLevel(world, blockposition, iblockdata, i - 1, entityhuman, CauldronLevelChangeEvent.ChangeReason.BOTTLE_FILL)) {
                            return true;
                        }
                        if (!entityhuman.capabilities.isCreativeMode) {
                            itemstack1 = PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.WATER);
                            entityhuman.addStat(StatList.CAULDRON_USED);
                            itemstack.shrink(1);
                            if (itemstack.isEmpty()) {
                                entityhuman.setHeldItem(enumhand, itemstack1);
                            } else if (!entityhuman.inventory.addItemStackToInventory(itemstack1)) {
                                entityhuman.dropItem(itemstack1, false);
                            } else if (entityhuman instanceof EntityPlayerMP) {
                                ((EntityPlayerMP) entityhuman).sendContainerToPlayer(entityhuman.inventoryContainer);
                            }
                        }

                        world.playSound((EntityPlayer) null, blockposition, SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
                        // this.a(world, blockposition, iblockdata, i - 1);
                        // CraftBukkit end
                    }

                    return true;
                } else if (item == Items.POTIONITEM && PotionUtils.getPotionFromItem(itemstack) == PotionTypes.WATER) {
                    if (i < 3 && !world.isRemote) {
                        // CraftBukkit start
                        if (!this.changeLevel(world, blockposition, iblockdata, i + 1, entityhuman, CauldronLevelChangeEvent.ChangeReason.BOTTLE_EMPTY)) {
                            return true;
                        }
                        if (!entityhuman.capabilities.isCreativeMode) {
                            itemstack1 = new ItemStack(Items.GLASS_BOTTLE);
                            entityhuman.addStat(StatList.CAULDRON_USED);
                            entityhuman.setHeldItem(enumhand, itemstack1);
                            if (entityhuman instanceof EntityPlayerMP) {
                                ((EntityPlayerMP) entityhuman).sendContainerToPlayer(entityhuman.inventoryContainer);
                            }
                        }

                        world.playSound((EntityPlayer) null, blockposition, SoundEvents.ITEM_BOTTLE_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
                        // this.a(world, blockposition, iblockdata, i + 1);
                        // CraftBukkit end
                    }

                    return true;
                } else {
                    if (i > 0 && item instanceof ItemArmor) {
                        ItemArmor itemarmor = (ItemArmor) item;

                        if (itemarmor.getArmorMaterial() == ItemArmor.ArmorMaterial.LEATHER && itemarmor.hasColor(itemstack) && !world.isRemote) {
                            // CraftBukkit start
                            if (!this.changeLevel(world, blockposition, iblockdata, i - 1, entityhuman, CauldronLevelChangeEvent.ChangeReason.ARMOR_WASH)) {
                                return true;
                            }
                            itemarmor.removeColor(itemstack);
                            // this.a(world, blockposition, iblockdata, i - 1);
                            // CraftBukkit end
                            entityhuman.addStat(StatList.ARMOR_CLEANED);
                            return true;
                        }
                    }

                    if (i > 0 && item instanceof ItemBanner) {
                        if (TileEntityBanner.getPatterns(itemstack) > 0 && !world.isRemote) {
                            // CraftBukkit start
                            if (!this.changeLevel(world, blockposition, iblockdata, i - 1, entityhuman, CauldronLevelChangeEvent.ChangeReason.BANNER_WASH)) {
                                return true;
                            }
                            itemstack1 = itemstack.copy();
                            itemstack1.setCount(1);
                            TileEntityBanner.removeBannerData(itemstack1);
                            entityhuman.addStat(StatList.BANNER_CLEANED);
                            if (!entityhuman.capabilities.isCreativeMode) {
                                itemstack.shrink(1);
                                // this.a(world, blockposition, iblockdata, i - 1);
                                // CraftBukkit end
                            }

                            if (itemstack.isEmpty()) {
                                entityhuman.setHeldItem(enumhand, itemstack1);
                            } else if (!entityhuman.inventory.addItemStackToInventory(itemstack1)) {
                                entityhuman.dropItem(itemstack1, false);
                            } else if (entityhuman instanceof EntityPlayerMP) {
                                ((EntityPlayerMP) entityhuman).sendContainerToPlayer(entityhuman.inventoryContainer);
                            }
                        }

                        return true;
                    } else {
                        return false;
                    }
                }
            }
        }
    }

    // CraftBukkit start
    public void setWaterLevel(World world, BlockPos blockposition, IBlockState iblockdata, int i) {
        this.changeLevel(world, blockposition, iblockdata, i, null, CauldronLevelChangeEvent.ChangeReason.UNKNOWN);
    }

    private boolean changeLevel(World world, BlockPos blockposition, IBlockState iblockdata, int i, Entity entity, CauldronLevelChangeEvent.ChangeReason reason) {
        int newLevel = Integer.valueOf(MathHelper.clamp(i, 0, 3));
        CauldronLevelChangeEvent event = new CauldronLevelChangeEvent(
                world.getWorld().getBlockAt(blockposition.getX(), blockposition.getY(), blockposition.getZ()),
                (entity == null) ? null : entity.getBukkitEntity(), reason, iblockdata.getValue(BlockCauldron.LEVEL), newLevel
        );
        world.getServer().getPluginManager().callEvent(event);
        if (event.isCancelled()) {
            return false;
        }
        world.setBlockState(blockposition, iblockdata.withProperty(BlockCauldron.LEVEL, event.getNewLevel()), 2);
        world.updateComparatorOutputLevel(blockposition, this);
        return true;
        // CraftBukkit end
    }

    public void fillWithRain(World world, BlockPos blockposition) {
        if (world.rand.nextInt(20) == 1) {
            float f = world.getBiome(blockposition).getTemperature(blockposition);

            if (world.getBiomeProvider().getTemperatureAtHeight(f, blockposition.getY()) >= 0.15F) {
                IBlockState iblockdata = world.getBlockState(blockposition);

                if (((Integer) iblockdata.getValue(BlockCauldron.LEVEL)).intValue() < 3) {
                    this.setWaterLevel(world, blockposition, iblockdata.cycleProperty((IProperty) BlockCauldron.LEVEL), 2); // CraftBukkit
                }

            }
        }
    }

    public Item getItemDropped(IBlockState iblockdata, Random random, int i) {
        return Items.CAULDRON;
    }

    public ItemStack getItem(World world, BlockPos blockposition, IBlockState iblockdata) {
        return new ItemStack(Items.CAULDRON);
    }

    public boolean hasComparatorInputOverride(IBlockState iblockdata) {
        return true;
    }

    public int getComparatorInputOverride(IBlockState iblockdata, World world, BlockPos blockposition) {
        return ((Integer) iblockdata.getValue(BlockCauldron.LEVEL)).intValue();
    }

    public IBlockState getStateFromMeta(int i) {
        return this.getDefaultState().withProperty(BlockCauldron.LEVEL, Integer.valueOf(i));
    }

    public int getMetaFromState(IBlockState iblockdata) {
        return ((Integer) iblockdata.getValue(BlockCauldron.LEVEL)).intValue();
    }

    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] { BlockCauldron.LEVEL});
    }

    public boolean isPassable(IBlockAccess iblockaccess, BlockPos blockposition) {
        return true;
    }

    public BlockFaceShape getBlockFaceShape(IBlockAccess iblockaccess, IBlockState iblockdata, BlockPos blockposition, EnumFacing enumdirection) {
        return enumdirection == EnumFacing.UP ? BlockFaceShape.BOWL : (enumdirection == EnumFacing.DOWN ? BlockFaceShape.UNDEFINED : BlockFaceShape.SOLID);
    }
}
