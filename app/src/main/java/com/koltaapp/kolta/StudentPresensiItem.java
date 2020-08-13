package com.koltaapp.kolta;

public class StudentPresensiItem {
    String username,kegiatan,tanggal;

    public StudentPresensiItem() {
    }

    public StudentPresensiItem(String username, String kegiatan, String tanggal) {
        this.username = username;
        this.kegiatan = kegiatan;
        this.tanggal = tanggal;
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

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }
}
