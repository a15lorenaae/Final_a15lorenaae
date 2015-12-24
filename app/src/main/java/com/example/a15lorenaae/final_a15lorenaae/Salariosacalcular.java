package com.example.a15lorenaae.final_a15lorenaae;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class Salariosacalcular extends AppCompatActivity {
    public static enum TIPOREDE{MOBIL,ETHERNET,WIFI,SENREDE};
    private TIPOREDE conexion;
    Button btndescargar;
    File rutaArquivo;
    File rutaArquivo1;
    Thread thread;
    BaseDatos bd;
    Button btnmostrar;
    TextView textoxml;
    Button btnpasarbdafile;
    Button btndescargar2;
    String xml2 = "http://manuais.iessanclemente.net/images/5/53/Salaries.xml2";
    String xml = "http://manuais.iessanclemente.net/images/5/53/Salaries.xml";
    Button btnmostrarsuper;
    private boolean complementos=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salariosacalcular);
        btnmostrarsuper=(Button)findViewById(R.id.btnsuper);
        textoxml = (TextView) findViewById(R.id.textoxml);
        btnmostrar = (Button) findViewById(R.id.btnprocesar);
        btndescargar = (Button) findViewById(R.id.btndescargar);
        btnpasarbdafile = (Button) findViewById(R.id.btnpasarbdadocumento);

        btnpasarbdafile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String nomeficheiro = "salarios";

                    File ruta = new File(getExternalFilesDir(null), nomeficheiro);
                    OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(ruta));
                    osw.write(textoxml.getText().toString());
                    osw.flush();
                    osw.close();
                    Log.i("SD", "Fichero creado en" + ruta);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        btnmostrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Salario> salarios = bd.obterSalarios();
                textoxml.setText("");
                textoxml.append("Total_Salary    Month \n");

                for (Salario salario : salarios) {
                    textoxml.append(salario.getTotal_salary() + "      " + salario.getMonth() + "\n");


                }


            }
        });

        btndescargar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                conexion = comprobarRede();
                if (conexion==TIPOREDE.SENREDE){
                    Toast.makeText(Salariosacalcular.this, "NON SE PODE FACER ESTA PRACTICA SEN CONEXION A INTERNET", Toast.LENGTH_LONG).show();
                    finish();
                }
                thread = new Thread() {

                    @Override
                    public void run() {
                        descargarArquivo();

                    }
                };
                thread.start();
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Toast.makeText(getApplicationContext(), "Acabouse a descarga", Toast.LENGTH_SHORT).show();
                try {
                    lerArquivo();

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                }


            }

        });
        btnmostrarsuper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                conexion = comprobarRede();
                if (conexion==TIPOREDE.SENREDE){
                    Toast.makeText(Salariosacalcular.this, "NON SE PODE FACER ESTA PRACTICA SEN CONEXION A INTERNET", Toast.LENGTH_LONG).show();
                    finish();
                }

                try {
                    lerArquivo2();
                    Log.i("Xml", "Procesado correctamente");
                    Toast.makeText(getApplicationContext(),"Pulsa en show salaries para ver os cambios",Toast.LENGTH_SHORT).show();

                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
    }
    private TIPOREDE comprobarRede(){
        NetworkInfo networkInfo=null;

        ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            switch(networkInfo.getType()){
                case ConnectivityManager.TYPE_MOBILE:
                    return TIPOREDE.MOBIL;
                case ConnectivityManager.TYPE_ETHERNET:
                    // ATENCION API LEVEL 13 PARA ESTA CONSTANTE
                    return TIPOREDE.ETHERNET;
                case ConnectivityManager.TYPE_WIFI:
                    // NON ESTEAS MOITO TEMPO CO WIFI POSTO
                    // MAIS INFORMACION EN http://www.avaate.org/
                    return TIPOREDE.WIFI;
            }
        }
        return TIPOREDE.SENREDE;
    }
    private void descargarArquivo() {
        URL url = null;

        try {
            url = new URL(xml);


        } catch (MalformedURLException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            return;
        }

        HttpURLConnection conn = null;
        String nomeArquivo = Uri.parse(xml).getLastPathSegment();

        File arquivo = new File(Environment.getExternalStorageDirectory().getAbsoluteFile() + File.separator + Final_a15lorenaae.textonombre.getText().toString());
        arquivo.mkdirs();
        rutaArquivo = new File(arquivo, nomeArquivo);

        try {

            conn = (HttpURLConnection) url.openConnection();

            conn.setReadTimeout(10000);     /* milliseconds */
            conn.setConnectTimeout(15000);  /* milliseconds */
            conn.setRequestMethod("POST");
            conn.setDoInput(true);                  /* Indicamos que a conexión vai recibir datos */

            conn.connect();

            int response = conn.getResponseCode();
            if (response != HttpURLConnection.HTTP_OK) {
                return;
            }
            OutputStream os = new FileOutputStream(rutaArquivo);
            InputStream in = conn.getInputStream();
            byte data[] = new byte[1024];   // Buffer a utilizar
            int count;
            while ((count = in.read(data)) != -1) {
                os.write(data, 0, count);
            }
            os.flush();
            os.close();
            in.close();
            conn.disconnect();
            Log.i("COMUNICACION", "ACABO");
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            Log.e("COMUNICACION", e.getMessage());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.e("COMUNICACION", e.getMessage());
        }

    }


    private void lerArquivo() throws IOException, XmlPullParserException {


        InputStream is = new FileInputStream(rutaArquivo);
        XmlPullParser parser = Xml.newPullParser();
        parser.setInput(is, "UTF-8");

        int evento = parser.nextTag();
        Salario salario = null;

        while (evento != XmlPullParser.END_DOCUMENT) {
            if (evento == XmlPullParser.START_TAG) {
                if (parser.getName().equals("salary")) {
                    salario = new Salario();
                    evento = parser.nextTag();
                    salario.setMonth(parser.nextText());
                    evento = parser.nextTag();
                    salario.setTotal_salary(Double.parseDouble(parser.nextText()));
                    evento = parser.nextTag();
                    salario.setTotal_salary(salario.getTotal_salary() + Double.parseDouble(parser.nextText()));
                    evento = parser.nextTag();
                    salario.setTotal_salary(salario.getTotal_salary() + Double.parseDouble(parser.nextText()));
                }
            }
            if (evento == XmlPullParser.END_TAG) {
                if (parser.getName().equals("salary")) {
                    try {
                        bd.engadirSalario(salario);
                        Log.i("Engadido", salario.toString());
                    } catch (Exception e) {
                    }
                }

            }
            evento = parser.next();
        }

        is.close();
    }
    private void lerArquivo2() throws IOException, XmlPullParserException {


        InputStream is =getAssets().open("salarios2.xml");
        XmlPullParser parser = Xml.newPullParser();
        parser.setInput(is, "UTF-8");

        int evento = parser.nextTag();
        Salario salario1 = null;

        while (evento != XmlPullParser.END_DOCUMENT) {
            if (evento == XmlPullParser.START_TAG) {
                if (parser.getName().equals("salary")) {
                    salario1 = new Salario();
                    evento = parser.nextTag();
                    salario1.setMonth(parser.nextText());
                    evento = parser.nextTag();
                    salario1.setTotal_salary(Double.parseDouble(parser.nextText()));
                    while (complementos) {
                        try {

                            evento = parser.nextTag();
                            salario1.setTotal_salary(salario1.getTotal_salary() + Double.parseDouble(parser.nextText()));

                        } catch (Exception e) {
                            complementos = false;
                        }
                    }
                    complementos = true;
                }
            }
            if (evento == XmlPullParser.END_TAG) {
                if (parser.getName().equals("salary")) {
                    try {
                        bd.engadirSalario(salario1);
                        Log.i("Engadido", salario1.toString());
                    } catch (Exception e) {
                    }
                }

            }
            evento = parser.next();
        }

        is.close();
    }


    @Override
    public void onStart() {
        super.onStart();
        if (bd == null) {
            bd = new BaseDatos(this);
            bd.sqlLiteDB = bd.getReadableDatabase();
            bd.sqlLiteDB = bd.getWritableDatabase();
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        if (bd != null) {    // Pechamos a base de datos.
            bd.close();
            bd = null;
        }
    }
}
