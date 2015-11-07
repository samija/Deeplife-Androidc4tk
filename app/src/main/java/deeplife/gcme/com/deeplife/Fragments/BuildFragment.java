package deeplife.gcme.com.deeplife.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import deeplife.gcme.com.deeplife.Models.Question;
import deeplife.gcme.com.deeplife.R;


/**
 * Created by rog on 11/7/2015.
 */


public class BuildFragment extends Fragment {

    public static final String ARG_PAGE = "page";

    private int mPageNumber;


    public static ArrayList<Question> questions = new ArrayList<Question>();

    public static BuildFragment create(int pageNumber) {
        BuildFragment fragment = new BuildFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, pageNumber);

        Bundle b = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        questions.add(new Question("1", "Win", "Is your disciple filled with the Holy Spirit? To be filled with Holy Spirit means ...."));
        questions.add(new Question("2","Win","Is your sdfhsdfhsdfhsdfhsdfhsdfhsdfhsdf  sdfhsdfh sdfh sdf hsdfh sdfh sdfh sdfh ...."));
        questions.add(new Question("3","Win","When changing pages, reset the action bar actions since they are dependent...."));
        questions.add(new Question("4","Win","fragment expose actions itself (rather than the activity exposing actions),...."));
        questions.add(new Question("5", "Win", "Is your disciple filled with the Holy Spirit? To be filled with Holy Spirit means ...."));

        mPageNumber = getArguments().getInt(ARG_PAGE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout containing a title and body text.
        ViewGroup rootView = (ViewGroup) inflater
                .inflate(R.layout.winfragment, container, false);

        // Set the title view to show the page number.
        //((TextView) rootView.findViewById(android.R.id.text1)).setText(
         //       getString(R.string.title_template_step, mPageNumber + 1));
        TextView tv_qdisc = (TextView) rootView.findViewById(R.id.win_question);
        tv_qdisc.setText(questions.get(mPageNumber).getDescription());
        return rootView;
    }


    public int getPageNumber() {
        return mPageNumber;
    }
}
