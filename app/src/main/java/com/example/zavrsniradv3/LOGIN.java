package com.example.zavrsniradv3;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Objects;
import java.util.regex.Pattern;
import de.hdodenhof.circleimageview.CircleImageView;

public class LOGIN extends AppCompatActivity {
    TextInputEditText loginEmail,loginLozinka;
    String email,lozinka;
    public String url="http://192.168.0.187:80/";
    //public String url="https://c3fb-95-168-121-49.ngrok-free.app/";
    public ArrayList<Korisnik>lista;
    boolean imali=false;
    public int odabrana=0;
    int[] images={R.drawable.avatar,R.drawable.avatar2,R.drawable.avatar3,R.drawable.avatar4,R.drawable.avatar5,R.drawable.avatar6,R.drawable.avatar8,R.drawable.avatar9,R.drawable.avatar10,R.drawable.avatar11,R.drawable.avatar12,R.drawable.avatar13,R.drawable.avatar14,R.drawable.avatar15,R.drawable.avatar16};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lista=new ArrayList<>();
        email=lozinka="";
        loginEmail=findViewById(R.id.eEmail);
        loginLozinka=findViewById(R.id.eLozinka);


    }
    public void register(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.AlertDialogStyle);
        View izgled=getLayoutInflater().inflate(R.layout.dialog_registracija,null);
        GridView g=izgled.findViewById(R.id.grid);
        g.setAdapter(new ImageAdapter(this,images));
        g.setOnItemClickListener((adapterView, view1, i, l) -> {
            CircleImageView odabrani=izgled.findViewById(R.id.odabrani);
            odabrani.setImageResource(images[i]);
            odabrana=i;
        });

        builder.setView(izgled);
        builder.show();
        MaterialButton btnReg=izgled.findViewById(R.id.button2);
        btnReg.setOnClickListener(view12 -> {
            TextInputEditText ime=izgled.findViewById(R.id.ime);
            TextInputEditText prezime=izgled.findViewById(R.id.prezime);
            TextInputEditText email=izgled.findViewById(R.id.email);
            TextInputEditText lozinka=izgled.findViewById(R.id.lozinka);
            TextInputEditText info=izgled.findViewById(R.id.bio);
            String name= Objects.requireNonNull(ime.getText()).toString();
            String surname= Objects.requireNonNull(prezime.getText()).toString();
            String mail= Objects.requireNonNull(email.getText()).toString();
            String password= Objects.requireNonNull(lozinka.getText()).toString();
            String opis= Objects.requireNonNull(info.getText()).toString();

            if(ispravnostEmaila(mail)){
                StringRequest request = new StringRequest(url+"zav/dohvatiSve.php", response -> {
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
                            if (k.getEmail().equals(mail)) {
                                imali = true;
                                break;
                            }
                        }
                        if(!imali){
                            String locUrl=url+"zav/insertData.php";
                            String type = "register";
                            BackgroundWorker backgroundWorker = new BackgroundWorker(LOGIN.this,1);
                            backgroundWorker.execute(locUrl,type,name,surname,mail,password,url,odabrana+"",opis);
                        }
                    }
                    catch (Exception e) {e.printStackTrace();
                    }
                }, error -> {
                });
                Volley.newRequestQueue(LOGIN.this).add(request);
            }
            ime.setText("");
            prezime.setText("");
            email.setText("");
            lozinka.setText("");
            info.setText("");
        });
    }
    public void login(View view){
        String email= Objects.requireNonNull(loginEmail.getText()).toString();
        String password= Objects.requireNonNull(loginLozinka.getText()).toString();

        String locUrl=url+"zav/validateData.php";
        String type = "login";
        BackgroundWorker backgroundWorker = new BackgroundWorker(LOGIN.this,2);
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