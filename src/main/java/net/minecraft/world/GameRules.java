package net.minecraft.world;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;

import net.minecraft.nbt.NBTTagCompound;

public class GameRules {

    private final TreeMap<String, GameRules.Value> field_82771_a = new TreeMap();

    public GameRules() {
        this.func_180262_a("doFireTick", "true", GameRules.ValueType.BOOLEAN_VALUE);
        this.func_180262_a("mobGriefing", "true", GameRules.ValueType.BOOLEAN_VALUE);
        this.func_180262_a("keepInventory", "false", GameRules.ValueType.BOOLEAN_VALUE);
        this.func_180262_a("doMobSpawning", "true", GameRules.ValueType.BOOLEAN_VALUE);
        this.func_180262_a("doMobLoot", "true", GameRules.ValueType.BOOLEAN_VALUE);
        this.func_180262_a("doTileDrops", "true", GameRules.ValueType.BOOLEAN_VALUE);
        this.func_180262_a("doEntityDrops", "true", GameRules.ValueType.BOOLEAN_VALUE);
        this.func_180262_a("commandBlockOutput", "true", GameRules.ValueType.BOOLEAN_VALUE);
        this.func_180262_a("naturalRegeneration", "true", GameRules.ValueType.BOOLEAN_VALUE);
        this.func_180262_a("doDaylightCycle", "true", GameRules.ValueType.BOOLEAN_VALUE);
        this.func_180262_a("logAdminCommands", "true", GameRules.ValueType.BOOLEAN_VALUE);
        this.func_180262_a("showDeathMessages", "true", GameRules.ValueType.BOOLEAN_VALUE);
        this.func_180262_a("randomTickSpeed", "3", GameRules.ValueType.NUMERICAL_VALUE);
        this.func_180262_a("sendCommandFeedback", "true", GameRules.ValueType.BOOLEAN_VALUE);
        this.func_180262_a("reducedDebugInfo", "false", GameRules.ValueType.BOOLEAN_VALUE);
        this.func_180262_a("spectatorsGenerateChunks", "true", GameRules.ValueType.BOOLEAN_VALUE);
        this.func_180262_a("spawnRadius", "10", GameRules.ValueType.NUMERICAL_VALUE);
        this.func_180262_a("disableElytraMovementCheck", "false", GameRules.ValueType.BOOLEAN_VALUE);
        this.func_180262_a("maxEntityCramming", "24", GameRules.ValueType.NUMERICAL_VALUE);
        this.func_180262_a("doWeatherCycle", "true", GameRules.ValueType.BOOLEAN_VALUE);
        this.func_180262_a("doLimitedCrafting", "false", GameRules.ValueType.BOOLEAN_VALUE);
        this.func_180262_a("maxCommandChainLength", "65536", GameRules.ValueType.NUMERICAL_VALUE);
        this.func_180262_a("announceAdvancements", "true", GameRules.ValueType.BOOLEAN_VALUE);
        this.func_180262_a("gameLoopFunction", "-", GameRules.ValueType.FUNCTION);
    }

    public void func_180262_a(String s, String s1, GameRules.ValueType gamerules_enumgameruletype) {
        this.field_82771_a.put(s, new GameRules.Value(s1, gamerules_enumgameruletype));
    }

    public void func_82764_b(String s, String s1) {
        GameRules.Value gamerules_gamerulevalue = (GameRules.Value) this.field_82771_a.get(s);

        if (gamerules_gamerulevalue != null) {
            gamerules_gamerulevalue.func_82757_a(s1);
        } else {
            this.func_180262_a(s, s1, GameRules.ValueType.ANY_VALUE);
        }

    }

    public String func_82767_a(String s) {
        GameRules.Value gamerules_gamerulevalue = (GameRules.Value) this.field_82771_a.get(s);

        return gamerules_gamerulevalue != null ? gamerules_gamerulevalue.func_82756_a() : "";
    }

    public boolean func_82766_b(String s) {
        GameRules.Value gamerules_gamerulevalue = (GameRules.Value) this.field_82771_a.get(s);

        return gamerules_gamerulevalue != null ? gamerules_gamerulevalue.func_82758_b() : false;
    }

    public int func_180263_c(String s) {
        GameRules.Value gamerules_gamerulevalue = (GameRules.Value) this.field_82771_a.get(s);

        return gamerules_gamerulevalue != null ? gamerules_gamerulevalue.func_180255_c() : 0;
    }

    public NBTTagCompound func_82770_a() {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        Iterator iterator = this.field_82771_a.keySet().iterator();

        while (iterator.hasNext()) {
            String s = (String) iterator.next();
            GameRules.Value gamerules_gamerulevalue = (GameRules.Value) this.field_82771_a.get(s);

            nbttagcompound.func_74778_a(s, gamerules_gamerulevalue.func_82756_a());
        }

        return nbttagcompound;
    }

    public void func_82768_a(NBTTagCompound nbttagcompound) {
        Set set = nbttagcompound.func_150296_c();
        Iterator iterator = set.iterator();

        while (iterator.hasNext()) {
            String s = (String) iterator.next();

            this.func_82764_b(s, nbttagcompound.func_74779_i(s));
        }

    }

    public String[] func_82763_b() {
        Set set = this.field_82771_a.keySet();

        return (String[]) set.toArray(new String[set.size()]);
    }

    public boolean func_82765_e(String s) {
        return this.field_82771_a.containsKey(s);
    }

    public boolean func_180264_a(String s, GameRules.ValueType gamerules_enumgameruletype) {
        GameRules.Value gamerules_gamerulevalue = (GameRules.Value) this.field_82771_a.get(s);

        return gamerules_gamerulevalue != null && (gamerules_gamerulevalue.func_180254_e() == gamerules_enumgameruletype || gamerules_enumgameruletype == GameRules.ValueType.ANY_VALUE);
    }

    public static enum ValueType {

        ANY_VALUE, BOOLEAN_VALUE, NUMERICAL_VALUE, FUNCTION;

        private ValueType() {}
    }

    static class Value {

        private String field_82762_a;
        private boolean field_82760_b;
        private int field_82761_c;
        private double field_82759_d;
        private final GameRules.ValueType field_180256_e;

        public Value(String s, GameRules.ValueType gamerules_enumgameruletype) {
            this.field_180256_e = gamerules_enumgameruletype;
            this.func_82757_a(s);
        }

        public void func_82757_a(String s) {
            this.field_82762_a = s;
            this.field_82760_b = Boolean.parseBoolean(s);
            this.field_82761_c = this.field_82760_b ? 1 : 0;

            try {
                this.field_82761_c = Integer.parseInt(s);
            } catch (NumberFormatException numberformatexception) {
                ;
            }

            try {
                this.field_82759_d = Double.parseDouble(s);
            } catch (NumberFormatException numberformatexception1) {
                ;
            }

        }

        public String func_82756_a() {
            return this.field_82762_a;
        }

        public boolean func_82758_b() {
            return this.field_82760_b;
        }

        public int func_180255_c() {
            return this.field_82761_c;
        }

        public GameRules.ValueType func_180254_e() {
            return this.field_180256_e;
        }
    }
}
