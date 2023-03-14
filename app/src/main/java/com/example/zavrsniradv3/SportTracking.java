package com.example.zavrsniradv3;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
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

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;

import org.json.JSONArray;
import org.json.JSONObject;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.xml.datatype.Duration;

public class SportTracking extends AppCompatActivity {

    public String url="";
    private static final int REQUEST_LOCATION = 1;
    Button btnGetLocation;
    TextView showLocation;
    LocationManager locationManager;
    String latitude, longitude;
    private final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;
    MyLocationNewOverlay myLocationOverlay;
    double udaljenost=0;
    double elevation=0;
    double pocLat=0;
    double pocLong=0;
    double pocElev=0;
    String e,pa;

    Timer timer;
    TimerTask timerTask;
    Double vrijeme = 0.0;
    boolean timerStarted = false;

    Boolean pokrenuto=false;
    Runnable run;
    final Handler h = new Handler();
    boolean proslo=false;
    double minuteUk=0;
    String krajnje="";
    int idAkt;
    public ArrayList<Rute>listRut;
    double avg;
    public ArrayList<Oprema>listaOpreme;
    public ArrayList<String>pop;
    String tip="";
    //String selOp="";
    String oprema="";
    int brojcek=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sport_tracking);
        Intent i=getIntent();
        url=i.getStringExtra("URL");
        tip=i.getStringExtra("tip");
        timer=new Timer();
        if(proslo==false){
            uPokretu();
        }
        listRut=new ArrayList<>();
        listaOpreme=new ArrayList<>();
        pop=new ArrayList<>();
        StringRequest request = new StringRequest(url+"zav/dohvatiBrAkt.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray array = new JSONArray(response);
                    for (int loop = 0; loop < array.length(); loop++) {
                        JSONObject object = array.getJSONObject(loop);
                        idAkt=object.getInt("numOfAct")+1;
                    }
                }
                catch (Exception e) {
                    AlertDialog alertDialog = new AlertDialog.Builder(getApplicationContext()).create();
                    alertDialog.setTitle("Greška");
                    alertDialog.setMessage(""+e.getMessage());
                    //alertDialog.show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        Volley.newRequestQueue(SportTracking.this).add(request);
    }

    private void OnGPS() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton("Yes", new  DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
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
                if(prvi==false){
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
                    //udaljenost/=1000f;
                    TextView udalj=(TextView) findViewById(R.id.udaljenost);
                    TextView elev=(TextView) findViewById(R.id.uzvisina);
                    TextView prosj=(TextView) findViewById(R.id.prosjek);
                    if(pocElev<alt){
                        double rezultat=alt-pocElev;
                        elevation+=rezultat;
                    }
                    Log.d("udaljenost prije: ",udaljenost+"");
                    Log.d("udaljenost poslije: ",udaljenost+"");
                    udalj.setText(String.format("%.2f", udaljenost/1000f));
                    elev.setText(String.format("%.0f",elevation));

                    avg=(udaljenost)/(minuteUk*60);
                    //Log.d("avg;",String.format("%.2f", udaljenost)+"km"+String.format("%.5f", avg)+"km/h");
                    prosj.setText(String.format("%.2f", avg*3.6f));
                    //t2.setText("Udaljenost: "+udaljenost+" Elevacija: "+elevation+" m");
                    if(brojcek==5){
                        listRut.add(new Rute(idAkt,pocLat,pocLong,lat,longi));
                        brojcek=0;
                    }
                    brojcek++;
                }

                pocLat=lat;
                pocLong=longi;
                pocElev=alt;
            } else {
                //Toast.makeText(this, "Unable to find location.", Toast.LENGTH_SHORT).show();
            }
        }
    }
    public void uPokretu(){
        startTimer();
        if(pokrenuto==false){
            pokrenuto=true;
            Button btn=(Button)findViewById(R.id.stop);
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
                        if(pokrenuto==true){
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
            Button btn=(Button)findViewById(R.id.start);
            //btn.setText("START");
           // h.removeCallbacksAndMessages(run);
        }

    }
    public void stop(View view){
        /*if(proslo==false){
            startStopTapped();
            proslo=true;
        }
        else{
            startStopTapped();
            uPokretu();

        }*/
        TextView vrem=(TextView)findViewById(R.id.vrijeme);
        Log.d("vrijemeee: ",vrem.getText().toString());
        pokrenuto=!pokrenuto;
        if(pokrenuto==false){
            MaterialButton btn=(MaterialButton)findViewById(R.id.finish);
            btn.setVisibility(View.VISIBLE);
        }
        else{
            MaterialButton btn=(MaterialButton)findViewById(R.id.finish);
            btn.setVisibility(View.GONE);
        }
        startStopTapped();

      //  pokrenuto=false;


    }
    public void kraj(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(SportTracking.this, R.style.AlertDialogStyle);
        View dizajn = getLayoutInflater().inflate(R.layout.spremi_aktivnost, null);
        builder.setView(dizajn);
        builder.show();
        Intent i=getIntent();
        String locurl=url+"zav/unosAktivnosti.php";
        String type = "act";

        //String nas="voznja";
        //String dist=String.format("%.2f", udaljenost/1000f);
        String nmv=elevation+"";

        String idU=i.getStringExtra("id");
        String ime=i.getStringExtra("ime");
        String dat=i.getStringExtra("dat");
        String vrsta="dyn";
        //String oprema="";
        StringRequest request5 = new StringRequest(url+"zav/dohvatiOpremu.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response5) {
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
                        Log.d("idcije: ",idU);
                        Log.d("idcije o ",o.getIdCije()+"");
                        Log.d("tip",tip);
                        //idU=i.getStringExtra("id");
                        if(o.getIdCije()==Integer.parseInt(idU)){
                            if((o.getTip().equals("Tenisice") && tip.equals("Trčanje")) || (o.getTip().equals("Bicikl") && tip.equals("Biciklizam"))){
                                pop.add(o.getNadimak());
                                //adapter2.notifyDataSetChanged();
                            }
                        }
                    }
                    Spinner spinner2 = (Spinner) dizajn.findViewById(R.id.spinOprema);
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
                        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                            oprema=pop.get(pos);

                        }
                        public void onNothingSelected(AdapterView<?> parent) {

                        }

                    });


                    Button btn=(Button) dizajn.findViewById(R.id.button2);
                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            TextView naslov=(TextView)dizajn.findViewById(R.id.naslov);
                            String nas=naslov.getText().toString();
                            //String av=String.format("%.2f", avg*3.6f);
                            double pro=avg*3.6f;
                            double ud=udaljenost/1000f;
                            BackgroundWorker backgroundWorker = new BackgroundWorker(SportTracking.this,3);
                            //oprema="";
                            DecimalFormat form = new DecimalFormat("0.00");
                            //String av=form.format(pro);
                            //String dist=form.format(ud);
                            BigDecimal o1= new BigDecimal(pro).setScale(2, RoundingMode.HALF_EVEN);
                            BigDecimal o2= new BigDecimal(ud).setScale(2, RoundingMode.HALF_EVEN);
                            backgroundWorker.execute(locurl,type,nas,krajnje,o2+"",nmv,dat,idU,ime,vrsta,o1+"",oprema,tip);

                          //  Log.d("akt: ",locurl+" "+type+" "+nas+" "+krajnje+" "+dist+" "+nmv+" "+dat+" "+idU+" "+ime+" "+vrsta+" "+av+" "+oprema);
                            for(Rute r:listRut){
                                Log.d("rute: ",r.getIdAkt()+"");
                                String locUrl2=url+"zav/unosKoord.php";
                                String type2 = "rut";
                                //String idAkt="";
                                //String start="";
                                //String end="";
                                BackgroundWorker backgroundWorker2 = new BackgroundWorker(SportTracking.this,9);
                                backgroundWorker2.execute(locUrl2,type2,idAkt+"",r.getStartLat()+"",r.getStartLong()+"",r.getEndLat()+"",r.getEndLong()+"");
                            }
                            finish();
                        }
                    });
                }
                catch (Exception e) {
                    AlertDialog alertDialog = new AlertDialog.Builder(getApplicationContext()).create();
                    alertDialog.setTitle("Greška");
                    alertDialog.setMessage(""+e.getMessage());
                    //alertDialog.show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        Volley.newRequestQueue(this).add(request5);

    }
    public void startStopTapped()
    {
        if(timerStarted == false)
        {
            timerStarted = true;
            startTimer();
            run.run();
            Button btn=(Button)findViewById(R.id.stop);
            btn.setText("ZAUSTAVI");
        }
        else
        {
            timerStarted = false;
            timerTask.cancel();
            Button btn=(Button)findViewById(R.id.stop);
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
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        vrijeme++;
                        TextView vrem=(TextView)findViewById(R.id.vrijeme);
                        vrem.setText(getTimerText());
                        krajnje=getTimerText();
                        Log.d("krajnje: ",krajnje);
                        Log.d("timer",vrijeme+"");
                    }
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

    private String formatTime(double seconds, double minutes, double hours)
    {
        double noOfHours=hours<=0 ? 0 : hours*60;
        //int noOfMinutes = minutes<=0 ? 0 : minutes* 60 * 1000;
        double noOfSeconds = seconds<=0 ? 0 : seconds / 60;
        minuteUk=noOfHours+minutes+noOfSeconds;
        Log.d("minuteUk",minuteUk+"");
        int h=Integer.parseInt(Math.round(hours)+"");
        int m=Integer.parseInt(Math.round(minutes)+"");
        int s=Integer.parseInt(Math.round(seconds)+"");
        return String.format("%02d",h) + ":" + String.format("%02d",m) + ":" + String.format("%02d",s);
    }
}