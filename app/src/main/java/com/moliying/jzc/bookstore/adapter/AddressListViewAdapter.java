package com.moliying.jzc.bookstore.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.moliying.jzc.bookstore.R;
import com.moliying.jzc.bookstore.vo.Address;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Jzc on 2016/8/15.
 */
public class AddressListViewAdapter extends BaseAdapter {
    Context context;
    ArrayList<Address> mAddresses;

    public void setBookInfos(ArrayList<Address> mAddresses) {
        this.mAddresses = mAddresses;
    }

    public AddressListViewAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        if (mAddresses == null) return 0;
        return mAddresses.size();
    }

    @Override
    public Object getItem(int position) {
        return mAddresses.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.address_listview_item_layout, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Address address = mAddresses.get(position);
        holder.mTextViewUsername.setText(address.getUsername());
        holder.mTextViewTelPhone.setText(address.getPhoneNumber());
        holder.mTextViewAddress.setText(address.getAddress());
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.textView_username)
        TextView mTextViewUsername;
        @BindView(R.id.textView_address)
        TextView mTextViewAddress;
        @BindView(R.id.imageView_button)
        ImageView mImageViewButton;
        @BindView(R.id.textView_telPhone)
        TextView mTextViewTelPhone;
        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
