package code.webcomicviewer;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.webkit.WebView;

public class CustomOnTouchListener implements OnTouchListener {
	
	private String Imageurl;
	private int iterator = 0;
	
	public CustomOnTouchListener(String imageurl)
	{
		Imageurl = imageurl;
	}
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		
		WebView.HitTestResult hr = ((WebView)v).getHitTestResult();
		Log.d("CustomOnTouchListener", String.valueOf(iterator));
	    iterator++;
	    
	    
	    
        try{
        	
        	if(hr != null)
        	{
        		if(hr.getType() == WebView.HitTestResult.IMAGE_TYPE)
        		{
        			Log.d("CustomOnTouchListener", hr.getExtra());
        			Imageurl = hr.getExtra();
        		}
        		else
        		{
        			Log.d("CustomOnTouchListener", "Not an IMAGE_TYPE" + hr.getExtra());
        			Imageurl = hr.getExtra();
        			
        		}
        	}
        }
        catch(Exception e)
        {
        	//Log.d("CustomOnTouchListener", e.getMessage());
        	e.printStackTrace();
        }
		return false;
	}
	
	
	
	public String getImageUrl()
	{
		return Imageurl;
	}
	public boolean canAddComic()
	{
		if(Imageurl != "Notset") return true;
		
		return false;
	}

	

}
