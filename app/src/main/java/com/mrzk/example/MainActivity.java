package com.mrzk.example;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.mrzk.example.login.LoginActivity;
import com.mrzk.example.pagelist.PageListActivity;

/**
 * Created by zhangke on 2016-11-4.
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick(View v){
        Intent intent = null;

        switch (v.getId()){
            case R.id.btn_login:
                startActivity(new Intent(MainActivity.this,LoginActivity.class));
                break;
            case R.id.btn_list1:
                startActivity(new Intent(MainActivity.this,PageListActivity.class));
                break;
            case R.id.btn_list2:
                 intent = new Intent(MainActivity.this,PageListActivity.class);
                intent.putExtra("type",2);
                startActivity(intent);
                break;
            case R.id.btn_list3:
                intent = new Intent(MainActivity.this,PageListActivity.class);
                intent.putExtra("type",3);
                startActivity(intent);
                break;
        }
    }

}
