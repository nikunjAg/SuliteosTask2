package com.roshanjha.loginscreenmd;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
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

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SignupActivity extends AppCompatActivity {

    private static final String TAG = "SignupActivity";

    EditText _nameText3;
    EditText _addressText3;
    EditText _emailText3;
    EditText _mobileText3;
    EditText _bloodGroup3;
    TextView _lastDonated3;
    EditText _passwordText3;
    EditText _reEnterPasswordText3;
    Button _signupButton3;
    TextView _loginLink3;
    Switch _switch3;

    DatePickerDialog.OnDateSetListener mDateSetListener;

    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        _nameText3 = (EditText)findViewById(R.id.input_name3);
        _addressText3 = (EditText)findViewById(R.id.input_address3);
        _emailText3 = (EditText)findViewById(R.id.input_email3);
        _mobileText3 = (EditText)findViewById(R.id.input_mobile3);
        _bloodGroup3 = (EditText)findViewById(R.id.input_bloodgroup3);
        _lastDonated3 = (TextView)findViewById(R.id.input_lastdonated3);
        _passwordText3 =(EditText)findViewById(R.id.input_password3);
        _reEnterPasswordText3 = (EditText)findViewById(R.id.input_reEnterPassword3);
        _signupButton3 = (Button)findViewById(R.id.btn_signup3);
        _loginLink3 = (TextView)findViewById(R.id.link_login3);
        _switch3 = (Switch)findViewById(R.id.switch3);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        _signupButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        _loginLink3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });

        _switch3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(_switch3.isChecked()){
                    startActivity(new Intent(SignupActivity.this, EmpSignupActivity.class));
                }
            }
        });

        _lastDonated3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        SignupActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year,month,day
                );
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d(TAG, "onDateSet: mm/dd/yyyy: " + month + "/" + day + "/" + year);

                String date = month + "/" + day + "/" + year;
                _lastDonated3.setText("Last Donated Date " + date);
            }
        };

    }

    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        _signupButton3.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        String email = _emailText3.getText().toString();
        String password = _passwordText3.getText().toString();

        firebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {
                            progressDialog.dismiss();
                            Toast.makeText(SignupActivity.this, "New User Created", Toast.LENGTH_LONG).show();
                            sendEmailVerification();
                        }
                        else {
                            progressDialog.dismiss();
                            Toast.makeText(SignupActivity.this, "Error " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
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
        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Sending Data...");
        progressDialog.show();

        DatabaseReference databaseReference = firebaseDatabase.getReference().child("Donaters");

        String name = _nameText3.getText().toString();
        String address = _addressText3.getText().toString();
        String email = _emailText3.getText().toString();
        String mobile = _mobileText3.getText().toString();
        String blood = _bloodGroup3.getText().toString();
        String date = _lastDonated3.getText().toString();

        DonatorModelClass donatorModelClass = new DonatorModelClass(name,email,address,mobile,blood,date);

        databaseReference.child(firebaseAuth.getCurrentUser().getUid()).setValue(donatorModelClass)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                        {
                            progressDialog.dismiss();
                            Toast.makeText(SignupActivity.this, "Data Sent To Database Successfully", Toast.LENGTH_LONG).show();
                            firebaseAuth.signOut();
                            finish();
                            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                        }
                        else {
                            progressDialog.dismiss();
                            firebaseAuth.signOut();
                            Toast.makeText(SignupActivity.this, "Error " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }

    private void sendEmailVerification() {

        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this,
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
                    Toast.makeText(SignupActivity.this, "Email Verification Sent", Toast.LENGTH_LONG).show();
                    sendDataToDatabase();
                }
                else {
                    progressDialog.dismiss();
                    firebaseAuth.signOut();
                    Toast.makeText(SignupActivity.this, "Error " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    public void onSignupSuccess() {
        _signupButton3.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _signupButton3.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = _nameText3.getText().toString();
        String address = _addressText3.getText().toString();
        String email = _emailText3.getText().toString();
        String mobile = _mobileText3.getText().toString();
        String bloodGroup = _bloodGroup3.getText().toString();
        String lastDonated = _lastDonated3.getText().toString();
        String password = _passwordText3.getText().toString();
        String reEnterPassword = _reEnterPasswordText3.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            _nameText3.setError("at least 3 characters");
            valid = false;
        } else {
            _nameText3.setError(null);
        }

        if (address.isEmpty()) {
            _addressText3.setError("Enter Valid Address");
            valid = false;
        } else {
            _addressText3.setError(null);
        }


        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText3.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText3.setError(null);
        }

        if (mobile.isEmpty() || mobile.length()!=10) {
            _mobileText3.setError("Enter Valid Mobile Number");
            valid = false;
        } else {
            _mobileText3.setError(null);
        }

        if (TextUtils.isEmpty(bloodGroup)) {
            _bloodGroup3.setError("Enter Blood Group");
            valid = false;
        } else {
            if (!validateBloodGroup(bloodGroup))
            {
                _bloodGroup3.setError("Enter Valid Blood Group(O+,O-,A+,A-,B+,B-,AB+,AB-)");
                valid = false;
            }

            else
            {
                _bloodGroup3.setError(null);
            }
        }

        if (lastDonated.isEmpty()) {
            _lastDonated3.setError("Enter A Date");
            valid = false;
        } else {
            _lastDonated3.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText3.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText3.setError(null);
        }

        if (reEnterPassword.isEmpty() || reEnterPassword.length() < 4 || reEnterPassword.length() > 10 || !(reEnterPassword.equals(password))) {
            _reEnterPasswordText3.setError("Password Do not match");
            valid = false;
        } else {
            _reEnterPasswordText3.setError(null);
        }

        return valid;
    }

    private boolean validateBloodGroup(CharSequence bloodGroup) {

        Pattern pattern = Pattern.compile("^(A|B|AB|O)[+-]$");
        Matcher matcher = pattern.matcher(bloodGroup);
        return matcher.matches();

    }
}
