package com.mairos.twisterblog.model;

import java.util.ArrayList;

/**
 * Created by Mike on 07.01.2015.
 */
public class Comment {
    public int id;
    public int post_id;
    public String body;
    public String created_at;
    public String updated_at;

    public Comment(){}

    public Comment(int id, int post_id, String body, String created_at, String updated_at){
        this.id = id;
        this.post_id = post_id;
        this.body = body;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    @SuppressWarnings("serial")
    public static class List extends ArrayList<Comment> {
    }
}
