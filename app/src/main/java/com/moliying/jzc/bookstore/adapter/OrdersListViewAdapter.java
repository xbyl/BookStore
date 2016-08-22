package com.moliying.jzc.bookstore.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.moliying.jzc.bookstore.R;
import com.moliying.jzc.bookstore.vo.Orders;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Jzc on 2016/8/18.
 */
public class OrdersListViewAdapter extends BaseAdapter {
    Context context;
    List<Orders> list;
    SparseArray<Double> subTotalSumArray = new SparseArray<>();
    SparseArray<Integer> totalSumArray = new SparseArray<>();
    ItemCountListener itemCountListener;

    public OrdersListViewAdapter(Context context, ItemCountListener itemCountListener) {
        this.context = context;
        this.itemCountListener = itemCountListener;
    }

    public void updateUI() {
        int count = getCount();
        this.itemCountListener.itemCountChange(count, getSubTotalSumArray(count), getTotalSumArray(count));
    }

    private double getSubTotalSumArray(int count) {
        double sum = 0;
        int mCount = count;
        for (int i = 0; i < mCount; i++) {
            sum += subTotalSumArray.get(i);
        }
        return sum;
    }

    private int getTotalSumArray(int count) {
        int sum = 0;
        int mCount = count;
        for (int i = 0; i < mCount; i++) {
            sum += totalSumArray.get(i);
        }
        return sum;
    }

    public void setList(List<Orders> list) {
        this.list = list;
    }

    public List<Orders> getList() {
        return list;
    }

    @Override
    public int getCount() {
        if (list == null) return 0;
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_orders_list_layout, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Orders orders = list.get(position);
        holder.mImageViewBookPic.setImageURI(orders.getBookImage());
        holder.mTextViewBookName.setText(orders.getBookName());
        double subtotal = orders.getSubtotal();
        subTotalSumArray.put(position, subtotal);
        holder.mTextViewSubtotal.setText("￥" + String.valueOf(subtotal));
        int total = orders.getTotal();
        totalSumArray.put(position, total);
        holder.mTextViewCount.setText("数量:" + String.valueOf(total));
        holder.mTextViewDiscountPrice.setText("单价:" + String.valueOf(orders.getDiscountPrice()));
        return convertView;
    }


    public static interface ItemCountListener {
        public void itemCountChange(int itemcount, double subTotalSum, int totalSum);
    }

    static class ViewHolder {
        @BindView(R.id.imageView_book_pic)
        SimpleDraweeView mImageViewBookPic;
        @BindView(R.id.textView_bookName)
        TextView mTextViewBookName;
        @BindView(R.id.textView_discount_price)
        TextView mTextViewDiscountPrice;
        @BindView(R.id.textView_count)
        TextView mTextViewCount;
        @BindView(R.id.textView_subtotal)
        TextView mTextViewSubtotal;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
