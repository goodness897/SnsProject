package com.mu.compet.data;

import java.io.Serializable;

/**
 * Created by Mu on 2016-10-20.
 */

public class Board implements Serializable {

    private int userNum;
    private String userId;
    private String userNick;
    private int boardNum;
    private String boardContent;
    private String boardRegDate;
    private String boardUdtDate;
    private String firstImageUrl;
    private int boardImgCnt;
    private int boardAudCnt;
    private int boardVidCnt;
    private int boardReplyCnt;

    public String getFirstImageUrl() {
        return firstImageUrl;
    }

    public void setFirstImageUrl(String firstImageUrl) {
        this.firstImageUrl = firstImageUrl;
    }

    public int getUserNum() {
        return userNum;
    }

    public void setUserNum(int userNum) {
        this.userNum = userNum;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserNick() {
        return userNick;
    }

    public void setUserNick(String userNick) {
        this.userNick = userNick;
    }

    public int getBoardNum() {
        return boardNum;
    }

    public void setBoardNum(int boardNum) {
        this.boardNum = boardNum;
    }

    public String getBoardContent() {
        return boardContent;
    }

    public void setBoardContent(String boardContent) {
        this.boardContent = boardContent;
    }

    public String getBoardRegDate() {
        return boardRegDate;
    }

    public void setBoardRegDate(String boardRegDate) {
        this.boardRegDate = boardRegDate;
    }

    public String getBoardUdtDate() {
        return boardUdtDate;
    }

    public void setBoardUdtDate(String boardUdtDate) {
        this.boardUdtDate = boardUdtDate;
    }

    public int getBoardImgCnt() {
        return boardImgCnt;
    }

    public void setBoardImgCnt(int boardImgCnt) {
        this.boardImgCnt = boardImgCnt;
    }

    public int getBoardAudCnt() {
        return boardAudCnt;
    }

    public void setBoardAudCnt(int boardAudCnt) {
        this.boardAudCnt = boardAudCnt;
    }

    public int getBoardVidCnt() {
        return boardVidCnt;
    }

    public void setBoardVidCnt(int boardVidCnt) {
        this.boardVidCnt = boardVidCnt;
    }

    public int getBoardReplyCnt() {
        return boardReplyCnt;
    }

    public void setBoardReplyCnt(int boardReplyCnt) {
        this.boardReplyCnt = boardReplyCnt;
    }


}