package com.superurl.magneturl.ui;

import android.content.ClipboardManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.superurl.magneturl.R;
import com.superurl.magneturl.common.MagnetUrl;
import com.superurl.magneturl.view.LinkButton;

import java.util.ArrayList;
import java.util.List;

public class ResultShowAdapter extends BaseAdapter implements View.OnClickListener,AdapterView.OnItemClickListener{
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
        View view = convertView;
        ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.listview_row_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.title = (TextView) view.findViewById(R.id.title);
            viewHolder.magnetbutton = (LinkButton) view.findViewById(R.id.magnet);
            viewHolder.thunterbutton = (LinkButton) view.findViewById(R.id.thunder);
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
        LinkButton button=(LinkButton)v;
        String link=button.getLink();
        ClipboardManager clip = (ClipboardManager)this.mContext.getSystemService(Context.CLIPBOARD_SERVICE);
        clip.setText(link); // 复制
        Toast toast=Toast.makeText(this.mContext, "链接已经复制到剪贴版", Toast.LENGTH_SHORT);
        if(clip.getText()!=null)
            toast.show();
        if(button.linktype()=="magnet"){
            Toast magnetsource=Toast.makeText(this.mContext, "磁力链接不行的化，试试迅雷链接", Toast.LENGTH_SHORT);
            magnetsource.show();
        }else{
            Toast thundersource=Toast.makeText(this.mContext, "迅雷链接不行的化，试试磁力链接", Toast.LENGTH_SHORT);
            thundersource.show();
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    }

    private class ViewHolder {
        TextView title;
        LinkButton magnetbutton;
        LinkButton thunterbutton;
    }
}

