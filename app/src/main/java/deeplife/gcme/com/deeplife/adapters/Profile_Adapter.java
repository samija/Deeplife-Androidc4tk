package deeplife.gcme.com.deeplife.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import deeplife.gcme.com.deeplife.Models.Disciples;
import deeplife.gcme.com.deeplife.R;

/**
 * Created by BENGEOS on 11/7/2015.
 */
public class Profile_Adapter extends BaseAdapter {
    private Context myContext;
    private ArrayList<Disciples> myEntries;

    public Profile_Adapter(Context context,ArrayList<Disciples> entries){
        myEntries = entries;
        myContext = context;
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
        }else{
            convertView = layoutInflater.inflate(R.layout.profile_items,null);
            ImageView Icon = (ImageView) convertView.findViewById(R.id.profile_icon);
            TextView Labele = (TextView) convertView.findViewById(R.id.profile_item_name);
            if(position == 1){
                Icon.setBackgroundResource(R.drawable.account);
                Labele.setText("Account");
            }else if(position == 2){
                Icon.setBackgroundResource(R.drawable.settings_24_512);
                Labele.setText("Setting");
            }
        }
        return convertView;
    }
}
