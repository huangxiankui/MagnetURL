package com.superurl.magneturl.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.superurl.magneturl.R;
import com.superurl.magneturl.common.MagnetUrl;
import com.superurl.magneturl.view.CiliButton;

import java.util.ArrayList;
import java.util.List;

public class ResultShowAdapter extends BaseAdapter implements View.OnClickListener {
    private List<MagnetUrl> mList = new ArrayList<MagnetUrl>();
    private Context mContext;

    public void setContext(Context context) {
        this.mContext = context;

    }

    public void addMsgNetList(List<MagnetUrl> msgurl) {
        for (int i = 0; i < msgurl.size(); i++) {
            this.mList.add(msgurl.get(i));
        }
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        ViewHolder viewHolder = null;
        if (convertView == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.listview_row_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.title = (TextView) view.findViewById(R.id.title);
            viewHolder.magnetbutton = (CiliButton) view.findViewById(R.id.magnet);
            viewHolder.thunterbutton = (CiliButton) view.findViewById(R.id.thunder);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        MagnetUrl msgurl = mList.get(position);
        String title = msgurl.getTitle();
        String magnetlink = msgurl.getMagnet();
        String thunderlink = msgurl.getThunder();
        if (!title.isEmpty()) {
            viewHolder.title.setText(title);
        }
        if (!magnetlink.isEmpty()) {
            viewHolder.magnetbutton.setLink(magnetlink);
            viewHolder.magnetbutton.setOnClickListener(this);
        }
        if (!thunderlink.isEmpty()) {
            viewHolder.thunterbutton.setLink(thunderlink);
            viewHolder.thunterbutton.setOnClickListener(this);
        }
        return view;
    }

    @Override
    public void onClick(View v) {

    }

    private class ViewHolder {
        TextView title;
        CiliButton magnetbutton;
        CiliButton thunterbutton;
    }
}

