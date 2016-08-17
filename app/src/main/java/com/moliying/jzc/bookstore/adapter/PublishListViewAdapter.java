package com.moliying.jzc.bookstore.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.moliying.jzc.bookstore.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Jzc on 2016/8/15.
 */
public class PublishListViewAdapter extends BaseAdapter {
    Context context;
    ArrayList<String> titles;
    ArrayList<String> contents;
    public PublishListViewAdapter(Context context, ArrayList<String> titles
            , ArrayList<String> contents) {
        this.context = context;
        this.titles = titles;
        this.contents = contents;
    }

    @Override
    public int getCount() {
        if (titles == null) return 0;
        return titles.size();
    }

    @Override
    public Object getItem(int position) {
        return titles.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_publish_layout, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.mTextViewTitle.setText(titles.get(position));
        holder.mTextViewContent.setText(contents.get(position));
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.textView_title)
        TextView mTextViewTitle;
        @BindView(R.id.textView_content)
        TextView mTextViewContent;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
