package com.example.zavrsniradv3;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.routing.OSRMRoadManager;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Polyline;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class PopisAktivnosti extends AppCompatActivity {
    public String url="";
    public String id="";
    public ArrayList<Aktivnost>listAkt;
    public ArrayList<Rute>listRut;
    String userAgent = System.getProperty("http.agent");
    double poclat,poclong;
    Road road;
    public ArrayList<Korisnik>listUs;
    public ArrayList<LikeRelation>listLike;
    int[]images;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popis_aktivnosti);
        listAkt=new ArrayList<Aktivnost>();
        ImageView img=(ImageView) findViewById(R.id.backAkt);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        listLike=new ArrayList<>();
        Intent intent=getIntent();
        images=intent.getIntArrayExtra("images");
        id=intent.getStringExtra("id");
        url=intent.getStringExtra("URL");
        listRut=intent.getParcelableArrayListExtra("listRut");
        listUs=intent.getParcelableArrayListExtra("listUs");
        //ArrayList<Aktivnost>listAkt=intent.getParcelableArrayListExtra("listAkt");
        //View home=getLayoutInflater().inflate(R.layout.fragment_home,null);
        LinearLayout l=(LinearLayout) findViewById(R.id.ll1);
        /*for(Aktivnost a:listAkt){
            if(a.getIdUsera()==Integer.parseInt(id)){
                View akt=getLayoutInflater().inflate(R.layout.aktivnost,null);
                TextView ime=(TextView)akt.findViewById(R.id.name);
                TextView datum=(TextView)akt.findViewById(R.id.date);
                TextView naslov=(TextView)akt.findViewById(R.id.title);
                TextView udalj=(TextView)akt.findViewById(R.id.dist);
                TextView nmv=(TextView)akt.findViewById(R.id.elev);
                TextView vri=(TextView)akt.findViewById(R.id.time);
                TextView like=(TextView)akt.findViewById(R.id.like);

                MapView map=(MapView)akt.findViewById(R.id.mapAkt);
                map.setTileSource(TileSourceFactory.MAPNIK);
                GeoPoint start=new GeoPoint(46.5179479,16.3948141);
                IMapController mapController=map.getController();
                mapController.setZoom(18.0);
                mapController.setCenter(start);

                ime.setText(a.getImePrezime());
                datum.setText(a.getDatum());
                naslov.setText(a.getNaslov());
                udalj.setText(""+a.getUdaljenost());
                nmv.setText(""+a.getNmv());
                vri.setText(a.getVrijeme());
                like.setText(a.getBrojLajkova()+" oznaka sviđa mi se");
                LinearLayout p=(LinearLayout)akt.findViewById(R.id.parent);
                l.addView(p);
            }
        }*/
        //dohvaćanje svih aktivnosti
        StringRequest request2 = new StringRequest(url+"zav/dohvatiSveAktivnosti.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response2) {
                try {
                    JSONArray array = new JSONArray(response2);
                    for (int loop = 0; loop < array.length(); loop++) {
                        JSONObject object = array.getJSONObject(loop);
                        listAkt.add(new Aktivnost(
                                object.getInt("id"),
                                object.getInt("idUsera"),
                                object.getString("ime"),
                                object.getString("datum"),
                                object.getString("naslov"),
                                Float.parseFloat(object.getString("udaljenost")),
                                object.getInt("nadmorskaVisina"),
                                object.getString("vrijeme"),
                                object.getInt("brojLajkova"),
                                object.getString("vrsta"),
                                Float.parseFloat(object.getString("avgBrzina")),
                                object.getString("oprema")));
                    }
                    View akt;
                    Log.d("nigga","broj el: "+listAkt.size());
                    for(Aktivnost a:listAkt){
                        if(a.getIdUsera()==Integer.parseInt(id)){
                        Log.d("akt",""+a.getId());
                        if(a.getVrsta().equals("man")){
                            akt=getLayoutInflater().inflate(R.layout.akt_izgled_manual,null);
                        }
                        else{
                            akt=getLayoutInflater().inflate(R.layout.aktivnost,null);
                            MapView map=(MapView)akt.findViewById(R.id.mapAkt);
                            Thread thread = new Thread(new Runnable(){
                                @Override
                                public void run(){
                                    Configuration.getInstance().load(getApplicationContext(), PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));
                                    Configuration.getInstance().setUserAgentValue(userAgent);

                                    map.setTileSource(TileSourceFactory.MAPNIK);
                                    map.setMultiTouchControls(true);
                                    //map.setBuiltInZoomControls(false);
                                    for(Rute r:listRut){
                                        if (r.getIdAkt() == a.getId()) {
                                            poclat=r.getStartLat();
                                            poclong=r.getStartLong();
                                            break;
                                        }
                                    }
                                    for(Rute r:listRut) {
                                        if (r.getIdAkt() == a.getId()) {

                                            /*new Thread(new Runnable()
                                            {
                                                public void run()
                                                {*/
                                                    // Log.d("usera: ",userAgent);

                                                    RoadManager roadManager = new OSRMRoadManager(getApplicationContext(),userAgent);

                                                    ArrayList<GeoPoint> waypoints = new ArrayList<GeoPoint>();
                                                    GeoPoint startPoint = new GeoPoint(r.getStartLat(),r.getStartLong());
                                                    waypoints.add(startPoint);
                                                    GeoPoint endPoint = new GeoPoint(r.getEndLat(),r.getEndLong());
                                                    waypoints.add(endPoint);
                                                    try
                                                    {
                                                        road = roadManager.getRoad(waypoints);
                                                    }
                                                    catch (Exception e)
                                                    {
                                                        e.printStackTrace();
                                                    }
                                                 /* runOnUiThread(new Runnable()
                                                    {
                                                        public void run()
                                                        {*/
                                                            if (road.mStatus != Road.STATUS_OK)
                                                            {

                                                            }

                                                            Polyline roadOverlay = RoadManager.buildRoadOverlay(road);
                                                            map.getOverlays().add(roadOverlay);
                                                       // }
                                                  //  });
                                           //     }
                                         //   }
                                            //).start();
                                        }
                                    }



                                }
                            });
                            thread.start();
                            GeoPoint start=new GeoPoint(poclat,poclong);
                            Log.d("---","poclat: "+poclat+" poclong"+poclong);
                            IMapController mapController=map.getController();
                            mapController.setZoom(15.0);
                            mapController.setCenter(start);
                        }
                        TextView ime=(TextView)akt.findViewById(R.id.name);
                        TextView datum=(TextView)akt.findViewById(R.id.date);
                        TextView naslov=(TextView)akt.findViewById(R.id.title);
                        TextView udalj=(TextView)akt.findViewById(R.id.dist);
                        TextView nmv=(TextView)akt.findViewById(R.id.elev);
                        TextView vri=(TextView)akt.findViewById(R.id.time);
                        TextView avg=(TextView)akt.findViewById(R.id.avg);
                        TextView like=(TextView)akt.findViewById(R.id.like);
                        CircleImageView profile=(CircleImageView)akt.findViewById(R.id.profile_image);
                        profile.setImageResource(images[listUs.get(a.getIdUsera()-1).getSlika()]);


                        LocalDateTime d=LocalDateTime.parse(a.getDatum(),DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                        Log.d("datum",""+d);
                        final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy.", Locale.ENGLISH);
                        final DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("dd", Locale.ENGLISH);
                        final DateTimeFormatter dtf3 = DateTimeFormatter.ofPattern("MM", Locale.ENGLISH);
                        final DateTimeFormatter dtf4 = DateTimeFormatter.ofPattern("yyyy", Locale.ENGLISH);
                        final DateTimeFormatter dtf5 = DateTimeFormatter.ofPattern("HH:mm", Locale.ENGLISH);


                        ime.setText(a.getImePrezime());
                        datum.setText("dana "+makniNule(dtf2.format(d))+". "+kojiMjesec(dtf3,d)+" "+dtf4.format(d)+". u "+dtf5.format(d));
                        naslov.setText(a.getNaslov().toUpperCase());
                        udalj.setText(a.getUdaljenost()+" km");
                        nmv.setText(a.getNmv()+" m");
                        vri.setText(a.getVrijeme());
                        avg.setText(a.getAvgBrzina()+" km/h");
                        like.setText(a.getBrojLajkova()+" oznaka sviđa mi se");
                        BottomNavigationView bnv=(BottomNavigationView)akt.findViewById(R.id.bottomAkt);
                        bnv.setOnItemSelectedListener(item ->{
                            switch (item.getItemId()){
                                case R.id.lajk:
                                    if(a.getIdUsera()!=Integer.parseInt(id)){
                                        StringRequest request2 = new StringRequest(url+"zav/dohvatiLajk.php", new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response2) {
                                                try {
                                                    JSONArray array = new JSONArray(response2);
                                                    for (int loop = 0; loop < array.length(); loop++) {
                                                        JSONObject object = array.getJSONObject(loop);
                                                        listLike.add(new LikeRelation(
                                                                object.getInt("id"),
                                                                object.getInt("idAkt"),
                                                                object.getInt("idOb"),
                                                                object.getInt("idUsera")));
                                                    }
                                                    Boolean b=false;
                                                    for(LikeRelation l:listLike){
                                                        if((l.getIdAkt()==a.getId()) && (l.getIdUsera()==Integer.parseInt(id))){
                                                            b=true;
                                                        }
                                                    }
                                                    if(b==false){
                                                        String locUrl=url+"zav/lajkPost.php";
                                                        String type = "lajk";
                                                        int br=a.getBrojLajkova()+1;
                                                        like.setText(br+" oznaka sviđa mi se");
                                                        BackgroundWorker backgroundWorker = new BackgroundWorker(getApplicationContext(),8);
                                                        backgroundWorker.execute(locUrl,type,""+a.getId(),""+br,"act",""+id);
                                                        item.setEnabled(false);
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
                                            public void onErrorResponse(VolleyError error2) {
                                            }
                                        });
                                        Volley.newRequestQueue(getApplicationContext()).add(request2);

                                    }
                                    else{
                                        item.setEnabled(false);
                                    }
                                    break;
                                case R.id.kom:
                                    Intent intent=new Intent(getApplicationContext(),Komentari.class);
                                    intent.putExtra("idU",id+"");
                                    intent.putExtra("idOb","0");
                                    intent.putExtra("idAkt",a.getId()+"");
                                    Log.d("koji akt: ",a.getId()+"");
                                    intent.putExtra("URL",url);
                                    intent.putExtra("lista",listUs);
                                    startActivity(intent);
                                    break;
                            }
                            return true;
                        });
                        LinearLayout p=(LinearLayout)akt.findViewById(R.id.parent);
                        l.addView(p);}
                    }
                    //}
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
            public void onErrorResponse(VolleyError error2) {
            }
        });
        Volley.newRequestQueue(this).add(request2);
    }
    public String kojiMjesec(DateTimeFormatter f,LocalDateTime d){
        if(f.format(d).equals("01")){
            return "siječnja";
        }
        else if(f.format(d).equals("02")){
            return "veljače";
        }
        else if(f.format(d).equals("03")){
            return "ožujka";
        }
        else if(f.format(d).equals("04")){
            return "travnja";
        }
        else if(f.format(d).equals("05")){
            return "svibnja";
        }
        else if(f.format(d).equals("06")){
            return "lipnja";
        }
        else if(f.format(d).equals("07")){
            return "srpnja";
        }
        else if(f.format(d).equals("08")){
            return "kolovoza";
        }
        else if(f.format(d).equals("09")){
            return "rujna";
        }
        else if(f.format(d).equals("10")){
            return "listopada";
        }
        else if(f.format(d).equals("11")){
            return "studenog";
        }
        else if(f.format(d).equals("12")){
            return "prosinca";
        }
        return "";
    }
    public String makniNule(String num)
    {
        for(int i=0;i<num.length();i++){

            if(num.charAt(i)!='0'){
                String res = num.substring(i);
                return res;
            }
        }
        return "0";
    }
}