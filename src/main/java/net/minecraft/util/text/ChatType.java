package net.minecraft.util.text;

public enum ChatType {

    CHAT((byte) 0), SYSTEM((byte) 1), GAME_INFO((byte) 2);

    private final byte field_192588_d;

    private ChatType(byte b0) {
        this.field_192588_d = b0;
    }

    public byte func_192583_a() {
        return this.field_192588_d;
    }

    public static ChatType func_192582_a(byte b0) {
        ChatType[] achatmessagetype = values();
        int i = achatmessagetype.length;

        for (int j = 0; j < i; ++j) {
            ChatType chatmessagetype = achatmessagetype[j];

            if (b0 == chatmessagetype.field_192588_d) {
                return chatmessagetype;
            }
        }

        return ChatType.CHAT;
    }
}
