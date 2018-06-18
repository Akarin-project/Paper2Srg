package net.minecraft.item;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import java.text.DecimalFormat;
import java.util.Random;
import javax.annotation.Nullable;

import java.util.List;
import java.util.Map;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockJukebox;
import net.minecraft.block.BlockMushroom;
import net.minecraft.block.BlockSapling;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentDurability;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.IDataWalker;
import net.minecraft.util.datafix.walkers.BlockEntityTag;
import net.minecraft.util.datafix.walkers.EntityTag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import org.bukkit.Location;
import org.bukkit.TreeType;
import org.bukkit.block.BlockState;
import org.bukkit.craftbukkit.block.CraftBlockState;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;
import org.bukkit.entity.Player;
import org.bukkit.event.world.StructureGrowEvent;

// CraftBukkit start
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.TreeType;
import org.bukkit.block.BlockState;
import org.bukkit.craftbukkit.block.CraftBlockState;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;
import org.bukkit.entity.Player;
import org.bukkit.event.world.StructureGrowEvent;
// CraftBukkit end

public final class ItemStack {

    public static final ItemStack EMPTY = new ItemStack((Item) null);
    public static final DecimalFormat DECIMALFORMAT = new DecimalFormat("#.##");
    private int stackSize;
    private int animationsToGo;
    private Item item;
    private NBTTagCompound stackTagCompound;
    private boolean isEmpty;
    private int itemDamage; public void setDamage(int i) { this.itemDamage = i; } // Paper - OBFHELPER
    private EntityItemFrame itemFrame;
    private Block canDestroyCacheBlock;
    private boolean canDestroyCacheResult;
    private Block canPlaceOnCacheBlock;
    private boolean canPlaceOnCacheResult;

    public ItemStack(Block block) {
        this(block, 1);
    }

    public ItemStack(Block block, int i) {
        this(block, i, 0);
    }

    public ItemStack(Block block, int i, int j) {
        this(Item.getItemFromBlock(block), i, j);
    }

    public ItemStack(Item item) {
        this(item, 1);
    }

    public ItemStack(Item item, int i) {
        this(item, i, 0);
    }

    // CraftBukkit start
    // Paper start
    private static final java.util.Comparator<NBTTagCompound> enchantSorter = java.util.Comparator.comparingInt(o -> o.getShort("id"));
    private void processEnchantOrder(NBTTagCompound tag) {
        if (tag == null || !tag.hasKey("ench", 9)) {
            return;
        }
        NBTTagList list = tag.getTagList("ench", 10);
        if (list.tagCount() < 2) {
            return;
        }
        try {
            list.sort(enchantSorter); // Paper
        } catch (Exception ignored) {}
    }
    // Paper end

    public ItemStack(Item item, int i, int j) {
        this(item, i, j, true);
    }

    public ItemStack(Item item, int i, int j, boolean convert) {
        // CraftBukkit end
        this.item = item;
        this.itemDamage = j;
        this.stackSize = i;
        // CraftBukkit start - Pass to setData to do filtering
        if (MinecraftServer.getServer() != null) {
            this.setItemDamage(j);
        }
        if (convert) {
            this.convertStack();
        }
        // CraftBukkit end
        if (this.itemDamage < 0) {
            // this.damage = 0; // CraftBukkit - remove this.
        }

        this.updateEmptyState();
    }

    // Called to run this stack through the data converter to handle older storage methods and serialized items
    public void convertStack() {
        if (MinecraftServer.getServer() != null) {
            // Don't convert beds - both the old and new data values are valid
            // Conversion would make getting white beds (data value 0) impossible
            if (this.item == Items.BED) {
                return;
            }

            NBTTagCompound savedStack = new NBTTagCompound();
            this.writeToNBT(savedStack);
            MinecraftServer.getServer().dataFixer.process(FixTypes.ITEM_INSTANCE, savedStack); // PAIL: convert
            this.load(savedStack);
        }
    }

    private void updateEmptyState() {
        if (this.isEmpty && this == ItemStack.EMPTY) throw new AssertionError("TRAP"); // CraftBukkit
        this.isEmpty = this.isEmpty();
    }

