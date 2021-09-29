package tw.tcnr16.happyfarming;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.method.ScrollingMovementMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.ajts.androidmads.fontutils.FontUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Q0100 extends AppCompatActivity implements View.OnClickListener {
    private Intent intent = new Intent();
    private ImageView img001,img002,img003,img004,img005;
    public static String lat = "24.1469";
    public static String lon = "120.6839";
    private LocationManager manager;
    private Location currentLocation;
    private static final String[][] permissionsArray = new String[][]{
            {Manifest.permission.ACCESS_FINE_LOCATION, "定位"}};
    private List<String> permissionsList = new ArrayList<String>();
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 101;
    private static final int REQUEST_CODE_ASK_PERMISSIONS = 1;
    private String bestgps;
    int minTime = 5000;
    float minDistance = 5;
    private String TAG = "tcnr16=>";
    private String areaname = "";
    private int maxResult = 1;
    private WebView webView;
    private static final String MAP_URL = "file:///android_asset/hf.html";
    private Uri uri;
    private Intent it;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.q0100);
        checkRequiredPermission(this);
        u_checkgps();
        setupViewComponent();
    }
    private void setupViewComponent() {
        img002 = (ImageView)findViewById(R.id.q0100_img002);
        img003 = (ImageView)findViewById(R.id.q0100_img003);
        img004 = (ImageView)findViewById(R.id.q0100_img004);
        img005 = (ImageView)findViewById(R.id.q0100_img005);
        webView = (WebView) findViewById(R.id.q0100_webview);
        WebSettings settings = webView.getSettings();
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(MAP_URL);
        img002.setOnClickListener(this);
        img003.setOnClickListener(this);
        img004.setOnClickListener(this);
        img005.setOnClickListener(this);
        LinearLayout lay01 = (LinearLayout)findViewById(R.id.q0100_lay01);
        LinearLayout lay03 = (LinearLayout)findViewById(R.id.q0100_lay03);
        LinearLayout lay04 = (LinearLayout)findViewById(R.id.q0100_lay04);
        Intent intent=this.getIntent();
        String mode_title = intent.getStringExtra("class_title");
        this.setTitle(mode_title);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.q0100_img002:
                intent.putExtra("class_title", getString(R.string.q0100_b003));
                intent.setClass(Q0100.this, Q0512.class);
                break;
            case R.id.q0100_img003:
                intent.putExtra("class_title", getString(R.string.q0100_b006));
                intent.setClass(Q0100.this, Q0511.class);
                break;
            case R.id.q0100_img004:
                intent.putExtra("class_title", getString(R.string.q0100_b002));
                intent.setClass(Q0100.this, Q0515.class);
                break;
            case R.id.q0100_img005:
                intent.putExtra("class_title", getString(R.string.q0100_b004));
                intent.setClass(Q0100.this, Q0514.class);
                break;
            case R.id.b001:
                startActivity(it);
                break;
        }
        startActivity(intent);
    }
    private void updatePosition() {
        if (currentLocation == null) {
            lat = "24.1469";
            lon = "120.6839";
        } else {
            lat = Double.toString(currentLocation.getLatitude());
            lon = Double.toString(currentLocation.getLongitude());
        }
    }
    private void u_checkgps() {
        manager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("定位管理")
                    .setMessage("GPS目前狀態是尚未啟用，請問你是否現在就設定啟用GPS?")
                    .setPositiveButton("啟用", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(i);
                        }
                    })
                    .setNegativeButton("不啟用", null).create().show();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }
    private LocationListener listener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            currentLocation = location;
            updatePosition();
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
    };
    private void checkRequiredPermission(final Activity activity) {
        for (int i = 0; i < permissionsArray.length; i++) {
            if (ContextCompat.checkSelfPermission(activity, permissionsArray[i][0]) != PackageManager.PERMISSION_GRANTED) {
                permissionsList.add(permissionsArray[i][0]);
            }
        }
        if (permissionsList.size() != 0) {
            ActivityCompat.requestPermissions(activity, permissionsList.toArray(new
                    String[permissionsList.size()]), REQUEST_CODE_ASK_PERMISSIONS);
        }
    }
    private void requestNeededPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_ASK_PERMISSIONS);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                for (int i = 0; i < permissions.length; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(getApplicationContext(), permissionsArray[i][1] + "權限申請成功!", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "權限被拒絕： " + permissionsArray[i][1], Toast.LENGTH_LONG).show();
                        //------------------
                        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                            Q0100_Util.showDialog(this, R.string.q0100_dialog_msg1, android.R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    requestNeededPermission();
                                }
                            });
                        } else {
                            requestNeededPermission();
                        }
                    }
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        Criteria criteria = new Criteria();
        bestgps = manager.getBestProvider(criteria, true);
        try {
            if (bestgps != null) {
                currentLocation = manager.getLastKnownLocation(bestgps);
                manager.requestLocationUpdates(bestgps, minTime, minDistance, listener);
            } else {
                currentLocation = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                manager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                        minTime, minDistance, listener);
            }
        } catch (SecurityException e) {
            Log.e(TAG, "GPS權限失敗..." + e.getMessage());
        }
        updatePosition();
    }
    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.q0100, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.q0100_m003:
                intent.putExtra("class_title",getString(R.string.q0100_m003));
                intent.setClass( Q0100.this, Q0512.class);
                startActivity(intent);
                break;
            case R.id.q0100_m004:
                intent.putExtra("class_title",getString(R.string.q0100_m004));
                intent.setClass( Q0100.this, Q0514.class);
                startActivity(intent);
                break;
            case R.id.q0100_m005:
                intent.putExtra("class_title",getString(R.string.q0100_m005));
                intent.setClass( Q0100.this, Q0511.class);
                startActivity(intent);
                break;
            case R.id.q0100_m006:
                intent.putExtra("class_title",getString(R.string.q0100_m006));
                intent.setClass( Q0100.this, Q0515.class);
                startActivity(intent);
                break;
            case R.id.q0100_m002:
                new AlertDialog.Builder(Q0100.this)
                        .setTitle(getString(R.string.q0100_menu_about))
                        .setMessage(getString(R.string.q0100_menu_message))
                        .setCancelable(false)
                        .setIcon(R.drawable.hf_s)
                        .setPositiveButton(getString(R.string.q0100_menu_ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .show();
                break;
            case R.id.q0100_action_settings:
                this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}