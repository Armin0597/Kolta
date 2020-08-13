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

public class ListDosenAdapter extends RecyclerView.Adapter<ListDosenAdapter.MyViewHolder> {

    Context context;
    ArrayList<ListDosenItem> listDosenItems;

    public ListDosenAdapter(Context c, ArrayList<ListDosenItem> p){
        context = c;
        listDosenItems = p;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.list_dosen_item,parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Picasso.with(holder.xurl_photo_profile.getContext())
                .load(listDosenItems.get(position).getUrl_photo_profile()).centerCrop().fit()
                .into(holder.xurl_photo_profile);
        holder.xnama.setText(listDosenItems.get(position).getNama());
        holder.xnim.setText(listDosenItems.get(position).getNip());

        final String getImageMahasiswa = listDosenItems.get(position).getUrl_photo_profile();
        final String getNamaMahasiswa = listDosenItems.get(position).getNama();
        final String getNimMahasiswa = listDosenItems.get(position).getNip();
        final String getProdiMahasiswa = listDosenItems.get(position).getProdi();
        final String getEmailMahasiswa = listDosenItems.get(position).getEmail_address();
        final String getUsernameMahasiswa = listDosenItems.get(position).getUsername();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotomahasiswadetail = new Intent(context, DosenDetailAct.class);
                gotomahasiswadetail.putExtra("url_photo_profile", getImageMahasiswa);
                gotomahasiswadetail.putExtra("nama", getNamaMahasiswa);
                gotomahasiswadetail.putExtra("nim", getNimMahasiswa);
                gotomahasiswadetail.putExtra("prodi", getProdiMahasiswa);
                gotomahasiswadetail.putExtra("email_address", getEmailMahasiswa);
                gotomahasiswadetail.putExtra("username", getUsernameMahasiswa);
                context.startActivity(gotomahasiswadetail);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listDosenItems.size();
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
