package com.example.apppokemon;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.List;

public class AdapterPokemon extends RecyclerView.Adapter< AdapterPokemon.ViewHolder > {

    List<Pokemon> listaPokemon;

    public AdapterPokemon(List<Pokemon> listaPokemon){
        this.listaPokemon = listaPokemon;
    }

    @NonNull
    @Override
    public AdapterPokemon.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pokemon, parent, false);
        return new ViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterPokemon.ViewHolder holder, int position) {
        Pokemon temporal = this.listaPokemon.get(position);
        holder.cargarDatos(temporal, position);
    }

    @Override
    public int getItemCount() {
        return listaPokemon.size();
    }

    public void limpiarLista(){
        listaPokemon.clear();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView numeroPokemon, nombrePokemon;
        ImageView btnVer;
        Context contexto;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.contexto = itemView.getContext();
            this.numeroPokemon = itemView.findViewById(R.id.numeroPokemon);
            this.nombrePokemon = itemView.findViewById(R.id.nombrePokemon);
            this.btnVer = itemView.findViewById(R.id.btnVer);
        }

        public void cargarDatos(Pokemon datos, int position){
            this.numeroPokemon.setText(datos.getNumeroPokemon());
            this.nombrePokemon.setText(datos.getNombrePokemon().toUpperCase());
            this.btnVer.setTag(datos.getUrlImg());
            btnVer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intencion = new Intent(contexto.getApplicationContext(), InfoPokemon.class);
                    intencion.putExtra("url", v.getTag().toString());
                    intencion.putExtra("nombre", datos.getNombrePokemon());
                    contexto.startActivity(intencion);
                }
            });
        }
    }
}
