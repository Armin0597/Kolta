package com.koltaapp.kolta;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ListDraftStudentAdapter extends RecyclerView.Adapter<ListDraftStudentAdapter.MyViewHolder> {

    Context context;
    ArrayList<ListDraftStudentItem> listDraftStudentItems;

    public ListDraftStudentAdapter(Context c, ArrayList<ListDraftStudentItem> p){
        context = c;
        listDraftStudentItems = p;
    }

    @NonNull
    @Override
    public ListDraftStudentAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.list_draft_student_item,parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ListDraftStudentAdapter.MyViewHolder holder, int position) {
        holder.xnama_file.setText(listDraftStudentItems.get(position).getNama_file());

    }

    @Override
    public int getItemCount() {
        return listDraftStudentItems.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView xnama_file;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            xnama_file = itemView.findViewById(R.id.xnama_file);

        }
    }
}
