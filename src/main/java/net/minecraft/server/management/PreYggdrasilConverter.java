package net.minecraft.server.management;

import com.destroystokyo.paper.exception.ServerInternalException;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.io.Files;
import com.mojang.authlib.Agent;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.ProfileLookupCallback;
import com.mojang.authlib.yggdrasil.ProfileNotFoundException;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import javax.annotation.Nullable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.server.dedicated.PropertyManager;
import net.minecraft.util.StringUtils;

public class PreYggdrasilConverter {

    private static final Logger LOGGER = LogManager.getLogger();
    public static final File OLD_IPBAN_FILE = new File("banned-ips.txt");
    public static final File OLD_PLAYERBAN_FILE = new File("banned-players.txt");
    public static final File OLD_OPS_FILE = new File("ops.txt");
    public static final File OLD_WHITELIST_FILE = new File("white-list.txt");

    static List<String> readFile(File file, Map<String, String[]> map) throws IOException {
        List list = Files.readLines(file, StandardCharsets.UTF_8);
        Iterator iterator = list.iterator();

        while (iterator.hasNext()) {
            String s = (String) iterator.next();

            s = s.trim();
            if (!s.startsWith("#") && s.length() >= 1) {
                String[] astring = s.split("\\|");

                map.put(astring[0].toLowerCase(Locale.ROOT), astring);
            }
        }

        return list;
    }

    private static void lookupNames(MinecraftServer minecraftserver, Collection<String> collection, ProfileLookupCallback profilelookupcallback) {
        String[] astring = (String[]) Iterators.toArray(Iterators.filter(collection.iterator(), new Predicate() {
            public boolean a(@Nullable String s) {
                return !StringUtils.isNullOrEmpty(s);
            }

            public boolean apply(@Nullable Object object) {
                return this.a((String) object);
            }
        }), String.class);

        if (minecraftserver.isServerInOnlineMode()
                || (org.spigotmc.SpigotConfig.bungee && com.destroystokyo.paper.PaperConfig.bungeeOnlineMode)) { // Spigot: bungee = online mode, for now.  // Paper - Handle via setting
            minecraftserver.getGameProfileRepository().findProfilesByNames(astring, Agent.MINECRAFT, profilelookupcallback);
        } else {
            String[] astring1 = astring;
            int i = astring.length;

            for (int j = 0; j < i; ++j) {
                String s = astring1[j];
                UUID uuid = EntityPlayer.getUUID(new GameProfile((UUID) null, s));
                GameProfile gameprofile = new GameProfile(uuid, s);

                profilelookupcallback.onProfileLookupSucceeded(gameprofile);
            }
        }

    }

    public static boolean convertUserBanlist(final MinecraftServer minecraftserver) {
        final UserListBans gameprofilebanlist = new UserListBans(PlayerList.FILE_PLAYERBANS);

        if (PreYggdrasilConverter.OLD_PLAYERBAN_FILE.exists() && PreYggdrasilConverter.OLD_PLAYERBAN_FILE.isFile()) {
            if (gameprofilebanlist.getSaveFile().exists()) {
                try {
                    gameprofilebanlist.readSavedFile();
                // CraftBukkit start - FileNotFoundException -> IOException, don't print stacktrace
                } catch (IOException filenotfoundexception) {
                    PreYggdrasilConverter.LOGGER.warn("Could not load existing file {}", gameprofilebanlist.getSaveFile().getName());
                }
            }

            try {
                final HashMap hashmap = Maps.newHashMap();

                readFile(PreYggdrasilConverter.OLD_PLAYERBAN_FILE, (Map) hashmap);
                ProfileLookupCallback profilelookupcallback = new ProfileLookupCallback() {
                    public void onProfileLookupSucceeded(GameProfile gameprofile) {
                        minecraftserver.getPlayerProfileCache().addEntry(gameprofile);
                        String[] astring = (String[]) hashmap.get(gameprofile.getName().toLowerCase(Locale.ROOT));

                        if (astring == null) {
                            PreYggdrasilConverter.LOGGER.warn("Could not convert user banlist entry for {}", gameprofile.getName());
                            throw new PreYggdrasilConverter.ConversionError("Profile not in the conversionlist", null);
                        } else {
                            Date date = astring.length > 1 ? PreYggdrasilConverter.parseDate(astring[1], (Date) null) : null;
                            String s = astring.length > 2 ? astring[2] : null;
                            Date date1 = astring.length > 3 ? PreYggdrasilConverter.parseDate(astring[3], (Date) null) : null;
                            String s1 = astring.length > 4 ? astring[4] : null;

                            gameprofilebanlist.addEntry(new UserListBansEntry(gameprofile, date, s, date1, s1));
                        }
                    }

                    public void onProfileLookupFailed(GameProfile gameprofile, Exception exception) {
                        PreYggdrasilConverter.LOGGER.warn("Could not lookup user banlist entry for {}", gameprofile.getName(), exception);
                        if (!(exception instanceof ProfileNotFoundException)) {
                            throw new PreYggdrasilConverter.ConversionError("Could not request user " + gameprofile.getName() + " from backend systems", exception, null);
                        }
                    }
                };

                lookupNames(minecraftserver, hashmap.keySet(), profilelookupcallback);
                gameprofilebanlist.writeChanges();
                backupConverted(PreYggdrasilConverter.OLD_PLAYERBAN_FILE);
                return true;
            } catch (IOException ioexception) {
                PreYggdrasilConverter.LOGGER.warn("Could not read old user banlist to convert it!", ioexception);
                return false;
            } catch (PreYggdrasilConverter.ConversionError namereferencingfileconverter_fileconversionexception) {
                PreYggdrasilConverter.LOGGER.error("Conversion failed, please try again later", namereferencingfileconverter_fileconversionexception);
                return false;
            }
        } else {
            return true;
        }
    }

