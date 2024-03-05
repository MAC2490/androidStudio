package com.example.apppreguntas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.apppreguntas.utils.Config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class IniciarCuestionario extends AppCompatActivity {

    TextView etq_usuario, etq_fechaInicio;

    String id_usuario;

    Config config;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iniciar_cuestionario);

        this.config = new Config(getApplicationContext());
        this.etq_usuario = findViewById(R.id.etq_usuario);
        this.etq_fechaInicio = findViewById(R.id.etq_fecha_inicio);

        Bundle datos = getIntent().getExtras();
        this.etq_usuario.setText(datos.getString("nombre"));
        this.id_usuario = datos.getString("id_usuario");

        Calendar calendar = Calendar.getInstance();
        Date fechaActual = calendar.getTime();
        SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.etq_fechaInicio.setText(formatoFecha.format(fechaActual));
    }

    public void registrarCuestionario(View vista){
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = this.config.getEndpoint("API_preguntas/createCuestionario.php");

        StringRequest solicitud =  new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    System.out.println("El servidor POST responde OK");
                    JSONObject jsonObject = new JSONObject(response);
                    System.out.println(response);
                    cambiarvista(jsonObject);
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
                params.put("id_usuario", id_usuario);

                return params;
            }
        };

        queue.add(solicitud);
    }

    public void cambiarvista(JSONObject datos){
        try {
            JSONObject id_cuestionario = datos.getJSONObject("id_cuestionario");
            Intent intencion= new Intent(getApplicationContext(), Preguntas.class);
            intencion.putExtra("nombre",etq_usuario.getText());
            intencion.putExtra("fecha_inicio",etq_fechaInicio.getText());
            intencion.putExtra("id_cuestionario", id_cuestionario.getString("id"));
            startActivity(intencion);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}