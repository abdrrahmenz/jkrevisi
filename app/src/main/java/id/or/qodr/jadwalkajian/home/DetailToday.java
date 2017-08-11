package id.or.qodr.jadwalkajian.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import id.or.qodr.jadwalkajian.LoginActivity;
import id.or.qodr.jadwalkajian.R;
import id.or.qodr.jadwalkajian.data.GlideLoad;

/**
 * Created by adul on 01/08/17.
 */

public class DetailToday extends AppCompatActivity implements OnMapReadyCallback {

    private TextView txt_id,txt_jenis_kajian,txt_setiap_hari,txt_pekan,txt_tanggal,txt_mulai,
            txt_sampai,txt_tema,txt_pemateri,txt_lokasi,txt_alamat,txt_cp;
    private ImageView img_foto_masjid;
    private GoogleMap mMap;
    private double lat_req, lng_req;
    private String str_id,str_jenis,str_foto,str_setiap,str_pekan,str_tanggal,str_mulai,str_sampai,
            str_tema,str_pemateri,str_lokasi,str_alamat,str_lat,str_lng,str_cp;
    private  String KEY_ID = "key_id";
    private  String KEY_JENIS = "key_jenis_kajian";
    private  String KEY_FOTO = "key_foto_masjid";
    private  String KEY_SETIAP = "key_setiap_hari";
    private  String KEY_PEKAN = "key_pekan";
    private  String KEY_TANGGAL = "key_tanggal";
    private  String KEY_MULAI = "key_mulai";
    private  String KEY_SAMPAI = "key_sampai";
    private  String KEY_TEMA = "key_tema";
    private  String KEY_PEMATERI = "key_pemateri";
    private  String KEY_LOKASI = "key_lokasi";
    private  String KEY_ALAMAT = "key_alamat";
    private  String KEY_LAT = "key_lat";
    private  String KEY_LNG = "key_lng";
    private  String KEY_CP = "key_cp";
    public static String BASE_URL_IMAGE = "https://adul.000webhostapp.com/apiv2/img/";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_detail_today);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.MyToolbar);
        if (toolbar!=null){
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        initView();

        CollapsingToolbarLayout collaps = (CollapsingToolbarLayout) findViewById(R.id.collapse_toolbar);
        collaps.setTitle("Detail Kajian ");

        Context context = this;
        collaps.setContentScrimColor(ContextCompat.getColor(context, R.color.white));
        collaps.setExpandedTitleTextAppearance(R.style.expandedappbar);
        collaps.setCollapsedTitleTextAppearance(R.style.collapsedappbar);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            str_id = extras.getString(KEY_ID);
            str_jenis = extras.getString(KEY_JENIS);
            str_foto = extras.getString(KEY_FOTO);
            str_setiap = extras.getString(KEY_SETIAP);
            str_pekan = extras.getString(KEY_PEKAN);
            str_tanggal = extras.getString(KEY_TANGGAL);
            str_mulai = extras.getString(KEY_MULAI);
            str_sampai = extras.getString(KEY_SAMPAI);
            str_tema = extras.getString(KEY_TEMA);
            str_pemateri = extras.getString(KEY_PEMATERI);
            str_lokasi = extras.getString(KEY_LOKASI);
            str_alamat = extras.getString(KEY_ALAMAT);
            str_lat = extras.getString(KEY_LAT);
            str_lng = extras.getString(KEY_LNG);
            str_cp = extras.getString(KEY_CP);
        }

        setDataFromList();
        GlideLoad.loadImage(this,BASE_URL_IMAGE+str_foto,img_foto_masjid);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_frag);
        mapFragment.getMapAsync(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_login:
                startActivity(new Intent(DetailToday.this, LoginActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public void setDataFromList(){
        String[] start = str_mulai.split(":");
        String[] ends = str_sampai.split(":");
//        if (str_setiap.isEmpty() && str_pekan.isEmpty()){
//            txt_setiap_hari.setVisibility(View.GONE);
//            txt_pekan.setVisibility(View.GONE);
//            txt_jenis_kajian.setText(str_jenis+" Khusus Akhwat");
//            txt_tanggal.setText(Html.fromHtml("<b>"+"Hari ini"+"<b>"));
//            txt_mulai.setText("Pukul "+start[0]+":"+start[1]);
//            txt_sampai.setText(ends[0]+":"+ends[1]+ " WIB");
//            txt_tema.setText(Html.fromHtml("Judul : "+"<b>"+str_tema+"<b>"));
//            txt_pemateri.setText(Html.fromHtml("Pemateri : "+"<b>"+str_pemateri+"<b>"));
//            txt_lokasi.setText("Tempat : "+str_lokasi);
//            txt_alamat.setText("Alamat : "+str_alamat);
//            txt_cp.setText("Cp : "+str_cp);
//        }else if (!str_setiap.isEmpty() && !str_pekan.isEmpty()){
            txt_jenis_kajian.setText(str_jenis+" Terbuka untuk umum");
            txt_setiap_hari.setText("Setiap Hari : "+str_setiap);
            txt_pekan.setText("Pekan ke : "+str_pekan);
            txt_tanggal.setText(Html.fromHtml("<b>"+"Hari ini"+"<b>"));
            txt_mulai.setText("Pukul "+start[0]+":"+start[1]);
            txt_sampai.setText(ends[0]+":"+ends[1]+ " WIB");
            txt_tema.setText(Html.fromHtml("Judul : "+"<b>"+str_tema+"<b>"));
            txt_pemateri.setText(Html.fromHtml("Pemateri : "+"<b>"+str_pemateri+"<b>"));
            txt_lokasi.setText("Tempat : "+str_lokasi);
            txt_alamat.setText("Alamat : "+str_alamat);
            txt_cp.setText("Cp : "+str_cp);
//        }
    }


    private void initView() {
        txt_id = (TextView) findViewById(R.id.txt_id);
        txt_jenis_kajian = (TextView) findViewById(R.id.txt_jenis_kajian);
        txt_setiap_hari = (TextView) findViewById(R.id.txt_setiap_hari);
        txt_pekan = (TextView) findViewById(R.id.txt_pekan);
        txt_tanggal = (TextView) findViewById(R.id.txt_tanggal);
        txt_mulai = (TextView) findViewById(R.id.txt_mulai);
        txt_sampai = (TextView) findViewById(R.id.txt_sampai);
        txt_tema = (TextView) findViewById(R.id.txt_tema);
        txt_pemateri = (TextView) findViewById(R.id.txt_pemateri);
        txt_lokasi = (TextView) findViewById(R.id.txt_lokasi);
        txt_alamat = (TextView) findViewById(R.id.txt_alamat);
        txt_cp = (TextView) findViewById(R.id.txt_cp);
        img_foto_masjid = (ImageView) findViewById(R.id.img_foto_masjid);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng masjid = new LatLng(Double.parseDouble(str_lat), Double.parseDouble(str_lng));
        mMap.addMarker(new MarkerOptions().position(masjid).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)).title("Lokasi Masjid").snippet("Tempat Kajian"));

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(masjid, 15));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(10), 1500, null);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(masjid));
    }
}
