package edu.csus.ecs.androidspyapp;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.telephony.TelephonyManager;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.common.AccountPicker;

import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity
{
    private String TAG = "SPY";
    String[] permissions = {"android.permission.GET_ACCOUNTS"};
    private String wantAccountsPermission = Manifest.permission.GET_ACCOUNTS;

    private String userEmail = "";

    //private TelephonyManager telephonyManager;

    private static final int PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        if (!checkPermission(wantAccountsPermission))
        {
            requestPermission(wantAccountsPermission);
        }
        else
        {
            getEmails();
        }

        //TODO: Initial app data acquisition here..
        //telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        //String phoneNumber = telephonyManager.getLine1Number();

        Log.d(TAG, "testing logging from onCreate()");

        //Check for permission to steal the user's device information.
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED)
//        {
//            //Ask for permission.
//            if (ActivityCompat.shouldShowRequestPermissionRationale(thisActivity, Manifest.permission.READ_PHONE_STATE))
//            {
//                // Show an explanation to the user *asynchronously* -- don't block
//                // this thread waiting for the user's response! After the user
//                // sees the explanation, try again to request the permission.
//            }
//            else
//                {
//                // No explanation needed
//                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, MY_PERMISSIONS_REQUEST_READ_CONTACTS);
//            }
//        } else {
//            // Permission has already been granted
//              getDeviceInfo();
//        }

        Intent googlePicker = AccountPicker
                .newChooseAccountIntent(null, null, new String[]{GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE},
                        true, null, null, null, null);
        startActivityForResult(googlePicker, PERMISSION_REQUEST_CODE);

        //TODO: Spawn background process that records all of your input and stuff.
    }

    private void getEmails()
    {
        Pattern emailPattern = Patterns.EMAIL_ADDRESS;

        // Getting all registered Google Accounts;
        // Account[] accounts = AccountManager.get(this).getAccountsByType("com.google");

        // Getting all registered Accounts;
        Account[] accounts = AccountManager.get(this).getAccounts();

        for (Account account : accounts)
        {
            if (emailPattern.matcher(account.name).matches())
            {
                Log.d(TAG, String.format("%s - %s", account.name, account.type));
                userEmail = account.name;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

//    public void getDeviceInfo()
//    {
//        //TODO:
//        String IMEI = telephonyManager.getDeviceId();
//        String subscriberID = telephonyManager.getDeviceId();
//        String SIM_SN = telephonyManager.getSimSerialNumber();
//        String networkCountryISO = telephonyManager.getNetworkCountryIso();
//        String SIMCountryISO = telephonyManager.getSimCountryIso();
//        String softwareVersion = telephonyManager.getDeviceSoftwareVersion();
//        String voiceMailNumber = telephonyManager.getVoiceMailNumber();
//    }

    private boolean checkPermission(String permission)
    {
        if (Build.VERSION.SDK_INT >= 23) {
            int result = ContextCompat.checkSelfPermission(this, permission);
            if (result == PackageManager.PERMISSION_GRANTED)
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        else
        {
            return true;
        }
    }

    private void requestPermission(String permission)
    {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission))
        {
            Toast.makeText(this, "Get account permission allows us to get your email",
                    Toast.LENGTH_LONG).show();
        }
        ActivityCompat.requestPermissions(this, new String[]{permission}, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
    {
        switch (requestCode)
        {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    getEmails();
                }
                else
                {
                    Toast.makeText(this, "Permission Denied.",
                            Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
}
