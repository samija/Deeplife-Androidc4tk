package deeplife.gcme.com.deeplife.Activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.ArrayList;

import deeplife.gcme.com.deeplife.Database.Database;
import deeplife.gcme.com.deeplife.Database.DeepLife;
import deeplife.gcme.com.deeplife.Fragments.BuildFragment;
import deeplife.gcme.com.deeplife.Fragments.Win_Thank_You;
import deeplife.gcme.com.deeplife.Models.Question;
import deeplife.gcme.com.deeplife.R;

/**
 * Created by rog on 11/7/2015.
 */
public class BuildActivity extends FragmentActivity {

    public static WinViewPager mPager;
    private PagerAdapter mPagerAdapter;

    public static final String BUILD = "BUILD";
    public int NUM_PAGES;

    public static ArrayList<Question> questions;

    public static ArrayList<String> answers;

    public static ArrayList<String> answerchoices;
    public static int answer_index = 0;

    public static int DISCIPLE_ID;
    Database dbadapter;
    DeepLife dbhelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.winactivity);

        mPager = (WinViewPager) findViewById(R.id.win_viewpager);
        mPager.setSwipeable(true);

        Bundle extras = this.getIntent().getExtras();

        if(extras!=null){
            DISCIPLE_ID = Integer.parseInt(extras.getString("disciple_id").toString());
        }
        else{
            return;
        }

        //initialize data
        init();

        mPager.setAdapter(new ScreenSlidePagerAdapter(getSupportFragmentManager()));

        mPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {


            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbadapter.dispose();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public void init(){
        //initialize database files
        dbadapter = new Database(this);
        dbhelper = new DeepLife();

        //set the max number of pages from db
        NUM_PAGES = (dbadapter.count_Questions(DeepLife.Table_QUESTION_LIST,BUILD));
        NUM_PAGES++;

        Log.i("Deep Life", "The Page number inside win activity is "+NUM_PAGES+"");

        questions = dbadapter.get_All_Questions(BUILD);

        answers = new ArrayList<String>();
        answerchoices = new ArrayList<String>();
        answerchoices.add("Yes");
        answerchoices.add("No");

        for(int i=0; i<NUM_PAGES-1;i++){
            answers.add(" ");
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        /*
        getMenuInflater().inflate(R.menu.activity_screen_slide, menu);

        menu.findItem(R.id.action_previous).setEnabled(mPager.getCurrentItem() > 0);

        // Add either a "next" or "finish" button to the action bar, depending on which page
        // is currently selected.
        MenuItem item = menu.add(Menu.NONE, R.id.action_next, Menu.NONE,
                (mPager.getCurrentItem() == mPagerAdapter.getCount() - 1)
                        ? R.string.action_finish
                        : R.string.action_next);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        return true;
        */

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /*
        switch (item.getItemId()) {
            case android.R.id.home:
                // Navigate "up" the demo structure to the launchpad activity.
                //NavUtils.navigateUpTo(this, new Intent(this, MainActivity.class));
                return true;

            case R.id.action_previous:
                // Go to the previous step in the wizard. If there is no previous step,
                // setCurrentItem will do nothing.
                mPager.setCurrentItem(mPager.getCurrentItem() - 1);
                return true;

            case R.id.action_next:
                // Advance to the next step in the wizard. If there is no next step, setCurrentItem
                // will do nothing.
                mPager.setCurrentItem(mPager.getCurrentItem() + 1);
                return true;
        }
        */
        return super.onOptionsItemSelected(item);
    }



    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {


            if(position==NUM_PAGES-1){
                Bundle b = new Bundle();
                b.putString("stage", "BUILD");
                Fragment win = new Win_Thank_You();
                win.setArguments(b);
                return win;

            }

            return BuildFragment.create(position);

            }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return BUILD;
        }
    }

}
