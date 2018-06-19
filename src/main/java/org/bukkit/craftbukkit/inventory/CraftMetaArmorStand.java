package org.bukkit.craftbukkit.inventory;

import com.destroystokyo.paper.inventory.meta.ArmorStandMeta;
import com.google.common.collect.ImmutableMap;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.DelegateDeserialization;

import java.util.Map;

import org.bukkit.craftbukkit.inventory.CraftMetaItem.ItemMetaKey;
import org.bukkit.craftbukkit.inventory.CraftMetaItem.SerializableMeta;

// Paper - Created entire class
@DelegateDeserialization(CraftMetaItem.SerializableMeta.class)
public class CraftMetaArmorStand extends CraftMetaItem implements ArmorStandMeta {

    static final ItemMetaKey ENTITY_TAG = new ItemMetaKey("EntityTag", "entity-tag");
    static final ItemMetaKey INVISIBLE = new ItemMetaKey("Invisible", "invisible");
    static final ItemMetaKey NO_BASE_PLATE = new ItemMetaKey("NoBasePlate", "no-base-plate");
    static final ItemMetaKey SHOW_ARMS = new ItemMetaKey("ShowArms", "show-arms");
    static final ItemMetaKey SMALL = new ItemMetaKey("Small", "small");
    static final ItemMetaKey MARKER = new ItemMetaKey("Marker", "marker");

    private NBTTagCompound entityTag;

    private boolean invisible;
    private boolean noBasePlate;
    private boolean showArms;
    private boolean small;
    private boolean marker;

    CraftMetaArmorStand(CraftMetaItem meta) {
        super(meta);

        if (!(meta instanceof CraftMetaArmorStand)) {
            return;
        }

        CraftMetaArmorStand standMeta = (CraftMetaArmorStand) meta;
        this.invisible = standMeta.invisible;
        this.noBasePlate = standMeta.noBasePlate;
        this.showArms = standMeta.showArms;
        this.small = standMeta.small;
        this.marker = standMeta.marker;
    }

    CraftMetaArmorStand(NBTTagCompound tag) {
        super(tag);

        if (tag.func_74764_b(ENTITY_TAG.NBT)) {
            entityTag = tag.func_74775_l(ENTITY_TAG.NBT);

            if (entityTag.func_74764_b(INVISIBLE.NBT)) {
                invisible = entityTag.func_74767_n(INVISIBLE.NBT);
            }

            if (entityTag.func_74764_b(NO_BASE_PLATE.NBT)) {
                noBasePlate = entityTag.func_74767_n(NO_BASE_PLATE.NBT);
            }

            if (entityTag.func_74764_b(SHOW_ARMS.NBT)) {
                showArms = entityTag.func_74767_n(SHOW_ARMS.NBT);
            }

            if (entityTag.func_74764_b(SMALL.NBT)) {
                small = entityTag.func_74767_n(SMALL.NBT);
            }

            if (entityTag.func_74764_b(MARKER.NBT)) {
                marker = entityTag.func_74767_n(MARKER.NBT);
            }
        }
    }

    CraftMetaArmorStand(Map<String, Object> map) {
        super(map);

        boolean invis = SerializableMeta.getBoolean(map, INVISIBLE.BUKKIT);
        boolean noBase = SerializableMeta.getBoolean(map, NO_BASE_PLATE.BUKKIT);
        boolean showArms = SerializableMeta.getBoolean(map, SHOW_ARMS.BUKKIT);
        boolean small = SerializableMeta.getBoolean(map, SMALL.BUKKIT);
        boolean marker = SerializableMeta.getBoolean(map, MARKER.BUKKIT);

        this.invisible = invis;
        this.noBasePlate = noBase;
        this.showArms = showArms;
        this.small = small;
        this.marker = marker;
    }

    @Override
    void applyToItem(NBTTagCompound tag) {
        super.applyToItem(tag);

        if (!isArmorStandEmpty() && entityTag == null) {
            entityTag = new NBTTagCompound();
        }

        if (isInvisible()) {
            entityTag.func_74757_a(INVISIBLE.NBT, invisible);
        }

        if (hasNoBasePlate()) {
            entityTag.func_74757_a(NO_BASE_PLATE.NBT, noBasePlate);
        }

        if (shouldShowArms()) {
            entityTag.func_74757_a(SHOW_ARMS.NBT, showArms);
        }

        if (isSmall()) {
            entityTag.func_74757_a(SMALL.NBT, small);
        }

        if (isMarker()) {
            entityTag.func_74757_a(MARKER.NBT, marker);
        }

        if (entityTag != null) {
            tag.func_74782_a(ENTITY_TAG.NBT, entityTag);
        }
    }

    @Override
    boolean applicableTo(Material type) {
        switch (type) {
            case ARMOR_STAND:
                return true;
            default:
                return false;
        }
    }

