package org.bukkit.craftbukkit.entity;

import com.google.common.base.Preconditions;
import java.util.Set;


import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.inventory.MainHand;
import org.bukkit.inventory.Merchant;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Villager;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.craftbukkit.inventory.CraftContainer;
import org.bukkit.craftbukkit.inventory.CraftInventory;
import org.bukkit.craftbukkit.inventory.CraftInventoryPlayer;
import org.bukkit.craftbukkit.inventory.CraftInventoryView;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.craftbukkit.inventory.CraftMerchant;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.permissions.PermissibleBase;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;


import net.minecraft.block.BlockAnvil;
import net.minecraft.block.BlockWorkbench;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.item.EntityMinecartHopper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.client.CPacketCloseWindow;
import net.minecraft.network.play.server.SPacketOpenWindow;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBeacon;
import net.minecraft.tileentity.TileEntityBrewingStand;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.tileentity.TileEntityDropper;
import net.minecraft.tileentity.TileEntityEnchantmentTable;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.tileentity.TileEntityShulkerBox;
import net.minecraft.util.CooldownTracker;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.IInteractionObject;

public class CraftHumanEntity extends CraftLivingEntity implements HumanEntity {
    private CraftInventoryPlayer inventory;
    private final CraftInventory enderChest;
    protected final PermissibleBase perm = new PermissibleBase(this);
    private boolean op;
    private GameMode mode;

    public CraftHumanEntity(final CraftServer server, final EntityPlayer entity) {
        super(server, entity);
        mode = server.getDefaultGameMode();
        this.inventory = new CraftInventoryPlayer(entity.field_71071_by);
        enderChest = new CraftInventory(entity.func_71005_bN());
    }

    public String getName() {
        return getHandle().func_70005_c_();
    }

    public PlayerInventory getInventory() {
        return inventory;
    }

    public EntityEquipment getEquipment() {
        return inventory;
    }

    public Inventory getEnderChest() {
        return enderChest;
    }

    public MainHand getMainHand() {
        return getHandle().func_184591_cq()== EnumHandSide.LEFT ? MainHand.LEFT : MainHand.RIGHT;
    }

    public ItemStack getItemInHand() {
        return getInventory().getItemInHand();
    }

    public void setItemInHand(ItemStack item) {
        getInventory().setItemInHand(item);
    }

    public ItemStack getItemOnCursor() {
        return CraftItemStack.asCraftMirror(getHandle().field_71071_by.func_70445_o());
    }

    public void setItemOnCursor(ItemStack item) {
        net.minecraft.item.ItemStack stack = CraftItemStack.asNMSCopy(item);
        getHandle().field_71071_by.func_70437_b(stack);
        if (this instanceof CraftPlayer) {
            ((EntityPlayerMP) getHandle()).func_71113_k(); // Send set slot for cursor
        }
    }

    public boolean isSleeping() {
        return getHandle().field_71083_bS;
    }

    public int getSleepTicks() {
        return getHandle().field_71076_b;
    }

    public boolean isOp() {
        return op;
    }

    public boolean isPermissionSet(String name) {
        return perm.isPermissionSet(name);
    }

    public boolean isPermissionSet(Permission perm) {
        return this.perm.isPermissionSet(perm);
    }

    public boolean hasPermission(String name) {
        return perm.hasPermission(name);
    }

    public boolean hasPermission(Permission perm) {
        return this.perm.hasPermission(perm);
    }

