package a2dv606_aa223de.assignment2.Alarm_Clock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Abeer on 2/28/2017.
 */

public class AlarmReciever extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
      boolean isOn = intent.getExtras().getBoolean("alarm_on");
        Intent intent1 = new Intent(context,RingtonePlayerService.class);
        intent1.putExtra("alarm_on",isOn);
        context.startService(intent1);
    }
}
