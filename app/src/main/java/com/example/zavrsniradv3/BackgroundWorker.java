package com.example.zavrsniradv3;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

public class BackgroundWorker extends AsyncTask<String,Void,String> {
    Context context;
    int login;
    String javniURL="",email,password;
    AlertDialog alertDialog;
    String login_url,type,name,surname;
    String naslov,vrijeme,udaljenost,elev,datum,id,ime;
    String datO,naslovO,tekstO,linkO,idO,imeO;
    String idPrati,idPracen;
    String nick,mark,mod,tip;

    String avg,oprema,idc;
    String brprat,brpratim;
    String brlajk,vrsta;
    String idAkt,idOb;
    String startLat,startLong,endLat,endLong;
    public String nazivProf="";
    BackgroundWorker(Context ctx,int log){
        context = ctx;
        login = log;
    }
    @Override
    protected String doInBackground(String... params) {
        if(login==1){
            login_url = params[0];
            type = params[1];
            name = params[2];
            surname = params[3];
            email = params[4];
            password = params[5];
            javniURL=params[6];
        }
        else if(login==2){
            login_url = params[0];
            type = params[1];
            email = params[2];
            password = params[3];
            javniURL=params[4];
        }
        else if(login==3){
            login_url = params[0];
            type = params[1];
            naslov=params[2];
            vrijeme=params[3];
            udaljenost=params[4];
            elev=params[5];
            datum=params[6];
            id=params[7];
            ime=params[8];
            vrsta=params[9];
            avg=params[10];
            oprema=params[11];
        }
        else if(login==4){
            login_url = params[0];
            type = params[1];
            datO=params[2];
            naslovO=params[3];
            tekstO=params[4];
            linkO=params[5];
            idO=params[6];
            imeO=params[7];
        }
        else if(login==5){
            login_url = params[0];
            type = params[1];
            idPrati=params[2];
            idPracen=params[3];
            brprat=params[4];
            brpratim=params[5];
        }
        else if(login==6){   //dodavanje opreme
            login_url = params[0];
            type = params[1];
            nick=params[2];
            mark=params[3];
            mod=params[4];
            tip=params[5];
            idc=params[6];
        }
        else if(login==7){
            login_url = params[0];
            type = params[1];
            id=params[2];
        }
        else if(login==8){ //lajkovi
            login_url = params[0];
            type = params[1];
            idAkt=params[2];
            brlajk=params[3];
            vrsta=params[4];
            id=params[5];
        }
        else if(login==9){
            login_url = params[0];
            type = params[1];
            idAkt=params[2];
            startLat=params[3];
            startLong=params[4];
            endLat=params[5];
            endLong=params[6];
        }
        else if(login==10){
            login_url = params[0];
            type = params[1];
            idOb=params[2];
            idAkt=params[3];
            id=params[4];
            tekstO=params[5];
        }
        try {

            URL url = new URL(login_url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

            String post_data = "";
            if(type.equals("register")) {
                post_data = URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(name, "UTF-8") + "&"
                        + URLEncoder.encode("surname", "UTF-8") + "=" + URLEncoder.encode(surname, "UTF-8")+ "&"
                        + URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8")+ "&"
                        + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");
            }
            else if(type.equals("login")){
                post_data = URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8") + "&"
                        + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");
            }
            else if(type.equals("act")){
                post_data = URLEncoder.encode("title", "UTF-8") + "=" + URLEncoder.encode(naslov, "UTF-8") + "&"
                        + URLEncoder.encode("time", "UTF-8") + "=" + URLEncoder.encode(vrijeme, "UTF-8")+ "&"
                        + URLEncoder.encode("dist", "UTF-8") + "=" + URLEncoder.encode(udaljenost, "UTF-8")+ "&"
                        + URLEncoder.encode("date", "UTF-8") + "=" + URLEncoder.encode(datum, "UTF-8")+ "&"
                        + URLEncoder.encode("elev", "UTF-8") + "=" + URLEncoder.encode(elev, "UTF-8")+ "&"
                        + URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8")+ "&"
                        + URLEncoder.encode("ime", "UTF-8") + "=" + URLEncoder.encode(ime, "UTF-8")+"&"
                + URLEncoder.encode("vrsta", "UTF-8") + "=" + URLEncoder.encode(vrsta, "UTF-8")+"&"
                        + URLEncoder.encode("avg", "UTF-8") + "=" + URLEncoder.encode(avg, "UTF-8")+"&"
                + URLEncoder.encode("oprema", "UTF-8") + "=" + URLEncoder.encode(oprema, "UTF-8");

            }
            else if(type.equals("obj")){
                post_data = URLEncoder.encode("dat", "UTF-8") + "=" + URLEncoder.encode(datO, "UTF-8") + "&"
                        + URLEncoder.encode("title", "UTF-8") + "=" + URLEncoder.encode(naslovO, "UTF-8")+ "&"
                        + URLEncoder.encode("opis", "UTF-8") + "=" + URLEncoder.encode(tekstO, "UTF-8")+ "&"
                        + URLEncoder.encode("link", "UTF-8") + "=" + URLEncoder.encode(linkO, "UTF-8")+ "&"
                        + URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(idO, "UTF-8")+ "&"
                        + URLEncoder.encode("ime", "UTF-8") + "=" + URLEncoder.encode(imeO, "UTF-8");

            }
            else if(type.equals("odn")) {
                post_data = URLEncoder.encode("idPrati", "UTF-8") + "=" + URLEncoder.encode(idPrati, "UTF-8") + "&"
                        + URLEncoder.encode("idPracen", "UTF-8") + "=" + URLEncoder.encode(idPracen, "UTF-8")+"&"
                + URLEncoder.encode("br", "UTF-8") + "=" + URLEncoder.encode(brprat, "UTF-8")+"&"
                + URLEncoder.encode("brp", "UTF-8") + "=" + URLEncoder.encode(brpratim, "UTF-8");
            }
            else if(type.equals("opr")) {
                post_data = URLEncoder.encode("nick", "UTF-8") + "=" + URLEncoder.encode(nick, "UTF-8") + "&"
                        + URLEncoder.encode("mark", "UTF-8") + "=" + URLEncoder.encode(mark, "UTF-8") + "&"
                + URLEncoder.encode("mod", "UTF-8") + "=" + URLEncoder.encode(mod, "UTF-8") + "&"
                + URLEncoder.encode("tip", "UTF-8") + "=" + URLEncoder.encode(tip, "UTF-8")+"&"
                + URLEncoder.encode("idc", "UTF-8") + "=" + URLEncoder.encode(idc, "UTF-8");
            }
            else if(type.equals("uklOp")) {
                post_data = URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8");
            }
            else if(type.equals("lajk")) {
                post_data = URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(idAkt, "UTF-8") + "&"
                        + URLEncoder.encode("brl", "UTF-8") + "=" + URLEncoder.encode(brlajk, "UTF-8")+"&"
                        + URLEncoder.encode("idUsera", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8")+"&"
                        + URLEncoder.encode("vr", "UTF-8") + "=" + URLEncoder.encode(vrsta, "UTF-8");
            }else if(type.equals("rut")){
                post_data = URLEncoder.encode("idAkt", "UTF-8") + "=" + URLEncoder.encode(idAkt, "UTF-8") + "&"
                        + URLEncoder.encode("startLat", "UTF-8") + "=" + URLEncoder.encode(startLat, "UTF-8")+ "&"
                        + URLEncoder.encode("startLong", "UTF-8") + "=" + URLEncoder.encode(startLong, "UTF-8")+ "&"
                        + URLEncoder.encode("endLat", "UTF-8") + "=" + URLEncoder.encode(endLat, "UTF-8")+ "&"
                        + URLEncoder.encode("endLong", "UTF-8") + "=" + URLEncoder.encode(endLong, "UTF-8");
            }
            else if(type.equals("kom")){
                post_data = URLEncoder.encode("idOb", "UTF-8") + "=" + URLEncoder.encode(idOb, "UTF-8") + "&"
                        + URLEncoder.encode("idAkt", "UTF-8") + "=" + URLEncoder.encode(idAkt, "UTF-8")+ "&"
                        + URLEncoder.encode("idUsera", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8")+ "&"
                        + URLEncoder.encode("tekst", "UTF-8") + "=" + URLEncoder.encode(tekstO, "UTF-8");
            }
            bufferedWriter.write(post_data);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
            String result = "";
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                result += line;
            }
            bufferedReader.close();
            inputStream.close();
            httpURLConnection.disconnect();
            return result;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPreExecute() {
        alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("Login Status");
    }
    @Override
    protected void onPostExecute(String result) {
        //alertDialog.setMessage("aha");
        //alertDialog.show();

        Intent intent=new Intent(context.getApplicationContext(),HOME.class);
        if(result.contains("Login Successful..Welcome ")){
            intent.putExtra("EMAIL",email);
            Log.d("email2",email);
            intent.putExtra("PASS",password);
            intent.putExtra("URL",javniURL);
            context.startActivity(intent);
        }
    }
    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}
