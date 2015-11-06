package deeplife.gcme.com.deeplife.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import deeplife.gcme.com.deeplife.Database.DeepLife;
import deeplife.gcme.com.deeplife.Activities.MainMenu;
import deeplife.gcme.com.deeplife.Models.Schedule;
import deeplife.gcme.com.deeplife.R;
import deeplife.gcme.com.deeplife.Database.Database;

/**
 * A simple {@link Fragment} subclass.
 * 
 */
public class Schedules extends Fragment {

	
	private static final int FRAGMENT_GROUPID = 30;
	public ListView lv_schedule;

	Button addSchedule;
	int fallback;


	ArrayList<Schedule> schedules;

	Database dbadapter;
	DeepLife dbhelper;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.schedule_list, container,
				false);


		dbadapter = new Database(getActivity());
		dbhelper = new DeepLife();

        lv_schedule = (ListView) view.findViewById(R.id.ls_schedule);

        
        populateList(getActivity());


        addSchedule = (Button) view.findViewById(R.id.bt_add_schedule);
        addSchedule.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
                addScheduleDialog();

            }
		});

		setHasOptionsMenu(true);

		//new AsyncDownloader().execute("http://newsrss.bbc.co.uk/rss/newsonline_uk_edition/entertainment/rss.xml");

		return 	view;
		
	}


    public void populateList(Context context){
        ArrayList<Schedule> schedules = dbadapter.get_All_Schedule();
        lv_schedule.setAdapter(new MyDiscipleListAdapter(context,schedules));

    }





    public void addScheduleDialog(){
        final Dialog add = new Dialog(getActivity().getBaseContext());

        add.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        add.requestWindowFeature(Window.FEATURE_NO_TITLE);

        add.setContentView(R.layout.schedule_add);

        WindowManager window = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        window.getDefaultDisplay().getMetrics(dm);
        double wid = dm.widthPixels*0.9;
        double hei = dm.heightPixels*0.8;
        int width = (int) wid;
        int height = (int) hei;
        add.getWindow().setLayout(width, height);


        add.show();

        ArrayList<String> list = dbadapter.get_all_in_column(dbhelper.Table_DISCIPLES,dbhelper.DISCIPLES_FIELDS[0]);



        final Spinner names = (Spinner) add.findViewById(R.id.schedule_add_name);
        final EditText ed_disc = (EditText) add.findViewById(R.id.schedule_add_disc);
        final DatePicker dp_date = (DatePicker) add.findViewById(R.id.schedule_add_date);
        final TimePicker tp_time = (TimePicker) add.findViewById(R.id.schedule_add_time_picker);

        final String phone = "091177";
        names.setAdapter(new MySpinnerAdapter(getActivity(), R.layout.countries_spinner, list));


        names.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //int i = names.getSelectedItemPosition();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        Button bt_add_disciple = (Button) add.findViewById(R.id.btn_add_disciple);
        bt_add_disciple.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String disc = ed_disc.getText().toString();
                String name = names.getSelectedItem().toString();
                String time = tp_time.getCurrentHour().toString() + tp_time.getCurrentMinute();

                //get date
                int day = dp_date.getDayOfMonth();
                int month = dp_date.getMonth();
                int year =  dp_date.getYear();
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, day);
                Date date = calendar.getTime();

                        ContentValues values = new ContentValues();
                values.put(dbhelper.SCHEDULES_FIELDS[0], phone);
                values.put(dbhelper.SCHEDULES_FIELDS[1], date + time);
                values.put(dbhelper.SCHEDULES_FIELDS[2],0);
                values.put(dbhelper.SCHEDULES_FIELDS[3], disc);

                long i = dbadapter.insert(dbhelper.Table_SCHEDULES,values);
                if(i!=-1){
                    //insert the disciple to log table
                    ContentValues cv1 = new ContentValues();
                    cv1.put(DeepLife.LOGS_FIELDS[0], "Send_Schedule");
                    cv1.put(DeepLife.LOGS_FIELDS[1], dbadapter.get_Value_At_Bottom(DeepLife.Table_SCHEDULES, DeepLife.SCHEDULES_COLUMN[0]));
                    if(dbadapter.insert(DeepLife.Table_LOGS, cv1)!=-1){
                        Log.i("Deep Life", "Successfully Added to Log");
                    }

                    Toast.makeText(getActivity(),"Disciple successfully added!",Toast.LENGTH_SHORT).show();
                    add.cancel();
                    reload();
                }
            }
        });

    }




    @Override
	public void onPause() {
		// TODO Auto-generated method stub
	//	unregisterForContextMenu(newsView);
		super.onPause();
	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		
		super.onResume();
	//	registerForContextMenu(newsView);
	}
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		super.onCreateContextMenu(menu, v, menuInfo);
		
		MenuInflater inflater = getActivity().getMenuInflater();
		inflater.inflate(R.menu.main_context_menu, menu);

	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		
	       AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
	        switch (item.getItemId()){

	            case R.id.menu_save:
	            	Toast.makeText(getActivity(), "Save Option Chosen", Toast.LENGTH_SHORT).show();
	            case R.id.menu_delete:
	            	Toast.makeText(getActivity(), "Delete Option Chosen", Toast.LENGTH_SHORT).show();
	            default:
	                return super.onContextItemSelected(item);

	        }

	}


    public void delete_Dialog(final int id) {

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked

                        long deleted = dbadapter.remove(DeepLife.Table_SCHEDULES,id);
                        if(deleted!=-1){
                            Toast.makeText(getActivity(),"Successfully Deleted",Toast.LENGTH_SHORT).show();
                            reload();
                        }
                        break;
//				        case DialogInterface.BUTTON_NEUTRAL:
                    //Yes button clicked

                    //			            break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };


        android.app.AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Remove Schedule ").setMessage("Are You sure you want to remove this schedule" )
                .setPositiveButton("Yes ", dialogClickListener)
                .setNegativeButton("No", dialogClickListener)
//			   .setNeutralButton(" ", dialogClickListener)
                .show();
    }

    public void reload(){
        dbadapter.dispose();
        Intent intent = new Intent(this.getActivity(),MainMenu.class);
        startActivity(intent);
    }





    public class MySpinnerAdapter extends ArrayAdapter<String> {

        ArrayList<String> object;
        public MySpinnerAdapter(Context ctx, int txtViewResourceId, ArrayList<String> objects) {
            super(ctx, txtViewResourceId, objects);
            this.object = objects;
        }

        @Override
        public View getDropDownView(int position, View cnvtView, ViewGroup prnt) {
            return getCustomView(position, cnvtView, prnt);
        }
        @Override
        public View getView(int pos, View cnvtView, ViewGroup prnt) {
            return getCustomView(pos, cnvtView, prnt);
        }
        public View getCustomView(int position, View convertView,
                                  ViewGroup parent) {
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View mySpinner = inflater.inflate(R.layout.countries_spinner, parent,
                    false);
            TextView main_text = (TextView) mySpinner
                    .findViewById(R.id.spinner_text);
            main_text.setText(object.get(position));

            return mySpinner;
        }
    }




    public class MyDiscipleListAdapter extends BaseAdapter
    {
        Context context;
        ArrayList<Schedule> schedule;
        public MyDiscipleListAdapter(Context context,ArrayList<Schedule> schedule)
        {
            this.context = context;
            this.schedule = schedule;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return schedule.size();
        }

        @Override
        public Object getItem(int arg0) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflate = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflate.inflate(R.layout.schedule,null);


            TextView tv_name=(TextView)convertView.findViewById(R.id.schedulename);
            TextView tv_phone=(TextView)convertView.findViewById(R.id.schedulephone);
            TextView tv_disc=(TextView)convertView.findViewById(R.id.scheduledisciption);
            TextView tv_time=(TextView)convertView.findViewById(R.id.scheduletime);



            //final String name = schedule.get(position).ge;
            final String phone = schedule.get(position).getDis_Phone();
            final String time = schedule.get(position).getAlarm_Time();
            final String discription = schedule.get(position).getDescription();
            final int id = Integer.parseInt(schedule.get(position).getID());

            tv_name.setText(" Roger");
            tv_phone.setText(phone);
            tv_time.setText(time);
            tv_disc.setText(discription);


            convertView.setOnLongClickListener(new OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    delete_Dialog(id);
                    return true;
                }
            });


            return convertView;
        }
    }

	
}
