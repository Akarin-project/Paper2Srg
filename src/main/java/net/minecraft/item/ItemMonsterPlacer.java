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
        this.func_77637_a(CreativeTabs.field_78026_f);
    }

    public String func_77653_i(ItemStack itemstack) {
        String s = ("" + I18n.func_74838_a(this.func_77658_a() + ".name")).trim();
        String s1 = EntityList.func_191302_a(func_190908_h(itemstack));

        if (s1 != null) {
            s = s + " " + I18n.func_74838_a("entity." + s1 + ".name");
        }

        return s;
    }

    public EnumActionResult func_180614_a(EntityPlayer entityhuman, World world, BlockPos blockposition, EnumHand enumhand, EnumFacing enumdirection, float f, float f1, float f2) {
        ItemStack itemstack = entityhuman.func_184586_b(enumhand);

        if (world.field_72995_K) {
            return EnumActionResult.SUCCESS;
        } else if (!entityhuman.func_175151_a(blockposition.func_177972_a(enumdirection), enumdirection, itemstack)) {
            return EnumActionResult.FAIL;
        } else {
            IBlockState iblockdata = world.func_180495_p(blockposition);
            Block block = iblockdata.func_177230_c();

            if (block == Blocks.field_150474_ac) {
                TileEntity tileentity = world.func_175625_s(blockposition);

                if (tileentity instanceof TileEntityMobSpawner) {
                    MobSpawnerBaseLogic mobspawnerabstract = ((TileEntityMobSpawner) tileentity).func_145881_a();

                    mobspawnerabstract.func_190894_a(func_190908_h(itemstack));
                    tileentity.func_70296_d();
                    world.func_184138_a(blockposition, iblockdata, iblockdata, 3);
                    if (!entityhuman.field_71075_bZ.field_75098_d) {
                        itemstack.func_190918_g(1);
                    }

                    return EnumActionResult.SUCCESS;
                }
            }

            BlockPos blockposition1 = blockposition.func_177972_a(enumdirection);
            double d0 = this.func_190909_a(world, blockposition1);
            Entity entity = func_77840_a(world, func_190908_h(itemstack), (double) blockposition1.func_177958_n() + 0.5D, (double) blockposition1.func_177956_o() + d0, (double) blockposition1.func_177952_p() + 0.5D);

            if (entity != null) {
                if (entity instanceof EntityLivingBase && itemstack.func_82837_s()) {
                    entity.func_96094_a(itemstack.func_82833_r());
                }

                func_185079_a(world, entityhuman, itemstack, entity);
                if (!entityhuman.field_71075_bZ.field_75098_d) {
                    itemstack.func_190918_g(1);
                }
            }

            return EnumActionResult.SUCCESS;
        }
    }

    protected double func_190909_a(World world, BlockPos blockposition) {
        AxisAlignedBB axisalignedbb = (new AxisAlignedBB(blockposition)).func_72321_a(0.0D, -1.0D, 0.0D);
        List list = world.func_184144_a((Entity) null, axisalignedbb);

        if (list.isEmpty()) {
            return 0.0D;
        } else {
            double d0 = axisalignedbb.field_72338_b;

            AxisAlignedBB axisalignedbb1;

            for (Iterator iterator = list.iterator(); iterator.hasNext(); d0 = Math.max(axisalignedbb1.field_72337_e, d0)) {
                axisalignedbb1 = (AxisAlignedBB) iterator.next();
            }

            return d0 - (double) blockposition.func_177956_o();
        }
    }

    public static void func_185079_a(World world, @Nullable EntityPlayer entityhuman, ItemStack itemstack, @Nullable Entity entity) {
        MinecraftServer minecraftserver = world.func_73046_m();

        if (minecraftserver != null && entity != null) {
            NBTTagCompound nbttagcompound = itemstack.func_77978_p();

            if (nbttagcompound != null && nbttagcompound.func_150297_b("EntityTag", 10)) {
                if (!world.field_72995_K && entity.func_184213_bq() && (entityhuman == null || !minecraftserver.func_184103_al().func_152596_g(entityhuman.func_146103_bH()))) {
                    return;
                }

                NBTTagCompound nbttagcompound1 = entity.func_189511_e(new NBTTagCompound());
                UUID uuid = entity.func_110124_au();

                // Paper start - Filter out position and motion information
                final NBTTagCompound entityTag = nbttagcompound.func_74775_l("EntityTag");
                if (world.paperConfig.filterNBTFromSpawnEgg) {
                    entityTag.func_82580_o("Pos");
                    entityTag.func_82580_o("Motion");
                }
                nbttagcompound1.func_179237_a(entityTag);
                // Paper end
                entity.func_184221_a(uuid);
                entity.func_70020_e(nbttagcompound1);
            }

        }
    }

    public ActionResult<ItemStack> func_77659_a(World world, EntityPlayer entityhuman, EnumHand enumhand) {
        ItemStack itemstack = entityhuman.func_184586_b(enumhand);

        if (world.field_72995_K) {
            return new ActionResult(EnumActionResult.PASS, itemstack);
        } else {
            RayTraceResult movingobjectposition = this.func_77621_a(world, entityhuman, true);

            if (movingobjectposition != null && movingobjectposition.field_72313_a == RayTraceResult.Type.BLOCK) {
                BlockPos blockposition = movingobjectposition.func_178782_a();

                if (!(world.func_180495_p(blockposition).func_177230_c() instanceof BlockLiquid)) {
                    return new ActionResult(EnumActionResult.PASS, itemstack);
                } else if (world.func_175660_a(entityhuman, blockposition) && entityhuman.func_175151_a(blockposition, movingobjectposition.field_178784_b, itemstack)) {
                    Entity entity = func_77840_a(world, func_190908_h(itemstack), (double) blockposition.func_177958_n() + 0.5D, (double) blockposition.func_177956_o() + 0.5D, (double) blockposition.func_177952_p() + 0.5D);

                    if (entity == null) {
                        return new ActionResult(EnumActionResult.PASS, itemstack);
                    } else {
                        if (entity instanceof EntityLivingBase && itemstack.func_82837_s()) {
                            entity.func_96094_a(itemstack.func_82833_r());
                        }

                        func_185079_a(world, entityhuman, itemstack, entity);
                        if (!entityhuman.field_71075_bZ.field_75098_d) {
                            itemstack.func_190918_g(1);
                        }

                        entityhuman.func_71029_a(StatList.func_188057_b((Item) this));
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
    public static Entity func_77840_a(World world, @Nullable ResourceLocation minecraftkey, double d0, double d1, double d2) {
        return spawnCreature(world, minecraftkey, d0, d1, d2, org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason.SPAWNER_EGG);
    }

    @Nullable
    public static Entity spawnCreature(World world, @Nullable ResourceLocation minecraftkey, double d0, double d1, double d2, org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason spawnReason) {
        if (minecraftkey != null && EntityList.field_75627_a.containsKey(minecraftkey)) {
            Entity entity = null;

            for (int i = 0; i < 1; ++i) {
                entity = EntityList.func_188429_b(minecraftkey, world);
                if (entity instanceof EntityLiving) {
                    EntityLiving entityinsentient = (EntityLiving) entity;

                    entity.func_70012_b(d0, d1, d2, MathHelper.func_76142_g(world.field_73012_v.nextFloat() * 360.0F), 0.0F);
                    entityinsentient.field_70759_as = entityinsentient.field_70177_z;
                    entityinsentient.field_70761_aq = entityinsentient.field_70177_z;
                    entityinsentient.func_180482_a(world.func_175649_E(new BlockPos(entityinsentient)), (IEntityLivingData) null);
                    // CraftBukkit start - don't return an entity when CreatureSpawnEvent is canceled
                    if (!world.addEntity(entity, spawnReason)) {
                        entity = null;
                    } else {
                        entityinsentient.func_70642_aH();
                    }
                    // CraftBukkit end
                }
            }

            return entity;
        } else {
            return null;
        }
    }

    public void func_150895_a(CreativeTabs creativemodetab, NonNullList<ItemStack> nonnulllist) {
        if (this.func_194125_a(creativemodetab)) {
            Iterator iterator = EntityList.field_75627_a.values().iterator();

            while (iterator.hasNext()) {
                EntityList.EntityEggInfo entitytypes_monsteregginfo = (EntityList.EntityEggInfo) iterator.next();
                ItemStack itemstack = new ItemStack(this, 1);

                func_185078_a(itemstack, entitytypes_monsteregginfo.field_75613_a);
                nonnulllist.add(itemstack);
            }
        }

    }

    public static void func_185078_a(ItemStack itemstack, ResourceLocation minecraftkey) {
        NBTTagCompound nbttagcompound = itemstack.func_77942_o() ? itemstack.func_77978_p() : new NBTTagCompound();
        NBTTagCompound nbttagcompound1 = new NBTTagCompound();

        nbttagcompound1.func_74778_a("id", minecraftkey.toString());
        nbttagcompound.func_74782_a("EntityTag", nbttagcompound1);
        itemstack.func_77982_d(nbttagcompound);
    }

    @Nullable
    public static ResourceLocation func_190908_h(ItemStack itemstack) {
        NBTTagCompound nbttagcompound = itemstack.func_77978_p();

        if (nbttagcompound == null) {
            return null;
        } else if (!nbttagcompound.func_150297_b("EntityTag", 10)) {
            return null;
        } else {
            NBTTagCompound nbttagcompound1 = nbttagcompound.func_74775_l("EntityTag");

            if (!nbttagcompound1.func_150297_b("id", 8)) {
                return null;
            } else {
                String s = nbttagcompound1.func_74779_i("id");
                ResourceLocation minecraftkey = new ResourceLocation(s);

                if (!s.contains(":")) {
                    nbttagcompound1.func_74778_a("id", minecraftkey.toString());
                }

                return minecraftkey;
            }
        }
    }
}
