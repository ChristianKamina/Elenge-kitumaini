package com.youthfimodd.elenges.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.youthfimodd.elenges.R;

public class MagAdapterFlipper extends BaseAdapter {

    private Context context;
    private int[] image;
    private String[] name;

    public MagAdapterFlipper(Context context, int[] image, String[] name) {
        this.context = context;
        this.image = image;
        this.name = name;
    }

    @Override
    public int getCount() {
        return name.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        view = LayoutInflater.from(context).inflate(R.layout.filpper_image_mag, null);

        ImageView imageView = view.findViewById(R.id.images_flipper);
        TextView textView = view.findViewById(R.id.name_flipper);
        textView.setText(name[position]);
        imageView.setImageResource(Integer.parseInt(String.valueOf(image[position])));

        return view;
    }
}