    // CraftBukkit - break into own method
    public void load(NBTTagCompound nbttagcompound) {
        this.item = nbttagcompound.hasKey("id", 8) ? Item.getByNameOrId(nbttagcompound.getString("id")) : Item.getItemFromBlock(Blocks.AIR); // Paper - fix NumberFormatException caused by attempting to read an EMPTY ItemStack
        this.stackSize = nbttagcompound.getByte("Count");
        // CraftBukkit start - Route through setData for filtering
        // this.damage = Math.max(0, nbttagcompound.getShort("Damage"));
        this.setItemDamage(nbttagcompound.getShort("Damage"));
        // CraftBukkit end

        if (nbttagcompound.hasKey("tag", 10)) {
            // CraftBukkit start - make defensive copy as this data may be coming from the save thread
            this.stackTagCompound = (NBTTagCompound) nbttagcompound.getCompoundTag("tag").clone();
            processEnchantOrder(this.stackTagCompound); // Paper
            if (this.item != null) {
                this.item.updateItemStackNBT(this.stackTagCompound);
                // CraftBukkit end
            }
        }
    }

    public ItemStack(NBTTagCompound nbttagcompound) {
        this.load(nbttagcompound);
        // CraftBukkit end
        this.updateEmptyState();
    }

    // Paper start - optimize isEmpty
    private static Item airItem;
    public boolean isEmpty() {
        if (airItem == null) {
            airItem = Item.REGISTRY.getObject(new ResourceLocation("air"));
        }
        return this == ItemStack.EMPTY || this.item == null || this.item == airItem || (this.stackSize <= 0 || (this.itemDamage < -32768 || this.itemDamage > '\uffff'));
    }
    // Paper end

    public static void registerFixes(DataFixer dataconvertermanager) {
        dataconvertermanager.registerWalker(FixTypes.ITEM_INSTANCE, (IDataWalker) (new BlockEntityTag()));
        dataconvertermanager.registerWalker(FixTypes.ITEM_INSTANCE, (IDataWalker) (new EntityTag()));
    }

    public ItemStack splitStack(int i) {
        int j = Math.min(i, this.stackSize);
        ItemStack itemstack = this.copy();

        itemstack.setCount(j);
        this.shrink(j);
        return itemstack;
    }

    public Item getItem() {
        return this.isEmpty ? Item.getItemFromBlock(Blocks.AIR) : this.item;
    }

