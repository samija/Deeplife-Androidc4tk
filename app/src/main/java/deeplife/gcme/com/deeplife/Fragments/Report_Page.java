package deeplife.gcme.com.deeplife.Fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import deeplife.gcme.com.deeplife.Adapters.ReportItems_Adapter;
import deeplife.gcme.com.deeplife.Models.ReportItem;
import deeplife.gcme.com.deeplife.R;

/**
 * Created by BENGEOS on 2/17/16.
 */
public class Report_Page extends Fragment {
    private static List<ReportItem> Reports;
    public Report_Page(){
        Reports = new ArrayList<ReportItem>();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.report_page, container, false);
        ListView ReportLists = (ListView) view.findViewById(R.id.report_items);
        Reports.add(new ReportItem("1"));
        Reports.add(new ReportItem("2"));
        Reports.add(new ReportItem("3"));
        Reports.add(new ReportItem("4"));

        ReportLists.setAdapter(new ReportItems_Adapter(getActivity(), Reports));
        Button btn_Report = (Button) view.findViewById(R.id.btn_send_report);
        btn_Report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(),"First Report Item Value: "+Reports.get(0).getValue(),Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }
    public static void Update_Report_Value(int pos,int value){
        Reports.get(pos).setValue(value);
    }
}
