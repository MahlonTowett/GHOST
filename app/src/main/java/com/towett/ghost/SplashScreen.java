package com.towett.ghost;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class SplashScreen extends AppCompatActivity {

    private ProgressBar progressBar;
    private TextView loadingTextView;
    private Timer timer;
    private int dotCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        progressBar = findViewById(R.id.progressBar);
        loadingTextView = findViewById(R.id.loadingTextView);

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                dotCount++;
                if (dotCount > 3) {
                    dotCount = 1;
                }
                String dots = "";
                for (int i = 0; i < dotCount; i++) {
                    dots += ".";
                }
                String finalDots = dots;
                runOnUiThread(() -> loadingTextView.setText("Loading" + finalDots));
                runOnUiThread(() -> progressBar.incrementProgressBy(1));

                if (progressBar.getProgress() == progressBar.getMax()) {
                    timer.cancel();
                    Intent intent = new Intent(SplashScreen.this, Launch.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, 0, 33);
    }

}