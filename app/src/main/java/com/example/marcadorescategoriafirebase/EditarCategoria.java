package com.example.marcadorescategoriafirebase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.HashMap;
import java.util.Map;

public class EditarCategoria extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference myRefLeerCategoria, myRefLeerMarcador;
    FirebaseUser user;
    String usuarioId;

    private EditText etCategoriaEditarCategoria;
    private Button btEditarCategoria;

    private String nombreCategoria, nombrePrimMay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_categoria);

        initComponents();
        init();
    }

    private void initComponents() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance();
        myRefLeerCategoria = database.getReference("categoria");
        myRefLeerMarcador = database.getReference("marcadores");

        etCategoriaEditarCategoria =  findViewById(R.id.etCategoriaEditarCategoria);
        btEditarCategoria = findViewById(R.id.btEditarCategoria);
    }

    private void init() {
        if (user != null) {
            usuarioId = user.getUid();
        }

        etCategoriaEditarCategoria.setText(getIntent().getStringExtra("categoria"));

        btEditarCategoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etCategoriaEditarCategoria.getText().toString().equals("")){
                    Toast.makeText(EditarCategoria.this, getResources().getString(R.string.camposvacios), Toast.LENGTH_SHORT).show();
                }else{
                    myRefLeerCategoria.child(usuarioId).addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                            myRefLeerCategoria.child(usuarioId).child(dataSnapshot.getKey()).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.getValue(Categoria.class).getNombre().equals(getIntent().getStringExtra("categoria"))){
                                        System.out.println("ClaveModificar: " + dataSnapshot.getKey());
                                        nombreCategoria = etCategoriaEditarCategoria.getText().toString();
                                        nombrePrimMay = nombreCategoria.substring(0,1).toUpperCase() + nombreCategoria.substring(1);
                                        myRefLeerCategoria.child(usuarioId).child(dataSnapshot.getKey()).setValue(new Categoria(0, nombrePrimMay));
                                        myRefLeerMarcador.child(usuarioId).addChildEventListener(new ChildEventListener() {
                                            @Override
                                            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                                                myRefLeerMarcador.child(usuarioId).child(dataSnapshot.getKey()).addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        if (dataSnapshot.getValue(Marcadores.class).getCategoria().equals(getIntent().getStringExtra("categoria"))){
                                                            Map<String, Object> map = new HashMap<>();
                                                            map.put("categoria", nombrePrimMay);
                                                            myRefLeerMarcador.child(usuarioId).child(dataSnapshot.getKey()).updateChildren(map);
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

                                        Toast.makeText(EditarCategoria.this, getResources().getString(R.string.categoriamodificada), Toast.LENGTH_SHORT).show();
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
