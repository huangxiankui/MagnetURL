package com.superurl.magneturl.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
    private boolean mLastRow = false;
    private LinearLayout layout_pro;
    private ProgressBarCircular loading_pro_bar;
    private TextView end_text;
    public int currentPage = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultshow);
        initKey();
        initUiResource();
        loadMore();
    }

    private void initKey() {
        Intent intent = getIntent();
        this.content = intent.getStringExtra("content");
    }

    public void initUiResource() {
        this.mListView = (ListView) findViewById(R.id.show_list);
        this.mProgressBarEx = (ProgressBarEx) findViewById(R.id.progress);
        this.mSearchtext = (TextView) findViewById(R.id.search_text);
        LayoutInflater layoutInflater = this.getLayoutInflater();
        layout_pro = (LinearLayout) layoutInflater.inflate(R.layout.loading_bar, null);
        loading_pro_bar = (ProgressBarCircular) layout_pro.findViewById(R.id.loading_pro_bar);
        end_text = (TextView) layout_pro.findViewById(R.id.end);
        mListView.addFooterView(layout_pro);
        this.mListView.setOnScrollListener(this);
        this.mAdapter = new ResultShowAdapter();
        this.mAdapter.setContext(getBaseContext());
        this.mListView.setAdapter(this.mAdapter);
    }


    @Override
    public void onDataReceivedSuccess(List<MagnetUrl> magneturl) {
        Log.d(Constant.TAG, "onDataReceivedSucess!");
        mProgressBarEx.setVisibility(View.GONE);
        mSearchtext.setVisibility(View.GONE);
        if (magneturl != null) {
            this.mAdapter.addMsgNetList(magneturl);
            mAdapter.notifyDataSetChanged();
        }
        else {
            ToastUtil.showToast(getApplicationContext(),R.string.no_things);
        }
    }

    @Override
    public void onDataReceivedFailed() {
        Log.e(Constant.TAG, "onDataReceivedFailed!");
        mProgressBarEx.setVisibility(View.GONE);
        mSearchtext.setVisibility(View.GONE);
        ToastUtil.showToast(getApplicationContext(), "加载失败啦！");
        showLoading(Constant.SHOW_HIDE);
    }

    @Override
    public void onCancleLoad() {
        finish();
    }

    @Override
    public void onDataReceiving() {
        mProgressBarEx.setVisibility(View.VISIBLE);
        mSearchtext.setVisibility(View.VISIBLE);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        switch (scrollState) {
            case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                Log.d(TAG, "已经停止：SCROLL_STATE_IDLE");
                if (mLastRow) {
                    loadMore();
                }
                break;
            case AbsListView.OnScrollListener.SCROLL_STATE_FLING:
                break;
            case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
                break;
        }

    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    Log.e(TAG, "showLoading" + msg.arg1);
                    if (msg.arg1 != 1) {
                        showLoading(Constant.SHOW_LOAD);
                    }
                    getDatas(content, msg.arg1);
                    break;
                default:
                    break;
            }
        }
    };

    public void loadMore() {
        Log.d(TAG, "getCount:" + mAdapter.getCount());
        int rem = mAdapter.getCount() % 20;
        if (rem == 0) {
            currentPage = mAdapter.getCount() / 20;
        } else {
            currentPage = mAdapter.getCount() / 20 + 1;
        }
        Log.d(TAG, "当前页为：" + currentPage);
        if (mLastRow) {
            Log.d(TAG, "到底部了，开始加载！");
            new Thread() {
                @Override
                public void run() {
                    Message message = mHandler.obtainMessage();
                    message.what = 0;
                    message.arg1 = currentPage + 1;
                    mHandler.sendMessage(message);
                }
            }.start();
        }
        mLastRow = false;
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        //判断是否滑倒底部
        mLastRow = (totalItemCount == firstVisibleItem + visibleItemCount);
    }

    private void showLoading(int type) {
        if (type == Constant.SHOW_LOAD) {
            loading_pro_bar.setVisibility(View.VISIBLE);
            end_text.setVisibility(View.VISIBLE);

        } else if (type == Constant.SHOW_HIDE) {
            loading_pro_bar.setVisibility(View.GONE);
            end_text.setVisibility(View.GONE);
        } else if (type == Constant.SHOW_LOAD_END) {
            loading_pro_bar.setVisibility(View.GONE);
            end_text.setVisibility(View.VISIBLE);
        }
    }

    public void getDatas(String key, int pagenum) {
        Log.e(TAG, "pagenum:" + pagenum);
        SearchTask searchtask = new SearchTask(pagenum);
        searchtask.setOnAsyncResponse(this);
        searchtask.execute(key);
    }

    private void loadingError(String msg) {
        Log.d(TAG, "LoadingError!");
    }
}
