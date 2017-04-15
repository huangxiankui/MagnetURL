package com.superurl.magneturl.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageButton;

public abstract class LinkButton extends ImageButton {
    private String link;

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public LinkButton(Context context, AttributeSet attrs) {
        super(context, attrs);

    }
    public abstract String linktype();


}
