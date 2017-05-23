package com.cricumcar.parkinfinderprogramacion;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cricumcar.parkinfinderprogramacion.capa_de_datos.Parqueadero;
import com.cricumcar.parkinfinderprogramacion.capa_de_datos.Usuario;
import com.cricumcar.parkinfinderprogramacion.capa_de_datos.base_de_datos.BaseDeDatosParqueaderos;
import com.cricumcar.parkinfinderprogramacion.capa_de_datos.base_de_datos.BaseDeDatosUsuarios;

public class MenuIniciarSesion extends AppCompatActivity {

    // Atributos privados de tipo Button
    private Button iniciarSesion,irRegistrar;
    private EditText usuario, contrasena;

    @Override
    protected void onPostResume() {
        super.onPostResume();
        usuario.setText("");
        contrasena.setText("");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        /* CREA LA ESTRUCTURA DE LA VENTANA Y CARGA UN
        CONTENIDO --> LA ACTIVIDAD MENU INICIAR SESION*/

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_iniciar_sesion);


        /* SE ENLAZAN LOS ATRIBUTOS DE ESTA CLASE
        MEDIANTE UN CASTING A OBJETOS DE UNA ACTIVIDAD*/

        iniciarSesion = (Button) findViewById(R.id.iniciarSecionIS);
        irRegistrar = (Button) findViewById(R.id.sinCuencaIS);
        usuario = (EditText) findViewById(R.id.usuarioIS);
        contrasena = (EditText) findViewById(R.id.contrasenaIS);


        iniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(usuario.getText().toString().isEmpty() || contrasena.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"COMPLETE LOS CAMPOS DE ACCESO", Toast.LENGTH_SHORT).show();
                }else{
                    BaseDeDatosUsuarios db = new BaseDeDatosUsuarios(getApplicationContext());
                    if (db.estaRegistrado(usuario.getText().toString(),contrasena.getText().toString())){
                        Intent intent=new Intent(MenuIniciarSesion.this,MenuParqueadero.class);
                        intent.putExtra("nombreUsuario",usuario.getText().toString());
                        startActivity(intent);
                    }else{
                        Toast.makeText(getApplicationContext(),"VERIFIQUE SU AUTENTICIDAD", Toast.LENGTH_SHORT).show();
                    }
                }

                Intent intent = new Intent(MenuIniciarSesion.this,MenuParqueadero.class);
            }
        });


        irRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* APERTURA DE UNA NUEVA ACTIVIDAD DESDE ESTA CLASE*/
                Intent intent = new Intent(MenuIniciarSesion.this,MenuRegistrar.class);
                startActivity(intent);
            }
        });


    }
}