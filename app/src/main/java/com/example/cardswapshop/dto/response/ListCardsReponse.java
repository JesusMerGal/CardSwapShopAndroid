package com.example.cardswapshop.dto.response;

import org.json.JSONObject;

import java.util.List;

public class ListCardsReponse {
    List<CardResponse> cards;

    public ListCardsReponse(List<CardResponse> cards) {
        this.cards = cards;
    }

    public ListCardsReponse() {
    }

    public List<CardResponse> getCards() {
        return cards;
    }

    public void setCards(List<CardResponse> cards) {
        this.cards = cards;
    }
}