    public static boolean convertIpBanlist(MinecraftServer minecraftserver) {
        UserListIPBans ipbanlist = new UserListIPBans(PlayerList.FILE_IPBANS);

        if (PreYggdrasilConverter.OLD_IPBAN_FILE.exists() && PreYggdrasilConverter.OLD_IPBAN_FILE.isFile()) {
            if (ipbanlist.getSaveFile().exists()) {
                try {
                    ipbanlist.readSavedFile();
                // CraftBukkit start - FileNotFoundException -> IOException, don't print stacktrace
                } catch (IOException filenotfoundexception) {
                    PreYggdrasilConverter.LOGGER.warn("Could not load existing file {}", ipbanlist.getSaveFile().getName());
                }
            }

            try {
                HashMap hashmap = Maps.newHashMap();

                readFile(PreYggdrasilConverter.OLD_IPBAN_FILE, (Map) hashmap);
                Iterator iterator = hashmap.keySet().iterator();

                while (iterator.hasNext()) {
                    String s = (String) iterator.next();
                    String[] astring = (String[]) hashmap.get(s);
                    Date date = astring.length > 1 ? parseDate(astring[1], (Date) null) : null;
                    String s1 = astring.length > 2 ? astring[2] : null;
                    Date date1 = astring.length > 3 ? parseDate(astring[3], (Date) null) : null;
                    String s2 = astring.length > 4 ? astring[4] : null;

                    ipbanlist.addEntry(new UserListIPBansEntry(s, date, s1, date1, s2));
                }

                ipbanlist.writeChanges();
                backupConverted(PreYggdrasilConverter.OLD_IPBAN_FILE);
                return true;
            } catch (IOException ioexception) {
                PreYggdrasilConverter.LOGGER.warn("Could not parse old ip banlist to convert it!", ioexception);
                return false;
            }
        } else {
            return true;
        }
    }

    public static boolean convertOplist(final MinecraftServer minecraftserver) {
        final UserListOps oplist = new UserListOps(PlayerList.FILE_OPS);

        if (PreYggdrasilConverter.OLD_OPS_FILE.exists() && PreYggdrasilConverter.OLD_OPS_FILE.isFile()) {
            if (oplist.getSaveFile().exists()) {
                try {
                    oplist.readSavedFile();
                // CraftBukkit start - FileNotFoundException -> IOException, don't print stacktrace
                } catch (IOException filenotfoundexception) {
                    PreYggdrasilConverter.LOGGER.warn("Could not load existing file {}", oplist.getSaveFile().getName());
                }
            }

            try {
                List list = Files.readLines(PreYggdrasilConverter.OLD_OPS_FILE, StandardCharsets.UTF_8);
                ProfileLookupCallback profilelookupcallback = new ProfileLookupCallback() {
                    public void onProfileLookupSucceeded(GameProfile gameprofile) {
                        minecraftserver.getPlayerProfileCache().addEntry(gameprofile);
                        oplist.addEntry(new UserListOpsEntry(gameprofile, minecraftserver.getOpPermissionLevel(), false));
                    }

                    public void onProfileLookupFailed(GameProfile gameprofile, Exception exception) {
                        PreYggdrasilConverter.LOGGER.warn("Could not lookup oplist entry for {}", gameprofile.getName(), exception);
                        if (!(exception instanceof ProfileNotFoundException)) {
                            throw new PreYggdrasilConverter.ConversionError("Could not request user " + gameprofile.getName() + " from backend systems", exception, null);
                        }
                    }
                };

                lookupNames(minecraftserver, list, profilelookupcallback);
                oplist.writeChanges();
                backupConverted(PreYggdrasilConverter.OLD_OPS_FILE);
                return true;
            } catch (IOException ioexception) {
                PreYggdrasilConverter.LOGGER.warn("Could not read old oplist to convert it!", ioexception);
                return false;
            } catch (PreYggdrasilConverter.ConversionError namereferencingfileconverter_fileconversionexception) {
                PreYggdrasilConverter.LOGGER.error("Conversion failed, please try again later", namereferencingfileconverter_fileconversionexception);
                return false;
            }
        } else {
            return true;
        }
    }

