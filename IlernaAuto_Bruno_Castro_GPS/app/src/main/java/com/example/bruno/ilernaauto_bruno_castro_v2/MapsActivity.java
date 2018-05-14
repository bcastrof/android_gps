package com.example.bruno.ilernaauto_bruno_castro_v2;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.example.bruno.ilernaauto_bruno_castro_v2.entidades.Localizacion;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Marker marcador;
    private String mensaje;
    //creo un array de localizacion para almacenar las dir de las diferentes marcas
    private List<Localizacion> localizaciones = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mapFragment.getView().setClickable(true);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMinZoomPreference(4.0f);
        mMap.setMaxZoomPreference(20.0f);


        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)).title("Mi marca").position(latLng));

                DireccionSeleccionada buscarDireccion = new DireccionSeleccionada(getApplicationContext());
                buscarDireccion.execute("" + latLng.latitude, "" + latLng.longitude);   // Parámetros que recibe doInBackground
            }
        });
    }

    /*
     * Con este metodo lo que se pretende es que se puedan activar los servicios del gps, cuando este
     * está apago.
     */
    public void inicioLocalizacion() {
        //Objeto de la clase LocationManager que recoge los servicios activados en el contexto
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //Variable que controla si el gps está activado
        final boolean gpsDesactivado = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        //Condicion de control caso el gps este desactivado
        if (!gpsDesactivado) {
            //si lo esta, con las dos lineas siguiente lo que hacemos es abrir los ajustes de ubicacion
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }
    }

    //Variable de control para que en caso de error sepa a que actividad tiene que retornar
    private static int PETICION_PERMISO_LOCALIZACION = 101;

    //Como obtener nuestra ubicacion actual
    private void miUbicacion() {
        //Evaluo si tengo los servicios activados, de no ser asi devuelvo el usuario a la actividad principal
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PETICION_PERMISO_LOCALIZACION);
            return;
        } else {
            mMap.setMyLocationEnabled(true);
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(new Criteria(), true));
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1200, 0, locListener);
        }

    }

    /*
     * Metodo que controla los cambios del gps
     */ LocationListener locListener = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {
            //si hay un cambio actualizo mi ubicacion pasando las nuevas coordenadas
            miUbicacion();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {
            //saco un toast diciendo que el gps esta activado
            mensaje = ("gps activado");
            Mensaje();
        }

        @Override
        public void onProviderDisabled(String provider) {
            //saco un toast diciendo que el gps esta desactivado
            mensaje = ("gps desactivado");
            Mensaje();
            inicioLocalizacion();
        }
    };

    //metodo que saca el toast
    public void Mensaje() {
        Toast toast = Toast.makeText(this, mensaje, Toast.LENGTH_LONG);
        toast.show();
    }

    /*
     * Class que nos va permitir añadir diferentes marcas a nuestro mapa y recoger las direcciones
     * de dichas marcas, a su vez tambien gestiona la conexion con los servicios de google
     */
    public class DireccionSeleccionada extends AsyncTask<String, Integer, String> {
        //Instancio el contexto, que a su vez es un elemento de esta clase
        Context context;
        //Instancio la clase localizacion
        Localizacion localizacion;

        //constructor de la clase que tiene como parametros el contexto
        DireccionSeleccionada(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... params) {

            String ficheroJSON;

            /* Monto la cadena con la latitud y longitud que les paso y me construyo el String completo
             * como primer parametro paso la direccion de servios de goolgle. parms 0 y 1 es donde
             * estaran almacenadas las coordenadas del gps.
             * http://maps.googleapis.com/maps/api/geocode/json?latlng=
             * https://maps.googleapis.com/maps/api/geocode/json?address=
             */

            String cadena = "https://maps.googleapis.com/maps/api/geocode/json?key=AIzaSyD_qlHyEEq59ENRkzes6J-NIk6VXFZQ7uI&";
            cadena = cadena + "&latlng=";
            cadena = cadena + params[0];
            cadena = cadena + ",";
            cadena = cadena + params[1];
            cadena = cadena + "&sensor=false";

            //recogo el Json que me devuelve la url
            ficheroJSON = getFicheroJSON(cadena);

            Log.i("TAG", ficheroJSON);
            //retorno el fichero
            return ficheroJSON;
        }

        @Override
        protected void onPostExecute(String cadena) {
            try {
                //evaluo que la cadena no esta vacia
                if (!cadena.equals("")) {
                    //Creo el Json
                    JSONObject jsonObject = new JSONObject(cadena);

                    //Recojo longitud y latitud del Json

                    double latitud = ((JSONArray) jsonObject.get("results")).getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getDouble("lat");

                    double longitud = ((JSONArray) jsonObject.get("results")).getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getDouble("lng");

                    //almaceno la direccion seleccionada la cual extraigo del Json
                    String direccionSeleccionada = ((JSONArray) jsonObject.get("results")).getJSONObject(0).getString("formatted_address");

                    //saco un toast con la direccion seleccionada donde tambien muestro las coordenadas
                    Toast.makeText(context, "Dirección seleccionada: " + direccionSeleccionada + "\nLatitud: " + latitud + "\nLongitud: " + longitud, Toast.LENGTH_SHORT).show();

                    try {
                        /*
                         * Con la instancia de esta clase lo que pretendemos es obtener una direccion
                         * de donde hemos colocado la marca.
                         */
                        Geocoder geocoder = new Geocoder(context);

                        //creo una lista de Address en la que voy almacenar un unico resultado
                        List<Address> list = geocoder.getFromLocation(latitud, longitud, 1);
                        //monto el objeto de la clase localizacion la provincia la obtengo del adress
                        localizacion = new Localizacion(list.get(0).getSubAdminArea(), latitud, longitud);
                        //añado el objeto a un array donde guardando asi todas las posiciones que marque
                        localizaciones.add(localizacion);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    //bucle para ver si estoy obteniendo el resultado deseado
                    for (int i = 0; i < localizaciones.size(); i++) {
                        System.out.println(localizaciones.get(i).getDireccion() + " " + localizaciones.get(i).getLatitud() + " " + localizaciones.get(i).getLongitud());
                    }
                } else {
                    Toast.makeText(context, "Ups ha habido un problema", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        public String getFicheroJSON(String URLparaJSON) {
            URL url;
            String response = "";
            try {
                url = new URL(URLparaJSON);
                Log.i("TAG", URLparaJSON);
                HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
                conexion.setReadTimeout(15000);
                conexion.setConnectTimeout(15000);
                conexion.setRequestMethod("GET");
                conexion.setDoInput(true);
                conexion.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conexion.setDoOutput(true);
                int responseCode = conexion.getResponseCode();
                Log.i("TAG", responseCode + "");
                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    String line;
                    BufferedReader br = new BufferedReader(new InputStreamReader(conexion.getInputStream()));
                    while ((line = br.readLine()) != null) {
                        response += line;
                    }
                } else {
                    response = "";
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.i("TAG", response);
            return response;
        }
    }
}
