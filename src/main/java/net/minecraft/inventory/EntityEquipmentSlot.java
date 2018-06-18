package net.minecraft.inventory;

public enum EntityEquipmentSlot {

    MAINHAND(EntityEquipmentSlot.Type.HAND, 0, 0, "mainhand"), OFFHAND(EntityEquipmentSlot.Type.HAND, 1, 5, "offhand"), FEET(EntityEquipmentSlot.Type.ARMOR, 0, 1, "feet"), LEGS(EntityEquipmentSlot.Type.ARMOR, 1, 2, "legs"), CHEST(EntityEquipmentSlot.Type.ARMOR, 2, 3, "chest"), HEAD(EntityEquipmentSlot.Type.ARMOR, 3, 4, "head");

    private final EntityEquipmentSlot.Type slotType;
    private final int index;
    private final int slotIndex;
    private final String name;

    private EntityEquipmentSlot(EntityEquipmentSlot.Type enumitemslot_function, int i, int j, String s) {
        this.slotType = enumitemslot_function;
        this.index = i;
        this.slotIndex = j;
        this.name = s;
    }

    public EntityEquipmentSlot.Type getType() { return this.getSlotType(); } // Paper - OBFHELPER
    public EntityEquipmentSlot.Type getSlotType() {
        return this.slotType;
    }

    public int getIndex() {
        return this.index;
    }

    public int getSlotIndex() {
        return this.slotIndex;
    }

    public String getName() {
        return this.name;
    }

    public static EntityEquipmentSlot fromString(String s) {
        EntityEquipmentSlot[] aenumitemslot = values();
        int i = aenumitemslot.length;

        for (int j = 0; j < i; ++j) {
            EntityEquipmentSlot enumitemslot = aenumitemslot[j];

            if (enumitemslot.getName().equals(s)) {
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
