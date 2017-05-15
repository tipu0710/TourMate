package com.tsr.android.tourmate;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

public class ShowEvent extends AppCompatActivity {

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private ChildEventListener mChildEventListener;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseStorage mFirebaseStorage;
    private EventAdapter eventAdapter;
    private ArrayList<EventList> eventLists;
    private String mUid= "";

    private long fromDay,fromMounth,fromYear,toDay,toMounth,toYear;

    private RecyclerView recyclerView;
    public static final int RC_SIGN_IN = 1;
    public static final String ACTION = "send_key";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_event);
        recyclerView = (RecyclerView) findViewById(R.id.recycle);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        mFirebaseDatabase = FirebaseDatabase.getInstance();
        Intent intent = getIntent();
        mUid = intent.getStringExtra("uid");
        mDatabaseReference = mFirebaseDatabase.getReference().child(mUid);

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<EventList,eventViewHolder>firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<EventList, eventViewHolder>(
                EventList.class,
                R.layout.single_event_row,
                eventViewHolder.class,
                mDatabaseReference
        ) {
            @Override
            protected void populateViewHolder(eventViewHolder viewHolder, EventList model, int position) {

                final String key = getRef(position).getKey();

                viewHolder.setEventName(model.getEventName());
                viewHolder.setDestination(model.getDestination());
                viewHolder.setBudget(model.getBudget());
                DateFinder fromDateFinder = new DateFinder();
                fromDateFinder = model.getFromDateFinder();
                fromDay = fromDateFinder.getDay();
                fromMounth = fromDateFinder.getMounth();
                fromYear = fromDateFinder.getYear();
                String formDate = fromDay+"/"+fromMounth+"/"+fromYear;
                viewHolder.setFromDate(formDate);
                DateFinder toDateFinder = new DateFinder();
                toDateFinder = model.getToDateFinder();

                toDay = toDateFinder.getDay();
                toMounth = toDateFinder.getMounth();
                toYear = toDateFinder.getYear();
                String toDate = toDay+"/"+toMounth+"/"+toYear;
                viewHolder.setToDate(toDate);

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(ShowEvent.this,MainActivity.class);
                        intent.putExtra("key",key);
                        intent.putExtra("status",true);
                        intent.putExtra("uid",mUid);
                        /*intent.putExtra("fromDay",fromDay);
                        intent.putExtra("fromMounth",fromMounth);
                        intent.putExtra("fromYear",fromYear);
                        intent.putExtra("toDay",toDay);
                        intent.putExtra("toMounth",toMounth);
                        intent.putExtra("toYear",toYear);*/
                        startActivity(intent);
                    }
                });
            }

        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);

    }

    public static class eventViewHolder extends RecyclerView.ViewHolder{

        View mView;
        public eventViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setEventName(String eventName){
            TextView eventNameTv = (TextView) mView.findViewById(R.id.event_name_tv);
            eventNameTv.setText(eventName);
        }
        public void setDestination(String destination){
            TextView eventNameTv = (TextView) mView.findViewById(R.id.destination_tv);
            eventNameTv.setText(destination);
        }
        public void setBudget(String budget){
            TextView eventNameTv = (TextView) mView.findViewById(R.id.budget_tv);
            eventNameTv.setText(budget);
        }
        public void setFromDate(String formDate){
            TextView eventNameTv = (TextView) mView.findViewById(R.id.from_date_tv);
            eventNameTv.setText(formDate);
        }
        public void setToDate(String toDate){
            TextView eventNameTv = (TextView) mView.findViewById(R.id.to_date_tv);
            eventNameTv.setText(toDate);
        }

    }
}
