package com.umobilized.helpers.views.adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

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
