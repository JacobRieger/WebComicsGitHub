package activityCode;

import it.sephiroth.android.library.imagezoom.ImageViewTouch;

import code.webcomicviewer.R;

import comicCode.Comic;
import dataCode.DataBaseHandler;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class View_Comic extends Activity {
	String ImageUrl;
	String Name;
	String Url;
	Comic  viewed;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view__comic);
        
        ImageViewTouch imageView = (ImageViewTouch) findViewById(R.id.ImageView01);
        
        DataBaseHandler db = new DataBaseHandler(this);
        
        Bundle extras = getIntent().getExtras();
        Name = extras.getString("Name");
        viewed = db.getComic(Name);
        //
        ImageUrl = viewed.getImageUrl();
        Url      = viewed.getUrl();
        
        
        
        
        imageView.setImageBitmap(viewed.getComicBitmap());
        
        //We redownload the image, doesn't take very long on SGS3
        //imageDownloader download = new imageDownloader(imageView, ImageUrl);
        //download.execute();
        
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_view__comic, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
    	//This reloads the image
    	switch(item.getItemId())
    	{
    	/**
    	case R.id.Reload:
	    	ImageViewTouch imageView = (ImageViewTouch) findViewById(R.id.ImageView01);
	    	imageView.setImageResource(R.drawable.loading);
	    	imageDownloader download = new imageDownloader(imageView, ImageUrl);
	    	download.execute();
	    	System.out.println(imageView.getWidth() + " " + imageView.getHeight());
	    	break;
	    	
    	case R.id.ViewUpdate:
    		ComicUpdater update = new ComicUpdater(this);
    		update.execute();
    		break;
    		**/
    		
    	case R.id.ViewBrowser:
    		Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Url));
    		startActivity(browserIntent);
    		break;
    	
    	case R.id.ViewAltText:
    		Toast.makeText(this, viewed.getAltText(), Toast.LENGTH_LONG).show();
    		
    	}
    	return true;
    }
    /**
    private class imageDownloader extends AsyncTask<Integer, Void, Bitmap>
    {
    	//Our Reference to the imageView
    	private final WeakReference<ImageView> imageViewReference;
    	//Our comic
        private String ImageUrl;
    	
    	public imageDownloader(ImageViewTouch imageView, String url)
    	{
    		//Basic constructor that sets our variables
    		imageViewReference = new WeakReference<ImageView>(imageView);
    		ImageUrl = url;
    		
    	}

		@Override
		protected Bitmap doInBackground(Integer... params) {
		
			Comic temp = new Comic("temp","notneeded", ImageUrl);
			temp.retrieveImageBitmap();
			return temp.getComicBitmap();
		}
		
		@Override
	    protected void onPostExecute(Bitmap bitmap) {
			//This sets the imageview (if it still exists) to the bitmap returned from
			//doinBackground
	        if (imageViewReference != null && bitmap != null) {
	            final ImageView imageView = imageViewReference.get();
	            if (imageView != null) {
	                imageView.setImageBitmap(bitmap);
	                
	            }
	        }   
	    }
    }
    
    private class ComicUpdater extends AsyncTask<Void, Void, Void>
    {
    	private ImageViewTouch ourimage;
    	private DataBaseHandler db;
    	private Comic current;
    	private Bitmap comicBitmap;
    	
    	public ComicUpdater(Context context)
    	{
    		db = new DataBaseHandler(context);
    		current = db.getComic(Name);
    	}
    	
    	@Override
    	protected void onPreExecute()
    	{
    		ourimage = (ImageViewTouch) findViewById(R.id.ImageView01);
    		ourimage.setImageResource(R.drawable.loading);
    	}

		@Override
		protected Void doInBackground(Void... params) {
			
			Log.d("ComicIUrl", current.getImageUrl());
			Log.d("ComicUrl", current.getUrl());
			//current.retrieveImageBitmap();
			comicBitmap = current.getComicBitmap();
			if(current.modified())
			{
				current.Update();
				db.updateComic(current);
			}
			else
			{
				current.setComicBitmap(comicBitmap);
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result)
		{
			ourimage.setImageBitmap(current.getComicBitmap());
		}
    	
    }
**/
}
