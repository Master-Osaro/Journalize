package com.inc.os_i.journalize;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.inc.os_i.journalize.adapter.JournalAdapter;
import com.inc.os_i.journalize.model.JournalEntry;
import com.inc.os_i.journalize.ui.AboutActivity;
import com.inc.os_i.journalize.ui.LogInActivity;
import com.inc.os_i.journalize.ui.NewJournalActivity;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks  {

    private static final String TAG = MainActivity.class.getSimpleName();

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference mDatabase;
    private String mUserId, mKey;
    protected GoogleApiClient mGoogleApiClient;
    private ArrayList<JournalEntry> journalItems;

    static {
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        FloatingActionButton btnNewJournalEntry = findViewById(R.id.fab);
        btnNewJournalEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, NewJournalActivity.class));
            }
        });


        //The arraylist that will hold the journal entries
        journalItems = new ArrayList<JournalEntry>();
        DatabaseReference titleRef = FirebaseDatabase.getInstance().getReference("items");
        titleRef.keepSynced(true);
        // Initialize Firebase Auth and Database Reference
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        //connect to the current google account
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                       ///
                    }
                }).addApi(Auth.GOOGLE_SIGN_IN_API).build();


        if (mFirebaseUser == null) {
            // Not logged in, launch the Log In activity
            loadLogInView();
        } else {
            mUserId = mFirebaseUser.getUid();
            // Set up ListView
            final ListView listView = (ListView) findViewById(R.id.listView);
            listView.setEmptyView(findViewById(R.id.emptyElement));
            final JournalAdapter adapter = new JournalAdapter(MainActivity.this,journalItems);

            // Use Firebase to populate the list.
            mDatabase.child("users").child(mUserId).child("items").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    //adapter.add((String) dataSnapshot.child("journalContent").getValue());
                    journalItems.add(new JournalEntry((String) dataSnapshot.child("title").getValue() ,(String) dataSnapshot.child("journalContent").getValue(),(String) dataSnapshot.child("date").getValue(),(String)dataSnapshot.child("key").getValue()));
                    //Log.e("Check Response",(String) dataSnapshot.child("title").getValue());
                    adapter.notifyDataSetChanged();
                    listView.invalidate();

                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                    journalItems.remove(new JournalEntry((String) dataSnapshot.child("title").getValue(),(String) dataSnapshot.child("journalContent").getValue(),(String) dataSnapshot.child("date").getValue()));
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            listView.setAdapter(adapter);
        }

    }

    /**
     * manually opening / closing bottom sheet on button click
     */

    private void loadLogInView() {
        Intent intent = new Intent(this, LogInActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) {}
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if(dir!= null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_logout) {
            if(mFirebaseAuth!=null) {
                mFirebaseAuth.signOut();
            }
            if (mGoogleApiClient.hasConnectedApi(Auth.GOOGLE_SIGN_IN_API)) {
                mGoogleApiClient.clearDefaultAccountAndReconnect();
            }
            deleteCache(MainActivity.this);
            loadLogInView();
            return true;
        }
        if(id == R.id.action_about){
            startActivity(new Intent(MainActivity.this, AboutActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    public void showErrorDialog(){
        //Show this
        AlertDialog alertDialog = new AlertDialog.Builder(getApplicationContext()).create();
        alertDialog.setTitle("Info");
        alertDialog.setMessage("Journal Entry cannot be empty");
        alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "WIFI", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //
            }
        });

        alertDialog.show();
    }

}
