package net.minecraft.entity.projectile;

import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.world.World;

public class EntityTippedArrow extends EntityArrow {

    private static final DataParameter<Integer> COLOR = EntityDataManager.createKey(EntityTippedArrow.class, DataSerializers.VARINT);
    private PotionType potion;
    public final Set<PotionEffect> customPotionEffects;
    private boolean fixedColor;

    public EntityTippedArrow(World world) {
        super(world);
        this.potion = PotionTypes.EMPTY;
        this.customPotionEffects = Sets.newHashSet();
    }

    public EntityTippedArrow(World world, double d0, double d1, double d2) {
        super(world, d0, d1, d2);
        this.potion = PotionTypes.EMPTY;
        this.customPotionEffects = Sets.newHashSet();
    }

    public EntityTippedArrow(World world, EntityLivingBase entityliving) {
        super(world, entityliving);
        this.potion = PotionTypes.EMPTY;
        this.customPotionEffects = Sets.newHashSet();
    }

    public void setPotionEffect(ItemStack itemstack) {
        if (itemstack.getItem() == Items.TIPPED_ARROW) {
            this.potion = PotionUtils.getPotionFromItem(itemstack);
            List list = PotionUtils.getFullEffectsFromItem(itemstack);

            if (!list.isEmpty()) {
                Iterator iterator = list.iterator();

                while (iterator.hasNext()) {
                    PotionEffect mobeffect = (PotionEffect) iterator.next();

                    this.customPotionEffects.add(new PotionEffect(mobeffect));
                }
            }

            int i = getCustomColor(itemstack);

            if (i == -1) {
                this.refreshColor();
            } else {
                this.setFixedColor(i);
            }
        } else if (itemstack.getItem() == Items.ARROW) {
            this.potion = PotionTypes.EMPTY;
            this.customPotionEffects.clear();
            this.dataManager.set(EntityTippedArrow.COLOR, Integer.valueOf(-1));
        }

    }

    public static int getCustomColor(ItemStack itemstack) {
        NBTTagCompound nbttagcompound = itemstack.getTagCompound();

        return nbttagcompound != null && nbttagcompound.hasKey("CustomPotionColor", 99) ? nbttagcompound.getInteger("CustomPotionColor") : -1;
    }

    private void refreshColor() {
        this.fixedColor = false;
        this.dataManager.set(EntityTippedArrow.COLOR, Integer.valueOf(PotionUtils.getPotionColorFromEffectList((Collection) PotionUtils.mergeEffects(this.potion, (Collection) this.customPotionEffects))));
    }

