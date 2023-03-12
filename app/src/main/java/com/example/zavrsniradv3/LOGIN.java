package com.example.zavrsniradv3;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

public class LOGIN extends AppCompatActivity {
    TextInputEditText loginEmail,loginLozinka;
    String email,lozinka;
    public String url="http://192.168.0.187:80/";
    //public String url="https://be1a-95-168-120-65.eu.ngrok.io/";
    Boolean dobar=false;
    public ArrayList<Korisnik>lista;
    Boolean imali=false;
    public int odabrana=0;
    int[] images={R.drawable.avatar,R.drawable.avatar2,R.drawable.avatar3,R.drawable.avatar4,R.drawable.avatar5,R.drawable.avatar6,R.drawable.avatar8,R.drawable.avatar9,R.drawable.avatar10,R.drawable.avatar11,R.drawable.avatar12,R.drawable.avatar13,R.drawable.avatar14,R.drawable.avatar15,R.drawable.avatar16};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lista=new ArrayList<Korisnik>();
        email=lozinka="";
        loginEmail=(TextInputEditText) findViewById(R.id.eEmail);
        loginLozinka=(TextInputEditText) findViewById(R.id.eLozinka);


    }
    String[] names={"Slika 1","Slika 1","Slika 1","Slika 1","Slika 1","Slika 1","Slika 1","Slika 1","Slika 1","Slika 1","Slika 1","Slika 1","Slika 1","Slika 1","Slika 1",};
    public void register(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.AlertDialogStyle);
        View izgled=getLayoutInflater().inflate(R.layout.dialog_registracija,null);

        /*Spinner s=(Spinner)izgled.findViewById(R.id.spinner);
        CustomAdapter adapter=new CustomAdapter(this,names,images);
        s.setAdapter(adapter);
        s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getApplicationContext(),images[i],Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });*/
        GridView g=(GridView)izgled.findViewById(R.id.grid);
        g.setAdapter(new ImageAdapter(this,images));
        g.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(getApplicationContext(),images[i],Toast.LENGTH_SHORT).show();
                CircleImageView odabrani=(CircleImageView) izgled.findViewById(R.id.odabrani);
                odabrani.setImageResource(images[i]);
                odabrana=i;
            }
        });

        builder.setView(izgled);
        builder.show();
        Button btnReg=(Button)izgled.findViewById(R.id.button2);
        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextInputEditText ime=(TextInputEditText) izgled.findViewById(R.id.ime);
                TextInputEditText prezime=(TextInputEditText) izgled.findViewById(R.id.prezime);
                TextInputEditText email=(TextInputEditText) izgled.findViewById(R.id.email);
                TextInputEditText lozinka=(TextInputEditText) izgled.findViewById(R.id.lozinka);
                TextInputEditText info=(TextInputEditText)izgled.findViewById(R.id.bio);
                String name=ime.getText().toString();
                String surname=prezime.getText().toString();
                String mail=email.getText().toString();
                String password=lozinka.getText().toString();
                String opis=info.getText().toString();

                if(ispravnostEmaila(mail)==true){
                    StringRequest request = new StringRequest(url+"zav/dohvatiSve.php", new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONArray array = new JSONArray(response);
                                for (int loop = 0; loop < array.length(); loop++) {
                                    JSONObject object = array.getJSONObject(loop);
                                    lista.add(new Korisnik(object.getInt("id"),
                                            object.getString("ime"),
                                            object.getString("prezime"),
                                            object.getString("email"),
                                            object.getString("lozinka"),
                                            object.getInt("brojPratitelji"),
                                            object.getInt("brojPratim"),
                                            object.getInt("slika"),
                                            object.getString("opis")));
                                }
                                for (Korisnik k:lista) {
                                    if(k.getEmail().equals(mail)){
                                        imali=true;
                                    }
                                }
                                if(imali==false){
                                    String locUrl=url+"zav/insertData.php";
                                    String type = "register";
                                    BackgroundWorker backgroundWorker = new BackgroundWorker(LOGIN.this,1);
                                    backgroundWorker.execute(locUrl,type,name,surname,mail,password,url,odabrana+"",opis);
                                }
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
                    Volley.newRequestQueue(LOGIN.this).add(request);


                }


                ime.setText("");
                prezime.setText("");
                email.setText("");
                lozinka.setText("");
                info.setText("");
            }
        });
    }
    public void login(View view){
       loginEmail.setText("davidlego009@gmail.com");
        loginLozinka.setText("dUUo79GG");
       //loginEmail.setText("dora@gmail.com");
       //loginLozinka.setText("123");
        String email=loginEmail.getText().toString();
        String password=loginLozinka.getText().toString();
        //String url = "http://10.0.2.2:80/validateData.php";
        //String url = "http://192.168.0.187:80/validateData.php";
        // String url="https://d853-95-168-121-0.eu.ngrok.io/validateData.php";
        String locUrl=url+"zav/validateData.php";
        String type = "login";
        BackgroundWorker backgroundWorker = new BackgroundWorker(LOGIN.this,2);
        // backgroundWorker.execute(locUrl,type,"znj","6969",url);
        //backgroundWorker.execute(locUrl,type,"a","b",url);
        backgroundWorker.execute(locUrl,type,email,password,url);
        loginEmail.setText("");
        loginLozinka.setText("");
    }
    public static boolean patternMatches(String emailAddress, String regexPattern) {
        return Pattern.compile(regexPattern)
                .matcher(emailAddress)
                .matches();
    }
    public boolean ispravnostEmaila(String emailAddress) {
        return patternMatches(emailAddress,"^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$");
    }
}