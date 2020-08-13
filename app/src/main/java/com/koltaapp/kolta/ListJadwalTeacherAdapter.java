package com.koltaapp.kolta;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ListJadwalTeacherAdapter extends RecyclerView.Adapter<ListJadwalTeacherAdapter.MyViewHolder> {

    Context context;
    ArrayList<ListJadwalTeacherItem> listJadwalTeacherItems;

    public ListJadwalTeacherAdapter(Context c, ArrayList<ListJadwalTeacherItem> p){
        context = c;
        listJadwalTeacherItems = p;
    }

    @NonNull
    @Override
    public ListJadwalTeacherAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.list_jadwal_teacher_item,parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ListJadwalTeacherAdapter.MyViewHolder holder, int position) {
        holder.xnama.setText(listJadwalTeacherItems.get(position).getNama_kegiatan());
        holder.xtanggal.setText(listJadwalTeacherItems.get(position).getTanggal_kegiatan());

    }

    @Override
    public int getItemCount() {
        return listJadwalTeacherItems.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView xnama,xtanggal;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            xnama = itemView.findViewById(R.id.xnama);
            xtanggal = itemView.findViewById(R.id.xtanggal);

        }
    }
}
