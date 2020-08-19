package com.koltaapp.kolta;

public class StudentPresensiItem {
    String username,kegiatan,tanggal_pengumpulan,tanggal_pertemuan;

    public StudentPresensiItem() {
    }

    public StudentPresensiItem(String username, String kegiatan, String tanggal_pengumpulan, String tanggal_pertemuan) {
        this.username = username;
        this.kegiatan = kegiatan;
        this.tanggal_pengumpulan = tanggal_pengumpulan;
        this.tanggal_pertemuan = tanggal_pertemuan;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getKegiatan() {
        return kegiatan;
    }

    public void setKegiatan(String kegiatan) {
        this.kegiatan = kegiatan;
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
