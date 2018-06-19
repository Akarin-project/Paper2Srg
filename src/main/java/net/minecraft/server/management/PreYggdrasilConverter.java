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

    private static final Logger field_152732_e = LogManager.getLogger();
    public static final File field_152728_a = new File("banned-ips.txt");
    public static final File field_152729_b = new File("banned-players.txt");
    public static final File field_152730_c = new File("ops.txt");
    public static final File field_152731_d = new File("white-list.txt");

    static List<String> func_152721_a(File file, Map<String, String[]> map) throws IOException {
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

    private static void func_152717_a(MinecraftServer minecraftserver, Collection<String> collection, ProfileLookupCallback profilelookupcallback) {
        String[] astring = (String[]) Iterators.toArray(Iterators.filter(collection.iterator(), new Predicate() {
            public boolean a(@Nullable String s) {
                return !StringUtils.func_151246_b(s);
            }

            public boolean apply(@Nullable Object object) {
                return this.a((String) object);
            }
        }), String.class);

        if (minecraftserver.func_71266_T()
                || (org.spigotmc.SpigotConfig.bungee && com.destroystokyo.paper.PaperConfig.bungeeOnlineMode)) { // Spigot: bungee = online mode, for now.  // Paper - Handle via setting
            minecraftserver.func_152359_aw().findProfilesByNames(astring, Agent.MINECRAFT, profilelookupcallback);
        } else {
            String[] astring1 = astring;
            int i = astring.length;

            for (int j = 0; j < i; ++j) {
                String s = astring1[j];
                UUID uuid = EntityPlayer.func_146094_a(new GameProfile((UUID) null, s));
                GameProfile gameprofile = new GameProfile(uuid, s);

                profilelookupcallback.onProfileLookupSucceeded(gameprofile);
            }
        }

    }

    public static boolean func_152724_a(final MinecraftServer minecraftserver) {
        final UserListBans gameprofilebanlist = new UserListBans(PlayerList.field_152613_a);

        if (PreYggdrasilConverter.field_152729_b.exists() && PreYggdrasilConverter.field_152729_b.isFile()) {
            if (gameprofilebanlist.func_152691_c().exists()) {
                try {
                    gameprofilebanlist.func_152679_g();
                // CraftBukkit start - FileNotFoundException -> IOException, don't print stacktrace
                } catch (IOException filenotfoundexception) {
                    PreYggdrasilConverter.field_152732_e.warn("Could not load existing file {}", gameprofilebanlist.func_152691_c().getName());
                }
            }

            try {
                final HashMap hashmap = Maps.newHashMap();

                func_152721_a(PreYggdrasilConverter.field_152729_b, (Map) hashmap);
                ProfileLookupCallback profilelookupcallback = new ProfileLookupCallback() {
                    public void onProfileLookupSucceeded(GameProfile gameprofile) {
                        minecraftserver.func_152358_ax().func_152649_a(gameprofile);
                        String[] astring = (String[]) hashmap.get(gameprofile.getName().toLowerCase(Locale.ROOT));

                        if (astring == null) {
                            PreYggdrasilConverter.field_152732_e.warn("Could not convert user banlist entry for {}", gameprofile.getName());
                            throw new PreYggdrasilConverter.ConversionError("Profile not in the conversionlist", null);
                        } else {
                            Date date = astring.length > 1 ? PreYggdrasilConverter.func_152713_b(astring[1], (Date) null) : null;
                            String s = astring.length > 2 ? astring[2] : null;
                            Date date1 = astring.length > 3 ? PreYggdrasilConverter.func_152713_b(astring[3], (Date) null) : null;
                            String s1 = astring.length > 4 ? astring[4] : null;

                            gameprofilebanlist.func_152687_a(new UserListBansEntry(gameprofile, date, s, date1, s1));
                        }
                    }

                    public void onProfileLookupFailed(GameProfile gameprofile, Exception exception) {
                        PreYggdrasilConverter.field_152732_e.warn("Could not lookup user banlist entry for {}", gameprofile.getName(), exception);
                        if (!(exception instanceof ProfileNotFoundException)) {
                            throw new PreYggdrasilConverter.ConversionError("Could not request user " + gameprofile.getName() + " from backend systems", exception, null);
                        }
                    }
                };

                func_152717_a(minecraftserver, hashmap.keySet(), profilelookupcallback);
                gameprofilebanlist.func_152678_f();
                func_152727_c(PreYggdrasilConverter.field_152729_b);
                return true;
            } catch (IOException ioexception) {
                PreYggdrasilConverter.field_152732_e.warn("Could not read old user banlist to convert it!", ioexception);
                return false;
            } catch (PreYggdrasilConverter.ConversionError namereferencingfileconverter_fileconversionexception) {
                PreYggdrasilConverter.field_152732_e.error("Conversion failed, please try again later", namereferencingfileconverter_fileconversionexception);
                return false;
            }
        } else {
            return true;
        }
    }

    public static boolean func_152722_b(MinecraftServer minecraftserver) {
        UserListIPBans ipbanlist = new UserListIPBans(PlayerList.field_152614_b);

        if (PreYggdrasilConverter.field_152728_a.exists() && PreYggdrasilConverter.field_152728_a.isFile()) {
            if (ipbanlist.func_152691_c().exists()) {
                try {
                    ipbanlist.func_152679_g();
                // CraftBukkit start - FileNotFoundException -> IOException, don't print stacktrace
                } catch (IOException filenotfoundexception) {
                    PreYggdrasilConverter.field_152732_e.warn("Could not load existing file {}", ipbanlist.func_152691_c().getName());
                }
            }

            try {
                HashMap hashmap = Maps.newHashMap();

                func_152721_a(PreYggdrasilConverter.field_152728_a, (Map) hashmap);
                Iterator iterator = hashmap.keySet().iterator();

                while (iterator.hasNext()) {
                    String s = (String) iterator.next();
                    String[] astring = (String[]) hashmap.get(s);
                    Date date = astring.length > 1 ? func_152713_b(astring[1], (Date) null) : null;
                    String s1 = astring.length > 2 ? astring[2] : null;
                    Date date1 = astring.length > 3 ? func_152713_b(astring[3], (Date) null) : null;
                    String s2 = astring.length > 4 ? astring[4] : null;

                    ipbanlist.func_152687_a(new UserListIPBansEntry(s, date, s1, date1, s2));
                }

                ipbanlist.func_152678_f();
                func_152727_c(PreYggdrasilConverter.field_152728_a);
                return true;
            } catch (IOException ioexception) {
                PreYggdrasilConverter.field_152732_e.warn("Could not parse old ip banlist to convert it!", ioexception);
                return false;
            }
        } else {
            return true;
        }
    }

    public static boolean func_152718_c(final MinecraftServer minecraftserver) {
        final UserListOps oplist = new UserListOps(PlayerList.field_152615_c);

        if (PreYggdrasilConverter.field_152730_c.exists() && PreYggdrasilConverter.field_152730_c.isFile()) {
            if (oplist.func_152691_c().exists()) {
                try {
                    oplist.func_152679_g();
                // CraftBukkit start - FileNotFoundException -> IOException, don't print stacktrace
                } catch (IOException filenotfoundexception) {
                    PreYggdrasilConverter.field_152732_e.warn("Could not load existing file {}", oplist.func_152691_c().getName());
                }
            }

            try {
                List list = Files.readLines(PreYggdrasilConverter.field_152730_c, StandardCharsets.UTF_8);
                ProfileLookupCallback profilelookupcallback = new ProfileLookupCallback() {
                    public void onProfileLookupSucceeded(GameProfile gameprofile) {
                        minecraftserver.func_152358_ax().func_152649_a(gameprofile);
                        oplist.func_152687_a(new UserListOpsEntry(gameprofile, minecraftserver.func_110455_j(), false));
                    }

                    public void onProfileLookupFailed(GameProfile gameprofile, Exception exception) {
                        PreYggdrasilConverter.field_152732_e.warn("Could not lookup oplist entry for {}", gameprofile.getName(), exception);
                        if (!(exception instanceof ProfileNotFoundException)) {
                            throw new PreYggdrasilConverter.ConversionError("Could not request user " + gameprofile.getName() + " from backend systems", exception, null);
                        }
                    }
                };

                func_152717_a(minecraftserver, list, profilelookupcallback);
                oplist.func_152678_f();
                func_152727_c(PreYggdrasilConverter.field_152730_c);
                return true;
            } catch (IOException ioexception) {
                PreYggdrasilConverter.field_152732_e.warn("Could not read old oplist to convert it!", ioexception);
                return false;
            } catch (PreYggdrasilConverter.ConversionError namereferencingfileconverter_fileconversionexception) {
                PreYggdrasilConverter.field_152732_e.error("Conversion failed, please try again later", namereferencingfileconverter_fileconversionexception);
                return false;
            }
        } else {
            return true;
        }
    }

    public static boolean func_152710_d(final MinecraftServer minecraftserver) {
        final UserListWhitelist whitelist = new UserListWhitelist(PlayerList.field_152616_d);

        if (PreYggdrasilConverter.field_152731_d.exists() && PreYggdrasilConverter.field_152731_d.isFile()) {
            if (whitelist.func_152691_c().exists()) {
                try {
                    whitelist.func_152679_g();
                // CraftBukkit start - FileNotFoundException -> IOException, don't print stacktrace
                } catch (IOException filenotfoundexception) {
                    PreYggdrasilConverter.field_152732_e.warn("Could not load existing file {}", whitelist.func_152691_c().getName());
                }
            }

            try {
                List list = Files.readLines(PreYggdrasilConverter.field_152731_d, StandardCharsets.UTF_8);
                ProfileLookupCallback profilelookupcallback = new ProfileLookupCallback() {
                    public void onProfileLookupSucceeded(GameProfile gameprofile) {
                        minecraftserver.func_152358_ax().func_152649_a(gameprofile);
                        whitelist.func_152687_a(new UserListWhitelistEntry(gameprofile));
                    }

                    public void onProfileLookupFailed(GameProfile gameprofile, Exception exception) {
                        PreYggdrasilConverter.field_152732_e.warn("Could not lookup user whitelist entry for {}", gameprofile.getName(), exception);
                        if (!(exception instanceof ProfileNotFoundException)) {
                            throw new PreYggdrasilConverter.ConversionError("Could not request user " + gameprofile.getName() + " from backend systems", exception, null);
                        }
                    }
                };

                func_152717_a(minecraftserver, list, profilelookupcallback);
                whitelist.func_152678_f();
                func_152727_c(PreYggdrasilConverter.field_152731_d);
                return true;
            } catch (IOException ioexception) {
                PreYggdrasilConverter.field_152732_e.warn("Could not read old whitelist to convert it!", ioexception);
                return false;
            } catch (PreYggdrasilConverter.ConversionError namereferencingfileconverter_fileconversionexception) {
                PreYggdrasilConverter.field_152732_e.error("Conversion failed, please try again later", namereferencingfileconverter_fileconversionexception);
                return false;
            }
        } else {
            return true;
        }
    }

    public static String func_187473_a(final MinecraftServer minecraftserver, String s) {
        if (!StringUtils.func_151246_b(s) && s.length() <= 16) {
            GameProfile gameprofile = minecraftserver.func_152358_ax().func_152655_a(s);

            if (gameprofile != null && gameprofile.getId() != null) {
                return gameprofile.getId().toString();
            } else if (!minecraftserver.func_71264_H() && minecraftserver.func_71266_T()) {
                final ArrayList arraylist = Lists.newArrayList();
                ProfileLookupCallback profilelookupcallback = new ProfileLookupCallback() {
                    public void onProfileLookupSucceeded(GameProfile gameprofile) {
                        minecraftserver.func_152358_ax().func_152649_a(gameprofile);
                        arraylist.add(gameprofile);
                    }

                    public void onProfileLookupFailed(GameProfile gameprofile, Exception exception) {
                        PreYggdrasilConverter.field_152732_e.warn("Could not lookup user whitelist entry for {}", gameprofile.getName(), exception);
                    }
                };

                func_152717_a(minecraftserver, Lists.newArrayList(new String[] { s}), profilelookupcallback);
                return !arraylist.isEmpty() && ((GameProfile) arraylist.get(0)).getId() != null ? ((GameProfile) arraylist.get(0)).getId().toString() : "";
            } else {
                return EntityPlayer.func_146094_a(new GameProfile((UUID) null, s)).toString();
            }
        } else {
            return s;
        }
    }

    public static boolean func_152723_a(final DedicatedServer dedicatedserver, PropertyManager propertymanager) {
        final File file = func_152725_d(propertymanager);
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
                        dedicatedserver.func_152358_ax().func_152649_a(gameprofile);
                        UUID uuid = gameprofile.getId();

                        if (uuid == null) {
                            throw new PreYggdrasilConverter.ConversionError("Missing UUID for user profile " + gameprofile.getName(), null);
                        } else {
                            this.a(file, this.a(gameprofile), uuid.toString());
                        }
                    }

                    public void onProfileLookupFailed(GameProfile gameprofile, Exception exception) {
                        PreYggdrasilConverter.field_152732_e.warn("Could not lookup user uuid for {}", gameprofile.getName(), exception);
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
                            root = CompressedStreamTools.func_74796_a(new java.io.FileInputStream(file1));
                        } catch (Exception exception) {
                            exception.printStackTrace();
                            ServerInternalException.reportInternalException(exception); // Paper
                        }

                        if (root != null) {
                            if (!root.func_74764_b("bukkit")) {
                                root.func_74782_a("bukkit", new NBTTagCompound());
                            }
                            NBTTagCompound data = root.func_74775_l("bukkit");
                            data.func_74778_a("lastKnownName", s);

                            try {
                                CompressedStreamTools.func_74799_a(root, new java.io.FileOutputStream(file2));
                            } catch (Exception exception) {
                                exception.printStackTrace();
                                ServerInternalException.reportInternalException(exception); // Paper
                            }
                       }
                        // CraftBukkit end

                        PreYggdrasilConverter.func_152711_b(file);
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

                func_152717_a(dedicatedserver, Lists.newArrayList(astring), profilelookupcallback);
                return true;
            } catch (PreYggdrasilConverter.ConversionError namereferencingfileconverter_fileconversionexception) {
                PreYggdrasilConverter.field_152732_e.error("Conversion failed, please try again later", namereferencingfileconverter_fileconversionexception);
                return false;
            }
        } else {
            return true;
        }
    }

    private static void func_152711_b(File file) {
        if (file.exists()) {
            if (!file.isDirectory()) {
                throw new PreYggdrasilConverter.ConversionError("Can\'t create directory " + file.getName() + " in world save directory.", null);
            }
        } else if (!file.mkdirs()) {
            throw new PreYggdrasilConverter.ConversionError("Can\'t create directory " + file.getName() + " in world save directory.", null);
        }
    }

    public static boolean func_152714_a(PropertyManager propertymanager) {
        boolean flag = func_152712_b(propertymanager);

        flag = flag && func_152715_c(propertymanager);
        return flag;
    }

    private static boolean func_152712_b(PropertyManager propertymanager) {
        boolean flag = false;

        if (PreYggdrasilConverter.field_152729_b.exists() && PreYggdrasilConverter.field_152729_b.isFile()) {
            flag = true;
        }

        boolean flag1 = false;

        if (PreYggdrasilConverter.field_152728_a.exists() && PreYggdrasilConverter.field_152728_a.isFile()) {
            flag1 = true;
        }

        boolean flag2 = false;

        if (PreYggdrasilConverter.field_152730_c.exists() && PreYggdrasilConverter.field_152730_c.isFile()) {
            flag2 = true;
        }

        boolean flag3 = false;

        if (PreYggdrasilConverter.field_152731_d.exists() && PreYggdrasilConverter.field_152731_d.isFile()) {
            flag3 = true;
        }

        if (!flag && !flag1 && !flag2 && !flag3) {
            return true;
        } else {
            PreYggdrasilConverter.field_152732_e.warn("**** FAILED TO START THE SERVER AFTER ACCOUNT CONVERSION!");
            PreYggdrasilConverter.field_152732_e.warn("** please remove the following files and restart the server:");
            if (flag) {
                PreYggdrasilConverter.field_152732_e.warn("* {}", PreYggdrasilConverter.field_152729_b.getName());
            }

            if (flag1) {
                PreYggdrasilConverter.field_152732_e.warn("* {}", PreYggdrasilConverter.field_152728_a.getName());
            }

            if (flag2) {
                PreYggdrasilConverter.field_152732_e.warn("* {}", PreYggdrasilConverter.field_152730_c.getName());
            }

            if (flag3) {
                PreYggdrasilConverter.field_152732_e.warn("* {}", PreYggdrasilConverter.field_152731_d.getName());
            }

            return false;
        }
    }

    private static boolean func_152715_c(PropertyManager propertymanager) {
        File file = func_152725_d(propertymanager);

        if (file.exists() && file.isDirectory() && (file.list().length > 0 || !file.delete())) {
            PreYggdrasilConverter.field_152732_e.warn("**** DETECTED OLD PLAYER DIRECTORY IN THE WORLD SAVE");
            PreYggdrasilConverter.field_152732_e.warn("**** THIS USUALLY HAPPENS WHEN THE AUTOMATIC CONVERSION FAILED IN SOME WAY");
            PreYggdrasilConverter.field_152732_e.warn("** please restart the server and if the problem persists, remove the directory \'{}\'", file.getPath());
            return false;
        } else {
            return true;
        }
    }

    private static File func_152725_d(PropertyManager propertymanager) {
        String s = propertymanager.func_73671_a("level-name", "world");
        File file = new File(MinecraftServer.getServer().server.getWorldContainer(), s); // CraftBukkit - Respect container setting

        return new File(file, "players");
    }

    private static void func_152727_c(File file) {
        File file1 = new File(file.getName() + ".converted");

        file.renameTo(file1);
    }

    private static Date func_152713_b(String s, Date date) {
        Date date1;

        try {
            date1 = UserListEntryBan.field_73698_a.parse(s);
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
