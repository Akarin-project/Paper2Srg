package net.minecraft.server.management;

// CraftBukkit start
import java.util.ArrayList;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.event.Event;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCake;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockCommandBlock;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockShulkerBox;
import net.minecraft.block.BlockStructure;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.server.SPacketBlockChange;
import net.minecraft.network.play.server.SPacketCloseWindow;
import net.minecraft.network.play.server.SPacketPlayerListItem;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameType;
import net.minecraft.world.ILockableContainer;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

// CraftBukkit end

public class PlayerInteractionManager {

    public World world;
    public EntityPlayerMP player;
    private GameType gameType;
    private boolean isDestroyingBlock;
    private int initialDamage;
    private BlockPos destroyPos;
    private int curblockDamage;
    private boolean receivedFinishDiggingPacket;
    private BlockPos delayedDestroyPos;
    private int initialBlockDamage;
    private int durabilityRemainingOnBlock;

    public PlayerInteractionManager(World world) {
        this.gameType = GameType.NOT_SET;
        this.destroyPos = BlockPos.ORIGIN;
        this.delayedDestroyPos = BlockPos.ORIGIN;
        this.durabilityRemainingOnBlock = -1;
        this.world = world;
    }

    public void setGameType(GameType enumgamemode) {
        this.gameType = enumgamemode;
        enumgamemode.configurePlayerCapabilities(this.player.capabilities);
        this.player.sendPlayerAbilities();
        this.player.mcServer.getPlayerList().sendAll(new SPacketPlayerListItem(SPacketPlayerListItem.Action.UPDATE_GAME_MODE, new EntityPlayerMP[] { this.player}), this.player); // CraftBukkit
        this.world.updateAllPlayersSleepingFlag();
    }

    public GameType getGameType() {
        return this.gameType;
    }

    public boolean survivalOrAdventure() {
        return this.gameType.isSurvivalOrAdventure();
    }

    public boolean isCreative() {
        return this.gameType.isCreative();
    }

    public void initializeGameType(GameType enumgamemode) {
        if (this.gameType == GameType.NOT_SET) {
            this.gameType = enumgamemode;
        }

        this.setGameType(this.gameType);
    }

    public void updateBlockRemoving() {
        this.curblockDamage = MinecraftServer.currentTick; // CraftBukkit;
        float f;
        int i;

        if (this.receivedFinishDiggingPacket) {
            int j = this.curblockDamage - this.initialBlockDamage;
            IBlockState iblockdata = this.world.getBlockState(this.delayedDestroyPos);

            if (iblockdata.getMaterial() == Material.AIR) {
                this.receivedFinishDiggingPacket = false;
            } else {
                f = iblockdata.getPlayerRelativeBlockHardness((EntityPlayer) this.player, this.player.world, this.delayedDestroyPos) * (float) (j + 1);
                i = (int) (f * 10.0F);
                if (i != this.durabilityRemainingOnBlock) {
                    this.world.sendBlockBreakProgress(this.player.getEntityId(), this.delayedDestroyPos, i);
                    this.durabilityRemainingOnBlock = i;
                }

                if (f >= 1.0F) {
                    this.receivedFinishDiggingPacket = false;
                    this.tryHarvestBlock(this.delayedDestroyPos);
                }
            }
        } else if (this.isDestroyingBlock) {
            IBlockState iblockdata1 = this.world.getBlockState(this.destroyPos);

            if (iblockdata1.getMaterial() == Material.AIR) {
                this.world.sendBlockBreakProgress(this.player.getEntityId(), this.destroyPos, -1);
                this.durabilityRemainingOnBlock = -1;
                this.isDestroyingBlock = false;
            } else {
                int k = this.curblockDamage - this.initialDamage;

                f = iblockdata1.getPlayerRelativeBlockHardness((EntityPlayer) this.player, this.player.world, this.delayedDestroyPos) * (float) (k + 1);
                i = (int) (f * 10.0F);
                if (i != this.durabilityRemainingOnBlock) {
                    this.world.sendBlockBreakProgress(this.player.getEntityId(), this.destroyPos, i);
                    this.durabilityRemainingOnBlock = i;
                }
            }
        }

    }

