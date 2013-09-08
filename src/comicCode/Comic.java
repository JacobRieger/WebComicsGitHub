package comicCode;


import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import dataCode.HtmlImageTag;

public class Comic {

	private int      id;            //ID for the database
	private String   Name;          //Name of the comic
	private String   url;           //Url of the comic
	private String   updatedSince;  //Server variable that may or may not be present
	private HtmlImageTag comicImg;      //Container for ImageUrl and AltText
	private Bitmap   comicBitmap;   //Bitmap of the image
	private boolean  isUpdated;     //Whether or not the comic has been confirmed still on the site
	//--------------------------------------------------------------------------------//

	public Comic(String name, String URL, String ImageUrl) {
		Name = name;
		//imageUrl = ImageUrl
		comicImg = new HtmlImageTag(ImageUrl, "None");
		url = URL;
		comicBitmap = Bitmap.createBitmap(1, 1, Config.ALPHA_8);
		isUpdated = true;
		updatedSince = "0";
	}
	
	public Comic(int ID, String name, String URL, String ImageUrl) {
		id = ID;
		Name = name;
		//imageUrl = ImageUrl;
		comicImg = new HtmlImageTag(ImageUrl, "None");
		url = URL;
		comicBitmap = Bitmap.createBitmap(1, 1, Config.ALPHA_8);
		isUpdated = true;
		updatedSince = "0";
	}
	
	public Comic(int ID, String name, String ImageUrl, String updated, String URL) {
		if(updated.equals("true"))
		{
			isUpdated = true;
		}
		else
		{
			isUpdated = false;
		}
		id = ID;
		Name = name;
		comicImg = new HtmlImageTag(ImageUrl, "None");
		url = URL;
		comicBitmap = Bitmap.createBitmap(1, 1, Config.ALPHA_8);
		updatedSince = "0";
	}
	
	public Comic(int ID, String name, String ImageUrl, String updated, String URL,
			String UpdatedSince, byte[] bitmapbytes, String alttext) {
		if(updated.equals("true"))
		{
			isUpdated = true;
		}
		else
		{
			isUpdated = false;
		}
		id = ID;
		Name = name;
		comicImg = new HtmlImageTag(ImageUrl, alttext);
		url = URL;
		
		//comicBitmap = Bitmap.createBitmap(1, 1, Config.ALPHA_8);
		Log.d("Comic Constructor", "About to create Bitmap TESTING");
		
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeByteArray(
				bitmapbytes, 0, bitmapbytes.length, options);
		options.inSampleSize = calculateInSampleSize(options, 720, 1230);
		options.inJustDecodeBounds = false;
		
		comicBitmap = BitmapFactory.decodeByteArray(
				bitmapbytes, 0, bitmapbytes.length, options);
		
		updatedSince = String.valueOf(UpdatedSince);
	}
	
	public Comic()
	{
		comicImg     = new HtmlImageTag("Unset", "Unset");
		comicBitmap  = Bitmap.createBitmap(10, 10, Bitmap.Config.ALPHA_8);
		updatedSince = "0";
		if(comicBitmap == null) System.out.println("Wtf your comic bitmap is Null in constructor");
	}
	//--------------------------------------------------------------------------------//
	public int      getId()
	{
		return id;
	}
	public void     setId(int newId)
	{
		id = newId;
	}
	
	public String   getName() {
		return Name;
	}
	public void     setName(String name) {
		Name = name;
	}

	public String   getImageUrl() {
		return comicImg.getImageUrl();
	}
	public void     setImageUrl(String imageUrl) {
		this.comicImg.setImageUrl(imageUrl);
	}

	public Bitmap   getComicBitmap() {
		
		return comicBitmap;
	}
	public void     setComicBitmap(Bitmap comicBitmap) {

		this.comicBitmap = comicBitmap;
	}
	public void     setComicBitmap(byte[] bitmapbytes)
	{
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeByteArray(
				bitmapbytes, 0, bitmapbytes.length, options);
		options.inSampleSize = calculateInSampleSize(options, 720, 900);
		System.out.println(options.inSampleSize);
		options.inJustDecodeBounds = false;
		
		comicBitmap = BitmapFactory.decodeByteArray(
				bitmapbytes, 0, bitmapbytes.length, options);
	}
	
	public boolean  isUpdated() {
		return isUpdated;
	}
	public void     setUpdated(boolean set) {
		isUpdated = set;
	}
	public void     setUpdated(String set)
	{
		if(set.equals("true")) isUpdated = true;
		else isUpdated = false;
	}
	
