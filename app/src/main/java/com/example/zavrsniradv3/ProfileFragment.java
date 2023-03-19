package com.example.zavrsniradv3;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.android.material.textfield.TextInputEditText;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONObject;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    public ProfileFragment() {
    }
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
    int br2;
    int oveGodine;
    int brojAktivnosti=0;
    int brojObjava=0;
    String e;
    public int brojPratitelja=0;
    public int brojPratim=0;
    String zadnja="";
    public ArrayList<Korisnik> listUs;
    public ArrayList<Aktivnost> listAkt;
    public ArrayList<Aktivnost> listMojiAkt;
    public ArrayList<ObjavaC> listOb;
    public ArrayList<Oprema>listOp;
    public ArrayList<Odnos>listOd;
    int[]images;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        MojeMetode m=new MojeMetode();
        listUs=new ArrayList<>();
        listAkt=new ArrayList<>();
        listOb=new ArrayList<>();
        listOp=new ArrayList<>();
        listOd=new ArrayList<>();
        listMojiAkt=new ArrayList<>();
        ArrayList<Rute>listRut=((HOME)getActivity()).getRut();
        View prof=inflater.inflate(R.layout.fragment_profile,container,false);
        LineChart lineChart=(LineChart) prof.findViewById(R.id.chart);
        LinearLayout aktivnosti=(LinearLayout) prof.findViewById(R.id.prva);
        LinearLayout stats=(LinearLayout) prof.findViewById(R.id.druga);
        LinearLayout objave=(LinearLayout) prof.findViewById(R.id.treca);
        LinearLayout oprema=(LinearLayout) prof.findViewById(R.id.cetvrta);
        TextView nameProf=(TextView)prof.findViewById(R.id.tv1);
        TextView opis=(TextView)prof.findViewById(R.id.tv2);
        assert this.getArguments() != null;
        nameProf.setText(this.getArguments().getString("ime"));
        opis.setText(this.getArguments().getString("opis"));

        TextView st=(TextView) prof.findViewById(R.id.opis2);
        TextView ob=(TextView) prof.findViewById(R.id.opis3);
        TextView ak=(TextView) prof.findViewById(R.id.opis1);
        TextView bra=(TextView) prof.findViewById(R.id.bra);
        TextView bike=(TextView) prof.findViewById(R.id.opis4);
        TextView onimene=(TextView) prof.findViewById(R.id.onimene);
        TextView janjih=(TextView) prof.findViewById(R.id.janjih);
        CircleImageView avatar=(CircleImageView)prof.findViewById(R.id.avatar);

        String id=this.getArguments().getString("id");
        String url=this.getArguments().getString("URL");
        images=this.getArguments().getIntArray("images");
        e=this.getArguments().getString("email");

