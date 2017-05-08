package com.tsr.android.tourmate;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

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

    private RecyclerView recyclerView;
    public static final int RC_SIGN_IN = 1;
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
                viewHolder.setEventName(model.getEventName());

                viewHolder.setDestination(model.getDestination());
                viewHolder.setBudget(model.getBudget());
                DateFiender fromDateFiender = new DateFiender();
                fromDateFiender = model.getFromDateFinder();
                String formDate = fromDateFiender.getDay()+"/"+fromDateFiender.getMounth()+"/"+fromDateFiender.getYear();
                viewHolder.setFromDate(formDate);
                DateFiender toDateFiender = new DateFiender();
                toDateFiender = model.getFromDateFinder();
                String toDate = toDateFiender.getDay()+"/"+toDateFiender.getMounth()+"/"+toDateFiender.getYear();
                viewHolder.setToDate(toDate);
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
