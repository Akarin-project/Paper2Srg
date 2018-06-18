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

    protected static final Logger LOGGER = LogManager.getLogger();
    protected final Gson gson;
    private final File saveFile;
    private final Map<String, V> values = Maps.newHashMap();
    private boolean lanServer = true;
    private static final ParameterizedType USER_LIST_ENTRY_TYPE = new ParameterizedType() {
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
        this.saveFile = file;
        GsonBuilder gsonbuilder = (new GsonBuilder()).setPrettyPrinting();

        gsonbuilder.registerTypeHierarchyAdapter(UserListEntry.class, new UserList.Serializer(null));
        this.gson = gsonbuilder.create();
    }

    public boolean isLanServer() {
        return this.lanServer;
    }

    public void setLanServer(boolean flag) {
        this.lanServer = flag;
    }

    public File getSaveFile() {
        return this.saveFile;
    }

    public void addEntry(V v0) {
        this.values.put(this.getObjectKey(v0.getValue()), v0);

        try {
            this.writeChanges();
        } catch (IOException ioexception) {
            UserList.LOGGER.warn("Could not save the list after adding a user.", ioexception);
        }

    }

    public V getEntry(K k0) {
        this.removeExpired();
        return (V) this.values.get(this.getObjectKey(k0)); // CraftBukkit - fix decompile error
    }

    public void removeEntry(K k0) {
        this.values.remove(this.getObjectKey(k0));

        try {
            this.writeChanges();
        } catch (IOException ioexception) {
            UserList.LOGGER.warn("Could not save the list after removing a user.", ioexception);
        }

    }

    public String[] getKeys() {
        return (String[]) this.values.keySet().toArray(new String[this.values.size()]);
    }

    // CraftBukkit start
    public Collection<V> getValues() {
        return this.values.values();
    }
    // CraftBukkit end

    public boolean isEmpty() {
        return this.values.size() < 1;
    }

    protected String getObjectKey(K k0) {
        return k0.toString();
    }

    protected boolean hasEntry(K k0) {
        return this.values.containsKey(this.getObjectKey(k0));
    }

    private void removeExpired() {
        ArrayList arraylist = Lists.newArrayList();
        Iterator iterator = this.values.values().iterator();

        while (iterator.hasNext()) {
            UserListEntry jsonlistentry = (UserListEntry) iterator.next();

            if (jsonlistentry.hasBanExpired()) {
                arraylist.add(jsonlistentry.getValue());
            }
        }

        iterator = arraylist.iterator();

        while (iterator.hasNext()) {
            Object object = iterator.next();

            this.values.remove(object);
        }

    }

    protected UserListEntry<K> createEntry(JsonObject jsonobject) {
        return new UserListEntry((Object) null, jsonobject);
    }

    protected Map<String, V> getValues() {
        return this.values;
    }

    public void writeChanges() throws IOException {
        Collection collection = this.values.values();
        String s = this.gson.toJson(collection);
        BufferedWriter bufferedwriter = null;

        try {
            bufferedwriter = Files.newWriter(this.saveFile, StandardCharsets.UTF_8);
            bufferedwriter.write(s);
        } finally {
            IOUtils.closeQuietly(bufferedwriter);
        }

    }

    public void readSavedFile() throws FileNotFoundException {
        if (this.saveFile.exists()) {
            Collection collection = null;
            BufferedReader bufferedreader = null;

            try {
                bufferedreader = Files.newReader(this.saveFile, StandardCharsets.UTF_8);
                collection = (Collection) JsonUtils.fromJson(this.gson, (Reader) bufferedreader, (Type) UserList.USER_LIST_ENTRY_TYPE);
            // Spigot Start
            } catch ( com.google.gson.JsonParseException ex )
            {
                org.bukkit.Bukkit.getLogger().log( java.util.logging.Level.WARNING, "Unable to read file " + this.saveFile + ", backing it up to {0}.backup and creating new copy.", ex );
                File backup = new File( this.saveFile + ".backup" );
                this.saveFile.renameTo( backup );
                this.saveFile.delete();
            // Spigot End
            } finally {
                IOUtils.closeQuietly(bufferedreader);
            }

            if (collection != null) {
                this.values.clear();
                Iterator iterator = collection.iterator();

                while (iterator.hasNext()) {
                    UserListEntry jsonlistentry = (UserListEntry) iterator.next();

                    if (jsonlistentry.getValue() != null) {
                        this.values.put(this.getObjectKey((K) jsonlistentry.getValue()), (V) jsonlistentry); // CraftBukkit - fix decompile error
                    }
                }
            }

        }
    }

    class Serializer implements JsonDeserializer<UserListEntry<K>>, JsonSerializer<UserListEntry<K>> {

        private Serializer() {}

        public JsonElement serialize(UserListEntry<K> jsonlistentry, Type type, JsonSerializationContext jsonserializationcontext) {
            JsonObject jsonobject = new JsonObject();

            jsonlistentry.onSerialization(jsonobject);
            return jsonobject;
        }

        public UserListEntry<K> deserialize(JsonElement jsonelement, Type type, JsonDeserializationContext jsondeserializationcontext) throws JsonParseException {
            if (jsonelement.isJsonObject()) {
                JsonObject jsonobject = jsonelement.getAsJsonObject();

                return UserList.this.createEntry(jsonobject);
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
