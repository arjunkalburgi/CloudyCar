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
    private Map<String, Class<? extends T>> requestTypes = new HashMap<>();

    private SubclassDeserializer(String typeFieldName, Map<String, Class<? extends T>> requestTypes) {
        this.typeFieldName = typeFieldName;
        this.requestTypes.putAll(requestTypes);
    }

    @Override
    public T deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext)
            throws JsonParseException {

        JsonObject jsonObject = jsonElement.getAsJsonObject();
        JsonElement typeElement = jsonObject.get(typeFieldName);
        Class<? extends T> typeClass = requestTypes.get(typeElement.getAsString());

        return new Gson().fromJson(jsonObject, typeClass);
    }

    public static class Builder<T> {
        private String typeFieldName;
        private Map<String, Class<? extends T>> requestTypes = new HashMap<>();

        public Builder<T> setTypeFieldName(String typeFieldName) {
            this.typeFieldName = typeFieldName;
            return this;
        }

        public Builder<T> registerRequestType(String type, Class<? extends T> requestClass) {
            requestTypes.put(type, requestClass);
            return this;
        }

        public SubclassDeserializer<T> build() {
            if (typeFieldName == null) {
                throw new NullPointerException("typeFieldName is null");
            }
            return new SubclassDeserializer<>(typeFieldName, requestTypes);
        }
    }
}
