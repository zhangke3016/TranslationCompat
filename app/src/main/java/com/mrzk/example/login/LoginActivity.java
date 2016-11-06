package com.mrzk.example.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.EditText;

import com.mrzk.example.R;
import com.mrzk.transitioncontroller.controller.animationUtils.TransitionController;


public class LoginActivity extends AppCompatActivity {

    EditText etUsername;
    EditText etPassword;
    CardView cv;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        etUsername = (EditText) findViewById(R.id.et_username);
        etPassword = (EditText) findViewById(R.id.et_password);
        cv = (CardView) findViewById(R.id.cv);
        fab = (FloatingActionButton) findViewById(R.id.fab);

    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab:
                TransitionController.getInstance().startActivity(this,new Intent(this, RegisterActivity.class),fab,R.id.fab);
                break;
            case R.id.bt_go:
                Intent i2 = new Intent(this,LoginSuccessActivity.class);
                startActivity(i2);
                break;
        }
    }

}