    public void onBlockClicked(BlockPos blockposition, EnumFacing enumdirection) {
        // CraftBukkit start
        PlayerInteractEvent event = CraftEventFactory.callPlayerInteractEvent(this.player, Action.LEFT_CLICK_BLOCK, blockposition, enumdirection, this.player.inventory.getCurrentItem(), EnumHand.MAIN_HAND);
        if (event.isCancelled()) {
            // Let the client know the block still exists
            ((EntityPlayerMP) this.player).connection.sendPacket(new SPacketBlockChange(this.world, blockposition));
            // Update any tile entity data for this block
            TileEntity tileentity = this.world.getTileEntity(blockposition);
            if (tileentity != null) {
                this.player.connection.sendPacket(tileentity.getUpdatePacket());
            }
            return;
        }
        // CraftBukkit end
        if (this.isCreative()) {
            if (!this.world.extinguishFire((EntityPlayer) null, blockposition, enumdirection)) {
                this.tryHarvestBlock(blockposition);
            }

        } else {
            IBlockState iblockdata = this.world.getBlockState(blockposition);
            Block block = iblockdata.getBlock();

            if (this.gameType.hasLimitedInteractions()) {
                if (this.gameType == GameType.SPECTATOR) {
                    return;
                }

                if (!this.player.isAllowEdit()) {
                    ItemStack itemstack = this.player.getHeldItemMainhand();

                    if (itemstack.isEmpty()) {
                        return;
                    }

                    if (!itemstack.canDestroy(block)) {
                        return;
                    }
                }
            }

            // this.world.douseFire((EntityHuman) null, blockposition, enumdirection); // CraftBukkit - Moved down
            this.initialDamage = this.curblockDamage;
            float f = 1.0F;

            // CraftBukkit start - Swings at air do *NOT* exist.
            if (event.useInteractedBlock() == Event.Result.DENY) {
                // If we denied a door from opening, we need to send a correcting update to the client, as it already opened the door.
                IBlockState data = this.world.getBlockState(blockposition);
                if (block == Blocks.OAK_DOOR) {
                    // For some reason *BOTH* the bottom/top part have to be marked updated.
                    boolean bottom = data.getValue(BlockDoor.HALF) == BlockDoor.EnumDoorHalf.LOWER;
                    ((EntityPlayerMP) this.player).connection.sendPacket(new SPacketBlockChange(this.world, blockposition));
                    ((EntityPlayerMP) this.player).connection.sendPacket(new SPacketBlockChange(this.world, bottom ? blockposition.up() : blockposition.down()));
                } else if (block == Blocks.TRAPDOOR) {
                    ((EntityPlayerMP) this.player).connection.sendPacket(new SPacketBlockChange(this.world, blockposition));
                }
            } else if (iblockdata.getMaterial() != Material.AIR) {
                block.onBlockClicked(this.world, blockposition, this.player);
                f = iblockdata.getPlayerRelativeBlockHardness((EntityPlayer) this.player, this.player.world, blockposition);
                // Allow fire punching to be blocked
                this.world.extinguishFire((EntityPlayer) null, blockposition, enumdirection);
            }

            if (event.useItemInHand() == Event.Result.DENY) {
                // If we 'insta destroyed' then the client needs to be informed.
                if (f > 1.0f) {
                    ((EntityPlayerMP) this.player).connection.sendPacket(new SPacketBlockChange(this.world, blockposition));
                }
                return;
            }
            org.bukkit.event.block.BlockDamageEvent blockEvent = CraftEventFactory.callBlockDamageEvent(this.player, blockposition.getX(), blockposition.getY(), blockposition.getZ(), this.player.inventory.getCurrentItem(), f >= 1.0f);

            if (blockEvent.isCancelled()) {
                // Let the client know the block still exists
                ((EntityPlayerMP) this.player).connection.sendPacket(new SPacketBlockChange(this.world, blockposition));
                return;
            }

            if (blockEvent.getInstaBreak()) {
                f = 2.0f;
            }
            // CraftBukkit end

            if (iblockdata.getMaterial() != Material.AIR && f >= 1.0F) {
                this.tryHarvestBlock(blockposition);
            } else {
                this.isDestroyingBlock = true;
                this.destroyPos = blockposition;
                int i = (int) (f * 10.0F);

                this.world.sendBlockBreakProgress(this.player.getEntityId(), blockposition, i);
                this.durabilityRemainingOnBlock = i;
            }

        }

        this.world.chunkPacketBlockController.updateNearbyBlocks(this.world, blockposition); // Paper - Anti-Xray
    }

