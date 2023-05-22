package com.example.cardswapshop;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class Inicio extends AppCompatActivity {

    Button IniSubirBtn;
    Button IniBuscarBtn;
    Button IniCartasBtn;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        IniBuscarBtn = (Button) findViewById(R.id.IniBuscarBtn);
        IniBuscarBtn.setOnClickListener(v -> {
            Intent intent = new Intent(Inicio.this, Buscar.class);
            startActivity(intent);
        });

        IniSubirBtn = (Button) findViewById(R.id.IniSubirBtn);
        IniSubirBtn.setOnClickListener(v -> {
            Intent intent = new Intent(Inicio.this, Subir.class);
            startActivity(intent);
        });

        IniCartasBtn = (Button) findViewById(R.id.IniCartasBtn);
        IniCartasBtn.setOnClickListener(v -> {
            Intent intent = new Intent(Inicio.this, Cartas.class);
            startActivity(intent);
        });
    }
}