package deeplife.gcme.com.deeplife;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
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

public class Register extends Activity{

	//private EditText ed_name,ed_password,ed_phone,ed_email,ed_country;
	private Button  Register;
    private EditText Full_Name,Email,Phone,Country,Pass;

    // Progress Dialog
    private ProgressDialog pDialog;

    Database dbadapter;
    DeepLife dbhelper;

    // JSON parser class
    JSONParser jsonParser;
    
    //php login script
    
    private static final String LOGIN_URL = "http://192.168.137.1/deeplife/register.php";

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private Context myContext;

    protected void onCreate(Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_NO_TITLE);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);

        dbadapter = new Database(this);
        dbhelper = new DeepLife();
        myContext = this;
        jsonParser = new JSONParser();
        Init();

	}

    private void Init(){
        Full_Name = (EditText) findViewById(R.id.signup_first_name);
        Email = (EditText) findViewById(R.id.signup_email);
        Phone = (EditText) findViewById(R.id.signup_phone);
        Country = (EditText) findViewById(R.id.signup_country);
        Pass = (EditText) findViewById(R.id.signup_password);

        Register =  (Button) findViewById(R.id.btnregister);
        Register.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("Task", "Register"));
                params.add(new BasicNameValuePair("Full_Name", Full_Name.getText().toString()));
                params.add(new BasicNameValuePair("Password", Pass.getText().toString()));
                params.add(new BasicNameValuePair("Email", Email.getText().toString()));
                params.add(new BasicNameValuePair("Email_Phone", Email.getText().toString()));
                params.add(new BasicNameValuePair("Phone", Phone.getText().toString()));
                params.add(new BasicNameValuePair("Pic", Country.getText().toString()));
                params.add(new BasicNameValuePair("Country", Country.getText().toString()));
                new Make_Request(params).execute();
            }
        });
    }
    public class Make_Request extends AsyncTask<String, String, String>{
        private List<NameValuePair> _params = new ArrayList<NameValuePair>();
        private String Result,msg;
        private JSONArray Req_Res = new JSONArray();
        private ProgressDialog myDialog;


        public Make_Request(List<NameValuePair> param){
            _params = param;
            msg = "-";
        }
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            myDialog = new ProgressDialog(myContext);
            myDialog.setTitle("Authenticating ...");
            myDialog.setCancelable(true);
            myDialog.show();
            Toast.makeText(myContext, "Request Started", Toast.LENGTH_SHORT).show();
        }
        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            try {
                JSONObject myObject = jsonParser.makeHttpRequest("http://192.168.137.1/Deeplife-Android-php-C4tk/API.php", "POST", _params);
                Req_Res = myObject.getJSONArray("User_Profile");
                msg = Req_Res.toString();
            } catch (Exception e) {
                // TODO: handle exception
                msg = e.toString();
            }
            return null;
        }
        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            //Toast.makeText(myContext, msg, Toast.LENGTH_LONG).show();
            if(Req_Res.length()>0){
                try {
                    DeepLife.Register_Profile(Req_Res);
                    Toast.makeText(myContext, msg, Toast.LENGTH_LONG).show();

                    Intent service = new Intent(Register.this,Service.class);
                    startService(service);

                    Intent intent = new Intent(getApplicationContext(), MainMenu.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            myDialog.cancel();
        }

    }
		 

}
