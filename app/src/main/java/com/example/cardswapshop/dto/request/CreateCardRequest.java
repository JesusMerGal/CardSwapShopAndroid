package com.example.cardswapshop.dto.request;

import org.json.JSONObject;

public class CreateCardRequest extends JSONObject {

    private String name;
    private Long collection_id;
    private String card_number;

    private String file_name;

    private String file_type;

    private String image;

    public CreateCardRequest(String name, Long collection_id, String card_number, String file_name, String file_type, String image) {
        this.name = name;
        this.collection_id = collection_id;
        this.card_number = card_number;
        this.file_name = file_name;
        this.file_type = file_type;
        this.image = image;
    }

    public CreateCardRequest() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getCollection_id() {
        return collection_id;
    }

    public void setCollection_id(Long collection_id) {
        this.collection_id = collection_id;
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
