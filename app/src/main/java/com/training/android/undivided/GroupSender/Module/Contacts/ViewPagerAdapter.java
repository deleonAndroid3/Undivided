package com.training.android.undivided.GroupSender.Module.Contacts;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hillary Briones on 1/28/2018.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private String[] mTitles;
    private List<Fragment> mFragmentList;



    public ViewPagerAdapter(FragmentManager fm, String[] titles) {
        super(fm);
        mTitles = titles;
        mFragmentList = new ArrayList<>();
    }

    public void addFragment(Fragment fragment) {
        mFragmentList.add(fragment);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList == null ? 0 : mFragmentList.size();
    }
    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }
}
