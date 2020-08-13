package com.koltaapp.kolta;

public class ListJadwalTeacherItem {
    String nama_kegiatan, tanggal_kegiatan;

    public ListJadwalTeacherItem() {
    }

    public ListJadwalTeacherItem(String nama_kegiatan, String tanggal_kegiatan) {
        this.nama_kegiatan = nama_kegiatan;
        this.tanggal_kegiatan = tanggal_kegiatan;
    }

    public String getNama_kegiatan() {
        return nama_kegiatan;
    }

    public void setNama_kegiatan(String nama_kegiatan) {
        this.nama_kegiatan = nama_kegiatan;
    }

    public String getTanggal_kegiatan() {
        return tanggal_kegiatan;
    }

    public void setTanggal_kegiatan(String tanggal_kegiatan) {
        this.tanggal_kegiatan = tanggal_kegiatan;
    }
}
