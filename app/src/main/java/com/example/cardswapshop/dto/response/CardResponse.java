package com.example.cardswapshop.dto.response;

import org.json.JSONObject;

public class CardResponse {

    private Long id;

    private String name;

    private CollectionResponse collection;

    private UserResponse user;

    private String card_number;

    private String file_name;

    private String file_type;

    private String image;

    public CardResponse(Long id, String name, CollectionResponse collection, UserResponse user, String card_number, String file_name, String file_type, String image) {
        this.id = id;
        this.name = name;
        this.collection = collection;
        this.user = user;
        this.card_number = card_number;
        this.file_name = file_name;
        this.file_type = file_type;
        this.image = image;
    }

    public CardResponse() {
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

    public CollectionResponse getCollection() {
        return collection;
    }

    public void setCollection(CollectionResponse collection) {
        this.collection = collection;
    }

    public UserResponse getUser() {
        return user;
    }

    public void setUser(UserResponse user) {
        this.user = user;
    }

    public String getCard_number() {
        return card_number;
    }

    public void setCard_number(String card_number) {
        this.card_number = card_number;
    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public String getFile_type() {
        return file_type;
    }

    public void setFile_type(String file_type) {
        this.file_type = file_type;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
