package deeplife.gcme.com.deeplife.Activities;



import android.content.ContentValues;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
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


		new Handler().postAtTime(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				getNextActivity();
			}
		}, 5000);

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

/*
        Intent intent = new Intent(this, WinActivity.class);
        startActivity(intent);
        finish();


        //Populate questions
        ContentValues cv1 = new ContentValues();
        cv1.put(DeepLife.QUESTION_LIST_FIELDS[0],"WIN");
        cv1.put(DeepLife.QUESTION_LIST_FIELDS[1],"Have you presented the ministry of the Holy Spirit to your disciple?");
        cv1.put(DeepLife.QUESTION_LIST_FIELDS[2],"This is win Note");
        cv1.put(DeepLife.QUESTION_LIST_FIELDS[3],"NOT_MANDATORY");

        //populate
        ContentValues cv2 = new ContentValues();
        cv2.put(DeepLife.QUESTION_LIST_FIELDS[0],"BUILD");
        cv2.put(DeepLife.QUESTION_LIST_FIELDS[1],"Is your disciple engaging in active groups like churches, fellowships, small groups, online communities? ");
        cv2.put(DeepLife.QUESTION_LIST_FIELDS[2],"This is build Note");
        cv2.put(DeepLife.QUESTION_LIST_FIELDS[3], "MANDATORY");

        //populate
        ContentValues cv3 = new ContentValues();
        cv3.put(DeepLife.QUESTION_LIST_FIELDS[0],"SEND");
        cv3.put(DeepLife.QUESTION_LIST_FIELDS[1],"Has your disciple been entrusted with an opportunity for action?  ");
        cv3.put(DeepLife.QUESTION_LIST_FIELDS[2],"This is send Note");
        cv3.put(DeepLife.QUESTION_LIST_FIELDS[3], "NOT_MANDATORY");


        long win = myDatabase.insert(DeepLife.Table_QUESTION_LIST,cv1);
        if(win!=-1){
            Log.i("Deep Life", "Win question added");
        }


        long build = myDatabase.insert(DeepLife.Table_QUESTION_LIST,cv2);
        if(build!=-1){
            Log.i("Deep Life", "build question added");
        }


        long send = myDatabase.insert(DeepLife.Table_QUESTION_LIST,cv3);
        if(send!=-1){
            Log.i("Deep Life", "send question added");
        }


*/

        int count = myDatabase.count(DeepLife.Table_USER);
        if(count == 1){
            Intent intent = new Intent(getApplicationContext(), MainMenu.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            myDatabase.dispose();
            finish();
        }
        else {
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
            myDatabase.dispose();
            finish();
        }

	}

	@Override
	protected void onDestroy() {

		super.onDestroy();
	}
}
