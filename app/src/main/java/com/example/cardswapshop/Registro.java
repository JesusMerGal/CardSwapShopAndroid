package com.example.cardswapshop;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.cardswapshop.dto.request.RegisterRequest;
import com.example.cardswapshop.dto.response.TokenResponse;
import com.example.cardswapshop.helpers.StringHelper;
import com.google.gson.Gson;

import org.json.JSONObject;
import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

public class Registro extends AppCompatActivity {

    Button RegBackBtn;
    Button RegRegisBtn;
    EditText RegFName, RegLName, RegEmail, RegPassword;

    private RequestQueue queue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        RegFName = findViewById(R.id.RegFirstNameTxt);
        RegLName = findViewById(R.id.RegLastNameTxt);
        RegEmail = findViewById(R.id.RegEmailTxt);
        RegPassword = findViewById(R.id.RegPasswordTxt);

        RegRegisBtn = findViewById(R.id.RegRegisBtn);

        queue = Volley.newRequestQueue(Registro.this);

        RegBackBtn = (Button) findViewById(R.id.RegBackBtn);
        RegBackBtn.setOnClickListener(v -> {
            Intent intent = new Intent(Registro.this, MainActivity.class);
            startActivity(intent);
        });

        RegRegisBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    processFormFields();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    public void processFormFields() throws JSONException {
        if (!validateFirstName() || !validateLastName() || !validateEmail() || !validatePass()) {
            return;
        }

        String url = "http://10.0.2.2:8080/api/v1/auth/register";

        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setFirstname(RegFName.getText().toString());
        registerRequest.setLastname(RegLName.getText().toString());
        registerRequest.setEmail(RegEmail.getText().toString());
        registerRequest.setPassword(RegPassword.getText().toString());

        Gson gson = new Gson();
        JSONObject req = new JSONObject(gson.toJson(registerRequest));
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, req, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Gson gson = new Gson();
                TokenResponse tokenResponse = gson.fromJson(response.toString(), TokenResponse.class);
                RegFName.setText("");
                RegLName.setText("");
                RegEmail.setText("");
                RegPassword.setText("");
                SharedPreferences preferences = getSharedPreferences("myprefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                String token = tokenResponse.getToken();
                editor.putString("token", token);
                editor.commit();

                System.out.println(preferences.getString("token", ""));

                Toast.makeText(Registro.this, "Registro completado", Toast.LENGTH_LONG).show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Registro.this, "Registro NO completado", Toast.LENGTH_LONG).show();
            }
        });
        queue.add(request);
                }
    public boolean validateFirstName(){
        String RFName = RegFName.getText().toString();

        if(RFName.isEmpty()){
            RegFName.setError("Name cannot be empty");
            return false;
        }else{
            RegFName.setError(null);
            return true;
        }
    }

    public boolean validateLastName(){
        String RLName = RegLName.getText().toString();

        if(RLName.isEmpty()){
            RegLName.setError("Name cannot be empty");
            return false;
        }else{
            RegLName.setError(null);
            return true;
        }
    }

    public boolean validateEmail(){
        String REmail = RegEmail.getText().toString();

        if(REmail.isEmpty()){
            RegEmail.setError("Email cannot be empty");
            return false;
        }else if (!StringHelper.regexEmailValidation(REmail)){
            RegEmail.setError("Correo no valido");
            return false;
        }else{
            RegEmail.setError(null);
            return true;
        }
    }

    public boolean validatePass(){
        String RPass = RegPassword.getText().toString();

        if(RPass.isEmpty()){
            RegPassword.setError("Contraseña cannot be empty");
            return false;
        }else{
            RegPassword.setError(null);
            return true;
        }
    }
}