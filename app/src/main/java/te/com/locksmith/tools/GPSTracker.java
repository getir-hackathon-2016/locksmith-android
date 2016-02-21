package te.com.locksmith.tools;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import io.nlopez.smartlocation.SmartLocation;
import io.nlopez.smartlocation.location.config.LocationParams;
import te.com.locksmith.constants.Globals;
import te.com.locksmith.interfaces.OnGPSConnected;
import te.com.locksmith.interfaces.OnGPSTimeOut;
import te.com.locksmith.interfaces.OnGPSWhenClose;

public class GPSTracker implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    // LogCat tag
    private static final String TAG = GPSTracker.class.getSimpleName();

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;

    // Google client to interact with Google API
    private GoogleApiClient mGoogleApiClient;

    // boolean flag to toggle periodic location updates
    private boolean mRequestingLocationUpdates = false;

    private LocationRequest mLocationRequest;

    // Location updates intervals in sec
    private static int UPDATE_INTERVAL = 10000; // 10 sec
    private static int FATEST_INTERVAL = 2000; // 2 sec
    private static int DISPLACEMENT = 10; // 10 meters
    private static int LOCATION_TIME_OUT = 10; // 10 sec

    private Context context;
    private OnGPSConnected onGPSConnected;
    private OnGPSWhenClose onGPSWhenClose;
    private OnGPSTimeOut onGPSTimeOut;
    private boolean oneFix;
    private SmartLocation smartLocation;
    private LocationParams LocationParamMode = LocationParams.BEST_EFFORT;
    private int GPSIntentRequestCode = 1234;
    private boolean tryAutoGPSSetting = true;
    private boolean LocationChangedRun = false;
    private boolean LocationConnectRun = false;
    private boolean LocationTimeOut = false;

    public GPSTracker(Context context,boolean oneFix, @Nullable LocationParams LocationParamsMode) {
        this.context = context;
        this.oneFix = oneFix;
        this.smartLocation = new SmartLocation.Builder(context).logging(false).build();

        if (LocationParamsMode != null) {
            this.LocationParamMode = LocationParamsMode;
        }

        // First we need to check availability of play services
        if (checkPlayServices()) {
            // Building the GoogleApi client
            buildGoogleApiClient();

            createLocationRequest();
        }
    }

    public void getLocation(OnGPSWhenClose onGPSWhenClose, OnGPSConnected onGPSConnected,OnGPSTimeOut onGPSTimeOut){

        this.onGPSConnected = onGPSConnected;
        this.onGPSWhenClose = onGPSWhenClose;
        this.onGPSTimeOut = onGPSTimeOut;

        if(!mGoogleApiClient.isConnected()) {
            mGoogleApiClient.connect();
        }
    }

    private void returnLocation(Location location){
        if(!LocationTimeOut) {
            if(Globals.FakeLocation){
                location.setLatitude(40.761898);
                location.setLongitude(30.361898);
            }
            this.onGPSConnected.OnGPSConnected(location);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == this.GPSIntentRequestCode && onGPSWhenClose != null && onGPSConnected != null) {
            this.getLocation(onGPSWhenClose, onGPSConnected, onGPSTimeOut);
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        if (checkLocationService()) {
            startLocationUpdates();
            final Location[] loc = {LocationServices.FusedLocationApi
                    .getLastLocation(mGoogleApiClient)};
            final Handler handler = new Handler();
            final int[] count = {0};

            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    loc[0] = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                    count[0]++;
                    if (count[0] < LOCATION_TIME_OUT && loc[0] == null) {
                        handler.postDelayed(this, 1000);
                    } else {
                        if (loc[0] != null) {
                            LocationConnectRun = true;
                            if(oneFix) {
                                stopLocationUpdates();
                                if(!LocationChangedRun) {
                                    returnLocation(loc[0]);
                                }
                            }else{
                                returnLocation(loc[0]);
                            }
                        } else {
                            LocationTimeOut = true;
                            stopLocationUpdates();
                            onGPSTimeOut.OnGPSTimeOut();
                        }
                    }
                }
            };

            if (loc[0] == null) {
                Log.d("---runnable---", "00000");
                handler.postDelayed(runnable, 1000);
            } else {
                runnable.run();
            }

        } else {
            if(mGoogleApiClient.isConnected()){
                mGoogleApiClient.disconnect();
            }
            if (tryAutoGPSSetting && canToggleGPS()) {
                toggleGPS();
            } else {
                onGPSWhenClose.OnGPSWhenClose();
            }
        }



    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        LocationTimeOut = true;
        stopLocationUpdates();
        onGPSTimeOut.OnGPSTimeOut();
    }

    @Override
    public void onLocationChanged(Location location) {
        if(location!= null) {
            LocationChangedRun = true;
            if(oneFix) {
                stopLocationUpdates();
                if(!LocationConnectRun) {
                    returnLocation(location);
                }
            }else {
                returnLocation(location);
            }
        }
    }

    /**
     * Method to verify google play services on the device
     * */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(context);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, (FragmentActivity) context,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(context,
                        "This device is not supported.", Toast.LENGTH_LONG)
                        .show();
            }
            return false;
        }
        return true;
    }

    /**
     * Creating google api client object
     * */
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
    }

    /**
     * Creating location request object
     * */
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FATEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(DISPLACEMENT);
    }

    /**
     * Starting the location updates
     * */
    protected void startLocationUpdates() {
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);

    }

    /**
     * Stopping location updates
     */
    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
    }

    private boolean canToggleGPS() {
        PackageManager pacman = context.getPackageManager();
        PackageInfo pacInfo = null;

        try {
            pacInfo = pacman.getPackageInfo("com.android.settings", PackageManager.GET_RECEIVERS);
        } catch (PackageManager.NameNotFoundException e) {
            return false; //package not found
        }

        if (pacInfo != null) {
            for (ActivityInfo actInfo : pacInfo.receivers) {
                //test if recevier is exported. if so, we can toggle GPS.
                if (actInfo.name.equals("com.android.settings.widget.SettingsAppWidgetProvider") && actInfo.exported) {
                    return true;
                }
            }
        }

        return false; //default
    }

    private void toggleGPS() {
        final Intent poke = new Intent();
        poke.setClassName("com.android.settings",
                "com.android.settings.widget.SettingsAppWidgetProvider");
        poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
        poke.setData(Uri.parse("3"));
        context.sendBroadcast(poke);
    }

    private boolean checkLocationService() {
        boolean checkLocationService = false;

        if (this.LocationParamMode == LocationParams.BEST_EFFORT) {
            checkLocationService = smartLocation.location().state().isAnyProviderAvailable();
        } else if (this.LocationParamMode == LocationParams.NAVIGATION) {
            checkLocationService = smartLocation.location().state().isGpsAvailable();
        } else if (this.LocationParamMode == LocationParams.LAZY) {
            checkLocationService = smartLocation.location().state().isPassiveAvailable();
        }

        return checkLocationService;
    }

    public boolean isTryAutoGPSSetting() {
        return tryAutoGPSSetting;
    }

    public void setTryAutoGPSSetting(boolean tryAutoGPSSetting) {
        this.tryAutoGPSSetting = tryAutoGPSSetting;
    }

    public boolean isOneFix() {
        return oneFix;
    }

    public void setOneFix(boolean oneFix) {
        this.oneFix = oneFix;
    }

    public int getGPSIntentRequestCode() {
        return GPSIntentRequestCode;
    }

}
