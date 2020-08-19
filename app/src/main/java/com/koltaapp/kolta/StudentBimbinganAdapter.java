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

public class StudentBimbinganAdapter extends RecyclerView.Adapter<StudentBimbinganAdapter.MyViewHolder> {

    Context context;
    ArrayList<StudentBimbinganItem> studentBimbinganItems;

    public StudentBimbinganAdapter(Context c, ArrayList<StudentBimbinganItem> p){
        context = c;
        studentBimbinganItems = p;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.list_student_bimbingan, parent, false));
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.xnama_tugas.setText(studentBimbinganItems.get(position).getNama_tugas());
        holder.xdeskripsi.setText(studentBimbinganItems.get(position).getDeskripsi());
        holder.xdate.setText(studentBimbinganItems.get(position).getTanggal_pengumpulan());
        holder.xdate_pertemuan.setText(studentBimbinganItems.get(position).getTanggal_pertemuan());

        final String getNamaTugas = studentBimbinganItems.get(position).getNama_tugas();
        final String getNamaFile = studentBimbinganItems.get(position).getNama_file();
        final String getUrlDocument = studentBimbinganItems.get(position).getUrl_document();
        final String getDeskripsiTugas = studentBimbinganItems.get(position).getDeskripsi();
        final String getDate = studentBimbinganItems.get(position).getTanggal_pengumpulan();
        final String getDatePertemuan = studentBimbinganItems.get(position).getTanggal_pertemuan();
        final String getUsername = studentBimbinganItems.get(position).getUsername();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotodetailbimbingan = new Intent(context, StudentBimbinganDetailAct.class);
                gotodetailbimbingan.putExtra("nama_tugas", getNamaTugas);
                gotodetailbimbingan.putExtra("nama_file", getNamaFile);
                gotodetailbimbingan.putExtra("url_document", getUrlDocument);
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
        return studentBimbinganItems.size();
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
