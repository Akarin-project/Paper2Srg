package net.minecraft.network.rcon;

public interface IServer {

    int getIntProperty(String s, int i);

    String getStringProperty(String s, String s1);

    void setProperty(String s, Object object);

    void saveProperties();

    String getSettingsFilename();

    String getHostname();

    int getPort();

    String getMotd();

    String getMinecraftVersion();

    int getCurrentPlayerCount();

    int getMaxPlayers();

    String[] getOnlinePlayerNames();

    String getFolderName();

    String getPlugins();

    String handleRConCommand(String s);

    boolean isDebuggingEnabled();

    void logInfo(String s);

    void logWarning(String s);

    void logSevere(String s);

    void logDebug(String s);
}
