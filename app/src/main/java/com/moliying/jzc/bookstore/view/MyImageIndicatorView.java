package com.moliying.jzc.bookstore.view;

import android.content.Context;
import android.util.AttributeSet;

import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.moliying.jzc.bookstore.R;
import com.panxw.android.imageindicator.ImageIndicatorView;

import java.util.List;

/**
 * Created by Jzc on 2016/8/17.
 */
public class MyImageIndicatorView extends ImageIndicatorView {
    Context context;

    public MyImageIndicatorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public MyImageIndicatorView(Context context) {
        super(context);
        this.context = context;
    }
    public void setupLayoutByImageUrl(List<String> urlList) {
//        for(String url: urlList) {
//            ImageView imageView = new ImageView(getContext());
//            //load image from url and set to imageView, you can use UIL or Volley to do this work
//            addViewItem(imageView);
//        }
        for(String url: urlList) {
            GenericDraweeHierarchyBuilder builder = new GenericDraweeHierarchyBuilder(getResources());
            builder.setPlaceholderImage(R.mipmap.a)
                    .setActualImageScaleType(ScalingUtils.ScaleType.FIT_CENTER);
            GenericDraweeHierarchy hierarchy = builder.build();
            SimpleDraweeView simpleDraweeView = new SimpleDraweeView(context,hierarchy);
            simpleDraweeView.setImageURI(url);
            addViewItem(simpleDraweeView);
        }
    }
}
