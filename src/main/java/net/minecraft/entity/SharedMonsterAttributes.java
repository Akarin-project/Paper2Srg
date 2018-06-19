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

    private static final Logger field_151476_f = LogManager.getLogger();
    // Spigot start
    public static final IAttribute field_111267_a = (new RangedAttribute((IAttribute) null, "generic.maxHealth", 20.0D, 0.0D, org.spigotmc.SpigotConfig.maxHealth)).func_111117_a("Max Health").func_111112_a(true);
    public static final IAttribute field_111265_b = (new RangedAttribute((IAttribute) null, "generic.followRange", 32.0D, 0.0D, 2048.0D)).func_111117_a("Follow Range");
    public static final IAttribute field_111266_c = (new RangedAttribute((IAttribute) null, "generic.knockbackResistance", 0.0D, 0.0D, 1.0D)).func_111117_a("Knockback Resistance");
    public static final IAttribute field_111263_d = (new RangedAttribute((IAttribute) null, "generic.movementSpeed", 0.699999988079071D, 0.0D, org.spigotmc.SpigotConfig.movementSpeed)).func_111117_a("Movement Speed").func_111112_a(true);
    public static final IAttribute field_193334_e = (new RangedAttribute((IAttribute) null, "generic.flyingSpeed", 0.4000000059604645D, 0.0D, 1024.0D)).func_111117_a("Flying Speed").func_111112_a(true);
    public static final IAttribute field_111264_e = new RangedAttribute((IAttribute) null, "generic.attackDamage", 2.0D, 0.0D, org.spigotmc.SpigotConfig.attackDamage);
    public static final IAttribute field_188790_f = (new RangedAttribute((IAttribute) null, "generic.attackSpeed", 4.0D, 0.0D, 1024.0D)).func_111112_a(true);
    public static final IAttribute field_188791_g = (new RangedAttribute((IAttribute) null, "generic.armor", 0.0D, 0.0D, 30.0D)).func_111112_a(true);
    public static final IAttribute field_189429_h = (new RangedAttribute((IAttribute) null, "generic.armorToughness", 0.0D, 0.0D, 20.0D)).func_111112_a(true);
    public static final IAttribute field_188792_h = (new RangedAttribute((IAttribute) null, "generic.luck", 0.0D, -1024.0D, 1024.0D)).func_111112_a(true);
    // Spigot end

    public static NBTTagList func_111257_a(AbstractAttributeMap attributemapbase) {
        NBTTagList nbttaglist = new NBTTagList();
        Iterator iterator = attributemapbase.func_111146_a().iterator();

        while (iterator.hasNext()) {
            IAttributeInstance attributeinstance = (IAttributeInstance) iterator.next();

            nbttaglist.func_74742_a(func_111261_a(attributeinstance));
        }

        return nbttaglist;
    }

    private static NBTTagCompound func_111261_a(IAttributeInstance attributeinstance) {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        IAttribute iattribute = attributeinstance.func_111123_a();

        nbttagcompound.func_74778_a("Name", iattribute.func_111108_a());
        nbttagcompound.func_74780_a("Base", attributeinstance.func_111125_b());
        Collection collection = attributeinstance.func_111122_c();

        if (collection != null && !collection.isEmpty()) {
            NBTTagList nbttaglist = new NBTTagList();
            Iterator iterator = collection.iterator();

            while (iterator.hasNext()) {
                AttributeModifier attributemodifier = (AttributeModifier) iterator.next();

                if (attributemodifier.func_111165_e()) {
                    nbttaglist.func_74742_a(func_111262_a(attributemodifier));
                }
            }

            nbttagcompound.func_74782_a("Modifiers", nbttaglist);
        }

        return nbttagcompound;
    }

    public static NBTTagCompound func_111262_a(AttributeModifier attributemodifier) {
        NBTTagCompound nbttagcompound = new NBTTagCompound();

        nbttagcompound.func_74778_a("Name", attributemodifier.func_111166_b());
        nbttagcompound.func_74780_a("Amount", attributemodifier.func_111164_d());
        nbttagcompound.func_74768_a("Operation", attributemodifier.func_111169_c());
        nbttagcompound.func_186854_a("UUID", attributemodifier.func_111167_a());
        return nbttagcompound;
    }

    public static void func_151475_a(AbstractAttributeMap attributemapbase, NBTTagList nbttaglist) {
        for (int i = 0; i < nbttaglist.func_74745_c(); ++i) {
            NBTTagCompound nbttagcompound = nbttaglist.func_150305_b(i);
            IAttributeInstance attributeinstance = attributemapbase.func_111152_a(nbttagcompound.func_74779_i("Name"));

            if (attributeinstance == null) {
                SharedMonsterAttributes.field_151476_f.warn("Ignoring unknown attribute \'{}\'", nbttagcompound.func_74779_i("Name"));
            } else {
                func_111258_a(attributeinstance, nbttagcompound);
            }
        }

    }

    private static void func_111258_a(IAttributeInstance attributeinstance, NBTTagCompound nbttagcompound) {
        attributeinstance.func_111128_a(nbttagcompound.func_74769_h("Base"));
        if (nbttagcompound.func_150297_b("Modifiers", 9)) {
            NBTTagList nbttaglist = nbttagcompound.func_150295_c("Modifiers", 10);

            for (int i = 0; i < nbttaglist.func_74745_c(); ++i) {
                AttributeModifier attributemodifier = func_111259_a(nbttaglist.func_150305_b(i));

                if (attributemodifier != null) {
                    AttributeModifier attributemodifier1 = attributeinstance.func_111127_a(attributemodifier.func_111167_a());

                    if (attributemodifier1 != null) {
                        attributeinstance.func_111124_b(attributemodifier1);
                    }

                    attributeinstance.func_111121_a(attributemodifier);
                }
            }
        }

    }

    @Nullable
    public static AttributeModifier func_111259_a(NBTTagCompound nbttagcompound) {
        UUID uuid = nbttagcompound.func_186857_a("UUID");

        try {
            return new AttributeModifier(uuid, nbttagcompound.func_74779_i("Name"), nbttagcompound.func_74769_h("Amount"), nbttagcompound.func_74762_e("Operation"));
        } catch (Exception exception) {
            SharedMonsterAttributes.field_151476_f.warn("Unable to create attribute: {}", exception.getMessage());
            return null;
        }
    }
}
