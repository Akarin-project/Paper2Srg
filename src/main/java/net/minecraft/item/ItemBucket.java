package net.minecraft.item;

import javax.annotation.Nullable;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;

// CraftBukkit start
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
// CraftBukkit end

public class ItemBucket extends Item {

    private final Block field_77876_a;

    public ItemBucket(Block block) {
        this.field_77777_bU = 1;
        this.field_77876_a = block;
        this.func_77637_a(CreativeTabs.field_78026_f);
    }

    public ActionResult<ItemStack> func_77659_a(World world, EntityPlayer entityhuman, EnumHand enumhand) {
        boolean flag = this.field_77876_a == Blocks.field_150350_a;
        ItemStack itemstack = entityhuman.func_184586_b(enumhand);
        RayTraceResult movingobjectposition = this.func_77621_a(world, entityhuman, flag);

        if (movingobjectposition == null) {
            return new ActionResult(EnumActionResult.PASS, itemstack);
        } else if (movingobjectposition.field_72313_a != RayTraceResult.Type.BLOCK) {
            return new ActionResult(EnumActionResult.PASS, itemstack);
        } else {
            BlockPos blockposition = movingobjectposition.func_178782_a();

            if (!world.func_175660_a(entityhuman, blockposition)) {
                return new ActionResult(EnumActionResult.FAIL, itemstack);
            } else if (flag) {
                if (!entityhuman.func_175151_a(blockposition.func_177972_a(movingobjectposition.field_178784_b), movingobjectposition.field_178784_b, itemstack)) {
                    return new ActionResult(EnumActionResult.FAIL, itemstack);
                } else {
                    IBlockState iblockdata = world.func_180495_p(blockposition);
                    Material material = iblockdata.func_185904_a();

                    if (material == Material.field_151586_h && ((Integer) iblockdata.func_177229_b(BlockLiquid.field_176367_b)).intValue() == 0) {
                        // CraftBukkit start
                        PlayerBucketFillEvent event = CraftEventFactory.callPlayerBucketFillEvent(entityhuman, blockposition.func_177958_n(), blockposition.func_177956_o(), blockposition.func_177952_p(), null, itemstack, Items.field_151131_as);
 
                        if (event.isCancelled()) {
                            return new ActionResult(EnumActionResult.FAIL, itemstack);
                        }
                        // CraftBukkit end
                        world.func_180501_a(blockposition, Blocks.field_150350_a.func_176223_P(), 11);
                        entityhuman.func_71029_a(StatList.func_188057_b((Item) this));
                        entityhuman.func_184185_a(SoundEvents.field_187630_M, 1.0F, 1.0F);
                        return new ActionResult(EnumActionResult.SUCCESS, this.a(itemstack, entityhuman, Items.field_151131_as, event.getItemStack())); // CraftBukkit
                    } else if (material == Material.field_151587_i && ((Integer) iblockdata.func_177229_b(BlockLiquid.field_176367_b)).intValue() == 0) {
                        // CraftBukkit start
                        PlayerBucketFillEvent event = CraftEventFactory.callPlayerBucketFillEvent(entityhuman, blockposition.func_177958_n(), blockposition.func_177956_o(), blockposition.func_177952_p(), null, itemstack, Items.field_151129_at);

                        if (event.isCancelled()) {
                            return new ActionResult(EnumActionResult.FAIL, itemstack);
                        }
                        // CraftBukkit end
                        entityhuman.func_184185_a(SoundEvents.field_187633_N, 1.0F, 1.0F);
                        world.func_180501_a(blockposition, Blocks.field_150350_a.func_176223_P(), 11);
                        entityhuman.func_71029_a(StatList.func_188057_b((Item) this));
                        return new ActionResult(EnumActionResult.SUCCESS, this.a(itemstack, entityhuman, Items.field_151129_at, event.getItemStack())); // CraftBukkit
                    } else {
                        return new ActionResult(EnumActionResult.FAIL, itemstack);
                    }
                }
            } else {
                boolean flag1 = world.func_180495_p(blockposition).func_177230_c().func_176200_f((IBlockAccess) world, blockposition);
                BlockPos blockposition1 = flag1 && movingobjectposition.field_178784_b == EnumFacing.UP ? blockposition : blockposition.func_177972_a(movingobjectposition.field_178784_b);

                if (!entityhuman.func_175151_a(blockposition1, movingobjectposition.field_178784_b, itemstack)) {
                    return new ActionResult(EnumActionResult.FAIL, itemstack);
                } else if (this.a(entityhuman, world, blockposition1, movingobjectposition.field_178784_b, blockposition, itemstack)) { // CraftBukkit
                    if (entityhuman instanceof EntityPlayerMP) {
                        CriteriaTriggers.field_193137_x.func_193173_a((EntityPlayerMP) entityhuman, blockposition1, itemstack);
                    }

                    entityhuman.func_71029_a(StatList.func_188057_b((Item) this));
                    return !entityhuman.field_71075_bZ.field_75098_d ? new ActionResult(EnumActionResult.SUCCESS, new ItemStack(Items.field_151133_ar)) : new ActionResult(EnumActionResult.SUCCESS, itemstack);
                } else {
                    return new ActionResult(EnumActionResult.FAIL, itemstack);
                }
            }
        }
    }