    public void addEffect(PotionEffect mobeffect) {
        this.customPotionEffects.add(mobeffect);
        this.getDataManager().set(EntityTippedArrow.COLOR, Integer.valueOf(PotionUtils.getPotionColorFromEffectList((Collection) PotionUtils.mergeEffects(this.potion, (Collection) this.customPotionEffects))));
    }

    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(EntityTippedArrow.COLOR, Integer.valueOf(-1));
    }

    public void onUpdate() {
        super.onUpdate();
        if (this.world.isRemote) {
            if (this.inGround) {
                if (this.timeInGround % 5 == 0) {
                    this.spawnPotionParticles(1);
                }
            } else {
                this.spawnPotionParticles(2);
            }
        } else if (this.inGround && this.timeInGround != 0 && !this.customPotionEffects.isEmpty() && this.timeInGround >= 600) {
            this.world.setEntityState(this, (byte) 0);
            this.potion = PotionTypes.EMPTY;
            this.customPotionEffects.clear();
            this.dataManager.set(EntityTippedArrow.COLOR, Integer.valueOf(-1));
        }

    }

    private void spawnPotionParticles(int i) {
        int j = this.getColor();

        if (j != -1 && i > 0) {
            double d0 = (double) (j >> 16 & 255) / 255.0D;
            double d1 = (double) (j >> 8 & 255) / 255.0D;
            double d2 = (double) (j >> 0 & 255) / 255.0D;

            for (int k = 0; k < i; ++k) {
                this.world.spawnParticle(EnumParticleTypes.SPELL_MOB, this.posX + (this.rand.nextDouble() - 0.5D) * (double) this.width, this.posY + this.rand.nextDouble() * (double) this.height, this.posZ + (this.rand.nextDouble() - 0.5D) * (double) this.width, d0, d1, d2, new int[0]);
            }

        }
    }

    // CraftBukkit start accessor methods
    public void refreshEffects() {
        this.getDataManager().set(EntityTippedArrow.COLOR, Integer.valueOf(PotionUtils.getPotionColorFromEffectList((Collection) PotionUtils.mergeEffects(this.potion, (Collection) this.customPotionEffects))));
    }

    public String getType() {
        return ((ResourceLocation) PotionType.REGISTRY.getNameForObject(this.potion)).toString();
    }

    public void setType(String string) {
        this.potion = PotionType.REGISTRY.getObject(new ResourceLocation(string));
        this.dataManager.set(EntityTippedArrow.COLOR, Integer.valueOf(PotionUtils.getPotionColorFromEffectList((Collection) PotionUtils.mergeEffects(this.potion, (Collection) this.customPotionEffects))));
    }

    public boolean isTipped() {
        return !(this.customPotionEffects.isEmpty() && this.potion == PotionTypes.EMPTY);
    }
    // CraftBukkit end

    public int getColor() {
        return ((Integer) this.dataManager.get(EntityTippedArrow.COLOR)).intValue();
    }

    public void setFixedColor(int i) {
        this.fixedColor = true;
        this.dataManager.set(EntityTippedArrow.COLOR, Integer.valueOf(i));
    }

    public static void registerFixesTippedArrow(DataFixer dataconvertermanager) {
        EntityArrow.registerFixesArrow(dataconvertermanager, "TippedArrow");
    }

    public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
        super.writeEntityToNBT(nbttagcompound);
        if (this.potion != PotionTypes.EMPTY && this.potion != null) {
            nbttagcompound.setString("Potion", ((ResourceLocation) PotionType.REGISTRY.getNameForObject(this.potion)).toString());
        }

        if (this.fixedColor) {
            nbttagcompound.setInteger("Color", this.getColor());
        }

        if (!this.customPotionEffects.isEmpty()) {
            NBTTagList nbttaglist = new NBTTagList();
            Iterator iterator = this.customPotionEffects.iterator();

            while (iterator.hasNext()) {
                PotionEffect mobeffect = (PotionEffect) iterator.next();

                nbttaglist.appendTag(mobeffect.writeCustomPotionEffectToNBT(new NBTTagCompound()));
            }

            nbttagcompound.setTag("CustomPotionEffects", nbttaglist);
        }

    }

    public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
        super.readEntityFromNBT(nbttagcompound);
        if (nbttagcompound.hasKey("Potion", 8)) {
            this.potion = PotionUtils.getPotionTypeFromNBT(nbttagcompound);
        }

        Iterator iterator = PotionUtils.getFullEffectsFromTag(nbttagcompound).iterator();

        while (iterator.hasNext()) {
            PotionEffect mobeffect = (PotionEffect) iterator.next();

            this.addEffect(mobeffect);
        }

        if (nbttagcompound.hasKey("Color", 99)) {
            this.setFixedColor(nbttagcompound.getInteger("Color"));
        } else {
            this.refreshColor();
        }

    }

    protected void arrowHit(EntityLivingBase entityliving) {
        super.arrowHit(entityliving);
        Iterator iterator = this.potion.getEffects().iterator();

        PotionEffect mobeffect;

        while (iterator.hasNext()) {
            mobeffect = (PotionEffect) iterator.next();
            entityliving.addPotionEffect(new PotionEffect(mobeffect.getPotion(), Math.max(mobeffect.getDuration() / 8, 1), mobeffect.getAmplifier(), mobeffect.getIsAmbient(), mobeffect.doesShowParticles()));
        }

        if (!this.customPotionEffects.isEmpty()) {
            iterator = this.customPotionEffects.iterator();

            while (iterator.hasNext()) {
                mobeffect = (PotionEffect) iterator.next();
                entityliving.addPotionEffect(mobeffect);
            }
        }

    }

    protected ItemStack getArrowStack() {
        if (this.customPotionEffects.isEmpty() && this.potion == PotionTypes.EMPTY) {
            return new ItemStack(Items.ARROW);
        } else {
            ItemStack itemstack = new ItemStack(Items.TIPPED_ARROW);

            PotionUtils.addPotionToItemStack(itemstack, this.potion);
            PotionUtils.appendEffects(itemstack, (Collection) this.customPotionEffects);
            if (this.fixedColor) {
                NBTTagCompound nbttagcompound = itemstack.getTagCompound();

                if (nbttagcompound == null) {
                    nbttagcompound = new NBTTagCompound();
                    itemstack.setTagCompound(nbttagcompound);
                }

                nbttagcompound.setInteger("CustomPotionColor", this.getColor());
            }

            return itemstack;
        }
    }
}
