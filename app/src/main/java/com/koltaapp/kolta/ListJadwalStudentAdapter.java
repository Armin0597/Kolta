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

public class ListJadwalStudentAdapter extends RecyclerView.Adapter<ListJadwalStudentAdapter.MyViewHolder> {

    Context context;
    ArrayList<ListJadwalStudentItem> listJadwalStudentItems;

    public ListJadwalStudentAdapter(Context c, ArrayList<ListJadwalStudentItem> p){
        context = c;
        listJadwalStudentItems = p;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.list_jadwal_student_item,parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.xnama.setText(listJadwalStudentItems.get(position).getNama_kegiatan());
        holder.xtanggal.setText(listJadwalStudentItems.get(position).getTanggal_kegiatan());

    }

    @Override
    public int getItemCount() {
        return listJadwalStudentItems.size();
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
