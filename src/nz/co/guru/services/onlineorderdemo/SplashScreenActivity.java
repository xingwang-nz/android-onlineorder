package nz.co.guru.services.onlineorderdemo;

import nz.co.guru.services.onlineorderdemo.settings.SettingsActivity;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;

public class SplashScreenActivity extends Activity {

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        // check if display splash screen is set
        final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        final boolean displaySplash = sharedPref.getBoolean(SettingsActivity.KEY_PREF_DISPLAY_SPLASH_SCREEN, true);

        if (displaySplash) {

            new Handler().postDelayed(new Runnable() {

                /*
                 * Showing splash screen with a timer. This will be useful when you
                 */

                @Override
                public void run() {
                    // This method will be executed once the timer is over
                    // Start your app main activity
                    displayLoginScreen();
                }
            }, SPLASH_TIME_OUT);
        }
        else {
            displayLoginScreen();
        }
    }

    private void displayLoginScreen() {
        final Intent i = new Intent(SplashScreenActivity.this, LoginActivity.class);
        startActivity(i);
        // close this activity
        finish();
    }

}