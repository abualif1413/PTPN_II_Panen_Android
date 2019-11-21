package com.ptpn.panen.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ptpn.panen.handler.SQLiteHandler;

public class Mandor {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("nama_lengkap")
    @Expose
    private String namaLengkap;
    @SerializedName("id_jabatan")
    @Expose
    private String idJabatan;
    @SerializedName("id_kerani_askep")
    @Expose
    private String idKeraniAskep;
    @SerializedName("id_kerani_kcs")
    @Expose
    private String idKeraniKcs;
    @SerializedName("id_kebun")
    @Expose
    private String idKebun;
    @SerializedName("id_afdeling")
    @Expose
    private String idAfdeling;
    @SerializedName("photo")
    @Expose
    private String photo;
    @SerializedName("token")
    @Expose
    private String token;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNamaLengkap() {
        return namaLengkap;
    }

    public void setNamaLengkap(String namaLengkap) {
        this.namaLengkap = namaLengkap;
    }

    public String getIdJabatan() {
        return idJabatan;
    }

    public void setIdJabatan(String idJabatan) {
        this.idJabatan = idJabatan;
    }

    public String getIdKeraniAskep() {
        return idKeraniAskep;
    }

    public void setIdKeraniAskep(String idKeraniAskep) {
        this.idKeraniAskep = idKeraniAskep;
    }

    public String getIdKeraniKcs() {
        return idKeraniKcs;
    }

    public void setIdKeraniKcs(String idKeraniKcs) {
        this.idKeraniKcs = idKeraniKcs;
    }

    public String getIdKebun() {
        return idKebun;
    }

    public void setIdKebun(String idKebun) {
        this.idKebun = idKebun;
    }

    public String getIdAfdeling() {
        return idAfdeling;
    }

    public void setIdAfdeling(String idAfdeling) {
        this.idAfdeling = idAfdeling;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
