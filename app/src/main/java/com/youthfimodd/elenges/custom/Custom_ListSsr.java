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

public class Custom_ListSsr extends ArrayAdapter<String> {
    //
    private String[] tname;
    private String[] desc;
    private Integer[] imgid;
    private Activity context;

    public Custom_ListSsr(Activity context, String[] tname, Integer[] imgid, String[] desc) {
        super(context, R.layout.custom_ssr, tname);
        //
        this.context = context;
        this.tname = tname;
        this.desc = desc;
        this.imgid = imgid;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View r = convertView;
        viewHolder viewHolder = null;
        //
        if (r==null)
        {
            LayoutInflater layoutInflater = context.getLayoutInflater();
            r=layoutInflater.inflate(R.layout.custom_ssr, null, true);
            viewHolder = new viewHolder(r);
            r.setTag(viewHolder);
        }
        else {
            viewHolder = (viewHolder) r.getTag();

        }
        viewHolder.ivw.setImageResource(imgid[position]);
        viewHolder.tvw1.setText(tname[position]);
        viewHolder.txw2.setText(desc[position]);

        return r;
    }
    //
    static class viewHolder
    {
        TextView tvw1;
        TextView txw2;
        ImageView ivw;
        //
        viewHolder(View v)
        {
            tvw1 = (TextView) v.findViewById(R.id.find_name);
            txw2 = v.findViewById(R.id.find_desc);
            ivw = v.findViewById(R.id.logo);

        }

    }
}
