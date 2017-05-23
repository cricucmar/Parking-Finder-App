package com.cricumcar.parkinfinderprogramacion;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RatingBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.cricumcar.parkinfinderprogramacion.capa_de_datos.Parqueadero;
import com.cricumcar.parkinfinderprogramacion.capa_de_datos.base_de_datos.BaseDeDatosParqueaderos;
import com.cricumcar.parkinfinderprogramacion.clases.Capacidad;

public class MenuParqueadero extends AppCompatActivity {

    private Button agregarVehiculo, quitarVehiculo, ajustes, estadistica, irMapa,miCuenta;
    private Parqueadero parqueadero = null;
    private TextView nombreUsuario, nombreParqueadero, costoServicio, capacidadParqueadero;
    Switch encendido;

    BaseDeDatosParqueaderos db = new BaseDeDatosParqueaderos(this);

    @Override
    protected void onResume() {
        super.onResume();
        if(db.existeParqueadero(getIntent().getExtras().getString("nombreUsuario"))){
            parqueadero = db.dameParqueadero(getIntent().getExtras().getString("nombreUsuario"));
            actualizaInforamcion();
        }else{
            finish();
        }

    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder saliendo = new AlertDialog.Builder(this);
        saliendo.setMessage("¿SEGURO QUE QUIERES SALIR?").setIcon(R.drawable.ajustes)
                .setTitle("SALIENDO DE "+parqueadero.getNombre().toUpperCase())
                .setPositiveButton("SI",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String mensaje = db.informarCambiosCapacidad(parqueadero.getCapacidad(), parqueadero.getPropietario());
                        Toast.makeText(getApplicationContext(),mensaje,Toast.LENGTH_LONG).show();
                        finish();
                    }
                });
        saliendo.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // termian esta ventana
                dialog.cancel();
            }
        });

        AlertDialog dialog = saliendo.create();
        dialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_parqueadero);

        enlazarObjetos();

        parqueadero = db.dameParqueadero(getIntent().getExtras().getString("nombreUsuario"));

        actualizaInforamcion();

