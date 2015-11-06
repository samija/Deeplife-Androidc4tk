package deeplife.gcme.com.deeplife.Activities;



import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Window;

import deeplife.gcme.com.deeplife.R;
import deeplife.gcme.com.deeplife.Database.Database;
import deeplife.gcme.com.deeplife.Database.DeepLife;
import deeplife.gcme.com.deeplife.Registration.Login;


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

	}

	@Override
	protected void onDestroy() {

		super.onDestroy();
	}
}
