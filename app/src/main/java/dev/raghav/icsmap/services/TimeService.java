package dev.raghav.icsmap.services;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;


import dev.raghav.icsmap.MainActivity;

import static android.content.ContentValues.TAG;


public class TimeService extends BroadcastReceiver {

        public static final int REQUEST_CODE = 12345;
        Context mContext;
        public static final String ACTION = "dev.raghav.icsmap.services;";

        // Triggered by the Alarm periodically (starts the service to run task)
        @Override
        public void onReceive(Context context, Intent intent) {
            //*****************
            Intent intent1 = new Intent(context, LocationMonitoringService.class);

            intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.getApplicationContext().startService(intent1);
           // Toast.makeText(context, "Airplane mod is on", Toast.LENGTH_SHORT).show();


//            if (intent.getAction().matches("android.location.PROVIDERS_CHANGED"))
//            { Toast.makeText(context, "in android.location.PROVIDERS_CHANGED", Toast.LENGTH_SHORT).show();
//            Intent pushIntent = new Intent(context, LocationMonitoringService.class);
//            context.startService(pushIntent); }
            //***********************************

//            final LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
//            if ( !manager.isProviderEnabled(LocationManager.GPS_PROVIDER) ) {
//                Intent i1 = new Intent(context, MainActivity.class);
//                i1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                context.startActivity(i1);
//                   buildAlertMessageNoGps();
//            }else{
//                Intent i = new Intent(context, LocationMonitoringService.class);
//                context.startService(i);
//
//
//            }
        }
    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        Intent pushIntent=    (new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        mContext.startService(pushIntent);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }


}
