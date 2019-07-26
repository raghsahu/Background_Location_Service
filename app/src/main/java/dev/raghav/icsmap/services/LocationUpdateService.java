package dev.raghav.icsmap.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.location.LocationResult;

public final class LocationUpdateService extends IntentService {

    private static final String TAG = "LocationUpdateService";

    public static final String ACTION_HANDLE_LOCATION = "ACTION_HANDLE_LOCATION";

    public LocationUpdateService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(final Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (action != null) {
                switch (action) {
                    case ACTION_HANDLE_LOCATION:
                        onActionHandleLocation(intent);
                        break;

                    default:
                        Log.w(TAG, "onHandleIntent(), unhandled action: " + action);
                        break;
                }
            }
        }
    }

    private void onActionHandleLocation(@NonNull final Intent intent) {
        if (!LocationResult.hasResult(intent)) {
            Log.w(TAG, "No location result in supplied intent");
            return;
        }

        final LocationResult locationResult = LocationResult.extractResult(intent);
        if (locationResult == null) {
            Log.w(TAG, "LocationResult is null in supplied intent");
            return;
        }

        // TODO send to server using a blocking request.
        // Remember that this is the background thread already
    }
}



