package id.or.qodr.jadwalkajian.model;

/**
 * Created by adul on 01/08/17.
 */

public class Jadwal {
    /**
     * id : 1
     * jenis_kajian : Tidak Rutin
     * foto_masjid : img-bdc64c6911b887a5e32f89add35791b0.jpg
     * setiap_hari : Senin
     * pekan : 1, 2
     * tanggal : 2017-06-05
     * mulai : 10:00:00
     * sampai : 11:00:00
     * tema : Fiqh
     * pemateri : Ust Syahiid
     * lokasi : Muadz bin Jabal
     * alamat : Jl. Pekalongan Raya
     * lat : 21.32424
     * lng : -12.4241
     * cp : 091084028
     */

    private int id;
    private String jenis_kajian;
    private String foto_masjid;
    private String setiap_hari;
    private String pekan;
    private String tanggal;
    private String mulai;
    private String sampai;
    private String tema;
    private String pemateri;
    private String lokasi;
    private String alamat;
    private String lat;
    private String lng;
    private String cp;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getJenis_kajian() {
        return jenis_kajian;
    }

    public void setJenis_kajian(String jenis_kajian) {
        this.jenis_kajian = jenis_kajian;
    }

    public String getFoto_masjid() {
        return foto_masjid;
    }

    public void setFoto_masjid(String foto_masjid) {
        this.foto_masjid = foto_masjid;
    }

    public String getSetiap_hari() {
        return setiap_hari;
    }

    public void setSetiap_hari(String setiap_hari) {
        this.setiap_hari = setiap_hari;
    }

    public String getPekan() {
        return pekan;
    }

    public void setPekan(String pekan) {
        this.pekan = pekan;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getMulai() {
        return mulai;
    }

    public void setMulai(String mulai) {
        this.mulai = mulai;
    }

    public String getSampai() {
        return sampai;
    }

    public void setSampai(String sampai) {
        this.sampai = sampai;
    }

    public String getTema() {
        return tema;
    }

    public void setTema(String tema) {
        this.tema = tema;
    }

    public String getPemateri() {
        return pemateri;
    }

    public void setPemateri(String pemateri) {
        this.pemateri = pemateri;
    }

    public String getLokasi() {
        return lokasi;
    }

    public void setLokasi(String lokasi) {
        this.lokasi = lokasi;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getCp() {
        return cp;
    }

    public void setCp(String cp) {
        this.cp = cp;
    }
}
