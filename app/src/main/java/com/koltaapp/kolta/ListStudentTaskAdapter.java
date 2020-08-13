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

public class ListStudentTaskAdapter extends RecyclerView.Adapter<ListStudentTaskAdapter.MyViewHolder> {

    Context context;
    ArrayList<ListStudentTask> listStudentTask;

    public ListStudentTaskAdapter(Context c, ArrayList<ListStudentTask> p){
        context = c;
        listStudentTask = p;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.list_student_task, parent, false));
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Picasso.with(holder.xurl_photo_profile.getContext())
                .load(listStudentTask.get(position).getUrl_photo_profile()).centerCrop().fit()
                .into(holder.xurl_photo_profile);
        holder.xnama.setText(listStudentTask.get(position).getNama());
        holder.xnim.setText(listStudentTask.get(position).getNim());

        final String getImageMahasiswa = listStudentTask.get(position).getUrl_photo_profile();
        final String getNamaMahasiswa = listStudentTask.get(position).getNama();
        final String getNimMahasiswa = listStudentTask.get(position).getNim();
        final String getProdiMahasiswa = listStudentTask.get(position).getProdi();
        final String getEmailMahasiswa = listStudentTask.get(position).getEmail_address();
        final String getUsernameMahasiswa = listStudentTask.get(position).getUsername();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotobimbinganmhs = new Intent(context, BimbinganStudentAct.class);
                gotobimbinganmhs.putExtra("url_photo_profile", getImageMahasiswa);
                gotobimbinganmhs.putExtra("nama", getNamaMahasiswa);
                gotobimbinganmhs.putExtra("nim", getNimMahasiswa);
                gotobimbinganmhs.putExtra("prodi", getProdiMahasiswa);
                gotobimbinganmhs.putExtra("email_address", getEmailMahasiswa);
                gotobimbinganmhs.putExtra("username", getUsernameMahasiswa);
                context.startActivity(gotobimbinganmhs);
            }
        });
    }


    @Override
    public int getItemCount() {
        return listStudentTask.size();
    }

    class  MyViewHolder extends RecyclerView.ViewHolder{

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
