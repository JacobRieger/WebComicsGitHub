package asyncTasks;

import java.lang.ref.WeakReference;


import comicCode.Comic;
import dataCode.DataBaseHandler;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;


public class ComicLoader extends AsyncTask<String, Void, Bitmap> {

    private final WeakReference<ImageView> imageViewReference;
    private DataBaseHandler db;
    private boolean scaled = false;

    public ComicLoader(ImageView imageView, Context context) {
        // Use a WeakReference to ensure the ImageView can be garbage collected
        imageViewReference = new WeakReference<ImageView>(imageView);
       
        
        db = new DataBaseHandler(context);
    }
    
    public ComicLoader(ImageView imageView, Context context, boolean scale) {
        // Use a WeakReference to ensure the ImageView can be garbage collected
        imageViewReference = new WeakReference<ImageView>(imageView);
        
        scaled = scale;
        
        db = new DataBaseHandler(context);
    }
    

    // Decode image in background.
    @Override
    protected Bitmap doInBackground(String... params) {
       
    	if(scaled)
    	{
    		Comic current = db.getComic(params[0]);
    		byte[] bytes = current.getBitmapBytes();
    		
    		return Bitmap.createBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length),
            		0, 0, 75, 75);
    	}
        return db.getComic(params[0]).getComicBitmap();
    }

    // Once complete, see if ImageView is still around and set bitmap.
    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (imageViewReference != null && bitmap != null) {
            final ImageView imageView = imageViewReference.get();
            if (imageView != null) {
                imageView.setImageBitmap(bitmap);
            }
        }
    }
}