	public String   getUrl()
	{
		return url;
	}
	public void     setUrl(String URL)
	{
		url = URL;
	}
	
	public byte[]   getBitmapBytes() {
		// This is for inputing bitmaps into a bundle to pass between functions
		// Bitmap bitmap = BitmapFactory.decodeFile("/path/images/image.jpg");
		ByteArrayOutputStream blob = new ByteArrayOutputStream();
		comicBitmap.compress(CompressFormat.PNG, 0 /* ignored for PNG */, blob);
		byte[] bitmapdata = blob.toByteArray();
		
		if(bitmapdata == null)
		{
			Log.d("getBitmapBytes", "Returned default bitmap bytes");
			Bitmap Default = Bitmap.createBitmap(10, 10, Bitmap.Config.ALPHA_8);
			Default.compress(CompressFormat.PNG,  0, blob);
		}
		return bitmapdata;
	}

	public String   getUpdatedSince()
	{
		return String.valueOf(updatedSince);
	}
	public String   getAltText()
	{
		return comicImg.getAltText();
	}
	public void     setUpdatedSince(String since)
	{
		updatedSince = since;
	}

	public void     setAltText(String alt)
	{
		comicImg.setAltText(alt);
	}
	//--------------------------------------------------------------------------------//
	public void retrieveImageBitmap() {
		
		boolean DEBUG = true;
		
		try {
			// Opens a URL connection
			URL url = new URL(comicImg.getImageUrl());
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			
			connection.setDoInput(true);
			connection.connect();
			// Get the inputstream
			InputStream input = connection.getInputStream();
			// We know it's an image url so we decode it into a bitmap
			Bitmap myBitmap = BitmapFactory.decodeStream(input);
			//This is for when the bitmap is too big (aka SMBC)
			//TODO Change 4096
			//Tailored only for GSIII, 4096 is equal to GL max size, should find way to 
			//get this
			if (myBitmap.getHeight() > 4096 || myBitmap.getWidth() > 4096) {
				System.out.println("Scaling image");
				
				if(DEBUG)Log.d("retrieveImageBitmap", "Scaling image");
				
				int origWidth = myBitmap.getWidth();
				int origHeight = myBitmap.getHeight();
				int newHeight = 4096;
				int newWidth = 4096;
				float scaleWidth;
				float scaleHeight;
				if (origWidth >= origHeight) {
					scaleWidth = (float) newWidth / origWidth;
					scaleHeight = scaleWidth;
				} else {
					scaleHeight = (float) newHeight / origHeight;
					scaleWidth = scaleHeight;
				}
				myBitmap = Bitmap.createScaledBitmap(myBitmap,
						(int) (origWidth * scaleWidth), (int) (origHeight * scaleHeight), false);
			}
			
			if(DEBUG)Log.e("Comic", "Returned in retrieveImageBitmap");
			comicBitmap = myBitmap;
		} catch (IOException e) {
			
			if(e.getMessage() != null)
			{
				Log.e("Exception", e.getMessage());
			}
			Bitmap bitmap = Bitmap.createBitmap(10, 10, Bitmap.Config.ALPHA_8);
			comicBitmap = bitmap;
		}
	}

	public void     Update() {
		
		if(true)Log.d("Comic", "Updated called in " + this.getName());

        ComicSiteParser comicSiteParser = new ComicSiteParser(this);
        List<HtmlImageTag> htmlImageTags = comicSiteParser.GetComicHtmlImageTags();



		//setNewImageurl(GetComicHtmlImageTags());
	}
	
	public boolean  modified()
	{
		boolean DEBUG = false;
		
		long oldDate;
		long newDate;
		if(updatedSince != null)
		{
			oldDate = Long.valueOf(updatedSince);
		}
		else
		{
			oldDate = 0;
		}
		try
		{
			URLConnection connection = new URL(url).openConnection();
			newDate = connection.getLastModified();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			newDate = 0;
		}
		
		if(newDate > oldDate || newDate == 0)
		{
			oldDate = newDate;
			updatedSince = String.valueOf(oldDate);
			return true;
		}
		
		if(DEBUG) Log.d("modified", "Old date was " + oldDate
						+ " New date is " + newDate );
		return false;
	}

	public double   Similarity(String newString) {
		double count = 0;

		for (int i = 0; i < comicImg.getImageUrl().length(); i++) {
			
			if (comicImg.getImageUrl().charAt(i) == newString.charAt(i)) {
				count++;
			} 
			else {
				return count;
			}
		}

		return 0;
	}

