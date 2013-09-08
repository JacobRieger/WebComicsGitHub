package com.webcomic.services;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import com.webcomic.models.Comic;
import com.webcomic.models.Content;

public class DataBaseService extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "webcomicsManager";

    // Comics table name
    private static final String TABLE_WEBCOMICS = "Webcomics";
    private static final String TABLE_CONTENT = "Content";

    // Comics Table Columns names
    private static final String KEY_ID      = "id";
    private static final String KEY_NAME    = "name";
    private static final String KEY_WEBSITE = "website";
    private static final String KEY_LAST_UPDATED = "last_updated";
    private static final String KEY_HAS_BEEN_VIEWED = "has_been_viewed";

    //Content Table Column names
    private static final String KEY_COMIC_ID = "comic_id";
    private static final String KEY_IMAGE_URL = "image_url";
    private static final String KEY_ALT_TEXT = "alt_text";
    private static final String KEY_IMAGE = "image";

    public DataBaseService(Context context)
    {
        super(context,DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_COMICS_TABLE = "CREATE TABLE " +
                TABLE_WEBCOMICS + "(" +
                KEY_ID + " INTEGER PRIMARY KEY," +
                KEY_NAME + " TEXT, " +
                KEY_WEBSITE + " TEXT, " +
                KEY_LAST_UPDATED + " TEXT, " +
                KEY_HAS_BEEN_VIEWED + "TEXT" + ")";

        String CREATE_CONTENT_TABLE = "CREATE TABLE " +
                TABLE_CONTENT + "(" +
                KEY_ID + " INTEGER PRIMARY KEY, " +
                KEY_COMIC_ID + " INTEGER," +
                KEY_ALT_TEXT + " TEXT, " +
                KEY_IMAGE_URL + " TEXT, " +
                KEY_IMAGE + " BLOB";

        System.out.println("SQLite Command " + CREATE_COMICS_TABLE);
        System.out.println("SQLite Command " + CREATE_CONTENT_TABLE);

        db.execSQL(CREATE_COMICS_TABLE);
        db.execSQL(CREATE_CONTENT_TABLE);

    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WEBCOMICS);

        // Create tables again
        onCreate(db);

    }

    // Adding new contact
    public void addComic(Comic comic) {

        if(!doesComicExist(comic))
        {
            Log.i("addComic", "Adding comic...");
            SQLiteDatabase db = this.getWritableDatabase();
            Log.i("addComic", "Database variable set");

            ContentValues comicValues = new ContentValues();
            comicValues.put(KEY_NAME, comic.getName());
            comicValues.put(KEY_WEBSITE, comic.getWebsite());
            comicValues.put(KEY_LAST_UPDATED, comic.getLastUpdated());
            values.put(KEY_
            if(comic.isUpdated())
            {
                values.put(KEY_UPDATED, "true");
            }
            else
            {
                values.put(KEY_UPDATED, "false");
            }
            values.put(KEY_SURL, comic.getUrl());
            values.put(KEY_SINCE, comic.getUpdatedSince());
            values.put(KEY_IMAGE, comic.getBitmapBytes());
            Log.i("addComic", "Values variable set");

            // Inserting Row
            db.insert(TABLE_WEBCOMICS, null, values);
            db.close(); // Closing database connection
        }
    }

    public void    deleteComic(Comic comic) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_WEBCOMICS, KEY_ID + " = ?",
                new String[] { String.valueOf(comic.getId()) });
        db.close();
    }

    public void deleteComic(long id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_WEBCOMICS, KEY_ID + " = ?",
                new String[] { String.valueOf(id) });
        db.close();
    }

    public boolean doesComicExist(Comic comic)
    {
        Log.d("doesComicExist", "Checking " + comic.getName());

        if (comic.getName() !=null)
        {
            if (getComic(comic.getName()) != null)
            {
                Log.d("doesComicExist", "getComic(name) returned non null inside doesComicExist");
                return true;
            } else {
                Log.d("doesComicExist", comic.getName() + " does not exist in Database");
                return false;
            }
        }
        else
        {
            Log.d("DataBaseHandler", "Comic name is null, will not add to database");
            return true;
        }

    }

    // Updating single comic
    public int     updateComic(Comic comic) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, comic.getName());
        values.put(KEY_URL, comic.getImageUrl());
        values.put(KEY_ALT, comic.getAltText());
        if(comic.isUpdated())
        {
            values.put(KEY_UPDATED, "true");
        }
        else
        {
            values.put(KEY_UPDATED, "false");
        }

        values.put(KEY_SURL, comic.getUrl());
        values.put(KEY_SINCE, comic.getUpdatedSince());
        values.put(KEY_IMAGE, comic.getBitmapBytes());

        // updating row
        int value = db.update(TABLE_WEBCOMICS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(comic.getId()) });
        db.close();
        return value;
    }

    // Getting single contact
    public Comic   getComic(String name) {
        if(getComicsCount() != 0)
        {
            SQLiteDatabase db = this.getReadableDatabase();

            Cursor cursor = db.query(TABLE_WEBCOMICS,

                    new String[] {
                            KEY_ID,
                            KEY_NAME,
                            KEY_WEBSITE,
                            KEY_LAST_UPDATED,
                            KEY_HAS_BEEN_VIEWED}, KEY_NAME + "=?",
                    new String[] { name }, null, null, null, null);

            if (cursor != null && cursor.getCount() != 0)
            {
                cursor.moveToFirst();

                Comic comic = new Comic(Integer.parseInt(cursor.getString(0)),
                        cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4),
                        cursor.getString(5), cursor.getBlob(6), cursor.getString(7));

                cursor.close();
                db.close();

                return comic;
            }
            else
            {
                //Log.i("getComic", "Comic does not exist in database");
                db.close();
                return null;
            }
        }
        return null;
    }
    /**
     public byte[]  getComicBitmapBytes(Integer position)
     {
     if(getComicsCount() != 0)
     {
     SQLiteDatabase db = this.getReadableDatabase();
     Cursor cursor = db.query(TABLE_WEBCOMICS, new String[] {KEY_IMAGE}, KEY_ID + "=?",
     new String[] {position.toString()}, null, null, null, null);
     if(cursor != null && cursor.getCount() != 0)
     {
     System.out.println(cursor.getColumnCount());
     cursor.moveToFirst();
     byte[] result = cursor.getBlob(0);
     cursor.close();
     db.close();
     return  result;
     }
     }
     return null;
     }**/

    /**public String  getComicName(Integer position)
     {
     if(getComicsCount() != 0)
     {
     SQLiteDatabase db = this.getReadableDatabase();
     Cursor cursor = db.query(TABLE_WEBCOMICS, new String[] {KEY_NAME}, KEY_ID + "=?",
     new String[] {position.toString()}, null,null,null,null);

     if(cursor != null && cursor.getCount() != 0)
     {
     //System.out.println(cursor.getColumnCount());
     cursor.moveToFirst();
     String result = cursor.getString(0);
     cursor.close();
     db.close();
     return  result;
     }
     cursor.close();
     db.close();
     }

     return null;
     }**/

    /**public boolean getComicUpdated(Integer position)
     {
     if(getComicsCount() != 0)
     {
     SQLiteDatabase db = this.getReadableDatabase();
     Cursor cursor = db.query(TABLE_WEBCOMICS, new String[] {KEY_UPDATED}, KEY_ID + "=?",
     new String[] {position.toString()}, null,null,null,null);

     if(cursor != null && cursor.getCount() != 0)
     {
     boolean result;
     cursor.moveToFirst();
     Log.d("DataBaseHandler", "cursor = " + cursor.getString(0));
     if(cursor.getString(0).equals("true"))
     {
     result = true;
     Log.d("DataBaseHandler", "Returned true");
     }
     else
     {
     result = false;
     Log.d("DataBaseHandler", "Returned false");
     }
     cursor.close();
     db.close();

     return  result;
     }
     }
     Log.d("DataBaseHandler", "Returned Default. SHOULD NOT BE SEEN");
     return false;
     }**/
    /**
     public Comic   getComic(Integer position)
     {
     //position++;
     if(getComicsCount() != 0)
     {
     SQLiteDatabase db = this.getReadableDatabase();
     Cursor cursor = db.query(TABLE_WEBCOMICS, new String[] { KEY_ID,
     KEY_NAME, KEY_URL, KEY_UPDATED, KEY_SURL, KEY_SINCE, KEY_IMAGE, KEY_ALT }, KEY_ID + "=?",
     new String[] {position.toString()}, null, null, null, null);
     if (cursor != null && cursor.getCount() != 0)
     {
     cursor.moveToFirst();


     Comic comic = new Comic(Integer.parseInt(cursor.getString(0)),
     cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4),
     cursor.getString(5), cursor.getBlob(6), cursor.getString(7));
     cursor.close();
     db.close();
     return comic;

     }
     else
     {
     Log.i("getComic", "No Comic at Position " + position);
     db.close();
     return null;
     }
     }
     return null;
     }**/

    public ArrayList<Comic>  getAllComics() {
        ArrayList<Comic> ComicList = new ArrayList<Comic>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_WEBCOMICS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Comic Comic = new Comic();
                Comic.setId(Integer.parseInt(cursor.getString(0)));
                Comic.setName(cursor.getString(1));
                Comic.setImageUrl(cursor.getString(2));
                Comic.setUpdated(cursor.getString(3));
                Comic.setUrl(cursor.getString(4));
                Comic.setUpdatedSince(cursor.getString(5));
                Comic.setComicBitmap(cursor.getBlob(6));
                Comic.setAltText(cursor.getString(7));
                // Adding Comic to list
                ComicList.add(Comic);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        // return Comic list
        return ComicList;
    }

    public ArrayList<String> getAllComicNames()
    {
        ArrayList<String> ComicNames = new ArrayList<String>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_WEBCOMICS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst() && cursor != null) {
            do {
                if(!cursor.isNull(1)) ComicNames.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        // return Comic list
        return ComicNames;
    }

    // Getting contacts Count
    public int     getComicsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_WEBCOMICS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int temp = cursor.getCount();
        cursor.close();
        db.close();

        // return count
        return temp;
    }

}
