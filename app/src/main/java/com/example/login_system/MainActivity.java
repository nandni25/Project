package com.example.login_system;

import androidx.annotation.NonNull;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private EditText email, pass;
    private Button sign;
    CheckBox rem;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sign = (Button) findViewById(R.id.sign);

        email = (EditText) findViewById(R.id.email);
        pass = (EditText) findViewById(R.id.pass);
        rem = findViewById(R.id.checkBox);
        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userlogin();
            }
        });
        mAuth = FirebaseAuth.getInstance();
    }


    private void userlogin() {
        String mail = email.getText().toString().trim();
        String pas = pass.getText().toString().trim();
        if (mail.isEmpty()) {
            email.setError("email is required");
            email.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(mail).matches()) {
            email.setError("please enter valid email");
            email.requestFocus();
            return;
        }
        if (pas.isEmpty()) {
            pass.setError("password is required");
            pass.requestFocus();
            return;
        }
        if (pas.length() < 6) {
            pass.setError("min password length is 6 character");
            pass.requestFocus();
            return;
        }

        mAuth.signInWithEmailAndPassword(mail, pas).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                } else {
                    Toast.makeText(MainActivity.this, "Failed to login! Please check your credentials", Toast.LENGTH_LONG);
                }
            }

        });
        rem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()) {
                    SharedPreferences preferences = getSharedPreferences("checkbox", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("remember", "true");
                    editor.apply();
                    Toast.makeText(MainActivity.this, "checked", Toast.LENGTH_SHORT).show();
                } else if (!compoundButton.isChecked()) {
                    SharedPreferences preferences = getSharedPreferences("checkbox", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("remember", "false");
                    editor.apply();
                    Toast.makeText(MainActivity.this, "unchecked", Toast.LENGTH_SHORT).show();
                } else {

                }
            }
        });


    }

    public void onConnected(@Nullable Bundle bundle) {

    }


    public void onConnectionSuspended(int i) {

    }
}