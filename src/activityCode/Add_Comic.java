package activityCode;


import code.webcomicviewer.R;
import comicCode.Comic;
import dataCode.Bookmark;
import dataCode.BookmarkList;
import dataCode.DataBaseHandler;

import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Browser;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class Add_Comic extends Activity implements OnClickListener {

	String ImageUrl;
	public BookmarkList Bookmarks;
	private Comic AddedComic;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_add__comic);  
        
        //Our two buttons to check and add comics
        Button checkComic = (Button) findViewById(R.id.checkComic);
        Button addComic = (Button) findViewById(R.id.addComic);
        
        //Set our onclicklisteners
        //addComic.setOnClickListener(this);
        checkComic.setOnClickListener(this);
        
        //Add comic should be invisible until the comics been verified
        //Does not currently work
        addComic.setVisibility(View.INVISIBLE);
    
        
        if (Bookmarks == null) {
        	//This loads our bookmarks
        	
			Bookmarks = new BookmarkList();
			
			//How to load bookmarks from the phone
			Cursor cursor = getContentResolver().query(Browser.BOOKMARKS_URI,
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_add__comic, menu);
  
        //This generates our bookmark submenu items
        MenuItem BookMark = (MenuItem) menu.findItem(R.id.AddAdd);
        SubMenu BookMarkSub = BookMark.getSubMenu();
        
        for(int x = 0; x < Bookmarks.size(); x++)
        {
        	BookMarkSub.add(32, 0, 0, Bookmarks.get(x).getName());
        	Log.d("added", Bookmarks.get(x).getName());
        }  
        
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
    	//If our item is a bookmark, we load up it's data into the entry fields
    	if(item.getGroupId() == 32)
    	{
    		//These are the text entry fields
    		EditText cName = (EditText) findViewById(R.id.ComicName);
        	EditText cUrl  = (EditText) findViewById(R.id.Comic_Url);
        	
        	//Find our bookmark 
        	Bookmark current = Bookmarks.find(item.getTitle().toString());
        	
        	//Load the bookmark data into the fields
        	cName.setText(current.getName());
        	cUrl.setText (current.getUrl());
    		return true;
    	}
    	
    	return false;
    }

	@Override
	public void onClick(View view) {
		
		
		EditText cName = (EditText) findViewById(R.id.ComicName);
		EditText cUrl  = (EditText) findViewById(R.id.Comic_Url);
		
		String Name    = cName.getText().toString();
		String Url     = cUrl.getText().toString();
		
		switch(view.getId()){
		
		case R.id.checkComic:
			//Extracts the comic from the website
			Comic Check = new Comic(Name, Url, "unset");
			Log.d("CheckComic", Name + " " + Url);
			
			comicDownloader CD = new comicDownloader(Check, this);
			CD.execute();
			
			Button addComic = (Button) findViewById(R.id.addComic);
			
			addComic.setOnClickListener(this);
			addComic.setVisibility(View.VISIBLE);
			break;
			
		case R.id.addComic:
			DataBaseHandler db = new DataBaseHandler(this);
			//Comic NewComic = new Comic(Name, Url, ImageUrl);
			AddedComic.setUpdatedSince("0");
			AddedComic.setUpdated(true);
			db.addComic(AddedComic);
			Intent intent = new Intent(this, ComicListActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
			break;
		}
		
	}
	
	public boolean validateURL(String mImageUrl)
	{
		//TODO
		//Had problems with regex, some good urls were being blocked
		/*
		String regex = "^(https?|ftp|file)://.+$";
		Log.d("validateUrl", mImageUrl);
		if(true)
		{
			Log.d("validateURL", "URL matches regex");
			
			return true;
		}
		else return false;
		*/
		return true;
	}
	
	private class comicDownloader extends AsyncTask<Void, Void, Boolean>
	{
		ProgressDialog pd;
		Comic ourComic;
		Context context;
		public comicDownloader(Comic current, Context ourcontext)
		{
			ourComic = current;
			context = ourcontext;
		}
		
		@Override
		protected void onPreExecute()
		{
			pd = new ProgressDialog(context, ProgressDialog.STYLE_SPINNER);
			pd.show();
			pd.setMessage("Extracting Comic");
			pd.setCancelable(false);
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			
			if(validateURL(ourComic.getUrl()))
			{
				ourComic.findImageUrl();
				ourComic.retrieveImageBitmap();
				return true;
			}
			else
			{
				pd.dismiss();
				return false;
			}
			
		}
		
		@Override
		protected void onPostExecute(Boolean result)
		{
			if(result)
			{
				pd.dismiss();
				ImageView IV = (ImageView) findViewById(R.id.imageView1);
				IV.setImageBitmap(ourComic.getComicBitmap());
				ImageUrl = ourComic.getImageUrl();
				AddedComic = ourComic;
			}
			else
			{
				Dialog dialog = new Dialog(context);
				dialog.setTitle("Invalid URL : Please Enter a Valid URL");
				dialog.show();
			}
		}
		
	}
}
