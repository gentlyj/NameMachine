package com.ifading.namemachine.activity;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.TextView;

import com.ifading.namemachine.R;
import com.ifading.namemachine.adapter.SwipeNameAdapter;
import com.ifading.namemachine.db.NameBean;
import com.ifading.namemachine.db.ObjectBoxUtils;
import com.ifading.namemachine.swipecard.CardConfig;
import com.ifading.namemachine.swipecard.OverLayCardLayoutManager;
import com.ifading.namemachine.swipecard.TanTanCallback;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.objectbox.Box;

public class SelectActivity extends AppCompatActivity {
    @BindView(R.id.name_activity_tv_last_name)
    protected TextView mTvLastName;
    @BindView(R.id.name_activity_tv_mid_name)
    protected TextView mTvMidName;
    @BindView(R.id.name_activity_tv_first_name)
    protected TextView mTvFirstName;
    @BindView(R.id.name_rv)
    protected RecyclerView mRv;

    ArrayList<String> mNameList = new ArrayList<>();
    private int mLastIndex;

    SwipeNameAdapter mAdapter;
    private Box<NameBean> namesBox;
    private String lastName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);
        ButterKnife.bind(this);
        Intent intent = getIntent();

        lastName = intent.getStringExtra(MainActivity.LAST_NAME);
        String midName = intent.getStringExtra(MainActivity.MID_NAME);
        if (midName == null) {
            mTvMidName.setVisibility(View.INVISIBLE);
            generateName(lastName, true,"");
        } else {
            mTvMidName.setText(midName);
            //generateName(lastName, midName);
            generateName(lastName, false,midName);
        }
        mTvLastName.setText(lastName);
        initNameTable();
        initData();
    }

    private void initData() {
        mAdapter = new SwipeNameAdapter();
        mAdapter.setData(mNameList);
        mRv.setLayoutManager(new OverLayCardLayoutManager());
        mRv.setAdapter(mAdapter);
        CardConfig.initConfig(this);
        final TanTanCallback callback = new TanTanCallback(mRv, mAdapter, mNameList);
        final ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(mRv);
        mAdapter.notifyDataSetChanged();
        callback.setBox(namesBox);
    }

    private void initNameTable() {
        //todo 还要研究 objectBox 关于关联表,和每个表名的关系,现在有点蒙圈啊,怎么区分呢
        namesBox = ObjectBoxUtils.getBoxStore().boxFor(NameBean.class);
    }

    public void generateName(String lastName, boolean doubleChar, String midName) {

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
                String firstName = createCharacter(random, contentByAsset);
                name = lastName + midName + firstName;
            }
            addCache(name);
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
        //Log.d(TAG, "index:" + index + " isChinese(midName):" + isChinese(midName) + " isChinese(firstName):" + isChinese(firstName));
        if (isChinese(midName) && isChinese(firstName)) {
            String sMid = String.valueOf(midName);
            String sFir = String.valueOf(firstName);
            String result = sMid + sFir;
            //Log.d(TAG, "sMid:" + sMid + " char:" + midName + " sFir:" + sFir + " firstName:" + firstName + " result:" + result);
            return result;
        } else {
            return createDoubleCharacter(random, content);
        }
    }

    public static boolean isChinese(char c) {
        return c >= 0x4E00 && c <= 0x9FA5;// 根据字节码判断
    }


    /**
     * 从Asset读取txt文件
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

}
