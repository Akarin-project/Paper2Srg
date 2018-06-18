package net.minecraft.tileentity;

import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.IDataWalker;
import net.minecraft.util.datafix.walkers.ItemStackDataLists;
import net.minecraft.util.math.BlockPos;
import org.bukkit.craftbukkit.entity.CraftHumanEntity;
import org.bukkit.entity.HumanEntity;

// CraftBukkit start
import org.bukkit.craftbukkit.entity.CraftHumanEntity;
import org.bukkit.entity.HumanEntity;
// CraftBukkit end

public class TileEntityChest extends TileEntityLockableLoot { // Paper - Remove ITickable

    private NonNullList<ItemStack> chestContents;
    public boolean adjacentChestChecked;
    public TileEntityChest adjacentChestZNeg; // Paper - Adjacent Chest Z Neg
    public TileEntityChest adjacentChestXPos; // Paper - Adjacent Chest X Pos
    public TileEntityChest adjacentChestXNeg; // Paper - Adjacent Chest X Neg
    public TileEntityChest adjacentChestZPos; // Paper - Adjacent Chest Z Pos
    public float lidAngle; // Paper - lid angle
    public float prevLidAngle;
    public int numPlayersUsing; // Paper - Number of viewers
    private int ticksSinceSync;
    private BlockChest.Type cachedChestType;

    // CraftBukkit start - add fields and methods
    public List<HumanEntity> transaction = new java.util.ArrayList<HumanEntity>();
    private int maxStack = MAX_STACK;

    public List<ItemStack> getContents() {
        return this.chestContents;
    }

    public void onOpen(CraftHumanEntity who) {
        transaction.add(who);
    }

    public void onClose(CraftHumanEntity who) {
        transaction.remove(who);
    }

    public List<HumanEntity> getViewers() {
        return transaction;
    }

    public void setMaxStackSize(int size) {
        maxStack = size;
    }
    // CraftBukkit end

    public TileEntityChest() {
        this.chestContents = NonNullList.withSize(27, ItemStack.EMPTY);
    }

    public TileEntityChest(BlockChest.Type blockchest_type) {
        this.chestContents = NonNullList.withSize(27, ItemStack.EMPTY);
        this.cachedChestType = blockchest_type;
    }

    public int getSizeInventory() {
        return 27;
    }

    public boolean isEmpty() {
        Iterator iterator = this.chestContents.iterator();

        ItemStack itemstack;

        do {
            if (!iterator.hasNext()) {
                return true;
            }

            itemstack = (ItemStack) iterator.next();
        } while (itemstack.isEmpty());

        return false;
    }

    public String getName() {
        return this.hasCustomName() ? this.customName : "container.chest";
    }

