package com.example.cardswapshop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button LogRegisBtn;
    Button LogLoginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LogRegisBtn = (Button) findViewById(R.id.LogRegisBtn);
        LogRegisBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, Registro.class);
            startActivity(intent);
        });

        LogLoginBtn = (Button) findViewById(R.id.LogLoginBtn);
        LogLoginBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, Inicio.class);
            startActivity(intent);
        });
    }

}