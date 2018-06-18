package net.minecraft.server.dedicated;

import com.mojang.authlib.GameProfile;
import java.io.IOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.server.management.PlayerList;

public class DedicatedPlayerList extends PlayerList {

    private static final Logger LOGGER = LogManager.getLogger();

    public DedicatedPlayerList(DedicatedServer dedicatedserver) {
        super(dedicatedserver);
        this.setViewDistance(dedicatedserver.getIntProperty("view-distance", 10));
        this.maxPlayers = dedicatedserver.getIntProperty("max-players", 20);
        this.setWhiteListEnabled(dedicatedserver.getBooleanProperty("white-list", false));
        if (!dedicatedserver.isSinglePlayer()) {
            this.getBannedPlayers().setLanServer(true);
            this.getBannedIPs().setLanServer(true);
        }

        this.loadPlayerBanList();
        this.savePlayerBanList();
        this.loadIPBanList();
        this.saveIPBanList();
        this.loadOpsList();
        this.readWhiteList();
        this.saveOpsList();
        if (!this.getWhitelistedPlayers().getSaveFile().exists()) {
            this.saveWhiteList();
        }

    }

    public void setWhiteListEnabled(boolean flag) {
        super.setWhiteListEnabled(flag);
        this.getServerInstance().setProperty("white-list", (Object) Boolean.valueOf(flag));
        this.getServerInstance().saveProperties();
    }

    public void addOp(GameProfile gameprofile) {
        super.addOp(gameprofile);
        this.saveOpsList();
    }

    public void removeOp(GameProfile gameprofile) {
        super.removeOp(gameprofile);
        this.saveOpsList();
    }

    public void removePlayerFromWhitelist(GameProfile gameprofile) {
        super.removePlayerFromWhitelist(gameprofile);
        this.saveWhiteList();
    }

    public void addWhitelistedPlayer(GameProfile gameprofile) {
        super.addWhitelistedPlayer(gameprofile);
        this.saveWhiteList();
    }

    public void reloadWhitelist() {
        this.readWhiteList();
    }

    private void saveIPBanList() {
        try {
            this.getBannedIPs().writeChanges();
        } catch (IOException ioexception) {
            DedicatedPlayerList.LOGGER.warn("Failed to save ip banlist: ", ioexception);
        }

    }

    private void savePlayerBanList() {
        try {
            this.getBannedPlayers().writeChanges();
        } catch (IOException ioexception) {
            DedicatedPlayerList.LOGGER.warn("Failed to save user banlist: ", ioexception);
        }

    }

    private void loadIPBanList() {
        try {
            this.getBannedIPs().readSavedFile();
        } catch (IOException ioexception) {
            DedicatedPlayerList.LOGGER.warn("Failed to load ip banlist: ", ioexception);
        }

    }

    private void loadPlayerBanList() {
        try {
            this.getBannedPlayers().readSavedFile();
        } catch (IOException ioexception) {
            DedicatedPlayerList.LOGGER.warn("Failed to load user banlist: ", ioexception);
        }

    }

    private void loadOpsList() {
        try {
            this.getOppedPlayers().readSavedFile();
        } catch (Exception exception) {
            DedicatedPlayerList.LOGGER.warn("Failed to load operators list: ", exception);
        }

    }

    private void saveOpsList() {
        try {
            this.getOppedPlayers().writeChanges();
        } catch (Exception exception) {
            DedicatedPlayerList.LOGGER.warn("Failed to save operators list: ", exception);
        }

    }

    private void readWhiteList() {
        try {
            this.getWhitelistedPlayers().readSavedFile();
        } catch (Exception exception) {
            DedicatedPlayerList.LOGGER.warn("Failed to load white-list: ", exception);
        }

    }

    private void saveWhiteList() {
        try {
            this.getWhitelistedPlayers().writeChanges();
        } catch (Exception exception) {
            DedicatedPlayerList.LOGGER.warn("Failed to save white-list: ", exception);
        }

    }

    public boolean canJoin(GameProfile gameprofile) {
        return !this.isWhiteListEnabled() || this.canSendCommands(gameprofile) || this.getWhitelistedPlayers().isWhitelisted(gameprofile);
    }

    public DedicatedServer getServerInstance() {
        return (DedicatedServer) super.getServerInstance();
    }

    public boolean bypassesPlayerLimit(GameProfile gameprofile) {
        return this.getOppedPlayers().bypassesPlayerLimit(gameprofile);
    }

    public MinecraftServer getServer() {
        return this.getServer();
    }
}
