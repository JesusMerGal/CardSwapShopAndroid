package com.example.cardswapshop.dto.response;

import org.json.JSONObject;

import java.util.List;

public class ListCollectionsResponse  {
    List<CollectionResponse> collections;

    public ListCollectionsResponse(List<CollectionResponse> collections) {
        this.collections = collections;
    }

    public ListCollectionsResponse() {
    }

    public List<CollectionResponse> getCollections() {
        return collections;
    }

    public void setCollections(List<CollectionResponse> collections) {
        this.collections = collections;
    }
}
