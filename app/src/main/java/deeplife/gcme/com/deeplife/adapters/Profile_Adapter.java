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
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(position == 0){
            convertView = layoutInflater.inflate(R.layout.profile_layout,null);
            TextView Labele = (TextView) convertView.findViewById(R.id.profile_state);
            ImageView Pic = (ImageView) convertView.findViewById(R.id.profile_image_);
            ImageView Pic_bg = (ImageView) convertView.findViewById(R.id.profile_image);

            BitmapFactory.Options option = new BitmapFactory.Options();
            option.inPreferredConfig = Bitmap.Config.ARGB_8888;

            File myFile = myFileManager.getFileAt("Profile","Profile.png");

            //Toast.makeText(myContext,myFile.getAbsolutePath(),Toast.LENGTH_LONG).show();
            Bitmap image = BitmapFactory.decodeFile(myFile.getAbsolutePath());

            Pic.setImageBitmap(image);
            Pic.setScaleType(ImageView.ScaleType.CENTER_CROP);

            Pic_bg.setImageBitmap(image);
            Pic_bg.setScaleType(ImageView.ScaleType.CENTER_CROP);




            //if(DeepLife.isConnectingToInternet()){
            //    Labele.setText("Online");
            //}else{
             //   Labele.setText("Offline");
           // }
            Labele.setText(myFile.getAbsolutePath());




        }else{
            convertView = layoutInflater.inflate(R.layout.profile_items,null);
            ImageView Icon = (ImageView) convertView.findViewById(R.id.profile_icon);
            TextView Labele = (TextView) convertView.findViewById(R.id.profile_item_name);

            if(position == 1){
                Icon.setBackgroundResource(R.drawable.account);
                String Name = myDatabase.get_Value_At_Top(DeepLife.Table_USER,DeepLife.USER_FIELDS[0]);
                Labele.setText(Name);
            }else if(position == 2){
                Icon.setBackgroundResource(R.drawable.settings_24_512);
                String Name = myDatabase.get_Value_At_Top(DeepLife.Table_USER,DeepLife.USER_FIELDS[1]);
                Labele.setText(Name);
            }else if(position == 3){
                Icon.setBackgroundResource(R.drawable.settings_24_512);
                String Name = myDatabase.get_Value_At_Top(DeepLife.Table_USER,DeepLife.USER_FIELDS[4]);
                Labele.setText(Name);
            }
        }
        return convertView;
    }

}
