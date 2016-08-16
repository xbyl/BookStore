package com.moliying.jzc.bookstore.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RadioButton;

import com.moliying.jzc.bookstore.R;
import com.moliying.jzc.bookstore.adapter.MyViewPagerAdapter;
import com.moliying.jzc.bookstore.ui.fragment.DiscoveryFragment;
import com.moliying.jzc.bookstore.ui.fragment.HomeFragment;
import com.moliying.jzc.bookstore.ui.fragment.PersonalFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity implements ViewPager.OnPageChangeListener {

    @BindView(R.id.main_viewPager)
    ViewPager mMainViewPager;
    @BindView(R.id.radioButton_main)
    RadioButton mRadioButtonMain;
    @BindView(R.id.radioButton_find)
    RadioButton mRadioButtonFind;
    @BindView(R.id.radioButton_personal)
    RadioButton mRadioButtonPersonal;

    ArrayList<RadioButton> mRadioButtons = new ArrayList<>();
    ArrayList<Fragment> mFragments = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
    }

    protected void initView() {
        mRadioButtons.add(mRadioButtonMain);
        mRadioButtons.add(mRadioButtonFind);
        mRadioButtons.add(mRadioButtonPersonal);
        HomeFragment mHomeFragment = HomeFragment.getInstance();
        DiscoveryFragment mDiscoveryFragment = DiscoveryFragment.getInstance();
        PersonalFragment mPersonalFragment = PersonalFragment.getInstance();
        mFragments.add(mHomeFragment);
        mFragments.add(mDiscoveryFragment);
        mFragments.add(mPersonalFragment);
        MyViewPagerAdapter adapter = new MyViewPagerAdapter(getSupportFragmentManager(), mFragments);
        mMainViewPager.setAdapter(adapter);
        mMainViewPager.setCurrentItem(0);
        mMainViewPager.addOnPageChangeListener(this);
    }
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        mRadioButtons.get(position).setChecked(true);
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @OnClick({R.id.radioButton_main, R.id.radioButton_find, R.id.radioButton_personal})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.radioButton_main:
                mMainViewPager.setCurrentItem(0);
                break;
            case R.id.radioButton_find:
                mMainViewPager.setCurrentItem(1);
                break;
            case R.id.radioButton_personal:
                mMainViewPager.setCurrentItem(2);
                break;
        }
    }
}
