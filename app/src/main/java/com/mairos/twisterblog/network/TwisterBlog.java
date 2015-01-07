package com.mairos.twisterblog.network;

import com.mairos.twisterblog.model.Comment;
import com.mairos.twisterblog.model.Post;
import com.mairos.twisterblog.model.RequestResult;

import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by Mike on 06.01.2015.
 */
public interface TwisterBlog {

    public static String API_URL = "http://guess-composer.ru/twister_blog";

    @GET("/select_all.php")
    Post.List getPosts();

    @GET("/select_comments.php")
    Comment.List getComments(@Query("post_id") int post_id);

    @GET("/insert_post.php")
    RequestResult addPost(@Query("post_title") String post_title, @Query("post_body") String post_body);
}
