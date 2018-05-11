package com.example.bruno.ilernaauto_bruno_castro_v2.basedatos;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

        //clase que me permite la conexion con la base datos
public class ConexionBaseDatos extends SQLiteOpenHelper {

        //constructor de la base
    public ConexionBaseDatos(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
       //contexto en el que estamo, como se llama la base, version de nuestra basedatos
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //creacion de la tabla en el sqlite
        db.execSQL(TablasCampos.CREAR_TABLA_COCHES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //actualizacion version base
        db.execSQL("DROP TABLE IF EXISTS "+ TablasCampos.TABLA_COCHES);
    }
}
