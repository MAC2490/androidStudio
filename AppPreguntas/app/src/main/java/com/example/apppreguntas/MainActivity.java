package com.example.apppreguntas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
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
import com.example.apppreguntas.utils.Config;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    EditText correo, password;
    TextView etq_error;
    Config config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.correo = findViewById(R.id.correo);
        this.password = findViewById(R.id.password);
        this.etq_error = findViewById(R.id.etq_error);

        this.config = new Config(getApplicationContext());

        this.validarSesion();
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
        // Abrir archivos de persistencia y almacenar datos de usuario.
        SharedPreferences archivo = getSharedPreferences("app-preguntas", MODE_PRIVATE);
        SharedPreferences.Editor editor = archivo.edit();
        editor.putString("id_usuario", id_usuario);
        editor.putString("nombres", nombres);
        editor.commit();

        Intent intencion= new Intent(getApplicationContext(), Resumen.class);
        startActivity(intencion);
        finish();
    }

    public void validarSesion(){
        SharedPreferences archivo = getSharedPreferences("app-preguntas", MODE_PRIVATE);
        String id_usuario = archivo.getString("id_usuario", null);
        String nombres = archivo.getString("nombres", null);

        if (id_usuario != null && nombres!= null){
            Intent intencion= new Intent(getApplicationContext(), Resumen.class);
            startActivity(intencion);
            finish();
        }
    }
}