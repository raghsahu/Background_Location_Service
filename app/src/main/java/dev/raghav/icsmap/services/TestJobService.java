package dev.raghav.icsmap.services;

import android.Manifest;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.text.DateFormat;
import java.util.Date;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)

public class TestJobService extends JobService {
    protected GoogleApiClient mGoogleApiClient;
    protected Location mLastLocation;
    protected JobParameters mJobParameters;

    public TestJobService() {
        super();
    }

    private class GetLocation extends AsyncTask<Integer, Void, Integer> implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
        protected Integer doInBackground(Integer... jobID) {
            if (mGoogleApiClient == null) {
                mGoogleApiClient = new GoogleApiClient.Builder(getBaseContext())
                        .addConnectionCallbacks(this)
                        .addOnConnectionFailedListener(this)
                        .addApi(LocationServices.API)
                        .build();
            }

            return jobID[0];
        }


        protected void onPostExecute(Integer jobID) {
            Log.i("JobSchedulerTest", "Job Finished!");
            jobFinished(mJobParameters, true);
        }

        @Override
        public void onConnected(@Nullable Bundle bundle) {
            if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            Log.i("JobSchedulerTest", "on start job: " + mJobParameters.getJobId() + "," +
                    DateFormat.getTimeInstance().format(new Date()) +
                    ",Location(" + mLastLocation.getLatitude() + "," + mLastLocation.getLongitude() + ")");

//           Toast.makeText(getApplicationContext(), "latlng", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onConnectionSuspended(int i) {

        }

        @Override
        public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        }
    }


    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Log.i("JobSchedulerTest", "Job Running!");
        mJobParameters = jobParameters;
        Integer i = new Integer(mJobParameters.getJobId());
        new GetLocation().execute(i);
        return true;

    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        Log.i("JobSchedulerTest", "Job Stopped!");
        return true;
    }
}