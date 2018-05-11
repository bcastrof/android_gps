package com.example.bruno.ilernaauto_bruno_castro_v2.basedatos;

public class TablasCampos {
    //constates tabla coches, con las diferentes variables doy nombre a los diferentes campos de la tabla
    public static final String TABLA_COCHES = "coches";
    public static final String CAMPO_ID = "_id";
    public static final String CAMPO_MARCA = "marca";
    public static final String CAMPO_MODELO = "modelo";
    public static final String CAMPO_COMBUSTIBLE = "combustible";
    public static final String CAMPO_ANO = "ano";
    public static final String CAMPO_TIENDA = "tienda";

    //creación de la tabla coches utilizo las variables anterior y defino que tipo de campo va ser y tipo dato que va recibir

    public static final String CREAR_TABLA_COCHES = "CREATE TABLE " + "" + TABLA_COCHES + "("
            +CAMPO_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT,"
            +CAMPO_MARCA+ " TEXT,"
            +CAMPO_MODELO+ " TEXT,"
            +CAMPO_COMBUSTIBLE+ " TEXT,"
            +CAMPO_ANO+ " INTEGER,"
            +CAMPO_TIENDA+ " TEXT)";
}
