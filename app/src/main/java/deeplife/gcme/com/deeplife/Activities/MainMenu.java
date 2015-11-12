package deeplife.gcme.com.deeplife.Activities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import deeplife.gcme.com.deeplife.Adapters.Profile_Adapter;
import deeplife.gcme.com.deeplife.Database.Database;
import deeplife.gcme.com.deeplife.Database.DeepLife;
import deeplife.gcme.com.deeplife.FileManager.FileManager;
import deeplife.gcme.com.deeplife.Fragments.DiscipleList;
import deeplife.gcme.com.deeplife.Fragments.Schedules;
import deeplife.gcme.com.deeplife.Models.Disciples;
import deeplife.gcme.com.deeplife.Parsers.JSONParser;
import deeplife.gcme.com.deeplife.R;
import deeplife.gcme.com.deeplife.Registration.Login;

public class MainMenu extends FragmentActivity implements OnItemClickListener {

	DrawerLayout drawerLayout;
	ListView dlist;
    private AlertDialog.Builder builder;
    private AlertDialog.Builder myBuilder;
    private Context myContext;
	private FileManager myFileManager;
	private Database myDatabase;
	JSONParser jsonParser = new JSONParser();
	private static final String LOGIN_URL = "http://api.cccsea.org/API.php";
	
	String[] drawerlistitems;
	
