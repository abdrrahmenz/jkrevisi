package id.or.qodr.jadwalkajian.home;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import id.or.qodr.jadwalkajian.R;
import id.or.qodr.jadwalkajian.api.RestApi;
import id.or.qodr.jadwalkajian.api.RestClient;
import id.or.qodr.jadwalkajian.helper.AdapterToday;
import id.or.qodr.jadwalkajian.helper.sqlite.SQLiteHandler;
import id.or.qodr.jadwalkajian.model.Jadwal;
import id.or.qodr.jadwalkajian.model.JadwalResponse;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by adul on 01/08/17.
 */

public class TabToday extends Fragment {

    private RecyclerView rvKajian;
    private View rootView;
    private SwipeRefreshLayout swipeRefresh;
    private AdapterToday adapterToday;
    ArrayList<Jadwal> jadwals = new ArrayList<>();
    private SQLiteHandler dblite;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = container;
        Log.i("TAG", "onCreateView: TabToday");
        View view = inflater.inflate(R.layout.rcv_list_kajian, container, false);

        setHasOptionsMenu(true);

        dblite = new SQLiteHandler(getActivity());
        swipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefresh);
        rvKajian = (RecyclerView) view.findViewById(R.id.rcvKajian);

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL);
        rvKajian.setLayoutManager(layoutManager);
        rvKajian.setAdapter(new AdapterToday(dblite.getDataToday("2017-06-05"),R.layout.view_kajian_today,getActivity()));
//        fetchData();

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefresh.setRefreshing(false);

            }
        });

        View tabBar = view.findViewById(R.id.tab_layout);
        View coloredBackgroundView = view.findViewById(R.id.colored_background_view);
        View toolbarContainer = view.findViewById(R.id.toolbar_container);
        View toolbar = view.findViewById(R.id.toolbar_lis);

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i("TAG", "onPause: TabTOday");
    }

    @Override
    public void onResume() {
        super.onResume();
//        fetchData();
        Log.i("TAG", "onResume: TabToday");
    }

    private void fetchData() {
        final ProgressDialog loading = ProgressDialog.show(getActivity(),"Loading...","Please wait...",false,false);

        OkHttpClient.Builder okhttpBuilder = new OkHttpClient.Builder();

        //LoggingInterceptor
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();

        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        okhttpBuilder.addInterceptor(logging);

        Retrofit retrofit = RestClient.getClient(okhttpBuilder).build();
        RestApi user = retrofit.create(RestApi.class);

        Call<JadwalResponse> call = user.getKajianToday("2017-06-05");
        call.enqueue(new Callback<JadwalResponse>() {
            @Override
            public void onResponse(Call<JadwalResponse> call, final Response<JadwalResponse> response) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loading.dismiss();
                        JadwalResponse jadwal = response.body();
                        List<Jadwal> jadwals = response.body().getJadwal();
                        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL);
                        rvKajian.setLayoutManager(layoutManager);
                        rvKajian.setAdapter(new AdapterToday(jadwals,R.layout.view_kajian_today,getActivity()));
//                        Snackbar.make(rootView, jadwal.getResult(), Snackbar.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onFailure(Call<JadwalResponse> call, Throwable t) {
                Log.i("adul", "hari: " + t.toString());
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loading.dismiss();
                        Snackbar.make(rootView, "Internet error", Snackbar.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }


}
