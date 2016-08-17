package com.moliying.jzc.bookstore.ui;

import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.facebook.drawee.view.SimpleDraweeView;
import com.moliying.jzc.bookstore.R;
import com.moliying.jzc.bookstore.adapter.DetailFragmentPagerAdapter;
import com.moliying.jzc.bookstore.ui.fragment.PublishFragment;
import com.moliying.jzc.bookstore.ui.fragment.WebViewFragment;
import com.moliying.jzc.bookstore.vo.BookInfo;
import com.moliying.jzc.bookstore.vo.Comment;
import com.panxw.android.imageindicator.AutoPlayManager;
import com.panxw.android.imageindicator.ImageIndicatorView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.FindListener;

public class BookDetailActivity extends BaseActivity {

    private static final int TYPE_DISPLAY_CONTENT = 0x1;
    private static final int TYPE_DISPLAY_AUTHOR = 0x2;
    private static final int TYPE_DISPLAY_CATALOG = 0x3;
    @BindView(R.id.button_return)
    Button mButtonReturn;
    @BindView(R.id.imageView_shopping_cart)
    ImageView mImageViewShoppingCart;
    @BindView(R.id.image_indicater_view)
    ImageIndicatorView mImageIndicaterView;
    @BindView(R.id.textView_current_price)
    TextView mTextViewCurrentPrice;
    @BindView(R.id.textView_original_price)
    TextView mTextViewOriginalPrice;
    @BindView(R.id.textView_discount)
    TextView mTextViewDiscount;
    @BindView(R.id.textView_bookName)
    TextView mTextViewBookName;
    @BindView(R.id.textView_author)
    TextView mTextViewAuthor;
    @BindView(R.id.button_add_shopping_cart)
    Button mButtonAddShoppingCart;
    @BindView(R.id.textView_comment_count)
    TextView mTextViewCommentCount;
    @BindView(R.id.layout_comment_count)
    LinearLayout mLayoutCommentCount;
    @BindView(R.id.imageView_book_pic)
    SimpleDraweeView mImageViewBookPic;
    @BindView(R.id.textView_username)
    TextView mTextViewUsername;
    @BindView(R.id.textView_star_count)
    TextView mTextViewStarCount;
    @BindView(R.id.ratingBar)
    RatingBar mRatingBar;
    @BindView(R.id.textView_comment_content)
    TextView mTextViewCommentContent;
    @BindView(R.id.PagerSlidingTabStrip_detail)
    PagerSlidingTabStrip mPagerSlidingTabStripDetail;
    @BindView(R.id.view_pager_detail)
    ViewPager mViewPagerDetail;
    @BindView(R.id.scrollView)
    ScrollView mScrollView;

    BookInfo bookInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);
        ButterKnife.bind(this);
        bookInfo = (BookInfo) getIntent().getSerializableExtra("bookInfo");
        initView();

    }

    private void initView() {
        List<String> urlList = new ArrayList<>();
        urlList.add(bookInfo.getBookImage().getUrl());
        mImageIndicaterView.setupLayoutByImageUrl(urlList);
        mImageIndicaterView.setIndicateStyle(ImageIndicatorView.INDICATE_USERGUIDE_STYLE);
        mImageIndicaterView.show();
        AutoPlayManager autoBrocastManager = new AutoPlayManager(mImageIndicaterView);
        autoBrocastManager.setBroadcastEnable(true);
        autoBrocastManager.setBroadCastTimes(5);//loop times
        autoBrocastManager.setBroadcastTimeIntevel(3 * 1000, 3 * 1000);//set first play time and interval
        autoBrocastManager.loop();

        mTextViewCurrentPrice.setText(String.valueOf("￥"+bookInfo.getDiscountPrice()));
        mTextViewOriginalPrice.setText(String.valueOf(bookInfo.getPrice()));
        mTextViewOriginalPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        mTextViewDiscount.setText(String.valueOf(bookInfo.getDiscount())+"折");
        mTextViewBookName.setText(bookInfo.getBookName());
        mTextViewAuthor.setText(bookInfo.getAuthor());
        queryComment();
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(WebViewFragment.getInstance(bookInfo,TYPE_DISPLAY_CONTENT));
        fragments.add(WebViewFragment.getInstance(bookInfo,TYPE_DISPLAY_AUTHOR));
        fragments.add(WebViewFragment.getInstance(bookInfo,TYPE_DISPLAY_CATALOG));
        fragments.add(PublishFragment.getInstance(bookInfo));
        DetailFragmentPagerAdapter adapter = new DetailFragmentPagerAdapter(getSupportFragmentManager(),fragments);
        mViewPagerDetail.setAdapter(adapter);
        mPagerSlidingTabStripDetail.setViewPager(mViewPagerDetail);
    }

    private void queryComment() {
        BmobQuery<Comment> query = new BmobQuery<>();
        query.addWhereEqualTo("bookInfoId",bookInfo.getObjectId());
//        // 根据score字段升序显示数据
//        query.order("score");
//        // 根据score字段降序显示数据
//        query.order("-score");
        query.order("createdAt");
        query.findObjects(new FindListener<Comment>() {
            @Override
            public void done(List<Comment> list, BmobException e) {
                if(e == null){
                    if(list!= null &&list.size()>0){
                        int count = list.size();
                        mTextViewCommentCount.setText("商品评价 (共有");
                        mTextViewCommentCount.append(String.valueOf(count));
                        mTextViewCommentCount.append("条评论)");
                        Comment comment = list.get(0);
                        BmobFile pic = comment.getPic();
                        pic.download(new DownloadFileListener() {
                            @Override
                            public void done(String s, BmobException e) {
                                if(e == null){
                                    mImageViewBookPic.setImageURI(s);
                                }
                            }

                            @Override
                            public void onProgress(Integer integer, long l) {

                            }
                        });
                        mTextViewUsername.setText(comment.getUser());
                        int starCount = comment.getStar();
                        mTextViewStarCount.setText(starCount);
                        mTextViewStarCount.append("星");
                        mRatingBar.setRating(starCount);
                        mTextViewCommentContent.setText(comment.getContent());
                    }
                }
            }
        });
    }

    @OnClick({R.id.button_return, R.id.imageView_shopping_cart, R.id.button_add_shopping_cart, R.id.layout_comment_count})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_return:
                this.finish();
                break;
            case R.id.imageView_shopping_cart:
                break;
            case R.id.button_add_shopping_cart:
                break;
            case R.id.layout_comment_count:
                break;
        }
    }
}
