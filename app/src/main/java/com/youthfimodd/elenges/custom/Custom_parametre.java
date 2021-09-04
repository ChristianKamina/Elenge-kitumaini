package com.youthfimodd.elenges.custom;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.youthfimodd.elenges.R;

public class Custom_parametre extends ArrayAdapter<String> {
    //
    private String[] tname;
    private Integer[] imgid;
    private Activity context;

    public Custom_parametre(Activity context, String[] tname, Integer[] imgid) {
        super(context, R.layout.custom_apropos, tname);
        //
        this.context = context;
        this.tname = tname;
        this.imgid = imgid;
    }
    //

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View r = convertView;
        viewHolderP viewHolder = null;

        if (r==null)
        {
            LayoutInflater layoutInflater = context.getLayoutInflater();
            r=layoutInflater.inflate(R.layout.custom_apropos, null, true);
            viewHolder = new viewHolderP(r);
            r.setTag(viewHolder);
        }
        else {
            viewHolder = (viewHolderP) r.getTag();

        }
        viewHolder.ivw.setImageResource(imgid[position]);
        viewHolder.tvw1.setText(tname[position]);

        return r;
    }
    //
    static class viewHolderP {
        TextView tvw1;
        ImageView ivw;
        //
        viewHolderP(View v)
        {
            tvw1 = v.findViewById(R.id.find_param);
            ivw = v.findViewById(R.id.logoP);

        }

    }
}
