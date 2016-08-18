package com.moliying.jzc.bookstore.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class ShoppingCartActivity extends AppCompatActivity implements ShoppingCartListViewAdapter.ItemCountListener {


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

    List<Orders> list;
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
        BmobQuery<Orders> ordersBmobQuery = new BmobQuery<>();
        ordersBmobQuery.addWhereEqualTo("userId", user.getObjectId());
        ordersBmobQuery.addWhereEqualTo("status", Constant.ORDER_IN_SHOPPING_CART);
        ordersBmobQuery.findObjects(new FindListener<Orders>() {
            @Override
            public void done(List<Orders> list, BmobException e) {
                if (e == null) {
                    initListView(list);
                } else {
                    Log.i("list != null",(list==null)+"");
                    if(list != null){
                        for (Orders orders : list) {
                            Log.i("orders.toString()",orders.toString());
                        }
                    }
                    Toast.makeText(ShoppingCartActivity.this, "数据加载失败"+e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initListView(List<Orders> list) {
        this.list = list;
        adapter = new ShoppingCartListViewAdapter(this,list,this);
        mProgressBarLoading.setVisibility(View.GONE);
        mTextViewLoading.setVisibility(View.GONE);
        mListView.setAdapter(adapter);
    }

    @OnClick({R.id.button_return, R.id.button_edit, R.id.button_complete, R.id.button_pay})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_return:
                this.finish();
                break;
            case R.id.button_edit:
                if(editAdapter == null){
                    editAdapter = new ShoppingCartListViewEditAdapter(this,adapter.getList());
                }else{
                    editAdapter.setList(adapter.getList());
                }
                mListView.setAdapter(editAdapter);
                mButtonEdit.setVisibility(View.INVISIBLE);
                mButtonComplete.setVisibility(View.VISIBLE);
                break;
            case R.id.button_complete:
                mProgressBarLoading.setVisibility(View.VISIBLE);
                mTextViewLoading.setVisibility(View.VISIBLE);
                queryOrders();
                mButtonEdit.setVisibility(View.VISIBLE);
                mButtonComplete.setVisibility(View.INVISIBLE);
                break;
            case R.id.button_pay:
                break;
        }
    }


    @Override
    public void itemCountChange(int itemcount, double subTotalSum, int totalSum) {
        if(itemcount > 0 ){
            mButtonPay.setEnabled(true);
        }else {
            mButtonPay.setEnabled(false);
        }
        mTextViewCount.setText("共"+totalSum+"件商品");
        mTextViewSubtotal.setText("金额:"+subTotalSum+"元");
    }
}
