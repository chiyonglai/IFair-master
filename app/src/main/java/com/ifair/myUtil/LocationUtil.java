package com.ifair.myUtil;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by fiona on 2017/4/13.
 */
@SuppressWarnings("MissingPermission")
public class LocationUtil {

    private static final String TAG = "LocationUtil";
    private static  Location location = null;
    public static String getCurrentLocation(Activity mContext) {

        String strGps = "";

        LocationManager mLocationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);

        boolean isGPSEnable = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean isNetworkEnable = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        Log.i(TAG, "getCurrentLocation: " + isGPSEnable + "," + isNetworkEnable);

        if (!(isGPSEnable || isNetworkEnable)) {
            return strGps;
        } else {
//            Criteria criteria = new Criteria();
//            criteria.setAccuracy(Criteria.ACCURACY_COARSE); //最大精度
//            criteria.setPowerRequirement(Criteria.POWER_HIGH);
//            criteria.setAltitudeRequired(false);
//            criteria.setBearingRequired(false);
//            String provider = mLocationManager.getBestProvider(criteria, false);
//            Log.i(TAG, "getCurrentLocation:  " + provider);
//            location = mLocationManager.getLastKnownLocation(provider);
//            Log.i(TAG, "getCurrentLocation: " + location);
//            if (location == null) {
//                Log.i(TAG, "getCurrentLocation: ");
//                mLocationManager.requestLocationUpdates(provider, 1000, 10, new LocationListener() {
//                    @Override
//                    public void onLocationChanged(Location location) {
//                        Log.i(TAG, " : " + location.toString());
//                    }
//
//                    @Override
//                    public void onStatusChanged(String provider, int status, Bundle extras) {
//                        Log.i(TAG, "onStatusChanged: " + provider);
//                    }
//
//                    @Override
//                    public void onProviderEnabled(String provider) {
//                        Log.i(TAG, "onStatusChanged: " + provider);
//                    }
//
//                    @Override
//                    public void onProviderDisabled(String provider) {
//                        Log.i(TAG, "onStatusChanged: " + provider);
//                    }
//                });
//            }
            if (isNetworkEnable) {
                location = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                if (location == null) {
                    mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 500, 10, new LocationListener() {
                        @Override
                        public void onLocationChanged(Location location) {
                            Log.i(TAG, " : " + location.toString());
                            mLocationManager.removeUpdates(this);
                        }

                        @Override
                        public void onStatusChanged(String provider, int status, Bundle extras) {
                            Log.i(TAG, "onStatusChanged: " + provider);
                        }

                        @Override
                        public void onProviderEnabled(String provider) {
                            Log.i(TAG, "onStatusChanged: " + provider);
                        }

                        @Override
                        public void onProviderDisabled(String provider) {
                            Log.i(TAG, "onStatusChanged: " + provider);
                        }
                    });
                }
            } else {
                location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (location == null) {
                    mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 10, new LocationListener() {
                        @Override
                        public void onLocationChanged(Location location) {
                            Log.i(TAG, " : " + location.toString());
                            mLocationManager.removeUpdates(this);
                        }

                        @Override
                        public void onStatusChanged(String provider, int status, Bundle extras) {
                            Log.i(TAG, "onStatusChanged: " + provider);
                        }

                        @Override
                        public void onProviderEnabled(String provider) {
                            Log.i(TAG, "onStatusChanged: " + provider);
                        }

                        @Override
                        public void onProviderDisabled(String provider) {
                            Log.i(TAG, "onStatusChanged: " + provider);
                        }
                    });
                }
            }
        }

        if (location != null) {
            Logger.d(location.getLatitude() + "," + location.getLongitude());
            strGps = location.getLatitude() + "," + location.getLongitude();
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(mContext, Locale.getDefault());

            try {
                addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                strGps=addresses.get(0).getAdminArea();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return strGps;
    }

    public static Location getLocation() {
        return location;
    }


    public static Location getLocationFirstTime(Context mContext) {
        try {
            LocationManager mLocationManager = (LocationManager) mContext.getSystemService(mContext.LOCATION_SERVICE);

            boolean isGPSEnable = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            boolean isNetworkEnable = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);


            if (!(isGPSEnable || isNetworkEnable)) {
                return location;
            } else {
                if (isNetworkEnable)
                    location = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                else
                    location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            }
            return location;
        } catch (SecurityException e) {
            e.printStackTrace();
            return null;
        }

    }
}
