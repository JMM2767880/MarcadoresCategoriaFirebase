package com.example.marcadorescategoriafirebase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Menu extends AppCompatActivity {

    private Button btCrearCategoriaMenu , btCrearMarcadorMenu, btVerMarcadoresMenu, button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        initComponents();
        init();
    }

    private void initComponents() {
        btCrearCategoriaMenu = findViewById(R.id.btCrearCategoriaMenu);
        btCrearMarcadorMenu = findViewById(R.id.btCrearMarcadorMenu);
        btVerMarcadoresMenu = findViewById(R.id.btVerMarcadoresMenu);
        button = findViewById(R.id.button);
    }

    private void init() {
        btCrearCategoriaMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCrearCategoria = new Intent(Menu.this, CrearCategoria.class);
                startActivity(intentCrearCategoria);
            }
        });

        btCrearMarcadorMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCrearMarcador = new Intent(Menu.this, CrearMarcador.class);
                startActivity(intentCrearMarcador);
            }
        });

        btVerMarcadoresMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Menu.this, VerMarcadoresMenu.class);
                startActivity(intent);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
