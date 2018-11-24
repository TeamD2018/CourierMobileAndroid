package track.club.couriermobile;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import com.google.android.gms.location.*;

public class LocationService {

    private static final String TAG = LocationService.class.getSimpleName();

    private FusedLocationProviderClient mFusedLocationClient;
    private LocationCallback locationCallback;
    private LocationRequest locationRequest;
    private LocationSettingsRequest locationSettingsRequest;

    private Workable<GPSPoint> workable;

    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 1000;
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = 1000;

    public LocationService(Context context) {
        this.locationRequest = LocationRequest.create();
        this.locationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        this.locationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        this.locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(this.locationRequest);
        this.locationSettingsRequest = builder.build();

        this.locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                Location currentLocation = locationResult.getLastLocation();

                GPSPoint gpsPoint = new GPSPoint(currentLocation.getLatitude(), currentLocation.getLongitude());
                Log.i(TAG, "Location Callback results: " + gpsPoint);
                if (null != workable)
                    workable.work(gpsPoint);
            }
        };

        this.mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);

        this.mFusedLocationClient.requestLocationUpdates(this.locationRequest,
                this.locationCallback, Looper.myLooper());
    }

    public void onChange(Workable<GPSPoint> workable) {
        this.workable = workable;
    }

    public LocationSettingsRequest getLocationSettingsRequest() {
        return this.locationSettingsRequest;
    }

    public void stop() {
        Log.i(TAG, "stop() Stopping location tracking");
        this.mFusedLocationClient.removeLocationUpdates(this.locationCallback);
    }
}
