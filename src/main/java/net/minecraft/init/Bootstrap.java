package net.minecraft.init;

import com.mojang.authlib.GameProfile;
import java.io.File;
import java.io.PrintStream;
import java.util.Random;
import java.util.UUID;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import net.minecraft.advancements.AdvancementManager;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDispenser;
import net.minecraft.block.BlockFire;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockPumpkin;
import net.minecraft.block.BlockSapling;
import net.minecraft.block.BlockShulkerBox;
import net.minecraft.block.BlockSkull;
import net.minecraft.block.BlockTNT;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.BehaviorProjectileDispense;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.IPosition;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityExpBottle;
import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.entity.projectile.EntitySmallFireball;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.entity.projectile.EntitySpectralArrow;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionHelper;
import net.minecraft.potion.PotionType;
import net.minecraft.server.DebugLoggingPrintStream;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.tileentity.TileEntityShulkerBox;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.LoggingPrintStream;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.StringUtils;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.storage.loot.LootTableList;
import org.bukkit.Location;
import org.bukkit.TreeType;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.world.StructureGrowEvent;

// CraftBukkit start
import java.util.List;
import org.bukkit.Location;
import org.bukkit.TreeType;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.world.StructureGrowEvent;
// CraftBukkit end

public class Bootstrap {

    public static final PrintStream field_179872_a = System.out;
    private static boolean field_151355_a;
    public static boolean field_194219_b;
    private static final Logger field_179871_c = LogManager.getLogger();

    public static boolean func_179869_a() {
        return Bootstrap.field_151355_a;
    }

