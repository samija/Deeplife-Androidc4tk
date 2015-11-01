package deeplife.gcme.com.deeplife;



import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.StrictMode;
import android.telephony.SignalStrength;
import android.util.Log;
import android.view.Menu;
import android.view.Window;

import deeplife.gcme.com.deeplife.database.Database;


public class Splash extends Activity {

	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        //StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().penaltyLog()
        //        .penaltyDeath().build());

    /*
		new Handler().postAtTime(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				getNextActivity();
			}
		}, 5000);
	*/
        Thread splash = new Thread(){
        	@Override
        	public void run() {
        		try {
        			sleep(1000);       			
        		} catch(InterruptedException e){        			
        		} finally {
        			getNextActivity();
        		}
        		//super.run();
        	}
        };
        
        splash.start();
	}
	public synchronized void getNextActivity() {


        Intent intent = new Intent(getApplicationContext(), MainMenu.class);
        startActivity(intent);
        finish();
        /*
        Database myDatabase = new Database(this);

        int count = myDatabase.count(DeepLife.Table_USER);
        if(count == 1){
            Intent intent = new Intent(getApplicationContext(), MainMenu.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
        else {
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
            finish();
        }
        */
	}

	@Override
	protected void onDestroy() {

		super.onDestroy();
	}
}
