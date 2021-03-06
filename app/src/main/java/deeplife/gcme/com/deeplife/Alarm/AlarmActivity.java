
package deeplife.gcme.com.deeplife.Alarm;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import deeplife.gcme.com.deeplife.Database.DeepLife;
import deeplife.gcme.com.deeplife.R;

public class AlarmActivity extends Activity {

    AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    private TimePicker alarmTimePicker;
    private static AlarmActivity inst;
    private TextView alarmTextView;
    private Button Cancel,Snooze;

    public static AlarmActivity instance() {
        return inst;
    }

    @Override
    public void onStart() {
        super.onStart();
        inst = this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_activity);
        Cancel = (Button) findViewById(R.id.alarm_stop);
        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeepLife.Cancel_Alarm();
                finish();
            }
        });
        Snooze = (Button) findViewById(R.id.alarm_snooz);
        Snooze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeepLife.Cancel_Alarm();
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.SECOND,10);
                DeepLife.Set_Alarm(cal);
            }
        });
        /*
        setContentView(R.layout.activity_my);
        alarmTimePicker = (TimePicker) findViewById(R.id.alarmTimePicker);
        alarmTextView = (TextView) findViewById(R.id.alarmText);
        ToggleButton alarmToggle = (ToggleButton) findViewById(R.id.alarmToggle);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        */


    }

    public void onToggleClicked(View view) {
        if (((ToggleButton) view).isChecked()) {
            alarmManager.cancel(DeepLife.AlarmPendingIntent);
            setAlarmText("");
            Toast.makeText(getApplicationContext(),"Alarm Discard",Toast.LENGTH_LONG).show();
        }
    }

    public void setAlarmText(String alarmText) {
        alarmTextView.setText(alarmText);
    }
}
