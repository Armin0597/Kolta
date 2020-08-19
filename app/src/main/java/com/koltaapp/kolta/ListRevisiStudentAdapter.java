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

public class ListRevisiStudentAdapter extends RecyclerView.Adapter<ListRevisiStudentAdapter.MyViewHolder> {

    Context context;
    ArrayList<ListRevisiStudentItem> listRevisiStudentItems;

    public ListRevisiStudentAdapter(Context c, ArrayList<ListRevisiStudentItem> p){
        context = c;
        listRevisiStudentItems = p;
    }

    @NonNull
    @Override
    public ListRevisiStudentAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.list_revisi_student_item, parent, false));
    }


    @Override
    public void onBindViewHolder(@NonNull ListRevisiStudentAdapter.MyViewHolder holder, int position) {

        holder.xnama_tugas.setText(listRevisiStudentItems.get(position).getNama_revisi());
        holder.xdeskripsi.setText(listRevisiStudentItems.get(position).getDeskripsi());
        holder.xdate.setText(listRevisiStudentItems.get(position).getTanggal_pengumpulan());
        holder.xdate_pertemuan.setText(listRevisiStudentItems.get(position).getTanggal_pertemuan());

        final String getNamaTugas = listRevisiStudentItems.get(position).getNama_revisi();
        final String getDeskripsiTugas = listRevisiStudentItems.get(position).getDeskripsi();
        final String getDate = listRevisiStudentItems.get(position).getTanggal_pengumpulan();
        final String getDatePertemuan = listRevisiStudentItems.get(position).getTanggal_pertemuan();
        final String getUsername = listRevisiStudentItems.get(position).getUsername();
        final String getNamaFile = listRevisiStudentItems.get(position).getNama_file();
        final String getUrlDocument = listRevisiStudentItems.get(position).getUrl_document();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotodetailbimbingan = new Intent(context, StudentRevisiDetailAct.class);
                gotodetailbimbingan.putExtra("nama_revisi", getNamaTugas);
                gotodetailbimbingan.putExtra("deskripsi", getDeskripsiTugas);
                gotodetailbimbingan.putExtra("tanggal", getDate);
                gotodetailbimbingan.putExtra("tanggal_pertemuan", getDatePertemuan);
                gotodetailbimbingan.putExtra("nama_file", getNamaFile);
                gotodetailbimbingan.putExtra("url_document", getUrlDocument);
                gotodetailbimbingan.putExtra("username", getUsername);
                context.startActivity(gotodetailbimbingan);
            }
        });

    }


    @Override
    public int getItemCount() {
        return listRevisiStudentItems.size();
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
