package deeplife.gcme.com.deeplife;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import deeplife.gcme.com.deeplife.database.Database;
import android.app.Application;
import android.content.ContentValues;
import android.content.Intent;

public class DeepLife extends Application {
	public static final String Table_DISCIPLES = "DISCIPLES";
	public static final String Table_SCHEDULES = "SCHEDULES";
	public static final String Table_LOGS = "LOGS";
	public static final String Table_USER = "USER";

	public static final String[] DISCIPLES_FIELDS = { "Full_Name",
			 "Phone", "Email", "Build_phase","Country","Gender","Picture" };

	public static final String[] LOGS_FIELDS = { "Type", "Loc_ID" };
	public static final String[] SCHEDULE_FIELDS = { "DiscipleId","Time", "Description" };

	public static final String[] USER_FIELDS = { "Name","Email", "Password","Phone"};

	public static final String[] DISCIPLES_COLUMN = { "id", "Full_Name",
			 "Phone", "Email", "Build_phase","Country","Gender","Picture" };
	public static final String[] SCHEDULE_COLUMN = { "id","Disciple_Id","Time", "Description" };

	public static final String[] LOGS_COLUMN = { "id", "Type", "Loc_ID" };
	public static final String[] USER_COLUMN = { "id", "Name","Email", "Password","Phone"};

	public static Database myDatabase;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		myDatabase = new Database(this);
		Intent intent = new Intent(this, Service.class);
		//startService(intent);
	}

	public static void Send_Log(String Type, String id) {
		ContentValues cv_log = new ContentValues();
		cv_log.put(LOGS_FIELDS[0], Type);
		cv_log.put(LOGS_FIELDS[1], id);
		myDatabase.insert(DeepLife.Table_LOGS, cv_log);
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
				sch_vals.put(DISCIPLES_FIELDS[4], obj.getString(DISCIPLES_FIELDS[4]));
				myDatabase.insert(Table_DISCIPLES, sch_vals);
			}
		}
	}
}
