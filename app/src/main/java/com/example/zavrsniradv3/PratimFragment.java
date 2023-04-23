package com.example.zavrsniradv3;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.ArrayList;
import de.hdodenhof.circleimageview.CircleImageView;

public class PratimFragment extends Fragment {
    int[]images;
    public ArrayList<Integer>pratitelji;
    int br2;
    @SuppressLint({"SetTextI18n", "ResourceType"})
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View fragPratim=inflater.inflate(R.layout.fragment_pratim, container, false);
        assert this.getArguments() != null;
        String id=this.getArguments().getString("id");
        String url=this.getArguments().getString("URL");
        ArrayList<Integer>pratim=new ArrayList<>();
        ArrayList<Odnos> listOd=this.getArguments().getParcelableArrayList("listaOdnosa");
        ArrayList<Korisnik>listUs=this.getArguments().getParcelableArrayList("lista");
        LinearLayout l=fragPratim.findViewById(R.id.ll4);
        images=this.getArguments().getIntArray("images");
        br2=this.getArguments().getInt("br2");
        pratitelji=new ArrayList<>();
        for(Odnos o:listOd){
            try{
                if(o.getIdKojiPrati()==Integer.parseInt(id)){
                    pratim.add(o.getIdPracen());
                }
            }
            catch(NumberFormatException n){n.printStackTrace();
            }
        }
        for(Korisnik k:listUs){
            pratitelji.add(k.getBrojPratitelji());
        }
        for(int pr:pratim){
            @SuppressLint("InflateParams") View us = getLayoutInflater().inflate(R.layout.prikaz_usera, null);
            TextView name = us.findViewById(R.id.ime);
            TextView desc =us.findViewById(R.id.op);
            name.setText(listUs.get(pr-1).getIme()+" "+listUs.get(pr-1).getPrezime());
            desc.setText(listUs.get(pr-1).getOpis());
            CircleImageView profile=us.findViewById(R.id.profile_image);
            profile.setImageResource(images[listUs.get(pr-1).getSlika()]);

            Button btn = us.findViewById(R.id.btn);
            btn.setId(pr);
            btn.setContentDescription("0");
            btn.setText("PRATIM");
            btn.setBackgroundColor(Color.WHITE);
            btn.setTextColor(Color.parseColor("#FF5722"));
            btn.setContentDescription("1");
            btn.setOnClickListener(view -> {
                int con = Integer.parseInt(btn.getContentDescription().toString());
                if (con == 0) {
                    MojeMetode.zaprati(btn);
                    pratitelji.set(btn.getId() - 1, pratitelji.get(btn.getId() - 1) + 1);
                    String idOsoba = btn.getId() + "";
                    String locurl = url + "zav/unosOdnos.php";
                    String type = "odn";

                    br2++;
                    BackgroundWorker backgroundWorker = new BackgroundWorker(getContext().getApplicationContext(), 5);
                    backgroundWorker.execute(locurl, type, id, idOsoba, pratitelji.get(btn.getId() - 1) + "", br2 + "");
                } else {
                    btn.setText("PRATI");
                    btn.setTextColor(Color.WHITE);
                    btn.setBackgroundColor(Color.parseColor("#FF5722"));
                    btn.setContentDescription("" + 0);

                    pratitelji.set(btn.getId() - 1, pratitelji.get(btn.getId() - 1) - 1);

                    String idOsoba = btn.getId() + "";
                    String locurl = url + "zav/ukloniOdnos.php";
                    String type = "odn";

                    br2--;
                    BackgroundWorker backgroundWorker = new BackgroundWorker(getContext().getApplicationContext(), 5);
                    backgroundWorker.execute(locurl, type, id, idOsoba, pratitelji.get(btn.getId() - 1) + "", br2 + "");
                }
            });
            LinearLayout p =us.findViewById(R.id.parent);
            l.addView(p);
        }

        return fragPratim;
    }
}