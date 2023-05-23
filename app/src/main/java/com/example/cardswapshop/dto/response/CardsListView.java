package com.example.cardswapshop.dto.response;

public class CardsListView {
    private String name;
    private String category;
    private String cardNumber;
    private String image;

    public CardsListView(String name, String category, String cardNumber, String image) {
        this.name = name;
        this.category = category;
        this.cardNumber = cardNumber;
        this.image = image;
    }

    public CardsListView() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
