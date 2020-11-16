package com.example.pelaporanbakesbangpolmalang.model;

public class LaporanItem {
    private int idLaporan;
    private int idUser;
    private String judul;
    private String description;
    private String tanggal;
    private String alamat;
    private double lat;
    private double lng;

    public LaporanItem(int idLaporan, int idUser, String judul, String description, String tanggal, String alamat, double lat, double lng) {
        this.idLaporan = idLaporan;
        this.idUser = idUser;
        this.judul = judul;
        this.description = description;
        this.tanggal = tanggal;
        this.alamat = alamat;
        this.lat = lat;
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public int getIdLaporan() {
        return idLaporan;
    }

    public void setIdLaporan(int idLaporan) {
        this.idLaporan = idLaporan;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }
}
