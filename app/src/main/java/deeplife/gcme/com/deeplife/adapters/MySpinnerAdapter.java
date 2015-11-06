package deeplife.gcme.com.deeplife.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import deeplife.gcme.com.deeplife.R;

/**
 * Created by rog on 11/6/2015.
 */
public class MySpinnerAdapter extends ArrayAdapter<String> {

    String[] object;
    Context context;
    public MySpinnerAdapter(Context ctx, int txtViewResourceId, String[] objects) {
        super(ctx, txtViewResourceId, objects);
        this.object = objects;
        context = ctx;
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
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View mySpinner = inflater.inflate(R.layout.countries_spinner, parent,
                false);
        TextView main_text = (TextView) mySpinner
                .findViewById(R.id.spinner_text);
        main_text.setText(object[position]);

        return mySpinner;
    }
}
