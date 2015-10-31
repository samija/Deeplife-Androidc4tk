package deeplife.gcme.com.deeplife.database;

import java.util.ArrayList;

import deeplife.gcme.com.deeplife.data_types.Disciples;
import deeplife.gcme.com.deeplife.DeepLife;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class Database {
	private SQLiteDatabase myDatabase;
	private SQL_Helper mySQL;
	private Context myContext;
	
	public Database(Context context){
		myContext = context;
		mySQL = new SQL_Helper(myContext);
		myDatabase = mySQL.getWritableDatabase();
		mySQL.createTables(DeepLife.Table_DISCIPLES, DeepLife.DISCIPLES_FIELDS);
		mySQL.createTables(DeepLife.Table_LOGS, DeepLife.LOGS_FIELDS);
		mySQL.createTables(DeepLife.Table_USER, DeepLife.USER_FIELDS);
		mySQL.createTables(DeepLife.Table_SCHEDULES,DeepLife.SCHEDULE_FIELDS);

	}
	public void dispose(){
		myDatabase.close();
		mySQL.close();

	}
	public long insert(String DB_Table,ContentValues cv){
		long state = myDatabase.insert(DB_Table, null, cv);
		return state;
	}
	public long Delete_All(String DB_Table){
		long state = myDatabase.delete(DB_Table, null, null);
		return state;
	}
	public long remove(String DB_Table,int id){
		String[] args = {""+id};
		long val = myDatabase.delete(DB_Table, "id = ?", args);
		return val;
	}
	public long update(String DB_Table,ContentValues cv,int id){
		String[] args = {""+id};
		long state = myDatabase.update(DB_Table, cv, "id = ?", args);
		return state;
	}
	public int count(String DB_Table){		
		Cursor c = myDatabase.query(DB_Table, getColumns(DB_Table), null, null, null, null, null);
		return c.getCount();
	}
	public Cursor getAll(String DB_Table){
		Cursor c = myDatabase.query(DB_Table, getColumns(DB_Table), null, null, null, null, null);
		return c;
	}
	public String get_Value_At_Top(String DB_Table,String column){
		String str = "";
		Cursor c = myDatabase.query(DB_Table, getColumns(DB_Table), null, null, null, null, null);
		c.moveToFirst();
		str = c.getString(c.getColumnIndex(column));
		return str;
	}
	public String get_Value_At_Bottom(String DB_Table,String column){
		String str = "";
		Cursor c = myDatabase.query(DB_Table, getColumns(DB_Table), null, null, null, null, null);
		c.moveToLast();
		str = c.getString(c.getColumnIndex(column));
		return str;
	}
	public Cursor get_value_by_ID (String DB_Table,String id){
		Cursor cur = myDatabase.rawQuery("select * from "+DB_Table+" where id="+id, null);
		return cur;
	}


	public long Delete_By_ID(String DB_Table,int pos){
		String[] args = {""+pos};
		long val = myDatabase.delete(DB_Table, "id = ?", args);
		return val;
	}
	public long Delete_By_Column(String DB_Table,String column,String val){
		String[] args = {val};
		long v = myDatabase.delete(DB_Table, column+" = ?", args);
		return v;
	}
	public void deleteTop(String DB_Table){
		String column = getColumns(DB_Table)[0];
		String value = get_Value_At_Top(DB_Table, column);
		Delete_By_Column(DB_Table, column, value);
	}
	public int get_Top_ID(String DB_Table){
		int pos = -1;
		Cursor c = myDatabase.query(DB_Table, getColumns(DB_Table), null, null, null, null, null);
		c.moveToFirst();
		pos = Integer.valueOf(c.getString(c.getColumnIndex("id")));
		return pos;
	}
	public ArrayList<String> get_all_in_column(String DB_Table,String atColumn){
		ArrayList<String> found = new ArrayList<String>();
		Cursor c = myDatabase.query(DB_Table, getColumns(DB_Table), null, null, null, null, null);
		c.moveToFirst();
		for(int i = 0;i < c.getCount();i++){
			c.moveToPosition(i);
			String DB_Val = c.getString(c.getColumnIndex(atColumn));
			if(!found.contains(DB_Val)){
				found.add(DB_Val);
			}
		}
		return found;
	}
	public ArrayList<Disciples> getDisciples(String DB_Table){
		ArrayList<Disciples> found = new ArrayList<Disciples>();
		Cursor c = myDatabase.query(DB_Table, getColumns(DB_Table), null, null, null, null, null);
		c.moveToFirst();
		
		for(int i=0;i<c.getCount();i++){
			c.moveToPosition(i);
			Disciples dis = new Disciples();
			dis.setId(c.getString(c.getColumnIndex(DeepLife.DISCIPLES_COLUMN[0])));
			dis.setFull_Name(c.getString(c.getColumnIndex(DeepLife.DISCIPLES_COLUMN[1])));
			dis.setPhone(c.getString(c.getColumnIndex(DeepLife.DISCIPLES_COLUMN[2])));
			dis.setPhone(c.getString(c.getColumnIndex(DeepLife.DISCIPLES_COLUMN[3])));
			dis.setBuild_Phase(c.getString(c.getColumnIndex(DeepLife.DISCIPLES_COLUMN[4])));
			dis.setCountry(c.getString(c.getColumnIndex(DeepLife.DISCIPLES_COLUMN[5])));

			found.add(dis);
		}
		return found;
	}
	private String[] getColumns(String DB_Table){
		String[] strs = null;
		if(DB_Table == DeepLife.Table_DISCIPLES){
			strs = DeepLife.DISCIPLES_COLUMN;
		}else if(DB_Table == DeepLife.Table_LOGS){
			strs = DeepLife.LOGS_COLUMN;
		}else if(DB_Table == DeepLife.Table_USER){
			strs = DeepLife.USER_COLUMN;
		}
		return strs;
	}
}
