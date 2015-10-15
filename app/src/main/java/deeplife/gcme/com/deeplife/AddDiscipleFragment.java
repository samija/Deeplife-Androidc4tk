package deeplife.gcme.com.deeplife;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.CursorAdapter;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * 
 */
public class AddDiscipleFragment extends Fragment {

	EditText ed_name;
	EditText ed_email;
	EditText ed_phone;
	Button addDisciple;

	int fallback;
	

	DbAdapter dbadapter;
	DbHelper dbhelper;

	@Override
	public View onCreateView(LayoutInflater inflater, final ViewGroup container,
			Bundle savedInstanceState) {
		
		
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_add_disciple, container,
				false);

		ed_name = (EditText) view.findViewById(R.id.adddisciple_name);
		ed_email = (EditText) view.findViewById(R.id.add_discple_email);
		ed_phone = (EditText) view.findViewById(R.id.add_disciple_phone);


		dbadapter = new DbAdapter(getActivity());
		dbhelper = new DbHelper(getActivity());

		addDisciple = (Button) view.findViewById(R.id.btn_add_disciple);

		addDisciple.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
					String name = ed_name.getText().toString();
					String email = ed_email.getText().toString();
					String phone = ed_phone.getText().toString();
					ContentValues values = new ContentValues();
					values.put(dbhelper.USER_NAME, name);
					values.put(dbhelper.USER_PHONE_NUMBER, phone);
					values.put(dbhelper.USER_PASSWORD,"");

					values.put(dbhelper.USER_EMAIL, email);
					values.put(dbhelper.BUILD_PHASE,"added");
					Log.i("EEEEEEEEEEEEEEE", values.toString());
					dbadapter.addUser(values);
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

	
}
