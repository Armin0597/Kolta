package com.koltaapp.kolta;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RevisiTaskAdapter extends RecyclerView.Adapter<RevisiTaskAdapter.MyViewHolder> {

    Context context;
    ArrayList<RevisiTaskItem> revisiTaskItems;

    public RevisiTaskAdapter(Context c, ArrayList<RevisiTaskItem> p){
        context = c;
        revisiTaskItems = p;
    }

    @NonNull
    @Override
    public RevisiTaskAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.list_revisi_task_item, parent, false));
    }


    @Override
    public void onBindViewHolder(@NonNull RevisiTaskAdapter.MyViewHolder holder, int position) {

        holder.xnama_tugas.setText(revisiTaskItems.get(position).getNama_revisi());
        holder.xdeskripsi.setText(revisiTaskItems.get(position).getDeskripsi());
        holder.xdate.setText(revisiTaskItems.get(position).getTanggal_pengumpulan());
        holder.xdate_pertemuan.setText(revisiTaskItems.get(position).getTanggal_pertemuan());

        final String getNamaTugas = revisiTaskItems.get(position).getNama_revisi();
        final String getDeskripsiTugas = revisiTaskItems.get(position).getDeskripsi();
        final String getDate = revisiTaskItems.get(position).getTanggal_pengumpulan();
        final String getDatePertemuan = revisiTaskItems.get(position).getTanggal_pertemuan();
        final String getNamaFile = revisiTaskItems.get(position).getNama_file();
        final String getUrlFile = revisiTaskItems.get(position).getUrl_document();
        final String getUsername = revisiTaskItems.get(position).getUsername();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotodetailbimbingan = new Intent(context, RevisiDetailAct.class);
                gotodetailbimbingan.putExtra("nama_revisi", getNamaTugas);
                gotodetailbimbingan.putExtra("nama_file", getNamaFile);
                gotodetailbimbingan.putExtra("url_document", getUrlFile);
                gotodetailbimbingan.putExtra("deskripsi", getDeskripsiTugas);
                gotodetailbimbingan.putExtra("tanggal", getDate);
                gotodetailbimbingan.putExtra("tanggal_pertemuan", getDatePertemuan);
                gotodetailbimbingan.putExtra("username", getUsername);
                context.startActivity(gotodetailbimbingan);
            }
        });

    }


    @Override
    public int getItemCount() {
        return revisiTaskItems.size();
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
