package cn.mike.me.noticeview;

/**
 * Created by ske on 2016/11/9.
 */

public class NormalItem {
    private String img;
    private String text;

    public NormalItem() {
    }

    public NormalItem(String img, String text) {
        this.img = img;
        this.text = text;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
