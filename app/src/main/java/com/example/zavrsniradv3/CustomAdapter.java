package com.example.zavrsniradv3;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import de.hdodenhof.circleimageview.CircleImageView;

public class CustomAdapter extends ArrayAdapter<String> {
    Context context;
    String[] names;
    int[] images;

    public CustomAdapter(@NonNull Context context,String[] names,int[] images) {
        super(context, R.layout.spinner_item2,names);
        this.context=context;
        this.names=names;
        this.images = images;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row=inflater.inflate(R.layout.spinner_item2,null);
        CircleImageView i1=(CircleImageView) row.findViewById(R.id.img);
        i1.setImageResource(images[position]);
        return row;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row=inflater.inflate(R.layout.spinner_item2,null);
        CircleImageView i1=(CircleImageView) row.findViewById(R.id.img);
        i1.setImageResource(images[position]);
        return row;
    }
}
