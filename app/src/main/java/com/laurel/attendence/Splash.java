package com.laurel.attendence;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class Splash extends Activity {


    boolean gps_enabled = false;
    boolean network_enabled = false;
    UserSessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_splash);
        isOnline();
        TextView textView=(TextView)findViewById(R.id.redies);


/*
            Timer t = new Timer();
            boolean checkConnection=new ApplicationUtility().checkConnection(this);
            if (checkConnection) {
                t.schedule(new splash(), 1500);
            } else {
                Toast.makeText(Splash.this,
                        "connection not found...plz check connection", 3000).show();
            }
        }

        class splash extends TimerTask {

            @Override
            public void run() {
                Intent i = new Intent(Splash.this,LoginAccount.class);
                finish();
                startActivity(i);
            }*/
        LocationManager lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        try {

            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.e("ex", ex.getMessage() + "");
        }

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {

            Log.e("ex", ex.getMessage() + "");
        }

        if (!gps_enabled && !network_enabled) {
            // notify user

            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setMessage(this.getResources().getString(R.string.gps_network_not_enabled));
            dialog.setPositiveButton(this.getResources().getString(R.string.open_location_settings), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub
                    Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);

                    startActivity(myIntent);
                    //get gps
                }
            });
            dialog.setNegativeButton(this.getString(R.string.cancel), new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub

                }
            });
            dialog.show();
        }
    }
    public void isOnline(){
        if(isWorkingInternetPersent()){
            splash();
        }
        else{
            /*Toast.makeText(this,"connection not found...plz check connection ",1500).show();*/
          /*  showAlertDialog(Splash.this, "Internet Connection",
                    "You don't have internet connection", false);
*/

           //Awaiting Internet Connection...
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
//                    final Intent mainIntent = new Intent(Splash.this, Splash.class);
//                    Splash.this.startActivity(mainIntent);
//                    Splash.this.finish();
                    isOnline();
                }
            }, 1500);



        }
    }

    public void splash() {

        Intent intent = new Intent(getApplicationContext(), LoginAccount.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    public boolean isWorkingInternetPersent() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getBaseContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }

        }
        return false;
    }


    public void showAlertDialog(Splash context, String title, String message, Boolean status) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();

        // Setting Dialog Title
        alertDialog.setTitle(title);

        // Setting Dialog Message
        alertDialog.setMessage(message);

        // Setting alert dialog icon
       /*  alertDialog.setIcon((status) ? R.mipmap.ic_launcher : R.mipmap.ic_launcher);

      alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                finish();
                System.exit(0);
            }
        });
*/
        alertDialog.show();
    }
 }
