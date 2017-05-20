package com.tsr.android.tourmate;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

public class CostList extends AppCompatActivity {

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference,mDatabaseReference1;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    private EditText titleET,ammountET;
    private Button addBTN;
    private TextView totalCostTV;
    private RecyclerView recyclerView;

    private String keyValue;
    private String mUid;
    public static final String EXPENSE= "Expense List";
    public static final String TOTAL_EXPENSE= "totalCost";

    private int totalCost = 0;
    private int totalAmount;
    private boolean status =false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cost_list);
        titleET = (EditText) findViewById(R.id.expense_titel);
        ammountET = (EditText) findViewById(R.id.expense_amount);
        addBTN = (Button) findViewById(R.id.expense_add_button);
        totalCostTV = (TextView) findViewById(R.id.expense_total_tv);
        recyclerView = (RecyclerView) findViewById(R.id.expense_list);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mFirebaseDatabase = FirebaseDatabase.getInstance();

        mFirebaseAuth = FirebaseAuth.getInstance();

        Intent intent =getIntent();
        keyValue = intent.getStringExtra("key");
        mUid = intent.getStringExtra("uid");

        mDatabaseReference = mFirebaseDatabase.getReference().child(mUid).child(keyValue).child(EXPENSE);
        mDatabaseReference1 = mFirebaseDatabase.getReference().child(mUid).child(keyValue);

        addBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (titleET.getText().toString().isEmpty()){
                    titleET.setError(getString(R.string.error_msg));
                }else if (ammountET.getText().toString().isEmpty()) {
                    ammountET.setError(getString(R.string.error_msg));
                }
                else {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    AllExpense allExpense = new AllExpense(titleET.getText().toString(),Integer.parseInt(ammountET.getText().toString()));
                    mDatabaseReference.push().setValue(allExpense);
                    mDatabaseReference1.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            String budget = (String) dataSnapshot.child("budget").getValue();
                            totalAmount = Integer.parseInt(budget);

                            if (status){
                                int percent = (totalAmount-totalCost)/totalAmount*100;
                                Toast.makeText(CostList.this, ""+percent, Toast.LENGTH_SHORT).show();
                                if (percent<=50){
                                    Toast.makeText(CostList.this, "You have "+percent+"% of your budget", Toast.LENGTH_SHORT).show();
                                }
                                status =false;
                            }else {
                                status =true;
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }

                ammountET.setText("");
                titleET.setText("");

            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<AllExpense,ExpenseViewHolder> recyclerAdapter = new FirebaseRecyclerAdapter<AllExpense, ExpenseViewHolder>(
                AllExpense.class,
                R.layout.expense_row,
                CostList.ExpenseViewHolder.class,
                mDatabaseReference) {
            @Override
            protected void populateViewHolder(ExpenseViewHolder viewHolder, AllExpense model, int position) {
                viewHolder.setExpenseTitle(model.getTitle());
                viewHolder.setExpenseAmount(String.valueOf(model.getCost()));
                totalCost += model.getCost();

                mDatabaseReference1.child(TOTAL_EXPENSE).setValue(totalCost);
                totalCostTV.setText(String.valueOf(totalCost));

            }
        };
        recyclerView.setAdapter(recyclerAdapter);

    }

    public static class ExpenseViewHolder extends RecyclerView.ViewHolder{

        View mView;
        public ExpenseViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setExpenseTitle(String title){
            TextView eventTitleTv = (TextView) mView.findViewById(R.id.expense_row_title);
            eventTitleTv.setText(title);
        }
         public void setExpenseAmount(String amount){
            TextView eventAmountTv = (TextView) mView.findViewById(R.id.expense_row_amount);
             eventAmountTv.setText(amount);
        }

    }
}
