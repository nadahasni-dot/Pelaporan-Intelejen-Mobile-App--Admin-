package com.example.pelaporanbakesbangpolmalang;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.pelaporanbakesbangpolmalang.helper.SessionHelper;

public class MainActivity extends AppCompatActivity {
    SessionHelper sessionHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sessionHelper = new SessionHelper(this);
        if(sessionHelper.isLogin()) {
            startActivity(new Intent(MainActivity.this, HomeActivity.class));
            finish();
        } else {
            //  intent ke login setelah beberapa detik
            Thread thread = new Thread() {
                public void run() {
                    try {
                        sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        MainActivity.this.finish();
                    }
                }
            };

            thread.start();
        }
    }
}