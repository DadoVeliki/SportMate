package com.example.zavrsniradv3;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class Objave extends AppCompatActivity {
    public String url="";
    int[]images;
    ArrayList<Korisnik>listUs;
    ArrayList<Odnos>listOd;
    ArrayList<LikeRelation>listLike;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_objave);

        ImageView img=(ImageView) findViewById(R.id.backOb);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        Intent intent=getIntent();
        images=intent.getIntArrayExtra("images");
        listUs=intent.getParcelableArrayListExtra("listUs");
        listOd=intent.getParcelableArrayListExtra("listaOdnosa");
        String id=intent.getStringExtra("id");
        url=intent.getStringExtra("URL");
        ArrayList<ObjavaC>listaObjava=intent.getParcelableArrayListExtra("listOb");
        ArrayList<Korisnik>kojePratim=new ArrayList<>();
        listLike=new ArrayList<>();
        for(Korisnik k:listUs){
            for(Odnos o:listOd){
                if((o.getIdKojiPrati()==Integer.parseInt(id)) && (o.getIdPracen()==k.getId())){
                    kojePratim.add(k);
                }
            }
        }
        LinearLayout l=(LinearLayout) findViewById(R.id.ll1);
        //dohvaćanje svih objava
        StringRequest request3 = new StringRequest(url+"zav/dohvatiSveObjave.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response3) {
                try {
                    JSONArray array = new JSONArray(response3);
                    for (int loop = 0; loop < array.length(); loop++) {
                        JSONObject object = array.getJSONObject(loop);
                        listaObjava.add(new ObjavaC(
                                object.getInt("id"),
                                object.getInt("idUsera"),
                                object.getString("ime"),
                                object.getString("datum"),
                                object.getString("naslov"),
                                object.getString("tekst"),
                                object.getString("link"),
                                object.getInt("brojLajkova")));
                    }
                    for(ObjavaC o:listaObjava){
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
                                                                BackgroundWorker backgroundWorker = new BackgroundWorker(getApplicationContext(),8);
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
                                                Volley.newRequestQueue(getApplicationContext()).add(request2);

                                            }
                                            else{
                                                item.setEnabled(false);
                                            }

                                            break;
                                        case R.id.kom:
                                            Intent intent=new Intent(Objave.this,Komentari.class);
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
                }
                catch (Exception e) {
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        Volley.newRequestQueue(this).add(request3);
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