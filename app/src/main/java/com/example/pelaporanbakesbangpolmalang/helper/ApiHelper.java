package com.example.pelaporanbakesbangpolmalang.helper;

public class ApiHelper {
//        public static final String BASE_URL = "http://192.168.43.68/ci-rest-api-bakesbangpol/api/";
//        public static final String BASE_URL = "http://172.20.10.2/ci-rest-api-bakesbangpol/api/";
    public static final String BASE_URL = "http://192.168.100.128/ci-rest-api-bakesbangpol/api/";
//    public static final String BASE_URL = "https://laporbakesbangpol.000webhostapp.com/api/";
    public static final String ASSETS_URL = "http://192.168.100.128/ci-rest-api-bakesbangpol/assets/img/";

    // LAPORAN
    public static final String ALL_LAPORAN = BASE_URL + "laporan/all";
    public static final String ALL_LAPORAN_LIMIT = BASE_URL + "laporan/all/limit/";
    public static final String LAPORAN_BY_ID_USER = BASE_URL + "laporan/id_user/";
    public static final String LAPORAN_BY_ID_LAPORAN = BASE_URL + "laporan/id_laporan/";
    public static final String ADD_LAPORAN = BASE_URL + "laporan/tambah";

    // USER
    public static final String LOGIN = BASE_URL + "auth";
    public static final String ALL_USER = BASE_URL + "auth/all";
    public static final String USER_BY_ID_USER = BASE_URL + "auth/id_user/";
    public static final String USER_PROFILE_BY_ID_USER = BASE_URL + "auth/profil/";
    public static final String USER_BY_LEVEL = BASE_URL + "auth/level/";

    public static final String REGISTER_NEW_USER = BASE_URL + "auth/register";
    public static final String AUTH_DELETE = BASE_URL + "auth/delete/";
    public static final String AUTH_UPDATE_USER = BASE_URL + "auth/update/user";
    public static final String AUTH_UPDATE_PASSWORD = BASE_URL + "auth/update/password";

    // PEMBERITAHUAN
    public static final String ALL_PEMBERITAHUAN = BASE_URL + "pemberitahuan/all";
    public static final String PEMBERITAHUAN_BY_ID_PEMBERITAHUAN = BASE_URL + "pemberitahuan/id_pemberitahuan/";
    public static final String PEMBERITAHUAN_BY_ID_USER = BASE_URL + "pemberitahuan/id_user/";
    public static final String PEMBERITAHUAN_BY_ID_PENERIMA = BASE_URL + "pemberitahuan/id_penerima/";
    public static final String PEMBERITAHUAN_COUNT_BY_ID_PENERIMA = BASE_URL + "pemberitahuan/count/";

    public static final String SEND_PEMBERITAHUAN = BASE_URL + "pemberitahuan/tambah";
}