    public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value) {
        return perm.addAttachment(plugin, name, value);
    }

    public PermissionAttachment addAttachment(Plugin plugin) {
        return perm.addAttachment(plugin);
    }

    public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value, int ticks) {
        return perm.addAttachment(plugin, name, value, ticks);
    }

    public PermissionAttachment addAttachment(Plugin plugin, int ticks) {
        return perm.addAttachment(plugin, ticks);
    }

    public void removeAttachment(PermissionAttachment attachment) {
        perm.removeAttachment(attachment);
    }

    public void recalculatePermissions() {
        perm.recalculatePermissions();
    }

    public void setOp(boolean value) {
        this.op = value;
        perm.recalculatePermissions();
    }

    public Set<PermissionAttachmentInfo> getEffectivePermissions() {
        return perm.getEffectivePermissions();
    }

    public GameMode getGameMode() {
        return mode;
    }

    public void setGameMode(GameMode mode) {
        if (mode == null) {
            throw new IllegalArgumentException("Mode cannot be null");
        }

        this.mode = mode;
    }

    @Override
    public EntityPlayer getHandle() {
        return (EntityPlayer) entity;
    }

    public void setHandle(final EntityPlayer entity) {
        super.setHandle(entity);
        this.inventory = new CraftInventoryPlayer(entity.field_71071_by);
    }

    @Override
    public String toString() {
        return "CraftHumanEntity{" + "id=" + getEntityId() + "name=" + getName() + '}';
    }

    public InventoryView getOpenInventory() {
        return getHandle().field_71070_bA.getBukkitView();
    }

    public InventoryView openInventory(Inventory inventory) {
        if(!(getHandle() instanceof EntityPlayerMP)) return null;
        EntityPlayerMP player = (EntityPlayerMP) getHandle();
        InventoryType type = inventory.getType();
        Container formerContainer = getHandle().field_71070_bA;

        IInventory iinventory = (inventory instanceof CraftInventory) ? ((CraftInventory) inventory).getInventory() : new org.bukkit.craftbukkit.inventory.InventoryWrapper(inventory);

        switch (type) {
            case PLAYER:
            case CHEST:
            case ENDER_CHEST:
                getHandle().func_71007_a(iinventory);
                break;
            case DISPENSER:
                if (iinventory instanceof TileEntityDispenser) {
                    getHandle().func_71007_a((TileEntityDispenser) iinventory);
                } else {
                    openCustomInventory(inventory, player, "minecraft:dispenser");
                }
                break;
            case DROPPER:
                if (iinventory instanceof TileEntityDropper) {
                    getHandle().func_71007_a((TileEntityDropper) iinventory);
                } else {
                    openCustomInventory(inventory, player, "minecraft:dropper");
                }
                break;
            case FURNACE:
                if (iinventory instanceof TileEntityFurnace) {
                    getHandle().func_71007_a((TileEntityFurnace) iinventory);
                } else {
                    openCustomInventory(inventory, player, "minecraft:furnace");
                }
                break;
            case WORKBENCH:
                openCustomInventory(inventory, player, "minecraft:crafting_table");
                break;
            case BREWING:
                if (iinventory instanceof TileEntityBrewingStand) {
                    getHandle().func_71007_a((TileEntityBrewingStand) iinventory);
                } else {
                    openCustomInventory(inventory, player, "minecraft:brewing_stand");
                }
                break;
            case ENCHANTING:
                openCustomInventory(inventory, player, "minecraft:enchanting_table");
                break;
            case HOPPER:
                if (iinventory instanceof TileEntityHopper) {
                    getHandle().func_71007_a((TileEntityHopper) iinventory);
                } else if (iinventory instanceof EntityMinecartHopper) {
                    getHandle().func_71007_a((EntityMinecartHopper) iinventory);
                } else {
                    openCustomInventory(inventory, player, "minecraft:hopper");
                }
                break;
            case BEACON:
                if (iinventory instanceof TileEntityBeacon) {
                    getHandle().func_71007_a((TileEntityBeacon) iinventory);
                } else {
                    openCustomInventory(inventory, player, "minecraft:beacon");
                }
                break;
            case ANVIL:
                if (iinventory instanceof BlockAnvil.Anvil) {
                    getHandle().func_180468_a((BlockAnvil.Anvil) iinventory);
                } else {
                    openCustomInventory(inventory, player, "minecraft:anvil");
                }
                break;
            case SHULKER_BOX:
                if (iinventory instanceof TileEntityShulkerBox) {
                    getHandle().func_71007_a((TileEntityShulkerBox) iinventory);
                } else {
                    openCustomInventory(inventory, player, "minecraft:shulker_box");
                }
                break;
            case CREATIVE:
            case CRAFTING:
                throw new IllegalArgumentException("Can't open a " + type + " inventory!");
        }
        if (getHandle().field_71070_bA == formerContainer) {
            return null;
        }
        getHandle().field_71070_bA.checkReachable = false;
        return getHandle().field_71070_bA.getBukkitView();
    }

    private void openCustomInventory(Inventory inventory, EntityPlayerMP player, String windowType) {
        if (player.field_71135_a == null) return;
        Container container = new CraftContainer(inventory, this.getHandle(), player.nextContainerCounter());

        container = CraftEventFactory.callInventoryOpenEvent(player, container);
        if(container == null) return;

        String title = container.getBukkitView().getTitle();
        int size = container.getBukkitView().getTopInventory().getSize();

        // Special cases
        if (windowType.equals("minecraft:crafting_table") 
                || windowType.equals("minecraft:anvil")
                || windowType.equals("minecraft:enchanting_table")
                ) {
            size = 0;
        }

        player.field_71135_a.func_147359_a(new SPacketOpenWindow(container.field_75152_c, windowType, new TextComponentString(title), size));
        getHandle().field_71070_bA = container;
        getHandle().field_71070_bA.func_75132_a(player);
    }

    public InventoryView openWorkbench(Location location, boolean force) {
        if (!force) {
            Block block = location.getBlock();
            if (block.getType() != Material.WORKBENCH) {
                return null;
            }
        }
        if (location == null) {
            location = getLocation();
        }
        getHandle().func_180468_a(new BlockWorkbench.InterfaceCraftingTable(getHandle().field_70170_p, new BlockPos(location.getBlockX(), location.getBlockY(), location.getBlockZ())));
        if (force) {
            getHandle().field_71070_bA.checkReachable = false;
        }
        return getHandle().field_71070_bA.getBukkitView();
    }

    public InventoryView openEnchanting(Location location, boolean force) {
        if (!force) {
            Block block = location.getBlock();
            if (block.getType() != Material.ENCHANTMENT_TABLE) {
                return null;
            }
        }
        if (location == null) {
            location = getLocation();
        }

        // If there isn't an enchant table we can force create one, won't be very useful though.
        BlockPos pos = new BlockPos(location.getBlockX(), location.getBlockY(), location.getBlockZ());
        TileEntity container = getHandle().field_70170_p.func_175625_s(pos);
        if (container == null && force) {
            container = new TileEntityEnchantmentTable();
            container.func_145834_a(getHandle().field_70170_p);
            container.func_174878_a(pos);
        }
        getHandle().func_180468_a((IInteractionObject) container);

        if (force) {
            getHandle().field_71070_bA.checkReachable = false;
        }
        return getHandle().field_71070_bA.getBukkitView();
    }

    public void openInventory(InventoryView inventory) {
        if (!(getHandle() instanceof EntityPlayerMP)) return; // TODO: NPC support?
        if (((EntityPlayerMP) getHandle()).field_71135_a == null) return;
        if (getHandle().field_71070_bA != getHandle().field_71069_bz) {
            // fire INVENTORY_CLOSE if one already open
            ((EntityPlayerMP)getHandle()).field_71135_a.func_147356_a(new CPacketCloseWindow(getHandle().field_71070_bA.field_75152_c));
        }
        EntityPlayerMP player = (EntityPlayerMP) getHandle();
        Container container;
        if (inventory instanceof CraftInventoryView) {
            container = ((CraftInventoryView) inventory).getHandle();
        } else {
            container = new CraftContainer(inventory, this.getHandle(), player.nextContainerCounter());
        }

        // Trigger an INVENTORY_OPEN event
        container = CraftEventFactory.callInventoryOpenEvent(player, container);
        if (container == null) {
            return;
        }

        // Now open the window
        InventoryType type = inventory.getType();
        String windowType = CraftContainer.getNotchInventoryType(type);
        String title = inventory.getTitle();
        int size = inventory.getTopInventory().getSize();
        player.field_71135_a.func_147359_a(new SPacketOpenWindow(container.field_75152_c, windowType, new TextComponentString(title), size));
        player.field_71070_bA = container;
        player.field_71070_bA.func_75132_a(player);
    }

    @Override
    public InventoryView openMerchant(Villager villager, boolean force) {
        Preconditions.checkNotNull(villager, "villager cannot be null");

        return this.openMerchant((Merchant) villager, force);
    }

    @Override
    public InventoryView openMerchant(Merchant merchant, boolean force) {
        Preconditions.checkNotNull(merchant, "merchant cannot be null");

        if (!force && merchant.isTrading()) {
            return null;
        } else if (merchant.isTrading()) {
            // we're not supposed to have multiple people using the same merchant, so we have to close it.
            merchant.getTrader().closeInventory();
        }

        IMerchant mcMerchant;
        if (merchant instanceof CraftVillager) {
            mcMerchant = ((CraftVillager) merchant).getHandle();
        } else if (merchant instanceof CraftMerchant) {
            mcMerchant = ((CraftMerchant) merchant).getMerchant();
        } else {
            throw new IllegalArgumentException("Can't open merchant " + merchant.toString());
        }

        mcMerchant.func_70932_a_(this.getHandle());
        this.getHandle().func_180472_a(mcMerchant);

        return this.getHandle().field_71070_bA.getBukkitView();
    }

    public void closeInventory() {
        getHandle().func_71053_j();
    }

    public boolean isBlocking() {
        return getHandle().func_184585_cz();
    }

    @Override
    public boolean isHandRaised() {
        return getHandle().func_184587_cr();
    }

    public boolean setWindowProperty(InventoryView.Property prop, int value) {
        return false;
    }

    public int getExpToLevel() {
        return getHandle().func_71050_bK();
    }

    @Override
    public boolean hasCooldown(Material material) {
        Preconditions.checkArgument(material != null, "material");

        return getHandle().func_184811_cZ().func_185141_a(CraftMagicNumbers.getItem(material));
    }

    @Override
    public int getCooldown(Material material) {
        Preconditions.checkArgument(material != null, "material");

        CooldownTracker.Cooldown cooldown = getHandle().func_184811_cZ().field_185147_a.get(CraftMagicNumbers.getItem(material));
        return (cooldown == null) ? 0 : Math.max(0, cooldown.field_185138_b - getHandle().func_184811_cZ().field_185148_b);
    }

    @Override
    public void setCooldown(Material material, int ticks) {
        Preconditions.checkArgument(material != null, "material");
        Preconditions.checkArgument(ticks >= 0, "Cannot have negative cooldown");

        getHandle().func_184811_cZ().func_185145_a(CraftMagicNumbers.getItem(material), ticks);
    }

    // Paper start
    @Override
    public org.bukkit.entity.Entity releaseLeftShoulderEntity() {
        if (!getHandle().func_192023_dk().func_82582_d()) {
            Entity entity = getHandle().releaseLeftShoulderEntity();
            if (entity != null) {
                return entity.getBukkitEntity();
            }
        }

        return null;
    }

    @Override
    public org.bukkit.entity.Entity releaseRightShoulderEntity() {
        if (!getHandle().func_192025_dl().func_82582_d()) {
            Entity entity = getHandle().releaseRightShoulderEntity();
            if (entity != null) {
                return entity.getBukkitEntity();
            }
        }

        return null;
    }
    // Paper end

    @Override
    public org.bukkit.entity.Entity getShoulderEntityLeft() {
        if (!getHandle().func_192023_dk().func_82582_d()) {
            Entity shoulder = EntityList.func_75615_a(getHandle().func_192023_dk(), getHandle().field_70170_p);

            return (shoulder == null) ? null : shoulder.getBukkitEntity();
        }

        return null;
    }

    @Override
    public void setShoulderEntityLeft(org.bukkit.entity.Entity entity) {
        getHandle().func_192029_h(entity == null ? new NBTTagCompound() : ((CraftEntity) entity).save());
        if (entity != null) {
            entity.remove();
        }
    }

    @Override
    public org.bukkit.entity.Entity getShoulderEntityRight() {
        if (!getHandle().func_192025_dl().func_82582_d()) {
            Entity shoulder = EntityList.func_75615_a(getHandle().func_192025_dl(), getHandle().field_70170_p);

            return (shoulder == null) ? null : shoulder.getBukkitEntity();
        }

        return null;
    }

    @Override
    public void setShoulderEntityRight(org.bukkit.entity.Entity entity) {
        getHandle().func_192031_i(entity == null ? new NBTTagCompound() : ((CraftEntity) entity).save());
        if (entity != null) {
            entity.remove();
        }
    }

    // Paper start - Add method to open already placed sign
    @Override
    public void openSign(org.bukkit.block.Sign sign) {
        org.apache.commons.lang.Validate.isTrue(sign.getWorld().equals(this.getWorld()), "Sign must be in the same world as player is in");
        org.bukkit.craftbukkit.block.CraftSign craftSign = (org.bukkit.craftbukkit.block.CraftSign) sign;
        net.minecraft.tileentity.TileEntitySign teSign = craftSign.getTileEntity();
        // Make sign editable temporarily, will be set back to false in PlayerConnection later
        teSign.field_145916_j = true;

        getHandle().func_175141_a(teSign);
    }
    // Paper end
}