    // CraftBukkit - added ob.ItemStack result - TODO: Is this... the right way to handle this?
    private ItemStack a(ItemStack itemstack, EntityPlayer entityhuman, Item item, org.bukkit.inventory.ItemStack result) {
        if (entityhuman.field_71075_bZ.field_75098_d) {
            return itemstack;
        } else {
            itemstack.func_190918_g(1);
            if (itemstack.func_190926_b()) {
                // CraftBukkit start
                return CraftItemStack.asNMSCopy(result);
            } else {
                if (!entityhuman.field_71071_by.func_70441_a(CraftItemStack.asNMSCopy(result))) {
                    entityhuman.func_71019_a(CraftItemStack.asNMSCopy(result), false);
                    // CraftBukkit end
                }

                return itemstack;
            }
        }
    }

    // CraftBukkit start
    public boolean func_180616_a(@Nullable EntityPlayer entityhuman, World world, BlockPos blockposition) {
        return a(entityhuman, world, blockposition, null, blockposition, null);
    }

    public boolean a(EntityPlayer entityhuman, World world, BlockPos blockposition, EnumFacing enumdirection, BlockPos clicked, ItemStack itemstack) {
        // CraftBukkit end
        if (this.field_77876_a == Blocks.field_150350_a) {
            return false;
        } else {
            IBlockState iblockdata = world.func_180495_p(blockposition);
            Material material = iblockdata.func_185904_a();
            boolean flag = !material.func_76220_a();
            boolean flag1 = iblockdata.func_177230_c().func_176200_f((IBlockAccess) world, blockposition);

            if (!world.func_175623_d(blockposition) && !flag && !flag1) {
                return false;
            } else {
                // CraftBukkit start
                if (entityhuman != null) {
                    PlayerBucketEmptyEvent event = CraftEventFactory.callPlayerBucketEmptyEvent(entityhuman, clicked.func_177958_n(), clicked.func_177956_o(), clicked.func_177952_p(), enumdirection, itemstack);
                    if (event.isCancelled()) {
                        // TODO: inventory not updated
                        return false;
                    }
                }
                // CraftBukkit end
                if (world.field_73011_w.func_177500_n() && this.field_77876_a == Blocks.field_150358_i) {
                    int i = blockposition.func_177958_n();
                    int j = blockposition.func_177956_o();
                    int k = blockposition.func_177952_p();

                    world.func_184133_a(entityhuman, blockposition, SoundEvents.field_187646_bt, SoundCategory.BLOCKS, 0.5F, 2.6F + (world.field_73012_v.nextFloat() - world.field_73012_v.nextFloat()) * 0.8F);

                    for (int l = 0; l < 8; ++l) {
                        world.func_175688_a(EnumParticleTypes.SMOKE_LARGE, (double) i + Math.random(), (double) j + Math.random(), (double) k + Math.random(), 0.0D, 0.0D, 0.0D, new int[0]);
                    }
                } else {
                    if (!world.field_72995_K && (flag || flag1) && !material.func_76224_d()) {
                        world.func_175655_b(blockposition, true);
                    }

                    SoundEvent soundeffect = this.field_77876_a == Blocks.field_150356_k ? SoundEvents.field_187627_L : SoundEvents.field_187624_K;

                    world.func_184133_a(entityhuman, blockposition, soundeffect, SoundCategory.BLOCKS, 1.0F, 1.0F);
                    world.func_180501_a(blockposition, this.field_77876_a.func_176223_P(), 11);
                }

                return true;
            }
        }
    }
}
