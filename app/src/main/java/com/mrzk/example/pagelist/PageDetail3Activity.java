package com.mrzk.example.pagelist;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;

import com.mrzk.example.R;
import com.mrzk.transitioncontroller.controller.animationUtils.TransitionController;
import com.mrzk.transitioncontroller.controller.listener.TransitionCustomListener;
import com.mrzk.transitioncontroller.controller.animationUtils.ViewAnimationCompatUtils;

public class PageDetail3Activity extends AppCompatActivity {

    private NestedScrollView nsv;
    private FloatingActionButton mFloatingActionButton;
    private CardView cardview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagedetail);
        cardview = (CardView) findViewById(R.id.cardview);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        getSupportActionBar().hide();
        nsv = (NestedScrollView) findViewById(R.id.nsv);
        nsv.setVisibility(View.INVISIBLE);
        ImageView iv_second = (ImageView) findViewById(R.id.iv_second);
        iv_second.setImageResource(R.drawable.list3);
        mFloatingActionButton = (FloatingActionButton) findViewById(R.id.fabbtn);

            mFloatingActionButton.setScaleX(0);
            mFloatingActionButton.setScaleY(0);
        TransitionController.getInstance().setEnterListener(new TransitionCustomListener() {
            @Override
            public void onTransitionStart(Animator animator) {
            }

            @Override
            public void onTransitionEnd(Animator animator) {
                getSupportActionBar().show();
                mFloatingActionButton.animate().setDuration(300).scaleX(1).scaleY(1);

                Animator mAnimator = ViewAnimationCompatUtils.createRectReveal( cardview, 0, cardview.getHeight(),ViewAnimationCompatUtils.RECT_TOP);
                mAnimator.setDuration(500);
                mAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
                mAnimator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                    }
                    @Override
                    public void onAnimationStart(Animator animation) {
                        nsv.setVisibility(View.VISIBLE);
                        super.onAnimationStart(animation);
                    }
                });
                mAnimator.start();
            }

            @Override
            public void onTransitionCancel(Animator animator) {
            }
        });
        TransitionController.getInstance().show(this,getIntent());
    }

    @Override
    public void onBackPressed() {

        mFloatingActionButton.animate().scaleX(0).scaleY(0).setListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                TransitionController.getInstance().exitActivity(PageDetail3Activity.this);

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }
}
