package com.ifading.namemachine;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;


import java.io.BufferedReader;
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
            mTvMidName.setVisibility(View.INVISIBLE);
            generateName(lastName, true);
        } else {
            mTvMidName.setText(midName);
            //generateName(lastName, midName);
            generateName(lastName, true);
        }
        mTvLastName.setText(lastName);

    }

    public void generateName(String lastName, boolean doubleChar) {

        Random random = new Random();
        String contentByAsset = getContentByAsset("cuci.txt");
        //Log.d(TAG, "contentByAsset:" + contentByAsset);
        if (contentByAsset == null) {
            return;
        }
        for (int i = 0; i < 100; i++) {
            String name;
            if (doubleChar) {
                name = lastName + createDoubleCharacter(random, contentByAsset);
            } else {
                String midName = createCharacter(random, contentByAsset);
                String firstName = createCharacter(random, contentByAsset);
                name = lastName + midName + firstName;
            }
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

    private String createCharacter(Random random, String content) {
        int index = random.nextInt(content.length());
        if (Math.abs(index - mLastIndex) < 100) {
            return createCharacter(random, content);
        }
        mLastIndex = index;
        char charAt = content.charAt(index);
        if (isChinese(charAt)) {
            return String.valueOf(charAt);
        } else {
            return createCharacter(random, content);
        }
    }

    private String createDoubleCharacter(Random random, String content) {
        int index = random.nextInt(content.length());
        if (Math.abs(index - mLastIndex) < 100) {
            return createDoubleCharacter(random, content);
        }
        mLastIndex = index;
        char midName = content.charAt(index);
        char firstName = content.charAt(index + 1);
        Log.d(TAG, "index:" + index + " isChinese(midName):" + isChinese(midName) + " isChinese(firstName):" + isChinese(firstName));
        if (isChinese(midName) && isChinese(firstName)) {
            String sMid = String.valueOf(midName);
            String sFir = String.valueOf(firstName);
            String result = sMid + sFir;
            Log.d(TAG, "sMid:" + sMid + " char:" + midName + " sFir:" + sFir + " firstName:" + firstName + " result:" + result);
            return result;
        } else {
            return createDoubleCharacter(random, content);
        }
    }

    public static boolean isChinese(char c) {
        return c >= 0x4E00 && c <= 0x9FA5;// 根据字节码判断
    }

    private void generateName(String lastName, String midName) {

    }

    private static final boolean isChinese1(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
            return true;
        }
        return false;
    }

    /**
     * 按行读取txt
     *
     * @param is
     * @return
     * @throws Exception
     */
    private String readTextFromSDcard(InputStream is) throws Exception {
        InputStreamReader reader = new InputStreamReader(is, "gbk");//, "UTF-8"
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
