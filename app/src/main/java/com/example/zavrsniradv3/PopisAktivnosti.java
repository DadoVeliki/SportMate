package com.example.zavrsniradv3;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
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
    public String url="",id="",userAgent = System.getProperty("http.agent");
    double poclat,poclong;
    Road road;
    int[]images;
    public ArrayList<Aktivnost>listAkt;
    public ArrayList<Rute>listRut;
    public ArrayList<Korisnik>listUs;
    public ArrayList<LikeRelation>listLike;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popis_aktivnosti);
        listAkt=new ArrayList<>();
        listLike=new ArrayList<>();
        Intent intent=getIntent();
        images=intent.getIntArrayExtra("images");
        id=intent.getStringExtra("id");
        url=intent.getStringExtra("URL");
        listRut=intent.getParcelableArrayListExtra("listRut");
        listUs=intent.getParcelableArrayListExtra("listUs");
        LinearLayout l=findViewById(R.id.ll1);
        Thread thread = new Thread(() -> {
            findViewById(R.id.backAkt).setOnClickListener(view -> finish());
    //dohvaćanje svih aktivnosti
    @SuppressLint({"InflateParams", "SetTextI18n", "NonConstantResourceId"}) StringRequest request2 = new StringRequest(url+"zav/dohvatiSveAktivnosti.php", response2 -> {
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
                        object.getString("oprema"),
                        object.getString("tipAkt")));
            }
            View akt;
            for(Aktivnost a:listAkt){
                if(a.getIdUsera()==Integer.parseInt(id)){
                if(a.getVrsta().equals("man")){
                    akt=getLayoutInflater().inflate(R.layout.akt_izgled_manual,null);
                }
                else{
                    akt=getLayoutInflater().inflate(R.layout.aktivnost,null);
                    MapView map=akt.findViewById(R.id.mapAkt);
                    Thread thread1 = new Thread(() -> {
                        Configuration.getInstance().load(getApplicationContext(), PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));
                        Configuration.getInstance().setUserAgentValue(userAgent);
                        ArrayList<GeoPoint> waypoints = new ArrayList<>();
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
                                GeoPoint startPoint = new GeoPoint(r.getStartLat(),r.getStartLong());
                                waypoints.add(startPoint);
                                GeoPoint endPoint = new GeoPoint(r.getEndLat(),r.getEndLong());
                                waypoints.add(endPoint);
                            }
                        }
                        for(int i=0;i<waypoints.size();i++){
                            try{
                                List<GeoPoint> routePoints = new ArrayList<>();
                                routePoints.add(waypoints.get(i));
                                routePoints.add(waypoints.get(i+1));
                                Polyline polyline = new Polyline();
                                polyline.setPoints(routePoints);
                                map.getOverlayManager().add(polyline);
                                map.invalidate();
                            }
                            catch(Exception e){}
                        }
                    });
                    thread1.start();
                    GeoPoint start=new GeoPoint(46.4208585,16.53123);
                    IMapController mapController=map.getController();
                    mapController.setZoom(11.0);
                    mapController.setCenter(start);
                }
                TextView ime=akt.findViewById(R.id.name);
                TextView datum=akt.findViewById(R.id.date);
                TextView naslov=akt.findViewById(R.id.title);
                TextView udalj=akt.findViewById(R.id.dist);
                TextView nmv=akt.findViewById(R.id.elev);
                TextView vri=akt.findViewById(R.id.time);
                TextView avg=akt.findViewById(R.id.avg);
                TextView like=akt.findViewById(R.id.like);
                TextView oprema=akt.findViewById(R.id.oprema);
                ImageView tipAkt=akt.findViewById(R.id.tip);
                switch (a.getTipAkt()) {
                        case "Biciklizam":
                            tipAkt.setImageResource(R.drawable.bajk2);
                            break;
                        case "Trčanje":
                            tipAkt.setImageResource(R.drawable.patike);
                            break;
                        case "Šetnja":
                            tipAkt.setImageResource(R.drawable.shoe);
                            break;
                    }
                CircleImageView profile=akt.findViewById(R.id.profile_image);
                profile.setImageResource(images[listUs.get(a.getIdUsera()-1).getSlika()]);

                LocalDateTime d=LocalDateTime.parse(a.getDatum(),DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                final DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("dd", Locale.ENGLISH);
                final DateTimeFormatter dtf3 = DateTimeFormatter.ofPattern("MM", Locale.ENGLISH);
                final DateTimeFormatter dtf4 = DateTimeFormatter.ofPattern("yyyy", Locale.ENGLISH);
                final DateTimeFormatter dtf5 = DateTimeFormatter.ofPattern("HH:mm", Locale.ENGLISH);

                ime.setText(a.getImePrezime());
                datum.setText("dana "+MojeMetode.makniNule(dtf2.format(d))+". "+MojeMetode.kojiMjesec(dtf3,d)+" "+dtf4.format(d)+". u "+dtf5.format(d));
                naslov.setText(a.getNaslov().toUpperCase());
                udalj.setText(a.getUdaljenost()+" km");
                nmv.setText(a.getNmv()+" m");
                vri.setText(a.getVrijeme());
                avg.setText(a.getAvgBrzina()+" km/h");
                like.setText(a.getBrojLajkova()+" oznaka sviđa mi se");
                oprema.setText(a.getOprema());
                BottomNavigationView bnv=akt.findViewById(R.id.bottomAkt);
                bnv.setOnItemSelectedListener(item ->{
                    switch (item.getItemId()){
                        case R.id.lajk:
                            if(a.getIdUsera()!=Integer.parseInt(id)){
                                StringRequest request21 = new StringRequest(url+"zav/dohvatiLajk.php", response21 -> {
                                    try {
                                        JSONArray array1 = new JSONArray(response21);
                                        for (int loop = 0; loop < array1.length(); loop++) {
                                            JSONObject object = array1.getJSONObject(loop);
                                            listLike.add(new LikeRelation(
                                                    object.getInt("id"),
                                                    object.getInt("idAkt"),
                                                    object.getInt("idOb"),
                                                    object.getInt("idUsera")));
                                        }
                                        boolean b=false;
                                        for(LikeRelation l1 :listLike){
                                            if((l1.getIdAkt()==a.getId()) && (l1.getIdUsera()==Integer.parseInt(id))){
                                                b=true;
                                            }
                                        }
                                        if(!b){
                                            String locUrl=url+"zav/lajkPost.php";
                                            String type = "lajk";
                                            int br=a.getBrojLajkova()+1;
                                            like.setText(br+" oznaka sviđa mi se");
                                            BackgroundWorker backgroundWorker = new BackgroundWorker(getApplicationContext(),8);
                                            backgroundWorker.execute(locUrl,type,""+a.getId(),""+br,"act",""+id);
                                            item.setEnabled(false);
                                        }

                                    }
                                    catch (Exception e) {e.printStackTrace();
                                    }
                                }, error2 -> {
                                });
                                Volley.newRequestQueue(getApplicationContext()).add(request21);
                            }
                            else{
                                item.setEnabled(false);
                            }
                            break;
                        case R.id.kom:
                            Intent intent1 =new Intent(getApplicationContext(),Komentari.class);
                            intent1.putExtra("idU",id+"");
                            intent1.putExtra("idOb","0");
                            intent1.putExtra("idAkt",a.getId()+"");
                            intent1.putExtra("URL",url);
                            intent1.putExtra("lista",listUs);
                            intent1.putExtra("images",images);
                            startActivity(intent1);
                            break;
                    }
                    return true;
                });
                LinearLayout p=akt.findViewById(R.id.parent);
                l.addView(p);}
            }
            //}
        }
        catch (Exception e) {e.printStackTrace();
        }
    }, error2 -> {
    });
    Volley.newRequestQueue(PopisAktivnosti.this).add(request2);
        });
        thread.start();
    }
}