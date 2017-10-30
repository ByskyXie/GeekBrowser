package com.jxnu.cure.geekbrowser;

import java.net.URI;

/**
 * Created by asus on 2017/10/27.
 */

public class IndexItem {
    static final int NONE_IMAGE = 0;
    private String text;
    private URI link;
    private int Img;
    IndexItem(String text,int imgSource,URI link){ this.text = text; Img = imgSource; this.link = link;}

    public String getText() {
        return text;
    }

    public int getImg() {
        return Img;
    }

    public URI getLink(){ return link;}
}
