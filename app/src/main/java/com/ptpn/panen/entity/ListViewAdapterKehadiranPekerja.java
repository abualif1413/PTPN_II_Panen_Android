package com.ptpn.panen.entity;

public class ListViewAdapterKehadiranPekerja {
    private int id;
    private int idPekerja;
    private String namaPemanen;
    private String tanggal;
    private String statusKehadiran;
    private String keterangan;
    private String statusUpload;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNamaPemanen() {
        return namaPemanen;
    }

    public void setNamaPemanen(String namaPemanen) {
        this.namaPemanen = namaPemanen;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getStatusKehadiran() {
        return statusKehadiran;
    }

    public void setStatusKehadiran(String statusKehadiran) {
        this.statusKehadiran = statusKehadiran;
    }

    public int getIdPekerja() {
        return idPekerja;
    }

    public void setIdPekerja(int idPekerja) {
        this.idPekerja = idPekerja;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public String getStatusUpload() {
        return statusUpload;
    }

    public void setStatusUpload(String statusUpload) {
        this.statusUpload = statusUpload;
    }
}
