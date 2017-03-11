package com.superurl.magneturl.common;

import java.util.List;

public interface AsyncResponse {
	public void onDataReceivedSuccess(List<MagnetUrl> list);
    public  void onDataReceivedFailed();
}
