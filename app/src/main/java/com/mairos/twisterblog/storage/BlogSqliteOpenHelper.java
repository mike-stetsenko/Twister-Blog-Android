package com.mairos.twisterblog.storage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.mairos.twisterblog.model.Comment;
import com.mairos.twisterblog.model.Post;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;

public class BlogSqliteOpenHelper extends SQLiteOpenHelper {


    private final static String DB_FILE = "data.db";
    private final static int DB_VERSION = 1;

    static {
        // register our models
        cupboard().register(Post.class);
        cupboard().register(Comment.class);
    }

    public BlogSqliteOpenHelper(Context context) {
        super(context, DB_FILE, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        cupboard().withDatabase(db).createTables();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        cupboard().withDatabase(db).upgradeTables();
    }

}
