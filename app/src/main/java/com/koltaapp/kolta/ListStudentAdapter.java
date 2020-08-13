package com.koltaapp.kolta;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ListStudentAdapter extends RecyclerView.Adapter<ListStudentAdapter.MyViewHolder> {

    Context context;
    ArrayList<ListStudent> listStudents;

    public ListStudentAdapter(Context c, ArrayList<ListStudent> p){
        context = c;
        listStudents = p;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.list_student_item,parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Picasso.with(holder.xurl_photo_profile.getContext())
                .load(listStudents.get(position).getUrl_photo_profile()).centerCrop().fit()
                .into(holder.xurl_photo_profile);
        holder.xnama.setText(listStudents.get(position).getNama());
        holder.xnim.setText(listStudents.get(position).getNim());

        final String getImageMahasiswa = listStudents.get(position).getUrl_photo_profile();
        final String getNamaMahasiswa = listStudents.get(position).getNama();
        final String getNimMahasiswa = listStudents.get(position).getNim();
        final String getProdiMahasiswa = listStudents.get(position).getProdi();
        final String getEmailMahasiswa = listStudents.get(position).getEmail_address();
        final String getUsernameMahasiswa = listStudents.get(position).getUsername();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotomahasiswadetail = new Intent(context, StudentDetailActivity.class);
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
        return listStudents.size();
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
