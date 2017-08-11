package id.or.qodr.jadwalkajian;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import id.or.qodr.jadwalkajian.helper.ViewPagerHelper;
import id.or.qodr.jadwalkajian.home.TabThisWeek;
import id.or.qodr.jadwalkajian.home.TabToday;

/**
 * Created by adul on 02/08/17.
 */

public class Home extends Fragment {

    private View rootView;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewpager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
//        setMenuVisibility(true);
        Log.v("TAG", "onCreate: Home");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = container;
        Log.i("TAG", "onCreateView: Home");
        View v = inflater.inflate(R.layout.view_home_activity, container, false);
//        setHasOptionsMenu(true);
        ((MainActivity) getActivity()).setActionbarTitle("Jadwal Kajian");
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        toolbar = (Toolbar) v.findViewById(R.id.toolbar);
        tabLayout = (TabLayout) v.findViewById(R.id.tabs);
        viewpager = (ViewPager) v.findViewById(R.id.viewpager);

        setupViewPager(viewpager);
        tabLayout.setupWithViewPager(viewpager);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
//        TabToday loginFrag = new TabToday();
//        getActivity().getSupportFragmentManager()
//                .beginTransaction()
//                .add(R.id.container, loginFrag)
//                .commit();
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerHelper adapter = new ViewPagerHelper(getActivity().getSupportFragmentManager());
        adapter.addFragment(new TabToday(), "HARI INI");
        adapter.addFragment(new TabThisWeek(), "PEKAN INI");
        viewPager.setAdapter(adapter);
        viewpager.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        Log.v("TAG", "onPrepareOptionsMenu: ");
//        menu.findItem(R.id.menu_login).setVisible(true);
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.v("TAG", "onCreateOptionsMenu: ");
//        getActivity().getMenuInflater().inflate(R.menu.main_menu, menu);
//        inflater.inflate(R.menu.main_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }
}