    public EnumActionResult onItemUse(EntityPlayer entityhuman, World world, BlockPos blockposition, EnumHand enumhand, EnumFacing enumdirection, float f, float f1, float f2) {
        // CraftBukkit start - handle all block place event logic here
        int oldData = this.getMetadata();
        int oldCount = this.getCount();

        if (!(this.getItem() instanceof ItemBucket)) { // if not bucket
            world.captureBlockStates = true;
            // special case bonemeal
            if (this.getItem() instanceof ItemDye && this.getMetadata() == 15) {
                Block block = world.getBlockState(blockposition).getBlock();
                if (block == Blocks.SAPLING || block instanceof BlockMushroom) {
                    world.captureTreeGeneration = true;
                }
            }
        }
        EnumActionResult enuminteractionresult = this.getItem().onItemUse(entityhuman, world, blockposition, enumhand, enumdirection, f, f1, f2);
        int newData = this.getMetadata();
        int newCount = this.getCount();
        this.setCount(oldCount);
        this.setItemDamage(oldData);
        world.captureBlockStates = false;
        if (enuminteractionresult == EnumActionResult.SUCCESS && world.captureTreeGeneration && world.capturedBlockStates.size() > 0) {
            world.captureTreeGeneration = false;
            Location location = new Location(world.getWorld(), blockposition.getX(), blockposition.getY(), blockposition.getZ());
            TreeType treeType = BlockSapling.treeType;
            BlockSapling.treeType = null;
            List<BlockState> blocks = (List<BlockState>) world.capturedBlockStates.clone();
            world.capturedBlockStates.clear();
            StructureGrowEvent event = null;
            if (treeType != null) {
                boolean isBonemeal = getItem() == Items.DYE && oldData == 15;
                event = new StructureGrowEvent(location, treeType, isBonemeal, (Player) entityhuman.getBukkitEntity(), blocks);
                org.bukkit.Bukkit.getPluginManager().callEvent(event);
            }
            if (event == null || !event.isCancelled()) {
                // Change the stack to its new contents if it hasn't been tampered with.
                if (this.getCount() == oldCount && this.getMetadata() == oldData) {
                    this.setItemDamage(newData);
                    this.setCount(newCount);
                }
                for (BlockState blockstate : blocks) {
                    blockstate.update(true);
                }
            }

            return enuminteractionresult;
        }
        world.captureTreeGeneration = false;

        if (enuminteractionresult == EnumActionResult.SUCCESS) {
            org.bukkit.event.block.BlockPlaceEvent placeEvent = null;
            List<BlockState> blocks = (List<BlockState>) world.capturedBlockStates.clone();
            world.capturedBlockStates.clear();
            if (blocks.size() > 1) {
                placeEvent = org.bukkit.craftbukkit.event.CraftEventFactory.callBlockMultiPlaceEvent(world, entityhuman, enumhand, blocks, blockposition.getX(), blockposition.getY(), blockposition.getZ());
            } else if (blocks.size() == 1) {
                placeEvent = org.bukkit.craftbukkit.event.CraftEventFactory.callBlockPlaceEvent(world, entityhuman, enumhand, blocks.get(0), blockposition.getX(), blockposition.getY(), blockposition.getZ());
            }

            if (placeEvent != null && (placeEvent.isCancelled() || !placeEvent.canBuild())) {
                enuminteractionresult = EnumActionResult.FAIL; // cancel placement
                // PAIL: Remove this when MC-99075 fixed
                placeEvent.getPlayer().updateInventory();
                // revert back all captured blocks
                for (BlockState blockstate : blocks) {
                    blockstate.update(true, false);
                }
            } else {
                // Change the stack to its new contents if it hasn't been tampered with.
                if (this.getCount() == oldCount && this.getMetadata() == oldData) {
                    this.setItemDamage(newData);
                    this.setCount(newCount);
                }

                for (Map.Entry<BlockPos, TileEntity> e : world.capturedTileEntities.entrySet()) {
                    world.setTileEntity(e.getKey(), e.getValue());
                }

                for (BlockState blockstate : blocks) {
                    int x = blockstate.getX();
                    int y = blockstate.getY();
                    int z = blockstate.getZ();
                    int updateFlag = ((CraftBlockState) blockstate).getFlag();
                    org.bukkit.Material mat = blockstate.getType();
                    Block oldBlock = CraftMagicNumbers.getBlock(mat);
                    BlockPos newblockposition = new BlockPos(x, y, z);
                    IBlockState block = world.getBlockState(newblockposition);

                    if (!(block.getBlock() instanceof BlockContainer)) { // Containers get placed automatically
                        block.getBlock().onBlockAdded(world, newblockposition, block);
                    }

                    world.notifyAndUpdatePhysics(newblockposition, null, oldBlock.getDefaultState(), block, updateFlag); // send null chunk as chunk.k() returns false by this point
                }

                // Special case juke boxes as they update their tile entity. Copied from ItemRecord.
                // PAIL: checkme on updates.
                if (this.item instanceof ItemRecord) {
                    ((BlockJukebox) Blocks.JUKEBOX).insertRecord(world, blockposition, world.getBlockState(blockposition), this);
                    world.playEvent((EntityPlayer) null, 1010, blockposition, Item.getIdFromItem(this.item));
                    this.shrink(1);
                    entityhuman.addStat(StatList.CRAFTING_TABLE_INTERACTION);
                }

                if (this.item == Items.SKULL) { // Special case skulls to allow wither spawns to be cancelled
                    BlockPos bp = blockposition;
                    if (!world.getBlockState(blockposition).getBlock().isReplaceable(world, blockposition)) {
                        if (!world.getBlockState(blockposition).getMaterial().isSolid()) {
                            bp = null;
                        } else {
                            bp = bp.offset(enumdirection);
                        }
                    }
                    if (bp != null) {
                        TileEntity te = world.getTileEntity(bp);
                        if (te instanceof TileEntitySkull) {
                            Blocks.SKULL.checkWitherSpawn(world, bp, (TileEntitySkull) te);
                        }
                    }
                }

                // SPIGOT-1288 - play sound stripped from ItemBlock
                if (this.item instanceof ItemBlock) {
                    SoundType soundeffecttype = ((ItemBlock) this.item).getBlock().getSoundType();
                    world.playSound(entityhuman, blockposition, soundeffecttype.getPlaceSound(), SoundCategory.BLOCKS, (soundeffecttype.getVolume() + 1.0F) / 2.0F, soundeffecttype.getPitch() * 0.8F);
                }

                entityhuman.addStat(StatList.getObjectUseStats(this.item));
            }
        }
        world.capturedTileEntities.clear();
        world.capturedBlockStates.clear();
        // CraftBukkit end

        return enuminteractionresult;
    }

