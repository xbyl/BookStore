package com.moliying.jzc.bookstore.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by Jzc on 2016/8/17.
 */
public class DetailFragmentPagerAdapter extends FragmentPagerAdapter {
    public static final String [] CATEGROYS = {"内容","作者","目录","出版"};

    ArrayList<Fragment> fragments;

    public DetailFragmentPagerAdapter(FragmentManager fm, ArrayList<Fragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return CATEGROYS[position];
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
