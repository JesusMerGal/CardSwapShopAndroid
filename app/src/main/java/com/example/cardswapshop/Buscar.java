package com.example.cardswapshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
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
import com.example.cardswapshop.dto.response.CollectionResponse;
import com.example.cardswapshop.dto.response.ListCardsReponse;
import com.example.cardswapshop.dto.response.ListCollectionsResponse;
import com.example.cardswapshop.helpers.Globals;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Buscar extends AppCompatActivity {

    Button BuscAtrasBtn;

    Context context;
    private Spinner buscCollSpin;
    private RecyclerView cartRecView;
    private SearchView buscBarraTxt;
    Globals sharedData = Globals.getInstance();

    List<String> collections;
    private HashMap<String,String> categoriesCode;
    String categoryId;
    String name;

    private ArrayList<CardsListView> cardList;
    private Buscar.CardAdapter adapter;
    private RequestQueue queue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar);
        cardList = new ArrayList<>();
        categoriesCode = new HashMap<>();
        queue = Volley.newRequestQueue(Buscar.this);
        buscCollSpin = (Spinner) findViewById(R.id.BuscCollSpin);
        buscBarraTxt = findViewById(R.id.BuscBarraTxt);
        context = this;

        getCategories();

        getAllCards(null);

        buscBarraTxt.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(categoryId != null){
                    getAllCardsByCategory(categoryId,query);
                }else{
                    getAllCards(query);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        buscCollSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
                categoryId = categoriesCode.get(collections.get(position));
                getAllCardsByCategory(categoryId,null);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                getAllCards(null);
            }
        });

        BuscAtrasBtn = (Button) findViewById(R.id.BuscAtrasBtn);
        BuscAtrasBtn.setOnClickListener(v -> {
            Intent intent = new Intent(Buscar.this, Inicio.class);
            startActivity(intent);
        });

    }

    private void getCategories(){
        String url = "http://10.0.2.2:8080/api/v1/collection";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Gson gson = new Gson();

                ListCollectionsResponse collectionsResponse = gson.fromJson(response.toString(), ListCollectionsResponse.class);
                collections = getListOfCollections(collectionsResponse);
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(context,
                        android.R.layout.simple_spinner_item, collections);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                buscCollSpin.setAdapter(dataAdapter);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Buscar.this, "Error al cargar cartas", Toast.LENGTH_LONG).show();
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
    }
    private void getAllCards(String name){
        String url = "http://10.0.2.2:8080/api/v1/card/all";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Gson gson = new Gson();
                ListCardsReponse listCardsReponse = gson.fromJson(response.toString(), ListCardsReponse.class);
                cardList = generateListObject(listCardsReponse);
                if(!cardList.isEmpty()) {
                    if(name!=null) {
                        List<CardsListView> cards = new ArrayList<>();
                        for (CardsListView c : cardList) {
                            if (c.getName().contains(name)) {
                                cards.add(c);
                            }
                        }
                        cardList = new ArrayList<>(cards);
                    }
                    cartRecView = findViewById(R.id.BuscRecView);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Buscar.this);
                    cartRecView.setLayoutManager(linearLayoutManager);
                    adapter = new Buscar.CardAdapter();
                    cartRecView.setAdapter(adapter);
                    adapter.notifyItemRangeInserted(0, cardList.size() - 1);
                }else{
                    Toast.makeText(Buscar.this, "No tienes cartas", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Buscar.this, "Error al cargar cartas", Toast.LENGTH_LONG).show();
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
    }

    private void getAllCardsByCategory(String collectionId, String name){
        String url = "http://10.0.2.2:8080/api/v1/card/collection/" + collectionId;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Gson gson = new Gson();
                ListCardsReponse listCardsReponse = gson.fromJson(response.toString(), ListCardsReponse.class);
                cardList = generateListObject(listCardsReponse);
                if(!cardList.isEmpty()) {
                    if(name!=null) {
                        List<CardsListView> cards = new ArrayList<>();
                        for (CardsListView c : cardList) {
                            if (c.getName().contains(name)) {
                                cards.add(c);
                            }
                        }
                        cardList = new ArrayList<>(cards);
                    }
                    cartRecView = findViewById(R.id.BuscRecView);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Buscar.this);
                    cartRecView.setLayoutManager(linearLayoutManager);
                    adapter = new Buscar.CardAdapter();
                    cartRecView.setAdapter(adapter);
                    adapter.notifyItemRangeInserted(0, cardList.size() - 1);
                }else{
                    Toast.makeText(Buscar.this, "No tienes cartas", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Buscar.this, "Error al cargar cartas", Toast.LENGTH_LONG).show();
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
    }

    private void searchCards(){
        String url = "http://10.0.2.2:8080/api/v1/card/all";
        buscCollSpin.findViewById(R.id.BuscCollSpin);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Gson gson = new Gson();
                ListCardsReponse listCardsReponse = gson.fromJson(response.toString(), ListCardsReponse.class);
                cardList = generateListObject(listCardsReponse);
                if(!cardList.isEmpty()) {
                    cartRecView = findViewById(R.id.BuscRecView);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Buscar.this);
                    cartRecView.setLayoutManager(linearLayoutManager);
                    adapter = new Buscar.CardAdapter();
                    cartRecView.setAdapter(adapter);
                    adapter.notifyItemRangeInserted(0, cardList.size() - 1);
                }else{
                    Toast.makeText(Buscar.this, "No tienes cartas", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Buscar.this, "Error al cargar cartas", Toast.LENGTH_LONG).show();
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

    public List<String> getListOfCollections(ListCollectionsResponse collectionsResponse){
        List<String> collections = new ArrayList<>();
        for (CollectionResponse c : collectionsResponse.getCollections()){
            collections.add(c.getName());
            categoriesCode.put(c.getName(),c.getId().toString());
        }
        return collections;
    }

    private class CardAdapter extends RecyclerView.Adapter<Buscar.CardAdapter.CardAdapterHolder>{

        @NonNull
        @Override
        public Buscar.CardAdapter.CardAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new Buscar.CardAdapter.CardAdapterHolder(getLayoutInflater().inflate(R.layout.layout_carta,parent,false));
        }

        @Override
        public void onBindViewHolder(@NonNull Buscar.CardAdapter.CardAdapterHolder holder, int position) {
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