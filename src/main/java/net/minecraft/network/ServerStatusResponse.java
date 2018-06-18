package net.minecraft.network;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.mojang.authlib.GameProfile;
import java.lang.reflect.Type;
import java.util.UUID;

import net.minecraft.util.JsonUtils;
import net.minecraft.util.text.ITextComponent;

public class ServerStatusResponse {

    private ITextComponent description;
    private ServerStatusResponse.Players players;
    private ServerStatusResponse.Version version;
    private String favicon;

    public ServerStatusResponse() {}

    public ITextComponent getServerDescription() {
        return this.description;
    }

    public void setServerDescription(ITextComponent ichatbasecomponent) {
        this.description = ichatbasecomponent;
    }

    public Players getPlayers() { return getPlayers(); } // Paper - OBFHELPER
    public ServerStatusResponse.Players getPlayers() {
        return this.players;
    }

    public void setPlayers(ServerStatusResponse.Players serverping_serverpingplayersample) {
        this.players = serverping_serverpingplayersample;
    }

    public ServerStatusResponse.Version getVersion() {
        return this.version;
    }

    public void setVersion(ServerStatusResponse.Version serverping_serverdata) {
        this.version = serverping_serverdata;
    }

    public void setFavicon(String s) {
        this.favicon = s;
    }

    public String getFavicon() {
        return this.favicon;
    }

    public static class Serializer implements JsonDeserializer<ServerStatusResponse>, JsonSerializer<ServerStatusResponse> {

        public Serializer() {}

        // Paper - decompile fix
        public ServerStatusResponse deserialize(JsonElement jsonelement, Type type, JsonDeserializationContext jsondeserializationcontext) throws JsonParseException {
            JsonObject jsonobject = JsonUtils.getJsonObject(jsonelement, "status");
            ServerStatusResponse serverping = new ServerStatusResponse();

            if (jsonobject.has("description")) {
                serverping.setServerDescription((ITextComponent) jsondeserializationcontext.deserialize(jsonobject.get("description"), ITextComponent.class));
            }

            if (jsonobject.has("players")) {
                serverping.setPlayers((ServerStatusResponse.Players) jsondeserializationcontext.deserialize(jsonobject.get("players"), ServerStatusResponse.Players.class));
            }

            if (jsonobject.has("version")) {
                serverping.setVersion((ServerStatusResponse.Version) jsondeserializationcontext.deserialize(jsonobject.get("version"), ServerStatusResponse.Version.class));
            }

            if (jsonobject.has("favicon")) {
                serverping.setFavicon(JsonUtils.getString(jsonobject, "favicon"));
            }

            return serverping;
        }

        // Paper - decompile fix
        public JsonElement serialize(ServerStatusResponse serverping, Type type, JsonSerializationContext jsonserializationcontext) {
            JsonObject jsonobject = new JsonObject();

            if (serverping.getServerDescription() != null) {
                jsonobject.add("description", jsonserializationcontext.serialize(serverping.getServerDescription()));
            }

            if (serverping.getPlayers() != null) {
                jsonobject.add("players", jsonserializationcontext.serialize(serverping.getPlayers()));
            }

            if (serverping.getVersion() != null) {
                jsonobject.add("version", jsonserializationcontext.serialize(serverping.getVersion()));
            }

            if (serverping.getFavicon() != null) {
                jsonobject.addProperty("favicon", serverping.getFavicon());
            }

            return jsonobject;
        }
    }

    public static class Version {

        private final String name;
        private final int protocol;

        public Version(String s, int i) {
            this.name = s;
            this.protocol = i;
        }

        public String getName() {
            return this.name;
        }

        public int getProtocol() {
            return this.protocol;
        }

        public static class Serializer implements JsonDeserializer<ServerStatusResponse.Version>, JsonSerializer<ServerStatusResponse.Version> {

            public Serializer() {}

            // Paper - decompile fix
            public ServerStatusResponse.Version deserialize(JsonElement jsonelement, Type type, JsonDeserializationContext jsondeserializationcontext) throws JsonParseException {
                JsonObject jsonobject = JsonUtils.getJsonObject(jsonelement, "version");

                return new ServerStatusResponse.Version(JsonUtils.getString(jsonobject, "name"), JsonUtils.getInt(jsonobject, "protocol"));
            }

