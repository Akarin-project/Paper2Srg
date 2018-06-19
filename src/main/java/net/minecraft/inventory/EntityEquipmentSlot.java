package net.minecraft.inventory;

public enum EntityEquipmentSlot {

    MAINHAND(EntityEquipmentSlot.Type.HAND, 0, 0, "mainhand"), OFFHAND(EntityEquipmentSlot.Type.HAND, 1, 5, "offhand"), FEET(EntityEquipmentSlot.Type.ARMOR, 0, 1, "feet"), LEGS(EntityEquipmentSlot.Type.ARMOR, 1, 2, "legs"), CHEST(EntityEquipmentSlot.Type.ARMOR, 2, 3, "chest"), HEAD(EntityEquipmentSlot.Type.ARMOR, 3, 4, "head");

    private final EntityEquipmentSlot.Type field_188462_g;
    private final int field_188463_h;
    private final int field_188464_i;
    private final String field_188465_j;

    private EntityEquipmentSlot(EntityEquipmentSlot.Type enumitemslot_function, int i, int j, String s) {
        this.field_188462_g = enumitemslot_function;
        this.field_188463_h = i;
        this.field_188464_i = j;
        this.field_188465_j = s;
    }

    public EntityEquipmentSlot.Type getType() { return this.func_188453_a(); } // Paper - OBFHELPER
    public EntityEquipmentSlot.Type func_188453_a() {
        return this.field_188462_g;
    }

    public int func_188454_b() {
        return this.field_188463_h;
    }

    public int func_188452_c() {
        return this.field_188464_i;
    }

    public String func_188450_d() {
        return this.field_188465_j;
    }

    public static EntityEquipmentSlot func_188451_a(String s) {
        EntityEquipmentSlot[] aenumitemslot = values();
        int i = aenumitemslot.length;

        for (int j = 0; j < i; ++j) {
            EntityEquipmentSlot enumitemslot = aenumitemslot[j];

            if (enumitemslot.func_188450_d().equals(s)) {
                return enumitemslot;
            }
        }

        throw new IllegalArgumentException("Invalid slot \'" + s + "\'");
    }

    public static enum Type {

        HAND, ARMOR;

        private Type() {}
    }
}
