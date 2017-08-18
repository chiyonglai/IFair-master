package com.ifair.module;

/**
 * 功能
 */

public class FireBaseMessage {

    /**
     * link :
     * title : 鞋子
     * message : 內容
     */

    private String link;
    private String title;
    private String message;

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "FireBaseMessage{" +
                "link='" + link + '\'' +
                ", title='" + title + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