    public float getDestroySpeed(IBlockState iblockdata) {
        return this.getItem().getDestroySpeed(this, iblockdata);
    }

    public ActionResult<ItemStack> useItemRightClick(World world, EntityPlayer entityhuman, EnumHand enumhand) {
        return this.getItem().onItemRightClick(world, entityhuman, enumhand);
    }

    public ItemStack onItemUseFinish(World world, EntityLivingBase entityliving) {
        return this.getItem().onItemUseFinish(this, world, entityliving);
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound) {
        ResourceLocation minecraftkey = (ResourceLocation) Item.REGISTRY.getNameForObject(this.item);

        nbttagcompound.setString("id", minecraftkey == null ? "minecraft:air" : minecraftkey.toString());
        nbttagcompound.setByte("Count", (byte) this.stackSize);
        nbttagcompound.setShort("Damage", (short) this.itemDamage);
        if (this.stackTagCompound != null) {
            nbttagcompound.setTag("tag", this.stackTagCompound.clone()); // CraftBukkit - make defensive copy, data is going to another thread
        }

        return nbttagcompound;
    }

    public int getMaxStackSize() {
        return this.getItem().getItemStackLimit();
    }

    public boolean isStackable() {
        return this.getMaxStackSize() > 1 && (!this.isItemStackDamageable() || !this.isItemDamaged());
    }

    public boolean isItemStackDamageable() {
        return this.isEmpty ? false : (this.item.getMaxDamage() <= 0 ? false : !this.hasTagCompound() || !this.getTagCompound().getBoolean("Unbreakable"));
    }

    public boolean getHasSubtypes() {
        return this.getItem().getHasSubtypes();
    }

    public boolean hasDamage() { return isItemDamaged(); } // Paper OBFHELPER
    public boolean isItemDamaged() {
        return this.isItemStackDamageable() && this.itemDamage > 0;
    }

    public int getDamage() { return getItemDamage(); } // Paper OBFHELPER
    public int getItemDamage() {
        return this.itemDamage;
    }

    public int getMetadata() {
        return this.itemDamage;
    }

    public void setItemDamage(int i) {
        // CraftBukkit start - Filter out data for items that shouldn't have it
        // The crafting system uses this value for a special purpose so we have to allow it
        if (i == 32767) {
            this.itemDamage = i;
            return;
        }

        // Is this a block?
        if (CraftMagicNumbers.getBlock(CraftMagicNumbers.getId(this.getItem())) != Blocks.AIR) {
            // If vanilla doesn't use data on it don't allow any
            if (!(this.getHasSubtypes() || this.getItem().isDamageable())) {
                i = 0;
            }
        }

        // Filter invalid plant data
        if (CraftMagicNumbers.getBlock(CraftMagicNumbers.getId(this.getItem())) == Blocks.DOUBLE_PLANT && (i > 5 || i < 0)) {
            i = 0;
        }
        // CraftBukkit end
        this.itemDamage = i;
        if (this.itemDamage < 0) {
            // this.damage = 0; // CraftBukkit - remove this.
        }
    }

    public int getMaxDamage() {
        return this.getItem().getMaxDamage();
    }

    public boolean attemptDamageItem(int i, Random random, @Nullable EntityPlayerMP entityplayer) {
        if (!this.isItemStackDamageable()) {
            return false;
        } else {
            if (i > 0) {
                int j = EnchantmentHelper.getEnchantmentLevel(Enchantments.UNBREAKING, this);
                int k = 0;

                for (int l = 0; j > 0 && l < i; ++l) {
                    if (EnchantmentDurability.negateDamage(this, j, random)) {
                        ++k;
                    }
                }

                i -= k;
                // Spigot start
                if (entityplayer != null) {
                    org.bukkit.craftbukkit.inventory.CraftItemStack item = org.bukkit.craftbukkit.inventory.CraftItemStack.asCraftMirror(this);
                    org.bukkit.event.player.PlayerItemDamageEvent event = new org.bukkit.event.player.PlayerItemDamageEvent(entityplayer.getBukkitEntity(), item, i);
                    org.bukkit.Bukkit.getServer().getPluginManager().callEvent(event);
                    if (i != event.getDamage() || event.isCancelled()) {
                        event.getPlayer().updateInventory();
                    }
                    if (event.isCancelled()) return false;
                    i = event.getDamage();
                }
                // Spigot end
                if (i <= 0) {
                    return false;
                }
            }

            if (entityplayer != null && i != 0) {
                CriteriaTriggers.ITEM_DURABILITY_CHANGED.trigger(entityplayer, this, this.itemDamage + i);
            }

            this.itemDamage += i;
            return this.itemDamage > this.getMaxDamage();
        }
    }

