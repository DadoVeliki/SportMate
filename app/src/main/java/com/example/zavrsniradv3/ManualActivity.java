package com.example.zavrsniradv3;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class ManualActivity extends AppCompatActivity {
    public String url="",tip="",selOp="",idU="",tipAkt="";
    public ArrayList<Oprema>listaOpreme;
    public ArrayList<String>pop;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual);
        pop=new ArrayList<String>();
        ImageView img=(ImageView) findViewById(R.id.backAkt);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        Intent i=getIntent();
        url=i.getStringExtra("URL");
        idU=i.getStringExtra("id");
        listaOpreme=i.getParcelableArrayListExtra("listOp");
        TextView datum=(TextView) findViewById(R.id.datum);
        datum.setInputType(InputType.TYPE_NULL);
        datum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateTimePicker(datum);
            }
        });
        TextView vrijeme=(TextView)findViewById(R.id.vrijeme);
        vrijeme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ManualActivity.this,R.style.AlertDialogStyle2);
                View izgled=getLayoutInflater().inflate(R.layout.timepicker,null);
                TimePicker timePicker=(TimePicker)izgled.findViewById(R.id.timePicker1);
                timePicker.setIs24HourView(true);
                timePicker.setHour(0);
                timePicker.setMinute(0);
                builder.setView(izgled);

                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        int vure,minute;
                        vure=timePicker.getHour();
                        minute=timePicker.getMinute();
                        vrijeme.setText(vure+":"+minute);
                    }
                });
                builder.setNegativeButton("Odustani", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });

                builder.show();
            }
        });


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
                    Spinner spinner2 = (Spinner) findViewById(R.id.spinOprema);
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

                    Spinner spinner = (Spinner) findViewById(R.id.spinnerTip);
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
                catch (Exception e) {
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        Volley.newRequestQueue(this).add(request5);
    }
    public void dateTimePicker(TextView datum){
        final Calendar calendar=Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR,year);
                calendar.set(Calendar.MONTH,month);
                calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);

                TimePickerDialog.OnTimeSetListener timeSetListener=new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
                        calendar.set(Calendar.MINUTE,minute);

                        SimpleDateFormat s=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        datum.setText(s.format(calendar.getTime()));

                    }
                };
                new TimePickerDialog(ManualActivity.this,timeSetListener,calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),true).show();
            }
        };
        new DatePickerDialog(ManualActivity.this,dateSetListener,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
    }
    public void kreirajAktivnost(View view){

        Intent intent=getIntent();
        String id=intent.getStringExtra("id"),ime=intent.getStringExtra("ime");
        url=intent.getStringExtra("URL");
        TextInputEditText naslov=(TextInputEditText) findViewById(R.id.naslov);
        TextView vrijeme=(TextView) findViewById(R.id.vrijeme);
        TextInputEditText udaljenost=(TextInputEditText) findViewById(R.id.udalj);
        TextInputEditText elev=(TextInputEditText) findViewById(R.id.elev);
        TextView datum=(TextView) findViewById(R.id.datum);

        String nas=naslov.getText().toString(),vri=vrijeme.getText().toString(),dist=udaljenost.getText().toString(),nmv=elev.getText().toString(),dat=datum.getText().toString();
        String locurl=url+"zav/unosAktivnosti.php",type = "act",vrsta="man",oprema=selOp;

        BackgroundWorker backgroundWorker = new BackgroundWorker(ManualActivity.this,3);
        TextView prazno=(TextView) findViewById(R.id.prazno);
        if(nas.equals("") || vri.equals("") || dist.equals("") || nmv.equals("") || dat.equals("") || oprema.equals("") || tipAkt.equals("")){
            prazno.setText("Potrebno ispuniti sva polja!");
        }
        else{
            prazno.setText("");
            backgroundWorker.execute(locurl,type,nas,vri,dist,nmv,dat,id,ime,vrsta,"0",oprema,tipAkt);
            finish();
        }


    }
}