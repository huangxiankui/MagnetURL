package com.superurl.magneturl.common;

import java.util.List;

public interface AsyncResponse {
	public void onDataReceivedSuccess(List<MagnetUrl> cililist);
    public  void onDataReceivedFailed();
}