            // Paper - decompile fix
            public JsonElement serialize(ServerStatusResponse.Version serverping_serverdata, Type type, JsonSerializationContext jsonserializationcontext) {
                JsonObject jsonobject = new JsonObject();

                jsonobject.addProperty("name", serverping_serverdata.getName());
                jsonobject.addProperty("protocol", Integer.valueOf(serverping_serverdata.getProtocol()));
                return jsonobject;
            }
        }
    }

    public static class Players {

        private final int maxPlayers;
        private final int onlinePlayerCount;
        private GameProfile[] players;

        public Players(int i, int j) {
            this.maxPlayers = i;
            this.onlinePlayerCount = j;
        }

        public int getMaxPlayers() {
            return this.maxPlayers;
        }

        public int getOnlinePlayerCount() {
            return this.onlinePlayerCount;
        }

        public GameProfile[] getSample() { return getPlayers(); } // Paper - OBFHELPER
        public GameProfile[] getPlayers() {
            return this.players;
        }

        public void setSample(GameProfile[] sample) { setPlayers(sample); } // Paper - OBFHELPER
        public void setPlayers(GameProfile[] agameprofile) {
            this.players = agameprofile;
        }

        public static class Serializer implements JsonDeserializer<ServerStatusResponse.Players>, JsonSerializer<ServerStatusResponse.Players> {

            public Serializer() {}

            // Paper - decompile fix
            public ServerStatusResponse.Players deserialize(JsonElement jsonelement, Type type, JsonDeserializationContext jsondeserializationcontext) throws JsonParseException {
                JsonObject jsonobject = JsonUtils.getJsonObject(jsonelement, "players");
                ServerStatusResponse.Players serverping_serverpingplayersample = new ServerStatusResponse.Players(JsonUtils.getInt(jsonobject, "max"), JsonUtils.getInt(jsonobject, "online"));

                if (JsonUtils.isJsonArray(jsonobject, "sample")) {
                    JsonArray jsonarray = JsonUtils.getJsonArray(jsonobject, "sample");

                    if (jsonarray.size() > 0) {
                        GameProfile[] agameprofile = new GameProfile[jsonarray.size()];

                        for (int i = 0; i < agameprofile.length; ++i) {
                            JsonObject jsonobject1 = JsonUtils.getJsonObject(jsonarray.get(i), "player[" + i + "]");
                            String s = JsonUtils.getString(jsonobject1, "id");

                            agameprofile[i] = new GameProfile(UUID.fromString(s), JsonUtils.getString(jsonobject1, "name"));
                        }

                        serverping_serverpingplayersample.setPlayers(agameprofile);
                    }
                }

                return serverping_serverpingplayersample;
            }

            // Paper - decompile fix
            public JsonElement serialize(ServerStatusResponse.Players serverping_serverpingplayersample, Type type, JsonSerializationContext jsonserializationcontext) {
                JsonObject jsonobject = new JsonObject();

                jsonobject.addProperty("max", Integer.valueOf(serverping_serverpingplayersample.getMaxPlayers()));
                jsonobject.addProperty("online", Integer.valueOf(serverping_serverpingplayersample.getOnlinePlayerCount()));
                if (serverping_serverpingplayersample.getPlayers() != null && serverping_serverpingplayersample.getPlayers().length > 0) {
                    JsonArray jsonarray = new JsonArray();

                    for (int i = 0; i < serverping_serverpingplayersample.getPlayers().length; ++i) {
                        JsonObject jsonobject1 = new JsonObject();
                        UUID uuid = serverping_serverpingplayersample.getPlayers()[i].getId();

                        jsonobject1.addProperty("id", uuid == null ? "" : uuid.toString());
                        jsonobject1.addProperty("name", serverping_serverpingplayersample.getPlayers()[i].getName());
                        jsonarray.add(jsonobject1);
                    }

                    jsonobject.add("sample", jsonarray);
                }

                return jsonobject;
            }
        }
    }
}
