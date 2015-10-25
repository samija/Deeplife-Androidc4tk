package deeplife.gcme.com.deeplife;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.sql.SQLException;

/**
 * Created by rog on 6/18/2015.
 */
public class DbAdapter {

    DbHelper dbhelper;
    Context context;
    public DbAdapter(Context context){
        dbhelper = new DbHelper(context);
        this.context = context;
    }

    
    public void close(){
    	if(dbhelper !=null){
    		dbhelper.close();
    	}
    	
    }

    public long addUser(ContentValues content){
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        Log.i("EEEEEEEEEEEEEEE", content.toString());

        long i = db.insert(dbhelper.TABLE_USERS,null,content);
        return i;
    }

    /*
    public long addMentor(ContentValues content){
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        long i = db.insert(dbhelper.TABLE_DISCIPLES,null,content);
        return i;
    }
    */
    public Cursor getAllUsers(){
        SQLiteDatabase db = dbhelper.getWritableDatabase();
       String[] column = {dbhelper.UID,dbhelper.USER_PHONE_NUMBER,dbhelper.USER_PASSWORD,dbhelper.UID,
               dbhelper.USER_EMAIL,dbhelper.USER_NAME};
        Cursor cursor = db.query(dbhelper.TABLE_USERS,column ,null, null, null, null, null);

        if(cursor !=null){
    		cursor.moveToFirst();
        }
        Log.i("DEEP LIFE", "All rows got!!");
        Log.i("DEEP LIFE","Cursor count: "+cursor.getCount());
    	return cursor;
    }
    public Cursor getAllDisciples(){
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        String[] column = {dbhelper.UID,dbhelper.USER_PHONE_NUMBER,dbhelper.UID,
                dbhelper.USER_EMAIL,dbhelper.USER_NAME,dbhelper.BUILD_PHASE};
        Cursor cursor = db.query(dbhelper.TABLE_USERS,column ,dbhelper.UID+"!='2'", null, null, null, null);

        if(cursor !=null){
            cursor.moveToFirst();
        }
        Log.i("DEEP LIFE", "Got all desciples list!!");
        Log.i("DEEP LIFE","Cursor count: "+cursor.getCount());
        return cursor;
    }

	public long deleteDesciple(int id) {
		// TODO Auto-generated method stub
		SQLiteDatabase db = dbhelper.getWritableDatabase();
		long count = db.delete(dbhelper.TABLE_USERS, dbhelper.UID + " = '"+id+"'", null);
		return count;
	}

    public long addDisciple(ContentValues content) {
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        long i = db.insert(dbhelper.CREATE_TABLE_USERS, null, content);
        return i;
    }

    public String getDiscipleId(String name) {

        SQLiteDatabase db = dbhelper.getWritableDatabase();
        String[] column = {dbhelper.UID};
        Cursor cursor = db.query(dbhelper.TABLE_USERS,column ,dbhelper.USER_NAME+"='"+name+"'", null, null, null, null);

        return cursor.toString();
    }
}


