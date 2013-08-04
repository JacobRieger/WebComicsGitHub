package dataCode;

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

import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.util.Log;

public class Comic2 {
    
	public static final String TABLE_NAME = "Comic";
	
    public static final String COL_ID     = "_id";
    public static final String COL_NAME   = "name";
    public static final String COL_URL    = "url";
    public static final String COL_SINCE  = "updatedSince";
    public static final String COL_IMAGE  = "image";
    public static final String COL_ALT    = "alt";
    public static final String COL_UPDATE = "updated";
    public static final String COL_BITMAP = "imageBit";
    
    public static final String[] FIELDS = {COL_ID, COL_NAME, COL_URL, COL_SINCE,
    									   COL_IMAGE, COL_ALT, COL_UPDATE, COL_BITMAP};
    public static final String[] NAMES = {COL_NAME};
    
    
    public  long     id;            //ID for the database
	private String   Name;          //Name of the comic
	private String   url;           //Url of the comic
	private String   updatedSince;  //Server variable that may or may not be present
	private Image    comicImg;      //Container for ImageUrl and AltText
	private Bitmap   comicBitmap;   //Bitmap of the image
	private boolean  isUpdated;     //Whether or not the comic has been confirmed still on the site
	//--------------------------------------------------------------------------------//
	
	public static final String CREATE_TABLE = 
			"CREATE TABLE " + TABLE_NAME + "("
            + COL_ID + " INTEGER PRIMARY KEY,"
            + COL_NAME + " TEXT NOT NULL DEFAULT '',"
            + COL_URL + " TEXT NOT NULL DEFAULT '',"
            + COL_SINCE + " TEXT NOT NULL DEFAULT ''," 
            + COL_IMAGE + " TEXT NOT NULL DEFAULT '',"
            + COL_ALT + " TEXT NOT NULL DEFAULT '',"
            + COL_UPDATE + " TEXT NOT NULL DEFAULT '',"
            + COL_BITMAP + " BLOB"
            + ")";
	
	public Comic2()
	{
		id           = -1;
		Name         = "Default";
		url          = "Default";
		updatedSince = "0";
		comicImg     = new Image(Name, Name);
		comicBitmap  = null;
		isUpdated    = false;
	}
	
	public Comic2(int ID, String name, String ImageUrl, String updated, String URL,
			String UpdatedSince, byte[] bitmapbytes, String alttext) {
		
		System.out.println("Classic constructor");
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
		comicImg = new Image(ImageUrl, alttext);
		url = URL;
		
		//comicBitmap = Bitmap.createBitmap(1, 1, Config.ALPHA_8);
		Log.d("Comic Constructor", "About to create Bitmap TESTING");
		
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeByteArray(
				bitmapbytes, 0, bitmapbytes.length, options);
		options.inSampleSize = calculateInSampleSize(options, 720, 1230);
		options.inJustDecodeBounds = false;
		
		updatedSince = String.valueOf(UpdatedSince);
	}
	
	public Comic2(Cursor cursor)
	{
		System.out.println("Cursor constructor");
		this.id   = cursor.getInt(0);
		this.Name = cursor.getString(1);
		this.url  = cursor.getString(2);
		this.updatedSince = cursor.getString(3);
		
		this.comicImg = new Image(cursor.getString(4), cursor.getString(5));
		
		if(cursor.getString(6).equals("True"))
		{
			this.isUpdated = true;
		}
		else
		{
			this.isUpdated = false;
		}
		
		byte[] bytes = cursor.getBlob(7);
		
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeByteArray(
				bytes, 0, bytes.length, options);
		options.inSampleSize = calculateInSampleSize(options, 720, 1230);
		options.inJustDecodeBounds = false;
		
		this.comicBitmap  = BitmapFactory.decodeByteArray(
				bytes, 0, bytes.length, options);
		if(comicBitmap == null) System.out.println("Bitmap is null in cursor constructor");
		
		//System.out.println(comicBitmap.getByteCount());
	}
	
	public ContentValues getContent()
	{
		ContentValues values = new ContentValues();
		System.out.println(id);
		System.out.println(Name);
		System.out.println(url);
		System.out.println(updatedSince);
		System.out.println(comicImg.getAltText());
		System.out.println(comicImg.getImageUrl());
		if(comicBitmap == null) System.out.println("Bitmap is null in getContent");
		
		values.put(COL_NAME, Name);
		values.put(COL_URL, url);
		values.put(COL_SINCE, updatedSince);
		values.put(COL_IMAGE, comicImg.getImageUrl());
		values.put(COL_ALT, comicImg.getAltText());
		values.put(COL_UPDATE, isUpdated);
		
		ByteArrayOutputStream blob = new ByteArrayOutputStream();
		comicBitmap.compress(CompressFormat.PNG, 0 /* ignored for PNG */, blob);
		byte[] bitmapdata = blob.toByteArray();
		
		values.put(COL_BITMAP, bitmapdata);
		
		
		
		return values;
	}
	
