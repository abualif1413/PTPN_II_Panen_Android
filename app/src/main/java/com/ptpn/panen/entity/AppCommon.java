package com.ptpn.panen.entity;

public class AppCommon {
    public static String ubahFormatTanggal(String tanggalLengkap, FORMAT_TANGGAL format) {
        String hasil = "";
        String[] pecahTanggalLengkap = tanggalLengkap.split(" ");
        String tanggal = pecahTanggalLengkap[0];
        String waktu = pecahTanggalLengkap[1];
        String[] pecahTanggal = tanggal.split("-");
        String[] pecahWaktu = waktu.split(":");

        switch (format) {
            case MYSQL_FORMAT_TANGGAL:
                hasil = tanggalLengkap;
                break;
            case INDONESIA_FORMAT_TANGGAL:
                hasil = pecahTanggal[2] + "-" + pecahTanggal[1] + "-" + pecahTanggal[0] + " " + waktu;
                break;
            case MYSQL_HANYA_TANGGAL:
                hasil = tanggal;
                break;
            case INDONESIA_HANYA_TANGGAL:
                hasil = pecahTanggal[2] + "-" + pecahTanggal[1] + "-" + pecahTanggal[0];
                break;
            case INDONESIA_KOMPLIT_TANPA_DETIK:
                hasil = pecahTanggal[2] + "-" + pecahTanggal[1] + "-" + pecahTanggal[0] + " " + pecahWaktu[0] + ":" + pecahWaktu[1];
                break;
        }

        return hasil;
    }

    public static String padLeftZeros(String inputString, int length) {
        if (inputString.length() >= length) {
            return inputString;
        }
        StringBuilder sb = new StringBuilder();
        while (sb.length() < length - inputString.length()) {
            sb.append('0');
        }
        sb.append(inputString);

        return sb.toString();
    }
}
