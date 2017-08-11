package id.or.qodr.jadwalkajian.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import id.or.qodr.jadwalkajian.R;
import id.or.qodr.jadwalkajian.helper.AdapterToday;
import id.or.qodr.jadwalkajian.helper.AdapterWeek;
import id.or.qodr.jadwalkajian.helper.sqlite.SQLiteHandler;

/**
 * Created by adul on 01/08/17.
 */

public class TabThisWeek extends Fragment {

    private RecyclerView rvKajian;
    private SwipeRefreshLayout swipeRefresh;
    private AdapterWeek adapterWeek;
    private SQLiteHandler dblite;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.rcv_list_kajian, container, false);

        setHasOptionsMenu(true);
        dblite = new SQLiteHandler(getActivity());

        swipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefresh);
        rvKajian = (RecyclerView) view.findViewById(R.id.rcvKajian);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL);
        rvKajian.setLayoutManager(layoutManager);
        rvKajian.setAdapter(new AdapterWeek(dblite.getDataWeek("2017-07-21","2017-07-29"),R.layout.view_kajian_week,getActivity()));

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
}
