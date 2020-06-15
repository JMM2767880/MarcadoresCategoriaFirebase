package com.example.marcadorescategoriafirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.marcadorescategoriafirebase.classes.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private EditText etUsuarioRegistro, etClaveRegistro;
    private Button btRegistrarRegistro, btLoginRegistro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initComponents();
        init();
    }

    private void initComponents() {
        mAuth = FirebaseAuth.getInstance();

        etUsuarioRegistro = findViewById(R.id.etUsuarioRegistro);
        etClaveRegistro = findViewById(R.id.etClaveRegistro);
        btRegistrarRegistro = findViewById(R.id.btRegistrarRegistro);
        btLoginRegistro = findViewById(R.id.btLoginReistro);
    }

    private void init() {
        btRegistrarRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usuario = etUsuarioRegistro.getText().toString();
                String clave = etClaveRegistro.getText().toString();

                if (usuario.equals("") || clave.equals("")){
                    Toast.makeText(MainActivity.this, getResources().getString(R.string.camposvacios), Toast.LENGTH_SHORT).show();
                }else{
                    mAuth.createUserWithEmailAndPassword(usuario, clave)
                            .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(MainActivity.this, getResources().getString(R.string.usuarioregistrado), Toast.LENGTH_SHORT).show();
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Toast.makeText(MainActivity.this, getResources().getString(R.string.usuarioexiste),
                                                Toast.LENGTH_SHORT).show();
                                    }

                                    // ...
                                }
                            });
                }
            }
        });

        btLoginRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Login.class);
                startActivity(intent);
            }
        });
    }
}
