package com.android.firebaseapp;



import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class forgpassActivity extends AppCompatActivity implements View.OnClickListener {



    private EditText emailEditText;
    private Button resetPasswordButton;
    private ProgressBar progressBar;

    FirebaseAuth auth;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgpass);

        auth=FirebaseAuth.getInstance();

        resetPasswordButton= findViewById(R.id.sign);
        resetPasswordButton.setOnClickListener(this);

        emailEditText=findViewById(R.id.emailAdd);
        progressBar=findViewById(R.id.progress_bar);


}

    @Override
    public void onClick(View view) {


        resetPassword();
    }

    private void resetPassword() {

        String email=emailEditText.getText().toString().trim();

        if (email.isEmpty()){
            emailEditText.setError("Email is required!");
            emailEditText.requestFocus();
            return;
        }

        if (! Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailEditText.setError("Enter a valid email!");
            emailEditText.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);

        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(forgpassActivity.this, "Check Your Email to reset password", Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(forgpassActivity.this, "Try again!Something Wrong happened!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}