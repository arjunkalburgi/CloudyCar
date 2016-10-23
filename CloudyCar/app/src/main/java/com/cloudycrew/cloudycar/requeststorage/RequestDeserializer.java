package com.cloudycrew.cloudycar.requeststorage;

import com.cloudycrew.cloudycar.models.requests.Request;
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
 * Created by George on 2016-10-23.
 */

public class RequestDeserializer implements JsonDeserializer<Request> {
    public final static String requestTypeFieldName = "requestType";
    private Map<String, Class<? extends Request>> requestTypes = new HashMap<>();

    private RequestDeserializer(Map<String, Class<? extends Request>> requestTypes) {
        this.requestTypes.putAll(requestTypes);
    }

    @Override
    public Request deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext)
            throws JsonParseException {

        JsonObject object = jsonElement.getAsJsonObject();
        JsonElement requestTypeElement = object.get(requestTypeFieldName);
        Class<? extends Request> requestClass = requestTypes.get(requestTypeElement.getAsString());

        return new Gson().fromJson(object, requestClass);
    }

    public static class Builder {
        private Map<String, Class<? extends Request>> requestTypes = new HashMap<>();

        public Builder registerRequestType(String type, Class<? extends Request> requestClass) {
            requestTypes.put(type, requestClass);
            return this;
        }

        public RequestDeserializer build() {
            return new RequestDeserializer(requestTypes);
        }
    }
}
