package com.example.zavrsniradv3;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;
import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import java.util.ArrayList;

public class Komentari extends AppCompatActivity {
    public String url="",idU="",idAkt="0",idOb="0";
    public ArrayList<Comment>listKom;
    public ArrayList<Korisnik>lista;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_komentari);
        listKom=new ArrayList<Comment>();
        ImageView img=(ImageView) findViewById(R.id.backAkt);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        Intent i=getIntent();
        url=i.getStringExtra("URL");
        idU=i.getStringExtra("idU");
        idAkt=i.getStringExtra("idAkt");
        idOb=i.getStringExtra("idOb");
        lista=i.getParcelableArrayListExtra("lista");

        ImageView obKom=(ImageView)findViewById(R.id.obKom);
        obKom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText unos=(EditText) findViewById(R.id.editText1);
                String locUrl=url+"zav/unosKom.php";
                String type = "kom";
                BackgroundWorker backgroundWorker = new BackgroundWorker(Komentari.this,10);
                backgroundWorker.execute(locUrl,type,idOb,idAkt,idU,unos.getText().toString());
                unos.setText("");
            }
        });

        LinearLayout l=(LinearLayout) findViewById(R.id.ll1);
        StringRequest request = new StringRequest(url+"zav/dohvatiKom.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray array = new JSONArray(response);
                    for (int loop = 0; loop < array.length(); loop++) {
                        JSONObject object = array.getJSONObject(loop);
                        listKom.add(new Comment(object.getInt("id"),
                                object.getInt("idOb"),
                                object.getInt("idAkt"),
                                object.getInt("idUsera"),
                                object.getString("tekst")));
                    }

                    for(Comment c:listKom){
                        Log.d("hello",idAkt+"=="+c.getIdAkt());
                        if((c.getIdAkt()==Integer.parseInt(idAkt)) ){
                            for(Korisnik k:lista){
                                if(k.getId()==c.getIdUsera()){
                                    View com=getLayoutInflater().inflate(R.layout.dizajn_kom,null);
                                    TextView ime=(TextView)com.findViewById(R.id.name);
                                    TextView tekst=(TextView)com.findViewById(R.id.tekst);

                                    ime.setText(k.getIme()+" "+k.getPrezime());
                                    tekst.setText(c.getTekst());
                                    LinearLayout p=(LinearLayout)com.findViewById(R.id.parent);
                                    l.addView(p);
                                }
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
        Volley.newRequestQueue(Komentari.this).add(request);
    }
}