    static void func_151353_a() {
        BlockDispenser.field_149943_a.func_82595_a(Items.field_151032_g, new BehaviorProjectileDispense() {
            @Override
            protected IProjectile func_82499_a(World world, IPosition iposition, ItemStack itemstack) {
                EntityTippedArrow entitytippedarrow = new EntityTippedArrow(world, iposition.func_82615_a(), iposition.func_82617_b(), iposition.func_82616_c());

                entitytippedarrow.field_70251_a = EntityArrow.PickupStatus.ALLOWED;
                return entitytippedarrow;
            }
        });
        BlockDispenser.field_149943_a.func_82595_a(Items.field_185167_i, new BehaviorProjectileDispense() {
            @Override
            protected IProjectile func_82499_a(World world, IPosition iposition, ItemStack itemstack) {
                EntityTippedArrow entitytippedarrow = new EntityTippedArrow(world, iposition.func_82615_a(), iposition.func_82617_b(), iposition.func_82616_c());

                entitytippedarrow.func_184555_a(itemstack);
                entitytippedarrow.field_70251_a = EntityArrow.PickupStatus.ALLOWED;
                return entitytippedarrow;
            }
        });
        BlockDispenser.field_149943_a.func_82595_a(Items.field_185166_h, new BehaviorProjectileDispense() {
            @Override
            protected IProjectile func_82499_a(World world, IPosition iposition, ItemStack itemstack) {
                EntitySpectralArrow entityspectralarrow = new EntitySpectralArrow(world, iposition.func_82615_a(), iposition.func_82617_b(), iposition.func_82616_c());

                entityspectralarrow.field_70251_a = EntityArrow.PickupStatus.ALLOWED;
                return entityspectralarrow;
            }
        });
        BlockDispenser.field_149943_a.func_82595_a(Items.field_151110_aK, new BehaviorProjectileDispense() {
            @Override
            protected IProjectile func_82499_a(World world, IPosition iposition, ItemStack itemstack) {
                return new EntityEgg(world, iposition.func_82615_a(), iposition.func_82617_b(), iposition.func_82616_c());
            }
        });
        BlockDispenser.field_149943_a.func_82595_a(Items.field_151126_ay, new BehaviorProjectileDispense() {
            @Override
            protected IProjectile func_82499_a(World world, IPosition iposition, ItemStack itemstack) {
                return new EntitySnowball(world, iposition.func_82615_a(), iposition.func_82617_b(), iposition.func_82616_c());
            }
        });
        BlockDispenser.field_149943_a.func_82595_a(Items.field_151062_by, new BehaviorProjectileDispense() {
            @Override
            protected IProjectile func_82499_a(World world, IPosition iposition, ItemStack itemstack) {
                return new EntityExpBottle(world, iposition.func_82615_a(), iposition.func_82617_b(), iposition.func_82616_c());
            }

            @Override
            protected float func_82498_a() {
                return super.func_82498_a() * 0.5F;
            }

            @Override
            protected float func_82500_b() {
                return super.func_82500_b() * 1.25F;
            }
        });
        BlockDispenser.field_149943_a.func_82595_a(Items.field_185155_bH, new IBehaviorDispenseItem() {
            @Override
            public ItemStack func_82482_a(IBlockSource isourceblock, final ItemStack itemstack) {
                return (new BehaviorProjectileDispense() {
                    @Override
                    protected IProjectile func_82499_a(World world, IPosition iposition, ItemStack itemstack1) { // CraftBukkit - decompile issue
                        return new EntityPotion(world, iposition.func_82615_a(), iposition.func_82617_b(), iposition.func_82616_c(), itemstack1.func_77946_l());
                    }

                    @Override
                    protected float func_82498_a() {
                        return super.func_82498_a() * 0.5F;
                    }

                    @Override
                    protected float func_82500_b() {
                        return super.func_82500_b() * 1.25F;
                    }
                }).func_82482_a(isourceblock, itemstack);
            }
        });
        BlockDispenser.field_149943_a.func_82595_a(Items.field_185156_bI, new IBehaviorDispenseItem() {
            @Override
            public ItemStack func_82482_a(IBlockSource isourceblock, final ItemStack itemstack) {
                return (new BehaviorProjectileDispense() {
                    @Override
                    protected IProjectile func_82499_a(World world, IPosition iposition, ItemStack itemstack1) { // CraftBukkit - decompile issue
                        return new EntityPotion(world, iposition.func_82615_a(), iposition.func_82617_b(), iposition.func_82616_c(), itemstack1.func_77946_l());
                    }

                    @Override
                    protected float func_82498_a() {
                        return super.func_82498_a() * 0.5F;
                    }

                    @Override
                    protected float func_82500_b() {
                        return super.func_82500_b() * 1.25F;
                    }
                }).func_82482_a(isourceblock, itemstack);
            }
        });
        BlockDispenser.field_149943_a.func_82595_a(Items.field_151063_bx, new BehaviorDefaultDispenseItem() {
            @Override
            public ItemStack func_82487_b(IBlockSource isourceblock, ItemStack itemstack) {
                EnumFacing enumdirection = isourceblock.func_189992_e().func_177229_b(BlockDispenser.field_176441_a);
                double d0 = isourceblock.func_82615_a() + enumdirection.func_82601_c();
                double d1 = isourceblock.func_180699_d().func_177956_o() + enumdirection.func_96559_d() + 0.2F;
                double d2 = isourceblock.func_82616_c() + enumdirection.func_82599_e();
                // Entity entity = ItemMonsterEgg.a(isourceblock.getWorld(), ItemMonsterEgg.h(itemstack), d0, d1, d2);

                // CraftBukkit start
                World world = isourceblock.func_82618_k();
                ItemStack itemstack1 = itemstack.func_77979_a(1);
                org.bukkit.block.Block block = world.getWorld().getBlockAt(isourceblock.func_180699_d().func_177958_n(), isourceblock.func_180699_d().func_177956_o(), isourceblock.func_180699_d().func_177952_p());
                CraftItemStack craftItem = CraftItemStack.asCraftMirror(itemstack1);

                BlockDispenseEvent event = new BlockDispenseEvent(block, craftItem.clone(), new org.bukkit.util.Vector(d0, d1, d2));
                if (!BlockDispenser.eventFired) {
                    world.getServer().getPluginManager().callEvent(event);
                }

                if (event.isCancelled()) {
                    itemstack.func_190917_f(1);
                    return itemstack;
                }

                if (!event.getItem().equals(craftItem)) {
                    itemstack.func_190917_f(1);
                    // Chain to handler for new item
                    ItemStack eventStack = CraftItemStack.asNMSCopy(event.getItem());
                    IBehaviorDispenseItem idispensebehavior = BlockDispenser.field_149943_a.func_82594_a(eventStack.func_77973_b());
                    if (idispensebehavior != IBehaviorDispenseItem.field_82483_a && idispensebehavior != this) {
                        idispensebehavior.func_82482_a(isourceblock, eventStack);
                        return itemstack;
                    }
                }

                itemstack1 = CraftItemStack.asNMSCopy(event.getItem());

                Entity entity = ItemMonsterPlacer.spawnCreature(isourceblock.func_82618_k(), ItemMonsterPlacer.func_190908_h(itemstack), event.getVelocity().getX(), event.getVelocity().getY(), event.getVelocity().getZ(), org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason.DISPENSE_EGG);

                if (entity instanceof EntityLivingBase && itemstack.func_82837_s()) {
                    entity.func_96094_a(itemstack.func_82833_r());
                }

                ItemMonsterPlacer.func_185079_a(isourceblock.func_82618_k(), (EntityPlayer) null, itemstack, entity);
                // itemstack.subtract(1);// Handled during event processing
                // CraftBukkit end
                return itemstack;
            }
        });
        BlockDispenser.field_149943_a.func_82595_a(Items.field_151152_bP, new BehaviorDefaultDispenseItem() {
            @Override
            public ItemStack func_82487_b(IBlockSource isourceblock, ItemStack itemstack) {
                EnumFacing enumdirection = isourceblock.func_189992_e().func_177229_b(BlockDispenser.field_176441_a);
                double d0 = isourceblock.func_82615_a() + enumdirection.func_82601_c();
                double d1 = isourceblock.func_180699_d().func_177956_o() + 0.2F;
                double d2 = isourceblock.func_82616_c() + enumdirection.func_82599_e();
                // CraftBukkit start
                World world = isourceblock.func_82618_k();
                ItemStack itemstack1 = itemstack.func_77979_a(1);
                org.bukkit.block.Block block = world.getWorld().getBlockAt(isourceblock.func_180699_d().func_177958_n(), isourceblock.func_180699_d().func_177956_o(), isourceblock.func_180699_d().func_177952_p());
                CraftItemStack craftItem = CraftItemStack.asCraftMirror(itemstack1);

                BlockDispenseEvent event = new BlockDispenseEvent(block, craftItem.clone(), new org.bukkit.util.Vector(d0, d1, d2));
                if (!BlockDispenser.eventFired) {
                    world.getServer().getPluginManager().callEvent(event);
                }

                if (event.isCancelled()) {
                    itemstack.func_190917_f(1);
                    return itemstack;
                }

                if (!event.getItem().equals(craftItem)) {
                    itemstack.func_190917_f(1);
                    // Chain to handler for new item
                    ItemStack eventStack = CraftItemStack.asNMSCopy(event.getItem());
                    IBehaviorDispenseItem idispensebehavior = BlockDispenser.field_149943_a.func_82594_a(eventStack.func_77973_b());
                    if (idispensebehavior != IBehaviorDispenseItem.field_82483_a && idispensebehavior != this) {
                        idispensebehavior.func_82482_a(isourceblock, eventStack);
                        return itemstack;
                    }
                }

                itemstack1 = CraftItemStack.asNMSCopy(event.getItem());
                EntityFireworkRocket entityfireworks = new EntityFireworkRocket(isourceblock.func_82618_k(), event.getVelocity().getX(), event.getVelocity().getY(), event.getVelocity().getZ(), itemstack1);

                isourceblock.func_82618_k().func_72838_d(entityfireworks);
                // itemstack.subtract(1); // Handled during event processing
                // CraftBukkit end
                return itemstack;
            }

            @Override
            protected void func_82485_a(IBlockSource isourceblock) {
                isourceblock.func_82618_k().func_175718_b(1004, isourceblock.func_180699_d(), 0);
            }
        });
        BlockDispenser.field_149943_a.func_82595_a(Items.field_151059_bz, new BehaviorDefaultDispenseItem() {
            @Override
            public ItemStack func_82487_b(IBlockSource isourceblock, ItemStack itemstack) {
                EnumFacing enumdirection = isourceblock.func_189992_e().func_177229_b(BlockDispenser.field_176441_a);
                IPosition iposition = BlockDispenser.func_149939_a(isourceblock);
                double d0 = iposition.func_82615_a() + enumdirection.func_82601_c() * 0.3F;
                double d1 = iposition.func_82617_b() + enumdirection.func_96559_d() * 0.3F;
                double d2 = iposition.func_82616_c() + enumdirection.func_82599_e() * 0.3F;
                World world = isourceblock.func_82618_k();
                Random random = world.field_73012_v;
                double d3 = random.nextGaussian() * 0.05D + enumdirection.func_82601_c();
                double d4 = random.nextGaussian() * 0.05D + enumdirection.func_96559_d();
                double d5 = random.nextGaussian() * 0.05D + enumdirection.func_82599_e();

                // CraftBukkit start
                ItemStack itemstack1 = itemstack.func_77979_a(1);
                org.bukkit.block.Block block = world.getWorld().getBlockAt(isourceblock.func_180699_d().func_177958_n(), isourceblock.func_180699_d().func_177956_o(), isourceblock.func_180699_d().func_177952_p());
                CraftItemStack craftItem = CraftItemStack.asCraftMirror(itemstack1);

                BlockDispenseEvent event = new BlockDispenseEvent(block, craftItem.clone(), new org.bukkit.util.Vector(d3, d4, d5));
                if (!BlockDispenser.eventFired) {
                    world.getServer().getPluginManager().callEvent(event);
                }

                if (event.isCancelled()) {
                    itemstack.func_190917_f(1);
                    return itemstack;
                }

                if (!event.getItem().equals(craftItem)) {
                    itemstack.func_190917_f(1);
                    // Chain to handler for new item
                    ItemStack eventStack = CraftItemStack.asNMSCopy(event.getItem());
                    IBehaviorDispenseItem idispensebehavior = BlockDispenser.field_149943_a.func_82594_a(eventStack.func_77973_b());
                    if (idispensebehavior != IBehaviorDispenseItem.field_82483_a && idispensebehavior != this) {
                        idispensebehavior.func_82482_a(isourceblock, eventStack);
                        return itemstack;
                    }
                }

                EntitySmallFireball fireball = new EntitySmallFireball(world, d0, d1, d2, event.getVelocity().getX(), event.getVelocity().getY(), event.getVelocity().getZ());
                fireball.projectileSource = new org.bukkit.craftbukkit.projectiles.CraftBlockProjectileSource((TileEntityDispenser) isourceblock.func_150835_j());

                world.func_72838_d(fireball);
                // itemstack.subtract(1); // Handled during event processing
                // CraftBukkit end
                return itemstack;
            }

            @Override
            protected void func_82485_a(IBlockSource isourceblock) {
                isourceblock.func_82618_k().func_175718_b(1018, isourceblock.func_180699_d(), 0);
            }
        });
        BlockDispenser.field_149943_a.func_82595_a(Items.field_151124_az, new Bootstrap.a(EntityBoat.Type.OAK));
        BlockDispenser.field_149943_a.func_82595_a(Items.field_185150_aH, new Bootstrap.a(EntityBoat.Type.SPRUCE));
        BlockDispenser.field_149943_a.func_82595_a(Items.field_185151_aI, new Bootstrap.a(EntityBoat.Type.BIRCH));
        BlockDispenser.field_149943_a.func_82595_a(Items.field_185152_aJ, new Bootstrap.a(EntityBoat.Type.JUNGLE));
        BlockDispenser.field_149943_a.func_82595_a(Items.field_185154_aL, new Bootstrap.a(EntityBoat.Type.DARK_OAK));
        BlockDispenser.field_149943_a.func_82595_a(Items.field_185153_aK, new Bootstrap.a(EntityBoat.Type.ACACIA));
        BehaviorDefaultDispenseItem dispensebehavioritem = new BehaviorDefaultDispenseItem() {
            private final BehaviorDefaultDispenseItem b = new BehaviorDefaultDispenseItem();

            @Override
            public ItemStack func_82487_b(IBlockSource isourceblock, ItemStack itemstack) {
                ItemBucket itembucket = (ItemBucket) itemstack.func_77973_b();
                BlockPos blockposition = isourceblock.func_180699_d().func_177972_a(isourceblock.func_189992_e().func_177229_b(BlockDispenser.field_176441_a));

                // CraftBukkit start
                World world = isourceblock.func_82618_k();
                int x = blockposition.func_177958_n();
                int y = blockposition.func_177956_o();
                int z = blockposition.func_177952_p();
                if (world.func_175623_d(blockposition) || !world.func_180495_p(blockposition).func_185904_a().func_76220_a()) {
                    org.bukkit.block.Block block = world.getWorld().getBlockAt(isourceblock.func_180699_d().func_177958_n(), isourceblock.func_180699_d().func_177956_o(), isourceblock.func_180699_d().func_177952_p());
                    CraftItemStack craftItem = CraftItemStack.asCraftMirror(itemstack);

                    BlockDispenseEvent event = new BlockDispenseEvent(block, craftItem.clone(), new org.bukkit.util.Vector(x, y, z));
                    if (!BlockDispenser.eventFired) {
                        world.getServer().getPluginManager().callEvent(event);
                    }

                    if (event.isCancelled()) {
                        return itemstack;
                    }

                    if (!event.getItem().equals(craftItem)) {
                        // Chain to handler for new item
                        ItemStack eventStack = CraftItemStack.asNMSCopy(event.getItem());
                        IBehaviorDispenseItem idispensebehavior = BlockDispenser.field_149943_a.func_82594_a(eventStack.func_77973_b());
                        if (idispensebehavior != IBehaviorDispenseItem.field_82483_a && idispensebehavior != this) {
                            idispensebehavior.func_82482_a(isourceblock, eventStack);
                            return itemstack;
                        }
                    }

                    itembucket = (ItemBucket) CraftItemStack.asNMSCopy(event.getItem()).func_77973_b();
                }
                // CraftBukkit end

                if (itembucket.func_180616_a((EntityPlayer) null, isourceblock.func_82618_k(), blockposition)) {
                    // CraftBukkit start - Handle stacked buckets
                    Item item = Items.field_151133_ar;
                    itemstack.func_190918_g(1);
                    if (itemstack.func_190926_b()) {
                        itemstack.setItem(Items.field_151133_ar);
                        itemstack.func_190920_e(1);
                    } else if (((TileEntityDispenser) isourceblock.func_150835_j()).func_146019_a(new ItemStack(item)) < 0) {
                        this.b.func_82482_a(isourceblock, new ItemStack(item));
                    }
                    // CraftBukkit end
                    return itemstack;
                } else {
                    return this.b.func_82482_a(isourceblock, itemstack);
                }
            }
        };

        BlockDispenser.field_149943_a.func_82595_a(Items.field_151129_at, dispensebehavioritem);
        BlockDispenser.field_149943_a.func_82595_a(Items.field_151131_as, dispensebehavioritem);
        BlockDispenser.field_149943_a.func_82595_a(Items.field_151133_ar, new BehaviorDefaultDispenseItem() {
            private final BehaviorDefaultDispenseItem b = new BehaviorDefaultDispenseItem();

            @Override
            public ItemStack func_82487_b(IBlockSource isourceblock, ItemStack itemstack) {
                World world = isourceblock.func_82618_k();
                BlockPos blockposition = isourceblock.func_180699_d().func_177972_a(isourceblock.func_189992_e().func_177229_b(BlockDispenser.field_176441_a));
                IBlockState iblockdata = world.func_180495_p(blockposition);
                Block block = iblockdata.func_177230_c();
                Material material = iblockdata.func_185904_a();
                Item item;

                if (Material.field_151586_h.equals(material) && block instanceof BlockLiquid && iblockdata.func_177229_b(BlockLiquid.field_176367_b).intValue() == 0) {
                    item = Items.field_151131_as;
                } else {
                    if (!Material.field_151587_i.equals(material) || !(block instanceof BlockLiquid) || iblockdata.func_177229_b(BlockLiquid.field_176367_b).intValue() != 0) {
                        return super.func_82487_b(isourceblock, itemstack);
                    }

                    item = Items.field_151129_at;
                }

                // CraftBukkit start
                org.bukkit.block.Block bukkitBlock = world.getWorld().getBlockAt(isourceblock.func_180699_d().func_177958_n(), isourceblock.func_180699_d().func_177956_o(), isourceblock.func_180699_d().func_177952_p());
                CraftItemStack craftItem = CraftItemStack.asCraftMirror(itemstack);

                BlockDispenseEvent event = new BlockDispenseEvent(bukkitBlock, craftItem.clone(), new org.bukkit.util.Vector(blockposition.func_177958_n(), blockposition.func_177956_o(), blockposition.func_177952_p()));
                if (!BlockDispenser.eventFired) {
                    world.getServer().getPluginManager().callEvent(event);
                }

                if (event.isCancelled()) {
                    return itemstack;
                }

                if (!event.getItem().equals(craftItem)) {
                    // Chain to handler for new item
                    ItemStack eventStack = CraftItemStack.asNMSCopy(event.getItem());
                    IBehaviorDispenseItem idispensebehavior = BlockDispenser.field_149943_a.func_82594_a(eventStack.func_77973_b());
                    if (idispensebehavior != IBehaviorDispenseItem.field_82483_a && idispensebehavior != this) {
                        idispensebehavior.func_82482_a(isourceblock, eventStack);
                        return itemstack;
                    }
                }
                // CraftBukkit end

                world.func_175698_g(blockposition);
                itemstack.func_190918_g(1);
                if (itemstack.func_190926_b()) {
                    return new ItemStack(item);
                } else {
                    if (((TileEntityDispenser) isourceblock.func_150835_j()).func_146019_a(new ItemStack(item)) < 0) {
                        this.b.func_82482_a(isourceblock, new ItemStack(item));
                    }

                    return itemstack;
                }
            }
        });
        BlockDispenser.field_149943_a.func_82595_a(Items.field_151033_d, new Bootstrap.b() {
            @Override
            protected ItemStack func_82487_b(IBlockSource isourceblock, ItemStack itemstack) {
                World world = isourceblock.func_82618_k();

                // CraftBukkit start
                org.bukkit.block.Block block = world.getWorld().getBlockAt(isourceblock.func_180699_d().func_177958_n(), isourceblock.func_180699_d().func_177956_o(), isourceblock.func_180699_d().func_177952_p());
                CraftItemStack craftItem = CraftItemStack.asCraftMirror(itemstack);

                BlockDispenseEvent event = new BlockDispenseEvent(block, craftItem.clone(), new org.bukkit.util.Vector(0, 0, 0));
                if (!BlockDispenser.eventFired) {
                    world.getServer().getPluginManager().callEvent(event);
                }

                if (event.isCancelled()) {
                    return itemstack;
                }

                if (!event.getItem().equals(craftItem)) {
                    // Chain to handler for new item
                    ItemStack eventStack = CraftItemStack.asNMSCopy(event.getItem());
                    IBehaviorDispenseItem idispensebehavior = BlockDispenser.field_149943_a.func_82594_a(eventStack.func_77973_b());
                    if (idispensebehavior != IBehaviorDispenseItem.field_82483_a && idispensebehavior != this) {
                        idispensebehavior.func_82482_a(isourceblock, eventStack);
                        return itemstack;
                    }
                }
                // CraftBukkit end

                this.b = true;
                BlockPos blockposition = isourceblock.func_180699_d().func_177972_a(isourceblock.func_189992_e().func_177229_b(BlockDispenser.field_176441_a));

                if (world.func_175623_d(blockposition)) {
                    // CraftBukkit start - Ignition by dispensing flint and steel
                    if (!org.bukkit.craftbukkit.event.CraftEventFactory.callBlockIgniteEvent(world, blockposition.func_177958_n(), blockposition.func_177956_o(), blockposition.func_177952_p(), isourceblock.func_180699_d().func_177958_n(), isourceblock.func_180699_d().func_177956_o(), isourceblock.func_180699_d().func_177952_p()).isCancelled()) {
                        world.func_175656_a(blockposition, Blocks.field_150480_ab.func_176223_P());
                        if (itemstack.func_96631_a(1, world.field_73012_v, (EntityPlayerMP) null)) {
                            itemstack.func_190920_e(0);
                        }
                    }
                    // CraftBukkit end
                } else if (world.func_180495_p(blockposition).func_177230_c() == Blocks.field_150335_W) {
                    Blocks.field_150335_W.func_176206_d(world, blockposition, Blocks.field_150335_W.func_176223_P().func_177226_a(BlockTNT.field_176246_a, Boolean.valueOf(true)));
                    world.func_175698_g(blockposition);
                } else {
                    this.b = false;
                }

                return itemstack;
            }
        });
        BlockDispenser.field_149943_a.func_82595_a(Items.field_151100_aR, new Bootstrap.b() {
            @Override
            protected ItemStack func_82487_b(IBlockSource isourceblock, ItemStack itemstack) {
                this.b = true;
                if (EnumDyeColor.WHITE == EnumDyeColor.func_176766_a(itemstack.func_77960_j())) {
                    World world = isourceblock.func_82618_k();
                    BlockPos blockposition = isourceblock.func_180699_d().func_177972_a(isourceblock.func_189992_e().func_177229_b(BlockDispenser.field_176441_a));

                    // CraftBukkit start
                    org.bukkit.block.Block block = world.getWorld().getBlockAt(isourceblock.func_180699_d().func_177958_n(), isourceblock.func_180699_d().func_177956_o(), isourceblock.func_180699_d().func_177952_p());
                    CraftItemStack craftItem = CraftItemStack.asCraftMirror(itemstack);

                    BlockDispenseEvent event = new BlockDispenseEvent(block, craftItem.clone(), new org.bukkit.util.Vector(0, 0, 0));
                    if (!BlockDispenser.eventFired) {
                        world.getServer().getPluginManager().callEvent(event);
                    }

                    if (event.isCancelled()) {
                        return itemstack;
                    }

                    if (!event.getItem().equals(craftItem)) {
                        // Chain to handler for new item
                        ItemStack eventStack = CraftItemStack.asNMSCopy(event.getItem());
                        IBehaviorDispenseItem idispensebehavior = BlockDispenser.field_149943_a.func_82594_a(eventStack.func_77973_b());
                        if (idispensebehavior != IBehaviorDispenseItem.field_82483_a && idispensebehavior != this) {
                            idispensebehavior.func_82482_a(isourceblock, eventStack);
                            return itemstack;
                        }
                    }

                    world.captureTreeGeneration = true;
                    // CraftBukkit end

                    if (ItemDye.func_179234_a(itemstack, world, blockposition)) {
                        if (!world.field_72995_K) {
                            world.func_175718_b(2005, blockposition, 0);
                        }
                    } else {
                        this.b = false;
                    }
                    // CraftBukkit start
                    world.captureTreeGeneration = false;
                    if (world.capturedBlockStates.size() > 0) {
                        TreeType treeType = BlockSapling.treeType;
                        BlockSapling.treeType = null;
                        Location location = new Location(world.getWorld(), blockposition.func_177958_n(), blockposition.func_177956_o(), blockposition.func_177952_p());
                        List<org.bukkit.block.BlockState> blocks = (List<org.bukkit.block.BlockState>) world.capturedBlockStates.clone();
                        world.capturedBlockStates.clear();
                        StructureGrowEvent structureEvent = null;
                        if (treeType != null) {
                            structureEvent = new StructureGrowEvent(location, treeType, false, null, blocks);
                            org.bukkit.Bukkit.getPluginManager().callEvent(structureEvent);
                        }
                        if (structureEvent == null || !structureEvent.isCancelled()) {
                            for (org.bukkit.block.BlockState blockstate : blocks) {
                                blockstate.update(true);
                            }
                        }
                    }
                    // CraftBukkit end

                    return itemstack;
                } else {
                    return super.func_82487_b(isourceblock, itemstack);
                }
            }
        });
        BlockDispenser.field_149943_a.func_82595_a(Item.func_150898_a(Blocks.field_150335_W), new BehaviorDefaultDispenseItem() {
            @Override
            protected ItemStack func_82487_b(IBlockSource isourceblock, ItemStack itemstack) {
                World world = isourceblock.func_82618_k();
                BlockPos blockposition = isourceblock.func_180699_d().func_177972_a(isourceblock.func_189992_e().func_177229_b(BlockDispenser.field_176441_a));
                // EntityTNTPrimed entitytntprimed = new EntityTNTPrimed(world, (double) blockposition.getX() + 0.5D, (double) blockposition.getY(), (double) blockposition.getZ() + 0.5D, (EntityLiving) null);

                // CraftBukkit start
                ItemStack itemstack1 = itemstack.func_77979_a(1);
                org.bukkit.block.Block block = world.getWorld().getBlockAt(isourceblock.func_180699_d().func_177958_n(), isourceblock.func_180699_d().func_177956_o(), isourceblock.func_180699_d().func_177952_p());
                CraftItemStack craftItem = CraftItemStack.asCraftMirror(itemstack1);

                BlockDispenseEvent event = new BlockDispenseEvent(block, craftItem.clone(), new org.bukkit.util.Vector(blockposition.func_177958_n() + 0.5D, blockposition.func_177956_o(), blockposition.func_177952_p() + 0.5D));
                if (!BlockDispenser.eventFired) {
                   world.getServer().getPluginManager().callEvent(event);
                }

                if (event.isCancelled()) {
                    itemstack.func_190917_f(1);
                    return itemstack;
                }

                if (!event.getItem().equals(craftItem)) {
                    itemstack.func_190917_f(1);
                    // Chain to handler for new item
                    ItemStack eventStack = CraftItemStack.asNMSCopy(event.getItem());
                    IBehaviorDispenseItem idispensebehavior = BlockDispenser.field_149943_a.func_82594_a(eventStack.func_77973_b());
                    if (idispensebehavior != IBehaviorDispenseItem.field_82483_a && idispensebehavior != this) {
                        idispensebehavior.func_82482_a(isourceblock, eventStack);
                        return itemstack;
                    }
                }

                EntityTNTPrimed entitytntprimed = new EntityTNTPrimed(world, event.getVelocity().getX(), event.getVelocity().getY(), event.getVelocity().getZ(), (EntityLivingBase) null);
                // CraftBukkit end

                world.func_72838_d(entitytntprimed);
                world.func_184148_a((EntityPlayer) null, entitytntprimed.field_70165_t, entitytntprimed.field_70163_u, entitytntprimed.field_70161_v, SoundEvents.field_187904_gd, SoundCategory.BLOCKS, 1.0F, 1.0F);
                // itemstack.subtract(1); // CraftBukkit - handled above
                return itemstack;
            }
        });
        BlockDispenser.field_149943_a.func_82595_a(Items.field_151144_bL, new Bootstrap.b() {
            @Override
            protected ItemStack func_82487_b(IBlockSource isourceblock, ItemStack itemstack) {
                World world = isourceblock.func_82618_k();
                EnumFacing enumdirection = isourceblock.func_189992_e().func_177229_b(BlockDispenser.field_176441_a);
                BlockPos blockposition = isourceblock.func_180699_d().func_177972_a(enumdirection);
                BlockSkull blockskull = Blocks.field_150465_bP;

                // CraftBukkit start
                org.bukkit.block.Block bukkitBlock = world.getWorld().getBlockAt(isourceblock.func_180699_d().func_177958_n(), isourceblock.func_180699_d().func_177956_o(), isourceblock.func_180699_d().func_177952_p());
                CraftItemStack craftItem = CraftItemStack.asCraftMirror(itemstack);

                BlockDispenseEvent event = new BlockDispenseEvent(bukkitBlock, craftItem.clone(), new org.bukkit.util.Vector(blockposition.func_177958_n(), blockposition.func_177956_o(), blockposition.func_177952_p()));
                if (!BlockDispenser.eventFired) {
                    world.getServer().getPluginManager().callEvent(event);
                }

                if (event.isCancelled()) {
                    return itemstack;
                }

                if (!event.getItem().equals(craftItem)) {
                    // Chain to handler for new item
                    ItemStack eventStack = CraftItemStack.asNMSCopy(event.getItem());
                    IBehaviorDispenseItem idispensebehavior = BlockDispenser.field_149943_a.func_82594_a(eventStack.func_77973_b());
                    if (idispensebehavior != IBehaviorDispenseItem.field_82483_a && idispensebehavior != this) {
                        idispensebehavior.func_82482_a(isourceblock, eventStack);
                        return itemstack;
                    }
                }
                // CraftBukkit end

                this.b = true;
                if (world.func_175623_d(blockposition) && blockskull.func_176415_b(world, blockposition, itemstack)) {
                    if (!world.field_72995_K) {
                        world.func_180501_a(blockposition, blockskull.func_176223_P().func_177226_a(BlockSkull.field_176418_a, EnumFacing.UP), 3);
                        TileEntity tileentity = world.func_175625_s(blockposition);

                        if (tileentity instanceof TileEntitySkull) {
                            if (itemstack.func_77960_j() == 3) {
                                GameProfile gameprofile = null;

                                if (itemstack.func_77942_o()) {
                                    NBTTagCompound nbttagcompound = itemstack.func_77978_p();

                                    if (nbttagcompound.func_150297_b("SkullOwner", 10)) {
                                        gameprofile = NBTUtil.func_152459_a(nbttagcompound.func_74775_l("SkullOwner"));
                                    } else if (nbttagcompound.func_150297_b("SkullOwner", 8)) {
                                        String s = nbttagcompound.func_74779_i("SkullOwner");

                                        if (!StringUtils.func_151246_b(s)) {
                                            gameprofile = new GameProfile((UUID) null, s);
                                        }
                                    }
                                }

                                ((TileEntitySkull) tileentity).func_152106_a(gameprofile);
                            } else {
                                ((TileEntitySkull) tileentity).func_152107_a(itemstack.func_77960_j());
                            }

                            ((TileEntitySkull) tileentity).func_145903_a(enumdirection.func_176734_d().func_176736_b() * 4);
                            Blocks.field_150465_bP.func_180679_a(world, blockposition, (TileEntitySkull) tileentity);
                        }

                        itemstack.func_190918_g(1);
                    }
                } else if (ItemArmor.func_185082_a(isourceblock, itemstack).func_190926_b()) {
                    this.b = false;
                }

                return itemstack;
            }
        });
        BlockDispenser.field_149943_a.func_82595_a(Item.func_150898_a(Blocks.field_150423_aK), new Bootstrap.b() {
            @Override
            protected ItemStack func_82487_b(IBlockSource isourceblock, ItemStack itemstack) {
                World world = isourceblock.func_82618_k();
                BlockPos blockposition = isourceblock.func_180699_d().func_177972_a(isourceblock.func_189992_e().func_177229_b(BlockDispenser.field_176441_a));
                BlockPumpkin blockpumpkin = (BlockPumpkin) Blocks.field_150423_aK;

                // CraftBukkit start
                org.bukkit.block.Block bukkitBlock = world.getWorld().getBlockAt(isourceblock.func_180699_d().func_177958_n(), isourceblock.func_180699_d().func_177956_o(), isourceblock.func_180699_d().func_177952_p());
                CraftItemStack craftItem = CraftItemStack.asCraftMirror(itemstack);

                BlockDispenseEvent event = new BlockDispenseEvent(bukkitBlock, craftItem.clone(), new org.bukkit.util.Vector(blockposition.func_177958_n(), blockposition.func_177956_o(), blockposition.func_177952_p()));
                if (!BlockDispenser.eventFired) {
                    world.getServer().getPluginManager().callEvent(event);
                }

                if (event.isCancelled()) {
                    return itemstack;
                }

                if (!event.getItem().equals(craftItem)) {
                    // Chain to handler for new item
                    ItemStack eventStack = CraftItemStack.asNMSCopy(event.getItem());
                    IBehaviorDispenseItem idispensebehavior = BlockDispenser.field_149943_a.func_82594_a(eventStack.func_77973_b());
                    if (idispensebehavior != IBehaviorDispenseItem.field_82483_a && idispensebehavior != this) {
                        idispensebehavior.func_82482_a(isourceblock, eventStack);
                        return itemstack;
                    }
                }
                // CraftBukkit end

                this.b = true;
                if (world.func_175623_d(blockposition) && blockpumpkin.func_176390_d(world, blockposition)) {
                    if (!world.field_72995_K) {
                        world.func_180501_a(blockposition, blockpumpkin.func_176223_P(), 3);
                    }

                    itemstack.func_190918_g(1);
                } else {
                    ItemStack itemstack1 = ItemArmor.func_185082_a(isourceblock, itemstack);

                    if (itemstack1.func_190926_b()) {
                        this.b = false;
                    }
                }

                return itemstack;
            }
        });
        EnumDyeColor[] aenumcolor = EnumDyeColor.values();
        int i = aenumcolor.length;

        for (int j = 0; j < i; ++j) {
            EnumDyeColor enumcolor = aenumcolor[j];

            BlockDispenser.field_149943_a.func_82595_a(Item.func_150898_a(BlockShulkerBox.func_190952_a(enumcolor)), new Bootstrap.c(null));
        }

    }

