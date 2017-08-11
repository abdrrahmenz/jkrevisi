//package id.or.qodr.jadwalkajian.helper.sqlite;
//
//import android.content.ContentValues;
//import android.content.Context;
//import android.database.Cursor;
//import android.database.SQLException;
//import android.database.sqlite.SQLiteDatabase;
//
//import id.or.qodr.jadwalkajian.model.JadwalModel;
//
///**
// * Created by adul on 03/05/17.
// */
//
//public class DBAdapter {
//
//    Context c;
//    SQLiteDatabase db;
//    SQLiteHandler helper;
//
//    public DBAdapter(Context c) {
//        this.c = c;
//        helper = new SQLiteHandler(c);
//    }
//
//    //OPEN DATABASE
//    public DBAdapter openDB()
//    {
//        try {
//            db=helper.getWritableDatabase();
//        }catch (SQLException e)
//        {
//            e.printStackTrace();
//        }
//        return this;
//    }
//
//    //CLOSE DATABASE
//    public void closeDB()
//    {
//        try {
//            helper.close();
//        }catch (SQLException e)
//        {
//            e.printStackTrace();
//        }
//    }
//
////    public Cursor readAllData(){
////        SQLiteDatabase db  = c.getWritableDatabase();
////
////        //query to get the data
////        Cursor result = db.rawQuery("select * from "+helper.TABLE_USER,null);
////        return result;
////    }
//
//    public Cursor selectedDate(String from_dt ,String to_dt)
//    {
//        try
//        {
//            String str_k="select RowId,Bill_Date from  SalesMaster Where Bill_Date between '"+
//                    from_dt+ "'and'"+to_dt+ "'";
//            System.out.println(str_k);
//
//            Cursor c1 = db.rawQuery(
//                    "select * from  jadwal Where tanggal between '"+
//                            from_dt+ "'and'"+to_dt+ "'", null);
//            return c1;
//        }
//        catch(Exception e)
//        {
//            System.out.println("inside database file "+ e);
//            return null;
//        }
//    }
//    //INSERT
//    public long add(String _id,String type, String url_masjid, String day, String week, String date,
//                    String start,String end,String topic,String host,String loc,String lat,
//                    String lng,String cp)
//    {
//        try
//        {
//            ContentValues values = new ContentValues();
//            values.put(Jadwal.KEY_ID, _id); // jenis_kajian
//            values.put(JadwalModel.KEY_TYPE, type); // jenis_kajian
//            values.put(JadwalModel.KEY_URL_MASJID, url_masjid); // foto_masjid
//            values.put(JadwalModel.KEY_DAY, day); // setiap_hari
//            values.put(JadwalModel.KEY_WEEK, week); // pekan
//            values.put(JadwalModel.KEY_DATE, date); // tanggal
//            values.put(JadwalModel.KEY_START, start); // mulai
//            values.put(JadwalModel.KEY_END, end); // sampai
//            values.put(JadwalModel.KEY_TOPIC, topic); // tema
//            values.put(JadwalModel.KEY_HOST, host); // pemateri
//            values.put(JadwalModel.KEY_LOC, loc); // lokasi
//            values.put(JadwalModel.KEY_LAT, lat); // lat
//            values.put(JadwalModel.KEY_LNG, lng); // lng
//            values.put(JadwalModel.KEY_CP, cp); // cp
//            return db.insert(JadwalModel.TABLE_USER,JadwalModel.KEY_ID,values);
//        }catch (SQLException e)
//        {
//            e.printStackTrace();
//        }
//        return 0;
//    }
//
//    //RETRIEVE
//    public Cursor getAllJadwalLis()
//    {
//        String[] columns={JadwalModel.KEY_ID,JadwalModel.KEY_TYPE,JadwalModel.KEY_URL_MASJID,JadwalModel.KEY_POSTER,
//                JadwalModel.KEY_DAY,JadwalModel.KEY_WEEK,JadwalModel.KEY_DATE,JadwalModel.KEY_START,
//                JadwalModel.KEY_END,JadwalModel.KEY_TOPIC,JadwalModel.KEY_HOST,JadwalModel.KEY_TAG,
//                JadwalModel.KEY_LOC,JadwalModel.KEY_LAT,JadwalModel.KEY_LNG,JadwalModel.KEY_ADDRESS,
//                JadwalModel.KEY_CP,JadwalModel.KEY_STATUS};
//        return db.query(JadwalModel.TABLE_USER,columns,null,null,null,null,JadwalModel.KEY_DATE+" DESC");
//    }
//
//
//
//    public Cursor retrieveKajianPekan(String date1, String date2) {
//        String[] column = {JadwalModel.KEY_ID,JadwalModel.KEY_TYPE,JadwalModel.KEY_URL_MASJID,JadwalModel.KEY_POSTER,
//                JadwalModel.KEY_DAY,JadwalModel.KEY_WEEK,JadwalModel.KEY_DATE,JadwalModel.KEY_START,
//                JadwalModel.KEY_END,JadwalModel.KEY_TOPIC,JadwalModel.KEY_HOST,JadwalModel.KEY_TAG,
//                JadwalModel.KEY_LOC,JadwalModel.KEY_LAT,JadwalModel.KEY_LNG,JadwalModel.KEY_ADDRESS,
//                JadwalModel.KEY_CP,JadwalModel.KEY_STATUS};
////        Cursor c = null;
////        if (date1 !=null && date1.length() >0 || date2 !=null && date2.length() >0) {
////            String sql = "SELECT * FROM " + JadwalModel.TABLE_USER + " WHERE " + JadwalModel.KEY_DATE + " BETWEEN '" + date1 + "' AND '" + date2 +"'";
////            c = db.rawQuery(sql, null);
////            return c;
////        }
//        Cursor c = db.query(JadwalModel.TABLE_USER,column,JadwalModel.KEY_DATE  + " BETWEEN ? AND ?", new String[]{date1, date2}," strftime(" + "\"%d" + "-" + "%m" + "-" + "%Y\""  + ",tanggal) ", null, null);
//        if (c!=null){
//            c.moveToFirst();
//        }
//        return c;
//    }
//
//    public Cursor history(String startdate,String enddate) {
//        Cursor mCursor = db.rawQuery("SELECT * FROM "+ JadwalModel.TABLE_USER +
//                " WHERE " + JadwalModel.KEY_DATE +
//                " BETWEEN ?  AND ?", new String[]{startdate, enddate});
//        return mCursor;
//    }
//
//    //RETRIEVE OR FILTERING
//    public Cursor retrieve(String search){
//        String[] columns={JadwalModel.KEY_ID,JadwalModel.KEY_TYPE,JadwalModel.KEY_URL_MASJID,JadwalModel.KEY_POSTER,
//                JadwalModel.KEY_DAY,JadwalModel.KEY_WEEK,JadwalModel.KEY_DATE,JadwalModel.KEY_START,
//                JadwalModel.KEY_END,JadwalModel.KEY_TOPIC,JadwalModel.KEY_HOST,JadwalModel.KEY_TAG,
//                JadwalModel.KEY_LOC,JadwalModel.KEY_LAT,JadwalModel.KEY_LNG,JadwalModel.KEY_ADDRESS,
//                JadwalModel.KEY_CP,JadwalModel.KEY_STATUS};
//        Cursor c =null;
//
//        if (search != null && search.length() > 0) {
//            String sql = "SELECT * FROM "+ JadwalModel.TABLE_USER+" WHERE "+JadwalModel.KEY_TAG+" LIKE '%"+search+"%'";
//            c=db.rawQuery(sql,null);
//            return c;
//        }
//        c=db.query(JadwalModel.TABLE_USER,columns,null,null,null,null,null);
//        return c;
//    }
//
//}
