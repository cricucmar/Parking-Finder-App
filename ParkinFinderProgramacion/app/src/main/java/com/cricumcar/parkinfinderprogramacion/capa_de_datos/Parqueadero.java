package com.cricumcar.parkinfinderprogramacion.capa_de_datos;


import com.cricumcar.parkinfinderprogramacion.clases.Capacidad;
import com.cricumcar.parkinfinderprogramacion.clases.CostoServicio;
import com.cricumcar.parkinfinderprogramacion.clases.PuntajeServicio;
import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;


public class Parqueadero implements Serializable{

    private String propietario, nombre;
    private LatLng ubicacion;
    private CostoServicio costo;
    private Capacidad capacidad;
    private PuntajeServicio puntajeServicio;

    public Parqueadero(String propietario, String nombre, LatLng ubicacion) {
        this.propietario = propietario;
        this.nombre = nombre;
        this.ubicacion = ubicacion;
        this.costo = new CostoServicio(0,0);
        this.capacidad = new Capacidad(0,1);
        this.puntajeServicio = new PuntajeServicio(5,1);
    }


    public String getDescripcion(){
        return "PROPIETARIO: " + propietario + "\nCOSTO DE SERVICIO: " + costo.toString() +
                "\nPUNTUACION: " + puntajeServicio.getPuntaje() + " de 5 estrellas";
    }

    public String getPropietario() {
        return propietario;
    }

    public String getNombre() {
        return nombre;
    }

    public LatLng getUbicacion() {
        return ubicacion;
    }

    public CostoServicio getCostoServicio() {return costo;}

    public void setCostoServicio(CostoServicio costo) {
        this.costo = costo;
    }

    public Capacidad getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(Capacidad capacidad) {
        this.capacidad = capacidad;
    }

    public PuntajeServicio getPuntajeServicio() {
        return puntajeServicio;
    }

    public void setPuntajeServicio(PuntajeServicio puntajeServicio) {
        this.puntajeServicio = puntajeServicio;
    }

}