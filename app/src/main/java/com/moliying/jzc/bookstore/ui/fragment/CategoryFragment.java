package com.moliying.jzc.bookstore.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.moliying.jzc.bookstore.R;
import com.moliying.jzc.bookstore.adapter.ListViewAdapter;
import com.moliying.jzc.bookstore.vo.BookInfo;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;


public class CategoryFragment extends BaseFragment {
    public String title;
    List<BookInfo> list;
    @BindView(R.id.listView_category)
    ListView mListViewCategory;
    ListViewAdapter listViewAdapter;
    public static CategoryFragment newInstance(List<BookInfo> list, String categoryName) {
        CategoryFragment fragment = new CategoryFragment();
        fragment.title = categoryName;
        fragment.list = list;
        return fragment;
    }

    public static CategoryFragment newInstance() {
        CategoryFragment fragment = new CategoryFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        ButterKnife.bind(this, view);
        listViewAdapter = new ListViewAdapter(getActivity(),list);
        mListViewCategory.setAdapter(listViewAdapter);
        for (final BookInfo bookInfo : list) {
            BmobFile bmobfile = bookInfo.getBookImage();
            bmobfile.download(new DownloadFileListener() {
                @Override
                public void done(String s, BmobException e) {
                    if(e==null){
                        bookInfo.setBookImagepath(s);
                        listViewAdapter.notifyDataSetChanged();
                    }else{
                        System.out.println("下载失败："+e.getErrorCode()+","+e.getMessage());
                    }
                }
                @Override
                public void onProgress(Integer integer, long l) {
//                    Log.i("bmob","下载进度："+integer+","+l);
                }
            });
        }
        return view;
    }


}
