package net.minecraft.item;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.MobSpawnerBaseLogic;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

public class ItemMonsterPlacer extends Item {

    public ItemMonsterPlacer() {
        this.setCreativeTab(CreativeTabs.MISC);
    }

    public String getItemStackDisplayName(ItemStack itemstack) {
        String s = ("" + I18n.translateToLocal(this.getUnlocalizedName() + ".name")).trim();
        String s1 = EntityList.getTranslationName(getNamedIdFrom(itemstack));

        if (s1 != null) {
            s = s + " " + I18n.translateToLocal("entity." + s1 + ".name");
        }

        return s;
    }

    public EnumActionResult onItemUse(EntityPlayer entityhuman, World world, BlockPos blockposition, EnumHand enumhand, EnumFacing enumdirection, float f, float f1, float f2) {
        ItemStack itemstack = entityhuman.getHeldItem(enumhand);

        if (world.isRemote) {
            return EnumActionResult.SUCCESS;
        } else if (!entityhuman.canPlayerEdit(blockposition.offset(enumdirection), enumdirection, itemstack)) {
            return EnumActionResult.FAIL;
        } else {
            IBlockState iblockdata = world.getBlockState(blockposition);
            Block block = iblockdata.getBlock();

            if (block == Blocks.MOB_SPAWNER) {
                TileEntity tileentity = world.getTileEntity(blockposition);

                if (tileentity instanceof TileEntityMobSpawner) {
                    MobSpawnerBaseLogic mobspawnerabstract = ((TileEntityMobSpawner) tileentity).getSpawnerBaseLogic();

                    mobspawnerabstract.setEntityId(getNamedIdFrom(itemstack));
                    tileentity.markDirty();
                    world.notifyBlockUpdate(blockposition, iblockdata, iblockdata, 3);
                    if (!entityhuman.capabilities.isCreativeMode) {
                        itemstack.shrink(1);
                    }

                    return EnumActionResult.SUCCESS;
                }
            }

            BlockPos blockposition1 = blockposition.offset(enumdirection);
            double d0 = this.getYOffset(world, blockposition1);
            Entity entity = spawnCreature(world, getNamedIdFrom(itemstack), (double) blockposition1.getX() + 0.5D, (double) blockposition1.getY() + d0, (double) blockposition1.getZ() + 0.5D);

            if (entity != null) {
                if (entity instanceof EntityLivingBase && itemstack.hasDisplayName()) {
                    entity.setCustomNameTag(itemstack.getDisplayName());
                }

                applyItemEntityDataToEntity(world, entityhuman, itemstack, entity);
                if (!entityhuman.capabilities.isCreativeMode) {
                    itemstack.shrink(1);
                }
            }

            return EnumActionResult.SUCCESS;
        }
    }

    protected double getYOffset(World world, BlockPos blockposition) {
        AxisAlignedBB axisalignedbb = (new AxisAlignedBB(blockposition)).expand(0.0D, -1.0D, 0.0D);
        List list = world.getCollisionBoxes((Entity) null, axisalignedbb);

        if (list.isEmpty()) {
            return 0.0D;
        } else {
            double d0 = axisalignedbb.minY;

            AxisAlignedBB axisalignedbb1;

            for (Iterator iterator = list.iterator(); iterator.hasNext(); d0 = Math.max(axisalignedbb1.maxY, d0)) {
                axisalignedbb1 = (AxisAlignedBB) iterator.next();
            }

            return d0 - (double) blockposition.getY();
        }
    }

