package code.webcomicviewer;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.util.Log;

public class Comic {
	
	private int id;
	private String Name;
	private String imageUrl;
	private Bitmap comicBitmap;
	private String url;
	private boolean isUpdated;
	private String updatedSince;

	public Comic(String name) {
		Name = name;
		imageUrl = "www.testurl.com";
		isUpdated = true;
	}

	public Comic(String name, String URL, String ImageUrl) {
		Name = name;
		imageUrl = ImageUrl;
		url = URL;
		comicBitmap = Bitmap.createBitmap(1, 1, Config.ALPHA_8);
		isUpdated = true;
	}
	
	public Comic(int ID, String name, String URL, String ImageUrl) {
		id = ID;
		Name = name;
		imageUrl = ImageUrl;
		url = URL;
		comicBitmap = Bitmap.createBitmap(1, 1, Config.ALPHA_8);
		isUpdated = true;
	}
	
	public Comic(int ID, String name, String ImageUrl, String updated, String URL) {
		if(updated == "true")
		{
			isUpdated = true;
		}
		else
		{
			isUpdated = false;
		}
		id = ID;
		Name = name;
		imageUrl = ImageUrl;
		url = URL;
		comicBitmap = Bitmap.createBitmap(1, 1, Config.ALPHA_8);
	}
	
	public Comic(int ID, String name, String ImageUrl, String updated, String URL,
			String UpdatedSince, byte[] bitmapbytes) {
		if(updated == "true")
		{
			isUpdated = true;
		}
		else
		{
			isUpdated = false;
		}
		id = ID;
		Name = name;
		imageUrl = ImageUrl;
		url = URL;
		//comicBitmap = Bitmap.createBitmap(1, 1, Config.ALPHA_8);
		Log.d("BitmapFactory", "About to create Bitmap TESTING");
		
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
		
	}

	public int getId()
	{
		return id;
	}
	public void setId(int newId)
	{
		id = newId;
	}
	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public Bitmap getComicBitmap() {
		return comicBitmap;
	}

	public void setComicBitmap(Bitmap comicBitmap) {
		this.comicBitmap = comicBitmap;
	}
	public void setComicBitmap(byte[] bitmapbytes)
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
		

	public boolean isUpdated() {
		return isUpdated;
	}
	public String getUrl()
	{
		return url;
	}
	public void setUrl(String URL)
	{
		url = URL;
	}

	public void setUpdated(boolean set) {
		isUpdated = set;
	}
	public void setUpdated(String set)
	{
		if(set == "true") isUpdated = true;
		else isUpdated = false;
	}
	
	public byte[] getBitmapBytes() {
		// This is for inputing bitmaps into a bundle to pass between functions
		// Bitmap bitmap = BitmapFactory.decodeFile("/path/images/image.jpg");
		ByteArrayOutputStream blob = new ByteArrayOutputStream();
		comicBitmap.compress(CompressFormat.PNG, 0 /* ignored for PNG */, blob);
		byte[] bitmapdata = blob.toByteArray();
		return bitmapdata;
	}

