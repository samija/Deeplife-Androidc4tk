package deeplife.gcme.com.deeplife.Services;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import deeplife.gcme.com.deeplife.Database.DeepLife;
import deeplife.gcme.com.deeplife.Database.Database;
import deeplife.gcme.com.deeplife.Parsers.JSONParser;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Debug;
import android.os.IBinder;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;


public class Service extends android.app.Service{
	private Database myDatabase;
	private JSONParser myParser;
	private TelephonyManager Tele;
	private Context myContext;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		myDatabase = new Database(this);
		myParser = new JSONParser();
		Tele = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		myContext = this;
		new Make_Service().execute();
		int xx = myDatabase.count(DeepLife.Table_LOGS);
		Log.i("Sync_Service", "Sync Service Started");
	}
	public class Make_Service extends AsyncTask<String, String, String>{
		private String msg = "";
		private String Task = "";
		private List<NameValuePair> init(){
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("Email_Phone", myDatabase.get_Value_At_Top(DeepLife.Table_USER, DeepLife.USER_FIELDS[2])));
			params.add(new BasicNameValuePair("Password", myDatabase.get_Value_At_Top(DeepLife.Table_USER, DeepLife.USER_FIELDS[3])));

			if(myDatabase.count(DeepLife.Table_LOGS)>0){
				msg = "Table Log:";
				Cursor myCursor = myDatabase.getAll(DeepLife.Table_LOGS);
				myCursor.moveToFirst();
				String type = myCursor.getString(myCursor.getColumnIndex(DeepLife.LOGS_COLUMN[1]));
				String id = myCursor.getString(myCursor.getColumnIndex(DeepLife.LOGS_COLUMN[2]));
				if(type.equals("Send_Disciple")){
					Cursor cur = myDatabase.get_value_by_ID(DeepLife.Table_DISCIPLES, id);
					cur.moveToFirst();
					params.add(new BasicNameValuePair("Task", type));
					params.add(new BasicNameValuePair(DeepLife.DISCIPLES_COLUMN[0], cur.getString(cur.getColumnIndex(DeepLife.DISCIPLES_COLUMN[0]))));
					params.add(new BasicNameValuePair(DeepLife.DISCIPLES_COLUMN[1], cur.getString(cur.getColumnIndex(DeepLife.DISCIPLES_COLUMN[1]))));
					params.add(new BasicNameValuePair(DeepLife.DISCIPLES_COLUMN[2], cur.getString(cur.getColumnIndex(DeepLife.DISCIPLES_COLUMN[2]))));
					params.add(new BasicNameValuePair(DeepLife.DISCIPLES_COLUMN[3], cur.getString(cur.getColumnIndex(DeepLife.DISCIPLES_COLUMN[3]))));
					params.add(new BasicNameValuePair(DeepLife.DISCIPLES_COLUMN[4], cur.getString(cur.getColumnIndex(DeepLife.DISCIPLES_COLUMN[4]))));
					params.add(new BasicNameValuePair(DeepLife.DISCIPLES_COLUMN[5], cur.getString(cur.getColumnIndex(DeepLife.DISCIPLES_COLUMN[5]))));
					params.add(new BasicNameValuePair(DeepLife.DISCIPLES_COLUMN[6], cur.getString(cur.getColumnIndex(DeepLife.DISCIPLES_COLUMN[6]))));
					params.add(new BasicNameValuePair(DeepLife.DISCIPLES_COLUMN[7], cur.getString(cur.getColumnIndex(DeepLife.DISCIPLES_COLUMN[7]))));
					Log.i("Sync_Service", "Sending new disciple");
					Log.i("Sync_Service","Full Name: "+cur.getString(cur.getColumnIndex(DeepLife.DISCIPLES_COLUMN[1])));
				}else if(type.equals("Send_Schedule")){
					Cursor cur = myDatabase.get_value_by_ID(DeepLife.Table_SCHEDULES, id);
					cur.moveToFirst();
					params.add(new BasicNameValuePair("Task", type));
					params.add(new BasicNameValuePair(DeepLife.SCHEDULES_COLUMN[0], cur.getString(cur.getColumnIndex(DeepLife.SCHEDULES_COLUMN[0]))));
					params.add(new BasicNameValuePair(DeepLife.SCHEDULES_COLUMN[1], cur.getString(cur.getColumnIndex(DeepLife.SCHEDULES_COLUMN[1]))));
					params.add(new BasicNameValuePair(DeepLife.SCHEDULES_COLUMN[2], cur.getString(cur.getColumnIndex(DeepLife.SCHEDULES_COLUMN[2]))));
					params.add(new BasicNameValuePair(DeepLife.SCHEDULES_COLUMN[3], cur.getString(cur.getColumnIndex(DeepLife.SCHEDULES_COLUMN[3]))));
					params.add(new BasicNameValuePair(DeepLife.SCHEDULES_COLUMN[4], cur.getString(cur.getColumnIndex(DeepLife.SCHEDULES_COLUMN[4]))));
					Log.i("Sync_Service", "Sending new Schedules");
					Log.i("Sync_Service", "for User_ID: "+cur.getString(cur.getColumnIndex(DeepLife.SCHEDULES_COLUMN[0])));
				}else if(type.equals("Delete_User")){
					params.add(new BasicNameValuePair("Task", "Delete_User"));
					params.add(new BasicNameValuePair("Email_Phone", id));
					Log.i("Sync_Service", "Sending Delete User request for server");
					Log.i("Sync_Service", "for User_ID: " + id);
				}else if(type.equals("Update_Full_Name")){
					params.add(new BasicNameValuePair("Task", "Update_Full_Name"));
					params.add(new BasicNameValuePair("Full_Name", id));
					Log.i("Sync_Service", "Sending update user Full_Name");
					Log.i("Sync_Service", "to: --->" + id);
				}else if(type.equals("Update_Password")){
					params.add(new BasicNameValuePair("Task", "Update_Email"));
					params.add(new BasicNameValuePair("Email", id));
					Log.i("Sync_Service", "Sending update user Email");
					Log.i("Sync_Service", "to: --->" + id);
				}else if(type.equals("Update_Phone")){
					params.add(new BasicNameValuePair("Task", "Update_Phone"));
					params.add(new BasicNameValuePair("Phone", id));
					Log.i("Sync_Service", "Sending update user Phone_Number");
					Log.i("Sync_Service", "to: --->" + id);
				}else if(type.equals("Send_Report")){
					int count = myDatabase.count(DeepLife.Table_Reports);
					if(count>0){
						Cursor cur = myDatabase.get_value_by_ID(DeepLife.Table_Reports, id);
						params.add(new BasicNameValuePair("Task", "Send_Report"));
						for(int i=0;i<DeepLife.REPORTS_FIELDS.length;i++){
							params.add(new BasicNameValuePair(DeepLife.REPORTS_FIELDS[i],cur.getString(cur.getColumnIndex(DeepLife.REPORTS_FIELDS[i]))));
						}
					}
					Log.i("Sync_Service", "Sending Reports");
					Log.i("Sync_Service", "to: --->" + id);
				}
			}else{
				msg = "dd";
				// update for disciples
				if(myDatabase.count(DeepLife.Table_DISCIPLES)==0){
					params.add(new BasicNameValuePair("Task1", "My_Disciples"));
					Log.i("Sync_Service", "Requesting the Server for All of the Disciples list");
				}else{
					params.add(new BasicNameValuePair("Task1", "Get_Disciples"));
					Log.i("Sync_Service", "Requesting the Server for New Disciples list");
				}

				/// update for Schedule
				if(myDatabase.count(DeepLife.Table_SCHEDULES)==0){
					params.add(new BasicNameValuePair("Task2", "My_Schedules"));
					Log.i("Sync_Service", "Requesting the Server for All of the Schedule list");
				}else{
					params.add(new BasicNameValuePair("Task2", "Get_Schedules"));
					Log.i("Sync_Service", "Requesting the Server for New Schedule list");
				}

				////update for Questions
				if(myDatabase.count(DeepLife.Table_QUESTION_LIST)==0){
					params.add(new BasicNameValuePair("Task3", "My_Questions"));
					Log.i("Sync_Service", "Requesting the Server for All of the Question list");
				}else{
					params.add(new BasicNameValuePair("Task3", "Get_Questions"));
					Log.i("Sync_Service", "Requesting the Server for New Question list");
				}
			}

			return params;
		}
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			Log.i("Sync_Service", "Preparing Sync Service ...");
		}

		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
            msg = msg +"00000000000000";
			Log.i("Sync_Service", "Sync Service Connecting to the server ......");
			Log.i("Sync_Service", "Sync Request: \n"+init());
			try {
				JSONObject myObject = myParser.makeHttpRequest(	"http://api.cccsea.org/API.php", "POST", init());
				msg = msg + myObject.toString() +"\n............."+init().toString();
				Task = myObject.getString("Task");
				if(myObject != null){
					Log.i("Sync_Service", "Sync Service Connected");

					Log.i("Sync_Service", "Sync Result: \n"+myObject.toString());
				}else{
					Log.i("Sync_Service", "Sync Service Not Connected");
				}
				DeepLife.Register_disciple(myObject.getJSONArray("Disciples"));
				DeepLife.Register_Schedule(myObject.getJSONArray("Schedules"));

				if(myDatabase.count(DeepLife.Table_QUESTION_LIST)<1) {
					DeepLife.Register_Question(myObject.getJSONArray("Questions"));
				}
				Log.i("Sync_Service", "Sync Service Slept!");
				Thread.sleep(1000);
				Log.i("Sync_Service", "Sync Service awake!");
			} catch (Exception e) {
				// TODO: handle exception
				msg = msg + e.toString();
				Log.i("Sync_Service", "Sync Error \n"+e.toString());
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					Log.i("Sync_Service", "Server Response Error");
					Log.i("Sync_Service", e1.toString());
				}
			}
			return null;
		}
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			//Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
			if(Task.equals("1")){
				myDatabase.deleteTop(DeepLife.Table_LOGS);
				//Toast.makeText(getApplicationContext(), "Deleted", Toast.LENGTH_SHORT).show();
				Log.i("Sync_Service", "Server Respond--> 1");
				Log.i("Sync_Service", "Last Task was Successful.   -->Deleting Last Task Log");
				int xx = myDatabase.count(DeepLife.Table_LOGS);
				if(xx>0) {
					String str = myDatabase.get_Value_At_Top(DeepLife.Table_LOGS, DeepLife.LOGS_FIELDS[0]);
					Log.i("Sync_Service", "Pending Services "+xx);
					Log.i("Sync_Service", "Pending Task "+str);
				}
			}
			Log.i("Sync_Service", "Server Sync Completion Restarting ....");
			new Make_Service().execute();
		}

	}
}
