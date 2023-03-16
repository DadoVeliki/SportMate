package com.example.zavrsniradv3;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Time;
import java.util.ArrayList;

public class Statistika extends AppCompatActivity {
    public ArrayList<Aktivnost>listAkt;
    public ArrayList<Aktivnost> listMojiAkt;
    public String url="",id="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistika);
        listAkt=new ArrayList<Aktivnost>();
        listMojiAkt=new ArrayList<Aktivnost>();
        Intent intent=getIntent();
        url=intent.getStringExtra("URL");
        id=intent.getStringExtra("id");
        ImageView img=(ImageView) findViewById(R.id.backSt);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
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
                                object.getString("oprema"),
                                object.getString("tipAkt")));
                    }
                    int ukUdSvi=0;
                    float najduzaAkt=0;
                    int najveciElev=0;
                    for(Aktivnost a:listAkt){
                        if(a.getIdUsera()==Integer.parseInt(id)){
                            listMojiAkt.add(a);
                        }
                        ukUdSvi+=a.getUdaljenost();
                        if(a.getUdaljenost()>najduzaAkt){
                            najduzaAkt=a.getUdaljenost();
                        }
                        if(a.getNmv()>najveciElev){
                            najveciElev=a.getNmv();
                        }
                    }
                    int zbrojUdaljJa=0;
                    int zbrojElevJa=0;
                    for(Aktivnost a:listMojiAkt){
                        zbrojUdaljJa+=a.getUdaljenost();
                        zbrojElevJa+=a.getNmv();


                    }
                    TextView brJa=(TextView) findViewById(R.id.brAktJa);
                    TextView udJa=(TextView) findViewById(R.id.udaljJa);
                    TextView elJa=(TextView) findViewById(R.id.elevJa);
                    brJa.setText(""+listMojiAkt.size());
                    udJa.setText(zbrojUdaljJa+" km");
                    elJa.setText(zbrojElevJa+" m");

                    int zbrojAkt=0;
                    int zbrojUdalj=0;
                    int zbrojElev=0;
                    TextView br10=(TextView) findViewById(R.id.brAkt10);
                    TextView ud10=(TextView) findViewById(R.id.udalj10);
                    TextView el10=(TextView) findViewById(R.id.elev10);
                    if(listMojiAkt.size()<10){
                        br10.setTextColor(Color.parseColor("#ff0000"));
                        ud10.setTextColor(Color.parseColor("#ff0000"));
                        el10.setTextColor(Color.parseColor("#ff0000"));
                        br10.setText("NEDOVOLJAN");
                        ud10.setText("BROJ");
                        el10.setText("PODATAKA");
                    }
                    else{
                        for(int i=0;i<10;i++){
                            zbrojUdalj+=listMojiAkt.get(i).getUdaljenost();
                            zbrojElev+=listMojiAkt.get(i).getNmv();
                        }
                        br10.setText("10");
                        ud10.setText((zbrojUdalj/10.0f)+" km");
                        el10.setText((zbrojElev/10.0f)+" m");
                    }

                    TextView brSvi=(TextView) findViewById(R.id.brAktSvi);
                    TextView udSvi=(TextView) findViewById(R.id.udaljSvi);
                    TextView najUd=(TextView) findViewById(R.id.najUd);
                    TextView najElev=(TextView) findViewById(R.id.najElev);
                    brSvi.setText(""+listAkt.size());
                    udSvi.setText(ukUdSvi+" km");
                    najUd.setText(Math.round(najduzaAkt)+" km");
                    najElev.setText(najveciElev+" m");
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
}