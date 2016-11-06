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

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;


public class CircularRevealLayout extends FrameLayout {

    private Path path;
    private float centerX;
    private float centerY;
    private float revealRadius;
    private boolean isRunning;
    private View childView;
    private float startRadius;
    private float endRadius;



    public CircularRevealLayout(Context context) {
        this(context, null);
    }

    public CircularRevealLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircularRevealLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        path = new Path();


    }


    public void setChildView(View childView) {
        this.childView = childView;
    }

    public void setCenterX(float centerX) {
        this.centerX = centerX;
    }

    public void setCenterY(float centerY) {
        this.centerY = centerY;
    }

    public void setStartRadius(float startRadius) {
        this.startRadius = startRadius;
    }

    public void setEndRadius(float endRadius) {
        this.endRadius = endRadius;
    }

    public void setRevealRadius(float revealRadius) {
        this.revealRadius = revealRadius;

        invalidate();

    }


    public Animator getAnimator() {
        ObjectAnimator reveal = ObjectAnimator.ofFloat(this, "revealRadius", startRadius, endRadius);

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
            path.addCircle(centerX, centerY, revealRadius, Path.Direction.CW);

            canvas.clipPath(path);

            boolean isInvalided = super.drawChild(canvas, child, drawingTime);

            canvas.restoreToCount(state);

            return isInvalided;
        }

        return super.drawChild(canvas, child, drawingTime);
    }


    public static class Builder {


        private View view;
        private float centerX;
        private float centerY;
        private float startRadius;
        private float endRadius;

        public Builder(View view) {
            this.view = view;
        }


        public static Builder on(View view) {
            return new Builder(view);
        }


        public Builder centerX(float centerX) {
            this.centerX = centerX;
            return this;
        }

        public Builder centerY(float centerY) {
            this.centerY = centerY;
            return this;
        }

        public Builder startRadius(float startRadius) {
            this.startRadius = startRadius;
            return this;
        }

        public Builder endRadius(float endRadius) {
            this.endRadius = endRadius;
            return this;
        }

        private void setParameter(CircularRevealLayout layout) {
            layout.setCenterX(centerX);
            layout.setCenterY(centerY);
            layout.setStartRadius(startRadius);
            layout.setEndRadius(endRadius);
            layout.setChildView(view);

        }


        public Animator create() {

            if (view.getParent() != null && view.getParent() instanceof CircularRevealLayout) {

                CircularRevealLayout layout = ((CircularRevealLayout) view.getParent());

                setParameter(layout);

                return layout.getAnimator();
            }

            CircularRevealLayout layout = new CircularRevealLayout(view.getContext());


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