    public void damageItem(int i, EntityLivingBase entityliving) {
        if (!(entityliving instanceof EntityPlayer) || !((EntityPlayer) entityliving).capabilities.isCreativeMode) {
            if (this.isItemStackDamageable()) {
                if (this.attemptDamageItem(i, entityliving.getRNG(), entityliving instanceof EntityPlayerMP ? (EntityPlayerMP) entityliving : null)) {
                    entityliving.renderBrokenItemStack(this);
                    // CraftBukkit start - Check for item breaking
                    if (this.stackSize == 1 && entityliving instanceof EntityPlayer) {
                        org.bukkit.craftbukkit.event.CraftEventFactory.callPlayerItemBreakEvent((EntityPlayer) entityliving, this);
                    }
                    // CraftBukkit end
                    this.shrink(1);
                    if (entityliving instanceof EntityPlayer) {
                        EntityPlayer entityhuman = (EntityPlayer) entityliving;

                        entityhuman.addStat(StatList.getObjectBreakStats(this.item));
                    }

                    this.itemDamage = 0;
                }

            }
        }
    }

    public void hitEntity(EntityLivingBase entityliving, EntityPlayer entityhuman) {
        boolean flag = this.item.hitEntity(this, entityliving, (EntityLivingBase) entityhuman);

        if (flag) {
            entityhuman.addStat(StatList.getObjectUseStats(this.item));
        }

    }

    public void onBlockDestroyed(World world, IBlockState iblockdata, BlockPos blockposition, EntityPlayer entityhuman) {
        boolean flag = this.getItem().onBlockDestroyed(this, world, iblockdata, blockposition, entityhuman);

        if (flag) {
            entityhuman.addStat(StatList.getObjectUseStats(this.item));
        }

    }

    public boolean canHarvestBlock(IBlockState iblockdata) {
        return this.getItem().canHarvestBlock(iblockdata);
    }

    public boolean interactWithEntity(EntityPlayer entityhuman, EntityLivingBase entityliving, EnumHand enumhand) {
        return this.getItem().itemInteractionForEntity(this, entityhuman, entityliving, enumhand);
    }

    public ItemStack copy() {
        ItemStack itemstack = new ItemStack(this.item, this.stackSize, this.itemDamage, false); // CraftBukkit

        itemstack.setAnimationsToGo(this.getAnimationsToGo());
        if (this.stackTagCompound != null) {
            itemstack.stackTagCompound = this.stackTagCompound.copy();
        }

        return itemstack;
    }

    public static boolean areItemStackTagsEqual(ItemStack itemstack, ItemStack itemstack1) {
        return itemstack.isEmpty() && itemstack1.isEmpty() ? true : (!itemstack.isEmpty() && !itemstack1.isEmpty() ? (itemstack.stackTagCompound == null && itemstack1.stackTagCompound != null ? false : itemstack.stackTagCompound == null || itemstack.stackTagCompound.equals(itemstack1.stackTagCompound)) : false);
    }

    // Spigot Start
    public static boolean fastMatches(ItemStack itemstack, ItemStack itemstack1) {
        if (itemstack.isEmpty() && itemstack1.isEmpty()) {
            return true;
        }
        if (!itemstack.isEmpty() && !itemstack1.isEmpty()) {
            return itemstack.stackSize == itemstack1.stackSize && itemstack.item == itemstack1.item && itemstack.itemDamage == itemstack1.itemDamage;
        }
        return false;
    }
    // Spigot End

    public static boolean areItemStacksEqual(ItemStack itemstack, ItemStack itemstack1) {
        return itemstack.isEmpty() && itemstack1.isEmpty() ? true : (!itemstack.isEmpty() && !itemstack1.isEmpty() ? itemstack.isItemStackEqual(itemstack1) : false);
    }

