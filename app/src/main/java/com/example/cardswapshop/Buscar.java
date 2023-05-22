package com.example.cardswapshop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class Buscar extends AppCompatActivity {

    Button BuscAtrasBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar);


        BuscAtrasBtn = (Button) findViewById(R.id.BuscAtrasBtn);
        BuscAtrasBtn.setOnClickListener(v -> {
            Intent intent = new Intent(Buscar.this, Inicio.class);
            startActivity(intent);
        });
    }

}