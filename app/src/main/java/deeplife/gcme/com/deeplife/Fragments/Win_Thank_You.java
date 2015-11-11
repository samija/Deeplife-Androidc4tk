package deeplife.gcme.com.deeplife.Fragments;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import deeplife.gcme.com.deeplife.Activities.BuildActivity;
import deeplife.gcme.com.deeplife.Activities.MainMenu;
import deeplife.gcme.com.deeplife.Activities.SendActivity;
import deeplife.gcme.com.deeplife.Activities.WinActivity;
import deeplife.gcme.com.deeplife.Database.Database;
import deeplife.gcme.com.deeplife.Database.DeepLife;
import deeplife.gcme.com.deeplife.R;

/**
 * Created by rog on 11/7/2015.
 */

public class Win_Thank_You extends Fragment {

    String stage;
    Button finish;
    Database db;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        db = new Database(getActivity());
        stage = getArguments().getString("stage");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.winbuild_thankyou,container,false);
        finish = (Button) viewGroup.findViewById(R.id.win_btn_finish);

        switch (stage){
            case "WIN":
                handleWin();
                break;
            case "BUILD":
                handleBuild();
                break;
            case "SEND":
                handleSend();
                break;
        }

        return viewGroup;
    }



    public void handleWin(){

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), WinActivity.answers.toString(), Toast.LENGTH_LONG).show();

                if(WinActivity.answers.size()>0) {
                    for (int i = 0; i < WinActivity.answers.size(); i++) {

                        if(i<WinActivity.questions.size()) {
                            Log.i("Deep Life", "i is less than question");
                            Log.i("Deep Life", "Mandatory = " +WinActivity.questions.get(i).getMandatory());

                            if (WinActivity.questions.get(i).getMandatory() == "MANDATORY") {
                                Log.i("Deep Life", "Mandatory = " +WinActivity.questions.get(i).getMandatory());

                                if (WinActivity.answers.get(i) == "No") {
                                    Log.i("Deep Life", "User answer no to a mandatory question");

                                    Toast.makeText(getActivity(), "Thank You!! Please come again and fill mandatory questions!", Toast.LENGTH_LONG).show();
                                    getNextActivity();
                                    Log.i("Deep Life", "closed without saving");

                                    return;
                                }
                            }
                        }
                        ContentValues cv = new ContentValues();
                        cv.put(DeepLife.QUESTION_ANSWER_FIELDS[0], WinActivity.DISCIPLE_ID);
                        cv.put(DeepLife.QUESTION_ANSWER_FIELDS[1], i);
                        cv.put(DeepLife.QUESTION_ANSWER_FIELDS[2], WinActivity.answers.get(i));

                        long check = db.insert(DeepLife.Table_QUESTION_ANSWER, cv);
                        if (check != -1)
                            Log.i("Deep Life", "Question Answer field updated");
                    }

                    ContentValues cv_build = new ContentValues();
                    cv_build.put(DeepLife.DISCIPLES_FIELDS[4], "WIN");
                    long update_state = db.update(DeepLife.Table_DISCIPLES, cv_build, WinActivity.DISCIPLE_ID);
                    if (update_state != -1) {
                        Toast.makeText(getActivity(), "Successfully Finished Win Stage!", Toast.LENGTH_LONG).show();
                        WinActivity.answers.clear();
                        WinActivity.answer_index = 0;
                        WinActivity.answerchoices.clear();
                        WinActivity.questions.clear();

                        getNextActivity();
                    }
                }
                else{
                    Toast.makeText(getActivity(), "Sorry No question available", Toast.LENGTH_LONG).show();
                    getNextActivity();
                }
            }
        });

    }

    public void handleBuild(){

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), BuildActivity.answers.toString(), Toast.LENGTH_LONG).show();


                if(BuildActivity.answers.size()>0) {
                    for (int i = 0; i < BuildActivity.answers.size(); i++) {

                        if(i<BuildActivity.questions.size()) {

                            if (BuildActivity.questions.get(i).getMandatory().endsWith("MANDATORY")) {

                                if (BuildActivity.answers.get(i) == "No") {

                                    Toast.makeText(getActivity(), "Thank You!! Please come again and fill mandatory questions!", Toast.LENGTH_LONG).show();
                                    getNextActivity();
                                    Log.i("Deep Life", "closed without saving");
                                    return;
                                }
                            }
                        }


                        ContentValues cv = new ContentValues();
                        cv.put(DeepLife.QUESTION_ANSWER_FIELDS[0], BuildActivity.DISCIPLE_ID);
                        cv.put(DeepLife.QUESTION_ANSWER_FIELDS[1], i);
                        cv.put(DeepLife.QUESTION_ANSWER_FIELDS[2], BuildActivity.answers.get(i));

                        long check = db.insert(DeepLife.Table_QUESTION_ANSWER, cv);
                        if (check != -1)
                            Log.i("Deep Life", "Question Answer field updated");
                    }

                    ContentValues cv_build = new ContentValues();
                    cv_build.put(DeepLife.DISCIPLES_FIELDS[4], "BUILD");
                    long update_state = db.update(DeepLife.Table_DISCIPLES, cv_build, BuildActivity.DISCIPLE_ID);
                    if (update_state != -1) {
                        Toast.makeText(getActivity(), "Successfully Finished Win Stage!", Toast.LENGTH_LONG).show();
                        BuildActivity.answers.clear();
                        BuildActivity.answer_index = 0;
                        BuildActivity.answerchoices.clear();
                        BuildActivity.questions.clear();

                        getNextActivity();
                    }
                }
                else{
                    Toast.makeText(getActivity(), "Sorry No question available", Toast.LENGTH_LONG).show();
                    getNextActivity();
                }
            }
        });
    }

    public void handleSend(){

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), SendActivity.answers.toString(), Toast.LENGTH_LONG).show();


                if(SendActivity.answers.size()>0) {
                    for (int i = 0; i < SendActivity.answers.size(); i++) {

                        ContentValues cv = new ContentValues();
                        cv.put(DeepLife.QUESTION_ANSWER_FIELDS[0], SendActivity.DISCIPLE_ID);
                        cv.put(DeepLife.QUESTION_ANSWER_FIELDS[1], i);
                        cv.put(DeepLife.QUESTION_ANSWER_FIELDS[2], SendActivity.answers.get(i));

                        long check = db.insert(DeepLife.Table_QUESTION_ANSWER, cv);
                        if (check != -1)
                            Log.i("Deep Life", "Question Answer field updated");
                    }

                    ContentValues cv_build = new ContentValues();
                    cv_build.put(DeepLife.DISCIPLES_FIELDS[4], "SEND");
                    long update_state = db.update(DeepLife.Table_DISCIPLES, cv_build, SendActivity.DISCIPLE_ID);
                    if (update_state != -1) {
                        Toast.makeText(getActivity(), "Successfully Finished Win Stage!", Toast.LENGTH_LONG).show();
                        SendActivity.answers.clear();
                        SendActivity.answer_index = 0;
                        SendActivity.answerchoices.clear();
                        SendActivity.questions.clear();

                        getNextActivity();
                    }
                }
                else{
                    Toast.makeText(getActivity(), "Sorry No question available", Toast.LENGTH_LONG).show();

                    getNextActivity();
                }
            }
        });
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
