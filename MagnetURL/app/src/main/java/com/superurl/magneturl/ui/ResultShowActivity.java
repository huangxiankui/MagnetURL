package com.superurl.magneturl.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.superurl.magneturl.R;
import com.superurl.magneturl.common.AsyncResponse;
import com.superurl.magneturl.common.MagnetUrl;
import com.superurl.magneturl.common.SearchTask;
import com.superurl.magneturl.utils.CommonUtils;
import com.superurl.magneturl.utils.Constant;
import com.superurl.magneturl.utils.ToastUtil;
import com.superurl.magneturl.view.ProgressBarCircular;
import com.superurl.magneturl.view.ProgressBarEx;

import java.util.ArrayList;
import java.util.List;


public class ResultShowActivity extends Activity implements AsyncResponse, AbsListView.OnScrollListener {
    private static final String TAG = "ResultShowActivity";
    private ListView mListView = null;
    private String content = null;
    private ResultShowAdapter mAdapter;
    private List<MagnetUrl> Mmagneturls = new ArrayList<MagnetUrl>();
    public ProgressBarEx mProgressBarEx;
    public TextView mSearchtext;
    private boolean mLastRow;

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
    }

    public void initUiResource() {
        this.mListView = (ListView) findViewById(R.id.show_list);
        this.mProgressBarEx = (ProgressBarEx) findViewById(R.id.progress);
        this.mSearchtext = (TextView) findViewById(R.id.search_text);
        this.mListView.setOnScrollListener(this);
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
        mProgressBarEx.setVisibility(View.GONE);
        mSearchtext.setVisibility(View.GONE);
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
        mProgressBarEx.setVisibility(View.GONE);
        mSearchtext.setVisibility(View.GONE);
        Log.d(Constant.TAG, "onDataReceivedFailed!");
        ToastUtil.showToast(getApplicationContext(), "搜索失败啦！");

    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        switch (scrollState) {
            case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                Log.d(TAG, "已经停止：SCROLL_STATE_IDLE");
                break;
            case AbsListView.OnScrollListener.SCROLL_STATE_FLING:
                Log.d(TAG, "开始滚动：SCROLL_STATE_FLING");
                break;
            case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
                Log.d(TAG, "正在滚动：SCROLL_STATE_TOUCH_SCROLL");
                break;
        }

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        Log.d(TAG, "onScroll!");
        if (view.getLastVisiblePosition() == (view.getCount() - 1)) {
            mLastRow = true;
            showLoading(Constant.SHOW_LOAD);
            Log.d(TAG, "到底部了！");
        } else {
            showLoading(Constant.SHOW_HIDE);
            mLastRow = false;
            Log.d(TAG, "没到底部！");
        }
    }

    private void showLoading(int type) {
        LayoutInflater layoutInflater = this.getLayoutInflater();
        LinearLayout layout_pro = (LinearLayout) layoutInflater.inflate(R.layout.loading_bar, null);
        ProgressBarCircular loading_pro_bar = (ProgressBarCircular) layout_pro.findViewById(R.id.loading_pro_bar);
        TextView end_text = (TextView) layout_pro.findViewById(R.id.end);
        if (type == Constant.SHOW_LOAD) {
            Log.d(TAG, "showLoading!");
            loading_pro_bar.setVisibility(View.VISIBLE);
            end_text.setVisibility(View.VISIBLE);

        } else if (type == Constant.SHOW_HIDE) {
            Log.d(TAG, "showHide!");
            loading_pro_bar.setVisibility(View.GONE);
            end_text.setVisibility(View.GONE);
            mListView.removeFooterView(layout_pro);

        }
    }

    private void loadingError(String msg) {
        Log.d(TAG, "LoadingError!");

    }
}
