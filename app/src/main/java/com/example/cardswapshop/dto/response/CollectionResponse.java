package com.example.cardswapshop.dto.response;

public class CollectionResponse {

    private Long id;
    private String name;

    public CollectionResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public CollectionResponse() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
