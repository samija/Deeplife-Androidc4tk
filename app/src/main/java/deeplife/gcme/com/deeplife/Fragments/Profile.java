package deeplife.gcme.com.deeplife.Fragments;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import deeplife.gcme.com.deeplife.Activities.AddDisciple;
import deeplife.gcme.com.deeplife.Activities.BuildActivity;
import deeplife.gcme.com.deeplife.Activities.ProfileActivity;
import deeplife.gcme.com.deeplife.Activities.SendActivity;
import deeplife.gcme.com.deeplife.Activities.WinActivity;
import deeplife.gcme.com.deeplife.Activities.WinViewPager;
import deeplife.gcme.com.deeplife.Database.DeepLife;
import deeplife.gcme.com.deeplife.R;
import deeplife.gcme.com.deeplife.Database.Database;

/**
 * A simple {@link Fragment} subclass.
 * 
 */
public class Profile extends Fragment {

	public ListView lv_schedule;

    TextView tv_build, tv_name, tv_phone, tv_gender,tv_email;

    ImageButton imageButton;
    ImageView profile_pic;

    Button btn_complet;

    String disciple_id;
	ArrayList<String> schedule_list;


	Database dbadapter;
	DeepLife dbhelper;

    private String mCurrentPhotoPath;
    private String newCurrentPhotoPath;

    private Bitmap image;

    public final static int CHANGE_PIC = 1;
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

        btn_complet = (Button) view.findViewById(R.id.btn_complete_build);
        tv_build = (TextView) view.findViewById(R.id.profile_build_stage);
        tv_name = (TextView) view.findViewById(R.id.profile_name);
        tv_phone = (TextView) view.findViewById(R.id.profile_phone);
        tv_gender = (TextView) view.findViewById(R.id.profile_gender);
        tv_email = (TextView) view.findViewById(R.id.profile_email);

        imageButton = (ImageButton) view.findViewById(R.id.profile_cover);
        profile_pic = (ImageView) view.findViewById(R.id.profile_pic);

        populateView(disciple_id);

        imageButton.setOnClickListener(new ImagePickListener());

		lv_schedule = (ListView) view.findViewById(R.id.profile_schedule_list);