    public void blockRemoving(BlockPos blockposition) {
        if (blockposition.equals(this.destroyPos)) {
            this.curblockDamage = MinecraftServer.currentTick; // CraftBukkit
            int i = this.curblockDamage - this.initialDamage;
            IBlockState iblockdata = this.world.getBlockState(blockposition);

            if (iblockdata.getMaterial() != Material.AIR) {
                float f = iblockdata.getPlayerRelativeBlockHardness((EntityPlayer) this.player, this.player.world, blockposition) * (float) (i + 1);

                if (f >= 0.7F) {
                    this.isDestroyingBlock = false;
                    this.world.sendBlockBreakProgress(this.player.getEntityId(), blockposition, -1);
                    this.tryHarvestBlock(blockposition);
                } else if (!this.receivedFinishDiggingPacket) {
                    this.isDestroyingBlock = false;
                    this.receivedFinishDiggingPacket = true;
                    this.delayedDestroyPos = blockposition;
                    this.initialBlockDamage = this.initialDamage;
                }
            }
        // CraftBukkit start - Force block reset to client
        } else {
            this.player.connection.sendPacket(new SPacketBlockChange(this.world, blockposition));
            // CraftBukkit end
        }

    }

    public void cancelDestroyingBlock() {
        this.isDestroyingBlock = false;
        this.world.sendBlockBreakProgress(this.player.getEntityId(), this.destroyPos, -1);
    }

    private boolean removeBlock(BlockPos blockposition) {
        IBlockState iblockdata = this.world.getBlockState(blockposition);

        iblockdata.getBlock().onBlockHarvested(this.world, blockposition, iblockdata, (EntityPlayer) this.player);
        boolean flag = this.world.setBlockToAir(blockposition);

        if (flag) {
            iblockdata.getBlock().onBlockDestroyedByPlayer(this.world, blockposition, iblockdata);
        }

        return flag;
    }

