package com.cloudycrew.cloudycar.elasticsearch;

import com.cloudycrew.cloudycar.Identifiable;

import java.util.List;

/**
 * Created by George on 2016-10-23.
 *
 * @param <T> the type being queried from elastic search
 */
public interface IElasticSearchService<T extends Identifiable> {
    /**
     * Gets all items stored in Elastic Search
     *
     * @return all items stored for a specific Elastic Search type
     */
    List<T> getAll();

    /**
     * Gets all items stored in Elastic Search matching the given query
     *
     * @param query the query
     * @return A list of all items stored in elastic search
     */
    List<T> search(String query);

    /**
     * Create a new item in Elastic Search for the given type.
     *
     * @param item the item to create
     */
    void create(T item);

    /**
     * Update an item in Elastic Search for the given type.
     *
     * @param item the item to be updated
     */
    void update(T item);

    /**
     * Delete an tem in Elastic Search for the given type.
     *
     * @param itemId the id of the item to delete
     */
    void delete(String itemId);
}