		return 	view;
		
	}

    public void populateView(String id){

        Cursor data = dbadapter.get_value_by_ID(DeepLife.Table_DISCIPLES, id);
        Log.i("deeeeepLife", data.getCount() + "");

        if(data != null && data.moveToFirst() && data.getCount()>0) {
            String name = data.getString(data.getColumnIndex(DeepLife.DISCIPLES_FIELDS[0]));
            String email = data.getString(data.getColumnIndex(DeepLife.DISCIPLES_FIELDS[1]));
            String phone = data.getString(data.getColumnIndex(DeepLife.DISCIPLES_FIELDS[2]));
            String country = data.getString(data.getColumnIndex(DeepLife.DISCIPLES_FIELDS[3]));
            final String build = data.getString(data.getColumnIndex(DeepLife.DISCIPLES_FIELDS[4]));
            String gender = data.getString(data.getColumnIndex(DeepLife.DISCIPLES_FIELDS[5]));
            String picture = data.getString(data.getColumnIndex(DeepLife.DISCIPLES_FIELDS[6]));

            tv_email.setText(email);
            if(build.endsWith("SEND")){
                tv_build.setText("COMPLETED");
            }
            else{tv_build.setText(build);}
            tv_name.setText(name);
            tv_phone.setText(phone);
            tv_gender.setText(gender);

            if(picture!=null){

                BitmapFactory.Options options = new BitmapFactory.Options();
                //options.inSampleSize = 4;
                Bitmap image = BitmapFactory.decodeFile(picture);
                profile_pic.setImageBitmap(image);
                profile_pic.setScaleType(ImageView.ScaleType.CENTER_CROP);
            }

            btn_complet.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(build.endsWith("Added")) {
                        Intent intent = new Intent(Profile.this.getActivity(), WinActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("disciple_id", disciple_id);

                        if(dbadapter.checkExistence(DeepLife.Table_QUESTION_ANSWER,DeepLife.QUESTION_ANSWER_FIELDS[0],disciple_id)>0){
                            bundle.putString("answer","yes");
                        }

                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                    else if(build.endsWith("WIN")){
                        Intent intent = new Intent(Profile.this.getActivity(), BuildActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("disciple_id", disciple_id);
                        if(dbadapter.checkExistence(DeepLife.Table_QUESTION_ANSWER,DeepLife.QUESTION_ANSWER_FIELDS[0],disciple_id)>0){
                            bundle.putString("answer","yes");
                        }
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                    else if(build.endsWith("BUILD")){
                        Intent intent = new Intent(Profile.this.getActivity(), SendActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("disciple_id", disciple_id);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                }
            });

        }
        data.close();

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
                case CHANGE_PIC:
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
        options.inSampleSize = 2;

        OutputStream out;
        InputStream in;

        //Bitmap newScaledImage = scaleBitmap(BitmapFactory.decodeFile(filePath, options), 300f, 300f);

        try {
           File newImageFile = createImageFile();
            //in = new FileInputStream(new File(mCurrentPhotoPath));
            out = new FileOutputStream(new File(newCurrentPhotoPath));
            //BitmapFactory.decodeFile(mCurrentPhotoPath).compress(Bitmap.CompressFormat.JPEG,50,out) ;
            scaleBitmap(BitmapFactory.decodeFile(mCurrentPhotoPath, options), 590f, 450f).compress(Bitmap.CompressFormat.JPEG,80,out);

            //copyFile(in,out);
            //in.close();
            //in = null;
            out.flush();
            out.close();
            out = null;

        } catch (IOException e) {
            e.printStackTrace();
        }
        if(newCurrentPhotoPath!=null) {
            //then add new image to database
            ContentValues content = new ContentValues();
            content.put(dbhelper.DISCIPLES_FIELDS[6], newCurrentPhotoPath);
            long i = dbadapter.update(dbhelper.Table_DISCIPLES, content, Integer.parseInt(disciple_id));
            if (i != -1) {
                Toast.makeText(getActivity(), "Profile Picture changed!", Toast.LENGTH_SHORT).show();
                this.updateImageView(BitmapFactory.decodeFile(newCurrentPhotoPath, options));
            } else {
                Toast.makeText(getActivity(), "There was an error! Couldn't change picture", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void updateImageView(Bitmap newImage) {
      // BitmapProcessor bitmapProcessor = new BitmapProcessor(newImage, 250, 250, 0);

        //this.image = bitmapProcessor.getBitmap();
        //this.profile_pic.setImageBitmap(this.image);
        this.profile_pic.setImageBitmap(BitmapFactory.decodeFile(newCurrentPhotoPath));
    }

    public static Bitmap scaleBitmap(Bitmap bitmapToScale, float newWidth, float newHeight) {
                if(bitmapToScale == null)
                    return null;
        //get the original width and height
                int width = bitmapToScale.getWidth();
                int height = bitmapToScale.getHeight();
        // create a matrix for the manipulation
                Matrix matrix = new Matrix();

        // resize the bit map
                matrix.postScale(newWidth / width, newHeight / height);

        // recreate the new Bitmap and set it back
                return Bitmap.createBitmap(bitmapToScale, 0, 0, bitmapToScale.getWidth(), bitmapToScale.getHeight(), matrix, true);
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        String storageDir = Environment.getExternalStorageDirectory() + "/deeplife";
        File dir = new File(storageDir);
        if (!dir.exists()) {
            if(dir.mkdir()){
                Log.i("Deep Life", "Directory created!");
            }

        }
        File image = new File(storageDir + "/" + imageFileName + ".jpg");

        // Save a file: path for use with ACTION_VIEW intents
        newCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }


    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while((read = in.read(buffer)) != -1){
            out.write(buffer, 0, read);
        }
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
            startActivityForResult(Intent.createChooser(intent, "Deep Life"), CHANGE_PIC);

        }
    }






}
