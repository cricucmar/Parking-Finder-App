package com.cricumcar.parkinfinderprogramacion;

import android.content.DialogInterface;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cricumcar.parkinfinderprogramacion.capa_de_datos.Parqueadero;
import com.cricumcar.parkinfinderprogramacion.capa_de_datos.base_de_datos.BaseDeDatosParqueaderos;

import static android.R.string.no;

public class MenuUsoParqueadero extends AppCompatActivity {

    private Parqueadero parqueadero = null;
    private BaseDeDatosParqueaderos db = new BaseDeDatosParqueaderos(this);
    private Button comenzar,terminar;
    private TextView nombre,descripcion;
    private Chronometer cronometro;
    private RatingBar estrellitas;

    private String parteLegal = "Este SERVICIO Forma parte de una RED COLABORADORA DE ESTACIONAMIENTOS" +
            " a través de PARKING FINDER APPLICATION, La cual no se hace responsable por acciones internas del " +
            " Servicio, ESPERAMOS que disfrutes tu estadía en este Parqueadero \n";

    @Override
    public void onBackPressed() {
        darPorTerminado();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_uso_parqueadero);

        comenzar = (Button) findViewById(R.id.comenzarUso);
        terminar = (Button) findViewById(R.id.terminarUso);
        nombre = (TextView) findViewById(R.id.nombreUso);
        descripcion = (TextView) findViewById(R.id.descripcionUso);
        cronometro = (Chronometer) findViewById(R.id.cronometro);
        estrellitas = (RatingBar) findViewById(R.id.estrellitasUso);

        parqueadero = db.dameParqueadero(getIntent().getExtras().getString("parqueadero"));
        actualizarInformacion();

        comenzar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comenzar.setEnabled(false);
                terminar.setEnabled(true);
                cronometro.setBase(SystemClock.elapsedRealtime());
                cronometro.start();
            }
        });

        terminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                darPorTerminado();
            }
        });
    }

    private void darPorTerminado(){
        String costo = cronometro.getText().toString();
        costo = costo.substring(0,costo.length()-3);
            int cd = (Integer.parseInt(costo)/parqueadero.getCostoServicio().getTiempo()+1)*parqueadero.getCostoServicio().getCosto();

        AlertDialog.Builder saliendo = new AlertDialog.Builder(MenuUsoParqueadero.this);
        saliendo.setMessage("¿SEGURO QUIERES FINALIZAR EL SERVICIO?").setIcon(R.drawable.ajustes)
                .setTitle("TOTAL A PAGAR: "+cd +" ctvs")
                .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        AlertDialog.Builder calificame = new AlertDialog.Builder(MenuUsoParqueadero.this);
                        View ventana = getLayoutInflater().inflate(R.layout.ventana_emergente_calificacion,null);
                        // AQUI SE PROGRAMA COMO SI FUERA EL MAIN DE MI NUEVO LAYOUT
                        Button califico = (Button) ventana.findViewById(R.id.calificarCal);
                        Button omitio = (Button) ventana.findViewById(R.id.omitirCal);
                        final RatingBar calificacion = (RatingBar) ventana.findViewById(R.id.estrellitasCal);

                        califico.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                parqueadero.getPuntajeServicio().setPuntaje(calificacion.getRating());
                                String mensaje = db.AgregarPuntuacion(parqueadero.getPuntajeServicio(),parqueadero.getPropietario());
                                Toast.makeText(getApplicationContext(),mensaje, Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });

                        omitio.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                finish();
                            }
                        });

                        calificame.setView(ventana);
                        AlertDialog dialog2 = calificame.create();
                        dialog2.show();

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

    private void actualizarInformacion(){
        nombre.setText(parqueadero.getNombre());
        descripcion.setText(parteLegal+"\n"+parqueadero.getDescripcion());
        estrellitas.setRating((float) parqueadero.getPuntajeServicio().getPuntaje());
    }
}