//dohvaćanje svih korisnika i slanje podataka od trenutnog prijavljenog
        @SuppressLint("SetTextI18n") StringRequest request = new StringRequest(url+"zav/dohvatiSve.php", response -> {
            try {
                JSONArray array = new JSONArray(response);
                for (int loop = 0; loop < array.length(); loop++) {
                    JSONObject object = array.getJSONObject(loop);
                    listUs.add(new Korisnik(object.getInt("id"),
                            object.getString("ime"),
                            object.getString("prezime"),
                            object.getString("email"),
                            object.getString("lozinka"),
                            object.getInt("brojPratitelji"),
                            object.getInt("brojPratim"),
                            object.getInt("slika"),
                            object.getString("opis")));
                }
                for (Korisnik k:listUs) {
                    if(k.getEmail().equals(e)){
                        brojPratitelja=k.getBrojPratitelji();
                        br2=k.getBrojPratim();
                        avatar.setImageResource(images[k.getSlika()]);
                    }
                }
                onimene.setText(""+brojPratitelja);
                janjih.setText(""+br2);
            }
            catch (Exception e) {
            }
        }, error -> {
        });
        Volley.newRequestQueue(getActivity().getApplicationContext()).add(request);

        //dohvaćanje svih aktivnosti
        @SuppressLint("SetTextI18n") StringRequest request2 = new StringRequest(url+"zav/dohvatiSveAktivnosti.php", response2 -> {
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
                for(Aktivnost a:listAkt){
                    if(a.getIdUsera()==Integer.parseInt(id)){
                        oveGodine+=a.getUdaljenost();
                        brojAktivnosti++;
                        if(zadnja.equals("")){
                            zadnja=a.getDatum();
                        }
                        listMojiAkt.add(a);
                    }
                }
                st.setText("Do sada: "+oveGodine+" km");
                bra.setText(""+brojAktivnosti);
                final DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("dd", Locale.ENGLISH);
                final DateTimeFormatter dtf3 = DateTimeFormatter.ofPattern("MM", Locale.ENGLISH);
                final DateTimeFormatter dtf4 = DateTimeFormatter.ofPattern("yyyy", Locale.ENGLISH);
                LocalDateTime d=LocalDateTime.parse(zadnja, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                ak.setText("Posljednja: "+dtf2.format(d)+". "+m.kojiMjesec(dtf3,d)+" "+dtf4.format(d)+".");
                List<Entry>entries=new ArrayList<>();
                for(int i=0;i<10;i++){
                    entries.add(new Entry(i,listMojiAkt.get(9-i).getUdaljenost()));
                }
                LineDataSet dataSet = new LineDataSet(entries, "");
                dataSet.setColor(Color.parseColor("#5BC0F8"));
                dataSet.setValueTextColor(Color.parseColor("#0081C9"));
                dataSet.setCircleColor(Color.parseColor("#5BC0F8"));
                dataSet.setLineWidth(5.0f);
                dataSet.setCircleRadius(5.0f);
                dataSet.setValueTextSize(15.0f);

                LineData lineData = new LineData(dataSet);
                lineChart.setData(lineData);
                Description desc=new Description();
                desc.setText("");
                lineChart.setDescription(desc);
                lineChart.getLegend().setEnabled(false);
                lineChart.invalidate();
            }
            catch (Exception e) {
            }
        }, error2 -> {
        });
        Volley.newRequestQueue(getActivity().getApplicationContext()).add(request2);

        //dohvaćanje svih objava
        StringRequest request3 = new StringRequest(url+"zav/dohvatiSveObjave.php", response3 -> {
            try {
                JSONArray array = new JSONArray(response3);
                for (int loop = 0; loop < array.length(); loop++) {
                    JSONObject object = array.getJSONObject(loop);
                    listOb.add(new ObjavaC(
                            object.getInt("id"),
                            object.getInt("idUsera"),
                            object.getString("ime"),
                            object.getString("datum"),
                            object.getString("naslov"),
                            object.getString("tekst"),
                            object.getString("link"),
                            object.getInt("brojLajkova")));
                }
                for(ObjavaC o:listOb){
                    if(o.getIdUsera()==Integer.parseInt(id)){
                        brojObjava++;
                    }
                }
                ob.setText(""+brojObjava);
            }
            catch (Exception e) {
            }
        }, error -> {
        });
        Volley.newRequestQueue(getActivity().getApplicationContext()).add(request3);

        //dohvaćanje svih odnosa
        StringRequest request4 = new StringRequest(url+"zav/dohvatiSveOdnose.php", response4 -> {
            try {
                JSONArray array = new JSONArray(response4);
                for (int loop = 0; loop < array.length(); loop++) {
                    JSONObject object = array.getJSONObject(loop);
                    listOd.add(new Odnos(
                            object.getInt("id"),
                            object.getInt("idPrati"),
                            object.getInt("idPracen")));
                }
            }
            catch (Exception e) {
            }
        }, error -> {
        });
        Volley.newRequestQueue(getActivity().getApplicationContext()).add(request4);
