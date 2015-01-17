package com.mairos.twisterblog.storage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.mairos.twisterblog.TwisterBlogApplication;
import com.mairos.twisterblog.model.Comment;
import com.mairos.twisterblog.model.Post;

import java.util.ArrayList;
import java.util.List;

import nl.qbusict.cupboard.DatabaseCompartment;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;

public class Storage {

    // ************************* Posts operations >>> ************************* //
    public Long savePost(Post post) {
        return dataCompartment.put(post);
    }
    public void savePosts(ArrayList<Post> posts) {
        dataCompartment.put(posts);
    }
    public List<Post> getPosts(){
        return dataCompartment.query(Post.class).list();
    }
    public int deleteAllPosts() {
        return database.delete(Post.class.getSimpleName(), null, null);
    }
    // ************************* <<< Posts operations ************************* //

    // ************************* Comments operations >>> ************************* //
    public void saveComments(ArrayList<Comment> comments) {
        dataCompartment.put(comments);
    }
    public void deleteCommentsByPost(int post_id) {
        dataCompartment.delete(Comment.class, "post_id = ?", Integer.toString(post_id));
    }
    public List<Comment> getCommentsByPost(int post_id){
        return dataCompartment.query(Comment.class).withSelection("post_id = ?", Integer.toString(post_id)).list();
    }
    // ************************* <<< Comments operations ************************* //

    BlogSqliteOpenHelper openHelper;
    SQLiteDatabase database;
    DatabaseCompartment dataCompartment;

    private static Storage instance;
    private static final Object INIT_LOCK = new Object();

    private Storage(Context context) {
        openHelper = new BlogSqliteOpenHelper(context);
        database = openHelper.getWritableDatabase();
        dataCompartment = cupboard().withDatabase(database);
    }

    public static Storage get() {
        if (instance == null) {
            synchronized (INIT_LOCK) {
                if (instance == null) {
                    instance = new Storage(TwisterBlogApplication.sInstance);
                }
            }
        }
        return instance;
    }
}
