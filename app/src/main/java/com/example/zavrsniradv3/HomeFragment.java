package com.example.zavrsniradv3;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceManager;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.style.TtsSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

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
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public HomeFragment() {
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    Boolean isFABOpen=false;
    FloatingActionButton fab1,fab2;
    String userAgent = System.getProperty("http.agent"),url="",id="";
    Road road;
    public ArrayList<LikeRelation>listLike;
    int[]images;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        url=this.getArguments().getString("URL");
        id=this.getArguments().getString("id");
        images=this.getArguments().getIntArray("images");
        ArrayList<Aktivnost> listAkt=((HOME)getActivity()).getAkt();
        ArrayList<ObjavaC>listOb=((HOME)getActivity()).getOb();
        ArrayList<Korisnik>listUs=((HOME)getActivity()).getUs();
        ArrayList<Odnos>listOd=((HOME)getActivity()).getOd();
        ArrayList<Oprema>listOp=((HOME)getActivity()).getOp();
        ArrayList<Rute>listRut=((HOME)getActivity()).getRut();
        View home=inflater.inflate(R.layout.fragment_home, container, false);
        LinearLayout l=(LinearLayout) home.findViewById(R.id.ll1);
        listLike=new ArrayList<>();
        View akt;
        ArrayList<Korisnik>kojePratim=new ArrayList<>();
        for(Korisnik k:listUs){
            for(Odnos o:listOd){
                if((o.getIdKojiPrati()==Integer.parseInt(id)) && (o.getIdPracen()==k.getId())){
                    kojePratim.add(k);
                }
            }
        }
        for(Aktivnost a:listAkt){
            for(Korisnik k:kojePratim){
                if((k.getId()==a.getIdUsera()) || (a.getIdUsera()==Integer.parseInt(id))){


            if(a.getVrsta().equals("man")){
                akt=getLayoutInflater().inflate(R.layout.akt_izgled_manual,null);
            }
            else{
                akt=getLayoutInflater().inflate(R.layout.aktivnost,null);
                MapView map=(MapView)akt.findViewById(R.id.mapAkt);
                Thread thread = new Thread(new Runnable(){
                    @Override
                    public void run(){
                        Configuration.getInstance().load(getActivity().getApplicationContext(), PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext()));
                        Configuration.getInstance().setUserAgentValue(userAgent);

                        map.setTileSource(TileSourceFactory.MAPNIK);
                        map.setMultiTouchControls(true);
                        //map.setBuiltInZoomControls(false);
                        /*for(Rute r:listRut){
                            if (r.getIdAkt() == a.getId()) {
                                poclat=r.getStartLat();
                                poclong=r.getStartLong();
                                break;
                            }
                        }*/
                        for(Rute r:listRut) {
                            if (r.getIdAkt() == a.getId()) {

                new Thread(new Runnable()
                {
                    public void run()
                    {
                                RoadManager roadManager = new OSRMRoadManager(getContext(),userAgent);

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
                                if(getActivity()==null){
                                    return;
                                }
                                getActivity().runOnUiThread(new Runnable()
                                {
                                    public void run()
                                    {
                                        if (road.mStatus != Road.STATUS_OK)
                                        {

                                        }

                                        Polyline roadOverlay = RoadManager.buildRoadOverlay(road);
                                        map.getOverlays().add(roadOverlay);
                                        map.invalidate();
                                    }
                                });
                                  }
                                 }
                                 ).start();
                                try{
                                    RoadManager roadManager = new OSRMRoadManager(getContext(),userAgent);

                                    ArrayList<GeoPoint> waypoints = new ArrayList<GeoPoint>();
                                    GeoPoint startPoint = new GeoPoint(r.getStartLat(),r.getStartLong());
                                    waypoints.add(startPoint);
                                    GeoPoint endPoint = new GeoPoint(r.getEndLat(),r.getEndLong());
                                    waypoints.add(endPoint);
                                    road = roadManager.getRoad(waypoints);

                                    Polyline roadOverlay = RoadManager.buildRoadOverlay(road);
                                    map.getOverlays().add(roadOverlay);
                                    map.invalidate();
                                }
                                catch (Exception e)
                                {
                                    e.printStackTrace();
                                }
                            }
                        }



                    }
                });

                thread.start();
                GeoPoint start=new GeoPoint(46.4208585,16.53123);
                IMapController mapController=map.getController();
                mapController.setZoom(11.0);
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
            ImageView tipAkt=(ImageView)akt.findViewById(R.id.tip);
            TextView oprema=(TextView)akt.findViewById(R.id.oprema);
            if(a.getTipAkt().equals("Biciklizam")){
                tipAkt.setImageResource(R.drawable.bajk2);
            }
            else if(a.getTipAkt().equals("Trčanje")){
                tipAkt.setImageResource(R.drawable.patike);
            }
            else if(a.getTipAkt().equals("Šetnja")){
                tipAkt.setImageResource(R.drawable.shoe);
            }


            CircleImageView profile=(CircleImageView)akt.findViewById(R.id.profile_image);
            profile.setImageResource(images[listUs.get(a.getIdUsera()-1).getSlika()]);


            LocalDateTime d=LocalDateTime.parse(a.getDatum(),DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

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
            oprema.setText(a.getOprema());
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
                                            BackgroundWorker backgroundWorker = new BackgroundWorker(getActivity().getApplicationContext(),8);
                                            backgroundWorker.execute(locUrl,type,""+a.getId(),""+br,"act",""+id);
                                            item.setEnabled(false);
                                        }

                                    }
                                    catch (Exception e) {
                                    }
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error2) {
                                }
                            });
                            Volley.newRequestQueue(getActivity().getApplicationContext()).add(request2);

                        }
                        else{
                            item.setEnabled(false);
                        }
                        break;
                    case R.id.kom:
                        Intent intent=new Intent(getContext(),Komentari.class);
                        intent.putExtra("idU",id+"");
                        intent.putExtra("idOb","0");
                        intent.putExtra("idAkt",a.getId()+"");
                        intent.putExtra("URL",url);
                        intent.putExtra("lista",listUs);
                        intent.putExtra("images",images);
                        startActivity(intent);
                        break;
                }
                return true;
            });
            LinearLayout p=(LinearLayout)akt.findViewById(R.id.parent);
            l.addView(p);
            break;
                }
            }
        }


        for(ObjavaC o:listOb){
            for(Korisnik k:kojePratim){
                if((k.getId()==o.getIdUsera()) || (o.getIdUsera()==Integer.parseInt(id))){
            View ob=getLayoutInflater().inflate(R.layout.preview_objava,null);
            TextView ime=(TextView)ob.findViewById(R.id.name);
            TextView datum=(TextView)ob.findViewById(R.id.date);
            TextView naslov=(TextView)ob.findViewById(R.id.title);
            TextView tekst=(TextView)ob.findViewById(R.id.tekst1);
            TextView pov=(TextView)ob.findViewById(R.id.link);
            TextView like=(TextView)ob.findViewById(R.id.like);
            CircleImageView profile=(CircleImageView)ob.findViewById(R.id.profile_image);
            LocalDateTime d=LocalDateTime.parse(o.getDatum(),DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy.", Locale.ENGLISH);
            final DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("dd", Locale.ENGLISH);
            final DateTimeFormatter dtf3 = DateTimeFormatter.ofPattern("MM", Locale.ENGLISH);
            final DateTimeFormatter dtf4 = DateTimeFormatter.ofPattern("yyyy", Locale.ENGLISH);
            final DateTimeFormatter dtf5 = DateTimeFormatter.ofPattern("HH:mm", Locale.ENGLISH);
            profile.setImageResource(images[listUs.get(o.getIdUsera()-1).getSlika()]);
            ime.setText(o.getImePrezime());
            datum.setText("dana "+makniNule(dtf2.format(d))+". "+kojiMjesec(dtf3,d)+" "+dtf4.format(d)+". u "+dtf5.format(d));
            naslov.setText(o.getNaslov().toUpperCase());
            tekst.setText(""+o.getTekst());
            String a="<a href='https://"+o.getLink()+"'>"+o.getLink()+"</a>";
            pov.setClickable(true);
            pov.setMovementMethod(LinkMovementMethod.getInstance());
            pov.setText(Html.fromHtml(a,Html.FROM_HTML_MODE_COMPACT));
            like.setText(o.getBrojLajkova()+" oznaka sviđa mi se");
            BottomNavigationView bnv=(BottomNavigationView)ob.findViewById(R.id.bottomAkt);
            bnv.setOnItemSelectedListener(item ->{
                switch (item.getItemId()){
                    case R.id.lajk:
                        if(o.getIdUsera()!=Integer.parseInt(id)){
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
                                            if((l.getIdAkt()==o.getId()) && (l.getIdUsera()==Integer.parseInt(id))){
                                                b=true;
                                            }
                                        }
                                        if(b==false){
                                            String locUrl=url+"zav/lajkPost.php";
                                            String type = "lajk";
                                            int br=o.getBrojLajkova()+1;
                                            like.setText(br+" oznaka sviđa mi se");
                                            BackgroundWorker backgroundWorker = new BackgroundWorker(getActivity().getApplicationContext(),8);
                                            backgroundWorker.execute(locUrl,type,""+o.getId(),""+br,"obj",""+id);
                                            item.setEnabled(false);
                                        }

                                    }
                                    catch (Exception e) {
                                    }
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error2) {
                                }
                            });
                            Volley.newRequestQueue(getActivity().getApplicationContext()).add(request2);

                        }
                        else{
                            item.setEnabled(false);
                        }

                        break;
                    case R.id.kom:
                        Intent intent=new Intent(getContext(),Komentari.class);
                        intent.putExtra("idU",id+"");
                        intent.putExtra("idOb",o.getId()+"");
                        intent.putExtra("idAkt","0");
                        intent.putExtra("URL",url);
                        intent.putExtra("lista",listUs);
                        intent.putExtra("images",images);
                        startActivity(intent);
                        break;
                }
                return true;
            });

            LinearLayout p=(LinearLayout)ob.findViewById(R.id.parent);
            l.addView(p);
            break;
                }
            }
        }

        String id=this.getArguments().getString("id");
        String ime=this.getArguments().getString("ime");
        FloatingActionButton fab=(FloatingActionButton) home.findViewById(R.id.floatingActionButton);
        fab2=(FloatingActionButton) home.findViewById(R.id.floatingActionButton2);//objava
        fab1=(FloatingActionButton) home.findViewById(R.id.floatingActionButton3);//aktivnost

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isFABOpen){
                    showFABMenu();
                }else{
                    closeFABMenu();
                }
            }
        });
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(),ManualActivity.class);
                intent.putExtra("id",id+"");
                intent.putExtra("ime",ime);
                intent.putExtra("URL",url);
                intent.putExtra("listOp",listOp);
                startActivity(intent);
            }
        });
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(),DodajObjavu.class);
                intent.putExtra("id",id);
                intent.putExtra("ime",ime);
                intent.putExtra("URL",url);
                startActivity(intent);
            }
        });

        ImageView img2=(ImageView) home.findViewById(R.id.ljudi);
        int br2=this.getArguments().getInt("brojPratim");
        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(),PopisKorisnika.class);
                intent.putParcelableArrayListExtra("lista",listUs);
                intent.putParcelableArrayListExtra("listaOdnosa",listOd);
                intent.putExtra("URL",url);
                intent.putExtra("id",id+"");
                intent.putExtra("br2",br2);
                intent.putExtra("images",images);
                startActivity(intent);
            }
        });

        return home;
    }
    private void showFABMenu(){
        isFABOpen=true;
        fab1.animate().translationY(-getResources().getDimension(R.dimen.standard_55));
        fab2.animate().translationY(-getResources().getDimension(R.dimen.standard_105));
    }
    private void closeFABMenu(){
        isFABOpen=false;
        fab1.animate().translationY(0);
        fab2.animate().translationY(0);
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