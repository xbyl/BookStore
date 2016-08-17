package com.moliying.jzc.bookstore.ui.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.moliying.jzc.bookstore.R;
import com.moliying.jzc.bookstore.vo.BookInfo;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class WebViewFragment extends Fragment {
    private static final int TYPE_DISPLAY_CONTENT = 0x1;
    private static final int TYPE_DISPLAY_AUTHOR = 0x2;
    private static final int TYPE_DISPLAY_CATALOG = 0x3;

    BookInfo bookInfo;
    int type_display;
    @BindView(R.id.webView)
    WebView mWebView;

    public static WebViewFragment getInstance(BookInfo bookInfo, int type_display) {
        WebViewFragment fragment = new WebViewFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("bookInfo", bookInfo);
        bundle.putInt("type_display", type_display);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_web_view, container, false);
        ButterKnife.bind(this, view);
        bookInfo = (BookInfo) getArguments().getSerializable("bookInfo");
        type_display = getArguments().getInt("type_display");
        String html = null;
        switch (type_display) {
            case TYPE_DISPLAY_CONTENT:
                html = bookInfo.getContentDescription();
                break;
            case TYPE_DISPLAY_AUTHOR:
                html = bookInfo.getAuthorDescription();
                break;
            case TYPE_DISPLAY_CATALOG:
                html = bookInfo.getCatalog();
                break;
        }
        mWebView.loadDataWithBaseURL("",html,"text/html","utf8",null);
        return view;
    }

}
