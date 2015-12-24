package com.example.a15lorenaae.final_a15lorenaae;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by a15lorenaae on 12/14/15.
 */
public class BaseDatos extends SQLiteOpenHelper {
    public static SQLiteDatabase sqlLiteDB;
    public final static String NOME_BD="salaries.db";
    public static int VERSION_BD=1;
    private  final String CONSULTAR_SALARIO="Select month,Total_Salary from SALARIOS order by month";
    private String CREAR_TABOA_SALARIOS="CREATE TABLE SALARIOS ("+"month VARCHAR(50) PRIMARY KEY,"+"Total_Salary REAL NOT NULL)";
    private final String TABOA_SALARIOS="SALARIOS";

    public BaseDatos(Context contex) {
        super(contex, NOME_BD, null, VERSION_BD);
    }
    public void engadirSalario(Salario salario_engadir){
        sqlLiteDB.execSQL("INSERT INTO SALARIOS (month, Total_Salary) VALUES ('"+salario_engadir.getMonth()+"','"+salario_engadir.getTotal_salary()+"')");



    }
    public ArrayList<Salario> obterSalarios() {
        ArrayList<Salario> salarios_devolver = new ArrayList<Salario>();

        Cursor datosConsulta = sqlLiteDB.rawQuery(CONSULTAR_SALARIO, null);
        if (datosConsulta.moveToFirst()) {
            Salario salario;
            while (!datosConsulta.isAfterLast()) {
                salario = new Salario( datosConsulta.getString(0),datosConsulta.getDouble(1));
                salarios_devolver.add(salario);
                datosConsulta.moveToNext();
            }
        }

        return salarios_devolver;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREAR_TABOA_SALARIOS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
db.execSQL("DROP TABLE IF EXISTS SALARIO");
        onCreate(db);
    }
}
