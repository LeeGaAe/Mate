package com.example.mate.Activity.Vo;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by 가애 on 2017-12-22.
 */
@IgnoreExtraProperties
public class DiaryVo {

    private String postingId;
    private String writerId;
    private String title;
    private String content;
    private String date;

    public DiaryVo(){

    }

    public String getPostingId() {
        return postingId;
    }

    public void setPostingId(String postingId) {
        this.postingId = postingId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getWriterId() {
        return writerId;
    }

    public void setWriterId(String writerId) {
        this.writerId = writerId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
