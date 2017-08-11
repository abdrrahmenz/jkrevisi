package id.or.qodr.jadwalkajian.helper.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

import id.or.qodr.jadwalkajian.model.Jadwal;

/**
 * Created by adul on 03/05/17.
 */

public class SQLiteHandler extends SQLiteOpenHelper {

    private static final String TAG = SQLiteHandler.class.getSimpleName();
    private SQLiteDatabase db;
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "kajian.db";
    // table name
    private static final String TABLE_JADWAL = "jadwal";
    private static final String TABLE_USER = "users";
    // Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_JENIS = "jenis_kajian";
    private static final String KEY_FOTO = "foto_masjid";
    private static final String KEY_SETIAP = "setiap_hari";
    private static final String KEY_PEKAN = "pekan";
    private static final String KEY_TANGGAL = "tanggal";
    private static final String KEY_MULAI = "mulai";
    private static final String KEY_SAMPAI = "sampai";
    private static final String KEY_TEMA = "tema";
    private static final String KEY_PEMATERI = "pemateri";
    private static final String KEY_LOKASI = "lokasi";
    private static final String KEY_ALAMAT = "alamat";
    private static final String KEY_LAT = "lat";
    private static final String KEY_LNG = "lng";
    private static final String KEY_CP = "cp";


    public SQLiteHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_JADWAL + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_JENIS + " TEXT,"
                + KEY_FOTO + " TEXT," + KEY_SETIAP + " TEXT,"
                + KEY_PEKAN + " TEXT," + KEY_TANGGAL + " TEXT,"
                + KEY_MULAI + " TEXT," + KEY_SAMPAI + " TEXT,"
                + KEY_TEMA + " TEXT," + KEY_PEMATERI + " TEXT,"
                + KEY_LOKASI + " TEXT," + KEY_ALAMAT + " TEXT,"
                + KEY_LAT + " TEXT," + KEY_LNG + " TEXT,"
                + KEY_CP + " TEXT" + ")";
        db.execSQL(CREATE_LOGIN_TABLE);

        Log.d(TAG, "Database tables created");
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_JADWAL);

        // Create tables again
        onCreate(db);
    }
    public void deleteIFLoad(){
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_JADWAL, null, null);
        db.close();
        Log.d(TAG, "DELETED DATABASE SQLITE: " );
    }
    /**
     * Storing jadwal details in database
     * */
//    public void addKajian(String _id,String type, String url_masjid, String day, String week, String date,
//                        String start,String end,String topic,String host,String loc,String lat,
//                        String lng,String cp) {
    public void addKajian(Jadwal modelobj) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, modelobj.getId()); // jenis_kajian
        values.put(KEY_JENIS, modelobj.getJenis_kajian()); // jenis_kajian
        values.put(KEY_FOTO, modelobj.getFoto_masjid()); // foto_masjid
        values.put(KEY_SETIAP, modelobj.getSetiap_hari()); // poster
        values.put(KEY_PEKAN, modelobj.getPekan()); // setiap_hari
        values.put(KEY_TANGGAL, modelobj.getTanggal()); // pekan
        values.put(KEY_MULAI, modelobj.getMulai()); // tanggal
        values.put(KEY_SAMPAI, modelobj.getSampai()); // mulai
        values.put(KEY_TEMA, modelobj.getTema()); // sampai
        values.put(KEY_PEMATERI, modelobj.getPemateri()); // tema
        values.put(KEY_LOKASI, modelobj.getLokasi()); // lokasi
        values.put(KEY_ALAMAT, modelobj.getAlamat()); // alamat
        values.put(KEY_LAT, modelobj.getLat()); // lat
        values.put(KEY_LNG, modelobj.getLng()); // lng
        values.put(KEY_CP, modelobj.getCp()); // cp

        // Inserting Row
        long id = db.replace(TABLE_JADWAL, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New user inserted into sqlite: " + id);
    }
    public Cursor readAllData(){
        SQLiteDatabase db  = this.getWritableDatabase();

        //query to get the data
        Cursor result = db.rawQuery("select * from "+ TABLE_JADWAL , null);
        return result;
    }

    public ArrayList<Jadwal> getDataToday(String date) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Jadwal> data = new ArrayList<Jadwal>();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_JADWAL + " WHERE " + KEY_TANGGAL + " LIKE '%" + date + "%'",null);
