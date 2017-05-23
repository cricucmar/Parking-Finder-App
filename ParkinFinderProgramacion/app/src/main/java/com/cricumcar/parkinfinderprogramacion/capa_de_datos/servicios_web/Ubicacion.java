package com.cricumcar.parkinfinderprogramacion.capa_de_datos.servicios_web;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.maps.model.LatLng;

public class Ubicacion implements LocationListener {

    private Context contexto;
    LocationManager localizador;
    String proveedor;
    private boolean networkOn;
    private LatLng latLng=null;

    public Ubicacion(Context contexto) {
        this.contexto = contexto;
        localizador = (LocationManager) contexto.getSystemService(Context.LOCATION_SERVICE);
        proveedor = LocationManager.NETWORK_PROVIDER;
        networkOn = localizador.isProviderEnabled(proveedor); // esta funcionando??
        if (ActivityCompat.checkSelfPermission(this.contexto, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.contexto, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
           return;
        }
        localizador.requestLocationUpdates(proveedor, 1000, 1, this);
        //obtenerLocalizacion();
    }

    public LatLng obtenerLocalizacion() {
        if (networkOn) {
            if (ActivityCompat.checkSelfPermission(this.contexto, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.contexto, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return latLng;
            }
            Location localiza = localizador.getLastKnownLocation(proveedor);
            if(localiza!=null){
                latLng = new LatLng(localiza.getLatitude(),localiza.getLongitude());
            }
        }
        return latLng;
    }

    @Override
    public void onLocationChanged(Location location) {
        obtenerLocalizacion();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) { }

    @Override
    public void onProviderEnabled(String provider) { }

    @Override
    public void onProviderDisabled(String provider) { }
}
