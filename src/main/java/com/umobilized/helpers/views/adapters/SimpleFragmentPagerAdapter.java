package com.umobilized.helpers.views.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by tpaczesny on 2017-07-10.
 */

public class SimpleFragmentPagerAdapter extends FragmentPagerAdapter {

    private final Fragment[] mFragments;
    private final String[] mTitles;

    public SimpleFragmentPagerAdapter(FragmentManager fm, Fragment[] fragments, String[] titles) {
        super(fm);
        mFragments = fragments;
        mTitles = titles;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments[position];
    }

    @Override
    public int getCount() {
        return mFragments.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (mTitles != null) {
            return mTitles[position];
        } else {
            return super.getPageTitle(position);
        }
    }
}
