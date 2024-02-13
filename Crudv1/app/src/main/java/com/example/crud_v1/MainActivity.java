package com.example.crud_v1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void windowInsert(View view){
        Intent intencion= new Intent(getApplicationContext(), WindowInsert.class);
        startActivity(intencion);
    }

    public void windowUpdate(View view){
        Intent intencion= new Intent(getApplicationContext(), WindowUpdate.class);
        startActivity(intencion);
    }

    public void windowShow(View view){
        Intent intencion= new Intent(getApplicationContext(), WindowShow.class);
        startActivity(intencion);
    }

    public void windowDelete(View view){
        Intent intencion= new Intent(getApplicationContext(), WindowDelete.class);
        startActivity(intencion);
    }
}