package com.mu.compet.data;

/**
 * Created by Mu on 2016-10-20.
 */

public class Reply implements java.io.Serializable {

    private int userNum;
    private String userNick;
    private String userId;
    private int replyNum;
    private String replyContent;
    private String replyRegDate;
    private String replyUdtDate;

    public int getUserNum() {
        return userNum;
    }

    public void setUserNum(int userNum) {
        this.userNum = userNum;
    }

    public String getUserNick() {
        return userNick;
    }

    public void setUserNick(String userNick) {
        this.userNick = userNick;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getReplyNum() {
        return replyNum;
    }

    public void setReplyNum(int replyNum) {
        this.replyNum = replyNum;
    }

    public String getReplyContent() {
        return replyContent;
    }

    public void setReplyContent(String replyContent) {
        this.replyContent = replyContent;
    }

    public String getReplyRegDate() {
        return replyRegDate;
    }

    public void setReplyRegDate(String replyRegDate) {
        this.replyRegDate = replyRegDate;
    }

    public String getReplyUdtDate() {
        return replyUdtDate;
    }

    public void setReplyUdtDate(String replyUdtDate) {
        this.replyUdtDate = replyUdtDate;
    }
}
