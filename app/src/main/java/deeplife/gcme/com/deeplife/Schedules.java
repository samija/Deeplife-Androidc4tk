package deeplife.gcme.com.deeplife;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import deeplife.gcme.com.deeplife.database.Database;

/**
 * A simple {@link Fragment} subclass.
 * 
 */
public class Schedules extends Fragment {

	
	private static final int FRAGMENT_GROUPID = 30;
	public ListView lv_disciple;
	String tagname;
	Button addDisciple;
	int fallback;
	
	int count = 0;
	
	public static final int MENU_EDIT =1;
	public static final int MENU_DELETE = 2;


	String[] names = {"Roger", "Biniam", "Henock","Biruk","Selam"};
	String[] phones = {"0916825542","09111111", "0916546565","09523536","091379341"};
	String[] times = {"Thuesday 10pm", "Monday 4pm" ,"Wednesday 6pm","Sunday 8am", "Monday 4pm"};


	Database dbadapter;
	DeepLife dbhelper;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_disciples_list, container,
				false);


		dbadapter = new Database(getActivity());
		dbhelper = new DeepLife();

		lv_disciple = (ListView) view.findViewById(R.id.ls_disciple);


        lv_disciple.setAdapter(new setadapter(getActivity(),names, phones, times));

            //populateList(getActivity());

		registerForContextMenu(lv_disciple);
		
		addDisciple = (Button) view.findViewById(R.id.bt_add_disciple);
		addDisciple.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(),AddDisciple.class);
				startActivity(intent);
			}
		});
		fallback = R.drawable.no_image;

		setHasOptionsMenu(true);

		//new AsyncDownloader().execute("http://newsrss.bbc.co.uk/rss/newsonline_uk_edition/entertainment/rss.xml");

		return 	view;
		
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






    public class setadapter extends BaseAdapter
    {
        String[] name;
        String[] phone;
        String[] build;
        public setadapter(Context context,String[] n, String[] p, String[] b)
        {
            name = n;
            phone = p;
            build = b;
        }
        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return name.length;
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
            convertView=inflate.inflate(R.layout.schedule,null);

            TextView tv_name=(TextView)convertView.findViewById(R.id.schedulename);
            TextView tv_phone=(TextView)convertView.findViewById(R.id.schedulephone);
            TextView tv_time=(TextView)convertView.findViewById(R.id.scheduletime);
			
            final String namee = name[position];
            final String phonee = phone[position];
            final String timee = build[position];


            //TextView title_first = (TextView) convertView.findViewById(R.id.title_first_word);
            //String firstowrd = news_list.get(position).getTitle().substring(0, 1);
            //  title_first.setText(firstowrd);



            tv_name.setText(namee);
            tv_phone.setText(phonee);
            tv_time.setText(timee);


            convertView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {

                    //startActivity(intent);

                }
            });
            convertView.setOnLongClickListener(new OnLongClickListener() {

                @Override
                public boolean onLongClick(View v) {
                    // TODO Auto-generated method stub

                    //Show_DialogBox(id,name);
                    return true;
                }
            });



            return convertView;
        }
    }

	
}
