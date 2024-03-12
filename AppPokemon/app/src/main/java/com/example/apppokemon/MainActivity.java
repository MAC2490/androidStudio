package com.example.apppokemon;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recycler;
    List<Pokemon>listaPokemon = new ArrayList<>();
    int posicion = 0, numPokemon = 0;
    AdapterPokemon adaptador = new AdapterPokemon( this.listaPokemon );
    String previous;
    String next;
    Button btnAnterior, btnSiguiente;
    ImageView imgGift;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.recycler = findViewById(R.id.recycler_pokemones);
        this.btnAnterior = findViewById(R.id.anterior);
        this.btnSiguiente = findViewById(R.id.siguiente);
        this.imgGift = findViewById(R.id.img_gift);

        this.cargarPokemons();
    }

    public void cargarPokemons(){
        Glide.with(this)
                .asGif()
                .load(R.drawable.loading_pokeball)
                .into(imgGift);
        this.imgGift.setVisibility(View.VISIBLE);
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = "https://pokeapi.co/api/v2/pokemon?offset="+this.posicion+"&limit=20";
        JsonObjectRequest solicitud =  new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>(){
            @Override
            public void onResponse(JSONObject response) {
                imgGift.setVisibility(View.GONE);
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
            this.previous = datos.getString("previous");
            this.next = datos.getString("next");
            this.adaptador.limpiarLista();
            JSONArray arrayResults = datos.getJSONArray("results");
            for (int i = 0; i<arrayResults.length();i++){
                JSONObject ifoPokemon = arrayResults.getJSONObject(i);
                this.numPokemon++;
                this.listaPokemon.add(new Pokemon((this.numPokemon < 10 ? "000" : (this.numPokemon < 100 ? "00" : "")) + String.valueOf(this.numPokemon), ifoPokemon.getString("name"), ifoPokemon.getString("url")));
            }
            this.adaptador = new AdapterPokemon( this.listaPokemon );
            this.recycler.setAdapter(adaptador);
            this.recycler.setLayoutManager( new LinearLayoutManager(getApplicationContext()) );
            validarBtn();
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
        if (this.next.equalsIgnoreCase("null")){
            this.numPokemon -= 22;
        }else{
            this.numPokemon -= 40;
        }
        this.cargarPokemons();
    }

    public void validarBtn(){
        System.out.println("next "+this.next+" prev "+this.previous);
        if (this.previous.equalsIgnoreCase("null")){
            this.btnAnterior.setEnabled(false);
        }else{
            this.btnAnterior.setEnabled(true);
        }

        if (this.next.equalsIgnoreCase("null")){
            this.btnSiguiente.setEnabled(false);
        }else{
            this.btnSiguiente.setEnabled(true);
        }
    }
}