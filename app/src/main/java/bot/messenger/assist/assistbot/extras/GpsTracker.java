package bot.messenger.assist.assistbot.extras;

import android.Manifest;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import bot.messenger.assist.assistbot.ChatMessage;
import bot.messenger.assist.assistbot.ChatMessenger;
import bot.messenger.assist.assistbot.extras.Interface.LocationCallback;


public class GpsTracker extends Service implements LocationListener {


    public LocationManager locationManager;
    public Boolean gpsFlag=false;
    public Boolean networkFlag=false;
    public Boolean locationFlag=false;
    private static final long MINIMUM_TIME=100*60*2;
    private static final long MINIMUM_DISTANCE=1000;
    private ChatMessenger activity=null;
    private Context mContext=null;

    Location location;
    double latitude;
    double longitude;

    public GpsTracker(ChatMessenger context){
        this.activity=context;
        this.mContext=context;
        getLocation();

    }

    public Location getLocation() {
        try {
            locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);

            gpsFlag = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            networkFlag = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!gpsFlag && !networkFlag) {
            } else {
                this.locationFlag = true;

                if (networkFlag) {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MINIMUM_TIME, MINIMUM_DISTANCE, this);


                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        updateGpsLocation();
                    }
                }

                if (gpsFlag) {
                    if (location == null) {
                        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            return null;
                        }

                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MINIMUM_TIME, MINIMUM_DISTANCE, this);
                        if (locationManager != null) {
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            updateGpsLocation();
                        }
                    }
                }
            }

        } catch (Exception e) {

        }
        return location;
    }

    public void updateGpsLocation(){
        if(location!=null){
            latitude=location.getLatitude();
            longitude=location.getLongitude();
        }
    }


    public String getPostalCode(Context context)
    {
        List<Address> addrList = getGeoAddr(context);
        if (addrList != null && addrList.size() > 0)
        {
            Address addr = addrList.get(0);
            String postalCode = addr.getPostalCode();

            return postalCode;
        }
        else
        {
            return null;
        }
    }

    public List<Address> getGeoAddr(Context context)
    {
        if (location != null)
        {
            Geocoder geocoder = new Geocoder(context, Locale.ENGLISH);
            try
            {
                List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
                return addresses;
            }
            catch (IOException e)
            {

            }
        }

        return null;
    }



    public boolean locationFlag(){
        return this.locationFlag;
    }



    @Override
    public void onLocationChanged(Location location) {


        /*if (location != null)
        {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            if(activity!=null)
                ((LocationCallback)activity).locationUpdate(location);
        }*/

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
