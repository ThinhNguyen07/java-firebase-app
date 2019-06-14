package com.doan.cookpad.Model;

import java.util.ArrayList;

public class Posts {
    private String IdPosts,IdUser,image_Dish,name_Dish,when_Dish;
    private ArrayList<String> resources_Dish;
    private String time_Cooking,number_PeopleEating;
    private ArrayList<CookingSteps> steps_Cooking;
    private boolean allow_Comment;
    private long timeCreate;

    public Posts(String idPosts, String idUser, String image_Dish, String name_Dish, String when_Dish, ArrayList<String> resources_Dish, String time_Cooking, String number_PeopleEating, ArrayList<CookingSteps> steps_Cooking, boolean allow_Comment, long timeCreate) {
        IdPosts = idPosts;
        IdUser = idUser;
        this.image_Dish = image_Dish;
        this.name_Dish = name_Dish;
        this.when_Dish = when_Dish;
        this.resources_Dish = resources_Dish;
        this.time_Cooking = time_Cooking;
        this.number_PeopleEating = number_PeopleEating;
        this.steps_Cooking = steps_Cooking;
        this.allow_Comment = allow_Comment;
        this.timeCreate = timeCreate;
    }

    public Posts() {
    }

    public long getTimeCreate() {
        return timeCreate;
    }

    public void setTimeCreate(long timeCreate) {
        this.timeCreate = timeCreate;
    }

    public String getIdPosts() {
        return IdPosts;
    }

    public void setIdPosts(String idPosts) {
        IdPosts = idPosts;
    }

    public String getIdUser() {
        return IdUser;
    }

    public void setIdUser(String idUser) {
        IdUser = idUser;
    }

    public String getImage_Dish() {
        return image_Dish;
    }

    public void setImage_Dish(String image_Dish) {
        this.image_Dish = image_Dish;
    }

    public String getName_Dish() {
        return name_Dish;
    }

    public void setName_Dish(String name_Dish) {
        this.name_Dish = name_Dish;
    }

    public String getWhen_Dish() {
        return when_Dish;
    }

    public void setWhen_Dish(String when_Dish) {
        this.when_Dish = when_Dish;
    }

    public ArrayList<String> getResources_Dish() {
        return resources_Dish;
    }

    public void setResources_Dish(ArrayList<String> resources_Dish) {
        this.resources_Dish = resources_Dish;
    }

    public String getTime_Cooking() {
        return time_Cooking;
    }

    public void setTime_Cooking(String time_Cooking) {
        this.time_Cooking = time_Cooking;
    }

    public String getNumber_PeopleEating() {
        return number_PeopleEating;
    }

    public void setNumber_PeopleEating(String number_PeopleEating) {
        this.number_PeopleEating = number_PeopleEating;
    }

    public ArrayList<CookingSteps> getSteps_Cooking() {
        return steps_Cooking;
    }

    public void setSteps_Cooking(ArrayList<CookingSteps> steps_Cooking) {
        this.steps_Cooking = steps_Cooking;
    }

    public boolean isAllow_Comment() {
        return allow_Comment;
    }

    public void setAllow_Comment(boolean allow_Comment) {
        this.allow_Comment = allow_Comment;
    }
}
