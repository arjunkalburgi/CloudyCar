package com.cloudycrew.cloudycar.elasticsearch;

/**
 * Created by Ryan on 2016-11-24.
 */

public class ElasticSearchConnectivityException extends RuntimeException
{
    public ElasticSearchConnectivityException(Exception e) {
        super(e);
    }
}
