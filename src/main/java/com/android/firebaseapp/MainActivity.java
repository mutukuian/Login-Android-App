package com.android.firebaseapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;



import android.content.Intent;
import android.os.Bundle;

import android.util.Patterns;
import android.view.View;



import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public  class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private TextView register,  forgotPassword;

    private Button signin;
    private EditText editTextemail, editTextpassword;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        signin = findViewById(R.id.sign_in);
        signin.setOnClickListener(this::onClick);
        editTextemail = findViewById(R.id.email);
        editTextemail.setOnClickListener(this::onClick);
        editTextpassword = findViewById(R.id.password);
        editTextpassword.setOnClickListener(this::onClick);
        progressBar = findViewById(R.id.progress_bar);
        progressBar.setOnClickListener(this::onClick);
        register = findViewById(R.id.register);
        register.setOnClickListener(this::onClick);
    forgotPassword=findViewById(R.id.forgot_password);
    forgotPassword.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.register:
                startActivity(new Intent(this,registrationActivity.class));
                break;

            case R.id.forgot_password:
                Intent intent=new Intent(MainActivity.this,forgpassActivity.class);
                startActivity(intent);
                break;



            case R.id.sign_in:
                userLogin();
                break;




        }



    }

    private void userLogin() {


            String email = editTextemail.getText().toString().trim();
            String password = editTextpassword.getText().toString().trim();

            if (email.isEmpty()) {
                editTextemail.setError("Email is required");
                editTextemail.requestFocus();
                return;
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                editTextemail.setError("Enter a valid email");
                editTextemail.requestFocus();
                return;
            }
            if (password.isEmpty()) {
                editTextpassword.setError("Password is required");
                editTextpassword.requestFocus();
                return;
            }
            if (password.length() < 6) {
                editTextpassword.setError("Minimum password characters is 6!");
                editTextpassword.requestFocus();
                return;
            }
            progressBar.setVisibility(View.VISIBLE);


            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        //redirect to user profile

                        Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(MainActivity.this, "Failed to login! please check your credentials", Toast.LENGTH_LONG).show();
                    }
                }
            });

        }

}