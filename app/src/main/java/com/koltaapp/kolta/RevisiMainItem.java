package com.koltaapp.kolta;

public class RevisiMainItem {
    String nama,nim,url_photo_profile,prodi,email_address,username;

    public RevisiMainItem() {
    }

    public RevisiMainItem(String nama, String nim, String url_photo_profile, String prodi, String email_address, String username) {
        this.nama = nama;
        this.nim = nim;
        this.url_photo_profile = url_photo_profile;
        this.prodi = prodi;
        this.email_address = email_address;
        this.username = username;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getNim() {
        return nim;
    }

    public void setNim(String nim) {
        this.nim = nim;
    }

    public String getUrl_photo_profile() {
        return url_photo_profile;
    }

    public void setUrl_photo_profile(String url_photo_profile) {
        this.url_photo_profile = url_photo_profile;
    }

    public String getProdi() {
        return prodi;
    }

    public void setProdi(String prodi) {
        this.prodi = prodi;
    }

    public String getEmail_address() {
        return email_address;
    }

    public void setEmail_address(String email_address) {
        this.email_address = email_address;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
