package com.mrzk.example.pagelist;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.mrzk.example.R;
import com.mrzk.transitioncontroller.controller.animationUtils.TransitionController;
import com.mrzk.transitioncontroller.controller.animationUtils.ViewAnimationCompatUtils;

public class PageListActivity extends AppCompatActivity {

    private int type = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_list);
        type = getIntent().getIntExtra("type",1);
        ListView lv = (ListView) findViewById(R.id.lv);
        lv.setAdapter(new MyBaseAdapter());
    }

    public void Circle(View v){

        Animator animator = ViewAnimationCompatUtils.createCircularReveal( v, v.getWidth() / 2, v.getTop(), 30, v.getWidth())
                .setDuration(500);
        animator.setInterpolator(new LinearInterpolator());
        animator.start();
    }


    public class MyBaseAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return 16;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView ==null){
                convertView = View.inflate(getApplicationContext(),R.layout.item_list_cardview,null);
            }

            ImageView imageView = (ImageView) convertView.findViewById(R.id.customImage);

            if (type == 1){
                imageView.setImageResource(R.drawable.list1);
            }else if (type == 2){
                imageView.setImageResource(R.drawable.list2);
            }else if (type == 3){
                imageView.setImageResource(R.drawable.list3);
            }

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (type == 1){
                        TransitionController.getInstance().startActivity(PageListActivity.this,new Intent(PageListActivity.this,PageDetailActivity.class),v,R.id.iv_second);
                    }else if (type == 2){
                        TransitionController.getInstance().startActivity(PageListActivity.this,new Intent(PageListActivity.this,PageDetail2Activity.class),v,R.id.iv_second);
                    }else if (type == 3){
                        TransitionController.getInstance().startActivity(PageListActivity.this,new Intent(PageListActivity.this,PageDetail3Activity.class),v,R.id.iv_second);
                    }
                }
            });
            return convertView;
        }
    }

}
