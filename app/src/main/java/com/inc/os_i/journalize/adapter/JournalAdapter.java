package com.inc.os_i.journalize.adapter;

/**
 * Created by SAHX on 11/8/2017.
 */

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.inc.os_i.journalize.R;
import com.inc.os_i.journalize.model.JournalEntry;
import com.inc.os_i.journalize.ui.NewJournalActivity;

import java.util.ArrayList;


/**
 * Created by SAHX on 25/6/2018.
 */

public class JournalAdapter extends ArrayAdapter<JournalEntry>{
    //Get reference to Auth and database
    private DatabaseReference mDatabase;
    private final FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
    private final FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
    private String mUserId = mFirebaseUser.getUid();


    public JournalAdapter(Activity context, ArrayList<JournalEntry> journalItem) {
        // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
        // the second argument is used when the ArrayAdapter is populating a single TextView.
        // Because this is a custom adapter for two TextViews and an ImageView, the adapter is not
        // going to use this second argument, so it can be any value. Here, we used 0.
        super(context, 0, journalItem);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Gets the AndroidFlavor object from the ArrayAdapter at the appropriate position


        // Adapters recycle views to AdapterViews.
        // If this is a new View object we're getting, then inflate the layout.
        // If not, this view already has the layout inflated from a previous call to getView,
        // and we modify the View widgets as usual.

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.journal_item_card, parent, false);
        }

        //ImageView iconView = (ImageView) convertView.findViewById(R.id.list_item_icon);
        //iconView.setImageResource(androidFlavor.image);

        final JournalEntry currentJournalItem = getItem(position);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        TextView dateTextView = (TextView) convertView.findViewById(R.id.date);
        TextView titleTextView = (TextView) convertView.findViewById(R.id.journal_title);
        TextView jEntryTextView = (TextView) convertView.findViewById(R.id.journal_entry);
        ImageView delJournalView = convertView.findViewById(R.id.ivDelete);
        assert currentJournalItem != null;
        titleTextView.setText(currentJournalItem.getTitle());
        jEntryTextView.setText(currentJournalItem.getJournalContent());
        dateTextView.setText(currentJournalItem.getDate());
        delJournalView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //remove selected item from database
                mDatabase.child("users").child(mUserId).child("items").child(currentJournalItem.getKey()).setValue(null);
                remove(currentJournalItem);
                Toast.makeText(getContext(), "Journal Entry Deleted", Toast.LENGTH_SHORT).show();
            }
        });
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = currentJournalItem.getTitle().trim();
                String item = currentJournalItem.getJournalContent().trim();
                String date = currentJournalItem.getDate().trim();
                String key = currentJournalItem.getKey().trim();
                Intent intent = new Intent(getContext(),NewJournalActivity.class)
                        .putExtra("content", item);
                intent.putExtra("date", date);
                intent.putExtra("title", title);
                intent.putExtra("key", key);
                getContext().startActivity(intent);
            }
        });




        return convertView;
    }


}
