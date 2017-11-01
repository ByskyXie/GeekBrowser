package com.jxnu.cure.geekbrowser;

import java.util.ArrayList;

/**
 * Created by asus on 2017/10/31.
 */

public final class BaseWebsite {
    private String title,uri;
    private int img_id;
    BaseWebsite (String title,String uri, int img_id){
        this.uri = uri;
        this.title = title;
        this.img_id = img_id;
    }
    public static ArrayList<BaseWebsite> getBaseWebsite(){
        ArrayList<BaseWebsite> list = new ArrayList<BaseWebsite>();
        list.add(new BaseWebsite("百度","www.baidu.com",R.drawable.ic_web_baidu));
        list.add(new BaseWebsite("淘宝","www.taobao.com",R.drawable.ic_web_taobao));
        list.add(new BaseWebsite("知乎","www.zhihu.com",R.drawable.ic_web_zhihu));
        list.add(new BaseWebsite("爱奇艺","www.aiqiyi.com",R.drawable.ic_web_aiqiyi));
        list.add(new BaseWebsite("GitHub","www.github.com",R.drawable.ic_web_github));
        list.add(new BaseWebsite("头条","www.toutiao.com",R.drawable.ic_web_toutiao));
        list.add(new BaseWebsite("360市场","zhushou.360.cn",R.drawable.ic_web_360store));
        return list;
    }

    public String getTitle() {
        return title;
    }

    public String getUri() {
        return uri;
    }

    public int getImg_id() {
        return img_id;
    }
}
