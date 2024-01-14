package com.example.zavrsniradv3;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import org.json.JSONArray;
import org.json.JSONObject;
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
        findViewById(R.id.backOb).setOnClickListener(view -> finish());
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
        LinearLayout l=findViewById(R.id.ll1);
        //dohvaćanje svih objava
        @SuppressLint({"SetTextI18n", "NonConstantResourceId"}) StringRequest request3 = new StringRequest(url+"zav/dohvatiSveObjave.php", response3 -> {
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
                            @SuppressLint("InflateParams") View ob=getLayoutInflater().inflate(R.layout.preview_objava,null);
                            TextView ime=ob.findViewById(R.id.name);
                            TextView datum=ob.findViewById(R.id.date);
                            TextView naslov=ob.findViewById(R.id.title);
                            TextView tekst=ob.findViewById(R.id.tekst1);
                            TextView pov=ob.findViewById(R.id.link);
                            TextView like=ob.findViewById(R.id.like);
                            CircleImageView profile=ob.findViewById(R.id.profile_image);
                            LocalDateTime d=LocalDateTime.parse(o.getDatum(),DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                            final DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("dd", Locale.ENGLISH);
                            final DateTimeFormatter dtf3 = DateTimeFormatter.ofPattern("MM", Locale.ENGLISH);
                            final DateTimeFormatter dtf4 = DateTimeFormatter.ofPattern("yyyy", Locale.ENGLISH);
                            final DateTimeFormatter dtf5 = DateTimeFormatter.ofPattern("HH:mm", Locale.ENGLISH);
                            profile.setImageResource(images[listUs.get(o.getIdUsera()-1).getSlika()]);
                            ime.setText(o.getImePrezime());
                            datum.setText("dana "+MojeMetode.makniNule(dtf2.format(d))+". "+MojeMetode.kojiMjesec(dtf3,d)+" "+dtf4.format(d)+". u "+dtf5.format(d));
                            naslov.setText(o.getNaslov().toUpperCase());
                            tekst.setText(""+o.getTekst());
                            String a="<a href='https://"+o.getLink()+"'>"+o.getLink()+"</a>";
                            pov.setClickable(true);
                            pov.setMovementMethod(LinkMovementMethod.getInstance());
                            pov.setText(Html.fromHtml(a,Html.FROM_HTML_MODE_COMPACT));
                            like.setText(o.getBrojLajkova()+" oznaka sviđa mi se");
                            BottomNavigationView bnv=ob.findViewById(R.id.bottomAkt);
                            bnv.setOnItemSelectedListener(item ->{
                                switch (item.getItemId()){
                                    case R.id.lajk:
                                        if(o.getIdUsera()!=Integer.parseInt(id)){
                                            StringRequest request2 = new StringRequest(url+"zav/dohvatiLajk.php", response2 -> {
                                                try {
                                                    JSONArray array1 = new JSONArray(response2);
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
                                                        if((l1.getIdAkt()==o.getId()) && (l1.getIdUsera()==Integer.parseInt(id))){
                                                            b=true;
                                                        }
                                                    }
                                                    if(!b){
                                                        String locUrl=url+"zav/lajkPost.php";
                                                        String type = "lajk";
                                                        int br=o.getBrojLajkova()+1;
                                                        like.setText(br+" oznaka sviđa mi se");
                                                        BackgroundWorker backgroundWorker = new BackgroundWorker(getApplicationContext(),8);
                                                        backgroundWorker.execute(locUrl,type,""+o.getId(),""+br,"obj",""+id);
                                                        item.setEnabled(false);
                                                    }
                                                }
                                                catch (Exception e) {e.printStackTrace();
                                                }
                                            }, error2 -> {
                                            });
                                            Volley.newRequestQueue(getApplicationContext()).add(request2);
                                        }
                                        else{
                                            item.setEnabled(false);
                                        }
                                        break;
                                    case R.id.kom:
                                        Intent intent1 =new Intent(Objave.this,Komentari.class);
                                        intent1.putExtra("idU",id+"");
                                        intent1.putExtra("idOb",o.getId()+"");
                                        intent1.putExtra("idAkt","0");
                                        intent1.putExtra("URL",url);
                                        intent1.putExtra("lista",listUs);
                                        intent1.putExtra("images",images);
                                        startActivity(intent1);
                                        break;
                                }
                                return true;
                            });
                            LinearLayout p=ob.findViewById(R.id.parent);
                            l.addView(p);
                            break;
                        }
                    }
                }
            }
            catch (Exception e) {e.printStackTrace();
            }
        }, error -> {
        });
        Volley.newRequestQueue(this).add(request3);
    }
}