package com.mrzk.transitioncontroller.controller.animationUtils;

import android.animation.Animator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;

import com.mrzk.transitioncontroller.controller.listener.TransitionCustomListener;
import com.mrzk.transitioncontroller.controller.utils.BarUtils;
import com.mrzk.transitioncontroller.controller.utils.BitmapUtil;

/**
 * Created by zhangke on 2016-11-3.
 */
public class TransitionController {

    private static final String TRANSITION_NEXT_ID = "transition_next_name";
    private static final String SCALE_WIDTH = "SCALE_WIDTH";
    private static final String SCALE_HEIGHT = "SCALE_HEIGHT";
    private static final String TRANSITION_X = "TRANSITION_X";
    private static final String TRANSITION_Y = "TRANSITION_Y";
    /**
     * 存储元素缩放比例和位移距离
     */
    private Bundle mScaleBundle = new Bundle();
    private Bundle mTransitionBundle = new Bundle();

    private int nResId = -1;
    /**
     *  if view instanceof imageView  should add resID
     * @param intent
     * @param view          当前页面view
     * @param nextShowViewId  下一个页面显示View的ID
     *
     */
    private Activity mFirstActivity;
    private View mFirstView;

    private TransitionController(){}

    private static TransitionController mInstance;
    //切换视图的包裹视图
    private FrameLayout mContainer;
    public static TransitionController getInstance(){
        if (mInstance == null){
            mInstance = new TransitionController();
        }
        return mInstance;
    }

    private TransitionCustomListener mTransitionCustomListener;

    public void setEnterListener(TransitionCustomListener mTransitionCustomListener){
        this.mTransitionCustomListener = mTransitionCustomListener;
    }
     public final void startActivity(Activity activity,Intent intent, View view, int nextShowViewId){

         mFirstActivity = activity;
         mFirstView =view;
         //rect 来存储共享元素位置信息
        Rect rect = new Rect();
        // 获取元素位置信息
        view.getGlobalVisibleRect(rect);
        // 将位置信息附加到 intent 上
        intent.setSourceBounds(rect);
        intent.putExtra(TRANSITION_NEXT_ID, nextShowViewId);
         nResId = nextShowViewId;
        activity.startActivity(intent);
        // 屏蔽 Activity 默认转场效果
        activity.overridePendingTransition(0,0);
    }


    /**
     *
     * @param activity
     * @param intent
     */
    public final void show(final Activity activity,final Intent intent){
        show(activity,intent,-1);
    }

    /**
     *
     * @param activity
     * @param intent
     * @param mBgColor  过渡背景色
     */
    public final void show(final Activity activity,final Intent intent,final int mBgColor){

        final ViewGroup parent = (ViewGroup) activity.findViewById(Window.ID_ANDROID_CONTENT);
        final View next_view = activity.findViewById(intent.getIntExtra(TRANSITION_NEXT_ID,-1));
        next_view.setVisibility(View.INVISIBLE);
        next_view.post(new Runnable() {
            @Override
            public void run() {
                final Rect mRect = intent.getSourceBounds();

                View virtalView = new View(activity);
                Bitmap cacheBitmap = BitmapUtil.getCacheBitmapFromView(next_view);


                // 获取上一个界面中，元素的宽度和高度
                final int mOriginWidth = mRect.right - mRect.left;
                final int mOriginHeight = mRect.bottom - mRect.top;

                getBundleInfo(next_view,mOriginWidth,mOriginHeight,mRect);

                mContainer = new FrameLayout(activity);
                FrameLayout.LayoutParams mContainerParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                parent.addView(mContainer,mContainerParams);
                if (mBgColor!=-1)
                     mContainer.setBackgroundColor(ContextCompat.getColor(activity, mBgColor));

                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(mOriginWidth, mOriginHeight);
                params.setMargins(mRect.left, mRect.top - BarUtils.getActionBarHeight(activity) -getStatusBarHeight(activity), mRect.right, mRect.bottom);

                virtalView.setBackgroundDrawable(new BitmapDrawable(activity.getResources(), cacheBitmap));
                virtalView.setLayoutParams(params);
                mContainer.addView(virtalView);

                cacheBitmap=null;
                runEnterAnim(virtalView,next_view,mContainer);
            }
        });
    }

