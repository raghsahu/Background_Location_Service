package dev.raghav.icsmap;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


public class SplashActivity extends AppCompatActivity {
    SessionManager manager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        manager =new SessionManager(SplashActivity.this);

        Thread myThread = new Thread(new MyThreadAction());
        myThread.start();
    }

    class MyThreadAction implements Runnable {
        @Override
        public void run() {
            //WRITE YOUR ACTION HERE
            try {
                Thread.sleep(1000*3);
            } catch (Exception ex) {
            }

            try{
                if (manager.isLoggedIn()) {

                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    SplashActivity.this.finish();
                } else {
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                    SplashActivity.this.finish();
                }

//
//                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
//                    startActivity(intent);
//                    SplashActivity.this.finish();


            }catch (Exception e)
            {


            }

        }
    }

}
