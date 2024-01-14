package com.example.zavrsniradv3;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import de.hdodenhof.circleimageview.CircleImageView;

public class PopisKorisnika extends AppCompatActivity {
    ArrayList<Korisnik>listUs;
    ArrayList<Odnos>listOd;
    int br2;
    public String url="";
    int[]images;
    public ArrayList<Integer>pratitelji;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popis_korisnika);
        pratitelji=new ArrayList<>();
        listOd=new ArrayList<>();
        Intent i=getIntent();
        images=i.getIntArrayExtra("images");
        url=i.getStringExtra("URL");
        String id=i.getStringExtra("id");
        br2=i.getIntExtra("br2",0);
        Log.d("br2",""+br2);
        LinearLayout l=findViewById(R.id.ll2);
        listUs=i.getParcelableArrayListExtra("lista");
        findViewById(R.id.backAkt).setOnClickListener(view -> finish());
        ArrayList<Integer>praceni=new ArrayList<>();
        //dohvaćanje svih odnosa
        @SuppressLint({"SetTextI18n", "ResourceType"}) StringRequest request4 = new StringRequest(url+"zav/dohvatiSveOdnose.php", response4 -> {
            try {
                JSONArray array = new JSONArray(response4);
                for (int loop = 0; loop < array.length(); loop++) {
                    JSONObject object = array.getJSONObject(loop);
                    listOd.add(new Odnos(
                            object.getInt("id"),
                            object.getInt("idPrati"),
                            object.getInt("idPracen")));
                }
                for(Odnos o:listOd){
                    try{
                        if(o.getIdKojiPrati()==Integer.parseInt(id)){
                            praceni.add(o.getIdPracen());
                        }
                    }
                    catch(NumberFormatException n){n.printStackTrace();
                    }
                }
                for(Korisnik k:listUs){
                    pratitelji.add(k.getBrojPratitelji());
                }

                for(Korisnik k:listUs){
                    if(k.getId()!=Integer.parseInt(id)) {
                        @SuppressLint("InflateParams") View us = getLayoutInflater().inflate(R.layout.prikaz_usera, null);
                        TextView name =  us.findViewById(R.id.ime);
                        TextView desc = us.findViewById(R.id.op);
                        CircleImageView profile=us.findViewById(R.id.profile_image);
                        name.setText(k.getIme() + " " + k.getPrezime());
                        desc.setText(k.getOpis());
                        profile.setImageResource(images[k.getSlika()]);

                        Button btn =us.findViewById(R.id.btn);
                        btn.setId(k.getId());
                        btn.setContentDescription("" + 0);
                        for (int a = 0; a < praceni.size(); a++) {
                            if (btn.getId() == praceni.get(a)) {
                                MojeMetode.zaprati(btn);
                            }
                        }
                        btn.setOnClickListener(view -> {
                            int con = Integer.parseInt(btn.getContentDescription().toString());
                            if (con == 0) {
                                MojeMetode.zaprati(btn);
                                pratitelji.set(btn.getId() - 1, pratitelji.get(btn.getId() - 1) + 1);
                                String idOsoba = btn.getId() + "";
                                String locurl = url + "zav/unosOdnos.php";
                                String type = "odn";

                                br2++;
                                BackgroundWorker backgroundWorker = new BackgroundWorker(PopisKorisnika.this, 5);
                                backgroundWorker.execute(locurl, type, id, idOsoba, pratitelji.get(btn.getId() - 1) + "", br2 + "");
                            } else {
                                btn.setText("PRATI");
                                btn.setTextColor(Color.WHITE);
                                btn.setBackgroundColor(Color.parseColor("#FF5722"));
                                btn.setContentDescription("" + 0);
                                pratitelji.set(btn.getId() - 1, pratitelji.get(btn.getId() - 1) - 1);
                                String idOsoba = btn.getId() + "";
                                String locurl = url + "zav/ukloniOdnos.php";
                                String type = "odn";
                                br2--;
                                BackgroundWorker backgroundWorker = new BackgroundWorker(PopisKorisnika.this, 5);
                                backgroundWorker.execute(locurl, type, id, idOsoba, pratitelji.get(btn.getId() - 1) + "", br2 + "");
                            }
                        });
                       // if(k.getId()!=Integer.parseInt(id)){
                        LinearLayout p = us.findViewById(R.id.parent);
                        l.addView(p);
                     //   }

                    }
                }
            }
            catch (Exception e) {e.printStackTrace();
            }
        }, error -> {
        });
        Volley.newRequestQueue(this).add(request4);
        /*for(Odnos o:listOd){
            try{
                if(o.getIdKojiPrati()==Integer.parseInt(id)){
                    praceni.add(o.getIdPracen());
                }
            }
            catch(NumberFormatException n){
                Log.e("nije broj: ",id+"");
            }
        }*/

        /*for(Korisnik k:listUs){
            View us=getLayoutInflater().inflate(R.layout.prikaz_usera,null);
            TextView name=(TextView)us.findViewById(R.id.ime);
            TextView desc=(TextView)us.findViewById(R.id.op);
            name.setText(k.getIme()+" "+k.getPrezime());
            desc.setText("Neka drzava");

            LinearLayout p=(LinearLayout)us.findViewById(R.id.parent);
            int br=brojac++;
            Button btn=(Button)us.findViewById(R.id.btn);
            btn.setId(br);
            btn.setContentDescription(""+0);
            for(int a=0;a<praceni.size();a++){
                if(btn.getId()==praceni.get(a)){
                    zaprati(btn);
                }
            }

            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("INDEX: ",btn.getContentDescription()+"");
                    int con=Integer.parseInt(btn.getContentDescription().toString());
                    if(con==0){
                        zaprati(btn);
                        int brojPrat=listUs.get(btn.getId()-1).getBrojPratitelji();
                        String idOsoba=btn.getId()+"";
                        String locurl=url+"zav/unosOdnos.php";
                        String type = "odn";
                        brojPrat++;
                        br2++;
                        BackgroundWorker backgroundWorker = new BackgroundWorker(PopisKorisnika.this,5);
                        backgroundWorker.execute(locurl,type,id,idOsoba,brojPrat+"",br2+"");
                    }
                    else{
                        btn.setText("PRATI");
                        btn.setTextColor(Color.WHITE);
                        btn.setBackgroundColor(Color.parseColor("#FF5722"));
                        btn.setContentDescription(""+0);
                        int brojPrat=listUs.get(btn.getId()-1).getBrojPratitelji();
                        String idOsoba=btn.getId()+"";
                        String locurl=url+"zav/ukloniOdnos.php";
                        String type = "odn";
                        brojPrat--;
                        br2--;
                        BackgroundWorker backgroundWorker = new BackgroundWorker(PopisKorisnika.this,5);
                        backgroundWorker.execute(locurl,type,id,idOsoba,brojPrat+"",br2+"");
                    }

                }
            });
            l.addView(p);
        }*/
    }
}