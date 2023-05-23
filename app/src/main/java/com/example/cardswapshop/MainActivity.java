package com.example.cardswapshop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
import com.android.volley.toolbox.Volley;
import com.example.cardswapshop.dto.request.LoginRequest;
import com.example.cardswapshop.dto.request.RegisterRequest;
import com.example.cardswapshop.dto.response.TokenResponse;
import com.example.cardswapshop.helpers.Globals;
import com.example.cardswapshop.helpers.StringHelper;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    Button LogRegisBtn;
    Button LogLoginBtn;

    EditText LogEmail,LogPassword;
    private RequestQueue queue;

    Globals sharedData = Globals.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LogEmail = findViewById(R.id.LogEmailTxt);
        LogPassword = findViewById(R.id.LogPassTxt);
        LogRegisBtn = (Button) findViewById(R.id.LogRegisBtn);


        queue = Volley.newRequestQueue(MainActivity.this);

        LogRegisBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, Registro.class);
            startActivity(intent);
        });

        LogLoginBtn = (Button) findViewById(R.id.LogLoginBtn);
        LogLoginBtn.setOnClickListener(new View.OnClickListener() {
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
        if (!validateEmail() || !validatePass()) {
            return;
        }

        String url = "http://10.0.2.2:8080/api/v1/auth/authenticate";

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(LogEmail.getText().toString());
        loginRequest.setPassword(LogPassword.getText().toString());

        Gson gson = new Gson();
        JSONObject req = new JSONObject(gson.toJson(loginRequest));
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, req, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Gson gson = new Gson();
                TokenResponse tokenResponse = gson.fromJson(response.toString(), TokenResponse.class);
                LogEmail.setText("");
                LogPassword.setText("");
                sharedData.setValue(tokenResponse.getToken());

                Toast.makeText(MainActivity.this, "Logged in", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(MainActivity.this, Inicio.class);
                startActivity(intent);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Login NO completado", Toast.LENGTH_LONG).show();
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


    public boolean validateEmail(){
        String REmail = LogEmail.getText().toString();

        if(REmail.isEmpty()){
            LogEmail.setError("Email cannot be empty");
            return false;
        }else if (!StringHelper.regexEmailValidation(REmail)){
            LogEmail.setError("Correo no valido");
            return false;
        }else{
            LogEmail.setError(null);
            return true;
        }
    }

    public boolean validatePass(){
        String RPass = LogPassword.getText().toString();

        if(RPass.isEmpty()){
            LogPassword.setError("Contraseña cannot be empty");
            return false;
        }else{
            LogPassword.setError(null);
            return true;
        }
    }
}