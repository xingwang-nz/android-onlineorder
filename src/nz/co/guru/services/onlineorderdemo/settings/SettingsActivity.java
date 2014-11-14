package nz.co.guru.services.onlineorderdemo.settings;

import android.app.Activity;
import android.os.Bundle;

public class SettingsActivity extends Activity {

    public static final String KEY_PREF_DISPLAY_SPLASH_SCREEN = "prefDisplaySplashScreen";

    public static final String KEY_PREF_DISPLAY_ADVERTISEMENT = "prefDisplayAdvertisement";

    public static final String KEY_PREF_LANGUAGE = "prefLanguage";

    /** Called when the activity is first created. */
    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Display the fragment as the main content.
        getFragmentManager().beginTransaction().replace(android.R.id.content, new SettingsFragment()).commit();

    }
}
