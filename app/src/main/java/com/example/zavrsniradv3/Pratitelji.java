package com.example.zavrsniradv3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class Pratitelji extends AppCompatActivity {
    public String url="",id="";
    public ArrayList<Korisnik>listUs;
    public ArrayList<Odnos>listOd;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    int[]images;
    int br2,prvo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pratitelji);
        ImageView img=(ImageView) findViewById(R.id.backAkt);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        Intent i=getIntent();
        prvo=i.getIntExtra("prvo",0);
        br2=i.getIntExtra("br2",0);
        id=i.getStringExtra("id");
        url=i.getStringExtra("URL");
        listUs=i.getParcelableArrayListExtra("lista");
        listOd=i.getParcelableArrayListExtra("listaOdnosa");
        images=i.getIntArrayExtra("images");
        tabLayout=findViewById(R.id.tablayout);
        viewPager=findViewById(R.id.viewpage);

        tabLayout.setupWithViewPager(viewPager);
        VPAdapter vpAdapter=new VPAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

        Bundle bundle=new Bundle();
        bundle.putString("id",id);
        bundle.putString("URL",url);
        bundle.putParcelableArrayList("lista",listUs);
        bundle.putParcelableArrayList("listaOdnosa",listOd);
        bundle.putIntArray("images",images);
        bundle.putInt("br2",br2);
        PratiteljiFragment p=new PratiteljiFragment();
        p.setArguments(bundle);

        Bundle bundle1=new Bundle();
        bundle1.putString("id",id);
        bundle1.putString("URL",url);
        bundle1.putParcelableArrayList("lista",listUs);
        bundle1.putParcelableArrayList("listaOdnosa",listOd);
        bundle1.putIntArray("images",images);
        bundle1.putInt("br2",br2);
        PratimFragment p1=new PratimFragment();
        p1.setArguments(bundle1);

        if(prvo==0){
            vpAdapter.addFragment(p,"PRATITELJI");
            vpAdapter.addFragment(p1,"PRATIM");
        }
        else{
            vpAdapter.addFragment(p1,"PRATIM");
            vpAdapter.addFragment(p,"PRATITELJI");

        }
        viewPager.setAdapter(vpAdapter);
    }
}