	ViewPager viewpager;
	ActionBarDrawerToggle drawerListener;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome);
		getActionBar().setTitle("Welcome");
        myContext = this;
		myDatabase = new Database(myContext);
		myFileManager = new FileManager(myContext);
		drawerlistitems = getResources().getStringArray(R.array.drawerentry);
		//view pager
		viewpager = (ViewPager) findViewById(R.id.welcome_viewpager);
		viewpager.setAdapter(new MyAdpater(getSupportFragmentManager()));
		drawerListener = new ActionBarDrawerToggle(this, drawerLayout, R.drawable.drawericon,
				R.string.draweropened, R.string.drawerclosed){
			@Override
			public void onDrawerOpened(View drawerView) {
				// TODO Auto-generated method stub
				Toast.makeText(MainMenu.this, "Opened", Toast.LENGTH_SHORT).show();
			}
			@Override
					public void onDrawerClosed(View drawerView) {
						// TODO Auto-generated method stub
					//	super.onDrawerClosed(drawerView);
				Toast.makeText(MainMenu.this, "Closed", Toast.LENGTH_SHORT).show();
					
			}
			
		};
		ArrayList<Disciples> ben = new ArrayList<>();
        ben.add(new Disciples());
        ben.add(new Disciples());
        ben.add(new Disciples());
		ben.add(new Disciples());
		ben.add(new Disciples());

		dlist = (ListView) findViewById(R.id.drawerList);
		dlist.setAdapter(new Profile_Adapter(getApplicationContext(),ben));
		dlist.setOnItemClickListener(this);
        dlist.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if(position == 0){
					Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
					intent.setType("image/*");
					startActivityForResult(Intent.createChooser(intent, "Deep Life"), 1);
				}else if(position == 4) {
					Show_DialogBox(100);

				}else{
					Show_DialogBox(position);
				}

            }
        });
		
		//drawerLayout.setDrawerListener(drawerListener);
		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setDisplayHomeAsUpEnabled(true);


		getOverflowMenu();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == 1 && resultCode == -1){
			Uri selectedImageUri = data.getData();
			String selectedImagePath = getPath(selectedImageUri);

			BitmapFactory.Options option = new BitmapFactory.Options();
			option.inPreferredConfig = Bitmap.Config.ARGB_8888;

			Bitmap bmp = BitmapFactory.decodeFile(selectedImagePath);
			//savePicture("Profile.png", bmp, myContext);

			File pic = new File(selectedImagePath);

			myFileManager.createFolder("Pics");
			try {
				myFileManager.CopyFile(pic,myFileManager.createFileAt("Profile","Profile.png"));

				int id = myDatabase.get_Top_ID(DeepLife.Table_USER);
				ContentValues cv = new ContentValues();
				cv.put(DeepLife.USER_FIELDS[5],selectedImagePath);
				myDatabase.update(DeepLife.Table_USER,cv,id);

			} catch (IOException e) {
				Toast.makeText(myContext,"File Not Found",Toast.LENGTH_LONG).show();
			}
			/*Bitmap bb = getImageBitmap(myContext,"Profile.png");
			if(bb != null){
				Toast.makeText(getApplicationContext(),bmp.toString(),Toast.LENGTH_LONG).show();
			}else{
				Toast.makeText(getApplicationContext(),"File Not Found",Toast.LENGTH_LONG).show();
			}*/

		}

	}
	private void savePicture(String filename, Bitmap b, Context ctx){
		try {
			ObjectOutputStream oos;
			FileOutputStream out;
			out = ctx.openFileOutput(filename, Context.MODE_PRIVATE);
			oos = new ObjectOutputStream(out);
			b.compress(Bitmap.CompressFormat.PNG, 100, oos);

			oos.close();
			oos.notifyAll();
			out.notifyAll();
			out.close();
			Toast.makeText(myContext,"File Saved",Toast.LENGTH_LONG).show();
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(myContext,e.toString(),Toast.LENGTH_LONG).show();
		}
	}
	public String getPath(Uri uri) {
		// just some safety built in
		if( uri == null ) {
			// TODO perform some logging or show user feedback
			return null;
		}
		// try to retrieve the image from the media store first
		// this will only work for images selected from gallery
		String[] projection = { MediaStore.Images.Media.DATA };
		Cursor cursor = managedQuery(uri, projection, null, null, null);
		if( cursor != null ){
			int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			return cursor.getString(column_index);
		}
		// this is our fallback here
		return uri.getPath();
	}

    public void Show_DialogBox(final int type){
		LayoutInflater LI = LayoutInflater.from(myContext);
		View view1 = LI.inflate(R.layout.dialog_1, null);
		TextView txt_view = (TextView) view1.findViewById(R.id.textView1);
		final EditText txt = (EditText) view1.findViewById(R.id.editTextDialogUserInput);
		if(type == 1){
			txt_view.setText("Full Name");
		}
		if(type == 2){
			txt_view.setText("Phone Number");
			txt.setInputType(InputType.TYPE_NUMBER_FLAG_SIGNED);
		}
		if(type == 3){
			txt_view.setText("Email");

		}
		if(type == 100){
			txt_view.setText("Logging out ... Are you sure?");
			txt.setVisibility(View.GONE);

		}

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked
						if(type == 1){

							if(txt.getText().toString().length()>5){
								int id = myDatabase.get_Top_ID(DeepLife.Table_USER);
								ContentValues cv = new ContentValues();
								cv.put(DeepLife.USER_FIELDS[0],txt.getText().toString());
								myDatabase.update(DeepLife.Table_USER,cv,id);
							}

						}
						if(type == 100){
							myDatabase.Delete_All(DeepLife.Table_USER);
							myDatabase.Delete_All(DeepLife.Table_DISCIPLES);
							myDatabase.Delete_All(DeepLife.Table_LOGS);
							myDatabase.Delete_All(DeepLife.Table_SCHEDULES);

							Intent intent = new Intent(myContext,Splash.class);
							intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
							myContext.startActivity(intent);
						}

                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };
        builder = new AlertDialog.Builder(myContext);
        builder.setView(view1);

        builder.setPositiveButton("Ok", dialogClickListener)
                .setNegativeButton("Cancel", dialogClickListener).show();
    }
	
	private void getOverflowMenu() {

	    try {
	       ViewConfiguration config = ViewConfiguration.get(this);
	       Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
	       if(menuKeyField != null) {
	           menuKeyField.setAccessible(true);
	           menuKeyField.setBoolean(config, false);
	       }
	   } catch (Exception e) {
	       e.printStackTrace();
	   }
	 }
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onPostCreate(savedInstanceState);
		//drawerListener.syncState();
	}
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
		//drawerListener.onConfigurationChanged(newConfig);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.option_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
        if(drawerListener.onOptionsItemSelected(item)){
            return true;
        }

	//	if(drawerListener.onOptionsItemSelected(item)){
	//		return true;
	//	}
		switch(item.getItemId()){
			case R.id.about:
			//	Log.e("EEEEEEEEEEEEEEE", "about page ");
				AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
		        // set the message to display
		        alertbox.setMessage("This is the Deep Life Community app");
		        
		        // add a neutral button to the alert box and assign a click listener
		        alertbox.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
		        	
		            // click listener on the alert box
		            public void onClick(DialogInterface arg0, int arg1) {
		                // the button was clicked
		            
		            }
		        });

	        // show it
	        	alertbox.show();
	        	break;
			case R.id.options_logout:
				Intent intent = new Intent(getApplicationContext(),Login.class);
				startActivity(intent);
				finish();
				break;
			case R.id.options_exit:
				exist_DialogBox();
				break;
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	public void exist_DialogBox(){


		DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
		    @Override
		    public void onClick(DialogInterface dialog, int which) {
		        switch (which){
		        case DialogInterface.BUTTON_POSITIVE:
		            //Yes button clicked
					 System.exit(0);
		            break;
//		        case DialogInterface.BUTTON_NEUTRAL:
		            //Yes button clicked
//			            break;

		        case DialogInterface.BUTTON_NEGATIVE:
		            //No button clicked
		            break;
		        }
		    }
		};


		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Deep Life:").setMessage("Are you sure you want to exit?")
	   .setPositiveButton("Yes", dialogClickListener)
	   .setNegativeButton("No", dialogClickListener)
