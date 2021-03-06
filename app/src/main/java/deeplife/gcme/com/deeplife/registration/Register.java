package deeplife.gcme.com.deeplife.Registration;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import deeplife.gcme.com.deeplife.Models.CountryDetails;
import deeplife.gcme.com.deeplife.Database.DeepLife;
import deeplife.gcme.com.deeplife.Parsers.JSONParser;
import deeplife.gcme.com.deeplife.Activities.MainMenu;
import deeplife.gcme.com.deeplife.R;
import deeplife.gcme.com.deeplife.Services.Service;
import deeplife.gcme.com.deeplife.Database.Database;

public class Register extends Activity{

	//private EditText ed_name,ed_password,ed_phone,ed_email,ed_country;
	private Button  Register;
    private EditText Full_Name,Email,Phone,Country,Pass,Ed_Codes;
    private Spinner sp_countries, sp_gender;
    // Progress Dialog
    private ProgressDialog pDialog;

    Database dbadapter;
    DeepLife dbhelper;

    // JSON parser class
    JSONParser jsonParser;
    
    //php login script
    
    private static final String LOGIN_URL = "http://api.cccsea.org/API.php";

    private Context myContext;

    protected void onCreate(Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_NO_TITLE);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);

        final String[] list = CountryDetails.country;
        final String[] codes = CountryDetails.code;

        dbadapter = new Database(this);
        dbhelper = new DeepLife();
        myContext = this;
        jsonParser = new JSONParser();

        Init();

        sp_countries = (Spinner) findViewById(R.id.signup_countries_spinner);
        sp_countries.setAdapter(new MySpinnerAdapter(this, R.layout.countries_spinner, list));

        sp_countries.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int i = sp_countries.getSelectedItemPosition();
                Ed_Codes.setText(codes[i]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Ed_Codes.setText(codes[0]);
            }
        });

	}

    private void Init(){

        Full_Name = (EditText) findViewById(R.id.signup_first_name);
        Email = (EditText) findViewById(R.id.signup_email);
        Phone = (EditText) findViewById(R.id.signup_phone);
        Pass = (EditText) findViewById(R.id.signup_password);
        Ed_Codes = (EditText) findViewById(R.id.signup_code);
        Register =  (Button) findViewById(R.id.btnregister);
        sp_gender = (Spinner) findViewById(R.id.register_gender_spinner);


        Register.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("Task", "Register"));
                params.add(new BasicNameValuePair("Full_Name", Full_Name.getText().toString()));
                params.add(new BasicNameValuePair("Password", Pass.getText().toString()));
                params.add(new BasicNameValuePair("Email", Email.getText().toString()));
                params.add(new BasicNameValuePair("Phone", Ed_Codes.getText().toString() + Phone.getText().toString()));
                params.add(new BasicNameValuePair("Build_phase", "Added"));
                params.add(new BasicNameValuePair("Gender", sp_gender.getSelectedItem().toString()));
                params.add(new BasicNameValuePair("Build_phase", Phone.getText().toString()));
                params.add(new BasicNameValuePair("Picture", null));
                params.add(new BasicNameValuePair("Country", sp_countries.getSelectedItem().toString()));
                new Make_Request(params).execute();
                Log.i("Sync_Service", "Registering user");
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
            Log.i("Sync_Service", "Registering service initialized");
        }
        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            try {
                JSONObject myObject = jsonParser.makeHttpRequest(LOGIN_URL, "POST", _params);
                Req_Res = myObject.getJSONArray("User_Profile");
                msg = Req_Res.toString();
                Log.i("Sync_Service", "Registering process in progress...");
            } catch (Exception e) {
                // TODO: handle exception
                msg = e.toString();
                Log.i("Sync_Service", "Registering error occured \n");
                Log.i("Sync_Service", msg);
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
                    Log.i("Sync_Service", "Registering user profile");

                    Intent service = new Intent(Register.this,Service.class);
                    startService(service);
                    Log.i("Sync_Service", "Registering service starting new service");
                    Intent intent = new Intent(getApplicationContext(), MainMenu.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    Log.i("Sync_Service", "Registering user profile incounter error\n");
                    Log.i("Sync_Service", e.toString());
                }
            }
            myDialog.cancel();
        }

    }

    public class MySpinnerAdapter extends ArrayAdapter<String> {

        String[] object;
        public MySpinnerAdapter(Context ctx, int txtViewResourceId, String[] objects) {
            super(ctx, txtViewResourceId, objects);
            this.object = objects;
        }

        @Override
        public View getDropDownView(int position, View cnvtView, ViewGroup prnt) {
            return getCustomView(position, cnvtView, prnt);
        }
        @Override
        public View getView(int pos, View cnvtView, ViewGroup prnt) {
            return getCustomView(pos, cnvtView, prnt);
        }
        public View getCustomView(int position, View convertView,
                                  ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View mySpinner = inflater.inflate(R.layout.countries_spinner, parent,
                    false);
            TextView main_text = (TextView) mySpinner
                    .findViewById(R.id.spinner_text);
            main_text.setText(object[position]);

            return mySpinner;
        }
    }
}
