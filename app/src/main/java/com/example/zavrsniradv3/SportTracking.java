package com.example.zavrsniradv3;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;
import org.json.JSONArray;
import org.json.JSONObject;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class SportTracking extends AppCompatActivity {

    public String url="",latitude, longitude,krajnje="",tip="",oprema="";
    private static final int REQUEST_LOCATION = 1;
    LocationManager locationManager;
    double udaljenost=0,elevation=0,pocLat=0,pocLong=0,pocElev=0,minuteUk=0,avg;
    int idAkt,brojcek,ucestalost=0;
    Timer timer;
    TimerTask timerTask;
    Double vrijeme = 0.0;
    boolean timerStarted = false,proslo=false,pokrenuto=false;
    Runnable run;
    final Handler h = new Handler();
    public ArrayList<Rute>listRut;
    public ArrayList<Oprema>listaOpreme;
    public ArrayList<String>pop;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sport_tracking);
        Intent i=getIntent();
        url=i.getStringExtra("URL");
        tip=i.getStringExtra("tip");
        if(tip.equals("Šetnja")){
            ucestalost=15;
        }
        if(tip.equals("Trčanje")){
            ucestalost=10;
        }
        if(tip.equals("Biciklizam")){
            ucestalost=5;
        }

        timer=new Timer();
        if(!proslo){
            uPokretu();
        }
        listRut=new ArrayList<>();
        listaOpreme=new ArrayList<>();
        pop=new ArrayList<>();
        StringRequest request = new StringRequest(url+"zav/dohvatiBrAkt.php", response -> {
            try {
                JSONArray array = new JSONArray(response);
                for (int loop = 0; loop < array.length(); loop++) {
                    JSONObject object = array.getJSONObject(loop);
                    idAkt=object.getInt("numOfAct")+1;
                }
            }
            catch (Exception e) {e.printStackTrace();
            }
        }, error -> {
        });
        Volley.newRequestQueue(SportTracking.this).add(request);
    }

    private void OnGPS() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton("Yes", (dialog, which) -> startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))).setNegativeButton("No", (dialog, which) -> dialog.cancel());
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    @SuppressLint("DefaultLocale")
    private void getLocation(Boolean prvi) {
        if (ActivityCompat.checkSelfPermission(
                SportTracking.this,android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                SportTracking.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        } else {
            Location locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (locationGPS != null) {
                double lat = locationGPS.getLatitude();
                double longi = locationGPS.getLongitude();
                double alt=Math.round(locationGPS.getAltitude());
                latitude = String.valueOf(lat);
                longitude = String.valueOf(longi);
                if(!prvi){
                    double r=6371e3;
                    double fi1=lat*Math.PI/180;
                    double fi2=pocLat*Math.PI/180;
                    double deltafi=(pocLat-lat)*Math.PI/180;
                    double deltalambda=(pocLong-longi)*Math.PI/180;

                    double a=Math.sin(deltafi/2)*Math.sin(deltafi/2)+
                            Math.cos(fi1)*Math.cos(fi2)* Math.sin(deltalambda/2)*Math.sin(deltalambda/2);

                    double c=2*Math.atan2(Math.sqrt(a),Math.sqrt(1-a));
                    double d=r*c;
                    udaljenost+=d;
                    TextView udalj= findViewById(R.id.udaljenost);
                    TextView elev=findViewById(R.id.uzvisina);
                    TextView prosj=findViewById(R.id.prosjek);
                    if(pocElev<alt){
                        double rezultat=alt-pocElev;
                        elevation+=rezultat;
                    }
                    udalj.setText(String.format("%.2f", udaljenost/1000f));
                    elev.setText(String.format("%.0f",elevation));

                    avg=(udaljenost)/(minuteUk*60);
                    prosj.setText(String.format("%.2f", avg*3.6f));
                    if(brojcek==ucestalost){
                        listRut.add(new Rute(idAkt,pocLat,pocLong,lat,longi));
                        brojcek=0;
                    }
                    brojcek++;
                }

                pocLat=lat;
                pocLong=longi;
                pocElev=alt;
            }
        }
    }
    public void uPokretu(){
        startTimer();
        if(!pokrenuto){
            pokrenuto=true;
            Button btn=findViewById(R.id.stop);
            btn.setText("ZAUSTAVI");
            udaljenost=0;
            elevation=0;
            ActivityCompat.requestPermissions( this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                OnGPS();
            } else {
                getLocation(true);
                run=new Runnable()
                {
                    private long time = 0;
                    @Override
                    public void run()
                    {
                        if(pokrenuto){
                            getLocation(false);
                            time += 1000;
                            h.postDelayed(this, 1000);
                        }
                    }
                };
                h.postDelayed(run,1000);
            }
        }
        else{
            pokrenuto=false;
        }
    }
    public void stop(View view){
        pokrenuto=!pokrenuto;
        MaterialButton btn=findViewById(R.id.finish);
        if(!pokrenuto){
            btn.setVisibility(View.VISIBLE);
        }
        else{
            btn.setVisibility(View.GONE);
        }
        startStopTapped();
    }
    public void kraj(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(SportTracking.this, R.style.AlertDialogStyle);
        View dizajn = getLayoutInflater().inflate(R.layout.spremi_aktivnost, null);
        builder.setView(dizajn);
        builder.show();
        Intent i=getIntent();
        String locurl=url+"zav/unosAktivnosti.php";
        String type = "act";

        String nmv=elevation+"";
        String idU=i.getStringExtra("id");
        String ime=i.getStringExtra("ime");
        String dat=i.getStringExtra("dat");
        String vrsta="dyn";
        StringRequest request5 = new StringRequest(url+"zav/dohvatiOpremu.php", response5 -> {
            try {
                JSONArray array = new JSONArray(response5);
                for (int loop = 0; loop < array.length(); loop++) {
                    JSONObject object = array.getJSONObject(loop);
                    listaOpreme.add(new Oprema(
                            object.getInt("id"),
                            object.getString("nadimak"),
                            object.getString("marka"),
                            object.getString("model"),
                            object.getString("tip"),
                            object.getInt("idCije")));
                }
                pop.clear();
                pop.add("Oprema:");
                for(Oprema o:listaOpreme){
                    if(o.getIdCije()==Integer.parseInt(idU)){
                        if((o.getTip().equals("Tenisice") && tip.equals("Trčanje")) || (o.getTip().equals("Bicikl") && tip.equals("Biciklizam"))){
                            pop.add(o.getNadimak());
                            //adapter2.notifyDataSetChanged();
                        }
                    }
                }
                Spinner spinner2 = dizajn.findViewById(R.id.spinOprema);
                ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(SportTracking.this,
                        android.R.layout.simple_spinner_item, pop){
                    @Override
                    public boolean isEnabled(int position){
                        return position != 0;
                    }
                };
                adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner2.setAdapter(adapter2);
                spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, View view1, int pos, long id) {
                        oprema=pop.get(pos);
                    }
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
                Button btn= dizajn.findViewById(R.id.button2);
                btn.setOnClickListener(view1 -> {
                    TextView naslov=dizajn.findViewById(R.id.naslov);
                    String nas=naslov.getText().toString();
                    double pro=avg*3.6f;
                    double ud=udaljenost/1000f;
                    BackgroundWorker backgroundWorker = new BackgroundWorker(SportTracking.this,3);
                    BigDecimal o1= new BigDecimal(pro).setScale(2, RoundingMode.HALF_EVEN);
                    BigDecimal o2= new BigDecimal(ud).setScale(2, RoundingMode.HALF_EVEN);
                    backgroundWorker.execute(locurl,type,nas,krajnje,o2+"",nmv,dat,idU,ime,vrsta,o1+"",oprema,tip);
                    for(Rute r:listRut){
                        Log.d("rute: ",r.getIdAkt()+"");
                        String locUrl2=url+"zav/unosKoord.php";
                        String type2 = "rut";
                        BackgroundWorker backgroundWorker2 = new BackgroundWorker(SportTracking.this,9);
                        backgroundWorker2.execute(locUrl2,type2,idAkt+"",r.getStartLat()+"",r.getStartLong()+"",r.getEndLat()+"",r.getEndLong()+"");
                    }
                    finish();
                });
            }
            catch (Exception e) {e.printStackTrace();
            }
        }, error -> {
        });
        Volley.newRequestQueue(this).add(request5);
    }
    public void startStopTapped()
    {
        if(!timerStarted)
        {
            timerStarted = true;
            startTimer();
            run.run();
            Button btn=findViewById(R.id.stop);
            btn.setText("ZAUSTAVI");
        }
        else
        {
            timerStarted = false;
            timerTask.cancel();
            Button btn=findViewById(R.id.stop);
            btn.setText("NASTAVI");
        }
    }
    private void startTimer()
    {
        timerTask = new TimerTask()
        {
            @Override
            public void run()
            {
                runOnUiThread(() -> {
                    vrijeme++;
                    TextView vrem=findViewById(R.id.vrijeme);
                    vrem.setText(getTimerText());
                    krajnje=getTimerText();
                });
            }
        };
        timerStarted=true;
        timer.scheduleAtFixedRate(timerTask, 0 ,1000);
    }
    private String getTimerText()
    {
        int rounded = (int) Math.round(vrijeme);
        double seconds = ((rounded % 86400) % 3600) % 60;
        double minutes = ((rounded % 86400) % 3600) / 60;
        double hours = ((rounded % 86400) / 3600);
        return formatTime(seconds, minutes, hours);
    }

    @SuppressLint("DefaultLocale")
    private String formatTime(double seconds, double minutes, double hours)
    {
        double noOfHours=hours<=0 ? 0 : hours*60;
        //int noOfMinutes = minutes<=0 ? 0 : minutes* 60 * 1000;
        double noOfSeconds = seconds<=0 ? 0 : seconds / 60;
        minuteUk=noOfHours+minutes+noOfSeconds;
        int h=Integer.parseInt(Math.round(hours)+"");
        int m=Integer.parseInt(Math.round(minutes)+"");
        int s=Integer.parseInt(Math.round(seconds)+"");
        return String.format("%02d",h) + ":" + String.format("%02d",m) + ":" + String.format("%02d",s);
    }
}