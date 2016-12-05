package com.laurel.attendence;

import android.*;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Attendence extends Activity {
    double lat;
    double lng;
    Button scan, in, out;
    String empId;
    int q = 0;
    static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendence);
        Firebase.setAndroidContext(getApplicationContext());
        scan = (Button) findViewById(R.id.scan);
        in = (Button) findViewById(R.id.emp_in);
        out = (Button) findViewById(R.id.emp_out);
        empId = LoginAccount.empId;
        setGPSCoordinates();
        dateAndTime();
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                    , android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                    , android.Manifest.permission.READ_EXTERNAL_STORAGE
                    , android.Manifest.permission.CAMERA
                    , android.Manifest.permission.RECORD_AUDIO
                    , android.Manifest.permission.ACCESS_FINE_LOCATION
            }, q);
        }
    }

    public void scanQR(View v) {
        try {
            Intent intent = new Intent(ACTION_SCAN);
            intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
            startActivityForResult(intent, 0);

        } catch (ActivityNotFoundException anfe) {
            Log.e("scane", anfe.getMessage());
            // showDialog(Attendence.this, "No Scanner Found", "Download a scanner code activity?", "Yes", "No").show();
        }
    }

    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 200;

    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                // successfully captured the image
                // launching upload activity
                // launchUploadActivity(true);


            } else if (resultCode == RESULT_CANCELED) {

                // user cancelled Image capture
                Toast.makeText(getApplicationContext(),
                        "User cancelled image capture", Toast.LENGTH_SHORT)
                        .show();

            } else {
                // failed to capture image
                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                        .show();
            }

        } else if (requestCode == CAMERA_CAPTURE_VIDEO_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                // video successfully recorded
                // launching upload activity
                //  launchUploadActivity(false);

            } else if (resultCode == RESULT_CANCELED) {

                // user cancelled recording
                Toast.makeText(getApplicationContext(),
                        "User cancelled video recording", Toast.LENGTH_SHORT)
                        .show();

            } else {
                // failed to record video
                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to record video", Toast.LENGTH_SHORT)
                        .show();
            }
        }

        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {

                String contents = intent.getStringExtra("SCAN_RESULT");
                Log.e("contents", contents);
                if (contents.equals("Laurel Solutions")) {
                    scan.setVisibility(View.GONE);
                    out.setVisibility(View.VISIBLE);
                    in.setVisibility(View.VISIBLE);
                }

            }
        }
    }

    private void setGPSCoordinates() {

        try {
            LocationManager locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            Criteria criteria = new Criteria();
            criteria.setPowerRequirement(Criteria.POWER_LOW);
            String provider = locManager.getBestProvider(criteria, true);

            locManager.requestLocationUpdates(provider, 0, 0, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    lat = location.getLatitude();
                    lng = location.getLongitude();

                    Log.d("latlon", "" + lat + "," + lng);

                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {
                }

                @Override
                public void onProviderEnabled(String provider) {
                }

                @Override
                public void onProviderDisabled(String provider) {
                }
            });

            Location location = locManager.getLastKnownLocation(provider);

            lat = location.getLatitude();
            lng = location.getLongitude();

            Log.d("newLoc", lat + "," + lng);
        } catch (SecurityException e) {
            Log.e("seexp", e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            //Log.e("seexp", e.getMessage());
        }
    }

    String year, mon, dat, time;

    private void dateAndTime() {
        Date today = new Date();
        DateFormat df7 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = df7.format(today);
        Log.e("date", date);
        year = date.substring(0, 4);
        Log.e("date", year);
        mon = date.substring(5, 7);
        dat = date.substring(8, 10);
        Log.e("date", dat + "hai" + mon);
        time = date.substring(11, 19);
        Log.e("date", time);
        Pojo pojo = new Pojo();
        pojo.setLatitude(lat);
        pojo.setLongitude(lng);
        pojo.setTime(time);

    }

    final Pojo pojo = new Pojo();

    public void in(View v) {

        try {

            Log.e("in", "in");
            Firebase ref = new Firebase(Config.FIREBASE_URL);
            Log.e("in", "222");
            //  ref.child("sowmya").child(year).child(mon).child(dat).child("In").setValue(pojo);

            ref.child("hai").child("bye").setValue(pojo);
            Log.e("in", "226");
            ref.addValueEventListener(new ValueEventListener() {

                @Override

                public void onDataChange(DataSnapshot snapshot) {
                    for (DataSnapshot postSnapshot : snapshot.getChildren()) {

                        //Getting the data from snapshot

                        Log.e("gpsget", Pojo.class + "");

                        //GpsSettr gpsSettr1 = postSnapshot.getValue(GpsSettr.class);
                        //Adding it to a string
                        // Log.e("gps","GpsLot  "+gpsSettr1.getLat_array());
                    }
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                    System.out.println("The read failed: " + firebaseError.getMessage());
                }
            });


        } catch (Exception e) {
            Log.e("fire", e.getMessage());
        }
    }

    public void out(View v) {
        try {

            Log.e("out", "out");
            Log.e("time", year + mon + dat + time + "");
            Firebase ref = new Firebase(Config.FIREBASE_URL);
            ref.child("sowmya").child(year).child(mon).child(dat).child("Out").setValue(pojo);
        } catch (Exception e) {
            Log.e("fire", e.getMessage());
        }
    }
}

class Pojo {

    private Double latitude;
    private Double longitude;
    private String time;

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}