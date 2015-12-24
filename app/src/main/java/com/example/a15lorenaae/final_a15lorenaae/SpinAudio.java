package com.example.a15lorenaae.final_a15lorenaae;

import android.content.DialogInterface;
import android.content.pm.InstrumentationInfo;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SpinAudio extends AppCompatActivity {
    Spinner spinaudios;
    Button btnreproducir;
    Button btnparar;
    Button btngravar;
    String audioseleccionado = "";
    private  MediaPlayer mediaplayer;
    MediaRecorder mediaRecorder;
    String arquivoGravar;
    Button btnrefrescar;
    boolean pause;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spin_audio);
        btnreproducir = (Button) findViewById(R.id.btnreproducir);
        btngravar = (Button) findViewById(R.id.btngravar);
        btnrefrescar = (Button) findViewById(R.id.btnrefrescar);
        btnrefrescar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> audios = new ArrayList<String>();
                ArrayAdapter<String> adaptador = new ArrayAdapter<String>(SpinAudio.this, android.R.layout.simple_spinner_dropdown_item, audios);
                String nomearquivo = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "AUDIO" + File.separator + Final_a15lorenaae.textonombre.getText().toString();
                final File arquivoaudio = new File(nomearquivo);
                File[] directorio = arquivoaudio.listFiles();
                for (int i = 0; i < directorio.length; i++) {
                    adaptador.add(directorio[i].getName());
                }
                spinaudios.setAdapter(adaptador);
            }
        });


        btnparar = (Button) findViewById(R.id.btnparar);
        btnparar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaplayer.stop();
                mediaplayer.release();
            }
        });
        String nomearquivo = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "AUDIO" + File.separator + Final_a15lorenaae.textonombre.getText().toString();
        final File arquivoaudio = new File(nomearquivo);
        arquivoaudio.mkdirs();
        btnreproducir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String audioreproducir = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "AUDIO" + File.separator + Final_a15lorenaae.textonombre.getText().toString() + File.separator + audioseleccionado;
                try {
                    mediaplayer=new MediaPlayer();
                    mediaplayer.reset();
                    mediaplayer.setDataSource(audioreproducir);
                    mediaplayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    mediaplayer.prepare();
                    mediaplayer.start();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(),
                            "ERRO:" + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
        btngravar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String timeStamp = DateFormat.getDateTimeInstance().format(
                        new Date()).replaceAll(":", "").replaceAll("/", "_")
                        .replaceAll(" ", "_");

                mediaRecorder = new MediaRecorder();
                arquivoGravar = Environment.getExternalStorageDirectory() + File.separator + "AUDIO" + File.separator + Final_a15lorenaae.textonombre.getText().toString() + File.separator + "record" + timeStamp + ".3gp";
                mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                mediaRecorder.setMaxDuration(15000);
                mediaRecorder.setAudioEncodingBitRate(32768);
                mediaRecorder.setAudioSamplingRate(8000); // No emulador só 8000 coma
                mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
                mediaRecorder.setOutputFile(arquivoGravar);
                Log.i("SD", "Aqui gardanse os audios" + arquivoGravar);
                try {
                    mediaRecorder.prepare();
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    mediaRecorder.reset();
                }
                mediaRecorder.start();
                abrirDialogo("GRAVAR");
            }
        });
        spinaudios = (Spinner) findViewById(R.id.spinaudios);
        ArrayList<String> audios = new ArrayList<String>();
        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, audios);
        File[] directorio = arquivoaudio.listFiles();
        for (int i = 0; i < directorio.length; i++) {
            adaptador.add(directorio[i].getName());
        }
        spinaudios.setAdapter(adaptador);
        spinaudios.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getBaseContext(), "Seleccionaches: " + ((TextView) view).getText(), Toast.LENGTH_LONG).show();
                ArrayAdapter<String> adaptador = (ArrayAdapter<String>) parent.getAdapter();
                audioseleccionado = adaptador.getItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void abrirDialogo(String tipo) {
        if (tipo == "GRAVAR") {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this)
                    .setMessage("GRAVANDO").setPositiveButton(
                            "PREME PARA PARAR",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    // TODO Auto-generated method stub
                                    mediaRecorder.stop();
                                    mediaRecorder.release();
                                    mediaRecorder = null;
                                }
                            });
            dialog.show();
        }

    }


    @Override
    protected void onPause() {
        super.onPause();
        if (mediaplayer.isPlaying()) {
            mediaplayer.pause();
            pause = true;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (pause) {
            mediaplayer.start();
            pause = false;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle estado) {
        estado.putBoolean("Pausado",pause);
        super.onSaveInstanceState(estado);

    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        savedInstanceState.putBoolean("Pausado", false);
        pause = savedInstanceState.getBoolean("Pausado");

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mediaplayer.isPlaying())mediaplayer.stop();
        if(mediaplayer!=null)mediaplayer.release();
        mediaplayer=null;
    }

}