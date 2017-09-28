package com.ifading.namemachine;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    public static final String LAST_NAME = "last_name";
    public static final String MID_NAME = "mid_name";
    @BindView(R.id.main_btn_generate)
    protected Button mBtnGenerate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.main_btn_generate})
    public void onClick(View v) {
        if (v == mBtnGenerate) {
            showGenerateDialog();
        }
    }

    private void showGenerateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = View.inflate(this, R.layout.dialog_generate, null);
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

                Intent intent = new Intent(MainActivity.this, NameActivity.class);
                intent.putExtra(LAST_NAME,lastName);
                intent.putExtra(MID_NAME,midName);
                startActivity(intent);
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

    private void showToast(String content) {
        Toast.makeText(this.getApplicationContext(), content, Toast.LENGTH_SHORT).show();
    }
}
