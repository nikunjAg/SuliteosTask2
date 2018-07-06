package com.roshanjha.loginscreenmd;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;


    TextView forgotPassword;
    EditText _emailText;
    EditText _passwordText;
    Button _loginButton;
    TextView _signupLink;

    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;

    Switch _switch1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _emailText = (EditText) findViewById(R.id.input_email);
        _passwordText = (EditText) findViewById(R.id.input_password);
        _loginButton = (Button) findViewById(R.id.btn_login);
        _signupLink = (TextView)findViewById(R.id.link_signup);
        _switch1 = (Switch)findViewById(R.id.switch1);
        forgotPassword = findViewById(R.id.link_forgotPassword);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this,R.style.AppTheme_Dark_Dialog);
        progressDialog.setMessage("Loading ...");
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.show();

        if (firebaseAuth.getCurrentUser() != null)
        {

            DatabaseReference databaseReference = firebaseDatabase.getReference();
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (dataSnapshot.hasChild("Donaters"))
                    {
                        progressDialog.dismiss();
                        Intent intent = new Intent(MainActivity.this,ShowDonatorDetails.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                    else if (dataSnapshot.hasChild("Hospital"))
                    {
                        progressDialog.dismiss();
                        Intent intent = new Intent(MainActivity.this,ShowHospitalDetails.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
        progressDialog.dismiss();

        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,ForgotPassword.class));
            }
        });

        _switch1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(_switch1.isChecked()){
                    startActivity(new Intent(MainActivity.this, EmpLoginActivity.class));
                }
            }
        });
    }


    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        _loginButton.setEnabled(true);

        final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        firebaseAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful())
                {
                    progressDialog.dismiss();
                    _loginButton.setEnabled(true);
                    Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_LONG).show();
                    isEmailVerifiedorNot();
                }
                else {
                    _loginButton.setEnabled(true);
                    progressDialog.dismiss();
                    Toast.makeText(MainActivity.this, "Error " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });


//        new android.os.Handler().postDelayed(
//                new Runnable() {
//                    public void run() {
//                        onLoginSuccess();
//                        progressDialog.dismiss();
//                    }
//                }, 3000);
    }


    private void isEmailVerifiedorNot() {
        final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Checking Email Verification...");
        progressDialog.show();


        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if (firebaseUser.isEmailVerified())
        {
            progressDialog.dismiss();
            _loginButton.setEnabled(true);
            finish();
            Intent intent = new Intent(getApplicationContext(),ShowDonatorDetails.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        }
        else {
            progressDialog.dismiss();
            _loginButton.setEnabled(true);
            firebaseAuth.signOut();
            Toast.makeText(this, "Verify The Email Sent To Your EmailId First, To Login", Toast.LENGTH_LONG).show();
        }


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        _loginButton.setEnabled(true);
        finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }
}
