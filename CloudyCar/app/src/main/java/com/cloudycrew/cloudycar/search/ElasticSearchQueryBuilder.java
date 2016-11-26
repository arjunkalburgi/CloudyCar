package com.cloudycrew.cloudycar.search;

/**
 * Created by George on 2016-11-26.
 */

public class ElasticSearchQueryBuilder {
    public String buildQuery(SearchContext searchContext) {
        return "";
    }

    public String getKeywordQuery(String keyword) {
        return "{\n" +
                "\"match\": { \n" +
                "\"description\": \"" + keyword + "\" \n" +
                "}\n" +
                "}";
    }

    public String getRadiusQuery(double lon, double lat, double radius) {
        return "";
    }

    public String getPricePerKmQuery(double pricePerKm) {
        return "";
    }
}
