package net.minecraft.server.management;

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
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.util.JsonUtils;

public class UserList<K, V extends UserListEntry<K>> {

    protected static final Logger field_152693_a = LogManager.getLogger();
    protected final Gson field_152694_b;
    private final File field_152695_c;
    private final Map<String, V> field_152696_d = Maps.newHashMap();
    private boolean field_152697_e = true;
    private static final ParameterizedType field_152698_f = new ParameterizedType() {
        public Type[] getActualTypeArguments() {
            return new Type[] { UserListEntry.class};
        }

        public Type getRawType() {
            return List.class;
        }

        public Type getOwnerType() {
            return null;
        }
    };

    public UserList(File file) {
        this.field_152695_c = file;
        GsonBuilder gsonbuilder = (new GsonBuilder()).setPrettyPrinting();

        gsonbuilder.registerTypeHierarchyAdapter(UserListEntry.class, new UserList.Serializer(null));
        this.field_152694_b = gsonbuilder.create();
    }

    public boolean func_152689_b() {
        return this.field_152697_e;
    }

    public void func_152686_a(boolean flag) {
        this.field_152697_e = flag;
    }

    public File func_152691_c() {
        return this.field_152695_c;
    }

    public void func_152687_a(V v0) {
        this.field_152696_d.put(this.func_152681_a(v0.func_152640_f()), v0);

        try {
            this.func_152678_f();
        } catch (IOException ioexception) {
            UserList.field_152693_a.warn("Could not save the list after adding a user.", ioexception);
        }

    }

    public V func_152683_b(K k0) {
        this.func_152680_h();
        return (V) this.field_152696_d.get(this.func_152681_a(k0)); // CraftBukkit - fix decompile error
    }

    public void func_152684_c(K k0) {
        this.field_152696_d.remove(this.func_152681_a(k0));

        try {
            this.func_152678_f();
        } catch (IOException ioexception) {
            UserList.field_152693_a.warn("Could not save the list after removing a user.", ioexception);
        }

    }

    public String[] func_152685_a() {
        return (String[]) this.field_152696_d.keySet().toArray(new String[this.field_152696_d.size()]);
    }

    // CraftBukkit start
    public Collection<V> getValues() {
        return this.field_152696_d.values();
    }
    // CraftBukkit end

    public boolean func_152690_d() {
        return this.field_152696_d.size() < 1;
    }

    protected String func_152681_a(K k0) {
        return k0.toString();
    }

    protected boolean func_152692_d(K k0) {
        return this.field_152696_d.containsKey(this.func_152681_a(k0));
    }

    private void func_152680_h() {
        ArrayList arraylist = Lists.newArrayList();
        Iterator iterator = this.field_152696_d.values().iterator();

        while (iterator.hasNext()) {
            UserListEntry jsonlistentry = (UserListEntry) iterator.next();

            if (jsonlistentry.func_73682_e()) {
                arraylist.add(jsonlistentry.func_152640_f());
            }
        }

        iterator = arraylist.iterator();

        while (iterator.hasNext()) {
            Object object = iterator.next();

            this.field_152696_d.remove(object);
        }

    }

    protected UserListEntry<K> func_152682_a(JsonObject jsonobject) {
        return new UserListEntry((Object) null, jsonobject);
    }

    protected Map<String, V> func_152688_e() {
        return this.field_152696_d;
    }

    public void func_152678_f() throws IOException {
        Collection collection = this.field_152696_d.values();
        String s = this.field_152694_b.toJson(collection);
        BufferedWriter bufferedwriter = null;

        try {
            bufferedwriter = Files.newWriter(this.field_152695_c, StandardCharsets.UTF_8);
            bufferedwriter.write(s);
        } finally {
            IOUtils.closeQuietly(bufferedwriter);
        }

    }

    public void func_152679_g() throws FileNotFoundException {
        if (this.field_152695_c.exists()) {
            Collection collection = null;
            BufferedReader bufferedreader = null;

            try {
                bufferedreader = Files.newReader(this.field_152695_c, StandardCharsets.UTF_8);
                collection = (Collection) JsonUtils.func_193841_a(this.field_152694_b, (Reader) bufferedreader, (Type) UserList.field_152698_f);
            // Spigot Start
            } catch ( com.google.gson.JsonParseException ex )
            {
                org.bukkit.Bukkit.getLogger().log( java.util.logging.Level.WARNING, "Unable to read file " + this.field_152695_c + ", backing it up to {0}.backup and creating new copy.", ex );
                File backup = new File( this.field_152695_c + ".backup" );
                this.field_152695_c.renameTo( backup );
                this.field_152695_c.delete();
            // Spigot End
            } finally {
                IOUtils.closeQuietly(bufferedreader);
            }

            if (collection != null) {
                this.field_152696_d.clear();
                Iterator iterator = collection.iterator();

                while (iterator.hasNext()) {
                    UserListEntry jsonlistentry = (UserListEntry) iterator.next();

                    if (jsonlistentry.func_152640_f() != null) {
                        this.field_152696_d.put(this.func_152681_a((K) jsonlistentry.func_152640_f()), (V) jsonlistentry); // CraftBukkit - fix decompile error
                    }
                }
            }

        }
    }

    class Serializer implements JsonDeserializer<UserListEntry<K>>, JsonSerializer<UserListEntry<K>> {

        private Serializer() {}

        public JsonElement serialize(UserListEntry<K> jsonlistentry, Type type, JsonSerializationContext jsonserializationcontext) {
            JsonObject jsonobject = new JsonObject();

            jsonlistentry.func_152641_a(jsonobject);
            return jsonobject;
        }

        public UserListEntry<K> deserialize(JsonElement jsonelement, Type type, JsonDeserializationContext jsondeserializationcontext) throws JsonParseException {
            if (jsonelement.isJsonObject()) {
                JsonObject jsonobject = jsonelement.getAsJsonObject();

                return UserList.this.func_152682_a(jsonobject);
            } else {
                return null;
            }
        }

        public JsonElement serialize(UserListEntry<K> object, Type type, JsonSerializationContext jsonserializationcontext) { // CraftBukkit - fix decompile error
            return this.serialize((UserListEntry) object, type, jsonserializationcontext);
        }

        public UserListEntry<K> deserialize(JsonElement jsonelement, Type type, JsonDeserializationContext jsondeserializationcontext) throws JsonParseException { // CraftBukkit - fix decompile error
            return this.deserialize(jsonelement, type, jsondeserializationcontext);
        }

        Serializer(Object object) {
            this();
        }
    }
}
