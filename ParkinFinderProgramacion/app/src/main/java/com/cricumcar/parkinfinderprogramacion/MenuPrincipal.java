package com.cricumcar.parkinfinderprogramacion;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MenuPrincipal extends AppCompatActivity {

    private Button irMapa, irIniciarSesion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);

        irMapa = (Button) findViewById(R.id.irMapaP);
        irIniciarSesion = (Button) findViewById(R.id.irIniciarSesionP);

        irIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MenuPrincipal.this,MenuIniciarSesion.class);
                startActivity(intent);
            }
        });

        irMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MenuPrincipal.this,MenuMapa.class);
                startActivity(intent);
            }
        });

    }
}
