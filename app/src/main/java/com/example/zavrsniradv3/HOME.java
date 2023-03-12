package com.example.zavrsniradv3;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HOME extends AppCompatActivity {
    public ArrayList<Korisnik> lista;
    public ArrayList<Aktivnost> listaAktivnosti;
    public ArrayList<ObjavaC> listaObjava;
    public ArrayList<Odnos>listaOdnosa;
    public ArrayList<Oprema>listaOpreme;
    public ArrayList<Rute>listRut;
    public String naziv="";
    public String idUsera="";
    public float oveGodine;
    public int brojObjava=0;
    public int brojAktivnosti=0;
    public String zadnji="";
    public ArrayList<Aktivnost> getAkt() {
        return listaAktivnosti;
    }
    public ArrayList<ObjavaC>getOb(){return listaObjava;}
    public ArrayList<Korisnik>getUs(){return lista;}
    public ArrayList<Odnos>getOd(){return listaOdnosa;}
    public ArrayList<Oprema>getOp(){return listaOpreme;}
    public ArrayList<Rute>getRut(){return listRut;}
    public String url="";
    public String e;
    public int brojPratitelja=0;
    public int brojPratim=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Intent i=getIntent();
        url=i.getStringExtra("URL");
        e=i.getStringExtra("EMAIL");
        Log.d("email3",e);

        String pa=i.getStringExtra("PASSWORD");
        lista=new ArrayList<Korisnik>();
        listaAktivnosti=new ArrayList<Aktivnost>();
        listaObjava=new ArrayList<ObjavaC>();
        listaOdnosa=new ArrayList<Odnos>();
        listaOpreme=new ArrayList<>();
        listRut=new ArrayList<Rute>();
        //replaceFragment(new HomeFragment());
        BottomNavigationView bnv=(BottomNavigationView) findViewById(R.id.bottomNavigationView);
        bnv.setOnItemSelectedListener(item ->{
            switch (item.getItemId()){
                case R.id.home:
                    sendList();
                    //replaceFragment(new HomeFragment());
                    break;
                case R.id.record:
                    //replaceFragment(new RecordFragment());
                    Intent intent=new Intent(this,ActivityRecord.class);
                    intent.putExtra("URL",url);
                    intent.putExtra("EMAIL",e);
                    intent.putExtra("PASS",pa);
                    intent.putExtra("id",idUsera+"");
                    intent.putExtra("ime",naziv);
                    intent.putExtra("listOp",listaOpreme);
                    intent.putExtra("listRut",listRut);
                    startActivity(intent);
                    break;
                case R.id.profile:
                    Bundle bundle=new Bundle();
                    bundle.putParcelableArrayList("listaKor",lista);
                    bundle.putParcelableArrayList("lista",listaAktivnosti);
                    bundle.putParcelableArrayList("listaOb",listaObjava);
                    bundle.putString("poruka",naziv);
                    bundle.putFloat("ovegodine",oveGodine);
                    bundle.putInt("bro",brojObjava);
                    bundle.putInt("bra",brojAktivnosti);
                    bundle.putString("id",idUsera+"");
                    bundle.putString("URL",url);
                    bundle.putString("email",e);
                    bundle.putInt("brojPratitelja",brojPratitelja);
                    bundle.putInt("brojPratim",brojPratim);
                    //bundle.putString("zadnjiD",zadnji);
                    ProfileFragment p=new ProfileFragment();
                    p.setArguments(bundle);
                    replaceFragment(p);
                    break;
            }
            return true;
        });

        View prof=getLayoutInflater().inflate(R.layout.fragment_profile,null);
        LinearLayout aktivnosti=(LinearLayout) prof.findViewById(R.id.prva);
        aktivnosti.setClickable(true);
        aktivnosti.setOnClickListener(view -> {
            Intent intent=new Intent(HOME.this,Statistika.class);
            HOME.this.startActivity(intent);
        });

        Thread thread = new Thread(new Runnable(){
            @Override
            public void run(){

        //dohvaćanje svih korisnika i slanje podataka od trenutnog prijavljenog
        StringRequest request = new StringRequest(url+"zav/dohvatiSve.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray array = new JSONArray(response);
                    for (int loop = 0; loop < array.length(); loop++) {
                        JSONObject object = array.getJSONObject(loop);
                        lista.add(new Korisnik(object.getInt("id"),
                                object.getString("ime"),
                                object.getString("prezime"),
                                object.getString("email"),
                                object.getString("lozinka"),
                                object.getInt("brojPratitelji"),
                                object.getInt("brojPratim"),
                                object.getInt("slika"),
                                object.getString("opis")));
                    }
                    Intent in=getIntent();
                    String e=in.getStringExtra("EMAIL");
                    Log.d("email: ",e);
                    for (Korisnik k:lista) {
                        if(k.getEmail().equals(e)){
                            brojPratitelja=k.getBrojPratitelji();
                            brojPratim=k.getBrojPratim();
                            naziv=k.getIme()+" "+k.getPrezime();
                            idUsera=""+k.getId();
                            Log.d("idUsera: ",idUsera);
                            break;
                        }
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
        Volley.newRequestQueue(HOME.this).add(request);
            }
        });
        thread.start();
        //dohvaćanje svih aktivnosti
        StringRequest request2 = new StringRequest(url+"zav/dohvatiSveAktivnosti.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response2) {
                try {
                    JSONArray array = new JSONArray(response2);
                    for (int loop = 0; loop < array.length(); loop++) {
                        JSONObject object = array.getJSONObject(loop);
                        listaAktivnosti.add(new Aktivnost(
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
                    //sendList();
                    /*for(Aktivnost a:listaAktivnosti){
                        if(a.getIdUsera()==Integer.parseInt(idUsera)){
                            oveGodine+=a.getUdaljenost();
                            brojAktivnosti++;
                        }

                    }*/
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
                  // sendList();
                    /*for(ObjavaC o:listaObjava){
                        if(o.getIdUsera()==Integer.parseInt(idUsera)){
                            brojObjava++;
                        }
                    }*/
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
        Volley.newRequestQueue(this).add(request3);

        //dohvaćanje svih odnosa
        StringRequest request4 = new StringRequest(url+"zav/dohvatiSveOdnose.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response4) {
                try {
                    JSONArray array = new JSONArray(response4);
                    for (int loop = 0; loop < array.length(); loop++) {
                        JSONObject object = array.getJSONObject(loop);
                        listaOdnosa.add(new Odnos(
                                object.getInt("id"),
                                object.getInt("idPrati"),
                                object.getInt("idPracen")));
                    }
                   // sendList();
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
        Volley.newRequestQueue(this).add(request4);

//dohvaćanje sve opreme
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
                    sendList();

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

        //dohvati rute
        StringRequest request6 = new StringRequest(url+"zav/dohvatiKoord.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray array = new JSONArray(response);
                    for (int loop = 0; loop < array.length(); loop++) {
                        JSONObject object = array.getJSONObject(loop);
                        listRut.add(new Rute(object.getInt("idAkt"),
                                object.getDouble("startLat"),
                                object.getDouble("startLong"),
                                object.getDouble("endLat"),
                                object.getDouble("endLong")));
                    }
                    for(Rute r:listRut){
                                Log.d("aha",r.getIdAkt()+"");
                            }



                }
                catch (Exception e) {
                    AlertDialog alertDialog = new AlertDialog.Builder(HOME.this).create();
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
        Volley.newRequestQueue(this).add(request6);



    }
    private void replaceFragment(Fragment fragment){
        FragmentManager fM=getSupportFragmentManager();
        FragmentTransaction fT=fM.beginTransaction();
        fT.replace(R.id.frame1,fragment);
        fT.commit();
    }
    public void sendList(){
        Bundle bundle1=new Bundle();
        bundle1.putParcelableArrayList("listaKor",lista);
        bundle1.putParcelableArrayList("lista",listaAktivnosti);
        bundle1.putParcelableArrayList("listaObjava",listaObjava);
        bundle1.putParcelableArrayList("listaOdnosa",listaOdnosa);
        bundle1.putParcelableArrayList("listaOpreme",listaOpreme);
        bundle1.putParcelableArrayList("listRut",listRut);
        bundle1.putFloat("ovegodine",oveGodine);
        bundle1.putInt("bro",brojObjava);
        bundle1.putInt("bra",brojAktivnosti);
        bundle1.putString("id",idUsera+"");
        bundle1.putString("ime",naziv);
        bundle1.putString("URL",url);
        bundle1.putString("email",e);
        bundle1.putInt("brojPratim",brojPratim);
        HomeFragment h=new HomeFragment();
        h.setArguments(bundle1);
        replaceFragment(h);
    }
}