    /**
     * 模拟入场动画
     */
    private void runEnterAnim(View next_view,final View realNextView,final FrameLayout mContainer) {

        next_view.animate()
                .setInterpolator(new LinearInterpolator())
                .setDuration(300)
                .scaleX(mScaleBundle.getFloat(SCALE_WIDTH))
                .scaleY(mScaleBundle.getFloat(SCALE_HEIGHT))
                .translationX(mTransitionBundle.getFloat(TRANSITION_X))
                .translationY(mTransitionBundle.getFloat(TRANSITION_Y))
        .setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                realNextView.setVisibility(View.GONE);
                if (mTransitionCustomListener!=null){
                    mTransitionCustomListener.onTransitionStart(animation);
                }
            }
            @Override
            public void onAnimationEnd(Animator animation) {
                mContainer.setVisibility(View.GONE);
                realNextView.setVisibility(View.VISIBLE);
                if (mTransitionCustomListener!=null){
                    mTransitionCustomListener.onTransitionEnd(animation);
                }
            }
            @Override
            public void onAnimationCancel(Animator animation) {
                if (mTransitionCustomListener!=null){
                    mTransitionCustomListener.onTransitionCancel(animation);
                }
            }
            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
    }


    /**
     * 模拟退场动画
     */
    public void exitActivity(final Activity activity) {
        if (nResId!=-1 && mContainer!=null){
            ((ViewGroup) activity.findViewById(Window.ID_ANDROID_CONTENT)).removeView(mContainer);
            activity.finish();
            activity.overridePendingTransition(0,0);

            ((ViewGroup) mFirstActivity.findViewById(Window.ID_ANDROID_CONTENT)).addView(mContainer);
            mContainer.setVisibility(View.VISIBLE);

            mContainer.getChildAt(0).animate()
                    .setInterpolator(new LinearInterpolator())
                    .setDuration(300)
                    .scaleX(1)
                    .scaleY(1)
                    .translationX(0)
                    .translationY(0)
                    .setListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            mFirstView.setVisibility(View.INVISIBLE);
                        }
                        @Override
                        public void onAnimationEnd(Animator animation) {

                            mFirstView.setVisibility(View.VISIBLE);
                            mContainer.setVisibility(View.GONE);
                            ((ViewGroup) mFirstActivity.findViewById(Window.ID_ANDROID_CONTENT)).removeView(mContainer);
                            mContainer.removeAllViews();
                            mContainer = null;
                            mFirstView = null;
                            mFirstActivity =null;
                        }
                        @Override
                        public void onAnimationCancel(Animator animation) {
                        }
                        @Override
                        public void onAnimationRepeat(Animator animation) {
                        }
                    });

        }else{
            activity.finish();
            activity.overridePendingTransition(0,0);
        }

    }
    /**
     * 计算元素缩放比例，以及位移距离
     *
     * @param
     */
    private void getBundleInfo(View mView,int mOriginWidth,int mOriginHeight,Rect mRect) {
        // 计算元素缩放比例
        mScaleBundle.putFloat(SCALE_WIDTH, (float) mView.getWidth() / mOriginWidth);
        mScaleBundle.putFloat(SCALE_HEIGHT, (float) mView.getHeight() / mOriginHeight);

        Rect rect = new Rect();
        mView.getGlobalVisibleRect(rect);
        // 计算位移距离
        mTransitionBundle.putFloat(TRANSITION_X, (rect.left+(rect.right - rect.left) / 2) - (mRect.left + (mRect.right - mRect.left) / 2));
        mTransitionBundle.putFloat(TRANSITION_Y, (rect.top + (rect.bottom - rect.top) / 2) - (mRect.top + (mRect.bottom - mRect.top) / 2));

    }
    /**
     * 获取状态栏高度
     *
     * @return
     */
    private int getStatusBarHeight(Activity activity) {

        return BarUtils.getStatusBarHeight(activity);
    }
}
