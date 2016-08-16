package com.moliying.jzc.bookstore.ui.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.moliying.jzc.bookstore.App;
import com.moliying.jzc.bookstore.R;
import com.moliying.jzc.bookstore.ui.LoginActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class PersonalFragment extends BaseFragment {

    @BindView(R.id.imageView_mine_icon)
    SimpleDraweeView mImageViewMineIcon;
    @BindView(R.id.button_address)
    Button mButtonAddress;
    @BindView(R.id.textView_login)
    TextView mTextViewLogin;
    @BindView(R.id.item_order)
    LinearLayout mItemOrder;
    @BindView(R.id.item_unpay)
    LinearLayout mItemUnpay;
    @BindView(R.id.item_untruck)
    LinearLayout mItemUntruck;
    @BindView(R.id.item_unthumb)
    LinearLayout mItemUnthumb;

    public static PersonalFragment getInstance() {
        PersonalFragment personalFragment = new PersonalFragment();
        return personalFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_personal, container, false);
        ButterKnife.bind(this, view);
        checkLogin();
        return view;
    }

    private void checkLogin() {
        if(App.currentUser == null){
            mTextViewLogin.setVisibility(View.VISIBLE);
            mImageViewMineIcon.setVisibility(View.INVISIBLE);
            mButtonAddress.setVisibility(View.INVISIBLE);
        }else{
            mTextViewLogin.setVisibility(View.INVISIBLE);
            mImageViewMineIcon.setVisibility(View.VISIBLE);
            mButtonAddress.setVisibility(View.VISIBLE);
        }
    }

    @OnClick({R.id.imageView_mine_icon, R.id.button_address, R.id.textView_login, R.id.item_order, R.id.item_unpay, R.id.item_untruck, R.id.item_unthumb})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageView_mine_icon:
                break;
            case R.id.button_address:
                break;
            case R.id.textView_login:
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.item_order:
                break;
            case R.id.item_unpay:
                break;
            case R.id.item_untruck:
                break;
            case R.id.item_unthumb:
                break;
        }
    }
}
