package com.youthfimodd.elenges.custom.forum_models;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseUser;
import com.youthfimodd.elenges.R;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class CmntAdapter extends RecyclerView.Adapter<CmntAdapter.ViewHolder> {
    //
    private static final int MSG_TITLE_LRFT = 0;

    private Context mContext;
    private List<Cmnt> mCmnt;
    //private String Imageurl;

    FirebaseUser fuser;

    public CmntAdapter(Context mContext, List<Cmnt> mCmnt /*, String imageurl*/) {
        //
        this.mContext = mContext;
        this.mCmnt = mCmnt;
        // Imageurl = imageurl;
    }

    @NonNull
    @Override
    public CmntAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //
        View mView = LayoutInflater.from(mContext).inflate(R.layout.cmnt_item, parent, false);
        return new CmntAdapter.ViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull final CmntAdapter.ViewHolder holder, int position) {
        //
        Picasso.with(mContext).load(mCmnt.get(position).getUserimg()).into(holder.profil_users);
        holder.username.setText(mCmnt.get(position).getUsername());
        holder.show_comnt.setText(mCmnt.get(position).getComment());
        //problem pour recuperer le time
        //holder.CmntTimes.setText(timestampstoString((Long) mCmnt.get(position).getTimestamps()));

        //Clock pour see datz
        holder.show_comnt.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                holder.CmntTimes.setVisibility(View.VISIBLE);

                Toast.makeText(mContext, " DATE IS VISIBLE", Toast.LENGTH_SHORT).show();

                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCmnt.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        //
        TextView show_comnt, username, CmntTimes;
        ImageView profil_users;
        TextView cmnt_see;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            show_comnt = itemView.findViewById(R.id.Cmnt_txt);
            username = itemView.findViewById(R.id.Cmnt_username);
            CmntTimes = itemView.findViewById(R.id.cmnt_times);
            profil_users = itemView.findViewById(R.id.Cmnt_profil_image);
            cmnt_see = itemView.findViewById(R.id.cmnt_see);
        }

    }
    //time et date
    private String timestampstoString(Long timestamps){
        //
        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(timestamps);
        String date = DateFormat.format("dd-MM-yyyy", calendar).toString();
        return date;
    }
}
