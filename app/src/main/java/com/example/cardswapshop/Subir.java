package com.example.cardswapshop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class Subir extends AppCompatActivity {

    Button UpCancelBtn;
    Spinner UpCollSpin;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subir);


        UpCancelBtn = (Button) findViewById(R.id.UpCancelBtn);
        UpCancelBtn.setOnClickListener(v -> {
            Intent intent = new Intent(Subir.this, Inicio.class);
            startActivity(intent);
        });





    }
}