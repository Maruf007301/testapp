package com.ruetgmail.taufiqur.tipsntricks.data.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ruetgmail.taufiqur.tipsntricks.models.favorite.FavoriteModel;

import java.util.ArrayList;

public class FavoriteDbController {

    private SQLiteDatabase db;

    public FavoriteDbController(Context context) {
        db = DbHelper.getInstance(context).getWritableDatabase();
    }

    public int insertData(String postTitle, String postSubTitle, String postDetails, String imageUrl) {

        ContentValues values = new ContentValues();
        values.put(DbConstants.POST_TITLE, postTitle);
        values.put(DbConstants.POST_SUB_TITLE, postSubTitle);
        values.put(DbConstants.POST_DETAILS, postDetails);
        values.put(DbConstants.POST_IMAGE_URL, imageUrl);

        // Insert the new row, returning the primary key value of the new row
        return (int) db.insert(
                DbConstants.FAVORITE_TABLE_NAME,
                DbConstants.COLUMN_NAME_NULLABLE,
                values);
    }

    public ArrayList<FavoriteModel> getAllData() {


        String[] projection = {
                DbConstants._ID,
                DbConstants.POST_TITLE,
                DbConstants.POST_SUB_TITLE,
                DbConstants.POST_DETAILS,
                DbConstants.POST_IMAGE_URL
        };

        // How you want the results sorted in the resulting Cursor
        String sortOrder = DbConstants._ID + " DESC";

        Cursor c = db.query(
                DbConstants.FAVORITE_TABLE_NAME,  // The table name to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        return fetchData(c);
    }

    private ArrayList<FavoriteModel> fetchData(Cursor c) {
        ArrayList<FavoriteModel> favDataArray = new ArrayList<>();

        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    // get  the  data into array,or class variable
                    int itemId = c.getInt(c.getColumnIndexOrThrow(DbConstants._ID));
                    String postTitle = c.getString(c.getColumnIndexOrThrow(DbConstants.POST_TITLE));
                    String postSubTitle = c.getString(c.getColumnIndexOrThrow(DbConstants.POST_SUB_TITLE));
                    String postDetails = c.getString(c.getColumnIndexOrThrow(DbConstants.POST_DETAILS));
                    String imageUrl = c.getString(c.getColumnIndexOrThrow(DbConstants.POST_IMAGE_URL));


                    // wrap up data list and return
                    favDataArray.add(new FavoriteModel(itemId, postTitle, postSubTitle, postDetails, imageUrl));
                } while (c.moveToNext());
            }
            c.close();
        }
        return favDataArray;
    }

    public void deleteEachFav(String postTitle) {
        // Which row to update, based on the ID
        String selection = DbConstants.POST_TITLE + "=?";
        String[] selectionArgs = {postTitle};

        db.delete(
                DbConstants.FAVORITE_TABLE_NAME,
                selection,
                selectionArgs);
    }

    public void deleteAllFav() {
        db.delete(
                DbConstants.FAVORITE_TABLE_NAME,
                null,
                null);
    }

}
