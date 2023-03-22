package com.jcecilio.parejas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.*;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;


public class comit extends AppCompatActivity {

    // respuestas de la página web
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comit);
        // ocultar la barra de la aplicación
        if (getSupportActionBar() != null)  getSupportActionBar().hide();

        final Button btEnviar = findViewById(R.id.btnEnviar);
        final Button volver = findViewById(R.id.button3);
        final EditText cajaJugador =  findViewById(R.id.eTxtJugador);
        final EditText cajaPuntosT =  findViewById(R.id.eTxtPT);
        final EditText cajaPuntosJ =  findViewById(R.id.eTxtPJ);
        final EditText cajaBonus =  findViewById(R.id.eTxtBonus);
        final EditText cajaTiempo =  findViewById(R.id.eTxtTiempo);
        final TextView msjERROR = findViewById(R.id.txtMSJerror);
        long tiempo = MainActivity.tiempoDeJuego;
        long bonus = tiempo *5;
        long puntos =MainActivity.puntuacion;
        long total = bonus+puntos;
        cajaJugador.setText("");
        cajaPuntosJ.setText(String.valueOf(puntos));
        cajaPuntosT.setText(String.valueOf(total));
        cajaTiempo.setText(String.valueOf(tiempo));
        cajaBonus.setText(String.valueOf(bonus));
volver.setVisibility(View.INVISIBLE);
          btEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // servidor php que procesa el POST
                String url = "https://digitalgentilis.com/apps/android/actualiza.php";
                StringRequest stringRequest = new StringRequest(Request.Method.POST,url,
                        // response -> Toast.makeText(MainActivity.this, response, Toast.LENGTH_LONG).show(),
                        response -> msjERROR.setText(response),
                        error -> Toast.makeText(comit.this, error.toString(), Toast.LENGTH_LONG).show()){
                    // añadir los parametros de la respuesta
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError{
                        Map<String, String> params = new HashMap<>();
                        params.put("JU", cajaJugador.getText().toString());
                        params.put("PT", cajaPuntosT.getText().toString());
                        params.put("PJ", cajaPuntosJ.getText().toString());
                        params.put("BO", cajaBonus.getText().toString());
                        params.put("TI", cajaTiempo.getText().toString());
                        return params;
                    }
                };
                requestQueue = Volley.newRequestQueue(comit.this);
                requestQueue.add(stringRequest);
            }
        });
volver.setVisibility(View.VISIBLE);
volver.setText("Volver");
volver.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Intent intent = new Intent(comit.this, MainActivity.class);
        startActivity(intent);
    }
});


    }
}