package com.moliying.jzc.bookstore.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.moliying.jzc.bookstore.ui.fragment.CategoryFragment;

import java.util.ArrayList;

/**
 * Created by Jzc on 2016/8/15.
 */
public class CategoryFragmentPageAdapter extends FragmentPagerAdapter{
    ArrayList<CategoryFragment> fragments;
    public CategoryFragmentPageAdapter(FragmentManager fm, ArrayList<CategoryFragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }
    @Override
    public CharSequence getPageTitle(int position) {
        return fragments.get(position).title;
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