    public static void func_151354_b() {
        if (!Bootstrap.field_151355_a) {
            Bootstrap.field_151355_a = true;
            func_179868_d();
            SoundEvent.func_187504_b();
            Block.func_149671_p();
            BlockFire.func_149843_e();
            Potion.func_188411_k();
            Enchantment.func_185257_f();
            Item.func_150900_l();
            PotionType.func_185175_b();
            PotionHelper.func_185207_a();
            EntityList.func_151514_a();
            Biome.func_185358_q();
            func_151353_a();
            if (!CraftingManager.func_193377_a()) {
                Bootstrap.field_194219_b = true;
                Bootstrap.field_179871_c.error("Errors with built-in recipes!");
            }

            StatList.func_151178_a();
            if (Bootstrap.field_179871_c.isDebugEnabled()) {
                if ((new AdvancementManager((File) null)).func_193767_b()) {
                    Bootstrap.field_194219_b = true;
                    Bootstrap.field_179871_c.error("Errors with built-in advancements!");
                }

                if (!LootTableList.func_193579_b()) {
                    Bootstrap.field_194219_b = true;
                    Bootstrap.field_179871_c.error("Errors with built-in loot tables");
                }
            }

        }
    }

    private static void func_179868_d() {
        if (Bootstrap.field_179871_c.isDebugEnabled()) {
            System.setErr(new DebugLoggingPrintStream("STDERR", System.err));
            System.setOut(new DebugLoggingPrintStream("STDOUT", Bootstrap.field_179872_a));
        } else {
            System.setErr(new LoggingPrintStream("STDERR", System.err));
            System.setOut(new LoggingPrintStream("STDOUT", Bootstrap.field_179872_a));
        }

    }

