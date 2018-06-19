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

    public static final ItemStack field_190927_a = new ItemStack((Item) null);
    public static final DecimalFormat field_111284_a = new DecimalFormat("#.##");
    private int field_77994_a;
    private int field_77992_b;
    private Item field_151002_e;
    private NBTTagCompound field_77990_d;
    private boolean field_190928_g;
    private int field_77991_e; public void setDamage(int i) { this.field_77991_e = i; } // Paper - OBFHELPER
    private EntityItemFrame field_82843_f;
    private Block field_179552_h;
    private boolean field_179553_i;
    private Block field_179550_j;
    private boolean field_179551_k;

    public ItemStack(Block block) {
        this(block, 1);
    }

    public ItemStack(Block block, int i) {
        this(block, i, 0);
    }

    public ItemStack(Block block, int i, int j) {
        this(Item.func_150898_a(block), i, j);
    }

    public ItemStack(Item item) {
        this(item, 1);
    }

    public ItemStack(Item item, int i) {
        this(item, i, 0);
    }

    // CraftBukkit start
    // Paper start
    private static final java.util.Comparator<NBTTagCompound> enchantSorter = java.util.Comparator.comparingInt(o -> o.func_74765_d("id"));
    private void processEnchantOrder(NBTTagCompound tag) {
        if (tag == null || !tag.func_150297_b("ench", 9)) {
            return;
        }
        NBTTagList list = tag.func_150295_c("ench", 10);
        if (list.func_74745_c() < 2) {
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
        this.field_151002_e = item;
        this.field_77991_e = j;
        this.field_77994_a = i;
        // CraftBukkit start - Pass to setData to do filtering
        if (MinecraftServer.getServer() != null) {
            this.func_77964_b(j);
        }
        if (convert) {
            this.convertStack();
        }
        // CraftBukkit end
        if (this.field_77991_e < 0) {
            // this.damage = 0; // CraftBukkit - remove this.
        }

        this.func_190923_F();
    }

    // Called to run this stack through the data converter to handle older storage methods and serialized items
    public void convertStack() {
        if (MinecraftServer.getServer() != null) {
            // Don't convert beds - both the old and new data values are valid
            // Conversion would make getting white beds (data value 0) impossible
            if (this.field_151002_e == Items.field_151104_aV) {
                return;
            }

            NBTTagCompound savedStack = new NBTTagCompound();
            this.func_77955_b(savedStack);
            MinecraftServer.getServer().field_184112_s.func_188257_a(FixTypes.ITEM_INSTANCE, savedStack); // PAIL: convert
            this.load(savedStack);
        }
    }

    private void func_190923_F() {
        if (this.field_190928_g && this == ItemStack.field_190927_a) throw new AssertionError("TRAP"); // CraftBukkit
        this.field_190928_g = this.func_190926_b();
    }

    // CraftBukkit - break into own method
    public void load(NBTTagCompound nbttagcompound) {
        this.field_151002_e = nbttagcompound.func_150297_b("id", 8) ? Item.func_111206_d(nbttagcompound.func_74779_i("id")) : Item.func_150898_a(Blocks.field_150350_a); // Paper - fix NumberFormatException caused by attempting to read an EMPTY ItemStack
        this.field_77994_a = nbttagcompound.func_74771_c("Count");
        // CraftBukkit start - Route through setData for filtering
        // this.damage = Math.max(0, nbttagcompound.getShort("Damage"));
        this.func_77964_b(nbttagcompound.func_74765_d("Damage"));
        // CraftBukkit end

        if (nbttagcompound.func_150297_b("tag", 10)) {
            // CraftBukkit start - make defensive copy as this data may be coming from the save thread
            this.field_77990_d = (NBTTagCompound) nbttagcompound.func_74775_l("tag").clone();
            processEnchantOrder(this.field_77990_d); // Paper
            if (this.field_151002_e != null) {
                this.field_151002_e.func_179215_a(this.field_77990_d);
                // CraftBukkit end
            }
        }
    }

    public ItemStack(NBTTagCompound nbttagcompound) {
        this.load(nbttagcompound);
        // CraftBukkit end
        this.func_190923_F();
    }

    // Paper start - optimize isEmpty
    private static Item airItem;
    public boolean func_190926_b() {
        if (airItem == null) {
            airItem = Item.field_150901_e.func_82594_a(new ResourceLocation("air"));
        }
        return this == ItemStack.field_190927_a || this.field_151002_e == null || this.field_151002_e == airItem || (this.field_77994_a <= 0 || (this.field_77991_e < -32768 || this.field_77991_e > '\uffff'));
    }
    // Paper end

    public static void func_189868_a(DataFixer dataconvertermanager) {
        dataconvertermanager.func_188258_a(FixTypes.ITEM_INSTANCE, (IDataWalker) (new BlockEntityTag()));
        dataconvertermanager.func_188258_a(FixTypes.ITEM_INSTANCE, (IDataWalker) (new EntityTag()));
    }

    public ItemStack func_77979_a(int i) {
        int j = Math.min(i, this.field_77994_a);
        ItemStack itemstack = this.func_77946_l();

        itemstack.func_190920_e(j);
        this.func_190918_g(j);
        return itemstack;
    }

    public Item func_77973_b() {
        return this.field_190928_g ? Item.func_150898_a(Blocks.field_150350_a) : this.field_151002_e;
    }

    public EnumActionResult func_179546_a(EntityPlayer entityhuman, World world, BlockPos blockposition, EnumHand enumhand, EnumFacing enumdirection, float f, float f1, float f2) {
        // CraftBukkit start - handle all block place event logic here
        int oldData = this.func_77960_j();
        int oldCount = this.func_190916_E();

        if (!(this.func_77973_b() instanceof ItemBucket)) { // if not bucket
            world.captureBlockStates = true;
            // special case bonemeal
            if (this.func_77973_b() instanceof ItemDye && this.func_77960_j() == 15) {
                Block block = world.func_180495_p(blockposition).func_177230_c();
                if (block == Blocks.field_150345_g || block instanceof BlockMushroom) {
                    world.captureTreeGeneration = true;
                }
            }
        }
        EnumActionResult enuminteractionresult = this.func_77973_b().func_180614_a(entityhuman, world, blockposition, enumhand, enumdirection, f, f1, f2);
        int newData = this.func_77960_j();
        int newCount = this.func_190916_E();
        this.func_190920_e(oldCount);
        this.func_77964_b(oldData);
        world.captureBlockStates = false;
        if (enuminteractionresult == EnumActionResult.SUCCESS && world.captureTreeGeneration && world.capturedBlockStates.size() > 0) {
            world.captureTreeGeneration = false;
            Location location = new Location(world.getWorld(), blockposition.func_177958_n(), blockposition.func_177956_o(), blockposition.func_177952_p());
            TreeType treeType = BlockSapling.treeType;
            BlockSapling.treeType = null;
            List<BlockState> blocks = (List<BlockState>) world.capturedBlockStates.clone();
            world.capturedBlockStates.clear();
            StructureGrowEvent event = null;
            if (treeType != null) {
                boolean isBonemeal = func_77973_b() == Items.field_151100_aR && oldData == 15;
                event = new StructureGrowEvent(location, treeType, isBonemeal, (Player) entityhuman.getBukkitEntity(), blocks);
                org.bukkit.Bukkit.getPluginManager().callEvent(event);
            }
            if (event == null || !event.isCancelled()) {
                // Change the stack to its new contents if it hasn't been tampered with.
                if (this.func_190916_E() == oldCount && this.func_77960_j() == oldData) {
                    this.func_77964_b(newData);
                    this.func_190920_e(newCount);
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
                placeEvent = org.bukkit.craftbukkit.event.CraftEventFactory.callBlockMultiPlaceEvent(world, entityhuman, enumhand, blocks, blockposition.func_177958_n(), blockposition.func_177956_o(), blockposition.func_177952_p());
            } else if (blocks.size() == 1) {
                placeEvent = org.bukkit.craftbukkit.event.CraftEventFactory.callBlockPlaceEvent(world, entityhuman, enumhand, blocks.get(0), blockposition.func_177958_n(), blockposition.func_177956_o(), blockposition.func_177952_p());
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
                if (this.func_190916_E() == oldCount && this.func_77960_j() == oldData) {
                    this.func_77964_b(newData);
                    this.func_190920_e(newCount);
                }

                for (Map.Entry<BlockPos, TileEntity> e : world.capturedTileEntities.entrySet()) {
                    world.func_175690_a(e.getKey(), e.getValue());
                }

                for (BlockState blockstate : blocks) {
                    int x = blockstate.getX();
                    int y = blockstate.getY();
                    int z = blockstate.getZ();
                    int updateFlag = ((CraftBlockState) blockstate).getFlag();
                    org.bukkit.Material mat = blockstate.getType();
                    Block oldBlock = CraftMagicNumbers.getBlock(mat);
                    BlockPos newblockposition = new BlockPos(x, y, z);
                    IBlockState block = world.func_180495_p(newblockposition);

                    if (!(block.func_177230_c() instanceof BlockContainer)) { // Containers get placed automatically
                        block.func_177230_c().func_176213_c(world, newblockposition, block);
                    }

                    world.notifyAndUpdatePhysics(newblockposition, null, oldBlock.func_176223_P(), block, updateFlag); // send null chunk as chunk.k() returns false by this point
                }

                // Special case juke boxes as they update their tile entity. Copied from ItemRecord.
                // PAIL: checkme on updates.
                if (this.field_151002_e instanceof ItemRecord) {
                    ((BlockJukebox) Blocks.field_150421_aI).func_176431_a(world, blockposition, world.func_180495_p(blockposition), this);
                    world.func_180498_a((EntityPlayer) null, 1010, blockposition, Item.func_150891_b(this.field_151002_e));
                    this.func_190918_g(1);
                    entityhuman.func_71029_a(StatList.field_188062_ab);
                }

                if (this.field_151002_e == Items.field_151144_bL) { // Special case skulls to allow wither spawns to be cancelled
                    BlockPos bp = blockposition;
                    if (!world.func_180495_p(blockposition).func_177230_c().func_176200_f(world, blockposition)) {
                        if (!world.func_180495_p(blockposition).func_185904_a().func_76220_a()) {
                            bp = null;
                        } else {
                            bp = bp.func_177972_a(enumdirection);
                        }
                    }
                    if (bp != null) {
                        TileEntity te = world.func_175625_s(bp);
                        if (te instanceof TileEntitySkull) {
                            Blocks.field_150465_bP.func_180679_a(world, bp, (TileEntitySkull) te);
                        }
                    }
                }

                // SPIGOT-1288 - play sound stripped from ItemBlock
                if (this.field_151002_e instanceof ItemBlock) {
                    SoundType soundeffecttype = ((ItemBlock) this.field_151002_e).func_179223_d().func_185467_w();
                    world.func_184133_a(entityhuman, blockposition, soundeffecttype.func_185841_e(), SoundCategory.BLOCKS, (soundeffecttype.func_185843_a() + 1.0F) / 2.0F, soundeffecttype.func_185847_b() * 0.8F);
                }

                entityhuman.func_71029_a(StatList.func_188057_b(this.field_151002_e));
            }
        }
        world.capturedTileEntities.clear();
        world.capturedBlockStates.clear();
        // CraftBukkit end

        return enuminteractionresult;
    }

    public float func_150997_a(IBlockState iblockdata) {
        return this.func_77973_b().func_150893_a(this, iblockdata);
    }

    public ActionResult<ItemStack> func_77957_a(World world, EntityPlayer entityhuman, EnumHand enumhand) {
        return this.func_77973_b().func_77659_a(world, entityhuman, enumhand);
    }

    public ItemStack func_77950_b(World world, EntityLivingBase entityliving) {
        return this.func_77973_b().func_77654_b(this, world, entityliving);
    }

    public NBTTagCompound func_77955_b(NBTTagCompound nbttagcompound) {
        ResourceLocation minecraftkey = (ResourceLocation) Item.field_150901_e.func_177774_c(this.field_151002_e);

        nbttagcompound.func_74778_a("id", minecraftkey == null ? "minecraft:air" : minecraftkey.toString());
        nbttagcompound.func_74774_a("Count", (byte) this.field_77994_a);
        nbttagcompound.func_74777_a("Damage", (short) this.field_77991_e);
        if (this.field_77990_d != null) {
            nbttagcompound.func_74782_a("tag", this.field_77990_d.clone()); // CraftBukkit - make defensive copy, data is going to another thread
        }

        return nbttagcompound;
    }

    public int func_77976_d() {
        return this.func_77973_b().func_77639_j();
    }

    public boolean func_77985_e() {
        return this.func_77976_d() > 1 && (!this.func_77984_f() || !this.func_77951_h());
    }

    public boolean func_77984_f() {
        return this.field_190928_g ? false : (this.field_151002_e.func_77612_l() <= 0 ? false : !this.func_77942_o() || !this.func_77978_p().func_74767_n("Unbreakable"));
    }

    public boolean func_77981_g() {
        return this.func_77973_b().func_77614_k();
    }

    public boolean hasDamage() { return func_77951_h(); } // Paper OBFHELPER
    public boolean func_77951_h() {
        return this.func_77984_f() && this.field_77991_e > 0;
    }

    public int getDamage() { return func_77952_i(); } // Paper OBFHELPER
    public int func_77952_i() {
        return this.field_77991_e;
    }

    public int func_77960_j() {
        return this.field_77991_e;
    }

    public void func_77964_b(int i) {
        // CraftBukkit start - Filter out data for items that shouldn't have it
        // The crafting system uses this value for a special purpose so we have to allow it
        if (i == 32767) {
            this.field_77991_e = i;
            return;
        }

        // Is this a block?
        if (CraftMagicNumbers.getBlock(CraftMagicNumbers.getId(this.func_77973_b())) != Blocks.field_150350_a) {
            // If vanilla doesn't use data on it don't allow any
            if (!(this.func_77981_g() || this.func_77973_b().func_77645_m())) {
                i = 0;
            }
        }

        // Filter invalid plant data
        if (CraftMagicNumbers.getBlock(CraftMagicNumbers.getId(this.func_77973_b())) == Blocks.field_150398_cm && (i > 5 || i < 0)) {
            i = 0;
        }
        // CraftBukkit end
        this.field_77991_e = i;
        if (this.field_77991_e < 0) {
            // this.damage = 0; // CraftBukkit - remove this.
        }
    }

    public int func_77958_k() {
        return this.func_77973_b().func_77612_l();
    }

    public boolean func_96631_a(int i, Random random, @Nullable EntityPlayerMP entityplayer) {
        if (!this.func_77984_f()) {
            return false;
        } else {
            if (i > 0) {
                int j = EnchantmentHelper.func_77506_a(Enchantments.field_185307_s, this);
                int k = 0;

                for (int l = 0; j > 0 && l < i; ++l) {
                    if (EnchantmentDurability.func_92097_a(this, j, random)) {
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
                CriteriaTriggers.field_193132_s.func_193158_a(entityplayer, this, this.field_77991_e + i);
            }

            this.field_77991_e += i;
            return this.field_77991_e > this.func_77958_k();
        }
    }

    public void func_77972_a(int i, EntityLivingBase entityliving) {
        if (!(entityliving instanceof EntityPlayer) || !((EntityPlayer) entityliving).field_71075_bZ.field_75098_d) {
            if (this.func_77984_f()) {
                if (this.func_96631_a(i, entityliving.func_70681_au(), entityliving instanceof EntityPlayerMP ? (EntityPlayerMP) entityliving : null)) {
                    entityliving.func_70669_a(this);
                    // CraftBukkit start - Check for item breaking
                    if (this.field_77994_a == 1 && entityliving instanceof EntityPlayer) {
                        org.bukkit.craftbukkit.event.CraftEventFactory.callPlayerItemBreakEvent((EntityPlayer) entityliving, this);
                    }
                    // CraftBukkit end
                    this.func_190918_g(1);
                    if (entityliving instanceof EntityPlayer) {
                        EntityPlayer entityhuman = (EntityPlayer) entityliving;

                        entityhuman.func_71029_a(StatList.func_188059_c(this.field_151002_e));
                    }

                    this.field_77991_e = 0;
                }

            }
        }
    }

    public void func_77961_a(EntityLivingBase entityliving, EntityPlayer entityhuman) {
        boolean flag = this.field_151002_e.func_77644_a(this, entityliving, (EntityLivingBase) entityhuman);

        if (flag) {
            entityhuman.func_71029_a(StatList.func_188057_b(this.field_151002_e));
        }

    }

    public void func_179548_a(World world, IBlockState iblockdata, BlockPos blockposition, EntityPlayer entityhuman) {
        boolean flag = this.func_77973_b().func_179218_a(this, world, iblockdata, blockposition, entityhuman);

        if (flag) {
            entityhuman.func_71029_a(StatList.func_188057_b(this.field_151002_e));
        }

    }

    public boolean func_150998_b(IBlockState iblockdata) {
        return this.func_77973_b().func_150897_b(iblockdata);
    }

    public boolean func_111282_a(EntityPlayer entityhuman, EntityLivingBase entityliving, EnumHand enumhand) {
        return this.func_77973_b().func_111207_a(this, entityhuman, entityliving, enumhand);
    }

    public ItemStack func_77946_l() {
        ItemStack itemstack = new ItemStack(this.field_151002_e, this.field_77994_a, this.field_77991_e, false); // CraftBukkit

        itemstack.func_190915_d(this.func_190921_D());
        if (this.field_77990_d != null) {
            itemstack.field_77990_d = this.field_77990_d.func_74737_b();
        }

        return itemstack;
    }

    public static boolean func_77970_a(ItemStack itemstack, ItemStack itemstack1) {
        return itemstack.func_190926_b() && itemstack1.func_190926_b() ? true : (!itemstack.func_190926_b() && !itemstack1.func_190926_b() ? (itemstack.field_77990_d == null && itemstack1.field_77990_d != null ? false : itemstack.field_77990_d == null || itemstack.field_77990_d.equals(itemstack1.field_77990_d)) : false);
    }

    // Spigot Start
    public static boolean fastMatches(ItemStack itemstack, ItemStack itemstack1) {
        if (itemstack.func_190926_b() && itemstack1.func_190926_b()) {
            return true;
        }
        if (!itemstack.func_190926_b() && !itemstack1.func_190926_b()) {
            return itemstack.field_77994_a == itemstack1.field_77994_a && itemstack.field_151002_e == itemstack1.field_151002_e && itemstack.field_77991_e == itemstack1.field_77991_e;
        }
        return false;
    }
    // Spigot End

    public static boolean func_77989_b(ItemStack itemstack, ItemStack itemstack1) {
        return itemstack.func_190926_b() && itemstack1.func_190926_b() ? true : (!itemstack.func_190926_b() && !itemstack1.func_190926_b() ? itemstack.func_77959_d(itemstack1) : false);
    }

    private boolean func_77959_d(ItemStack itemstack) {
        return this.field_77994_a != itemstack.field_77994_a ? false : (this.func_77973_b() != itemstack.func_77973_b() ? false : (this.field_77991_e != itemstack.field_77991_e ? false : (this.field_77990_d == null && itemstack.field_77990_d != null ? false : this.field_77990_d == null || this.field_77990_d.equals(itemstack.field_77990_d))));
    }

    public static boolean func_179545_c(ItemStack itemstack, ItemStack itemstack1) {
        return itemstack == itemstack1 ? true : (!itemstack.func_190926_b() && !itemstack1.func_190926_b() ? itemstack.func_77969_a(itemstack1) : false);
    }

    public static boolean func_185132_d(ItemStack itemstack, ItemStack itemstack1) {
        return itemstack == itemstack1 ? true : (!itemstack.func_190926_b() && !itemstack1.func_190926_b() ? itemstack.func_185136_b(itemstack1) : false);
    }

    public boolean func_77969_a(ItemStack itemstack) {
        return !itemstack.func_190926_b() && this.field_151002_e == itemstack.field_151002_e && this.field_77991_e == itemstack.field_77991_e;
    }

    public boolean func_185136_b(ItemStack itemstack) {
        return !this.func_77984_f() ? this.func_77969_a(itemstack) : !itemstack.func_190926_b() && this.field_151002_e == itemstack.field_151002_e;
    }

    public String func_77977_a() {
        return this.func_77973_b().func_77667_c(this);
    }

    public String toString() {
        return this.field_77994_a + "x" + this.func_77973_b().func_77658_a() + "@" + this.field_77991_e;
    }

    public void func_77945_a(World world, Entity entity, int i, boolean flag) {
        if (this.field_77992_b > 0) {
            --this.field_77992_b;
        }

        if (this.field_151002_e != null) {
            this.field_151002_e.func_77663_a(this, world, entity, i, flag);
        }

    }

    public void func_77980_a(World world, EntityPlayer entityhuman, int i) {
        entityhuman.func_71064_a(StatList.func_188060_a(this.field_151002_e), i);
        this.func_77973_b().func_77622_d(this, world, entityhuman);
    }

    public int getItemUseMaxDuration() { return func_77988_m(); } // Paper - OBFHELPER
    public int func_77988_m() {
        return this.func_77973_b().func_77626_a(this);
    }

    public EnumAction func_77975_n() {
        return this.func_77973_b().func_77661_b(this);
    }

    public void func_77974_b(World world, EntityLivingBase entityliving, int i) {
        this.func_77973_b().func_77615_a(this, world, entityliving, i);
    }

    public boolean func_77942_o() {
        return !this.field_190928_g && this.field_77990_d != null;
    }

    @Nullable
    public NBTTagCompound func_77978_p() {
        return this.field_77990_d;
    }

    public NBTTagCompound func_190925_c(String s) {
        if (this.field_77990_d != null && this.field_77990_d.func_150297_b(s, 10)) {
            return this.field_77990_d.func_74775_l(s);
        } else {
            NBTTagCompound nbttagcompound = new NBTTagCompound();

            this.func_77983_a(s, (NBTBase) nbttagcompound);
            return nbttagcompound;
        }
    }

    @Nullable
    public NBTTagCompound func_179543_a(String s) {
        return this.field_77990_d != null && this.field_77990_d.func_150297_b(s, 10) ? this.field_77990_d.func_74775_l(s) : null;
    }

    public void func_190919_e(String s) {
        if (this.field_77990_d != null && this.field_77990_d.func_150297_b(s, 10)) {
            this.field_77990_d.func_82580_o(s);
        }

    }

    public NBTTagList func_77986_q() {
        return this.field_77990_d != null ? this.field_77990_d.func_150295_c("ench", 10) : new NBTTagList();
    }

    public void func_77982_d(@Nullable NBTTagCompound nbttagcompound) {
        this.field_77990_d = nbttagcompound;
        processEnchantOrder(this.field_77990_d); // Paper
    }

    public String func_82833_r() {
        NBTTagCompound nbttagcompound = this.func_179543_a("display");

        if (nbttagcompound != null) {
            if (nbttagcompound.func_150297_b("Name", 8)) {
                return nbttagcompound.func_74779_i("Name");
            }

            if (nbttagcompound.func_150297_b("LocName", 8)) {
                return I18n.func_74838_a(nbttagcompound.func_74779_i("LocName"));
            }
        }

        return this.func_77973_b().func_77653_i(this);
    }

    public ItemStack func_190924_f(String s) {
        this.func_190925_c("display").func_74778_a("LocName", s);
        return this;
    }

    public ItemStack func_151001_c(String s) {
        this.func_190925_c("display").func_74778_a("Name", s);
        return this;
    }

    public void func_135074_t() {
        NBTTagCompound nbttagcompound = this.func_179543_a("display");

        if (nbttagcompound != null) {
            nbttagcompound.func_82580_o("Name");
            if (nbttagcompound.func_82582_d()) {
                this.func_190919_e("display");
            }
        }

        if (this.field_77990_d != null && this.field_77990_d.func_82582_d()) {
            this.field_77990_d = null;
        }

    }

    public boolean func_82837_s() {
        NBTTagCompound nbttagcompound = this.func_179543_a("display");

        return nbttagcompound != null && nbttagcompound.func_150297_b("Name", 8);
    }

    public EnumRarity func_77953_t() {
        return this.func_77973_b().func_77613_e(this);
    }

    public boolean func_77956_u() {
        return !this.func_77973_b().func_77616_k(this) ? false : !this.func_77948_v();
    }

    public void func_77966_a(Enchantment enchantment, int i) {
        if (this.field_77990_d == null) {
            this.func_77982_d(new NBTTagCompound());
        }

        if (!this.field_77990_d.func_150297_b("ench", 9)) {
            this.field_77990_d.func_74782_a("ench", new NBTTagList());
        }

        NBTTagList nbttaglist = this.field_77990_d.func_150295_c("ench", 10);
        NBTTagCompound nbttagcompound = new NBTTagCompound();

        nbttagcompound.func_74777_a("id", (short) Enchantment.func_185258_b(enchantment));
        nbttagcompound.func_74777_a("lvl", (short) ((byte) i));
        nbttaglist.func_74742_a(nbttagcompound);
        processEnchantOrder(nbttagcompound); // Paper
    }

    public boolean func_77948_v() {
        return this.field_77990_d != null && this.field_77990_d.func_150297_b("ench", 9) ? !this.field_77990_d.func_150295_c("ench", 10).func_82582_d() : false;
    }

    public void func_77983_a(String s, NBTBase nbtbase) {
        if (this.field_77990_d == null) {
            this.func_77982_d(new NBTTagCompound());
        }

        this.field_77990_d.func_74782_a(s, nbtbase);
    }

    public boolean func_82835_x() {
        return this.func_77973_b().func_82788_x();
    }

    public boolean func_82839_y() {
        return this.field_82843_f != null;
    }

    public void func_82842_a(EntityItemFrame entityitemframe) {
        this.field_82843_f = entityitemframe;
    }

    @Nullable
    public EntityItemFrame func_82836_z() {
        return this.field_190928_g ? null : this.field_82843_f;
    }

    public int func_82838_A() {
        return this.func_77942_o() && this.field_77990_d.func_150297_b("RepairCost", 3) ? this.field_77990_d.func_74762_e("RepairCost") : 0;
    }

    public void func_82841_c(int i) {
        // CraftBukkit start - remove RepairCost tag when 0 (SPIGOT-3945)
        if (i == 0) {
            if (this.func_77942_o()) {
                this.field_77990_d.func_82580_o("RepairCost");
            }
            return;
        }
        // CraftBukkit end
        if (!this.func_77942_o()) {
            this.field_77990_d = new NBTTagCompound();
        }

        this.field_77990_d.func_74768_a("RepairCost", i);
    }

    public Multimap<String, AttributeModifier> func_111283_C(EntityEquipmentSlot enumitemslot) {
        Object object;

        if (this.func_77942_o() && this.field_77990_d.func_150297_b("AttributeModifiers", 9)) {
            object = HashMultimap.create();
            NBTTagList nbttaglist = this.field_77990_d.func_150295_c("AttributeModifiers", 10);

            for (int i = 0; i < nbttaglist.func_74745_c(); ++i) {
                NBTTagCompound nbttagcompound = nbttaglist.func_150305_b(i);
                AttributeModifier attributemodifier = SharedMonsterAttributes.func_111259_a(nbttagcompound);

                if (attributemodifier != null && (!nbttagcompound.func_150297_b("Slot", 8) || nbttagcompound.func_74779_i("Slot").equals(enumitemslot.func_188450_d())) && attributemodifier.func_111167_a().getLeastSignificantBits() != 0L && attributemodifier.func_111167_a().getMostSignificantBits() != 0L) {
                    ((Multimap) object).put(nbttagcompound.func_74779_i("AttributeName"), attributemodifier);
                }
            }
        } else {
            object = this.func_77973_b().func_111205_h(enumitemslot);
        }

        return (Multimap) object;
    }

    public void func_185129_a(String s, AttributeModifier attributemodifier, @Nullable EntityEquipmentSlot enumitemslot) {
        if (this.field_77990_d == null) {
            this.field_77990_d = new NBTTagCompound();
        }

        if (!this.field_77990_d.func_150297_b("AttributeModifiers", 9)) {
            this.field_77990_d.func_74782_a("AttributeModifiers", new NBTTagList());
        }

        NBTTagList nbttaglist = this.field_77990_d.func_150295_c("AttributeModifiers", 10);
        NBTTagCompound nbttagcompound = SharedMonsterAttributes.func_111262_a(attributemodifier);

        nbttagcompound.func_74778_a("AttributeName", s);
        if (enumitemslot != null) {
            nbttagcompound.func_74778_a("Slot", enumitemslot.func_188450_d());
        }

        nbttaglist.func_74742_a(nbttagcompound);
    }

    @Deprecated
    public void setItem(Item item) {
        this.field_151002_e = item;
        this.func_77964_b(this.func_77960_j()); // CraftBukkit - Set data again to ensure it is filtered properly
    }

    public ITextComponent func_151000_E() {
        TextComponentString chatcomponenttext = new TextComponentString(this.func_82833_r());

        if (this.func_82837_s()) {
            chatcomponenttext.func_150256_b().func_150217_b(Boolean.valueOf(true));
        }

        ITextComponent ichatbasecomponent = (new TextComponentString("[")).func_150257_a(chatcomponenttext).func_150258_a("]");

        if (!this.field_190928_g) {
            NBTTagCompound nbttagcompound = this.func_77955_b(new NBTTagCompound());

            ichatbasecomponent.func_150256_b().func_150209_a(new HoverEvent(HoverEvent.Action.SHOW_ITEM, new TextComponentString(nbttagcompound.toString())));
            ichatbasecomponent.func_150256_b().func_150238_a(this.func_77953_t().field_77937_e);
        }

        return ichatbasecomponent;
    }

    public boolean func_179544_c(Block block) {
        if (block == this.field_179552_h) {
            return this.field_179553_i;
        } else {
            this.field_179552_h = block;
            if (this.func_77942_o() && this.field_77990_d.func_150297_b("CanDestroy", 9)) {
                NBTTagList nbttaglist = this.field_77990_d.func_150295_c("CanDestroy", 8);

                for (int i = 0; i < nbttaglist.func_74745_c(); ++i) {
                    Block block1 = Block.func_149684_b(nbttaglist.func_150307_f(i));

                    if (block1 == block) {
                        this.field_179553_i = true;
                        return true;
                    }
                }
            }

            this.field_179553_i = false;
            return false;
        }
    }

    public boolean func_179547_d(Block block) {
        if (block == this.field_179550_j) {
            return this.field_179551_k;
        } else {
            this.field_179550_j = block;
            if (this.func_77942_o() && this.field_77990_d.func_150297_b("CanPlaceOn", 9)) {
                NBTTagList nbttaglist = this.field_77990_d.func_150295_c("CanPlaceOn", 8);

                for (int i = 0; i < nbttaglist.func_74745_c(); ++i) {
                    Block block1 = Block.func_149684_b(nbttaglist.func_150307_f(i));

                    if (block1 == block) {
                        this.field_179551_k = true;
                        return true;
                    }
                }
            }

            this.field_179551_k = false;
            return false;
        }
    }

    public int func_190921_D() {
        return this.field_77992_b;
    }

    public void func_190915_d(int i) {
        this.field_77992_b = i;
    }

    public int func_190916_E() {
        return this.field_190928_g ? 0 : this.field_77994_a;
    }

    public void func_190920_e(int i) {
        this.field_77994_a = i;
        this.func_190923_F();
    }

    public void func_190917_f(int i) {
        this.func_190920_e(this.field_77994_a + i);
    }

    public void func_190918_g(int i) {
        this.func_190917_f(-i);
    }
}
