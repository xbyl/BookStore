package com.moliying.jzc.bookstore.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.moliying.jzc.bookstore.R;
import com.moliying.jzc.bookstore.adapter.ListViewAdapter;
import com.moliying.jzc.bookstore.vo.BookInfo;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.FindListener;


public class CategoryFragment extends BaseFragment {
    public String title;
    public String categroyId;
    ArrayList<BookInfo> list = new ArrayList<>();
    @BindView(R.id.listView_category)
    ListView mListViewCategory;
    ListViewAdapter listViewAdapter;
    public static CategoryFragment newInstance(String categroyId, String categoryName) {
        CategoryFragment fragment = new CategoryFragment();
        fragment.title = categoryName;
        Bundle args = new Bundle();
        args.putString("title",categoryName);
        args.putString("categroyId",categroyId);
        //setArguments 设置 参数
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        ButterKnife.bind(this, view);
        title = getArguments().getString("title");
        categroyId = getArguments().getString("categroyId");
        listViewAdapter = new ListViewAdapter(getActivity(),list);
        mListViewCategory.setAdapter(listViewAdapter);
        loading();
        return view;
    }

    private void loading() {
        BmobQuery<BookInfo> bmobQuery = new BmobQuery<BookInfo>();
        bmobQuery.addWhereEqualTo("categoryId", categroyId);
        bmobQuery.findObjects(new FindListener<BookInfo>() {
            @Override
            public void done(List<BookInfo> list, BmobException e) {
                if (e == null) {
                    CategoryFragment.this.list.addAll(list);
//                    listViewAdapter.setBookInfos(CategoryFragment.this.list);
                    listViewAdapter.notifyDataSetChanged();
                    loadingImage();
                }
            }
        });
    }

    private void loadingImage() {
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
    }

}
