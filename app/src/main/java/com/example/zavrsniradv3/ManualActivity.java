package com.example.zavrsniradv3;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import org.json.JSONArray;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

public class ManualActivity extends AppCompatActivity {
    public String url="",tip="",selOp="",idU="",tipAkt="";
    public ArrayList<Oprema>listaOpreme;
    public ArrayList<String>pop;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual);
        pop=new ArrayList<>();
        ImageView img=findViewById(R.id.backAkt);
        img.setOnClickListener(view -> finish());
        Intent i=getIntent();
        url=i.getStringExtra("URL");
        idU=i.getStringExtra("id");
        listaOpreme=i.getParcelableArrayListExtra("listOp");
        TextView datum=findViewById(R.id.datum);
        datum.setInputType(InputType.TYPE_NULL);
        datum.setOnClickListener(view -> dateTimePicker(datum));
        TextView vrijeme=findViewById(R.id.vrijeme);
        vrijeme.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(ManualActivity.this,R.style.AlertDialogStyle3);
            View izgled=getLayoutInflater().inflate(R.layout.timepicker,null);
            TimePicker timePicker=izgled.findViewById(R.id.timePicker1);
            timePicker.setIs24HourView(true);
            timePicker.setHour(0);
            timePicker.setMinute(0);
            builder.setView(izgled);

            builder.setPositiveButton("Ok", (dialog, id) -> {
                int vure,minute;
                vure=timePicker.getHour();
                minute=timePicker.getMinute();
                vrijeme.setText(vure+":"+minute);
            });
            builder.setNegativeButton("Odustani", (dialog, id) -> {
            });
            builder.show();
        });
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
                Spinner spinner2 = findViewById(R.id.spinOprema);
                ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(ManualActivity.this,
                        android.R.layout.simple_spinner_item, pop){
                    @Override
                    public boolean isEnabled(int position){
                        return position != 0;
                    }
                };
                adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner2.setAdapter(adapter2);
                spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                        selOp=pop.get(pos);
                    }
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
                //ODABIR TIPA AKTIVNOSTI
                String[] items = new String[4];
                items[0]="Tip Aktivnosti";
                items[1]="Trčanje";
                items[2]="Šetnja";
                items[3]="Biciklizam";

                Spinner spinner = findViewById(R.id.spinnerTip);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(ManualActivity.this,
                        android.R.layout.simple_spinner_dropdown_item, items){
                    @Override
                    public boolean isEnabled(int position){
                        return position != 0;
                    }
                };
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);

                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                        tipAkt=items[pos];
                        tip=items[pos];
                        if(tip.equals("Biciklizam")){
                            tip="Bicikl";
                        }
                        else if(tip.equals("Trčanje") || tip.equals("Šetnja")){
                            tip="Tenisice";
                        }
                        pop.clear();
                        pop.add("Oprema:");
                        for(Oprema o:listaOpreme){
                            idU=i.getStringExtra("id");
                            if(o.getIdCije()==Integer.parseInt(idU)){
                                if(o.getTip().equals(tip)){
                                    pop.add(o.getNadimak());
                                    adapter2.notifyDataSetChanged();
                                }
                            }
                        }
                    }
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
            }
            catch (Exception e) {e.printStackTrace();
            }
        }, error -> {
        });
        Volley.newRequestQueue(this).add(request5);
    }
    public void dateTimePicker(TextView datum){
        final Calendar calendar=Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener= (datePicker, year, month, dayOfMonth) -> {
            calendar.set(Calendar.YEAR,year);
            calendar.set(Calendar.MONTH,month);
            calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);

            TimePickerDialog.OnTimeSetListener timeSetListener= (timePicker, hourOfDay, minute) -> {
                calendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
                calendar.set(Calendar.MINUTE,minute);

                @SuppressLint("SimpleDateFormat") SimpleDateFormat s=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                datum.setText(s.format(calendar.getTime()));

            };
            new TimePickerDialog(ManualActivity.this,timeSetListener,calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),true).show();
        };
        new DatePickerDialog(ManualActivity.this,dateSetListener,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
    }
    public void kreirajAktivnost(View view){
        Intent intent=getIntent();
        String id=intent.getStringExtra("id"),ime=intent.getStringExtra("ime");
        url=intent.getStringExtra("URL");
        TextInputEditText naslov=findViewById(R.id.naslov);
        TextView vrijeme=findViewById(R.id.vrijeme);
        TextInputEditText udaljenost=findViewById(R.id.udalj);
        TextInputEditText elev=findViewById(R.id.elev);
        TextView datum=findViewById(R.id.datum);

        String nas= Objects.requireNonNull(naslov.getText()).toString(),vri=vrijeme.getText().toString(),dist= Objects.requireNonNull(udaljenost.getText()).toString(),nmv= Objects.requireNonNull(elev.getText()).toString(),dat=datum.getText().toString();
        String locurl=url+"zav/unosAktivnosti.php",type = "act",vrsta="man",oprema=selOp;

        BackgroundWorker backgroundWorker = new BackgroundWorker(ManualActivity.this,3);
        TextView prazno=findViewById(R.id.prazno);
        if(nas.equals("") || vri.equals("") || dist.equals("") || nmv.equals("") || dat.equals("") || tipAkt.equals("")){
            prazno.setText("Potrebno ispuniti sva polja!");
        }
        else{
            prazno.setText("");
            backgroundWorker.execute(locurl,type,nas,vri,dist,nmv,dat,id,ime,vrsta,"0",oprema,tipAkt);
            finish();
        }


    }
}