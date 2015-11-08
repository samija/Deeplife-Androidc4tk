package deeplife.gcme.com.deeplife.Activities;

import java.lang.reflect.Field;
import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import deeplife.gcme.com.deeplife.Adapters.Profile_Adapter;
import deeplife.gcme.com.deeplife.Fragments.DiscipleList;
import deeplife.gcme.com.deeplife.Fragments.Schedules;
import deeplife.gcme.com.deeplife.Models.Disciples;
import deeplife.gcme.com.deeplife.R;
import deeplife.gcme.com.deeplife.Registration.Login;

public class MainMenu extends FragmentActivity implements OnItemClickListener {

	DrawerLayout drawerLayout;
	ListView dlist;
    private AlertDialog.Builder builder;
    private Context myContext;
	
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

		dlist = (ListView) findViewById(R.id.drawerList);
		dlist.setAdapter(new Profile_Adapter(getApplicationContext(),ben));
		dlist.setOnItemClickListener(this);
        dlist.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Show_DialogBox("What are you doing?",1);
                return false;
            }
        });
		
		//drawerLayout.setDrawerListener(drawerListener);
		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setDisplayHomeAsUpEnabled(true);


		getOverflowMenu();
	}
    public void Show_DialogBox(String messge,int type){
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked

                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };
        builder = new AlertDialog.Builder(myContext);
        builder.setTitle("DeepLife:").setMessage(messge).setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
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

}
