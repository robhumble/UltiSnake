package com.me.snake;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.badlogic.gdx.backends.android.AndroidApplication;
//import com.google.ads.AdSize;
//import com.google.android.gms.ads.AdRequest;
//import com.google.android.gms.ads.AdView;
import com.me.snake.*;
//import com.badlogic.gdx.helloworld.IActivityRequestHandler;
//import com.mobclix.android.sdk.MobclixMMABannerXLAdView;
//import com.google.android.gms.ads.AdRequest;
//import com.google.android.gms.ads.AdSize;
//import com.google.android.gms.ads.AdView;


public class MainActivity extends AndroidApplication {
	
//	 protected AdView adView;

	 
	 private static final String AD_UNIT_ID = "ca-app-pub-3531448463845705/2265856074";

	private static final boolean NOADDS = true;
	 
	 
	 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        
        if(NOADDS)
        {
        	   //original code to run w/o ads
              AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
              cfg.useGL20 = false;
              
              initialize(new SnakeGame(), cfg);
        	
        }
        else
        {
        
        // Create the layout
        RelativeLayout layout = new RelativeLayout(this);

        // Do the stuff that initialize() would do for you
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

        // Create the libgdx View
        View gameView = initializeForView(new SnakeGame(), false);

        // Create and setup the AdMob view
       // AdView adView = new AdView(this, AdSize.BANNER, "xxxxxxxx"); // Put in your secret key here
        //adView.loadAd(new AdRequest());
        
     //   adView = new AdView(this);
     //   adView.setAdSize(AdSize.SMART_BANNER);
     //   adView.setAdUnitId(AD_UNIT_ID);

        //AdRequest adRequest = new AdRequest.Builder().build();
        //adRequest.
        
     //   AdRequest adRequest = new AdRequest.Builder()
        //.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)       // Emulator
     //   .addTestDevice("1B1028ED06101E9D0B1C9DB562A5ECCA") // for Test Ads on my gs3
     //   .build();
     
        
        // Start loading the ad in the background.
       // adView.loadAd(adRequest);
        
        
    
        
        // Add the libgdx view
        layout.addView(gameView);

        // Add the AdMob view
        RelativeLayout.LayoutParams adParams = 
            new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, 
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
        //adParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        adParams.addRule(RelativeLayout.ABOVE);
        //adParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

        //layout.addView(adView, adParams);

        // Hook it all up
        setContentView(layout);
    
        }
    }
}