package com.r2.scau.moblieofficing.activity;

import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.r2.scau.moblieofficing.Contants;
import com.r2.scau.moblieofficing.R;
import com.r2.scau.moblieofficing.gson.GsonUsers;
import com.r2.scau.moblieofficing.retrofit.ILoginBiz;
import com.r2.scau.moblieofficing.smack.SmackManager;
import com.r2.scau.moblieofficing.untils.OkHttpUntil;
import com.r2.scau.moblieofficing.untils.RetrofitUntil;
import com.r2.scau.moblieofficing.untils.UserUntil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchPhoneActivity extends BaseActivity {

    private TextView mTitleTV;
    private Toolbar toolbar;
    private SearchView mSearchView;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_search_phone);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mTitleTV = (TextView) findViewById(R.id.toolbar_title);
        mSearchView = (SearchView) findViewById(R.id.sv_phone);
        mSearchView.setIconifiedByDefault(false);

        toolbar.setTitle("");
        mTitleTV.setText("添加");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                getUserInfo(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    public void getUserInfo(final String phone){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Contants.SERVER_IP + "/group/")
                .callFactory(OkHttpUntil.getInstance())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ILoginBiz iLoginBiz = retrofit.create(ILoginBiz.class);
        Call<GsonUsers> call = iLoginBiz.getUserInfo(UserUntil.phone);
        call.enqueue(new Callback<GsonUsers>() {
            @Override
            public void onResponse(Call<GsonUsers> call, Response<GsonUsers> response) {
                GsonUsers gsonUsers = response.body();
                if(gsonUsers.getCode() == 200){
                    Log.e("getUser", "success");
                    SmackManager.getInstance().addFriend(phone, gsonUsers.getUserInfo().getNickname(), null);
                    Toast toast = Toast.makeText(getApplicationContext(), "添加好友成功", Toast.LENGTH_SHORT);
                    toast.show();
                    RetrofitUntil.getFriend();
                    finish();
                }else {
                    Toast toast = Toast.makeText(getApplicationContext(), "添加好友失败", Toast.LENGTH_SHORT);
                    toast.show();
                }

            }

            @Override
            public void onFailure(Call<GsonUsers> call, Throwable t) {
                Toast toast = Toast.makeText(getApplicationContext(), "添加好友失败", Toast.LENGTH_SHORT);
                toast.show();
                Log.e("getUser", "fail");
            }
        });
    }

    @Override
    public void onClick(View view) {

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
