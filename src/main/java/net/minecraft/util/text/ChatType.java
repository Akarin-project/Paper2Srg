package net.minecraft.util.text;

public enum ChatType {

    CHAT((byte) 0), SYSTEM((byte) 1), GAME_INFO((byte) 2);

    private final byte id;

    private ChatType(byte b0) {
        this.id = b0;
    }

    public byte getId() {
        return this.id;
    }

    public static ChatType byId(byte b0) {
        ChatType[] achatmessagetype = values();
        int i = achatmessagetype.length;

        for (int j = 0; j < i; ++j) {
            ChatType chatmessagetype = achatmessagetype[j];

            if (b0 == chatmessagetype.id) {
                return chatmessagetype;
            }
        }

        return ChatType.CHAT;
    }
}
