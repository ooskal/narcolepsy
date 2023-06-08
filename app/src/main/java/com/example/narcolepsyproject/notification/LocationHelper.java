package com.example.narcolepsyproject.notification;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

public class LocationHelper {

    private Context context;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private static String locationStr;

    public LocationHelper(Context context) {
        this.context = context;
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                // 위치가 변경되었을 때 호출되는 메서드
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                String locationText = "위도: " + latitude + ", 경도: " + longitude;
                setLocationText(locationText);
                Log.d("lllca", locationText);


            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                // 위치 공급자의 상태가 변경되었을 때 호출되는 메서드
            }

            @Override
            public void onProviderEnabled(String provider) {
                // 위치 공급자가 사용 가능한 상태로 변경되었을 때 호출되는 메서드
            }

            @Override
            public void onProviderDisabled(String provider) {
                // 위치 공급자가 사용 불가능한 상태로 변경되었을 때 호출되는 메서드
            }
        };
    }

    public void requestLocationUpdates() {
        try {
            // 위치 업데이트 요청
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    public void removeLocationUpdates() {
        // 위치 업데이트 제거
        locationManager.removeUpdates(locationListener);
    }

    public void setLocationText(String location){
        locationStr = location;
    }

    public static String getLocationText(){
        return locationStr;
    }
}
