package deeplife.gcme.com.deeplife;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import deeplife.gcme.com.deeplife.database.Database;

public class Register extends Activity implements OnClickListener{
	
	private EditText ed_name,ed_password,ed_phone,ed_email,ed_country;
	private Button  mRegister;
	
	 // Progress Dialog
    private ProgressDialog pDialog;

    Database dbadapter;
    DeepLife dbhelper;

    // JSON parser class
    JSONParser jsonParser = new JSONParser();
    
    //php login script
    
    private static final String LOGIN_URL = "http://192.168.137.1/deeplife/register.php";

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";



	@Override
	protected void onCreate(Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_NO_TITLE);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);

        dbadapter = new Database(this);
        dbhelper = new DeepLife();


        ed_name = (EditText)findViewById(R.id.signup_first_name);
        ed_password = (EditText)findViewById(R.id.signup_password);
        ed_email = (EditText) findViewById(R.id.signup_email);
        ed_phone = (EditText) findViewById(R.id.signup_phone);
        ed_country = (EditText) findViewById(R.id.signup_country);
		mRegister = (Button)findViewById(R.id.btnregister);

		mRegister.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		  new CreateUser().execute();
		
	}
	
	class CreateUser extends AsyncTask<String, String, String> {

		 /**
         * Before starting background thread Show Progress Dialog
         * */
        String name;
        String password;
        String phone;
        String email;
        String country;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Register.this);
            pDialog.setMessage("Creating User...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();

            name = ed_name.getText().toString();
            password = ed_password.getText().toString();
            phone = ed_phone.getText().toString();
            email = ed_email.getText().toString();
            country = ed_country.getText().toString();
        }
		
		@Override
		protected String doInBackground(String... args) {
			// TODO Auto-generated method stub
            int success;
            try {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("fname", name));
                params.add(new BasicNameValuePair("password", password));
                params.add(new BasicNameValuePair("phone", phone));
                params.add(new BasicNameValuePair("email", email));
                params.add(new BasicNameValuePair("country", country));

                ContentValues contents = new ContentValues();
                contents.put(dbhelper.USER_FIELDS[0], name);
                contents.put(dbhelper.USER_FIELDS[1], phone);
                contents.put(dbhelper.USER_FIELDS[2],email);
                contents.put(dbhelper.USER_FIELDS[3], "Added");
                contents.put(dbhelper.USER_FIELDS[4], country);

                Log.d("request!", "request starting");

                JSONObject json = jsonParser.makeHttpRequest(
                       LOGIN_URL, "POST", params);

                //success = json.getInt(TAG_SUCCESS);
                success = 1;

                if (success == 1) {
                	Log.d("User Created!", json.toString());
                    long i = dbadapter.insert(dbhelper.Table_USER, contents);
                    if(i!=-1){
                        Log.i("Deeplife","Successfully added to local database");
                    }
                    Intent intent = new Intent(Register.this, Login.class);
                    startActivity(intent);
                	finish();
                    return "Succssfully registered!";
                    //return json.getString(TAG_MESSAGE);
                }else{
                	Log.d("Login Failure!", json.getString(TAG_MESSAGE));
                	return json.getString(TAG_MESSAGE);
                	
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
 
            return null;
			
		}

        protected void onPostExecute(String file_url) {
            // dismiss the dialog once product deleted
            pDialog.dismiss();
            if (file_url != null){
            	Toast.makeText(Register.this, file_url, Toast.LENGTH_LONG).show();
            }
 
        }
		
	}
		 

}
