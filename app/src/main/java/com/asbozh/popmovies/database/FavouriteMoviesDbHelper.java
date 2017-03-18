package com.asbozh.popmovies.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class FavouriteMoviesDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "favouriteMoviesDb.db";

    private static final int VERSION = 1;


    public FavouriteMoviesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_TABLE = "CREATE TABLE "  + FavouriteMoviesContract.FavouriteMoviesEntry.TABLE_NAME + " (" +
                FavouriteMoviesContract.FavouriteMoviesEntry._ID                + " INTEGER PRIMARY KEY, " +
                FavouriteMoviesContract.FavouriteMoviesEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
                FavouriteMoviesContract.FavouriteMoviesEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                FavouriteMoviesContract.FavouriteMoviesEntry.COLUMN_POSTER + " TEXT NOT NULL, " +
                FavouriteMoviesContract.FavouriteMoviesEntry.COLUMN_OVERVIEW + " TEXT NOT NULL, " +
                FavouriteMoviesContract.FavouriteMoviesEntry.COLUMN_RATING + " TEXT NOT NULL, " +
                FavouriteMoviesContract.FavouriteMoviesEntry.COLUMN_RELEASE_DATE    + " TEXT NOT NULL);";

        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + FavouriteMoviesContract.FavouriteMoviesEntry.TABLE_NAME);
        onCreate(db);
    }
}
