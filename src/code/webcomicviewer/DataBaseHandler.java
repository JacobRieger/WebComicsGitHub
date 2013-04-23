package code.webcomicviewer;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.util.Log;

public class DataBaseHandler extends SQLiteOpenHelper {
	
	// All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;
 
    // Database Name
    private static final String DATABASE_NAME = "webcomicsManager";
 
    // Comics table name
    private static final String TABLE_WEBCOMICS = "Webcomics";
 
    // Comics Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_URL = "url";
    private static final String KEY_UPDATED = "isUpdated";
    private static final String KEY_SURL = "surl";
    private static final String KEY_SINCE = "updatedSince";
    private static final String KEY_IMAGE = "Image";
	
	public DataBaseHandler(Context context)
	{
		super(context,DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_WEBCOMICS_TABLE = "CREATE TABLE " + TABLE_WEBCOMICS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_URL + " TEXT," + KEY_UPDATED + " TEXT, " + KEY_SURL + " TEXT," 
                + KEY_SINCE + " TEXT," + KEY_IMAGE + " BLOB" + ")";
		System.out.println("SQLite Command " + CREATE_WEBCOMICS_TABLE);
        db.execSQL(CREATE_WEBCOMICS_TABLE);
		

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
		 
		    ContentValues values = new ContentValues();
		    values.put(KEY_NAME, comic.getName()); 
		    //System.out.println(comic.getName());
		    values.put(KEY_URL, comic.getImageUrl());
		    //System.out.println(comic.getImageUrl());
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
    
    public void deleteComic(Comic comic) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_WEBCOMICS, KEY_ID + " = ?",
                new String[] { String.valueOf(comic.getId()) });
        db.close();
    }
    
    public boolean doesComicExist(Comic comic)
    {
    	Log.d("doesComicExist", "Checking " + comic.getName());
    	if(this.getComic(comic.getName()) != null)
    	{
    		return true;
    	}
    	else
    	{
    		Log.d("doesComicExist", comic.getName() + " does not exist in Database");
    		return false;
    	}
    	
    	
    }
    
 // Updating single comic
    public int updateComic(Comic comic) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, comic.getName());
        values.put(KEY_URL, comic.getImageUrl());
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
    public Comic getComic(String name) {
    	if(getComicsCount() != 0)
    	{
	        SQLiteDatabase db = this.getReadableDatabase();
	     
	        Cursor cursor = db.query(TABLE_WEBCOMICS, new String[] { KEY_ID,
	                KEY_NAME, KEY_URL, KEY_UPDATED, KEY_SURL, KEY_SINCE, KEY_IMAGE }, KEY_NAME + "=?",
	                new String[] { name }, null, null, null, null);
	        if (cursor != null && cursor.getCount() != 0)
	        {
	            cursor.moveToFirst();
	            
	            
		        Comic comic = new Comic(Integer.parseInt(cursor.getString(0)),
		        	cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4),
		        	cursor.getString(5), cursor.getBlob(6));
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
    
    public byte[] getComicBitmapBytes(Integer position)
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
    }
    
    public String getComicName(Integer position)
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
    	}
    	return null;
    	
    	
    	
    }
    
    public Comic getComic(Integer position)
    {
    	//position++;
    	if(getComicsCount() != 0)
    	{
	    	SQLiteDatabase db = this.getReadableDatabase();
	    	Cursor cursor = db.query(TABLE_WEBCOMICS, new String[] { KEY_ID,
	    			KEY_NAME, KEY_URL, KEY_UPDATED, KEY_SURL, KEY_SINCE, KEY_IMAGE }, KEY_ID + "=?",
	    			new String[] {position.toString()}, null, null, null, null);
	    	if (cursor != null && cursor.getCount() != 0)
	        {
	            cursor.moveToFirst();
	            
	            
		        Comic comic = new Comic(Integer.parseInt(cursor.getString(0)),
		        	cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4),
		        	cursor.getString(5), cursor.getBlob(6));
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
    
    public ArrayList<Comic> getAllComics() {
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
    public int getComicsCount() {
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
