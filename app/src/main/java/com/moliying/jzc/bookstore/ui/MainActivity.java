package com.moliying.jzc.bookstore.ui;

import android.Manifest;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import com.moliying.jzc.bookstore.R;
import com.moliying.jzc.bookstore.adapter.MyViewPagerAdapter;
import com.moliying.jzc.bookstore.ui.fragment.DiscoveryFragment;
import com.moliying.jzc.bookstore.ui.fragment.HomeFragment;
import com.moliying.jzc.bookstore.ui.fragment.PersonalFragment;
import com.mylhyl.acp.Acp;
import com.mylhyl.acp.AcpListener;
import com.mylhyl.acp.AcpOptions;

import java.util.ArrayList;
import java.util.List;

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
//        initPermission();
    }

    private void initPermission() {
        Acp.getInstance(this).request(new AcpOptions.Builder()
                        .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE
                                , Manifest.permission.READ_EXTERNAL_STORAGE
                                , Manifest.permission.WAKE_LOCK
                                , Manifest.permission.READ_PHONE_STATE
                                , Manifest.permission.RECEIVE_BOOT_COMPLETED
                                , Manifest.permission.READ_LOGS
                                , Manifest.permission.GET_ACCOUNTS
                                , Manifest.permission.READ_CONTACTS
                                , Manifest.permission.SEND_SMS)
                /*以下为自定义提示语、按钮文字
                .setDeniedMessage()
                .setDeniedCloseBtn()
                .setDeniedSettingBtn()
                .setRationalMessage()
                .setRationalBtn()*/
                        .build(),
                new AcpListener() {
                    @Override
                    public void onGranted() {
                        Toast.makeText(MainActivity.this,  "权限申请成功", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onDenied(List<String> permissions) {
                        Toast.makeText(MainActivity.this, permissions.toString() + "权限拒绝", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    protected void onDestroy() {
        destroyView();
        super.onDestroy();
    }

    private void destroyView() {
        mMainViewPager.setAdapter(null);
        mFragments.clear();
        mFragments = null;
        mRadioButtons.clear();
        mRadioButtons = null;

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
