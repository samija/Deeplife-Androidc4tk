package deeplife.gcme.com.deeplife.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;

import deeplife.gcme.com.deeplife.Activities.WinActivity;
import deeplife.gcme.com.deeplife.Models.Question;
import deeplife.gcme.com.deeplife.R;


/**
 * Created by rog on 11/7/2015.
 */


public class WinFragment extends Fragment {

    public static final String ARG_PAGE = "page";

    private int mPageNumber;

    RadioGroup rdGroup;
    RadioButton rb_yes;
    RadioButton rb_no;
    TextView tv_qdisc, tv_note;

    public static WinFragment create(int pageNumber) {
        WinFragment fragment = new WinFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, pageNumber);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPageNumber = getArguments().getInt(ARG_PAGE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout containing a title and body text.
        ViewGroup rootView = (ViewGroup) inflater
                .inflate(R.layout.winfragment, container, false);

         tv_qdisc = (TextView) rootView.findViewById(R.id.win_question);
        rb_yes = (RadioButton) rootView.findViewById(R.id.rb_yes);
        rb_no = (RadioButton) rootView.findViewById(R.id.rb_no);
        tv_note = (TextView) rootView.findViewById(R.id.win_note);
        Log.i("Deep Life", "The Page number inside win fragment is "+getPageNumber() + "");
        if(getPageNumber()>4) {
            tv_qdisc.setText(WinActivity.questions.get(getPageNumber() - 2).getDescription());
            tv_note.setText(WinActivity.questions.get(getPageNumber() - 2).getNote());
        }

        if(!rb_no.isSelected() & !rb_yes.isSelected()){
            WinActivity.mPager.setSwipeable(false);
        }


        rb_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                WinActivity.mPager.setSwipeable(true);
            }
        });
        rb_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WinActivity.mPager.setSwipeable(true);
            }
        });

        //rb_no.setSelected(true);

        return rootView;
    }


    public int getPageNumber() {
        return mPageNumber;
    }
}
