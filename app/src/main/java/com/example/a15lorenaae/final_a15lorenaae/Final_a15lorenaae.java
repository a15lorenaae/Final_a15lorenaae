package com.example.a15lorenaae.final_a15lorenaae;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Final_a15lorenaae extends AppCompatActivity {
    Button btnchamar;
    EditText textotelefono;
    static EditText textonombre;
    SharedPreferences preferencias;
    SharedPreferences.Editor editor;
    Button btnAudio;
    Button btnSalario;
    Button btnvideo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_a15lorenaae);
        btnchamar=(Button)findViewById(R.id.btnchamar);
        textotelefono=(EditText)findViewById(R.id.textotelefono);
        textonombre=(EditText)findViewById(R.id.textonome);
        btnAudio=(Button)findViewById(R.id.btnAudio );
        btnSalario=(Button)findViewById(R.id.btnSalario);
        btnvideo=(Button)findViewById(R.id.btnVideo);
        btnvideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent(Final_a15lorenaae.this,Video.class);
                startActivity(in);
            }
        });
        btnSalario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(textonombre.getText().toString().equals("")){
                    Toast.makeText(Final_a15lorenaae.this,R.string.ComprobarNome,Toast.LENGTH_SHORT).show();
                }
                else{
                    Intent intent=new Intent(Final_a15lorenaae.this,Salariosacalcular.class);
                    startActivity(intent);

                }
            }
        });
        btnAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(textonombre.getText().toString().equals("")){
                    Toast.makeText(Final_a15lorenaae.this,R.string.ComprobarNome,Toast.LENGTH_SHORT).show();
                }
                else{
                    Intent intent=new Intent(Final_a15lorenaae.this,SpinAudio.class);
                    startActivity(intent);

                }
            }
        });
        btnchamar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (textotelefono.getText().toString().equals("")) {
                    Toast.makeText(Final_a15lorenaae.this, R.string.Comprobartelf, Toast.LENGTH_SHORT).show();
                } else {
                    Intent i = new Intent(Intent.ACTION_CALL, Uri.parse("tel:(+34)" + textotelefono.getText().toString()));
                    startActivity(i);

                }
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menucolores, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menucolorazul:
                preferencias=getSharedPreferences("Color",MODE_PRIVATE);
               editor=preferencias.edit();
                editor.putString("Color","Azul");
                editor.commit();
                aplicarpreferencias();
                return true;

            case R.id.menucolorverde:
                preferencias=getSharedPreferences("Color",MODE_PRIVATE);
              editor=preferencias.edit();
                editor.putString("Color","Verde");
                editor.commit();
                aplicarpreferencias();
                return true;
            case R.id.menucolorrojo:
                preferencias=getSharedPreferences("Color",MODE_PRIVATE);
                editor=preferencias.edit();
                editor.putString("Color","Rojo");
                editor.commit();
                aplicarpreferencias();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    public void aplicarpreferencias(){
         preferencias=getSharedPreferences("Color",MODE_PRIVATE);
        String color=preferencias.getString("Color","Azul");

        if(color.equals("Azul")){
            textonombre.setTextColor(getResources().getColor(R.color.azul));

        }
        if(color.equals("Verde")){
            textonombre.setTextColor(getResources().getColor(R.color.verde));
        }
        if(color.equals("Rojo")){
            textonombre.setTextColor(getResources().getColor(R.color.Rojo));
        }
    }
    @Override
    protected void onResume(){
        super.onResume();

        aplicarpreferencias();

    }
}
