package deeplife.gcme.com.deeplife.Adapters;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import deeplife.gcme.com.deeplife.Activities.MainMenu;
import deeplife.gcme.com.deeplife.Database.Database;
import deeplife.gcme.com.deeplife.Database.DeepLife;
import deeplife.gcme.com.deeplife.FileManager.FileManager;
import deeplife.gcme.com.deeplife.Models.Disciples;
import deeplife.gcme.com.deeplife.R;

/**
 * Created by BENGEOS on 11/7/2015.
 */
public class Profile_Adapter extends BaseAdapter {
    private Context myContext;
    private ArrayList<Disciples> myEntries;
    private Database myDatabase;
    private AlertDialog.Builder myBuilder;
    private FileManager myFileManager;
    private AlertDialog.Builder builder;

    public Profile_Adapter(Context context,ArrayList<Disciples> entries){
        myEntries = entries;
        myContext = context;
        myDatabase = new Database(myContext);
        myBuilder = new AlertDialog.Builder(myContext);
        myFileManager = new FileManager(myContext);
    }
    @Override
    public int getCount() {
        return myEntries.size();
    }

    @Override
    public Object getItem(int position) {
        return myEntries.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(position == 0){
            convertView = layoutInflater.inflate(R.layout.profile_layout,null);
            TextView Labele = (TextView) convertView.findViewById(R.id.profile_state);
            final ImageView Pic = (ImageView) convertView.findViewById(R.id.profile_image_);

            final ImageView Pic_bg = (ImageView) convertView.findViewById(R.id.profile_image);
            BitmapFactory.Options option = new BitmapFactory.Options();
            option.inPreferredConfig = Bitmap.Config.ARGB_8888;

            final File myFile = myFileManager.getFileAt("Profile","Profile.png");

            Pic_bg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(myFile.isFile()){
                        //Toast.makeText(myContext,myFile.getAbsolutePath(),Toast.LENGTH_LONG).show();
                        Bitmap image = BitmapFactory.decodeFile(myFile.getAbsolutePath());

                        Pic.setImageBitmap(image);
                        Pic.setScaleType(ImageView.ScaleType.CENTER_CROP);

                        Pic_bg.setImageBitmap(image);
                        Pic_bg.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    }


                }
            });

            //Toast.makeText(myContext,myFile.getAbsolutePath(),Toast.LENGTH_LONG).show();
//            Bitmap image = BitmapFactory.decodeFile(myFile.getAbsolutePath());

   ///         Pic.setImageBitmap(image);
     //       Pic.setScaleType(ImageView.ScaleType.CENTER_CROP);

        //    Pic_bg.setImageBitmap(image);
        //    Pic_bg.setScaleType(ImageView.ScaleType.CENTER_CROP);





            //if(DeepLife.isConnectingToInternet()){
            //    Labele.setText("Online");
            //}else{
             //   Labele.setText("Offline");
           // }




        }else{
            convertView = layoutInflater.inflate(R.layout.profile_items,null);
            ImageView Icon = (ImageView) convertView.findViewById(R.id.profile_icon);
            final TextView Labele = (TextView) convertView.findViewById(R.id.profile_item_name);
            LinearLayout Item = (LinearLayout) convertView.findViewById(R.id.profile_item);

            if(position == 1){
                Icon.setBackgroundResource(R.drawable.account);
                String Name = myDatabase.get_Value_At_Top(DeepLife.Table_USER,DeepLife.USER_FIELDS[0]);
                Labele.setText(Name);

                Item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Labele.setText(myDatabase.get_Value_At_Top(DeepLife.Table_USER,DeepLife.USER_FIELDS[0]));
                    }
                });
            }else if(position == 2){
                Icon.setBackgroundResource(R.drawable.settings_24_512);
                String Name = myDatabase.get_Value_At_Top(DeepLife.Table_USER,DeepLife.USER_FIELDS[1]);
                Labele.setText(Name);
                Item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Labele.setText(myDatabase.get_Value_At_Top(DeepLife.Table_USER, DeepLife.USER_FIELDS[1]));
                    }
                });
            }else if(position == 3){
                Icon.setBackgroundResource(R.drawable.settings_24_512);
                String Name = myDatabase.get_Value_At_Top(DeepLife.Table_USER,DeepLife.USER_FIELDS[4]);
                Labele.setText(Name);
                Item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Labele.setText(myDatabase.get_Value_At_Top(DeepLife.Table_USER, DeepLife.USER_FIELDS[4]));
                    }
                });
            }else if(position == 4){
                Icon.setBackgroundResource(R.drawable.check_out_512);
                Labele.setText("Log Out");
            }
        }
        return convertView;
    }

}
