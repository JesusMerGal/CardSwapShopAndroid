package com.example.cardswapshop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class Cartas extends AppCompatActivity {

    Button CartAtrasBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cartas);


        CartAtrasBtn = (Button) findViewById(R.id.CartAtrasBtn);
        CartAtrasBtn.setOnClickListener(v -> {
            Intent intent = new Intent(Cartas.this, Inicio.class);
            startActivity(intent);
        });
    }
}