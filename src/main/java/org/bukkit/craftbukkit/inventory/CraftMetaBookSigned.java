package org.bukkit.craftbukkit.inventory;

import java.util.Map;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import org.bukkit.Material;
import org.bukkit.configuration.serialization.DelegateDeserialization;
import org.bukkit.craftbukkit.inventory.CraftMetaItem.SerializableMeta;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;
import org.bukkit.inventory.meta.BookMeta;

import com.google.common.collect.ImmutableMap.Builder;
import net.minecraft.util.text.ITextComponent.Serializer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.nbt.NBTTagString;

@DelegateDeserialization(SerializableMeta.class)
class CraftMetaBookSigned extends CraftMetaBook implements BookMeta {

    CraftMetaBookSigned(CraftMetaItem meta) {
        super(meta);
    }

    CraftMetaBookSigned(NBTTagCompound tag) {
        super(tag, false);

        boolean resolved = true;
        if (tag.func_74764_b(RESOLVED.NBT)) {
            resolved = tag.func_74767_n(RESOLVED.NBT);
        }

        if (tag.func_74764_b(BOOK_PAGES.NBT)) {
            NBTTagList pages = tag.func_150295_c(BOOK_PAGES.NBT, CraftMagicNumbers.NBT.TAG_STRING);

            for (int i = 0; i < Math.min(pages.func_74745_c(), MAX_PAGES); i++) {
                String page = pages.func_150307_f(i);
                if (resolved) {
                    try {
                        this.pages.add(Serializer.func_150699_a(page));
                        continue;
                    } catch (Exception e) {
                        // Ignore and treat as an old book
                    }
                }
                addPage(page);
            }
        }
    }

    CraftMetaBookSigned(Map<String, Object> map) {
        super(map);
    }

    @Override
    void applyToItem(NBTTagCompound itemData) {
        super.applyToItem(itemData, false);

        if (hasTitle()) {
            itemData.func_74778_a(BOOK_TITLE.NBT, this.title);
        } else {
            itemData.func_74778_a(BOOK_TITLE.NBT, " ");
        }

        if (hasAuthor()) {
            itemData.func_74778_a(BOOK_AUTHOR.NBT, this.author);
        } else {
            itemData.func_74778_a(BOOK_AUTHOR.NBT, " ");
        }

        if (hasPages()) {
            NBTTagList list = new NBTTagList();
            for (ITextComponent page : pages) {
                list.func_74742_a(new NBTTagString(
                    Serializer.func_150696_a(page)
                ));
            }
            itemData.func_74782_a(BOOK_PAGES.NBT, list);
        }
        itemData.func_74757_a(RESOLVED.NBT, true);

        if (generation != null) {
            itemData.func_74768_a(GENERATION.NBT, generation);
        } else {
            itemData.func_74768_a(GENERATION.NBT, 0);
        }
    }

    @Override
    boolean isEmpty() {
        return super.isEmpty();
    }

    @Override
    boolean applicableTo(Material type) {
        switch (type) {
        case WRITTEN_BOOK:
        case BOOK_AND_QUILL:
            return true;
        default:
            return false;
        }
    }

    @Override
    public CraftMetaBookSigned clone() {
        CraftMetaBookSigned meta = (CraftMetaBookSigned) super.clone();
        return meta;
    }

    @Override
    int applyHash() {
        final int original;
        int hash = original = super.applyHash();
        return original != hash ? CraftMetaBookSigned.class.hashCode() ^ hash : hash;
    }

    @Override
    boolean equalsCommon(CraftMetaItem meta) {
        return super.equalsCommon(meta);
    }

    @Override
    boolean notUncommon(CraftMetaItem meta) {
        return super.notUncommon(meta) && (meta instanceof CraftMetaBookSigned || isBookEmpty());
    }

    @Override
    Builder<String, Object> serialize(Builder<String, Object> builder) {
        super.serialize(builder);
        return builder;
    }
}
