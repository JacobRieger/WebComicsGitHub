package activityCode;

import code.webcomicviewer.BookmarkLoader;
import code.webcomicviewer.CustomOnEditorActionListener;
import code.webcomicviewer.CustomOnTouchListener;
import code.webcomicviewer.R;
import comicCode.Comic;
import dataCode.Bookmark;
import dataCode.BookmarkList;
import dataCode.DataBaseHandler;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import asyncTasks.SingleComicRetriever;

public class Add_Comic_Webview extends Activity implements OnClickListener {

	private WebView        webview;
	private TextView       name;
	private EditText       edittext;
	private BookmarkList   Bookmarks;
	private Button         addComicButton;
	private String         comicName;
	private String         comicImageUrl = "Notset";
	private CustomOnTouchListener cotl;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add__comic__webview);
		
		//final String TAG = this.getClass().getSimpleName();
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); 
		
		webview        = (WebView)  findViewById(R.id.AddComicWebView);
		edittext       = (EditText) findViewById(R.id.AddComicWebEdit);
		addComicButton = (Button) findViewById(R.id.AddComicWebButton);
		name           = (EditText) findViewById(R.id.AddComicWebEditName);
		
		if(name.getText().equals(""))
		{
			DataBaseHandler db = new DataBaseHandler(this);
			comicName = String.valueOf(db.getComicsCount() + 1);
		}
		
		if (Bookmarks == null) {
        	
			Bookmarks = new BookmarkLoader(this).getBookmarks();
		}
		

		webview.setWebViewClient(new VideoWebViewClient());
		//webview.getSettings().setBuiltInZoomControls(true);
		webview.getSettings().setUseWideViewPort(true);
		
		edittext.setText("http://");
		
		//Setting Listeners
		cotl = new CustomOnTouchListener(comicImageUrl);
        webview.setOnTouchListener(cotl);
        
 
        edittext.setOnEditorActionListener(
        		new CustomOnEditorActionListener(webview, edittext));
        addComicButton.setOnClickListener(this);
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add__comic__webview, menu);
  
        //This generates our bookmark submenu items
        MenuItem BookMark = (MenuItem) menu.findItem(R.id.AWVM);
        
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
        	Bookmark current = Bookmarks.find(item.getTitle().toString());
        	
        	//Load the bookmark data into the fields
        	webview.loadUrl (current.getUrl());
        	edittext.setText(current.getUrl());
        	comicName = current.getName();
        	name.setText(current.getName());
        	
    		return true;
    	}
    	
    	return false;
    }
    
    private class VideoWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            //view.loadUrl(url);
        	edittext.setText(url);
        	
            return false;
        }

    }

	@Override
	public void onClick(View v) {
		
		switch(v.getId())
		{
			case R.id.AddComicWebButton:
				if(cotl.canAddComic())
				{
					Comic newComic = new Comic();
					
					String comicname = name.getText().toString();
					if(comicname.equals("")) comicname = comicName;
					newComic.setName        (comicname);
					newComic.setImageUrl    (cotl.getImageUrl());
					newComic.setUrl         (edittext.getText().toString());
					newComic.setUpdated     (false);
					newComic.setUpdatedSince("0");
					
					Log.d("Add Comic WebView", "Comic name is : " + newComic.getName());
					Log.d("Add Comic WebView", "Comic Url is : "  + newComic.getUrl());
					
					SingleComicRetriever scr = new SingleComicRetriever(newComic, this);
					scr.execute();
				}
				else
				{
					Toast toast = Toast.makeText(this, "Incorrect Selection", Toast.LENGTH_SHORT);
					toast.show();
				}
		}
		
	}

}