    public static void registerFixesChest(DataFixer dataconvertermanager) {
        dataconvertermanager.registerWalker(FixTypes.BLOCK_ENTITY, (IDataWalker) (new ItemStackDataLists(TileEntityChest.class, new String[] { "Items"})));
    }

    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.chestContents = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);
        if (!this.checkLootAndRead(nbttagcompound)) {
            ItemStackHelper.loadAllItems(nbttagcompound, this.chestContents);
        }

        if (nbttagcompound.hasKey("CustomName", 8)) {
            this.customName = nbttagcompound.getString("CustomName");
        }

    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        if (!this.checkLootAndWrite(nbttagcompound)) {
            ItemStackHelper.saveAllItems(nbttagcompound, this.chestContents);
        }

        if (this.hasCustomName()) {
            nbttagcompound.setString("CustomName", this.customName);
        }

        return nbttagcompound;
    }

    public int getInventoryStackLimit() {
        return maxStack; // CraftBukkit
    }

    public void updateContainingBlockInfo() {
        super.updateContainingBlockInfo();
        this.adjacentChestChecked = false;
    }

    private void setNeighbor(TileEntityChest tileentitychest, EnumFacing enumdirection) {
        if (tileentitychest.isInvalid()) {
            this.adjacentChestChecked = false;
        } else if (this.adjacentChestChecked) {
            switch (enumdirection) {
            case NORTH:
                if (this.adjacentChestZNeg != tileentitychest) {
                    this.adjacentChestChecked = false;
                }
                break;

            case SOUTH:
                if (this.adjacentChestZPos != tileentitychest) {
                    this.adjacentChestChecked = false;
                }
                break;

            case EAST:
                if (this.adjacentChestXPos != tileentitychest) {
                    this.adjacentChestChecked = false;
                }
                break;

            case WEST:
                if (this.adjacentChestXNeg != tileentitychest) {
                    this.adjacentChestChecked = false;
                }
            }
        }

    }

    public void checkForAdjacentChests() {
        if (!this.adjacentChestChecked) {
            this.adjacentChestChecked = true;
            this.adjacentChestXNeg = this.getAdjacentChest(EnumFacing.WEST);
            this.adjacentChestXPos = this.getAdjacentChest(EnumFacing.EAST);
            this.adjacentChestZNeg = this.getAdjacentChest(EnumFacing.NORTH);
            this.adjacentChestZPos = this.getAdjacentChest(EnumFacing.SOUTH);
        }
    }

    @Nullable
    protected TileEntityChest getAdjacentChest(EnumFacing enumdirection) {
        BlockPos blockposition = this.pos.offset(enumdirection);

        if (this.isChestAt(blockposition)) {
            TileEntity tileentity = this.world.getTileEntity(blockposition);

            if (tileentity instanceof TileEntityChest) {
                TileEntityChest tileentitychest = (TileEntityChest) tileentity;

                tileentitychest.setNeighbor(this, enumdirection.getOpposite());
                return tileentitychest;
            }
        }

        return null;
    }

    private boolean isChestAt(BlockPos blockposition) {
        if (this.world == null) {
            return false;
        } else {
            Block block = this.world.getBlockState(blockposition).getBlock();

            return block instanceof BlockChest && ((BlockChest) block).chestType == this.getChestType();
        }
    }

    public void update() {
        // Paper - Disable all of this, just in case this does get ticked
        /*
        this.o();
        int i = this.position.getX();
        int j = this.position.getY();
        int k = this.position.getZ();

        ++this.q;
        float f;

        if (!this.world.isClientSide && this.l != 0 && (this.q + i + j + k) % 200 == 0) {
            this.l = 0;
            f = 5.0F;
            List list = this.world.a(EntityHuman.class, new AxisAlignedBB((double) ((float) i - 5.0F), (double) ((float) j - 5.0F), (double) ((float) k - 5.0F), (double) ((float) (i + 1) + 5.0F), (double) ((float) (j + 1) + 5.0F), (double) ((float) (k + 1) + 5.0F)));
            Iterator iterator = list.iterator();

            while (iterator.hasNext()) {
                EntityHuman entityhuman = (EntityHuman) iterator.next();

                if (entityhuman.activeContainer instanceof ContainerChest) {
                    IInventory iinventory = ((ContainerChest) entityhuman.activeContainer).e();

                    if (iinventory == this || iinventory instanceof InventoryLargeChest && ((InventoryLargeChest) iinventory).a((IInventory) this)) {
                        ++this.l;
                    }
                }
            }
        }

        this.k = this.j;
        f = 0.1F;
        double d0;

        if (this.l > 0 && this.j == 0.0F && this.f == null && this.h == null) {
            double d1 = (double) i + 0.5D;

            d0 = (double) k + 0.5D;
            if (this.i != null) {
                d0 += 0.5D;
            }

            if (this.g != null) {
                d1 += 0.5D;
            }

            this.world.a((EntityHuman) null, d1, (double) j + 0.5D, d0, SoundEffects.ac, SoundCategory.BLOCKS, 0.5F, this.world.random.nextFloat() * 0.1F + 0.9F);
        }

        if (this.l == 0 && this.j > 0.0F || this.l > 0 && this.j < 1.0F) {
            float f1 = this.j;

            if (this.l > 0) {
                this.j += 0.1F;
            } else {
                this.j -= 0.1F;
            }

            if (this.j > 1.0F) {
                this.j = 1.0F;
            }

            float f2 = 0.5F;

            if (this.j < 0.5F && f1 >= 0.5F && this.f == null && this.h == null) {
                d0 = (double) i + 0.5D;
                double d2 = (double) k + 0.5D;

                if (this.i != null) {
                    d2 += 0.5D;
                }

                if (this.g != null) {
                    d0 += 0.5D;
                }

                this.world.a((EntityHuman) null, d0, (double) j + 0.5D, d2, SoundEffects.aa, SoundCategory.BLOCKS, 0.5F, this.world.random.nextFloat() * 0.1F + 0.9F);
            }

            if (this.j < 0.0F) {
                this.j = 0.0F;
            }
        }
        */
        // Paper end
    }

    public boolean receiveClientEvent(int i, int j) {
        if (i == 1) {
            this.numPlayersUsing = j;
            return true;
        } else {
            return super.receiveClientEvent(i, j);
        }
    }

    public void openInventory(EntityPlayer entityhuman) {
        if (!entityhuman.isSpectator()) {
            if (this.numPlayersUsing < 0) {
                this.numPlayersUsing = 0;
            }
            int oldPower = Math.max(0, Math.min(15, this.numPlayersUsing)); // CraftBukkit - Get power before new viewer is added

            ++this.numPlayersUsing;

            // Paper start - Move chest open sound out of the tick loop
            this.checkForAdjacentChests();

            if (this.numPlayersUsing > 0 && this.lidAngle == 0.0F && this.adjacentChestZNeg == null && this.adjacentChestXNeg == null) {
                this.lidAngle = 0.7F;

                double d0 = (double) this.pos.getZ() + 0.5D;
                double d1 = (double) this.pos.getX() + 0.5D;

                if (this.adjacentChestZPos != null) {
                    d0 += 0.5D;
                }

                if (this.adjacentChestXPos != null) {
                    d1 += 0.5D;
                }

                this.world.playSound((EntityPlayer) null, d1, (double) this.pos.getY() + 0.5D, d0, SoundEvents.BLOCK_CHEST_OPEN, SoundCategory.BLOCKS, 0.5F, this.world.rand.nextFloat() * 0.1F + 0.9F);
            }
            // Paper end

            if (this.world == null) return; // CraftBukkit
            this.world.addBlockEvent(this.pos, this.getBlockType(), 1, this.numPlayersUsing);

            // CraftBukkit start - Call redstone event
            if (this.getBlockType() == Blocks.TRAPPED_CHEST) {
                int newPower = Math.max(0, Math.min(15, this.numPlayersUsing));

                if (oldPower != newPower) {
                    org.bukkit.craftbukkit.event.CraftEventFactory.callRedstoneChange(world, pos.getX(), pos.getY(), pos.getZ(), oldPower, newPower);
                }
            }
            // CraftBukkit end
            this.world.notifyNeighborsOfStateChange(this.pos, this.getBlockType(), false);
            if (this.getChestType() == BlockChest.Type.TRAP) {
                this.world.notifyNeighborsOfStateChange(this.pos.down(), this.getBlockType(), false);
            }
        }

    }

    public void closeInventory(EntityPlayer entityhuman) {
        if (!entityhuman.isSpectator() && this.getBlockType() instanceof BlockChest) {
            int oldPower = Math.max(0, Math.min(15, this.numPlayersUsing)); // CraftBukkit - Get power before new viewer is added
            --this.numPlayersUsing;

            // Paper start - Move chest close sound out of the tick loop
            if (this.numPlayersUsing == 0 && this.lidAngle > 0.0F || this.numPlayersUsing > 0 && this.lidAngle < 1.0F) {
                float f = 0.1F;

                if (this.numPlayersUsing > 0) {
                    this.lidAngle += f;
                } else {
                    this.lidAngle -= f;
                }

                double d0 = (double) this.getPos().getX() + 0.5D;
                double d2 = (double) this.getPos().getZ() + 0.5D;
                int yLoc = this.pos.getY();

                if (this.adjacentChestZPos != null) {
                    d2 += 0.5D;
                }

                if (this.adjacentChestXPos != null) {
                    d0 += 0.5D;
                }

                this.world.playSound((EntityPlayer) null, d0, (double) yLoc + 0.5D, d2, SoundEvents.BLOCK_CHEST_CLOSE, SoundCategory.BLOCKS, 0.5F, this.world.rand.nextFloat() * 0.1F + 0.9F);
                this.lidAngle = 0.0F;
            }
            // Paper end

            this.world.addBlockEvent(this.pos, this.getBlockType(), 1, this.numPlayersUsing);
            this.world.notifyNeighborsOfStateChange(this.pos, this.getBlockType(), false);

            // CraftBukkit start - Call redstone event
            if (this.getChestType() == BlockChest.Type.TRAP) {
                int newPower = Math.max(0, Math.min(15, this.numPlayersUsing));

                if (oldPower != newPower) {
                    org.bukkit.craftbukkit.event.CraftEventFactory.callRedstoneChange(world, pos.getX(), pos.getY(), pos.getZ(), oldPower, newPower);
                }
                this.world.notifyNeighborsOfStateChange(this.pos.down(), this.getBlockType(), false);
            }
            // CraftBukkit end
        }

    }

    public void invalidate() {
        super.invalidate();
        this.updateContainingBlockInfo();
        this.checkForAdjacentChests();
    }

    public BlockChest.Type getChestType() {
        if (this.cachedChestType == null) {
            if (this.world == null || !(this.getBlockType() instanceof BlockChest)) {
                return BlockChest.Type.BASIC;
            }

            this.cachedChestType = ((BlockChest) this.getBlockType()).chestType;
        }

        return this.cachedChestType;
    }

    public String getGuiID() {
        return "minecraft:chest";
    }

    public Container createContainer(InventoryPlayer playerinventory, EntityPlayer entityhuman) {
        this.fillWithLoot(entityhuman);
        return new ContainerChest(playerinventory, this, entityhuman);
    }

    protected NonNullList<ItemStack> getItems() {
        return this.chestContents;
    }

    // CraftBukkit start
    @Override
    public boolean onlyOpsCanSetNbt() {
        return true;
    }
    // CraftBukkit end
}
