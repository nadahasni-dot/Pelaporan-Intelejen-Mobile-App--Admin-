package com.example.pelaporanbakesbangpolmalang;

public class PenggunaItem {
    private int idUser;
    private String username;
    private String email;
    private String userImageUrl;

    public PenggunaItem(int idUser, String username, String email, String userImageUrl) {
        this.idUser = idUser;
        this.username = username;
        this.email = email;
        this.userImageUrl = userImageUrl;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserImageUrl() {
        return userImageUrl;
    }

    public void setUserImageUrl(String userImageUrl) {
        this.userImageUrl = userImageUrl;
    }
}
