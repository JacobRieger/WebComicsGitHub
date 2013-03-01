package code.webcomicviewer;

import it.sephiroth.android.library.imagezoom.ImageViewTouch;

import java.lang.ref.WeakReference;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.graphics.Bitmap;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

public class View_Comic extends Activity {
	String ImageUrl;
	String Name;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view__comic);
        
        ImageViewTouch imageView = (ImageViewTouch) findViewById(R.id.ImageView01);
        TextView textview = (TextView) findViewById(R.id.TitleName);
        
        Bundle extras = getIntent().getExtras();
        ImageUrl = extras.getString("ImageUrl");
        Name = extras.getString("Name");
        
        textview.setText(Name);
        //We redownload the image, doesn't take very long on SGS3
        imageDownloader download = new imageDownloader(imageView, ImageUrl);
        download.execute();
        
        
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
    	
    	ImageViewTouch imageView = (ImageViewTouch) findViewById(R.id.ImageView01);
    	imageView.setImageResource(R.drawable.loading);
    	imageDownloader download = new imageDownloader(imageView, ImageUrl);
    	download.execute();
    	return true;
    }
    
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
}
