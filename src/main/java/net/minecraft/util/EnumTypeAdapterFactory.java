package net.minecraft.util;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import javax.annotation.Nullable;

public class EnumTypeAdapterFactory implements TypeAdapterFactory {

    public EnumTypeAdapterFactory() {}

    @Nullable
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typetoken) {
        Class oclass = typetoken.getRawType();

        if (!oclass.isEnum()) {
            return null;
        } else {
            final HashMap hashmap = Maps.newHashMap();
            Object[] aobject = oclass.getEnumConstants();
            int i = aobject.length;

            for (int j = 0; j < i; ++j) {
                Object object = aobject[j];

                hashmap.put(this.func_151232_a(object), object);
            }

            return new TypeAdapter() {
                public void write(JsonWriter jsonwriter, T t0) throws IOException {
                    if (t0 == null) {
                        jsonwriter.nullValue();
                    } else {
                        jsonwriter.value(EnumTypeAdapterFactory.this.func_151232_a(t0));
                    }

                }

                @Nullable
                public T read(JsonReader jsonreader) throws IOException {
                    if (jsonreader.peek() == JsonToken.NULL) {
                        jsonreader.nextNull();
                        return null;
                    } else {
                        return hashmap.get(jsonreader.nextString());
                    }
                }
            };
        }
    }

    private String func_151232_a(Object object) {
        return object instanceof Enum ? ((Enum) object).name().toLowerCase(Locale.ROOT) : object.toString().toLowerCase(Locale.ROOT);
    }
}