    public static boolean convertWhitelist(final MinecraftServer minecraftserver) {
        final UserListWhitelist whitelist = new UserListWhitelist(PlayerList.FILE_WHITELIST);

        if (PreYggdrasilConverter.OLD_WHITELIST_FILE.exists() && PreYggdrasilConverter.OLD_WHITELIST_FILE.isFile()) {
            if (whitelist.getSaveFile().exists()) {
                try {
                    whitelist.readSavedFile();
                // CraftBukkit start - FileNotFoundException -> IOException, don't print stacktrace
                } catch (IOException filenotfoundexception) {
                    PreYggdrasilConverter.LOGGER.warn("Could not load existing file {}", whitelist.getSaveFile().getName());
                }
            }

            try {
                List list = Files.readLines(PreYggdrasilConverter.OLD_WHITELIST_FILE, StandardCharsets.UTF_8);
                ProfileLookupCallback profilelookupcallback = new ProfileLookupCallback() {
                    public void onProfileLookupSucceeded(GameProfile gameprofile) {
                        minecraftserver.getPlayerProfileCache().addEntry(gameprofile);
                        whitelist.addEntry(new UserListWhitelistEntry(gameprofile));
                    }

                    public void onProfileLookupFailed(GameProfile gameprofile, Exception exception) {
                        PreYggdrasilConverter.LOGGER.warn("Could not lookup user whitelist entry for {}", gameprofile.getName(), exception);
                        if (!(exception instanceof ProfileNotFoundException)) {
                            throw new PreYggdrasilConverter.ConversionError("Could not request user " + gameprofile.getName() + " from backend systems", exception, null);
                        }
                    }
                };

                lookupNames(minecraftserver, list, profilelookupcallback);
                whitelist.writeChanges();
                backupConverted(PreYggdrasilConverter.OLD_WHITELIST_FILE);
                return true;
            } catch (IOException ioexception) {
                PreYggdrasilConverter.LOGGER.warn("Could not read old whitelist to convert it!", ioexception);
                return false;
            } catch (PreYggdrasilConverter.ConversionError namereferencingfileconverter_fileconversionexception) {
                PreYggdrasilConverter.LOGGER.error("Conversion failed, please try again later", namereferencingfileconverter_fileconversionexception);
                return false;
            }
        } else {
            return true;
        }
    }

    public static String convertMobOwnerIfNeeded(final MinecraftServer minecraftserver, String s) {
        if (!StringUtils.isNullOrEmpty(s) && s.length() <= 16) {
            GameProfile gameprofile = minecraftserver.getPlayerProfileCache().getGameProfileForUsername(s);

            if (gameprofile != null && gameprofile.getId() != null) {
                return gameprofile.getId().toString();
            } else if (!minecraftserver.isSinglePlayer() && minecraftserver.isServerInOnlineMode()) {
                final ArrayList arraylist = Lists.newArrayList();
                ProfileLookupCallback profilelookupcallback = new ProfileLookupCallback() {
                    public void onProfileLookupSucceeded(GameProfile gameprofile) {
                        minecraftserver.getPlayerProfileCache().addEntry(gameprofile);
                        arraylist.add(gameprofile);
                    }

                    public void onProfileLookupFailed(GameProfile gameprofile, Exception exception) {
                        PreYggdrasilConverter.LOGGER.warn("Could not lookup user whitelist entry for {}", gameprofile.getName(), exception);
                    }
                };

                lookupNames(minecraftserver, Lists.newArrayList(new String[] { s}), profilelookupcallback);
                return !arraylist.isEmpty() && ((GameProfile) arraylist.get(0)).getId() != null ? ((GameProfile) arraylist.get(0)).getId().toString() : "";
            } else {
                return EntityPlayer.getUUID(new GameProfile((UUID) null, s)).toString();
            }
        } else {
            return s;
        }
    }

