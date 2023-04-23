package com.example.zavrsniradv3;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class DodajObjavu extends AppCompatActivity {
    public String url="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dodaj_objavu);
        Intent intent=getIntent();
        url=intent.getStringExtra("URL");

        ImageView img=findViewById(R.id.backAkt);
        img.setOnClickListener(view -> finish());
    }
    public void kreirajObjavu(View view){
        Intent intent=getIntent();
        String id=intent.getStringExtra("id");
        String ime=intent.getStringExtra("ime");
        TextInputEditText naslov=findViewById(R.id.naslov);
        TextInputEditText opis=findViewById(R.id.tekst);
        TextInputEditText link=findViewById(R.id.link);

        String nas= Objects.requireNonNull(naslov.getText()).toString();
        String op= Objects.requireNonNull(opis.getText()).toString();
        String li= Objects.requireNonNull(link.getText()).toString();

        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String dat= formatter.format(date);
        String locurl=url+"zav/unosObjave.php";
        String type = "obj";
        BackgroundWorker backgroundWorker = new BackgroundWorker(DodajObjavu.this,4);
        TextView prazno=findViewById(R.id.prazno);
        if(nas.equals("")){
            prazno.setText("Potrebno napisati naslov!");
        }
        else{
            prazno.setText("");
            backgroundWorker.execute(locurl,type,dat,nas,op,li,id,ime);
            finish();
        }
    }
}