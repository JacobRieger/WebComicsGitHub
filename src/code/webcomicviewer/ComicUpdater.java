package code.webcomicviewer;

import java.util.ArrayList;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

public class ComicUpdater extends AsyncTask<Void, Void, Void> {

	Context context;
	ProgressDialog pdialog;
	DataBaseHandler db;
	Bitmap ComicBitmap;
	
	public ComicUpdater(Context ourcontext)
	{
		context = ourcontext;
		db = new DataBaseHandler(context);
	}
	
	@Override
	protected void onPreExecute()
	{
		pdialog = new ProgressDialog(context);
		pdialog.setCancelable(false);
		//pdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		pdialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		pdialog.setMax(db.getComicsCount());
		pdialog.setMessage("Updating Comics");
		pdialog.show();
	}
	
	@Override
	protected Void doInBackground(Void... params) {
		System.out.println("UPDATING " + db.getComicsCount() + " COMICS");
		ArrayList<String> ComicNames = db.getAllComicNames();
		for(int i = 0; i < db.getComicsCount(); i++)
		{
			Comic current = db.getComic(ComicNames.get(i));
			if(current.modified())
			{
				current.Update();
				db.updateComic(current);
			}
			else
			{
				
			}
			pdialog.incrementProgressBy(1);
		}
		return null;
	}
	
	@Override
	protected void onPostExecute(Void result)
	{
		pdialog.dismiss();
		NotificationCompat.Builder mBuilder =
		        new NotificationCompat.Builder(context)
		        .setSmallIcon(R.drawable.launcher)
		        .setContentTitle("Updater Started")
		        .setContentText("Updating Comics..");
		
		// Creates an explicit intent for an Activity in your app
		Intent resultIntent = new Intent(context, Front_Page.class);

		// The stack builder object will contain an artificial back stack for the
		// started Activity.
		// This ensures that navigating backward from the Activity leads out of
		// your application to the Home screen.
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
		// Adds the back stack for the Intent (but not the Intent itself)
		stackBuilder.addParentStack(Front_Page.class);
		// Adds the Intent that starts the Activity to the top of the stack
		stackBuilder.addNextIntent(resultIntent);
		PendingIntent resultPendingIntent =
		        stackBuilder.getPendingIntent(
		            0,
		            PendingIntent.FLAG_UPDATE_CURRENT
		        );
		mBuilder.setContentIntent(resultPendingIntent);
		
		NotificationManager mNotificationManager =
			    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
			// mId allows you to update the notification later on.
			mNotificationManager.notify(10, mBuilder.build());
	}

}
