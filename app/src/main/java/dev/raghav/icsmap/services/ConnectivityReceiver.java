package dev.raghav.icsmap.services;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.SystemClock;
import android.widget.Toast;

public final class ConnectivityReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        final AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        final PendingIntent wakeupIntent = PendingIntent.getService(context, 0,
                new Intent(context, LocationUpdaterService.class), PendingIntent.FLAG_UPDATE_CURRENT);

        final boolean hasNetwork = !intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
        if (hasNetwork) {
            // start service now for doing once
            context.startService(new Intent(context, LocationUpdaterService.class));

            // schedule service for every 15 minutes
            alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    SystemClock.elapsedRealtime() + AlarmManager.INTERVAL_FIFTEEN_MINUTES,
                    AlarmManager.INTERVAL_FIFTEEN_MINUTES, wakeupIntent);
        } else {
            alarmManager.cancel(wakeupIntent);
        }
    }

}

final class LocationUpdaterService extends Service implements LocationListener {

    private enum State {
        IDLE, WORKING;
    }



    private static State state;

    private LocationManager locationManager;
    private PowerManager.WakeLock wakeLock;

    static {
        state = State.IDLE;
    }

    @SuppressLint("InvalidWakeLockTag")
    @Override
    public void onCreate() {
        super.onCreate();
        final PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        this.wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "LocationUpdaterService");
    }


    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (state == State.IDLE) {
            state = State.WORKING;
            this.wakeLock.acquire();
            // register location updates, not gonna code it you know

        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        state = State.IDLE;
        if (this.wakeLock.isHeld()) {
            this.wakeLock.release();
        }
    }

    private void sendToServer(Location location) {
        // send to server in background thread. you might want to start AsyncTask here

        Toast.makeText(getApplicationContext(), "location"+location, Toast.LENGTH_SHORT).show();
    }

    private void onSendingFinished() {
        // call this after sending finished to stop the service
        this.stopSelf(); //stopSelf will call onDestroy and the WakeLock releases.
        //Be sure to call this after everything is done (handle exceptions and other stuff) so you release a wakeLock
        //or you will end up draining battery like hell
    }

    @Override
    public void onLocationChanged(Location location) {
        locationManager.removeUpdates(this); // you will want to listen for updates only once
        sendToServer(location);
        Toast.makeText(getApplicationContext(), "location"+location, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

}