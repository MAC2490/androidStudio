package com.example.apppokemon;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterFotoPokemon extends RecyclerView.Adapter< AdapterFotoPokemon.ViewHolder > {

    List<FotoPokemon> listaFoto;

    public AdapterFotoPokemon(List<FotoPokemon>listaFoto){
        this.listaFoto = listaFoto;
    }

    @NonNull
    @Override
    public AdapterFotoPokemon.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_fotos_pokemon, parent, false);
        return new AdapterFotoPokemon.ViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterFotoPokemon.ViewHolder holder, int position) {
        FotoPokemon temporal = this.listaFoto.get(position);
        holder.cargarDatos(temporal);
    }

    @Override
    public int getItemCount() {
        return listaFoto.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPokemon;
        Context contexto;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.contexto = itemView.getContext();
            this.imgPokemon = itemView.findViewById(R.id.imgPokemon);
        }

        public void cargarDatos(FotoPokemon datos){
            System.out.println(datos.getUrlPokemon());
            Picasso.get()
                    .load(datos.getUrlPokemon())
                    .resize(500, 500)
                    .centerCrop()
                    .into(imgPokemon);
        }
    }
}
