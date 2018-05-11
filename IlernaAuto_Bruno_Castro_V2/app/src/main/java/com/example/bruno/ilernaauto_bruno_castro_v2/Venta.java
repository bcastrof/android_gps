package com.example.bruno.ilernaauto_bruno_castro_v2;

import android.content.ContentValues;
import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.view.Menu;
import android.view.MenuItem;

import com.example.bruno.ilernaauto_bruno_castro_v2.basedatos.ConexionBaseDatos;
import com.example.bruno.ilernaauto_bruno_castro_v2.basedatos.TablasCampos;
import com.example.bruno.ilernaauto_bruno_castro_v2.entidades.Coche;

public class Venta extends AppCompatActivity {

   //instancio todos los elementosl que me hacen falta para coordinar la interfaz
    String tienda;
    TextView txtMarca;
    TextView txtModelo;
    RadioGroup groupCombus;
    RadioButton gasoil;
    RadioButton gasolina;
    String combustible;
    Spinner spyears;
    Button vender;
    Integer[] years = new Integer[29];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venta);
          android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        enlaceInterfaz();
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
    public void cambioComprar(View view) {
        Coche coche = new Coche(txtMarca.getText().toString(), txtModelo.getText().toString(),
        combustible, Integer.parseInt(spyears.getSelectedItem().toString()), tienda);

        if (insertarCoche(coche) == true) {
            Toast.makeText(getApplicationContext(), "Venta Registrada", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(getApplicationContext(), ComprarCoche.class);
            startActivity(intent);

        }
    }

    public void cambioLogin(View view) {
        Intent intent = new Intent(getApplicationContext(), LoginIlernaAuto.class);
        startActivity(intent);
    }

    public void enlaceInterfaz() {
       //creo las dos estancias de control para los edittext y radioButton
        ControlText controlText = new ControlText();
        ControlRadio controlRadio = new ControlRadio();

        //recojo el valor de la tienda que me envia la actividad compraVenta
        tienda = (getIntent().getExtras().getString("tienda"));

        //enlazo los edittext con el xml y con el listener
        txtMarca = findViewById(R.id.edt_marca);
        txtMarca.addTextChangedListener(controlText);
        txtModelo = findViewById(R.id.edt_modelo);
        txtModelo.addTextChangedListener(controlText);

        //enlazo el grupo de radiobuttons con el xml y el listner
        groupCombus = findViewById(R.id.radioGroupCombus);
        groupCombus.setOnCheckedChangeListener(controlRadio);
        gasoil = findViewById(R.id.rd_diesel);
        gasolina = findViewById(R.id.rd_gasolina);

        //el boton venta con el xml
        vender = findViewById(R.id.bt_v_enviar);

        int j = 0;
        for (int i = 1990; i <= 2018; i++) {
            years[j] = i;
            j++;
        }
        //Creamos el array utilizando los valores del array creado anteriormente
        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, years);

        spyears = (Spinner) findViewById(R.id.sp_years);

        //Se lo asignamos al spinner
        spyears.setAdapter(adapter);

    }


    //metodo para insertar el coche que recibe un Objeto tipo Coche

    public boolean insertarCoche(Coche coche) {

       //creo la conexion con mi base datos
        ConexionBaseDatos conn = new ConexionBaseDatos(getApplicationContext(), "bd_coches", null, 1);

       //llamo el metodo de escritura en la base datos
        SQLiteDatabase db = conn.getWritableDatabase();


        try {
             //creo un contenedor de valores
            ContentValues values = new ContentValues();

            //lleno los diferentes campos de la tabla con la informacion que me da el Coche
            values.put(TablasCampos.CAMPO_MARCA, coche.getMarca());
            values.put(TablasCampos.CAMPO_MODELO, coche.getModelo());
            values.put(TablasCampos.CAMPO_COMBUSTIBLE, coche.getCombustible());
            values.put(TablasCampos.CAMPO_ANO, coche.getAno());
            values.put(TablasCampos.CAMPO_TIENDA, coche.getTienda());

           //hago la insercion
            Long insert = db.insert(TablasCampos.TABLA_COCHES, TablasCampos.CAMPO_ID, values);

            db.close();
        } catch (SQLException e) {
            return false;
        }
        return true;
    }

    //clase que me creo para controlar los editText
    public class ControlText implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        //solo utilizo este metodo porque siempre va a saltar mientras se cambie el texto de los editText
        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            //evaluo la condicion
            if (!txtMarca.getText().toString().equals("") && !txtModelo.getText().toString().equals("")
            && (gasoil.isChecked() || gasolina.isChecked())) {
               //si es ok enseño el button
                vender.setVisibility(View.VISIBLE);
            } else {
               //sino lo es lo vuelvo a ocultar
                vender.setVisibility(View.INVISIBLE);
            }
        }
        @Override
        public void afterTextChanged(Editable editable) {
        }
    }

    //clase que controla los radio button
    public class ControlRadio implements RadioGroup.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {

           //checkedId utilizo esta variable para los diferentes casos, utilizando lal id de recurso
            switch (checkedId) {

                case R.id.rd_diesel: {
                   // si esta checkeado combustible cambia su valor
                    combustible = "Diesel";
                    //evaluo la condicion
                    if (gasoil.isChecked() && !txtMarca.getText().toString().equals("") &&
                    !txtModelo.getText().toString().equals("")) {
                        //si es correcto enseño button

                        vender.setVisibility(View.VISIBLE);
                    }else{
                        //si no lo es lo oculto
                        vender.setVisibility(View.INVISIBLE);
                    }
                    System.out.println(combustible);
                    break;
                }

                //lo mismo que el anterior
                case R.id.rd_gasolina:{
                    combustible = "Gasolina";
                    if (gasolina.isChecked() && !txtMarca.getText().toString().equals("") && !txtModelo.getText().toString().equals("") ) {

                        vender.setVisibility(View.VISIBLE);
                    }else{
                        vender.setVisibility(View.INVISIBLE);
                    }
                    break;
                }
            }
        }
    }
}