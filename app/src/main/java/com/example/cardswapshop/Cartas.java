package com.example.cardswapshop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.cardswapshop.dto.response.CardResponse;
import com.example.cardswapshop.dto.response.CardsListView;
import com.example.cardswapshop.dto.response.ListCardsReponse;
import com.example.cardswapshop.dto.response.TokenResponse;
import com.example.cardswapshop.helpers.Globals;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Cartas extends AppCompatActivity {

    Button CartAtrasBtn;
    private RecyclerView cartRecView;
    Globals sharedData = Globals.getInstance();

    private ArrayList<CardsListView> cardList;

    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cartas);
        cardList = new ArrayList<>();
        queue = Volley.newRequestQueue(Cartas.this);

        String url = "http://10.0.2.2:8080/api/v1/card";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Gson gson = new Gson();
                ListCardsReponse listCardsReponse = gson.fromJson(response.toString(), ListCardsReponse.class);
                cardList = generateListObject(listCardsReponse);
                cartRecView=findViewById(R.id.CartRecView);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager( Cartas.this);
                cartRecView.setLayoutManager(linearLayoutManager);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Cartas.this, "Error al cargar cartas", Toast.LENGTH_LONG).show();
            }
        }
        )
        {
            @Override
            public Map<String , String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("Authorization", "Bearer " + sharedData.getValue());
                return params;
            }
        };
        queue.add(request);


        CartAtrasBtn = (Button) findViewById(R.id.CartAtrasBtn);
        CartAtrasBtn.setOnClickListener(v -> {
            Intent intent = new Intent(Cartas.this, Inicio.class);
            startActivity(intent);
        });
    }

    public ArrayList<CardsListView> generateListObject(ListCardsReponse response){
        ArrayList <CardsListView> cards = new ArrayList<>();
        for(CardResponse c : response.getCards()){
            CardsListView card = new CardsListView();
            card.setName(c.getName());
            card.setCardNumber(c.getCard_number());
            card.setCategory(c.getCollection().getName());
            card.setImage(c.getImage());
            cards.add(card);
        }
        return cards;
    }
}