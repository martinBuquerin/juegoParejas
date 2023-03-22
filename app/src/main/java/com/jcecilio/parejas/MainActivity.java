package com.jcecilio.parejas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.os.CountDownTimer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {
    /*
        Creamos un array con los identificadores de los objetos ImageView
        Estos identificadores son simplemente enteros int
     */
    private static final int[] idArray = {R.id.img00,R.id.img01,R.id.img02,R.id.img03,R.id.img04,
            R.id.img10,R.id.img11,R.id.img12,R.id.img13,R.id.img14,
            R.id.img20,R.id.img21,R.id.img22,R.id.img23,R.id.img24,
            R.id.img30,R.id.img31,R.id.img32,R.id.img33,R.id.img34};

    /*
        Creamos un array con los identificadores de los drawable
     */
    private static final int[] idDrawable = {R.drawable._0_, R.drawable._1_, R.drawable._2_,
            R.drawable._3_, R.drawable._4_, R.drawable._5_, R.drawable._6_, R.drawable._7_,
            R.drawable._8_, R.drawable._9_ };

    int clickCont =0;
    View viewMiCarta = null;
    /*
        Creamos un array con las posiciones de las cartas durante el juego
        Para ello, necesitamos un array del doble de longitud que el de las imágenes
        ya que cada carta está dos veces en el juego.
     */
    private static final int[] posicionesPartida = new int[idArray.length];

    /*
        Ahora, creamos un array de objetos ImageView
        de momento está vacío
     */
    private ImageView carta[] = new ImageView[idArray.length];
    ArrayList<Integer> resultado = new ArrayList<>();
    // El siguiente array mantiene el estado de cada carta según esta tabla
    // 0 -> tapada
    // 1 -> destapada
    // 2 -> acertada
    private static int destapada[] = new int[20];
    final Handler handler = new Handler();
    int aciertos;
    private static int imagenAnteriorDestapada = 0;
    private static int posicionAnteriorDestapada = 99;
    private ImageView imagenViewAnteriorDestapada= null;
    static long tiempoDeJuego;
    static int puntuacion;
    EditText timer,scoreBoard;
    TextView txtTimer,txtScoreBoard;
    Button enviarResultado, iniciarReiniciar ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        timer = (EditText) findViewById(R.id.editTextTextPersonName2);
        scoreBoard = (EditText) findViewById(R.id.editTextTextPersonName);
        txtScoreBoard = (TextView) findViewById(R.id.textView);
        txtTimer = (TextView) findViewById(R.id.textView2);
        enviarResultado = (Button) findViewById(R.id.button);
        iniciarReiniciar = (Button) findViewById(R.id.button2);
        timer.setText("");
        scoreBoard.setText("");
        txtScoreBoard.setTextSize(22);
        txtScoreBoard.setTextColor(getResources().getColor(R.color.black));
        txtScoreBoard.setText("Puntuación: ");
        txtTimer.setTextSize(22);
        txtTimer.setTextColor(getResources().getColor(R.color.black));
        txtTimer.setText("Tiempo: ");
        timer.setBackgroundColor(Color.TRANSPARENT);
        scoreBoard.setBackgroundColor(Color.TRANSPARENT);
        enviarResultado.setVisibility(View.INVISIBLE);
        iniciarReiniciar.setText("Iniciar");
        if (getSupportActionBar() != null) getSupportActionBar().hide();

        for (int i = 0; i < idArray.length; i++){
            carta[i] = (ImageView) findViewById(idArray[i]);
            carta[i].setImageResource(R.drawable.rever00);
        }
        aciertos=0;
        barajea(idDrawable.length);
        iniciarReiniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iniciarReiniciar.setVisibility(View.INVISIBLE);
                new CountDownTimer(120000, 1000) {

                    public void onTick(long millisUntilFinished) {
                        tiempoDeJuego  = millisUntilFinished/1000;

                        if (tiempoDeJuego<10 ){
                            timer.setTextColor(getResources().getColor(R.color.rojo));
                            timer.setText(String.valueOf(tiempoDeJuego));
                        }else{
                            timer.setTextColor(getResources().getColor(R.color.verde));
                            timer.setText(String.valueOf(tiempoDeJuego));
                        }
                        if (aciertos==idDrawable.length){
                            tiempoDeJuego  = millisUntilFinished/1000;
                            timer.setTextColor(getResources().getColor(R.color.black));
                            timer.setText(String.valueOf(tiempoDeJuego));
                            scoreBoard.setText(String.valueOf(puntuacion));
                            enviarResultado.setVisibility(View.VISIBLE);
                            enviarResultado.setText("Enviar");
                            enviarResultado.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(MainActivity.this, comit.class);
                                    startActivity(intent);

                                }
                            });
                            cancel();


                        }

                    }
                    public void onFinish() {
                        for( int nn = 0; nn<idArray.length; nn++) {
                            carta[nn] = (ImageView) findViewById(idArray[nn]);
                            carta[nn].setEnabled(false);
                        }
                        enviarResultado.setVisibility(View.VISIBLE);
                        enviarResultado.setText("Enviar");
                        enviarResultado.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(MainActivity.this, comit.class);
                                startActivity(intent);

                            }
                        });

                    }
                }.start();


                for( int nn = 0; nn<idArray.length; nn++){
                    carta[nn] = (ImageView)findViewById(idArray[nn]);
                    carta[nn].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View viewMiCarta) {
                            // viewMiCarta es la carta (el ImageView) sobre la que estoy pulsando

                            int posEnArray = damePos(viewMiCarta.getId());
                            //Toast.makeText(getApplicationContext(), String.valueOf(posEnArray), Toast.LENGTH_SHORT ).show();
                            // Toast.makeText(getApplicationContext(), String.valueOf(viewMiCarta.getId()), Toast.LENGTH_SHORT ).show();

                            if (cartasDestapadas() < 2) {
                                // atención aquí a los paréntesis
                                ((ImageView) viewMiCarta).setImageResource(posicionesPartida[posEnArray]);
                                destapada[posEnArray] = 1;

                                if (imagenAnteriorDestapada == posicionesPartida[posEnArray] && cartasDestapadas() ==2) {
                                    new CountDownTimer(2000, 1000) {
                                        public void onTick(long millisUntilFinished) {


                                        }
                                        public void onFinish() {
                                            ((ImageView) viewMiCarta).setImageResource(R.drawable._acierto_);
                                            //imagenViewAnteriorDestapada.setImageResource(R.drawable._acierto_);
                                            ((ImageView) viewMiCarta).setEnabled(false);
                                            puntuacion = puntuacion+5;
                                            aciertos++;
                                            scoreBoard.setTextColor(getResources().getColor(R.color.verde));
                                            scoreBoard.setText(String.valueOf(puntuacion));
                                        }
                                    }.start();

                                    //((ImageView) viewMiCarta).setImageResource(R.drawable._acierto_);
                                    imagenViewAnteriorDestapada.setImageResource(R.drawable._acierto_);
                                    imagenViewAnteriorDestapada.setEnabled(false);
                                    destapada[posEnArray] = 2;
                                    destapada[posicionAnteriorDestapada] = 2;

                                } else if(cartasDestapadas()==2&&imagenAnteriorDestapada != posicionesPartida[posEnArray]){

                                    new CountDownTimer(2000, 2000) {
                                        public void onTick(long millisUntilFinished) {


                                        }
                                        public void onFinish() {
                                            ((ImageView) viewMiCarta).setImageResource(R.drawable.rever00);
                                            //imagenViewAnteriorDestapada.setImageResource(R.drawable._acierto_);

                                            puntuacion--;
                                            scoreBoard.setTextColor(getResources().getColor(R.color.rojo));
                                            scoreBoard.setText(String.valueOf(puntuacion));
                                        }

                                    }.start();

                                            imagenViewAnteriorDestapada.setImageResource(R.drawable.rever00);

                                    //((ImageView) viewMiCarta).setImageResource(R.drawable._acierto_);


                                    destapada[posEnArray] = 2;
                                    destapada[posicionAnteriorDestapada] = 2;
                                    //carta[posEnArray].setImageResource(R.drawable.rever00);
                                    for(int n = 0; n < idArray.length; n++) {

                                        if (destapada[n] == 1) {

                                            carta[n] = (ImageView) findViewById(idArray[n]);    // accedo a cada objeto ImageView mediante su id previamente almacenado en idArray[]
                                            //carta[n].setImageResource(idDrawable[resultado.get(n)]);      // establezco en cada ImageView (carta[n]) la imagen del reverso
                                            destapada[n] = 0;
                                        }
                                    }

                                }

                                // almaceno el id de la imagen destapada. En este caso se refiere a la foto, es decir al valor que está mostrando
                                imagenAnteriorDestapada = posicionesPartida[posEnArray];
                                // almaceno la posición que estaba destapada
                                posicionAnteriorDestapada = posEnArray;
                                // almaceno el ImageView que estaba destapado... creo que esto lo puedo simplificar
                                imagenViewAnteriorDestapada = (ImageView) viewMiCarta;
                            }

                            else {      // si no hay menos de dos cartas destapadas se inicia() la baraja. Se refiere a q se tapan todas las cartas

                            }
                        }
                    });     // Cierre del método .setOnClickListener()
                }

                iniZERO();
                inicia();
            }
        });








    }

    // INICIO FUNCIONES USUARIO


    private void barajea(int longitud){


        for (int i =0; i< longitud*2;i++){
            resultado.add(i % longitud);
        }
        Collections.shuffle(resultado);
        System.out.println(Arrays.toString(resultado.toArray()));


        for(int j = 0; j < idDrawable.length*2; j++) {
            posicionesPartida[j] = idDrawable[resultado.get(j)];
        }

    }

    /*
        Función que devuelve la posición de la carta pulsada
        Se compara el id del ImageView recibido con cada uno de los almacenados
        en el array idArray
     */
    private int damePos(int pIdObjeto) {
        int ii = 0;
        while (pIdObjeto != idArray[ii++]);     // doy por hecho que encuentro el id... si no DESBORDAMIENTO
        return ii-1;
    }

    /*
        devuelve el número de cartas destapadas
     */
    private int cartasDestapadas(){
        int contador = 0;
        for(int ii = 0; ii < destapada.length; ii++){
            if (destapada[ii] ==1) contador++;
        }
        return contador;
    }

    /*
        En realidad inica o reinicia las cartas del reverso
     */
    private void inicia(){
        for(int n = 0; n < idArray.length; n++) {

            if (destapada[n] == 1) {

                carta[n] = (ImageView) findViewById(idArray[n]);    // accedo a cada objeto ImageView mediante su id previamente almacenado en idArray[]
                carta[n].setImageResource(idDrawable[resultado.get(n)]);      // establezco en cada ImageView (carta[n]) la imagen del reverso
                destapada[n] = 0;
            }
        }
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < idArray.length; i++){
                    carta[i].setImageResource(R.drawable.rever00);
                }
            }
        },1500);
    }

    /*
        Pone el array destapada todos a 1
        Como el layout está construido con las imágenes de las cartas destapadas... primero relleno la matriz de estado con 1
        para que la función inicia() las tape (cargando la imagen del corcho)
     */
    private void iniZERO(){
        for(int n = 0; n < 20; n++) destapada[n] = 1;
    }

}   // Cierre de la clase MainActivity