package net.minecraft.server.management;

import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.mojang.authlib.Agent;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.GameProfileRepository;
import com.mojang.authlib.ProfileLookupCallback;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import javax.annotation.Nullable;
import org.apache.commons.io.IOUtils;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MCUtil;
import net.minecraft.util.JsonUtils;

public class PlayerProfileCache {

    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
    private static boolean onlineMode;
    private final Map<String, PlayerProfileCache.ProfileEntry> usernameToProfileEntryMap = Maps.newHashMap();private final Map<String, PlayerProfileCache.ProfileEntry> nameCache = usernameToProfileEntryMap; // Paper - OBFHELPER
    private final Map<UUID, PlayerProfileCache.ProfileEntry> uuidToProfileEntryMap = Maps.newHashMap();
    private final Deque<GameProfile> gameProfiles = new java.util.concurrent.LinkedBlockingDeque<GameProfile>(); // CraftBukkit
    private final GameProfileRepository profileRepo;
    protected final Gson gson;
    private final File usercacheFile;
    private static final ParameterizedType TYPE = new ParameterizedType() {
        public Type[] getActualTypeArguments() {
            return new Type[] { PlayerProfileCache.ProfileEntry.class};
        }

        public Type getRawType() {
            return List.class;
        }

        public Type getOwnerType() {
            return null;
        }
    };

    public PlayerProfileCache(GameProfileRepository gameprofilerepository, File file) {
        this.profileRepo = gameprofilerepository;
        this.usercacheFile = file;
        GsonBuilder gsonbuilder = new GsonBuilder();

        gsonbuilder.registerTypeHierarchyAdapter(PlayerProfileCache.ProfileEntry.class, new PlayerProfileCache.Serializer(null));
        this.gson = gsonbuilder.create();
        this.load();
    }

    private static GameProfile lookupProfile(GameProfileRepository gameprofilerepository, String s) {
        final GameProfile[] agameprofile = new GameProfile[1];
        ProfileLookupCallback profilelookupcallback = new ProfileLookupCallback() {
            public void onProfileLookupSucceeded(GameProfile gameprofile) {
                agameprofile[0] = gameprofile;
            }

            public void onProfileLookupFailed(GameProfile gameprofile, Exception exception) {
                agameprofile[0] = null;
            }
        };

        gameprofilerepository.findProfilesByNames(new String[] { s}, Agent.MINECRAFT, profilelookupcallback);
        if (!isOnlineMode() && agameprofile[0] == null && !org.apache.commons.lang3.StringUtils.isBlank(s)) { // Paper - Don't lookup a profile with a blank name
            UUID uuid = EntityPlayer.getUUID(new GameProfile((UUID) null, s));
            GameProfile gameprofile = new GameProfile(uuid, s);

            profilelookupcallback.onProfileLookupSucceeded(gameprofile);
        }

        return agameprofile[0];
    }

    public static void setOnlineMode(boolean flag) {
        PlayerProfileCache.onlineMode = flag;
    }

    private static boolean isOnlineMode() {
        return PlayerProfileCache.onlineMode;
    }

    public void addEntry(GameProfile gameprofile) {
        this.addEntry(gameprofile, (Date) null);
    }

    private synchronized void addEntry(GameProfile gameprofile, Date date) { // Paper - synchronize
        UUID uuid = gameprofile.getId();

        if (date == null) {
            Calendar calendar = Calendar.getInstance();

            calendar.setTime(new Date());
            calendar.add(2, 1);
            date = calendar.getTime();
        }

        String s = gameprofile.getName().toLowerCase(Locale.ROOT);
        PlayerProfileCache.ProfileEntry usercache_usercacheentry = new PlayerProfileCache.ProfileEntry(gameprofile, date, null);

        //if (this.e.containsKey(uuid)) { // Paper
            PlayerProfileCache.ProfileEntry usercache_usercacheentry1 = (PlayerProfileCache.ProfileEntry) this.uuidToProfileEntryMap.get(uuid);
        if (usercache_usercacheentry1 != null) { // Paper

            this.usernameToProfileEntryMap.remove(usercache_usercacheentry1.getGameProfile().getName().toLowerCase(Locale.ROOT));
            this.gameProfiles.remove(gameprofile);
        }

        this.usernameToProfileEntryMap.put(gameprofile.getName().toLowerCase(Locale.ROOT), usercache_usercacheentry);
        this.uuidToProfileEntryMap.put(uuid, usercache_usercacheentry);
        this.gameProfiles.addFirst(gameprofile);
        if( !org.spigotmc.SpigotConfig.saveUserCacheOnStopOnly ) this.save(); // Spigot - skip saving if disabled
    }

