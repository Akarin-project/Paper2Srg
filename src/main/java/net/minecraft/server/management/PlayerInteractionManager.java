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

    public World field_73092_a;
    public EntityPlayerMP field_73090_b;
    private GameType field_73091_c;
    private boolean field_73088_d;
    private int field_73089_e;
    private BlockPos field_180240_f;
    private int field_73100_i;
    private boolean field_73097_j;
    private BlockPos field_180241_i;
    private int field_73093_n;
    private int field_73094_o;

    public PlayerInteractionManager(World world) {
        this.field_73091_c = GameType.NOT_SET;
        this.field_180240_f = BlockPos.field_177992_a;
        this.field_180241_i = BlockPos.field_177992_a;
        this.field_73094_o = -1;
        this.field_73092_a = world;
    }

    public void func_73076_a(GameType enumgamemode) {
        this.field_73091_c = enumgamemode;
        enumgamemode.func_77147_a(this.field_73090_b.field_71075_bZ);
        this.field_73090_b.func_71016_p();
        this.field_73090_b.field_71133_b.func_184103_al().sendAll(new SPacketPlayerListItem(SPacketPlayerListItem.Action.UPDATE_GAME_MODE, new EntityPlayerMP[] { this.field_73090_b}), this.field_73090_b); // CraftBukkit
        this.field_73092_a.func_72854_c();
    }

    public GameType func_73081_b() {
        return this.field_73091_c;
    }

    public boolean func_180239_c() {
        return this.field_73091_c.func_77144_e();
    }

    public boolean func_73083_d() {
        return this.field_73091_c.func_77145_d();
    }

    public void func_73077_b(GameType enumgamemode) {
        if (this.field_73091_c == GameType.NOT_SET) {
            this.field_73091_c = enumgamemode;
        }

        this.func_73076_a(this.field_73091_c);
    }

    public void func_73075_a() {
        this.field_73100_i = MinecraftServer.currentTick; // CraftBukkit;
        float f;
        int i;

        if (this.field_73097_j) {
            int j = this.field_73100_i - this.field_73093_n;
            IBlockState iblockdata = this.field_73092_a.func_180495_p(this.field_180241_i);

            if (iblockdata.func_185904_a() == Material.field_151579_a) {
                this.field_73097_j = false;
            } else {
                f = iblockdata.func_185903_a((EntityPlayer) this.field_73090_b, this.field_73090_b.field_70170_p, this.field_180241_i) * (float) (j + 1);
                i = (int) (f * 10.0F);
                if (i != this.field_73094_o) {
                    this.field_73092_a.func_175715_c(this.field_73090_b.func_145782_y(), this.field_180241_i, i);
                    this.field_73094_o = i;
                }

                if (f >= 1.0F) {
                    this.field_73097_j = false;
                    this.func_180237_b(this.field_180241_i);
                }
            }
        } else if (this.field_73088_d) {
            IBlockState iblockdata1 = this.field_73092_a.func_180495_p(this.field_180240_f);

            if (iblockdata1.func_185904_a() == Material.field_151579_a) {
                this.field_73092_a.func_175715_c(this.field_73090_b.func_145782_y(), this.field_180240_f, -1);
                this.field_73094_o = -1;
                this.field_73088_d = false;
            } else {
                int k = this.field_73100_i - this.field_73089_e;

                f = iblockdata1.func_185903_a((EntityPlayer) this.field_73090_b, this.field_73090_b.field_70170_p, this.field_180241_i) * (float) (k + 1);
                i = (int) (f * 10.0F);
                if (i != this.field_73094_o) {
                    this.field_73092_a.func_175715_c(this.field_73090_b.func_145782_y(), this.field_180240_f, i);
                    this.field_73094_o = i;
                }
            }
        }

    }

    public void func_180784_a(BlockPos blockposition, EnumFacing enumdirection) {
        // CraftBukkit start
        PlayerInteractEvent event = CraftEventFactory.callPlayerInteractEvent(this.field_73090_b, Action.LEFT_CLICK_BLOCK, blockposition, enumdirection, this.field_73090_b.field_71071_by.func_70448_g(), EnumHand.MAIN_HAND);
        if (event.isCancelled()) {
            // Let the client know the block still exists
            ((EntityPlayerMP) this.field_73090_b).field_71135_a.func_147359_a(new SPacketBlockChange(this.field_73092_a, blockposition));
            // Update any tile entity data for this block
            TileEntity tileentity = this.field_73092_a.func_175625_s(blockposition);
            if (tileentity != null) {
                this.field_73090_b.field_71135_a.func_147359_a(tileentity.func_189518_D_());
            }
            return;
        }
        // CraftBukkit end
        if (this.func_73083_d()) {
            if (!this.field_73092_a.func_175719_a((EntityPlayer) null, blockposition, enumdirection)) {
                this.func_180237_b(blockposition);
            }

        } else {
            IBlockState iblockdata = this.field_73092_a.func_180495_p(blockposition);
            Block block = iblockdata.func_177230_c();

            if (this.field_73091_c.func_82752_c()) {
                if (this.field_73091_c == GameType.SPECTATOR) {
                    return;
                }

                if (!this.field_73090_b.func_175142_cm()) {
                    ItemStack itemstack = this.field_73090_b.func_184614_ca();

                    if (itemstack.func_190926_b()) {
                        return;
                    }

                    if (!itemstack.func_179544_c(block)) {
                        return;
                    }
                }
            }

            // this.world.douseFire((EntityHuman) null, blockposition, enumdirection); // CraftBukkit - Moved down
            this.field_73089_e = this.field_73100_i;
            float f = 1.0F;

            // CraftBukkit start - Swings at air do *NOT* exist.
            if (event.useInteractedBlock() == Event.Result.DENY) {
                // If we denied a door from opening, we need to send a correcting update to the client, as it already opened the door.
                IBlockState data = this.field_73092_a.func_180495_p(blockposition);
                if (block == Blocks.field_180413_ao) {
                    // For some reason *BOTH* the bottom/top part have to be marked updated.
                    boolean bottom = data.func_177229_b(BlockDoor.field_176523_O) == BlockDoor.EnumDoorHalf.LOWER;
                    ((EntityPlayerMP) this.field_73090_b).field_71135_a.func_147359_a(new SPacketBlockChange(this.field_73092_a, blockposition));
                    ((EntityPlayerMP) this.field_73090_b).field_71135_a.func_147359_a(new SPacketBlockChange(this.field_73092_a, bottom ? blockposition.func_177984_a() : blockposition.func_177977_b()));
                } else if (block == Blocks.field_150415_aT) {
                    ((EntityPlayerMP) this.field_73090_b).field_71135_a.func_147359_a(new SPacketBlockChange(this.field_73092_a, blockposition));
                }
            } else if (iblockdata.func_185904_a() != Material.field_151579_a) {
                block.func_180649_a(this.field_73092_a, blockposition, this.field_73090_b);
                f = iblockdata.func_185903_a((EntityPlayer) this.field_73090_b, this.field_73090_b.field_70170_p, blockposition);
                // Allow fire punching to be blocked
                this.field_73092_a.func_175719_a((EntityPlayer) null, blockposition, enumdirection);
            }

            if (event.useItemInHand() == Event.Result.DENY) {
                // If we 'insta destroyed' then the client needs to be informed.
                if (f > 1.0f) {
                    ((EntityPlayerMP) this.field_73090_b).field_71135_a.func_147359_a(new SPacketBlockChange(this.field_73092_a, blockposition));
                }
                return;
            }
            org.bukkit.event.block.BlockDamageEvent blockEvent = CraftEventFactory.callBlockDamageEvent(this.field_73090_b, blockposition.func_177958_n(), blockposition.func_177956_o(), blockposition.func_177952_p(), this.field_73090_b.field_71071_by.func_70448_g(), f >= 1.0f);

            if (blockEvent.isCancelled()) {
                // Let the client know the block still exists
                ((EntityPlayerMP) this.field_73090_b).field_71135_a.func_147359_a(new SPacketBlockChange(this.field_73092_a, blockposition));
                return;
            }

            if (blockEvent.getInstaBreak()) {
                f = 2.0f;
            }
            // CraftBukkit end

            if (iblockdata.func_185904_a() != Material.field_151579_a && f >= 1.0F) {
                this.func_180237_b(blockposition);
            } else {
                this.field_73088_d = true;
                this.field_180240_f = blockposition;
                int i = (int) (f * 10.0F);

                this.field_73092_a.func_175715_c(this.field_73090_b.func_145782_y(), blockposition, i);
                this.field_73094_o = i;
            }

        }

        this.field_73092_a.chunkPacketBlockController.updateNearbyBlocks(this.field_73092_a, blockposition); // Paper - Anti-Xray
    }

    public void func_180785_a(BlockPos blockposition) {
        if (blockposition.equals(this.field_180240_f)) {
            this.field_73100_i = MinecraftServer.currentTick; // CraftBukkit
            int i = this.field_73100_i - this.field_73089_e;
            IBlockState iblockdata = this.field_73092_a.func_180495_p(blockposition);

            if (iblockdata.func_185904_a() != Material.field_151579_a) {
                float f = iblockdata.func_185903_a((EntityPlayer) this.field_73090_b, this.field_73090_b.field_70170_p, blockposition) * (float) (i + 1);

                if (f >= 0.7F) {
                    this.field_73088_d = false;
                    this.field_73092_a.func_175715_c(this.field_73090_b.func_145782_y(), blockposition, -1);
                    this.func_180237_b(blockposition);
                } else if (!this.field_73097_j) {
                    this.field_73088_d = false;
                    this.field_73097_j = true;
                    this.field_180241_i = blockposition;
                    this.field_73093_n = this.field_73089_e;
                }
            }
        // CraftBukkit start - Force block reset to client
        } else {
            this.field_73090_b.field_71135_a.func_147359_a(new SPacketBlockChange(this.field_73092_a, blockposition));
            // CraftBukkit end
        }

    }

    public void func_180238_e() {
        this.field_73088_d = false;
        this.field_73092_a.func_175715_c(this.field_73090_b.func_145782_y(), this.field_180240_f, -1);
    }

    private boolean func_180235_c(BlockPos blockposition) {
        IBlockState iblockdata = this.field_73092_a.func_180495_p(blockposition);

        iblockdata.func_177230_c().func_176208_a(this.field_73092_a, blockposition, iblockdata, (EntityPlayer) this.field_73090_b);
        boolean flag = this.field_73092_a.func_175698_g(blockposition);

        if (flag) {
            iblockdata.func_177230_c().func_176206_d(this.field_73092_a, blockposition, iblockdata);
        }

        return flag;
    }

    public boolean func_180237_b(BlockPos blockposition) {
        // CraftBukkit start - fire BlockBreakEvent
        BlockBreakEvent event = null;

        if (this.field_73090_b instanceof EntityPlayerMP) {
            org.bukkit.block.Block block = this.field_73092_a.getWorld().getBlockAt(blockposition.func_177958_n(), blockposition.func_177956_o(), blockposition.func_177952_p());

            // Sword + Creative mode pre-cancel
            boolean isSwordNoBreak = this.field_73091_c.func_77145_d() && !this.field_73090_b.func_184614_ca().func_190926_b() && this.field_73090_b.func_184614_ca().func_77973_b() instanceof ItemSword;

            // Tell client the block is gone immediately then process events
            // Don't tell the client if its a creative sword break because its not broken!
            if (field_73092_a.func_175625_s(blockposition) == null && !isSwordNoBreak) {
                SPacketBlockChange packet = new SPacketBlockChange(this.field_73092_a, blockposition);
                packet.field_148883_d = Blocks.field_150350_a.func_176223_P();
                ((EntityPlayerMP) this.field_73090_b).field_71135_a.func_147359_a(packet);
            }

            event = new BlockBreakEvent(block, this.field_73090_b.getBukkitEntity());

            // Sword + Creative mode pre-cancel
            event.setCancelled(isSwordNoBreak);

            // Calculate default block experience
            IBlockState nmsData = this.field_73092_a.func_180495_p(blockposition);
            Block nmsBlock = nmsData.func_177230_c();

            ItemStack itemstack = this.field_73090_b.func_184582_a(EntityEquipmentSlot.MAINHAND);

            if (nmsBlock != null && !event.isCancelled() && !this.func_73083_d() && this.field_73090_b.func_184823_b(nmsBlock.func_176223_P())) {
                // Copied from block.a(World world, EntityHuman entityhuman, BlockPosition blockposition, IBlockData iblockdata, @Nullable TileEntity tileentity, ItemStack itemstack)
                // PAIL: checkme each update
                if (!(nmsBlock.func_149652_G() && EnchantmentHelper.func_77506_a(Enchantments.field_185306_r, itemstack) > 0)) {
                    int bonusLevel = EnchantmentHelper.func_77506_a(Enchantments.field_185308_t, itemstack);

                    event.setExpToDrop(nmsBlock.getExpDrop(this.field_73092_a, nmsData, bonusLevel));
                }
            }

            this.field_73092_a.getServer().getPluginManager().callEvent(event);

            if (event.isCancelled()) {
                if (isSwordNoBreak) {
                    return false;
                }
                // Let the client know the block still exists
                ((EntityPlayerMP) this.field_73090_b).field_71135_a.func_147359_a(new SPacketBlockChange(this.field_73092_a, blockposition));
                // Send other half of the door
                if (nmsBlock instanceof BlockDoor) {
                    boolean bottom = nmsData.func_177229_b(BlockDoor.field_176523_O) == BlockDoor.EnumDoorHalf.LOWER;
                    ((EntityPlayerMP) this.field_73090_b).field_71135_a.func_147359_a(new SPacketBlockChange(field_73092_a, bottom ? blockposition.func_177984_a() : blockposition.func_177977_b()));
                }
                // Update any tile entity data for this block
                TileEntity tileentity = this.field_73092_a.func_175625_s(blockposition);
                if (tileentity != null) {
                    this.field_73090_b.field_71135_a.func_147359_a(tileentity.func_189518_D_());
                }
                return false;
            }
        }
        if (false && this.field_73091_c.func_77145_d() && !this.field_73090_b.func_184614_ca().func_190926_b() && this.field_73090_b.func_184614_ca().func_77973_b() instanceof ItemSword) { // CraftBukkit - false
            return false;
        } else {
            IBlockState iblockdata = this.field_73092_a.func_180495_p(blockposition);
            if (iblockdata.func_177230_c() == Blocks.field_150350_a) return false; // CraftBukkit - A plugin set block to air without cancelling
            TileEntity tileentity = this.field_73092_a.func_175625_s(blockposition);
            Block block = iblockdata.func_177230_c();

            // CraftBukkit start - Special case skulls, their item data comes from a tile entity (Also check if block should drop items)
            if (iblockdata.func_177230_c() == Blocks.field_150465_bP && !this.func_73083_d() && event.isDropItems()) {
                iblockdata.func_177230_c().func_180653_a(field_73092_a, blockposition, iblockdata, 1.0F, 0);
                return this.func_180235_c(blockposition);
            }

            // And shulker boxes too for duplication on cancel reasons (Also check if block should drop items)
            if (iblockdata.func_177230_c() instanceof BlockShulkerBox && event.isDropItems()) {
                iblockdata.func_177230_c().func_180653_a(field_73092_a, blockposition, iblockdata, 1.0F, 0);
                return this.func_180235_c(blockposition);
            }
            // CraftBukkit end

            if ((block instanceof BlockCommandBlock || block instanceof BlockStructure) && !this.field_73090_b.func_189808_dh()) {
                this.field_73092_a.func_184138_a(blockposition, iblockdata, iblockdata, 3);
                return false;
            } else {
                if (this.field_73091_c.func_82752_c()) {
                    if (this.field_73091_c == GameType.SPECTATOR) {
                        return false;
                    }

                    if (!this.field_73090_b.func_175142_cm()) {
                        ItemStack itemstack = this.field_73090_b.func_184614_ca();

                        if (itemstack.func_190926_b()) {
                            return false;
                        }

                        if (!itemstack.func_179544_c(block)) {
                            return false;
                        }
                    }
                }

                this.field_73092_a.func_180498_a(this.field_73090_b, 2001, blockposition, Block.func_176210_f(iblockdata));
                // CraftBukkit start
                field_73092_a.captureDrops = new ArrayList<>();
                boolean flag = this.func_180235_c(blockposition);
                if (event.isDropItems()) {
                    for (EntityItem item : field_73092_a.captureDrops) {
                        field_73092_a.func_72838_d(item);
                    }
                }
                field_73092_a.captureDrops = null;
                // CraftBukkit end

                if (this.func_73083_d()) {
                    this.field_73090_b.field_71135_a.func_147359_a(new SPacketBlockChange(this.field_73092_a, blockposition));
                } else {
                    ItemStack itemstack1 = this.field_73090_b.func_184614_ca();
                    ItemStack itemstack2 = itemstack1.func_190926_b() ? ItemStack.field_190927_a : itemstack1.func_77946_l();
                    boolean flag1 = this.field_73090_b.func_184823_b(iblockdata);

                    if (!itemstack1.func_190926_b()) {
                        itemstack1.func_179548_a(this.field_73092_a, iblockdata, blockposition, this.field_73090_b);
                    }

                    // CraftBukkit start - Check if block should drop items
                    if (flag && flag1 && event.isDropItems()) {
                        iblockdata.func_177230_c().func_180657_a(this.field_73092_a, this.field_73090_b, blockposition, iblockdata, tileentity, itemstack2);
                    }
                    // CraftBukkit end
                }

                // CraftBukkit start - Drop event experience
                if (flag && event != null) {
                    iblockdata.func_177230_c().dropExperience(this.field_73092_a, blockposition, event.getExpToDrop(), this.field_73090_b); // Paper
                }
                // CraftBukkit end

                return flag;
            }
        }
    }

    public EnumActionResult func_187250_a(EntityPlayer entityhuman, World world, ItemStack itemstack, EnumHand enumhand) {
        if (this.field_73091_c == GameType.SPECTATOR) {
            return EnumActionResult.PASS;
        } else if (entityhuman.func_184811_cZ().func_185141_a(itemstack.func_77973_b())) {
            return EnumActionResult.PASS;
        } else {
            int i = itemstack.func_190916_E();
            int j = itemstack.func_77960_j();
            ActionResult interactionresultwrapper = itemstack.func_77957_a(world, entityhuman, enumhand);
            ItemStack itemstack1 = (ItemStack) interactionresultwrapper.func_188398_b();

            if (itemstack1 == itemstack && itemstack1.func_190916_E() == i && itemstack1.func_77988_m() <= 0 && itemstack1.func_77960_j() == j) {
                return interactionresultwrapper.func_188397_a();
            } else if (interactionresultwrapper.func_188397_a() == EnumActionResult.FAIL && itemstack1.func_77988_m() > 0 && !entityhuman.func_184587_cr()) {
                return interactionresultwrapper.func_188397_a();
            } else {
                entityhuman.func_184611_a(enumhand, itemstack1);
                if (this.func_73083_d()) {
                    itemstack1.func_190920_e(i);
                    if (itemstack1.func_77984_f()) {
                        itemstack1.func_77964_b(j);
                    }
                }

                if (itemstack1.func_190926_b()) {
                    entityhuman.func_184611_a(enumhand, ItemStack.field_190927_a);
                }

                if (!entityhuman.func_184587_cr()) {
                    ((EntityPlayerMP) entityhuman).func_71120_a(entityhuman.field_71069_bz);
                }

                return interactionresultwrapper.func_188397_a();
            }
        }
    }

    // CraftBukkit start - whole method
    public boolean interactResult = false;
    public boolean firedInteract = false;
    public EnumActionResult func_187251_a(EntityPlayer entityhuman, World world, ItemStack itemstack, EnumHand enumhand, BlockPos blockposition, EnumFacing enumdirection, float f, float f1, float f2) {
        IBlockState blockdata = world.func_180495_p(blockposition);
        EnumActionResult enuminteractionresult = EnumActionResult.FAIL;
        if (blockdata.func_177230_c() != Blocks.field_150350_a) {
            boolean cancelledBlock = false;

            if (this.field_73091_c == GameType.SPECTATOR) {
                TileEntity tileentity = world.func_175625_s(blockposition);
                cancelledBlock = !(tileentity instanceof ILockableContainer || tileentity instanceof IInventory);
            }

            if (entityhuman.func_184811_cZ().func_185141_a(itemstack.func_77973_b())) {
                cancelledBlock = true;
            }

            if (itemstack.func_77973_b() instanceof ItemBlock && !entityhuman.func_189808_dh()) {
                Block block1 = ((ItemBlock) itemstack.func_77973_b()).func_179223_d();

                if (block1 instanceof BlockCommandBlock || block1 instanceof BlockStructure) {
                    cancelledBlock = true;
                }
            }

            PlayerInteractEvent event = CraftEventFactory.callPlayerInteractEvent(entityhuman, Action.RIGHT_CLICK_BLOCK, blockposition, enumdirection, itemstack, cancelledBlock, enumhand);
            firedInteract = true;
            interactResult = event.useItemInHand() == Event.Result.DENY;

            if (event.useInteractedBlock() == Event.Result.DENY) {
                // If we denied a door from opening, we need to send a correcting update to the client, as it already opened the door.
                if (blockdata.func_177230_c() instanceof BlockDoor) {
                    boolean bottom = blockdata.func_177229_b(BlockDoor.field_176523_O) == BlockDoor.EnumDoorHalf.LOWER;
                    ((EntityPlayerMP) entityhuman).field_71135_a.func_147359_a(new SPacketBlockChange(world, bottom ? blockposition.func_177984_a() : blockposition.func_177977_b()));
                } else if (blockdata.func_177230_c() instanceof BlockCake) {
                    ((EntityPlayerMP) entityhuman).getBukkitEntity().sendHealthUpdate(); // SPIGOT-1341 - reset health for cake
                // Paper start - extend Player Interact cancellation to GUIs
                } else if (blockdata.func_177230_c() instanceof BlockStructure) {
                    ((EntityPlayerMP) entityhuman).field_71135_a.func_147359_a(new SPacketCloseWindow());
                } else if (blockdata.func_177230_c() instanceof BlockCommandBlock) {
                    ((EntityPlayerMP) entityhuman).field_71135_a.func_147359_a(new SPacketCloseWindow());
                // Paper end - extend Player Interact cancellation to GUIs
                }
                ((EntityPlayerMP) entityhuman).getBukkitEntity().updateInventory(); // SPIGOT-2867
                enuminteractionresult = (event.useItemInHand() != Event.Result.ALLOW) ? EnumActionResult.SUCCESS : EnumActionResult.PASS;
            } else if (this.field_73091_c == GameType.SPECTATOR) {
                TileEntity tileentity = world.func_175625_s(blockposition);

                if (tileentity instanceof ILockableContainer) {
                    Block block = world.func_180495_p(blockposition).func_177230_c();
                    ILockableContainer itileinventory = (ILockableContainer) tileentity;

                    if (itileinventory instanceof TileEntityChest && block instanceof BlockChest) {
                        itileinventory = ((BlockChest) block).func_180676_d(world, blockposition);
                    }

                    if (itileinventory != null) {
                        entityhuman.func_71007_a(itileinventory);
                        return EnumActionResult.SUCCESS;
                    }
                } else if (tileentity instanceof IInventory) {
                    entityhuman.func_71007_a((IInventory) tileentity);
                    return EnumActionResult.SUCCESS;
                }

                return EnumActionResult.PASS;
            } else {
                if (!entityhuman.func_70093_af() || entityhuman.func_184614_ca().func_190926_b() && entityhuman.func_184592_cb().func_190926_b()) {
                    IBlockState iblockdata = world.func_180495_p(blockposition);

                    enuminteractionresult = iblockdata.func_177230_c().func_180639_a(world, blockposition, iblockdata, entityhuman, enumhand, enumdirection, f, f1, f2) ? EnumActionResult.SUCCESS : EnumActionResult.PASS;
                }
            }

            if (!itemstack.func_190926_b() && enuminteractionresult != EnumActionResult.SUCCESS && !interactResult) { // add !interactResult SPIGOT-764
                int i = itemstack.func_77960_j();
                int j = itemstack.func_190916_E();

                enuminteractionresult = itemstack.func_179546_a(entityhuman, world, blockposition, enumhand, enumdirection, f, f1, f2);

                // The item count should not decrement in Creative mode.
                if (this.func_73083_d()) {
                    itemstack.func_77964_b(i);
                    itemstack.func_190920_e(j);
                }
            }
        }
        return enuminteractionresult;
        // CraftBukkit end
    }

    public void func_73080_a(WorldServer worldserver) {
        this.field_73092_a = worldserver;
    }
}
