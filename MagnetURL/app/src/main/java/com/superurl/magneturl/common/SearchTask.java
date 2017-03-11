package com.superurl.magneturl.common;

import android.os.AsyncTask;

import java.util.List;

public class SearchTask extends AsyncTask<String,Void,List<MagnetUrl>>{


    @Override
    protected List<MagnetUrl> doInBackground(String... params) {
        String content  = params[0];
        MagNetSearch search=new MagNetSearch();
        List<MagnetUrl> maglist = search.getSearch(content);
        return maglist;
    }
    public AsyncResponse asyncResponse;
    public void setOnAsyncResponse(AsyncResponse asyncResponse)
    {
        this.asyncResponse = asyncResponse;
    }
    @Override
    protected void onPostExecute(List<MagnetUrl> magnetUrls) {
        super.onPostExecute(magnetUrls);
        if(magnetUrls != null)
        {
            //sucess
            asyncResponse.onDataReceivedSuccess(magnetUrls);//将结果传给回调接口中的函数

        }
        else
        {
            asyncResponse.onDataReceivedFailed();
            //faliure
        }
    }
}