    static class c extends Bootstrap.b {

        private c() {}

        @Override
        protected ItemStack func_82487_b(IBlockSource isourceblock, ItemStack itemstack) {
            Block block = Block.func_149634_a(itemstack.func_77973_b());
            World world = isourceblock.func_82618_k();
            EnumFacing enumdirection = isourceblock.func_189992_e().func_177229_b(BlockDispenser.field_176441_a);
            BlockPos blockposition = isourceblock.func_180699_d().func_177972_a(enumdirection);

            // CraftBukkit start
            org.bukkit.block.Block bukkitBlock = world.getWorld().getBlockAt(isourceblock.func_180699_d().func_177958_n(), isourceblock.func_180699_d().func_177956_o(), isourceblock.func_180699_d().func_177952_p());
            CraftItemStack craftItem = CraftItemStack.asCraftMirror(itemstack);

            BlockDispenseEvent event = new BlockDispenseEvent(bukkitBlock, craftItem.clone(), new org.bukkit.util.Vector(blockposition.func_177958_n(), blockposition.func_177956_o(), blockposition.func_177952_p()));
            if (!BlockDispenser.eventFired) {
                world.getServer().getPluginManager().callEvent(event);
            }

            if (event.isCancelled()) {
                return itemstack;
            }

            if (!event.getItem().equals(craftItem)) {
                // Chain to handler for new item
                ItemStack eventStack = CraftItemStack.asNMSCopy(event.getItem());
                IBehaviorDispenseItem idispensebehavior = BlockDispenser.field_149943_a.func_82594_a(eventStack.func_77973_b());
                if (idispensebehavior != IBehaviorDispenseItem.field_82483_a && idispensebehavior != this) {
                    idispensebehavior.func_82482_a(isourceblock, eventStack);
                    return itemstack;
                }
            }
            // CraftBukkit end

            this.b = world.func_190527_a(block, blockposition, false, EnumFacing.DOWN, (Entity) null);
            if (this.b) {
                EnumFacing enumdirection1 = world.func_175623_d(blockposition.func_177977_b()) ? enumdirection : EnumFacing.UP;
                IBlockState iblockdata = block.func_176223_P().func_177226_a(BlockShulkerBox.field_190957_a, enumdirection1);

                world.func_175656_a(blockposition, iblockdata);
                TileEntity tileentity = world.func_175625_s(blockposition);
                ItemStack itemstack1 = itemstack.func_77979_a(1);

                if (itemstack1.func_77942_o()) {
                    ((TileEntityShulkerBox) tileentity).func_190586_e(itemstack1.func_77978_p().func_74775_l("BlockEntityTag"));
                }

                if (itemstack1.func_82837_s()) {
                    ((TileEntityShulkerBox) tileentity).func_190575_a(itemstack1.func_82833_r());
                }

                world.func_175666_e(blockposition, iblockdata.func_177230_c());
            }

            return itemstack;
        }

