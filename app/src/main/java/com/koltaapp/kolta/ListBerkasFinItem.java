package com.koltaapp.kolta;

public class ListBerkasFinItem {
    String mhs_id,nama_file,url_document;

    public ListBerkasFinItem() {
    }

    public ListBerkasFinItem(String mhs_id, String nama_file, String url_document) {
        this.mhs_id = mhs_id;
        this.nama_file = nama_file;
        this.url_document = url_document;
    }

    public String getMhs_id() {
        return mhs_id;
    }

    public void setMhs_id(String mhs_id) {
        this.mhs_id = mhs_id;
    }

    public String getNama_file() {
        return nama_file;
    }

    public void setNama_file(String nama_file) {
        this.nama_file = nama_file;
    }

    public String getUrl_document() {
        return url_document;
    }

    public void setUrl_document(String url_document) {
        this.url_document = url_document;
    }
}
