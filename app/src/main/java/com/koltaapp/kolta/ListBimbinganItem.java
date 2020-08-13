package com.koltaapp.kolta;

import androidx.recyclerview.widget.RecyclerView;

public class ListBimbinganItem {

    String username, nama_tugas,deskripsi,tanggal_pengumpulan,tanggal_pertemuan;

    public ListBimbinganItem() {
    }

    public ListBimbinganItem(String username, String nama_tugas, String deskripsi, String tanggal_pengumpulan, String tanggal_pertemuan) {
        this.username = username;
        this.nama_tugas = nama_tugas;
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

    public String getNama_tugas() {
        return nama_tugas;
    }

    public void setNama_tugas(String nama_tugas) {
        this.nama_tugas = nama_tugas;
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
