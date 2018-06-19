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

    public static final SimpleDateFormat field_152659_a = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
    private static boolean field_187322_c;
    private final Map<String, PlayerProfileCache.ProfileEntry> field_152661_c = Maps.newHashMap();private final Map<String, PlayerProfileCache.ProfileEntry> nameCache = field_152661_c; // Paper - OBFHELPER
    private final Map<UUID, PlayerProfileCache.ProfileEntry> field_152662_d = Maps.newHashMap();
    private final Deque<GameProfile> field_152663_e = new java.util.concurrent.LinkedBlockingDeque<GameProfile>(); // CraftBukkit
    private final GameProfileRepository field_187323_g;
    protected final Gson field_152660_b;
    private final File field_152665_g;
    private static final ParameterizedType field_152666_h = new ParameterizedType() {
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
        this.field_187323_g = gameprofilerepository;
        this.field_152665_g = file;
        GsonBuilder gsonbuilder = new GsonBuilder();

        gsonbuilder.registerTypeHierarchyAdapter(PlayerProfileCache.ProfileEntry.class, new PlayerProfileCache.Serializer(null));
        this.field_152660_b = gsonbuilder.create();
        this.func_152657_b();
    }

    private static GameProfile func_187319_a(GameProfileRepository gameprofilerepository, String s) {
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
        if (!func_187321_d() && agameprofile[0] == null && !org.apache.commons.lang3.StringUtils.isBlank(s)) { // Paper - Don't lookup a profile with a blank name
            UUID uuid = EntityPlayer.func_146094_a(new GameProfile((UUID) null, s));
            GameProfile gameprofile = new GameProfile(uuid, s);

            profilelookupcallback.onProfileLookupSucceeded(gameprofile);
        }

        return agameprofile[0];
    }

    public static void func_187320_a(boolean flag) {
        PlayerProfileCache.field_187322_c = flag;
    }

    private static boolean func_187321_d() {
        return PlayerProfileCache.field_187322_c;
    }

    public void func_152649_a(GameProfile gameprofile) {
        this.func_152651_a(gameprofile, (Date) null);
    }

    private synchronized void func_152651_a(GameProfile gameprofile, Date date) { // Paper - synchronize
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
            PlayerProfileCache.ProfileEntry usercache_usercacheentry1 = (PlayerProfileCache.ProfileEntry) this.field_152662_d.get(uuid);
        if (usercache_usercacheentry1 != null) { // Paper

            this.field_152661_c.remove(usercache_usercacheentry1.func_152668_a().getName().toLowerCase(Locale.ROOT));
            this.field_152663_e.remove(gameprofile);
        }

        this.field_152661_c.put(gameprofile.getName().toLowerCase(Locale.ROOT), usercache_usercacheentry);
        this.field_152662_d.put(uuid, usercache_usercacheentry);
        this.field_152663_e.addFirst(gameprofile);
        if( !org.spigotmc.SpigotConfig.saveUserCacheOnStopOnly ) this.func_152658_c(); // Spigot - skip saving if disabled
    }

    @Nullable
    public synchronized GameProfile func_152655_a(String s) { // Paper - synchronize
        String s1 = s.toLowerCase(Locale.ROOT);
        PlayerProfileCache.ProfileEntry usercache_usercacheentry = (PlayerProfileCache.ProfileEntry) this.field_152661_c.get(s1);

        if (usercache_usercacheentry != null && (new Date()).getTime() >= usercache_usercacheentry.field_152673_c.getTime()) {
            this.field_152662_d.remove(usercache_usercacheentry.func_152668_a().getId());
            this.field_152661_c.remove(usercache_usercacheentry.func_152668_a().getName().toLowerCase(Locale.ROOT));
            this.field_152663_e.remove(usercache_usercacheentry.func_152668_a());
            usercache_usercacheentry = null;
        }

        GameProfile gameprofile;

        if (usercache_usercacheentry != null) {
            gameprofile = usercache_usercacheentry.func_152668_a();
            this.field_152663_e.remove(gameprofile);
            this.field_152663_e.addFirst(gameprofile);
        } else {
            gameprofile = func_187319_a(this.field_187323_g, s); // Spigot - use correct case for offline players
            if (gameprofile != null) {
                this.func_152649_a(gameprofile);
                usercache_usercacheentry = (PlayerProfileCache.ProfileEntry) this.field_152661_c.get(s1);
            }
        }

        if( !org.spigotmc.SpigotConfig.saveUserCacheOnStopOnly ) this.func_152658_c(); // Spigot - skip saving if disabled
        return usercache_usercacheentry == null ? null : usercache_usercacheentry.func_152668_a();
    }

    public synchronized String[] func_152654_a() { // Paper - synchronize
        ArrayList arraylist = Lists.newArrayList(this.field_152661_c.keySet());

        return (String[]) arraylist.toArray(new String[arraylist.size()]);
    }

    // Paper start
    @Nullable public GameProfile getProfileIfCached(String name) {
        PlayerProfileCache.ProfileEntry entry = this.nameCache.get(name.toLowerCase(Locale.ROOT));
        return entry == null ? null : entry.getProfile();
    }
    // Paper end

    @Nullable public GameProfile getProfile(UUID uuid) { return func_152652_a(uuid);  } // Paper - OBFHELPER
    @Nullable
    public synchronized GameProfile func_152652_a(UUID uuid) { // Paper - synchronize
        PlayerProfileCache.ProfileEntry usercache_usercacheentry = (PlayerProfileCache.ProfileEntry) this.field_152662_d.get(uuid);

        return usercache_usercacheentry == null ? null : usercache_usercacheentry.func_152668_a();
    }

    private PlayerProfileCache.ProfileEntry func_152653_b(UUID uuid) {
        PlayerProfileCache.ProfileEntry usercache_usercacheentry = (PlayerProfileCache.ProfileEntry) this.field_152662_d.get(uuid);

        if (usercache_usercacheentry != null) {
            GameProfile gameprofile = usercache_usercacheentry.func_152668_a();

            this.field_152663_e.remove(gameprofile);
            this.field_152663_e.addFirst(gameprofile);
        }

        return usercache_usercacheentry;
    }

    public void func_152657_b() {
        BufferedReader bufferedreader = null;

        try {
            bufferedreader = Files.newReader(this.field_152665_g, StandardCharsets.UTF_8);
            List list = (List) JsonUtils.func_193841_a(this.field_152660_b, (Reader) bufferedreader, (Type) PlayerProfileCache.field_152666_h);

            this.field_152661_c.clear();
            this.field_152662_d.clear();
            this.field_152663_e.clear();
            if (list != null) {
                Iterator iterator = Lists.reverse(list).iterator();

                while (iterator.hasNext()) {
                    PlayerProfileCache.ProfileEntry usercache_usercacheentry = (PlayerProfileCache.ProfileEntry) iterator.next();

                    if (usercache_usercacheentry != null) {
                        this.func_152651_a(usercache_usercacheentry.func_152668_a(), usercache_usercacheentry.func_152670_b());
                    }
                }
            }
        } catch (FileNotFoundException filenotfoundexception) {
            ;
        // Spigot Start
        } catch (com.google.gson.JsonSyntaxException ex) {
            UserList.field_152693_a.warn( "Usercache.json is corrupted or has bad formatting. Deleting it to prevent further issues." );
            this.field_152665_g.delete();
        // Spigot End
        } catch (JsonParseException jsonparseexception) {
            ;
        } finally {
            IOUtils.closeQuietly(bufferedreader);
        }

    }

    // Paper start
    public void func_152658_c() {
        c(true);
    }
    public void c(boolean asyncSave) {
        // Paper end
        String s = this.field_152660_b.toJson(this.func_152656_a(org.spigotmc.SpigotConfig.userCacheCap));
        Runnable save = () -> {

        BufferedWriter bufferedwriter = null;

        try {
            bufferedwriter = Files.newWriter(this.field_152665_g, StandardCharsets.UTF_8);
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

    private List<PlayerProfileCache.ProfileEntry> func_152656_a(int i) {
        ArrayList arraylist = Lists.newArrayList();
        ArrayList arraylist1 = Lists.newArrayList(Iterators.limit(this.field_152663_e.iterator(), i));
        Iterator iterator = arraylist1.iterator();

        while (iterator.hasNext()) {
            GameProfile gameprofile = (GameProfile) iterator.next();
            PlayerProfileCache.ProfileEntry usercache_usercacheentry = this.func_152653_b(gameprofile.getId());

            if (usercache_usercacheentry != null) {
                arraylist.add(usercache_usercacheentry);
            }
        }

        return arraylist;
    }

    class ProfileEntry {

        private final GameProfile field_152672_b;public GameProfile getProfile() { return field_152672_b; } // Paper - OBFHELPER
        private final Date field_152673_c;

        private ProfileEntry(GameProfile gameprofile, Date date) {
            this.field_152672_b = gameprofile;
            this.field_152673_c = date;
        }

        public GameProfile func_152668_a() {
            return this.field_152672_b;
        }

        public Date func_152670_b() {
            return this.field_152673_c;
        }

        ProfileEntry(GameProfile gameprofile, Date date, Object object) {
            this(gameprofile, date);
        }
    }

    class Serializer implements JsonDeserializer<PlayerProfileCache.ProfileEntry>, JsonSerializer<PlayerProfileCache.ProfileEntry> {

        private Serializer() {}

        public JsonElement serialize(PlayerProfileCache.ProfileEntry usercache_usercacheentry, Type type, JsonSerializationContext jsonserializationcontext) {
            JsonObject jsonobject = new JsonObject();

            jsonobject.addProperty("name", usercache_usercacheentry.func_152668_a().getName());
            UUID uuid = usercache_usercacheentry.func_152668_a().getId();

            jsonobject.addProperty("uuid", uuid == null ? "" : uuid.toString());
            jsonobject.addProperty("expiresOn", PlayerProfileCache.field_152659_a.format(usercache_usercacheentry.func_152670_b()));
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
                            date = PlayerProfileCache.field_152659_a.parse(jsonelement3.getAsString());
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