    @Nullable
    public synchronized GameProfile getGameProfileForUsername(String s) { // Paper - synchronize
        String s1 = s.toLowerCase(Locale.ROOT);
        PlayerProfileCache.ProfileEntry usercache_usercacheentry = (PlayerProfileCache.ProfileEntry) this.usernameToProfileEntryMap.get(s1);

        if (usercache_usercacheentry != null && (new Date()).getTime() >= usercache_usercacheentry.expirationDate.getTime()) {
            this.uuidToProfileEntryMap.remove(usercache_usercacheentry.getGameProfile().getId());
            this.usernameToProfileEntryMap.remove(usercache_usercacheentry.getGameProfile().getName().toLowerCase(Locale.ROOT));
            this.gameProfiles.remove(usercache_usercacheentry.getGameProfile());
            usercache_usercacheentry = null;
        }

        GameProfile gameprofile;

        if (usercache_usercacheentry != null) {
            gameprofile = usercache_usercacheentry.getGameProfile();
            this.gameProfiles.remove(gameprofile);
            this.gameProfiles.addFirst(gameprofile);
        } else {
            gameprofile = lookupProfile(this.profileRepo, s); // Spigot - use correct case for offline players
            if (gameprofile != null) {
                this.addEntry(gameprofile);
                usercache_usercacheentry = (PlayerProfileCache.ProfileEntry) this.usernameToProfileEntryMap.get(s1);
            }
        }

        if( !org.spigotmc.SpigotConfig.saveUserCacheOnStopOnly ) this.save(); // Spigot - skip saving if disabled
        return usercache_usercacheentry == null ? null : usercache_usercacheentry.getGameProfile();
    }

    public synchronized String[] getUsernames() { // Paper - synchronize
        ArrayList arraylist = Lists.newArrayList(this.usernameToProfileEntryMap.keySet());

        return (String[]) arraylist.toArray(new String[arraylist.size()]);
    }

    // Paper start
    @Nullable public GameProfile getProfileIfCached(String name) {
        PlayerProfileCache.ProfileEntry entry = this.nameCache.get(name.toLowerCase(Locale.ROOT));
        return entry == null ? null : entry.getProfile();
    }
    // Paper end

    @Nullable public GameProfile getProfile(UUID uuid) { return getProfileByUUID(uuid);  } // Paper - OBFHELPER
    @Nullable
    public synchronized GameProfile getProfileByUUID(UUID uuid) { // Paper - synchronize
        PlayerProfileCache.ProfileEntry usercache_usercacheentry = (PlayerProfileCache.ProfileEntry) this.uuidToProfileEntryMap.get(uuid);

        return usercache_usercacheentry == null ? null : usercache_usercacheentry.getGameProfile();
    }

    private PlayerProfileCache.ProfileEntry getByUUID(UUID uuid) {
        PlayerProfileCache.ProfileEntry usercache_usercacheentry = (PlayerProfileCache.ProfileEntry) this.uuidToProfileEntryMap.get(uuid);

        if (usercache_usercacheentry != null) {
            GameProfile gameprofile = usercache_usercacheentry.getGameProfile();

            this.gameProfiles.remove(gameprofile);
            this.gameProfiles.addFirst(gameprofile);
        }

        return usercache_usercacheentry;
    }

    public void load() {
        BufferedReader bufferedreader = null;

        try {
            bufferedreader = Files.newReader(this.usercacheFile, StandardCharsets.UTF_8);
            List list = (List) JsonUtils.fromJson(this.gson, (Reader) bufferedreader, (Type) PlayerProfileCache.TYPE);

            this.usernameToProfileEntryMap.clear();
            this.uuidToProfileEntryMap.clear();
            this.gameProfiles.clear();
            if (list != null) {
                Iterator iterator = Lists.reverse(list).iterator();

                while (iterator.hasNext()) {
                    PlayerProfileCache.ProfileEntry usercache_usercacheentry = (PlayerProfileCache.ProfileEntry) iterator.next();

                    if (usercache_usercacheentry != null) {
                        this.addEntry(usercache_usercacheentry.getGameProfile(), usercache_usercacheentry.getExpirationDate());
                    }
                }
            }
        } catch (FileNotFoundException filenotfoundexception) {
            ;
        // Spigot Start
        } catch (com.google.gson.JsonSyntaxException ex) {
            UserList.LOGGER.warn( "Usercache.json is corrupted or has bad formatting. Deleting it to prevent further issues." );
            this.usercacheFile.delete();
        // Spigot End
        } catch (JsonParseException jsonparseexception) {
            ;
        } finally {
            IOUtils.closeQuietly(bufferedreader);
        }

    }

