package id.or.qodr.jadwalkajian.model;

import java.util.List;

/**
 * Created by adul on 01/08/17.
 */

public class JadwalResponse {

    /**
     * result : true
     * jadwal : [{"id":1,"jenis_kajian":"Tidak Rutin","foto_masjid":"img-bdc64c6911b887a5e32f89add35791b0.jpg","setiap_hari":"Senin","pekan":"1, 2","tanggal":"2017-06-05","mulai":"10:00:00","sampai":"11:00:00","tema":"Fiqh","pemateri":"Ust Syahiid","lokasi":"Muadz bin Jabal","alamat":"Jl. Pekalongan Raya","lat":21.32424,"lng":-12.4241,"cp":"091084028"},{"id":2,"jenis_kajian":"Tidak Rutin","foto_masjid":"img-5d6933f23c0fec37266e74b7d060631c.jpg","setiap_hari":"","pekan":"","tanggal":"2017-06-05","mulai":"10:43:00","sampai":"10:43:00","tema":"Fiqh","pemateri":"Ust Afif","lokasi":"SPBU 4451107","alamat":"Karangsari, Kec. Karanganyar, Pekalongan, Jawa Tengah 51182, Indonesia","lat":-7.031609200000001,"lng":109.6106803,"cp":"098666"}]
     */

    private String result;
    private List<Jadwal> jadwal;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public List<Jadwal> getJadwal() {
        return jadwal;
    }

    public void setJadwal(List<Jadwal> jadwal) {
        this.jadwal = jadwal;
    }
}
