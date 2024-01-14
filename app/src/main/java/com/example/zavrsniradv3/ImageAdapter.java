package com.example.zavrsniradv3;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import de.hdodenhof.circleimageview.CircleImageView;

public class ImageAdapter extends BaseAdapter {
    private final Context c;
    private final int[] images;
    public ImageAdapter(Context c,int[]images) {
        this.c = c;
        this.images=images;
    }
    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position,View view,ViewGroup parent) {
        CircleImageView imageView;
        if(view ==null){
            imageView=new CircleImageView(c);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(150,150));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(16,16,16,16);
        }
        else{
            imageView=(CircleImageView) view;
        }
        imageView.setImageResource(images[position]);
        return imageView;
    }
}