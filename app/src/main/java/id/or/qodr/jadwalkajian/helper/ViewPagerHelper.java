package id.or.qodr.jadwalkajian.helper;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by adul on 01/08/17.
 */

public class ViewPagerHelper extends FragmentPagerAdapter {

    private final List<Fragment> kajianList = new ArrayList<>();
    private final List<String> titleKajianList = new ArrayList<>();

    public ViewPagerHelper(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return kajianList.get(position);
    }

    @Override
    public int getCount() {
        return kajianList.size();
    }

    public void addFragment(Fragment fragment, String title) {
        kajianList.add(fragment);
        titleKajianList.add(title);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titleKajianList.get(position);
    }
}
