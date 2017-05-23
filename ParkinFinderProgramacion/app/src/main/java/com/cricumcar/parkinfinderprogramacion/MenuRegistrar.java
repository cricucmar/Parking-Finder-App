package com.cricumcar.parkinfinderprogramacion;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cricumcar.parkinfinderprogramacion.capa_de_datos.Usuario;
import com.cricumcar.parkinfinderprogramacion.capa_de_datos.base_de_datos.BaseDeDatosParqueaderos;
import com.cricumcar.parkinfinderprogramacion.capa_de_datos.base_de_datos.BaseDeDatosUsuarios;
import com.cricumcar.parkinfinderprogramacion.capa_de_datos.servicios_web.Direcciones;
import com.cricumcar.parkinfinderprogramacion.capa_de_datos.servicios_web.Ubicacion;
import com.cricumcar.parkinfinderprogramacion.capa_de_datos.servicios_web.interfaces_para_la_web.ParaDireccion;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;

import static android.os.Build.VERSION_CODES.M;

public class MenuRegistrar extends AppCompatActivity implements ParaDireccion{

    private Button registrar,ubicar;
    private EditText usuario,nombreParqueadero,contrasena,verificarContrasena;
    private TextView latitud, longitud,direccion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_registrar);

        /* Referencia a los objetos*/
        registrar = (Button) findViewById(R.id.registrarR);
        ubicar = (Button) findViewById(R.id.ubicarR);
        usuario = (EditText) findViewById(R.id.usuarioR);
        nombreParqueadero = (EditText) findViewById(R.id.nombreParqueaderoR);
        contrasena = (EditText) findViewById(R.id.contrasenaR);
        verificarContrasena = (EditText) findViewById(R.id.verificarContrasenaR);
        latitud = (TextView) findViewById(R.id.latitudR);
        longitud = (TextView) findViewById(R.id.longitudR);
        direccion = (TextView) findViewById(R.id.direccionR);

        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                BaseDeDatosUsuarios db = new BaseDeDatosUsuarios(getApplicationContext());

                if(usuario.getText().toString().isEmpty() || nombreParqueadero.getText().toString().isEmpty() ||
                        contrasena.getText().toString().isEmpty() || verificarContrasena.getText().toString().isEmpty()||
                        latitud.getText().toString().isEmpty() || longitud.getText().toString().isEmpty()){

                    // muestra un mensaje rapido que informa la restriccion
                    Toast.makeText(getApplicationContext(),"TODOS LOS CAMPOS SON IMPORTANTES",Toast.LENGTH_SHORT).show();

                }else if(!contrasena.getText().toString().equals(verificarContrasena.getText().toString())){

                    // En caso de que las contraseñas no coicidan
                    Toast.makeText(getApplicationContext(),"LA CONTRASEÑA NO CONINCIDE",Toast.LENGTH_SHORT).show();

                } else {

                    LatLng ubicacion = new LatLng(Double.parseDouble(latitud.getText().toString()),Double.parseDouble(longitud.getText().toString()));
                    Usuario nuevoUsuario = new Usuario(usuario.getText().toString(),contrasena.getText().toString(),nombreParqueadero.getText().toString(),ubicacion);

                    String mensaje = db.crearUsuario(nuevoUsuario,getApplicationContext());

                    Toast.makeText(getApplicationContext(),mensaje,Toast.LENGTH_SHORT).show();

                    if (!mensaje.equals("ALGO ANDA MAL") && !mensaje.equals("ALGO ESTA MAL VERIFICA TU CONEXION") && !mensaje.equals("USUARIO YA EXISTENTE")){
                        Intent intent = new Intent(MenuRegistrar.this,MenuParqueadero.class);
                        //intent.putExtra("usuario", nuevoUsuario);
                        intent.putExtra("nombreUsuario",nuevoUsuario.getUsuario());

                        startActivity(intent);
                        finish();
                    }
                }
            }
        });

        ubicar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Ubicacion ubicar = new Ubicacion(getApplicationContext());
                LatLng latLng = ubicar.obtenerLocalizacion();

                if(latLng!=null) {
                    completarDireccion(latLng);
                }
            }
        });
    }

    private void completarDireccion(LatLng coordenadas){
        latitud.setText(String.valueOf(coordenadas.latitude));
        longitud.setText(String.valueOf(coordenadas.longitude));
        // CREA EL NUEVO HILO
        new Direcciones(MenuRegistrar.this).execute(coordenadas);
    }


    // respuesta a la particion de segundo plano
    @Override
    public void respuestaDireccion(String resultado) {
        direccion.setText(resultado);
    }
}
