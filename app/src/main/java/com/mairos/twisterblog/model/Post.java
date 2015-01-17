package com.mairos.twisterblog.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Mike on 06.01.2015.
 */
public class Post implements Serializable {
    public int id;
    public String title;
    public String body;
    public String created_at;
    public String updated_at;

    public Post(){}

    public Post(int id, String title, String body, String created_at, String updated_at){
        this.id = id;
        this.title = title;
        this.body = body;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    @SuppressWarnings("serial")
    public static class List extends ArrayList<Post> {
    }
}
