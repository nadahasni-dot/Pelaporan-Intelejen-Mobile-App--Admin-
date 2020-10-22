package com.example.pelaporanbakesbangpolmalang.model;

public class PemberitahuanItem {
    private int idPemberitahuan;
    private int idUser;
    private String judul;
    private String description;
    private String tanggal;

    public PemberitahuanItem(int idPemberitahuan, int idUser, String judul, String description, String tanggal) {
        this.idPemberitahuan = idPemberitahuan;
        this.idUser = idUser;
        this.judul = judul;
        this.description = description;
        this.tanggal = tanggal;
    }

    public int getIdPemberitahuan() {
        return idPemberitahuan;
    }

    public void setIdPemberitahuan(int idPemberitahuan) {
        this.idPemberitahuan = idPemberitahuan;
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
