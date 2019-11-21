package com.ptpn.panen.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AlatPanen {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("nama_alat")
    @Expose
    private String namaAlat;
    @SerializedName("premi_alat")
    @Expose
    private String premiAlat;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNamaAlat() {
        return namaAlat;
    }

    public void setNamaAlat(String namaAlat) {
        this.namaAlat = namaAlat;
    }

    public String getPremiAlat() {
        return premiAlat;
    }

    public void setPremiAlat(String premiAlat) {
        this.premiAlat = premiAlat;
    }
}