    public boolean tryHarvestBlock(BlockPos blockposition) {
        // CraftBukkit start - fire BlockBreakEvent
        BlockBreakEvent event = null;

        if (this.player instanceof EntityPlayerMP) {
            org.bukkit.block.Block block = this.world.getWorld().getBlockAt(blockposition.getX(), blockposition.getY(), blockposition.getZ());

            // Sword + Creative mode pre-cancel
            boolean isSwordNoBreak = this.gameType.isCreative() && !this.player.getHeldItemMainhand().isEmpty() && this.player.getHeldItemMainhand().getItem() instanceof ItemSword;

            // Tell client the block is gone immediately then process events
            // Don't tell the client if its a creative sword break because its not broken!
            if (world.getTileEntity(blockposition) == null && !isSwordNoBreak) {
                SPacketBlockChange packet = new SPacketBlockChange(this.world, blockposition);
                packet.blockState = Blocks.AIR.getDefaultState();
                ((EntityPlayerMP) this.player).connection.sendPacket(packet);
            }

            event = new BlockBreakEvent(block, this.player.getBukkitEntity());

            // Sword + Creative mode pre-cancel
            event.setCancelled(isSwordNoBreak);

            // Calculate default block experience
            IBlockState nmsData = this.world.getBlockState(blockposition);
            Block nmsBlock = nmsData.getBlock();

            ItemStack itemstack = this.player.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND);

            if (nmsBlock != null && !event.isCancelled() && !this.isCreative() && this.player.canHarvestBlock(nmsBlock.getDefaultState())) {
                // Copied from block.a(World world, EntityHuman entityhuman, BlockPosition blockposition, IBlockData iblockdata, @Nullable TileEntity tileentity, ItemStack itemstack)
                // PAIL: checkme each update
                if (!(nmsBlock.getEnableStats() && EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, itemstack) > 0)) {
                    int bonusLevel = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, itemstack);

                    event.setExpToDrop(nmsBlock.getExpDrop(this.world, nmsData, bonusLevel));
                }
            }

            this.world.getServer().getPluginManager().callEvent(event);

            if (event.isCancelled()) {
                if (isSwordNoBreak) {
                    return false;
                }
                // Let the client know the block still exists
                ((EntityPlayerMP) this.player).connection.sendPacket(new SPacketBlockChange(this.world, blockposition));
                // Send other half of the door
                if (nmsBlock instanceof BlockDoor) {
                    boolean bottom = nmsData.getValue(BlockDoor.HALF) == BlockDoor.EnumDoorHalf.LOWER;
                    ((EntityPlayerMP) this.player).connection.sendPacket(new SPacketBlockChange(world, bottom ? blockposition.up() : blockposition.down()));
                }
                // Update any tile entity data for this block
                TileEntity tileentity = this.world.getTileEntity(blockposition);
                if (tileentity != null) {
                    this.player.connection.sendPacket(tileentity.getUpdatePacket());
                }
                return false;
            }
        }
        if (false && this.gameType.isCreative() && !this.player.getHeldItemMainhand().isEmpty() && this.player.getHeldItemMainhand().getItem() instanceof ItemSword) { // CraftBukkit - false
            return false;
        } else {
            IBlockState iblockdata = this.world.getBlockState(blockposition);
            if (iblockdata.getBlock() == Blocks.AIR) return false; // CraftBukkit - A plugin set block to air without cancelling
            TileEntity tileentity = this.world.getTileEntity(blockposition);
            Block block = iblockdata.getBlock();

            // CraftBukkit start - Special case skulls, their item data comes from a tile entity (Also check if block should drop items)
            if (iblockdata.getBlock() == Blocks.SKULL && !this.isCreative() && event.isDropItems()) {
                iblockdata.getBlock().dropBlockAsItemWithChance(world, blockposition, iblockdata, 1.0F, 0);
                return this.removeBlock(blockposition);
            }

            // And shulker boxes too for duplication on cancel reasons (Also check if block should drop items)
            if (iblockdata.getBlock() instanceof BlockShulkerBox && event.isDropItems()) {
                iblockdata.getBlock().dropBlockAsItemWithChance(world, blockposition, iblockdata, 1.0F, 0);
                return this.removeBlock(blockposition);
            }
            // CraftBukkit end

            if ((block instanceof BlockCommandBlock || block instanceof BlockStructure) && !this.player.canUseCommandBlock()) {
                this.world.notifyBlockUpdate(blockposition, iblockdata, iblockdata, 3);
                return false;
            } else {
                if (this.gameType.hasLimitedInteractions()) {
                    if (this.gameType == GameType.SPECTATOR) {
                        return false;
                    }

                    if (!this.player.isAllowEdit()) {
                        ItemStack itemstack = this.player.getHeldItemMainhand();

                        if (itemstack.isEmpty()) {
                            return false;
                        }

                        if (!itemstack.canDestroy(block)) {
                            return false;
                        }
                    }
                }

                this.world.playEvent(this.player, 2001, blockposition, Block.getStateId(iblockdata));
                // CraftBukkit start
                world.captureDrops = new ArrayList<>();
                boolean flag = this.removeBlock(blockposition);
                if (event.isDropItems()) {
                    for (EntityItem item : world.captureDrops) {
                        world.spawnEntity(item);
                    }
                }
                world.captureDrops = null;
                // CraftBukkit end

                if (this.isCreative()) {
                    this.player.connection.sendPacket(new SPacketBlockChange(this.world, blockposition));
                } else {
                    ItemStack itemstack1 = this.player.getHeldItemMainhand();
                    ItemStack itemstack2 = itemstack1.isEmpty() ? ItemStack.EMPTY : itemstack1.copy();
                    boolean flag1 = this.player.canHarvestBlock(iblockdata);

                    if (!itemstack1.isEmpty()) {
                        itemstack1.onBlockDestroyed(this.world, iblockdata, blockposition, this.player);
                    }

                    // CraftBukkit start - Check if block should drop items
                    if (flag && flag1 && event.isDropItems()) {
                        iblockdata.getBlock().harvestBlock(this.world, this.player, blockposition, iblockdata, tileentity, itemstack2);
                    }
                    // CraftBukkit end
                }

                // CraftBukkit start - Drop event experience
                if (flag && event != null) {
                    iblockdata.getBlock().dropExperience(this.world, blockposition, event.getExpToDrop(), this.player); // Paper
                }
                // CraftBukkit end

                return flag;
            }
        }
    }

    public EnumActionResult processRightClick(EntityPlayer entityhuman, World world, ItemStack itemstack, EnumHand enumhand) {
        if (this.gameType == GameType.SPECTATOR) {
            return EnumActionResult.PASS;
        } else if (entityhuman.getCooldownTracker().hasCooldown(itemstack.getItem())) {
            return EnumActionResult.PASS;
        } else {
            int i = itemstack.getCount();
            int j = itemstack.getMetadata();
            ActionResult interactionresultwrapper = itemstack.useItemRightClick(world, entityhuman, enumhand);
            ItemStack itemstack1 = (ItemStack) interactionresultwrapper.getResult();

            if (itemstack1 == itemstack && itemstack1.getCount() == i && itemstack1.getMaxItemUseDuration() <= 0 && itemstack1.getMetadata() == j) {
                return interactionresultwrapper.getType();
            } else if (interactionresultwrapper.getType() == EnumActionResult.FAIL && itemstack1.getMaxItemUseDuration() > 0 && !entityhuman.isHandActive()) {
                return interactionresultwrapper.getType();
            } else {
                entityhuman.setHeldItem(enumhand, itemstack1);
                if (this.isCreative()) {
                    itemstack1.setCount(i);
                    if (itemstack1.isItemStackDamageable()) {
                        itemstack1.setItemDamage(j);
                    }
                }

                if (itemstack1.isEmpty()) {
                    entityhuman.setHeldItem(enumhand, ItemStack.EMPTY);
                }

                if (!entityhuman.isHandActive()) {
                    ((EntityPlayerMP) entityhuman).sendContainerToPlayer(entityhuman.inventoryContainer);
                }

                return interactionresultwrapper.getType();
            }
        }
    }

    // CraftBukkit start - whole method
    public boolean interactResult = false;
    public boolean firedInteract = false;
    public EnumActionResult processRightClickBlock(EntityPlayer entityhuman, World world, ItemStack itemstack, EnumHand enumhand, BlockPos blockposition, EnumFacing enumdirection, float f, float f1, float f2) {
        IBlockState blockdata = world.getBlockState(blockposition);
        EnumActionResult enuminteractionresult = EnumActionResult.FAIL;
        if (blockdata.getBlock() != Blocks.AIR) {
            boolean cancelledBlock = false;

            if (this.gameType == GameType.SPECTATOR) {
                TileEntity tileentity = world.getTileEntity(blockposition);
                cancelledBlock = !(tileentity instanceof ILockableContainer || tileentity instanceof IInventory);
            }

            if (entityhuman.getCooldownTracker().hasCooldown(itemstack.getItem())) {
                cancelledBlock = true;
            }

            if (itemstack.getItem() instanceof ItemBlock && !entityhuman.canUseCommandBlock()) {
                Block block1 = ((ItemBlock) itemstack.getItem()).getBlock();

                if (block1 instanceof BlockCommandBlock || block1 instanceof BlockStructure) {
                    cancelledBlock = true;
                }
            }

            PlayerInteractEvent event = CraftEventFactory.callPlayerInteractEvent(entityhuman, Action.RIGHT_CLICK_BLOCK, blockposition, enumdirection, itemstack, cancelledBlock, enumhand);
            firedInteract = true;
            interactResult = event.useItemInHand() == Event.Result.DENY;

            if (event.useInteractedBlock() == Event.Result.DENY) {
                // If we denied a door from opening, we need to send a correcting update to the client, as it already opened the door.
                if (blockdata.getBlock() instanceof BlockDoor) {
                    boolean bottom = blockdata.getValue(BlockDoor.HALF) == BlockDoor.EnumDoorHalf.LOWER;
                    ((EntityPlayerMP) entityhuman).connection.sendPacket(new SPacketBlockChange(world, bottom ? blockposition.up() : blockposition.down()));
                } else if (blockdata.getBlock() instanceof BlockCake) {
                    ((EntityPlayerMP) entityhuman).getBukkitEntity().sendHealthUpdate(); // SPIGOT-1341 - reset health for cake
                // Paper start - extend Player Interact cancellation to GUIs
                } else if (blockdata.getBlock() instanceof BlockStructure) {
                    ((EntityPlayerMP) entityhuman).connection.sendPacket(new SPacketCloseWindow());
                } else if (blockdata.getBlock() instanceof BlockCommandBlock) {
                    ((EntityPlayerMP) entityhuman).connection.sendPacket(new SPacketCloseWindow());
                // Paper end - extend Player Interact cancellation to GUIs
                }
                ((EntityPlayerMP) entityhuman).getBukkitEntity().updateInventory(); // SPIGOT-2867
                enuminteractionresult = (event.useItemInHand() != Event.Result.ALLOW) ? EnumActionResult.SUCCESS : EnumActionResult.PASS;
            } else if (this.gameType == GameType.SPECTATOR) {
                TileEntity tileentity = world.getTileEntity(blockposition);

                if (tileentity instanceof ILockableContainer) {
                    Block block = world.getBlockState(blockposition).getBlock();
                    ILockableContainer itileinventory = (ILockableContainer) tileentity;

                    if (itileinventory instanceof TileEntityChest && block instanceof BlockChest) {
                        itileinventory = ((BlockChest) block).getLockableContainer(world, blockposition);
                    }

                    if (itileinventory != null) {
                        entityhuman.displayGUIChest(itileinventory);
                        return EnumActionResult.SUCCESS;
                    }
                } else if (tileentity instanceof IInventory) {
                    entityhuman.displayGUIChest((IInventory) tileentity);
                    return EnumActionResult.SUCCESS;
                }

                return EnumActionResult.PASS;
            } else {
                if (!entityhuman.isSneaking() || entityhuman.getHeldItemMainhand().isEmpty() && entityhuman.getHeldItemOffhand().isEmpty()) {
                    IBlockState iblockdata = world.getBlockState(blockposition);

                    enuminteractionresult = iblockdata.getBlock().onBlockActivated(world, blockposition, iblockdata, entityhuman, enumhand, enumdirection, f, f1, f2) ? EnumActionResult.SUCCESS : EnumActionResult.PASS;
                }
            }

            if (!itemstack.isEmpty() && enuminteractionresult != EnumActionResult.SUCCESS && !interactResult) { // add !interactResult SPIGOT-764
                int i = itemstack.getMetadata();
                int j = itemstack.getCount();

                enuminteractionresult = itemstack.onItemUse(entityhuman, world, blockposition, enumhand, enumdirection, f, f1, f2);

                // The item count should not decrement in Creative mode.
                if (this.isCreative()) {
                    itemstack.setItemDamage(i);
                    itemstack.setCount(j);
                }
            }
        }
        return enuminteractionresult;
        // CraftBukkit end
    }

    public void setWorld(WorldServer worldserver) {
        this.world = worldserver;
    }
}
