package com.cricumcar.parkinfinderprogramacion.capa_de_datos.base_de_datos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.cricumcar.parkinfinderprogramacion.capa_de_datos.Usuario;

public class BaseDeDatosUsuarios extends SQLiteOpenHelper {

    // String que representa mi base de Datos
    private String sqlTabla="CREATE TABLE datosUsuario (usuario TEXT primary key, contrasena TEXT, nombreParqueadero TEXT)";


    public BaseDeDatosUsuarios(Context context){
        super(context,"parkingFinderBD",null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(sqlTabla);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXIST datosUsuario");
        db.execSQL(this.sqlTabla);
    }

    public String crearUsuario(Usuario nuevoUsuario,Context context){

        String mensaje = "ALGO ANDA MAL";

        SQLiteDatabase database = this.getWritableDatabase(); // referecia a nuestra base de datos TIPO escritura

        if(database!= null){

            ContentValues contenedor = new ContentValues();

            contenedor.put("usuario",nuevoUsuario.getUsuario());                                    // string
            contenedor.put("contrasena",nuevoUsuario.getContrasena());                              // string
            contenedor.put("nombreParqueadero",nuevoUsuario.getParqueadero().getNombre());          // String

            try{
                database.insertOrThrow("datosUsuario",null,contenedor);
                BaseDeDatosParqueaderos db2 = new BaseDeDatosParqueaderos(context);
                mensaje=db2.crearParqueadero(nuevoUsuario);
            }catch (SQLException e){// POR SI PASA ALGO
                mensaje="USUARIO YA EXISTENTE";
            }
        }

        return mensaje;
    }

    public String modificarContrasena(String usuario, String contNueva){
        String mensaje = "Error de Identificacion";
        SQLiteDatabase database = this.getWritableDatabase(); // referecia a nuestra base de datos TIPO escritura
        if(database!= null){
            ContentValues registroModificado = new ContentValues();
            registroModificado.put("contrasena",contNueva);
            try{
                database.update("datosUsuario",registroModificado,"usuario='"+usuario+"'",null);
                mensaje = "Contraseña Actualizada";
            }catch (SQLException e){                    // POR SI PASA ALGO
                return mensaje;
            }
        }
        database.close();
        return mensaje;
    }

    public String eliminarCuenta(String usuario,Context context){
        String mensaje = "NO FUE POSIBLE ELIMIMINAR LA CUENTA";
            SQLiteDatabase database = this.getWritableDatabase(); // referecia a nuestra base de datos TIPO escritura
            if(database!= null) {
                try{
                    database.delete("datosUsuario", "usuario='"+usuario+"'", null);
                    BaseDeDatosParqueaderos db = new BaseDeDatosParqueaderos(context);
                    mensaje = db.eliminarParqueadero(usuario);
                }catch (SQLException e){
                    return mensaje;
                }
            }
        return mensaje;
    }

    public Boolean estaRegistrado(String usuario, String contrasena){
        SQLiteDatabase database = this.getWritableDatabase();
        if(database!=null){
            String q = "SELECT * FROM datosUsuario WHERE usuario='"+usuario+"'";  // BUSCA MI USUARIO
            Cursor registros = database.rawQuery(q,null); // ME DEVUELVE UN ENLACE o puntero
            if (registros.moveToFirst()){
                return registros.getString(1).equals(contrasena);
            }
        }
        return false;
    }
}