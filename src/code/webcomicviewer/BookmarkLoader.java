package code.webcomicviewer;


import android.app.Activity;
import android.database.Cursor;
import android.provider.Browser;
import android.util.Log;

import dataCode.Bookmark;
import dataCode.BookmarkList;

public class BookmarkLoader {

	private BookmarkList Bookmarks;
	
	public BookmarkLoader(Activity activity)
	{
		//This loads our bookmarks
    	
		Bookmarks = new BookmarkList();
		
		//How to load bookmarks from the phone
		Cursor cursor = activity.getContentResolver().query(Browser.BOOKMARKS_URI,
				null, null, null, null);
		cursor.moveToFirst();
		//We want the title and the urls of the bookmarks
		int titleIdx = cursor.getColumnIndex(Browser.BookmarkColumns.TITLE);
		int urlIdx = cursor.getColumnIndex(Browser.BookmarkColumns.URL);
		int bookmark = cursor
				.getColumnIndex(Browser.BookmarkColumns.BOOKMARK);
		
		while (cursor.isAfterLast() == false) {
			//Loads all our bookmarks that we can
			if (cursor.getInt(bookmark) > 0) {
				Log.d("Adding Bookmark", cursor.getString(titleIdx));
				Bookmarks.add(new Bookmark(cursor.getString(titleIdx),
						cursor.getString(urlIdx)));
			}
			cursor.moveToNext();
		}
	}
	
	public BookmarkList getBookmarks()
	{
		return Bookmarks;
	}

}
