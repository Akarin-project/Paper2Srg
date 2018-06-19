package net.minecraft.util.datafix.fixes;

import com.google.common.collect.Maps;
import java.util.Map;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IFixableData;

public class TileEntityId implements IFixableData {

    private static final Map<String, String> field_191275_a = Maps.newHashMap();

    public TileEntityId() {}

    public int func_188216_a() {
        return 704;
    }

    public NBTTagCompound func_188217_a(NBTTagCompound nbttagcompound) {
        String s = (String) TileEntityId.field_191275_a.get(nbttagcompound.func_74779_i("id"));

        if (s != null) {
            nbttagcompound.func_74778_a("id", s);
        }

        return nbttagcompound;
    }

    static {
        TileEntityId.field_191275_a.put("Airportal", "minecraft:end_portal");
        TileEntityId.field_191275_a.put("Banner", "minecraft:banner");
        TileEntityId.field_191275_a.put("Beacon", "minecraft:beacon");
        TileEntityId.field_191275_a.put("Cauldron", "minecraft:brewing_stand");
        TileEntityId.field_191275_a.put("Chest", "minecraft:chest");
        TileEntityId.field_191275_a.put("Comparator", "minecraft:comparator");
        TileEntityId.field_191275_a.put("Control", "minecraft:command_block");
        TileEntityId.field_191275_a.put("DLDetector", "minecraft:daylight_detector");
        TileEntityId.field_191275_a.put("Dropper", "minecraft:dropper");
        TileEntityId.field_191275_a.put("EnchantTable", "minecraft:enchanting_table");
        TileEntityId.field_191275_a.put("EndGateway", "minecraft:end_gateway");
        TileEntityId.field_191275_a.put("EnderChest", "minecraft:ender_chest");
        TileEntityId.field_191275_a.put("FlowerPot", "minecraft:flower_pot");
        TileEntityId.field_191275_a.put("Furnace", "minecraft:furnace");
        TileEntityId.field_191275_a.put("Hopper", "minecraft:hopper");
        TileEntityId.field_191275_a.put("MobSpawner", "minecraft:mob_spawner");
        TileEntityId.field_191275_a.put("Music", "minecraft:noteblock");
        TileEntityId.field_191275_a.put("Piston", "minecraft:piston");
        TileEntityId.field_191275_a.put("RecordPlayer", "minecraft:jukebox");
        TileEntityId.field_191275_a.put("Sign", "minecraft:sign");
        TileEntityId.field_191275_a.put("Skull", "minecraft:skull");
        TileEntityId.field_191275_a.put("Structure", "minecraft:structure_block");
        TileEntityId.field_191275_a.put("Trap", "minecraft:dispenser");
    }
}
