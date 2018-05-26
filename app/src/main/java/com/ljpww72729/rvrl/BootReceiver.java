package com.ljpww72729.rvrl;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by LinkedME06 on 20/06/2017.
 */

public class BootReceiver extends BroadcastReceiver {

    static final String action_boot = "android.intent.action.BOOT_COMPLETED";

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.

        //
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            Toast.makeText(context, "开机了", Toast.LENGTH_LONG).show();
            Log.e("boot", "开机了————————");
            Intent intentService = new Intent(context, LaunchService.class);

            context.startService(intentService);
        }
    }
}
