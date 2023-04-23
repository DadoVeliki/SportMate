package com.example.zavrsniradv3;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class ActivityRecord extends AppCompatActivity{
    String e,pa,id,ime,url="",tip="";
    public MapView map;
    private final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;
    MyLocationNewOverlay myLocationOverlay;
    public ArrayList<Oprema>listOp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i=getIntent();
        url=i.getStringExtra("URL");
        e=i.getStringExtra("EMAIL");
        pa=i.getStringExtra("PASSWORD");
        id=i.getStringExtra("id");
        ime=i.getStringExtra("ime");
        listOp=i.getParcelableArrayListExtra("listOp");

        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));

        setContentView(R.layout.activity_record);
        map = findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setMultiTouchControls(true);

        GeoPoint start=new GeoPoint(46.4208585,16.53123);
        IMapController mapController=map.getController();
        mapController.setZoom(11.0);
        mapController.setCenter(start);

        requestPermissionsIfNecessary(new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
    });

        myLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(this), map);
        myLocationOverlay.enableMyLocation();
        map.getOverlays().add(myLocationOverlay);
        map.invalidate();
        GeoPoint currentLocation = myLocationOverlay.getMyLocation();
        if (currentLocation != null) {
            Log.d("MainActivity", "Current location: " + currentLocation.getLatitude() + ", " + currentLocation.getLongitude());
        } else {
            Log.d("MainActivity", "Current location not available");
        }

        String[] items = new String[3];
        items[0]="Trčanje";
        items[1]="Šetnja";
        items[2]="Biciklizam";

        Spinner spinner = findViewById(R.id.odabir);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                R.layout.spinner_item1, items);
        adapter.setDropDownViewResource(R.layout.spinner_item1);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                TextView t=findViewById(R.id.toolbar_title);
                t.setText(items[pos]);
                tip=items[pos];
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
    @Override
    public void onResume(){
        super.onResume();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (int i = 0; i < grantResults.length; i++) {
            permissionsToRequest.add(permissions[i]);
        }
        if (permissionsToRequest.size() > 0) {
            ActivityCompat.requestPermissions(
                    this,
                    permissionsToRequest.toArray(new String[0]),
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    private void requestPermissionsIfNecessary(String[] permissions) {
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(permission);
            }
        }
        if (permissionsToRequest.size() > 0) {
            ActivityCompat.requestPermissions(
                    this,
                    permissionsToRequest.toArray(new String[0]),
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    public void klik(View view){
        Intent intent=new Intent(this,SportTracking.class);
        intent.putExtra("id",id);
        intent.putExtra("ime",ime);
        intent.putExtra("URL",url);
        intent.putExtra("listOp",listOp);
        intent.putExtra("tip",tip);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat s=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dat=s.format(Calendar.getInstance().getTime());
        intent.putExtra("dat",dat);
        startActivity(intent);
    }
    public void zatvori(View view){
        Intent intent=new Intent(this,HOME.class);
        intent.putExtra("URL",url);
        intent.putExtra("EMAIL",e);
        intent.putExtra("PASS",pa);
        startActivity(intent);
    }
}