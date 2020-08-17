package com.koltaapp.kolta;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RevisiMainAdapter extends RecyclerView.Adapter<RevisiMainAdapter.MyViewHolder> {

    Context context;
    ArrayList<RevisiMainItem> revisiMainItems;

    public RevisiMainAdapter(Context c, ArrayList<RevisiMainItem> p){
        context = c;
        revisiMainItems = p;
    }


    @NonNull
    @Override
    public RevisiMainAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.list_revisi_main_item, parent, false));
    }


    @Override
    public void onBindViewHolder(@NonNull RevisiMainAdapter.MyViewHolder holder, int position) {
        Picasso.with(holder.xurl_photo_profile.getContext())
                .load(revisiMainItems.get(position).getUrl_photo_profile()).centerCrop().fit()
                .into(holder.xurl_photo_profile);
        holder.xnama.setText(revisiMainItems.get(position).getNama());
        holder.xnim.setText(revisiMainItems.get(position).getNim());

        final String getImageMahasiswa = revisiMainItems.get(position).getUrl_photo_profile();
        final String getNamaMahasiswa = revisiMainItems.get(position).getNama();
        final String getNimMahasiswa = revisiMainItems.get(position).getNim();
        final String getProdiMahasiswa = revisiMainItems.get(position).getProdi();
        final String getEmailMahasiswa = revisiMainItems.get(position).getEmail_address();
        final String getUsernameMahasiswa = revisiMainItems.get(position).getUsername();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotobimbinganmhs = new Intent(context, RevisiTaskAct.class);
                gotobimbinganmhs.putExtra("url_photo_profile", getImageMahasiswa);
                gotobimbinganmhs.putExtra("nama", getNamaMahasiswa);
                gotobimbinganmhs.putExtra("nim", getNimMahasiswa);
                gotobimbinganmhs.putExtra("prodi", getProdiMahasiswa);
                gotobimbinganmhs.putExtra("email_address", getEmailMahasiswa);
                gotobimbinganmhs.putExtra("username", getUsernameMahasiswa);
                context.startActivity(gotobimbinganmhs);
            }
        });
    }


    @Override
    public int getItemCount() {
        return revisiMainItems.size();
    }

    class  MyViewHolder extends RecyclerView.ViewHolder{

        ImageView xurl_photo_profile;
        TextView xnama,xnim;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            xurl_photo_profile = itemView.findViewById(R.id.xurl_photo_profile);
            xnama = itemView.findViewById(R.id.xnama);
            xnim = itemView.findViewById(R.id.xnim);
        }
    }

}
