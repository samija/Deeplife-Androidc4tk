package deeplife.gcme.com.deeplife;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.CursorAdapter;
import android.text.Html;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AbsoluteLayout;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import deeplife.gcme.com.deeplife.data_types.Disciples;
import deeplife.gcme.com.deeplife.database.Database;

/**
 * A simple {@link Fragment} subclass.
 * 
 */
public class DiscipleList extends Fragment {

	
	private static final int FRAGMENT_GROUPID = 30;
	public ListView lv_disciple;
	ArrayList<Disciples> disciples_list;
	String tagname;
	Button addDisciple;

	public static final int MENU_EDIT =1;
	public static final int MENU_DELETE = 2;


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


        populateList(getActivity());



		addDisciple = (Button) view.findViewById(R.id.bt_add_disciple);
		addDisciple.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
                    addDiscipleDialog();

                //FragmentTransaction ft = getFragmentManager().beginTransaction();
               //AddDiscipleDialog fd = new AddDiscipleDialog();
                //fd.show(ft,"addDisciple");

				//Intent intent = new Intent(getActivity(),AddDisciple.class);
				//startActivity(intent);

                //AddDiscipleDialog frag = new AddDiscipleDialog();
                //frag.show(getFragmentManager(),"addDisciple");
				}


		});

		return 	view;
		
	}

    public void addDiscipleDialog(){
        Dialog add = new Dialog(DiscipleList.this.getActivity().getBaseContext());
        add.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        add.setContentView(R.layout.fragment_add_disciple);


        String[] list = CountryDetails.country;

        ArrayList<String> country_list = new ArrayList<String>();

        for(int i=0;i<list.length;i++){
            country_list.add(list[i]);
        }
        Log.i("Deeep Lifeeeeeeeeeeee",country_list.toString());
        Log.i("Deeeeep Lifeeeeeeee", "Coountry array length = " + country_list.size());

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getActivity(), android.R.layout.simple_spinner_item, country_list);

        Spinner countries = (Spinner) add.findViewById(R.id.countries_spinner);

        countries.setAdapter(new MySpinnerAdapter(getActivity(),R.layout.countries_spinner,list));

        add.setTitle("Add Desciple");
        add.show();
    }

	public void populateList(Context context){

		ArrayList<Disciples> discples = dbadapter.getDisciples(dbhelper.Table_DISCIPLES);
		lv_disciple.setAdapter(new MyDiscipleListAdapter(getActivity(), discples));
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
	public void onDestroy() {
		super.onDestroy();
		dbadapter.dispose();
	}

	public void delete_Dialog(final int id,final String name) {

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked

                        long deleted = dbadapter.remove(DeepLife.Table_DISCIPLES,id);
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
        builder.setTitle("Delete Disciple ").setMessage("Do you want to delete you disciple "+name)
                .setPositiveButton("Delete ", dialogClickListener)
                .setNegativeButton("Cancel", dialogClickListener)
//			   .setNeutralButton(" ", dialogClickListener)
                .show();
    }


    public void reload(){
        dbadapter.dispose();
        Intent intent = new Intent(this.getActivity(),MainMenu.class);
        startActivity(intent);
    }


    public class MySpinnerAdapter extends ArrayAdapter<String> {

        String[] object;
        public MySpinnerAdapter(Context ctx, int txtViewResourceId, String[] objects) {
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
            main_text.setText(object[position]);

            return mySpinner;
        }
    }




    public class MyDiscipleListAdapter extends BaseAdapter
			{
				Context context;
				ArrayList<Disciples> disciples;
				public MyDiscipleListAdapter(Context context,ArrayList<Disciples> disciple)
				{
					this.context = context;
					disciples = disciple;
				}
				@Override
				public int getCount() {
					// TODO Auto-generated method stub
					return disciples.size();
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
					convertView = inflate.inflate(R.layout.dislist,null);

                    ImageView dialer = (ImageView) convertView.findViewById(R.id.disciple_phoneimage);

					TextView tv_name=(TextView)convertView.findViewById(R.id.userN);
					TextView tv_phone=(TextView)convertView.findViewById(R.id.userphone);
					TextView tv_build_phase=(TextView)convertView.findViewById(R.id.userbuild);

					final String namee = disciples.get(position).getFull_Name();
					final String phonee = disciples.get(position).getPhone();
					final String buildd = disciples.get(position).getBuild_Phase();
					final int id = Integer.parseInt(disciples.get(position).getId());
                    final String idstring = disciples.get(position).getId();

					tv_name.setText(namee);
					tv_phone.setText(phonee);
					tv_build_phase.setText(buildd);

                    convertView.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(DiscipleList.this.getActivity().getApplicationContext(), ProfileActivity.class);
                            Bundle b = new Bundle();
                            b.putString("id", idstring);
                            intent.putExtras(b);
                            startActivity(intent);
                        }
                    });

                    convertView.setOnLongClickListener(new OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            delete_Dialog(id,namee);
                            return true;
                        }
                    });

                    dialer.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(Intent.ACTION_DIAL);
                            intent.setData(Uri.parse("tel:"+phonee));
                            startActivity(intent);
                        }
                    });






				return convertView;
				}
	}





	
}
