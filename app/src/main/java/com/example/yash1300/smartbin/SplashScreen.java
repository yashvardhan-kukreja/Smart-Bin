package com.example.yash1300.smartbin;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

public class SplashScreen extends AppCompatActivity {
LinearLayout mainItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        mainItem = findViewById(R.id.mainItem);
        YoYo.with(Techniques.FadeIn).duration(2500).repeat(1).playOn(mainItem);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity((new Intent(SplashScreen.this, MainActivity.class)));
            }
        }, 3000);
    }
}
