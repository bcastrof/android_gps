package com.example.bruno.ilernaauto_bruno_castro_v2;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.bruno.ilernaauto_bruno_castro_v2.adaptadores.AdaptadorComprarCoche;
import com.example.bruno.ilernaauto_bruno_castro_v2.basedatos.ConexionBaseDatos;
import com.example.bruno.ilernaauto_bruno_castro_v2.basedatos.TablasCampos;
import com.example.bruno.ilernaauto_bruno_castro_v2.entidades.Coche;

import java.util.ArrayList;

public class ComprarCoche extends AppCompatActivity {
    ArrayList<Coche> listaCoches;
    RecyclerView recyclerViewComprarCoche;
    ConexionBaseDatos conn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comprar_coche);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //establezco la conexion con la base datos
        conn = new ConexionBaseDatos(getApplicationContext(), "bd_coches", null, 1);

        //array que voy a poblar con los datos de la consulta a la base datos
        listaCoches = new ArrayList<>();

        // llenarListaCoches();

        //enlazo el recicle view con el recicle del xml
        recyclerViewComprarCoche = (RecyclerView) findViewById(R.id.pruebaLista);

       //como doy por echo que mi tamaño es fijo lo establezco como tal
        recyclerViewComprarCoche.setHasFixedSize(true);

       //doto de funcion al recicleview
        recyclerViewComprarCoche.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        //invoco al metodo que hace la consulta a la tabla
        consultarListaCoches();

       //lamo al adptador de que transformara los datos del array en la vista cardView
        AdaptadorComprarCoche adpter = new AdaptadorComprarCoche(listaCoches);

         //cargo el adptador con el cardvie en el recicle
        recyclerViewComprarCoche.setAdapter(adpter);


    }

@Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ilerna_auto, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.preferencias) {

            Intent intent = new Intent(getApplicationContext(), EleccionVehiculo.class);
            startActivity(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void consultarListaCoches() {

        //invoco el metodo de lectura de la base datos
        SQLiteDatabase db = conn.getReadableDatabase();

       //objeto de la clase coche que utilizare para poblar el array
        Coche coche = null;

      //hago la consulta a la base datos
        Cursor cursor = db.rawQuery("SELECT * FROM " + TablasCampos.TABLA_COCHES, null);

       //me muevo por la diferentes tuplas de la tabla
        while (cursor.moveToNext()) {
             //recojo los valores de la base datos y los uso para contruir mi objeto Coche
            coche = new Coche();
            coche.setId(cursor.getInt(0));
            coche.setMarca(cursor.getString(1));
            coche.setModelo(cursor.getString(2));
            coche.setCombustible(cursor.getString(3));
            coche.setAno(cursor.getInt(4));
            coche.setTienda(cursor.getString(5));

            // añado coche a la lista
            listaCoches.add(coche);
        }
    }
        //datos de muestra para saber si el recicle se comportaba como es debido
    private void llenarListaCoches(){
        listaCoches.add(new Coche("opel","astra","diesel",2001,"vitoria-gasteiz"));
        listaCoches.add(new Coche("opel","astra","diesel",2001,"lleida"));
        listaCoches.add(new Coche("opel","astra","diesel",2001,"bilbao"));
    }

}