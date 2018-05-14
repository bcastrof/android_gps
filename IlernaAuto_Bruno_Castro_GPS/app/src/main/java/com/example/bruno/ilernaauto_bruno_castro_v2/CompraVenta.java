package com.example.bruno.ilernaauto_bruno_castro_v2;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

public class CompraVenta extends AppCompatActivity {
    Spinner tiendas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compra_venta);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

         enlaceInterfaz();
    }

    public void enlaceInterfaz(){

        ImageView slider;
        AnimationDrawable animacion;


        //enlace del contenedor de la imagen con el XML
        slider = findViewById(R.id.slider);

        /*
         * fijamos el fondo al contenedor de imagen establiciendo la imagen inicial,
         * usando como recurso el XML que contiene nuestras imagenes
         * */
        slider.setBackgroundResource(R.drawable.cambio_imagen);

        /*
         * Casteamos el componente animacion, asegurando que realmente es un AnimationDrawable y
         * recuperamos la imagen que previamente hemos cargado.
         */
        animacion = (AnimationDrawable) slider.getBackground();

        //arrancamos nuestra animacion
        animacion.start();

        /*Enlace con el spinner con el XML, configuracion del spinner recogida de la pagina:
         * https://developer.android.com/guide/topics/ui/controls/spinner.html?hl=es-419*/
        tiendas = findViewById(R.id.sp_tiendas);

        /*creamos un arrays de secuencia de caracteres, el cual va contener la lista de tiendas
         * que hemos creado previamente en archivo strings.xml*/
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.lista_tiendas, android.R.layout.simple_spinner_item);

        /*especificamos el diseño que se usara cuando aparezca la lista de opciones*/
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        /*enlazamos los valores del array con el spinner tiendas.*/
        tiendas.setAdapter(adapter);
    }

    //introduccion del menu, si lo queremos quitar borrar hasta el siguiente comentario

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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


    public void cambioComprar(View view) {

        Intent intent = new Intent(getApplicationContext(), ComprarCoche.class);
        startActivity(intent);
    }

    public void cambioVenta(View view) {

        Intent intent = new Intent(getApplicationContext(), Venta.class);
       //envio el valor de tienda
        intent.putExtra("tienda",tiendas.getSelectedItem().toString());
        startActivity(intent);
    }

    public void mostrarMapa(View view) {

        Intent intent = new Intent(getApplicationContext(), MapsActivity.class);

        startActivity(intent);

    }
}
