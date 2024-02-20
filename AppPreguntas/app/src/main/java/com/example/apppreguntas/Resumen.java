package com.example.apppreguntas;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class Resumen extends AppCompatActivity {

    TextView etq_nombre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resumen);
        this.etq_nombre = findViewById(R.id.etq_nombre);
        Bundle datos = getIntent().getExtras();
        this.etq_nombre.setText(datos.getString("nombres"));
    }
}