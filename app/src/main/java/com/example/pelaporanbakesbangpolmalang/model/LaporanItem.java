package com.example.pelaporanbakesbangpolmalang.model;

public class LaporanItem {
    private int idLaporan;
    private int idUser;
    private String judul;
    private String description;
    private String tanggal;

    public LaporanItem(int idLaporan, int idUser, String judul, String description, String tanggal) {
        this.judul = judul;
        this.description = description;
        this.tanggal = tanggal;
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
