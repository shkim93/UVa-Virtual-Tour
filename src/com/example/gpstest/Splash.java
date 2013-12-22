package com.example.gpstest;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;

public class Splash extends Activity {

	private Thread mSplashThread;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashlayout);
        
        final Splash splash = this;
        
        mSplashThread = new Thread(){
        	@Override
        	public void run(){
        		try{
        			synchronized(this){
        				wait(5000);
        			}
        		}
        		catch(InterruptedException ex){
        		}
        		finish();
        		
        		Intent intent = new Intent();
        		intent.setClass(splash, MainActivity.class);
        		startActivity(intent);
        		stop();
        	}
        };
        
        mSplashThread.start();
        
    }
    
    

}
