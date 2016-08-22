package com.moliying.jzc.bookstore.ui;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.moliying.jzc.bookstore.R;
import com.moliying.jzc.bookstore.adapter.OrdersListViewAdapter;
import com.moliying.jzc.bookstore.utils.Constant;
import com.moliying.jzc.bookstore.vo.Address;
import com.moliying.jzc.bookstore.vo.Orders;
import com.moliying.jzc.bookstore.vo.User;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import c.b.BP;
import c.b.PListener;
import c.b.QListener;
import cn.bmob.v3.BmobBatch;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BatchResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListListener;

public class PayActivity extends AppCompatActivity implements OrdersListViewAdapter.ItemCountListener {
    public static final int PAY_WEI_XIN = 0x1;
    public static final int PAY_ZHI_FU_BAO = 0x2;
    private static final int REQUEST_CODE_ADDRESS = 0x10;
    private static final int UPDATE_UI = 0x100;
    private static final int PLUGINVERSION = 7;

    List<Orders> mOrdersList = new ArrayList<>();
    Address currentAddress;
    List<Address> list;
    User user;
    int mode_pay = 0X1;

    @BindView(R.id.button_return)
    Button mButtonReturn;
    @BindView(R.id.relativeLayout)
    RelativeLayout mRelativeLayout;
    @BindView(R.id.textView_username)
    TextView mTextViewUsername;
    @BindView(R.id.textView_address)
    TextView mTextViewAddress;
    @BindView(R.id.imageView_button)
    ImageView mImageViewButton;
    @BindView(R.id.textView_telPhone)
    TextView mTextViewTelPhone;
    @BindView(R.id.layout_address)
    RelativeLayout mLayoutAddress;
    @BindView(R.id.radioButton_pay_weixin)
    RadioButton mRadioButtonPayWeixin;
    @BindView(R.id.radioButton_pay_zhifubao)
    RadioButton mRadioButtonPayZhifubao;
    @BindView(R.id.listView_orders)
    ListView mListViewOrders;
    @BindView(R.id.textView_count)
    TextView mTextViewCount;
    @BindView(R.id.textView_subtotal)
    TextView mTextViewSubtotal;
    @BindView(R.id.button_pay)
    Button mButtonPay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        ButterKnife.bind(this);
        initData();
        initView();
        verifyStoragePermissions(this);
        int pluginVersion = BP.getPluginVersion();
        if (pluginVersion < PLUGINVERSION) {// 为0说明未安装支付插件, 否则就是支付插件的版本低于官方最新版
            Toast.makeText(
                    this,
                    pluginVersion == 0 ? "监测到本机尚未安装支付插件,无法进行支付,请先安装插件(无流量消耗)"
                            : "监测到本机的支付插件不是最新版,最好进行更新,请先更新插件(无流量消耗)", Toast.LENGTH_SHORT).show();
            installBmobPayPlugin("bp.db");
        }
    }
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE);
        }
    }
    private void initView() {
        final OrdersListViewAdapter adapter = new OrdersListViewAdapter(this, this);
        mListViewOrders.setAdapter(adapter);
        adapter.setList(mOrdersList);
        adapter.notifyDataSetChanged();
        new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case UPDATE_UI:
                        adapter.updateUI();
                        break;
                }
                super.handleMessage(msg);
            }
        }.sendEmptyMessageDelayed(UPDATE_UI, 100);
    }

    private void initData() {
        int size = getIntent().getIntExtra("size", 0);
        for (int i = 0; i < size; i++) {
            mOrdersList.add(i, (Orders) getIntent().getSerializableExtra(String.valueOf(i)));
        }
        user = BmobUser.getCurrentUser(User.class);
        BmobQuery<Address> addressBmobQuery = new BmobQuery<>();
        addressBmobQuery.order("isDefault");
        addressBmobQuery.findObjects(new FindListener<Address>() {
            @Override
            public void done(List<Address> list, BmobException e) {
                if (e == null) {
                    PayActivity.this.list = list;
                    currentAddress = list.get(0);
                    initAddress();
                }
            }
        });
    }

    private void initAddress() {
        if (currentAddress != null) {
            mButtonPay.setEnabled(true);
            mTextViewUsername.setText(currentAddress.getUsername());
            mTextViewTelPhone.setText(currentAddress.getPhoneNumber());
            mTextViewAddress.setText(currentAddress.getAddress());
        }
    }

    @OnClick({R.id.button_return, R.id.layout_address, R.id.radioButton_pay_weixin, R.id.radioButton_pay_zhifubao, R.id.button_pay})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_return:
                this.finish();
                break;
            case R.id.layout_address:
                Intent intent = new Intent(this, AddressActivity.class);
                boolean returnAddress = true;
                intent.putExtra("returnAddress",returnAddress);
                startActivityForResult(intent, REQUEST_CODE_ADDRESS);
                break;
            case R.id.radioButton_pay_weixin:
                mode_pay = PAY_WEI_XIN;
                break;
            case R.id.radioButton_pay_zhifubao:
                mode_pay = PAY_ZHI_FU_BAO;
                break;
            case R.id.button_pay:
                pay(mode_pay == PAY_ZHI_FU_BAO);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(REQUEST_CODE_ADDRESS == requestCode && resultCode == RESULT_OK){
            currentAddress = (Address) data.getSerializableExtra("address");
            initAddress();
        }
    }

    @Override
    public void itemCountChange(int itemcount, double subTotalSum, int totalSum) {
        mTextViewCount.setText("共" + totalSum + "件商品");
        mTextViewSubtotal.setText("金额:" + subTotalSum + "元");
    }

    /**
     * 调用支付
     *
     * @param alipayOrWechatPay
     *            支付类型，true为支付宝支付,false为微信支付
     */
    void pay(final boolean alipayOrWechatPay) {

        try {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            ComponentName cn = new ComponentName("com.bmob.app.sport",
                    "com.bmob.app.sport.wxapi.BmobActivity");
            intent.setComponent(cn);
            this.startActivity(intent);
        } catch (Throwable e) {
            e.printStackTrace();
        }

        showDialog("正在获取订单...");
//        final String name = getName();
        for (final Orders orders : mOrdersList) {
            orders.setAddress(currentAddress);
            BP.pay( getName(), getBody(), getPrice(), alipayOrWechatPay, new PListener() {

                // 因为网络等原因,支付结果未知(小概率事件),出于保险起见稍后手动查询
                @Override
                public void unknow() {
                    Toast.makeText(PayActivity.this, "支付结果未知,请稍后手动查询", Toast.LENGTH_SHORT)
                            .show();
                    hideDialog();
                    updateOrdesList();
                }

                // 支付成功,如果金额较大请手动查询确认
                @Override
                public void succeed() {
                    orders.setStatus(Constant.ORDER_PAYMENTS_RECEIVED);
                    Toast.makeText(PayActivity.this, "支付成功!", Toast.LENGTH_SHORT).show();
                    hideDialog();
                    updateOrdesList();
                }

                // 无论成功与否,返回订单号
                @Override
                public void orderId(String orderId) {
                    // 此处应该保存订单号,比如保存进数据库等,以便以后查询
                    orders.setOrderId(orderId);
                    showDialog("获取订单成功!请等待跳转到支付页面~");
                }

                // 支付失败,原因可能是用户中断支付操作,也可能是网络原因
                @Override
                public void fail(int code, String reason) {
                    // 当code为-2,意味着用户中断了操作
                    // code为-3意味着没有安装BmobPlugin插件
                    if (code == -3) {
                        Toast.makeText(
                                PayActivity.this,
                                "监测到你尚未安装支付插件,无法进行支付,请先安装插件(已打包在本地,无流量消耗),安装结束后重新支付",
                                Toast.LENGTH_SHORT).show();
                        installBmobPayPlugin("bp.db");
                    } else {
                        Toast.makeText(PayActivity.this, "支付中断!", Toast.LENGTH_SHORT)
                                .show();
                    }
                    hideDialog();
                }
            });
        }

    }

    private void updateOrdesList() {
        ArrayList<BmobObject> objects = new ArrayList<>();
        objects.addAll(mOrdersList);
        new BmobBatch().updateBatch(objects).doBatch(new QueryListListener<BatchResult>() {
            @Override
            public void done(List<BatchResult> list, BmobException e) {
            }
        });
    }

    // 执行订单查询
    void query(Orders orders) {
        showDialog("正在查询订单...");
        final String orderId = getOrder(orders);

        BP.query(orderId, new QListener() {

            @Override
            public void succeed(String status) {
                Toast.makeText(PayActivity.this, "查询成功!该订单状态为 : " + status,
                        Toast.LENGTH_SHORT).show();
                hideDialog();
            }

            @Override
            public void fail(int code, String reason) {
                Toast.makeText(PayActivity.this, "查询失败", Toast.LENGTH_SHORT).show();
                hideDialog();
            }
        });
    }

    // 默认为0.02
    double getPrice() {
        double price = 0.02;
//        try {
//            price = Double.parseDouble(this.price.getText().toString());
//        } catch (NumberFormatException e) {
//        }
        return price;
    }

    // 商品详情(可不填)
    String getName() {
//        return this.name.getText().toString();
        return "测试名字";
    }

    // 商品详情(可不填)
    String getBody() {
        return "测试商品详情";
    }

    // 支付订单号(查询时必填)
    String getOrder(Orders orders) {
        return orders.getOrderId();
    }
    ProgressDialog dialog;

    void showDialog(String message) {
        try {
            if (dialog == null) {
                dialog = new ProgressDialog(this);
                dialog.setCancelable(true);
            }
            dialog.setMessage(message);
            dialog.show();
        } catch (Exception e) {
            // 在其他线程调用dialog会报错
        }
    }

    void hideDialog() {
        if (dialog != null && dialog.isShowing())
            try {
                dialog.dismiss();
            } catch (Exception e) {
            }
    }

    void installBmobPayPlugin(String fileName) {
        try {
            InputStream is = getAssets().open(fileName);
            File file = new File(Environment.getExternalStorageDirectory()
                    + File.separator + "BmobPayPlugin_7.apk");
            if (file.exists())
                file.delete();
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            byte[] temp = new byte[1024];
            int i = 0;
            while ((i = is.read(temp)) > 0) {
                fos.write(temp, 0, i);
            }
            fos.close();
            is.close();
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.parse("file://" + file),
                    "application/vnd.android.package-archive");
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
