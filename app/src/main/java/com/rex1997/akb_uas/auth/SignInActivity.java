package com.rex1997.akb_uas.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.rex1997.akb_uas.MainActivity;
import com.rex1997.akb_uas.R;

/*
Created at 12/08/2022
Created by Bina Damareksa (NIM: 10121702; Class: AKB-7)
*/

public class SignInActivity extends AppCompatActivity {

    private EditText inputEmail, inputPassword;
    private ProgressBar progressBar;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        auth = FirebaseAuth.getInstance();

        Button btnSignIn = findViewById(R.id.btnSignIn);
        Button btnSingUp = findViewById(R.id.btnSignUp);

        inputEmail = findViewById(R.id.email);
        inputPassword = findViewById(R.id.password);
        progressBar = findViewById(R.id.loading);

        btnSingUp.setOnClickListener(view -> startActivity(new Intent(this, SignUpActivity.class)));

        btnSignIn.setOnClickListener(view -> {
            String email = inputEmail.getText().toString().trim();
            String password = inputPassword.getText().toString().trim();

            if(TextUtils.isEmpty(email)){
                inputEmail.setError(getString(R.string.err_null_email));
            } else if (TextUtils.isEmpty(password)){
                inputPassword.setError(getString(R.string.err_null_pass));
            } else if(password.length() < 6) {
                inputPassword.setError(getString(R.string.err_less_pass));
            } else {
                progressBar.setVisibility(View.VISIBLE);
                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(SignInActivity.this, task -> {
                    if(task.isSuccessful()){
                        Toast.makeText(SignInActivity.this, "Login success!", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(SignInActivity.this, MainActivity.class));
                    } else {
                        Toast.makeText(SignInActivity.this, "Login fail!", Toast.LENGTH_LONG).show();
                    }
                    progressBar.setVisibility(View.GONE);
                });
            }
        });
    }
}