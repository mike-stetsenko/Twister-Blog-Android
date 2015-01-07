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

    @SuppressWarnings("serial")
    public static class List extends ArrayList<Comment> {
    }
}
