package id.or.qodr.jadwalkajian;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import id.or.qodr.jadwalkajian.api.RestApi;
import id.or.qodr.jadwalkajian.api.RestClient;
import id.or.qodr.jadwalkajian.data.SessionManager;
import id.or.qodr.jadwalkajian.model.Jadwal;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    private TextInputEditText input_user, input_pass;
    private Button login;
    private SessionManager session;
    private String jsonUsername,jsonFullname,jsonId;
    private String edt_pasword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Session Manager
        session = new SessionManager(getApplicationContext());

        input_user = (TextInputEditText) findViewById(R.id.input_email);
        input_pass = (TextInputEditText) findViewById(R.id.input_password);
        login = (Button) findViewById(R.id.btn_login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = input_user.getText().toString();
                edt_pasword = input_pass.getText().toString();

                if (username.trim().length() > 0 && edt_pasword.trim().length() > 0){
                    if(!username.trim().isEmpty() && !edt_pasword.trim().isEmpty()){

                        getLoginDataFromServer(username,edt_pasword);
                    }else{
                        Snackbar.make(login, "Username/Password is incorrect",Snackbar.LENGTH_SHORT).show();
                    }
                }else {
                    Snackbar.make(login, "Please enter username and password",Snackbar.LENGTH_SHORT).show();
                }
            }
        });

    }

    void getLoginDataFromServer(String username, String password) {
        final ProgressDialog loading = ProgressDialog.show(this,"Loading...","Please wait...",false,false);

        OkHttpClient.Builder okhttpBuilder = new OkHttpClient.Builder();

        //LoggingInterceptor
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();

        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        okhttpBuilder.addInterceptor(logging);

        Retrofit retrofit = RestClient.getClient(okhttpBuilder).build();
        RestApi user = retrofit.create(RestApi.class);

        Call<ResponseBody> call = user.getLoginAdmin(username,password);
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
                                JSONArray array = jsonObj.getJSONArray("users");
                                for(int i = 0; i < array.length(); i++) {

                                    JSONObject addToObj = array.getJSONObject(i);
                                    String id = addToObj.getString("id");
                                    String fulname = addToObj.getString("full_name");
                                    String uname = addToObj.getString("username");
                                    jsonId = id;
                                    jsonFullname = fulname;
                                    jsonUsername = uname;
                                    Log.v("TAG", jsonId+" "+jsonUsername+" "+jsonFullname+" "+edt_pasword);

                                }
                                session.createLoginSession(jsonId,jsonFullname,jsonUsername, edt_pasword);
                                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(i);
                                Toast.makeText(getApplicationContext(), "Login Success", Toast.LENGTH_LONG).show();
                            }else {
                                Snackbar.make(login," Username/Password is incorrect",Snackbar.LENGTH_SHORT).show();
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                loading.dismiss();
                Snackbar.make(login," Internet Error",Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(i);
    }
}

