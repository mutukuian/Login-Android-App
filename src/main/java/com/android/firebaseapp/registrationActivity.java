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
import com.google.firebase.database.FirebaseDatabase;

public class registrationActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView banner;
    private EditText editTextfullname, editTextage, editTextemail, editTextpassword;
    private ProgressBar progressBar;
    private Button registerUser;


    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        mAuth = FirebaseAuth.getInstance();//instance of firebase.

        banner = findViewById(R.id.banner);//use id given in activity_registration.xml
        banner.setOnClickListener(this::onClick);

        registerUser = findViewById(R.id.register_user);//use id given in activity_registration.xml
        registerUser.setOnClickListener(this::onClick);


        editTextfullname = findViewById(R.id.fullname);//use id given in activity_registration.xml
        editTextfullname.setOnClickListener(this::onClick);

        editTextage = findViewById(R.id.age);//use id given in activity_registration.xml
        editTextage.setOnClickListener(this::onClick);

        editTextemail = findViewById(R.id.email);//use id given in activity_registration.xml
        editTextemail.setOnClickListener(this::onClick);

        editTextpassword = findViewById(R.id.password);//use id given in activity_registration.xml
        editTextpassword.setOnClickListener(this::onClick);

        progressBar = findViewById(R.id.progress_bar);//use id given in activity_registration.xml
        progressBar.setOnClickListener(this::onClick);


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.banner:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;

            case R.id.register_user:
                registerUser();//calling register user method on clicking the button registerUser
                break;
        }

    }

    private void registerUser() {
        //register user method to enhance functionality of register user button in registration.xml

        String email = editTextemail.getText().toString().trim();
        String password = editTextpassword.getText().toString().trim();
        String fullname = editTextfullname.getText().toString().trim();
        String age = editTextage.getText().toString().trim();

        if (fullname.isEmpty()) {
            editTextfullname.setError("Full Name is required");
            editTextfullname.requestFocus();
            return;
        }
        if (age.isEmpty()) {
            editTextage.setError("Age is required");
            editTextage.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            editTextemail.setError("Email is required");
            editTextemail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextemail.setError("Please provide valid email");
            editTextemail.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            editTextpassword.setError("Password is required");
            editTextpassword.requestFocus();
            return;
        }
        if (password.length() < 6) {
            editTextpassword.setError("minimum password length should be 6 characters!");
            editTextpassword.requestFocus();
            return;
        }


        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    //checks if task is completed.
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            User user = new User(fullname, age, email);

//passing data filled to firebase database

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(registrationActivity.this, "User has been registered successfully!", Toast.LENGTH_LONG).show();

                                                progressBar.setVisibility(View.GONE);

                                                //redirect login layout
                                            } else {
                                                Toast.makeText(registrationActivity.this, "Failed to register!Try again!", Toast.LENGTH_LONG).show();
                                                progressBar.setVisibility(View.GONE);
                                            }
                                        }
                                    });


                        } else {
                            Toast.makeText(registrationActivity.this, "Failed to register!Try again!", Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }
}