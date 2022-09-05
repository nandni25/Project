package com.example.login_system;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
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
import com.google.firebase.database.FirebaseDatabase;

public class registration extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth mAuth;
    private EditText fn,age,mail,pass;
    private TextView reg;
    private Button btn;
    CheckBox spa;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        mAuth = FirebaseAuth.getInstance();
        reg=(TextView) findViewById(R.id.rg);
        reg.setOnClickListener(this);
        btn=(Button) findViewById(R.id.button);
        btn.setOnClickListener(this);
        fn=(EditText) findViewById(R.id.fname);
        age=(EditText) findViewById(R.id.age);
        mail=(EditText) findViewById(R.id.ma);
        pass=(EditText) findViewById(R.id.pw);
        spa=findViewById(R.id.spass);
        spa.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    pass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    pass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
    }


    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.rg:
                startActivity(new Intent(registration.this,MainActivity.class));
                break;
            case R.id.button:
                registeruser();
                break;
        }
    }
    private void registeruser() {
        final String email=mail.getText().toString().trim();
        String password=pass.getText().toString().trim();
        final String fullname=fn.getText().toString().trim();
        final String Age=age.getText().toString().trim();
        if(fullname.isEmpty()){
            fn.setError("full name is required");
            fn.requestFocus();
            return;
        }
        if(Age.isEmpty()){
            age.setError("AGE is required");
            age.requestFocus();
            return;
        }
        if(email.isEmpty()){
            mail.setError("mail is required");
            mail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            mail.setError("please provide valid email");
            mail.requestFocus();
            return;
        }
        if(password.isEmpty()){
            pass.setError("password is required");
            pass.requestFocus();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            user use=new user(fullname,Age,email);
                            FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(use).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(registration.this,"user has been successfully registerd",Toast.LENGTH_LONG).show();
                                    }
                                    else{
                                        Toast.makeText(registration.this,"failed to register",Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }
                        else{
                            Toast.makeText(registration.this,"Failed to register ,try again",Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}