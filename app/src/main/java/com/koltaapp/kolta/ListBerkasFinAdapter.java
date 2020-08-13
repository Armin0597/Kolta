package com.koltaapp.kolta;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ListBerkasFinAdapter extends RecyclerView.Adapter<ListBerkasFinAdapter.MyViewHolder> {

    Context context;
    ArrayList<ListBerkasFinItem> listBerkasFinItems;

    public ListBerkasFinAdapter(Context c, ArrayList<ListBerkasFinItem> p){
        context = c;
        listBerkasFinItems = p;
    }

    @NonNull
    @Override
    public ListBerkasFinAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.list_berkasfin_item,parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ListBerkasFinAdapter.MyViewHolder holder, int position) {
        holder.xnama_file.setText(listBerkasFinItems.get(position).getNama_file());

    }

    @Override
    public int getItemCount() {
        return listBerkasFinItems.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView xnama_file;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            xnama_file = itemView.findViewById(R.id.xnama_file);

        }
    }
}

