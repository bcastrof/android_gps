package com.example.bruno.ilernaauto_bruno_castro_v2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class EleccionVehiculo extends AppCompatActivity {
    private  List<CheckBox> checkBoxList;
    private SeekBar rangoPrecio;
    TextView precioElegido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eleccion_vehiculo);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        enlaceInterfaz();

    }

    private void enlaceInterfaz(){

        //checkboxes
        final int[] rChk = {
                R.id.chk_renault,
                R.id.chk_seat,
                R.id.chk_hyundai,
                R.id.chk_skoda,
                R.id.chk_opel,
                R.id.chk_bmw,
                R.id.chk_vw,
                R.id.chk_tata,
                R.id.chk_lotus,
                R.id.chk_mazda
        };

        checkBoxList = new ArrayList<CheckBox>();

        for (int id : rChk) {
            CheckBox checkBox = findViewById(id);
            checkBoxList.add(checkBox);
        }

        //seekBar
        /*
         * enlaze de los elementos con el XML*/
        rangoPrecio = findViewById(R.id.rangoPrecio);
        precioElegido = findViewById(R.id.lab_precio);
        /*
         * cambiamos el texto de la etiqueta por el valor maximo del seekbar y el simbolo €*/
        precioElegido.setText( " €");
        /*
         * ponemos a la escucha el seekbar*/
        rangoPrecio.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    /*variable que va alamacenar el valor de desplazamiento del seekbar*/
                    int valor_progreso;

                    @Override
                    /*
                     * metodo que detecta los cambios en el seekbar, en el hemos definido que:
                     * el valor_progreso va a contener el valor del desplazamiento el cual estara
                     * disponible para futuros usos.
                     * cambiamos el contenido de precioElegido por el valor que va devolviendo el
                     * seekbar*/
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        valor_progreso = progress;
                        precioElegido.setText(progress + " €");
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    /*
                     * cuando el seekbar deja de tener movimiento este metodo nos puede transferir su
                     * valor a la variable que eleijamos siempre y cuando este disponible.*/
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        precioElegido.setText(valor_progreso + " €" );
                    }
                }
        );

    }

    public void onPause() {
        super.onPause();
        SharedPreferences datos = PreferenceManager.getDefaultSharedPreferences(this); //contexto en el que me encuetro
        SharedPreferences.Editor miEditor = datos.edit();
        for (int i = 0; i < checkBoxList.size(); i++) {

            if (checkBoxList.get(i).isChecked()) {
                miEditor.putBoolean("checked" + i, true);
            } else {
                miEditor.putBoolean("checked" + i, false);
            }
        }
        int mProgress = rangoPrecio.getProgress();
        miEditor.putInt("precioElegido", mProgress);
        miEditor.apply();
    }

    public void onResume() {
        super.onResume();
        SharedPreferences datos = PreferenceManager.getDefaultSharedPreferences(this); //contexto en el que me encuetro
        boolean checkBoxValue;
        for (int i = 0; i < checkBoxList.size(); i++) {
            checkBoxValue = datos.getBoolean("checked" + i, false);
            checkBoxList.get(i).setChecked(checkBoxValue);
        }
        int mProgress = datos.getInt("precioElegido", 0);
        rangoPrecio.setProgress(mProgress);
    }

    public void cambioCompraVenta(View view) {

        Intent intent = new Intent (getApplicationContext(), CompraVenta.class);
        startActivity(intent);

    }
}
