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

public class ShowDonatorDetails extends AppCompatActivity {

    TextView name4,email4,address4,mobile4,blood4,date4;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    Button Logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_donator_details);

        name4 = findViewById(R.id.namedetails);
        email4 = findViewById(R.id.Emaildetails);
        address4 = findViewById(R.id.Addressdetails);
        mobile4 = findViewById(R.id.Mobiledetails);
        blood4 = findViewById(R.id.BloodGroupdetails);
        date4 = findViewById(R.id.Datedetails);
        Logout = findViewById(R.id.donorLogout);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                finish();
                startActivity(new Intent(ShowDonatorDetails.this,MainActivity.class));
            }
        });

        if (firebaseAuth.getCurrentUser()!=null)
        {
            DatabaseReference databaseReference = firebaseDatabase.getReference().child("Donaters");
            databaseReference.child(firebaseAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (dataSnapshot.hasChild("name1") && dataSnapshot.hasChild("email1") && dataSnapshot.hasChild("address1") && dataSnapshot.hasChild("mobile1") && dataSnapshot.hasChild("bloodGroup1") && dataSnapshot.hasChild("lastDonatedDate1"))
                    {
                        String name = dataSnapshot.child("name1").getValue().toString().trim();
                        String email = dataSnapshot.child("email1").getValue().toString().trim();
                        String address = dataSnapshot.child("address1").getValue().toString().trim();
                        String mobile = dataSnapshot.child("mobile1").getValue().toString().trim();
                        String bloodGroup = dataSnapshot.child("bloodGroup1").getValue().toString().trim();
                        String date = dataSnapshot.child("lastDonatedDate1").getValue().toString().trim();

                        name4.setText(name);
                        email4.setText(email);
                        address4.setText(address);
                        mobile4.setText(mobile);
                        blood4.setText(bloodGroup);
                        date4.setText(date);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
        else {
            finish();
            startActivity(new Intent(ShowDonatorDetails.this,MainActivity.class));
        }

    }
}
