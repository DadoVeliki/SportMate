package com.example.zavrsniradv3;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Path;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.android.volley.Response;
import com.android.volley.VolleyError;
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

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String imePrezime;
    public ProfileFragment() {
        //this.imePrezime=naziv;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
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
    int br1,br2;
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
   // int[] images={R.drawable.avatar,R.drawable.avatar2,R.drawable.avatar3,R.drawable.avatar4,R.drawable.avatar5,R.drawable.avatar6,R.drawable.avatar8,R.drawable.avatar9,R.drawable.avatar10,R.drawable.avatar11,R.drawable.avatar12,R.drawable.avatar13,R.drawable.avatar14,R.drawable.avatar15,R.drawable.avatar16,};
    int[]images;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        listUs=new ArrayList<Korisnik>();
        listAkt=new ArrayList<Aktivnost>();
        listOb=new ArrayList<ObjavaC>();
        listOp=new ArrayList<Oprema>();
        listOd=new ArrayList<Odnos>();
        listMojiAkt=new ArrayList<Aktivnost>();
        //ArrayList<Korisnik>listUs=((HOME)getActivity()).getUs();
        //ArrayList<Odnos>listOd=((HOME)getActivity()).getOd();
        //ArrayList<Aktivnost>listAkt=((HOME) getActivity()).getAkt();
        //ArrayList<ObjavaC>listOb=((HOME) getActivity()).getOb();
        //ArrayList<Oprema>listOp=((HOME)getActivity()).getOp();
        ArrayList<Rute>listRut=((HOME)getActivity()).getRut();
        View prof=inflater.inflate(R.layout.fragment_profile,container,false);
        LineChart lineChart=(LineChart) prof.findViewById(R.id.chart);
        LinearLayout aktivnosti=(LinearLayout) prof.findViewById(R.id.prva);
        LinearLayout stats=(LinearLayout) prof.findViewById(R.id.druga);
        LinearLayout objave=(LinearLayout) prof.findViewById(R.id.treca);
        LinearLayout oprema=(LinearLayout) prof.findViewById(R.id.cetvrta);
        TextView nameProf=(TextView)prof.findViewById(R.id.tv1);
        TextView opis=(TextView)prof.findViewById(R.id.tv2);

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
        //br1=this.getArguments().getInt("brojPratitelja");
        //br2=this.getArguments().getInt("brojPratim");

        String id=this.getArguments().getString("id");
        String url=this.getArguments().getString("URL");
        images=this.getArguments().getIntArray("images");
        e=this.getArguments().getString("email");
        /*for(Oprema o:listOp){
            if(o.getIdCije()==Integer.parseInt(id)){
                bike.setText(o.getMarka()+" "+o.getModel());
            }
        }
        for(Aktivnost a:listAkt){
            if(a.getIdUsera()==Integer.parseInt(id)){
                oveGodine+=a.getUdaljenost();
                brojAktivnosti++;
                zadnja=a.getDatum();
            }
        }
        for(ObjavaC o:listOb){
            if(o.getIdUsera()==Integer.parseInt(id)){
                brojObjava++;
            }
        }
        e=this.getArguments().getString("email");
        for (Korisnik k:listUs) {
            if (k.getEmail().equals(e)) {
                brojPratitelja=k.getBrojPratitelji();
                brojPratim=k.getBrojPratim();
            }
        }*/
//dohvaćanje svih korisnika i slanje podataka od trenutnog prijavljenog
        StringRequest request = new StringRequest(url+"zav/dohvatiSve.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
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
                    AlertDialog alertDialog = new AlertDialog.Builder(getActivity().getApplicationContext()).create();
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
        Volley.newRequestQueue(getActivity().getApplicationContext()).add(request);

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
                    //sendList();
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
                    final DateTimeFormatter dtf5 = DateTimeFormatter.ofPattern("HH:mm", Locale.ENGLISH);
                    LocalDateTime d=LocalDateTime.parse(zadnja, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

                    ak.setText("Posljednja: "+dtf2.format(d)+". "+kojiMjesec(dtf3,d)+" "+dtf4.format(d)+".");

                    List<Entry>entries=new ArrayList<Entry>();
                    for(int i=0;i<10;i++){
                        entries.add(new Entry(i,listMojiAkt.get(9-i).getUdaljenost()));
                    }
                    LineDataSet dataSet = new LineDataSet(entries, ""); // add entries to dataset
                    dataSet.setColor(Color.parseColor("#FF5722"));
                    dataSet.setValueTextColor(Color.parseColor("#FF5722"));
                    dataSet.setCircleColor(Color.parseColor("#FF5722"));
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
                    AlertDialog alertDialog = new AlertDialog.Builder(getActivity().getApplicationContext()).create();
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
        Volley.newRequestQueue(getActivity().getApplicationContext()).add(request2);

        //dohvaćanje svih objava
        StringRequest request3 = new StringRequest(url+"zav/dohvatiSveObjave.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response3) {
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
                    AlertDialog alertDialog = new AlertDialog.Builder(getActivity().getApplicationContext()).create();
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
        Volley.newRequestQueue(getActivity().getApplicationContext()).add(request3);

        //dohvaćanje svih odnosa
        StringRequest request4 = new StringRequest(url+"zav/dohvatiSveOdnose.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response4) {
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
                    AlertDialog alertDialog = new AlertDialog.Builder(getActivity().getApplicationContext()).create();
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
        Volley.newRequestQueue(getActivity().getApplicationContext()).add(request4);
//dohvaćanje sve opreme
        StringRequest request5 = new StringRequest(url+"zav/dohvatiOpremu.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response5) {
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
                    AlertDialog alertDialog = new AlertDialog.Builder(getActivity().getApplicationContext()).create();
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
        Volley.newRequestQueue(getActivity().getApplicationContext()).add(request5);
/*
        bra.setText(""+brojAktivnosti);
        ob.setText(""+brojObjava);
        st.setText("Ove godine: "+oveGodine+" km");
        onimene.setText(""+brojPratitelja);
        janjih.setText(""+brojPratim);
        ak.setText(zadnja);
*/
        aktivnosti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(),PopisAktivnosti.class);
                intent.putExtra("listAkt",listAkt);
                intent.putExtra("listUs",listUs);
                intent.putExtra("listRut",listRut);
                intent.putExtra("id",id);
                intent.putExtra("URL",url);
                intent.putExtra("images",images);
                startActivity(intent);
            }
        });
        stats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(),Statistika.class);
                intent.putExtra("id",id);
                intent.putExtra("URL",url);
                startActivity(intent);
            }
        });
        objave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(),Objave.class);
                intent.putExtra("listAkt",listAkt);
                intent.putExtra("listUs",listUs);
                intent.putExtra("listOb",listOb);
                intent.putExtra("id",id);
                intent.putExtra("URL",url);
                intent.putExtra("images",images);
                startActivity(intent);
            }
        });
        oprema.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(), PopisOpreme.class);
                intent.putExtra("URL",url);
                intent.putExtra("listOp",listOp);
                intent.putExtra("listAkt",listAkt);
                intent.putExtra("id",id);
                for(Oprema o:listOp){
                    Log.d("objava1:",o.getNadimak()+" "+o.getMarka()+" "+o.getModel());
                }
                startActivity(intent);
            }
        });

        LinearLayout kA=(LinearLayout) prof.findViewById(R.id.klikAkt);
        kA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(),PopisAktivnosti.class);
                intent.putExtra("listAkt",listAkt);
                intent.putExtra("listUs",listUs);
                intent.putExtra("listRut",listRut);
                intent.putExtra("id",id);
                intent.putExtra("URL",url);
                intent.putExtra("images",images);
                startActivity(intent);
            }
        });
        LinearLayout kP=(LinearLayout) prof.findViewById(R.id.klikPra);
        kP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(),Pratitelji.class);
                intent.putExtra("id",id);
                intent.putExtra("URL",url);
                intent.putParcelableArrayListExtra("lista",listUs);
                intent.putParcelableArrayListExtra("listaOdnosa",listOd);
                intent.putExtra("images",images);
                intent.putExtra("br2",br2);
                intent.putExtra("prvo",0);
                startActivity(intent);
            }
        });
        LinearLayout kP2=(LinearLayout) prof.findViewById(R.id.klikPratim);
        kP2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
            }
        });


        ImageView img=(ImageView) prof.findViewById(R.id.ljudi);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(),PopisKorisnika.class);
                intent.putParcelableArrayListExtra("lista",listUs);
                intent.putParcelableArrayListExtra("listaOdnosa",listOd);
                intent.putExtra("URL",url);
                intent.putExtra("id",id);
                intent.putExtra("br2",brojPratim);
                intent.putExtra("images",images);
                startActivity(intent);
            }
        });
        ImageView img2=(ImageView) prof.findViewById(R.id.edit);
        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        BackgroundWorker backgroundWorker = new BackgroundWorker(getContext().getApplicationContext(),11);
                        backgroundWorker.execute(locurl,type,id,ime.getText().toString(),prezime.getText().toString(),bio.getText().toString());
                    }
                });
            }
        });

        return prof;
    }
    public String kojiMjesec(DateTimeFormatter f, LocalDateTime d){
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
}