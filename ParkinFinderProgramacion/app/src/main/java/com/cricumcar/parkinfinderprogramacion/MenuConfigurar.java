package com.cricumcar.parkinfinderprogramacion;

import android.opengl.EGLSurface;
import android.renderscript.Double2;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cricumcar.parkinfinderprogramacion.capa_de_datos.base_de_datos.BaseDeDatosParqueaderos;
import com.cricumcar.parkinfinderprogramacion.clases.CostoServicio;

public class MenuConfigurar extends AppCompatActivity {

    private Button omitir, actualizar;
    private EditText nuevoCosto,nuevoTiempo,nuevaCapadidad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_configurar);

        omitir = (Button) findViewById(R.id.omitirC);
        actualizar = (Button) findViewById(R.id.guardarC);
        nuevaCapadidad = (EditText) findViewById(R.id.nuevaCapacidad);
        nuevoCosto = (EditText) findViewById(R.id.nuevoCosto);
        nuevoTiempo = (EditText) findViewById(R.id.nuevoTiempo);

        omitir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        actualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nuevoCosto.getText().toString().isEmpty() || nuevaCapadidad.getText().toString().isEmpty()
                        || nuevoTiempo.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"TODO LOS CAMPOS SON NECESARIOS",Toast.LENGTH_LONG).show();
                }else{
                    BaseDeDatosParqueaderos db = new BaseDeDatosParqueaderos(getApplicationContext());
                    String mensaje = db.mejorarParqueadero(new CostoServicio(Integer.parseInt(nuevoCosto.getText().toString()),Integer.parseInt(nuevoTiempo.getText().toString())),
                            Integer.parseInt(nuevaCapadidad.getText().toString()),getIntent().getExtras().getString("nombreUsuario"));
                    Toast.makeText(getApplicationContext(),mensaje,Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        });
    }
}
