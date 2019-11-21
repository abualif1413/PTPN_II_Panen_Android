package com.ptpn.panen.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Blok {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("id_kebun")
    @Expose
    private String idKebun;
    @SerializedName("id_afdeling")
    @Expose
    private String idAfdeling;
    @SerializedName("blok")
    @Expose
    private String blok;
    @SerializedName("bt")
    @Expose
    private String bt;
    @SerializedName("p0")
    @Expose
    private String p0;
    @SerializedName("p1")
    @Expose
    private String p1;
    @SerializedName("p2")
    @Expose
    private String p2;
    @SerializedName("p3")
    @Expose
    private String p3;
    @SerializedName("rp_p0")
    @Expose
    private String rpP0;
    @SerializedName("rp_p1")
    @Expose
    private String rpP1;
    @SerializedName("rp_p2")
    @Expose
    private String rpP2;
    @SerializedName("rp_p3")
    @Expose
    private String rpP3;
    @SerializedName("tahun_tanam")
    @Expose
    private String tahunTanam;
    @SerializedName("prediksi_komidel")
    @Expose
    private String prediksiKomidel;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("keterangan")
    @Expose
    private String keterangan;

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

    public String getIdAfdeling() {
        return idAfdeling;
    }

    public void setIdAfdeling(String idAfdeling) {
        this.idAfdeling = idAfdeling;
    }

    public String getBlok() {
        return blok;
    }

    public void setBlok(String blok) {
        this.blok = blok;
    }

    public String getBt() {
        return bt;
    }

    public void setBt(String bt) {
        this.bt = bt;
    }

    public String getP0() {
        return p0;
    }

    public void setP0(String p0) {
        this.p0 = p0;
    }

    public String getP1() {
        return p1;
    }

    public void setP1(String p1) {
        this.p1 = p1;
    }

    public String getP2() {
        return p2;
    }

    public void setP2(String p2) {
        this.p2 = p2;
    }

    public String getP3() {
        return p3;
    }

    public void setP3(String p3) {
        this.p3 = p3;
    }

    public String getRpP0() {
        return rpP0;
    }

    public void setRpP0(String rpP0) {
        this.rpP0 = rpP0;
    }

    public String getRpP1() {
        return rpP1;
    }

    public void setRpP1(String rpP1) {
        this.rpP1 = rpP1;
    }

    public String getRpP2() {
        return rpP2;
    }

    public void setRpP2(String rpP2) {
        this.rpP2 = rpP2;
    }

    public String getRpP3() {
        return rpP3;
    }

    public void setRpP3(String rpP3) {
        this.rpP3 = rpP3;
    }

    public String getTahunTanam() {
        return tahunTanam;
    }

    public void setTahunTanam(String tahunTanam) {
        this.tahunTanam = tahunTanam;
    }

    public String getPrediksiKomidel() {
        return prediksiKomidel;
    }

    public void setPrediksiKomidel(String prediksiKomidel) {
        this.prediksiKomidel = prediksiKomidel;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }
}