        c(Object object) {
            this();
        }
    }

    public abstract static class b extends BehaviorDefaultDispenseItem {

        protected boolean b = true;

        public b() {}

        @Override
        protected void func_82485_a(IBlockSource isourceblock) {
            isourceblock.func_82618_k().func_175718_b(this.b ? 1000 : 1001, isourceblock.func_180699_d(), 0);
        }
    }

    public static class a extends BehaviorDefaultDispenseItem {

        private final BehaviorDefaultDispenseItem b = new BehaviorDefaultDispenseItem();
        private final EntityBoat.Type c;

        public a(EntityBoat.Type entityboat_enumboattype) {
            this.c = entityboat_enumboattype;
        }

        @Override
        public ItemStack func_82487_b(IBlockSource isourceblock, ItemStack itemstack) {
            EnumFacing enumdirection = isourceblock.func_189992_e().func_177229_b(BlockDispenser.field_176441_a);
            World world = isourceblock.func_82618_k();
            double d0 = isourceblock.func_82615_a() + enumdirection.func_82601_c() * 1.125F;
            double d1 = isourceblock.func_82617_b() + enumdirection.func_96559_d() * 1.125F;
            double d2 = isourceblock.func_82616_c() + enumdirection.func_82599_e() * 1.125F;
            BlockPos blockposition = isourceblock.func_180699_d().func_177972_a(enumdirection);
            Material material = world.func_180495_p(blockposition).func_185904_a();
            double d3;

            if (Material.field_151586_h.equals(material)) {
                d3 = 1.0D;
            } else {
                if (!Material.field_151579_a.equals(material) || !Material.field_151586_h.equals(world.func_180495_p(blockposition.func_177977_b()).func_185904_a())) {
                    return this.b.func_82482_a(isourceblock, itemstack);
                }

                d3 = 0.0D;
            }

            // EntityBoat entityboat = new EntityBoat(world, d0, d1 + d3, d2);
            // CraftBukkit start
            ItemStack itemstack1 = itemstack.func_77979_a(1);
            org.bukkit.block.Block block = world.getWorld().getBlockAt(isourceblock.func_180699_d().func_177958_n(), isourceblock.func_180699_d().func_177956_o(), isourceblock.func_180699_d().func_177952_p());
            CraftItemStack craftItem = CraftItemStack.asCraftMirror(itemstack1);

            BlockDispenseEvent event = new BlockDispenseEvent(block, craftItem.clone(), new org.bukkit.util.Vector(d0, d1 + d3, d2));
            if (!BlockDispenser.eventFired) {
                world.getServer().getPluginManager().callEvent(event);
            }

            if (event.isCancelled()) {
                itemstack.func_190917_f(1);
                return itemstack;
            }

            if (!event.getItem().equals(craftItem)) {
                itemstack.func_190917_f(1);
                // Chain to handler for new item
                ItemStack eventStack = CraftItemStack.asNMSCopy(event.getItem());
                IBehaviorDispenseItem idispensebehavior = BlockDispenser.field_149943_a.func_82594_a(eventStack.func_77973_b());
                if (idispensebehavior != IBehaviorDispenseItem.field_82483_a && idispensebehavior != this) {
                    idispensebehavior.func_82482_a(isourceblock, eventStack);
                    return itemstack;
                }
            }

            EntityBoat entityboat = new EntityBoat(world, event.getVelocity().getX(), event.getVelocity().getY(), event.getVelocity().getZ());
            // CraftBukkit end

            entityboat.func_184458_a(this.c);
            entityboat.field_70177_z = enumdirection.func_185119_l();
            if (!world.func_72838_d(entityboat)) itemstack.func_190917_f(1); // CraftBukkit
            // itemstack.subtract(1); // CraftBukkit - handled during event processing
            return itemstack;
        }

        @Override
        protected void func_82485_a(IBlockSource isourceblock) {
            isourceblock.func_82618_k().func_175718_b(1000, isourceblock.func_180699_d(), 0);
        }
    }
}
