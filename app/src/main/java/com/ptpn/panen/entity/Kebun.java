package com.ptpn.panen.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Kebun {
    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("nama_kebun")
    @Expose
    private String namaKebun;

    @SerializedName("id_distrik")
    @Expose
    private String idDistrik;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNamaKebun() {
        return namaKebun;
    }

    public void setNamaKebun(String namaKebun) {
        this.namaKebun = namaKebun;
    }

    public String getIdDistrik() {
        return idDistrik;
    }

    public void setIdDistrik(String idDistrik) {
        this.idDistrik = idDistrik;
    }
}
