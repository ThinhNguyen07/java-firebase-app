package com.doan.cookpad.Model;

public class Comment {
    private String idComment,idUser,Message;
    private long createDate;

    public Comment(String idComment, String idUser, String message, long createDate) {
        this.idComment = idComment;
        this.idUser = idUser;
        Message = message;
        this.createDate = createDate;
    }

    public Comment() {
    }

    public String getIdComment() {
        return idComment;
    }

    public void setIdComment(String idComment) {
        this.idComment = idComment;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }
}
