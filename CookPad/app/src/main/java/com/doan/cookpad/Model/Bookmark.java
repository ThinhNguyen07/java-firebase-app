package com.doan.cookpad.Model;

public class Bookmark {
    private String idPosts,IdUser;

    public Bookmark(String idPosts, String idUser) {
        this.idPosts = idPosts;
        IdUser = idUser;
    }

    public Bookmark() {
    }

    public String getIdPosts() {
        return idPosts;
    }

    public void setIdPosts(String idPosts) {
        this.idPosts = idPosts;
    }

    public String getIdUser() {
        return IdUser;
    }

    public void setIdUser(String idUser) {
        IdUser = idUser;
    }
}
