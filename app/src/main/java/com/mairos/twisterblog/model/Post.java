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

    @SuppressWarnings("serial")
    public static class List extends ArrayList<Post> {
    }
}
