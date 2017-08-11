package id.or.qodr.jadwalkajian;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import id.or.qodr.jadwalkajian.api.RestApi;
import id.or.qodr.jadwalkajian.api.RestClient;
import id.or.qodr.jadwalkajian.data.SessionManager;
import id.or.qodr.jadwalkajian.data.UtilsDate;
import id.or.qodr.jadwalkajian.helper.ViewPagerHelper;
import id.or.qodr.jadwalkajian.helper.sqlite.SQLiteHandler;
import id.or.qodr.jadwalkajian.home.TabThisWeek;
import id.or.qodr.jadwalkajian.home.TabToday;
import id.or.qodr.jadwalkajian.model.Jadwal;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSIONS_REQUEST_FINE_LOCATION = 111;
    private static final int PLACE_PICKER_REQUEST = 1;
    private static final int PICK_IMAGE_REQUEST = 2;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private Toolbar toolbar;
    private MaterialDialog dialog;
    private FloatingActionButton fab;
    private TabLayout tabLayout;
    private ViewPager viewpager;
    private ArrayList<Jadwal> jadwalist;
    private SessionManager session;
    private SQLiteHandler dblite;
    private ImageView image_masjid;
    private Spinner spinStiaphari;
    private EditText edtCp, edtTema, edtPemateri;
    private Button tglInput, btnPlace, btnPekan, timeMulai, timeSampai;
    private RadioButton rutin, tdkRutin;
    private TextView txtLng,txtLat,txtAdrress;
    private EditText input_fulname,input_name,input_pass,input_cfrpass;
    private Bitmap bitmap; View view_opt_rutin; LinearLayout opt_rutin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

        fab = (FloatingActionButton) findViewById(R.id.fab);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

        // Session Manager
        session = new SessionManager(getApplicationContext());

        //Check DetailToday Status Admin
        if (session.isLoggedIn()) {
            fab.setVisibility(View.VISIBLE);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    inputNewData();
                }
            });
        }

        // SQLite database handler
        dblite = new SQLiteHandler(getApplicationContext());
        if (jadwalist == null) {
            jadwalist = new ArrayList<Jadwal>();
        }

        setupViewPager(viewpager);
        tabLayout.setupWithViewPager(viewpager);

        fetchDataFromServer();
    }

    void initView(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        viewpager = (ViewPager) findViewById(R.id.viewpager);
    }


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerHelper adapter = new ViewPagerHelper(getSupportFragmentManager());
        adapter.addFragment(new TabToday(), "HARI INI");
        adapter.addFragment(new TabThisWeek(), "PEKAN INI");
        viewPager.setAdapter(adapter);
        viewpager.getAdapter().notifyDataSetChanged();
    }

    public void setActionbarTitle(String title){
        TextView textTitle = (TextView) findViewById(R.id.toolbar_title);
        textTitle.setText(title);
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            new AlertDialog.Builder(this)
                    .setTitle("Close Application")
                    .setMessage("Are you sure you want to exit?")
                    .setNegativeButton(android.R.string.no, null)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                            Intent intent = new Intent(MainActivity.this,
                                    SplashScreen.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.putExtra("EXIT", true);
                            startActivity(intent);
                        }
                    }).create().show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (session.isLoggedIn()){
            getMenuInflater().inflate(R.menu.admin_menu, menu);
        }else {
            getMenuInflater().inflate(R.menu.main_menu, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        HashMap<String, String> user = session.getUserDetails();
        final String _id = user.get(SessionManager.KEY_ID);
        String fulname = user.get(SessionManager.KEY_FULNAME);
        String name = user.get(SessionManager.KEY_NAME);
        final String pass = user.get(SessionManager.KEY_PASS);

        switch (item.getItemId()) {
            case R.id.menu_login:
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                return true;
            case R.id.menu_profil:
                AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                LayoutInflater inflat = this.getLayoutInflater();
                View dialog = inflat.inflate(R.layout.view_profile_admin, null);
                TextView txt_fulname = (TextView) dialog.findViewById(R.id.fullname);
                TextView txt_name = (TextView) dialog.findViewById(R.id.txtusername);
                final TextView txt_pass = (TextView) dialog.findViewById(R.id.txtpassword);
                final ImageButton show_pass = (ImageButton) dialog.findViewById(R.id.show_pass);
                final ImageButton hide_pass = (ImageButton) dialog.findViewById(R.id.hide_pass);

                final String pas_uniq = "******";
                txt_fulname.setText("Fullname : "+fulname);
                txt_name.setText("Username : "+name);
                txt_pass.setText("Password : "+pas_uniq);
                show_pass.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        txt_pass.setText("Password : "+pass);
                        show_pass.setVisibility(View.GONE);
                        hide_pass.setVisibility(View.VISIBLE);
                    }
                });
                hide_pass.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        txt_pass.setText("Password : "+pas_uniq);
                        hide_pass.setVisibility(View.GONE);
                        show_pass.setVisibility(View.VISIBLE);
                    }
                });
                alert.setTitle("Profile Admin Account");

                alert.setView(dialog);

                // Set up the buttons
                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                alert.setCancelable(false);
                alert.show();
                return true;
            case R.id.menu_change_pass:
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                LayoutInflater inflater = this.getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.view_change_password, null);
                input_fulname = (EditText) dialogView.findViewById(R.id.edt_fulname_form);
                input_name = (EditText) dialogView.findViewById(R.id.edt_user_form);
                input_pass = (EditText) dialogView.findViewById(R.id.edt_pass_form);
                input_cfrpass = (EditText) dialogView.findViewById(R.id.edt_cfrpass_form);

                input_fulname.setText(fulname);
                input_name.setText(name);
                input_pass.setText(pass);
                input_cfrpass.setText(pass);
                builder.setTitle("Change Data Account");

                builder.setView(dialogView);

                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String fulnamUpdate = input_fulname.getText().toString();
                        String namUpdate = input_name.getText().toString();
                        String passUpdate = input_pass.getText().toString();
                        String cfrpassUpdate = input_cfrpass.getText().toString();

                        //update the nama
                        HashMap<String, String> params = new HashMap<String, String>();
                        if (validateAccount()){
                            if (passUpdate.equals(cfrpassUpdate)) {
                                SimpleDateFormat currentDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                Date date = new Date();
                                params.put("id", _id);
                                params.put("full_name", fulnamUpdate);
                                params.put("username", namUpdate);
                                params.put("password", passUpdate);
                                params.put("created_at", currentDate.format(date));
                                params.put("updated_at", currentDate.format(date));
                                session.createLoginSession(_id, fulnamUpdate, namUpdate, passUpdate);
                                changeUserAccount(params);
                                Toast.makeText(getApplicationContext(), _id + "\n " + fulnamUpdate + "\n " + namUpdate + " " + passUpdate, Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(getApplicationContext(), "Password don't match", Toast.LENGTH_LONG).show();
                            }
                        }else {
                            Toast.makeText(getApplicationContext(), "Please insert correctly", Toast.LENGTH_LONG).show();
                        }

                    }
                });
                builder.setNegativeButton("Kembali", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.setCancelable(false);
                builder.show();
                return true;
            case R.id.menu_logout:
                // Clear the session data
                // This will clear all session data and
                // redirect user to LoginActivity
                fab.setVisibility(View.GONE);
                session.logoutUser();
                Intent intent = new Intent(MainActivity.this,
                        LoginActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void inputNewData(){
        boolean wrapInScrollView = true;
        dialog = new MaterialDialog.Builder(this)
                .title(R.string.title_input_kajian)
                .customView(R.layout.view_admin_input,wrapInScrollView)
                .positiveText(android.R.string.ok)
                .negativeText(android.R.string.cancel)
                .cancelable(false)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialogview) {
                        super.onPositive(dialogview);
                        opt_rutin = (LinearLayout) dialogview.getCustomView().findViewById(R.id.opt_rutin);
                        view_opt_rutin = (View) dialogview.getCustomView().findViewById(R.id.view_opt_rutin);
                        rutin = (RadioButton) dialogview.getCustomView().findViewById(R.id.rutin);
                        tdkRutin = (RadioButton) dialogview.getCustomView().findViewById(R.id.tdkRutin);
                        spinStiaphari = (Spinner) dialogview.getCustomView().findViewById(R.id.spin_stiaphari);
                        edtCp = (EditText) dialogview.getCustomView().findViewById(R.id.edt_cp);
                        edtTema = (EditText) dialogview.getCustomView().findViewById(R.id.edt_tema);
                        edtPemateri = (EditText) dialogview.getCustomView().findViewById(R.id.edt_pemateri);
                        btnPekan = (Button) dialogview.getCustomView().findViewById(R.id.btn_pekan);
                        tglInput = (Button) dialogview.getCustomView().findViewById(R.id.tgl_input);
                        timeMulai = (Button) dialogview.getCustomView().findViewById(R.id.time_mulai);
                        timeSampai = (Button) dialogview.getCustomView().findViewById(R.id.time_sampai);
                        btnPlace = (Button) dialogview.getCustomView().findViewById(R.id.place);
                        txtAdrress = (TextView) dialogview.getCustomView().findViewById(R.id.place_address);
                        txtLat= (TextView) dialogview.getCustomView().findViewById(R.id.place_lat);
                        txtLng= (TextView) dialogview.getCustomView().findViewById(R.id.place_lng);
                        image_masjid = (ImageView) dialogview.getCustomView().findViewById(R.id.image_masjid);

                        HashMap<String, String> params = new HashMap<String, String>();
                        String poster = getStringImage(bitmap);

                        if (rutin.isChecked()){
                            if (validateInputRutin()){
                                params.put("jenis_kajian", rutin.getText().toString());
                                params.put("foto_masjid", poster);
                                params.put("setiap_hari", spinStiaphari.getSelectedItem().toString());
                                params.put("pekan", btnPekan.getText().toString());
                                params.put("tanggal", "");
                                params.put("mulai", timeMulai.getText().toString());
                                params.put("sampai", timeSampai.getText().toString());
                                params.put("tema", edtTema.getText().toString());
                                params.put("pemateri", edtPemateri.getText().toString());
                                params.put("lokasi", btnPlace.getText().toString());
                                params.put("alamat", txtAdrress.getText().toString());
                                params.put("lat", txtLat.getText().toString());
                                params.put("lng", txtLng.getText().toString());
                                params.put("cp", edtCp.getText().toString());
                                Log.i("TAG", "onPositive Rutin: "+params);
                                sendNewDataKajian(params);
                            }else {
                                Toast.makeText(MainActivity.this, "isi dengan benar", Toast.LENGTH_SHORT).show();
                            }
                        }else if (tdkRutin.isChecked()){
                            if (validateInputPekan()){
                                params.put("jenis_kajian", tdkRutin.getText().toString());
                                params.put("foto_masjid", poster);
                                params.put("setiap_hari", "");
                                params.put("pekan", "");
                                params.put("tanggal", tglInput.getText().toString());
                                params.put("mulai", timeMulai.getText().toString());
                                params.put("sampai", timeSampai.getText().toString());
                                params.put("tema", edtTema.getText().toString());
                                params.put("pemateri", edtPemateri.getText().toString());
                                params.put("lokasi", btnPlace.getText().toString());
                                params.put("alamat", txtAdrress.getText().toString());
                                params.put("lat", txtLat.getText().toString());
                                params.put("lng", txtLng.getText().toString());
                                params.put("cp", edtCp.getText().toString());
                                Log.i("TAG", "onPositive tdkRutin: "+params);
                                sendNewDataKajian(params);
                            }else{
                                Toast.makeText(MainActivity.this, "isi dengan benar", Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(MainActivity.this, "Pilih Jenis Kajian dulu", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onNegative(MaterialDialog dialogview) {
                        super.onNegative(dialogview);
                    }
                })
                .build();
        opt_rutin = (LinearLayout) dialog.getCustomView().findViewById(R.id.opt_rutin);
        view_opt_rutin = (View) dialog.getCustomView().findViewById(R.id.view_opt_rutin);
        rutin = (RadioButton) dialog.getCustomView().findViewById(R.id.rutin);
        rutin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rutin.isChecked()) rutin.setBackgroundResource(R.drawable.clickable_style);
                rutin.setTextColor(getResources().getColor(R.color.white));
                tdkRutin.setBackgroundResource(R.drawable.rounded_style);
                tdkRutin.setTextColor(getResources().getColor(R.color.mdtp_numbers_text_color));
                checkJenisRutin();
            }
        });
        tdkRutin = (RadioButton) dialog.getCustomView().findViewById(R.id.tdkRutin);
        tdkRutin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tdkRutin.isChecked()) tdkRutin.setBackgroundResource(R.drawable.clickable_style);
                tdkRutin.setTextColor(getResources().getColor(R.color.white));
                rutin.setBackgroundResource(R.drawable.rounded_style);
                rutin.setTextColor(getResources().getColor(R.color.mdtp_numbers_text_color));
                checkJenisTdkRutin();
            }
        });


        spinStiaphari= (Spinner) dialog.getCustomView().findViewById(R.id.spin_stiaphari);
        ArrayAdapter<String> hariAdapter = new ArrayAdapter<String>(getApplicationContext(),
                R.layout.view_spinner_item, ID_SETIAP_HARI);
        hariAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinStiaphari.setAdapter(hariAdapter);

        btnPekan = (Button) dialog.getCustomView().findViewById(R.id.btn_pekan);
        final String[] listPekan = new String[] {"1","2","3","4","5"};
        final boolean[] checkedPekan = new boolean[listPekan.length];
        final ArrayList<Integer> mItemsPekan = new ArrayList<>();
        btnPekan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialogPekan = new AlertDialog.Builder(MainActivity.this);
                dialogPekan.setTitle("Select Pekan");
                dialogPekan.setMultiChoiceItems(listPekan, checkedPekan, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int indexSelected, boolean isChecked) {
                        if (isChecked) {
                            // If the user checked the item, add it to the selected items
                            if (!mItemsPekan.contains(indexSelected)){
                                mItemsPekan.add(indexSelected);
                            }
                        } else if (mItemsPekan.contains(indexSelected)) {
                            // Else, if the item is already in the array, remove it
                            mItemsPekan.remove(Integer.valueOf(indexSelected));
                        }
                    }
                });

                dialogPekan.setCancelable(false);
                dialogPekan.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        String item = "";
                        for (int i = 0; i < mItemsPekan.size(); i++) {
                            item = item + listPekan[mItemsPekan.get(i)];
                            if (i != mItemsPekan.size() -1) {
                                item = item + ", ";
                            }
                        }
                        btnPekan.setText(item);
                    }
                });

                dialogPekan.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        //  Your code when user clicked on Cancel
                        dialog.dismiss();
                    }
                });

                dialogPekan.setNeutralButton("Clear List", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for (int i = 0; i < checkedPekan.length ; i++) {
                            checkedPekan[i] = false;
                            mItemsPekan.clear();
                            btnPekan.setText("Pilih Pekan");
                        }
                    }
                });
                AlertDialog mDilog = dialogPekan.create();
                mDilog.show();
            }
        });

        image_masjid = (ImageView) dialog.getCustomView().findViewById(R.id.image_masjid);
        image_masjid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence[] dialogitem = {"Galery", "Camera"};
                AlertDialog.Builder imgDialog = new AlertDialog.Builder(MainActivity.this);
                imgDialog.setCancelable(true);
                imgDialog.setItems(dialogitem, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub

                        switch (which) {
                            case 0:
                                showFileChooser();
                                break;
                            case 1:
                                captureImage();
                                break;
                        }
                    }
                }).show();
            }
        });

        tglInput = (Button) dialog.getCustomView().findViewById(R.id.tgl_input);
        tglInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePickerDialog v, int year, int monthOfYear, int dayOfMonth) {
                                String date =  year + ":" + (monthOfYear + 1) + ":" + dayOfMonth;
                                tglInput.setText(date);
                            }
                        },
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.setVersion(DatePickerDialog.Version.VERSION_2);
                dpd.show(getFragmentManager(), "Tanggal");

            }
        });

        timeMulai = (Button) dialog.getCustomView().findViewById(R.id.time_mulai);
        timeMulai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar nowTime = Calendar.getInstance();
                TimePickerDialog tpd = TimePickerDialog.newInstance(
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
                                String time = hourOfDay + ":" + minute;
                                timeMulai.setText(time);
                            }
                        },
                        nowTime.get(Calendar.HOUR_OF_DAY),
                        nowTime.get(Calendar.MINUTE),
                        true
                );
                tpd.setVersion(TimePickerDialog.Version.VERSION_2);
                tpd.setAccentColor(getResources().getColor(R.color.colorPrimary));
                tpd.show(getFragmentManager(), "Mulai");
            }
        });
        timeSampai = (Button) dialog.getCustomView().findViewById(R.id.time_sampai);
        timeSampai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar nowTimes = Calendar.getInstance();
                TimePickerDialog ttpd = TimePickerDialog.newInstance(
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {

                                String time2 = hourOfDay + ":" + minute;

                                timeSampai.setText(time2);
                            }
                        },
                        nowTimes.get(Calendar.HOUR_OF_DAY),
                        nowTimes.get(Calendar.MINUTE),
                        true
                );
                ttpd.setVersion(TimePickerDialog.Version.VERSION_2);
                ttpd.setAccentColor(getResources().getColor(R.color.colorPrimary));
                ttpd.show(getFragmentManager(), "Sampai");
            }
        });
        btnPlace = (Button) dialog.getCustomView().findViewById(R.id.place);
        txtAdrress = (TextView) dialog.getCustomView().findViewById(R.id.place_address);
        txtLat= (TextView) dialog.getCustomView().findViewById(R.id.place_lat);
        txtLng= (TextView) dialog.getCustomView().findViewById(R.id.place_lng);
        btnPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                            PERMISSIONS_REQUEST_FINE_LOCATION);

                    Toast.makeText(MainActivity.this, getString(R.string.need_location_permission_message), Toast.LENGTH_LONG).show();

                    return;
                }
                try {
                    // Start a new Activity for the Place Picker API, this will trigger {@code #onActivityResult}
                    // when a place is selected or with the user cancels.
                    PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                    Intent i = builder.build(MainActivity.this);
                    startActivityForResult(i, PLACE_PICKER_REQUEST);

                } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                    Log.e("TAG", String.format("GooglePlayServices Not Available [%s]", e.getMessage()));
                } catch (Exception e) {
                    Log.e("TAG", String.format("PlacePicker Exception: %s", e.getMessage()));
                }
            }
        });

        dialog.show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST && resultCode == RESULT_OK) {
            Place place = PlacePicker.getPlace(this, data);
            if (place == null) {
                Log.v("TAG", "No place selected");
                return;
            }

            // Extract the place information from the API
            String str_place_name = place.getName().toString();
            String str_place_address = place.getAddress().toString();
            String placeLatLng = place.getLatLng().toString();
            String removeStr1 = placeLatLng.replace("lat/lng: (","");
            String removeStr2 = removeStr1.replace(")","");
            String splitWithComa = ",";
            String[] str_place_latLng = removeStr2.split(splitWithComa);
            btnPlace.setText(str_place_name);
            txtAdrress.setText(str_place_address);
            txtLat.setText(str_place_latLng[0]);
            txtLng.setText(str_place_latLng[1]);
        } else if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePatch = data.getData();
            try {
                //Getting the Bitmap from Gallery
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePatch);
                Glide.with(MainActivity.this).load(filePatch).into(image_masjid);
                final int sdk = android.os.Build.VERSION.SDK_INT;
                if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    image_masjid.setBackgroundDrawable( getResources().getDrawable(R.drawable.rounded_style) );
                } else {
                    image_masjid.setBackground( getResources().getDrawable(R.drawable.rounded_style));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE && resultCode == RESULT_OK) {
            Uri filePatch = data.getData();
            try {
                //Getting the Bitmap from Gallery
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePatch);
                Glide.with(MainActivity.this).load(filePatch).into(image_masjid);
                final int sdk = android.os.Build.VERSION.SDK_INT;
                if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    image_masjid.setBackgroundDrawable( getResources().getDrawable(R.drawable.rounded_style) );
                } else {
                    image_masjid.setBackground( getResources().getDrawable(R.drawable.rounded_style));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean validateInputRutin() {
        if (spinStiaphari.getSelectedItemPosition()==0){
            TextView erorText = (TextView)spinStiaphari.getSelectedView();
            erorText.setError("null");
            return false;
        }else if (btnPekan.getText().toString().equals("Pilih Pekan")){
            btnPekan.setError("error");
            return false;
        }else if (timeMulai.getText().toString().equals("From")){
            timeMulai.setError("error");
            return false;
        }else if (timeSampai.getText().toString().equals("To")){
            timeSampai.setError("null");
            return false;
        }else if (TextUtils.isEmpty(edtPemateri.getText())){
            edtPemateri.setError("kosong");
            return false;
        }else if (TextUtils.isEmpty(edtTema.getText())){
            edtTema.setError("kosong");
            return false;
        }else if (btnPlace.getText().toString().equals("Place")){
            btnPlace.setError("kosong");
            return false;
        }else if (bitmap == null){
            image_masjid.setBackgroundColor(getResources().getColor(R.color.alert));
            Toast.makeText(this, "Tambahkan gambar", Toast.LENGTH_SHORT).show();
            return false;
        }else if (TextUtils.isEmpty(edtCp.getText())){
            edtCp.setError("kosong");
            return false;
        }else {
            Toast.makeText(this, "isi dengan benar", Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    private boolean validateInputPekan() {
        if (tglInput.getText().toString().equals("Date")){
            tglInput.setError("error");
            return false;
        }else if (timeMulai.getText().toString().equals("From")){
            timeMulai.setError("error");
            return false;
        }else if (timeSampai.getText().toString().equals("To")){
            timeSampai.setError("null");
            return false;
        }else if (btnPlace.getText().toString().equals("Place")){
            btnPlace.setError("kosong");
            return false;
        }else if (TextUtils.isEmpty(edtPemateri.getText())){
            edtPemateri.setError("kosong");
            return false;
        }else if (TextUtils.isEmpty(edtTema.getText())){
            edtTema.setError("kosong");
            return false;
        }else if (bitmap == null){
            image_masjid.setBackgroundColor(getResources().getColor(R.color.alert));
            Toast.makeText(this, "Tambahkan gambar", Toast.LENGTH_SHORT).show();
            return false;
        }else if (image_masjid.getDrawable() == null){
            Toast.makeText(this, "Tambahkan gambar", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(edtCp.getText())){
            edtCp.setError("kosong");
            return false;
        }
        return true;
    }

    private boolean validateAccount() {
        if (TextUtils.isEmpty(input_fulname.getText())){
            input_fulname.setError("kosong");
            return false;
        }else if (TextUtils.isEmpty(input_name.getText())) {
            input_name.setError("kosong");
            return false;
        }else if (TextUtils.isEmpty(input_pass.getText())) {
            input_pass.setError("kosong");
            return false;
        }else if (TextUtils.isEmpty(input_cfrpass.getText())) {
            input_cfrpass.setError("kosong");
            return false;
        }
        return true;
    }

    private void checkJenisRutin() {
        if (rutin.isChecked()) {
            tglInput.setEnabled(false);
            view_opt_rutin.setVisibility(View.VISIBLE);
            opt_rutin.setVisibility(View.VISIBLE);
        }
    }

    private void checkJenisTdkRutin() {
        if (tdkRutin.isChecked()) {
            opt_rutin.setVisibility(View.GONE);
            tglInput.setEnabled(true);
            view_opt_rutin.setVisibility(View.GONE);
        }
    }

    /**
     * Launching camera app to capture image
     */
    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // start the image capture Intent
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }

    public String getStringImage(Bitmap bmp){
        if (bitmap == null) {
            return null;
        } else {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 75, baos);
            byte[] imageBytes = baos.toByteArray();
            String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
            return encodedImage;
        }
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    private static final String[] ID_SETIAP_HARI = new String[]{
            "Pilih Hari ", "Senin", "Selasa", "Rabu", "Kamis", "Jum'at", "Sabtu", "Ahad"
    };

    private void sendNewDataKajian(Map<String, String> paramas) {
        final ProgressDialog loading = ProgressDialog.show(this,"Uploading data...","Please wait...",false,false);

        OkHttpClient.Builder okhttpBuilder = new OkHttpClient.Builder();

        //LoggingInterceptor
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();

        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        okhttpBuilder.addInterceptor(logging);

        Retrofit retrofit = RestClient.getClient(okhttpBuilder).build();
        RestApi user = retrofit.create(RestApi.class);

        Call<ResponseBody> call = user.submitNewKajian(paramas);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                loading.dismiss();
                Toast.makeText(MainActivity.this, "sukses", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                loading.dismiss();
                Toast.makeText(MainActivity.this, "fail", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void changeUserAccount(Map<String, String> params) {
        final ProgressDialog loading = ProgressDialog.show(this,"Loading...","Please wait...",false,false);

        OkHttpClient.Builder okhttpBuilder = new OkHttpClient.Builder();

        //LoggingInterceptor
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();

        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        okhttpBuilder.addInterceptor(logging);

        Retrofit retrofit = RestClient.getClient(okhttpBuilder).build();
        RestApi user = retrofit.create(RestApi.class);

        Call<ResponseBody> call = user.updateUserAdmin(params);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loading.dismiss();
                        Toast.makeText(getApplicationContext(), "Successfuly Change Password", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loading.dismiss();
                        Toast.makeText(getApplicationContext(), "Error to Change Password!!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    void fetchDataFromServer() {
        final ProgressDialog loading = ProgressDialog.show(this,"Loading...","Please wait...",false,false);

        OkHttpClient.Builder okhttpBuilder = new OkHttpClient.Builder();

        //LoggingInterceptor
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();

        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        okhttpBuilder.addInterceptor(logging);

        Retrofit retrofit = RestClient.getClient(okhttpBuilder).build();
        RestApi user = retrofit.create(RestApi.class);

        Call<ResponseBody> call = user.getAllDataFromServer();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            loading.dismiss();
                            String strResponse = response.body().string();
                            JSONObject jsonObj = new JSONObject(strResponse);
                            if (jsonObj.getBoolean("result")) {
                                JSONArray array = jsonObj.getJSONArray("jadwal");
                                dblite.deleteIFLoad();
                                for(int i = 0; i < array.length(); i++) {
                                    Jadwal jdwalmodel = new Jadwal();
                                    JSONObject addToObj = array.getJSONObject(i);
                                    jdwalmodel.setId(addToObj.getInt("id"));
                                    jdwalmodel.setJenis_kajian(addToObj.getString("jenis_kajian"));
                                    jdwalmodel.setFoto_masjid(addToObj.getString("foto_masjid"));
                                    jdwalmodel.setPekan(addToObj.getString("pekan"));
                                    jdwalmodel.setTanggal(addToObj.getString("tanggal"));
                                    jdwalmodel.setMulai(addToObj.getString("mulai"));
                                    jdwalmodel.setSampai(addToObj.getString("sampai"));
                                    jdwalmodel.setTema(addToObj.getString("tema"));
                                    jdwalmodel.setPemateri(addToObj.getString("pemateri"));
                                    jdwalmodel.setLokasi(addToObj.getString("lokasi"));
                                    jdwalmodel.setAlamat(addToObj.getString("alamat"));
                                    jdwalmodel.setLat(addToObj.getString("lat"));
                                    jdwalmodel.setLng(addToObj.getString("lng"));
                                    jdwalmodel.setCp(addToObj.getString("cp"));
                                    jadwalist.add(jdwalmodel);
                                    // Inserting row in users table
                                    dblite.addKajian(jdwalmodel);
                                    Log.i("TAG", "run: "+jdwalmodel.getId());
                                }
                                Log.i("TAG", "run: "+"DATA SAVED");
                            }
                            Log.v("TAG", "onResponse: MainActivity " +strResponse);
                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                loading.dismiss();
            }
        });
    }

}

