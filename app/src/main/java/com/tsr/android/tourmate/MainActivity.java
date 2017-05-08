package com.tsr.android.tourmate;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private EditText eventName,destination,budget;
    private Button createEvent,showEventBtn,fromDate,toDate;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseStorage mFirebaseStorage;
    private StorageReference mChatPhotosStorageReference;
    private FirebaseRemoteConfig mFirebaseRemoteConfig;



    private int fromDay,fromMounth,fromYear,toDay,toMounth,toYear,currentDay,currentMounth;
    private String muserName;
    private String mUid= "";

    public static final int RC_SIGN_IN = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        destination= (EditText) findViewById(R.id.destination);
        budget= (EditText) findViewById(R.id.budget);
        fromDate= (Button) findViewById(R.id.from_date);
        toDate= (Button) findViewById(R.id.to_date);
        createEvent = (Button)findViewById(R.id.create_event);
        showEventBtn = (Button)findViewById(R.id.show_event);
        eventName = (EditText) findViewById(R.id.event_name);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseStorage = FirebaseStorage.getInstance();

        fromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar calendar = Calendar.getInstance(Locale.getDefault());

                fromYear = calendar.get(Calendar.YEAR);
                fromMounth = calendar.get(Calendar.MONTH);
                fromDay = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dpd = new DatePickerDialog(MainActivity.this,dateListener,fromYear,fromMounth,fromDay);
                dpd.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                dpd.show();

            }

            private DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    fromYear = year;
                    fromMounth = month+1;
                    fromDay = dayOfMonth;
                    fromDate.setText(""+dayOfMonth+"/"+fromMounth+"/"+year);
                }
            };
        });

        toDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar calendar = Calendar.getInstance(Locale.getDefault());

                toYear = calendar.get(Calendar.YEAR);
                currentMounth = calendar.get(Calendar.MONTH);
                currentDay = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dpd = new DatePickerDialog(MainActivity.this,dateListener,toYear,currentMounth,currentDay);
                dpd.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                dpd.show();

            }

            private DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    toYear = year;
                    toMounth = month+1;
                    toDay = dayOfMonth;
                    toDate.setText(""+dayOfMonth+"/"+toMounth+"/"+year);
                }
            };
        });

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user!=null){
                    muserName = user.getDisplayName();
                    mUid = user.getUid();
                    Toast.makeText(MainActivity.this, "Welcome "+muserName, Toast.LENGTH_SHORT).show();

                }else {
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false)
                                    .setProviders(
                                            AuthUI.EMAIL_PROVIDER,
                                            AuthUI.GOOGLE_PROVIDER)
                                    .build(),
                            RC_SIGN_IN);
                }
            }
        };





        createEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (destination.getText().toString().isEmpty()){
                    destination.setError(getString(R.string.error_msg));
                }else if (budget.getText().toString().isEmpty()) {
                    budget.setError(getString(R.string.error_msg));
                }else if (eventName.getText().toString().isEmpty()){
                    eventName.setError(getString(R.string.error_msg));
                }else if (currentDay==toDay && currentMounth == toMounth){
                    toDate.setError("Wrong To Date");
                }else if (fromMounth>= toMounth && fromDay>=toDay){
                    toDate.setError("Wrong To Date");
                }
                else {
                    DateFiender fromDateFinder = new DateFiender(fromYear,fromMounth,fromDay);
                    DateFiender toDateFinder = new DateFiender(toYear,toMounth,toDay);
                    EventList eventList = new EventList(eventName.getText().toString(),destination.getText().toString(),budget.getText().toString(),fromDateFinder,toDateFinder);
                    mDatabaseReference = mFirebaseDatabase.getReference().child(mUid);
                    mDatabaseReference.push().setValue(eventList);

                    Toast.makeText(MainActivity.this, "Done", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==RC_SIGN_IN){
            if(resultCode==RESULT_OK){
                Toast.makeText(this,"Signed in!",Toast.LENGTH_SHORT).show();
            }else if(resultCode == RESULT_CANCELED){
                Toast.makeText(this,"Signed in canceled",Toast.LENGTH_SHORT).show();
                finish();
            }}

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAuthStateListener != null) {
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sign_out_menu:
                AuthUI.getInstance().signOut(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void showEvent(View view) {
        Intent intent = new Intent(this,ShowEvent.class);
        intent.putExtra("uid",mUid);
        startActivity(intent);
    }

}

