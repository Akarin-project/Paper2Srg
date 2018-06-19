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

    public static final PropertyInteger field_176591_a = PropertyInteger.func_177719_a("level", 0, 3);
    protected static final AxisAlignedBB field_185596_b = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.3125D, 1.0D);
    protected static final AxisAlignedBB field_185597_c = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.125D);
    protected static final AxisAlignedBB field_185598_d = new AxisAlignedBB(0.0D, 0.0D, 0.875D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB field_185599_e = new AxisAlignedBB(0.875D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB field_185600_f = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.125D, 1.0D, 1.0D);

    public BlockCauldron() {
        super(Material.field_151573_f, MapColor.field_151665_m);
        this.func_180632_j(this.field_176227_L.func_177621_b().func_177226_a(BlockCauldron.field_176591_a, Integer.valueOf(0)));
    }

    public void func_185477_a(IBlockState iblockdata, World world, BlockPos blockposition, AxisAlignedBB axisalignedbb, List<AxisAlignedBB> list, @Nullable Entity entity, boolean flag) {
        func_185492_a(blockposition, axisalignedbb, list, BlockCauldron.field_185596_b);
        func_185492_a(blockposition, axisalignedbb, list, BlockCauldron.field_185600_f);
        func_185492_a(blockposition, axisalignedbb, list, BlockCauldron.field_185597_c);
        func_185492_a(blockposition, axisalignedbb, list, BlockCauldron.field_185599_e);
        func_185492_a(blockposition, axisalignedbb, list, BlockCauldron.field_185598_d);
    }

    public AxisAlignedBB func_185496_a(IBlockState iblockdata, IBlockAccess iblockaccess, BlockPos blockposition) {
        return BlockCauldron.field_185505_j;
    }

    public boolean func_149662_c(IBlockState iblockdata) {
        return false;
    }

    public boolean func_149686_d(IBlockState iblockdata) {
        return false;
    }

    public void func_180634_a(World world, BlockPos blockposition, IBlockState iblockdata, Entity entity) {
        int i = ((Integer) iblockdata.func_177229_b(BlockCauldron.field_176591_a)).intValue();
        float f = (float) blockposition.func_177956_o() + (6.0F + (float) (3 * i)) / 16.0F;

        if (!world.field_72995_K && entity.func_70027_ad() && i > 0 && entity.func_174813_aQ().field_72338_b <= (double) f) {
            // CraftBukkit start
            if (!this.changeLevel(world, blockposition, iblockdata, i - 1, entity, CauldronLevelChangeEvent.ChangeReason.EXTINGUISH)) {
                return;
            }
            entity.func_70066_B();
            // this.a(world, blockposition, iblockdata, i - 1);
            // CraftBukkit end
        }

    }

    public boolean func_180639_a(World world, BlockPos blockposition, IBlockState iblockdata, EntityPlayer entityhuman, EnumHand enumhand, EnumFacing enumdirection, float f, float f1, float f2) {
        ItemStack itemstack = entityhuman.func_184586_b(enumhand);

        if (itemstack.func_190926_b()) {
            return true;
        } else {
            int i = ((Integer) iblockdata.func_177229_b(BlockCauldron.field_176591_a)).intValue();
            Item item = itemstack.func_77973_b();

            if (item == Items.field_151131_as) {
                if (i < 3 && !world.field_72995_K) {
                    // CraftBukkit start
                    if (!this.changeLevel(world, blockposition, iblockdata, 3, entityhuman, CauldronLevelChangeEvent.ChangeReason.BUCKET_EMPTY)) {
                        return true;
                    }
                    if (!entityhuman.field_71075_bZ.field_75098_d) {
                        entityhuman.func_184611_a(enumhand, new ItemStack(Items.field_151133_ar));
                    }

                    entityhuman.func_71029_a(StatList.field_188077_K);
                    // this.a(world, blockposition, iblockdata, 3);
                    // CraftBukkit end
                    world.func_184133_a((EntityPlayer) null, blockposition, SoundEvents.field_187624_K, SoundCategory.BLOCKS, 1.0F, 1.0F);
                }

                return true;
            } else if (item == Items.field_151133_ar) {
                if (i == 3 && !world.field_72995_K) {
                    // CraftBukkit start
                    if (!this.changeLevel(world, blockposition, iblockdata, 0, entityhuman, CauldronLevelChangeEvent.ChangeReason.BUCKET_FILL)) {
                        return true;
                    }
                    if (!entityhuman.field_71075_bZ.field_75098_d) {
                        itemstack.func_190918_g(1);
                        if (itemstack.func_190926_b()) {
                            entityhuman.func_184611_a(enumhand, new ItemStack(Items.field_151131_as));
                        } else if (!entityhuman.field_71071_by.func_70441_a(new ItemStack(Items.field_151131_as))) {
                            entityhuman.func_71019_a(new ItemStack(Items.field_151131_as), false);
                        }
                    }

                    entityhuman.func_71029_a(StatList.field_188078_L);
                    // this.a(world, blockposition, iblockdata, 0);
                    // CraftBukkit end
                    world.func_184133_a((EntityPlayer) null, blockposition, SoundEvents.field_187630_M, SoundCategory.BLOCKS, 1.0F, 1.0F);
                }

                return true;
            } else {
                ItemStack itemstack1;

                if (item == Items.field_151069_bo) {
                    if (i > 0 && !world.field_72995_K) {
                        // CraftBukkit start
                        if (!this.changeLevel(world, blockposition, iblockdata, i - 1, entityhuman, CauldronLevelChangeEvent.ChangeReason.BOTTLE_FILL)) {
                            return true;
                        }
                        if (!entityhuman.field_71075_bZ.field_75098_d) {
                            itemstack1 = PotionUtils.func_185188_a(new ItemStack(Items.field_151068_bn), PotionTypes.field_185230_b);
                            entityhuman.func_71029_a(StatList.field_188078_L);
                            itemstack.func_190918_g(1);
                            if (itemstack.func_190926_b()) {
                                entityhuman.func_184611_a(enumhand, itemstack1);
                            } else if (!entityhuman.field_71071_by.func_70441_a(itemstack1)) {
                                entityhuman.func_71019_a(itemstack1, false);
                            } else if (entityhuman instanceof EntityPlayerMP) {
                                ((EntityPlayerMP) entityhuman).func_71120_a(entityhuman.field_71069_bz);
                            }
                        }

                        world.func_184133_a((EntityPlayer) null, blockposition, SoundEvents.field_187615_H, SoundCategory.BLOCKS, 1.0F, 1.0F);
                        // this.a(world, blockposition, iblockdata, i - 1);
                        // CraftBukkit end
                    }

                    return true;
                } else if (item == Items.field_151068_bn && PotionUtils.func_185191_c(itemstack) == PotionTypes.field_185230_b) {
                    if (i < 3 && !world.field_72995_K) {
                        // CraftBukkit start
                        if (!this.changeLevel(world, blockposition, iblockdata, i + 1, entityhuman, CauldronLevelChangeEvent.ChangeReason.BOTTLE_EMPTY)) {
                            return true;
                        }
                        if (!entityhuman.field_71075_bZ.field_75098_d) {
                            itemstack1 = new ItemStack(Items.field_151069_bo);
                            entityhuman.func_71029_a(StatList.field_188078_L);
                            entityhuman.func_184611_a(enumhand, itemstack1);
                            if (entityhuman instanceof EntityPlayerMP) {
                                ((EntityPlayerMP) entityhuman).func_71120_a(entityhuman.field_71069_bz);
                            }
                        }

                        world.func_184133_a((EntityPlayer) null, blockposition, SoundEvents.field_191241_J, SoundCategory.BLOCKS, 1.0F, 1.0F);
                        // this.a(world, blockposition, iblockdata, i + 1);
                        // CraftBukkit end
                    }

                    return true;
                } else {
                    if (i > 0 && item instanceof ItemArmor) {
                        ItemArmor itemarmor = (ItemArmor) item;

                        if (itemarmor.func_82812_d() == ItemArmor.ArmorMaterial.LEATHER && itemarmor.func_82816_b_(itemstack) && !world.field_72995_K) {
                            // CraftBukkit start
                            if (!this.changeLevel(world, blockposition, iblockdata, i - 1, entityhuman, CauldronLevelChangeEvent.ChangeReason.ARMOR_WASH)) {
                                return true;
                            }
                            itemarmor.func_82815_c(itemstack);
                            // this.a(world, blockposition, iblockdata, i - 1);
                            // CraftBukkit end
                            entityhuman.func_71029_a(StatList.field_188079_M);
                            return true;
                        }
                    }

                    if (i > 0 && item instanceof ItemBanner) {
                        if (TileEntityBanner.func_175113_c(itemstack) > 0 && !world.field_72995_K) {
                            // CraftBukkit start
                            if (!this.changeLevel(world, blockposition, iblockdata, i - 1, entityhuman, CauldronLevelChangeEvent.ChangeReason.BANNER_WASH)) {
                                return true;
                            }
                            itemstack1 = itemstack.func_77946_l();
                            itemstack1.func_190920_e(1);
                            TileEntityBanner.func_175117_e(itemstack1);
                            entityhuman.func_71029_a(StatList.field_188080_N);
                            if (!entityhuman.field_71075_bZ.field_75098_d) {
                                itemstack.func_190918_g(1);
                                // this.a(world, blockposition, iblockdata, i - 1);
                                // CraftBukkit end
                            }

                            if (itemstack.func_190926_b()) {
                                entityhuman.func_184611_a(enumhand, itemstack1);
                            } else if (!entityhuman.field_71071_by.func_70441_a(itemstack1)) {
                                entityhuman.func_71019_a(itemstack1, false);
                            } else if (entityhuman instanceof EntityPlayerMP) {
                                ((EntityPlayerMP) entityhuman).func_71120_a(entityhuman.field_71069_bz);
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
    public void func_176590_a(World world, BlockPos blockposition, IBlockState iblockdata, int i) {
        this.changeLevel(world, blockposition, iblockdata, i, null, CauldronLevelChangeEvent.ChangeReason.UNKNOWN);
    }

    private boolean changeLevel(World world, BlockPos blockposition, IBlockState iblockdata, int i, Entity entity, CauldronLevelChangeEvent.ChangeReason reason) {
        int newLevel = Integer.valueOf(MathHelper.func_76125_a(i, 0, 3));
        CauldronLevelChangeEvent event = new CauldronLevelChangeEvent(
                world.getWorld().getBlockAt(blockposition.func_177958_n(), blockposition.func_177956_o(), blockposition.func_177952_p()),
                (entity == null) ? null : entity.getBukkitEntity(), reason, iblockdata.func_177229_b(BlockCauldron.field_176591_a), newLevel
        );
        world.getServer().getPluginManager().callEvent(event);
        if (event.isCancelled()) {
            return false;
        }
        world.func_180501_a(blockposition, iblockdata.func_177226_a(BlockCauldron.field_176591_a, event.getNewLevel()), 2);
        world.func_175666_e(blockposition, this);
        return true;
        // CraftBukkit end
    }

    public void func_176224_k(World world, BlockPos blockposition) {
        if (world.field_73012_v.nextInt(20) == 1) {
            float f = world.func_180494_b(blockposition).func_180626_a(blockposition);

            if (world.func_72959_q().func_76939_a(f, blockposition.func_177956_o()) >= 0.15F) {
                IBlockState iblockdata = world.func_180495_p(blockposition);

                if (((Integer) iblockdata.func_177229_b(BlockCauldron.field_176591_a)).intValue() < 3) {
                    this.func_176590_a(world, blockposition, iblockdata.func_177231_a((IProperty) BlockCauldron.field_176591_a), 2); // CraftBukkit
                }

            }
        }
    }

    public Item func_180660_a(IBlockState iblockdata, Random random, int i) {
        return Items.field_151066_bu;
    }

    public ItemStack func_185473_a(World world, BlockPos blockposition, IBlockState iblockdata) {
        return new ItemStack(Items.field_151066_bu);
    }

    public boolean func_149740_M(IBlockState iblockdata) {
        return true;
    }

    public int func_180641_l(IBlockState iblockdata, World world, BlockPos blockposition) {
        return ((Integer) iblockdata.func_177229_b(BlockCauldron.field_176591_a)).intValue();
    }

    public IBlockState func_176203_a(int i) {
        return this.func_176223_P().func_177226_a(BlockCauldron.field_176591_a, Integer.valueOf(i));
    }

    public int func_176201_c(IBlockState iblockdata) {
        return ((Integer) iblockdata.func_177229_b(BlockCauldron.field_176591_a)).intValue();
    }

    protected BlockStateContainer func_180661_e() {
        return new BlockStateContainer(this, new IProperty[] { BlockCauldron.field_176591_a});
    }

    public boolean func_176205_b(IBlockAccess iblockaccess, BlockPos blockposition) {
        return true;
    }

    public BlockFaceShape func_193383_a(IBlockAccess iblockaccess, IBlockState iblockdata, BlockPos blockposition, EnumFacing enumdirection) {
        return enumdirection == EnumFacing.UP ? BlockFaceShape.BOWL : (enumdirection == EnumFacing.DOWN ? BlockFaceShape.UNDEFINED : BlockFaceShape.SOLID);
    }
}
