package com.ptpn.panen.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Afdeling {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("id_kebun")
    @Expose
    private String idKebun;
    @SerializedName("nama_afdeling")
    @Expose
    private String namaAfdeling;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdKebun() {
        return idKebun;
    }

    public void setIdKebun(String idKebun) {
        this.idKebun = idKebun;
    }

    public String getNamaAfdeling() {
        return namaAfdeling;
    }

    public void setNamaAfdeling(String namaAfdeling) {
        this.namaAfdeling = namaAfdeling;
    }
}
