package deeplife.gcme.com.deeplife.Fragments;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import deeplife.gcme.com.deeplife.Activities.ProfileActivity;
import deeplife.gcme.com.deeplife.Database.DeepLife;
import deeplife.gcme.com.deeplife.Activities.MainMenu;
import deeplife.gcme.com.deeplife.Models.CountryDetails;
import deeplife.gcme.com.deeplife.R;
import deeplife.gcme.com.deeplife.Adapters.MySpinnerAdapter;
import deeplife.gcme.com.deeplife.Models.Disciples;
import deeplife.gcme.com.deeplife.Database.Database;

/**
 * A simple {@link Fragment} subclass.
 * 
 */
public class DiscipleList extends Fragment {

	
	public ListView lv_disciple;
	Button addDisciple;

    String mCurrentPhotoPath;

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
				}


		});

		return 	view;
		
	}





    public void addDiscipleDialog(){
        final Dialog add = new Dialog(getActivity().getBaseContext());

        add.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        add.requestWindowFeature(Window.FEATURE_NO_TITLE);

        add.setContentView(R.layout.fragment_add_disciple);

        WindowManager window = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        window.getDefaultDisplay().getMetrics(dm);
        double wid = dm.widthPixels*0.9;
        double hei = dm.heightPixels*0.8;
        int width = (int) wid;
        int height = (int) hei;
        add.getWindow().setLayout(width, height);


        add.show();

        final String[] list = CountryDetails.country;
        final String[] codes = CountryDetails.code;
        String[] genderarray = {"Male","Female"};


        final Spinner countries = (Spinner) add.findViewById(R.id.countries_spinner);
        final EditText ed_code = (EditText) add.findViewById(R.id.add_phone_country_code);
        final EditText ed_name = (EditText) add.findViewById(R.id.adddisciple_name);
        final EditText ed_email = (EditText) add.findViewById(R.id.add_discple_email);
        final EditText ed_phone = (EditText) add.findViewById(R.id.add_disciple_phone);
        final Spinner sp_gender = (Spinner) add.findViewById(R.id.gender_spinner);

        countries.setAdapter(new MySpinnerAdapter(getActivity(), R.layout.countries_spinner, list));
        sp_gender.setAdapter(new MySpinnerAdapter(getActivity(), R.layout.countries_spinner, genderarray));



        countries.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int i = countries.getSelectedItemPosition();
                ed_code.setText(codes[i]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                ed_code.setText(codes[0]);
            }
        });



        Button bt_add_disciple = (Button) add.findViewById(R.id.btn_add_disciple);
        bt_add_disciple.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = ed_name.getText().toString();
                String email = ed_email.getText().toString();
                String country = ed_code.getText().toString() + countries.getSelectedItem().toString();
                String gender = sp_gender.getSelectedItem().toString();
                String phone = ed_code.getText().toString() + ed_phone.getText().toString();

                ContentValues values = new ContentValues();
                values.put(dbhelper.DISCIPLES_FIELDS[0], name);
                values.put(dbhelper.DISCIPLES_FIELDS[1], phone);
                values.put(dbhelper.DISCIPLES_FIELDS[2],email);
                values.put(dbhelper.DISCIPLES_FIELDS[3], "Added");
                values.put(dbhelper.DISCIPLES_FIELDS[4], country);
                values.put(dbhelper.DISCIPLES_FIELDS[5], gender);

                long i = dbadapter.insert(dbhelper.Table_DISCIPLES,values);
                if(i!=-1){
                    //insert the disciple to log table
                    ContentValues cv1 = new ContentValues();
                    cv1.put(DeepLife.LOGS_FIELDS[0], "Send_Disciple");
                    cv1.put(DeepLife.LOGS_FIELDS[1], dbadapter.get_Value_At_Bottom(DeepLife.Table_DISCIPLES, DeepLife.DISCIPLES_COLUMN[0]));
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

	public void populateList(Context context){

		ArrayList<Disciples> discples = dbadapter.getDisciples();
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
                    ImageView iv_profilepic = (ImageView) convertView.findViewById(R.id.list_profile_pic);

					TextView tv_name=(TextView)convertView.findViewById(R.id.userN);
					TextView tv_phone=(TextView)convertView.findViewById(R.id.userphone);
					TextView tv_build_phase=(TextView)convertView.findViewById(R.id.userbuild);


					final String namee = disciples.get(position).getFull_Name();
					final String phonee = disciples.get(position).getPhone();
					final String buildd = disciples.get(position).getBuild_Phase();
					final int id = Integer.parseInt(disciples.get(position).getId());
                    final String idstring = disciples.get(position).getId();
                    final String picture = disciples.get(position).getPicture();


					tv_name.setText(namee);
					tv_phone.setText(phonee);
					tv_build_phase.setText(buildd);

                    if(picture!=null) {
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inSampleSize = 4;
                        iv_profilepic.setImageBitmap(BitmapFactory.decodeFile(picture, options));
                    }

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
