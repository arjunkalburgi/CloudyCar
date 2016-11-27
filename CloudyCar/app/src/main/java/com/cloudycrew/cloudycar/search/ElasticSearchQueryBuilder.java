package com.cloudycrew.cloudycar.search;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by George on 2016-11-26.
 */

public class ElasticSearchQueryBuilder {
    public String buildQuery(SearchContext searchContext) {
        Map<Object, Object> elasticSearchQuery = new HashMap<>();
        List<Map<Object, Object>> queryObjects = new ArrayList<>();

        if (searchContext.hasKeyword()) {
            queryObjects.add(getKeywordQuery(searchContext));
        }

        if (searchContext.hasPricePerKm()) {
            queryObjects.add(getPricePerKmQuery(searchContext));
        }

        if (searchContext.hasLocation()) {
            elasticSearchQuery.put("filter", getRadiusFilter(searchContext));
        }

        elasticSearchQuery.put("query", getQueryObject(queryObjects));

        return serializeQuery(elasticSearchQuery);
    }

    private Map<Object, Object> getKeywordQuery(SearchContext searchContext) {
        Map<Object, Object> matchObject = new HashMap<>();
        Map<Object, Object> queryTypeObject = new HashMap<>();

        matchObject.put("description", searchContext.getKeyword());
        queryTypeObject.put("match", matchObject);

        return queryTypeObject;
    }

    private Map<Object, Object> getPricePerKmQuery(SearchContext searchContext) {
        Map<Object, Object> pricePerKmObject = new HashMap<>();
        Map<Object, Object> rangeObject = new HashMap<>();
        Map<Object, Object> queryTypeObject = new HashMap<>();

        pricePerKmObject.put("gte", searchContext.getMinPricePerKm());
        pricePerKmObject.put("lte", searchContext.getMaxPricePerKm());
        rangeObject.put("pricePerKm", pricePerKmObject);
        queryTypeObject.put("range", rangeObject);

        return queryTypeObject;
    }

    private Map<Object, Object> getRadiusFilter(SearchContext searchContext) {
        Map<Object, Object> geoPointObject = new HashMap<>();
        Map<Object, Object> geoDistanceObject = new HashMap<>();
        Map<Object, Object> filterTypeObject = new HashMap<>();

        geoPointObject.put("lat", searchContext.getLat());
        geoPointObject.put("lon", searchContext.getLon());
        geoDistanceObject.put("route.start.point", geoPointObject);
        geoDistanceObject.put("distance", searchContext.getRadius() + "km");
        filterTypeObject.put("geo_distance", geoDistanceObject);

        return filterTypeObject;
    }

    private Map<Object, Object> getQueryObject(List<? extends Map<Object, Object>> queryObjects) {
        Map<Object, Object> boolObject = new HashMap<>();
        Map<Object, Object> queryObject = new HashMap<>();


        boolObject.put("must", queryObjects);
        queryObject.put("bool", boolObject);

        return queryObject;
    }

    private String serializeQuery(Map<Object, Object> queryObject) {
        return new Gson().toJson(queryObject);
    }
}
