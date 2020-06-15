package com.example.marcadorescategoriafirebase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
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

public class EditarMarcador extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference myRefLeerMarcador;
    private DatabaseReference myRefLeerCategorias;

    private Spinner spnEditarMarcador;
    private EditText etUrlEditarMarcador, etDescripcionEditarMarcador;
    private Button btEditarMarcador;

    private FirebaseUser user;
    private String usuarioId;

    private SharedPreferences prefs;

    private String categoriaMarcador;

    private List<String> categorias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_marcador);

        initComponents();
        init();
    }

    private void initComponents() {
        database = FirebaseDatabase.getInstance();
        myRefLeerMarcador = database.getReference("marcadores");
        myRefLeerCategorias = database.getReference("categoria");

        spnEditarMarcador = findViewById(R.id.spnEditarMarcador);
        etUrlEditarMarcador = findViewById(R.id.etUrlEditarMarcador);
        etDescripcionEditarMarcador = findViewById(R.id.etDescripcionEditarMarcador);
        btEditarMarcador = findViewById(R.id.btEditarMarcador);

        user = FirebaseAuth.getInstance().getCurrentUser();

        prefs = getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);

        categoriaMarcador = prefs.getString("categoriamarcador","");

        categorias = new ArrayList<>();
    }

    private void init() {
        if (user != null) {
            usuarioId = user.getUid();
        }

        myRefLeerCategorias.child(usuarioId).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                System.out.println("KEY: " + dataSnapshot.getKey());

                myRefLeerCategorias.child(usuarioId).child(dataSnapshot.getKey()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Categoria value = dataSnapshot.getValue(Categoria.class);
                        //System.out.println("CategoríaExisteAdd: " + value.toString());

                        if (value.getNombre().equals("ejemplo")){

                        }else{
                            categorias.add(value.getNombre());
                        }

                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(EditarMarcador.this,
                                android.R.layout.simple_spinner_item, categorias);
                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spnEditarMarcador.setAdapter(dataAdapter);

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

        myRefLeerMarcador.child(usuarioId).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                myRefLeerMarcador.child(usuarioId).child(dataSnapshot.getKey()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Marcadores marcadores = dataSnapshot.getValue(Marcadores.class);

                        if (marcadores.getUrl().equals(getIntent().getStringExtra("marcador")) && marcadores.getCategoria().equals(categoriaMarcador)){
                            System.out.println("ES igual: " + marcadores.toString());
                            etUrlEditarMarcador.setText(marcadores.getUrl());
                            etDescripcionEditarMarcador.setText(marcadores.getDescripcion());

                        }
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

        btEditarMarcador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etUrlEditarMarcador.getText().toString().equals("") || etDescripcionEditarMarcador.getText().toString().equals("")){
                    Toast.makeText(EditarMarcador.this, getResources().getString(R.string.camposvacios), Toast.LENGTH_SHORT).show();
                }else{
                    myRefLeerMarcador.child(usuarioId).addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                            myRefLeerMarcador.child(usuarioId).child(dataSnapshot.getKey()).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    Marcadores marcadores = dataSnapshot.getValue(Marcadores.class);

                                    if (marcadores.getUrl().equals(getIntent().getStringExtra("marcador")) && marcadores.getCategoria().equals(categoriaMarcador)){
                                        myRefLeerMarcador.child(usuarioId).child(dataSnapshot.getKey()).setValue(new Marcadores(0, spnEditarMarcador.getSelectedItem().toString(), etUrlEditarMarcador.getText().toString(), etDescripcionEditarMarcador.getText().toString()));
                                        Toast.makeText(EditarMarcador.this, getResources().getString(R.string.marcadoreditado), Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
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
                }
            }
        });
    }
}
