package com.ifading.namemachine;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by yangjingsheng on 17/9/28.
 */

public class NameActivity extends AppCompatActivity {
    private static final String TAG = "NameActivity";
    @BindView(R.id.name_activity_tv_last_name)
    protected TextView mTvLastName;
    @BindView(R.id.name_activity_tv_mid_name)
    protected TextView mTvMidName;
    @BindView(R.id.name_activity_tv_first_name)
    protected TextView mTvFirstName;


    ArrayList<String> mNameList = new ArrayList<>();
    private int mLastIndex;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        String lastName = intent.getStringExtra(MainActivity.LAST_NAME);
        String midName = intent.getStringExtra(MainActivity.MID_NAME);
        if (midName == null) {
            Log.d(TAG, "contentByAsset1");

            mTvMidName.setVisibility(View.INVISIBLE);
            generateName(lastName);
        } else {
            mTvMidName.setText(midName);
            //generateName(lastName, midName);
            Log.d(TAG, "contentByAsset2");
            generateName(lastName);

        }
        mTvLastName.setText(lastName);

    }

    public void generateName(String lastName) {

        Random random = new Random();
        String contentByAsset = getContentByAsset("cuci.txt");
        Log.d(TAG, "contentByAsset:" + contentByAsset);
        if (contentByAsset == null) {
            return;
        }
        random.setSeed(contentByAsset.length());
        for (int i = 0; i < 100; i++) {
            String midName = createharacterC(random, contentByAsset);
            String firstName = createharacterC(random, contentByAsset);
            String name = lastName + midName + firstName;
            addCache(name);
        }
        for (String str :
                mNameList) {
            Log.d(TAG, str);
        }
    }

    private void addCache(String name) {
        for (String str :
                mNameList) {
            if (str.equals(name)) {
                return;
            }
        }
        mNameList.add(name);
    }

    private String createharacterC(Random random, String content) {
        int index = random.nextInt(content.length());
        if (Math.abs(index - mLastIndex) < 100) {
            return createharacterC(random, content);
        }
        mLastIndex = index;
        char charAt = content.charAt(index);
        if (isChinese(charAt)) {
            return String.valueOf(charAt);
        } else {
            return createharacterC(random, content);
        }
    }

    public static boolean isChinese(char c) {
        return c >= 0x4E00 && c <= 0x9FA5;// 根据字节码判断
    }

    private void generateName(String lastName, String midName) {

    }

    /**
     * 按行读取txt
     *
     * @param is
     * @return
     * @throws Exception
     */
    private String readTextFromSDcard(InputStream is) throws Exception {
        InputStreamReader reader = new InputStreamReader(is);//, "UTF-8"
        BufferedReader bufferedReader = new BufferedReader(reader);
        StringBuffer buffer = new StringBuffer("");
        String str;
        while ((str = bufferedReader.readLine()) != null) {
            buffer.append(str);
            buffer.append("\n");
        }

        return buffer.toString();
    }

    /**
     * 从Asset读取text文件
     *
     * @param filename 文件名
     */
    private String getContentByAsset(String filename) {
        try {
            AssetManager am = getAssets();
            InputStream is = am.open(filename);
            final String content = readTextFromSDcard(is) + "";
            return content;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
