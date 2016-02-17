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
import android.widget.TextView;

import deeplife.gcme.com.deeplife.R;

/**
 * Created by BENGEOS on 2/17/16.
 */
public class Report_Page extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.report_page, container, false);
        Button btn1_Inc,btn1_Dec,btn2_Inc,btn2_Dec,btn_Send;
        TextView chs1,chs2,chs3;
        final EditText input1,txt1,txt2;
        txt1 = (EditText) view.findViewById(R.id.txt_1);
        txt2 = (EditText) view.findViewById(R.id.txt_2);

        btn1_Inc = (Button) view.findViewById(R.id.btn_inc);
        btn1_Inc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int x = Integer.parseInt(txt1.getText().toString());
                if(x<100){
                    x++;
                    txt1.setText(""+x);
                }

            }
        });
        btn1_Dec = (Button) view.findViewById(R.id.btn_dec);
        btn1_Dec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int x = Integer.parseInt(txt1.getText().toString());
                if(x>0){
                    x--;
                    txt1.setText(""+x);
                }
            }
        });
        btn2_Inc = (Button) view.findViewById(R.id.btn2_inc);
        btn2_Inc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int x = Integer.parseInt(txt2.getText().toString());
                if(x<100){
                    x++;
                    txt2.setText(""+x);
                }
            }
        });
        btn2_Dec = (Button) view.findViewById(R.id.btn2_dec);
        btn2_Dec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int x = Integer.parseInt(txt2.getText().toString());
                if(x>0){
                    x--;
                    txt2.setText(""+x);
                }
            }
        });
        btn_Send = (Button) view.findViewById(R.id.btn_send);



        return view;
    }
}
