package code.webcomicviewer;

import java.io.UnsupportedEncodingException;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class Add_Comic extends Activity implements OnClickListener {

	String ImageUrl;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_add__comic);  
        //Our two buttons to check and add comics
        Button checkComic = (Button) findViewById(R.id.checkComic);
        Button addComic = (Button) findViewById(R.id.addComic);
        //Set our onclicklisteners
        checkComic.setOnClickListener(this);
        addComic.setOnClickListener(this);
        //Add comic should be invisible until the comics been verified
        addComic.setVisibility(View.VISIBLE);
        
        Bundle extras = getIntent().getExtras();
        if(extras != null)
        {
        	//This loads the data when adding from bookmarks
        	EditText cName = (EditText) findViewById(R.id.ComicName);
        	EditText cUrl = (EditText) findViewById(R.id.Comic_Url);
        	//Sets the values from bookmark into the text fields
        	cName.setText(extras.getString("Name"));
        	cUrl.setText(extras.getString("Url"));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_add__comic, menu);
        return true;
    }

	@Override
	public void onClick(View view) {
		EditText cName = (EditText) findViewById(R.id.ComicName);
		EditText cUrl = (EditText) findViewById(R.id.Comic_Url);
		String Name = cName.getText().toString();
		String Url = cUrl.getText().toString();
		
		switch(view.getId()){
		case R.id.checkComic:
			String encodedUrl;
			try {
				encodedUrl = java.net.URLEncoder.encode(Url, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				encodedUrl = "www.google.com";
				e.printStackTrace();
			}
			
			Comic Check = new Comic(Name, encodedUrl, "unset");
			Log.d("CheckComic", Name + " " + Url);
			comicDownloader CD = new comicDownloader(Check, this);
			CD.execute();
			break;
			
		case R.id.addComic:
			Intent intent = new Intent(this, MainActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
			intent.putExtra("Name", Name);
			intent.putExtra("Url", Url);
			intent.putExtra("ImageUrl", ImageUrl);
			startActivity(intent);
			break;
		}
		
	}
	
	public boolean validateURL(String mImageUrl)
	{
		String regex = "^(https?|ftp|file)://.+$";
		Log.d("validateUrl", mImageUrl);
		if(mImageUrl.matches(regex))
		{
			Log.d("validateURL", "URL matches regex");
			return true;
		}
		else return false;
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
			pd.setMessage("Checking Comic");
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
