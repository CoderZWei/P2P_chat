package com.example.zw.p2p_test;

/**
 * Created by zw on 2016/10/25.
 */
public class Msg {
    public static final int TYPE_RECEIVED=0;
    public static final int TYPE_SENT=1;
    private int type;
    private String content;

    public Msg(String content, int type) {
        this.content = content;
        this.type = type;
    }

    public String getContent() {
        return content;
    }



    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
