package com.ifading.namemachine.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.ifading.namemachine.R;
import com.ifading.namemachine.adapter.NameRvAdapter;
import com.ifading.namemachine.constant.NameConstant;
import com.ifading.namemachine.db.NameBean;
import com.ifading.namemachine.db.NameBean_;
import com.ifading.namemachine.db.ObjectBoxUtils;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.objectbox.Box;
import io.objectbox.query.QueryBuilder;

/**
 * Created by yangjingsheng on 17/9/28.
 */

public class NameActivity extends AppCompatActivity implements NameRvAdapter.OnItemClickListener {
    private static final String TAG = "NameActivity";
    /**
     * Gridlayout colume count
     */
    private static final int SPAN_COUNT = 3;
    @BindView(R.id.name_activity_tv_last_name)
    protected TextView mTvLastName;
    @BindView(R.id.name_activity_tv_mid_name)
    protected TextView mTvMidName;
    @BindView(R.id.name_activity_tv_first_name)
    protected TextView mTvFirstName;
    @BindView(R.id.name_rv)
    protected RecyclerView mRv;
    @BindView(R.id.name_activity_generate_name)
    protected Button mBtnGenerateName;


    ArrayList<String> mNameList = new ArrayList<>();
    private int mLastIndex;

    private NameRvAdapter mAdapter;
    private Box<NameBean> namesBox;
    private String lastName;
    private int nameNoteId;
    private List<NameBean> nameDatas;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name);
        ButterKnife.bind(this);
        Intent intent = getIntent();

        nameNoteId = (int) intent.getLongExtra(MainActivity.NAME_NOTE_ID, 0);
        lastName = intent.getStringExtra(MainActivity.LAST_NAME);
        Log.d(TAG, "nameNoteId:" + nameNoteId + " lastName:" + lastName);
        mTvLastName.setText(lastName);
        initView();
        initNameTable();
        initEvent();
    }

    private void initEvent() {
        mAdapter.setOnItemClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadAndShowData();
    }

    private void initView() {
        mAdapter = new NameRvAdapter();
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this.getApplicationContext(), SPAN_COUNT, LinearLayoutManager.VERTICAL, false);
        mRv.setLayoutManager(layoutManager);
        mRv.setAdapter(mAdapter);
    }

    private void initNameTable() {
        namesBox = ObjectBoxUtils.getBoxStore().boxFor(NameBean.class);
    }

    private void loadAndShowData() {
        Log.d(TAG, "loadAndShowData,nameNoteId:" + nameNoteId);
        QueryBuilder<NameBean> query = namesBox.query().equal(NameBean_.nameNoteId, nameNoteId);

        nameDatas = query.build().find();
        mAdapter.setData(nameDatas);
        mAdapter.notifyDataSetChanged();
        mRv.scrollToPosition(mAdapter.getItemCount() - 1);
        //addNameNoteId();
        for (NameBean bean :
                nameDatas) {
            Log.d(TAG, "NAME:" + bean.toString() + " id:" + bean.getNameNoteId());
            //bean.setNameNoteId(3);
        }
        //namesBox.put(nameDatas);
    }

    private void addNameNoteId() {
        for (NameBean bean :
                nameDatas) {
            bean.setNameNoteId(nameNoteId);
            namesBox.put(bean);
        }
    }

    @OnClick({R.id.name_activity_generate_name})
    protected void onClick(View v) {
        if (v == mBtnGenerateName) {
            showGenerateDialog();
        }
    }

    private void showGenerateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = View.inflate(this, R.layout.dialog_generate_name, null);
        builder.setView(dialogView);
        final EditText edMidName = (EditText) dialogView.findViewById(R.id.ed_mid_name);
        TextView tvLastName = (TextView) dialogView.findViewById(R.id.tv_last_name);
        tvLastName.setText(lastName);
        Button btnOK = (Button) dialogView.findViewById(R.id.dialog_generate_btn_ok);
        Button btnCancel = (Button) dialogView.findViewById(R.id.dialog_generate_btn_cancel);
        final AlertDialog dialog = builder.show();
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean threeCharcterName = false;
                if (edMidName.getText().length() > 0) {
                    threeCharcterName = true;
                }

                String midName = "";
                if (threeCharcterName) {
                    midName = edMidName.getText().toString();
                }
                startSelectActivity(midName, threeCharcterName);
                dialog.dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void startSelectActivity(String midName, boolean threeCharcterName) {
        Intent intent = new Intent(this, SelectActivity.class);
        intent.putExtra(MainActivity.LAST_NAME, lastName);
        intent.putExtra(MainActivity.NAME_NOTE_ID, nameNoteId);
        intent.putExtra(MainActivity.MID_NAME, midName);
        intent.putExtra(MainActivity.THREE_CHARCTER_NAME, threeCharcterName);
        startActivity(intent);
    }

    public void generateName(String lastName, boolean doubleChar) {

        Random random = new Random();
        String contentByAsset = getContentByAsset("cuci.txt");
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
        if (Math.abs(index - mLastIndex) < NameConstant.GENERATE_NAME_DEFAULT_COUNT) {
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
        if (Math.abs(index - mLastIndex) < NameConstant.GENERATE_NAME_DEFAULT_COUNT) {
            return createDoubleCharacter(random, content);
        }
        mLastIndex = index;
        char midName = content.charAt(index);
        char firstName = content.charAt(index + 1);
        if (isChinese(midName) && isChinese(firstName)) {
            String sMid = String.valueOf(midName);
            String sFir = String.valueOf(firstName);
            String result = sMid + sFir;
            return result;
        } else {
            return createDoubleCharacter(random, content);
        }
    }

    public static boolean isChinese(char c) {
        // 根据字节码判断
        return c >= 0x4E00 && c <= 0x9FA5;
    }

    private static boolean isChinese1(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        return ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS;
    }

    /**
     * 按行读取txt
     *
     * @param is
     * @return
     * @throws Exception
     */
    private String readTextFromSDcard(InputStream is) throws Exception {
        InputStreamReader reader = new InputStreamReader(is, "gbk");
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

    @Override
    public void onItemClick(int positon) {

    }

    @Override
    public void onItemLongClick(final int position) {

        new QMUIDialog.MessageDialogBuilder(this)
                .setTitle("提示")
                .setMessage("确定要删除这个名字吗？")
                .addAction("取消", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
                .addAction("确定", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                        NameBean nameBean = nameDatas.get(position);
                        namesBox.remove(nameBean);
                        loadAndShowData();
                    }
                }).show();
    }
}
