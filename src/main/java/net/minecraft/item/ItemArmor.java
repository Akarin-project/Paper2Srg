package net.minecraft.item;

import com.google.common.base.Predicates;
import com.google.common.collect.Multimap;
import java.util.List;
import java.util.UUID;

import net.minecraft.block.BlockDispenser;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.event.block.BlockDispenseEvent;

// CraftBukkit start
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.event.block.BlockDispenseEvent;
// CraftBukkit end

public class ItemArmor extends Item {

    private static final int[] field_77882_bY = new int[] { 13, 15, 16, 11};
    private static final UUID[] field_185084_n = new UUID[] { UUID.fromString("845DB27C-C624-495F-8C9F-6020A9A58B6B"), UUID.fromString("D8499B04-0E66-4726-AB29-64469D734E0D"), UUID.fromString("9F3D476D-C118-4544-8365-64846904B48E"), UUID.fromString("2AD3F246-FEE1-4E67-B886-69FD380BB150")};
    public static final String[] field_94603_a = new String[] { "minecraft:items/empty_armor_slot_boots", "minecraft:items/empty_armor_slot_leggings", "minecraft:items/empty_armor_slot_chestplate", "minecraft:items/empty_armor_slot_helmet"};
    public static final IBehaviorDispenseItem field_96605_cw = new BehaviorDefaultDispenseItem() {
        protected ItemStack func_82487_b(IBlockSource isourceblock, ItemStack itemstack) {
            ItemStack itemstack1 = ItemArmor.func_185082_a(isourceblock, itemstack);

            return itemstack1.func_190926_b() ? super.func_82487_b(isourceblock, itemstack) : itemstack1;
        }
    };
    public final EntityEquipmentSlot field_77881_a;
    public final int field_77879_b;
    public final float field_189415_e;
    public final int field_77880_c;
    private final ItemArmor.ArmorMaterial field_77878_bZ;

