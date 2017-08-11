package id.or.qodr.jadwalkajian.helper;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import id.or.qodr.jadwalkajian.R;
import id.or.qodr.jadwalkajian.data.SessionManager;
import id.or.qodr.jadwalkajian.data.UtilsDate;
import id.or.qodr.jadwalkajian.home.DetailToday;
import id.or.qodr.jadwalkajian.home.DetailWeek;
import id.or.qodr.jadwalkajian.model.Jadwal;

/**
 * Created by adul on 01/08/17.
 */

public class AdapterWeek extends RecyclerView.Adapter<VHolderWeek> {
    private Context context;
    private UtilsDate utils;
    private SessionManager session;
    private JSONArray json_listkajian;
    private String date;
    private int rowLayout;
    private List<Jadwal> jadwals;
    private final String KEY_ID = "key_id";
    private final String KEY_JENIS = "key_jenis_kajian";
    private final String KEY_FOTO = "key_foto_masjid";
    private final String KEY_SETIAP = "key_setiap_hari";
    private final String KEY_PEKAN = "key_pekan";
    private final String KEY_TANGGAL = "key_tanggal";
    private final String KEY_MULAI = "key_mulai";
    private final String KEY_SAMPAI = "key_sampai";
    private final String KEY_TEMA = "key_tema";
    private final String KEY_PEMATERI = "key_pemateri";
    private final String KEY_LOKASI = "key_lokasi";
    private final String KEY_ALAMAT = "key_alamat";
    private final String KEY_LAT = "key_lat";
    private final String KEY_LNG = "key_lng";
    private final String KEY_CP = "key_cp";

    public void setJson_listkajian(JSONArray json_listkajian) {
        this.json_listkajian = json_listkajian;
        notifyDataSetChanged();
    }

    public AdapterWeek(List<Jadwal> jadwals, int rowLayout, Context context) {
        this.jadwals = jadwals;
        this.rowLayout = rowLayout;
        this.context = context;
    }