	public String getUpdatedSince()
	{
		return String.valueOf(updatedSince);
	}
	public void setUpdatedSince(String since)
	{
		updatedSince = since;
	}
	public void retrieveImageBitmap() {
		try {
			// Log.e("src",src);
			// Opens a URL connection
			URL url = new URL(imageUrl);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			
			connection.setDoInput(true);
			connection.connect();
			// Get the inputstream
			InputStream input = connection.getInputStream();
			// We know it's an image url so we decode it into a bitmap
			Bitmap myBitmap = BitmapFactory.decodeStream(input);
			//This is for when the bitmap is too big (aka SMBC)
			//Tailored only for GSIII, 4096 is equal to GL max size, should find way to 
			//get this
			if (myBitmap.getHeight() > 4096 || myBitmap.getWidth() > 4096) {
				System.out.println("Scaling image");
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
			
			Log.e("Bitmap", "returned in retrieveImageBitmap");
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

	public void Update() {
		
		Log.d("Comic", "Updated called in " + this.getName());
		setNewImageurl(RetrieveImgStrings());
		retrieveImageBitmap();
		
	}
	
	public boolean modified()
	{
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
		
		System.out.println(oldDate);
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
		
		if(newDate > oldDate || newDate ==0)
		{
			oldDate = newDate;
			updatedSince = String.valueOf(oldDate);
			return true;
		}
		return false;
	
	}

	public double Similarity(String newString) {
		double count = 0;

		for (int i = 0; i < imageUrl.length(); i++) {
			if (imageUrl.charAt(i) == newString.charAt(i)) {
				count++;
			} else {
				return count;
			}
		}

		return 0;
	}

	public void setNewImageurl(List<String> imageURLs) {

		// Checks the list given against the saved Imageurl and sets the one
		// most similar to that former.
		// This is our similarity variable
		// System.out.println("SetNewImageurl called");
		
		double greatest = 0.0;
		// Base string
		String newImageurl = imageUrl;
		// Flag allows us to check if string was changed
		boolean flag = false;
		// This is checking to see if the image has changed since last time we
		// updated

		if (!imageURLs.contains(imageUrl)) {
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
					// Sets the new bar
					greatest = temp;
				}
			}

			if (flag) {
				// As long as the loop found a new string this is called
				// Otherwise we'd set the string to "Unset" if it hadn't changed
				imageUrl = newImageurl;
				this.setUpdated(true);
			}
		} else {
			// This is set when the imageurl that is saved is still currently on
			// the page, so, it hasn't been updated since
			// System.out.println("Setting updated to false");
			this.setUpdated(false);
		}

	}

	public List<String> RetrieveImgStrings() {
		// Connects to the url and retrieves all the img urls from the site
		// and returns them in strings
		Log.d("RetrieveImgStrings", "Called");
		try {
			List<String> ImgStrings = new ArrayList<String>();
			// Connecting to the site
			// Long timeout given, as was terminating too quickly
			// Not sure about consequences of that
			HttpConnection c = (HttpConnection) Jsoup.connect(url).timeout(1000000);
			c.followRedirects(true);
		
			// Get the html
			Document page = c.get();
			Elements meta = page.select("html head meta");
		    if (meta.attr("http-equiv").contains("REFRESH"))
		    {
		        page = Jsoup.connect(meta.attr("content").split("=")[1]).get();
		        System.out.println(page.baseUri());
		    }
			// System.out.println(page.html());
			// Now we have the html we need to select the elements we want
			Elements imgs = page.select("img");
			
			for (Element e : page.select("img")) {
				String attr = e.attr("style");
				if(attr.indexOf("http://") > 0)
				{
					String img = attr.substring( attr.indexOf("http://"), attr.indexOf(")"));
					ImgStrings.add(img);
				}
			}
			// We are going to iterate through these elements
			Iterator<Element> iterator = imgs.iterator();
			// Extracted string url's from the elements go into ImgStrings

			while (iterator.hasNext()) {
				// Here we iterate through the elements and extract
				// the url from the src of the element
				Element temp = iterator.next();
				//System.out.println(temp.toString());
				String extracted = temp.attr("src");
				//System.out.println(extracted);
				if (extracted.startsWith("/") || !extracted.startsWith("http")) {
					// This was necessary for DrMcNinja, as the full url
					// was not posted, I suspect it's true for other sites
					// as well
					if(!extracted.startsWith("/"))
					{
						extracted = "/".concat(extracted);
					}
					extracted = url.concat(extracted);
				}
				if (!extracted.startsWith("http")) {
					// Saves us from the malformed URL exception
					extracted = "http://".concat(extracted);
				}

				String New = extracted;

				URL testurl = new URL(extracted);
				URLConnection conn = testurl.openConnection();
				int cLength = conn.getContentLength();
				Log.d("Length Checking", New + " " + cLength);
				if (cLength > 10000) {
					// Log.d("Length Checking", New + " " + cLength);
					ImgStrings.add(New);
				}
				else if(cLength == -1)
				{
					ImgStrings.add(New);
				}
				else
				{
					//System.out.println("Rejected " + New + " for " + cLength);
				}
			}

			// Return all the img tagged src strings in the html
			if (ImgStrings.size() == 0) {
				System.out.println(this.getName() + " "
						+ "Zero images returned");
			}
			return ImgStrings;
		} catch (MalformedURLException e) {
			System.out.println("Malformed URL : " + this.url);
			// e.printStackTrace();
			List<String> empty = new ArrayList<String>();
			empty.add("Unset");
			return empty;
		} catch (ConnectException e) {
			System.out.println("Could not connect to : " + this.url);
			List<String> empty = new ArrayList<String>();
			empty.add("Unset");
			return empty;
		} catch (IOException e) {
			System.out.println("General IO Exception Caught");
			e.printStackTrace();
			List<String> empty = new ArrayList<String>();
			empty.add("Unset");
			return empty;
		}
	}
	
	public void findImageUrl()
	{
		List<String> ImageUrls = new ArrayList<String>();
		ImageUrls = RetrieveImgStrings();
		String current;
		int height;
		int width;
		float AR;
		int area;
		float rating;
		String max = "unset";
		float maxRating = 0;
		
		for(int i = 0; i < ImageUrls.size(); i++)
		{
			height = 0; width = 0; AR = 0; area = 0; rating = 0;
			current = ImageUrls.get(i);
			//Log.d("ImageUrl", current);
			try {
				//Now we get the sizes
				URL oururl = new URL(current);
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
						Log.d("AR deduction" , current);
						rating = rating - (float)0.25;
					}
					if (max == "unset") {
						max = current;
					} 
					Log.d("Area", Integer.toString(area));
					Log.d("AR", Float.toString(AR));
					Log.d("Rating", current + " " + rating);
					
					if (rating > maxRating) {
						Log.d("Rating", "Max set to " + current);
						max = current;
						maxRating = rating;
						imageUrl = max;
					}
					
				}
				
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				System.out.println("findImageUrl "+ current);
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
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
