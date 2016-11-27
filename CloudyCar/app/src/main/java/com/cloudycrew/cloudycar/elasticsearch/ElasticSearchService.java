package com.cloudycrew.cloudycar.elasticsearch;

import com.cloudycrew.cloudycar.Identifiable;

import java.util.ArrayList;
import java.util.List;

import io.searchbox.client.JestClient;
import io.searchbox.core.Delete;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;

/**
 * Created by George on 2016-10-23.
 */

public class ElasticSearchService<T extends Identifiable> implements IElasticSearchService<T> {
    private String index;
    private String type;
    private Class<T> typeClass;
    private JestClient jestClient;

    private ElasticSearchService(JestClient jestClient, String index, String type, Class<T> classType) {
        this.jestClient = jestClient;
        this.index = index;
        this.type = type;
        this.typeClass = classType;
    }

    @Override
    public List<T> getAll() {
        return search("{ \"size\" : 100 } ");
    }

    @Override
    public List<T> search(String query) {
        Search search = new Search.Builder(query)
                .addIndex(index)
                .addType(type)
                .build();

        try {
            SearchResult result = jestClient.execute(search);

            if (result.isSucceeded()) {
                return extractItemsFromHits(result.getHits(typeClass));
            }
        }
        catch (Exception e) {
            throw new ElasticSearchConnectivityException(e);
        }

        return new ArrayList<>();
    }

    @Override
    public void create(T item) {
        upsertItem(item);
    }

    @Override
    public void update(T item) {
        upsertItem(item);
    }

    @Override
    public void delete(String itemId) {
        Delete delete = new Delete.Builder(itemId)
                .index(index)
                .type(type)
                .build();

        try {
            jestClient.execute(delete);
        } catch (Exception e) {
            throw new ElasticSearchConnectivityException(e);
        }
    }

    private List<T> extractItemsFromHits(List<SearchResult.Hit<T, Void>> searchHits) {
        List<T> out = new ArrayList<>();
        for(SearchResult.Hit<T, Void> hit : searchHits) {
            out.add(hit.source);
        }

        return out;
    }

    private void upsertItem(T item) {
        Index documentIndex = new Index.Builder(item)
                .index(index)
                .type(type)
                .id(item.getId())
                .build();

        try {
            jestClient.execute(documentIndex);
        } catch (Exception e) {
            throw new ElasticSearchConnectivityException(e);
        }
    }

    public static class Builder<T extends Identifiable> {
        private String index;
        private String type;
        private Class<T> typeClass;
        private JestClient jestClient;

        public ElasticSearchService<T> build() {
            return new ElasticSearchService<>(jestClient, index, type, typeClass);
        }

        public Builder<T> setIndex(String index) {
            this.index = index;
            return this;
        }

        public Builder<T> setType(String type) {
            this.type = type;
            return this;
        }

        public Builder<T> setTypeClass(Class<T> typeClass) {
            this.typeClass = typeClass;
            return this;
        }

        public Builder<T> setJestClient(JestClient jestClient) {
            this.jestClient = jestClient;
            return this;
        }
    }
}
