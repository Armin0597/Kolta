package com.koltaapp.kolta;

public class ListRevisiStudentItem {
    String username, nama_revisi,nama_file,url_document,deskripsi,tanggal_pengumpulan,tanggal_pertemuan;

    public ListRevisiStudentItem() {
    }

    public ListRevisiStudentItem(String username, String nama_revisi, String nama_file, String url_document, String deskripsi, String tanggal_pengumpulan, String tanggal_pertemuan) {
        this.username = username;
        this.nama_revisi = nama_revisi;
        this.nama_file = nama_file;
        this.url_document = url_document;
        this.deskripsi = deskripsi;
        this.tanggal_pengumpulan = tanggal_pengumpulan;
        this.tanggal_pertemuan = tanggal_pertemuan;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNama_revisi() {
        return nama_revisi;
    }

    public void setNama_revisi(String nama_revisi) {
        this.nama_revisi = nama_revisi;
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

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getTanggal_pengumpulan() {
        return tanggal_pengumpulan;
    }

    public void setTanggal_pengumpulan(String tanggal_pengumpulan) {
        this.tanggal_pengumpulan = tanggal_pengumpulan;
    }

    public String getTanggal_pertemuan() {
        return tanggal_pertemuan;
    }

    public void setTanggal_pertemuan(String tanggal_pertemuan) {
        this.tanggal_pertemuan = tanggal_pertemuan;
    }
}
