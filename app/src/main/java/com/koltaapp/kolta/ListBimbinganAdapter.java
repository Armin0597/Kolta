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

public class ListBimbinganAdapter extends RecyclerView.Adapter<ListBimbinganAdapter.MyViewHolder> {

    Context context;
    ArrayList<ListBimbinganItem> listBimbinganItems;

    public ListBimbinganAdapter(Context c, ArrayList<ListBimbinganItem> p){
        context = c;
        listBimbinganItems = p;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.list_bimbingan_item, parent, false));
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.xnama_tugas.setText(listBimbinganItems.get(position).getNama_tugas());
        holder.xdeskripsi.setText(listBimbinganItems.get(position).getDeskripsi());
        holder.xdate.setText(listBimbinganItems.get(position).getTanggal_pengumpulan());
        holder.xdate_pertemuan.setText(listBimbinganItems.get(position).getTanggal_pertemuan());

        final String getNamaTugas = listBimbinganItems.get(position).getNama_tugas();
        final String getDeskripsiTugas = listBimbinganItems.get(position).getDeskripsi();
        final String getDate = listBimbinganItems.get(position).getTanggal_pengumpulan();
        final String getDate_pertemuan = listBimbinganItems.get(position).getTanggal_pertemuan();
        final String getUsername = listBimbinganItems.get(position).getUsername();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotodetailbimbingan = new Intent(context, TaskDetailAct.class);
                gotodetailbimbingan.putExtra("nama_tugas", getNamaTugas);
                gotodetailbimbingan.putExtra("deskripsi", getDeskripsiTugas);
                gotodetailbimbingan.putExtra("tanggal", getDate);
                gotodetailbimbingan.putExtra("tanggal_pertemuan", getDate_pertemuan);
                gotodetailbimbingan.putExtra("username", getUsername);
                context.startActivity(gotodetailbimbingan);
            }
        });

    }


    @Override
    public int getItemCount() {
        return listBimbinganItems.size();
    }

    class  MyViewHolder extends RecyclerView.ViewHolder{

        TextView xnama_tugas,xdeskripsi,xdate,xdate_pertemuan;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            xnama_tugas = itemView.findViewById(R.id.xnama_tugas);
            xdeskripsi = itemView.findViewById(R.id.xdeskripsi);
            xdate = itemView.findViewById(R.id.xdate);
            xdate_pertemuan = itemView.findViewById(R.id.xdate_pertemuan);
        }
    }

}
