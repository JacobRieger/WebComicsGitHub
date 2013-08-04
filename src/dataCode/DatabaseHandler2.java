package dataCode;

import code.webcomicviewer.ComicContentProvider;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.util.Log;

public class DatabaseHandler2 extends SQLiteOpenHelper {
	
	private static DatabaseHandler2 singleton;

    public static DatabaseHandler2 getInstance(final Context context) {
        if (singleton == null) {
            singleton = new DatabaseHandler2(context);
        }
        return singleton;
    }

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "providerExample";

    private final Context context;

    public DatabaseHandler2(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        // Good idea to use process context here
        this.context = context.getApplicationContext();
    }

	@Override
	public void onCreate(SQLiteDatabase db) {
		
		db.execSQL(Comic2.CREATE_TABLE);
		System.out.println(Comic2.CREATE_TABLE);
		
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		db.execSQL("DROP TABLE IF EXISTS " + Comic2.CREATE_TABLE);
		 
        // Create tables again
        onCreate(db);
		
	}
	
	public Integer getCount()
	{
		final SQLiteDatabase db = this.getReadableDatabase();
        final Cursor cursor = db.rawQuery("SELECT COUNT(*)", null);
        Log.d("Cursor", "Count of database is " + cursor.getCount());
        return cursor.getCount();
        
		
	}
	
	
	public synchronized Comic2 getComic(final long id) {
		
		final SQLiteDatabase db = this.getReadableDatabase();
        final Cursor cursor = db.query(Comic2.TABLE_NAME,
        		Comic2.FIELDS, Comic2.COL_ID + " IS ?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        
        if (cursor == null || cursor.isAfterLast()) 
        {
            return null;
        }

        Comic2 item = null;
        if (cursor.moveToFirst()) {
            item = new Comic2(cursor);
        }
        cursor.close();
        if(item.getBitmapBytes() == null) System.out.println("Bitmap is null in getComic");

        return item;
    }

    public synchronized boolean putComic(final Comic2 comic) {
        
        final SQLiteDatabase db = this.getWritableDatabase();
        boolean success = false;
        int result = 0;
        if(comic.getBitmapBytes() == null) System.out.println("Bitmap null in putComic");
       // comic.getContent().get("COL")

        if (comic.getId() > -1) {
            result += db.update(Comic2.TABLE_NAME, comic.getContent(),
                    Comic2.COL_ID + " IS ?",
                    new String[] { String.valueOf(comic.getId()) });
        }

        if (result > 0) 
        {
            success = true;
        } 
        
        else
        {
        	System.out.println("Update failed or wasn't possible, insert instead");
            // Update failed or wasn't possible, insert instead
            final long id = db.insert(Comic2.TABLE_NAME, null,
                    comic.getContent());
            

            if (id > -1)
            {
                comic.setId(id);
                success = true;
            }
        }

        if (success) {
            notifyProviderOnComicChange();
        }
        
        return success;
        
    }

    public synchronized int removeComic(final Comic2 comic) {
    	
    	final SQLiteDatabase db = this.getWritableDatabase();
        final int result = db.delete(Comic2.TABLE_NAME,
        		Comic2.COL_ID + " IS ?",
                new String[] { Long.toString(comic.getId()) });

        if (result > 0) {
            notifyProviderOnComicChange();
        }
        
        return result;
    }
    
    private void notifyProviderOnComicChange() {
        context.getContentResolver().notifyChange(
                ComicContentProvider.URI_COMICS, null, false);
    }
    
    public void sanityCheck()
    {
    	Log.d("Sanity", "started sanity check");
    	Comic2 comic = new Comic2();
    	comic.setName("DrMcNinja");
    	comic.setAltText("booya");
    	comic.setComicBitmap(Bitmap.createBitmap(1, 1, Config.ALPHA_8));
    	comic.setImageUrl("unset");
    	comic.setUrl("http://www.drmcninja.com");
    	
    	putComic(comic);
    	
    	if(getComic(1).getComicBitmap() == null)
    	{
    		Log.d("SANITY", "FAILED");
    	}
    	else
    	{
    		if(getComic(1).getComicBitmap().equals(Bitmap.createBitmap(1, 1, Config.ALPHA_8)))
    		{
    			Log.d("SANITY", "SANITY ACCOMPLISHED");
    		}
    		Log.d("SANITY", "not quite");
    	}
    	
    	
    	removeComic(comic);
    }

}
