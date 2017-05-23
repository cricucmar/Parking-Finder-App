package com.cricumcar.parkinfinderprogramacion.clases;


public class PuntajeServicio {
    private  double puntaje;
    private  int numVotantes;

    public PuntajeServicio(double puntaje,int numVotantes){
        this.puntaje = puntaje;
        this.numVotantes = numVotantes;
    }


    public double getPuntaje() {
        return puntaje;
    }

    public void setPuntaje(double puntaje) {

        this.puntaje = (this.puntaje * numVotantes + puntaje)/++numVotantes;

    }

    public int getNumVotantes() {
        return numVotantes;
    }

    public void setNumVotantes(int numVotantes) {
        this.numVotantes = numVotantes;
    }

    @Override
    public String toString() {
        return "Este Parqueadero a sido calificado por un total de " + numVotantes +
                " Persona(s), obteniendo "+puntaje + " de 5 estrellas en el Ranking " +
                "de Parking Finder Application";
    }
}
