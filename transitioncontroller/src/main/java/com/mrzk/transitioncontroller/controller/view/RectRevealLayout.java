package com.mrzk.transitioncontroller.controller.view;


import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.mrzk.transitioncontroller.controller.animationUtils.ViewAnimationCompatUtils;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;


public class RectRevealLayout extends FrameLayout {

    private Path path;
    private float revealRadius;
    private boolean isRunning;
    private View childView;
    private float startHeight;
    private float endHeight;
    private int nDirection;



    public RectRevealLayout(Context context) {
        this(context, null);
    }

    public RectRevealLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RectRevealLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        path = new Path();


    }


    public void setChildView(View childView) {
        this.childView = childView;
    }

    public void setDirection(int nDirection){
        this.nDirection=nDirection;
    }


    public void setStartHeight(float startHeight) {
        this.startHeight = startHeight;
    }

    public void setEndHeight(float endHeight) {
        this.endHeight = endHeight;
    }

    public void setRevealRadius(float revealRadius) {
        this.revealRadius = revealRadius;

        invalidate();

    }


    public Animator getAnimator() {
        ObjectAnimator reveal = ObjectAnimator.ofFloat(this, "revealRadius", startHeight, endHeight);

        reveal.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                animationStart(animator);
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                animationEnd(animator);
            }

            @Override
            public void onAnimationCancel(Animator animator) {
                animationCancel(animator);
            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });

        return reveal;
    }

    private void animationStart(Animator animator) {
        isRunning = true;

    }

    private void animationEnd(Animator animator) {
        isRunning = false;

    }
    private void animationCancel(Animator animator) {
        isRunning = false;

    }

    @Override
    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
        if (isRunning && childView == child) {
            final int state = canvas.save();

            path.reset();
//            path.addCircle(centerX, centerY, revealRadius, Path.Direction.CW);

            switch (nDirection){
                case ViewAnimationCompatUtils.RECT_TOP:
                    path.addRect(0,0,getWidth(),revealRadius,Path.Direction.CW);
                    break;
                case ViewAnimationCompatUtils.RECT_LEFT:
                    path.addRect(0,0,revealRadius,getHeight(),Path.Direction.CW);
                    break;
                case ViewAnimationCompatUtils.RECT_BOTTOM:
                    path.addRect(0,revealRadius,getWidth(),getHeight(),Path.Direction.CW);
                    break;
                case ViewAnimationCompatUtils.RECT_RIGHT:
                    path.addRect(revealRadius,0,getWidth(),getHeight(),Path.Direction.CW);
                    break;
            }

            canvas.clipPath(path);

            boolean isInvalided = super.drawChild(canvas, child, drawingTime);

            canvas.restoreToCount(state);

            return isInvalided;
        }

        return super.drawChild(canvas, child, drawingTime);
    }


    public static class Builder {


        private View view;
//        private float centerX;
//        private float centerY;
        private float startHeight;
        private float endHeight;
        private int nDirection = ViewAnimationCompatUtils.RECT_TOP;

        public Builder(View view) {
            this.view = view;
        }


        public static Builder on(View view) {
            return new Builder(view);
        }


        public Builder Direction(int nDirection) {
            this.nDirection = nDirection;
            return this;
        }
        public Builder startHeight(float startHeight) {
            this.startHeight = startHeight;
            return this;
        }

        public Builder endHeight(float endHeight) {
            this.endHeight = endHeight;
            return this;
        }

        private void setParameter(RectRevealLayout layout) {
            layout.setDirection(nDirection);
            layout.setStartHeight(startHeight);
            layout.setEndHeight(endHeight);
            layout.setChildView(view);

        }


        public Animator create() {

            if (view.getParent() != null && view.getParent() instanceof RectRevealLayout) {

                RectRevealLayout layout = ((RectRevealLayout) view.getParent());

                setParameter(layout);

                return layout.getAnimator();
            }

            RectRevealLayout layout = new RectRevealLayout(view.getContext());


            if(Build.VERSION.SDK_INT>=11){
                layout.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            }

            setParameter(layout);


            ViewGroup.LayoutParams params = view.getLayoutParams();
            ViewGroup parent = (ViewGroup) view.getParent();
            int index = 0;


            if (parent != null) {
                index = parent.indexOfChild(view);
                parent.removeView(view);
            }

            layout.addView(view, new ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT));

            if (parent != null) {
                parent.addView(layout, index, params);
            }


            return layout.getAnimator();

        }


    }
}
