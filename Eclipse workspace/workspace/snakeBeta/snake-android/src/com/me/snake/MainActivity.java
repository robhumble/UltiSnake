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
import com.me.snake.*;


public class MainActivity extends AndroidApplication {	 

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
              
        	   //original code to run w/o ads
              AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
              cfg.useGL20 = false;
              
              initialize(new SnakeGame(), cfg);        
    }
}