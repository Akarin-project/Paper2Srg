package net.minecraft.tileentity;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.annotation.Nullable;

import com.destroystokyo.paper.event.block.BeaconEffectEvent;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.BlockStainedGlass;
import net.minecraft.block.BlockStainedGlassPane;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerBeacon;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import org.bukkit.craftbukkit.entity.CraftHumanEntity;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.craftbukkit.potion.CraftPotionUtil;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

// CraftBukkit start
import org.bukkit.craftbukkit.entity.CraftHumanEntity;
import org.bukkit.craftbukkit.potion.CraftPotionUtil;
import org.bukkit.entity.HumanEntity;
import org.bukkit.potion.PotionEffect;
// CraftBukkit end

// Paper start
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import com.destroystokyo.paper.event.block.BeaconEffectEvent;
// Paper end

public class TileEntityBeacon extends TileEntityLockable implements ITickable, ISidedInventory {

    public static final Potion[][] EFFECTS_LIST = new Potion[][] { { MobEffects.SPEED, MobEffects.HASTE}, { MobEffects.RESISTANCE, MobEffects.JUMP_BOOST}, { MobEffects.STRENGTH}, { MobEffects.REGENERATION}};
    private static final Set<Potion> VALID_EFFECTS = Sets.newHashSet();
    private final List<TileEntityBeacon.BeamSegment> beamSegments = Lists.newArrayList();
    private boolean isComplete;
    public int levels = -1;
    @Nullable
    public Potion primaryEffect;
    @Nullable
    public Potion secondaryEffect;
    private ItemStack payment;
    private String customName;
    // CraftBukkit start - add fields and methods
    public List<HumanEntity> transaction = new java.util.ArrayList<HumanEntity>();
    private int maxStack = MAX_STACK;