    public static boolean convertSaveFiles(final DedicatedServer dedicatedserver, PropertyManager propertymanager) {
        final File file = getPlayersDirectory(propertymanager);
        final File file1 = new File(file.getParentFile(), "playerdata");
        final File file2 = new File(file.getParentFile(), "unknownplayers");

        if (file.exists() && file.isDirectory()) {
            File[] afile = file.listFiles();
            ArrayList arraylist = Lists.newArrayList();
            File[] afile1 = afile;
            int i = afile.length;

            for (int j = 0; j < i; ++j) {
                File file3 = afile1[j];
                String s = file3.getName();

                if (s.toLowerCase(Locale.ROOT).endsWith(".dat")) {
                    String s1 = s.substring(0, s.length() - ".dat".length());

                    if (!s1.isEmpty()) {
                        arraylist.add(s1);
                    }
                }
            }

            try {
                final String[] astring = (String[]) arraylist.toArray(new String[arraylist.size()]);
                ProfileLookupCallback profilelookupcallback = new ProfileLookupCallback() {
                    public void onProfileLookupSucceeded(GameProfile gameprofile) {
                        dedicatedserver.getPlayerProfileCache().addEntry(gameprofile);
                        UUID uuid = gameprofile.getId();

                        if (uuid == null) {
                            throw new PreYggdrasilConverter.ConversionError("Missing UUID for user profile " + gameprofile.getName(), null);
                        } else {
                            this.a(file, this.a(gameprofile), uuid.toString());
                        }
                    }

                    public void onProfileLookupFailed(GameProfile gameprofile, Exception exception) {
                        PreYggdrasilConverter.LOGGER.warn("Could not lookup user uuid for {}", gameprofile.getName(), exception);
                        if (exception instanceof ProfileNotFoundException) {
                            String s = this.a(gameprofile);

                            this.a(file, s, s);
                        } else {
                            throw new PreYggdrasilConverter.ConversionError("Could not request user " + gameprofile.getName() + " from backend systems", exception, null);
                        }
                    }

                    private void a(File file, String s, String s1) {
                        File file1 = new File(file2, s + ".dat");
                        File file3 = new File(file, s1 + ".dat");

                        // CraftBukkit start - Use old file name to seed lastKnownName
                        NBTTagCompound root = null;

                        try {
                            root = CompressedStreamTools.readCompressed(new java.io.FileInputStream(file1));
                        } catch (Exception exception) {
                            exception.printStackTrace();
                            ServerInternalException.reportInternalException(exception); // Paper
                        }

                        if (root != null) {
                            if (!root.hasKey("bukkit")) {
                                root.setTag("bukkit", new NBTTagCompound());
                            }
                            NBTTagCompound data = root.getCompoundTag("bukkit");
                            data.setString("lastKnownName", s);

                            try {
                                CompressedStreamTools.writeCompressed(root, new java.io.FileOutputStream(file2));
                            } catch (Exception exception) {
                                exception.printStackTrace();
                                ServerInternalException.reportInternalException(exception); // Paper
                            }
                       }
                        // CraftBukkit end

                        PreYggdrasilConverter.mkdir(file);
                        if (!file1.renameTo(file3)) {
                            throw new PreYggdrasilConverter.ConversionError("Could not convert file for " + s, null);
                        }
                    }

                    private String a(GameProfile gameprofile) {
                        String s = null;
                        // String[] astring = astring1; // CraftBukkit - decompile error
                        int i = astring.length;

                        for (int j = 0; j < i; ++j) {
                            String s1 = astring[j];

                            if (s1 != null && s1.equalsIgnoreCase(gameprofile.getName())) {
                                s = s1;
                                break;
                            }
                        }

                        if (s == null) {
                            throw new PreYggdrasilConverter.ConversionError("Could not find the filename for " + gameprofile.getName() + " anymore", null);
                        } else {
                            return s;
                        }
                    }
                };

                lookupNames(dedicatedserver, Lists.newArrayList(astring), profilelookupcallback);
                return true;
            } catch (PreYggdrasilConverter.ConversionError namereferencingfileconverter_fileconversionexception) {
                PreYggdrasilConverter.LOGGER.error("Conversion failed, please try again later", namereferencingfileconverter_fileconversionexception);
                return false;
            }
        } else {
            return true;
        }
    }

