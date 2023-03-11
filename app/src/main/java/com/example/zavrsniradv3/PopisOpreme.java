package com.example.zavrsniradv3;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONObject;
import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import java.util.ArrayList;

public class PopisOpreme extends AppCompatActivity {
    public String url="";
    String tip="",id;
    public ArrayList<Oprema>listaOpreme;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oprema);
        ImageView img=(ImageView) findViewById(R.id.backOp);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        Intent intent=getIntent();
        url=intent.getStringExtra("URL");
        id=intent.getStringExtra("id");
        listaOpreme=new ArrayList<>();
        //ArrayList<Oprema>listaOpreme=intent.getParcelableArrayListExtra("listOp");
        String id=intent.getStringExtra("id");
        LinearLayout l=(LinearLayout) findViewById(R.id.linearBikes);

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
                    for(Oprema o:listaOpreme){
                        if(o.getIdCije()==Integer.parseInt(id)){

                            View op=getLayoutInflater().inflate(R.layout.prikaz_opreme,null);
                            TextView nick=(TextView)op.findViewById(R.id.nick);
                            TextView name=(TextView)op.findViewById(R.id.name);
                            ImageView vrsta=(ImageView)op.findViewById(R.id.vrsta);

                            nick.setText(o.getNadimak());
                            name.setText(o.getMarka()+" "+o.getModel());
                            if(o.getTip().equals("Bicikl")){
                                vrsta.setImageResource(R.drawable.bajk2);
                            }
                            else if(o.getTip().equals("Tenisice")){
                                vrsta.setImageResource(R.drawable.patike);
                            }
                            LinearLayout p=(LinearLayout)op.findViewById(R.id.parent);
                            l.addView(p);
                            ImageView remove=(ImageView) op.findViewById(R.id.remove);
                            remove.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    String locUrl=url+"zav/ukloniOpremu.php";
                                    String type = "uklOp";
                                    BackgroundWorker backgroundWorker = new BackgroundWorker(PopisOpreme.this,7);
                                    backgroundWorker.execute(locUrl,type,o.getId()+"");
                                }
                            });
                    }
                    }
                    //sendList();

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

        ImageView dodavanje=(ImageView) findViewById(R.id.add);
        dodavanje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PopisOpreme.this, R.style.AlertDialogStyle);
                View dizajn = getLayoutInflater().inflate(R.layout.dodavanje_opreme, null);
                builder.setView(dizajn);
                builder.show();
                String[] items=new String[2];
                items[0]="Bicikl";
                items[1]="Tenisice";
                Spinner spinner = (Spinner) dizajn.findViewById(R.id.spinner);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(PopisOpreme.this,
                        android.R.layout.simple_spinner_dropdown_item, items);

                spinner.setAdapter(adapter);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                        if(pos==0){
                            tip=items[0];
                        }
                        else{
                            tip=items[1];
                        }
                    }
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                Button btn=(Button) dizajn.findViewById(R.id.button2);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        TextInputEditText nadimak=(TextInputEditText) dizajn.findViewById(R.id.nick);
                        TextInputEditText marka=(TextInputEditText) dizajn.findViewById(R.id.marka);
                        TextInputEditText model=(TextInputEditText) dizajn.findViewById(R.id.model);

                        String nick=nadimak.getText().toString();
                        String mark=marka.getText().toString();
                        String mod=model.getText().toString();

                        String locurl = url + "zav/unosOpreme.php";
                        String type = "opr";
                        BackgroundWorker backgroundWorker = new BackgroundWorker(PopisOpreme.this, 6);
                        backgroundWorker.execute(locurl, type, nick,mark,mod,tip,id);
                        nadimak.setText("");
                        marka.setText("");
                        model.setText("");
                    }
                });
            }
        });
    }


    }
