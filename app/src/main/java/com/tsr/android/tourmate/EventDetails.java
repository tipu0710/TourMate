package com.tsr.android.tourmate;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.HashMap;

public class EventDetails extends AppCompatActivity {

    private TextView eventName,destination,budget;
    private TextView fromDate,toDate;


    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    private String mUid= "";
    private String keyValue = "";

    private long fromDay,fromMounth,fromYear,toDay,toMounth,toYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        destination= (TextView) findViewById(R.id.destinationTv);
        budget= (TextView) findViewById(R.id.budgetTv);
        fromDate= (TextView) findViewById(R.id.from_dateTv);
        toDate= (TextView) findViewById(R.id.to_dateTv);
        eventName = (TextView) findViewById(R.id.event_nameTv);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();

        Intent intent =getIntent();
        mUid = intent.getStringExtra("uid");

        keyValue = intent.getStringExtra("key");
        mDatabaseReference = mFirebaseDatabase.getReference().child(mUid);

        mDatabaseReference.child(keyValue).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                    String s = (String) dataSnapshot.child("eventName").getValue();
                    eventName.setText(s);
                    s=(String) dataSnapshot.child("budget").getValue();
                    budget.setText(s);
                    s= (String) dataSnapshot.child("destination").getValue();
                    destination.setText(s);
                    HashMap<Long,Object> map = new HashMap<>();
                    map = (HashMap<Long, Object>) dataSnapshot.child("fromDateFinder").getValue();
                    fromDay= (long) map.get("day");
                    fromMounth= (long) map.get("mounth");
                    fromYear= (long) map.get("year");

                    map = (HashMap<Long, Object>) dataSnapshot.child("toDateFinder").getValue();
                    toDay= (long) map.get("day");
                    toMounth= (long) map.get("mounth");
                    toYear= (long) map.get("year");

                fromDate.setText(fromDay+"/"+fromMounth+"/"+fromYear);

                toDate.setText(""+String.valueOf(toDay)+"/"+String.valueOf(toMounth)+"/"+String.valueOf(toYear));


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
}
