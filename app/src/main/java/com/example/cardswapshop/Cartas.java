package com.example.cardswapshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
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
    private CardAdapter adapter;
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
                if(!cardList.isEmpty()) {
                    cartRecView = findViewById(R.id.CartRecView);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Cartas.this);
                    cartRecView.setLayoutManager(linearLayoutManager);
                    adapter = new CardAdapter();
                    cartRecView.setAdapter(adapter);
                    adapter.notifyItemRangeInserted(0, cardList.size() - 1);
                }else{
                    Toast.makeText(Cartas.this, "No tienes cartas", Toast.LENGTH_LONG).show();
                }
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



        CartAtrasBtn = (Button) findViewById(R.id.CartAtrasBtn);
        CartAtrasBtn.setOnClickListener(v -> {
            Intent intent = new Intent(Cartas.this, Inicio.class);
            startActivity(intent);
        });
        queue.add(request);
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

    private class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardAdapterHolder>{

        @NonNull
        @Override
        public CardAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new CardAdapterHolder(getLayoutInflater().inflate(R.layout.layout_carta,parent,false));
        }

        @Override
        public void onBindViewHolder(@NonNull CardAdapterHolder holder, int position) {
            holder.print(position);
        }

        @Override
        public int getItemCount() {
            return cardList.size();
        }

        class CardAdapterHolder extends RecyclerView.ViewHolder {
            TextView tvCardName, tvCardCategory, tvCardNumber;
            ImageView ivCardImage;
            public CardAdapterHolder(@NonNull View itemView){
                super(itemView);
                tvCardName = itemView.findViewById(R.id.tvCardName);
                tvCardCategory = itemView.findViewById(R.id.tvCardCategory);
                tvCardNumber = itemView.findViewById(R.id.tvCardNumber);
                ivCardImage = itemView.findViewById(R.id.ivCardImage);
            }
            public void print(int position){
                tvCardName.setText("Nombre: " + cardList.get(position).getName());
                tvCardCategory.setText("Collection: " + cardList.get(position).getCategory());
                tvCardNumber.setText("Código Carta: " + cardList.get(position).getCardNumber());
                recoverImage(cardList.get(position).getImage(),ivCardImage);
            }

            public void recoverImage(String image, ImageView imageView){
                byte[] decodedString = Base64.decode(image, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imageView.setImageBitmap(decodedByte);
            }

        }
    }
}