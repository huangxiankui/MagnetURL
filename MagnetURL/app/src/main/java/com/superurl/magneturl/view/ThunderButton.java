package com.superurl.magneturl.view;

import android.content.Context;
import android.util.AttributeSet;

public class ThunderButton extends CiliButton {

	public ThunderButton(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public String linktype() {
		return "thunter";
	}

}
