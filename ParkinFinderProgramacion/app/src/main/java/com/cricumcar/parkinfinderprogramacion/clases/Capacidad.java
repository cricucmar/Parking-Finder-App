package com.cricumcar.parkinfinderprogramacion.clases;


public class Capacidad {

    private int capacidadMaxima,totalEspacioOcupado;
    private boolean espacioDisponible;

    public Capacidad (int capacidadMaxima, int totalEspacioOcupado){
        this.capacidadMaxima = capacidadMaxima;
        this.totalEspacioOcupado = totalEspacioOcupado;
        this.espacioDisponible = hayEspacioDisponible();
    }

    public int getCapacidadMaxima() {
        return capacidadMaxima;
    }

    public void setCapacidadMaxima(int capacidadMaxima) {
        this.capacidadMaxima = capacidadMaxima;
    }

    public int getTotalEspacioOcupado() {
        return totalEspacioOcupado;
    }

    public void setTotalEspacioOcupado(int totalEspacioOcupado) {
        this.totalEspacioOcupado = totalEspacioOcupado;
    }

    public boolean isEspacioDisponible() {
        return espacioDisponible;
    }

    public void setEspacioDisponible(boolean espacioDisponible) {
        this.espacioDisponible = espacioDisponible;
    }

    public boolean hayEspacioDisponible(){
        return !(capacidadMaxima == totalEspacioOcupado);
    }

    public boolean estaActivo(){ return capacidadMaxima!=(totalEspacioOcupado-1); }

    // devuelve true si ya se ha agregado al ultimo vehiculo
    public boolean agregarVehiculo(){
        return (capacidadMaxima == ++totalEspacioOcupado);
    }
    public boolean quitarVehiculo(){
        return (0 == --totalEspacioOcupado);
    }

    @Override
    public String toString() {
        return totalEspacioOcupado + " de "+ capacidadMaxima + " Vehículos";
    }
}
