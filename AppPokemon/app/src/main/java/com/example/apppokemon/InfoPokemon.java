package com.example.apppokemon;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.List;

public class InfoPokemon extends AppCompatActivity {

    List<FotoPokemon> listaUrl = new ArrayList<>();
    List<DatosInfoPokemon> listaDatos = new ArrayList<>();
    RecyclerView recycler, recycler_habilidades;
    AdapterFotoPokemon adaptador = new AdapterFotoPokemon( listaUrl );
    AdapterInfoPokemon adaptadorInfo = new AdapterInfoPokemon( listaDatos );
    TextView etqNombrePokemon, etqAltura, etqPeso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_pokemon);

        this.recycler = findViewById(R.id.recycler_info_pokemon);
        this.recycler_habilidades = findViewById(R.id.recycler_habilidades);
        this.etqNombrePokemon = findViewById(R.id.nombrePokemon);
        this.etqAltura = findViewById(R.id.etqAltura);
        this.etqPeso = findViewById(R.id.etqPeso);


        Bundle datos = getIntent().getExtras();
        consumoFotos(datos.getString("url", null));
        this.etqNombrePokemon.setText(datos.getString("nombre", "nombre").toUpperCase());
    }

    public void consumoFotos(String urlImg){
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = urlImg;

        JsonObjectRequest solicitud =  new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>(){
            @Override
            public void onResponse(JSONObject response) {
                cargarDatosFotos(response);
                cargarDatosInfo(response);
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

    public void cargarDatosFotos(JSONObject datos){
        try {
            JSONObject sprites = datos.getJSONObject("sprites");

            this.listaUrl.add(new FotoPokemon(sprites.getString("front_default")));
            this.listaUrl.add(new FotoPokemon(sprites.getString("back_default")));
            this.listaUrl.add(new FotoPokemon(sprites.getString("front_shiny")));
            this.listaUrl.add(new FotoPokemon(sprites.getString("back_shiny")));

            this.adaptador = new AdapterFotoPokemon( this.listaUrl );
            this.recycler.setAdapter(adaptador);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
            this.recycler.setLayoutManager(layoutManager);

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public void cargarDatosInfo(JSONObject datos){
        try {
            this.etqPeso.setText(datos.getString("weight"));
            this.etqAltura.setText(datos.getString("height"));
            JSONArray abilities = datos.getJSONArray("abilities");
            for (int i = 0; i<abilities.length(); i++){
                JSONObject object = abilities.getJSONObject(i);
                JSONObject ability = object.getJSONObject("ability");

                this.listaDatos.add(new DatosInfoPokemon(ability.getString("name")));
            }
            this.adaptadorInfo = new AdapterInfoPokemon( this.listaDatos );
            this.recycler_habilidades.setAdapter(adaptadorInfo);
            this.recycler_habilidades.setLayoutManager( new LinearLayoutManager(getApplicationContext()) );
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

    }
}