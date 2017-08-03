package com.r2.scau.moblieofficing.activity;

import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.r2.scau.moblieofficing.R;

/**
 * 作者：邓嘉进
 * 作用：新建群时，上传群图片和设置群名字
 */
public class EditGroupActivity extends BaseActivity {

    private Toolbar mToolbar;
    private Button mCommitBtn;
    private EditText mNameET;
    private TextView mTitleTV;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_edit_group);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mCommitBtn = (Button) findViewById(R.id.btn_group_edit_commit);
        mNameET = (EditText) findViewById(R.id.et_group_name);
        mTitleTV = (TextView) findViewById(R.id.toolbar_title);

        mToolbar.setTitle("");
        mTitleTV.setText("编辑群信息");

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {
        mCommitBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_group_edit_commit:
                if(!mNameET.getText().toString().equals("")){
                    Intent intent = new Intent(EditGroupActivity.this, SelectMemberActivity.class);
                    startActivity(intent);
                }else {
                    Toast toast = Toast.makeText(getApplicationContext(), "群名称不能为空", Toast.LENGTH_SHORT);
                    toast.show();
                }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
