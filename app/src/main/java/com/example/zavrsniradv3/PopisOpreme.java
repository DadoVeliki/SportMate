package com.example.zavrsniradv3;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Objects;

public class PopisOpreme extends AppCompatActivity {
    public String url="",tip="",id;
    public ArrayList<Oprema>listaOpreme;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oprema);
        findViewById(R.id.backOp).setOnClickListener(view -> finish());
        Intent intent=getIntent();
        url=intent.getStringExtra("URL");
        id=intent.getStringExtra("id");
        listaOpreme=new ArrayList<>();
        String id=intent.getStringExtra("id");
        LinearLayout l=findViewById(R.id.linearBikes);

        //dohvaćanje sve opreme
        @SuppressLint("SetTextI18n") StringRequest request5 = new StringRequest(url+"zav/dohvatiOpremu.php", response5 -> {
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
                        @SuppressLint("InflateParams") View op=getLayoutInflater().inflate(R.layout.prikaz_opreme,null);
                        TextView nick=op.findViewById(R.id.nick);
                        TextView name=op.findViewById(R.id.name);
                        ImageView vrsta=op.findViewById(R.id.vrsta);

                        nick.setText(o.getNadimak());
                        name.setText(o.getMarka()+" "+o.getModel());
                        if(o.getTip().equals("Bicikl")){
                            vrsta.setImageResource(R.drawable.bajk2);
                        }
                        else if(o.getTip().equals("Tenisice")){
                            vrsta.setImageResource(R.drawable.patike);
                        }
                        LinearLayout p=op.findViewById(R.id.parent);
                        l.addView(p);
                        ImageView remove= op.findViewById(R.id.remove);
                        remove.setOnClickListener(view -> {
                            String locUrl=url+"zav/ukloniOpremu.php";
                            String type = "uklOp";
                            BackgroundWorker backgroundWorker = new BackgroundWorker(PopisOpreme.this,7);
                            backgroundWorker.execute(locUrl,type,o.getId()+"");
                        });
                }
                }
                //sendList();
            }
            catch (Exception e) {e.printStackTrace();
            }
        }, error -> {
        });
        Volley.newRequestQueue(this).add(request5);

        ImageView dodavanje= findViewById(R.id.add);
        dodavanje.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(PopisOpreme.this, R.style.AlertDialogStyle);
            View dizajn = getLayoutInflater().inflate(R.layout.dodavanje_opreme, null);
            builder.setView(dizajn);
            builder.show();
            String[] items=new String[2];
            items[0]="Bicikl";
            items[1]="Tenisice";
            Spinner spinner = dizajn.findViewById(R.id.spinner);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(PopisOpreme.this,
                    android.R.layout.simple_spinner_dropdown_item, items);
            spinner.setAdapter(adapter);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> parent, View view, int pos, long id1) {
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
            Button btn=dizajn.findViewById(R.id.button2);
            btn.setOnClickListener(view1 -> {
                TextInputEditText nadimak=dizajn.findViewById(R.id.nick);
                TextInputEditText marka=dizajn.findViewById(R.id.marka);
                TextInputEditText model= dizajn.findViewById(R.id.model);

                String nick= Objects.requireNonNull(nadimak.getText()).toString();
                String mark= Objects.requireNonNull(marka.getText()).toString();
                String mod= Objects.requireNonNull(model.getText()).toString();

                String locurl = url + "zav/unosOpreme.php";
                String type = "opr";
                BackgroundWorker backgroundWorker = new BackgroundWorker(PopisOpreme.this, 6);
                backgroundWorker.execute(locurl, type, nick,mark,mod,tip,id);
                nadimak.setText("");
                marka.setText("");
                model.setText("");
            });
        });
    }


    }
