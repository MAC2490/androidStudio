package com.example.apppreguntas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import  com.example.apppreguntas.utils.config;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    EditText correo, password;
    TextView etq_error;
    config config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.correo = findViewById(R.id.correo);
        this.password = findViewById(R.id.password);
        this.etq_error = findViewById(R.id.etq_error);

        config = new  config(getApplicationContext());
    }

    public void validarIngreso(View vista){
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = config.getEndpoint("API_preguntas/validarIngreso.php");

        StringRequest solicitud =  new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    System.out.println("El servidor POST responde OK");
                    JSONObject jsonObject = new JSONObject(response);

                    Boolean status = jsonObject.getBoolean("status");

                    if (status){
                        String id_usuario = jsonObject.getJSONObject("usuarios").getString("id_usuario");
                        String nombres = jsonObject.getJSONObject("usuarios").getString("nombres");
                        cambiarActivity(id_usuario, nombres);
                    }else{
                        etq_error.setText("EL USUARIO NO ESTA REGITRADO");
                    }
                } catch (JSONException e) {
                    System.out.println("El servidor POST responde con un error:");
                    System.out.println(e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("El servidor POST responde con un error:");
                System.out.println(error.getMessage());
            }
        }){
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("correo", correo.getText().toString());
                params.put("password", password.getText().toString());

                return params;
            }
        };

        queue.add(solicitud);
    }

    public void cambiarActivity(String id_usuario, String nombres){
        Intent intencion= new Intent(getApplicationContext(), Resumen.class);
        intencion.putExtra("nombres", nombres);
        intencion.putExtra("id_usuario", id_usuario);
        startActivity(intencion);
    }
}