	public void     setNewImageurl(List<HtmlImageTag> htmlImageTags) {

		// Checks the list given against the saved Imageurl and sets the one
		// most similar to that former.
		// This is our similarity variable
		// System.out.println("SetNewImageurl called");
		
		List<String> imageURLs = new ArrayList<String>();
		for(int i = 0; i < htmlImageTags.size(); i++)
		{
			imageURLs.add(htmlImageTags.get(i).getImageUrl());
		}
		
		
		double greatest = 0.0;
		// Base string
		String newImageurl = comicImg.getImageUrl();
		String alttext     = comicImg.getAltText();
		// Flag allows us to check if string was changed
		boolean flag = false;
		// This is checking to see if the image has changed since last time we
		// updated

		if (!imageURLs.contains(comicImg.getImageUrl())) {
			for (int i = 0; i < imageURLs.size(); i++) {
				// Checking the similarity rating of the new strings to our
				// saved string
				
				double temp = Similarity(imageURLs.get(i));
				
				Log.d("Comic.SetNewImageurl", temp + " " + imageURLs.get(i));
				if (temp >= greatest && imageURLs.get(i) != "Unset") {
					// Set our flag to true for below
					flag = true;
					// Most common string is set for update
					newImageurl = imageURLs.get(i);
					alttext     = htmlImageTags.get(i).getAltText();
					// Sets the new bar
					greatest = temp;
				}
			}

			if (flag) {
				// As long as the loop found a new string this is called
				// Otherwise we'd set the string to "Unset" if it hadn't changed
				Log.d("Comic.SetNewImageurl", "New ImageUrl is " + newImageurl);
				comicImg.setImageUrl(newImageurl);
				comicImg.setAltText(alttext);
				this.setUpdated(true);
				retrieveImageBitmap();
			}
		} else {
			// This is set when the imageurl that is saved is still currently on
			// the page, so, it hasn't been updated since
			// System.out.println("Setting updated to false");
			this.setUpdated(false);
		}

	}

	@SuppressWarnings("static-access")
	public void     findImageUrl()
	{
        ComicSiteParser comicSiteParser = new ComicSiteParser(this);
		List<HtmlImageTag> htmlImageTags = comicSiteParser.GetComicHtmlImageTags();
		HtmlImageTag current;
		int height;
		int width;
		float AR;
		int area;
		float rating;
		String max = "unset";
		float maxRating = 0;
		
		for(int i = 0; i < htmlImageTags.size(); i++)
		{
			height = 0; width = 0; AR = 0; area = 0; rating = 0;
			current = htmlImageTags.get(i);
			//Log.d("ImageUrl", current);
			try {
				//Now we get the sizes
				URL oururl = new URL(current.getImageUrl());
		        HttpURLConnection connection = (HttpURLConnection) oururl.openConnection();
		        connection.setDoInput(true);
		        connection.setFollowRedirects(true);
		        connection.connect();
		        InputStream input = connection.getInputStream();
		        Bitmap myBitmap = BitmapFactory.decodeStream(input);
				
				if (myBitmap != null) {
		
					//Find the width and height
					width = myBitmap.getWidth();
					height = myBitmap.getHeight();
					//Print the dimmensions for testing purposes
					//System.out.println(width + " " + height);
					//System.out.println((float)imgHeight/(float)imgWidth);
					
					AR = ((float) height / (float) width);
					area = height * width;
					if (AR > 1) {
						rating = AR * area;
					} else {
						rating = area / AR;
					}
					rating = rating / 1000000;
					if (area > 280000) {
						rating = rating * (float) 1.5;
					}
					if (AR < .2 || AR > 3.25) {
						Log.d("AR deduction" , current.getImageUrl());
						rating = rating - (float)0.25;
					}
					if (max == "unset") {
						max = current.getImageUrl();
					} 
					Log.d("Area", Integer.toString(area));
					Log.d("AR", Float.toString(AR));
					Log.d("Rating", current + " " + rating);
					
					if (rating > maxRating) {
						Log.d("Rating", "Max set to " + current);
						max = current.getImageUrl();
						maxRating = rating;
						comicImg.setImageUrl(max);
						comicImg.setAltText(current.getAltText());
					}
					
				}
				
			} catch (MalformedURLException e) {
				
				System.out.println("findImageUrl "+ current);
				e.printStackTrace();
			} catch (IOException e) {
				
				System.out.println("findImageUrl "+ current);
				e.printStackTrace();
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