//	   .setNeutralButton(" ", dialogClickListener)
	   .show();

}
	
	class MyAdpater extends FragmentPagerAdapter{

			Fragment page =null;
		public MyAdpater(FragmentManager fm) {
			super(fm);
			// TODO Auto-generated constructor stub
		}

		@Override
		public Fragment getItem(int arg0) {
			if(arg0==0){
				page = new DiscipleList();

			}
			if(arg0==1){
				page = new Schedules();
			}
			return page;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 2;
		}
		
		@Override
		public CharSequence getPageTitle(int position) {
			switch (position) {
			case 0:
				return "Disciple List";
			case 1:
				return "Schedule";

		}
			return null;
	}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
	//	Toast.makeText(this, drawerlistitems[arg2] + " was clicked", Toast.LENGTH_SHORT).show();
	//	selectItem(arg2);
		switch(arg2){
		case 0: 
			Intent intent0 = new Intent(this, MainMenu.class);
			startActivity(intent0);
			break;
		case 1:
			//Intent intent2 = new Intent(this, Profile.class);
			//startActivity(intent2);
			break;
		case 2:
			//Intent intent2 = new Intent(this, Win.class);
			//startActivity(intent2);
			break;
		case 3:
			//Intent intent3 = new Intent(this, Build.class);
			//startActivity(intent3);
			break;
		case 4:
			//Intent intent4 = new Intent(this, Send.class);
			//startActivity(intent4);
			break;
		case 5:
			//Intent intent5 = new Intent(this, Logout.class);
			//startActivity(intent5);
			break;	
		}
	}

	private void selectItem(int arg2) {
		// TODO Auto-generated method stub
		dlist.setItemChecked(arg2, true);
		setTitle(drawerlistitems[arg2]);
	}

	public class AttemptLogin extends AsyncTask<String, String, String> {

		boolean failure = false;
		private JSONArray Req_Res = new JSONArray();
		String success,phone,password,msg;
		private ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();


			pDialog = new ProgressDialog(myContext);
			pDialog.setMessage("Attempting login...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
			msg = "";
		}

		@Override
		protected String doInBackground(String... args) {
			// TODO Auto-generated method stub
			// Check for success tag

			try {

				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("Task", "Authenticate"));
				params.add(new BasicNameValuePair("Email_Phone", phone));
				params.add(new BasicNameValuePair("Password", password));

				JSONObject json = jsonParser.makeHttpRequest(LOGIN_URL, "POST", params);
				Req_Res = json.getJSONArray("User_Profile");

				// json success tag
				success = json.getString("Task");
				msg = json.toString();
			} catch (JSONException e) {
				e.printStackTrace();
				msg = e.toString();
			}

			return null;

		}

		@Override
		protected void onPostExecute(String s) {
			super.onPostExecute(s);
			pDialog.dismiss();
			if (Req_Res.length() >0) {
				// Log.d("Login Successful!", json.toString());
				Toast.makeText(myContext, "Successful", Toast.LENGTH_SHORT).show();
				try {
					DeepLife.Register_Profile(Req_Res);

					Intent i = new Intent(myContext, MainMenu.class);
					startActivity(i);

					finish();
				} catch (JSONException e) {
					e.printStackTrace();
				}

				// return json.getString(TAG_MESSAGE);
			}else{
				// Log.d("Login Failure!", json.getString("Msg"));
				Toast.makeText(myContext, msg, Toast.LENGTH_SHORT).show();
			}
		}
	}

}
