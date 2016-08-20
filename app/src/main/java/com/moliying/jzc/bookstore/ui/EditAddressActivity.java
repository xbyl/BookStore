package com.moliying.jzc.bookstore.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.moliying.jzc.bookstore.R;
import com.moliying.jzc.bookstore.vo.Address;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobBatch;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BatchResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListListener;
import cn.bmob.v3.listener.UpdateListener;

public class EditAddressActivity extends AppCompatActivity {
    Address address;
    ArrayList<Address> mAddresses;

    @BindView(R.id.button_return)
    Button mButtonReturn;
    @BindView(R.id.editText_name)
    EditText mEditTextName;
    @BindView(R.id.editText_telPhone)
    EditText mEditTextTelPhone;
    @BindView(R.id.editText_address)
    EditText mEditTextAddress;
    @BindView(R.id.button_del_address)
    Button mButtonDelAddress;
    @BindView(R.id.button_set_default)
    Button mButtonSetDefault;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_address);
        ButterKnife.bind(this);
        address = (Address) getIntent().getSerializableExtra("address");
        mAddresses = (ArrayList<Address>) getIntent().getSerializableExtra("mAddresses");
        initView();
    }

    private void initView() {
        mEditTextName.setText(address.getUsername());
        mEditTextTelPhone.setText(address.getPhoneNumber());
        mEditTextAddress.setText(address.getAddress());
    }

    @OnClick({R.id.button_return, R.id.button_del_address, R.id.button_set_default})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_return:
                address.update(new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                    }
                });
                break;
            case R.id.button_del_address:
                address.delete(new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                    }
                });
                break;
            case R.id.button_set_default:
                for (Address address1 : mAddresses) {
                    address1.setIsDefault(false);
                }
                ArrayList<BmobObject> objects = new ArrayList<>();
                objects.addAll(mAddresses);
                new BmobBatch().deleteBatch(objects).doBatch(new QueryListListener<BatchResult>() {
                    @Override
                    public void done(List<BatchResult> list, BmobException e) {}
                });
                address.setIsDefault(true);
                address.update(new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                    }
                });
                break;
        }
        setResult(RESULT_OK);
        this.finish();
    }

}
