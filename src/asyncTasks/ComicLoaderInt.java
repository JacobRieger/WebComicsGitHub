package asyncTasks;

import java.lang.ref.WeakReference;

import dataCode.DataBaseHandler;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.TextView;

class ComicLoaderInt extends AsyncTask<Integer, Void, Bitmap> {
	private String title;
    private final WeakReference<ImageView> imageViewReference;
    private final WeakReference<TextView>  textViewReference;
    private DataBaseHandler db;

    public ComicLoaderInt(ImageView imageView, TextView textView, Context context)
    {
    	imageViewReference = new WeakReference<ImageView>(imageView);
    	textViewReference =  new WeakReference<TextView>(textView);
    	
    	db = new DataBaseHandler(context);
    }

    // Decode image in background.
    @Override
    protected Bitmap doInBackground(Integer... params) {
       
    	title = db.getComicName(params[0]);
    	
    	byte[] bitmapbytes = db.getComicBitmapBytes(params[0]);
		
		Bitmap result = BitmapFactory.decodeByteArray(
				bitmapbytes, 0, bitmapbytes.length, null);
    	
    	result = Bitmap.createBitmap(result, result.getWidth()/2,
    			result.getHeight()/2, 75, 75);

    	
        return result;
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
        
        if (textViewReference != null && bitmap != null) {
        	final TextView textView = textViewReference.get();
        	if (textView != null) {
                textView.setText(title);
            }
        }
    }
    
    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
    // Raw height and width of image
    final int height = options.outHeight;
    final int width = options.outWidth;
    int inSampleSize = 1;

    if (height > reqHeight || width > reqWidth) {

        // Calculate ratios of height and width to requested height and width
        final int heightRatio = Math.round((float) height / (float) reqHeight);
        final int widthRatio = Math.round((float) width / (float) reqWidth);

        // Choose the smallest ratio as inSampleSize value, this will guarantee
        // a final image with both dimensions larger than or equal to the
        // requested height and width.
        inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
    }

    return inSampleSize;
	}
}