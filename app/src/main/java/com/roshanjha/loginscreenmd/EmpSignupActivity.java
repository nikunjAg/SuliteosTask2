package com.roshanjha.loginscreenmd;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class EmpSignupActivity extends AppCompatActivity {

    private static final String TAG = "SignupActivity";

    EditText _nameText4;
    EditText _addressText4;
    EditText _emailText4;
    EditText _mobileText4;
    EditText _passwordText4;
    EditText _reEnterPasswordText4;
    EditText _bloodRequired4;
    Button _signupButton4;
    TextView _loginLink4;
    Switch _switch4;

    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emp_signup);

        _nameText4 = (EditText)findViewById(R.id.input_name4);
        _addressText4 = (EditText)findViewById(R.id.input_address4);
        _emailText4 = (EditText)findViewById(R.id.input_email4);
        _mobileText4 = (EditText)findViewById(R.id.input_mobile4);
        _passwordText4 = (EditText)findViewById(R.id.input_password4);
        _reEnterPasswordText4 = (EditText)findViewById(R.id.input_reEnterPassword4);
        _signupButton4 = (Button)findViewById(R.id.btn_signup4);
        _loginLink4 = (TextView)findViewById(R.id.link_login4);
        _bloodRequired4 = (EditText)findViewById(R.id.avg_blood_reqd4);
        _switch4 = (Switch)findViewById(R.id.switch4);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        _signupButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        _loginLink4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),EmpLoginActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });

        _switch4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(_switch4.isChecked()){
                    startActivity(new Intent(EmpSignupActivity.this, SignupActivity.class));
                }
            }
        });
    }

    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        _signupButton4.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(EmpSignupActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        String email = _emailText4.getText().toString();
        String password = _passwordText4.getText().toString();

        firebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {
                            progressDialog.dismiss();
                            Toast.makeText(EmpSignupActivity.this, "New User Created", Toast.LENGTH_LONG).show();
                            sendEmailVerification();
                        }
                        else {
                            progressDialog.dismiss();
                            Toast.makeText(EmpSignupActivity.this, "Error " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });


//        new android.os.Handler().postDelayed(
//                new Runnable() {
//                    public void run() {
//                        onSignupSuccess();
//                        progressDialog.dismiss();
//                    }
//                }, 3000);
    }


    private void sendDataToDatabase() {

        boolean flag = false;
        final ProgressDialog progressDialog = new ProgressDialog(EmpSignupActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Sending Data...");
        progressDialog.show();

        DatabaseReference databaseReference = firebaseDatabase.getReference().child("Hospital");

        String name = _nameText4.getText().toString();
        String address = _addressText4.getText().toString();
        String email = _emailText4.getText().toString();
        String mobile = _mobileText4.getText().toString();
        String blood = _bloodRequired4.getText().toString();

        HospitalModelClass hospitalModelClass = new HospitalModelClass(name,address,email,mobile,blood);

        databaseReference.child(firebaseAuth.getCurrentUser().getUid()).setValue(hospitalModelClass)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                        {
                            progressDialog.dismiss();
                            Toast.makeText(EmpSignupActivity.this, "Data Sent To Database Successfully", Toast.LENGTH_LONG).show();
                            firebaseAuth.signOut();
                            finish();
                            Intent intent = new Intent(getApplicationContext(),EmpLoginActivity.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                        }
                        else {
                            progressDialog.dismiss();
                            firebaseAuth.signOut();
                            Toast.makeText(EmpSignupActivity.this, "Error " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }

    private void sendEmailVerification() {

        final ProgressDialog progressDialog = new ProgressDialog(EmpSignupActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Sending Verification Mail...");
        progressDialog.show();

        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        firebaseUser.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                        {
                            progressDialog.dismiss();
                            Toast.makeText(EmpSignupActivity.this, "Email Verification Sent", Toast.LENGTH_LONG).show();
                            sendDataToDatabase();
                        }
                        else {
                            progressDialog.dismiss();
                            firebaseAuth.signOut();
                            Toast.makeText(EmpSignupActivity.this, "Error " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }


//    public void onSignupSuccess() {
//        _signupButton4.setEnabled(true);
//        setResult(RESULT_OK, null);
//        finish();
//    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
        _signupButton4.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = _nameText4.getText().toString();
        String address = _addressText4.getText().toString();
        String email = _emailText4.getText().toString();
        String mobile = _mobileText4.getText().toString();
        String password = _passwordText4.getText().toString();
        String reEnterPassword = _reEnterPasswordText4.getText().toString();
        String blood = _bloodRequired4.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            _nameText4.setError("at least 3 characters");
            valid = false;
        } else {
            _nameText4.setError(null);
        }

        if (address.isEmpty()) {
            _addressText4.setError("Enter Valid Address");
            valid = false;
        } else {
            _addressText4.setError(null);
        }

        if (TextUtils.isEmpty(blood))
        {
            _bloodRequired4.setError("Enter Blood Details");
            valid = false;
        }
        else {
            _bloodRequired4.setError(null);
        }


        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText4.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText4.setError(null);
        }

        if (mobile.isEmpty() || mobile.length()!=10) {
            _mobileText4.setError("Enter Valid Mobile Number");
            valid = false;
        } else {
            _mobileText4.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText4.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText4.setError(null);
        }

        if (reEnterPassword.isEmpty() || reEnterPassword.length() < 4 || reEnterPassword.length() > 10 || !(reEnterPassword.equals(password))) {
            _reEnterPasswordText4.setError("Password Do not match");
            valid = false;
        } else {
            _reEnterPasswordText4.setError(null);
        }

        return valid;
    }
}
