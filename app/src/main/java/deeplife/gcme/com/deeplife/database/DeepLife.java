package deeplife.gcme.com.deeplife.Database;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import deeplife.gcme.com.deeplife.Alarm.Alarm_BroadCast;
import deeplife.gcme.com.deeplife.Database.Database;
import deeplife.gcme.com.deeplife.Services.Service;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;

public class DeepLife extends Application {
	public static final String Table_DISCIPLES = "DISCIPLES";
	public static final String Table_SCHEDULES = "SCHEDULES";
	public static final String Table_LOGS = "LOGS";
	public static final String Table_USER = "USER";

	public static final String[] DISCIPLES_FIELDS = { "Full_Name", "Email", "Phone", "Country","Build_phase","Gender","Picture" };

	public static final String[] LOGS_FIELDS = { "Type", "Loc_ID" };
	public static final String[] SCHEDULES_FIELDS = { "Dis_Phone", "Alarm_Time","Alarm_Repeat","Description" };

	public static final String[] USER_FIELDS = { "Full_Name", "Email","Phone","Password","Country" };

	public static final String[] DISCIPLES_COLUMN = { "id", "Full_Name","Email", "Phone", "Country","Build_phase","Gender","Picture" };

	public static final String[] SCHEDULES_COLUMN = { "id","Dis_Phone", "Alarm_Time","Alarm_Repeat","Description" };

	public static final String[] LOGS_COLUMN = { "id", "Type", "Loc_ID" };
	public static final String[] USER_COLUMN = { "id", "Full_Name", "Email","Phone","Password","Country" };

	public static Database myDatabase;

    public static Intent AlarmIntent;
    public static PendingIntent AlarmPendingIntent;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		myDatabase = new Database(getApplicationContext());
		Intent intent = new Intent(this, Service.class);
		if(myDatabase.count(Table_USER)==1){
			//startService(intent);
		}
        AlarmIntent = new Intent(this, Alarm_BroadCast.class);
        AlarmPendingIntent = PendingIntent.getBroadcast(this,0,AlarmIntent,0);
	}
	//Register User Profile
	public static void Register_Profile(JSONArray NOTIS) throws JSONException {
		myDatabase.Delete_All(Table_USER);
		if (NOTIS.length() > 0) {
			for (int i = 0; i < NOTIS.length(); i++) {
				JSONObject obj = NOTIS.getJSONObject(i);
				ContentValues sch_vals = new ContentValues();
				sch_vals.put(USER_FIELDS[0], obj.getString(USER_FIELDS[0]));
				sch_vals.put(USER_FIELDS[1], obj.getString(USER_FIELDS[1]));
				sch_vals.put(USER_FIELDS[2], obj.getString(USER_FIELDS[2]));
				sch_vals.put(USER_FIELDS[3], obj.getString(USER_FIELDS[3]));
				sch_vals.put(USER_FIELDS[4], obj.getString(USER_FIELDS[4]));
				myDatabase.insert(Table_USER, sch_vals);
			}
		}
	}
	public static void Register_disciple(JSONArray NOTIS) throws JSONException {
		if (NOTIS.length() > 0) {
			for (int i = 0; i < NOTIS.length(); i++) {
				JSONObject obj = NOTIS.getJSONObject(i);
				ContentValues sch_vals = new ContentValues();
				sch_vals.put(DISCIPLES_FIELDS[0], obj.getString(DISCIPLES_FIELDS[0]));
				sch_vals.put(DISCIPLES_FIELDS[1], obj.getString(DISCIPLES_FIELDS[1]));
				sch_vals.put(DISCIPLES_FIELDS[2], obj.getString(DISCIPLES_FIELDS[2]));
				sch_vals.put(DISCIPLES_FIELDS[3], obj.getString(DISCIPLES_FIELDS[3]));
				sch_vals.put(DISCIPLES_FIELDS[4], obj.getString(DISCIPLES_FIELDS[3]));
				sch_vals.put(DISCIPLES_FIELDS[5], obj.getString(DISCIPLES_FIELDS[3]));
				sch_vals.put(DISCIPLES_FIELDS[6], obj.getString(DISCIPLES_FIELDS[3]));
				myDatabase.insert(Table_DISCIPLES, sch_vals);
			}
		}
	}
	public static void Register_Schedule(JSONArray NOTIS) throws JSONException {
		if (NOTIS.length() > 0) {
			for (int i = 0; i < NOTIS.length(); i++) {
				JSONObject obj = NOTIS.getJSONObject(i);
				ContentValues sch_vals = new ContentValues();
				sch_vals.put(SCHEDULES_FIELDS[0], obj.getString(SCHEDULES_FIELDS[0]));
				sch_vals.put(SCHEDULES_FIELDS[1], obj.getString(SCHEDULES_FIELDS[1]));
				sch_vals.put(SCHEDULES_FIELDS[2], obj.getString(SCHEDULES_FIELDS[2]));
				sch_vals.put(SCHEDULES_FIELDS[3], obj.getString(SCHEDULES_FIELDS[3]));
				myDatabase.insert(Table_SCHEDULES, sch_vals);
			}
		}
	}
}
