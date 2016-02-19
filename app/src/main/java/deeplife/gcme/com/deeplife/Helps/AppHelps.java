package deeplife.gcme.com.deeplife.Helps;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.FragmentActivity;

import java.util.ArrayList;
import java.util.List;

import deeplife.gcme.com.deeplife.R;

/**
 * Created by BENGEOS on 2/19/16.
 */
public class AppHelps extends FragmentActivity {

    public static HelpViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help_activity);

        viewPager = (HelpViewPager) findViewById(R.id.help_viewpage);
        viewPager.setSwipeable(true);
        List<HelpDataType> Helps = new ArrayList<HelpDataType>();
        Helps.add(new HelpDataType(R.drawable.crop_image,"First"));
        Helps.add(new HelpDataType(R.drawable.crop_image,"Second"));
        Helps.add(new HelpDataType(R.drawable.crop_image,"Third"));
        Helps.add(new HelpDataType(R.drawable.crop_image,"Fourth"));

        viewPager.setAdapter(new HelpSlideAdapter(getSupportFragmentManager(),Helps));
    }
}
