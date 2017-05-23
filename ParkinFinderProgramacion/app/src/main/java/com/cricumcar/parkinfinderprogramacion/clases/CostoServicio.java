package com.cricumcar.parkinfinderprogramacion.clases;

public class CostoServicio {

    private int costo, tiempo;

    public CostoServicio(int costo, int tiempo){
        this.costo = costo;
        this.tiempo = tiempo;
    }

    public int getCosto() {
        return costo;
    }

    public void setCosto(int costo) {
        this.costo = costo;
    }

    public int getTiempo() {
        return tiempo;
    }

    public void setTiempo(int tiempo) {
        this.tiempo = tiempo;
    }

    @Override
    public String toString() {
        return costo+" ctvs por " + tiempo + " minutos";
    }
}
