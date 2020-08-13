package com.koltaapp.kolta;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ListDraftBimbinganAdapter extends RecyclerView.Adapter<ListDraftBimbinganAdapter.MyViewHolder> {

    Context context;
    ArrayList<ListDrafBimbinganItem> listDrafBimbinganItems;

    public ListDraftBimbinganAdapter(Context c, ArrayList<ListDrafBimbinganItem> p){
        context = c;
        listDrafBimbinganItems = p;
    }

    @NonNull
    @Override
    public ListDraftBimbinganAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.list_draf_bimbingan_item,parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ListDraftBimbinganAdapter.MyViewHolder holder, int position) {
        holder.xnama_file.setText(listDrafBimbinganItems.get(position).getNama_file());

    }

    @Override
    public int getItemCount() {
        return listDrafBimbinganItems.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView xnama_file;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            xnama_file = itemView.findViewById(R.id.xnama_file);

        }
    }
}

