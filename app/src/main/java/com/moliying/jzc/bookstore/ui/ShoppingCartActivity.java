package com.moliying.jzc.bookstore.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.moliying.jzc.bookstore.R;
import com.moliying.jzc.bookstore.adapter.ShoppingCartListViewAdapter;
import com.moliying.jzc.bookstore.adapter.ShoppingCartListViewEditAdapter;
import com.moliying.jzc.bookstore.utils.Constant;
import com.moliying.jzc.bookstore.vo.Orders;
import com.moliying.jzc.bookstore.vo.User;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobBatch;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BatchResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListListener;

public class ShoppingCartActivity extends AppCompatActivity implements ShoppingCartListViewAdapter.ItemCountListener {


    private static final int UPDATE_UI = 0x1;
    @BindView(R.id.button_return)
    Button mButtonReturn;
    @BindView(R.id.button_edit)
    Button mButtonEdit;
    @BindView(R.id.button_complete)
    Button mButtonComplete;
    @BindView(R.id.listView)
    ListView mListView;
    @BindView(R.id.progressBar_loading)
    ProgressBar mProgressBarLoading;
    @BindView(R.id.textView_loading)
    TextView mTextViewLoading;
    @BindView(R.id.textView_count)
    TextView mTextViewCount;
    @BindView(R.id.textView_subtotal)
    TextView mTextViewSubtotal;
    @BindView(R.id.button_pay)
    Button mButtonPay;

    List<Orders> mOrdersList;
    ArrayList<Orders> rubbishList = new ArrayList<>();
    ShoppingCartListViewAdapter adapter;
    ShoppingCartListViewEditAdapter editAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);
        ButterKnife.bind(this);
        queryOrders();
    }

    private void queryOrders() {
        User user = BmobUser.getCurrentUser(User.class);
        if (user == null) {
            Toast.makeText(this, "请先登录", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
        BmobQuery<Orders> ordersBmobQuery = new BmobQuery<>();
        ordersBmobQuery.addWhereEqualTo("userId", user.getObjectId());
        ordersBmobQuery.addWhereEqualTo("status", Constant.ORDER_IN_SHOPPING_CART);
        ordersBmobQuery.findObjects(new FindListener<Orders>() {
            @Override
            public void done(List<Orders> list, BmobException e) {
                if (e == null) {
                    mOrdersList = list;
                    initListView();
                } else {
                    Toast.makeText(ShoppingCartActivity.this, "添加失败 " + e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initListView() {
        if (adapter == null) {
            adapter = new ShoppingCartListViewAdapter(this, this);
        }
        mProgressBarLoading.setVisibility(View.GONE);
        mTextViewLoading.setVisibility(View.GONE);
        mListView.setAdapter(adapter);
        adapter.setList(mOrdersList);
        adapter.notifyDataSetChanged();
        mHandler.sendEmptyMessageDelayed(UPDATE_UI, 100);
//        adapter.updateUI();
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_UI:
                    adapter.updateUI();
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @OnClick({R.id.button_return, R.id.button_edit, R.id.button_complete, R.id.button_pay})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_return:
                updateOrdersList();
                clearRubbishList();
                this.finish();
                break;
            case R.id.button_edit:
                if (editAdapter == null) {
                    editAdapter = new ShoppingCartListViewEditAdapter(this, adapter.getList(), rubbishList);
                } else {
                    editAdapter.setList(adapter.getList(), rubbishList);
                }
                mListView.setAdapter(editAdapter);
                mButtonEdit.setVisibility(View.INVISIBLE);
                mButtonComplete.setVisibility(View.VISIBLE);
                break;
            case R.id.button_complete:
                mProgressBarLoading.setVisibility(View.VISIBLE);
                mTextViewLoading.setVisibility(View.VISIBLE);
                clearRubbishList();
                initListView();
                mButtonEdit.setVisibility(View.VISIBLE);
                mButtonComplete.setVisibility(View.INVISIBLE);
                break;
            case R.id.button_pay:
                updateOrdersList();
                clearRubbishList();
                Intent intent = new Intent(this,PayActivity.class);
                int size = mOrdersList.size();
                intent.putExtra("size",size);
                for(int i  = 0 ; i < size ; i ++ ){
                    intent.putExtra(String.valueOf(i),mOrdersList.get(i));
                }
                startActivity(intent);
                break;
        }
    }

    private void updateOrdersList() {
        ArrayList<BmobObject> objects = new ArrayList<>();
        objects.addAll(mOrdersList);
        new BmobBatch().updateBatch(objects).doBatch(new QueryListListener<BatchResult>() {
            @Override
            public void done(List<BatchResult> list, BmobException e) {
            }
        });
    }

    private void clearRubbishList() {
        ArrayList<BmobObject> objects = new ArrayList<>();
        objects.addAll(rubbishList);
        new BmobBatch().deleteBatch(objects).doBatch(new QueryListListener<BatchResult>() {
            @Override
            public void done(List<BatchResult> list, BmobException e) {

            }
        });
        rubbishList.clear();
    }


    @Override
    public void itemCountChange(int itemcount, double subTotalSum, int totalSum) {
        if (itemcount > 0) {
            mButtonPay.setEnabled(true);
        } else {
            mButtonPay.setEnabled(false);
        }
        mTextViewCount.setText("共" + totalSum + "件商品");
        mTextViewSubtotal.setText("金额:" + subTotalSum + "元");
    }
}
