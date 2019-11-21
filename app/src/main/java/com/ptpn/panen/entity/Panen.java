package com.ptpn.panen.entity;

public class Panen {
    private String id;
    private String id_mandor;
    private String id_absen;
    private String kehadiran;
    private String keterangan;
    private String tanggal;
    private String jam;
    private String creat_att;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId_mandor() {
        return id_mandor;
    }

    public void setId_mandor(String id_mandor) {
        this.id_mandor = id_mandor;
    }

    public String getId_absen() {
        return id_absen;
    }

    public void setId_absen(String id_absen) {
        this.id_absen = id_absen;
    }

    public String getKehadiran() {
        return kehadiran;
    }

    public void setKehadiran(String kehadiran) {
        this.kehadiran = kehadiran;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getJam() {
        return jam;
    }

    public void setJam(String jam) {
        this.jam = jam;
    }

    public String getCreat_att() {
        return creat_att;
    }

    public void setCreat_att(String creat_att) {
        this.creat_att = creat_att;
    }
}
