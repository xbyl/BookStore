package com.moliying.jzc.bookstore.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.moliying.jzc.bookstore.R;
import com.moliying.jzc.bookstore.vo.BookInfo;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Jzc on 2016/8/15.
 */
public class ListViewAdapter extends BaseAdapter {
    Context context;
    ArrayList<BookInfo> bookInfos;

    public void setBookInfos(ArrayList<BookInfo> bookInfos) {
        this.bookInfos = bookInfos;
    }

    public ListViewAdapter(Context context, ArrayList<BookInfo> bookInfos) {
        this.context = context;
        this.bookInfos = bookInfos;
    }

    @Override
    public int getCount() {
        if(bookInfos == null ) return 0;
        return bookInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return bookInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.book_listview_item_layout, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        BookInfo bookInfo = bookInfos.get(position);
        String path = bookInfo.getBookImagepath();
        if(path != null){
            holder.mImageViewBookPic.setImageURI(Uri.fromFile(new File(path)));
        }
        holder.mTextViewBookName.setText(bookInfo.getBookName());
        holder.mTextViewCurrentPrice.setText(String.valueOf("￥"+bookInfo.getDiscountPrice()));
        holder.mTextViewOriginalPrice.setText(String.valueOf(bookInfo.getPrice()));
        holder.mTextViewDiscount.setText(String.valueOf(bookInfo.getDiscount())+"折");
        holder.mTextViewStarLevel.setText(String.valueOf(bookInfo.getStar())+"星");
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.imageView_book_pic)
        SimpleDraweeView mImageViewBookPic;
        @BindView(R.id.textView_bookName)
        TextView mTextViewBookName;
        @BindView(R.id.textView_current_price)
        TextView mTextViewCurrentPrice;
        @BindView(R.id.textView_original_price)
        TextView mTextViewOriginalPrice;
        @BindView(R.id.textView_discount)
        TextView mTextViewDiscount;
        @BindView(R.id.textView_star_level)
        TextView mTextViewStarLevel;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
