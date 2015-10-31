package deeplife.gcme.com.deeplife;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
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

import deeplife.gcme.com.deeplife.database.Database;

/**
 * A simple {@link Fragment} subclass.
 * 
 */
public class AddDiscipleFragment extends Fragment {

	EditText ed_name;
	EditText ed_email;
	EditText ed_phone;
    EditText ed_country;
	Button addDisciple;

	int fallback;


	Database dbadapter;
	DeepLife dbhelper;
	@Override
	public View onCreateView(LayoutInflater inflater, final ViewGroup container,
			Bundle savedInstanceState) {
		
		
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_add_disciple, container,false);

		ed_name = (EditText) view.findViewById(R.id.adddisciple_name);
		ed_email = (EditText) view.findViewById(R.id.add_discple_email);
		ed_phone = (EditText) view.findViewById(R.id.add_disciple_phone);
        ed_country = (EditText) view.findViewById(R.id.add_disciple_country);

		dbadapter = new Database(getActivity());
		dbhelper = new DeepLife();

		addDisciple = (Button) view.findViewById(R.id.btn_add_disciple);

		addDisciple.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
					String name = ed_name.getText().toString();
					String email = ed_email.getText().toString();
					String phone = ed_phone.getText().toString();
					String country = ed_country.getText().toString();

					ContentValues values = new ContentValues();
					values.put(dbhelper.DISCIPLES_FIELDS[0], name);
					values.put(dbhelper.DISCIPLES_FIELDS[1], phone);
					values.put(dbhelper.DISCIPLES_FIELDS[2],email);
					values.put(dbhelper.DISCIPLES_FIELDS[3], "Added");
					values.put(dbhelper.DISCIPLES_FIELDS[4], country);

					long i = dbadapter.insert(dbhelper.Table_DISCIPLES, values);

					if(i!=-1){
						Log.i("EEEEEEEEEEEEEEE", values.toString());
						Toast.makeText(getActivity(), "Successfully Added!!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getActivity(),MainMenu.class);
                        startActivity(intent);
                        getActivity().finish();

                    }

			}
		});
		fallback = R.drawable.no_image;

		setHasOptionsMenu(true);

		return 	view;
		
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		dbadapter.dispose();
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