    public static void applyItemEntityDataToEntity(World world, @Nullable EntityPlayer entityhuman, ItemStack itemstack, @Nullable Entity entity) {
        MinecraftServer minecraftserver = world.getMinecraftServer();

        if (minecraftserver != null && entity != null) {
            NBTTagCompound nbttagcompound = itemstack.getTagCompound();

            if (nbttagcompound != null && nbttagcompound.hasKey("EntityTag", 10)) {
                if (!world.isRemote && entity.ignoreItemEntityData() && (entityhuman == null || !minecraftserver.getPlayerList().canSendCommands(entityhuman.getGameProfile()))) {
                    return;
                }

                NBTTagCompound nbttagcompound1 = entity.writeToNBT(new NBTTagCompound());
                UUID uuid = entity.getUniqueID();

                // Paper start - Filter out position and motion information
                final NBTTagCompound entityTag = nbttagcompound.getCompoundTag("EntityTag");
                if (world.paperConfig.filterNBTFromSpawnEgg) {
                    entityTag.removeTag("Pos");
                    entityTag.removeTag("Motion");
                }
                nbttagcompound1.merge(entityTag);
                // Paper end
                entity.setUniqueId(uuid);
                entity.readFromNBT(nbttagcompound1);
            }

        }
    }

    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer entityhuman, EnumHand enumhand) {
        ItemStack itemstack = entityhuman.getHeldItem(enumhand);

        if (world.isRemote) {
            return new ActionResult(EnumActionResult.PASS, itemstack);
        } else {
            RayTraceResult movingobjectposition = this.rayTrace(world, entityhuman, true);

            if (movingobjectposition != null && movingobjectposition.typeOfHit == RayTraceResult.Type.BLOCK) {
                BlockPos blockposition = movingobjectposition.getBlockPos();

                if (!(world.getBlockState(blockposition).getBlock() instanceof BlockLiquid)) {
                    return new ActionResult(EnumActionResult.PASS, itemstack);
                } else if (world.isBlockModifiable(entityhuman, blockposition) && entityhuman.canPlayerEdit(blockposition, movingobjectposition.sideHit, itemstack)) {
                    Entity entity = spawnCreature(world, getNamedIdFrom(itemstack), (double) blockposition.getX() + 0.5D, (double) blockposition.getY() + 0.5D, (double) blockposition.getZ() + 0.5D);

                    if (entity == null) {
                        return new ActionResult(EnumActionResult.PASS, itemstack);
                    } else {
                        if (entity instanceof EntityLivingBase && itemstack.hasDisplayName()) {
                            entity.setCustomNameTag(itemstack.getDisplayName());
                        }

                        applyItemEntityDataToEntity(world, entityhuman, itemstack, entity);
                        if (!entityhuman.capabilities.isCreativeMode) {
                            itemstack.shrink(1);
                        }

                        entityhuman.addStat(StatList.getObjectUseStats((Item) this));
                        return new ActionResult(EnumActionResult.SUCCESS, itemstack);
                    }
                } else {
                    return new ActionResult(EnumActionResult.FAIL, itemstack);
                }
            } else {
                return new ActionResult(EnumActionResult.PASS, itemstack);
            }
        }
    }

    @Nullable
    public static Entity spawnCreature(World world, @Nullable ResourceLocation minecraftkey, double d0, double d1, double d2) {
        return spawnCreature(world, minecraftkey, d0, d1, d2, org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason.SPAWNER_EGG);
    }

    @Nullable
    public static Entity spawnCreature(World world, @Nullable ResourceLocation minecraftkey, double d0, double d1, double d2, org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason spawnReason) {
        if (minecraftkey != null && EntityList.ENTITY_EGGS.containsKey(minecraftkey)) {
            Entity entity = null;

            for (int i = 0; i < 1; ++i) {
                entity = EntityList.createEntityByIDFromName(minecraftkey, world);
                if (entity instanceof EntityLiving) {
                    EntityLiving entityinsentient = (EntityLiving) entity;

                    entity.setLocationAndAngles(d0, d1, d2, MathHelper.wrapDegrees(world.rand.nextFloat() * 360.0F), 0.0F);
                    entityinsentient.rotationYawHead = entityinsentient.rotationYaw;
                    entityinsentient.renderYawOffset = entityinsentient.rotationYaw;
                    entityinsentient.onInitialSpawn(world.getDifficultyForLocation(new BlockPos(entityinsentient)), (IEntityLivingData) null);
                    // CraftBukkit start - don't return an entity when CreatureSpawnEvent is canceled
                    if (!world.addEntity(entity, spawnReason)) {
                        entity = null;
                    } else {
                        entityinsentient.playLivingSound();
                    }
                    // CraftBukkit end
                }
            }

            return entity;
        } else {
            return null;
        }
    }

    public void getSubItems(CreativeTabs creativemodetab, NonNullList<ItemStack> nonnulllist) {
        if (this.isInCreativeTab(creativemodetab)) {
            Iterator iterator = EntityList.ENTITY_EGGS.values().iterator();

            while (iterator.hasNext()) {
                EntityList.EntityEggInfo entitytypes_monsteregginfo = (EntityList.EntityEggInfo) iterator.next();
                ItemStack itemstack = new ItemStack(this, 1);

                applyEntityIdToItemStack(itemstack, entitytypes_monsteregginfo.spawnedID);
                nonnulllist.add(itemstack);
            }
        }

    }

    public static void applyEntityIdToItemStack(ItemStack itemstack, ResourceLocation minecraftkey) {
        NBTTagCompound nbttagcompound = itemstack.hasTagCompound() ? itemstack.getTagCompound() : new NBTTagCompound();
        NBTTagCompound nbttagcompound1 = new NBTTagCompound();

        nbttagcompound1.setString("id", minecraftkey.toString());
        nbttagcompound.setTag("EntityTag", nbttagcompound1);
        itemstack.setTagCompound(nbttagcompound);
    }

    @Nullable
    public static ResourceLocation getNamedIdFrom(ItemStack itemstack) {
        NBTTagCompound nbttagcompound = itemstack.getTagCompound();

        if (nbttagcompound == null) {
            return null;
        } else if (!nbttagcompound.hasKey("EntityTag", 10)) {
            return null;
        } else {
            NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("EntityTag");

            if (!nbttagcompound1.hasKey("id", 8)) {
                return null;
            } else {
                String s = nbttagcompound1.getString("id");
                ResourceLocation minecraftkey = new ResourceLocation(s);

                if (!s.contains(":")) {
                    nbttagcompound1.setString("id", minecraftkey.toString());
                }

                return minecraftkey;
            }
        }
    }
}
