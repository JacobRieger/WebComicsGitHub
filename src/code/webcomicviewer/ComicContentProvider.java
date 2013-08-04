package code.webcomicviewer;

import dataCode.Comic2;
import dataCode.DatabaseHandler2;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

public class ComicContentProvider extends ContentProvider {
	
	// All URIs share these parts
    public static final String AUTHORITY = "dataCode";
    public static final String SCHEME = "content://";

    // URIs
    // Used for all persons
    public static final String COMICS = SCHEME + AUTHORITY + "/comic";
    public static final Uri URI_COMICS = Uri.parse(COMICS);
    // Used for a single person, just add the id to the end
    public static final String COMIC_BASE = COMICS + "/";
    
    
	public ComicContentProvider() {
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// Implement this to handle requests to delete one or more rows.
		throw new UnsupportedOperationException("Not yet implemented");
	}

	@Override
	public String getType(Uri uri) {
		// TODO: Implement this to handle requests for the MIME type of the data
		// at the given URI.
		throw new UnsupportedOperationException("Not yet implemented");
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// TODO: Implement this to handle requests to insert a new row.
		throw new UnsupportedOperationException("Not yet implemented");
	}

	@Override
	public boolean onCreate() {
		 return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		
		Cursor result = null;
        if (URI_COMICS.equals(uri)) {
            result = DatabaseHandler2
                    .getInstance(getContext())
                    .getReadableDatabase()
                    .query(Comic2.TABLE_NAME, Comic2.FIELDS, null, null, null,
                            null, null, null);
            result.setNotificationUri(getContext().getContentResolver(), URI_COMICS);
        } else if (uri.toString().startsWith(COMIC_BASE)) {
            final long id = Long.parseLong(uri.getLastPathSegment());
            result = DatabaseHandler2
                    .getInstance(getContext())
                    .getReadableDatabase()
                    .query(Comic2.TABLE_NAME, Comic2.FIELDS,
                            Comic2.COL_ID + " IS ?",
                            new String[] { String.valueOf(id) }, null, null,
                            null, null);
             result.setNotificationUri(getContext().getContentResolver(), URI_COMICS);
        } else {
            throw new UnsupportedOperationException("Not yet implemented");
        }

        return result;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO: Implement this to handle requests to update one or more rows.
		throw new UnsupportedOperationException("Not yet implemented");
	}
	
	
}
