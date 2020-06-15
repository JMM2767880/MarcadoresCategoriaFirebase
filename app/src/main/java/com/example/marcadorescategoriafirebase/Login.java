package com.example.marcadorescategoriafirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private EditText etUsuarioLogin, etClaveLogin;
    private Button btAccederLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initComponents();
        init();
    }

    private void initComponents() {
        mAuth = FirebaseAuth.getInstance();

        etUsuarioLogin = findViewById(R.id.etUsuarioLogin);
        etClaveLogin = findViewById(R.id.etClaveLogin);
        btAccederLogin = findViewById(R.id.btAccederLogin);
    }

    private void init() {
        btAccederLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usuario = etUsuarioLogin.getText().toString();
                String clave = etClaveLogin.getText().toString();

                if (usuario.equals("") || clave.equals("")){
                    Toast.makeText(Login.this, getResources().getString(R.string.camposvacios), Toast.LENGTH_SHORT).show();
                }else{
                    mAuth.signInWithEmailAndPassword(usuario, clave)
                            .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        Toast.makeText(Login.this, getResources().getString(R.string.bienvenido) + " " + user.getEmail(), Toast.LENGTH_SHORT).show();

                                        Intent intent = new Intent(Login.this, Menu.class);
                                        startActivity(intent);
                                        etUsuarioLogin.setText("");
                                        etClaveLogin.setText("");
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Toast.makeText(Login.this, getResources().getString(R.string.loginincorrecto),
                                                Toast.LENGTH_SHORT).show();
                                    }

                                    // ...
                                }
                            });
                }
            }
        });
    }
}
