package net.minecraft.tileentity;

import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockShulkerBox;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerShulkerBox;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.IDataWalker;
import net.minecraft.util.datafix.walkers.ItemStackDataLists;
import net.minecraft.util.math.AxisAlignedBB;
import org.bukkit.craftbukkit.entity.CraftHumanEntity;
import org.bukkit.entity.HumanEntity;

// CraftBukkit start
import org.bukkit.craftbukkit.entity.CraftHumanEntity;
import org.bukkit.entity.HumanEntity;
// CraftBukkit end

public class TileEntityShulkerBox extends TileEntityLockableLoot implements ITickable, ISidedInventory {

    private static final int[] SLOTS = new int[27];
    private NonNullList<ItemStack> items;
    private boolean hasBeenCleared;
    private int openCount;
    private TileEntityShulkerBox.AnimationStatus animationStatus;
    private float progress;
    private float progressOld;
    private EnumDyeColor color;
    private boolean destroyedByCreativePlayer;

    // CraftBukkit start - add fields and methods
    public List<HumanEntity> transaction = new java.util.ArrayList<HumanEntity>();
    private int maxStack = MAX_STACK;

    public List<ItemStack> getContents() {
        return this.items;
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

    public TileEntityShulkerBox() {
        this((EnumDyeColor) null);
    }

    public TileEntityShulkerBox(@Nullable EnumDyeColor enumcolor) {
        this.items = NonNullList.withSize(27, ItemStack.EMPTY);
        this.animationStatus = TileEntityShulkerBox.AnimationStatus.CLOSED;
        this.color = enumcolor;
    }

    public void update() {
        this.updateAnimation();
        if (this.animationStatus == TileEntityShulkerBox.AnimationStatus.OPENING || this.animationStatus == TileEntityShulkerBox.AnimationStatus.CLOSING) {
            this.moveCollidedEntities();
        }

    }

    protected void updateAnimation() {
        this.progressOld = this.progress;
        switch (this.animationStatus) {
        case CLOSED:
            this.progress = 0.0F;
            break;

        case OPENING:
            this.progress += 0.1F;
            if (this.progress >= 1.0F) {
                this.moveCollidedEntities();
                this.animationStatus = TileEntityShulkerBox.AnimationStatus.OPENED;
                this.progress = 1.0F;
            }
            break;

        case CLOSING:
            this.progress -= 0.1F;
            if (this.progress <= 0.0F) {
                this.animationStatus = TileEntityShulkerBox.AnimationStatus.CLOSED;
                this.progress = 0.0F;
            }
            break;

        case OPENED:
            this.progress = 1.0F;
        }

    }

    public TileEntityShulkerBox.AnimationStatus getAnimationStatus() {
        return this.animationStatus;
    }

    public AxisAlignedBB getBoundingBox(IBlockState iblockdata) {
        return this.getBoundingBox((EnumFacing) iblockdata.getValue(BlockShulkerBox.FACING));
    }

    public AxisAlignedBB getBoundingBox(EnumFacing enumdirection) {
        return Block.FULL_BLOCK_AABB.expand((double) (0.5F * this.getProgress(1.0F) * (float) enumdirection.getFrontOffsetX()), (double) (0.5F * this.getProgress(1.0F) * (float) enumdirection.getFrontOffsetY()), (double) (0.5F * this.getProgress(1.0F) * (float) enumdirection.getFrontOffsetZ()));
    }

    private AxisAlignedBB getTopBoundingBox(EnumFacing enumdirection) {
        EnumFacing enumdirection1 = enumdirection.getOpposite();

        return this.getBoundingBox(enumdirection).contract((double) enumdirection1.getFrontOffsetX(), (double) enumdirection1.getFrontOffsetY(), (double) enumdirection1.getFrontOffsetZ());
    }

    private void moveCollidedEntities() {
        IBlockState iblockdata = this.world.getBlockState(this.getPos());

        if (iblockdata.getBlock() instanceof BlockShulkerBox) {
            EnumFacing enumdirection = (EnumFacing) iblockdata.getValue(BlockShulkerBox.FACING);
            AxisAlignedBB axisalignedbb = this.getTopBoundingBox(enumdirection).offset(this.pos);
            List list = this.world.getEntitiesWithinAABBExcludingEntity((Entity) null, axisalignedbb);

            if (!list.isEmpty()) {
                for (int i = 0; i < list.size(); ++i) {
                    Entity entity = (Entity) list.get(i);

                    if (entity.getPushReaction() != EnumPushReaction.IGNORE) {
                        double d0 = 0.0D;
                        double d1 = 0.0D;
                        double d2 = 0.0D;
                        AxisAlignedBB axisalignedbb1 = entity.getEntityBoundingBox();

                        switch (enumdirection.getAxis()) {
                        case X:
                            if (enumdirection.getAxisDirection() == EnumFacing.AxisDirection.POSITIVE) {
                                d0 = axisalignedbb.maxX - axisalignedbb1.minX;
                            } else {
                                d0 = axisalignedbb1.maxX - axisalignedbb.minX;
                            }

                            d0 += 0.01D;
                            break;

                        case Y:
                            if (enumdirection.getAxisDirection() == EnumFacing.AxisDirection.POSITIVE) {
                                d1 = axisalignedbb.maxY - axisalignedbb1.minY;
                            } else {
                                d1 = axisalignedbb1.maxY - axisalignedbb.minY;
                            }

                            d1 += 0.01D;
                            break;

                        case Z:
                            if (enumdirection.getAxisDirection() == EnumFacing.AxisDirection.POSITIVE) {
                                d2 = axisalignedbb.maxZ - axisalignedbb1.minZ;
                            } else {
                                d2 = axisalignedbb1.maxZ - axisalignedbb.minZ;
                            }

                            d2 += 0.01D;
                        }

                        entity.move(MoverType.SHULKER_BOX, d0 * (double) enumdirection.getFrontOffsetX(), d1 * (double) enumdirection.getFrontOffsetY(), d2 * (double) enumdirection.getFrontOffsetZ());
                    }
                }

            }
        }
    }

    public int getSizeInventory() {
        return this.items.size();
    }

    public int getInventoryStackLimit() {
        return maxStack; // CraftBukkit
    }

    public boolean receiveClientEvent(int i, int j) {
        if (i == 1) {
            this.openCount = j;
            if (j == 0) {
                this.animationStatus = TileEntityShulkerBox.AnimationStatus.CLOSING;
            }

            if (j == 1) {
                this.animationStatus = TileEntityShulkerBox.AnimationStatus.OPENING;
            }

            return true;
        } else {
            return super.receiveClientEvent(i, j);
        }
    }

    public void openInventory(EntityPlayer entityhuman) {
        if (!entityhuman.isSpectator()) {
            if (this.openCount < 0) {
                this.openCount = 0;
            }

            ++this.openCount;
            this.world.addBlockEvent(this.pos, this.getBlockType(), 1, this.openCount);
            if (this.openCount == 1) {
                this.world.playSound((EntityPlayer) null, this.pos, SoundEvents.BLOCK_SHULKER_BOX_OPEN, SoundCategory.BLOCKS, 0.5F, this.world.rand.nextFloat() * 0.1F + 0.9F);
            }
        }

    }

    public void closeInventory(EntityPlayer entityhuman) {
        if (!entityhuman.isSpectator()) {
            --this.openCount;
            this.world.addBlockEvent(this.pos, this.getBlockType(), 1, this.openCount);
            if (this.openCount <= 0) {
                this.world.playSound((EntityPlayer) null, this.pos, SoundEvents.BLOCK_SHULKER_BOX_CLOSE, SoundCategory.BLOCKS, 0.5F, this.world.rand.nextFloat() * 0.1F + 0.9F);
            }
        }

    }

    public Container createContainer(InventoryPlayer playerinventory, EntityPlayer entityhuman) {
        return new ContainerShulkerBox(playerinventory, this, entityhuman);
    }

    public String getGuiID() {
        return "minecraft:shulker_box";
    }

    public String getName() {
        return this.hasCustomName() ? this.customName : "container.shulkerBox";
    }

    public static void registerFixesShulkerBox(DataFixer dataconvertermanager) {
        dataconvertermanager.registerWalker(FixTypes.BLOCK_ENTITY, (IDataWalker) (new ItemStackDataLists(TileEntityShulkerBox.class, new String[] { "Items"})));
    }

    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.loadFromNbt(nbttagcompound);
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        return this.saveToNbt(nbttagcompound);
    }

