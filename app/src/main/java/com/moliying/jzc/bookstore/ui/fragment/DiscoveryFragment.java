package com.moliying.jzc.bookstore.ui.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.moliying.jzc.bookstore.R;
import com.moliying.jzc.bookstore.adapter.CategoryFragmentPageAdapter;
import com.moliying.jzc.bookstore.vo.Categroy;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class DiscoveryFragment extends BaseFragment {
    @BindView(R.id.tabs)
    PagerSlidingTabStrip mTabs;
    @BindView(R.id.view_pager_discovery)
    ViewPager mViewPagerDiscovery;
    @BindView(R.id.TextView_search)
    TextView mTextViewSearch;

    @BindView(R.id.include_layout_loading_discover)
    LinearLayout mIncludeLayoutLoading;
    private ArrayList<CategoryFragment> fragments = new ArrayList<>();

    public DiscoveryFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_discovery, container, false);
        ButterKnife.bind(this, view);
        loading();
        return view;
    }

    private void loading() {
        BmobQuery<Categroy> query = new BmobQuery<>();
        query.findObjects(new FindListener<Categroy>() {
            @Override
            public void done(List<Categroy> list, BmobException e) {
                if (e == null) {
                    for (final Categroy categroy : list) {
                        fragments.add(CategoryFragment.newInstance
                                (categroy.getObjectId(), categroy.getCategoryName()));

                    }
                    LoadingCategory();
                }
            }
        });
    }

    boolean loadComplete = true;

    private void LoadingCategory() {
        CategoryFragmentPageAdapter pageAdapter = new CategoryFragmentPageAdapter
                (getActivity().getSupportFragmentManager(), fragments);
        mViewPagerDiscovery.setAdapter(pageAdapter);
        mTabs.setViewPager(mViewPagerDiscovery);
        if (loadComplete) {
            mIncludeLayoutLoading.setVisibility(View.GONE);
            loadComplete = false;
        }
    }

    public static DiscoveryFragment getInstance() {
        DiscoveryFragment discoveryFragment = new DiscoveryFragment();
        return discoveryFragment;
    }

    @OnClick(R.id.TextView_search)
    public void onClick() {

    }
}
