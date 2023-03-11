package com.example.zavrsniradv3;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class PratiteljiFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        String id=this.getArguments().getString("id");
        String url=this.getArguments().getString("URL");

        View fragprat=inflater.inflate(R.layout.fragment_pratitelji, container, false);
        ArrayList<Odnos> listOd=this.getArguments().getParcelableArrayList("listaOdnosa");
        ArrayList<Korisnik>listUs=this.getArguments().getParcelableArrayList("lista");
        LinearLayout l=(LinearLayout) fragprat.findViewById(R.id.ll3);
        ArrayList<Integer>praceni=new ArrayList<Integer>();
        ArrayList<Integer>pratim=new ArrayList<Integer>();
        for(Odnos o:listOd){
            try{
                if(o.getIdPracen()==Integer.parseInt(id)){
                    praceni.add(o.getIdKojiPrati());
                }
            }
            catch(NumberFormatException n){
                Log.e("nije broj: ",id+"");
            }
        }
        for(Odnos o:listOd){
            try{
                if(o.getIdKojiPrati()==Integer.parseInt(id)){
                    pratim.add(o.getIdPracen());
                }
            }
            catch(NumberFormatException n){
                Log.e("nije broj: ",id+"");
            }
        }
        int brojac=1;
        for(int pr:praceni){
                View us = getLayoutInflater().inflate(R.layout.prikaz_usera, null);
                LinearLayout p = (LinearLayout) us.findViewById(R.id.parent);
                TextView name = (TextView) us.findViewById(R.id.ime);
                TextView desc = (TextView) us.findViewById(R.id.op);
                name.setText(listUs.get(pr-1).getIme()+" "+listUs.get(pr-1).getPrezime());
                desc.setText("Neka drzava");

                Button btn = (Button) us.findViewById(R.id.btn);
                btn.setId(pr);
                btn.setContentDescription("0");
                for (int a = 0; a < pratim.size(); a++) {
                    if (btn.getId() == pratim.get(a)) {
                        btn.setText("PRATIM");
                        btn.setBackgroundColor(Color.WHITE);
                        btn.setTextColor(Color.parseColor("#FF5722"));
                        btn.setContentDescription("1");
                    }
                }

                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d("INDEX: ", btn.getContentDescription() + "");
                        int con = Integer.parseInt(btn.getContentDescription().toString());
                        if (con == 0) {
                            btn.setText("PRATIM");
                            btn.setBackgroundColor(Color.WHITE);
                            btn.setTextColor(Color.parseColor("#FF5722"));
                            btn.setContentDescription("1");
                            String idOsoba = btn.getId()  + "";
                            String locurl = url + "zav/unosOdnos.php";
                            String type = "odn";
                            BackgroundWorker backgroundWorker = new BackgroundWorker(getContext(), 5);
                            backgroundWorker.execute(locurl, type, id, idOsoba);
                        } else {
                            btn.setText("PRATI");
                            btn.setTextColor(Color.WHITE);
                            btn.setBackgroundColor(Color.parseColor("#FF5722"));
                            btn.setContentDescription("0");
                            String idOsoba = btn.getId() + "";
                            String locurl = url + "zav/ukloniOdnos.php";
                            String type = "odn";
                            BackgroundWorker backgroundWorker = new BackgroundWorker(getContext(), 5);
                            backgroundWorker.execute(locurl, type, id, idOsoba);
                        }

                    }
                });
                l.addView(p);
        }

        return fragprat;
    }
}