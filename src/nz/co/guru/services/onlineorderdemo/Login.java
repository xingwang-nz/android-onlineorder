package nz.co.guru.services.onlineorderdemo;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends Activity {

    public static final String PREFS_NAME = "LoginPrefs";

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        /*
         * Check if we successfully logged in before.
         * If we did, redirect to home page
         */
        final SharedPreferences settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        if (settings.getString("logged", "").toString().equals("logged")) {
            final Intent intent = new Intent(Login.this, MainActivity.class);
            startActivity(intent);
        }

        final Button b = (Button) findViewById(R.id.loginbutton);
        b.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(final View v) {
                final EditText username = (EditText) findViewById(R.id.login);
                final EditText password = (EditText) findViewById(R.id.password);

                if (username.getText().toString().length() > 0 && password.getText().toString().length() > 0) {
                    if (username.getText().toString().equalsIgnoreCase("demo") && password.getText().toString().equals("demo")) {
                        /*
                         * So login information is correct,
                         * we will save the Preference data
                         * and redirect to next class / home
                         */
                        final SharedPreferences settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
                        final SharedPreferences.Editor editor = settings.edit();
                        editor.putString("logged", "logged");
                        editor.commit();

                        final Intent intent = new Intent(Login.this, MainActivity.class);
                        startActivity(intent);
                    }
                    else {
                        Toast.makeText(getBaseContext(), "invalid login", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}