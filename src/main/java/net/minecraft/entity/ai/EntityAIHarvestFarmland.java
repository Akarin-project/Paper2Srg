package net.minecraft.entity.ai;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;


public class EntityAIHarvestFarmland extends EntityAIMoveToBlock {

    private final EntityVillager villager;
    private boolean hasFarmItem;
    private boolean wantsToReapStuff;
    private int currentTask;

    public EntityAIHarvestFarmland(EntityVillager entityvillager, double d0) {
        super(entityvillager, d0, 16);
        this.villager = entityvillager;
    }

    public boolean shouldExecute() {
        if (this.runDelay <= 0) {
            if (!this.villager.world.getGameRules().getBoolean("mobGriefing")) {
                return false;
            }

            this.currentTask = -1;
            this.hasFarmItem = this.villager.isFarmItemInInventory();
            this.wantsToReapStuff = this.villager.wantsMoreFood();
        }

        return super.shouldExecute();
    }

    public boolean shouldContinueExecuting() {
        return this.currentTask >= 0 && super.shouldContinueExecuting();
    }

    public void updateTask() {
        super.updateTask();
        this.villager.getLookHelper().setLookPosition((double) this.destinationBlock.getX() + 0.5D, (double) (this.destinationBlock.getY() + 1), (double) this.destinationBlock.getZ() + 0.5D, 10.0F, (float) this.villager.getVerticalFaceSpeed());
        if (this.getIsAboveDestination()) {
            World world = this.villager.world;
            BlockPos blockposition = this.destinationBlock.up();
            IBlockState iblockdata = world.getBlockState(blockposition);
            Block block = iblockdata.getBlock();

            if (this.currentTask == 0 && block instanceof BlockCrops && ((BlockCrops) block).isMaxAge(iblockdata)) {
                // CraftBukkit start
                if (!org.bukkit.craftbukkit.event.CraftEventFactory.callEntityChangeBlockEvent(this.villager, blockposition, Blocks.AIR, 0).isCancelled()) {
                    world.destroyBlock(blockposition, true);
                }
                // CraftBukkit end
            } else if (this.currentTask == 1 && iblockdata.getMaterial() == Material.AIR) {
                InventoryBasic inventorysubcontainer = this.villager.getVillagerInventory();

                for (int i = 0; i < inventorysubcontainer.getSizeInventory(); ++i) {
                    ItemStack itemstack = inventorysubcontainer.getStackInSlot(i);
                    boolean flag = false;

                    if (!itemstack.isEmpty()) {
                        // CraftBukkit start
                        Block planted = null;
                        if (itemstack.getItem() == Items.WHEAT_SEEDS) {
                            planted = Blocks.WHEAT;
                            flag = true;
                        } else if (itemstack.getItem() == Items.POTATO) {
                            planted = Blocks.POTATOES;
                            flag = true;
                        } else if (itemstack.getItem() == Items.CARROT) {
                            planted = Blocks.CARROTS;
                            flag = true;
                        } else if (itemstack.getItem() == Items.BEETROOT_SEEDS) {
                            planted = Blocks.BEETROOTS;
                            flag = true;
                        }

                        if (planted != null && !org.bukkit.craftbukkit.event.CraftEventFactory.callEntityChangeBlockEvent(this.villager, blockposition, planted, 0).isCancelled()) {
                            world.setBlockState(blockposition, planted.getDefaultState(), 3);
                        } else {
                            flag = false;
                        }
                        // CraftBukkit end
                    }

                    if (flag) {
                        itemstack.shrink(1);
                        if (itemstack.isEmpty()) {
                            inventorysubcontainer.setInventorySlotContents(i, ItemStack.EMPTY);
                        }
                        break;
                    }
                }
            }

            this.currentTask = -1;
            this.runDelay = 10;
        }

    }

    protected boolean shouldMoveTo(World world, BlockPos blockposition) {
        Block block = world.getBlockState(blockposition).getBlock();

        if (block == Blocks.FARMLAND) {
            blockposition = blockposition.up();
            IBlockState iblockdata = world.getBlockState(blockposition);

            block = iblockdata.getBlock();
            if (block instanceof BlockCrops && ((BlockCrops) block).isMaxAge(iblockdata) && this.wantsToReapStuff && (this.currentTask == 0 || this.currentTask < 0)) {
                this.currentTask = 0;
                return true;
            }

            if (iblockdata.getMaterial() == Material.AIR && this.hasFarmItem && (this.currentTask == 1 || this.currentTask < 0)) {
                this.currentTask = 1;
                return true;
            }
        }

        return false;
    }
}
