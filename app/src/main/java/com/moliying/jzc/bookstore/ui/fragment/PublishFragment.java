package com.moliying.jzc.bookstore.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.moliying.jzc.bookstore.R;
import com.moliying.jzc.bookstore.adapter.PublishListViewAdapter;
import com.moliying.jzc.bookstore.vo.BookInfo;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PublishFragment extends Fragment {
    BookInfo bookInfo;
    @BindView(R.id.listView_publish)
    ListView mListViewPublish;
    ArrayList<String> titles;
    ArrayList<String> contents;
    public static Fragment getInstance(BookInfo bookInfo) {
        PublishFragment fragment = new PublishFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("bookInfo", bookInfo);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_publish, container, false);
        ButterKnife.bind(this, view);
        bookInfo = (BookInfo) getArguments().getSerializable("bookInfo");
        prepareData();
        PublishListViewAdapter adapter = new PublishListViewAdapter(getActivity(),titles,contents);
        mListViewPublish.setAdapter(adapter);
        return view;
    }

    private void prepareData() {
        titles = new ArrayList<>();
        contents = new ArrayList<>();
        titles.add("书名");contents.add(bookInfo.getBookName());
        titles.add("ISBN");contents.add(bookInfo.getISBN());
        titles.add("作者"); contents.add(bookInfo.getAuthor());
        titles.add("出版社");contents.add(bookInfo.getPublishingCompany());
        titles.add("出版时间");contents.add(bookInfo.getPublishingTime());
        titles.add("版次"); contents.add(String.valueOf(bookInfo.getRevision()));
        titles.add("印次");contents.add(String.valueOf(bookInfo.getImpression()));
        titles.add("页数");contents.add(String.valueOf(bookInfo.getPages()));
        titles.add("字数");contents.add(String.valueOf(bookInfo.getNumberOfWords()));
        titles.add("开本");contents.add(bookInfo.getFolio());
        titles.add("纸张");contents.add(bookInfo.getPaper());
        titles.add("包装"); contents.add(bookInfo.getPacking());
    }
}
