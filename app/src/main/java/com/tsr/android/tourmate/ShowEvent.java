package com.tsr.android.tourmate;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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
    private String keyValue;

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

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(ShowEvent.this,EventDetails.class);
                        intent.putExtra("uid",mUid);
                        intent.putExtra("key",key);
                        startActivity(intent);

                    }
                });

                viewHolder.mView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        keyValue = key;
                        return false;
                    }
                });
                registerForContextMenu(viewHolder.mView);

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

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        startActivity(new Intent(this,MainActivity.class));
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.show_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.add_cost:

                Intent intent1 = new Intent(ShowEvent.this,CostList.class);
                intent1.putExtra("key",keyValue);
                intent1.putExtra("uid",mUid);
                startActivity(intent1);

                return true;
            case R.id.delete:
                mDatabaseReference = mFirebaseDatabase.getReference().child(mUid);
                mDatabaseReference.child(keyValue).removeValue();
                return true;
            case R.id.update:
                Intent intent = new Intent(ShowEvent.this,MainActivity.class);
                intent.putExtra("key",keyValue);
                intent.putExtra("status",true);
                intent.putExtra("uid",mUid);
                startActivity(intent);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
}
