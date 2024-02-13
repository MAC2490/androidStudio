package com.example.crud_v1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class WindowInsert extends AppCompatActivity {
    EditText document, name, lastName, phone, addres, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_window_insert);
        this.document = findViewById(R.id.document);
        this.name = findViewById(R.id.name);
        this.lastName = findViewById(R.id.lastName);
        this.phone = findViewById(R.id.phone);
        this.addres = findViewById(R.id.addres);
        this.email = findViewById(R.id.email);
    }

    public void consumoPostJson(View vista){
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = "http://192.168.0.23/APIenPHP/Insert.php";
        StringRequest solicitud =  new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    System.out.println("El servidor POST responde OK");
                    JSONObject jsonObject = new JSONObject(response);
                    Intent intencion= new Intent(getApplicationContext(), WindowShow.class);
                    startActivity(intencion);
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
                params.put("cedula", document.getText().toString());
                params.put("nombres", name.getText().toString());
                params.put("apellidos", lastName.getText().toString());
                params.put("telefono", phone.getText().toString());
                params.put("direccion", addres.getText().toString());
                params.put("email", email.getText().toString());

                return params;
            }
        };

        queue.add(solicitud);
    }
}