    private boolean isItemStackEqual(ItemStack itemstack) {
        return this.stackSize != itemstack.stackSize ? false : (this.getItem() != itemstack.getItem() ? false : (this.itemDamage != itemstack.itemDamage ? false : (this.stackTagCompound == null && itemstack.stackTagCompound != null ? false : this.stackTagCompound == null || this.stackTagCompound.equals(itemstack.stackTagCompound))));
    }

    public static boolean areItemsEqual(ItemStack itemstack, ItemStack itemstack1) {
        return itemstack == itemstack1 ? true : (!itemstack.isEmpty() && !itemstack1.isEmpty() ? itemstack.isItemEqual(itemstack1) : false);
    }

    public static boolean areItemsEqualIgnoreDurability(ItemStack itemstack, ItemStack itemstack1) {
        return itemstack == itemstack1 ? true : (!itemstack.isEmpty() && !itemstack1.isEmpty() ? itemstack.isItemEqualIgnoreDurability(itemstack1) : false);
    }

    public boolean isItemEqual(ItemStack itemstack) {
        return !itemstack.isEmpty() && this.item == itemstack.item && this.itemDamage == itemstack.itemDamage;
    }

    public boolean isItemEqualIgnoreDurability(ItemStack itemstack) {
        return !this.isItemStackDamageable() ? this.isItemEqual(itemstack) : !itemstack.isEmpty() && this.item == itemstack.item;
    }

    public String getUnlocalizedName() {
        return this.getItem().getUnlocalizedName(this);
    }

    public String toString() {
        return this.stackSize + "x" + this.getItem().getUnlocalizedName() + "@" + this.itemDamage;
    }

    public void updateAnimation(World world, Entity entity, int i, boolean flag) {
        if (this.animationsToGo > 0) {
            --this.animationsToGo;
        }

        if (this.item != null) {
            this.item.onUpdate(this, world, entity, i, flag);
        }

    }

    public void onCrafting(World world, EntityPlayer entityhuman, int i) {
        entityhuman.addStat(StatList.getCraftStats(this.item), i);
        this.getItem().onCreated(this, world, entityhuman);
    }

    public int getItemUseMaxDuration() { return getMaxItemUseDuration(); } // Paper - OBFHELPER
    public int getMaxItemUseDuration() {
        return this.getItem().getMaxItemUseDuration(this);
    }

    public EnumAction getItemUseAction() {
        return this.getItem().getItemUseAction(this);
    }

    public void onPlayerStoppedUsing(World world, EntityLivingBase entityliving, int i) {
        this.getItem().onPlayerStoppedUsing(this, world, entityliving, i);
    }

    public boolean hasTagCompound() {
        return !this.isEmpty && this.stackTagCompound != null;
    }

    @Nullable
    public NBTTagCompound getTagCompound() {
        return this.stackTagCompound;
    }

    public NBTTagCompound getOrCreateSubCompound(String s) {
        if (this.stackTagCompound != null && this.stackTagCompound.hasKey(s, 10)) {
            return this.stackTagCompound.getCompoundTag(s);
        } else {
            NBTTagCompound nbttagcompound = new NBTTagCompound();

            this.setTagInfo(s, (NBTBase) nbttagcompound);
            return nbttagcompound;
        }
    }

    @Nullable
    public NBTTagCompound getSubCompound(String s) {
        return this.stackTagCompound != null && this.stackTagCompound.hasKey(s, 10) ? this.stackTagCompound.getCompoundTag(s) : null;
    }

    public void removeSubCompound(String s) {
        if (this.stackTagCompound != null && this.stackTagCompound.hasKey(s, 10)) {
            this.stackTagCompound.removeTag(s);
        }

    }

    public NBTTagList getEnchantmentTagList() {
        return this.stackTagCompound != null ? this.stackTagCompound.getTagList("ench", 10) : new NBTTagList();
    }

    public void setTagCompound(@Nullable NBTTagCompound nbttagcompound) {
        this.stackTagCompound = nbttagcompound;
        processEnchantOrder(this.stackTagCompound); // Paper
    }

