package com.example.marcadorescategoriafirebase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.marcadorescategoriafirebase.classes.Categoria;
import com.example.marcadorescategoriafirebase.classes.Marcadores;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CrearMarcador extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference myRefLeerCategorias;
    private DatabaseReference myRef;
    FirebaseUser user;
    String usuarioId;

    private Spinner spnCrearMarcador;
    private EditText etUrlCrearMarcador, etDescripcionCrearMarcador;
    private Button btCrearMarcador;

    List<String> categorias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_marcador);

        initComponents();
        init();
    }

    private void initComponents() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance();
        myRefLeerCategorias = database.getReference("categoria");
        myRef = database.getReference("marcadores");

        spnCrearMarcador = findViewById(R.id.spnCrearMarcador);
        etUrlCrearMarcador = findViewById(R.id.etUrlCrearMarcador);
        etDescripcionCrearMarcador = findViewById(R.id.etDescripcionCrearMarcador);
        btCrearMarcador = findViewById(R.id.btCrearMarcador);

        categorias = new ArrayList<>();
    }

    private void init() {
        if (user != null) {
            usuarioId = user.getUid();
        }

        myRef.child(usuarioId).child("Ejemplo").setValue(new Marcadores(0, "ejemplo", "ejemplo", "ejemplo"));

        myRefLeerCategorias.child(usuarioId).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                System.out.println("KEY: " + dataSnapshot.getKey());

                myRefLeerCategorias.child(usuarioId).child(dataSnapshot.getKey()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Categoria value = dataSnapshot.getValue(Categoria.class);
                        System.out.println("CategoríaExisteAdd: " + value.toString());

                        if (value.getNombre().equals("ejemplo")){

                        }else{
                            categorias.add(value.getNombre());
                        }

                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(CrearMarcador.this,
                                android.R.layout.simple_spinner_item, categorias);
                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spnCrearMarcador.setAdapter(dataAdapter);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btCrearMarcador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etUrlCrearMarcador.getText().toString().equals("") || etDescripcionCrearMarcador.getText().toString().equals("")){
                    Toast.makeText(CrearMarcador.this, getResources().getString(R.string.camposvacios), Toast.LENGTH_SHORT).show();
                }else{
                    myRef.child(usuarioId).push().setValue(new Marcadores(0, spnCrearMarcador.getSelectedItem().toString(), etUrlCrearMarcador.getText().toString(), etDescripcionCrearMarcador.getText().toString()));
                }
            }
        });
    }
}