    @Override
    boolean isEmpty() {
        return super.isEmpty() && isArmorStandEmpty();
    }

    boolean isArmorStandEmpty() {
        return !(isInvisible() || hasNoBasePlate() || shouldShowArms() || isSmall() || isMarker() || entityTag != null);
    }

    @Override
    ImmutableMap.Builder<String, Object> serialize(ImmutableMap.Builder<String, Object> builder) {
        super.serialize(builder);

        if (isInvisible()) {
            builder.put(INVISIBLE.BUKKIT, invisible);
        }

        if (hasNoBasePlate()) {
            builder.put(NO_BASE_PLATE.BUKKIT, noBasePlate);
        }

        if (shouldShowArms()) {
            builder.put(SHOW_ARMS.BUKKIT, showArms);
        }

        if (isSmall()) {
            builder.put(SMALL.BUKKIT, small);
        }

        if (isMarker()) {
            builder.put(MARKER.BUKKIT, marker);
        }

        return builder;
    }

    @Override
    void deserializeInternal(NBTTagCompound tag) {
        super.deserializeInternal(tag);

        if (tag.func_74764_b(ENTITY_TAG.NBT)) {
            entityTag = tag.func_74775_l(ENTITY_TAG.NBT);
            MinecraftServer.getServer().field_184112_s.func_188257_a(FixTypes.ENTITY, entityTag); // PAIL: convert

            if (entityTag.func_74764_b(INVISIBLE.NBT)) {
                invisible = entityTag.func_74767_n(INVISIBLE.NBT);
            }

            if (entityTag.func_74764_b(NO_BASE_PLATE.NBT)) {
                noBasePlate = entityTag.func_74767_n(NO_BASE_PLATE.NBT);
            }

            if (entityTag.func_74764_b(SHOW_ARMS.NBT)) {
                showArms = entityTag.func_74767_n(SHOW_ARMS.NBT);
            }

            if (entityTag.func_74764_b(SMALL.NBT)) {
                small = entityTag.func_74767_n(SMALL.NBT);
            }

            if (entityTag.func_74764_b(MARKER.NBT)) {
                marker = entityTag.func_74767_n(MARKER.NBT);
            }
        }
    }

    @Override
    boolean equalsCommon(CraftMetaItem meta) {
        if (!super.equalsCommon(meta)) {
            return false;
        }
        if (meta instanceof CraftMetaArmorStand) {
            CraftMetaArmorStand that = (CraftMetaArmorStand) meta;

            return invisible == that.invisible &&
                    noBasePlate == that.noBasePlate &&
                    showArms == that.showArms &&
                    small == that.small &&
                    marker == that.marker;
        }
        return true;
    }

    @Override
    boolean notUncommon(CraftMetaItem meta) {
        return super.notUncommon(meta) && (meta instanceof CraftMetaArmorStand || isArmorStandEmpty());
    }

    @Override
    void serializeInternal(Map<String, NBTBase> internalTags) {
        if (entityTag != null) {
            internalTags.put(ENTITY_TAG.NBT, entityTag);
        }
    }

    @Override
    public boolean isInvisible() {
        return invisible;
    }

    @Override
    public boolean hasNoBasePlate() {
        return noBasePlate;
    }

    @Override
    public boolean shouldShowArms() {
        return showArms;
    }

    @Override
    public boolean isSmall() {
        return small;
    }

    @Override
    public boolean isMarker() {
        return marker;
    }

    @Override
    public void setInvisible(boolean invisible) {
        this.invisible = invisible;
    }

    @Override
    public void setNoBasePlate(boolean noBasePlate) {
        this.noBasePlate = noBasePlate;
    }

    @Override
    public void setShowArms(boolean showArms) {
        this.showArms = showArms;
    }

    @Override
    public void setSmall(boolean small) {
        this.small = small;
    }

    @Override
    public void setMarker(boolean marker) {
        this.marker = marker;
    }

    @Override
    int applyHash() {
        final int original;
        int hash = original = super.applyHash();

        hash += entityTag != null ? 73 * hash + entityTag.hashCode() : 0;
        hash += isInvisible() ? 61 * hash + 1231 : 0;
        hash += hasNoBasePlate() ? 61 * hash + 1231 : 0;
        hash += shouldShowArms() ? 61 * hash + 1231 : 0;
        hash += isSmall() ? 61 * hash + 1231 : 0;
        hash += isMarker() ? 61 * hash + 1231 : 0;

        return original != hash ? CraftMetaArmorStand.class.hashCode() ^ hash : hash;
    }

    @Override
    public CraftMetaArmorStand clone() {
        CraftMetaArmorStand clone = (CraftMetaArmorStand) super.clone();

        if (entityTag != null) {
            clone.entityTag = entityTag.func_74737_b();
        }

        return clone;
    }
}
