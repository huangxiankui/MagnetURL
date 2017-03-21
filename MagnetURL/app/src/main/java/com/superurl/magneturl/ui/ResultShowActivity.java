package com.superurl.magneturl.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.superurl.magneturl.R;
import com.superurl.magneturl.common.AsyncResponse;
import com.superurl.magneturl.common.MagnetUrl;
import com.superurl.magneturl.common.SearchTask;
import com.superurl.magneturl.utils.CommonUtils;
import com.superurl.magneturl.utils.Constant;
import com.superurl.magneturl.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;


public class ResultShowActivity extends Activity implements AsyncResponse {
    private ListView mListView = null;
    private String content = null;
    private ResultShowAdapter mAdapter;
    private List<MagnetUrl> Mmagneturls = new ArrayList<MagnetUrl>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultshow);
        initData();
        initUiResource();
        initList(content);
    }

    private void initData() {
        Intent intent = getIntent();
        this.content = intent.getStringExtra("content");
        ToastUtil.showToast(getApplicationContext(), "正在玩命搜索中。。。");
    }

    public void initUiResource() {
        this.mListView = (ListView) findViewById(R.id.show_list);
    }

    private void initList(String key) {
        if (!CommonUtils.isNetworkAvailable(getApplicationContext())) {
            ToastUtil.showToast(getApplicationContext(), "请先检查网络连接情况！");
        } else {
            //异步执行
            SearchTask searchtask = new SearchTask();
            searchtask.setOnAsyncResponse(this);
            searchtask.execute(key);
        }
    }

    @Override
    public void onDataReceivedSuccess(List<MagnetUrl> magneturl) {
        Log.d(Constant.TAG, "onDataReceivedSucess!");
        if (magneturl != null) {
            for (int i = 0; i < magneturl.size(); i++) {
                if (magneturl.get(i) != null) {
                    this.Mmagneturls.add(magneturl.get(i));
                }

            }
            this.mAdapter = new ResultShowAdapter();
            this.mAdapter.setContext(getBaseContext());
            this.mAdapter.addMsgNetList(this.Mmagneturls);
            this.mListView.setAdapter(this.mAdapter);
        }

    }

    @Override
    public void onDataReceivedFailed() {
        Log.d(Constant.TAG, "onDataReceivedFailed!");
        ToastUtil.showToast(getApplicationContext(), "搜索失败啦！");

    }
}
