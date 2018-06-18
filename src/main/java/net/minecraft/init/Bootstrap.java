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
import net.minecraft.server.DispenserRegistry.a;
import net.minecraft.server.DispenserRegistry.b;
import net.minecraft.server.DispenserRegistry.c;
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

    public static final PrintStream SYSOUT = System.out;
    private static boolean alreadyRegistered;
    public static boolean hasErrored;
    private static final Logger LOGGER = LogManager.getLogger();

    public static boolean isRegistered() {
        return Bootstrap.alreadyRegistered;
    }

    static void registerDispenserBehaviors() {
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(Items.ARROW, new BehaviorProjectileDispense() {
            protected IProjectile getProjectileEntity(World world, IPosition iposition, ItemStack itemstack) {
                EntityTippedArrow entitytippedarrow = new EntityTippedArrow(world, iposition.getX(), iposition.getY(), iposition.getZ());

                entitytippedarrow.pickupStatus = EntityArrow.PickupStatus.ALLOWED;
                return entitytippedarrow;
            }
        });
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(Items.TIPPED_ARROW, new BehaviorProjectileDispense() {
            protected IProjectile getProjectileEntity(World world, IPosition iposition, ItemStack itemstack) {
                EntityTippedArrow entitytippedarrow = new EntityTippedArrow(world, iposition.getX(), iposition.getY(), iposition.getZ());

                entitytippedarrow.setPotionEffect(itemstack);
                entitytippedarrow.pickupStatus = EntityArrow.PickupStatus.ALLOWED;
                return entitytippedarrow;
            }
        });
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(Items.SPECTRAL_ARROW, new BehaviorProjectileDispense() {
            protected IProjectile getProjectileEntity(World world, IPosition iposition, ItemStack itemstack) {
                EntitySpectralArrow entityspectralarrow = new EntitySpectralArrow(world, iposition.getX(), iposition.getY(), iposition.getZ());

                entityspectralarrow.pickupStatus = EntityArrow.PickupStatus.ALLOWED;
                return entityspectralarrow;
            }
        });
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(Items.EGG, new BehaviorProjectileDispense() {
            protected IProjectile getProjectileEntity(World world, IPosition iposition, ItemStack itemstack) {
                return new EntityEgg(world, iposition.getX(), iposition.getY(), iposition.getZ());
            }
        });
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(Items.SNOWBALL, new BehaviorProjectileDispense() {
            protected IProjectile getProjectileEntity(World world, IPosition iposition, ItemStack itemstack) {
                return new EntitySnowball(world, iposition.getX(), iposition.getY(), iposition.getZ());
            }
        });
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(Items.EXPERIENCE_BOTTLE, new BehaviorProjectileDispense() {
            protected IProjectile getProjectileEntity(World world, IPosition iposition, ItemStack itemstack) {
                return new EntityExpBottle(world, iposition.getX(), iposition.getY(), iposition.getZ());
            }

            protected float getProjectileInaccuracy() {
                return super.getProjectileInaccuracy() * 0.5F;
            }

            protected float getProjectileVelocity() {
                return super.getProjectileVelocity() * 1.25F;
            }
        });
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(Items.SPLASH_POTION, new IBehaviorDispenseItem() {
            public ItemStack dispense(IBlockSource isourceblock, final ItemStack itemstack) {
                return (new BehaviorProjectileDispense() {
                    protected IProjectile getProjectileEntity(World world, IPosition iposition, ItemStack itemstack1) { // CraftBukkit - decompile issue
                        return new EntityPotion(world, iposition.getX(), iposition.getY(), iposition.getZ(), itemstack1.copy());
                    }

                    protected float getProjectileInaccuracy() {
                        return super.getProjectileInaccuracy() * 0.5F;
                    }

                    protected float getProjectileVelocity() {
                        return super.getProjectileVelocity() * 1.25F;
                    }
                }).dispense(isourceblock, itemstack);
            }
        });
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(Items.LINGERING_POTION, new IBehaviorDispenseItem() {
            public ItemStack dispense(IBlockSource isourceblock, final ItemStack itemstack) {
                return (new BehaviorProjectileDispense() {
                    protected IProjectile getProjectileEntity(World world, IPosition iposition, ItemStack itemstack1) { // CraftBukkit - decompile issue
                        return new EntityPotion(world, iposition.getX(), iposition.getY(), iposition.getZ(), itemstack1.copy());
                    }

                    protected float getProjectileInaccuracy() {
                        return super.getProjectileInaccuracy() * 0.5F;
                    }

                    protected float getProjectileVelocity() {
                        return super.getProjectileVelocity() * 1.25F;
                    }
                }).dispense(isourceblock, itemstack);
            }
        });
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(Items.SPAWN_EGG, new BehaviorDefaultDispenseItem() {
            public ItemStack dispenseStack(IBlockSource isourceblock, ItemStack itemstack) {
                EnumFacing enumdirection = (EnumFacing) isourceblock.getBlockState().getValue(BlockDispenser.FACING);
                double d0 = isourceblock.getX() + (double) enumdirection.getFrontOffsetX();
                double d1 = (double) ((float) (isourceblock.getBlockPos().getY() + enumdirection.getFrontOffsetY()) + 0.2F);
                double d2 = isourceblock.getZ() + (double) enumdirection.getFrontOffsetZ();
                // Entity entity = ItemMonsterEgg.a(isourceblock.getWorld(), ItemMonsterEgg.h(itemstack), d0, d1, d2);

                // CraftBukkit start
                World world = isourceblock.getWorld();
                ItemStack itemstack1 = itemstack.splitStack(1);
                org.bukkit.block.Block block = world.getWorld().getBlockAt(isourceblock.getBlockPos().getX(), isourceblock.getBlockPos().getY(), isourceblock.getBlockPos().getZ());
                CraftItemStack craftItem = CraftItemStack.asCraftMirror(itemstack1);

                BlockDispenseEvent event = new BlockDispenseEvent(block, craftItem.clone(), new org.bukkit.util.Vector(d0, d1, d2));
                if (!BlockDispenser.eventFired) {
                    world.getServer().getPluginManager().callEvent(event);
                }

                if (event.isCancelled()) {
                    itemstack.grow(1);
                    return itemstack;
                }

                if (!event.getItem().equals(craftItem)) {
                    itemstack.grow(1);
                    // Chain to handler for new item
                    ItemStack eventStack = CraftItemStack.asNMSCopy(event.getItem());
                    IBehaviorDispenseItem idispensebehavior = (IBehaviorDispenseItem) BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.getObject(eventStack.getItem());
                    if (idispensebehavior != IBehaviorDispenseItem.DEFAULT_BEHAVIOR && idispensebehavior != this) {
                        idispensebehavior.dispense(isourceblock, eventStack);
                        return itemstack;
                    }
                }

                itemstack1 = CraftItemStack.asNMSCopy(event.getItem());

                Entity entity = ItemMonsterPlacer.spawnCreature(isourceblock.getWorld(), ItemMonsterPlacer.getNamedIdFrom(itemstack), event.getVelocity().getX(), event.getVelocity().getY(), event.getVelocity().getZ(), org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason.DISPENSE_EGG);

                if (entity instanceof EntityLivingBase && itemstack.hasDisplayName()) {
                    entity.setCustomNameTag(itemstack.getDisplayName());
                }

                ItemMonsterPlacer.applyItemEntityDataToEntity(isourceblock.getWorld(), (EntityPlayer) null, itemstack, entity);
                // itemstack.subtract(1);// Handled during event processing
                // CraftBukkit end
                return itemstack;
            }
        });
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(Items.FIREWORKS, new BehaviorDefaultDispenseItem() {
            public ItemStack dispenseStack(IBlockSource isourceblock, ItemStack itemstack) {
                EnumFacing enumdirection = (EnumFacing) isourceblock.getBlockState().getValue(BlockDispenser.FACING);
                double d0 = isourceblock.getX() + (double) enumdirection.getFrontOffsetX();
                double d1 = (double) ((float) isourceblock.getBlockPos().getY() + 0.2F);
                double d2 = isourceblock.getZ() + (double) enumdirection.getFrontOffsetZ();
                // CraftBukkit start
                World world = isourceblock.getWorld();
                ItemStack itemstack1 = itemstack.splitStack(1);
                org.bukkit.block.Block block = world.getWorld().getBlockAt(isourceblock.getBlockPos().getX(), isourceblock.getBlockPos().getY(), isourceblock.getBlockPos().getZ());
                CraftItemStack craftItem = CraftItemStack.asCraftMirror(itemstack1);

                BlockDispenseEvent event = new BlockDispenseEvent(block, craftItem.clone(), new org.bukkit.util.Vector(d0, d1, d2));
                if (!BlockDispenser.eventFired) {
                    world.getServer().getPluginManager().callEvent(event);
                }

                if (event.isCancelled()) {
                    itemstack.grow(1);
                    return itemstack;
                }

                if (!event.getItem().equals(craftItem)) {
                    itemstack.grow(1);
                    // Chain to handler for new item
                    ItemStack eventStack = CraftItemStack.asNMSCopy(event.getItem());
                    IBehaviorDispenseItem idispensebehavior = (IBehaviorDispenseItem) BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.getObject(eventStack.getItem());
                    if (idispensebehavior != IBehaviorDispenseItem.DEFAULT_BEHAVIOR && idispensebehavior != this) {
                        idispensebehavior.dispense(isourceblock, eventStack);
                        return itemstack;
                    }
                }

                itemstack1 = CraftItemStack.asNMSCopy(event.getItem());
                EntityFireworkRocket entityfireworks = new EntityFireworkRocket(isourceblock.getWorld(), event.getVelocity().getX(), event.getVelocity().getY(), event.getVelocity().getZ(), itemstack1);

                isourceblock.getWorld().spawnEntity(entityfireworks);
                // itemstack.subtract(1); // Handled during event processing
                // CraftBukkit end
                return itemstack;
            }

            protected void playDispenseSound(IBlockSource isourceblock) {
                isourceblock.getWorld().playEvent(1004, isourceblock.getBlockPos(), 0);
            }
        });
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(Items.FIRE_CHARGE, new BehaviorDefaultDispenseItem() {
            public ItemStack dispenseStack(IBlockSource isourceblock, ItemStack itemstack) {
                EnumFacing enumdirection = (EnumFacing) isourceblock.getBlockState().getValue(BlockDispenser.FACING);
                IPosition iposition = BlockDispenser.getDispensePosition(isourceblock);
                double d0 = iposition.getX() + (double) ((float) enumdirection.getFrontOffsetX() * 0.3F);
                double d1 = iposition.getY() + (double) ((float) enumdirection.getFrontOffsetY() * 0.3F);
                double d2 = iposition.getZ() + (double) ((float) enumdirection.getFrontOffsetZ() * 0.3F);
                World world = isourceblock.getWorld();
                Random random = world.rand;
                double d3 = random.nextGaussian() * 0.05D + (double) enumdirection.getFrontOffsetX();
                double d4 = random.nextGaussian() * 0.05D + (double) enumdirection.getFrontOffsetY();
                double d5 = random.nextGaussian() * 0.05D + (double) enumdirection.getFrontOffsetZ();

                // CraftBukkit start
                ItemStack itemstack1 = itemstack.splitStack(1);
                org.bukkit.block.Block block = world.getWorld().getBlockAt(isourceblock.getBlockPos().getX(), isourceblock.getBlockPos().getY(), isourceblock.getBlockPos().getZ());
                CraftItemStack craftItem = CraftItemStack.asCraftMirror(itemstack1);

                BlockDispenseEvent event = new BlockDispenseEvent(block, craftItem.clone(), new org.bukkit.util.Vector(d3, d4, d5));
                if (!BlockDispenser.eventFired) {
                    world.getServer().getPluginManager().callEvent(event);
                }

                if (event.isCancelled()) {
                    itemstack.grow(1);
                    return itemstack;
                }

                if (!event.getItem().equals(craftItem)) {
                    itemstack.grow(1);
                    // Chain to handler for new item
                    ItemStack eventStack = CraftItemStack.asNMSCopy(event.getItem());
                    IBehaviorDispenseItem idispensebehavior = (IBehaviorDispenseItem) BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.getObject(eventStack.getItem());
                    if (idispensebehavior != IBehaviorDispenseItem.DEFAULT_BEHAVIOR && idispensebehavior != this) {
                        idispensebehavior.dispense(isourceblock, eventStack);
                        return itemstack;
                    }
                }

                EntitySmallFireball fireball = new EntitySmallFireball(world, d0, d1, d2, event.getVelocity().getX(), event.getVelocity().getY(), event.getVelocity().getZ());
                fireball.projectileSource = new org.bukkit.craftbukkit.projectiles.CraftBlockProjectileSource((TileEntityDispenser) isourceblock.getBlockTileEntity());

                world.spawnEntity(fireball);
                // itemstack.subtract(1); // Handled during event processing
                // CraftBukkit end
                return itemstack;
            }

            protected void playDispenseSound(IBlockSource isourceblock) {
                isourceblock.getWorld().playEvent(1018, isourceblock.getBlockPos(), 0);
            }
        });
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(Items.BOAT, new DispenserRegistry.a(EntityBoat.Type.OAK));
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(Items.SPRUCE_BOAT, new DispenserRegistry.a(EntityBoat.Type.SPRUCE));
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(Items.BIRCH_BOAT, new DispenserRegistry.a(EntityBoat.Type.BIRCH));
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(Items.JUNGLE_BOAT, new DispenserRegistry.a(EntityBoat.Type.JUNGLE));
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(Items.DARK_OAK_BOAT, new DispenserRegistry.a(EntityBoat.Type.DARK_OAK));
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(Items.ACACIA_BOAT, new DispenserRegistry.a(EntityBoat.Type.ACACIA));
        BehaviorDefaultDispenseItem dispensebehavioritem = new BehaviorDefaultDispenseItem() {
            private final BehaviorDefaultDispenseItem b = new BehaviorDefaultDispenseItem();

            public ItemStack dispenseStack(IBlockSource isourceblock, ItemStack itemstack) {
                ItemBucket itembucket = (ItemBucket) itemstack.getItem();
                BlockPos blockposition = isourceblock.getBlockPos().offset((EnumFacing) isourceblock.getBlockState().getValue(BlockDispenser.FACING));

                // CraftBukkit start
                World world = isourceblock.getWorld();
                int x = blockposition.getX();
                int y = blockposition.getY();
                int z = blockposition.getZ();
                if (world.isAirBlock(blockposition) || !world.getBlockState(blockposition).getMaterial().isSolid()) {
                    org.bukkit.block.Block block = world.getWorld().getBlockAt(isourceblock.getBlockPos().getX(), isourceblock.getBlockPos().getY(), isourceblock.getBlockPos().getZ());
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
                        IBehaviorDispenseItem idispensebehavior = (IBehaviorDispenseItem) BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.getObject(eventStack.getItem());
                        if (idispensebehavior != IBehaviorDispenseItem.DEFAULT_BEHAVIOR && idispensebehavior != this) {
                            idispensebehavior.dispense(isourceblock, eventStack);
                            return itemstack;
                        }
                    }

                    itembucket = (ItemBucket) CraftItemStack.asNMSCopy(event.getItem()).getItem();
                }
                // CraftBukkit end

                if (itembucket.tryPlaceContainedLiquid((EntityPlayer) null, isourceblock.getWorld(), blockposition)) {
                    // CraftBukkit start - Handle stacked buckets
                    Item item = Items.BUCKET;
                    itemstack.shrink(1);
                    if (itemstack.isEmpty()) {
                        itemstack.setItem(Items.BUCKET);
                        itemstack.setCount(1);
                    } else if (((TileEntityDispenser) isourceblock.getBlockTileEntity()).addItemStack(new ItemStack(item)) < 0) {
                        this.b.dispense(isourceblock, new ItemStack(item));
                    }
                    // CraftBukkit end
                    return itemstack;
                } else {
                    return this.b.dispense(isourceblock, itemstack);
                }
            }
        };

        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(Items.LAVA_BUCKET, dispensebehavioritem);
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(Items.WATER_BUCKET, dispensebehavioritem);
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(Items.BUCKET, new BehaviorDefaultDispenseItem() {
            private final BehaviorDefaultDispenseItem b = new BehaviorDefaultDispenseItem();

            public ItemStack dispenseStack(IBlockSource isourceblock, ItemStack itemstack) {
                World world = isourceblock.getWorld();
                BlockPos blockposition = isourceblock.getBlockPos().offset((EnumFacing) isourceblock.getBlockState().getValue(BlockDispenser.FACING));
                IBlockState iblockdata = world.getBlockState(blockposition);
                Block block = iblockdata.getBlock();
                Material material = iblockdata.getMaterial();
                Item item;

                if (Material.WATER.equals(material) && block instanceof BlockLiquid && ((Integer) iblockdata.getValue(BlockLiquid.LEVEL)).intValue() == 0) {
                    item = Items.WATER_BUCKET;
                } else {
                    if (!Material.LAVA.equals(material) || !(block instanceof BlockLiquid) || ((Integer) iblockdata.getValue(BlockLiquid.LEVEL)).intValue() != 0) {
                        return super.dispenseStack(isourceblock, itemstack);
                    }

                    item = Items.LAVA_BUCKET;
                }

                // CraftBukkit start
                org.bukkit.block.Block bukkitBlock = world.getWorld().getBlockAt(isourceblock.getBlockPos().getX(), isourceblock.getBlockPos().getY(), isourceblock.getBlockPos().getZ());
                CraftItemStack craftItem = CraftItemStack.asCraftMirror(itemstack);

                BlockDispenseEvent event = new BlockDispenseEvent(bukkitBlock, craftItem.clone(), new org.bukkit.util.Vector(blockposition.getX(), blockposition.getY(), blockposition.getZ()));
                if (!BlockDispenser.eventFired) {
                    world.getServer().getPluginManager().callEvent(event);
                }

                if (event.isCancelled()) {
                    return itemstack;
                }

                if (!event.getItem().equals(craftItem)) {
                    // Chain to handler for new item
                    ItemStack eventStack = CraftItemStack.asNMSCopy(event.getItem());
                    IBehaviorDispenseItem idispensebehavior = (IBehaviorDispenseItem) BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.getObject(eventStack.getItem());
                    if (idispensebehavior != IBehaviorDispenseItem.DEFAULT_BEHAVIOR && idispensebehavior != this) {
                        idispensebehavior.dispense(isourceblock, eventStack);
                        return itemstack;
                    }
                }
                // CraftBukkit end

                world.setBlockToAir(blockposition);
                itemstack.shrink(1);
                if (itemstack.isEmpty()) {
                    return new ItemStack(item);
                } else {
                    if (((TileEntityDispenser) isourceblock.getBlockTileEntity()).addItemStack(new ItemStack(item)) < 0) {
                        this.b.dispense(isourceblock, new ItemStack(item));
                    }

                    return itemstack;
                }
            }
        });
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(Items.FLINT_AND_STEEL, new DispenserRegistry.b() {
            protected ItemStack dispenseStack(IBlockSource isourceblock, ItemStack itemstack) {
                World world = isourceblock.getWorld();

                // CraftBukkit start
                org.bukkit.block.Block block = world.getWorld().getBlockAt(isourceblock.getBlockPos().getX(), isourceblock.getBlockPos().getY(), isourceblock.getBlockPos().getZ());
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
                    IBehaviorDispenseItem idispensebehavior = (IBehaviorDispenseItem) BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.getObject(eventStack.getItem());
                    if (idispensebehavior != IBehaviorDispenseItem.DEFAULT_BEHAVIOR && idispensebehavior != this) {
                        idispensebehavior.dispense(isourceblock, eventStack);
                        return itemstack;
                    }
                }
                // CraftBukkit end

                this.b = true;
                BlockPos blockposition = isourceblock.getBlockPos().offset((EnumFacing) isourceblock.getBlockState().getValue(BlockDispenser.FACING));

                if (world.isAirBlock(blockposition)) {
                    // CraftBukkit start - Ignition by dispensing flint and steel
                    if (!org.bukkit.craftbukkit.event.CraftEventFactory.callBlockIgniteEvent(world, blockposition.getX(), blockposition.getY(), blockposition.getZ(), isourceblock.getBlockPos().getX(), isourceblock.getBlockPos().getY(), isourceblock.getBlockPos().getZ()).isCancelled()) {
                        world.setBlockState(blockposition, Blocks.FIRE.getDefaultState());
                        if (itemstack.attemptDamageItem(1, world.rand, (EntityPlayerMP) null)) {
                            itemstack.setCount(0);
                        }
                    }
                    // CraftBukkit end
                } else if (world.getBlockState(blockposition).getBlock() == Blocks.TNT) {
                    Blocks.TNT.onBlockDestroyedByPlayer(world, blockposition, Blocks.TNT.getDefaultState().withProperty(BlockTNT.EXPLODE, Boolean.valueOf(true)));
                    world.setBlockToAir(blockposition);
                } else {
                    this.b = false;
                }

                return itemstack;
            }
        });
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(Items.DYE, new DispenserRegistry.b() {
            protected ItemStack dispenseStack(IBlockSource isourceblock, ItemStack itemstack) {
                this.b = true;
                if (EnumDyeColor.WHITE == EnumDyeColor.byDyeDamage(itemstack.getMetadata())) {
                    World world = isourceblock.getWorld();
                    BlockPos blockposition = isourceblock.getBlockPos().offset((EnumFacing) isourceblock.getBlockState().getValue(BlockDispenser.FACING));

                    // CraftBukkit start
                    org.bukkit.block.Block block = world.getWorld().getBlockAt(isourceblock.getBlockPos().getX(), isourceblock.getBlockPos().getY(), isourceblock.getBlockPos().getZ());
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
                        IBehaviorDispenseItem idispensebehavior = (IBehaviorDispenseItem) BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.getObject(eventStack.getItem());
                        if (idispensebehavior != IBehaviorDispenseItem.DEFAULT_BEHAVIOR && idispensebehavior != this) {
                            idispensebehavior.dispense(isourceblock, eventStack);
                            return itemstack;
                        }
                    }

                    world.captureTreeGeneration = true;
                    // CraftBukkit end

                    if (ItemDye.applyBonemeal(itemstack, world, blockposition)) {
                        if (!world.isRemote) {
                            world.playEvent(2005, blockposition, 0);
                        }
                    } else {
                        this.b = false;
                    }
                    // CraftBukkit start
                    world.captureTreeGeneration = false;
                    if (world.capturedBlockStates.size() > 0) {
                        TreeType treeType = BlockSapling.treeType;
                        BlockSapling.treeType = null;
                        Location location = new Location(world.getWorld(), blockposition.getX(), blockposition.getY(), blockposition.getZ());
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
                    return super.dispenseStack(isourceblock, itemstack);
                }
            }
        });
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(Item.getItemFromBlock(Blocks.TNT), new BehaviorDefaultDispenseItem() {
            protected ItemStack dispenseStack(IBlockSource isourceblock, ItemStack itemstack) {
                World world = isourceblock.getWorld();
                BlockPos blockposition = isourceblock.getBlockPos().offset((EnumFacing) isourceblock.getBlockState().getValue(BlockDispenser.FACING));
                // EntityTNTPrimed entitytntprimed = new EntityTNTPrimed(world, (double) blockposition.getX() + 0.5D, (double) blockposition.getY(), (double) blockposition.getZ() + 0.5D, (EntityLiving) null);

                // CraftBukkit start
                ItemStack itemstack1 = itemstack.splitStack(1);
                org.bukkit.block.Block block = world.getWorld().getBlockAt(isourceblock.getBlockPos().getX(), isourceblock.getBlockPos().getY(), isourceblock.getBlockPos().getZ());
                CraftItemStack craftItem = CraftItemStack.asCraftMirror(itemstack1);

                BlockDispenseEvent event = new BlockDispenseEvent(block, craftItem.clone(), new org.bukkit.util.Vector((double) blockposition.getX() + 0.5D, (double) blockposition.getY(), (double) blockposition.getZ() + 0.5D));
                if (!BlockDispenser.eventFired) {
                   world.getServer().getPluginManager().callEvent(event);
                }

                if (event.isCancelled()) {
                    itemstack.grow(1);
                    return itemstack;
                }

                if (!event.getItem().equals(craftItem)) {
                    itemstack.grow(1);
                    // Chain to handler for new item
                    ItemStack eventStack = CraftItemStack.asNMSCopy(event.getItem());
                    IBehaviorDispenseItem idispensebehavior = (IBehaviorDispenseItem) BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.getObject(eventStack.getItem());
                    if (idispensebehavior != IBehaviorDispenseItem.DEFAULT_BEHAVIOR && idispensebehavior != this) {
                        idispensebehavior.dispense(isourceblock, eventStack);
                        return itemstack;
                    }
                }

                EntityTNTPrimed entitytntprimed = new EntityTNTPrimed(world, event.getVelocity().getX(), event.getVelocity().getY(), event.getVelocity().getZ(), (EntityLivingBase) null);
                // CraftBukkit end

                world.spawnEntity(entitytntprimed);
                world.playSound((EntityPlayer) null, entitytntprimed.posX, entitytntprimed.posY, entitytntprimed.posZ, SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.BLOCKS, 1.0F, 1.0F);
                // itemstack.subtract(1); // CraftBukkit - handled above
                return itemstack;
            }
        });
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(Items.SKULL, new DispenserRegistry.b() {
            protected ItemStack dispenseStack(IBlockSource isourceblock, ItemStack itemstack) {
                World world = isourceblock.getWorld();
                EnumFacing enumdirection = (EnumFacing) isourceblock.getBlockState().getValue(BlockDispenser.FACING);
                BlockPos blockposition = isourceblock.getBlockPos().offset(enumdirection);
                BlockSkull blockskull = Blocks.SKULL;

                // CraftBukkit start
                org.bukkit.block.Block bukkitBlock = world.getWorld().getBlockAt(isourceblock.getBlockPos().getX(), isourceblock.getBlockPos().getY(), isourceblock.getBlockPos().getZ());
                CraftItemStack craftItem = CraftItemStack.asCraftMirror(itemstack);

                BlockDispenseEvent event = new BlockDispenseEvent(bukkitBlock, craftItem.clone(), new org.bukkit.util.Vector(blockposition.getX(), blockposition.getY(), blockposition.getZ()));
                if (!BlockDispenser.eventFired) {
                    world.getServer().getPluginManager().callEvent(event);
                }

                if (event.isCancelled()) {
                    return itemstack;
                }

                if (!event.getItem().equals(craftItem)) {
                    // Chain to handler for new item
                    ItemStack eventStack = CraftItemStack.asNMSCopy(event.getItem());
                    IBehaviorDispenseItem idispensebehavior = (IBehaviorDispenseItem) BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.getObject(eventStack.getItem());
                    if (idispensebehavior != IBehaviorDispenseItem.DEFAULT_BEHAVIOR && idispensebehavior != this) {
                        idispensebehavior.dispense(isourceblock, eventStack);
                        return itemstack;
                    }
                }
                // CraftBukkit end

                this.b = true;
                if (world.isAirBlock(blockposition) && blockskull.canDispenserPlace(world, blockposition, itemstack)) {
                    if (!world.isRemote) {
                        world.setBlockState(blockposition, blockskull.getDefaultState().withProperty(BlockSkull.FACING, EnumFacing.UP), 3);
                        TileEntity tileentity = world.getTileEntity(blockposition);

                        if (tileentity instanceof TileEntitySkull) {
                            if (itemstack.getMetadata() == 3) {
                                GameProfile gameprofile = null;

                                if (itemstack.hasTagCompound()) {
                                    NBTTagCompound nbttagcompound = itemstack.getTagCompound();

                                    if (nbttagcompound.hasKey("SkullOwner", 10)) {
                                        gameprofile = NBTUtil.readGameProfileFromNBT(nbttagcompound.getCompoundTag("SkullOwner"));
                                    } else if (nbttagcompound.hasKey("SkullOwner", 8)) {
                                        String s = nbttagcompound.getString("SkullOwner");

                                        if (!StringUtils.isNullOrEmpty(s)) {
                                            gameprofile = new GameProfile((UUID) null, s);
                                        }
                                    }
                                }

                                ((TileEntitySkull) tileentity).setPlayerProfile(gameprofile);
                            } else {
                                ((TileEntitySkull) tileentity).setType(itemstack.getMetadata());
                            }

                            ((TileEntitySkull) tileentity).setSkullRotation(enumdirection.getOpposite().getHorizontalIndex() * 4);
                            Blocks.SKULL.checkWitherSpawn(world, blockposition, (TileEntitySkull) tileentity);
                        }

                        itemstack.shrink(1);
                    }
                } else if (ItemArmor.dispenseArmor(isourceblock, itemstack).isEmpty()) {
                    this.b = false;
                }

                return itemstack;
            }
        });
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(Item.getItemFromBlock(Blocks.PUMPKIN), new DispenserRegistry.b() {
            protected ItemStack dispenseStack(IBlockSource isourceblock, ItemStack itemstack) {
                World world = isourceblock.getWorld();
                BlockPos blockposition = isourceblock.getBlockPos().offset((EnumFacing) isourceblock.getBlockState().getValue(BlockDispenser.FACING));
                BlockPumpkin blockpumpkin = (BlockPumpkin) Blocks.PUMPKIN;

                // CraftBukkit start
                org.bukkit.block.Block bukkitBlock = world.getWorld().getBlockAt(isourceblock.getBlockPos().getX(), isourceblock.getBlockPos().getY(), isourceblock.getBlockPos().getZ());
                CraftItemStack craftItem = CraftItemStack.asCraftMirror(itemstack);

                BlockDispenseEvent event = new BlockDispenseEvent(bukkitBlock, craftItem.clone(), new org.bukkit.util.Vector(blockposition.getX(), blockposition.getY(), blockposition.getZ()));
                if (!BlockDispenser.eventFired) {
                    world.getServer().getPluginManager().callEvent(event);
                }

                if (event.isCancelled()) {
                    return itemstack;
                }

                if (!event.getItem().equals(craftItem)) {
                    // Chain to handler for new item
                    ItemStack eventStack = CraftItemStack.asNMSCopy(event.getItem());
                    IBehaviorDispenseItem idispensebehavior = (IBehaviorDispenseItem) BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.getObject(eventStack.getItem());
                    if (idispensebehavior != IBehaviorDispenseItem.DEFAULT_BEHAVIOR && idispensebehavior != this) {
                        idispensebehavior.dispense(isourceblock, eventStack);
                        return itemstack;
                    }
                }
                // CraftBukkit end

                this.b = true;
                if (world.isAirBlock(blockposition) && blockpumpkin.canDispenserPlace(world, blockposition)) {
                    if (!world.isRemote) {
                        world.setBlockState(blockposition, blockpumpkin.getDefaultState(), 3);
                    }

                    itemstack.shrink(1);
                } else {
                    ItemStack itemstack1 = ItemArmor.dispenseArmor(isourceblock, itemstack);

                    if (itemstack1.isEmpty()) {
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

            BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(Item.getItemFromBlock(BlockShulkerBox.getBlockByColor(enumcolor)), new DispenserRegistry.c(null));
        }

    }

    public static void register() {
        if (!Bootstrap.alreadyRegistered) {
            Bootstrap.alreadyRegistered = true;
            redirectOutputToLog();
            SoundEvent.registerSounds();
            Block.registerBlocks();
            BlockFire.init();
            Potion.registerPotions();
            Enchantment.registerEnchantments();
            Item.registerItems();
            PotionType.registerPotionTypes();
            PotionHelper.init();
            EntityList.init();
            Biome.registerBiomes();
            registerDispenserBehaviors();
            if (!CraftingManager.init()) {
                Bootstrap.hasErrored = true;
                Bootstrap.LOGGER.error("Errors with built-in recipes!");
            }

            StatList.init();
            if (Bootstrap.LOGGER.isDebugEnabled()) {
                if ((new AdvancementManager((File) null)).hasErrored()) {
                    Bootstrap.hasErrored = true;
                    Bootstrap.LOGGER.error("Errors with built-in advancements!");
                }

                if (!LootTableList.test()) {
                    Bootstrap.hasErrored = true;
                    Bootstrap.LOGGER.error("Errors with built-in loot tables");
                }
            }

        }
    }

    private static void redirectOutputToLog() {
        if (Bootstrap.LOGGER.isDebugEnabled()) {
            System.setErr(new DebugLoggingPrintStream("STDERR", System.err));
            System.setOut(new DebugLoggingPrintStream("STDOUT", Bootstrap.SYSOUT));
        } else {
            System.setErr(new LoggingPrintStream("STDERR", System.err));
            System.setOut(new LoggingPrintStream("STDOUT", Bootstrap.SYSOUT));
        }

    }

    static class c extends DispenserRegistry.b {

        private c() {}

        protected ItemStack dispenseStack(IBlockSource isourceblock, ItemStack itemstack) {
            Block block = Block.getBlockFromItem(itemstack.getItem());
            World world = isourceblock.getWorld();
            EnumFacing enumdirection = (EnumFacing) isourceblock.getBlockState().getValue(BlockDispenser.FACING);
            BlockPos blockposition = isourceblock.getBlockPos().offset(enumdirection);

            // CraftBukkit start
            org.bukkit.block.Block bukkitBlock = world.getWorld().getBlockAt(isourceblock.getBlockPos().getX(), isourceblock.getBlockPos().getY(), isourceblock.getBlockPos().getZ());
            CraftItemStack craftItem = CraftItemStack.asCraftMirror(itemstack);

            BlockDispenseEvent event = new BlockDispenseEvent(bukkitBlock, craftItem.clone(), new org.bukkit.util.Vector(blockposition.getX(), blockposition.getY(), blockposition.getZ()));
            if (!BlockDispenser.eventFired) {
                world.getServer().getPluginManager().callEvent(event);
            }

            if (event.isCancelled()) {
                return itemstack;
            }

            if (!event.getItem().equals(craftItem)) {
                // Chain to handler for new item
                ItemStack eventStack = CraftItemStack.asNMSCopy(event.getItem());
                IBehaviorDispenseItem idispensebehavior = (IBehaviorDispenseItem) BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.getObject(eventStack.getItem());
                if (idispensebehavior != IBehaviorDispenseItem.DEFAULT_BEHAVIOR && idispensebehavior != this) {
                    idispensebehavior.dispense(isourceblock, eventStack);
                    return itemstack;
                }
            }
            // CraftBukkit end

            this.b = world.mayPlace(block, blockposition, false, EnumFacing.DOWN, (Entity) null);
            if (this.b) {
                EnumFacing enumdirection1 = world.isAirBlock(blockposition.down()) ? enumdirection : EnumFacing.UP;
                IBlockState iblockdata = block.getDefaultState().withProperty(BlockShulkerBox.FACING, enumdirection1);

                world.setBlockState(blockposition, iblockdata);
                TileEntity tileentity = world.getTileEntity(blockposition);
                ItemStack itemstack1 = itemstack.splitStack(1);

                if (itemstack1.hasTagCompound()) {
                    ((TileEntityShulkerBox) tileentity).loadFromNbt(itemstack1.getTagCompound().getCompoundTag("BlockEntityTag"));
                }

                if (itemstack1.hasDisplayName()) {
                    ((TileEntityShulkerBox) tileentity).setCustomName(itemstack1.getDisplayName());
                }

                world.updateComparatorOutputLevel(blockposition, iblockdata.getBlock());
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

        protected void playDispenseSound(IBlockSource isourceblock) {
            isourceblock.getWorld().playEvent(this.b ? 1000 : 1001, isourceblock.getBlockPos(), 0);
        }
    }

    public static class a extends BehaviorDefaultDispenseItem {

        private final BehaviorDefaultDispenseItem b = new BehaviorDefaultDispenseItem();
        private final EntityBoat.Type c;

        public a(EntityBoat.Type entityboat_enumboattype) {
            this.c = entityboat_enumboattype;
        }

        public ItemStack dispenseStack(IBlockSource isourceblock, ItemStack itemstack) {
            EnumFacing enumdirection = (EnumFacing) isourceblock.getBlockState().getValue(BlockDispenser.FACING);
            World world = isourceblock.getWorld();
            double d0 = isourceblock.getX() + (double) ((float) enumdirection.getFrontOffsetX() * 1.125F);
            double d1 = isourceblock.getY() + (double) ((float) enumdirection.getFrontOffsetY() * 1.125F);
            double d2 = isourceblock.getZ() + (double) ((float) enumdirection.getFrontOffsetZ() * 1.125F);
            BlockPos blockposition = isourceblock.getBlockPos().offset(enumdirection);
            Material material = world.getBlockState(blockposition).getMaterial();
            double d3;

            if (Material.WATER.equals(material)) {
                d3 = 1.0D;
            } else {
                if (!Material.AIR.equals(material) || !Material.WATER.equals(world.getBlockState(blockposition.down()).getMaterial())) {
                    return this.b.dispense(isourceblock, itemstack);
                }

                d3 = 0.0D;
            }

            // EntityBoat entityboat = new EntityBoat(world, d0, d1 + d3, d2);
            // CraftBukkit start
            ItemStack itemstack1 = itemstack.splitStack(1);
            org.bukkit.block.Block block = world.getWorld().getBlockAt(isourceblock.getBlockPos().getX(), isourceblock.getBlockPos().getY(), isourceblock.getBlockPos().getZ());
            CraftItemStack craftItem = CraftItemStack.asCraftMirror(itemstack1);

            BlockDispenseEvent event = new BlockDispenseEvent(block, craftItem.clone(), new org.bukkit.util.Vector(d0, d1 + d3, d2));
            if (!BlockDispenser.eventFired) {
                world.getServer().getPluginManager().callEvent(event);
            }

            if (event.isCancelled()) {
                itemstack.grow(1);
                return itemstack;
            }

            if (!event.getItem().equals(craftItem)) {
                itemstack.grow(1);
                // Chain to handler for new item
                ItemStack eventStack = CraftItemStack.asNMSCopy(event.getItem());
                IBehaviorDispenseItem idispensebehavior = (IBehaviorDispenseItem) BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.getObject(eventStack.getItem());
                if (idispensebehavior != IBehaviorDispenseItem.DEFAULT_BEHAVIOR && idispensebehavior != this) {
                    idispensebehavior.dispense(isourceblock, eventStack);
                    return itemstack;
                }
            }

            EntityBoat entityboat = new EntityBoat(world, event.getVelocity().getX(), event.getVelocity().getY(), event.getVelocity().getZ());
            // CraftBukkit end

            entityboat.setBoatType(this.c);
            entityboat.rotationYaw = enumdirection.getHorizontalAngle();
            if (!world.spawnEntity(entityboat)) itemstack.grow(1); // CraftBukkit
            // itemstack.subtract(1); // CraftBukkit - handled during event processing
            return itemstack;
        }

        protected void playDispenseSound(IBlockSource isourceblock) {
            isourceblock.getWorld().playEvent(1000, isourceblock.getBlockPos(), 0);
        }
    }
}
