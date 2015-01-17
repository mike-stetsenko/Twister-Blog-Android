package com.mairos.twisterblog.network;

import com.mairos.twisterblog.model.Comment;
import com.mairos.twisterblog.model.Post;
import com.mairos.twisterblog.model.RequestResult;

import java.util.ArrayList;

import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by Mike on 06.01.2015.
 */
public interface TwisterBlogHttp {

    public static String API_URL = "http://guess-composer.ru/twister_blog";

    @GET("/select_all.php")
    Post.List getPosts();

    @GET("/select_comments.php")
    Comment.List getComments(@Query("post_id") int post_id);

    @GET("/insert_post.php")
    RequestResult addPost(@Query("post_title") String post_title, @Query("post_body") String post_body);

    @GET("/insert_comment.php")
    RequestResult addComment(@Query("post_id") int post_id, @Query("comment_body") String comment_body);

    @GET("/delete_post.php")
    RequestResult deletePost(@Query("post_id") int post_id);
}