//-----------------------------------------BOTONES ---------------------------------------------------------------------

        ajustes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MenuParqueadero.this,MenuConfigurar.class);
                intent.putExtra("nombreUsuario",parqueadero.getPropietario());
                startActivity(intent);
            }
        });

        agregarVehiculo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(parqueadero.getCapacidad().agregarVehiculo()){
                    agregarVehiculo.setEnabled(false);
                    String mensaje = db.informarCambiosCapacidad(parqueadero.getCapacidad(),parqueadero.getPropietario());
                    Toast.makeText(getApplicationContext(),mensaje,Toast.LENGTH_LONG).show();
                }
                quitarVehiculo.setEnabled(true);
                capacidadParqueadero.setText(parqueadero.getCapacidad().toString());
            }
        });

        quitarVehiculo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(parqueadero.getCapacidad().quitarVehiculo()){
                    quitarVehiculo.setEnabled(false);
                }
                if(parqueadero.getCapacidad().getCapacidadMaxima() == parqueadero.getCapacidad().getTotalEspacioOcupado()+1) {
                    String mensaje = db.informarCambiosCapacidad(parqueadero.getCapacidad(), parqueadero.getPropietario());
                    Toast.makeText(getApplicationContext(),mensaje,Toast.LENGTH_LONG).show();
                }

                agregarVehiculo.setEnabled(true);
                capacidadParqueadero.setText(parqueadero.getCapacidad().toString());
            }
        });


        irMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MenuParqueadero.this,MenuMapa.class);
                startActivity(intent);
            }
        });

        miCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MenuParqueadero.this,MiCuenta.class);
                intent.putExtra("nombreUsuario",parqueadero.getPropietario());
                startActivity(intent);
            }
        });

        estadistica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verMisEstadisticas();
            }
        });

        encendido.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String mensaje ="";
                if(isChecked){  // si se le enciende
                    parqueadero.setCapacidad(new Capacidad(parqueadero.getCapacidad().getCapacidadMaxima(),0));
                    capacidadParqueadero.setText(parqueadero.getCapacidad().toString());
                    mensaje = db.informarCambiosCapacidad(parqueadero.getCapacidad(),parqueadero.getPropietario());
                    Toast.makeText(getApplicationContext(),mensaje,Toast.LENGTH_LONG).show();
                    comprobarActividad();
                }else{ // se se le apaga
                    if(parqueadero.getCapacidad().getTotalEspacioOcupado()==0){
                        mensaje = db.informarCambiosCapacidad(new Capacidad(parqueadero.getCapacidad().getCapacidadMaxima(),parqueadero.getCapacidad().getCapacidadMaxima()+1),parqueadero.getPropietario());
                        informarQueEstaApagado();
                        Toast.makeText(getApplicationContext(),mensaje,Toast.LENGTH_LONG).show();
                    }else{
                        encendido.setChecked(true);
                        Toast.makeText(getApplicationContext(),"AUN HAY VEHICULOS ESTACIONADOS",Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
//-----------------------------------------------FIN DE BOTONES--------------------------------------------------------

    }

    private void enlazarObjetos(){

        agregarVehiculo = (Button) findViewById(R.id.agregarP);
        quitarVehiculo = (Button) findViewById(R.id.quitarP);
        ajustes = (Button) findViewById(R.id.ajustesP);
        estadistica = (Button) findViewById(R.id.estadisticasP);
        irMapa = (Button) findViewById(R.id.verMapa2);
        miCuenta = (Button) findViewById(R.id.miCuentaP);
        nombreUsuario = (TextView) findViewById(R.id.nombreUsuario);
        nombreParqueadero = (TextView) findViewById(R.id.nombreParqueadero);
        costoServicio = (TextView) findViewById(R.id.costoServicio);
        capacidadParqueadero = (TextView) findViewById(R.id.capacidadParqueadero);
        encendido = (Switch) findViewById(R.id.activo);

    }

    private void comprobarActividad(){


        ajustes.setEnabled(false);

        if (parqueadero.getCapacidad().hayEspacioDisponible())
            agregarVehiculo.setEnabled(true);

        if (parqueadero.getCapacidad().getTotalEspacioOcupado() != 0)
            quitarVehiculo.setEnabled(true);

    }

    private void informarQueEstaApagado(){

        irMapa.setEnabled(true);
        agregarVehiculo.setEnabled(false);
        quitarVehiculo.setEnabled(false);
        ajustes.setEnabled(true);
        capacidadParqueadero.setText("EL PARQUEADERO NO ESTA EN FUNCIONAMIENTO\n" +
                "Cuando estes listo ACTIVA tu servicio");
    }

    private void actualizaInforamcion(){
        nombreUsuario.setText(parqueadero.getPropietario());
        nombreParqueadero.setText(parqueadero.getNombre());
        costoServicio.setText(parqueadero.getCostoServicio().toString());
        if(parqueadero.getCapacidad().estaActivo()){
            encendido.setChecked(true);
            capacidadParqueadero.setText(parqueadero.getCapacidad().toString());
            comprobarActividad();
        }else{
            informarQueEstaApagado();
        }
    }

    private void verMisEstadisticas(){

        AlertDialog.Builder prestigio = new AlertDialog.Builder(MenuParqueadero.this);

        View ventana = getLayoutInflater().inflate(R.layout.ventana_emergente_estadistica,null);

        // AQUI SE PROGRAMA COMO SI FUERA EL MAIN DE MI NUEVO LAYOUT
        TextView descripcion = (TextView) ventana.findViewById(R.id.descripcionE);

        final RatingBar calificacion = (RatingBar) ventana.findViewById(R.id.estrellitasE);

        calificacion.setEnabled(false);
        calificacion.setRating((float) parqueadero.getPuntajeServicio().getPuntaje());
        descripcion.setText(parqueadero.getPuntajeServicio().toString());

        prestigio.setView(ventana);
        final AlertDialog dialog2 = prestigio.create();
        dialog2.show();

    }
}
