package com.roshanjha.loginscreenmd;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ShowHospitalDetails extends AppCompatActivity {

    TextView name5,email5,address5,mobile5,blood5;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    Button Logout1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_hospital_details);

        name5 = findViewById(R.id.Hospitalnamedetails);
        email5 = findViewById(R.id.HospitalEmaildetails);
        address5 = findViewById(R.id.HospitalAddressdetails);
        mobile5 = findViewById(R.id.HospitalMobiledetails);
        blood5 = findViewById(R.id.BloodRequireddetails);
        Logout1 = findViewById(R.id.HospitalLogout);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        Logout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                finish();
                startActivity(new Intent(ShowHospitalDetails.this,MainActivity.class));
            }
        });

        if (firebaseAuth.getCurrentUser()!=null)
        {
            DatabaseReference databaseReference = firebaseDatabase.getReference().child("Hospital");
            databaseReference.child(firebaseAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (dataSnapshot.hasChild("name") && dataSnapshot.hasChild("number") && dataSnapshot.hasChild("myemail") && dataSnapshot.hasChild("myaddress") && dataSnapshot.hasChild("bloodRequired"))
                    {
                        String name = dataSnapshot.child("name").getValue().toString().trim();
                        String email = dataSnapshot.child("myemail").getValue().toString().trim();
                        String address = dataSnapshot.child("myaddress").getValue().toString().trim();
                        String mobile = dataSnapshot.child("number").getValue().toString().trim();
                        String bloodGroup = dataSnapshot.child("bloodRequired").getValue().toString().trim();

                        name5.setText(name);
                        email5.setText(email);
                        address5.setText(address);
                        mobile5.setText(mobile);
                        blood5.setText(bloodGroup);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
        else {
            finish();
            startActivity(new Intent(ShowHospitalDetails.this,MainActivity.class));
        }

    }
}
