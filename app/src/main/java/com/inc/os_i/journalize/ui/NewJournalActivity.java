package com.inc.os_i.journalize.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.inc.os_i.journalize.MainActivity;
import com.inc.os_i.journalize.R;
import com.inc.os_i.journalize.model.JournalEntry;
import com.inc.os_i.journalize.utils.DateUtils;
import com.inc.os_i.journalize.utils.FieldValidationUtils;

public class NewJournalActivity extends AppCompatActivity {

    private static final String TAG = NewJournalActivity.class.getSimpleName();

    private DatabaseReference mDatabase;
    private String formattedDate;
    private String mUserId, mKey, sKey;
    private EditText text,eTitle;
    private TextView mDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_journal);
        FloatingActionButton fab = findViewById(R.id.fab_save_entry);
        text =  findViewById(R.id.contText);
        eTitle = findViewById(R.id.titleText);
        mDate = findViewById(R.id.add_journal_new_date);
        formattedDate = DateUtils.getSimpleDate();
        setTitle(getString(R.string.title_activity_new_journal));


         Bundle bundle;
         final String content, title,simpleDate;
         FirebaseAuth mFirebaseAuth;
         FirebaseUser mFirebaseUser;

        // Initialize Firebase Auth and Database Reference
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mUserId = mFirebaseUser.getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference("users").child(mUserId).child("items");

        bundle = getIntent().getExtras();
        if(bundle!=null){
            setTitle(getString(R.string.title_activity_edit_entry));
            title = bundle.getString("title");
            content = bundle.getString("content");
            simpleDate = bundle.getString("date");
            sKey = bundle.getString("key");
            text.setText(content);
            eTitle.setText(title);
            mDate.setText(simpleDate);

            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    JournalEntry item = new JournalEntry(eTitle.getText().toString(), text.getText().toString(), simpleDate, sKey);
                    mDatabase.child(sKey).setValue(item);
                    Intent intent = new Intent(NewJournalActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            });
        }
        else {
            mDate.setText(formattedDate);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (FieldValidationUtils.isValidEntry(text.getText().toString())) {
                        JournalEntry item = new JournalEntry(eTitle.getText().toString(), text.getText().toString(), formattedDate, generateKey());
                        mDatabase.child(mKey).setValue(item);
                        text.setText("");
                        Toast.makeText(NewJournalActivity.this, "Journal Entry Added Successfully ", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(NewJournalActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    } else {
                        Toast.makeText(NewJournalActivity.this, "You cannot save an empty entry ", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }

    private String generateKey() {
        mKey = mDatabase.push().getKey();
        Log.i(TAG, "NODE CHILD KEY: " + mKey);
        if(mKey.equalsIgnoreCase(mKey)){
            mKey = mDatabase.push().getKey();
            Log.i(TAG, "NEW NODE CHILD KEY: " + mKey);
        }
        return mKey;
    }

}
