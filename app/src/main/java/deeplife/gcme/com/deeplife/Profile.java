package deeplife.gcme.com.deeplife;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

import deeplife.gcme.com.deeplife.data_types.Disciples;
import deeplife.gcme.com.deeplife.database.Database;

/**
 * A simple {@link Fragment} subclass.
 * 
 */
public class Profile extends Fragment {

	public ListView lv_schedule;

    TextView tv_build, tv_name, tv_phone, tv_gender,tv_email;

    ImageButton imageView;
    ImageView profile_pic;

    String disciple_id;
	ArrayList<String> schedule_list;

	Button addDisciple;

	Database dbadapter;
	DeepLife dbhelper;
    private String mCurrentPhotoPath;
    private Bitmap image;


    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.disciple_dashboard, container,
				false);

        ProfileActivity myactivity = (ProfileActivity) getActivity();

		dbadapter = new Database(getActivity());
		dbhelper = new DeepLife();

        disciple_id = myactivity.getDisciple_id();
        Log.i("Deeeeep Life",disciple_id + "");

        tv_build = (TextView) view.findViewById(R.id.profile_build_stage);
        tv_name = (TextView) view.findViewById(R.id.profile_name);
        tv_phone = (TextView) view.findViewById(R.id.profile_phone);
        tv_gender = (TextView) view.findViewById(R.id.profile_gender);
        tv_email = (TextView) view.findViewById(R.id.profile_email);

        imageView = (ImageButton) view.findViewById(R.id.profile_cover);
        profile_pic = (ImageView) view.findViewById(R.id.profile_pic);

        populateView(disciple_id);

        imageView.setOnClickListener(new ImagePickListener());

		lv_schedule = (ListView) view.findViewById(R.id.profile_schedule_list);

		addDisciple = (Button) view.findViewById(R.id.profile_add_button);
		addDisciple.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

                //add.setContentView(R.layout.fragment_add_disciple);
                //add.setTitle("Add Desciple");
                //add.show();

                //FragmentTransaction ft = getFragmentManager().beginTransaction();
               //AddDiscipleDialog fd = new AddDiscipleDialog();
                //fd.show(ft,"addDisciple");

				Intent intent = new Intent(getActivity(),AddDisciple.class);
				startActivity(intent);

                //AddDiscipleDialog frag = new AddDiscipleDialog();
                //frag.show(getFragmentManager(),"addDisciple");
				}


		});


		return 	view;
		
	}

    public void populateView(String id){

        Cursor data = dbadapter.get_value_by_ID(DeepLife.Table_DISCIPLES, id);
        Log.i("deeeeepLife", data.getCount() + "");

        if(data != null && data.moveToFirst() && data.getCount()>0) {
            String name = data.getString(data.getColumnIndex(DeepLife.DISCIPLES_FIELDS[0]));
            String phone = data.getString(data.getColumnIndex(DeepLife.DISCIPLES_FIELDS[1]));
            String build = data.getString(data.getColumnIndex(DeepLife.DISCIPLES_FIELDS[3]));
            String email = data.getString(data.getColumnIndex(DeepLife.DISCIPLES_FIELDS[2]));
            String gender = data.getString(data.getColumnIndex(DeepLife.DISCIPLES_FIELDS[5]));
            String country = data.getString(data.getColumnIndex(DeepLife.DISCIPLES_FIELDS[4]));
            String picture = data.getString(data.getColumnIndex(DeepLife.DISCIPLES_FIELDS[6]));

            tv_email.setText(email);
            tv_build.setText(build);
            tv_name.setText(name);
            tv_phone.setText(phone);
            tv_gender.setText(gender);

            if(picture!=null){
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 4;
                profile_pic.setImageBitmap(BitmapFactory.decodeFile(picture, options));            }

            data.close();

        }
    }

	public void populateList(Context context){

		//ArrayList<Disciples> discples = dbadapter.getDisciples(dbhelper.Table_DISCIPLES);
		//lv_disciple.setAdapter(new MyDiscipleListAdapter(getActivity(),discples));
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




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode== Activity.RESULT_OK){
            Log.i("Deeeep Life","On Activity result called");
            switch (requestCode){
                case 1:
                    this.imageFromGallery(resultCode,data);
                    break;
            }
        }
    }




    private void imageFromGallery(int resultCode, Intent data) {
        Uri selectedImage = data.getData();
        String [] filePathColumn = {MediaStore.Images.Media.DATA};

        Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String filePath = cursor.getString(columnIndex);
        cursor.close();

        mCurrentPhotoPath = filePath;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 4;

        ContentValues content = new ContentValues();
        content.put(dbhelper.DISCIPLES_FIELDS[6],mCurrentPhotoPath);
        long i = dbadapter.update(dbhelper.Table_DISCIPLES,content,Integer.parseInt(disciple_id));
        if(i!=-1){
            Toast.makeText(getActivity(),"Profile Picture changed!",Toast.LENGTH_SHORT).show();
            this.updateImageView(BitmapFactory.decodeFile(filePath, options));
        }
        else{
            Toast.makeText(getActivity(),"There was an error! Couldn't change picture", Toast.LENGTH_SHORT).show();
        }
    }



    private void updateImageView(Bitmap newImage) {
        BitmapProcessor bitmapProcessor = new BitmapProcessor(newImage, 1000, 500, 0);


        this.image = bitmapProcessor.getBitmap();
        this.profile_pic.setImageBitmap(this.image);

    }
    
    
    
    
    
	public void delete_Dialog(final int id,final String name) {


        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked
                        long i = dbadapter.remove(dbhelper.Table_DISCIPLES, id);
                        if(i!=-1){
                            Log.i("DeepLife", "Successfully Deleted");
                            Toast.makeText(getActivity(),"Successfully Deleted!", Toast.LENGTH_SHORT).show();
                        }
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
    }




	public class MyScheduleListAdapter extends BaseAdapter
			{
				Context context;
				ArrayList<String> schedule;
				public MyScheduleListAdapter(Context context,ArrayList<String> schedules)
				{
					this.context = context;
					schedule = schedules;
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
					convertView = inflate.inflate(R.layout.dislist,null);

					TextView tv_name=(TextView)convertView.findViewById(R.id.userN);
					TextView tv_phone=(TextView)convertView.findViewById(R.id.userphone);
					TextView tv_build_phase=(TextView)convertView.findViewById(R.id.userbuild);

					//final String namee = schedule.get(position).getFull_Name();

					//tv_name.setText(namee);



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

							return true;
						}
					});



				return convertView;
				}
	}


    class ImagePickListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            startActivityForResult(Intent.createChooser(intent, "Deep Life"), 1);

        }
    }






}
