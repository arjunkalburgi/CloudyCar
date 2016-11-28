package com.cloudycrew.cloudycar.search;

import com.cloudycrew.cloudycar.Constants;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by George on 2016-11-26.
 */

public class ElasticSearchQueryBuilder {
    /**
     * Build an elasticsearch query based on the contents of the searchContext
     * @param searchContext the object describing the search parameters to filter by
     * @return the elasticsearch query string
     */
    public String buildQuery(SearchContext searchContext) {
        Map<Object, Object> elasticSearchQuery = new HashMap<>();
        List<Map<Object, Object>> queryObjects = new ArrayList<>();

        if (searchContext.hasKeyword()) {
            queryObjects.add(getKeywordQuery(searchContext));
        }

        if (searchContext.hasPrice()) {
            queryObjects.add(getPriceQuery(searchContext));
        }

        if (searchContext.hasPricePerKm()) {
            queryObjects.add(getPricePerKmQuery(searchContext));
        }

        if (searchContext.hasLocation()) {
            elasticSearchQuery.put("filter", getRadiusFilter(searchContext));
        }

        elasticSearchQuery.put("query", getQueryObject(queryObjects));
        elasticSearchQuery.put("size", Constants.MAX_ELASTIC_SEARCH_RESULTS);

        return serializeQuery(elasticSearchQuery);
    }

    /**
     * Build a elasticsearch query to search for requests based on the contents of their descriptions
     * @param searchContext the object containing the keyword to search for
     * @return a map describing a keyword search
     */
    private Map<Object, Object> getKeywordQuery(SearchContext searchContext) {
        Map<Object, Object> matchObject = new HashMap<>();
        Map<Object, Object> queryTypeObject = new HashMap<>();

        matchObject.put("description", searchContext.getKeyword());
        queryTypeObject.put("match", matchObject);

        return queryTypeObject;
    }
    /**
     * Build a elasticsearch query to search for requests based on the contents of their price per km
     * @param searchContext the object containing the keyword to search for
     * @return a map describing a price per km search
     */
    private Map<Object, Object> getPricePerKmQuery(SearchContext searchContext) {
        Map<Object, Object> pricePerKmObject = new HashMap<>();
        Map<Object, Object> rangeObject = new HashMap<>();
        Map<Object, Object> queryTypeObject = new HashMap<>();

        pricePerKmObject.put("gte", searchContext.getMinPricePerKm());
        rangeObject.put("pricePerKm", pricePerKmObject);
        queryTypeObject.put("range", rangeObject);

        return queryTypeObject;
    }
    /**
     * Build a elasticsearch query to search for requests based on the contents of their total price
     * @param searchContext the object containing the keyword to search for
     * @return a map describing a price search
     */
    private Map<Object, Object> getPriceQuery(SearchContext searchContext) {
        Map<Object, Object> priceObject = new HashMap<>();
        Map<Object, Object> rangeObject = new HashMap<>();
        Map<Object, Object> queryTypeObject = new HashMap<>();

        priceObject.put("gte", searchContext.getMinPrice());
        rangeObject.put("price", priceObject);
        queryTypeObject.put("range", rangeObject);

        return queryTypeObject;
    }
    /**
     * Build a elasticsearch query to search for requests based on whether their starting point is
     * within a certain area
     * @param searchContext the object containing the keyword to search for
     * @return a map describing a geo-radius search
     */
    private Map<Object, Object> getRadiusFilter(SearchContext searchContext) {
        Map<Object, Object> geoPointObject = new HashMap<>();
        Map<Object, Object> geoDistanceObject = new HashMap<>();
        Map<Object, Object> filterTypeObject = new HashMap<>();

        geoPointObject.put("lat", searchContext.getLat());
        geoPointObject.put("lon", searchContext.getLon());
        geoDistanceObject.put("route.start.point", geoPointObject);
        geoDistanceObject.put("distance", searchContext.getRadius() + "m");
        filterTypeObject.put("geo_distance", geoDistanceObject);

        return filterTypeObject;
    }

    /**
     * Build a full query map based on all previously created sub query maps
     * @param queryObjects all query objects constructed to be used
     * @return the overall query object
     */
    private Map<Object, Object> getQueryObject(List<? extends Map<Object, Object>> queryObjects) {
        Map<Object, Object> boolObject = new HashMap<>();
        Map<Object, Object> queryObject = new HashMap<>();


        boolObject.put("must", queryObjects);
        queryObject.put("bool", boolObject);

        return queryObject;
    }

    /**
     * Convert the overall query object to a JSON string for use with elasticsearch
     * @param queryObject the overall query object
     * @return the JSON string used by elastic search
     */
    private String serializeQuery(Map<Object, Object> queryObject) {
        return new Gson().toJson(queryObject);
    }
}
