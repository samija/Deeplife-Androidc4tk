package deeplife.gcme.com.deeplife;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.CursorAdapter;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import deeplife.gcme.com.deeplife.data_types.Disciples;
import deeplife.gcme.com.deeplife.data_types.Schedule;
import deeplife.gcme.com.deeplife.database.Database;

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