    public List<ItemStack> getContents() {
        return Arrays.asList(this.payment);
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

    public PotionEffect getPrimaryEffect() {
        return (this.primaryEffect != null) ? CraftPotionUtil.toBukkit(new PotionEffect(this.primaryEffect, getLevel(), getAmplification(), true, true)) : null;
    }

    public PotionEffect getSecondaryEffect() {
        return (hasSecondaryEffect()) ? CraftPotionUtil.toBukkit(new PotionEffect(this.secondaryEffect, getLevel(), getAmplification(), true, true)) : null;
    }
    // CraftBukkit end

    public TileEntityBeacon() {
        this.payment = ItemStack.EMPTY;
    }

    public void update() {
        if (this.world.getTotalWorldTime() % 80L == 0L) {
            this.updateBeacon();
        }

    }

    public void updateBeacon() {
        if (this.world != null) {
            this.updateSegmentColors();
            this.addEffectsToPlayers();
        }

    }

    // CraftBukkit start - split into components
    private byte getAmplification() {
        {
            byte b0 = 0;

            if (this.levels >= 4 && this.primaryEffect == this.secondaryEffect) {
                b0 = 1;
            }

            return b0;
        }
    }

    private int getLevel() {
        {
            int i = (9 + this.levels * 2) * 20;
            return i;
        }
    }

    public List getHumansInRange() {
        {
            double d0 = (double) (this.levels * 10 + 10);

            int j = this.pos.getX();
            int k = this.pos.getY();
            int l = this.pos.getZ();
            AxisAlignedBB axisalignedbb = (new AxisAlignedBB((double) j, (double) k, (double) l, (double) (j + 1), (double) (k + 1), (double) (l + 1))).grow(d0).expand(0.0D, (double) this.world.getHeight(), 0.0D);
            List list = this.world.getEntitiesWithinAABB(EntityPlayer.class, axisalignedbb);

            return list;
        }
    }

    private void applyEffect(List list, Potion effects, int i, int b0) {
        // Paper - BeaconEffectEvent
        applyEffect(list, effects, i, b0, true);
    }

    private void applyEffect(List list, Potion effects, int i, int b0, boolean isPrimary) {
        // Paper - BeaconEffectEvent
        {
            Iterator iterator = list.iterator();

            EntityPlayer entityhuman;

            // Paper start - BeaconEffectEvent
            org.bukkit.block.Block block = world.getWorld().getBlockAt(pos.getX(), pos.getY(), pos.getZ());
            PotionEffect effect = CraftPotionUtil.toBukkit(new PotionEffect(effects, i, b0, true, true));
            // Paper end

            while (iterator.hasNext()) {
                entityhuman = (EntityPlayer) iterator.next();
                // Paper start - BeaconEffectEvent
                BeaconEffectEvent event = new BeaconEffectEvent(block, effect, (Player) entityhuman.getBukkitEntity(), isPrimary);
                if (CraftEventFactory.callEvent(event).isCancelled()) continue;
                PotionEffect eventEffect = event.getEffect();
                entityhuman.getBukkitEntity().addPotionEffect(eventEffect, true);
                // Paper end
            }
        }
    }

    private boolean hasSecondaryEffect() {
        {
            if (this.levels >= 4 && this.primaryEffect != this.secondaryEffect && this.secondaryEffect != null) {
                return true;
            }

            return false;
        }
    }

    private void addEffectsToPlayers() {
        if (this.isComplete && this.levels > 0 && !this.world.isRemote && this.primaryEffect != null) {
            byte b0 = getAmplification();

            int i = getLevel();
            List list = getHumansInRange();

            applyEffect(list, this.primaryEffect, i, b0, true); // Paper - BeaconEffectEvent

            if (hasSecondaryEffect()) {
                applyEffect(list, this.secondaryEffect, i, 0, false); // Paper - BeaconEffectEvent
            }
        }

    }
    // CraftBukkit end

    private void updateSegmentColors() {
        int i = this.pos.getX();
        int j = this.pos.getY();
        int k = this.pos.getZ();
        int l = this.levels;

        this.levels = 0;
        this.beamSegments.clear();
        this.isComplete = true;
        TileEntityBeacon.BeamSegment tileentitybeacon_beaconcolortracker = new TileEntityBeacon.BeamSegment(EnumDyeColor.WHITE.getColorComponentValues());

        this.beamSegments.add(tileentitybeacon_beaconcolortracker);
        boolean flag = true;
        BlockPos.MutableBlockPos blockposition_mutableblockposition = new BlockPos.MutableBlockPos();

        int i1;

        for (i1 = j + 1; i1 < 256; ++i1) {
            IBlockState iblockdata = this.world.getBlockState(blockposition_mutableblockposition.setPos(i, i1, k));
            float[] afloat;

            if (iblockdata.getBlock() == Blocks.STAINED_GLASS) {
                afloat = ((EnumDyeColor) iblockdata.getValue(BlockStainedGlass.COLOR)).getColorComponentValues();
            } else {
                if (iblockdata.getBlock() != Blocks.STAINED_GLASS_PANE) {
                    if (iblockdata.getLightOpacity() >= 15 && iblockdata.getBlock() != Blocks.BEDROCK) {
                        this.isComplete = false;
                        this.beamSegments.clear();
                        break;
                    }

                    tileentitybeacon_beaconcolortracker.incrementHeight();
                    continue;
                }

                afloat = ((EnumDyeColor) iblockdata.getValue(BlockStainedGlassPane.COLOR)).getColorComponentValues();
            }

            if (!flag) {
                afloat = new float[] { (tileentitybeacon_beaconcolortracker.getColors()[0] + afloat[0]) / 2.0F, (tileentitybeacon_beaconcolortracker.getColors()[1] + afloat[1]) / 2.0F, (tileentitybeacon_beaconcolortracker.getColors()[2] + afloat[2]) / 2.0F};
            }

            if (Arrays.equals(afloat, tileentitybeacon_beaconcolortracker.getColors())) {
                tileentitybeacon_beaconcolortracker.incrementHeight();
            } else {
                tileentitybeacon_beaconcolortracker = new TileEntityBeacon.BeamSegment(afloat);
                this.beamSegments.add(tileentitybeacon_beaconcolortracker);
            }

            flag = false;
        }

        if (this.isComplete) {
            for (i1 = 1; i1 <= 4; this.levels = i1++) {
                int j1 = j - i1;

                if (j1 < 0) {
                    break;
                }

                boolean flag1 = true;

                for (int k1 = i - i1; k1 <= i + i1 && flag1; ++k1) {
                    for (int l1 = k - i1; l1 <= k + i1; ++l1) {
                        Block block = this.world.getBlockState(new BlockPos(k1, j1, l1)).getBlock();

                        if (block != Blocks.EMERALD_BLOCK && block != Blocks.GOLD_BLOCK && block != Blocks.DIAMOND_BLOCK && block != Blocks.IRON_BLOCK) {
                            flag1 = false;
                            break;
                        }
                    }
                }

                if (!flag1) {
                    break;
                }
            }

            if (this.levels == 0) {
                this.isComplete = false;
            }
        }

        if (!this.world.isRemote && l < this.levels) {
            Iterator iterator = this.world.getEntitiesWithinAABB(EntityPlayerMP.class, (new AxisAlignedBB((double) i, (double) j, (double) k, (double) i, (double) (j - 4), (double) k)).grow(10.0D, 5.0D, 10.0D)).iterator();

            while (iterator.hasNext()) {
                EntityPlayerMP entityplayer = (EntityPlayerMP) iterator.next();

                CriteriaTriggers.CONSTRUCT_BEACON.trigger(entityplayer, this);
            }
        }

    }

    public int getLevels() {
        return this.levels;
    }

    @Nullable
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(this.pos, 3, this.getUpdateTag());
    }

    public NBTTagCompound getUpdateTag() {
        return this.writeToNBT(new NBTTagCompound());
    }