//        Cursor c = null;
        if (date !=null ) {
//            c = db.rawQuery(selectQuery, null);
//            return c;
            while (cursor.moveToNext()) {
                Jadwal item = new Jadwal();
                item.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                item.setJenis_kajian(cursor.getString(cursor.getColumnIndex(KEY_JENIS)));
                item.setFoto_masjid(cursor.getString(cursor.getColumnIndex(KEY_FOTO)));
                item.setSetiap_hari(cursor.getString(cursor.getColumnIndex(KEY_SETIAP)));
                item.setPekan(cursor.getString(cursor.getColumnIndex(KEY_PEKAN)));
                item.setTanggal(cursor.getString(cursor.getColumnIndex(KEY_TANGGAL)));
                item.setMulai(cursor.getString(cursor.getColumnIndex(KEY_MULAI)));
                item.setSampai(cursor.getString(cursor.getColumnIndex(KEY_SAMPAI)));
                item.setTema(cursor.getString(cursor.getColumnIndex(KEY_TEMA)));
                item.setPemateri(cursor.getString(cursor.getColumnIndex(KEY_PEMATERI)));
                item.setLokasi(cursor.getString(cursor.getColumnIndex(KEY_LOKASI)));
                item.setAlamat(cursor.getString(cursor.getColumnIndex(KEY_ALAMAT)));
                item.setLat(cursor.getString(cursor.getColumnIndex(KEY_LAT)));
                item.setLng(cursor.getString(cursor.getColumnIndex(KEY_LNG)));
                item.setCp(cursor.getString(cursor.getColumnIndex(KEY_CP)));
                data.add(item);
            }
        }
        db.close();
        return data;
    }

    public ArrayList<Jadwal> getDataWeek(String fromDate, String toDate) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Jadwal> data = new ArrayList<Jadwal>();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_JADWAL + " WHERE " + KEY_TANGGAL + " BETWEEN '" + fromDate + "' AND '" + toDate + "' ORDER BY tanggal ASC", null);
//        Cursor c = null;
        if (cursor != null ) {
//            c = db.rawQuery(selectQuery, null);
//            return c;
            while (cursor.moveToNext()) {
                Jadwal item = new Jadwal();
                item.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                item.setJenis_kajian(cursor.getString(cursor.getColumnIndex(KEY_JENIS)));
                item.setFoto_masjid(cursor.getString(cursor.getColumnIndex(KEY_FOTO)));
                item.setSetiap_hari(cursor.getString(cursor.getColumnIndex(KEY_SETIAP)));
                item.setPekan(cursor.getString(cursor.getColumnIndex(KEY_PEKAN)));
                item.setTanggal(cursor.getString(cursor.getColumnIndex(KEY_TANGGAL)));
                item.setMulai(cursor.getString(cursor.getColumnIndex(KEY_MULAI)));
                item.setSampai(cursor.getString(cursor.getColumnIndex(KEY_SAMPAI)));
                item.setTema(cursor.getString(cursor.getColumnIndex(KEY_TEMA)));
                item.setPemateri(cursor.getString(cursor.getColumnIndex(KEY_PEMATERI)));
                item.setLokasi(cursor.getString(cursor.getColumnIndex(KEY_LOKASI)));
                item.setAlamat(cursor.getString(cursor.getColumnIndex(KEY_ALAMAT)));
                item.setLat(cursor.getString(cursor.getColumnIndex(KEY_LAT)));
                item.setLng(cursor.getString(cursor.getColumnIndex(KEY_LNG)));
                item.setCp(cursor.getString(cursor.getColumnIndex(KEY_CP)));
                data.add(item);
            }
        }
        db.close();
        return data;
    }

    public Cursor retrieveKajianHari(String date) {
        String[] column = {KEY_ID,KEY_JENIS,KEY_FOTO,KEY_SETIAP,
                KEY_PEKAN,KEY_TANGGAL,KEY_MULAI,KEY_SAMPAI,
                KEY_TEMA,KEY_PEMATERI,KEY_LOKASI,KEY_ALAMAT,
                KEY_LAT,KEY_LNG,KEY_CP};
        Cursor c = null;
        if (date !=null && date.length() >0) {
            String sql = "SELECT * FROM " + TABLE_JADWAL + " WHERE " + KEY_TANGGAL + " LIKE '%" + date + "%'";
            c = db.rawQuery(sql, null);
            return c;
        }
        c = db.query(TABLE_JADWAL,column,null,null,null,null,null);
        return c;
    }

}
