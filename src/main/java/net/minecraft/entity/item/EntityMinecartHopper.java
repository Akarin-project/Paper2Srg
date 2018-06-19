package net.minecraft.entity.item;

import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerHopper;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.IHopper;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumHand;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntityMinecartHopper extends EntityMinecartContainer implements IHopper {

    private boolean isBlocked = true;
    private int transferTicker = -1;
    private final BlockPos lastPosition;

    public EntityMinecartHopper(World world) {
        super(world);
        this.lastPosition = BlockPos.ORIGIN;
    }

    public EntityMinecartHopper(World world, double d0, double d1, double d2) {
        super(world, d0, d1, d2);
        this.lastPosition = BlockPos.ORIGIN;
    }

    @Override
    public EntityMinecart.Type getType() {
        return EntityMinecart.Type.HOPPER;
    }

    @Override
    public IBlockState getDefaultDisplayTile() {
        return Blocks.HOPPER.getDefaultState();
    }

    @Override
    public int getDefaultDisplayTileOffset() {
        return 1;
    }

    @Override
    public int getSizeInventory() {
        return 5;
    }

    @Override
    public boolean processInitialInteract(EntityPlayer entityhuman, EnumHand enumhand) {
        if (!this.world.isRemote) {
            entityhuman.displayGUIChest(this);
        }

        return true;
    }

    @Override
    public void onActivatorRailPass(int i, int j, int k, boolean flag) {
        boolean flag1 = !flag;

        if (flag1 != this.getBlocked()) {
            this.setBlocked(flag1);
        }

    }

    public boolean getBlocked() {
        return this.isBlocked;
    }

    public void setBlocked(boolean flag) {
        this.isBlocked = flag;
    }

    @Override
    public World getWorld() { return getEntityWorld(); } // Akarin
    @Override
    public World getEntityWorld() {
        return this.world;
    }

    @Override
    public double getXPos() {
        return this.posX;
    }

    @Override
    public double getYPos() {
        return this.posY + 0.5D;
    }

    @Override
    public double getZPos() {
        return this.posZ;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (!this.world.isRemote && this.isEntityAlive() && this.getBlocked()) {
            BlockPos blockposition = new BlockPos(this);

            if (blockposition.equals(this.lastPosition)) {
                --this.transferTicker;
            } else {
                this.setTransferTicker(0);
            }

            if (!this.canTransfer()) {
                this.setTransferTicker(0);
                if (this.captureDroppedItems()) {
                    this.setTransferTicker(4);
                    this.markDirty();
                }
            }
        }

    }

    public boolean captureDroppedItems() {
        if (TileEntityHopper.pullItems(this)) {
            return true;
        } else {
            List list = this.world.getEntitiesWithinAABB(EntityItem.class, this.getEntityBoundingBox().grow(0.25D, 0.0D, 0.25D), EntitySelectors.IS_ALIVE);

            if (!list.isEmpty()) {
                TileEntityHopper.putDropInInventoryAllSlots((IInventory) null, this, (EntityItem) list.get(0));
            }

            return false;
        }
    }

    @Override
    public void killMinecart(DamageSource damagesource) {
        super.killMinecart(damagesource);
        if (this.world.getGameRules().getBoolean("doEntityDrops")) {
            this.dropItemWithOffset(Item.getItemFromBlock(Blocks.HOPPER), 1, 0.0F);
        }

    }

    public static void registerFixesMinecartHopper(DataFixer dataconvertermanager) {
        EntityMinecartContainer.addDataFixers(dataconvertermanager, EntityMinecartHopper.class);
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound nbttagcompound) {
        super.writeEntityToNBT(nbttagcompound);
        nbttagcompound.setInteger("TransferCooldown", this.transferTicker);
        nbttagcompound.setBoolean("Enabled", this.isBlocked);
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound nbttagcompound) {
        super.readEntityFromNBT(nbttagcompound);
        this.transferTicker = nbttagcompound.getInteger("TransferCooldown");
        this.isBlocked = nbttagcompound.hasKey("Enabled") ? nbttagcompound.getBoolean("Enabled") : true;
    }

    public void setTransferTicker(int i) {
        this.transferTicker = i;
    }

    public boolean canTransfer() {
        return this.transferTicker > 0;
    }

    @Override
    public String getGuiID() {
        return "minecraft:hopper";
    }

    @Override
    public Container createContainer(InventoryPlayer playerinventory, EntityPlayer entityhuman) {
        return new ContainerHopper(playerinventory, this, entityhuman);
    }
}
