package deeplife.gcme.com.deeplife;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import deeplife.gcme.com.deeplife.connection.JSONParser;
import deeplife.gcme.com.deeplife.database.Database;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.IBinder;
import android.telephony.TelephonyManager;
import android.widget.Toast;

public class Service extends android.app.Service{
	private Database myDatabase;
	private JSONParser myParser;
	private TelephonyManager Tele;
	
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
		new Make_Service().execute();
		
	}
	public class Make_Service extends AsyncTask<String, String, String>{
		private String msg;
		private String Task;
		private List<NameValuePair> init(){
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("US_Email", "BENGEOS"));
			params.add(new BasicNameValuePair("US_Password", "BENGEOS"));
			
			if(myDatabase.count(DeepLife.Table_LOGS)>0){
				Cursor myCursor = myDatabase.getAll(DeepLife.Table_LOGS);
				myCursor.moveToFirst();
				String type = myCursor.getString(myCursor.getColumnIndex(DeepLife.LOGS_FIELDS[0]));
				String id = myCursor.getString(myCursor.getColumnIndex(DeepLife.LOGS_FIELDS[1]));
				if(type.equals("Send_Disciple")){
					Cursor cur = myDatabase.get_value_by_ID(DeepLife.Table_DISCIPLES, id);
					cur.moveToFirst();
					params.add(new BasicNameValuePair("Task", type));
					params.add(new BasicNameValuePair(DeepLife.DISCIPLES_COLUMN[0], cur.getString(cur.getColumnIndex(DeepLife.DISCIPLES_COLUMN[0]))));
					params.add(new BasicNameValuePair(DeepLife.DISCIPLES_COLUMN[1], cur.getString(cur.getColumnIndex(DeepLife.DISCIPLES_COLUMN[1]))));
					params.add(new BasicNameValuePair(DeepLife.DISCIPLES_COLUMN[2], cur.getString(cur.getColumnIndex(DeepLife.DISCIPLES_COLUMN[2]))));
					params.add(new BasicNameValuePair(DeepLife.DISCIPLES_COLUMN[3], cur.getString(cur.getColumnIndex(DeepLife.DISCIPLES_COLUMN[3]))));
					params.add(new BasicNameValuePair(DeepLife.DISCIPLES_COLUMN[4], cur.getString(cur.getColumnIndex(DeepLife.DISCIPLES_COLUMN[4]))));
				}else if (type.equals("Send_Schedule")){
					Cursor cur = myDatabase.get_value_by_ID(DeepLife.Table_SCHEDULES, id);
					cur.moveToFirst();
					params.add(new BasicNameValuePair("Task", type));
					params.add(new BasicNameValuePair(DeepLife.SCHEDULE_COLUMN[0], cur.getString(cur.getColumnIndex(DeepLife.SCHEDULE_COLUMN[0]))));
					params.add(new BasicNameValuePair(DeepLife.SCHEDULE_COLUMN[1], cur.getString(cur.getColumnIndex(DeepLife.SCHEDULE_COLUMN[1]))));
					params.add(new BasicNameValuePair(DeepLife.SCHEDULE_COLUMN[2], cur.getString(cur.getColumnIndex(DeepLife.SCHEDULE_COLUMN[2]))));
				}
			}else if(myDatabase.count(DeepLife.Table_DISCIPLES)== 0){
				params.add(new BasicNameValuePair("Task", "Get_Disciples"));
			}else if(myDatabase.count(DeepLife.Table_DISCIPLES)==0){
				params.add(new BasicNameValuePair("Task", "Get_Schedule"));
			}else if(){
				
			}
			
			return params;
		}
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			msg = "";
			Toast.makeText(getApplicationContext(), "Service Started", Toast.LENGTH_SHORT).show();
			Task = "";
		}
		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub	
			try {
				JSONObject myObject = myParser.makeHttpRequest(	"http://192.168.137.1/DeepLife/API.php", "POST", init());
				msg = myObject.toString();
				Task = myObject.getString("Task");
				DeepLife.Register_disciple(myObject.getJSONArray("Disciples"));
				Thread.sleep(5000);
			} catch (Exception e) {
				// TODO: handle exception
				msg = e.toString();
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			return null;
		}
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
			if(Task.equals("1")){
				myDatabase.deleteTop(DeepLife.Table_LOGS);
				Toast.makeText(getApplicationContext(), "Deleted", Toast.LENGTH_SHORT).show();
			}
			new Make_Service().execute();
		}
		
	}
}
