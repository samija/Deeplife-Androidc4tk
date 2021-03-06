
package deeplife.gcme.com.deeplife.Database;/////

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

import deeplife.gcme.com.deeplife.Models.Disciples;
import deeplife.gcme.com.deeplife.Models.Question;
import deeplife.gcme.com.deeplife.Models.QuestionAnswer;
import deeplife.gcme.com.deeplife.Models.Schedule;

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
        mySQL.createTables(DeepLife.Table_SCHEDULES, DeepLife.SCHEDULES_FIELDS);
        mySQL.createTables(DeepLife.Table_QUESTION_LIST,DeepLife.QUESTION_LIST_FIELDS);
        mySQL.createTables(DeepLife.Table_QUESTION_ANSWER, DeepLife.QUESTION_ANSWER_FIELDS);
        mySQL.createTables(DeepLife.Table_Reports, DeepLife.REPORTS_FIELDS);
    }

    public void dispose(){
        myDatabase.close();
    }


    public void populateQuestions(){
        ContentValues cv = new ContentValues();
        cv.put(DeepLife.QUESTION_LIST_FIELDS[0],"WIN");
        cv.put(DeepLife.QUESTION_LIST_FIELDS[1],"Have you been following your disciple? ");
        cv.put(DeepLife.QUESTION_LIST_FIELDS[2],"This is to inform that you are properly following you disciple");
        cv.put(DeepLife.QUESTION_LIST_FIELDS[3], "Mandatory");

        long state = insert(DeepLife.Table_QUESTION_LIST,cv);
        if(state!=-1) Log.i("Deep Life", "Database row successfully added!!!");
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

    public int count_Questions(String DB_Table, String Category){
        Cursor c = myDatabase.query(DB_Table, getColumns(DB_Table), DeepLife.QUESTION_LIST_FIELDS[0]+" = '"+Category+"'", null, null, null, null);
        return c.getCount();
    }

    public long checkExistence(String Db_Table,String column,String id, String build){

        Cursor cursor = myDatabase.query(Db_Table, getColumns(Db_Table),column+" = '"+id+"' and "+DeepLife.QUESTION_ANSWER_FIELDS[3]+" = '"+ build+"'",null,null,null,null);
        return cursor.getCount();

    }

    public Cursor getAll(String DB_Table){
        Cursor c = myDatabase.query(DB_Table, getColumns(DB_Table), null, null, null, null, null);
        return c;
    }

    public String get_Value_At_Top(String DB_Table,String column){
        String str = "";
        try {

            Cursor c = myDatabase.query(DB_Table, getColumns(DB_Table), null, null, null, null, null);
            c.moveToFirst();
            str = c.getString(c.getColumnIndex(column));
        }catch (Exception e){

        }

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

    public String get_Name_by_phone(String phone){
        String name = "";
        String DB_Table = DeepLife.Table_DISCIPLES;
        Cursor c = myDatabase.query(DB_Table, getColumns(DB_Table), DeepLife.DISCIPLES_FIELDS[2]+" = '"+phone+"'", null, null, null, null);
        c.moveToLast();
        name = c.getString(c.getColumnIndex(DeepLife.DISCIPLES_FIELDS[0]));
        return name;
    }


/*
    public Cursor get_value_by_colum_id(String Db_Table, String column, String id,String build){
        Cursor c = myDatabase.query(Db_Table, getColumns(Db_Table),column+" = '"+id+"' &",null,null,null,null);
        c.moveToFirst();
        for(int i = 0;i < c.getCount();i++){
            c.moveToPosition(i);
            String qId = c.getString(c.getColumnIndex(DeepLife.QUESTION_ANSWER_FIELDS[1]));
            Cursor question = get_value_by_ID(DeepLife.Table_QUESTION_LIST,qId);

        }
    }
*/


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


    public String get_DiscipleName(String phone){
		String Name = null;
		String DB_Table = DeepLife.Table_DISCIPLES;
		Cursor c = myDatabase.query(DB_Table, getColumns(DB_Table), null, null, null, null, null);
		c.moveToFirst();
		for(int i=0;i<c.getCount();i++){
			c.moveToPosition(i);
			String str = c.getString(c.getColumnIndex(DeepLife.DISCIPLES_COLUMN[3]));
			if(str.equals(phone)){
				Name = c.getString(c.getColumnIndex(DeepLife.DISCIPLES_COLUMN[1]));
			}
		}
		return Name;
	}

    public ArrayList<Question> get_All_Questions(String Category){
        String DB_Table = DeepLife.Table_QUESTION_LIST;
        ArrayList<Question> found = new ArrayList<Question>();
        Cursor c = myDatabase.query(DB_Table, getColumns(DB_Table), DeepLife.QUESTION_LIST_FIELDS[0]+" = '"+Category+"'", null, null, null, null);
        c.moveToFirst();

        for(int i=0;i<c.getCount();i++){
            c.moveToPosition(i);
            Question dis = new Question();
            dis.setId(c.getString(c.getColumnIndex(DeepLife.QUESTION_LIST_COLUMN[0])));
            dis.setCategory(c.getString(c.getColumnIndex(DeepLife.QUESTION_LIST_COLUMN[1])));
            dis.setDescription(c.getString(c.getColumnIndex(DeepLife.QUESTION_LIST_COLUMN[2])));
            dis.setNote(c.getString(c.getColumnIndex(DeepLife.QUESTION_LIST_COLUMN[3])));
            dis.setMandatory(c.getString(c.getColumnIndex(DeepLife.QUESTION_LIST_COLUMN[4])));
            found.add(dis);
        }
        return found;
    }

    public ArrayList<Disciples> getDisciples(){
        String DB_Table = DeepLife.Table_DISCIPLES;
        ArrayList<Disciples> found = new ArrayList<Disciples>();
        Cursor c = myDatabase.query(DB_Table, getColumns(DB_Table), null, null, null, null, null);
        if(c.getCount()>0){
            c.moveToFirst();
            for(int i=0;i<c.getCount();i++){
                c.moveToPosition(i);
                Disciples dis = new Disciples();
                dis.setId(c.getString(c.getColumnIndex(DeepLife.DISCIPLES_COLUMN[0])));
                dis.setFull_Name(c.getString(c.getColumnIndex(DeepLife.DISCIPLES_COLUMN[1])));
                dis.setEmail(c.getString(c.getColumnIndex(DeepLife.DISCIPLES_COLUMN[2])));
                dis.setPhone(c.getString(c.getColumnIndex(DeepLife.DISCIPLES_COLUMN[3])));
                dis.setCountry(c.getString(c.getColumnIndex(DeepLife.DISCIPLES_COLUMN[4])));
                dis.setBuild_Phase(c.getString(c.getColumnIndex(DeepLife.DISCIPLES_COLUMN[5])));
                dis.setGender(c.getString(c.getColumnIndex(DeepLife.DISCIPLES_COLUMN[6])));
                dis.setPicture(c.getString(c.getColumnIndex(DeepLife.DISCIPLES_COLUMN[7])));
                found.add(dis);
            }
        }

        return found;
    }

     public ArrayList<Schedule> get_All_Schedule(){
        String DB_Table = DeepLife.Table_SCHEDULES;
        ArrayList<Schedule> found = new ArrayList<Schedule>();
        Cursor c = myDatabase.query(DB_Table, getColumns(DB_Table), null, null, null, null, null);
         if(c.getCount()>0){
             c.moveToFirst();
             for(int i=0;i<c.getCount();i++){
                 c.moveToPosition(i);
                 Schedule dis = new Schedule();
                 dis.setID(c.getString(c.getColumnIndex(DeepLife.SCHEDULES_COLUMN[0])));
                 dis.setDis_Phone(c.getString(c.getColumnIndex(DeepLife.SCHEDULES_COLUMN[1])));
                 dis.setAlarm_Time(c.getString(c.getColumnIndex(DeepLife.SCHEDULES_COLUMN[2])));
                 dis.setAlarm_Repeat(c.getString(c.getColumnIndex(DeepLife.SCHEDULES_COLUMN[3])));
                 dis.setDescription(c.getString(c.getColumnIndex(DeepLife.SCHEDULES_COLUMN[4])));
                 found.add(dis);
             }
         }
        return found;
    }

    public ArrayList<Schedule> get_Schedule(String Dis_ID){
        String DB_Table = DeepLife.Table_SCHEDULES;
        ArrayList<Schedule> found = new ArrayList<Schedule>();
        Cursor c = myDatabase.query(DB_Table, getColumns(DB_Table), null, null, null, null, null);
        c.moveToFirst();
        for(int i=0;i<c.getCount();i++){
            c.moveToPosition(i);
            Schedule dis = new Schedule();
            String id = c.getString(c.getColumnIndex(DeepLife.SCHEDULES_COLUMN[0]));
            if(Dis_ID.equals(id)){
                dis.setID(c.getString(c.getColumnIndex(DeepLife.SCHEDULES_COLUMN[0])));
                dis.setDis_Phone(c.getString(c.getColumnIndex(DeepLife.SCHEDULES_COLUMN[1])));
                dis.setAlarm_Time(c.getString(c.getColumnIndex(DeepLife.SCHEDULES_COLUMN[2])));
                dis.setAlarm_Repeat(c.getString(c.getColumnIndex(DeepLife.SCHEDULES_COLUMN[3])));
                dis.setDescription(c.getString(c.getColumnIndex(DeepLife.SCHEDULES_COLUMN[4])));
                found.add(dis);
            }
        }
        return found;
    }
    public ArrayList<QuestionAnswer> get_Answer(String Dis_ID, String phase){
        String DB_Table = DeepLife.Table_QUESTION_ANSWER;
        ArrayList<QuestionAnswer> found = new ArrayList<QuestionAnswer>();
        Cursor c = myDatabase.query(DB_Table, getColumns(DB_Table), DeepLife.QUESTION_ANSWER_FIELDS[0]+" = '"+Dis_ID+"' and "+DeepLife.QUESTION_ANSWER_FIELDS[3]+"= '"+phase+"'", null, null, null, null);
        Log.i("Deep Life", "Answer from db count: "+c.getCount() + " data: "+c.toString());
        c.moveToFirst();
        for(int i=0;i<c.getCount();i++){
            c.moveToPosition(i);
            QuestionAnswer dis = new QuestionAnswer();
                dis.setId(c.getString(c.getColumnIndex(DeepLife.QUESTION_ANSWER_COLUMN[0])));
                dis.setDisciple_id(c.getString(c.getColumnIndex(DeepLife.QUESTION_ANSWER_COLUMN[1])));
                dis.setQuestion_id(c.getString(c.getColumnIndex(DeepLife.QUESTION_ANSWER_COLUMN[2])));
                dis.setAnswer(c.getString(c.getColumnIndex(DeepLife.QUESTION_ANSWER_COLUMN[3])));
                dis.setBuild_Phase(c.getString(c.getColumnIndex(DeepLife.QUESTION_ANSWER_COLUMN[4])));

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
        }else if(DB_Table == DeepLife.Table_SCHEDULES){
            strs = DeepLife.SCHEDULES_COLUMN;
        } else if(DB_Table == DeepLife.Table_QUESTION_LIST){
            strs = DeepLife.QUESTION_LIST_COLUMN;
        } else if(DB_Table == DeepLife.Table_QUESTION_ANSWER){
            strs = DeepLife.QUESTION_ANSWER_COLUMN;
        } else if(DB_Table == DeepLife.Table_Reports){
            strs = DeepLife.REPORTS_COLUMN;
        }
        return strs;
    }
}