    // Paper start
    public void save() {
        c(true);
    }
    public void c(boolean asyncSave) {
        // Paper end
        String s = this.gson.toJson(this.getEntriesWithLimit(org.spigotmc.SpigotConfig.userCacheCap));
        Runnable save = () -> {

        BufferedWriter bufferedwriter = null;

        try {
            bufferedwriter = Files.newWriter(this.usercacheFile, StandardCharsets.UTF_8);
            bufferedwriter.write(s);
            return;
        } catch (FileNotFoundException filenotfoundexception) {
            return;
        } catch (IOException ioexception) {
            ;
        } finally {
            IOUtils.closeQuietly(bufferedwriter);
        }
        // Paper start
        };
        if (asyncSave) {
            MCUtil.scheduleAsyncTask(save);
        } else {
            save.run();
        }
        // Paper end

    }

    private List<PlayerProfileCache.ProfileEntry> getEntriesWithLimit(int i) {
        ArrayList arraylist = Lists.newArrayList();
        ArrayList arraylist1 = Lists.newArrayList(Iterators.limit(this.gameProfiles.iterator(), i));
        Iterator iterator = arraylist1.iterator();

        while (iterator.hasNext()) {
            GameProfile gameprofile = (GameProfile) iterator.next();
            PlayerProfileCache.ProfileEntry usercache_usercacheentry = this.getByUUID(gameprofile.getId());

            if (usercache_usercacheentry != null) {
                arraylist.add(usercache_usercacheentry);
            }
        }

        return arraylist;
    }

    class ProfileEntry {

        private final GameProfile gameProfile;public GameProfile getProfile() { return gameProfile; } // Paper - OBFHELPER
        private final Date expirationDate;

        private ProfileEntry(GameProfile gameprofile, Date date) {
            this.gameProfile = gameprofile;
            this.expirationDate = date;
        }

        public GameProfile getGameProfile() {
            return this.gameProfile;
        }

        public Date getExpirationDate() {
            return this.expirationDate;
        }

        ProfileEntry(GameProfile gameprofile, Date date, Object object) {
            this(gameprofile, date);
        }
    }

    class Serializer implements JsonDeserializer<PlayerProfileCache.ProfileEntry>, JsonSerializer<PlayerProfileCache.ProfileEntry> {

        private Serializer() {}

        public JsonElement serialize(PlayerProfileCache.ProfileEntry usercache_usercacheentry, Type type, JsonSerializationContext jsonserializationcontext) {
            JsonObject jsonobject = new JsonObject();

            jsonobject.addProperty("name", usercache_usercacheentry.getGameProfile().getName());
            UUID uuid = usercache_usercacheentry.getGameProfile().getId();

            jsonobject.addProperty("uuid", uuid == null ? "" : uuid.toString());
            jsonobject.addProperty("expiresOn", PlayerProfileCache.DATE_FORMAT.format(usercache_usercacheentry.getExpirationDate()));
            return jsonobject;
        }

        public PlayerProfileCache.ProfileEntry deserialize(JsonElement jsonelement, Type type, JsonDeserializationContext jsondeserializationcontext) throws JsonParseException {
            if (jsonelement.isJsonObject()) {
                JsonObject jsonobject = jsonelement.getAsJsonObject();
                JsonElement jsonelement1 = jsonobject.get("name");
                JsonElement jsonelement2 = jsonobject.get("uuid");
                JsonElement jsonelement3 = jsonobject.get("expiresOn");

                if (jsonelement1 != null && jsonelement2 != null) {
                    String s = jsonelement2.getAsString();
                    String s1 = jsonelement1.getAsString();
                    Date date = null;

                    if (jsonelement3 != null) {
                        try {
                            date = PlayerProfileCache.DATE_FORMAT.parse(jsonelement3.getAsString());
                        } catch (ParseException parseexception) {
                            date = null;
                        }
                    }

                    if (s1 != null && s != null) {
                        UUID uuid;

                        try {
                            uuid = UUID.fromString(s);
                        } catch (Throwable throwable) {
                            return null;
                        }

                        return PlayerProfileCache.this.new ProfileEntry(new GameProfile(uuid, s1), date, null);
                    } else {
                        return null;
                    }
                } else {
                    return null;
                }
            } else {
                return null;
            }
        }

        public JsonElement serialize(ProfileEntry object, Type type, JsonSerializationContext jsonserializationcontext) { // CraftBukkit - decompile error
            return this.serialize((PlayerProfileCache.ProfileEntry) object, type, jsonserializationcontext);
        }

        public ProfileEntry deserialize(JsonElement jsonelement, Type type, JsonDeserializationContext jsondeserializationcontext) throws JsonParseException { // CraftBukkit - decompile error
            return this.deserialize(jsonelement, type, jsondeserializationcontext);
        }

        Serializer(Object object) {
            this();
        }
    }
}
