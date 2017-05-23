package com.cricumcar.parkinfinderprogramacion.capa_de_datos.servicios_web;

import android.os.AsyncTask;

import com.cricumcar.parkinfinderprogramacion.capa_de_datos.servicios_web.interfaces_para_la_web.ParaDireccion;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Direcciones extends AsyncTask<LatLng,Integer,String>{

    private ParaDireccion listener;

    // constructor
    public Direcciones(ParaDireccion listener){
        //Al instanciar la clase le pasamos un objeto que implemente la interfaz ParaDireccion
        //Para de esta manera poder regresar el control de este AsynTask a la Actividad Correspondiente
        this.listener=listener;
    }

    @Override
    protected String doInBackground(LatLng... params) {
        try {
            return dimeDireccion(params[0]);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    protected void onPreExecute() {
        // solo para darle al goce
        listener.respuestaDireccion("Cargando...");
    }

    @Override
    protected void onPostExecute(String result) {
        // accion en la clase peticionaria
        if(result != null){
            listener.respuestaDireccion(result);
        }
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onCancelled(String s) {
        super.onCancelled(s);
    }

    private String dimeDireccion(LatLng ubicacion) throws IOException, JSONException {

        String direccion = "";

        String cadena = "https://maps.googleapis.com/maps/api/geocode/json?latlng="+ubicacion.latitude+","+ubicacion.longitude+"&sensor=false";

        // Abrir la conexión hacia el servidor
        URL url = new URL(cadena);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        // Esta es una simple cabecera
        connection.setRequestProperty("contentType", "application/json");

        int respuesta = connection.getResponseCode();

        StringBuffer result = new StringBuffer();

        if (respuesta == HttpURLConnection.HTTP_OK){
            InputStream in = new BufferedInputStream(connection.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            String line;
            while ((line = reader.readLine()) != null){
                result.append(line);
            }

            // SE ALMACENA EL CONTENIDO DE LA URL
            JSONObject respuestaJSON = new JSONObject(result.toString());

            // Accedemos unicamente a la parte "results"
            JSONArray resultJSON = respuestaJSON.getJSONArray("results");

            if(resultJSON.length()>0){
                /* reducimos la busqueda al formato que queremos --> "formatted_address"
                ejemplo : si la coordenadas es -2.897610,-79.000969 --> "Mariano Cueva, Cuenca, Ecuador"
                */
                direccion = resultJSON.getJSONObject(0).getString("formatted_address");
            }

        }

        return direccion;
    }

}

