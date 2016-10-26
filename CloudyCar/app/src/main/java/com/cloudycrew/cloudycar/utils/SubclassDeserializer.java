package com.cloudycrew.cloudycar.utils;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by George on 2016-10-25.
 */

public class SubclassDeserializer<T> implements JsonDeserializer<T> {
    private String typeFieldName;
    private Map<String, Class<? extends T>> typesMap = new HashMap<>();

    private SubclassDeserializer(String typeFieldName, Map<String, Class<? extends T>> typesMap) {
        this.typeFieldName = typeFieldName;
        this.typesMap.putAll(typesMap);
    }

    @Override
    public T deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext)
            throws JsonParseException {

        JsonObject jsonObject = jsonElement.getAsJsonObject();
        JsonElement typeElement = jsonObject.get(typeFieldName);
        Class<? extends T> typeClass = typesMap.get(typeElement.getAsString());

        return new Gson().fromJson(jsonObject, typeClass);
    }

    public static class Builder<T> {
        private String typeFieldName;
        private Map<String, Class<? extends T>> typesMap = new HashMap<>();

        public Builder<T> setTypeFieldName(String typeFieldName) {
            this.typeFieldName = typeFieldName;
            return this;
        }

        public Builder<T> registerType(String type, Class<? extends T> typeClass) {
            typesMap.put(type, typeClass);
            return this;
        }

        public SubclassDeserializer<T> build() {
            if (typeFieldName == null) {
                throw new NullPointerException("typeFieldName is null");
            }
            return new SubclassDeserializer<>(typeFieldName, typesMap);
        }
    }
}
