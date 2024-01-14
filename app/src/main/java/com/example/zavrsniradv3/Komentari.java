package com.example.zavrsniradv3;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import de.hdodenhof.circleimageview.CircleImageView;

public class Komentari extends AppCompatActivity {
    public String url="",idU="",idAkt="0",idOb="0";
    public ArrayList<Comment>listKom;
    public ArrayList<Korisnik>lista;
    int[]images;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_komentari);
        listKom=new ArrayList<>();
        ImageView img=findViewById(R.id.backAkt);
        img.setOnClickListener(view -> finish());
        Intent i=getIntent();
        url=i.getStringExtra("URL");
        idU=i.getStringExtra("idU");
        idAkt=i.getStringExtra("idAkt");
        idOb=i.getStringExtra("idOb");
        lista=i.getParcelableArrayListExtra("lista");
        images=i.getIntArrayExtra("images");
        ImageView obKom=findViewById(R.id.obKom);
        obKom.setOnClickListener(view -> {
            EditText unos=findViewById(R.id.editText1);
            String locUrl=url+"zav/unosKom.php";
            String type = "kom";
            BackgroundWorker backgroundWorker = new BackgroundWorker(Komentari.this,10);
            backgroundWorker.execute(locUrl,type,idOb,idAkt,idU,unos.getText().toString());
            unos.setText("");
        });

        LinearLayout l=findViewById(R.id.ll1);
        @SuppressLint("SetTextI18n") StringRequest request = new StringRequest(url+"zav/dohvatiKom.php", response -> {
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
                                @SuppressLint("InflateParams") View com=getLayoutInflater().inflate(R.layout.dizajn_kom,null);
                                TextView ime=com.findViewById(R.id.name);
                                TextView tekst=com.findViewById(R.id.tekst);
                                CircleImageView profimg=com.findViewById(R.id.profile_image);

                                ime.setText(k.getIme()+" "+k.getPrezime());
                                tekst.setText(c.getTekst());
                                profimg.setImageResource(images[lista.get(c.getIdUsera()-1).getSlika()]);
                                LinearLayout p=com.findViewById(R.id.parent);
                                l.addView(p);
                            }
                        }
                    }
                }
            }
            catch (Exception e) {e.printStackTrace();
            }
        }, error -> {
        });
        Volley.newRequestQueue(Komentari.this).add(request);
    }
}