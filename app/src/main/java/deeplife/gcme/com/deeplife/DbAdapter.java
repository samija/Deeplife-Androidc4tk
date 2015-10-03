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

    
    public Cursor getAllDesciples(){
        SQLiteDatabase db = dbhelper.getWritableDatabase();
       String[] column = {dbhelper.UID,dbhelper.USER_PHONE_NUMBER,dbhelper.USER_PASSWORD,dbhelper.UID,dbhelper.USER_PICTURE,
               dbhelper.USER_EMAIL,
        		dbhelper.USER_GENDER,dbhelper.USER_FIRST_NAME,dbhelper.USER_LAST_NAME};
        Cursor cursor = db.query(dbhelper.TABLE_USERS,column ,null, null, null, null, null);

        if(cursor !=null){
    		cursor.moveToFirst();
        }
        Log.i("DEEP LIFE", "All rows got!!");
        Log.i("DEEP LIFE","Cursor count: "+cursor.getCount());
    	return cursor;
    }

	public long deleteDesciple(int id) {
		// TODO Auto-generated method stub
		SQLiteDatabase db = dbhelper.getWritableDatabase();
		String col;
		long count = db.delete(dbhelper.TABLE_USERS, dbhelper.UID + " = '"+id+"'", null);
		return count;
	}
}