    private static void mkdir(File file) {
        if (file.exists()) {
            if (!file.isDirectory()) {
                throw new PreYggdrasilConverter.ConversionError("Can\'t create directory " + file.getName() + " in world save directory.", null);
            }
        } else if (!file.mkdirs()) {
            throw new PreYggdrasilConverter.ConversionError("Can\'t create directory " + file.getName() + " in world save directory.", null);
        }
    }

    public static boolean tryConvert(PropertyManager propertymanager) {
        boolean flag = hasUnconvertableFiles(propertymanager);

        flag = flag && hasUnconvertablePlayerFiles(propertymanager);
        return flag;
    }

    private static boolean hasUnconvertableFiles(PropertyManager propertymanager) {
        boolean flag = false;

        if (PreYggdrasilConverter.OLD_PLAYERBAN_FILE.exists() && PreYggdrasilConverter.OLD_PLAYERBAN_FILE.isFile()) {
            flag = true;
        }

        boolean flag1 = false;

        if (PreYggdrasilConverter.OLD_IPBAN_FILE.exists() && PreYggdrasilConverter.OLD_IPBAN_FILE.isFile()) {
            flag1 = true;
        }

        boolean flag2 = false;

        if (PreYggdrasilConverter.OLD_OPS_FILE.exists() && PreYggdrasilConverter.OLD_OPS_FILE.isFile()) {
            flag2 = true;
        }

        boolean flag3 = false;

        if (PreYggdrasilConverter.OLD_WHITELIST_FILE.exists() && PreYggdrasilConverter.OLD_WHITELIST_FILE.isFile()) {
            flag3 = true;
        }

        if (!flag && !flag1 && !flag2 && !flag3) {
            return true;
        } else {
            PreYggdrasilConverter.LOGGER.warn("**** FAILED TO START THE SERVER AFTER ACCOUNT CONVERSION!");
            PreYggdrasilConverter.LOGGER.warn("** please remove the following files and restart the server:");
            if (flag) {
                PreYggdrasilConverter.LOGGER.warn("* {}", PreYggdrasilConverter.OLD_PLAYERBAN_FILE.getName());
            }

            if (flag1) {
                PreYggdrasilConverter.LOGGER.warn("* {}", PreYggdrasilConverter.OLD_IPBAN_FILE.getName());
            }

            if (flag2) {
                PreYggdrasilConverter.LOGGER.warn("* {}", PreYggdrasilConverter.OLD_OPS_FILE.getName());
            }

            if (flag3) {
                PreYggdrasilConverter.LOGGER.warn("* {}", PreYggdrasilConverter.OLD_WHITELIST_FILE.getName());
            }

            return false;
        }
    }

    private static boolean hasUnconvertablePlayerFiles(PropertyManager propertymanager) {
        File file = getPlayersDirectory(propertymanager);

        if (file.exists() && file.isDirectory() && (file.list().length > 0 || !file.delete())) {
            PreYggdrasilConverter.LOGGER.warn("**** DETECTED OLD PLAYER DIRECTORY IN THE WORLD SAVE");
            PreYggdrasilConverter.LOGGER.warn("**** THIS USUALLY HAPPENS WHEN THE AUTOMATIC CONVERSION FAILED IN SOME WAY");
            PreYggdrasilConverter.LOGGER.warn("** please restart the server and if the problem persists, remove the directory \'{}\'", file.getPath());
            return false;
        } else {
            return true;
        }
    }

    private static File getPlayersDirectory(PropertyManager propertymanager) {
        String s = propertymanager.getStringProperty("level-name", "world");
        File file = new File(MinecraftServer.getServer().server.getWorldContainer(), s); // CraftBukkit - Respect container setting

        return new File(file, "players");
    }

    private static void backupConverted(File file) {
        File file1 = new File(file.getName() + ".converted");

        file.renameTo(file1);
    }

    private static Date parseDate(String s, Date date) {
        Date date1;

        try {
            date1 = UserListEntryBan.DATE_FORMAT.parse(s);
        } catch (ParseException parseexception) {
            date1 = date;
        }

        return date1;
    }

    static class ConversionError extends RuntimeException {

        private ConversionError(String s, Throwable throwable) {
            super(s, throwable);
        }

        private ConversionError(String s) {
            super(s);
        }

        ConversionError(String s, Object object) {
            this(s);
        }

        ConversionError(String s, Throwable throwable, Object object) {
            this(s, throwable);
        }
    }
}
