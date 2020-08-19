package com.koltaapp.kolta;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
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
    public void onBindViewHolder(@NonNull final ListDraftBimbinganAdapter.MyViewHolder holder, final int position) {
        holder.xnama_file.setText(listDrafBimbinganItems.get(position).getNama_file());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadFile(holder.xnama_file.getContext(),listDrafBimbinganItems.get(position).getNama_file(),
                        Environment.getExternalStorageState(new File(Environment.DIRECTORY_DOWNLOADS)),listDrafBimbinganItems.get(position).getUrl_document());
            }
        });

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

    public long downloadFile(Context context, String fileName, String destinationDirectory, String url) {
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setMimeType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(context, destinationDirectory, fileName );

        return downloadManager.enqueue(request);
    }
}

