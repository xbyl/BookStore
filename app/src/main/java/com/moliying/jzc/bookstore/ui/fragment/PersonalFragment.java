package com.moliying.jzc.bookstore.ui.fragment;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.moliying.jzc.bookstore.R;
import com.moliying.jzc.bookstore.ui.LoginActivity;
import com.moliying.jzc.bookstore.utils.Constant;
import com.moliying.jzc.bookstore.vo.User;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class PersonalFragment extends BaseFragment {

    private static final int REQUEST_CODE_LOGIN = 0x1;
    private static final int REQUEST_CODE_PICTURE = 0x2;
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
    @BindView(R.id.button_exit_current_user)
    Button mButtonExitCurrentUser;
    private View mFragment;


    public static PersonalFragment getInstance() {
        PersonalFragment personalFragment = new PersonalFragment();
        return personalFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(null == mFragment) {
            mFragment = inflater.inflate(R.layout.fragment_personal, container, false);
            ButterKnife.bind(this, mFragment);

            checkLogin();
        }
        return mFragment;
    }

    @Override
    public void onDestroy() {
        if(null != mFragment){
            ViewGroup group = (ViewGroup) mFragment.getParent();
            if(group != null)
            group.removeView(mFragment);
        }
        super.onDestroy();
    }

    private void checkLogin() {
        User user = BmobUser.getCurrentUser(User.class);
        if (user == null) {
            mTextViewLogin.setVisibility(View.VISIBLE);
            mImageViewMineIcon.setVisibility(View.INVISIBLE);
            mButtonExitCurrentUser.setVisibility(View.INVISIBLE);
            mButtonAddress.setVisibility(View.INVISIBLE);
        } else {
            mTextViewLogin.setVisibility(View.INVISIBLE);
            mImageViewMineIcon.setVisibility(View.VISIBLE);
            mImageViewMineIcon.setImageURI(user.getUserIcon().getUrl());
            mButtonExitCurrentUser.setVisibility(View.VISIBLE);
            mButtonAddress.setVisibility(View.VISIBLE);
        }
    }

    @OnClick({R.id.imageView_mine_icon
            , R.id.button_address, R.id.textView_login
            , R.id.item_order, R.id.item_unpay
            , R.id.item_untruck, R.id.item_unthumb
            ,R.id.button_exit_current_user})
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.imageView_mine_icon:
                selectIcon();
                break;
            case R.id.button_address:
                break;
            case R.id.textView_login:
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivityForResult(intent, REQUEST_CODE_LOGIN);
                break;
            case R.id.item_order:
                break;
            case R.id.item_unpay:
                break;
            case R.id.item_untruck:
                break;
            case R.id.item_unthumb:
                break;
            case R.id.button_exit_current_user:
                BmobUser.logOut();   //清除缓存用户对象
                checkLogin();
                break;
        }
    }

    private static final String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + Constant.CACHE_PATH ;
    private static final String fileName = "user_portrait.jpg";
    public File saveFile(Bitmap bm)  {
        File dirFile = new File(path);
        if(dirFile.exists()){
            dirFile.mkdir();
        }
        File myCaptureFile = new File(path+"/"+fileName);
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
            bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return myCaptureFile;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("resultCode  ",resultCode+"");
        Log.i("requestCode  ",requestCode+"");
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_LOGIN){
            this.checkLogin();
        }else if(resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_PICTURE){
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                final File file = saveFile(bitmap);
                final BmobFile bmobFile = new BmobFile(file);
                bmobFile.upload(new UploadFileListener() {
                    @Override
                    public void done(BmobException e) {
                        if(e == null){
                            final User currrentUser = BmobUser.getCurrentUser(User.class);
                            currrentUser.setUserIcon(bmobFile);
                            currrentUser.update(new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    if(e == null){
                                        mImageViewMineIcon.setImageURI(currrentUser.getUserIcon().getUrl());
                                    }else {
                                        Log.d("更新用户失败",e.toString());
                                        Toast.makeText(getActivity(), "更新用户失败"+e.toString(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }else{
                            Toast.makeText(getActivity(), "上传图片失败"+e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    private void selectIcon() {
        Intent intent = new Intent(Intent.ACTION_PICK,null);
        intent.setType("image/*");
//        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // crop为true是设置在开启的intent中设置显示的view可以剪裁
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("scale", true);
        // outputX,outputY 是剪裁图片的宽高
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 200);
        intent.putExtra("return-data", true);
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, tempImage);
//        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        startActivityForResult(intent, REQUEST_CODE_PICTURE);
    }
}
