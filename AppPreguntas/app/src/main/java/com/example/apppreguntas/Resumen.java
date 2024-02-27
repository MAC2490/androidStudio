package com.example.apppreguntas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.apppreguntas.utils.Config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Resumen extends AppCompatActivity {

    TextView etq_nombre, etq_prueba;

    Config config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resumen);

        this.config = new Config(getApplicationContext());
        SharedPreferences archivo = getSharedPreferences("app-preguntas", MODE_PRIVATE);

        this.etq_nombre = findViewById(R.id.etq_nombre);
        this.etq_prueba = findViewById(R.id.etq_prueba);
        this.cargarCuestionario(archivo.getString("id_usuario", ""));

        this.etq_nombre.setText(archivo.getString("nombres", ""));
    }

    public void cerrar(View vista){
        SharedPreferences archivo = getSharedPreferences("app-preguntas", MODE_PRIVATE);
        SharedPreferences.Editor editor = archivo.edit();
        editor.clear();
        editor.commit();
        Intent intencion= new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intencion);
        finish();
    }

    public void cargarCuestionario(String id_usuario){
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = config.getEndpoint("API_preguntas/GetCuestionario.php");

        StringRequest solicitud =  new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    System.out.println("El servidor POST responde OK");
                    JSONObject jsonObject = new JSONObject(response);
                    cargarDatos(jsonObject);
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
                params.put("id_usuario", id_usuario.toString());

                return params;
            }
        };

        queue.add(solicitud);
    }

    public void cargarDatos(JSONObject datos){
        try {
            JSONArray array = datos.getJSONArray("registros");
            for (int i = 0; i< array.length(); i++){
                JSONObject cuestionario = array.getJSONObject(i);
                TextView numero = new TextView();
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}