//dohvaćanje sve opreme
        StringRequest request5 = new StringRequest(url+"zav/dohvatiOpremu.php", response5 -> {
            try {
                JSONArray array = new JSONArray(response5);
                for (int loop = 0; loop < array.length(); loop++) {
                    JSONObject object = array.getJSONObject(loop);
                    listOp.add(new Oprema(
                            object.getInt("id"),
                            object.getString("nadimak"),
                            object.getString("marka"),
                            object.getString("model"),
                            object.getString("tip"),
                            object.getInt("idCije")));
                }
                for(Oprema o:listOp){
                    if(o.getIdCije()==Integer.parseInt(id)){
                        bike.setText(o.getMarka()+" "+o.getModel());
                    }
                }
            }
            catch (Exception e) {
            }
        }, error -> {
        });
        Volley.newRequestQueue(getActivity().getApplicationContext()).add(request5);
        aktivnosti.setOnClickListener(view -> {
            Intent intent=new Intent(getContext(),PopisAktivnosti.class);
            intent.putExtra("listAkt",listAkt);
            intent.putExtra("listUs",listUs);
            intent.putExtra("listRut",listRut);
            intent.putExtra("id",id);
            intent.putExtra("URL",url);
            intent.putExtra("images",images);
            startActivity(intent);
        });
        stats.setOnClickListener(view -> {
            Intent intent=new Intent(getContext(),Statistika.class);
            intent.putExtra("id",id);
            intent.putExtra("URL",url);
            startActivity(intent);
        });
        objave.setOnClickListener(view -> {
            Intent intent=new Intent(getContext(),Objave.class);
            intent.putExtra("listAkt",listAkt);
            intent.putExtra("listUs",listUs);
            intent.putExtra("listOb",listOb);
            intent.putExtra("id",id);
            intent.putExtra("URL",url);
            intent.putExtra("images",images);
            intent.putParcelableArrayListExtra("listaOdnosa",listOd);
            startActivity(intent);
        });
        oprema.setOnClickListener(view -> {
            Intent intent=new Intent(getContext(), PopisOpreme.class);
            intent.putExtra("URL",url);
            intent.putExtra("listOp",listOp);
            intent.putExtra("listAkt",listAkt);
            intent.putExtra("id",id);
            for(Oprema o:listOp){
                Log.d("objava1:",o.getNadimak()+" "+o.getMarka()+" "+o.getModel());
            }
            startActivity(intent);
        });

        LinearLayout kA=(LinearLayout) prof.findViewById(R.id.klikAkt);
        kA.setOnClickListener(view -> {
            Intent intent=new Intent(getContext(),PopisAktivnosti.class);
            intent.putExtra("listAkt",listAkt);
            intent.putExtra("listUs",listUs);
            intent.putExtra("listRut",listRut);
            intent.putExtra("id",id);
            intent.putExtra("URL",url);
            intent.putExtra("images",images);
            startActivity(intent);
        });
        LinearLayout kP=(LinearLayout) prof.findViewById(R.id.klikPra);
        kP.setOnClickListener(view -> {
            Intent intent=new Intent(getContext(),Pratitelji.class);
            intent.putExtra("id",id);
            intent.putExtra("URL",url);
            intent.putParcelableArrayListExtra("lista",listUs);
            intent.putParcelableArrayListExtra("listaOdnosa",listOd);
            intent.putExtra("images",images);
            intent.putExtra("br2",br2);
            intent.putExtra("prvo",0);
            startActivity(intent);
        });
        LinearLayout kP2=(LinearLayout) prof.findViewById(R.id.klikPratim);
        kP2.setOnClickListener(view -> {
            Intent intent=new Intent(getContext(),Pratitelji.class);
            intent.putExtra("id",id);
            intent.putExtra("URL",url);
            intent.putParcelableArrayListExtra("lista",listUs);
            intent.putParcelableArrayListExtra("listaOdnosa",listOd);
            intent.putExtra("images",images);
            intent.putExtra("br2",br2);
            intent.putExtra("prvo",1);
            Log.d("br2prof",""+br2);
            startActivity(intent);
        });


        ImageView img=(ImageView) prof.findViewById(R.id.ljudi);
        img.setOnClickListener(view -> {
            Intent intent=new Intent(getContext(),PopisKorisnika.class);
            intent.putParcelableArrayListExtra("lista",listUs);
            intent.putParcelableArrayListExtra("listaOdnosa",listOd);
            intent.putExtra("URL",url);
            intent.putExtra("id",id);
            intent.putExtra("br2",brojPratim);
            intent.putExtra("images",images);
            startActivity(intent);
        });
        ImageView img2=(ImageView) prof.findViewById(R.id.edit);
        img2.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.AlertDialogStyle2);
            View dizajn = getLayoutInflater().inflate(R.layout.edit_user, null);
            builder.setView(dizajn);
            builder.show();
            TextInputEditText ime=(TextInputEditText) dizajn.findViewById(R.id.ime);
            TextInputEditText prezime=(TextInputEditText) dizajn.findViewById(R.id.prezime);
            TextInputEditText bio=(TextInputEditText) dizajn.findViewById(R.id.bio);
            for(Korisnik k:listUs){
                if(k.getId()==Integer.parseInt(id)){
                    ime.setText(k.getIme());
                    prezime.setText(k.getPrezime());
                    bio.setText(k.getOpis());
                }
            }
            String locurl=url+"zav/urediKorisnika.php";
            String type = "uredi";

            Button btn=(Button) dizajn.findViewById(R.id.button2);
            btn.setOnClickListener(view1 -> {
                BackgroundWorker backgroundWorker = new BackgroundWorker(getContext().getApplicationContext(),11);
                backgroundWorker.execute(locurl,type,id, Objects.requireNonNull(ime.getText()).toString(), Objects.requireNonNull(prezime.getText()).toString(), Objects.requireNonNull(bio.getText()).toString());
            });
        });
        return prof;
    }
}