    @Nullable
    private static Potion isBeaconEffect(int i) {
        Potion mobeffectlist = Potion.getPotionById(i);

        return TileEntityBeacon.VALID_EFFECTS.contains(mobeffectlist) ? mobeffectlist : null;
    }

    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        // Craftbukkit start - persist manually set non-default beacon effects (SPIGOT-3598)
        this.primaryEffect = Potion.getPotionById(nbttagcompound.getInteger("Primary"));
        this.secondaryEffect = Potion.getPotionById(nbttagcompound.getInteger("Secondary"));
        // Craftbukkit end
        this.levels = nbttagcompound.getInteger("Levels");
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        nbttagcompound.setInteger("Primary", Potion.getIdFromPotion(this.primaryEffect));
        nbttagcompound.setInteger("Secondary", Potion.getIdFromPotion(this.secondaryEffect));
        nbttagcompound.setInteger("Levels", this.levels);
        return nbttagcompound;
    }

    public int getSizeInventory() {
        return 1;
    }

    public boolean isEmpty() {
        return this.payment.isEmpty();
    }

    public ItemStack getStackInSlot(int i) {
        return i == 0 ? this.payment : ItemStack.EMPTY;
    }

    public ItemStack decrStackSize(int i, int j) {
        if (i == 0 && !this.payment.isEmpty()) {
            if (j >= this.payment.getCount()) {
                ItemStack itemstack = this.payment;

                this.payment = ItemStack.EMPTY;
                return itemstack;
            } else {
                return this.payment.splitStack(j);
            }
        } else {
            return ItemStack.EMPTY;
        }
    }

    public ItemStack removeStackFromSlot(int i) {
        if (i == 0) {
            ItemStack itemstack = this.payment;

            this.payment = ItemStack.EMPTY;
            return itemstack;
        } else {
            return ItemStack.EMPTY;
        }
    }

    public void setInventorySlotContents(int i, ItemStack itemstack) {
        if (i == 0) {
            this.payment = itemstack;
        }

    }

    public String getName() {
        return this.hasCustomName() ? this.customName : "container.beacon";
    }

    public boolean hasCustomName() {
        return this.customName != null && !this.customName.isEmpty();
    }

    public void setName(String s) {
        this.customName = s;
    }

    public int getInventoryStackLimit() {
        return 1;
    }

    public boolean isUsableByPlayer(EntityPlayer entityhuman) {
        return this.world.getTileEntity(this.pos) != this ? false : entityhuman.getDistanceSq((double) this.pos.getX() + 0.5D, (double) this.pos.getY() + 0.5D, (double) this.pos.getZ() + 0.5D) <= 64.0D;
    }

    public void openInventory(EntityPlayer entityhuman) {}

    public void closeInventory(EntityPlayer entityhuman) {}

    public boolean isItemValidForSlot(int i, ItemStack itemstack) {
        return itemstack.getItem() == Items.EMERALD || itemstack.getItem() == Items.DIAMOND || itemstack.getItem() == Items.GOLD_INGOT || itemstack.getItem() == Items.IRON_INGOT;
    }

    public String getGuiID() {
        return "minecraft:beacon";
    }

    public Container createContainer(InventoryPlayer playerinventory, EntityPlayer entityhuman) {
        return new ContainerBeacon(playerinventory, this);
    }

    public int getField(int i) {
        switch (i) {
        case 0:
            return this.levels;

        case 1:
            return Potion.getIdFromPotion(this.primaryEffect);

        case 2:
            return Potion.getIdFromPotion(this.secondaryEffect);

        default:
            return 0;
        }
    }

    public void setField(int i, int j) {
        switch (i) {
        case 0:
            this.levels = j;
            break;

        case 1:
            this.primaryEffect = isBeaconEffect(j);
            break;

        case 2:
            this.secondaryEffect = isBeaconEffect(j);
        }

    }

    public int getFieldCount() {
        return 3;
    }

    public void clear() {
        this.payment = ItemStack.EMPTY;
    }

    public boolean receiveClientEvent(int i, int j) {
        if (i == 1) {
            this.updateBeacon();
            return true;
        } else {
            return super.receiveClientEvent(i, j);
        }
    }

    public int[] getSlotsForFace(EnumFacing enumdirection) {
        return new int[0];
    }

    public boolean canInsertItem(int i, ItemStack itemstack, EnumFacing enumdirection) {
        return false;
    }

    public boolean canExtractItem(int i, ItemStack itemstack, EnumFacing enumdirection) {
        return false;
    }

    static {
        Potion[][] amobeffectlist = TileEntityBeacon.EFFECTS_LIST;
        int i = amobeffectlist.length;

        for (int j = 0; j < i; ++j) {
            Potion[] amobeffectlist1 = amobeffectlist[j];

            Collections.addAll(TileEntityBeacon.VALID_EFFECTS, amobeffectlist1);
        }

    }

    public static class BeamSegment {

        private final float[] colors;
        private int height;

        public BeamSegment(float[] afloat) {
            this.colors = afloat;
            this.height = 1;
        }

        protected void incrementHeight() {
            ++this.height;
        }

        public float[] getColors() {
            return this.colors;
        }
    }
}