    public String getDisplayName() {
        NBTTagCompound nbttagcompound = this.getSubCompound("display");

        if (nbttagcompound != null) {
            if (nbttagcompound.hasKey("Name", 8)) {
                return nbttagcompound.getString("Name");
            }

            if (nbttagcompound.hasKey("LocName", 8)) {
                return I18n.translateToLocal(nbttagcompound.getString("LocName"));
            }
        }

        return this.getItem().getItemStackDisplayName(this);
    }

    public ItemStack setTranslatableName(String s) {
        this.getOrCreateSubCompound("display").setString("LocName", s);
        return this;
    }

    public ItemStack setStackDisplayName(String s) {
        this.getOrCreateSubCompound("display").setString("Name", s);
        return this;
    }

    public void clearCustomName() {
        NBTTagCompound nbttagcompound = this.getSubCompound("display");

        if (nbttagcompound != null) {
            nbttagcompound.removeTag("Name");
            if (nbttagcompound.hasNoTags()) {
                this.removeSubCompound("display");
            }
        }

        if (this.stackTagCompound != null && this.stackTagCompound.hasNoTags()) {
            this.stackTagCompound = null;
        }

    }

    public boolean hasDisplayName() {
        NBTTagCompound nbttagcompound = this.getSubCompound("display");

        return nbttagcompound != null && nbttagcompound.hasKey("Name", 8);
    }

    public EnumRarity getRarity() {
        return this.getItem().getRarity(this);
    }

    public boolean isItemEnchantable() {
        return !this.getItem().isEnchantable(this) ? false : !this.isItemEnchanted();
    }

    public void addEnchantment(Enchantment enchantment, int i) {
        if (this.stackTagCompound == null) {
            this.setTagCompound(new NBTTagCompound());
        }

        if (!this.stackTagCompound.hasKey("ench", 9)) {
            this.stackTagCompound.setTag("ench", new NBTTagList());
        }

        NBTTagList nbttaglist = this.stackTagCompound.getTagList("ench", 10);
        NBTTagCompound nbttagcompound = new NBTTagCompound();

        nbttagcompound.setShort("id", (short) Enchantment.getEnchantmentID(enchantment));
        nbttagcompound.setShort("lvl", (short) ((byte) i));
        nbttaglist.appendTag(nbttagcompound);
        processEnchantOrder(nbttagcompound); // Paper
    }

    public boolean isItemEnchanted() {
        return this.stackTagCompound != null && this.stackTagCompound.hasKey("ench", 9) ? !this.stackTagCompound.getTagList("ench", 10).hasNoTags() : false;
    }

    public void setTagInfo(String s, NBTBase nbtbase) {
        if (this.stackTagCompound == null) {
            this.setTagCompound(new NBTTagCompound());
        }

        this.stackTagCompound.setTag(s, nbtbase);
    }

    public boolean canEditBlocks() {
        return this.getItem().canItemEditBlocks();
    }

    public boolean isOnItemFrame() {
        return this.itemFrame != null;
    }

    public void setItemFrame(EntityItemFrame entityitemframe) {
        this.itemFrame = entityitemframe;
    }

    @Nullable
    public EntityItemFrame getItemFrame() {
        return this.isEmpty ? null : this.itemFrame;
    }

    public int getRepairCost() {
        return this.hasTagCompound() && this.stackTagCompound.hasKey("RepairCost", 3) ? this.stackTagCompound.getInteger("RepairCost") : 0;
    }

    public void setRepairCost(int i) {
        // CraftBukkit start - remove RepairCost tag when 0 (SPIGOT-3945)
        if (i == 0) {
            if (this.hasTagCompound()) {
                this.stackTagCompound.removeTag("RepairCost");
            }
            return;
        }
        // CraftBukkit end
        if (!this.hasTagCompound()) {
            this.stackTagCompound = new NBTTagCompound();
        }

        this.stackTagCompound.setInteger("RepairCost", i);
    }

