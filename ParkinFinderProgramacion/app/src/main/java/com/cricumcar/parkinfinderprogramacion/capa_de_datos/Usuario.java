package com.cricumcar.parkinfinderprogramacion.capa_de_datos;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

public class Usuario implements Serializable{

    private String usuario,contrasena;
    private Parqueadero parqueadero;

    public Usuario(String usuario, String contrasena, String nombreParqueadero, LatLng ubicacion) {
        this.usuario = usuario;
        this.contrasena = contrasena;
        this.parqueadero  = new Parqueadero(usuario,nombreParqueadero,ubicacion);
    }

    public String getUsuario() {
        return usuario;
    }

    public String getContrasena() {
        return contrasena;
    }

    public Parqueadero getParqueadero() {
        return parqueadero;
    }

    public void setParqueadero(Parqueadero parqueadero) {
        this.parqueadero = parqueadero;
    }
}
