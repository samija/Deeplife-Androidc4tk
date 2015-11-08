package deeplife.gcme.com.deeplife.Fragments;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import deeplife.gcme.com.deeplife.Activities.BuildActivity;
import deeplife.gcme.com.deeplife.Activities.MainMenu;
import deeplife.gcme.com.deeplife.Database.Database;
import deeplife.gcme.com.deeplife.Database.DeepLife;
import deeplife.gcme.com.deeplife.R;

/**
 * Created by rog on 11/7/2015.
 */

public class Build_Thank_You extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.winbuild_thankyou,container,false);
        final Button finish = (Button) viewGroup.findViewById(R.id.win_btn_finish);
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), BuildActivity.answers.toString(), Toast.LENGTH_LONG).show();

                Database db = new Database(getActivity());

                if(BuildActivity.answers.size()>0) {
                    for (int i = 0; i < BuildActivity.answers.size(); i++) {

                        ContentValues cv = new ContentValues();
                        cv.put(DeepLife.QUESTION_ANSWER_FIELDS[0], BuildActivity.DISCIPLE_ID);
                        cv.put(DeepLife.QUESTION_ANSWER_FIELDS[1], i);
                        cv.put(DeepLife.QUESTION_ANSWER_FIELDS[2], BuildActivity.answers.get(i));

                        long check = db.insert(DeepLife.Table_QUESTION_ANSWER, cv);

                    }

                    ContentValues cv_build = new ContentValues();
                    cv_build.put(DeepLife.DISCIPLES_FIELDS[4], "BUILD");
                    long update_state = db.update(DeepLife.Table_DISCIPLES, cv_build, BuildActivity.DISCIPLE_ID);
                    if (update_state != -1) {
                        Toast.makeText(getActivity(), "Successfully Finished Build Stage!", Toast.LENGTH_LONG).show();
                        getNextActivity();
                    }
                }
                else{
                    Toast.makeText(getActivity(), "Sorry No question available", Toast.LENGTH_LONG).show();
                    getNextActivity();
                }
            }
        });
        return viewGroup;
    }

    public void getNextActivity(){
        Intent intent = new Intent(getActivity(),MainMenu.class);
        startActivity(intent);
        getActivity().finish();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}
