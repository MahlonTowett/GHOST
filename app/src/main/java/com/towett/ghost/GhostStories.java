package com.towett.ghost;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.towett.ghost.stories.Japanese;
import com.towett.ghost.stories.three;

public class GhostStories extends AppCompatActivity {
private Button three,japanese,cooper,expression,farewell,the;
private Button imageButton;
private InterstitialAd mInterstitialAd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ghost_stories);
loadAd();

        three=findViewById(R.id.three);
        japanese=findViewById(R.id.Japanese);
        cooper=findViewById(R.id.cooper);
        expression=findViewById(R.id.Expression);
        farewell=findViewById(R.id.farewell);
        the=findViewById(R.id.the);

        three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(GhostStories.this, com.towett.ghost.stories.three.class);
                startActivity(intent);

            }
        });


        japanese.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(GhostStories.this, com.towett.ghost.stories.Japanese.class);
                startActivity(intent);

            }
        });



        cooper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(GhostStories.this, com.towett.ghost.stories.Cooper.class);
                startActivity(intent);

            }
        });



        expression.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(GhostStories.this, com.towett.ghost.stories.Expression.class);
                startActivity(intent);

            }
        });


        farewell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(GhostStories.this, com.towett.ghost.stories.Farewell.class);
                startActivity(intent);

            }
        });


        the.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(GhostStories.this, com.towett.ghost.stories.The.class);
                startActivity(intent);

            }
        });

        imageButton=findViewById(R.id.homebtn);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(GhostStories.this, HomeActivity.class);
                if (mInterstitialAd != null) {
                    mInterstitialAd.show(GhostStories.this);
                    mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback(){
                        @Override
                        public void onAdDismissedFullScreenContent() {
                            // Called when fullscreen content is dismissed.

                            startActivity(intent);
                            finish();

                        }

                        @Override
                        public void onAdFailedToShowFullScreenContent(AdError adError) {
                            // Called when fullscreen content failed to show.

                            Log.d("TAG", "The ad failed to show.");
                        }

                    });

                } else {
                    loadAd();
                    //load add again as above and visit second activity

                    startActivity(intent);
                    finish();
                }
            }
        });
    }
    @Override
    public void onBackPressed(){
        //Do Nothing
    }

    public void loadAd() {
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(
                this,
                "ca-app-pub-3799945535527779/7935504181",
                adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.

                        GhostStories.this.mInterstitialAd = interstitialAd;
                        Log.i(String.valueOf(TAG), "onAdLoaded");

                        interstitialAd.setFullScreenContentCallback(
                                new FullScreenContentCallback() {
                                    @Override
                                    public void onAdDismissedFullScreenContent() {
                                        // Called when fullscreen content is dismissed.
                                        // Make sure to set your reference to null so you don't
                                        // show it a second time.
                                        GhostStories.this.mInterstitialAd = null;
                                        Log.d("TAG", "The ad was dismissed.");
                                    }

                                    @Override
                                    public void onAdFailedToShowFullScreenContent(AdError adError) {
                                        // Called when fullscreen content failed to show.
                                        // Make sure to set your reference to null so you don't
                                        // show it a second time.
                                        GhostStories.this.mInterstitialAd = null;
                                        Log.d("TAG", "The ad failed to show.");
                                    }

                                    @Override
                                    public void onAdShowedFullScreenContent() {
                                        // Called when fullscreen content is shown.
                                        Log.d("TAG", "The ad was shown.");
                                    }
                                });
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error

                        Log.i(String.valueOf(TAG), loadAdError.getMessage());
                        mInterstitialAd = null;

                        String error =
                                String.format(
                                        "domain: %s, code: %d, message: %s",
                                        loadAdError.getDomain(), loadAdError.getCode(), loadAdError.getMessage());

                    }
                });
    }

}