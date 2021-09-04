package com.youthfimodd.elenges.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.youthfimodd.elenges.R;

public class SliderAdapter extends PagerAdapter {

    private Context context;
    Typeface typeface1;

    public SliderAdapter(Context context) {
        this.context = context;

    }

    private int[] slide_image={
            R.mipmap.s1,
            R.mipmap.s2,
            R.mipmap.s3,
            R.mipmap.s4,
    };

    private int [] slide_title={
            R.string.first_slide_title,
            R.string.second_slide_title,
            R.string.third_slide_title,
            R.string.fourth_slide_title,

    };

    private int [] slide_desc={
            R.string.first_slide_desc,
            R.string.second_slide_desc,
            R.string.third_slide_desc,
            R.string.fourth_slide_desc,
    };
    @Override
    public int getCount() {
        return slide_title.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==(RelativeLayout)object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        LayoutInflater inflater=(LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view= inflater.inflate(R.layout.slides_layout,container,false);

        ImageView img_banner = view.findViewById(R.id.slider_image);
        TextView tv_title = view.findViewById(R.id.kombo_title);
        TextView tv_desc = view.findViewById(R.id.kombo_desc);

        img_banner.setImageResource(slide_image[position]);
        tv_title.setText(slide_title[position]);
        tv_desc.setText(slide_desc[position]);

        container.addView(view);

        return  view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout)object);
    }
}
