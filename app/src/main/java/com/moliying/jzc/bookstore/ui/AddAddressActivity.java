package com.moliying.jzc.bookstore.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.moliying.jzc.bookstore.R;
import com.moliying.jzc.bookstore.vo.Address;
import com.moliying.jzc.bookstore.vo.User;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobBatch;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BatchResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListListener;
import cn.bmob.v3.listener.SaveListener;

public class AddAddressActivity extends AppCompatActivity {

    @BindView(R.id.button_return)
    Button mButtonReturn;
    @BindView(R.id.editText_name)
    EditText mEditTextName;
    @BindView(R.id.editText_telPhone)
    EditText mEditTextTelPhone;
    @BindView(R.id.editText_address)
    EditText mEditTextAddress;
    @BindView(R.id.button_save_address)
    Button mButtonSaveAddress;
    @BindView(R.id.button_save_default_address)
    Button mButtonSaveDefaultAddress;

    ArrayList<Address> mAddresses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);
        ButterKnife.bind(this);
        mAddresses = (ArrayList<Address>) getIntent().getSerializableExtra("mAddresses");
    }

    @OnClick({R.id.button_return, R.id.button_save_address, R.id.button_save_default_address})
    public void onClick(View view) {
        User user = BmobUser.getCurrentUser(User.class);
        String name = mEditTextName.getText().toString();
        String telPhone = mEditTextTelPhone.getText().toString();
        String address = mEditTextAddress.getText().toString();
        switch (view.getId()) {
            case R.id.button_return:
                setResult(RESULT_CANCELED);
                break;
            case R.id.button_save_address:
                Address address_save = new Address();
                address_save.setUserId(user.getObjectId());
                address_save.setUsername(name);
                address_save.setPhoneNumber(telPhone);
                address_save.setAddress(address);
                address_save.setIsDefault(false);
                setResult(RESULT_OK);
                break;
            case R.id.button_save_default_address:
                for (Address address1 : mAddresses) {
                    address1.setIsDefault(false);
                }
                ArrayList<BmobObject> objects = new ArrayList<>();
                objects.addAll(mAddresses);
                new BmobBatch().deleteBatch(objects).doBatch(new QueryListListener<BatchResult>() {
                    @Override
                    public void done(List<BatchResult> list, BmobException e) {}
                });

                Address address_save_default = new Address();
                address_save_default.setUserId(user.getObjectId());
                address_save_default.setUsername(name);
                address_save_default.setPhoneNumber(telPhone);
                address_save_default.setAddress(address);
                address_save_default.setIsDefault(true);
                address_save_default.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                    }
                });
                setResult(RESULT_OK);
                break;
        }
        this.finish();
    }
}
