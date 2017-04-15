package com.superurl.magneturl.view;

import android.content.Context;
import android.util.AttributeSet;

public class MagnetButton extends LinkButton {

	public MagnetButton(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public String linktype() {
		return "magnet";
	}

	

}
