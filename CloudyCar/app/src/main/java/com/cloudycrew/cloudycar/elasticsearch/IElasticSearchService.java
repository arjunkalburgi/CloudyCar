package com.cloudycrew.cloudycar.elasticsearch;

import com.cloudycrew.cloudycar.Identifiable;

import java.util.List;

/**
 * Created by George on 2016-10-23.
 */

public interface IElasticSearchService<T extends Identifiable> {
    List<T> getAll();
    List<T> search(String query);

    void create(T item);
    void update(T item);
    void delete(String itemId);
}
