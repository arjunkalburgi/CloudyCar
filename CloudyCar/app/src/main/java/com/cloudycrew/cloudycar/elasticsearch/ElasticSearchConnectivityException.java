package com.cloudycrew.cloudycar.elasticsearch;

/**
 * Exception to be thrown when the elastic search service fails to connect to the elastic search
 * server. Wraps the exception being thrown.
 *
 * Created by Ryan on 2016-11-24.
 */

public class ElasticSearchConnectivityException extends RuntimeException
{
    public ElasticSearchConnectivityException(Exception e) {
        super(e);
    }
}
