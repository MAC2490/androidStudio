package com.example.apppreguntas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.apppreguntas.utils.Config;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Preguntas extends AppCompatActivity {

    TextView etq_nombre, fecha_inicio, n_pregunta, pregunta, tvOpciones;
    ImageView img;
    Random random = new Random();
    RadioGroup opcionesRadioGroup;
    Config config;
    int id [];
    int pos;
    String id_cuestionario, inicio;
    Button btn;
    Map<String, String> datosMap;
    LinearLayout contenedorPrincipal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preguntas);

        this.id = new int[10];
        Arrays.fill(id, -1);
        this.pos = 0;
        this.config = new Config(getApplicationContext());
        this.datosMap = new HashMap<>();

        this.etq_nombre = findViewById(R.id.etq_nombre);
        this.fecha_inicio = findViewById(R.id.fecha_inicio);
        this.n_pregunta = findViewById(R.id.n_preguntas);
        this.pregunta = findViewById(R.id.pregunta);
        this.img = findViewById(R.id.img);
        this.btn = findViewById(R.id.btn);
        this.opcionesRadioGroup = findViewById(R.id.opcionesRadioGroup);
        this.contenedorPrincipal = findViewById(R.id.contenedorBtn);
        this.getPregunta(this.random.nextInt(10) + 1);
        Bundle datos = getIntent().getExtras();
        this.etq_nombre.setText(datos.getString("nombre"));
        this.inicio = datos.getString("fecha_inicio");
        this.id_cuestionario = datos.getString("id_cuestionario");
        SpannableString inicioSpannable = new SpannableString("Inicio: " + inicio);
        inicioSpannable.setSpan(new StyleSpan(Typeface.BOLD), 7, inicioSpannable.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        this.fecha_inicio.setText(inicioSpannable);
    }

    public void idAleatorio(View vista){
        if (this.btn.getText() != "FINALIZAR"){
            for (int i = 0; i<this.id.length;i++){
                int id_aleatorio = this.random.nextInt(10) + 1;
                boolean validar_id = true;
                for (int j = 0; j<this.id.length;j++){
                    if (id_aleatorio == this.id[j]){
                        validar_id = false;
                    }
                }
                if (validar_id){
                    this.getPregunta(id_aleatorio);
                    break;
                }
            }
            if (this.id[9] != -1){
                this.contenedorPrincipal.removeAllViews();
                this.btn.setBackgroundColor(Color.RED);
                this.btn.setText("FINALIZAR");
                this.contenedorPrincipal.addView(this.btn);
            }
            this.registrar_pregunta();
        }else{
            Intent intencion= new Intent(getApplicationContext(), Resumen.class);
            startActivity(intencion);
        }
    }

    public void getPregunta(int id){
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = this.config.getEndpoint("API_preguntas/getPregunta.php");

        StringRequest solicitud =  new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    System.out.println("El servidor POST responde OK");
                    JSONObject jsonObject = new JSONObject(response);
                    mostraPregunta(jsonObject);
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
                params.put("id_pregunta", String.valueOf(id));

                return params;
            }
        };

        queue.add(solicitud);
    }

    public void mostraPregunta(JSONObject datos){
        try {
            JSONObject respuesta = datos.getJSONObject("respuesta");
            JSONObject infoPregunta = respuesta.getJSONObject("pregunta");

            String n_pregunta_text = infoPregunta.getString("id");
            SpannableString nPreguntaSpannable = new SpannableString("Pregunta Actual: " + n_pregunta_text);
            nPreguntaSpannable.setSpan(new StyleSpan(Typeface.BOLD), 16, nPreguntaSpannable.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            this.n_pregunta.setText(nPreguntaSpannable);
            this.pregunta.setText(infoPregunta.getString("descripcion"));

            String id_correcta = infoPregunta.getString("id_correcta");

            Picasso.get()
                    .load(infoPregunta.getString("url_imagen"))
                    .resize(600, 600)
                    .centerCrop()
                    .into(img);

            JSONArray opcionesList = respuesta.getJSONArray("opciones");
            this.opcionesRadioGroup.removeAllViews();
            for (int i = 0; i<opcionesList.length(); i++){
                JSONObject opciones = opcionesList.getJSONObject(i);

                RadioButton opcion = new RadioButton(getApplicationContext());
                opcion.setTextSize(17);
                opcion.setId(Integer.parseInt(opciones.getString("id")));
                opcion.setText(opciones.getString("descripcion"));
                this.opcionesRadioGroup.addView(opcion);

                opcionesRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        RadioButton radioButton = findViewById(checkedId);

                        if (radioButton != null && radioButton.isChecked()) {
                            String estado;
                            if (checkedId == Integer.parseInt(id_correcta)){
                                estado = "OK";
                            }else{
                                estado = "ERROR";
                            }
                            datosMap.put("id_cuestionario", id_cuestionario);
                            datosMap.put("id_pregunta", n_pregunta_text);
                            datosMap.put("descripcion", radioButton.getText().toString());
                            datosMap.put("estado", estado);
                            datosMap.put("fecha_inicio", inicio);
                        }
                    }
                });
            }
            this.id[this.pos] = Integer.parseInt(infoPregunta.getString("id"));
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        this.pos++;
    }

    public void registrar_pregunta(){
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = this.config.getEndpoint("API_preguntas/insertRespuesta.php");

        StringRequest solicitud =  new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    System.out.println("El servidor POST responde OK");
                    JSONObject jsonObject = new JSONObject(response);
                    System.out.println("Respuesta: "+response);
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
                return datosMap;
            }
        };
        System.out.println("datos "+datosMap);
        queue.add(solicitud);
    }
}