package com.superurl.magneturl.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import com.superurl.magneturl.R;
import com.superurl.magneturl.common.AsyncResponse;
import com.superurl.magneturl.common.MagnetUrl;
import com.superurl.magneturl.common.SearchTask;

import java.util.ArrayList;
import java.util.List;


public class ResultShow extends Activity implements AsyncResponse {
    private ListView mListView = null;
    private String content = null;
    private ResultShowAdapter mAdapter;
    private List<MagnetUrl> Mmagneturls=new ArrayList<MagnetUrl>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultshow);
        initView();
        initData(content);
    }

    private void initView() {

        Intent intent = getIntent();
        String content = intent.getStringExtra("content");
    }

    private void initData(String key) {
        //异步执行
        SearchTask searchtask = new SearchTask();
        searchtask.setOnAsyncResponse(this);
        searchtask.execute(content);
    }

    @Override
    public void onDataReceivedSuccess(List<MagnetUrl> magneturl) {
        if(magneturl!=null){
            for(int i=0;i<magneturl.size();i++){
                if(magneturl.get(i)!=null){
                    this.Mmagneturls.add(magneturl.get(i));
                }

            }
            //load adapter

            this.mAdapter=new ResultShowAdapter();
            this.mAdapter.setContext(getBaseContext());
            this.mAdapter.addMsgNetList(this.Mmagneturls);
            this.mListView.setAdapter(this.mAdapter);
        }

    }

    @Override
    public void onDataReceivedFailed() {

    }
}
