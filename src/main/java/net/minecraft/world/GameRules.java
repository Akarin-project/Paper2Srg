package net.minecraft.world;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;

import net.minecraft.nbt.NBTTagCompound;

public class GameRules {

    private final TreeMap<String, GameRules.Value> rules = new TreeMap();

    public GameRules() {
        this.addGameRule("doFireTick", "true", GameRules.ValueType.BOOLEAN_VALUE);
        this.addGameRule("mobGriefing", "true", GameRules.ValueType.BOOLEAN_VALUE);
        this.addGameRule("keepInventory", "false", GameRules.ValueType.BOOLEAN_VALUE);
        this.addGameRule("doMobSpawning", "true", GameRules.ValueType.BOOLEAN_VALUE);
        this.addGameRule("doMobLoot", "true", GameRules.ValueType.BOOLEAN_VALUE);
        this.addGameRule("doTileDrops", "true", GameRules.ValueType.BOOLEAN_VALUE);
        this.addGameRule("doEntityDrops", "true", GameRules.ValueType.BOOLEAN_VALUE);
        this.addGameRule("commandBlockOutput", "true", GameRules.ValueType.BOOLEAN_VALUE);
        this.addGameRule("naturalRegeneration", "true", GameRules.ValueType.BOOLEAN_VALUE);
        this.addGameRule("doDaylightCycle", "true", GameRules.ValueType.BOOLEAN_VALUE);
        this.addGameRule("logAdminCommands", "true", GameRules.ValueType.BOOLEAN_VALUE);
        this.addGameRule("showDeathMessages", "true", GameRules.ValueType.BOOLEAN_VALUE);
        this.addGameRule("randomTickSpeed", "3", GameRules.ValueType.NUMERICAL_VALUE);
        this.addGameRule("sendCommandFeedback", "true", GameRules.ValueType.BOOLEAN_VALUE);
        this.addGameRule("reducedDebugInfo", "false", GameRules.ValueType.BOOLEAN_VALUE);
        this.addGameRule("spectatorsGenerateChunks", "true", GameRules.ValueType.BOOLEAN_VALUE);
        this.addGameRule("spawnRadius", "10", GameRules.ValueType.NUMERICAL_VALUE);
        this.addGameRule("disableElytraMovementCheck", "false", GameRules.ValueType.BOOLEAN_VALUE);
        this.addGameRule("maxEntityCramming", "24", GameRules.ValueType.NUMERICAL_VALUE);
        this.addGameRule("doWeatherCycle", "true", GameRules.ValueType.BOOLEAN_VALUE);
        this.addGameRule("doLimitedCrafting", "false", GameRules.ValueType.BOOLEAN_VALUE);
        this.addGameRule("maxCommandChainLength", "65536", GameRules.ValueType.NUMERICAL_VALUE);
        this.addGameRule("announceAdvancements", "true", GameRules.ValueType.BOOLEAN_VALUE);
        this.addGameRule("gameLoopFunction", "-", GameRules.ValueType.FUNCTION);
    }

    public void addGameRule(String s, String s1, GameRules.ValueType gamerules_enumgameruletype) {
        this.rules.put(s, new GameRules.Value(s1, gamerules_enumgameruletype));
    }

    public void setOrCreateGameRule(String s, String s1) {
        GameRules.Value gamerules_gamerulevalue = (GameRules.Value) this.rules.get(s);

        if (gamerules_gamerulevalue != null) {
            gamerules_gamerulevalue.setValue(s1);
        } else {
            this.addGameRule(s, s1, GameRules.ValueType.ANY_VALUE);
        }

    }

    public String getString(String s) {
        GameRules.Value gamerules_gamerulevalue = (GameRules.Value) this.rules.get(s);

        return gamerules_gamerulevalue != null ? gamerules_gamerulevalue.getString() : "";
    }

    public boolean getBoolean(String s) {
        GameRules.Value gamerules_gamerulevalue = (GameRules.Value) this.rules.get(s);

        return gamerules_gamerulevalue != null ? gamerules_gamerulevalue.getBoolean() : false;
    }

    public int getInt(String s) {
        GameRules.Value gamerules_gamerulevalue = (GameRules.Value) this.rules.get(s);

        return gamerules_gamerulevalue != null ? gamerules_gamerulevalue.getInt() : 0;
    }

    public NBTTagCompound writeToNBT() {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        Iterator iterator = this.rules.keySet().iterator();

        while (iterator.hasNext()) {
            String s = (String) iterator.next();
            GameRules.Value gamerules_gamerulevalue = (GameRules.Value) this.rules.get(s);

            nbttagcompound.setString(s, gamerules_gamerulevalue.getString());
        }

        return nbttagcompound;
    }

    public void readFromNBT(NBTTagCompound nbttagcompound) {
        Set set = nbttagcompound.getKeySet();
        Iterator iterator = set.iterator();

        while (iterator.hasNext()) {
            String s = (String) iterator.next();

            this.setOrCreateGameRule(s, nbttagcompound.getString(s));
        }

    }

    public String[] getRules() {
        Set set = this.rules.keySet();

        return (String[]) set.toArray(new String[set.size()]);
    }

    public boolean hasRule(String s) {
        return this.rules.containsKey(s);
    }

    public boolean areSameType(String s, GameRules.ValueType gamerules_enumgameruletype) {
        GameRules.Value gamerules_gamerulevalue = (GameRules.Value) this.rules.get(s);

        return gamerules_gamerulevalue != null && (gamerules_gamerulevalue.getType() == gamerules_enumgameruletype || gamerules_enumgameruletype == GameRules.ValueType.ANY_VALUE);
    }

    public static enum ValueType {

        ANY_VALUE, BOOLEAN_VALUE, NUMERICAL_VALUE, FUNCTION;

        private ValueType() {}
    }

    static class Value {

        private String valueString;
        private boolean valueBoolean;
        private int valueInteger;
        private double valueDouble;
        private final GameRules.ValueType type;

        public Value(String s, GameRules.ValueType gamerules_enumgameruletype) {
            this.type = gamerules_enumgameruletype;
            this.setValue(s);
        }

        public void setValue(String s) {
            this.valueString = s;
            this.valueBoolean = Boolean.parseBoolean(s);
            this.valueInteger = this.valueBoolean ? 1 : 0;

            try {
                this.valueInteger = Integer.parseInt(s);
            } catch (NumberFormatException numberformatexception) {
                ;
            }

            try {
                this.valueDouble = Double.parseDouble(s);
            } catch (NumberFormatException numberformatexception1) {
                ;
            }

        }

        public String getString() {
            return this.valueString;
        }

        public boolean getBoolean() {
            return this.valueBoolean;
        }

        public int getInt() {
            return this.valueInteger;
        }

        public GameRules.ValueType getType() {
            return this.type;
        }
    }
}
