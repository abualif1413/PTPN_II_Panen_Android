package com.ptpn.panen.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Pemanen {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("nama_pemanen")
    @Expose
    private String namaPemanen;
    @SerializedName("id_kerani_askep")
    @Expose
    private String idKeraniAskep;
    @SerializedName("id_kerani_kcs")
    @Expose
    private String idKeraniKcs;
    @SerializedName("id_mandor")
    @Expose
    private String idMandor;
    @SerializedName("id_kebun")
    @Expose
    private String idKebun;
    @SerializedName("id_afdeling")
    @Expose
    private String idAfdeling;
    @SerializedName("barcode")
    @Expose
    private String barcode;
    @SerializedName("img_barcode")
    @Expose
    private String imgBarcode;
    @SerializedName("photo")
    @Expose
    private String photo;
    @SerializedName("keterangan")
    @Expose
    private String keterangan;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNamaPemanen() {
        return namaPemanen;
    }

    public void setNamaPemanen(String namaPemanen) {
        this.namaPemanen = namaPemanen;
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

    public String getIdMandor() {
        return idMandor;
    }

    public void setIdMandor(String idMandor) {
        this.idMandor = idMandor;
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

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getImgBarcode() {
        return imgBarcode;
    }

    public void setImgBarcode(String imgBarcode) {
        this.imgBarcode = imgBarcode;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }
}
