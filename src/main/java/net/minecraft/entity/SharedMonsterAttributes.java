package net.minecraft.entity;

import java.util.Collection;
import java.util.Iterator;
import java.util.UUID;
import javax.annotation.Nullable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class SharedMonsterAttributes {

    private static final Logger LOGGER = LogManager.getLogger();
    // Spigot start
    public static final IAttribute MAX_HEALTH = (new RangedAttribute((IAttribute) null, "generic.maxHealth", 20.0D, 0.0D, org.spigotmc.SpigotConfig.maxHealth)).setDescription("Max Health").setShouldWatch(true);
    public static final IAttribute FOLLOW_RANGE = (new RangedAttribute((IAttribute) null, "generic.followRange", 32.0D, 0.0D, 2048.0D)).setDescription("Follow Range");
    public static final IAttribute KNOCKBACK_RESISTANCE = (new RangedAttribute((IAttribute) null, "generic.knockbackResistance", 0.0D, 0.0D, 1.0D)).setDescription("Knockback Resistance");
    public static final IAttribute MOVEMENT_SPEED = (new RangedAttribute((IAttribute) null, "generic.movementSpeed", 0.699999988079071D, 0.0D, org.spigotmc.SpigotConfig.movementSpeed)).setDescription("Movement Speed").setShouldWatch(true);
    public static final IAttribute FLYING_SPEED = (new RangedAttribute((IAttribute) null, "generic.flyingSpeed", 0.4000000059604645D, 0.0D, 1024.0D)).setDescription("Flying Speed").setShouldWatch(true);
    public static final IAttribute ATTACK_DAMAGE = new RangedAttribute((IAttribute) null, "generic.attackDamage", 2.0D, 0.0D, org.spigotmc.SpigotConfig.attackDamage);
    public static final IAttribute ATTACK_SPEED = (new RangedAttribute((IAttribute) null, "generic.attackSpeed", 4.0D, 0.0D, 1024.0D)).setShouldWatch(true);
    public static final IAttribute ARMOR = (new RangedAttribute((IAttribute) null, "generic.armor", 0.0D, 0.0D, 30.0D)).setShouldWatch(true);
    public static final IAttribute ARMOR_TOUGHNESS = (new RangedAttribute((IAttribute) null, "generic.armorToughness", 0.0D, 0.0D, 20.0D)).setShouldWatch(true);
    public static final IAttribute LUCK = (new RangedAttribute((IAttribute) null, "generic.luck", 0.0D, -1024.0D, 1024.0D)).setShouldWatch(true);
    // Spigot end

    public static NBTTagList writeBaseAttributeMapToNBT(AbstractAttributeMap attributemapbase) {
        NBTTagList nbttaglist = new NBTTagList();
        Iterator iterator = attributemapbase.getAllAttributes().iterator();

        while (iterator.hasNext()) {
            IAttributeInstance attributeinstance = (IAttributeInstance) iterator.next();

            nbttaglist.appendTag(writeAttributeInstanceToNBT(attributeinstance));
        }

        return nbttaglist;
    }

    private static NBTTagCompound writeAttributeInstanceToNBT(IAttributeInstance attributeinstance) {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        IAttribute iattribute = attributeinstance.getAttribute();

        nbttagcompound.setString("Name", iattribute.getName());
        nbttagcompound.setDouble("Base", attributeinstance.getBaseValue());
        Collection collection = attributeinstance.getModifiers();

        if (collection != null && !collection.isEmpty()) {
            NBTTagList nbttaglist = new NBTTagList();
            Iterator iterator = collection.iterator();

            while (iterator.hasNext()) {
                AttributeModifier attributemodifier = (AttributeModifier) iterator.next();

                if (attributemodifier.isSaved()) {
                    nbttaglist.appendTag(writeAttributeModifierToNBT(attributemodifier));
                }
            }

            nbttagcompound.setTag("Modifiers", nbttaglist);
        }

        return nbttagcompound;
    }

    public static NBTTagCompound writeAttributeModifierToNBT(AttributeModifier attributemodifier) {
        NBTTagCompound nbttagcompound = new NBTTagCompound();

        nbttagcompound.setString("Name", attributemodifier.getName());
        nbttagcompound.setDouble("Amount", attributemodifier.getAmount());
        nbttagcompound.setInteger("Operation", attributemodifier.getOperation());
        nbttagcompound.setUniqueId("UUID", attributemodifier.getID());
        return nbttagcompound;
    }

    public static void setAttributeModifiers(AbstractAttributeMap attributemapbase, NBTTagList nbttaglist) {
        for (int i = 0; i < nbttaglist.tagCount(); ++i) {
            NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
            IAttributeInstance attributeinstance = attributemapbase.getAttributeInstanceByName(nbttagcompound.getString("Name"));

            if (attributeinstance == null) {
                SharedMonsterAttributes.LOGGER.warn("Ignoring unknown attribute \'{}\'", nbttagcompound.getString("Name"));
            } else {
                applyModifiersToAttributeInstance(attributeinstance, nbttagcompound);
            }
        }

    }

    private static void applyModifiersToAttributeInstance(IAttributeInstance attributeinstance, NBTTagCompound nbttagcompound) {
        attributeinstance.setBaseValue(nbttagcompound.getDouble("Base"));
        if (nbttagcompound.hasKey("Modifiers", 9)) {
            NBTTagList nbttaglist = nbttagcompound.getTagList("Modifiers", 10);

            for (int i = 0; i < nbttaglist.tagCount(); ++i) {
                AttributeModifier attributemodifier = readAttributeModifierFromNBT(nbttaglist.getCompoundTagAt(i));

                if (attributemodifier != null) {
                    AttributeModifier attributemodifier1 = attributeinstance.getModifier(attributemodifier.getID());

                    if (attributemodifier1 != null) {
                        attributeinstance.removeModifier(attributemodifier1);
                    }

                    attributeinstance.applyModifier(attributemodifier);
                }
            }
        }

    }

    @Nullable
    public static AttributeModifier readAttributeModifierFromNBT(NBTTagCompound nbttagcompound) {
        UUID uuid = nbttagcompound.getUniqueId("UUID");

        try {
            return new AttributeModifier(uuid, nbttagcompound.getString("Name"), nbttagcompound.getDouble("Amount"), nbttagcompound.getInteger("Operation"));
        } catch (Exception exception) {
            SharedMonsterAttributes.LOGGER.warn("Unable to create attribute: {}", exception.getMessage());
            return null;
        }
    }
}
