package deeplife.gcme.com.deeplife.Registration;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import deeplife.gcme.com.deeplife.Database.Database;
import deeplife.gcme.com.deeplife.Database.DeepLife;
import deeplife.gcme.com.deeplife.Parsers.JSONParser;
import deeplife.gcme.com.deeplife.Activities.MainMenu;
import deeplife.gcme.com.deeplife.R;
import deeplife.gcme.com.deeplife.Services.Service;


public class Login extends Activity {

     // Progress Dialog
    private ProgressDialog pDialog;
    private Database myDatabase;

    private Context myContext;
    // JSON parser class
    JSONParser jsonParser = new JSONParser();

    String TAG = "Deep Life";

    public static final int currentUserId = 0;
    private static final String LOGIN_URL = "http://api.cccsea.org/API.php";

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    EditText ed_phoneNumber;
    EditText ed_password;
    Button bt_login;

    Button btn_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        myDatabase = new Database(this);
        myContext = this;
        ed_phoneNumber = (EditText) findViewById(R.id.login_phone);
        ed_password = (EditText) findViewById(R.id.login_password);
        bt_login = (Button) findViewById(R.id.btnLogin);
        btn_register = (Button) findViewById(R.id.btnLinkToRegisterScreen);


        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    new AttemptLogin().execute();
                }catch (Exception e){
                    Log.i("Sync_Service", "Logging has incountered error:");
                    Log.i("Sync_Service", "Error occurred:\n"+e.getMessage());
                }

            }
        });

        btn_register.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                Intent register = new Intent(getBaseContext(), Register.class);
                startActivity(register);
            }
        });
    }

    public void checkLogin(String phone, String password){

        DefaultHttpClient httpclient = new DefaultHttpClient();
        try {
            HttpPost httppost = new HttpPost(LOGIN_URL); // server

            HttpResponse response =  httpclient.execute(httppost);

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }




    class AttemptLogin extends AsyncTask<String, String, String> {

        boolean failure = false;
        private JSONArray Req_Res = new JSONArray();
        String success,phone,password,msg;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            phone = ed_phoneNumber.getText().toString();
            password = ed_password.getText().toString();

            pDialog = new ProgressDialog(Login.this);
            pDialog.setMessage("Attempting login...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
            msg = "";
        }

        @Override
        protected String doInBackground(String... args) {
            // TODO Auto-generated method stub
            // Check for success tag

            try {

                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("Task", "Authenticate"));
                params.add(new BasicNameValuePair("Email_Phone", phone));
                params.add(new BasicNameValuePair("Password", password));

                Log.d("request!", "starting");

                // getting product details by making HTTP request
                //JSONParser jsonParser = new JSONParser();
                JSONObject json = jsonParser.makeHttpRequest(LOGIN_URL, "POST", params);
                Req_Res = json.getJSONArray("User_Profile");

                // json success tag
                success = json.getString("Task");
                msg = json.toString();
            } catch (JSONException e) {
                e.printStackTrace();
                msg = e.toString();
            }

            return null;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pDialog.dismiss();
            if (Req_Res.length() >0) {
                // Log.d("Login Successful!", json.toString());
                Toast.makeText(myContext, "Successful logged in", Toast.LENGTH_LONG).show();
                try {
                    DeepLife.Register_Profile(Req_Res);

                    Intent intent = new Intent(myContext, Service.class);
                    startService(intent);

                    Intent i = new Intent(Login.this, MainMenu.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                    finish();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // return json.getString(TAG_MESSAGE);
            }else{
                // Log.d("Login Failure!", json.getString("Msg"));
                Toast.makeText(myContext, "Invalid user name and password used!", Toast.LENGTH_LONG).show();
            }
        }
    }


}
