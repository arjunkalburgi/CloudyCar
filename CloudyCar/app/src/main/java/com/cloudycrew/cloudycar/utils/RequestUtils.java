package com.cloudycrew.cloudycar.utils;

import com.cloudycrew.cloudycar.models.requests.CancelledRequest;
import com.cloudycrew.cloudycar.models.requests.CompletedRequest;
import com.cloudycrew.cloudycar.models.requests.ConfirmedRequest;
import com.cloudycrew.cloudycar.models.requests.PendingRequest;
import com.cloudycrew.cloudycar.models.requests.Request;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;

/**
 * Created by George on 2016-10-25.
 */

public class RequestUtils {
    private static JsonDeserializer<Request> getRequestDeserializer() {
        return new SubclassDeserializer.Builder<Request>()
                .setTypeFieldName("requestType")
                .registerType(PendingRequest.TYPE_NAME, PendingRequest.class)
                .registerType(ConfirmedRequest.TYPE_NAME, ConfirmedRequest.class)
                .registerType(CompletedRequest.TYPE_NAME, CompletedRequest.class)
                .registerType(CancelledRequest.TYPE_NAME, CancelledRequest.class)
                .build();
    }

    public static Gson getGson() {
        return new GsonBuilder().registerTypeAdapter(Request.class, getRequestDeserializer()).create();
    }
}
