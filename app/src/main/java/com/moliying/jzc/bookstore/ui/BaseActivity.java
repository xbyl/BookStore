package com.moliying.jzc.bookstore.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.widget.Toast;

import com.moliying.jzc.bookstore.App;

/**
 * Created by Jzc on 2016/8/15.
 */
public abstract class BaseActivity extends FragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.mActivities.add(this);
    }

    @Override
    protected void onDestroy() {
        App.mActivities.remove(this);
        super.onDestroy();
    }
    private long exitTime = 0L;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK&&
                event.getAction() == KeyEvent.ACTION_DOWN){
            if (System.currentTimeMillis() - exitTime > 20000) {
                showToast("再按一次退出应用");
                exitTime = System.currentTimeMillis();
            }else{
                closeAllActivities();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void closeAllActivities() {
        for (BaseActivity mActivity : App.mActivities) {
            mActivity.finish();
        }
        App.release();
    }

    private void showToast(String s) {
        Toast.makeText(BaseActivity.this, s, Toast.LENGTH_SHORT).show();
    }

}
