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

public class ListKehadiranAdapter extends RecyclerView.Adapter<ListKehadiranAdapter.MyViewHolder> {

    Context context;
    ArrayList<ListKehadiranItem> listKehadiranItems;

    public ListKehadiranAdapter(Context c, ArrayList<ListKehadiranItem> p){
        context = c;
        listKehadiranItems = p;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.list_student_kehandiran,parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Picasso.with(holder.xurl_photo_profile.getContext())
                .load(listKehadiranItems.get(position).getUrl_photo_profile()).centerCrop().fit()
                .into(holder.xurl_photo_profile);
        holder.xnama.setText(listKehadiranItems.get(position).getNama());
        holder.xnim.setText(listKehadiranItems.get(position).getNim());

        final String getImageMahasiswa = listKehadiranItems.get(position).getUrl_photo_profile();
        final String getNamaMahasiswa = listKehadiranItems.get(position).getNama();
        final String getNimMahasiswa = listKehadiranItems.get(position).getNim();
        final String getProdiMahasiswa = listKehadiranItems.get(position).getProdi();
        final String getEmailMahasiswa = listKehadiranItems.get(position).getEmail_address();
        final String getUsernameMahasiswa = listKehadiranItems.get(position).getUsername();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotokehadiranmhs = new Intent(context, PresensiStudentAct.class);
                gotokehadiranmhs.putExtra("url_photo_profile", getImageMahasiswa);
                gotokehadiranmhs.putExtra("nama", getNamaMahasiswa);
                gotokehadiranmhs.putExtra("nim", getNimMahasiswa);
                gotokehadiranmhs.putExtra("prodi", getProdiMahasiswa);
                gotokehadiranmhs.putExtra("email_address", getEmailMahasiswa);
                gotokehadiranmhs.putExtra("username", getUsernameMahasiswa);
                context.startActivity(gotokehadiranmhs);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listKehadiranItems.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

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

