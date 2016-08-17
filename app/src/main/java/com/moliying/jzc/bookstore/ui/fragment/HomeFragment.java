package com.moliying.jzc.bookstore.ui.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.moliying.jzc.bookstore.R;
import com.moliying.jzc.bookstore.adapter.ListViewAdapter;
import com.moliying.jzc.bookstore.ui.BookDetailActivity;
import com.moliying.jzc.bookstore.vo.BookInfo;
import com.panxw.android.imageindicator.AutoPlayManager;
import com.panxw.android.imageindicator.ImageIndicatorView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;


public class HomeFragment extends BaseFragment implements AdapterView.OnItemClickListener {

    @BindView(R.id.image_indicater_view)
    ImageIndicatorView mImageIndicaterView;
    @BindView(R.id.books_home_listView)
    ListView mBooksHomeListView;
    @BindView(R.id.include_layout_loading_home)
    LinearLayout mIncludeLayoutLoadingHome;

//    private boolean loadComplete = true ;
    ArrayList<BookInfo> mBookInfos = new ArrayList<>();
    ListViewAdapter listViewAdapter;
    View mFragment;
    public HomeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(null == mFragment){
            mFragment = inflater.inflate(R.layout.fragment_home, container, false);
            ButterKnife.bind(this, mFragment);
            final Integer[] resArray = new Integer[]{R.mipmap.qdzt
                    , R.mipmap.qinghuabiancheng
                    , R.mipmap.scala};
            mImageIndicaterView.setupLayoutByDrawable(resArray);
            mImageIndicaterView.setIndicateStyle(ImageIndicatorView.INDICATE_USERGUIDE_STYLE);
            mImageIndicaterView.show();
            AutoPlayManager autoBrocastManager = new AutoPlayManager(mImageIndicaterView);
            autoBrocastManager.setBroadcastEnable(true);
            autoBrocastManager.setBroadCastTimes(5);//loop times
            autoBrocastManager.setBroadcastTimeIntevel(3 * 1000, 3 * 1000);//set first play time and interval
            autoBrocastManager.loop();

            listViewAdapter = new ListViewAdapter(getActivity(), mBookInfos);
            mBooksHomeListView.setAdapter(listViewAdapter);
            mBooksHomeListView.setOnItemClickListener(this);
            loading();
        }

        return mFragment;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(null != mFragment){
            ViewGroup group = (ViewGroup) mFragment.getParent();
            group.removeView(mFragment);
        }
    }

    private void loading() {
        BmobQuery<BookInfo> bmobQuery = new BmobQuery<>();
        bmobQuery.findObjects(new FindListener<BookInfo>() {
            @Override
            public void done(List<BookInfo> list, BmobException e) {
                if (e == null) {
                    mBookInfos.clear();
                    mBookInfos.addAll(list);
//                    if(loadComplete){
                        mIncludeLayoutLoadingHome.setVisibility(View.GONE);
//                        loadComplete = false;
//                    }
                    HomeFragment.this.notifyDataDownLoad();
//                    for (final BookInfo bookInfo : list) {
//                        BmobFile bmobfile = bookInfo.getBookImage();
//                        bmobfile.download(new DownloadFileListener() {
//                            @Override
//                            public void done(String s, BmobException e) {
//                                if (e == null) {
//                                    bookInfo.setBookImagepath(s);
//                                    listViewAdapter.notifyDataSetChanged();
//                                } else {
//                                    System.out.println("下载失败：" + e.getErrorCode() + "," + e.getMessage());
//                                }
//                            }
//
//                            @Override
//                            public void onProgress(Integer integer, long l) {
////                                    Log.i("bmob","下载进度："+integer+","+l);
//                            }
//                        });
//                    }
                } else {
                    System.out.println("查询失败：" + e.getMessage());
                }
            }
        });
    }

    private void notifyDataDownLoad() {
        listViewAdapter.setBookInfos(mBookInfos);
        listViewAdapter.notifyDataSetChanged();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public static HomeFragment getInstance() {
        HomeFragment homeFragment = new HomeFragment();
        return homeFragment;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        BookInfo bookInfo = mBookInfos.get(position);
        Intent intent = new Intent(getActivity(), BookDetailActivity.class);
        intent.putExtra("bookInfo",bookInfo);
        startActivity(intent);
    }
}
