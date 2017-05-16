package com.example.mostafapc.movies.storage;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import static com.example.mostafapc.movies.storage.MoviesDBContract.*;

/**
 * Created by mostafa-pc on 5/13/2017.
 */

public class MoviesContentProvider extends ContentProvider {

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    static UriMatcher buildUriMatcher() {

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MoviesDBContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, MoviesDBContract.PATH_POPULAR_MOVIES , POPULAR_MOVIES_URI);
        matcher.addURI(authority, MoviesDBContract.PATH_TOP_RATED_MOVIES , TOP_RATED_MOVIES_URI);
        matcher.addURI(authority, MoviesDBContract.PATH_FAVOURITE_MOVIES , FAVOURITE_MOVIES_URI);

        matcher.addURI(authority, MoviesDBContract.PATH_POPULAR_MOVIES + "/#", POPULAR_MOVIE_ID);
        matcher.addURI(authority, MoviesDBContract.PATH_TOP_RATED_MOVIES + "/#", TOP_RATED_MOVIE_ID);
        matcher.addURI(authority, MoviesDBContract.PATH_FAVOURITE_MOVIES + "/#", FAVOURITE_MOVIE_ID);

        matcher.addURI(authority, MoviesDBContract.PATH_TRAILERS , TRAILERS);
        //matcher.addURI(authority, MoviesContract.PATH_TRAILERS + "/#", SINGLE_MOVIE_ID);

        matcher.addURI(authority, MoviesDBContract.PATH_REVIEWS , REVIEWS);

        //matcher.addURI(authority, MoviesContract.PATH_REVIEWS + "/#", SINGLE_REVIEW);

        return matcher;
    }

    MoviesDBHelper mOpenHelper;

    @Override
    public boolean onCreate() {
        mOpenHelper = new MoviesDBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        Cursor retCursor;
        switch(sUriMatcher.match(uri)){
            case POPULAR_MOVIES_URI:
                retCursor = db.query(
                        MoviesDBContract.popularMoviesEntries.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;

            case TOP_RATED_MOVIES_URI:

                retCursor = db.query(
                        MoviesDBContract.topRatedMoviesEntries.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;

            case FAVOURITE_MOVIES_URI:

                retCursor = db.query(
                        MoviesDBContract.favouriteMoviesEntries.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;

            case POPULAR_MOVIE_ID:
                String  [] movie_ID = MoviesDBContract.popularMoviesEntries.getMovieIDFromUri(uri);
                retCursor = db.query(
                        MoviesDBContract.popularMoviesEntries.TABLE_NAME,
                        projection,
                        MoviesDBContract.popularMoviesEntries.COLUMN_MOVIE_ID + " = ? ",
                        movie_ID,
                        null,
                        null,
                        sortOrder
                );
                break;

            case TOP_RATED_MOVIE_ID:
                String [] movie_ID1 = MoviesDBContract.topRatedMoviesEntries.getMovieIDFromUri(uri);
                retCursor = db.query(
                        MoviesDBContract.topRatedMoviesEntries.TABLE_NAME,
                        projection,
                        MoviesDBContract.topRatedMoviesEntries.COLUMN_MOVIE_ID + " = ? ",
                        movie_ID1,
                        null,
                        null,
                        sortOrder
                );
                break;

            case FAVOURITE_MOVIE_ID:
                String [] movie_ID2 = MoviesDBContract.favouriteMoviesEntries.getMovieIDFromUri(uri);
                retCursor = db.query(
                        MoviesDBContract.favouriteMoviesEntries.TABLE_NAME,
                        projection,
                        MoviesDBContract.favouriteMoviesEntries.COLUMN_MOVIE_ID + " = ? ",
                        movie_ID2,
                        null,
                        null,
                        sortOrder
                );
                break;

            case TRAILERS:
                retCursor = db.query(
                        MoviesDBContract.trailersEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;

            /*case SINGLE_MOVIE_ID:
                String temp = MoviesContract.trailersEntry.getMovieIDFromUri(uri);
                retCursor = db.query(
                        MoviesContract.trailersEntry.TABLE_NAME,
                        projection,
                        sTrailersMovieIDSelection,
                        new String[]{temp},
                        null,
                        null,
                        sortOrder
                );
                break;*/

            case REVIEWS:
                retCursor = db.query(
                        MoviesDBContract.reviewsEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;

            /*case SINGLE_REVIEW:
                String temp_movieID = MoviesContract.reviewsEntry.getMovieIDFromUri(uri);
                retCursor = db.query(
                        MoviesContract.reviewsEntry.TABLE_NAME,
                        projection,
                        sReviewsMovieIDSelection,
                        new String[]{temp_movieID},
                        null,
                        null,
                        sortOrder
                );
                break;*/

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Set the notification URI for the cursor to the one passed into the function. This
        // causes the cursor to register a content observer to watch for changes that happen to
        // this URI and any of it's descendants.
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {

        final int match = sUriMatcher.match(uri);

        switch (match) {
            case POPULAR_MOVIES_URI:
                return MoviesDBContract.popularMoviesEntries.CONTENT_TYPE;
            case TOP_RATED_MOVIES_URI:
                return MoviesDBContract.topRatedMoviesEntries.CONTENT_TYPE;
            case FAVOURITE_MOVIES_URI:
                return MoviesDBContract.favouriteMoviesEntries.CONTENT_TYPE;
            case POPULAR_MOVIE_ID:
                return MoviesDBContract.popularMoviesEntries.CONTENT_ITEM_TYPE;
            case TOP_RATED_MOVIE_ID:
                return MoviesDBContract.topRatedMoviesEntries.CONTENT_ITEM_TYPE;
            case FAVOURITE_MOVIE_ID:
                return MoviesDBContract.favouriteMoviesEntries.CONTENT_ITEM_TYPE;
            case TRAILERS:
                return MoviesDBContract.trailersEntry.CONTENT_TYPE;
            case REVIEWS:
                return MoviesDBContract.reviewsEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case FAVOURITE_MOVIES_URI: {
                long _id = db.insert(MoviesDBContract.favouriteMoviesEntries.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = MoviesDBContract.favouriteMoviesEntries.buildMovieUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int returnCount;
        switch (match) {

            case POPULAR_MOVIES_URI:
                db.beginTransaction();
                returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(MoviesDBContract.popularMoviesEntries.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;

            case TOP_RATED_MOVIES_URI:
                db.beginTransaction();
                returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(MoviesDBContract.topRatedMoviesEntries.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;

            case FAVOURITE_MOVIES_URI:
                db.beginTransaction();
                returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(MoviesDBContract.favouriteMoviesEntries.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;

            case TRAILERS:
                db.beginTransaction();
                returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(MoviesDBContract.trailersEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;

            case REVIEWS:
                db.beginTransaction();
                returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(MoviesDBContract.reviewsEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;

            default:
                return super.bulkInsert(uri, values);
        }
    }

}