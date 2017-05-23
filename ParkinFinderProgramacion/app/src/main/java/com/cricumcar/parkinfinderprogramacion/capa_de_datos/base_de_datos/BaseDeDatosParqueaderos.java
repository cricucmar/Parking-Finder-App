package com.cricumcar.parkinfinderprogramacion.capa_de_datos.base_de_datos;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.widget.Toast;

import com.cricumcar.parkinfinderprogramacion.R;
import com.cricumcar.parkinfinderprogramacion.capa_de_datos.Parqueadero;
import com.cricumcar.parkinfinderprogramacion.capa_de_datos.Usuario;
import com.cricumcar.parkinfinderprogramacion.clases.Capacidad;
import com.cricumcar.parkinfinderprogramacion.clases.CostoServicio;
import com.cricumcar.parkinfinderprogramacion.clases.PuntajeServicio;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class BaseDeDatosParqueaderos extends SQLiteOpenHelper{

    // String que representa mi base de Datos
    private String sqlTabla="CREATE TABLE datosParqueadero (usuario TEXT primary key, nombre TEXT," +
            " capacidadMaxima INTEGER, puestosOcup INTEGER, latitud DOUBLE, longitud DOUBLE, " +
            "costo INTEGER, tiempo INTEGER, puntaje DOUBLE, numVotantes INTEGER)";

    public BaseDeDatosParqueaderos(Context context){
        super(context,"parkingFinderBD2",null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(sqlTabla);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXIST datosParqueadero");
        db.execSQL(this.sqlTabla);
    }

    public String crearParqueadero(Usuario nuevoUsuario){

        String mensaje = "ALGO ANDA MAL";

        SQLiteDatabase database = this.getWritableDatabase(); // referecia a nuestra base de datos TIPO escritura
        if(database!= null){
            ContentValues contenedor = new ContentValues();
            contenedor.put("usuario",nuevoUsuario.getUsuario());                                                     // string
            contenedor.put("nombre",nuevoUsuario.getParqueadero().getNombre());                                                   // String
            contenedor.put("capacidadMaxima",nuevoUsuario.getParqueadero().getCapacidad().getCapacidadMaxima());     // int
            contenedor.put("puestosOcup",nuevoUsuario.getParqueadero().getCapacidad().getTotalEspacioOcupado());     // int
            contenedor.put("latitud",nuevoUsuario.getParqueadero().getUbicacion().latitude);                         // double
            contenedor.put("longitud",nuevoUsuario.getParqueadero().getUbicacion().longitude);                       // double
            contenedor.put("costo",nuevoUsuario.getParqueadero().getCostoServicio().getCosto());                     // int
            contenedor.put("tiempo",nuevoUsuario.getParqueadero().getCostoServicio().getTiempo());                   // int
            contenedor.put("puntaje",nuevoUsuario.getParqueadero().getPuntajeServicio().getPuntaje());               // double
            contenedor.put("numVotantes",nuevoUsuario.getParqueadero().getPuntajeServicio().getNumVotantes());       // int
            try{
                database.insertOrThrow("datosParqueadero",null,contenedor);
                mensaje="BIENVENIDO A: "+nuevoUsuario.getParqueadero().getNombre();
            }catch (SQLException e){                    // POR SI PASA ALGO
                mensaje="ALGO ANDA MAL";
            }
        }

        return mensaje;
    }

    public Parqueadero dameParqueadero(String usuario){

        SQLiteDatabase database = this.getWritableDatabase();

        Parqueadero miParqueadero = null;

        if(database!=null){
            String q = "SELECT * FROM datosParqueadero WHERE usuario='"+usuario+"'";  // BUSCA MI USUARIO
            Cursor registros = database.rawQuery(q,null); // ME DEVUELVE UN ENLACE o puntero

            if (registros.moveToFirst()){

                // instanciamos un parqueadero si esque tiene informacion que proporcionarme
                miParqueadero = new Parqueadero(registros.getString(0),registros.getString(1),
                new LatLng(registros.getDouble(4),registros.getDouble(5)));

                miParqueadero.setCapacidad(new Capacidad(registros.getInt(2),registros.getInt(3)));
                miParqueadero.setCostoServicio(new CostoServicio(registros.getInt(6),registros.getInt(7)));
                miParqueadero.setPuntajeServicio(new PuntajeServicio(registros.getDouble(8),registros.getInt(9)));

            }
        }
        return miParqueadero;
    }

    public String informarCambiosCapacidad(Capacidad cap,String usuario){
        String mensaje="ALGO OCURRIO EN LA BD";
        SQLiteDatabase database = this.getWritableDatabase(); // referecia a nuestra base de datos TIPO escritura
        if(database!= null){
            ContentValues registroModificado = new ContentValues();
            registroModificado.put("capacidadMaxima",cap.getCapacidadMaxima());                   // int
            registroModificado.put("puestosOcup",cap.getTotalEspacioOcupado());                        // int
            try{
                database.update("datosParqueadero",registroModificado,"usuario='"+usuario+"'",null);
                mensaje = "Se realizo un cambio en la BD";
            }catch (SQLException e){                    // POR SI PASA ALGO
                mensaje = "fallo al ultimo";
            }
        }
        database.close();
        return mensaje;
    }

    public String eliminarParqueadero(String usuario){
        String mensaje = "NO FUE POSIBLE ELIMIMINAR LA CUENTA";
        SQLiteDatabase database = this.getWritableDatabase(); // referecia a nuestra base de datos TIPO escritura
        if(database!= null) {
            try{
                database.delete("datosParqueadero", "usuario='"+usuario+"'", null);
                mensaje = "VUELVE PRONTO";
            }catch (SQLException e){
                return mensaje;
            }
        }
        return mensaje;
    }

    public String mejorarParqueadero(CostoServicio cost,int nuevaCapacida,String usuario){
        String mensaje="ALGO OCURRIO EN LA BD";
        SQLiteDatabase database = this.getWritableDatabase(); // referecia a nuestra base de datos TIPO escritura
        if(database!= null){
            ContentValues registroModificado = new ContentValues();
            registroModificado.put("capacidadMaxima",nuevaCapacida);                   // int
            registroModificado.put("costo",cost.getCosto());                   // int
            registroModificado.put("tiempo",cost.getTiempo());                   // int
            try{
                database.update("datosParqueadero",registroModificado,"usuario='"+usuario+"'",null);
                mensaje = "Se realizo un cambio en la BD";
            }catch (SQLException e){                    // POR SI PASA ALGO
                return mensaje;
            }
        }
        database.close();
        return mensaje;
    }

    public ArrayList<MarkerOptions> obtenerMarcadores(){

        ArrayList<MarkerOptions> marcadores= new ArrayList<>();

        SQLiteDatabase database = this.getWritableDatabase();

        if(database!=null){
            String q = "SELECT * FROM datosParqueadero";  // BUSCA MI USUARIO
            Cursor registros = database.rawQuery(q,null); // ME DEVUELVE UN ENLACE o puntero

            if(registros.moveToFirst()) {        // si es que encontro alguno
                for (registros.moveToFirst(); !registros.isAfterLast();registros.moveToNext()){
                    if(registros.getInt(2) != registros.getInt(3) && registros.getInt(2) != (registros.getInt(3)-1)){
                        LatLng posicion = new LatLng(registros.getDouble(4),registros.getDouble(5));
                        marcadores.add(new MarkerOptions().position(posicion).title(registros.getString(0))
                                .snippet(registros.getString(1))
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.parqueadero)));
                    }
                }
            }
        }
        return marcadores;
    }

    public String AgregarPuntuacion(PuntajeServicio nuevaPuntuacion,String usuario){

        // ALGORITMO DE PUNTUACION PILAS
        String mensajes="ALGO ESTA MAL VERIFICA TU CONEXION";
        SQLiteDatabase database = this.getWritableDatabase(); // referecia a nuestra base de datos TIPO escritura
        if(database!= null){
            ContentValues registroModificado = new ContentValues();
            registroModificado.put("puntaje",nuevaPuntuacion.getPuntaje());
            registroModificado.put("numVotantes",nuevaPuntuacion.getNumVotantes());
            try{
                database.update("datosParqueadero",registroModificado,"usuario='"+usuario+"'",null);
                mensajes="SE REALIZÓ LA CALIFICACIÓN";
            }catch (SQLException e){                                        // POR SI PASA ALGO
                mensajes="ALGO ESTA MAL VERIFICA TU CONEXION";
            }
        }
        database.close();
        return mensajes;
    }

    public boolean existeParqueadero(String usuario){
        SQLiteDatabase database = this.getWritableDatabase();
        if(database!=null){
            String q = "SELECT * FROM datosParqueadero WHERE usuario='"+usuario+"'";  // BUSCA MI USUARIO
            Cursor registros = database.rawQuery(q,null); // ME DEVUELVE UN ENLACE o puntero
            return registros.moveToFirst();
        }
        return false;
    }

}
