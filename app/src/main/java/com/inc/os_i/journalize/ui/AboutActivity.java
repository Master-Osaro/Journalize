package com.inc.os_i.journalize.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.inc.os_i.journalize.R;

/**
 *   An About Activity that makes use of the About Fragment in this app. Used it to I know right? Another
 *   overkill.
 * */


public class AboutActivity extends AppCompatActivity
        implements AppBarLayout.OnOffsetChangedListener {

    private static final int PERCENTAGE_TO_ANIMATE_AVATAR = 20;
    private boolean mIsAvatarShown = true;

    private ImageView mProfileImage;
    private int mMaxScrollSize;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        //Map all the views to their ID's
        TabLayout tabLayout =  findViewById(R.id.materialup_tabs);
        ViewPager viewPager  = findViewById(R.id.materialup_viewpager);
        AppBarLayout appbarLayout =  findViewById(R.id.materialup_appbar);
        mProfileImage =  findViewById(R.id.materialup_profile_image);
        FloatingActionButton fab = findViewById(R.id.aboutFab);
       FloatingActionButton fab_call = findViewById(R.id.phoneFab);
        FloatingActionButton fab_twitter = findViewById(R.id.twitterFab);

        //An an onclick listener to the Floating Action button that sens a mail
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:"));//only email apps should handle this
                intent.putExtra(intent.EXTRA_EMAIL, new String[] { "saro9600@hotmail.com" });
                intent.putExtra(intent.EXTRA_SUBJECT, "RE: REACH OUT TO OSARO IYOHA");

                if(intent.resolveActivity(getPackageManager())!=null){
                    startActivity(intent);
                }
            }
        });

        fab_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone = "+2348173400967";
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                startActivity(intent);
                if(intent.resolveActivity(getPackageManager())!=null){
                    startActivity(intent);
                }
            }
        });

        fab_twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = null;
                try {
                    // get the Twitter app if possible
                    AboutActivity.this.getPackageManager().getPackageInfo("com.twitter.android", 0);
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?user_id=369664831"));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                } catch (Exception e) {
                    // no Twitter app, revert to browser
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/iyoha_osaro"));
                }
                AboutActivity.this.startActivity(intent);
            }
        });


        Toolbar toolbar = (Toolbar) findViewById(R.id.materialup_toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                onBackPressed();
            }
        });

        //Add a Listener to the appbar Layout to detect when the scrolling offset is changed
        appbarLayout.addOnOffsetChangedListener(this);
        mMaxScrollSize = appbarLayout.getTotalScrollRange();

        viewPager.setAdapter(new TabsAdapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);
    }

    public static void start(Context c) {
        c.startActivity(new Intent(c, AboutActivity.class));
    }


    //Handles the animation of the Images when the user scrolls
    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
        if (mMaxScrollSize == 0)
            mMaxScrollSize = appBarLayout.getTotalScrollRange();

        int percentage = (Math.abs(i)) * 100 / mMaxScrollSize;

        if (percentage >= PERCENTAGE_TO_ANIMATE_AVATAR && mIsAvatarShown) {
            mIsAvatarShown = false;

            mProfileImage.animate()
                    .scaleY(0).scaleX(0)
                    .setDuration(200)
                    .start();
        }

        if (percentage <= PERCENTAGE_TO_ANIMATE_AVATAR && !mIsAvatarShown) {
            mIsAvatarShown = true;

            mProfileImage.animate()
                    .scaleY(1).scaleX(1)
                    .start();
        }
    }

    private static class TabsAdapter extends FragmentPagerAdapter {
        private static final int TAB_COUNT = 1;

        TabsAdapter(FragmentManager fm) {
            super(fm);
        }

        //Gets tabCount
        @Override
        public int getCount() {
            return TAB_COUNT;
        }

        //Gets an instance of the about Fragment
        @Override
        public Fragment getItem(int i) {
            return AboutPageFragment.newInstance();
        }

        //Displays page title of the fragment in the activity
        @Override
        public CharSequence getPageTitle(int position) {
            //return "Tab " + String.valueOf(position);
            return "Journalize";
        }
    }
}
