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

import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.util.ArrayList;

public class ListRevisiDrafTeacherAdapter extends RecyclerView.Adapter<ListRevisiDrafTeacherAdapter.MyViewHolder> {

    Context context;
    ArrayList<ListRevisiDrafTeacherItem> listRevisiDrafTeacherItems;

    public ListRevisiDrafTeacherAdapter(Context c, ArrayList<ListRevisiDrafTeacherItem> p) {
        context = c;
        listRevisiDrafTeacherItems = p;
    }

    @NonNull
    @Override
    public ListRevisiDrafTeacherAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.list_revisi_draf_teacher_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ListRevisiDrafTeacherAdapter.MyViewHolder holder, final int position) {
        holder.xnama_file.setText(listRevisiDrafTeacherItems.get(position).getNama_file());
        final String getUrlDocument = listRevisiDrafTeacherItems.get(position).getUrl_document();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadFile(holder.xnama_file.getContext(),listRevisiDrafTeacherItems.get(position).getNama_file(),
                        Environment.getExternalStorageState(new File(Environment.DIRECTORY_DOWNLOADS)),listRevisiDrafTeacherItems.get(position).getUrl_document());
            }
        });
    }

    @Override
    public int getItemCount() {
        return listRevisiDrafTeacherItems.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

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
        request.setDestinationInExternalFilesDir(context, destinationDirectory, fileName);

        return downloadManager.enqueue(request);
    }
}

