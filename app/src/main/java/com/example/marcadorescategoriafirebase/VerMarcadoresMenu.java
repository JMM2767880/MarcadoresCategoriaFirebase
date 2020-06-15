package com.example.marcadorescategoriafirebase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ArrayAdapter;

import com.example.marcadorescategoriafirebase.classes.Categoria;
import com.example.marcadorescategoriafirebase.classes.CategoriaAdapter;
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

public class VerMarcadoresMenu extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference myRefLeerCategorias;
    FirebaseUser user;
    String usuarioId;

    private List<String> categorias;

    private RecyclerView recycler;
    private CategoriaAdapter adapter;
    private RecyclerView.LayoutManager lManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_marcadores_menu);

        initComponents();
        init();
    }

    private void initComponents() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance();
        myRefLeerCategorias = database.getReference("categoria");

        categorias = new ArrayList<>();

        recycler = findViewById(R.id.rvVerMarcadores);
        recycler.setHasFixedSize(true);
        lManager = new LinearLayoutManager(VerMarcadoresMenu.this);
        recycler.setLayoutManager(lManager);

    }

    private void init() {
        if (user != null) {
            usuarioId = user.getUid();
        }

        /*myRefLeerCategorias.child(usuarioId).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                myRefLeerCategorias.child(usuarioId).child(dataSnapshot.getKey()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (dataSnapshot.getValue(Categoria.class).getNombre().equals("ejemplo")) {

                    }else{
                        categorias.add(dataSnapshot.getValue(Categoria.class).getNombre());
                    }

                    if (categorias.size() == 0){
                        categorias.add("");
                    }
                        adapter = new CategoriaAdapter(categorias, VerMarcadoresMenu.this, 1);
                        recycler.setAdapter(adapter);
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

        myRefLeerCategorias.child(usuarioId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                    Categoria categoria = dataSnapshot1.getValue(Categoria.class);

                    categorias.add(categoria.getNombre());
                }

                adapter = new CategoriaAdapter(categorias, VerMarcadoresMenu.this, 1);
                recycler.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        categorias.clear();

        myRefLeerCategorias.child(usuarioId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                    Categoria categoria = dataSnapshot1.getValue(Categoria.class);

                    categorias.add(categoria.getNombre());
                }

                adapter = new CategoriaAdapter(categorias, VerMarcadoresMenu.this, 1);
                recycler.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
