package com.cricumcar.parkinfinderprogramacion;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.cricumcar.parkinfinderprogramacion.capa_de_datos.Parqueadero;
import com.cricumcar.parkinfinderprogramacion.capa_de_datos.base_de_datos.BaseDeDatosParqueaderos;
import com.cricumcar.parkinfinderprogramacion.capa_de_datos.servicios_web.Direcciones;
import com.cricumcar.parkinfinderprogramacion.capa_de_datos.servicios_web.ManageGoogleRoutes;
import com.cricumcar.parkinfinderprogramacion.capa_de_datos.servicios_web.Ubicacion;
import com.cricumcar.parkinfinderprogramacion.capa_de_datos.servicios_web.interfaces_para_la_web.OnTaskCompleted;
import com.cricumcar.parkinfinderprogramacion.capa_de_datos.servicios_web.interfaces_para_la_web.ParaDireccion;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;


public class MenuMapa extends FragmentActivity implements OnMapReadyCallback, OnTaskCompleted, ParaDireccion {

    private GoogleMap mMap;

    private String partida, llegada;

    private Button irServicio, dameRuta;
    private TextView descripcion;

    private BaseDeDatosParqueaderos db = new BaseDeDatosParqueaderos(this);
    private Parqueadero parqueadero = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_mapa);

        alistarMapa();

        irServicio = (Button) findViewById(R.id.irServicio);
        dameRuta = (Button) findViewById(R.id.dameRuta);
        descripcion = (TextView) findViewById(R.id.descripcionM);

        irServicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (parqueadero == null) {
                    Toast.makeText(getApplicationContext(), "NO HAS DEFINIDO UN PARQUEADERO", Toast.LENGTH_LONG).show();
                } else {

                    Ubicacion ubicacion = new Ubicacion(getApplicationContext());
                    LatLng ac = ubicacion.obtenerLocalizacion();
                        // CONDICION DE APROXIMACION DE 100 metros
                        Boolean primeraCondicion = Math.abs((Math.abs(ac.latitude) - Math.abs(parqueadero.getUbicacion().latitude))) < 0.0001;
                        Boolean segundaCondicion = Math.abs((Math.abs(ac.longitude) - Math.abs(parqueadero.getUbicacion().longitude))) < 0.0001;
                    if(primeraCondicion && segundaCondicion){
                        Intent intent = new Intent(MenuMapa.this, MenuUsoParqueadero.class);
                        intent.putExtra("parqueadero", parqueadero.getPropietario());
                        startActivity(intent);
                        finish();
                    }else {
                        Toast.makeText(getApplicationContext(), "ESTAS MUY LEJOS DEL PARQUEADERO", Toast.LENGTH_LONG).show();
                        Toast.makeText(getApplicationContext(),String.valueOf(Math.abs((Math.abs(ac.latitude) - Math.abs(parqueadero.getUbicacion().latitude)))), Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        dameRuta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (parqueadero == null) {
                    Toast.makeText(getApplicationContext(), "NO HAS DEFINIDO UN PARQUEADERO", Toast.LENGTH_LONG).show();
                }else{
                    mMap.clear();
                    alistarMapa();
                    agregarMarcadores();
                    ubicarEnMapa();
                    partida = "";
                    llegada = "";
                    new Direcciones(MenuMapa.this).execute(parqueadero.getUbicacion());

                }
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        ubicarEnMapa();
        agregarMarcadores();

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                CameraUpdate miMarcador = CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 15);
                mMap.animateCamera(miMarcador);
                parqueadero = db.dameParqueadero(marker.getTitle());
                actualizarDescripcion();
                return false;
            }
        });

    }

    private void ubicarEnMapa() {
        Ubicacion ubicacion = new Ubicacion(this);
        CameraUpdate miUbicacion = CameraUpdateFactory.newLatLngZoom(ubicacion.obtenerLocalizacion(), 15);
        mMap.animateCamera(miUbicacion);
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
    }

    private void agregarMarcadores(){

        ArrayList<MarkerOptions> marcadores = db.obtenerMarcadores();

        Iterator<MarkerOptions> pee = marcadores.iterator();
        while(pee.hasNext()){
            mMap.addMarker(pee.next());
            pee.remove();
        }
    }


    private void actualizarDescripcion(){
        descripcion.setText(parqueadero.getDescripcion());
    }

    private void alistarMapa(){
        // Obtiene el SupportMapFragment y ser notificado cuando el mapa está listo para ser utilizado.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(MenuMapa.this);
    }

    private void drawRoutes(List<List<HashMap<String, String>>> result) {
        LatLng center = null;
        ArrayList<LatLng> points = null;
        PolylineOptions lineOptions = null;

        // recorriendo todas las rutas
        for(int i=0;i<result.size();i++){
            points = new ArrayList<LatLng>();
            lineOptions = new PolylineOptions();

            // Obteniendo el detalle de la ruta
            List<HashMap<String, String>> path = result.get(i);

            // Obteniendo todos los puntos y/o coordenadas de la ruta
            for(int j=0;j<path.size();j++){
                HashMap<String,String> point = path.get(j);

                double lat = Double.parseDouble(point.get("lat"));
                double lng = Double.parseDouble(point.get("lng"));
                LatLng position = new LatLng(lat, lng);

                if (center == null) {
                    //Obtengo la 1ra coordenada para centrar el mapa en la misma.
                    center = new LatLng(lat, lng);
                }
                points.add(position);
            }

            // Agregamos todos los puntos en la ruta al objeto LineOptions
            lineOptions.addAll(points);
            //Definimos el grosor de las Polilíneas
            lineOptions.width(4);
            //Definimos el color de la Polilíneas
            lineOptions.color(Color.BLUE);
        }

        // Dibujamos las Polilineas en el Google Map para cada ruta
        mMap.addPolyline(lineOptions);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(center, 13));
    }

    @Override
    public void onTaskCompleted(List<List<HashMap<String, String>>> listLatLong) {
        if (listLatLong != null && listLatLong.size() > 0) {
            // Dibuja directo en el mapa
            drawRoutes(listLatLong);
        } else {
            Toast.makeText(this, "Ruta no Identificada", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void respuestaDireccion(String direccion) {
        if(direccion.isEmpty() || direccion.equals("Cargando...")){
            Toast.makeText(getApplicationContext(),direccion,Toast.LENGTH_LONG).show();
        }else{
            if (llegada.isEmpty()){
                llegada = direccion;
                Ubicacion ubicacion = new Ubicacion(this);
                new Direcciones(MenuMapa.this).execute(ubicacion.obtenerLocalizacion());
            }else{
                partida = direccion;
                new ManageGoogleRoutes(MenuMapa.this).execute(partida, llegada);
            }
        }
    }
}
