package com.example.crud_v1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WindowShow extends AppCompatActivity {

    TextView etqShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_window_show);
        this.etqShow = findViewById(R.id.etq_show);
        this.showData();
    }

    public void showData(){
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = "http://192.168.0.23/APIenPHP/Obtener.php";

        JsonObjectRequest solicitud =  new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println("El servidor responde con un ok:");
                System.out.println(response);
                showUser(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("El servidor responde con un error:");
                System.out.println(error.getMessage());
            }
        });

        queue.add(solicitud);
    }

    public void showUser(JSONObject data){
        try {
            JSONArray array = data.getJSONArray("registros");
            this.etqShow.setText(" ");
            for (int i = 0; i< array.length(); i++){
                JSONObject users = array.getJSONObject(i);
                this.etqShow.append("Documento: "+users.getString("cedula")+"\n");
                this.etqShow.append("Nombre: "+users.getString("nombres")+"\n");
                this.etqShow.append("Apellido: "+users.getString("apellidos")+"\n");
                this.etqShow.append("Telefono: "+users.getString("telefono")+"\n");
                this.etqShow.append("Direccion: "+users.getString("direccion")+"\n");
                this.etqShow.append("Correo: "+users.getString("email")+"\n");
                this.etqShow.append(" "+"\n");
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}