	//--------------------------------------------------------------------------------//
	public long      getId()
	{
		return id;
	}
	public void     setId(long newId)
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
		if(comicBitmap == null) System.out.println("ComicBitmap is null");
		else
		{
			ByteArrayOutputStream blob = new ByteArrayOutputStream();
			comicBitmap.compress(CompressFormat.PNG, 0 /* ignored for PNG */, blob);
			byte[] bitmapdata = blob.toByteArray();
			return bitmapdata;
		}
		return null;
	}

	public String   getUpdatedSince()
	{
		return String.valueOf(updatedSince);
	}
	public void     setUpdatedSince(String since)
	{
		updatedSince = since;
	}
	
	public String   getAltText()
	{
		return comicImg.getAltText();
	}
	public void     setAltText(String alt)
	{
		comicImg.setAltText(alt);
	}
	
	//------------------------------------------------------------------------------------
	
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
		setNewImageurl(RetrieveImgs());
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
	
	
	public void     setNewImageurl(List<Image> images) {

		// Checks the list given against the saved Imageurl and sets the one
		// most similar to that former.
		// This is our similarity variable
		// System.out.println("SetNewImageurl called");
		
		List<String> imageURLs = new ArrayList<String>();
		for(int i = 0; i < images.size(); i++)
		{
			imageURLs.add(images.get(i).getImageUrl());
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
					alttext     = images.get(i).getAltText();
					// Sets the new bar
					greatest = temp;
				}
			}

			if (flag) {
				// As long as the loop found a new string this is called
				// Otherwise we'd set the string to "Unset" if it hadn't changed
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
		List<Image> Images = new ArrayList<Image>();
		Images = RetrieveImgs();
		Image current;
		int height;
		int width;
		float AR;
		int area;
		float rating;
		String max = "unset";
		float maxRating = 0;
		
		for(int i = 0; i < Images.size(); i++)
		{
			height = 0; width = 0; AR = 0; area = 0; rating = 0;
			current = Images.get(i);
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
	
	
	public List<Image> RetrieveImgs() {
		// Connects to the url and retrieves all the img urls from the site
		// and returns them in strings
		Log.d("RetrieveImgStrings", "Called");
		try {
			List<Image> Images = new ArrayList<Image>();
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
			
			for (Element e : imgs) {
				
				String attr = e.attr("style");
				//System.out.println(e.attr("alt"))
				
				
				if(attr.indexOf("http://") > 0)
				{
					String img = attr.substring( attr.indexOf("http://"), attr.indexOf(")"));
					String alt = e.attr("title");
					System.out.println(alt);
					
					Images.add(new Image(img, alt));
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
				String alt       = temp.attr("title");
				System.out.println("Src = " + extracted + "alt = " + alt);
				
				if (extracted.startsWith("/") || !extracted.startsWith("http")) {
					// This was necessary for DrMcNinja, as the full url
					// was not posted, I suspect it's true for other sites
					// as well
					if(!extracted.startsWith("/") && !url.endsWith("/"))
					{
						
						extracted = "/".concat(extracted);
					}
					extracted = url.concat(extracted);
				}
				if (!extracted.startsWith("http")) {
					// Saves us from the malformed URL exception
					extracted = "http://".concat(extracted);
				}
				extracted = extracted.replaceAll(" " , "%20");

				String New = extracted;
				//THIS IS UNRELIABLE
				//SERVERS LIE AND MESS IT UP
				//LOOKING AT YOU XKCD
				/*URL testurl = new URL(extracted);
				URLConnection conn = testurl.openConnection();
				int cLength = conn.getContentLength();
				Log.d("Length Checking", New + " " + cLength);
				if (cLength > 6000) {
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
				}*/
				Images.add(new Image(New, alt));
			}

			// Return all the img tagged src strings in the html
			if (Images.size() == 0) {
				System.out.println(this.getName() + " "
						+ "Zero images returned");
			}
			return Images;
		} catch (MalformedURLException e) {
			System.out.println("Malformed URL : " + this.url);
			// e.printStackTrace();
			List<Image> empty = new ArrayList<Image>();
			empty.add(new Image("Unset", "Unset"));
			return empty;
		} catch (ConnectException e) {
			System.out.println("Could not connect to : " + this.url);
			List<Image> empty = new ArrayList<Image>();
			empty.add(new Image("Unset","Unset"));
			return empty;
		} catch (IOException e) {
			System.out.println("General IO Exception Caught");
			e.printStackTrace();
			List<Image> empty = new ArrayList<Image>();
			empty.add(new Image("Unset", "Unset"));
			return empty;
		}
	}
    
}
