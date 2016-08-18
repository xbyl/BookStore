package com.moliying.jzc.bookstore.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.moliying.jzc.bookstore.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener {

    @BindView(R.id.button_toMainActivity)
    Button mButtonToMainActivity;
    @BindView(R.id.button_toRegisterActivity)
    Button mButtonToRegisterActivity;
    @BindView(R.id.AutoCompleteTextView_telPhone)
    AutoCompleteTextView mAutoCompleteTextViewTelPhone;
    @BindView(R.id.password)
    EditText mPassword;
    @BindView(R.id.checkbox_showPass)
    CheckBox mCheckboxShowPass;
    @BindView(R.id.button_login)
    Button mButtonLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        mCheckboxShowPass.setOnCheckedChangeListener(this);
    }

    @OnClick({R.id.button_toMainActivity, R.id.button_toRegisterActivity, R.id.button_login})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_toMainActivity:
                LoginActivity.this.setResult(Activity.RESULT_CANCELED);
                this.finish();
                break;
            case R.id.button_toRegisterActivity:
                Intent intent = new Intent(this,RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.button_login:
                login();

                break;
        }
    }

    private void login() {
        String telPhoneNum = mAutoCompleteTextViewTelPhone.getText().toString();
        String passWord = mPassword.getText().toString();
        BmobUser.loginByAccount(telPhoneNum, passWord, new LogInListener<Object>() {
            @Override
            public void done(Object o, BmobException e) {
                if(e == null){
                    Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                    LoginActivity.this.setResult(Activity.RESULT_OK);
                    LoginActivity.this.finish();
                }else{
                    Toast.makeText(LoginActivity.this,"账户或密码有误", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(isChecked){
            mPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        }else{
            mPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }
    }
}

