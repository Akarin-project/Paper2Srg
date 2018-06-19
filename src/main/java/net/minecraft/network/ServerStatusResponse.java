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

    private ITextComponent field_151326_a;
    private ServerStatusResponse.Players field_151324_b;
    private ServerStatusResponse.Version field_151325_c;
    private String field_151323_d;

    public ServerStatusResponse() {}

    public ITextComponent func_151317_a() {
        return this.field_151326_a;
    }

    public void func_151315_a(ITextComponent ichatbasecomponent) {
        this.field_151326_a = ichatbasecomponent;
    }

    public Players getPlayers() { return func_151318_b(); } // Paper - OBFHELPER
    public ServerStatusResponse.Players func_151318_b() {
        return this.field_151324_b;
    }

    public void func_151319_a(ServerStatusResponse.Players serverping_serverpingplayersample) {
        this.field_151324_b = serverping_serverpingplayersample;
    }

    public ServerStatusResponse.Version func_151322_c() {
        return this.field_151325_c;
    }

    public void func_151321_a(ServerStatusResponse.Version serverping_serverdata) {
        this.field_151325_c = serverping_serverdata;
    }

    public void func_151320_a(String s) {
        this.field_151323_d = s;
    }

    public String func_151316_d() {
        return this.field_151323_d;
    }

    public static class Serializer implements JsonDeserializer<ServerStatusResponse>, JsonSerializer<ServerStatusResponse> {

        public Serializer() {}

        // Paper - decompile fix
        public ServerStatusResponse deserialize(JsonElement jsonelement, Type type, JsonDeserializationContext jsondeserializationcontext) throws JsonParseException {
            JsonObject jsonobject = JsonUtils.func_151210_l(jsonelement, "status");
            ServerStatusResponse serverping = new ServerStatusResponse();

            if (jsonobject.has("description")) {
                serverping.func_151315_a((ITextComponent) jsondeserializationcontext.deserialize(jsonobject.get("description"), ITextComponent.class));
            }

            if (jsonobject.has("players")) {
                serverping.func_151319_a((ServerStatusResponse.Players) jsondeserializationcontext.deserialize(jsonobject.get("players"), ServerStatusResponse.Players.class));
            }

            if (jsonobject.has("version")) {
                serverping.func_151321_a((ServerStatusResponse.Version) jsondeserializationcontext.deserialize(jsonobject.get("version"), ServerStatusResponse.Version.class));
            }

            if (jsonobject.has("favicon")) {
                serverping.func_151320_a(JsonUtils.func_151200_h(jsonobject, "favicon"));
            }

            return serverping;
        }

        // Paper - decompile fix
        public JsonElement serialize(ServerStatusResponse serverping, Type type, JsonSerializationContext jsonserializationcontext) {
            JsonObject jsonobject = new JsonObject();

            if (serverping.func_151317_a() != null) {
                jsonobject.add("description", jsonserializationcontext.serialize(serverping.func_151317_a()));
            }

            if (serverping.func_151318_b() != null) {
                jsonobject.add("players", jsonserializationcontext.serialize(serverping.func_151318_b()));
            }

            if (serverping.func_151322_c() != null) {
                jsonobject.add("version", jsonserializationcontext.serialize(serverping.func_151322_c()));
            }

            if (serverping.func_151316_d() != null) {
                jsonobject.addProperty("favicon", serverping.func_151316_d());
            }

            return jsonobject;
        }
    }

    public static class Version {

        private final String field_151306_a;
        private final int field_151305_b;

        public Version(String s, int i) {
            this.field_151306_a = s;
            this.field_151305_b = i;
        }

        public String func_151303_a() {
            return this.field_151306_a;
        }

        public int func_151304_b() {
            return this.field_151305_b;
        }

        public static class Serializer implements JsonDeserializer<ServerStatusResponse.Version>, JsonSerializer<ServerStatusResponse.Version> {

            public Serializer() {}

            // Paper - decompile fix
            public ServerStatusResponse.Version deserialize(JsonElement jsonelement, Type type, JsonDeserializationContext jsondeserializationcontext) throws JsonParseException {
                JsonObject jsonobject = JsonUtils.func_151210_l(jsonelement, "version");

                return new ServerStatusResponse.Version(JsonUtils.func_151200_h(jsonobject, "name"), JsonUtils.func_151203_m(jsonobject, "protocol"));
            }

            // Paper - decompile fix
            public JsonElement serialize(ServerStatusResponse.Version serverping_serverdata, Type type, JsonSerializationContext jsonserializationcontext) {
                JsonObject jsonobject = new JsonObject();

                jsonobject.addProperty("name", serverping_serverdata.func_151303_a());
                jsonobject.addProperty("protocol", Integer.valueOf(serverping_serverdata.func_151304_b()));
                return jsonobject;
            }
        }
    }

    public static class Players {

        private final int field_151336_a;
        private final int field_151334_b;
        private GameProfile[] field_151335_c;

        public Players(int i, int j) {
            this.field_151336_a = i;
            this.field_151334_b = j;
        }

        public int func_151332_a() {
            return this.field_151336_a;
        }

        public int func_151333_b() {
            return this.field_151334_b;
        }

        public GameProfile[] getSample() { return func_151331_c(); } // Paper - OBFHELPER
        public GameProfile[] func_151331_c() {
            return this.field_151335_c;
        }

        public void setSample(GameProfile[] sample) { func_151330_a(sample); } // Paper - OBFHELPER
        public void func_151330_a(GameProfile[] agameprofile) {
            this.field_151335_c = agameprofile;
        }

        public static class Serializer implements JsonDeserializer<ServerStatusResponse.Players>, JsonSerializer<ServerStatusResponse.Players> {

            public Serializer() {}

            // Paper - decompile fix
            public ServerStatusResponse.Players deserialize(JsonElement jsonelement, Type type, JsonDeserializationContext jsondeserializationcontext) throws JsonParseException {
                JsonObject jsonobject = JsonUtils.func_151210_l(jsonelement, "players");
                ServerStatusResponse.Players serverping_serverpingplayersample = new ServerStatusResponse.Players(JsonUtils.func_151203_m(jsonobject, "max"), JsonUtils.func_151203_m(jsonobject, "online"));

                if (JsonUtils.func_151202_d(jsonobject, "sample")) {
                    JsonArray jsonarray = JsonUtils.func_151214_t(jsonobject, "sample");

                    if (jsonarray.size() > 0) {
                        GameProfile[] agameprofile = new GameProfile[jsonarray.size()];

                        for (int i = 0; i < agameprofile.length; ++i) {
                            JsonObject jsonobject1 = JsonUtils.func_151210_l(jsonarray.get(i), "player[" + i + "]");
                            String s = JsonUtils.func_151200_h(jsonobject1, "id");

                            agameprofile[i] = new GameProfile(UUID.fromString(s), JsonUtils.func_151200_h(jsonobject1, "name"));
                        }

                        serverping_serverpingplayersample.func_151330_a(agameprofile);
                    }
                }

                return serverping_serverpingplayersample;
            }

            // Paper - decompile fix
            public JsonElement serialize(ServerStatusResponse.Players serverping_serverpingplayersample, Type type, JsonSerializationContext jsonserializationcontext) {
                JsonObject jsonobject = new JsonObject();

                jsonobject.addProperty("max", Integer.valueOf(serverping_serverpingplayersample.func_151332_a()));
                jsonobject.addProperty("online", Integer.valueOf(serverping_serverpingplayersample.func_151333_b()));
                if (serverping_serverpingplayersample.func_151331_c() != null && serverping_serverpingplayersample.func_151331_c().length > 0) {
                    JsonArray jsonarray = new JsonArray();

                    for (int i = 0; i < serverping_serverpingplayersample.func_151331_c().length; ++i) {
                        JsonObject jsonobject1 = new JsonObject();
                        UUID uuid = serverping_serverpingplayersample.func_151331_c()[i].getId();

                        jsonobject1.addProperty("id", uuid == null ? "" : uuid.toString());
                        jsonobject1.addProperty("name", serverping_serverpingplayersample.func_151331_c()[i].getName());
                        jsonarray.add(jsonobject1);
                    }

                    jsonobject.add("sample", jsonarray);
                }

                return jsonobject;
            }
        }
    }
}
