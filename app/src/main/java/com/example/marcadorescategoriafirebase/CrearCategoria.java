package com.example.marcadorescategoriafirebase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.marcadorescategoriafirebase.classes.Categoria;
import com.example.marcadorescategoriafirebase.classes.Marcadores;
import com.example.marcadorescategoriafirebase.classes.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.EventListener;

public class CrearCategoria extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference myRef;
    FirebaseUser user;
    String usuarioId, nombreCategoria, nombrePrimMay;

    private EditText etCategoriaCrearCategoria;
    private Button btCrearCategoria;

    private int aux;

    private int contador = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_categoria);

        initComponents();
        init();
    }

    private void initComponents() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("categoria");

        etCategoriaCrearCategoria = findViewById(R.id.etCategoriaCrearCategoria);
        btCrearCategoria = findViewById(R.id.btCrearCategoria);

        aux = 0;
    }

    private void init() {
        if (user != null) {
            usuarioId = user.getUid();
        }

        //myRef.child(usuarioId).child("Ejemplo").setValue(new Categoria(0, "ejemplo"));

        btCrearCategoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aux = 0;
                if (etCategoriaCrearCategoria.getText().toString().equals("")){
                    Toast.makeText(CrearCategoria.this, getResources().getString(R.string.camposvacios), Toast.LENGTH_SHORT).show();
                }else{
                    nombreCategoria = etCategoriaCrearCategoria.getText().toString();
                    nombrePrimMay = nombreCategoria.substring(0,1).toUpperCase() + nombreCategoria.substring(1);

                    myRef.child(usuarioId).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                                Log.v("xyz", dataSnapshot1.toString());
                                Categoria categoria = dataSnapshot1.getValue(Categoria.class);
                                Log.v("xyz", categoria.toString());

                                if (categoria.getNombre().equals(nombrePrimMay)) {
                                    aux = 1;
                                }
                            }

                            if (aux == 0){
                                myRef.child(usuarioId).push().setValue(new Categoria(0, nombrePrimMay));
                                etCategoriaCrearCategoria.setText("");
                                Toast.makeText(CrearCategoria.this, getResources().getString(R.string.categoriaadd), Toast.LENGTH_SHORT).show();

                            }else{
                                Toast.makeText(CrearCategoria.this, getResources().getString(R.string.existecategoria), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    /*myRef.child(usuarioId).addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                            System.out.println("KEY: " + dataSnapshot.getKey());

                            myRef.child(usuarioId).child(dataSnapshot.getKey()).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    Categoria value = dataSnapshot.getValue(Categoria.class);
                                    System.out.println("CategoríaExisteAdd: " + value.toString());
                                    contador++;
                                    System.out.println("Contador: " + contador);

                                        if (value.getNombre().equals(nombrePrimMay)) {
                                            Toast.makeText(CrearCategoria.this, getResources().getString(R.string.existecategoria), Toast.LENGTH_SHORT).show();
                                            aux = 1;
                                            return;
                                        } else {
                                            myRef.child(usuarioId).push().setValue(new Categoria(0, nombrePrimMay));
                                            etCategoriaCrearCategoria.setText("");
                                            Toast.makeText(CrearCategoria.this, getResources().getString(R.string.categoriaadd), Toast.LENGTH_SHORT).show();
                                            aux = 2;
                                        }

                                        if (aux != 0){
                                            return;
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
                    });*/


                }

                aux = 0;
            }
        });
    }
}
