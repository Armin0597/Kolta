package com.koltaapp.kolta;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ListRevisiDraftStudentAdapter extends RecyclerView.Adapter<ListRevisiDraftStudentAdapter.MyViewHolder> {

    Context context;
    ArrayList<ListRevisiDraftStudentItem> listRevisiDraftStudentItems;

    public ListRevisiDraftStudentAdapter(Context c, ArrayList<ListRevisiDraftStudentItem> p){
        context = c;
        listRevisiDraftStudentItems = p;
    }

    @NonNull
    @Override
    public ListRevisiDraftStudentAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.list_revisi_draf_item,parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ListRevisiDraftStudentAdapter.MyViewHolder holder, int position) {
        holder.xnama_file.setText(listRevisiDraftStudentItems.get(position).getNama_file());

    }

    @Override
    public int getItemCount() {
        return listRevisiDraftStudentItems.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView xnama_file;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            xnama_file = itemView.findViewById(R.id.xnama_file);

        }
    }
}