    public static ItemStack func_185082_a(IBlockSource isourceblock, ItemStack itemstack) {
        BlockPos blockposition = isourceblock.func_180699_d().func_177972_a((EnumFacing) isourceblock.func_189992_e().func_177229_b(BlockDispenser.field_176441_a));
        List list = isourceblock.func_82618_k().func_175647_a(EntityLivingBase.class, new AxisAlignedBB(blockposition), Predicates.and(EntitySelectors.field_180132_d, new EntitySelectors.ArmoredMob(itemstack)));

        if (list.isEmpty()) {
            return ItemStack.field_190927_a;
        } else {
            EntityLivingBase entityliving = (EntityLivingBase) list.get(0);
            EntityEquipmentSlot enumitemslot = EntityLiving.func_184640_d(itemstack);
            ItemStack itemstack1 = itemstack.func_77979_a(1);
            // CraftBukkit start
            World world = isourceblock.func_82618_k();
            org.bukkit.block.Block block = world.getWorld().getBlockAt(isourceblock.func_180699_d().func_177958_n(), isourceblock.func_180699_d().func_177956_o(), isourceblock.func_180699_d().func_177952_p());
            CraftItemStack craftItem = CraftItemStack.asCraftMirror(itemstack1);

            BlockDispenseEvent event = new BlockDispenseEvent(block, craftItem.clone(), new org.bukkit.util.Vector(0, 0, 0));
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
                IBehaviorDispenseItem idispensebehavior = (IBehaviorDispenseItem) BlockDispenser.field_149943_a.func_82594_a(eventStack.func_77973_b());
                if (idispensebehavior != IBehaviorDispenseItem.field_82483_a && idispensebehavior != ItemArmor.field_96605_cw) {
                    idispensebehavior.func_82482_a(isourceblock, eventStack);
                    return itemstack;
                }
            }
            // CraftBukkit end

            entityliving.func_184201_a(enumitemslot, itemstack1);
            if (entityliving instanceof EntityLiving) {
                ((EntityLiving) entityliving).func_184642_a(enumitemslot, 2.0F);
            }

            return itemstack;
        }
    }

    public ItemArmor(ItemArmor.ArmorMaterial itemarmor_enumarmormaterial, int i, EntityEquipmentSlot enumitemslot) {
        this.field_77878_bZ = itemarmor_enumarmormaterial;
        this.field_77881_a = enumitemslot;
        this.field_77880_c = i;
        this.field_77879_b = itemarmor_enumarmormaterial.func_78044_b(enumitemslot);
        this.func_77656_e(itemarmor_enumarmormaterial.func_78046_a(enumitemslot));
        this.field_189415_e = itemarmor_enumarmormaterial.func_189416_e();
        this.field_77777_bU = 1;
        this.func_77637_a(CreativeTabs.field_78037_j);
        BlockDispenser.field_149943_a.func_82595_a(this, ItemArmor.field_96605_cw);
    }

    public int func_77619_b() {
        return this.field_77878_bZ.func_78045_a();
    }

    public ItemArmor.ArmorMaterial func_82812_d() {
        return this.field_77878_bZ;
    }

    public boolean func_82816_b_(ItemStack itemstack) {
        if (this.field_77878_bZ != ItemArmor.ArmorMaterial.LEATHER) {
            return false;
        } else {
            NBTTagCompound nbttagcompound = itemstack.func_77978_p();

            return nbttagcompound != null && nbttagcompound.func_150297_b("display", 10) ? nbttagcompound.func_74775_l("display").func_150297_b("color", 3) : false;
        }
    }

    public int func_82814_b(ItemStack itemstack) {
        if (this.field_77878_bZ != ItemArmor.ArmorMaterial.LEATHER) {
            return 16777215;
        } else {
            NBTTagCompound nbttagcompound = itemstack.func_77978_p();

            if (nbttagcompound != null) {
                NBTTagCompound nbttagcompound1 = nbttagcompound.func_74775_l("display");

                if (nbttagcompound1 != null && nbttagcompound1.func_150297_b("color", 3)) {
                    return nbttagcompound1.func_74762_e("color");
                }
            }

            return 10511680;
        }
    }

    public void func_82815_c(ItemStack itemstack) {
        if (this.field_77878_bZ == ItemArmor.ArmorMaterial.LEATHER) {
            NBTTagCompound nbttagcompound = itemstack.func_77978_p();

            if (nbttagcompound != null) {
                NBTTagCompound nbttagcompound1 = nbttagcompound.func_74775_l("display");

                if (nbttagcompound1.func_74764_b("color")) {
                    nbttagcompound1.func_82580_o("color");
                }

            }
        }
    }

    public void func_82813_b(ItemStack itemstack, int i) {
        if (this.field_77878_bZ != ItemArmor.ArmorMaterial.LEATHER) {
            throw new UnsupportedOperationException("Can\'t dye non-leather!");
        } else {
            NBTTagCompound nbttagcompound = itemstack.func_77978_p();

            if (nbttagcompound == null) {
                nbttagcompound = new NBTTagCompound();
                itemstack.func_77982_d(nbttagcompound);
            }

            NBTTagCompound nbttagcompound1 = nbttagcompound.func_74775_l("display");

            if (!nbttagcompound.func_150297_b("display", 10)) {
                nbttagcompound.func_74782_a("display", nbttagcompound1);
            }

            nbttagcompound1.func_74768_a("color", i);
        }
    }

    public boolean func_82789_a(ItemStack itemstack, ItemStack itemstack1) {
        return this.field_77878_bZ.func_151685_b() == itemstack1.func_77973_b() ? true : super.func_82789_a(itemstack, itemstack1);
    }

    public ActionResult<ItemStack> func_77659_a(World world, EntityPlayer entityhuman, EnumHand enumhand) {
        ItemStack itemstack = entityhuman.func_184586_b(enumhand);
        EntityEquipmentSlot enumitemslot = EntityLiving.func_184640_d(itemstack);
        ItemStack itemstack1 = entityhuman.func_184582_a(enumitemslot);

        if (itemstack1.func_190926_b()) {
            entityhuman.func_184201_a(enumitemslot, itemstack.func_77946_l());
            itemstack.func_190920_e(0);
            return new ActionResult(EnumActionResult.SUCCESS, itemstack);
        } else {
            return new ActionResult(EnumActionResult.FAIL, itemstack);
        }
    }

    public Multimap<String, AttributeModifier> func_111205_h(EntityEquipmentSlot enumitemslot) {
        Multimap multimap = super.func_111205_h(enumitemslot);

        if (enumitemslot == this.field_77881_a) {
            multimap.put(SharedMonsterAttributes.field_188791_g.func_111108_a(), new AttributeModifier(ItemArmor.field_185084_n[enumitemslot.func_188454_b()], "Armor modifier", (double) this.field_77879_b, 0));
            multimap.put(SharedMonsterAttributes.field_189429_h.func_111108_a(), new AttributeModifier(ItemArmor.field_185084_n[enumitemslot.func_188454_b()], "Armor toughness", (double) this.field_189415_e, 0));
        }

        return multimap;
    }

    public static enum ArmorMaterial {

        LEATHER("leather", 5, new int[] { 1, 2, 3, 1}, 15, SoundEvents.field_187728_s, 0.0F), CHAIN("chainmail", 15, new int[] { 1, 4, 5, 2}, 12, SoundEvents.field_187713_n, 0.0F), IRON("iron", 15, new int[] { 2, 5, 6, 2}, 9, SoundEvents.field_187725_r, 0.0F), GOLD("gold", 7, new int[] { 1, 3, 5, 2}, 25, SoundEvents.field_187722_q, 0.0F), DIAMOND("diamond", 33, new int[] { 3, 6, 8, 3}, 10, SoundEvents.field_187716_o, 2.0F);

        private final String field_179243_f;
        private final int field_78048_f;
        private final int[] field_78049_g;
        private final int field_78055_h;
        private final SoundEvent field_185020_j;
        private final float field_189417_k;

        private ArmorMaterial(String s, int i, int[] aint, int j, SoundEvent soundeffect, float f) {
            this.field_179243_f = s;
            this.field_78048_f = i;
            this.field_78049_g = aint;
            this.field_78055_h = j;
            this.field_185020_j = soundeffect;
            this.field_189417_k = f;
        }

        public int func_78046_a(EntityEquipmentSlot enumitemslot) {
            return ItemArmor.field_77882_bY[enumitemslot.func_188454_b()] * this.field_78048_f;
        }

        public int func_78044_b(EntityEquipmentSlot enumitemslot) {
            return this.field_78049_g[enumitemslot.func_188454_b()];
        }

        public int func_78045_a() {
            return this.field_78055_h;
        }

        public SoundEvent func_185017_b() {
            return this.field_185020_j;
        }

        public Item func_151685_b() {
            return this == ItemArmor.ArmorMaterial.LEATHER ? Items.field_151116_aA : (this == ItemArmor.ArmorMaterial.CHAIN ? Items.field_151042_j : (this == ItemArmor.ArmorMaterial.GOLD ? Items.field_151043_k : (this == ItemArmor.ArmorMaterial.IRON ? Items.field_151042_j : (this == ItemArmor.ArmorMaterial.DIAMOND ? Items.field_151045_i : null))));
        }

        public float func_189416_e() {
            return this.field_189417_k;
        }
    }
}
