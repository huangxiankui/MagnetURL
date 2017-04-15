package com.superurl.magneturl.common;

import android.os.AsyncTask;
import android.util.Log;

import java.util.List;

public class SearchTask extends AsyncTask<String, Void, List<MagnetUrl>> {

    public int pagenum;

    public SearchTask() {
    }

    public SearchTask(int pagenum) {
        this.pagenum = pagenum;
    }

    @Override
    protected List<MagnetUrl> doInBackground(String... params) {
        String content = params[0];
        MagNetSearch search = new MagNetSearch();
        List<MagnetUrl> maglist;
        try {
            maglist = search.getSearch(content, pagenum);
        } catch (Exception e) {
            Log.e("SearchTask", "get maglist falied! try again!");
            maglist = null;
        }
        return maglist;
    }

    public AsyncResponse asyncResponse;

    public void setOnAsyncResponse(AsyncResponse asyncResponse) {
        this.asyncResponse = asyncResponse;
    }

    @Override
    protected void onCancelled() {
        asyncResponse.onCancleLoad();
        super.onCancelled();
    }

    @Override
    protected void onPostExecute(List<MagnetUrl> magnetUrls) {
        super.onPostExecute(magnetUrls);
        if (magnetUrls != null) {
            //sucess
            asyncResponse.onDataReceivedSuccess(magnetUrls);

        } else {
            //faliure
            asyncResponse.onDataReceivedFailed();
        }
    }
}
