package com.example.marcadorescategoriafirebase.classes;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.marcadorescategoriafirebase.EditarCategoria;
import com.example.marcadorescategoriafirebase.EditarMarcador;
import com.example.marcadorescategoriafirebase.R;
import com.example.marcadorescategoriafirebase.VerMarcadores;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class CategoriaAdapter extends RecyclerView.Adapter<CategoriaAdapter.CategoriaViewHolder> {
    private List<String> items;
    private Context context;
    private int clase;
    private String usuarioId;
    FirebaseDatabase database;
    DatabaseReference myRefLeerCategorias;
    FirebaseUser user;

    public static class CategoriaViewHolder extends RecyclerView.ViewHolder {

        public TextView nombre;
        public LinearLayout llCardviewCategorias;

        public CategoriaViewHolder(View v) {
            super(v);
            nombre = v.findViewById(R.id.nombreCategoriaCard);
            llCardviewCategorias = v.findViewById(R.id.llCardCategoria);
        }
    }

    public CategoriaAdapter(List<String> items, Context context, int clase) {
        this.items = items;
        this.context = context;
        this.clase = clase;
    }

    public void update(List<String> lista)
    {
        items.clear();
        items.addAll(lista);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public CategoriaViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.categoria_card, viewGroup, false);
        return new CategoriaViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final CategoriaViewHolder viewHolder, int i) {
        viewHolder.nombre.setText(items.get(i));
        if (clase == 1){
            viewHolder.llCardviewCategorias.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, VerMarcadores.class);
                    intent.putExtra("categoria", viewHolder.nombre.getText().toString());
                    context.startActivity(intent);
                }
            });

            viewHolder.llCardviewCategorias.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(final View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Editar categoría");
                    builder.setMessage("¿Quieres borrar o editar la categoría?");

                    builder.setPositiveButton("Editar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(context, EditarCategoria.class);
                            intent.putExtra("categoria", viewHolder.nombre.getText().toString());
                            context.startActivity(intent);
                        }
                    });
                    builder.setNegativeButton("Borrar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            final FirebaseDatabase database = FirebaseDatabase.getInstance();
                            final DatabaseReference myRefLeerCategorias = database.getReference("categoria");
                            final DatabaseReference myRefLeerMarcadores = database.getReference("marcadores");
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            usuarioId = "";

                            if (user != null) {
                                usuarioId = user.getUid();
                            }

                            myRefLeerCategorias.child(usuarioId).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                                        Categoria c = dataSnapshot1.getValue(Categoria.class);

                                        if (c.getNombre().equals(viewHolder.nombre.getText().toString())){
                                            System.out.println("CLAVE BORRAR" + dataSnapshot1.getValue(Categoria.class).toString() + " " + dataSnapshot.getKey());
                                            myRefLeerCategorias.child(usuarioId).child(dataSnapshot1.getKey()).removeValue();
                                            //notifyDataSetChanged();
                                            //items.clear();

                                            myRefLeerMarcadores.child(usuarioId).addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                                                        Marcadores m = dataSnapshot1.getValue(Marcadores.class);

                                                        if (m.getCategoria().equals(viewHolder.nombre.getText().toString())){
                                                            System.out.println("ES igual: " + m.toString());
                                                            myRefLeerMarcadores.child(usuarioId).child(dataSnapshot1.getKey()).removeValue();
                                                        }
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                }
                                            });

                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                    return false;
                }
            });
        }else{
            viewHolder.llCardviewCategorias.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Editar marcador");
                    builder.setMessage("¿Quieres borrar o editar el marcador?");

                    builder.setPositiveButton("Editar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(context, EditarMarcador.class);
                            intent.putExtra("marcador", viewHolder.nombre.getText().toString());
                            context.startActivity(intent);
                        }
                    });
                    builder.setNegativeButton("Borrar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            final FirebaseDatabase database = FirebaseDatabase.getInstance();
                            final DatabaseReference myRefLeerMarcadores = database.getReference("marcadores");
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            SharedPreferences prefs = context.getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);

                            final String categoriaMarcador = prefs.getString("categoriamarcador","");
                            usuarioId = "";

                            if (user != null) {
                                usuarioId = user.getUid();
                            }

                            myRefLeerMarcadores.child(usuarioId).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                                        Marcadores m = dataSnapshot1.getValue(Marcadores.class);

                                        if (m.getUrl().equals(viewHolder.nombre.getText().toString()) && m.getCategoria().equals(categoriaMarcador)){
                                            System.out.println("ES igual: " + m.toString());
                                            myRefLeerMarcadores.child(usuarioId).child(dataSnapshot1.getKey()).removeValue();
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                    return false;
                }
            });
        }
    }
}