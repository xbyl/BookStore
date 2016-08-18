package com.moliying.jzc.bookstore.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.webkit.WebView;

/**
 * Created by Jzc on 2016/8/17.
 */
public class MyWebView extends WebView {
    public MyWebView(Context context) {
        super(context);
    }

    public MyWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    float lastY;
    int maxScrollY = this.getHeight();
    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        if(event.getAction() == MotionEvent.ACTION_MOVE){
//            if(this.getScrollY()>0){
//                Log.i("123987",this.getScrollY()+"");
//                requestDisallowInterceptTouchEvent(true);
//            }else {
//                Log.i("123987",this.getScrollY()+"");
//                requestDisallowInterceptTouchEvent(false);
//            }
//        }

//        switch (event.getAction()){
//            case MotionEvent.ACTION_DOWN:
//                lastY = event.getY();
//                break;
//            case MotionEvent.ACTION_MOVE:
////                float currentY = event.getY();
////                int currentScrollY = this.getScrollY();
////                if(currentY > lastY + 50){
////                    if(currentScrollY==0){
////                        requestDisallowInterceptTouchEvent(false);
////                    }else{
////                        requestDisallowInterceptTouchEvent(true);
////                    }
////
////                }else if(currentY < lastY + 50 ){
////                    if(this.getScrollY() == maxScrollY){
////                        requestDisallowInterceptTouchEvent(false);
////                    }else{
////                        requestDisallowInterceptTouchEvent(true);
////                    }
////                }
////                lastY = currentY;
////                maxScrollY = currentScrollY > maxScrollY ? currentScrollY : maxScrollY ;
//                break;
//            case MotionEvent.ACTION_UP:
//                float currentY = event.getY();
//                int currentScrollY = this.getScrollY();
//                if(currentY > lastY + 30){
//                    if(currentScrollY==0){
//                        requestDisallowInterceptTouchEvent(false);
//                    }else{
//                        requestDisallowInterceptTouchEvent(true);
//                    }
//                }else if(currentY < lastY + 30 ){
//                    if(this.getScrollY() == maxScrollY){
//                        requestDisallowInterceptTouchEvent(false);
//                    }else{
//                        requestDisallowInterceptTouchEvent(true);
//                    }
//                }
//                lastY = currentY;
//                maxScrollY = currentScrollY > maxScrollY ? currentScrollY : maxScrollY ;
//                break;
//        }
//
//        Log.i("event.getY()",event.getY()+"");
        requestDisallowInterceptTouchEvent(true);
        return super.onTouchEvent(event);
    }
}
