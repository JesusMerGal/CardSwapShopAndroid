package com.example.cardswapshop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.cardswapshop.dto.request.CreateCardRequest;
import com.example.cardswapshop.dto.response.CollectionResponse;
import com.example.cardswapshop.dto.response.ListCollectionsResponse;
import com.example.cardswapshop.helpers.Globals;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

public class Subir extends AppCompatActivity {

    Globals sharedData = Globals.getInstance();

    Button UpCancelBtn, UpGaleryBtn, UpCamBtn, UpSubirBtn;
    Spinner spinner;

    Context context;

    EditText UpName, UpNum;

    List<String> collections;
    private HashMap<String,String> categoriesCode;
    String categoryId;

    private ImageView imageViewUp;
    private static final int CAMERA_REQUEST = 1888;
    private static final int SELECT_PHOTO = 1889;

    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subir);
        context = this;
        categoriesCode = new HashMap<>();
        queue = Volley.newRequestQueue(Subir.this);
        UpName = findViewById(R.id.UpNameTxt);
        UpNum = findViewById(R.id.UpNumTxt);


        UpCancelBtn = (Button) findViewById(R.id.UpCancelBtn);
        UpCancelBtn.setOnClickListener(v -> {
            Intent intent = new Intent(Subir.this, Inicio.class);
            startActivity(intent);
        });

        spinner = (Spinner) findViewById(R.id.UpCollSpin);



        UpCamBtn = (Button) this.findViewById(R.id.UpCamBtn);
        UpCamBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        });
        this.imageViewUp = (ImageView) this.findViewById(R.id.imageViewUp);
        getCategories();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
                categoryId = categoriesCode.get(collections.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });




        UpSubirBtn = (Button) findViewById(R.id.UpSubirBtn);

        UpSubirBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    processFormFields();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    public void processFormFields() throws JSONException{

        String url = "http://10.0.2.2:8080/api/v1/card";


        CreateCardRequest upRequest = new CreateCardRequest();
        upRequest.setName(UpName.getText().toString());
        upRequest.setCollection_id(Long.parseLong(categoryId));
        upRequest.setCard_number(UpNum.getText().toString());
        imageViewUp.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) imageViewUp.getDrawable()).getBitmap();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        String base64String = Base64.encodeToString(byteArray, Base64.NO_WRAP);



        upRequest.setImage(base64String);
        System.out.println(base64String);


        Gson gson = new Gson();
        JSONObject req = new JSONObject(gson.toJson(upRequest));
        System.out.println(req.toString());
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, req, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Gson gson = new Gson();
                UpName.setText("");
                UpNum.setText("");
                imageViewUp.setImageDrawable(null);
                Toast.makeText(Subir.this, "Carta creada", Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Subir.this, "Error al subir", Toast.LENGTH_LONG).show();
            }
        }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer " + sharedData.getValue());
                return params;
            }
        };
        queue.add(request);
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            imageViewUp.setImageBitmap(photo);
        }

        if (requestCode == SELECT_PHOTO && resultCode == RESULT_OK && data != null &&
                data.getData() != null){
            Uri uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                imageViewUp.setImageBitmap(bitmap);
            }catch (IOException e){
                e.printStackTrace();
            }
        }


        UpGaleryBtn = (Button) this.findViewById(R.id.UpGaleryBtn);
        UpGaleryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galery = new Intent(Intent.ACTION_PICK);
                galery.setType("image/");
                startActivityForResult(galery, SELECT_PHOTO);
            }
        });
        this.imageViewUp = (ImageView) this.findViewById(R.id.imageViewUp);

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
                spinner.setAdapter(dataAdapter);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Subir.this, "Error al cargar Colecciones", Toast.LENGTH_LONG).show();
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

    public List<String> getListOfCollections(ListCollectionsResponse collectionsResponse){
        List<String> collections = new ArrayList<>();
        collections.add("coleccion");
        categoriesCode.put("coleccion","0");
        for (CollectionResponse c : collectionsResponse.getCollections()){
            collections.add(c.getName());
            categoriesCode.put(c.getName(),c.getId().toString());
        }
        return collections;
    }
}