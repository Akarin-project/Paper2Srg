package net.minecraft.util.datafix.fixes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IFixableData;


public class ShulkerBoxItemColor implements IFixableData {

    public static final String[] NAMES_BY_COLOR = new String[] { "minecraft:white_shulker_box", "minecraft:orange_shulker_box", "minecraft:magenta_shulker_box", "minecraft:light_blue_shulker_box", "minecraft:yellow_shulker_box", "minecraft:lime_shulker_box", "minecraft:pink_shulker_box", "minecraft:gray_shulker_box", "minecraft:silver_shulker_box", "minecraft:cyan_shulker_box", "minecraft:purple_shulker_box", "minecraft:blue_shulker_box", "minecraft:brown_shulker_box", "minecraft:green_shulker_box", "minecraft:red_shulker_box", "minecraft:black_shulker_box"};

    public ShulkerBoxItemColor() {}

    public int getFixVersion() {
        return 813;
    }

    public NBTTagCompound fixTagCompound(NBTTagCompound nbttagcompound) {
        if ("minecraft:shulker_box".equals(nbttagcompound.getString("id")) && nbttagcompound.hasKey("tag", 10)) {
            NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("tag");

            if (nbttagcompound1.hasKey("BlockEntityTag", 10)) {
                NBTTagCompound nbttagcompound2 = nbttagcompound1.getCompoundTag("BlockEntityTag");

                if (nbttagcompound2.getTagList("Items", 10).hasNoTags()) {
                    nbttagcompound2.removeTag("Items");
                }

                int i = nbttagcompound2.getInteger("Color");

                nbttagcompound2.removeTag("Color");
                if (nbttagcompound2.hasNoTags()) {
                    nbttagcompound1.removeTag("BlockEntityTag");
                }

                if (nbttagcompound1.hasNoTags()) {
                    nbttagcompound.removeTag("tag");
                }

                nbttagcompound.setString("id", ShulkerBoxItemColor.NAMES_BY_COLOR[i % 16]);
            }
        }

        return nbttagcompound;
    }
}
