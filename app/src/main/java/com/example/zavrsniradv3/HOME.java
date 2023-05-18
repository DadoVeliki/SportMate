package com.example.zavrsniradv3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;

public class HOME extends AppCompatActivity {
    public ArrayList<Korisnik> lista;
    public ArrayList<Aktivnost> listaAktivnosti;
    public ArrayList<ObjavaC> listaObjava;
    public ArrayList<Odnos>listaOdnosa;
    public ArrayList<Oprema>listaOpreme;
    public ArrayList<Rute>listRut;
    public String naziv="",opis="",idUsera="",url="",e,pa;
    public float oveGodine;
    public int brojObjava=0,brojAktivnosti=0,brojPratitelja=0,brojPratim=0;
    public ArrayList<Aktivnost> getAkt() {
        return listaAktivnosti;
    }
    public ArrayList<ObjavaC>getOb(){return listaObjava;}
    public ArrayList<Korisnik>getUs(){return lista;}
    public ArrayList<Odnos>getOd(){return listaOdnosa;}
    public ArrayList<Oprema>getOp(){return listaOpreme;}
    public ArrayList<Rute>getRut(){return listRut;}
    int[] images={R.drawable.avatar,R.drawable.avatar2,R.drawable.avatar3,R.drawable.avatar4,R.drawable.avatar5,R.drawable.avatar6,R.drawable.avatar8,R.drawable.avatar9,R.drawable.avatar10,R.drawable.avatar11,R.drawable.avatar12,R.drawable.avatar13,R.drawable.avatar14,R.drawable.avatar15,R.drawable.avatar16,};
    Thread thread;
    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Intent i=getIntent();
        url=i.getStringExtra("URL");
        e=i.getStringExtra("EMAIL");
        pa=i.getStringExtra("PASSWORD");
        lista=new ArrayList<>();
        listaAktivnosti=new ArrayList<>();
        listaObjava=new ArrayList<>();
        listaOdnosa=new ArrayList<>();
        listaOpreme=new ArrayList<>();
        listRut=new ArrayList<>();
        BottomNavigationView bnv=findViewById(R.id.bottomNavigationView);
        bnv.setOnItemSelectedListener(item ->{
            switch (item.getItemId()){
                case R.id.home:
                    new Thread(()->{sendList(new HomeFragment());}).start();
                    break;
                case R.id.record:
                    Intent intent=new Intent(this,ActivityRecord.class);
                    intent.putExtra("URL",url);
                    intent.putExtra("EMAIL",e);
                    intent.putExtra("PASS",pa);
                    intent.putExtra("id",idUsera+"");
                    intent.putExtra("ime",naziv);
                    intent.putExtra("opis",opis);
                    intent.putExtra("listOp",listaOpreme);
                    intent.putExtra("listRut",listRut);
                    intent.putExtra("images",images);
                    startActivity(intent);
                    break;
                case R.id.profile:
                    new Thread(()->{sendList(new ProfileFragment());}).start();
                    break;
            }
            return true;
        });

        /*@SuppressLint("InflateParams") View prof=getLayoutInflater().inflate(R.layout.fragment_profile,null);
        LinearLayout aktivnosti=prof.findViewById(R.id.prva);
        aktivnosti.setClickable(true);
        aktivnosti.setOnClickListener(view -> {
            Intent intent=new Intent(HOME.this,Statistika.class);
            HOME.this.startActivity(intent);
        });*/

        thread = new Thread(() -> {

    //dohvaćanje svih korisnika i slanje podataka od trenutnog prijavljenog
    StringRequest request = new StringRequest(url+"zav/dohvatiSve.php", response -> {
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
                    opis=k.getOpis();
                    idUsera=""+k.getId();
                    Log.d("idUsera: ",idUsera);
                    break;
                }
}
        }
        catch (Exception e) {e.printStackTrace();
        }
    }, error -> {
    });
    Volley.newRequestQueue(HOME.this).add(request);

    //dohvaćanje svih aktivnosti
    StringRequest request2 = new StringRequest(url+"zav/dohvatiSveAktivnosti.php", response2 -> {
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
                        object.getString("oprema"),
                        object.getString("tipAkt")));
            }
        }
        catch (Exception e) {e.printStackTrace();
        }
    }, error2 -> {
    });
    Volley.newRequestQueue(HOME.this).add(request2);
     //   }
   // });
    //dohvaćanje svih objava
    StringRequest request3 = new StringRequest(url+"zav/dohvatiSveObjave.php", response3 -> {
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
        }
        catch (Exception e) {e.printStackTrace();
        }
    }, error -> {
    });
    Volley.newRequestQueue(HOME.this).add(request3);

    //dohvaćanje svih odnosa
    StringRequest request4 = new StringRequest(url+"zav/dohvatiSveOdnose.php", response4 -> {
        try {
            JSONArray array = new JSONArray(response4);
            for (int loop = 0; loop < array.length(); loop++) {
                JSONObject object = array.getJSONObject(loop);
                listaOdnosa.add(new Odnos(
                        object.getInt("id"),
                        object.getInt("idPrati"),
                        object.getInt("idPracen")));
            }
        }
        catch (Exception e) {e.printStackTrace();
        }
    }, error -> {
    });
    Volley.newRequestQueue(HOME.this).add(request4);

//dohvaćanje sve opreme
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
        }
        catch (Exception e) {e.printStackTrace();
        }
    }, error -> {
    });
    Volley.newRequestQueue(HOME.this).add(request5);

    //dohvati rute
    StringRequest request6 = new StringRequest(url+"zav/dohvatiKoord.php", response -> {
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
            sendList(new HomeFragment());
        }
        catch (Exception e) {e.printStackTrace();
        }
    }, error -> {
    });
    Volley.newRequestQueue(HOME.this).add(request6);
        });
        thread.start();

    }
    private void replaceFragment(Fragment fragment){
        getSupportFragmentManager().beginTransaction().replace(R.id.frame1,fragment).commit();
    }
    public void sendList(Fragment f){
        Thread thread = new Thread(() -> {
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
            bundle1.putString("opis",opis);
            bundle1.putString("URL",url);
            bundle1.putString("email",e);
            bundle1.putInt("brojPratitelja",brojPratitelja);
            bundle1.putInt("brojPratim",brojPratim);
            bundle1.putIntArray("images",images);
            f.setArguments(bundle1);
            replaceFragment(f);
        });
        thread.start();
    }
}
