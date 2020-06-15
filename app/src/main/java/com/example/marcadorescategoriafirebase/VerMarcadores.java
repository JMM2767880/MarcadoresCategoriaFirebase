package com.example.marcadorescategoriafirebase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.example.marcadorescategoriafirebase.classes.Categoria;
import com.example.marcadorescategoriafirebase.classes.CategoriaAdapter;
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
import java.util.EventListener;
import java.util.List;

public class VerMarcadores extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference myRefLeerMarcador;
    FirebaseUser user;
    String usuarioId;

    private List<String> marcadores;

    private RecyclerView recycler;
    private CategoriaAdapter adapter;
    private RecyclerView.LayoutManager lManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_marcadores);

        initComponents();
        init();
    }

    private void initComponents() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance();
        myRefLeerMarcador = database.getReference("marcadores");

        marcadores = new ArrayList<>();

        recycler = findViewById(R.id.rvVerMarcadoresVerM);
        recycler.setHasFixedSize(true);
        lManager = new LinearLayoutManager(VerMarcadores.this);
        recycler.setLayoutManager(lManager);
    }

    private void init() {
        SharedPreferences prefs =
                getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("categoriamarcador", getIntent().getStringExtra("categoria"));
        editor.commit();

        if (user != null) {
            usuarioId = user.getUid();
        }

        /*myRefLeerMarcador.child(usuarioId).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                myRefLeerMarcador.child(usuarioId).child(dataSnapshot.getKey()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Marcadores value = dataSnapshot.getValue(Marcadores.class);

                        if (value.getCategoria().equals(getIntent().getStringExtra("categoria"))) {
                            marcadores.add(value.getUrl());
                        }else{
                            System.out.println("no");
                        }
                        adapter = new CategoriaAdapter(marcadores, VerMarcadores.this, 2);
                        recycler.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
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

        myRefLeerMarcador.child(usuarioId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                    Marcadores m = dataSnapshot1.getValue(Marcadores.class);

                    if (m.getCategoria().equals(getIntent().getStringExtra("categoria"))) {
                        marcadores.add(m.getUrl());
                    }else{
                        System.out.println("no");
                    }
                    adapter = new CategoriaAdapter(marcadores, VerMarcadores.this, 2);
                    recycler.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        marcadores.clear();

        myRefLeerMarcador.child(usuarioId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                    Marcadores m = dataSnapshot1.getValue(Marcadores.class);

                    if (m.getCategoria().equals(getIntent().getStringExtra("categoria"))) {
                        marcadores.add(m.getUrl());
                    }else{
                        System.out.println("no");
                    }
                    adapter = new CategoriaAdapter(marcadores, VerMarcadores.this, 2);
                    recycler.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
