package com.example.apppokemon;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

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

public class MainActivity extends AppCompatActivity {

    RecyclerView recycler;
    List<Pokemon>listaPokemon = new ArrayList<>();
    int posicion = 0;
    AdapterPokemon adaptador = new AdapterPokemon( this.listaPokemon );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.recycler = findViewById(R.id.recycler_pokemones);

        this.cargarPokemons();
    }

    public void cargarPokemons(){
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = "https://pokeapi.co/api/v2/pokemon?offset="+this.posicion+"&limit=20";

        JsonObjectRequest solicitud =  new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>(){
            @Override
            public void onResponse(JSONObject response) {
                mandarDatos(response);
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

    public void mandarDatos(JSONObject datos){
        try {
            this.adaptador.limpiarLista();
            JSONArray arrayResults = datos.getJSONArray("results");
            for (int i = 0; i<arrayResults.length();i++){
                JSONObject ifoPokemon = arrayResults.getJSONObject(i);
                this.listaPokemon.add(new Pokemon((i+1 < 10 ? "000" : (i+1 < 100 ? "00" : "")) + String.valueOf(i+1), ifoPokemon.getString("name"), ifoPokemon.getString("url")));
            }
            this.adaptador = new AdapterPokemon( this.listaPokemon );
            this.recycler.setAdapter(adaptador);
            this.recycler.setLayoutManager( new LinearLayoutManager(getApplicationContext()) );
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public void siguiente(View vista){
        this.posicion += 20;
        this.cargarPokemons();
    }

    public void anterior(View vista){
        this.posicion -= 20;
        this.cargarPokemons();
    }
}