    @Override
    public VHolderWeek onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_kajian_week, null);

        // Session Manager
        session = new SessionManager(context);

        utils = new UtilsDate(context);

        return new VHolderWeek(layoutView);
    }

    @Override
    public void onBindViewHolder(VHolderWeek holder, final int position) {

            Typeface custom_font = Typeface.createFromAsset(context.getAssets(), "fonts/digital-7.ttf");

            String[] pisah = jadwals.get(position).getTanggal().split("-");
            SimpleDateFormat format = new SimpleDateFormat("F");
            Calendar c = Calendar.getInstance();
            Date nowDate = new Date();
            c.setTime(nowDate);
            c.add(Calendar.DAY_OF_WEEK_IN_MONTH, 0);
            Date week1 = c.getTime();

            final String week = format.format(week1.getTime());

//            date = getDayOnWeek(jadwals.get(position).getSetiap_hari(), Integer.parseInt(week));

            if (jadwals.get(position).getPekan().isEmpty()) {
                holder.tanggal.setText(pisah[2]);
                holder.tanggal.setTypeface(custom_font, Typeface.BOLD);
                holder.bulan.setText(utils.monthPekan(pisah[1]));
                holder.bulan.setTypeface(custom_font, Typeface.BOLD);
            }
//            else {
//                String[] dateSpt = date.split("-");
//
////                holder.tanggal.setText(dateSpt[2]);
////                holder.tanggal.setTypeface(custom_font, Typeface.BOLD);
////                holder.bulan.setText(utils.monthPekan(dateSpt[1]));
//                holder.bulan.setTypeface(custom_font, Typeface.BOLD);
//            }

            String[] mule = jadwals.get(position).getMulai().split(":");
            holder.mulai.setText(mule[0]+":"+mule[1]);
            holder.mulai.setTypeface(custom_font, Typeface.BOLD);

            String[] sampe = jadwals.get(position).getSampai().split(":");
            holder.sampai.setText(sampe[0]+":"+sampe[1]);
            holder.sampai.setTypeface(custom_font, Typeface.BOLD);

            holder.tema.setText(jadwals.get(position).getTema());
            holder.pemateri.setText(jadwals.get(position).getPemateri());
            holder.lokasi.setText(jadwals.get(position).getLokasi());
            holder.lat.setText(jadwals.get(position).getLat());
            holder.lng.setText(jadwals.get(position).getLng());
            holder.cp.setText(jadwals.get(position).getCp());

            holder.rootViewPekan.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    final CharSequence[] dialogitem = {"Edit", "Delete"};
                    AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                    dialog.setCancelable(true);
                    dialog.setItems(dialogitem, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub

                            switch (which) {
                                case 0:
                                    if (!session.isLoggedIn()){
                                        Toast.makeText(context, "Maaf hanya admin yang bisa Edit", Toast.LENGTH_SHORT).show();
                                    }else {
//                                        try {
//                                            //mengirim data ke activity yg lain
//                                            Intent intent = new Intent(context, AdminInput.class);
//                                            intent.putExtra("id_key", listData.getString("id"));
//                                            intent.putExtra("type_key", listData.getString("jenis_kajian"));
//                                            intent.putExtra("tgl_key", listData.getString("tanggal"));
//                                            intent.putExtra("day_key", listData.getString("setiap_hari"));
//                                            intent.putExtra("pekan_key", listData.getString("pekan"));
//                                            intent.putExtra("mule_key", listData.getString("mulai"));
//                                            intent.putExtra("sampe_key", listData.getString("sampai"));
//                                            intent.putExtra("tema_key", listData.getString("tema"));
//                                            intent.putExtra("pemteri_key", listData.getString("pemateri"));
//                                            intent.putExtra("lokasi_key", listData.getString("lokasi"));
//                                            intent.putExtra("cp_key", listData.getString("cp"));
//                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                            context.startActivity(intent);
//                                        } catch (JSONException e) {
//                                            e.printStackTrace();
//                                        }
                                    }
                                    break;
                                case 1:
                                    if (!session.isLoggedIn()){
                                        Toast.makeText(context, "Maaf hanya admin yang bisa Hapus", Toast.LENGTH_SHORT).show();

                                    }else {
                                        new AlertDialog.Builder(context)
                                                .setTitle("Delete Kajian")
                                                .setMessage("Are you sure want to delete this?")
                                                .setNegativeButton(android.R.string.no, null)
                                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface arg0, int arg1) {
//                                                        try {
//                                                            deleteKajian(api.DEL_JADWAL + listData.getInt("id"));
//                                                            Intent intent = new Intent(context, AdminActivity.class);
//                                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                                            context.startActivity(intent);
//                                                        }catch (JSONException e){
//                                                            e.printStackTrace();
//                                                        }

                                                    }
                                                }).create().show();
                                    }
                                    break;
                            }
                        }
                    }).show();
                    return false;
                }
            });

            holder.rootViewPekan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, DetailWeek.class);
                    intent.putExtra(KEY_ID, jadwals.get(position).getId());
                    intent.putExtra(KEY_JENIS, jadwals.get(position).getJenis_kajian());
                    intent.putExtra(KEY_FOTO, jadwals.get(position).getFoto_masjid());
                    intent.putExtra(KEY_SETIAP, jadwals.get(position).getSetiap_hari());
                    intent.putExtra(KEY_PEKAN, jadwals.get(position).getPekan());
                    intent.putExtra(KEY_TANGGAL, jadwals.get(position).getTanggal());
                    intent.putExtra(KEY_MULAI, jadwals.get(position).getMulai());
                    intent.putExtra(KEY_SAMPAI, jadwals.get(position).getSampai());
                    intent.putExtra(KEY_TEMA, jadwals.get(position).getTema());
                    intent.putExtra(KEY_PEMATERI, jadwals.get(position).getPemateri());
                    intent.putExtra(KEY_LOKASI, jadwals.get(position).getLokasi());
                    intent.putExtra(KEY_ALAMAT, jadwals.get(position).getAlamat());
                    intent.putExtra(KEY_LAT, jadwals.get(position).getLat());
                    intent.putExtra(KEY_LNG, jadwals.get(position).getLng());
                    intent.putExtra(KEY_CP, jadwals.get(position).getCp());
                    context.startActivity(intent);
