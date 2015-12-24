package com.example.a15lorenaae.final_a15lorenaae;

import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Camera;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
import java.io.IOException;

import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Environment;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Video extends Activity {
    boolean sdDisponhible = false;
    boolean sdAccesoEscritura = false;
    VideoView vervideo;
    String videoseleccionado = "";
    Spinner spinvideos;
    TextView textover;
    MediaController controller;
    Button btnRec;
    private String arquivoGravar;
    private String nomevideo="video.mp4";
    private final int REQUEST_CODE_GRAVACION_OK = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        controller = new MediaController(this);
        vervideo = (VideoView) findViewById(R.id.vervideo);
        btnRec = (Button) findViewById(R.id.btnrec);
        vervideo.setMediaController(controller);
        spinvideos = (Spinner) findViewById(R.id.spinvideos);
        textover = (TextView) findViewById(R.id.textovideo);
        comprobarEstadoSD();
        spinvideos=(Spinner)findViewById(R.id.spinvideos);


        btnRec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sdAccesoEscritura) {
                    File ruta = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES);
                    File arquivo = new File(ruta, nomevideo);

                    Intent intento = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                    intento.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(arquivo));

                    startActivityForResult(intento, REQUEST_CODE_GRAVACION_OK);
                    ArrayList<String> videos = new ArrayList<String>();
                    ArrayAdapter<String> adaptador = new ArrayAdapter<String>(Video.this, android.R.layout.simple_spinner_dropdown_item, videos);

                    File[] directorio = ruta.listFiles();
                    for (int i = 0; i < directorio.length; i++) {
                        adaptador.add(directorio[i].getName());
                    }
                    spinvideos.setAdapter(adaptador);
                    spinvideos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            Toast.makeText(getBaseContext(), "Seleccionaches: " + ((TextView) view).getText(), Toast.LENGTH_LONG).show();
                            ArrayAdapter<String> adaptador = (ArrayAdapter<String>) parent.getAdapter();
                            videoseleccionado = adaptador.getItem(position);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });


                } else {
                    Toast.makeText(Video.this, R.string.SD, Toast.LENGTH_SHORT).show();
                }

            }
        });



    }
    public void comprobarEstadoSD() {
        String estado = Environment.getExternalStorageState();
        Log.e("SD", estado);

        if (estado.equals(Environment.MEDIA_MOUNTED)) {
            sdDisponhible = true;
            sdAccesoEscritura = true;
        } else if (estado.equals(Environment.MEDIA_MOUNTED_READ_ONLY))
            sdDisponhible = true;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_GRAVACION_OK) {
            if (resultCode == RESULT_OK) {
                    File ruta = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES);


                File arquivo = new File(ruta,nomevideo);
                    if (!arquivo.exists()) return;
                    textover.setText("A ruta donde está o archivo é: "+arquivo);
                    Toast.makeText(Video.this,"Video stored in "+arquivo,Toast.LENGTH_SHORT).show();
                    VideoView vidview = (VideoView) findViewById(R.id.vervideo);
                    vidview.setVideoURI(Uri.fromFile(arquivo));
                    vidview.start();
                vidview.requestFocus();
                }

            } else if (resultCode == RESULT_CANCELED) {
                // Video ou Foto cancelada
            } else {
                // Fallo na captura do Video ou foto.
            }
        }
    }

