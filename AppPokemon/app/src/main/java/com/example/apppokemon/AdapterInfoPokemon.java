package com.example.apppokemon;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdapterInfoPokemon extends RecyclerView.Adapter<AdapterInfoPokemon.ViewHolder> {

    List<DatosInfoPokemon> listaDatos;

    public AdapterInfoPokemon(List<DatosInfoPokemon> listaDatos) {
        this.listaDatos = listaDatos;
    }

    @NonNull
    @Override
    public AdapterInfoPokemon.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_habilidades, parent, false);
        return new AdapterInfoPokemon.ViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterInfoPokemon.ViewHolder holder, int position) {
        DatosInfoPokemon temporal = this.listaDatos.get(position);
        holder.cargarDatos(temporal);
    }

    @Override
    public int getItemCount() {
        return listaDatos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView etq_habilidades;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.etq_habilidades = itemView.findViewById(R.id.etqHabilidad);
        }

        public void cargarDatos(DatosInfoPokemon datos){
            this.etq_habilidades.setText(datos.getNombre());
        }
    }
}
