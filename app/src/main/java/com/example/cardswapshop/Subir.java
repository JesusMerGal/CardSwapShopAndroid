package com.example.cardswapshop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;

import java.io.IOException;
import java.util.Stack;

public class Subir extends AppCompatActivity {

    String[] collections= {"","Pokemon","Digimon","La liga", "Magic"};
    Button UpCancelBtn, UpGaleryBtn, UpCamBtn, UpSubirBtn;
    Spinner spinner;

    EditText UpName, UpNum;

    private ImageView imageViewUp;
    private static final int CAMERA_REQUEST = 1888;
    private static final int SELECT_PHOTO = 1889;

    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subir);

        UpName = findViewById(R.id.UpNameTxt);
        UpNum = findViewById(R.id.UpNumTxt);


        UpCancelBtn = (Button) findViewById(R.id.UpCancelBtn);
        UpCancelBtn.setOnClickListener(v -> {
            Intent intent = new Intent(Subir.this, Inicio.class);
            startActivity(intent);
        });

        spinner = findViewById(R.id.UpCollSpin);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item,
                collections);
        spinner.setAdapter(adapter);


        UpCamBtn = (Button) this.findViewById(R.id.UpCamBtn);
        UpCamBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        });
        this.imageViewUp = (ImageView) this.findViewById(R.id.imageViewUp);


        queue = Volley.newRequestQueue(Subir.this);

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

        String url = "";
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
}