package com.ifading.namemachine;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by yangjingsheng on 17/9/28.
 */

public class NameActivity extends AppCompatActivity{
    @BindView(R.id.name_activity_tv_last_name)
    protected TextView mTvLastName;
    @BindView(R.id.name_activity_tv_mid_name)
    protected TextView mTvMidName;
    @BindView(R.id.name_activity_tv_first_name)
    protected TextView mTvFirstName;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        String lastName = intent.getStringExtra(MainActivity.LAST_NAME);
        String midName = intent.getStringExtra(MainActivity.MID_NAME);
        if (midName == null){
            mTvMidName.setVisibility(View.INVISIBLE);
            generateName(lastName,null);
        }else{
            mTvMidName.setText(midName);
            generateName(lastName,midName);

        }
        mTvLastName.setText(lastName);

    }

    private void generateName(String lastName, String midName) {

    }
}
