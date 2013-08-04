package code.webcomicviewer;

import java.util.ArrayList;
import java.util.Date;

import comicCode.Comic;

import dataCode.DataBaseHandler;
import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.text.format.DateUtils;
import android.text.format.Time;
import android.util.Log;
import android.widget.Toast;

/**
 * 
 * @author Jacob
 *This is an IntentService that handles Updating the comics when it's run
 *Once it's run once, it will schedule itself to run again. 
 */
public class IntentServiceUpdater extends IntentService {
	
	public static final String PARAM_IN_MSG = "imsg";
    public static final String PARAM_OUT_MSG = "omsg";
	
	public IntentServiceUpdater(String name)
	{
		super(name);
		
	}

	public IntentServiceUpdater()
	{
		super("IntentServiceUpdater");
	}

	@Override
	protected void onHandleIntent(Intent intent)
	{
		DataBaseHandler db = new DataBaseHandler(this);
		ArrayList<String> comicNames = db.getAllComicNames();
		
		for(int i = 0; i < db.getComicsCount(); i++)
		{
			Comic current = db.getComic(comicNames.get(i));
			current.Update();
		    db.updateComic(current);
		}
		
		Intent broadcastIntent = new Intent();
		broadcastIntent.setAction(UpdateReceiver.ACTION_RESP);
		broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
		sendBroadcast(broadcastIntent);
		Log.d("IntentServiceUpdater","UPDATE CALLED FOR COMICS IN INTENT");
		setNextUpdate();
		
		Toast notify = Toast.makeText(this, "Update Finished", Toast.LENGTH_LONG);
	    notify.show();
	}
	
	public void setNextUpdate()
	{
		Intent intent = new Intent(this, IntentServiceUpdater.class);
		
	    PendingIntent pendingIntent =
	        PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

	    //TODO The update frequency should be user configurable.  This is not.

	    long currentTimeMillis = System.currentTimeMillis();
	    Log.d("IntentServiceUpdater", "Current Date is : " + new Date(currentTimeMillis).toString());
	    long nextUpdateTimeMillis = currentTimeMillis;
	    Time nextUpdateTime = new Time();
	    
	    nextUpdateTime.set(nextUpdateTimeMillis);
	    
	    nextUpdateTimeMillis = nextUpdateTime.toMillis(true) + DateUtils.HOUR_IN_MILLIS;
	    
	    AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
	    alarmManager.set(AlarmManager.RTC, nextUpdateTimeMillis, pendingIntent);
	    Log.d("IntentServiceUpdater", "Next Update Scheduled at : " + new Date(nextUpdateTimeMillis).toString());
	}
	


	
}