//                    try {
//
//                        date = getDayOnWeek(listData.getString("setiap_hari"), Integer.parseInt(week));
//
//                        Intent intent = new Intent(context, DetailKPekan.class);
//                        intent.putExtra("id_key", listData.getInt("id"));
//                        intent.putExtra("type_key", listData.getString("jenis_kajian"));
//                        intent.putExtra("tgl_key", listData.getString("tanggal"));
//                        intent.putExtra("tgl2_key", date);
//                        intent.putExtra("day_key", listData.getString("setiap_hari"));
//                        intent.putExtra("pekan_key", listData.getString("pekan"));
//                        intent.putExtra("mule_key", listData.getString("mulai"));
//                        intent.putExtra("sampe_key", listData.getString("sampai"));
//                        intent.putExtra("tema_key", listData.getString("tema"));
//                        intent.putExtra("pemteri_key", listData.getString("pemateri"));
//                        intent.putExtra("lokasi_key", listData.getString("lokasi"));
//                        intent.putExtra("cp_key", listData.getString("cp"));
//                        intent.putExtra("lat_key", listData.getDouble("lat"));
//                        intent.putExtra("lng_key", listData.getDouble("lng"));
//                        intent.putExtra("img_key", listData.getString("foto_masjid"));
//                        context.startActivity(intent);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
                }
            });
    }

    public String getDayOnWeek(String dayNow,int week){
        String reDate = "";
        int from=0;
        int until=0;
        if(week==1){
            from=1;
            until=7;
        }else if(week==2){
            from=8;
            until=14;
        }else if(week==3){
            from=15;
            until=21;
        }else if(week==4){
            from=22;
            until=28;
        }else if(week==5){
            from=29;
            until=31;
        }
        for(int i=from;i<=until;i++){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            int year = Calendar.getInstance().get(Calendar.YEAR);
            int month = Calendar.getInstance().get(Calendar.MONTH);
            Calendar calendar = new GregorianCalendar(year,month,i);
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
            String day=getDayName(dayOfWeek);
            if(dayNow.equals(day)){
                reDate=sdf.format(calendar.getTime());
                break;
            }
        }
        return reDate;
    }

    public String getDayName(int dayOfWeek){
        String day=null;
        switch(dayOfWeek){
            case 1:
                day="Ahad";
                break;
            case 2:
                day="Senin";
                break;
            case 3:
                day="Selasa";
                break;
            case 4:
                day="Rabu";
                break;
            case 5:
                day="Kamis";
                break;
            case 6:
                day="Jumat";
                break;
            case 7:
                day="Sabtu";
                break;
        }
        return day;
    }

    @Override
    public int getItemCount() {
//        if (json_listkajian!=null){
//            return json_listkajian.length();
//        }else {
//            return 0;
//        }
        return jadwals.size();
    }
}

class VHolderWeek extends RecyclerView.ViewHolder {
    public TextView mulai, sampai, tema, pemateri, lokasi, lat, lng, cp, id, type, tanggal, bulan;
    public View rootViewPekan;

    public VHolderWeek(View itemView) {
        super(itemView);
        rootViewPekan = itemView;
        id = (TextView) rootViewPekan.findViewById(R.id._id);
        type = (TextView) rootViewPekan.findViewById(R.id.jniskjian);
        tanggal = (TextView) rootViewPekan.findViewById(R.id.tanggal);
        bulan = (TextView) rootViewPekan.findViewById(R.id.bulan);
        mulai = (TextView) rootViewPekan.findViewById(R.id.mulaiPekan);
        sampai = (TextView) rootViewPekan.findViewById(R.id.sampaiPekan);
        tema = (TextView) rootViewPekan.findViewById(R.id.temaPekan);
        pemateri = (TextView) rootViewPekan.findViewById(R.id.pemateriPekan);
        lokasi = (TextView) rootViewPekan.findViewById(R.id.lokasiPekan);
        lat = (TextView) rootViewPekan.findViewById(R.id.lat);
        lng = (TextView) rootViewPekan.findViewById(R.id.lng);
        cp = (TextView) rootViewPekan.findViewById(R.id.cpPekan);
    }
}
