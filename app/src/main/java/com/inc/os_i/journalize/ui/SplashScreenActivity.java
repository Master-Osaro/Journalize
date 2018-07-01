package com.inc.os_i.journalize.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.inc.os_i.journalize.R;

import io.supercharge.shimmerlayout.ShimmerLayout;

/**
 * A splashscreen, Just for the fun of it :-)
 * */
public class SplashScreenActivity extends Activity {

    //A variable holding the timeout for the splashcreen in milliseconds
    private static int SPLASH_TIMEOUT = 3000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        //Oh, a little shimmer for logo animation
        ShimmerLayout shimmerText = findViewById(R.id.shimmer_view_container);
        shimmerText.startShimmerAnimation();

        //After the delay, move to the main Activity
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashScreenActivity.this, LogInActivity.class));
                finish();
            }
        }, SPLASH_TIMEOUT);
    }
}
