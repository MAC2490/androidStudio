package com.example.apppreguntas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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


public class Resumen extends AppCompatActivity {

    TextView etq_nombre;

    LinearLayout etq_contenedor;

    Config config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resumen);

        this.config = new Config(getApplicationContext());
        SharedPreferences archivo = getSharedPreferences("app-preguntas", MODE_PRIVATE);

        this.etq_nombre = findViewById(R.id.etq_nombre);
        this.etq_contenedor = findViewById(R.id.contenedor);
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
                LinearLayout tarjetaInfo = new LinearLayout(getApplicationContext());
                tarjetaInfo.setOrientation(LinearLayout.VERTICAL);
                tarjetaInfo.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                JSONObject cuestionario = array.getJSONObject(i);
                // TextView
                TextView numero = new TextView(getApplicationContext());
                TextView fecha_inicio = new TextView(getApplicationContext());
                TextView n_preguntas = new TextView(getApplicationContext());
                TextView estado = new TextView(getApplicationContext());
                // Button
                Button btn_ver = new Button(getApplicationContext());
                btn_ver.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                btn_ver.setText("Ver");
                btn_ver.setBackgroundResource(R.drawable.border_btn_drawable);
                btn_ver.setTextColor(Color.WHITE);
                btn_ver.setGravity(Gravity.CENTER);
                // String
                String correctas = cuestionario.getString("cant_ok");
                String errores = cuestionario.getString("cant_error");
                String fecha_fin = cuestionario.getString("fecha_fin");
                String fecha_inicio_t = cuestionario.getString("fecha_inicio");
                String id = cuestionario.getString("id");
                String preguntas = cuestionario.getString("cant_preguntas");
                // setText
                numero.setText("Numero: "+id);
                fecha_inicio.setText("Fecha inicio:  "+fecha_inicio_t);
                n_preguntas.setText("N° Preguntas: "+preguntas);
                estado.setText("N° OK: "+correctas+" - N° Error: "+errores);

                btn_ver.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intencion= new Intent(getApplicationContext(), Detalles_cuestionario.class);
                        intencion.putExtra("nombre",etq_nombre.getText());
                        intencion.putExtra("numero",id);
                        intencion.putExtra("fecha_inicio",fecha_inicio_t);
                        intencion.putExtra("fecha_fin", fecha_fin);
                        intencion.putExtra("n_preguntas",preguntas);
                        intencion.putExtra("correctas",correctas);
                        intencion.putExtra("errores",errores);
                        startActivity(intencion);
                    }
                });

                tarjetaInfo.addView(numero);
                tarjetaInfo.addView(fecha_inicio);
                tarjetaInfo.addView(n_preguntas);
                tarjetaInfo.addView(estado);
                tarjetaInfo.addView(btn_ver);
                tarjetaInfo.setBackgroundResource(R.drawable.border_drawable);
                this.etq_contenedor.addView(tarjetaInfo);
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}