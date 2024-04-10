package com.example.cookbook.Models;

public class Notifications {
    enum Type {
        NA,
        LIKE,
        COMMENT,
        FOLLOW,
        RATING
    }
    private Type type = Type.NA;
    

}
