package id.or.qodr.jadwalkajian;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import id.or.qodr.jadwalkajian.data.SessionManager;
import okhttp3.OkHttpClient;

public class SplashScreen extends AppCompatActivity {

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        // Session Manager
        session = new SessionManager(getApplicationContext());

        if (getIntent().getBooleanExtra("EXIT", false)) {
            finish();
            return;
        }

        SharedPreferences pref = this.getSharedPreferences("first_launch", Context.MODE_PRIVATE);
        boolean first_launch = pref.getBoolean("first_launch", true);

        if(first_launch) {
            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean("first_launch", false);
            editor.commit();

            Thread thread= new Thread() {
                @Override
                public void run() {
                    super.run();

                    synchronized (this) {
                        try {
                            wait(SPLASH_TIME_OUT);
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                        finally {
                            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                            startActivity(intent);
                        }
                    }
                }
            };
            thread.start();
        } else {

            if (session.isLoggedIn()) {
                Intent send = new Intent(getApplicationContext(),
                        MainActivity.class);
                startActivity(send);
                Toast.makeText(SplashScreen.this, "Admin", Toast.LENGTH_SHORT).show();
            }
				/*
				 * if user login test is false on oncreate then redirect the
				 * user to login & registration page
				 */
            else if (!session.isLoggedIn()){

                Intent send = new Intent(getApplicationContext(),
                        MainActivity.class);
                startActivity(send);
                Toast.makeText(SplashScreen.this, "Home ", Toast.LENGTH_SHORT).show();

            }
        }
    }
}
