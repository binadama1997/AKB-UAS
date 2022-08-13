package com.rex1997.akb_uas.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.rex1997.akb_uas.MainActivity;
import com.rex1997.akb_uas.R;

public class SignUpActivity extends AppCompatActivity {

    private EditText inputEmail, inputPassword, inputRePassword;
    private ProgressBar progressBar;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        auth = FirebaseAuth.getInstance();

        Button btnSignIn = findViewById(R.id.btnSignIn);
        Button btnSingUp = findViewById(R.id.btnSignUp);

        inputEmail = findViewById(R.id.email);
        inputPassword = findViewById(R.id.password);
        inputRePassword = findViewById(R.id.repassword);
        progressBar = findViewById(R.id.loading);

        btnSingUp.setOnClickListener(view -> {
            String email = inputEmail.getText().toString().trim();
            String password = inputPassword.getText().toString().trim();
            String rePassword = inputRePassword.getText().toString().trim();

            if(TextUtils.isEmpty(email)){
                inputEmail.setError(getString(R.string.err_null_email));
            } else if (TextUtils.isEmpty(password)){
                inputPassword.setError(getString(R.string.err_null_pass));
            } else if(password.length() < 6) {
                inputPassword.setError(getString(R.string.err_less_pass));
            } else if (TextUtils.isEmpty(rePassword)){
                inputRePassword.setError(getString(R.string.err_null_repass));
            } else if(!rePassword.equals(password)){
                inputRePassword.setError(getString(R.string.err_not_equal_pass));
            } else {
                progressBar.setVisibility(View.VISIBLE);
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(SignUpActivity.this, task -> {
                    Toast.makeText(SignUpActivity.this, "User : " + email +  ", successfully made!", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                    if(task.isSuccessful()){
                        startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                    } else {
                        Toast.makeText(SignUpActivity.this, "User : " + email + ", failed to create", Toast.LENGTH_LONG).show();
                    }
                    progressBar.setVisibility(View.GONE);
                });
            }
        });

        btnSignIn.setOnClickListener(view -> startActivity(new Intent(this, SignInActivity.class)));
    }
}