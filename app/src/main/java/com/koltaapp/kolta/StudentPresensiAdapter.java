package com.koltaapp.kolta;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class StudentPresensiAdapter extends RecyclerView.Adapter<StudentPresensiAdapter.MyViewHolder> {

    Context context;
    ArrayList<StudentPresensiItem> studentPresensiItems;

    public StudentPresensiAdapter(Context c, ArrayList<StudentPresensiItem> p){
        context = c;
        studentPresensiItems = p;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_student_presensi_main,parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.xkegiatan.setText(studentPresensiItems.get(position).getKegiatan());
        holder.xdate.setText(studentPresensiItems.get(position).getTanggal());

    }

    @Override
    public int getItemCount() {
        return studentPresensiItems.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView xkegiatan,xdate;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            xkegiatan = itemView.findViewById(R.id.xkegiatan);
            xdate = itemView.findViewById(R.id.xdate);

        }
    }
}
