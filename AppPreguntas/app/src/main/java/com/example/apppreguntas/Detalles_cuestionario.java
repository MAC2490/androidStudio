package com.example.apppreguntas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
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

import java.util.HashMap;
import java.util.Map;

public class Detalles_cuestionario extends AppCompatActivity {

    TextView nombre,fecha_inicio,preguntas,correctas,errores;

    Config config;

    String id;

    LinearLayout lis_respuestas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_cuestionario);
        this.nombre = findViewById(R.id.etq_nombre);
        this.fecha_inicio = findViewById(R.id.fecha_inicio);
        this.preguntas = findViewById(R.id.preguntas);
        this.correctas = findViewById(R.id.correctas);
        this.errores = findViewById(R.id.errores);
        this.lis_respuestas = findViewById(R.id.list_preguntas);

        this.config = new Config(getApplicationContext());

        Bundle datos = getIntent().getExtras();
        this.id = datos.getString("numero");
        this.nombre.setText(datos.getString("nombre"));
        this.fecha_inicio.setText("Fecha inicio: "+datos.getString("fecha_inicio"));
        this.preguntas.setText("Preguntas: "+datos.getString("n_preguntas"));
        this.correctas.setText("Correctas: "+datos.getString("correctas"));
        this.errores.setText("Incorrectas: "+datos.getString("errores"));

        this.cargarPreguntas();
    }

    public void cargarPreguntas(){
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = this.config.getEndpoint("API_preguntas/GetPreguntas.php?id_cuestionario="+this.id);

        StringRequest solicitud =  new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    System.out.println("El servidor POST responde OK");
                    JSONObject jsonObject = new JSONObject(response);
                    mostrarPreguntas(jsonObject);
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
        });

        queue.add(solicitud);
    }

    public void mostrarPreguntas(JSONObject datos){
        try {
            JSONArray respuesta = datos.getJSONArray("respuesta");
            for (int i = 0; i<respuesta.length(); i++){
                JSONObject objeto = respuesta.getJSONObject(i);
                JSONObject pregunta = objeto.getJSONObject("pregunta");
                String respuesta_p = pregunta.getString("respuesta");
                String estado_p = pregunta.getString("estado");

                TextView n_pregunta = new TextView(getApplicationContext());
                n_pregunta.setText("Pregunta: "+(i+1));
                n_pregunta.setTextColor(Color.BLACK);
                n_pregunta.setTypeface(n_pregunta.getTypeface(), Typeface.BOLD);
                n_pregunta.setTextSize(20);

                TextView texto_pregunta = new TextView(getApplicationContext());
                texto_pregunta.setText(pregunta.getString("descripcion"));
                texto_pregunta.setTextColor(Color.BLACK);

                this.lis_respuestas.addView(n_pregunta);
                this.lis_respuestas.addView(texto_pregunta);

                JSONArray opciones = objeto.getJSONArray("opciones");

                for (int j = 0; j<opciones.length(); j++){
                    JSONObject opcion = opciones.getJSONObject(j);
                    String descripcion = opcion.getString("descripcion");

                    TextView texto_opcion = new TextView(getApplicationContext());
                    texto_opcion.setText("   * "+opcion.getString("descripcion"));
                    texto_opcion.setTextColor(Color.BLACK);

                    if (respuesta_p.equalsIgnoreCase(descripcion)){
                        if (estado_p.equals("OK")){
                            texto_opcion.setTextColor(Color.parseColor("#27AE60"));
                            texto_opcion.setTypeface(n_pregunta.getTypeface(), Typeface.BOLD);
                        }else if (estado_p.equals("ERROR")){
                            texto_opcion.setTextColor(Color.RED);
                            texto_opcion.setTypeface(n_pregunta.getTypeface(), Typeface.BOLD);
                        }
                    }
                    this.lis_respuestas.addView(texto_opcion);
                }
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public void volver(View vista){
        Intent intencion= new Intent(getApplicationContext(), Resumen.class);
        startActivity(intencion);
    }
}