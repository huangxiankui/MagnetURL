package com.superurl.magneturl.common;


import android.util.Log;

import com.superurl.magneturl.utils.Constant;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class MagNetSearch implements ISearch {
    public List<MagnetUrl> getSearch(String content, int pagenum) throws Exception {
        String s = content + "-first-asc-" + pagenum;
        String pageurl = null;
        List<MagnetUrl> mMagnetUrls = new ArrayList<MagnetUrl>();
        MagnetUrl magneturl;
        try {
            pageurl = Constant.mHeadUrl + URLEncoder.encode(s, "UTF-8");
            Document doc = null;
            try {
                doc = Jsoup.connect(pageurl)
                        .userAgent("Mozilla/5.0 (X11; Ubuntu; Linux i686; rv:47.0) Gecko/20100101 Firefox/47.0")
                        .timeout(10000)
                        .get();
                String title = doc.title();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Elements urls = doc.select("div.search-item");
            if (urls.isEmpty()) {
                //null
            }
            for (Element url : urls) {
                magneturl = new MagnetUrl();
                String itemtitle = url.select("div.item-title").select("a").text();
                magneturl.setTitle(itemtitle);
                String itemmagnet = url.select("div.item-bar").select("a.download[href^=magnet]").attr("href");
                magneturl.setMagnet(itemmagnet);
                String itemthender = url.select("div.item-bar").select("a.download[href^=thunder]").attr("href");
                String size = url.select("div.item-bar").select("span").get(3).text().substring(5);
                magneturl.setSize(size);
                magneturl.setThunder(itemthender);
                mMagnetUrls.add(magneturl);
            }
            return mMagnetUrls;

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }

    }

}