    public void loadFromNbt(NBTTagCompound nbttagcompound) {
        this.items = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);
        if (!this.checkLootAndRead(nbttagcompound) && nbttagcompound.hasKey("Items", 9)) {
            ItemStackHelper.loadAllItems(nbttagcompound, this.items);
        }

        if (nbttagcompound.hasKey("CustomName", 8)) {
            this.customName = nbttagcompound.getString("CustomName");
        }

    }

    public NBTTagCompound saveToNbt(NBTTagCompound nbttagcompound) {
        if (!this.checkLootAndWrite(nbttagcompound)) {
            ItemStackHelper.saveAllItems(nbttagcompound, this.items, false);
        }

        if (this.hasCustomName()) {
            nbttagcompound.setString("CustomName", this.customName);
        }

        if (!nbttagcompound.hasKey("Lock") && this.isLocked()) {
            this.getLockCode().toNBT(nbttagcompound);
        }

        return nbttagcompound;
    }

    protected NonNullList<ItemStack> getItems() {
        return this.items;
    }

    public boolean isEmpty() {
        Iterator iterator = this.items.iterator();

        ItemStack itemstack;

        do {
            if (!iterator.hasNext()) {
                return true;
            }

            itemstack = (ItemStack) iterator.next();
        } while (itemstack.isEmpty());

        return false;
    }

    public int[] getSlotsForFace(EnumFacing enumdirection) {
        return TileEntityShulkerBox.SLOTS;
    }

    public boolean canInsertItem(int i, ItemStack itemstack, EnumFacing enumdirection) {
        return !(Block.getBlockFromItem(itemstack.getItem()) instanceof BlockShulkerBox);
    }

    public boolean canExtractItem(int i, ItemStack itemstack, EnumFacing enumdirection) {
        return true;
    }

    public void clear() {
        this.hasBeenCleared = true;
        super.clear();
    }

    public boolean isCleared() {
        return this.hasBeenCleared;
    }

    public float getProgress(float f) {
        return this.progressOld + (this.progress - this.progressOld) * f;
    }

    @Nullable
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(this.pos, 10, this.getUpdateTag());
    }

    public boolean isDestroyedByCreativePlayer() {
        return this.destroyedByCreativePlayer;
    }

    public void setDestroyedByCreativePlayer(boolean flag) {
        this.destroyedByCreativePlayer = flag;
    }

    public boolean shouldDrop() {
        return !this.isDestroyedByCreativePlayer() || !this.isEmpty() || this.hasCustomName() || this.lootTable != null;
    }

    static {
        for (int i = 0; i < TileEntityShulkerBox.SLOTS.length; TileEntityShulkerBox.SLOTS[i] = i++) {
            ;
        }

    }

    public static enum AnimationStatus {

        CLOSED, OPENING, OPENED, CLOSING;

        private AnimationStatus() {}
    }
}
