package com.example.zavrsniradv3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DodajObjavu extends AppCompatActivity {
    public String url="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dodaj_objavu);
        Intent intent=getIntent();
        url=intent.getStringExtra("URL");

        ImageView img=(ImageView) findViewById(R.id.backAkt);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    public void kreirajObjavu(View view){
        Intent intent=getIntent();
        String id=intent.getStringExtra("id");
        String ime=intent.getStringExtra("ime");
        TextInputEditText naslov=(TextInputEditText) findViewById(R.id.naslov);
        TextInputEditText opis=(TextInputEditText) findViewById(R.id.tekst);
        TextInputEditText link=(TextInputEditText) findViewById(R.id.link);
        //TextInputEditText elev=(TextInputEditText) findViewById(R.id.elev);
        //TextInputEditText datum=findViewById(R.id.datum);

        String nas=naslov.getText().toString();
        String op=opis.getText().toString();
        String li=link.getText().toString();
        //String nmv=elev.getText().toString();
        //String dat=datum.getText().toString();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String dat= formatter.format(date).toString();
        // String url = "http://192.168.0.187:80/unosAktivnosti.php";
        String locurl=url+"zav/unosObjave.php";
        String type = "obj";
        BackgroundWorker backgroundWorker = new BackgroundWorker(DodajObjavu.this,4);
        backgroundWorker.execute(locurl,type,dat,nas,op,li,id,ime);
        finish();
    }
}