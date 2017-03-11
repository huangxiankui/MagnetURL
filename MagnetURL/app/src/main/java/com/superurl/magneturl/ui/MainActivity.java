package com.superurl.magneturl.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.superurl.magneturl.R;
import com.superurl.magneturl.utils.ToastUtil;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button mSearchBT;
    private EditText mInputET;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = getApplicationContext();
        mSearchBT = (Button) findViewById(R.id.serach);
        mSearchBT.setOnClickListener(this);
        mInputET = (EditText) findViewById(R.id.input);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.serach:
                String content = mInputET.getText().toString();
                if (content.isEmpty()) {
                    ToastUtil.showToast(mContext, "请输入搜索内容！");
                    break;
                }
                Intent intent  = new Intent(this,ResultShow.class);
                intent.putExtra("content",content);
                startActivity(intent);

                break;
            default:
                break;
        }

    }
}
