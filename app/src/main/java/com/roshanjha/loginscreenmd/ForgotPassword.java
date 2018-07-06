package com.roshanjha.loginscreenmd;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {

    EditText EmailId;
    Button resetPassword;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        EmailId = (EditText)findViewById(R.id.forgotEmail);
        resetPassword = (Button)findViewById(R.id.resetPassword);

        firebaseAuth = FirebaseAuth.getInstance();

        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ProgressDialog progressDialog = new ProgressDialog(ForgotPassword.this,R.style.AppTheme_Dark_Dialog);
                progressDialog.setMessage("Sending Password Reset Email");
                progressDialog.setCancelable(false);
                progressDialog.show();

                String userEmail = EmailId.getText().toString().trim();
                if (userEmail.isEmpty()) {
                    Toast.makeText(ForgotPassword.this, "Enter the Email Id", Toast.LENGTH_LONG).show();
                } else {
                    firebaseAuth.sendPasswordResetEmail(userEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                progressDialog.dismiss();
                                Toast.makeText(ForgotPassword.this, "Password Reset Email sent", Toast.LENGTH_LONG).show();
                                finish();
                                startActivity(new Intent(ForgotPassword.this, MainActivity.class));
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(ForgotPassword.this, "Error " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
            }
        });
    }
}

