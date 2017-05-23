package com.cricumcar.parkinfinderprogramacion;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cricumcar.parkinfinderprogramacion.capa_de_datos.base_de_datos.BaseDeDatosUsuarios;

public class MiCuenta extends AppCompatActivity {

    private Button guardarCambios, salirSinCambios,eliminarCuenta;
    private EditText contA,contN,contN2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mi_cuenta);

        guardarCambios = (Button) findViewById(R.id.guardarMC);
        salirSinCambios = (Button) findViewById(R.id.regresarMC);
        eliminarCuenta = (Button) findViewById(R.id.eliminarMC);
        contA = (EditText)findViewById(R.id.contAntMC);
        contN = (EditText) findViewById(R.id.contNueMC);
        contN2 = (EditText) findViewById(R.id.confConMC);

        final BaseDeDatosUsuarios db = new BaseDeDatosUsuarios(this);

        guardarCambios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mensaje;
                if(contA.getText().toString().isEmpty() || contN.getText().toString().isEmpty() || contN2.getText().toString().isEmpty()){
                    mensaje = "TODOS LOS CAMPOS SON NECESARIOS";
                }else if (!contN.getText().toString().equals(contN2.getText().toString())){
                    mensaje = "LA NUEVA CONTRASEÑA NO COINCIDEN";
                }else if(!db.estaRegistrado(getIntent().getExtras().getString("nombreUsuario"),contA.getText().toString())) {
                    mensaje = "VERIFIQUE LA CONTRASEÑA ANTERIOR";
                }else{
                    mensaje = db.modificarContrasena(getIntent().getExtras().getString("nombreUsuario"),contN.getText().toString());
                }
                Toast.makeText(getApplicationContext(),mensaje,Toast.LENGTH_LONG).show();
                if(mensaje.equals("Contraseña Actualizada")) finish();
            }
        });

        salirSinCambios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        eliminarCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder saliendo = new AlertDialog.Builder(MiCuenta.this);
                saliendo.setMessage("¿SEGURO QUIERES ELIMINAR TU CUENTA?").setIcon(R.drawable.ajustes)
                        .setTitle("CUENTA DE: "+ getIntent().getExtras().getString("nombreUsuario"))
                        .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String mensaje = "";
                                if(contA.getText().toString().isEmpty()){
                                    mensaje = "SE REQUIERE COMPLETAR EL CAMPO CONTRASEÑA ANTERIOR";
                                }else if(!db.estaRegistrado(getIntent().getExtras().getString("nombreUsuario"),contA.getText().toString())){
                                    mensaje = "CONTRASEÑA INVALIDA";
                                }else{
                                    mensaje = db.eliminarCuenta(getIntent().getExtras().getString("nombreUsuario"),getApplicationContext());
                                }

                                Toast.makeText(getApplicationContext(),mensaje,Toast.LENGTH_LONG).show();

                                if (mensaje.equals("VUELVE PRONTO"))
                                    finish();
                            }
                        });
                saliendo.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // termina esta ventana
                        dialog.cancel();
                    }
                });

                AlertDialog dialog = saliendo.create();
                dialog.show();
            }
        });
    }
}
