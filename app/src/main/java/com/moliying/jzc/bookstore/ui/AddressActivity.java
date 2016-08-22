package com.moliying.jzc.bookstore.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.moliying.jzc.bookstore.R;
import com.moliying.jzc.bookstore.adapter.AddressListViewAdapter;
import com.moliying.jzc.bookstore.vo.Address;
import com.moliying.jzc.bookstore.vo.User;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class AddressActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private static final int REQUEST_CODE_ADD = 0x1;
    private static final int REQUEST_CODE_EDIT = 0x2;
    @BindView(R.id.button_return)
    Button mButtonReturn;
    @BindView(R.id.button_address_add)
    Button mButtonAddressAdd;
    @BindView(R.id.linearLayout)
    LinearLayout mLinearLayout;
    @BindView(R.id.listView_address)
    ListView mListViewAddress;

    ArrayList<Address> mAddresses;
    AddressListViewAdapter addressListViewAdapter;
    boolean returnAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        ButterKnife.bind(this);
        mAddresses = new ArrayList<>();
        returnAddress = getIntent().getBooleanExtra("returnAddress",false);
        initView();
        initData();
    }

    private void initView() {
        addressListViewAdapter = new AddressListViewAdapter(this);
        mListViewAddress.setAdapter(addressListViewAdapter);
        mListViewAddress.setOnItemClickListener(this);
    }

    private void initData() {
        User user = BmobUser.getCurrentUser(User.class);
        BmobQuery<Address> addressBmobQuery = new BmobQuery<>();
        addressBmobQuery.addWhereEqualTo("userId",user.getObjectId());
        addressBmobQuery.findObjects(new FindListener<Address>() {
            @Override
            public void done(List<Address> list, BmobException e) {
                if(e == null){
                    mAddresses.clear();
                    mAddresses.addAll(list);
                    addressListViewAdapter.setBookInfos(mAddresses);
                    addressListViewAdapter.notifyDataSetChanged();
                }else{
                    Toast.makeText(AddressActivity.this, "获取地址失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @OnClick({R.id.button_return, R.id.button_address_add})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_return:
                this.finish();
                break;
            case R.id.button_address_add:
                if(mAddresses == null){
                    break;
                }
                Intent intent = new Intent();
                intent.putExtra("mAddresses",mAddresses);
                startActivityForResult(intent,REQUEST_CODE_ADD);
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Address address = mAddresses.get(position);
        if(returnAddress){
            Intent intent = new Intent();
            intent.putExtra("address",address);
            setResult(RESULT_OK,intent);
            this.finish();
        }else{
            Intent intent = new Intent(this,EditAddressActivity.class);
            intent.putExtra("address",address);
            intent.putExtra("mAddresses",mAddresses);
            startActivityForResult(intent,REQUEST_CODE_EDIT);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            initData();
        }
    }
}
