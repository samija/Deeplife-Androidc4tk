package deeplife.gcme.com.deeplife;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

  public class DbHelper extends SQLiteOpenHelper{

        public static final String DATABASE_NAME = "deepLife";
        public static final String TABLE_USERS = "users";

        public static final String UID = "_id";
        public static final String USER_NAME = "name";
        public static final String USER_PASSWORD = "password";
        public static final String USER_PHONE_NUMBER= "phone";
        //public static final String USER_PICTURE = "Link";
        //public static final String USER_GENDER = "gender";
        public static final String USER_EMAIL = "email";

        public static final String BUILD_PHASE = "build_phase";
        public static final int VERSION = 2;

        public static final String CREATE_TABLE_USERS = "CREATE TABLE IF NOT EXISTS " + TABLE_USERS
        		+ "( "+ UID+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + USER_NAME + " VARCHAR(255),"
        		+ USER_PHONE_NUMBER + " VARCHAR(255)," + USER_PASSWORD + " VARCHAR(1000),"
                + USER_EMAIL + " VARCHAR(255),"
                + BUILD_PHASE + " VARCHAR(255)); ";

    /*
      public static final String CREATE_TABLE_DISCIPLES = "CREATE TABLE IF NOT EXISTS " + TABLE_DISCIPLES
              + "( "+ UID+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
              + DISCIPLE_ID + " VARCHAR(255),"
              + MENTOR_ID + " VARCHAR(255),"
              + BUILD_PHASE + " VARCHAR(255)); ";
        
*/
        
        public static final String DROP_TABLE_USERS = "DROP TABLE IF EXISTS " + TABLE_USERS;
        Context context;

        public DbHelper(Context context) {
            super(context, DATABASE_NAME, null, VERSION);
            this.context = context;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            //db.execSQL(CREATE_MY_NEWS_TABLE);
            //Message.Message(context, "OnCreate called");  
            Log.i("DEEP LIFE", "On create called!!!");
      	  db.execSQL(CREATE_TABLE_USERS);

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            //Message.Message(context, "OnUpgrade called");  
            Log.i("DEEP LIFE", "On upgrade called");
        	db.execSQL(DROP_TABLE_USERS);
        	onCreate(db);
        }
    }