    public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot enumitemslot) {
        Object object;

        if (this.hasTagCompound() && this.stackTagCompound.hasKey("AttributeModifiers", 9)) {
            object = HashMultimap.create();
            NBTTagList nbttaglist = this.stackTagCompound.getTagList("AttributeModifiers", 10);

            for (int i = 0; i < nbttaglist.tagCount(); ++i) {
                NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
                AttributeModifier attributemodifier = SharedMonsterAttributes.readAttributeModifierFromNBT(nbttagcompound);

                if (attributemodifier != null && (!nbttagcompound.hasKey("Slot", 8) || nbttagcompound.getString("Slot").equals(enumitemslot.getName())) && attributemodifier.getID().getLeastSignificantBits() != 0L && attributemodifier.getID().getMostSignificantBits() != 0L) {
                    ((Multimap) object).put(nbttagcompound.getString("AttributeName"), attributemodifier);
                }
            }
        } else {
            object = this.getItem().getItemAttributeModifiers(enumitemslot);
        }

        return (Multimap) object;
    }

    public void addAttributeModifier(String s, AttributeModifier attributemodifier, @Nullable EntityEquipmentSlot enumitemslot) {
        if (this.stackTagCompound == null) {
            this.stackTagCompound = new NBTTagCompound();
        }

        if (!this.stackTagCompound.hasKey("AttributeModifiers", 9)) {
            this.stackTagCompound.setTag("AttributeModifiers", new NBTTagList());
        }

        NBTTagList nbttaglist = this.stackTagCompound.getTagList("AttributeModifiers", 10);
        NBTTagCompound nbttagcompound = SharedMonsterAttributes.writeAttributeModifierToNBT(attributemodifier);

        nbttagcompound.setString("AttributeName", s);
        if (enumitemslot != null) {
            nbttagcompound.setString("Slot", enumitemslot.getName());
        }

        nbttaglist.appendTag(nbttagcompound);
    }

    @Deprecated
    public void setItem(Item item) {
        this.item = item;
        this.setItemDamage(this.getMetadata()); // CraftBukkit - Set data again to ensure it is filtered properly
    }

    public ITextComponent getTextComponent() {
        TextComponentString chatcomponenttext = new TextComponentString(this.getDisplayName());

        if (this.hasDisplayName()) {
            chatcomponenttext.getStyle().setItalic(Boolean.valueOf(true));
        }

        ITextComponent ichatbasecomponent = (new TextComponentString("[")).appendSibling(chatcomponenttext).appendText("]");

        if (!this.isEmpty) {
            NBTTagCompound nbttagcompound = this.writeToNBT(new NBTTagCompound());

            ichatbasecomponent.getStyle().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ITEM, new TextComponentString(nbttagcompound.toString())));
            ichatbasecomponent.getStyle().setColor(this.getRarity().rarityColor);
        }

        return ichatbasecomponent;
    }

    public boolean canDestroy(Block block) {
        if (block == this.canDestroyCacheBlock) {
            return this.canDestroyCacheResult;
        } else {
            this.canDestroyCacheBlock = block;
            if (this.hasTagCompound() && this.stackTagCompound.hasKey("CanDestroy", 9)) {
                NBTTagList nbttaglist = this.stackTagCompound.getTagList("CanDestroy", 8);

                for (int i = 0; i < nbttaglist.tagCount(); ++i) {
                    Block block1 = Block.getBlockFromName(nbttaglist.getStringTagAt(i));

                    if (block1 == block) {
                        this.canDestroyCacheResult = true;
                        return true;
                    }
                }
            }

            this.canDestroyCacheResult = false;
            return false;
        }
    }

    public boolean canPlaceOn(Block block) {
        if (block == this.canPlaceOnCacheBlock) {
            return this.canPlaceOnCacheResult;
        } else {
            this.canPlaceOnCacheBlock = block;
            if (this.hasTagCompound() && this.stackTagCompound.hasKey("CanPlaceOn", 9)) {
                NBTTagList nbttaglist = this.stackTagCompound.getTagList("CanPlaceOn", 8);

                for (int i = 0; i < nbttaglist.tagCount(); ++i) {
                    Block block1 = Block.getBlockFromName(nbttaglist.getStringTagAt(i));

                    if (block1 == block) {
                        this.canPlaceOnCacheResult = true;
                        return true;
                    }
                }
            }

            this.canPlaceOnCacheResult = false;
            return false;
        }
    }

    public int getAnimationsToGo() {
        return this.animationsToGo;
    }

    public void setAnimationsToGo(int i) {
        this.animationsToGo = i;
    }

    public int getCount() {
        return this.isEmpty ? 0 : this.stackSize;
    }

    public void setCount(int i) {
        this.stackSize = i;
        this.updateEmptyState();
    }

    public void grow(int i) {
        this.setCount(this.stackSize + i);
    }

    public void shrink(int i) {
        this.grow(-i);
    }
}
