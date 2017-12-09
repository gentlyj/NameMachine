package com.ifading.namemachine.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ifading.namemachine.R;
import com.ifading.namemachine.adapter.NameNoteRvAdapter;
import com.ifading.namemachine.db.NameBean;
import com.ifading.namemachine.db.NameNoteBean;
import com.ifading.namemachine.db.ObjectBoxUtils;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.objectbox.Box;
import io.objectbox.query.QueryBuilder;

public class MainActivity extends AppCompatActivity implements NameNoteRvAdapter.OnItemClickListener {
    public static final String LAST_NAME = "last_name";
    public static final String MID_NAME = "mid_name";
    public static final String NAME_NOTE_ID = "name_note_id";
    public static final String THREE_CHARCTER_NAME = "three_charcter_name";
    private static final String TAG = "MainActivity";
    @BindView(R.id.main_btn_generate)
    protected Button mBtnGenerate;
    private Box<NameNoteBean> nameNoteBox;
    @BindView(R.id.main_rv)
    protected RecyclerView mRv;
    private NameNoteRvAdapter mAdapter;
    private List<NameNoteBean> nameNotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initRv();
        initData();
    }

    private void initRv() {
        mAdapter = new NameNoteRvAdapter();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this.getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        mRv.setLayoutManager(layoutManager);
        mRv.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
    }

    private void initData() {
        initBox();
        loadNameNote();
    }

    private void initBox() {
        nameNoteBox = ObjectBoxUtils.getBoxStore().boxFor(NameNoteBean.class);
    }

    @OnClick({R.id.main_btn_generate})
    public void onClick(View v) {
        if (v == mBtnGenerate) {
            showGenerateDialog();
        }
    }

    private void showGenerateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = View.inflate(this, R.layout.dialog_generate_name_table, null);
        builder.setView(dialogView);
        final EditText edLastName = (EditText) dialogView.findViewById(R.id.ed_last_name);
        final EditText edMidName = (EditText) dialogView.findViewById(R.id.ed_mid_name);
        Button btnOK = (Button) dialogView.findViewById(R.id.dialog_generate_btn_ok);
        Button btnCancel = (Button) dialogView.findViewById(R.id.dialog_generate_btn_cancel);
        final AlertDialog dialog = builder.show();
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean threeCharcterName = false;
                if (edLastName.getText().length() == 0) {
                    showToast("姓氏不能为空");
                }
                if (edMidName.getText().length() > 0) {
                    threeCharcterName = true;
                }

                String lastName = edLastName.getText().toString();
                String midName = "";
                if (threeCharcterName) {
                    midName = edMidName.getText().toString();
                }
                addNameNote(lastName);
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

    private void addNameNote(String lastName) {
        NameNoteBean nameNoteBean = new NameNoteBean();
        nameNoteBean.setItemName(lastName);
        nameNoteBox.put(nameNoteBean);
        loadNameNote();
    }

    private void loadNameNote() {
        QueryBuilder<NameNoteBean> query = nameNoteBox.query();
        nameNotes = query.build().find();
        if (nameNotes.size() != 0) {
            mAdapter.setData(nameNotes);
            mAdapter.notifyDataSetChanged();
        }
        for (NameNoteBean bean :
                nameNotes) {
            Log.d(TAG, "NameNoteBean:" + bean.toString());
        }
    }

    private void showToast(String content) {
        Toast.makeText(this.getApplicationContext(), content, Toast.LENGTH_SHORT).show();
    }


    public void startNameActivity(long id, String lastName) {
        Intent intent = new Intent(MainActivity.this, NameActivity.class);
        Log.d(TAG, "onItemClick,id:" + id + " lastName:" + lastName);
        intent.putExtra(NAME_NOTE_ID, id);
        intent.putExtra(LAST_NAME, lastName);
        startActivity(intent);
    }

    @Override
    public void onItemClick(int position) {
        NameNoteBean nameNoteBean = nameNotes.get(position);
        long id = nameNoteBean.getId();
        String lastName = nameNoteBean.getItemName();
        startNameActivity(id, lastName);
    }

    @Override
    public void onLongClick(final int position) {
        new QMUIDialog.MessageDialogBuilder(this)
                .setTitle("提示")
                .setMessage("确定要删除该姓名本吗？")
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
                        NameNoteBean nameNoteBean = nameNotes.get(position);
                        nameNoteBox.remove(nameNoteBean);
                        loadNameNote();
                    }
                }).show();
    }
}
